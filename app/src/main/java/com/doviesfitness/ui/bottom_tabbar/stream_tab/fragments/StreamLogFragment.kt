package com.doviesfitness.ui.bottom_tabbar.stream_tab.fragments

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.SystemClock
import android.util.DisplayMetrics
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
import com.doviesfitness.allDialogs.menu.ErrorDownloadViewTypeDialog
import com.doviesfitness.chromecast.browser.VideoItemLoader
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.databinding.FragmentStreamLogBinding
import com.doviesfitness.temp.DownloadVideosUtil
import com.doviesfitness.temp.modal.VideoCategoryModal
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.base.BaseFragment
import com.doviesfitness.ui.bottom_tabbar.home_tab.dialog.SaveEditWorkoutDialog
import com.doviesfitness.ui.bottom_tabbar.stream_tab.activities.StreamLogActivity
import com.doviesfitness.ui.bottom_tabbar.stream_tab.activities.StreamLogHistoryActivity
import com.doviesfitness.ui.bottom_tabbar.stream_tab.adapters.StreamLogAdapter
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.StreamLogModel
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.StreamLogUpdatedModel
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.StreamWorkoutDetailModel
import com.doviesfitness.ui.multipleQuality.IntentUtil
import com.doviesfitness.ui.multipleQuality.StreamVideoPlayUrlActivityTemp
import com.doviesfitness.utils.*
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.util.Assertions
import com.google.android.exoplayer2.util.Util
import com.google.android.gms.cast.MediaInfo
import kotlinx.android.synthetic.main.filter_fitness_dialog_view.*
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class StreamLogFragment : BaseFragment(), View.OnClickListener, StreamLogAdapter.OnNoteClick,
        SaveEditWorkoutDialog.CommentCallBack, LoaderManager.LoaderCallbacks<List<MediaInfo>> {
    private var intpos: Int = 0
    lateinit var binding: FragmentStreamLogBinding
    lateinit var adapter: StreamLogAdapter
    var logList = ArrayList<StreamLogModel.Data>()
    lateinit var dialog: Dialog
    private var width: Int = 0
    private var mLastClickTime: Long = 0
    private var editDeletePosition: Int = 0
    var unitStr = ""
    var offset = 0
    var limit = 10
    var count = 0
    private var page: Int = 1
    private var nextPage: Int = 0
    var logDate = ""
    lateinit var fragmentListener: FragmentListener
    var isDataFiltering = false
    var castLoaderData = ArrayList<MediaInfo>()
    var downloadVideoList: ArrayList<VideoCategoryModal>? = ArrayList<VideoCategoryModal>()

    companion object {
        fun newInstance() = StreamLogFragment()
    }

    var position = 0
    public var download_status: Boolean = false
    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentListener = context as FragmentListener
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = activity!!.window.decorView
        view.systemUiVisibility = view.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_stream_log, container, false)
        initialization()
        return binding.root
    }

    private fun initialization() {
        binding.topBlurView.visibility = View.GONE
        setOnClick(binding.ivBack, binding.ivAdd, binding.containerId, binding.btnCalenderClearFilter)

        binding.ivAdd.visibility = View.GONE
        unitStr = getDataManager().getUserStringData(AppPreferencesHelper.PREF_UNIT_VALUE)!!
        logList.clear()

        var layoutManager = LinearLayoutManager(activity)
        binding.myWorkoutRv.setPadding(0, 0, 0, 0)
        adapter = StreamLogAdapter(activity!!, logList, this, unitStr)
        val spacingInPixels1 = resources.getDimensionPixelSize(R.dimen._10sdp)
        binding.myWorkoutRv.layoutManager = layoutManager
        binding.myWorkoutRv.addItemDecoration(MySpacesItemDecoration(spacingInPixels1))
        binding.myWorkoutRv.adapter = adapter

        val displaymetrics = DisplayMetrics()
        activity!!.windowManager?.defaultDisplay?.getMetrics(displaymetrics)
        width = displaymetrics.widthPixels

        getWorkOutApi(offset, "0", logDate)
        try {
            downloadVideoList!!.clear()
            downloadVideoList =
                    DownloadVideosUtil.getDownloadedVideo(Constants.DOWNLOADED_VIDEO)
            Log.e("StreamLogHisstory-> ", "Workout History" + downloadVideoList!!.size)
        } catch (e: Exception) {
            e.message
        }
        binding.swipeRefresh.setOnRefreshListener({
//            logList.clear()
//            adapter.notifyDataSetChanged()
//            offset=0
//            count=0
////            logDate=""
//            getWorkOutApi(offset, "1",logDate)
            reCallWorkoutApi()
        })


        binding.myWorkoutMain.setOnScrollChangeListener(object :
                NestedScrollView.OnScrollChangeListener {
            override fun onScrollChange(v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
                Log.d("on scroll", "on scroll onLoadMore...")

                if (v!!.getChildAt(v!!.getChildCount() - 1) != null) {
                    if (scrollY >= v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight() && scrollY > oldScrollY) {
                        //code to fetch more data for endless scrolling
                        offset = offset + 10
                        if (offset <= count!!.toInt()) {
                            adapter.showLoading(true)
                            adapter.notifyDataSetChanged()
                            getWorkOutApi(offset, "1", logDate)
                        }
                    }
                }
            }
        })

    }

    override fun onNoteClick(workoutDescription: String) {
        NotesDialog(workoutDescription)
    }

    private fun setOnClick(vararg views: View) {

        for (view in views) {
            view.setOnClickListener(this)
        }
    }

    override fun onClick(view: View?) {

        when (view!!.id) {
            R.id.iv_back -> {
                activity!!.onBackPressed()
            }

            R.id.container_id -> {
                hideNavigationBar()
                val view = activity!!.window.decorView
                view.systemUiVisibility =
                        view.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
            R.id.btn_calender_clear_filter -> {
//                logDate=""
//                hideNavigationBar()
//                page = 1
//                logList.clear()
//                adapter.notifyDataSetChanged()
//                getWorkOutApi(page, "1",logDate)
                reCallWorkoutApi()
            }
            /* R.id.iv_calender -> {
                 var cal = Calendar.getInstance()
                 var dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
                 var str = dateFormat.format(cal.getTime())

                 openDatePickerDialog(str)
 //          */
        }
//        }
    }

    override fun getWorkOutLogData(data: StreamLogModel.Data, whichClick: String, pos: Int) {
        intpos = pos
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return
        } else {
            mLastClickTime = SystemClock.elapsedRealtime()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 30 && resultCode == Activity.RESULT_OK) {
            Log.d("result", "result: -> ")

            if (data != null) {
                val item = data.getSerializableExtra("item") as StreamLogUpdatedModel.Data
                var log = logList.get(editDeletePosition)
                log.customer_calorie = item.customer_calorie
                log.customer_weight = item.customer_weight
                log.feedback_status = item.feedback_status
                log.log_created_date = item.log_created_date
                // log.= item.workout_description
                log.stream_workout_id = item.stream_workout_id
                log.log_id = item.log_id
                log.stream_workout_image = item.stream_workout_image
                log.stream_workout_name = item.stream_workout_name
                log.note = item.note
                log.video_id = item.video_id
                log.stream_workout_time = item.stream_workout_time
                log.workout_log_images = item.workout_log_images
                adapter.notifyItemChanged(editDeletePosition)

            }
        }
    }

    override fun onEditDeleteClick(pos: Int) {
        editDeletePosition = pos
        SaveEditWorkoutDialog.newInstance("log", this).show(childFragmentManager)
    }

    fun loadCastData() {
        Log.d("loader data...", "loader data...init")

        loaderManager.initLoader(0, null, this)

    }

    override fun onplayvideoClick(pos: Int) {
        position = pos
        download_status = false
        //        //-------------New Work Hemant View Type and Check Download Video-----------------------------------

        if (downloadVideoList != null) {
            for (i in 0 until downloadVideoList!!.size) {
                if ((downloadVideoList!!.get(i).stream_workout_id).equals(logList.get(pos).stream_workout_id)) {
                    for (k in 0 until downloadVideoList!!.get(i).download_list!!.size) {
                        if ((downloadVideoList!!.get(i).download_list!!.get(k).stream_video_id).equals(
                                        logList.get(pos).video_id
                                )
                        ) {
                            download_status = true
                            break
                        }
                    }
                }
            }
        }
        if (logList.get(pos).view_type.equals("3")|| logList.get(pos).view_type.equals("2"))
        {
            loadCastData()
        }
        else if ((logList.get(pos).view_type.equals("1")) && (download_status == true)) {
            loadCastData()
        } else {
            ErrorDownloadViewTypeDialog.newInstance("", "", getString(R.string.error_download_type))
                    .show(childFragmentManager)
//            Toast.makeText(context, "Video please download firstly then play", Toast.LENGTH_SHORT).show()
        }

    }

    fun showDeleteDialog(pos: Int) {
        val dialog = context?.let { Dialog(it) }
        var mActivity = activity as StreamLogHistoryActivity

        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
        )
        mActivity.showHideBlurView(true)

        dialog.setContentView(R.layout.dialog_delete)
        val tv_no = dialog.findViewById<TextView>(R.id.tv_no)
        val tv_delete = dialog.findViewById<TextView>(R.id.tv_delete)

        tv_no.setOnClickListener { v ->
            mActivity.showHideBlurView(false)
            dialog?.dismiss()
        }

        tv_delete.setOnClickListener { v ->
            deleteLog(pos)
            mActivity.showHideBlurView(false)
            dialog?.dismiss()
        }

        dialog.setOnDismissListener {
            mActivity.showHideBlurView(false)
        }

        dialog.show()
    }

    fun loadData() {
        logList.clear()
        adapter.notifyDataSetChanged()
        offset = 0
        count = 0
        getWorkOutApi(offset, "0", logDate)
    }

    private fun deleteLog(pos: Int) {
        val param = HashMap<String, String>()

        param.put(StringConstant.device_type, StringConstant.Android)
        param.put(StringConstant.auth_customer_id, getDataManager().getUserInfo().customer_auth_token)
        param.put(StringConstant.logId, "" + logList.get(pos).log_id)

        val header = HashMap<String, String>()
        header.put(StringConstant.AuthToken1, getDataManager().getUserInfo().customer_auth_token)
        getDataManager().deleteStreamLog(param, header)
                ?.getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        try {
                            val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                            val status = json?.get(StringConstant.success)
                            val msg = json?.get(StringConstant.message)
                            if (status!!.equals("1")) {
                                logList.removeAt(pos)
                                adapter.notifyDataSetChanged()
                                if (logList.size == 0 && logList.isEmpty()) {
                                    binding.swipeRefresh.setRefreshing(false)
                                    binding.noWorkoutFound.visibility = View.VISIBLE
                                    binding.progressLayout.visibility = View.GONE
                                }

                            } else {
                                //Constant.showCustomToast(mContext, "" + msg)
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

    private fun getWorkOutApi(offset: Int, fromRefresh: String, date: String) {

        if (CommanUtils.isNetworkAvailable(mContext)!!) {

            if (fromRefresh.equals("1")) {
                binding.progressLayout.visibility = View.GONE
            } else {
                binding.progressLayout.visibility = View.VISIBLE
            }

            val param = HashMap<String, String>()
            //  param.put(StringConstant.page_index, "" + pageCount)
            param.put(StringConstant.limit, "" + limit)
            param.put(StringConstant.offset, "" + offset)
            param.put(StringConstant.date, "" + date)
            param.put(StringConstant.device_type, StringConstant.Android)
//            if(isDataFiltering){
//                fragmentListener.onEventOccur(date)
//                isDataFiltering = false
//            }

            //    param.put(StringConstant.auth_customer_id, getDataManager().getUserInfo().customer_auth_token)

            val header = HashMap<String, String>()
            header.put(StringConstant.purchaseAuthToken, getDataManager().getUserInfo().customer_auth_token)
            header.put(StringConstant.apiKey, "HBDEV")
            header.put(StringConstant.apiVersion, "1")
            if (!date.isEmpty()) {
                binding.btnCalenderClearFilter.visibility = View.VISIBLE
            } else {
                binding.btnCalenderClearFilter.visibility = View.GONE

            }

            getDataManager().getStreamWorkoutLog(param, header)?.getAsJSONObject(object :
                    JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Log.d("response", "response Log...." + response?.toString(4))
                    val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                    val status: String? = jsonObject?.getString(StringConstant.success)

                    if (status!!.equals("1")) {
                        val message: String? = jsonObject?.getString(StringConstant.message)
                        val countString = jsonObject?.getString("count")
                        count = countString!!.toInt()

                        binding.progressLayout.visibility = View.GONE
                        binding.noWorkoutFound.visibility = View.GONE
                        binding.swipeRefresh.setRefreshing(false)
                        val logModel = getDataManager().mGson?.fromJson(response.toString(),
                                StreamLogModel::class.java)
                        logList.addAll(logModel!!.data)
                        adapter.notifyDataSetChanged()

                        hideFooterLoiader()
//                        setViewType()
                    } else if (status!!.equals("401") || status!!.equals("300")) {
                        getDataManager().logout(activity!!)
                    }
                    if (logList.size == 0 && logList.isEmpty()) {
                        binding.swipeRefresh.setRefreshing(false)
                        binding.noWorkoutFound.visibility = View.VISIBLE
                        binding.progressLayout.visibility = View.GONE
                    } else {
                        hideFooterLoiader()
                    }

                }

//                private fun setViewType() {
//                    for (i in 0 until logList.size) {
//                        if (i == 0) {
//                            logList[i].view_type = 1
//                        } else if (i % 2 == 0) {
//                            logList[i].view_type = 1
//                        } else {
//                            logList[i].view_type = 2
//                        }
//                    }
//                }

                override fun onError(anError: ANError) {
                    try {
                        binding.progressLayout.visibility = View.GONE
                        Constant.errorHandle(anError, activity!!)
                        hideFooterLoiader()
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
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

    private fun NotesDialog(description: String) {
        dialog = context?.let { Dialog(it, R.style.MyTheme_Transparent) }!!
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        var mActivity = activity as StreamLogHistoryActivity

        dialog.setOnDismissListener(object : DialogInterface.OnDismissListener {
            override fun onDismiss(dialog: DialogInterface?) {
                mActivity.showHideBlurView(false)

            }
        })

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.log_notes_dialog_layout);
        dialog.window?.setLayout(width - 30, WindowManager.LayoutParams.WRAP_CONTENT)
        val dialog_Heading = dialog.findViewById(R.id.txt_dialog_heading) as TextView
        val dialog_overViewDiscription =
                dialog.findViewById(R.id.txt_overView_discritpion) as TextView
        dialog_Heading.text = "Notes"
        dialog_overViewDiscription.text = description
        dialog_overViewDiscription.visibility = View.VISIBLE
        mActivity.showHideBlurView(true)

        dialog.show()
        dialog.iv_cancle_dialog.setOnClickListener {
            mActivity.showHideBlurView(false)

            dialog.dismiss()
        }
    }

    class MySpacesItemDecoration(space: Int) : RecyclerView.ItemDecoration() {
        private val space: Int

        init {
            this.space = space
        }

        override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
            val position = parent.getChildAdapterPosition(view)
            if (position == parent.getAdapter()!!.getItemCount() - 1) {
                //   outRect.bottom = space * 6
                outRect.bottom = 0

            } else {
                outRect.bottom = 0
            }
        }
    }

    override fun textOnClick1(type: String) {
        //edit
        var streamLog = logList.get(editDeletePosition)
        startActivityForResult(
                Intent(getActivity(), StreamLogActivity::class.java)
                        .putExtra("from", "edit")
                        .putExtra("streamLog", streamLog), 30
        )
    }

    override fun overwriteClick(type: String) {
//delete
        showDeleteDialog(editDeletePosition)
    }


    public fun eventTrigger(data: String) {
        logDate = data.toString()
        isDataFiltering = true
        Log.d("nfkasnfkasnfka", data.toString())
        hideNavigationBar()
        page = 0
        offset = 0
        logList.clear()
        adapter.notifyDataSetChanged()
        getWorkOutApi(page, "1", logDate)

    }

    private fun reCallWorkoutApi() {
        logList.clear()
        adapter.notifyDataSetChanged()
        offset = 0
        count = 0
        logDate = ""
        getWorkOutApi(offset, "1", logDate)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<MediaInfo>> {
        Log.d("loader data...", "loader data...create")

        return VideoItemLoader(getActivity(), logList.get(position), logList.get(position).video_image)
    }

    override fun onLoadFinished(loader: Loader<List<MediaInfo>>, data: List<MediaInfo>?) {
        castLoaderData.clear()
        data?.let { castLoaderData.addAll(it) }
        Log.d("loader data...", "loader data arraylist..." + castLoaderData?.size + "....data..." + castLoaderData.toString())
        loaderManager.destroyLoader(0)
        playVideo(position)

    }

    override fun onLoaderReset(loader: Loader<List<MediaInfo>>) {
        Log.d("loader data...", "loader data...reset")

    }

    fun playVideo(pos: Int) {

        var hlsList = ArrayList<StreamLogModel.Data.HlsVideo>()
        var exerciseList = java.util.ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>()
        var MaxProgress = Constant.getExerciseTime(logList.get(pos).video_duration)


        var data = StreamWorkoutDetailModel.Settings.Data.Video(
                logList.get(pos).stream_video,
                logList.get(pos).video_description,
                logList.get(pos).video_id,
                logList.get(pos).video_duration,
                logList.get(pos).stream_workout_image,
//            logList.get(pos).stream_workout_image_url,
                "",
                logList.get(pos).video_title,
                logList.get(pos).video_subtitle,
                0, 0,

                MaxProgress,
                0,
                false,
                false,
                false,
                hls_video =getHlsVideo(pos),
                mp4_video = getMp4Video(pos),
//            is_workout =      logList.get(pos).can_create_log
                is_workout = "",
                view_type = logList.get(pos).view_type

        )
        exerciseList.add(data)


        hlsList.add(logList.get(pos).hls_video)
        SampleListLoader(
                "image",
                activity!!,
                exerciseList,
                logList.get(pos).stream_workout_id,
                pos,
                hlsList, logList.get(pos).log_id, castLoaderData
        ).execute("")

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

    private class SampleListLoader(
            val streamImage: String,
            val context: Context,
            val strList: ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>,
            val workoutId: String,
            val pos: Int,
            val videoList: List<StreamLogModel.Data.HlsVideo>?, val historyId: String,
            val castLoaderData: ArrayList<MediaInfo>) : AsyncTask<String?, Void?, PlaylistHolder?>() {

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
            intent.putExtra("workout_id", "" + workoutId)
            intent.putExtra("local", "no")
            intent.putExtra("trailer", "no")
            intent.putExtra("position", 0)
            intent.putExtra("from", "history")
            intent.putExtra("castMedia", castLoaderData)
            intent.putExtra("history_id", "" + historyId)
            intent.putExtra("name", "" + strList.get(0).stream_video_name)
//        intent.putExtra("stream_image", strList.get(0).stream_video_image_url+"medium/"+strList.get(0).stream_video_image)
            intent.putExtra("stream_image", strList.get(0).stream_video_image_url + strList.get(0).stream_video_image)
//        intent.putExtra("stream_image", "https://d1n9vl26vbyc5s.cloudfront.net/stream_workout_image/medium/"+strList.get(0).stream_video_image)
            IntentUtil.addToIntent(result!!.mediaItems, intent)
            context as BaseActivity
            context.getActivity().startActivityForResult(intent, 50)
        }

        @Throws(IOException::class)
        private fun readEntry(videoList: List<StreamLogModel.Data.HlsVideo?>?): PlaylistHolder {
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

    private fun getHlsVideo(pos: Int):StreamWorkoutDetailModel.Settings.Data.Video.HlsVideo{
        val stream_quality = logList[pos].hls_video.stream_quality
        val vHlsMasterPlaylist = logList[pos].hls_video.vHlsMasterPlaylist
        val vHls2K = logList[pos].hls_video.vHls2K
        val vHls1080p = logList[pos].hls_video.vHls1080p
        val vHls720p = logList[pos].hls_video.vHls720p
        val vHls480p = logList[pos].hls_video.vHls480p
        val vHls360p = logList[pos].hls_video.vHls360p
        return StreamWorkoutDetailModel.Settings.Data.Video.HlsVideo(stream_quality,vHlsMasterPlaylist,vHls2K,vHls1080p,vHls720p,vHls480p,vHls360p)
    }
    private fun getMp4Video(pos: Int):StreamWorkoutDetailModel.Settings.Data.Video.Mp4Video{

        val downaload_quality = logList[pos].hls_video.stream_quality

        val vMpeg2K = logList[pos].mp4_video.vMpeg2K
        val vMpeg1080p = logList[pos].mp4_video.vMpeg1080p
        val vMpeg720p = logList[pos].mp4_video.vMpeg720p
        val vMpeg480p = logList[pos].mp4_video.vMpeg480p
        val vMpeg360p = logList[pos].mp4_video.vMpeg360p
        return StreamWorkoutDetailModel.Settings.Data.Video.Mp4Video(downaload_quality,vMpeg2K,vMpeg1080p,vMpeg720p,vMpeg480p,vMpeg360p)
    }
}

