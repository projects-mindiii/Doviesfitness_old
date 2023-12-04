package com.doviesfitness.temp

import android.Manifest
import android.app.Activity
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.*
import android.widget.PopupWindow
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.doviesfitness.Doviesfitness
import com.doviesfitness.R
import com.doviesfitness.allDialogs.ErrorDialog
import com.doviesfitness.allDialogs.ExclusiveWorkoutDialog
import com.doviesfitness.allDialogs.download_video_in_formate.DownLoadModal
import com.doviesfitness.allDialogs.download_video_in_formate.DownloadVideoInpxlFragment
import com.doviesfitness.allDialogs.menu.ErrorDownloadViewTypeDialog
import com.doviesfitness.chromecast.browser.VideoItemLoader
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.databinding.FragmentStreamVideoBinding
import com.doviesfitness.subscription.IsSubscribed
import com.doviesfitness.subscription.SubscriptionActivity
import com.doviesfitness.temp.modal.DownloadedVideoModal
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.base.BaseFragment
import com.doviesfitness.ui.bottom_tabbar.HomeTabActivity
import com.doviesfitness.ui.bottom_tabbar.stream_tab.activities.StreamDetailActivity
import com.doviesfitness.ui.bottom_tabbar.stream_tab.activities.StreamDetailActivity.Companion.overViewTrailerData
import com.doviesfitness.ui.bottom_tabbar.stream_tab.activities.StreamLogHistoryActivity
import com.doviesfitness.ui.bottom_tabbar.stream_tab.activities.StreamVideoPlayUrlActivity
import com.doviesfitness.ui.bottom_tabbar.stream_tab.download_background.DownloadUtil
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.StreamWorkoutDetailModel
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.VideoListItem
import com.doviesfitness.ui.bottom_tabbar.stream_tab.temp.ExclusiveInterface
import com.doviesfitness.ui.multipleQuality.IntentUtil
import com.doviesfitness.ui.multipleQuality.StreamVideoPlayUrlActivityTemp
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.Constants
import com.facebook.FacebookSdk
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.util.Assertions
import com.google.android.exoplayer2.util.Util
import com.google.android.gms.cast.MediaInfo
import com.showVisibility
import kotlinx.android.synthetic.main.popup_content.view.*
import java.io.IOException
import java.util.*

/**Created by Yashank Rathore on 16,December,2020 yashank.mindiii@gmail.com **/

class TempSteamVideoFragmentNew : BaseFragment(),
    StreamDetailActivity.UpdateListInterface, IsSubscribed,
    DownloadVideoInpxlFragment.DownloadDialogEventListener,
    LoaderManager.LoaderCallbacks<List<MediaInfo>>, ExclusiveInterface {
    private lateinit var pixelList: MutableList<DownLoadModal>
    lateinit var binding: FragmentStreamVideoBinding

    lateinit var downloadedVideoReceiver: BroadcastReceiver
    private lateinit var videoListItem: StreamWorkoutDetailModel.Settings.Data.Video
    var downloadingPos = -1
    var downloadingProgress = 0
    lateinit var mLayoutManager: LinearLayoutManager
    private val REQUEST_WRITE_PERMISSION_CODE = 1
    private var workoutid: String = ""
    private var downloadUrlInAccordingToPixel: String = ""
    lateinit var tempStreamVideoAdapter: TempStreamVideoAdapter
    private var workOutListPosition: Int = 0 //video list item position
    var strList = ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>()
    var workoutId = ""
    lateinit var downloadProgressReceiver: BroadcastReceiver
    var getItemPositionByIds = -1
    var getVideoUrl = ""
    lateinit var popupWindow: PopupWindow
    var count = 0
    var onclick = 0

    companion object {
        var shouldRefreshList = false
    }

    private var mLastClickTime: Long = 0
    var download_status = ""

    private var isDownloadingCoutinue = false
    var castLoaderData = ArrayList<MediaInfo>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val streamDetailActivity = context as StreamDetailActivity
        streamDetailActivity.setUpdateVideoListListener(this)
//-----------------Set api responce
        pixelList = mutableListOf<DownLoadModal>()
//        pixelList.add(DownLoadModal("1440p", "1440"))
//        pixelList.add(DownLoadModal("1080p", "1080"))
//        pixelList.add(DownLoadModal("720p", "720"))
//        pixelList.add(DownLoadModal("480p", "480"))
//        pixelList.add(DownLoadModal("360p", "360"))
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_stream_video, container, false)
        showDownloadProgressReceiver()
        showDownloadedVideoListFromReceiver()
        shouldRefreshList = true
        initialization()
        return binding.root
    }

    private fun initialization() {
        TempAsynctask.isDownloadStarted = true
        mLayoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)

        tempStreamVideoAdapter = object : TempStreamVideoAdapter(this,this) {
            override fun onVideoClickDownloadInpxl(itemPos: Int) {
                videoListItem = StreamDetailActivity.videoList[itemPos]
                Log.d("fnaknfa", "onVideoClickDownloadInpxl: ")
                if (videoListItem.view_type.equals("3"))
                //0==view only
                {
                    performVideoDownloadInPxl(itemPos)
                } else if (videoListItem.view_type.equals("1"))
                //1==download only
                {
                    performVideoDownloadInPxl(itemPos)
//                    ErrorDialog.newInstance("","ok",getString(R.string.error_download_type))
//                        .show(childFragmentManager)
                } else if (videoListItem.view_type.equals("2")) {
                    //2==both
//                    ErrorDialog.newInstance("","Ok",getString(R.string.error_view_type))
//                        .show(childFragmentManager)
                    ErrorDownloadViewTypeDialog.newInstance(
                        "2",
                        "Ok",
                        getString(R.string.error_view_type)
                    )
                        .show(childFragmentManager)

                }

            }

            override fun onDownloadingClick(itemPos: Int) {
                Log.d("fnaknfa", "onDownloadingClick: ")
                performOnDownloadingClick(itemPos)
            }

            override fun onVideoPreviewClick(itemPos: Int, status: String) {
                Log.d("fnaknfa", "downloadVideo: ")
                val time = System.currentTimeMillis()
                videoListItem = StreamDetailActivity.videoList[itemPos]
                if (videoListItem.view_type.equals("2") || videoListItem.view_type.equals("3")) {
                    performVideoPreviewOnClick(itemPos, status)
                } else if (videoListItem.view_type.equals("1") && videoListItem.Progress == 100) {
                    performVideoPreviewOnClick(itemPos, status)
                } else {
                    ErrorDownloadViewTypeDialog.newInstance(
                        "",
                        "Ok",
                        getString(R.string.error_download_type)
                    )
                        .show(childFragmentManager)
                }
            }


        }
        binding.videoRv.layoutManager = mLayoutManager
        binding.videoRv.setHasFixedSize(true)
        binding.videoRv.adapter = tempStreamVideoAdapter
        loadCastData()
        LocalBroadcastManager.getInstance(FacebookSdk.getApplicationContext())
            .registerReceiver(
                downloadProgressReceiver,
                IntentFilter(Constants.DOWNLOADING_PROGRESS)
            )
//        requireContext().registerReceiver(
//            downloadedVideoReceiver,
//            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
//        )
    }

    private fun performVideoPreviewOnClick(itemPos: Int, status: String) {
        /*check weather video downloaded in storage or not, if video downloaded then play directly form local storage otherwise
        play video form video URL of the list*/
//      if(count==0) {
//          count = 1;
        if (SystemClock.elapsedRealtime() - mLastClickTime < 4000) {
            return
        }
        mLastClickTime = SystemClock.elapsedRealtime()

        val downloadedList = DownloadVideosUtil.getDownloadedVideo(Constants.DOWNLOADED_VIDEO)
        if (strList != null && strList.isNotEmpty()) {
            strList.clear()
        }
        onclick = 1

        for (i in 0 until StreamDetailActivity.videoList.size) {
            val vModel = StreamDetailActivity.videoList.get(i)
            val maxProgress =
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
                MaxProgress = maxProgress,
                seekTo = vModel.seekTo,
                isPlaying = vModel.isPlaying,
                isComplete = vModel.isComplete,
                isRestTime = vModel.isRestTime,
                downLoadUrl = "",
                hls_video = vModel.hls_video,
                mp4_video = null, is_workout = vModel.is_workout,
                view_type = vModel.view_type
            )

            strList.add(tempListModal)

            var getTempVideoUrl = ""
            val getVideoList = StreamDetailActivity.videoList
            if (getVideoList[itemPos].Progress == 100) { //i.e. this item is downloaded
                DownloadVideosUtil.getDownloadedVideo(Constants.DOWNLOADED_VIDEO)
                    ?.let { downloadedVideoList ->
                        for (i in 0 until downloadedVideoList.size) {
                            if (downloadedVideoList[i].stream_workout_id.equals(StreamDetailActivity.overViewTrailerData!!.stream_workout_id)) {
                                for (j in 0 until downloadedVideoList[i].download_list.size) {
                                    for (k in 0 until strList.size) {
                                        if (downloadedVideoList.get(i).download_list[j].stream_video_id.equals(
                                                strList[k].stream_video_id
                                            )
                                        ) {
                                            strList[k].downLoadUrl =
                                                downloadedVideoList?.get(i)!!.download_list[j]
                                                    .downLoadUrl
                                            getTempVideoUrl = strList[k].downLoadUrl
                                            break
                                        }
                                    }
                                }
                            } else {
                                break
                            }
                        }
                    }
            } else {
                if (i == 0) {
                    if (download_status.equals("running")) {
                        if (getVideoList[itemPos].Progress > 0 && getVideoList[itemPos].Progress < 100) {
                            playVideo(itemPos);
                        }

                    } else if (download_status.equals("pending")) {
                        playVideo(itemPos);
                    } else if (download_status.equals("download")) {
                        if (onclick == 1) {
                            strList.let {
                                if (it.isNotEmpty()) {
                                    if (StreamDetailActivity.qualitymodel?.settings?.data?.videoList!!.isNotEmpty()) {
                                        var image =
                                            "" + overViewTrailerData?.stream_workout_image_url + "medium/" + overViewTrailerData?.stream_workout_image

                                        SampleListLoader(
                                            image,
                                            requireContext(),
                                            strList,
                                            workoutid,
                                            itemPos,
                                            StreamDetailActivity.qualitymodel?.settings?.data?.videoList,
                                            castLoaderData
                                        ).execute("")
                                    }
//                  onclick=1
//              }

//            if (it.isNotEmpty()) {
//                if (StreamDetailActivity.streamWorkoutId.equals("70") || StreamDetailActivity.streamWorkoutId.equals(
//                        "71"
//                    )
//                ) {
//                    Log.d("mvmxnvmnxmv", "71111 ${strList.size}" + StreamDetailActivity.streamWorkoutId+" == "+strList[0].stream_video)
//
//
//                } else {
//                    Log.d("mvmxnvmnxmv", "INNER   + ${StreamDetailActivity.streamWorkoutId} == ${strList[0].stream_video}")
//                    Log.d("hjfkasnfklnasklfas", "INNER :GetTep ${strList.size}" + workoutid)
//                    Log.d("hjfkasnfklnasklfas", "INNER :GetTep ${strList[0].downLoadUrl}")
//                    val intent = Intent(requireContext(), StreamVideoPlayUrlActivity::class.java)
//                    intent.putExtra("videoList", strList)
//                    intent.putExtra("workout_id", "" + workoutid)
//                    intent.putExtra("local", "no")
//                    intent.putExtra("trailer", "no")
//                    intent.putExtra("position", itemPos)
//                    requireContext().startActivity(intent)
//                }
//            }
                                }

                            }
//                              onclick=0
                        }
                    }
                }
            }
            Log.d(
                "hjfkasnfklnasklfas",
                "performVideoPreviewOnClick:GetTemp ${strList.size}" + getTempVideoUrl
            )
        }
        Log.d(
            "mvmxnvmnxmv",
            "OUTER   + ${StreamDetailActivity.streamWorkoutId} == ${strList[0].stream_video}"
        )
        if (onclick == 1) {

            strList.let {
                if (it.isNotEmpty()) {
                    if (StreamDetailActivity.qualitymodel?.settings?.data?.videoList!!.isNotEmpty()) {
                        var image =
                            "" + overViewTrailerData?.stream_workout_image_url + "medium/" + overViewTrailerData?.stream_workout_image

                        SampleListLoader(
                            image,
                            requireContext(),
                            strList,
                            workoutid,
                            itemPos,
                            StreamDetailActivity.qualitymodel?.settings?.data?.videoList,
                            castLoaderData
                        ).execute("")
                    }
//                  onclick=1
//              }

//            if (it.isNotEmpty()) {
//                if (StreamDetailActivity.streamWorkoutId.equals("70") || StreamDetailActivity.streamWorkoutId.equals(
//                        "71"
//                    )
//                ) {
//                    Log.d("mvmxnvmnxmv", "71111 ${strList.size}" + StreamDetailActivity.streamWorkoutId+" == "+strList[0].stream_video)
//
//
//                } else {
//                    Log.d("mvmxnvmnxmv", "INNER   + ${StreamDetailActivity.streamWorkoutId} == ${strList[0].stream_video}")
//                    Log.d("hjfkasnfklnasklfas", "INNER :GetTep ${strList.size}" + workoutid)
//                    Log.d("hjfkasnfklnasklfas", "INNER :GetTep ${strList[0].downLoadUrl}")
//                    val intent = Intent(requireContext(), StreamVideoPlayUrlActivity::class.java)
//                    intent.putExtra("videoList", strList)
//                    intent.putExtra("workout_id", "" + workoutid)
//                    intent.putExtra("local", "no")
//                    intent.putExtra("trailer", "no")
//                    intent.putExtra("position", itemPos)
//                    requireContext().startActivity(intent)
//                }
//            }
                }
            }
//              onclick=0
        }
//        Handler().postDelayed({
//            count=0
//
//            // close this activity
//
//        }, (5 * 1000).toLong())

    }

    private fun getDownloadedFileFromLocal(intent: Intent?) {
        Log.d("fnkanfnkfas", "getDownloadedFileFromLocal: OUTER")
        var filePath = ""
        val _query = DownloadManager.Query()

        _query.setFilterById(intent?.extras!!.getLong(DownloadManager.EXTRA_DOWNLOAD_ID))
        val c: Cursor = DownloadVideosUtil.GetDownloadManger.getInstance()!!.query(_query)

        if (c.moveToFirst()) {
            Log.d("fnkanfnkfas", "getDownloadedFileFromLocal: moveToFirst")
            val status: Int = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))
            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                val downloadFileLocalUri: String =
                    c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
                Uri.parse(downloadFileLocalUri).path?.let {
                    filePath = it
                }
                Log.d("fnkanfnkfas", "getDownloadedFileFromLocal: FILE $filePath")
            }
        }
        c.close()
    }

    private fun showDownloadedVideoListFromReceiver() {
        downloadedVideoReceiver = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                getDownloadedFileFromLocal(p1)
            }

        }
    }

    private fun showDownloadProgressReceiver() {
        downloadProgressReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val downloadStatus = intent.getIntExtra(Constants.DOWNLOAD_STATUS, -1)!!
                val progress = intent.getIntExtra(Constants.PROGRESS, 0)!!
                val workoutId = intent.getStringExtra(Constants.WORKOUT_ID)!!
                val streamVideoId = intent.getStringExtra(Constants.STREAM_VIDEO_ID)!!
                getVideoUrl = intent.getStringExtra(Constants.DOWNLOAD_URL_IN_ACCOURDING_TO_PIXEL)!!
                Log.d("xzxxzxzxzxzxz", "RECEIVER Part: $getVideoUrl")
                Log.d(
                    "tryitrytiryt",
                    "OUTER Part: ${workoutId} --- ${overViewTrailerData!!.stream_workout_id} -- Size ${StreamDetailActivity.videoList.size} == ${TempAsynctask.isDownloadStarted}"
                )
                if (StreamDetailActivity.videoList.size > 0 && workoutId == overViewTrailerData!!.stream_workout_id) {
                    if (TempAsynctask.isDownloadStarted) {
                        for (i in 0 until StreamDetailActivity.videoList.size) {
                            val videoId = StreamDetailActivity.videoList[i].stream_video_id
                            if (videoId.equals(streamVideoId, true)) {
                                getItemPositionByIds = i
                                break
                            }
                        }
                        TempAsynctask.isDownloadStarted = false
                    }

                    Log.d(
                        "tryitrytiryt",
                        "Inner Part: ${getItemPositionByIds} --- ${TempAsynctask.isDownloadStarted}"
                    )

                    binding.videoRv.findViewHolderForAdapterPosition(getItemPositionByIds)?.let {
                        val holderView =
                            binding.videoRv.findViewHolderForAdapterPosition(getItemPositionByIds) as TempStreamVideoAdapter.SteamHolder

                        when (downloadStatus) {
                            DownloadManager.STATUS_FAILED -> { /*delete current download and start download if queue list has more video*/
                                Log.d("tryitrytiryt", "STATUS_FAILED Part: ${downloadStatus}")
//                                DownloadVideosUtil.deleteAndStartDownload(
//                                    workoutId,
//                                    overViewTrailerData!!.stream_workout_id,
//                                    getVideoUrl
//                                )
                                StreamDetailActivity.videoList.get(getItemPositionByIds).Progress =
                                    0
                                StreamDetailActivity.videoList.get(getItemPositionByIds).isAddedToQueue =
                                    false

                                holderView.binding.loader.progress = 0
                                holderView.binding.downloadIcon.showVisibility(true)
                                holderView.binding.loader.showVisibility(false)
                                holderView.binding.downloadIcon.setImageResource(R.drawable.icon_download_new)
                                // showErrorDialog(getString(R.string.something_wrong))
                            }

                            DownloadManager.STATUS_PENDING -> {
                                isDownloadingCoutinue = true
                                if (TempAsynctask.isDownloadCancel) {
                                    isDownloadingCoutinue = false
                                    return
                                }

                                Log.d("vnvnmxnvmxv", "deleteQueueListItem: STATUS_PENDING ")
                                Log.d("tryitrytiryt", "STATUS_PENDING Part: ${downloadStatus}")
                                StreamDetailActivity.videoList.get(getItemPositionByIds).Progress =
                                    progress
                                StreamDetailActivity.videoList.get(getItemPositionByIds).isAddedToQueue =
                                    true
                                holderView.binding.loader.progress = 0
                                holderView.binding.downloadIcon.showVisibility(true)
                                holderView.binding.loader.showVisibility(false)
                                holderView.binding.downloadIcon.setImageResource(R.drawable.download_wait)
                                download_status = "pending"
                            }

                            DownloadManager.STATUS_RUNNING -> {
                                Log.d("vnvnmxnvmxv", "deleteQueueListItem: STATUS_RUNNING ")
                                isDownloadingCoutinue = true
                                /*after cancel the current download this running block will call for that you will find undesired UI update on recycler view, that's why used flag to stop further statement after cancel the current download video*/
                                if (TempAsynctask.isDownloadCancel) {
                                    isDownloadingCoutinue = false
                                    return
                                }
                                download_status = "running"

                                StreamDetailActivity.videoList.get(getItemPositionByIds).Progress =
                                    progress /*handling through list value that's why setting only value and do not need to call adapter notify*/
                                holderView.binding.progressLayout.showVisibility(true)
                                holderView.binding.loader.showVisibility(true)
                                holderView.binding.loader.progress = progress
                                holderView.binding.downloadIcon.showVisibility(false)
                                Log.d("tryitrytiryt", "onReceive: Running ${progress}")

                            }

                            DownloadManager.STATUS_SUCCESSFUL -> {
                                if (::popupWindow.isInitialized && popupWindow.isShowing) {
                                    popupWindow.dismiss()
                                }
                                isDownloadingCoutinue = false
                                StreamDetailActivity.videoList.get(getItemPositionByIds).Progress =
                                    100
                                holderView.binding.loader.progress = progress
                                holderView.binding.loader.showVisibility(false)
                                holderView.binding.progressLayout.showVisibility(false)
                                holderView.binding.downloadLayout.showVisibility(true)
                                holderView.binding.downloadIcon.showVisibility(true)
                                holderView.binding.downloadIcon.setImageResource(R.drawable.complete_downloaded)
                                Log.d("xcvxvvcxxvvvvvv", "onSuccess: ${progress}")
                                holderView.binding.playVideo.visibility = View.VISIBLE
                                download_status = "download"
                            }

                            Constants.ERROR_INSUFFICIENT_SPACE_CODE -> {
                                Log.d(
                                    "xcvxvvcxxvvvvvv",
                                    "ERROR_INSUFFICIENT_SPACE_CODE: ${downloadStatus}"
                                )
                                //consider as a failed status
//                                DownloadVideosUtil.deleteAndStartDownload(
//                                    workoutId,
//                                    overViewTrailerData!!.stream_workout_id,
//                                    getVideoUrl
//                                )
                                StreamDetailActivity.videoList.get(getItemPositionByIds).Progress =
                                    0
                                StreamDetailActivity.videoList.get(getItemPositionByIds).isAddedToQueue =
                                    false
                                holderView.binding.loader.progress = 0
                                holderView.binding.downloadIcon.showVisibility(true)
                                holderView.binding.loader.showVisibility(false)
                                holderView.binding.downloadIcon.setImageResource(R.drawable.icon_download_new)
                                showErrorDialog("Insufficient Storage Space")
                            }
                            //this will also call on Constants.DOWNLOAD_UNKNOWN_ERROR condition
                            else -> {
                                isDownloadingCoutinue = false
                                Log.d("xcvxvvcxxvvvvvv", "Else Part: ${downloadStatus}")
                                //consider as a failed status
//                                DownloadVideosUtil.deleteAndStartDownload(
//                                    workoutId,
//                                    overViewTrailerData!!.stream_workout_id,
//                                    getVideoUrl
//                                )
                                StreamDetailActivity.videoList.get(getItemPositionByIds).Progress =
                                    0
                                StreamDetailActivity.videoList.get(getItemPositionByIds).isAddedToQueue =
                                    false
                                holderView.binding.loader.progress = 0
                                holderView.binding.downloadIcon.showVisibility(true)
                                holderView.binding.loader.showVisibility(false)
                                holderView.binding.downloadIcon.setImageResource(R.drawable.icon_download_new)
                                // showErrorDialog(getString(R.string.something_wrong))
                            }
                        }
                    }
                }
            }
        }
    }

    private fun performVideoDownloadInPxl(itemPos: Int) {
        val queueVideoList = DownloadVideosUtil.getVideoQueueList(Constants.QUEUE_VIDEO)
        workOutListPosition = itemPos
        videoListItem = StreamDetailActivity.videoList[itemPos]
        // DownloadUtil.createDownloadLocalFile(StreamDetailActivity.videoList.get(itemPos).stream_video)/*need to create this */
        //-------------Stream Qulity coma sprate and handel Download Qty List---------------------------------
        var stream_quality = videoListItem.mp4_video?.downaload_quality
        val strs = stream_quality?.split(",")?.toTypedArray()
//        pixelList.add(strs)
        Log.e("Pixek List-->", strs.toString())
        pixelList.clear()
        for (k in 0 until strs!!.size) {
            pixelList.add(DownLoadModal(strs[k], strs[k].replace("p", "")))
        }
        //--------------------------------------------------------
        val holder =
            binding.videoRv.findViewHolderForAdapterPosition(itemPos) as TempStreamVideoAdapter.SteamHolder
        if (videoListItem.Progress != 0 && videoListItem.Progress != 100) {

            Log.d("nfalnfklsffas", "performVideoDownloadInPxl: DOWNLOADING")
            displayPopupWindow(holder.binding.downloadIcon, itemPos, "downloading")
        } else if (videoListItem.Progress == 100) {
            displayPopupWindow(holder.binding.downloadIcon, itemPos, "downloaded")
        } else if (videoListItem.isAddedToQueue) {
            displayPopupWindow(holder.binding.downloadIcon, itemPos, "beforeDownload")
        } else {
            Log.d("nfalnfklsffas", "performVideoDownloadInPxl: ELSEEEEE")
            if (queueVideoList != null && queueVideoList.isNotEmpty()) {
                /*checking video added in queue or not if not add then add to queue other wise popup will appear*/
                var isVideoAddedInQueue = false
                for (i in queueVideoList.indices) {
                    val url = queueVideoList[i].videoUrl.split("mp4/")
                    val extensionName = url[1].split("_").toTypedArray()
                    val lastIndexName = extensionName[0]
                    val videoUrl = url[0] + lastIndexName + ".mp4"

                    if (videoUrl == StreamDetailActivity.videoList.get(itemPos).stream_video) {
                        displayPopupWindow(
                            holder.binding.downloadIcon,
                            itemPos,
                            "added"
                        ) /*popup appear*/
                        isVideoAddedInQueue = true
                        break
                    }
                }

                if (!isVideoAddedInQueue) {
                    downloadVideoInPixel(pixelList, Constants.ADD_TO_QUEUE)

                }
            } else {
                if (ContextCompat.checkSelfPermission(
                        FacebookSdk.getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    Toast.makeText(
                        context,
                        "This app needs write sdcard permission, please allow.",
                        Toast.LENGTH_LONG
                    ).show()
                    ActivityCompat.requestPermissions(
                        context as Activity,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        REQUEST_WRITE_PERMISSION_CODE
                    )
                } else {
                    downloadVideoInPixel(pixelList, Constants.ADD_TO_DOWNLOAD)
//                    downloadUrlInAccordingToPixel = videoListItem.mp4_video!!.vMpeg480p
//                    addToDownload(workOutListPosition, downloadUrlInAccordingToPixel, Constants.ADD_TO_DOWNLOAD)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 50 && resultCode == Activity.RESULT_OK) {
            startActivity(Intent(getActivity(), StreamLogHistoryActivity::class.java))
            activity?.finish()
        } else {
        }
    }

    private fun downloadVideoInPixel(dialogData: MutableList<DownLoadModal>, status: String) {
//        val dialogFragment = DownloadVideoInpxlFragment.newInstance("Downloads", status)
        val dialogFragment = DownloadVideoInpxlFragment.newInstance("Download Quality", status)
        dialogFragment.addMenu(dialogData)
        dialogFragment.isCancelable = true
        dialogFragment.addDialogEventListener(this)
//        dialogFragment.show(childFragmentManager, "Downloads")
        dialogFragment.show(childFragmentManager, "Download Quality")
    }

    private fun performOnDownloadingClick(itemPos: Int) {
        val queueVideoList = DownloadVideosUtil.getVideoQueueList(Constants.QUEUE_VIDEO)
        if (queueVideoList != null && queueVideoList.size > 0) {
            if (queueVideoList[0].workout_id.equals(StreamDetailActivity.overViewTrailerData!!.stream_workout_id)) {
                if (StreamDetailActivity.videoList.size > 0) {
                    val holder =
                        binding.videoRv.findViewHolderForAdapterPosition(itemPos) as TempStreamVideoAdapter.SteamHolder
                    displayPopupWindow(holder.binding.downloadIcon, itemPos, "downloading")
                }
            }
        }
    }

    private fun displayPopupWindow(anchorView: View, position: Int, isDownloaded: String) {
        Log.d(
            "fanjkfnkasfa",
            "displayPopupWindow: NAME $isDownloaded "
        )
        Log.d("nfaksnfas", "displayPopupWindow: ${position} -> Item position")
        val queueVideoList = DownloadVideosUtil.getVideoQueueList(Constants.QUEUE_VIDEO)

        val holderView =
            binding.videoRv.findViewHolderForAdapterPosition(position) as TempStreamVideoAdapter.SteamHolder

        popupWindow = PopupWindow(requireActivity())
        val layout = layoutInflater.inflate(R.layout.popup_content, null)
        popupWindow.contentView = layout
        // Set content width and height
        popupWindow.height = WindowManager.LayoutParams.WRAP_CONTENT
        popupWindow.width = WindowManager.LayoutParams.WRAP_CONTENT
        // Closes the popup window when touch outside of it - when looses focus
        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true

        if (isDownloaded.equals("downloaded")) {
            layout.pause_txt.text = "play"
            layout.cancel_txt.text = "Delete Download"
            layout.pause_txt.visibility = View.GONE
        } else if (isDownloaded.equals("added")) {
            layout.pause_txt.visibility = View.GONE
        } else {
            if (getDataManager().getUserStringData(AppPreferencesHelper.IS_RESUME)
                    .equals("resume")
            ) {
                layout.pause_txt.text = "Resume All"
            }
            layout.pause_txt.visibility = View.GONE
        }

        layout.pause_txt.setOnClickListener {
            if (layout.pause_txt.text.toString().equals("play")) {
                playVideo(position)
            } else if (layout.pause_txt.text.toString().equals("Resume All")) {
                HomeTabActivity.downloadBinder!!.startDownload(
                    StreamDetailActivity.videoList.get(
                        position
                    ).stream_video,
                    StreamDetailActivity.overViewTrailerData!!.stream_workout_id,
                    0
                )
                getDataManager().setUserStringData(AppPreferencesHelper.IS_RESUME, "pause")
            } else {
                HomeTabActivity.downloadBinder!!.pauseDownload()
                getDataManager().setUserStringData(AppPreferencesHelper.IS_RESUME, "resume")
            }
            popupWindow.dismiss()
        }
        layout.cancel_txt.setOnClickListener {
            if (!layout.cancel_txt.text.toString()
                    .isEmpty() && layout.cancel_txt.text.toString()
                    .equals("Delete Download")
            ) {
                if (StreamDetailActivity.videoList.size > 0) {
                    Log.d(
                        "fankfnkasfa",
                        "Delete Button video URL: ${
                            StreamDetailActivity.videoList.get(
                                position
                            ).stream_video
                        }"
                    )
                    DownloadVideosUtil.deleteDownloadLocalFile(
                        StreamDetailActivity.videoList.get(
                            position
                        ).stream_video
                    )
                    if (StreamDetailActivity.videoList.get(position).view_type.equals("1")) {
                        holderView.binding.playVideo.visibility = View.GONE
                    }

                    holderView.binding.loader.progress = 0
                    holderView.binding.downloadLayout.showVisibility(true)
                    holderView.binding.downloadIcon.showVisibility(true)
                    holderView.binding.progressLayout.showVisibility(false)
                    holderView.binding.loader.showVisibility(false)
                    holderView.binding.downloadIcon.setImageResource(R.drawable.icon_download_new)


                    StreamDetailActivity.videoList.get(position).Progress = 0
                    StreamDetailActivity.videoList.get(position).isAddedToQueue = false
                    DownloadVideosUtil.deleteFromDownload(
                        position,
                        overViewTrailerData!!.stream_workout_id
                    )
                }
                popupWindow.dismiss()
            } else if (isDownloaded.equals("downloading")) {
                Log.d("bmbmbmbmbmmb", "displayPopupWindow: DOWNLOADING}")
                deleteCurrentDownloadVideo(position)
                popupWindow.dismiss()
            } else if (isDownloaded == "beforeDownload") {
                val _queueVideoList =
                    DownloadVideosUtil.getVideoQueueList(Constants.QUEUE_VIDEO)

                if (_queueVideoList != null) {
                    Log.d("bmbmbmbmbmmb", "displayPopupWindow: ${_queueVideoList.size}")
                    if (_queueVideoList.size == 1) { /*delete current video if only start for downloading not started through DM*/
                        Log.d("bmbmbmbmbmmb", "CURRENT DOWLOADING:")
                        deleteCurrentDownloadVideo(position)
                    } else {
                        Log.d("bmbmbmbmbmmb", "Delete Queue")
                        deleteQueueListItem(position)
                    }
                } else {

                    Log.d("bmbmbmbmbmmb", "displayPopupWindow: elseee")
                }
                popupWindow.dismiss()
            } else {
                Log.d(
                    "fanjkfnkasfa",
                    "ELSE ONE: ${getVideoUrl} === ${downloadUrlInAccordingToPixel}"
                )
                ///cancel download
                queueVideoList?.let {
                    if (it.isNotEmpty()) {
                        for (i in 0 until it.size) {
                            if (it[i].workout_id.equals(overViewTrailerData!!.stream_workout_id)) {

                                if (StreamDetailActivity.videoList.isNotEmpty()) {
                                    val url = queueVideoList.get(i).videoUrl.split("mp4/")
                                    val extensionName = url[1].split("_").toTypedArray()
                                    val lastIndexName = extensionName[0]
                                    val videoUrl = url[0] + lastIndexName + ".mp4"

                                    if (videoUrl.equals(
                                            StreamDetailActivity.videoList.get(
                                                position
                                            ).stream_video
                                        )
                                    ) {
                                        DownloadVideosUtil.deleteDownloadLocalFile(
                                            getVideoUrl
                                        )
                                        StreamDetailActivity.videoList.get(position).Progress =
                                            0
                                        StreamDetailActivity.videoList.get(position).isAddedToQueue =
                                            false
                                        tempStreamVideoAdapter.notifyItemChanged(position)
                                        queueVideoList.removeAt(i)
                                        if (queueVideoList.isNotEmpty()) {
                                            DownloadVideosUtil.saveRemainingQueueVideosToLocal(
                                                queueVideoList
                                            )
                                        }
                                        break
                                    }
                                }
                            }
                        }
                    }
                }
                popupWindow.dismiss()
            }
        }

        layout.show_download_txt.setOnClickListener {
            Intent(requireContext(), TempDownloadStreamActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(this)
            }
            popupWindow.dismiss()
        }
        popupWindow.setBackgroundDrawable(BitmapDrawable())
        val values = IntArray(2)
        anchorView.getLocationInWindow(values)
        val positionOfIcon = values[1]

        val displayMetrics = resources.displayMetrics
        val height = (displayMetrics.heightPixels * 2) / 3

        if (positionOfIcon > height) {
            popupWindow.showAsDropDown(anchorView, 0, -320)
        } else {
            popupWindow.showAsDropDown(anchorView, 0, 0)
        }
    }

    fun playVideo(position: Int) {
        if (strList.size > 0) {
            strList.clear()
        }
        for (i in 0..StreamDetailActivity.videoList.size - 1) {
            val vModel = StreamDetailActivity.videoList.get(i)
            val MaxProgress =
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
                mp4_video = null, is_workout = vModel.is_workout,
                view_type = vModel.view_type
            )
            strList.add(tempListModal)
        }

        workoutid = StreamDetailActivity.overViewTrailerData!!.stream_workout_id

        if (strList.size > 0) {
            val intent = Intent(activity, StreamVideoPlayUrlActivity::class.java)
            intent.putExtra("videoList", strList)
            intent.putExtra("workout_id", "" + workoutid)
            intent.putExtra("local", "no")
            intent.putExtra("trailer", "no")
            intent.putExtra("position", position)

            activity!!.startActivity(intent)
        }
    }

    private fun isCompleteDownload() {
        for (i in 0 until StreamDetailActivity.videoList.size) {
            Log.d(
                "fankfnkasfa",
                "isCompleteDownload: ${StreamDetailActivity.videoList.get(i).stream_video}"
            )
            DownloadUtil.createDownloadLocalFile(
                StreamDetailActivity.videoList.get(i).stream_video
            )?.let { downloadLocalFile ->
                val existLocalFileLength = downloadLocalFile.length()
                Log.d("fnalknfas", "isCompleteDownload: $existLocalFileLength")
                if (existLocalFileLength > 0) {
                    StreamDetailActivity.videoList.get(i).Progress = 100
                    StreamDetailActivity.videoList.get(i).MaxProgress = 100
                } else {
                    StreamDetailActivity.videoList.get(i).Progress = 0
                    StreamDetailActivity.videoList.get(i).MaxProgress = 0
                    StreamDetailActivity.videoList.get(i).isAddedToQueue = false
                }
            }
        }

        Log.d("fanfklnasfssssss", "isCompleteDownload: BEFORE ")


        DownloadVideosUtil.getVideoQueueList(Constants.QUEUE_VIDEO)?.let {
            Log.d("fnaslfnsf", "QUEEE VIDEO SIZE: ${it.size}")
            if (it.isNotEmpty()) {
                for (i in 0 until it.size) {
                    if (it[i].workout_id.equals(overViewTrailerData!!.stream_workout_id)) {
                        for (j in 0 until StreamDetailActivity.videoList.size) {
                            if (StreamDetailActivity.videoList[j].stream_video_id.equals(it[i].stream_video_id)) {
                                StreamDetailActivity.videoList[j].isAddedToQueue =
                                    it[i].isAddedQueue
                                break
                            } else {
                                StreamDetailActivity.videoList[j].isAddedToQueue = false
                            }
                        }
                    }
                }
            }
        }

        DownloadVideosUtil.getVideoQueueList(Constants.QUEUE_VIDEO)?.let {
            Log.d("fnaslfnsf", "Downloaded list SIZE: ${it.size}")
        }

        tempStreamVideoAdapter.notifyDataSetChanged()
    }

    override fun setUpdateVideoList(videoList: ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>) {
        tempStreamVideoAdapter.notifyDataSetChanged()
    }

    override fun isSubscribed() {
        Intent(requireActivity(), SubscriptionActivity::class.java).putExtra("home", "no")
            .apply {
                putExtra("exercise", "yes")
                startActivityForResult(this, 2)
            }
    }

    override fun isExclusive(programRedirectUrl: Any?) {
        val dialog = ExclusiveWorkoutDialog.newInstance(
            "$programRedirectUrl",
            "",
            getString(R.string.error_download_type)
        )
        dialog.show(childFragmentManager)
    }

    override fun onItemClicked(pixelType: String, position: Int, status: String) {
        videoListItem.let {
            if (pixelType.equals("1440p")) {
                downloadUrlInAccordingToPixel = it.mp4_video!!.vMpeg2K
            } else if (pixelType.equals("1080p")) {
                downloadUrlInAccordingToPixel = it.mp4_video!!.vMpeg1080p
            } else if (pixelType.equals("720p")) {
                downloadUrlInAccordingToPixel = it.mp4_video!!.vMpeg720p
            } else if (pixelType.equals("480p")) {
                downloadUrlInAccordingToPixel = it.mp4_video!!.vMpeg480p
            } else if (pixelType.equals("360p")) {
                downloadUrlInAccordingToPixel = it.mp4_video!!.vMpeg360p
            }

            Log.d("xzxxzxzxzxzxz", "Save URl: ${downloadUrlInAccordingToPixel}")
            //DownloadUtil.createDownloadLocalFile(downloadUrlInAccordingToPixel)
            addToDownload(workOutListPosition, downloadUrlInAccordingToPixel, status)
//          /storage/emulated/0/Android/data/com.doviesfitness/files/.Download/Dovies/sandeshadmin/3.078298314124986
        }
    }

    private fun addToDownload(
        workOutListPosition: Int,
        downloadUrlInAccordingToPixel: String,
        status: String
    ) {
        val holderView =
            binding.videoRv.findViewHolderForAdapterPosition(workOutListPosition) as TempStreamVideoAdapter.SteamHolder
        val videoModel = StreamDetailActivity.videoList.get(workOutListPosition)
        val modal = DownloadedVideoModal(
            workOutListPosition,
            downloadUrlInAccordingToPixel,
            StreamDetailActivity.overViewTrailerData!!.stream_workout_id,
            StreamDetailActivity.overViewTrailerData!!.stream_workout_image,
            StreamDetailActivity.overViewTrailerData!!.stream_workout_image_url,
            StreamDetailActivity.overViewTrailerData!!.stream_workout_name,
            StreamDetailActivity.overViewTrailerData!!.stream_workout_description,
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
            videoModel.order,
            videoModel.is_workout
        )
        DownloadVideosUtil.setVideoQueue(
            modal,
            Constants.QUEUE_VIDEO
        )

        StreamDetailActivity.videoList[workOutListPosition].isAddedToQueue = true
        holderView.binding.downloadIcon.setImageResource(R.drawable.download_wait)
        holderView.binding.downloadLayout.showVisibility(true)
        holderView.binding.downloadIcon.showVisibility(true)
        Log.d("nvmcnmcvnm", "Download Type Name : $status")
        if (Constants.ADD_TO_DOWNLOAD == status) {
            Handler().postDelayed({
                StartDownloadManager.beginDownload()
            }, 800)
        }
    }

    override fun onDialogDismiss() {

    }

    override fun onResume() {
        if (shouldRefreshList) {
            shouldRefreshList = false
            isCompleteDownload()
        }
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        LocalBroadcastManager.getInstance(FacebookSdk.getApplicationContext())
            .unregisterReceiver(downloadProgressReceiver)
    }

    private class SampleListLoader(
        val streamImage: String,
        val context: Context,
        val strList: ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>,
        val workoutId: String,
        val pos: Int,
        val videoList: List<VideoListItem?>?,
        var castLoaderData: ArrayList<MediaInfo>


    ) : AsyncTask<String?, Void?, PlaylistHolder?>() {

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
            intent.putExtra("workout_id", "" + overViewTrailerData!!.stream_workout_id)
            intent.putExtra("local", "no")
            intent.putExtra("trailer", "no")
            intent.putExtra("position", pos)
            intent.putExtra("castMedia", castLoaderData)
            intent.putExtra("name", overViewTrailerData!!.stream_workout_name)
            intent.putExtra("stream_image", "" + streamImage)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            IntentUtil.addToIntent(result!!.mediaItems, intent)
            //context.startActivity(intent)
            context as BaseActivity
            context.getActivity().startActivityForResult(intent, 50)
            context.getActivity().overridePendingTransition(0, 0)

        }

        @Throws(IOException::class)
        private fun readEntry(videoList: List<VideoListItem?>?): PlaylistHolder {
            val mediaList = arrayListOf<MediaItem>()
            for (i in 0..videoList?.size!! - 1) {
                var uri: Uri? = null
                var extension: String? = null
                var title: String? = null
                val mediaItem = MediaItem.Builder()
                title = "Dovies video"
                uri = Uri.parse(videoList.get(i)?.hlsVideo?.vHlsMasterPlaylist)
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
            this.mediaItems = Collections.unmodifiableList(
                java.util.ArrayList(mediaItems)
            )
        }
    }

    private fun deleteQueueListItem(
        itemPos: Int
    ) {
        var shouldStartRemainingDownload = false
        val queueVideoList = DownloadVideosUtil.getVideoQueueList(Constants.QUEUE_VIDEO)
        queueVideoList?.let {
            for (i in 0 until queueVideoList.size) {
                val url = queueVideoList[i].videoUrl.split("mp4/")
                val extensionName = url[1].split("_").toTypedArray()
                val lastIndexName = extensionName[0]
                val videoUrl = url[0] + lastIndexName + ".mp4"
                Log.d(
                    "hfdjhfjdfdsfd",
                    "$videoUrl --- ${StreamDetailActivity.videoList[itemPos].stream_video} : -- ${queueVideoList[i].workout_id} == ${overViewTrailerData!!.stream_workout_id}"
                )

                if (queueVideoList[i].workout_id == StreamDetailActivity.overViewTrailerData!!.stream_workout_id) {
                    Log.d(
                        "hfdjhfjdfdsfd",
                        "ENTER ${queueVideoList[i].stream_video_id} === ${StreamDetailActivity.videoList[itemPos].stream_video_id}"
                    )
                    if (queueVideoList[i].stream_video_id == StreamDetailActivity.videoList[itemPos].stream_video_id) {
                        queueVideoList.removeAt(i)
                        Log.d("hfdjhfjdfdsfd", "Remove")
                        Log.d(
                            "vcvcvcvcvcvcv",
                            "deleteQueueListItem: ${getVideoUrl} --- ${StreamDetailActivity.videoList[itemPos].stream_video}"
                        )
                        DownloadVideosUtil.deleteDownloadLocalFile(StreamDetailActivity.videoList[itemPos].stream_video)
                        DownloadVideosUtil.saveRemainingQueueVideosToLocal(queueVideoList)

                        if (isDownloadingCoutinue) {
                            shouldStartRemainingDownload =
                                true //taken because of isDownloadContinue flag turns true due to assigning in receiver that's why convert to local variable to perform further download video
                            DownloadVideosUtil.deleteCurrentDownloadingVideo()
                        }
                        TempAsynctask.isDownloadCancel = true
                        Log.d("vnvnmxnvmxv", "deleteQueueListItem: before ")
                        updateCancelDownloadUI(itemPos)
                        Log.d("vnvnmxnvmxv", "deleteQueueListItem: after ")
                        break
                    }
                }
            }
        }
        queueVideoList?.let {
            Log.d("vnvnmxnvmxv", " ${isDownloadingCoutinue} One ${queueVideoList.size}")
            if (queueVideoList.size > 0) {
                if (shouldStartRemainingDownload) {
                    Handler().postDelayed({
                        StartDownloadManager.beginDownload() //start download for remaining download video items
                    }, 1000)
                }
            } else {
                Doviesfitness.preferences.edit()
                    .putString(Constants.QUEUE_VIDEO, "")
                    .apply()
            }
        }
    }

    private fun deleteCurrentDownloadVideo(
        position: Int
    ) {
        val queueVideoList = DownloadVideosUtil.getVideoQueueList(Constants.QUEUE_VIDEO)
        DownloadUtil.createDownloadLocalFile(
            StreamDetailActivity.videoList.get(position).stream_video
        )?.let {
            Log.d("mlasmlasf", "deleteCurrentDownloadVideo--> Length: ${it.length()}")
            if (it.length() > 0) {
                Log.d("mlasmlasf", "deleteCurrentDownloadVideo--> isExist: ${it.exists()}")
                if (it.exists()) {
                    it.delete()
                }
            }
        }

        DownloadVideosUtil.deleteCurrentDownloadingVideo()
        updateCancelDownloadUI(position)
        queueVideoList?.let {
            if (queueVideoList.size > 0) {
                if (queueVideoList[0].workout_id.equals(overViewTrailerData!!.stream_workout_id)) {
                    Log.d("vcvcvcvcvcvcv", "DOWLOADING: THREE ${queueVideoList.size}")
                    queueVideoList.removeAt(0)
                    if (queueVideoList.size > 0) {
                        Log.d("vcvcvcvcvcvcv", "DOWLOADING: FOUR ${queueVideoList.size}")
                        Handler().postDelayed({
                            DownloadVideosUtil.saveRemainingQueueVideosToLocal(queueVideoList)
                            StartDownloadManager.beginDownload() //start download for remaining download video items
                        }, 1000)

                    } else {
                        Log.d("vcvcvcvcvcvcv", "DOWLOADING: ELSE FOUR")
                        Doviesfitness.preferences.edit()
                            .putString(Constants.QUEUE_VIDEO, "")
                            .apply()
                    }
                }
            }
            Log.d("vcvcvcvcvcvcv", "DOWLOADING: ELSE OUTER ${queueVideoList.size}")
        }
        Log.d(
            "vcvcvcvcvcvcv",
            "displayPopupWindow: LAST ${StreamDetailActivity.videoList.get(position).isAddedToQueue} "
        )
    }

    private fun updateCancelDownloadUI(position: Int) {
        val holderView =
            binding.videoRv.findViewHolderForAdapterPosition(position) as TempStreamVideoAdapter.SteamHolder

        holderView.binding.downloadLayout.showVisibility(true)
        holderView.binding.downloadIcon.showVisibility(true)
        holderView.binding.progressLayout.showVisibility(false)
        holderView.binding.loader.showVisibility(false)
        holderView.binding.downloadIcon.setImageResource(R.drawable.icon_download_new)
        StreamDetailActivity.videoList.get(position).Progress = 0
        StreamDetailActivity.videoList.get(position).isAddedToQueue = false
    }

    private fun showErrorDialog(msg: String) {
        try {
            val errorDialog = ErrorDialog.newInstance("", "Ok", msg)
            errorDialog.show(childFragmentManager)
            errorDialog.setListener(object : ErrorDialog.IsOK {
                override fun isOk() {
                    LocalBroadcastManager.getInstance(requireContext())
                        .registerReceiver(
                            downloadProgressReceiver,
                            IntentFilter(Constants.DOWNLOADING_PROGRESS)
                        )
                }
            })
            LocalBroadcastManager.getInstance(requireContext())
                .unregisterReceiver(downloadProgressReceiver)
        } catch (e: java.lang.Exception) {

            Log.d("njinjnjnjnjnj", "onReceive: 123${e.message}")
        }
    }

    /////////
    fun loadCastData() {
        loaderManager.initLoader(0, null, this)

    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<MediaInfo>> {
        return VideoItemLoader(
            getActivity(),
            StreamDetailActivity.qualitymodel?.settings?.data?.videoList,
            overViewTrailerData?.stream_workout_image_url + "medium/" + overViewTrailerData?.stream_workout_image
        )
    }

    override fun onLoadFinished(loader: Loader<List<MediaInfo>>, data: List<MediaInfo>?) {
        Log.d("loader data...", "loader data..." + data?.size + "....data..." + data.toString())
        data?.let { castLoaderData.addAll(it) }
        Log.d(
            "loader data...",
            "loader data arraylist..." + castLoaderData?.size + "....data..." + castLoaderData.toString()
        )
        loaderManager.destroyLoader(0)
    }

    override fun onLoaderReset(loader: Loader<List<MediaInfo>>) {

    }

}