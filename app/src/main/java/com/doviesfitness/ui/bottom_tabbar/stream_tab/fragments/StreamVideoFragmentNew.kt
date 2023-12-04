package com.doviesfitness.ui.bottom_tabbar.stream_tab.fragments

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.doviesfitness.BuildConfig
import com.doviesfitness.Doviesfitness
import com.doviesfitness.R
import com.doviesfitness.allDialogs.download_video_in_formate.DownLoadModal
import com.doviesfitness.allDialogs.download_video_in_formate.DownloadVideoInpxlFragment
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.databinding.FragmentStreamVideoBinding
import com.doviesfitness.subscription.IsSubscribed
import com.doviesfitness.subscription.SubscriptionActivity
import com.doviesfitness.ui.base.BaseFragment
import com.doviesfitness.ui.bottom_tabbar.HomeTabActivity
import com.doviesfitness.ui.bottom_tabbar.stream_tab.activities.DownloadsStreamActivity
import com.doviesfitness.ui.bottom_tabbar.stream_tab.activities.StreamDetailActivity
import com.doviesfitness.ui.bottom_tabbar.stream_tab.activities.StreamDetailActivity.Companion.overViewTrailerData
import com.doviesfitness.ui.bottom_tabbar.stream_tab.activities.StreamDetailActivity.Companion.videoList
import com.doviesfitness.ui.bottom_tabbar.stream_tab.activities.StreamVideoPlayUrlActivity
import com.doviesfitness.ui.bottom_tabbar.stream_tab.adapters.StreamVideoAdapter
import com.doviesfitness.ui.bottom_tabbar.stream_tab.download_background.DownloadUtil
import com.doviesfitness.ui.bottom_tabbar.stream_tab.download_background.DownloadedModal
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.StreamWorkoutDetailModel
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.VideoListItem
import com.doviesfitness.ui.multipleQuality.IntentUtil
import com.doviesfitness.ui.multipleQuality.StreamPlayerActivity
import com.doviesfitness.ui.multipleQuality.StreamVideoPlayUrlActivityTemp
import com.doviesfitness.ui.room_db.MyVideoList
import com.doviesfitness.utils.Constant
import com.facebook.FacebookSdk.getApplicationContext
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.util.Assertions
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.popup_content.view.*
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class StreamVideoFragmentNew : BaseFragment(), StreamVideoAdapter.OnVideoClick,
    StreamDetailActivity.UpdateListInterface, IsSubscribed,
    DownloadVideoInpxlFragment.DownloadDialogEventListener {
    private var downloadUrlInAccordingToPixel: String =""
    private var workOutListPosition: Int = 0
    private lateinit var videModalListInPixel: StreamWorkoutDetailModel.Settings.Data.Video
    private val preferExtensionDecodersMenuItem: MenuItem? = null
    private lateinit var pixel_list: MutableList<DownLoadModal>

    override fun downloadVideo(
        pos: Int,
        videoModal: StreamWorkoutDetailModel.Settings.Data.Video,
        view: ImageView,
        loader: ProgressBar,
        downloadingTxt: StreamVideoAdapter.MyViewHolder,
        status: String
    ) {
        if (strList != null) {
            strList.clear()
        }
        for (i in 0..videoList.size - 1) {
            var vModel = videoList.get(i)
            var MaxProgress =
                Constant.getExerciseTime(vModel.video_duration)

            val tempListModal = StreamWorkoutDetailModel.Settings.Data.Video(
                stream_video = vModel.stream_video,
                stream_video_description = vModel.stream_video_description,
                stream_video_id = vModel.stream_video_id,
                video_duration = vModel.video_duration,
                stream_video_image = vModel.stream_video_image,
                stream_video_image_url = vModel.stream_video_image_url,
                stream_video_name = vModel.stream_video_name,
                stream_video_subtitle = vModel.stream_video_subtitle,
                order = vModel.order,
                Progress = vModel.Progress,
                MaxProgress = MaxProgress,
                seekTo = vModel.seekTo,
                isPlaying = vModel.isPlaying,
                isComplete = vModel.isComplete,
                isRestTime = vModel.isRestTime,
                downLoadUrl = "",
                hls_video = null,
                mp4_video = null,is_workout = vModel.is_workout,view_type =vModel.view_type
            )
            strList.add(tempListModal)
            if (DownloadUtil.getDownloadedData("downloaded") != null) {
                var downloadedList = DownloadUtil.getDownloadedData("downloaded")
                if (downloadedList.size > 0) {
                    for (i in 0..downloadedList.size - 1) {
                        if (downloadedList.get(i).stream_workout_id.equals(overViewTrailerData!!.stream_workout_id)) {
                            if (downloadedList.get(i).download_list != null && downloadedList.get(i).download_list.size > 0) {

                                for (j in 0..downloadedList.get(i).download_list.size - 1) {

                                    for (k in 0..strList.size - 1) {
                                        if (downloadedList.get(i).download_list.get(j).stream_video_id.equals(
                                                strList.get(k).stream_video_id
                                            )
                                        ) {
                                            strList.get(k).downLoadUrl =
                                                downloadedList.get(i).download_list.get(j)
                                                    .downLoadUrl
                                            break
                                        }
                                    }
                                }
                            }
                            break
                        }
                    }
                }
            }
        }

        workoutid = overViewTrailerData!!.stream_workout_id

        if (strList != null && strList.size > 0) {
                if (StreamDetailActivity.qualitymodel?.settings?.data?.videoList!!.size > 0)
                    loadSample(strList, workoutid, pos, StreamDetailActivity.qualitymodel?.settings?.data?.videoList)

        }
    }

    private var timeStempUrl: String = ""
    private var pathget: String = ""
    private var workoutid: String = ""
    lateinit var binding: FragmentStreamVideoBinding
    lateinit var adapter: StreamVideoAdapter
    var videoUrlList = arrayListOf<String>()
    private var dirPath: String? = null
    private val REQUEST_WRITE_PERMISSION_CODE = 1
    private var strList = ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>()

    //private var videoList = ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>()
    var listFromLb = ArrayList<MyVideoList>()
    var workoutId = ""
    var itemPos = -1
    var downloadingPos = -1
    var downloadingProgress = 0
    lateinit var mLayoutManager: LinearLayoutManager

    private var forUpdate: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_stream_video, container, false)
        initialization()
        return binding.root
    }

    private fun initialization() {
        mLayoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        adapter = StreamVideoAdapter(mContext, videoList, this, this)
        binding.videoRv.layoutManager = mLayoutManager
        binding.videoRv.adapter = adapter
        LocalBroadcastManager.getInstance(getApplicationContext())
            .registerReceiver(receiver, IntentFilter("download_progress"))

        //Handler().postDelayed(Runnable {  updateToolbarBehaviour() },500)
    }

    public fun notifyList() {
        adapter.notifyDataSetChanged()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val streamDetailActivity = context as StreamDetailActivity
        if (streamDetailActivity != null) {
            streamDetailActivity.setUpdateVideoListListener(this)
        }

        pixel_list = mutableListOf<DownLoadModal>()
        pixel_list.add(DownLoadModal("1440p", "1440"))
        pixel_list.add(DownLoadModal("1080p", "1080"))
        pixel_list.add(DownLoadModal("720p", "720"))
        pixel_list.add(DownLoadModal("480p", "480"))
        pixel_list.add(DownLoadModal("360p", "360"))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //   Toast.makeText(context, "onDestroyView", Toast.LENGTH_SHORT).show()
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(receiver)
    }


    fun updateToolbarBehaviour() {
        if (mLayoutManager.findLastCompletelyVisibleItemPosition() == videoList.size - 1) {
            StreamDetailActivity.streamDetailActivity.turnOffToolbarScrolling()

        } else {
            StreamDetailActivity.streamDetailActivity.turnOnToolbarScrolling()
        }
    }

    override fun onResume() {
        isCompleteDownload()
        super.onResume()
    }


    private fun isCompleteDownload() {
        for (i in 0..videoList.size - 1) {
            val downloadLocalFile =
                com.doviesfitness.ui.bottom_tabbar.stream_tab.download_background.DownloadUtil.createDownloadLocalFile(
                    videoList.get(i).stream_video
                )

            if (downloadLocalFile != null) {
                val existLocalFileLength = downloadLocalFile!!.length()
                if (existLocalFileLength > 0) {
                    Log.d("Complete download", "Complete download activity...")

                    videoList.get(i).Progress = 100
                    videoList.get(i).MaxProgress = 100
                } else {
                    videoList.get(i).Progress = 0
                    videoList.get(i).MaxProgress = 0
                }
            }
        }

        val AList =
            com.doviesfitness.ui.bottom_tabbar.stream_tab.download_background.DownloadUtil.getData("video")
        if (AList != null && AList.size > 0) {
            for (i in 0..AList.size - 1) {
                if (AList[i].workout_id.equals(overViewTrailerData!!.stream_workout_id)) {
                    for (j in 0..videoList.size - 1) {
                        if (videoList.get(j).stream_video_id.equals(AList[i].stream_video_id)) {
                            videoList.get(j).isAddedToQueue = AList[i].isAddedQueue
                            break
                        } else {
                            videoList.get(j).isAddedToQueue = false
                        }
                    }
                }
            }
        } else {
            for (j in 0..videoList.size - 1) {
                videoList.get(j).isAddedToQueue = false
            }
        }
        adapter.notifyDataSetChanged()
    }

    fun playVideo(position: Int) {
        if (strList != null) {
            strList.clear()
        }
        for (i in 0..videoList.size - 1) {
            var vModel = videoList.get(i)
            var MaxProgress =
                Constant.getExerciseTime(vModel.video_duration)

            val tempListModal = StreamWorkoutDetailModel.Settings.Data.Video(
                stream_video = vModel.stream_video,
                stream_video_description = vModel.stream_video_description,
                stream_video_id = vModel.stream_video_id,
                video_duration = vModel.video_duration,
                stream_video_image = vModel.stream_video_image,
                stream_video_image_url = vModel.stream_video_image_url,
                stream_video_name = vModel.stream_video_name,
                stream_video_subtitle = vModel.stream_video_subtitle,
                order = vModel.order,
                Progress = vModel.Progress,
                MaxProgress = MaxProgress,
                seekTo = vModel.seekTo,
                isPlaying = vModel.isPlaying,
                isComplete = vModel.isComplete,
                isRestTime = vModel.isRestTime,
                downLoadUrl = "",
                hls_video = null,
                mp4_video = null,is_workout = vModel.is_workout,
                    view_type = vModel.view_type
            )
            strList.add(tempListModal)
        }

        workoutid = overViewTrailerData!!.stream_workout_id


        if (strList != null && strList.size > 0) {
            val intent = Intent(activity, StreamVideoPlayUrlActivity::class.java)
            intent.putExtra("videoList", strList)
            intent.putExtra("workout_id", "" + workoutid)
            intent.putExtra("local", "no")
            intent.putExtra("trailer", "no")
            intent.putExtra("position", position)

            activity!!.startActivity(intent)
        }
    }

    override fun isSubscribed() {
        var intent = Intent(getActivity(), SubscriptionActivity::class.java).putExtra("home", "no")
            .putExtra("exercise", "yes")
        startActivityForResult(intent, 2)
    }

   /* override fun onDownloadingClick(pos: Int) {
        val AList = DownloadUtil.getData("video")
        if (AList != null && AList.size > 0) {

            if (AList[0].workout_id.equals(overViewTrailerData!!.stream_workout_id)) {

                if (videoList != null && videoList.size > 0) {
                    if (AList[0].VideoUrl.equals(downloadUrlInAccordingToPixel)) {
                        val holder = binding.videoRv.findViewHolderForAdapterPosition(pos) as StreamVideoAdapter.MyViewHolder
                        displayPopupWindow(holder.downloadIcon, pos, "downloading")
                    }
                }
            }
        }
    }*/

    override fun onDownloadingClick(pos: Int) {
        val AList = DownloadUtil.getData("video")
        if (AList != null && AList.size > 0) {
            if (AList[0].workout_id.equals(overViewTrailerData!!.stream_workout_id)) {

                if (videoList != null && videoList.size > 0) {
                    //https://d1n9vl26vbyc5s.cloudfront.net/stream_video/mp4/1.2651960829756845_cbr_spHQ_360p.mp4
                    var url = AList.get(0).VideoUrl.split("mp4/")
                    val extensionName = url[1].split("_").toTypedArray()
                    val lastIndexName = extensionName[0]
                    var videoUrl = url[0] + lastIndexName + ".mp4"
                    Log.v("videoUrl", "" + videoUrl)


                    for (i in 0..videoList.size - 1) {
                        var mp4_video = videoList.get(i).mp4_video
                        if (AList.get(0).VideoUrl.equals(mp4_video!!.vMpeg1080p)) {
                            val holder =
                                binding.videoRv.findViewHolderForAdapterPosition(pos) as StreamVideoAdapter.MyViewHolder
                            displayPopupWindow(holder.downloadIcon, pos, "downloading")
                        } else if (AList.get(0).VideoUrl.equals(mp4_video!!.vMpeg2K)) {
                            val holder =
                                binding.videoRv.findViewHolderForAdapterPosition(pos) as StreamVideoAdapter.MyViewHolder
                            displayPopupWindow(holder.downloadIcon, pos, "downloading")
                        } else if (AList.get(0).VideoUrl.equals(mp4_video!!.vMpeg360p)) {
                            val holder =
                                binding.videoRv.findViewHolderForAdapterPosition(pos) as StreamVideoAdapter.MyViewHolder
                            displayPopupWindow(holder.downloadIcon, pos, "downloading")
                        }else if (AList.get(0).VideoUrl.equals(mp4_video!!.vMpeg480p)) {
                            val holder =
                                binding.videoRv.findViewHolderForAdapterPosition(pos) as StreamVideoAdapter.MyViewHolder
                            displayPopupWindow(holder.downloadIcon, pos, "downloading")
                        }else if (AList.get(0).VideoUrl.equals(mp4_video!!.vMpeg720p)) {
                            val holder =
                                binding.videoRv.findViewHolderForAdapterPosition(pos) as StreamVideoAdapter.MyViewHolder
                            displayPopupWindow(holder.downloadIcon, pos, "downloading")
                        }
                    }


                    /*if (AList.get(0).VideoUrl.equals(downloadUrlInAccordingToPixel)) {
                        val holder =
                            binding.videoRv.findViewHolderForAdapterPosition(pos) as StreamVideoAdapter.MyViewHolder
                        displayPopupWindow(holder.downloadIcon, pos, "downloading")
                    }*/
                }
            }
        }
    }



    override fun onVideoClickDownloadInpxl(pos: Int, videModal: StreamWorkoutDetailModel.Settings.Data.Video) {
        workOutListPosition = pos
        videModalListInPixel = videModal
        val holder = binding.videoRv.findViewHolderForAdapterPosition(pos) as StreamVideoAdapter.MyViewHolder

        val downloadLocalFile = DownloadUtil.createDownloadLocalFile(videoList.get(pos).stream_video)
        val existLocalFileLength = downloadLocalFile!!.length()
        val fileExist = downloadLocalFile.exists()
        Log.v("downloadLocalFile",""+downloadLocalFile)

        if (existLocalFileLength > 0) {
            if (holder.downloadIcon.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.download_wait).getConstantState()) {
                displayPopupWindow(holder.downloadIcon, pos, "downloading")
            } else {
                displayPopupWindow(holder.downloadIcon, pos, "downloaded")
            }
        }
        else {
            val AList = DownloadUtil.getData("video")
            if (AList != null && AList.size > 0) {
                var isAdded = false
                for (i in AList.indices) {
                    var url   =  AList.get(i).VideoUrl.split("mp4/")
                    val extensionName = url[1].split("_").toTypedArray()
                    val lastIndexName = extensionName[0]
                    var videoUrl = url[0]+lastIndexName+".mp4"
                    Log.v("videoUrl",""+videoUrl)

                    if (videoUrl == videoList.get(pos).stream_video) {
                        val holder = binding.videoRv.findViewHolderForAdapterPosition(pos) as StreamVideoAdapter.MyViewHolder
                        displayPopupWindow(holder.downloadIcon, pos, "added")
                        isAdded = true
                        break
                    }
                }
                if (!isAdded) {
                    //this code is run in dowonloading case
                    downloadVideoInPixel(pixel_list, "AddInQueue")
                }
            }
            else {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, "This app need write sdcard permission, please allow.", Toast.LENGTH_LONG).show()
                    ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_WRITE_PERMISSION_CODE)
                } else {
                    //this code is run in dowonloading case
                    downloadVideoInPixel(pixel_list,"WhenAddForDownload")

                   /* var videoModel = videoList.get(pos)
                    val modal = DownloadedModal.ProgressModal(
                        pos,
                        videoModel.stream_video,
                        overViewTrailerData!!.stream_workout_id,
                        overViewTrailerData!!.stream_workout_image,
                        overViewTrailerData!!.stream_workout_image_url,
                        overViewTrailerData!!.stream_workout_name,
                        overViewTrailerData!!.stream_workout_description,
                        true,
                        videoModel.stream_video_description,
                        videoModel.stream_video_id,
                        videoModel.video_duration,
                        videoModel.stream_video_image,
                        videoModel.stream_video_image_url,
                        videoModel.stream_video_name,
                        videoModel.stream_video_subtitle,
                        videoModel.Progress,
                        videoModel.MaxProgress,
                        videoModel.seekTo,
                        videoModel.isPlaying,
                        videoModel.isComplete,
                        videoModel.isRestTime,
                        "",
                        "",
                        videoModel.order
                    )
                    val holder = binding.videoRv.findViewHolderForAdapterPosition(pos) as StreamVideoAdapter.MyViewHolder
                    //  holder.episode.text = "added to queue"
                    holder.downloadIcon.setImageDrawable(resources.getDrawable(R.drawable.download_wait))
                    HomeTabActivity.downloadBinder!!.setUrlToList(modal)
                    HomeTabActivity.downloadBinder!!.startDownload(videoList.get(pos).stream_video, StreamDetailActivity.streamWorkoutId, 0)
                    getDataManager().setUserStringData(
                        AppPreferencesHelper.STEAM_WORKOUT_ID,
                        StreamDetailActivity.streamWorkoutId)*/
                }
            }
        }
    }

    private fun downloadVideoInPixel(dialogData: MutableList<DownLoadModal>, status: String) {
        val dialogFragment = DownloadVideoInpxlFragment.newInstance("Downloads", status)
        dialogFragment.addMenu(dialogData)
        dialogFragment.isCancelable = false
        dialogFragment.addDialogEventListener(this)
        dialogFragment.show(childFragmentManager, "Downloads")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_WRITE_PERMISSION_CODE) {
            val grantResult = grantResults[0]
            if (grantResult == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "You can continue to use this app.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(
                    context,
                    "You disallow write external storage permission, app closed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun displayPopupWindow(anchorView: View, position: Int, isDownloaded: String) {
        var popup = PopupWindow(activity!!)
        var layout = getLayoutInflater().inflate(R.layout.popup_content, null);
        popup.setContentView(layout);
        // Set content width and height
        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        // Closes the popup window when touch outside of it - when looses focus
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);

        if (isDownloaded.equals("downloaded")) {
            layout.pause_txt.text = "play"
            layout.cancel_txt.text = "Delete Download"
            layout.pause_txt.visibility = View.GONE
        } else if (isDownloaded.equals("added")) {
            layout.pause_txt.visibility = View.GONE
        } else {
            if (getDataManager().getUserStringData(AppPreferencesHelper.IS_RESUME).equals("resume")) {
                layout.pause_txt.text = "Resume All"
            }
            layout.pause_txt.visibility = View.GONE
        }

        layout.pause_txt.setOnClickListener({
            if (layout.pause_txt.text.toString().equals("play")) {
                playVideo(position)
            } else if (layout.pause_txt.text.toString().equals("Resume All")) {

                HomeTabActivity.downloadBinder!!.startDownload(videoList.get(position).stream_video, overViewTrailerData!!.stream_workout_id, 0)
                getDataManager().setUserStringData(AppPreferencesHelper.IS_RESUME, "pause")
            } else {
                HomeTabActivity.downloadBinder!!.pauseDownload()
                getDataManager().setUserStringData(AppPreferencesHelper.IS_RESUME, "resume")
            }
            popup.dismiss()
        })
        layout.cancel_txt.setOnClickListener({
            if (!layout.cancel_txt.text.toString().isEmpty() && layout.cancel_txt.text.toString().equals("Delete Download")) {
                ///// "Delete Downloaded"
                if (videoList != null && videoList.size > 0) {
                    val holder = binding.videoRv.findViewHolderForAdapterPosition(position) as StreamVideoAdapter.MyViewHolder

                    deleteDownloadLocalFile(videoList.get(position).stream_video)
                    holder.loader.visibility = View.GONE
                    holder.downloadLayout.visibility = View.VISIBLE
                    holder.downloadIcon.setImageResource(R.drawable.icon_download_new)
                    videoList.get(position).Progress = 0
                    videoList.get(position).isAddedToQueue = false
                    deleteFromDownload(position)
                }
                popup.dismiss()
            } else if (isDownloaded.equals("downloading")) {
                val holder = binding.videoRv.findViewHolderForAdapterPosition(position) as StreamVideoAdapter.MyViewHolder
                videoList.get(position).Progress = 0
                videoList.get(position).isAddedToQueue = false
                holder.loader.visibility = View.GONE
                holder.downloadLayout.visibility = View.VISIBLE
                holder.downloadIcon.setImageResource(R.drawable.icon_download_new)
                // adapter.notifyItemChanged(position)
                deleteDownloadLocalFile(downloadUrlInAccordingToPixel)
                DownloadUtil.deleteDownload()
                //  HomeTabActivity.downloadBinder!!.cancelDownload()

                val AList = DownloadUtil.getData("video")
                if (AList != null && AList.size > 0) {
                    if (AList[0].workout_id.equals(overViewTrailerData!!.stream_workout_id)) {
                        AList.removeAt(0)
                        if (AList.size > 0) {
                            DownloadUtil.setData(AList)
                            DownloadUtil.downloadExercise(AList.get(0).VideoUrl, 0, null, null)
                        } else {
                            Doviesfitness.preferences.edit().putString("video", "").commit()
                        }
                    }
                }
                popup.dismiss()
            } else {
                ///cancel download
                val AList = DownloadUtil.getData("video")
                if (AList != null && AList.size > 0) {

                    for (i in 0..AList.size - 1) {
                        if (AList[i].workout_id.equals(overViewTrailerData!!.stream_workout_id)) {

                            if (videoList != null && videoList.size > 0) {
                                val holder = binding.videoRv.findViewHolderForAdapterPosition(position) as StreamVideoAdapter.MyViewHolder


                               // https://d1n9vl26vbyc5s.cloudfront.net/stream_video/mp4/1.2651960829756845_cbr_spHQ_360p.mp4
                                var url   =  AList.get(i).VideoUrl.split("mp4/")
                                val extensionName = url[1].split("_").toTypedArray()
                                val lastIndexName = extensionName[0]
                                var videoUrl = url[0]+lastIndexName+".mp4"

                                if (videoUrl.equals(videoList.get(position).stream_video)) {
                                    deleteDownloadLocalFile(downloadUrlInAccordingToPixel)

                                    holder.loader.visibility = View.GONE
                                    holder.downloadLayout.visibility = View.VISIBLE
                                    holder.downloadIcon.setImageResource(R.drawable.icon_download_new);
                                    videoList.get(position).Progress = 0
                                    videoList.get(position).isAddedToQueue = false

                                    AList.removeAt(i)
                                    val updateList = DownloadUtil.setData(AList)
                                    val AList = DownloadUtil.getData("video")
                                    Log.v("LocalListData",""+AList+""+updateList)

                                    if (AList.size > 0) {
                                        val updateList = DownloadUtil.setData(AList)
                                        val AList = DownloadUtil.getData("video")
                                        Log.v("LocalListData",""+AList+""+updateList)

                                        if (AList != null && AList.size > 0) {
                                            popup.dismiss()
                                        }
                                    }
                                    break
                                }
                            }
                        }
                    }
                }
                popup.dismiss()
            }
        })
        layout.show_download_txt.setOnClickListener({
            val intent = Intent(mContext, DownloadsStreamActivity::class.java)
            startActivity(intent)
            popup.dismiss()
        })

        // Show anchored to button
        popup.setBackgroundDrawable(BitmapDrawable());
        //  popup.showAsDropDown(anchorView);

        var values = IntArray(2)
        anchorView.getLocationInWindow(values)
        var positionOfIcon = values[1];


        var displayMetrics = getResources().getDisplayMetrics();
        var height = (displayMetrics.heightPixels * 2) / 3

        if (positionOfIcon > height) {
            popup.showAsDropDown(anchorView, 0, -320);
        } else {
            popup.showAsDropDown(anchorView, 0, 0);
        }
    }

    private fun deleteDownloadLocalFile(downloadFileUrl: String?) {

        var ret: File? = null
        val customerName = Doviesfitness.getDataManager().getUserInfo().customer_user_name
        try {
            if (downloadFileUrl != null && !TextUtils.isEmpty(downloadFileUrl)) {
                val lastIndex = downloadFileUrl.lastIndexOf("/")
                if (lastIndex > -1) {
                    val downloadFileName1 = downloadFileUrl.substring(lastIndex + 1)
                    val extensionName = downloadFileName1.split("_").toTypedArray()
                    val lastIndexName = extensionName[0]

                    val path = Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" + BuildConfig.APPLICATION_ID + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + customerName + "//" + lastIndexName

                    ret = File(path)
                    Log.v("deletePath",""+ret)
                    if (!ret.parentFile.exists()) {
                    }
                    if (!ret.exists()) {
                    } else {
                        ret.delete()
                    }
                }
            }
        } catch (ex: IOException) {
            Log.e(DownloadUtil.TAG_DOWNLOAD_MANAGER, ex.message, ex)
        }
    }

    val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            //  Log.d("Broadcast Receiver", "Broadcast Receiver...:$intent")
            if (intent.action == "download_progress") {

                val position = intent.getIntExtra("position", 0)
                downloadingPos = position

                if (binding.videoRv.findViewHolderForAdapterPosition(position) != null) {

                    val AList = DownloadUtil.getData("video")
                    if (AList != null && AList.size > 0) {

                        if (AList[0].workout_id.equals(overViewTrailerData!!.stream_workout_id)) {

                            if (videoList != null && videoList.size > 0) {

                                //https://d1n9vl26vbyc5s.cloudfront.net/stream_video/mp4/1.2651960829756845_cbr_spHQ_360p.mp4
                                var url   =  AList.get(0).VideoUrl.split("mp4/")
                                val extensionName = url[1].split("_").toTypedArray()
                                val lastIndexName = extensionName[0]
                                var videoUrl = url[0]+lastIndexName+".mp4"

                                if (videoUrl.equals(videoList.get(position).stream_video)) {
                                    val holder =
                                        binding.videoRv.findViewHolderForAdapterPosition(position) as StreamVideoAdapter.MyViewHolder

                                    if (intent.getStringExtra("cancel") != null && !intent.getStringExtra("cancel")!!.isEmpty()) {
                                        if (intent.getStringExtra("file") != null && !intent.getStringExtra("file")!!.isEmpty()) {
                                            var filePath = intent.getStringExtra("file")
                                            var deleted = intent.getStringExtra("deleted")

                                            var PFile = File(filePath)
                                            if (PFile.exists() || deleted.equals("yes")) {
                                                holder.loader.visibility = View.GONE
                                                holder.downloadLayout.visibility = View.VISIBLE
                                                holder.downloadIcon.setImageResource(R.drawable.icon_download_new);
                                                videoList.get(position).Progress = 0
                                                videoList.get(position).isAddedToQueue = false
                                                if (PFile.exists())
                                                    PFile.delete()
                                                AList.removeAt(0)
                                                if (AList.size > 0) {
                                                    DownloadUtil.setData(AList)
                                                    if (AList != null && AList.size > 0) {

                                                        var url   =  AList.get(0).VideoUrl.split("mp4/")
                                                        val extensionName = url[1].split("_").toTypedArray()
                                                        val lastIndexName = extensionName[0]
                                                        var videoUrl = url[0]+lastIndexName+".mp4"

                                                        HomeTabActivity.downloadBinder!!.startDownload(videoUrl, StreamDetailActivity.streamWorkoutId, 0)
                                                        getDataManager().setUserStringData(AppPreferencesHelper.STEAM_WORKOUT_ID, StreamDetailActivity.streamWorkoutId)
                                                    }
                                                } else {
                                                    Doviesfitness.preferences.edit().putString("video", "").commit()
                                                }
                                            }
                                        }
                                    } else {
                                        if (intent.hasExtra("workout_id"))
                                            workoutId = intent.getStringExtra("workout_id")!!
                                        val progress =
                                            Integer.parseInt(intent.getStringExtra("progress"))
                                        Log.d(
                                            "Complete progress",
                                            "Complete progress..." + progress
                                        )

                                        holder.loader.visibility = View.VISIBLE
                                        holder.downloadLayout.visibility = View.GONE
                                        holder.loader.max = 100
                                        holder.loader.progress = progress
                                        downloadingProgress = progress
                                        if (progress == 100) {
                                            // holder.episode.text = "Downloaded"
                                            holder.loader.visibility = View.GONE
                                            holder.downloadLayout.visibility = View.VISIBLE
                                            holder.downloadIcon.setImageResource(R.drawable.complete_downloaded);
                                            Log.d(
                                                "Complete download",
                                                "Complete download..." + progress
                                            )
                                            downloadingProgress = 0
                                            downloadingPos = -1
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun deleteFromDownload(position: Int) {
        if (DownloadUtil.getDownloadedData("downloaded") != null) {
            var downloadedList = DownloadUtil.getDownloadedData("downloaded")
            if (downloadedList != null && downloadedList.size > 0) {
                for (i in 0..downloadedList.size - 1) {
                    if (downloadedList.get(i).stream_workout_id.equals(overViewTrailerData!!.stream_workout_id)) {
                        for (j in 0..downloadedList.get(i).download_list.size - 1) {
                            if (downloadedList.get(i).download_list.get(j).stream_video_id.equals(videoList.get(position).stream_video_id)) {
                                downloadedList.get(i).download_list.removeAt(j)
                                break
                            }
                        }
                        break
                    }
                }
            }
            if (downloadedList != null && downloadedList.size > 0) {
                var j = 0
                while (j < downloadedList.size) {
                    if (downloadedList.get(j).download_list != null && downloadedList.get(j).download_list.size > 0) {
                        j++
                    } else {
                        downloadedList.removeAt(j)
                    }
                }
                if (downloadedList != null && downloadedList.size > 0) {
                    DownloadUtil.setDownloadedData(downloadedList)
                } else
                    Doviesfitness.preferences.edit().putString("downloaded", "").commit()
            } else
                Doviesfitness.preferences.edit().putString("downloaded", "").commit()
        }
    }


    override fun setUpdateVideoList(videoList: ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>) {
        Log.v("checkVideoList", "" + videoList)
        notifyList()
    }

    private fun isNonNullAndChecked(menuItem: MenuItem?): Boolean {
        return menuItem != null && menuItem.isChecked
    }

    private fun loadSample(strList: ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>, workoutid: String, pos: Int, videoList: List<VideoListItem?>?) {
        val loaderTask: SampleListLoader = SampleListLoader(this, strList, workoutid, pos, videoList)
        loaderTask.execute("")
    }

    private class SampleListLoader(streamVideoFragmentNew: StreamVideoFragmentNew, strList: ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>,
                                   workoutid: String, pos: Int, videoList: List<VideoListItem?>?) : AsyncTask<String?, Void?, PlaylistHolder?>() {
        private var sawError = false
        var streamVideoFragmentNew = streamVideoFragmentNew
        var strList = strList
        var workoutid = workoutid
        var pos = pos
        var videoList = videoList

        override fun doInBackground(vararg uris: String?): PlaylistHolder? {
            var result: PlaylistHolder? = null
            try {
                result = readEntry(videoList)
            } catch (e: Exception) {
                com.google.android.exoplayer2.util.Log.e(
                    "error",
                    "Error loading sample list: $e"
                )
                sawError = true
            } finally {
            }
            return result
        }

        override fun onPostExecute(result: PlaylistHolder?) {
            // onPlaylistGroups(result, sawError)
//            val intent = Intent(streamVideoFragmentNew.mContext, StreamPlayerActivity::class.java)
            val intent = Intent(streamVideoFragmentNew.mContext, StreamVideoPlayUrlActivityTemp::class.java)

            intent.putExtra(
                IntentUtil.PREFER_EXTENSION_DECODERS_EXTRA,
                streamVideoFragmentNew.isNonNullAndChecked(streamVideoFragmentNew.preferExtensionDecodersMenuItem)
            )
            intent.putExtra("videoList", strList)
            intent.putExtra("workout_id", "" + workoutid)
            intent.putExtra("local", "no")
            intent.putExtra("trailer", "no")
            intent.putExtra("media_name", ""+StreamDetailActivity.mediaName)
            intent.putExtra("position", pos)
            IntentUtil.addToIntent(result!!.mediaItems, intent)

            streamVideoFragmentNew.startActivity(intent)
        }

        @Throws(IOException::class)
        private fun readEntry(videoList: List<VideoListItem?>?): PlaylistHolder {
            var mediaList = arrayListOf<MediaItem>()

            var vlist = arrayListOf<String>()
            vlist.add("https://d1n9vl26vbyc5s.cloudfront.net/stream_video/hls/6.095021958868067.m3u8")
            vlist.add("https://d1n9vl26vbyc5s.cloudfront.net/stream_video/hls/1.0349727709550933.m3u8")
            for (i in 0..videoList?.size!!-1) {

                var uri: Uri? = null
                var extension: String? = null
                var title: String? = null
                val mediaItem = MediaItem.Builder()
                title = "Dovies video"
               uri = Uri.parse(videoList.get(i)?.hlsVideo?.vHlsMasterPlaylist)
              //  uri = Uri.parse(vlist.get(i))
                extension = "m3u8"
           //     uri = Uri.parse("https://www.youtube.com/api/manifest/dash/id/3aa39fa2cc27967f/source/youtube?as=fmp4_audio_clear,fmp4_sd_hd_clear&sparams=ip,ipbits,expire,source,id,as&ip=0.0.0.0&ipbits=0&expire=19000000000&signature=A2716F75795F5D2AF0E88962FFCD10DB79384F29.84308FF04844498CE6FBCE4731507882B8307798&key=ik0")
           //     extension = "mpd"

/*
                if (i == videoList.size) {
                    uri =
                        Uri.parse("https://www.youtube.com/api/manifest/dash/id/3aa39fa2cc27967f/source/youtube?as=fmp4_audio_clear,fmp4_sd_hd_clear&sparams=ip,ipbits,expire,source,id,as&ip=0.0.0.0&ipbits=0&expire=19000000000&signature=A2716F75795F5D2AF0E88962FFCD10DB79384F29.84308FF04844498CE6FBCE4731507882B8307798&key=ik0")
                    extension = "mpd"
                } else {
                    uri = Uri.parse(videoList.get(i)?.hlsVideo?.vHlsMasterPlaylist)
                    extension = "m3u8"
                }
*/


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
            //   return PlaylistHolder(title, listOf(mediaItem.build()))
            return PlaylistHolder("Dovies", mediaList)
        }
    }

    private class PlaylistHolder(title: String, mediaItems: List<MediaItem>) {
        val title: String
        val mediaItems: List<MediaItem>

        init {
            Assertions.checkArgument(!mediaItems.isEmpty())
            this.title = title
            this.mediaItems = Collections.unmodifiableList(
                java.util.ArrayList(mediaItems)
            )
        }
    }

    override fun onItemClicked(pixelType: String, position: Int, status :String) {

        if (videModalListInPixel != null) {
            if (pixelType.equals("1440p")) {
                downloadUrlInAccordingToPixel = videModalListInPixel.mp4_video!!.vMpeg2K
            } else if (pixelType.equals("1080p")) {
                downloadUrlInAccordingToPixel = videModalListInPixel.mp4_video!!.vMpeg1080p
            } else if (pixelType.equals("720p")) {
                downloadUrlInAccordingToPixel = videModalListInPixel.mp4_video!!.vMpeg720p
            } else if (pixelType.equals("480p")) {
                downloadUrlInAccordingToPixel = videModalListInPixel.mp4_video!!.vMpeg480p
            } else if (pixelType.equals("360p")) {
                downloadUrlInAccordingToPixel = videModalListInPixel.mp4_video!!.vMpeg360p
            }

             DownloadUtil.createDownloadLocalFile(downloadUrlInAccordingToPixel)

            if("WhenAddForDownload".equals(status)){
                addToDownload(workOutListPosition, downloadUrlInAccordingToPixel)
            }else{
                addInQueue(workOutListPosition, downloadUrlInAccordingToPixel)
            }
        }
    }

    private fun addToDownload(workOutListPosition: Int, downloadUrlInAccordingToPixel: String) {

        var videoModel = videoList.get(workOutListPosition)
        val modal = DownloadedModal.ProgressModal(
            workOutListPosition,
            downloadUrlInAccordingToPixel,
            overViewTrailerData!!.stream_workout_id,
            overViewTrailerData!!.stream_workout_image,
            overViewTrailerData!!.stream_workout_image_url,
            overViewTrailerData!!.stream_workout_name,
            overViewTrailerData!!.stream_workout_description,
            true,
            videoModel.stream_video_description,
            videoModel.stream_video_id,
            videoModel.video_duration,
            videoModel.stream_video_image,
            videoModel.stream_video_image_url,
            videoModel.stream_video_name,
            videoModel.stream_video_subtitle,
            videoModel.Progress,
            videoModel.MaxProgress,
            videoModel.seekTo,
            videoModel.isPlaying,
            videoModel.isComplete,
            videoModel.isRestTime,
            "",
            "",
            videoModel.order
        )
        val holder = binding.videoRv.findViewHolderForAdapterPosition(this.workOutListPosition) as StreamVideoAdapter.MyViewHolder
        //  holder.episode.text = "added to queue"
        holder.downloadIcon.setImageDrawable(resources.getDrawable(R.drawable.download_wait))
        HomeTabActivity.downloadBinder!!.setUrlToList(modal)
        HomeTabActivity.downloadBinder!!.startDownload(downloadUrlInAccordingToPixel, StreamDetailActivity.streamWorkoutId, 0)
        getDataManager().setUserStringData(AppPreferencesHelper.STEAM_WORKOUT_ID, StreamDetailActivity.streamWorkoutId)
    }

    private fun addInQueue(workOutListPosition: Int, downloadUrlInAccordingToPixel: String) {
        var videoModel = videoList.get(workOutListPosition)
        val modal = DownloadedModal.ProgressModal(
            workOutListPosition,
            downloadUrlInAccordingToPixel,
            overViewTrailerData!!.stream_workout_id,
            overViewTrailerData!!.stream_workout_image,
            overViewTrailerData!!.stream_workout_image_url,
            overViewTrailerData!!.stream_workout_name,
            overViewTrailerData!!.stream_workout_description,
            true,
            videoModel.stream_video_description,
            videoModel.stream_video_id,
            videoModel.video_duration,
            videoModel.stream_video_image,
            videoModel.stream_video_image_url,
            videoModel.stream_video_name,
            videoModel.stream_video_subtitle,
            videoModel.Progress,
            videoModel.MaxProgress,
            videoModel.seekTo,
            videoModel.isPlaying,
            videoModel.isComplete,
            videoModel.isRestTime,
            "",
            "",
            videoModel.order
        )
        HomeTabActivity.downloadBinder!!.setUrlToList(modal)
        val holder = binding.videoRv.findViewHolderForAdapterPosition(workOutListPosition) as StreamVideoAdapter.MyViewHolder
        holder.downloadIcon.setImageDrawable(resources.getDrawable(R.drawable.download_wait))
    }

    override fun onDialogDismiss() {}
}
