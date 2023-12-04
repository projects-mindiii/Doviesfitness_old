package com.doviesfitness.ui.bottom_tabbar.stream_tab.fragments

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.doviesfitness.R
import com.doviesfitness.allDialogs.ErrorDialog
import com.doviesfitness.allDialogs.menu.ErrorDownloadViewTypeDialog
import com.doviesfitness.chromecast.browser.VideoItemLoader
import com.doviesfitness.databinding.FragmentMyNewWorkoutlogBinding
import com.doviesfitness.temp.DownloadVideosUtil
import com.doviesfitness.temp.modal.VideoCategoryModal
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.base.BaseFragment
import com.doviesfitness.ui.base.EndlessRecyclerViewScrollListener
import com.doviesfitness.ui.bottom_tabbar.home_tab.dialog.SaveEditWorkoutDialog
import com.doviesfitness.ui.bottom_tabbar.stream_tab.activities.StreamDetailActivity
import com.doviesfitness.ui.bottom_tabbar.stream_tab.activities.StreamLogActivity
import com.doviesfitness.ui.bottom_tabbar.stream_tab.activities.StreamLogHistoryActivity
import com.doviesfitness.ui.bottom_tabbar.stream_tab.adapters.StreamHistoryAdapter
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.StreamLogUpdatedModel
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.StreamPlayedHistoryModel
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.StreamWorkoutDetailModel
import com.doviesfitness.ui.multipleQuality.IntentUtil
import com.doviesfitness.ui.multipleQuality.StreamVideoPlayUrlActivityTemp
import com.doviesfitness.utils.CommanUtils
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.Constants
import com.doviesfitness.utils.StringConstant
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.util.Assertions
import com.google.android.exoplayer2.util.Util
import com.google.android.gms.cast.MediaInfo
import eightbitlab.com.blurview.RenderScriptBlur
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class StreamHistoryFragment : BaseFragment(), StreamHistoryAdapter.OnViewClick,
    SaveEditWorkoutDialog.CommentCallBack, LoaderManager.LoaderCallbacks<List<MediaInfo>>
{
    lateinit var binding: FragmentMyNewWorkoutlogBinding
    lateinit var adapter: StreamHistoryAdapter
    var workoutId = ""
    var historyList = ArrayList<StreamPlayedHistoryModel.Data>()
    lateinit var mLayoutManager: LinearLayoutManager
    var offset=0
    var limit=10
    var count=0
    var position=0
    var editDeletePosition=0
    var  castLoaderData= ArrayList<MediaInfo>()
    var downloadVideoList: ArrayList<VideoCategoryModal>? = ArrayList<VideoCategoryModal>()
    public var download_status: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_new_workoutlog, container, false)
        initialization()
        return binding.root
    }

    private fun initialization() {
        binding.topBlurView.visibility=View.GONE
        binding.myWorkoutRv.setPadding(0,0,0,0)
        mLayoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        adapter = StreamHistoryAdapter(mContext, historyList,this)
        binding.myWorkoutRv.layoutManager = mLayoutManager
        binding.myWorkoutRv.adapter = adapter
        try {
            downloadVideoList!!.clear()
            downloadVideoList =
                    DownloadVideosUtil.getDownloadedVideo(Constants.DOWNLOADED_VIDEO)
            Log.e("StreamLogHisstory-> ", "Workout History" + downloadVideoList!!.size)
        } catch (e: Exception) {
            e.message
        }

        getStreamHistory(offset,"1")
        binding.swipeRefresh.setOnRefreshListener(androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener {
            historyList.clear()
            adapter.notifyDataSetChanged()
            offset=0
            count=0
            getStreamHistory( offset,"1")
        })

        binding.myWorkoutMain.setOnScrollChangeListener(object :
            NestedScrollView.OnScrollChangeListener {
            override fun onScrollChange(v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
                Log.d("on scroll","on scroll onLoadMore...")

                if (v!!.getChildAt(v!!.getChildCount() - 1) != null) {
                    if (scrollY >= v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight() && scrollY > oldScrollY) {
                        //code to fetch more data for endless scrolling
                        offset = offset + 10
                        if (offset <= count!!.toInt()) {
                            adapter.showLoading(true)
                            adapter.notifyDataSetChanged()
                            getStreamHistory( offset,"1")
                        }
                    }
                }
            }
        })


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun getStreamHistory (offset:Int,fromRefresh: String) {

        if (CommanUtils.isNetworkAvailable(mContext)!!) {

            if (fromRefresh.equals("1")) {
                binding.loader.visibility = View.GONE
            } else {
                binding.loader.visibility = View.VISIBLE
            }

            val param = HashMap<String, String>()
            param.put(StringConstant.limit, ""+limit )
            param.put(StringConstant.offset, ""+offset )
            param.put(StringConstant.device_type, StringConstant.Android)

            val header = HashMap<String, String>()
            header.put(StringConstant.purchaseAuthToken, getDataManager().getUserInfo().customer_auth_token)
            header.put(StringConstant.apiKey, "HBDEV")
            header.put(StringConstant.apiVersion, "1")

            getDataManager().getStreamHistory(param, header)?.getAsJSONObject(object :
                JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    binding.swipeRefresh.setRefreshing(false)

                    Log.d("response","response...."+response?.toString(4))
                    val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                    val status: String? = jsonObject?.getString(StringConstant.success)

                      if (status!!.equals("1")) {
                          val countString: String? = jsonObject?.getString("count")
                          count= countString?.toInt()!!
                        binding.loader.visibility = View.GONE
                        binding.noWorkoutFound.visibility = View.GONE
                        val historyModel = getDataManager().mGson?.fromJson(response.toString(), StreamPlayedHistoryModel::class.java)
                        historyList.addAll(historyModel!!.data)

                        hideFooterLoiader()
//                          set_ViewType()
                    }
                      else if (status!!.equals("401") || status!!.equals("300")) {
                          getDataManager().logout(activity!!)
                      }

                    if (historyList.size == 0 && historyList.isEmpty()) {
                        binding.noWorkoutFound.visibility = View.VISIBLE
                        binding.loader.visibility = View.GONE
                    }
                    else{
                        hideFooterLoiader()
                    }

                }

//                private fun set_ViewType() {
//                    for (i in 0 until historyList.size) {
//                        if (i == 0) {
//                            historyList[i].view_type = 0
//                        } else if (i % 2 == 0) {
//                            historyList[i].view_type = 1
//                        } else {
//                            historyList[i].view_type = 2
//                        }
//                    }
//                }

                override fun onError(anError: ANError) {
                    binding.swipeRefresh.setRefreshing(false)
                    hideFooterLoiader()
                    Constant.errorHandle(anError, activity!!)
                    binding.loader.visibility = View.GONE
                }
            })

        } else {
            binding.swipeRefresh.setRefreshing(false)
            Constant.showInternetConnectionDialog(mContext as Activity)
        }

    }
    private fun hideFooterLoiader() {
        adapter.showLoading(false)
        adapter.notifyDataSetChanged()
    }
    override fun onEditDeleteClick(pos: Int) {
        editDeletePosition=pos
        var canCreateLog=""
        if(historyList.get(pos).can_create_log.equals("yes",true)){
            canCreateLog="history create"
        }
        else{
            canCreateLog="history"

        }
        SaveEditWorkoutDialog.newInstance(canCreateLog, this).show(childFragmentManager)
    }

    override fun onCellClick(pos: Int) {
        position=pos
        download_status = false
        //        //-------------New Work Hemant View Type and Check Download Video-----------------------------------

        if (downloadVideoList != null) {
            for (i in 0 until downloadVideoList!!.size) {
                if ((downloadVideoList!!.get(i).stream_workout_id).equals(historyList.get(pos).stream_workout_id)) {
                    for (k in 0 until downloadVideoList!!.get(i).download_list!!.size) {
                        if ((downloadVideoList!!.get(i).download_list!!.get(k).stream_video_id).equals(
                                        historyList.get(pos).video_id
                                )
                        ) {
                            download_status = true
                            break
                        }
                    }
                }
            }
        }
        if ((historyList.get(pos).view_type.equals("0")) || (historyList.get(pos).view_type.equals("2"))) {
            loadCastData()
        }else if ((historyList.get(pos).view_type.equals("1")) && (download_status == true)) {
            loadCastData()
        } else {
            ErrorDownloadViewTypeDialog.newInstance("", "",getString(R.string.error_download_type))
                    .show(childFragmentManager)
//            Toast.makeText(context, "Video please download firstly then play", Toast.LENGTH_SHORT).show()
        }


//        loadCastData()

    }

    override fun textOnClick1(type: String) {
        //edit
        var streamLog= historyList.get(editDeletePosition)
        startActivityForResult(
            Intent(getActivity(), StreamLogActivity::class.java)
                .putExtra("from", "history")
                .putExtra("streamHistory", streamLog),410
        )
    }

    override fun overwriteClick(type: String) {
//delete
        showDeleteDialog(editDeletePosition)

    }
    fun showDeleteDialog(pos: Int) {
        val dialog = context?.let { Dialog(it) }
        var mActivity=   activity as StreamLogHistoryActivity

        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )

        mActivity.showHideBlurView(true)
        dialog?.setContentView(R.layout.dialog_delete)
        val tv_no = dialog?.findViewById<TextView>(R.id.tv_no)
        val tvHeader = dialog?.findViewById<TextView>(R.id.tv_header)
        val tv_delete = dialog?.findViewById<TextView>(R.id.tv_delete)
        tvHeader.setText(getString(R.string.are_you_sure_you_want_to_delete_this_workout_history))
        tv_no.setOnClickListener { v ->

            mActivity.showHideBlurView(false)

            dialog?.dismiss() }

        tv_delete.setOnClickListener { v ->
            deleteHistory(pos)
            dialog?.dismiss()
        }
        dialog?.setOnDismissListener {

            mActivity.showHideBlurView(false)

        }


        dialog?.show()
    }

    private fun deleteHistory(pos: Int) {
        val param = HashMap<String, String>()

        param.put(StringConstant.auth_customer_id, getDataManager().getUserInfo().customer_auth_token)
        param.put(StringConstant.historyId, ""+historyList.get(pos).history_id)

        val header = HashMap<String, String>()
        header.put(StringConstant.AuthToken1, getDataManager().getUserInfo().customer_auth_token)
        getDataManager().deleteStreamLogHistory(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    try {
                        val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                        val status = json?.get(StringConstant.success)
                        val msg = json?.get(StringConstant.message)
                        if (status!!.equals("1")) {
                            historyList.removeAt(pos)
                            adapter.notifyDataSetChanged()
                            if (historyList.size == 0 && historyList.isEmpty()) {
                                binding.swipeRefresh.setRefreshing(false)
                                binding.noWorkoutFound.visibility = View.VISIBLE
                                binding.progressLayout.visibility = View.GONE
                            }

                        } else {

                        }
                    } catch (ex: Exception) {
                        Constant.showCustomToast(context!!, getString(R.string.something_wrong))
                    }
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, getActivity() as Activity)
                }
            })
    }
/////

    fun playVideo(pos:Int){
        var hlsList = ArrayList<StreamPlayedHistoryModel.Data.HlsVideo>()
        var exerciseList= java.util.ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>()
        var MaxProgress = Constant.getExerciseTime(historyList.get(pos).video_duration)


                var data = StreamWorkoutDetailModel.Settings.Data.Video(
                    historyList.get(pos).stream_video,
                    historyList.get(pos).video_description,
                    historyList.get(pos).video_id,
                    historyList.get(pos).video_duration,
                    historyList.get(pos).stream_workout_image,
                    historyList.get(pos).stream_workout_image_url,
                    historyList.get(pos).video_title,
                    historyList.get(pos).video_subtitle,
                    0,0,

                    MaxProgress,
                    0,
                    false,
                    false,
                    false,
                    hls_video = getHlsVideo(pos),
                    mp4_video = getMp4Video(pos),is_workout =      historyList.get(pos).can_create_log,
                        view_type = historyList.get(pos).view_type
                )
                exerciseList.add(data)


        hlsList.add(historyList.get(pos).hls_video)
        SampleListLoader(
            "image",
            activity!!,
            exerciseList,
            historyList.get(pos).stream_workout_id,
            pos,
            hlsList, historyList.get(pos).history_id,castLoaderData
        ).execute("")

    }

private class SampleListLoader(
    val streamImage: String,
    val context: Context,
    val strList: ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>,
    val workoutId: String,
    val pos: Int,
    val videoList: List<StreamPlayedHistoryModel.Data.HlsVideo?>?,val historyId:String,
   val castLoaderData:ArrayList<MediaInfo>) : AsyncTask<String?, Void?, PlaylistHolder?>() {

    private var sawError = false

    override fun onPreExecute() {
        super.onPreExecute()
        Log.d("mvmxnvmnxmv", "onPreExecute: ")
    }

    private val preferExtensionDecodersMenuItem: MenuItem? = null
    override fun doInBackground(vararg uris: String?): PlaylistHolder? {
        Log.d("mvmxnvmnxmv", "doInBackground: -> ")
        var result: PlaylistHolder? = null
        try {
            result = readEntry(videoList)
        } catch (e: Exception) {
            Log.d("mvmxnvmnxmv", "doInBackground: EXCEPTION -> ${e.message}")

            sawError = true
        } finally {
        }
        return result
    }

    override fun onPostExecute(result: PlaylistHolder?) {
        Log.d("mvmxnvmnxmv", "onPostExecute: -> ")
        val intent = Intent(context, StreamVideoPlayUrlActivityTemp::class.java)
        intent.putExtra(
            IntentUtil.PREFER_EXTENSION_DECODERS_EXTRA,
            isNonNullAndChecked(preferExtensionDecodersMenuItem)
        )
        intent.putExtra("videoList", strList)
        intent.putExtra("workout_id", ""+workoutId)
        intent.putExtra("local", "no")
        intent.putExtra("trailer", "no")
        intent.putExtra("position", 0)
        intent.putExtra("from", "history")
        intent.putExtra("castMedia", castLoaderData)
        intent.putExtra("history_id", ""+historyId)
        intent.putExtra("name", ""+strList.get(0).stream_video_name )
//        intent.putExtra("stream_image", strList.get(0).stream_video_image_url+"medium/"+strList.get(0).stream_video_image)
        intent.putExtra("stream_image", strList.get(0).stream_video_image_url+strList.get(0).stream_video_image)
//        intent.putExtra("stream_image", "https://d1n9vl26vbyc5s.cloudfront.net/stream_workout_image/medium/"+strList.get(0).stream_video_image)
        IntentUtil.addToIntent(result!!.mediaItems, intent)
        context as BaseActivity
        context.getActivity().startActivityForResult(intent,50)
    }

    @Throws(IOException::class)
    private fun readEntry(videoList: List<StreamPlayedHistoryModel.Data.HlsVideo?>?): PlaylistHolder {
        val mediaList = arrayListOf<MediaItem>()
        for (i in 0..videoList?.size!! - 1) {
            var uri: Uri? = null
            var extension: String? = null
            var title: String? = null
            val mediaItem = MediaItem.Builder()
            title = "Dovies video"
            uri = Uri.parse(videoList.get(i)?.vHlsMasterPlaylist)
            extension = "m3u8"

            val adaptiveMimeType =
                Util.getAdaptiveMimeTypeForContentType(
                    Util.inferContentType(
                        uri,
                        extension
                    )
                )
            mediaItem
                .setUri(uri)
                .setVideoUrl("setVideoUrl")
                .setMediaMetadata(
                    MediaMetadata.Builder().setTitle(title).build()
                )
                .setMimeType(adaptiveMimeType)

            mediaList.add(mediaItem.build())
        }
        return PlaylistHolder("Dovies", mediaList)
    }

    private fun isNonNullAndChecked(menuItem: MenuItem?): Boolean {
        return menuItem != null && menuItem.isChecked
    }
}
    private class PlaylistHolder(title: String, mediaItems: List<MediaItem>) {
        val title: String
        val mediaItems: List<MediaItem>

        init {
            Assertions.checkArgument(!mediaItems.isEmpty())
            this.title = title
            this.mediaItems = Collections.unmodifiableList(java.util.ArrayList(mediaItems))
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("result", "result: -> requestCode..."+requestCode+"...resultCode..."+resultCode)

        if (requestCode == 410 && resultCode==Activity.RESULT_OK) {
         var mActivity=   activity as StreamLogHistoryActivity
            mActivity.notifyData()
        }
    }
////chrome cast data
fun loadCastData(){
    Log.d("loader data...","loader data...init")

    loaderManager.initLoader(0, null, this)

}

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<MediaInfo>> {
        Log.d("loader data...","loader data...create")

        return VideoItemLoader(getActivity(),   historyList.get(position),historyList.get(position).video_image)
    }

    override fun onLoadFinished(loader: Loader<List<MediaInfo>>, data: List<MediaInfo>?) {
        castLoaderData.clear()
        data?.let { castLoaderData.addAll(it) }
        Log.d("loader data...","loader data arraylist..."+castLoaderData?.size+"....data..."+castLoaderData.toString())
        loaderManager.destroyLoader(0)
       playVideo(position)

    }

    override fun onLoaderReset(loader: Loader<List<MediaInfo>>) {
        Log.d("loader data...","loader data...reset")

    }
    private fun getHlsVideo(pos: Int):StreamWorkoutDetailModel.Settings.Data.Video.HlsVideo{
        val stream_quality = historyList[pos].hls_video.stream_quality
        val vHlsMasterPlaylist = historyList[pos].hls_video.vHlsMasterPlaylist
        val vHls2K = historyList[pos].hls_video.vHls2K
        val vHls1080p = historyList[pos].hls_video.vHls1080p
        val vHls720p = historyList[pos].hls_video.vHls720p
        val vHls480p = historyList[pos].hls_video.vHls480p
        val vHls360p = historyList[pos].hls_video.vHls360p
        return StreamWorkoutDetailModel.Settings.Data.Video.HlsVideo(stream_quality,vHlsMasterPlaylist,vHls2K,vHls1080p,vHls720p,vHls480p,vHls360p)
    }
    private fun getMp4Video(pos: Int):StreamWorkoutDetailModel.Settings.Data.Video.Mp4Video{
        val downaload_quality = historyList[pos].hls_video.stream_quality
        val vMpeg2K = historyList[pos].mp4_video.vMpeg2K
        val vMpeg1080p = historyList[pos].mp4_video.vMpeg1080p
        val vMpeg720p = historyList[pos].mp4_video.vMpeg720p
        val vMpeg480p = historyList[pos].mp4_video.vMpeg480p
        val vMpeg360p = historyList[pos].mp4_video.vMpeg360p
        return StreamWorkoutDetailModel.Settings.Data.Video.Mp4Video(downaload_quality,vMpeg2K,vMpeg1080p,vMpeg720p,vMpeg480p,vMpeg360p)
    }
}
