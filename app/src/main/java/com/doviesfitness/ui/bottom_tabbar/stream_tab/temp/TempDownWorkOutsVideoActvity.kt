package com.doviesfitness.temp

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.doviesfitness.Doviesfitness
import com.doviesfitness.R
import com.doviesfitness.allDialogs.ErrorDialog
import com.doviesfitness.chromecast.browser.VideoItemLoader
import com.doviesfitness.databinding.ActivityTempDownWorkOutsVideoActvityBinding
import com.doviesfitness.temp.adapter.TempDownLoadStreamWorkOutVideoAdapter
import com.doviesfitness.temp.modal.DownloadedVideoModal
import com.doviesfitness.temp.modal.VideoCategoryModal
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.bottom_tabbar.stream_tab.activities.StreamDownloadedVideoActivity
import com.doviesfitness.ui.bottom_tabbar.stream_tab.download_background.DownloadedModal
import com.doviesfitness.ui.bottom_tabbar.workout_tab.dialog.FinishActivityDialog
import com.doviesfitness.ui.spotify.SplashActivity1
import com.doviesfitness.utils.Constants
import com.google.android.gms.cast.MediaInfo
import com.showTost
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationResponse
import kotlinx.android.synthetic.main.activity_down_wor_kouts_video_actvity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext

class TempDownWorkOutsVideoActvity : BaseActivity(), FinishActivityDialog.IsDelete, CoroutineScope,
        View.OnClickListener, LoaderManager.LoaderCallbacks<List<MediaInfo>> {
    private val job by lazy {
        Job()
    }

    lateinit var thisActivityContext: Context
    var flag = false
    //lateinit var downloadProgressReceiver: BroadcastReceiver
    lateinit var binding: ActivityTempDownWorkOutsVideoActvityBinding
    lateinit var videoCategoryModal: VideoCategoryModal
    lateinit var adapter: TempDownLoadStreamWorkOutVideoAdapter
    var videoList = mutableListOf<DownloadedVideoModal>()
    var itemPosition = 0
    var deletePos = -1
    lateinit var receiver: BroadcastReceiver
    var getItemPositionByIds = -1
    var isCurrentDownloadCanceled = false
    var isBroadcastReceiverStated = false
    var  castLoaderData= ArrayList<MediaInfo>()
    var streamList = ArrayList<DownloadedModal.ProgressModal>()
    var position = 0
    var localStreamList = ArrayList<DownloadedModal.ProgressModal>()
    var streamImage = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
                this,
                R.layout.activity_temp_down_work_outs_video_actvity
        )

        thisActivityContext = this
        intent.getSerializableExtra(Constants.LOCAL_STREAM_VIDEO_DATA_MODAL)?.let {
            videoCategoryModal =
                    it as VideoCategoryModal
            title_name.text = videoCategoryModal.stream_workout_name
            Log.d(
                    "fbnalfssssnklsfas",
                    " onCreate: ===>> ${videoCategoryModal.download_list.size}  ===>>>> ${videoCategoryModal}"
            )
            videoList.addAll(videoCategoryModal.download_list)
        }
        isBroadcastReceiverStated = true
        getDownloadProgressReceiver()
        initialization()
    }

    private fun initialization() {
//        mapList = HashMap()
//        iv_back.setOnClickListener(this)
//        workout_vdo_rv.setLayoutManager(LinearLayoutManager(this))
//        if (intent.getSerializableExtra("localStreamVideoDataModal") != null) {
//            localStreamVideoDataModal = intent.getSerializableExtra("localStreamVideoDataModal") as DownloadedModal
//            title_name.text = localStreamVideoDataModal.stream_workout_name
//        }
//
//        localStreamList = ArrayList<DownloadedModal.ProgressModal>()
//        mainTempList = ArrayList<DownloadedModal.ProgressModal>()
//        localStreamList.addAll(localStreamVideoDataModal.download_list)
//
//        for (i in 0..localStreamList.size-1){
//            var hgfkjd  = localStreamList.get(i)
//            mapList[localStreamList.get(i).stream_video_id] = hgfkjd
//        }
//        val values: Collection<DownloadedModal.ProgressModal> = mapList.values
//        mainTempList.addAll(values)
//
//
//        localStreamList.clear()
//        localStreamList.addAll(mainTempList)
//
//        if (localStreamList.size > 0) {
//            txt_no_data_found.visibility = View.GONE
//            workout_vdo_rv.visibility = View.VISIBLE
//
//            Collections.sort(localStreamList, object : Comparator<DownloadedModal.ProgressModal> {
//                override fun compare(
//                    lhs: DownloadedModal.ProgressModal,
//                    rhs: DownloadedModal.ProgressModal
//                ): Int {
//                    return lhs.order.compareTo(rhs.order)
//                }
//            })
//
//            val hashSet = LinkedHashSet(localStreamList)
//            localStreamList.clear()
//            localStreamList = ArrayList(hashSet)
//
//            adapter = DownLoadStreamWorkOutVideoAdapter(getActivity(), localStreamList, this)
//            workout_vdo_rv.adapter = adapter
//        } else {
//            txt_no_data_found.visibility = View.VISIBLE
//            workout_vdo_rv.visibility = View.GONE
//        }
        iv_back.setOnClickListener(this)
        binding.workoutVdoRv.layoutManager = LinearLayoutManager(this)
        adapter = object : TempDownLoadStreamWorkOutVideoAdapter(videoList) {
            override fun onWorkOutVideoClick(itemPos: Int) {
                performClickOnVideo(itemPos)
            }

            override fun deleteVideo(itemPos: Int) {
                launchDialog(itemPos)
            }
        }
        binding.workoutVdoRv.adapter = adapter
        showDownloadProgressReceiver()
        LocalBroadcastManager.getInstance(this).registerReceiver(
                receiver,
                IntentFilter(Constants.DOWNLOADING_PROGRESS)
        )
    }

    private fun launchDialog(itemPos: Int) {
        itemPosition = itemPos
        val dialog = FinishActivityDialog.newInstance(
                "Delete",
                "No",
                "Are you sure, you want to delete this video?"
        )
        dialog.setListener(this) //after clicking on yes button will perform isDelete override method
        dialog.show(supportFragmentManager)
    }

    private fun getDownloadProgressReceiver() {
//        downloadProgressReceiver = object : BroadcastReceiver() {
//            override fun onReceive(p0: Context?, p1: Intent?) {
//                val downloadStatus = intent.getIntExtra(Constants.DOWNLOAD_STATUS, -1)
//                val progress = intent.getIntExtra(Constants.PROGRESS, 0)
//                val itemPosition = intent.getIntExtra(Constants.ITEM_POSITION, -1)
//                val workoutId = intent.getStringExtra(Constants.WORKOUT_ID)
//
//                when (downloadStatus) {
//                    DownloadManager.STATUS_FAILED -> {
//
//                    }
//                    DownloadManager.STATUS_PENDING -> {
//
//                    }
//                    DownloadManager.STATUS_RUNNING -> {
//                        if (itemPosition != -1) {
//
//                            Log.d("bxmncbvnxcmbv", "onReceive: " + progress)
//                        }
//                    }
//                    DownloadManager.STATUS_SUCCESSFUL -> {
//                        if (itemPosition != -1) {
//                            val holderView = binding.workoutVdoRv.findViewHolderForAdapterPosition(loaderPos) as DownLoadStreamWorkOutVideoAdapter.MyViewHolder
//
////                            if (loaderPos != -1) {
////                                if (workout_vdo_rv.findViewHolderForAdapterPosition(loaderPos) != null) {
////                                    val holder =
////                                        workout_vdo_rv.findViewHolderForAdapterPosition(loaderPos) as DownLoadStreamWorkOutVideoAdapter.MyViewHolder
////                                    holder.loader.visibility = View.VISIBLE
////                                    holder.downloadIcon.visibility = View.GONE
////                                    holder.loader.max = 100
////                                    holder.loader.progress = progress
////                                    if (progress == 100) {
////                                        videoList[loaderPos].isAddedQueue = false
////                                        // holder.episode.text = "Downloaded"
////                                        deletePos = -1
////                                        holder.loader.visibility = View.GONE
////                                        holder.downloadIcon.setImageResource(R.drawable.complete_downloaded)
////                                        Log.d("Complete_download", "Complete download..." + progress)
////                                        holder.downloadIcon.visibility = View.VISIBLE
////                                    } else {
////
////                                    }
////                                }
////                            }
//
//
//                            Log.d("bxmncbvnxcmbv", "onReceive STATUS_SUCCESSFUL: " + progress)
//                        }
//                    }
//                }
//            }
//        }
    }

    fun performClickOnVideo(itemPosition: Int) {
        this.itemPosition=itemPosition
        launch {
            val downloadedLocalVideoList =
                    DownloadVideosUtil.getDownloadedVideo(Constants.DOWNLOADED_VIDEO)
            if (!videoList[itemPosition].isAddedQueue) { //video will be play after downloading
                if (downloadedLocalVideoList != null) {
                    if (downloadedLocalVideoList.size > 0) {
                        for (i in 0 until downloadedLocalVideoList.size) {
                            if (downloadedLocalVideoList[i].stream_workout_id.equals(videoList[itemPosition].workout_id)) {
                                if (downloadedLocalVideoList[i].download_list != null && downloadedLocalVideoList[i].download_list.size > 0) {
                                    for (j in 0..downloadedLocalVideoList.get(i).download_list.size - 1) {
                                        if (downloadedLocalVideoList.get(i).download_list.get(j).stream_video_id.equals(
                                                        videoList[itemPosition].stream_video_id
                                                )
                                        ) {
                                            videoList[itemPosition].downLoadUrl = downloadedLocalVideoList.get(
                                                    i
                                            ).download_list.get(j).downLoadUrl
                                            break
                                        }
                                    }
                                }
                                break
                            }
                        }
                    }
                }

//                 localStreamList = ArrayList<DownloadedModal.ProgressModal>()
                if(localStreamList.size>0)
                    localStreamList.clear()

                for (i in 0 until videoList.size) {
                    var data = videoList[i]
                    Log.d("videoUrl", "videoUrl..." + data.downLoadUrl)
                    var pd = DownloadedModal.ProgressModal(
                            data.position,
                            data.videoUrl,
                            data.workout_id,
                            data.stream_workout_image,
                            data.stream_workout_image_url,
                            data.stream_workout_name,
                            data.stream_workout_description,
                            data.isAddedQueue,
                            data.stream_video_description,
                            data.stream_video_id,
                            data.video_duration,
                            data.stream_video_image,
                            data.stream_video_image_url,
                            data.stream_video_name,
                            data.stream_video_subtitle,
                            data.progress,
                            data.maxProgress,
                            data.seekTo,
                            data.isPlaying,
                            data.isComplete,
                            data.isRestTime,
                            data.downLoadUrl,
                            data.timeStempUrl,
                            data.order,
                            data.is_workout
                    )
                    localStreamList.add(pd)
                }
                if(streamList.size>0) streamList.clear()
                streamList = ArrayList<DownloadedModal.ProgressModal>()
                position = itemPosition
                streamList.add(localStreamList[position])
                streamImage=streamList.get(0).stream_workout_image_url+streamList.get(0).stream_workout_image
                Log.e("CreateLoader call-->", "listadd" + localStreamList[position].toString())
                Log.e("CreateLoader call-->", "streamImage" + streamImage)
                CoroutineScope(Dispatchers.Main).launch {
                    loadCastData()

                }

            }
        }
    }

    fun navigateToStreamVideoScreen(localStreamList: ArrayList<DownloadedModal.ProgressModal>) {

        Log.e("CreateLoader call-->", "Intent Call->" + castLoaderData.size)
        val intent = Intent(this, StreamDownloadedVideoActivity::class.java)
        intent.putExtra("localStreamList", localStreamList)
        intent.putExtra("workout_id", "" + videoCategoryModal.stream_workout_id)
        intent.putExtra("local", "yes")
        intent.putExtra("position", itemPosition)
        intent.putExtra("castMedia", castLoaderData)
        intent.putExtra("streamImage", "" + streamImage)
//        startActivity(intent)
        startActivityForResult(intent, 50)
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
        job.cancel()
    }

    override fun isDelete() {//delete video item
        val downloadedVideoList = DownloadVideosUtil.getDownloadedVideo(Constants.DOWNLOADED_VIDEO)
        if (deletePos == itemPosition) {
            showTost("Can not delete downloading video")
        } else {
            if (downloadedVideoList != null) {

                if (downloadedVideoList != null && downloadedVideoList.size > 0) {
                    for (i in 0..downloadedVideoList.size - 1) {
                        if (downloadedVideoList.get(i).stream_workout_id.equals(videoCategoryModal.stream_workout_id)) {
                            for (j in 0..downloadedVideoList.get(i).download_list.size - 1) {
                                if (downloadedVideoList.get(i).download_list.get(j).stream_video_id.equals(
                                                videoList[itemPosition].stream_video_id
                                        )
                                ) {
                                    val pItem = downloadedVideoList.get(i).download_list.get(j)
                                    DownloadVideosUtil.deleteDownloadLocalFile(pItem.downLoadUrl)
                                    DownloadVideosUtil.deleteFromDownload(
                                            i,
                                            videoCategoryModal.stream_workout_id
                                    )
                                    Log.d(
                                            "fanfklnfsklas",
                                            " Before Delete -> ${downloadedVideoList.get(i).download_list.size}"
                                    )
                                    downloadedVideoList.get(i).download_list.removeAt(j)
                                    Log.d(
                                            "fanfklnfsklas",
                                            "After Delete-> ${downloadedVideoList.get(i).download_list.size}"
                                    )
                                    videoList.removeAt(itemPosition)
                                    adapter.notifyDataSetChanged()
                                    if (downloadedVideoList.get(i).download_list.size != null && downloadedVideoList.get(
                                                    i
                                            ).download_list.size > 0
                                    ) {
                                        Log.d("fanfklnfsklas", "OUTER ")
                                        DownloadVideosUtil.setDownloadedData(downloadedVideoList)
                                    } else {
                                        Log.d("fanfklnfsklas", "EMPTY-> ")
                                        Doviesfitness.preferences.edit()
                                                .putString(Constants.DOWNLOADED_VIDEO, "")
                                                .apply()
                                    }
                                    break
                                }
                            }
                            break
                        }
                    }
                }

            }

            if (videoList.size > 0) {
                txt_no_data_found.visibility = View.GONE
                workout_vdo_rv.visibility = View.VISIBLE

            } else {
                txt_no_data_found.visibility = View.VISIBLE
                workout_vdo_rv.visibility = View.GONE
            }
        }

        DownloadVideosUtil.getDownloadedVideo(Constants.DOWNLOADED_VIDEO)?.let {
            Log.d("fanfklnfsklas", "isDelete:  getDownloadedVideo -> ${it.size}")
        }

        sendIntentToPreviousActivity(false)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        // Check if result comes from the correct activity
        if (requestCode == 50) {
            finish()


        }
    }
    private fun showDownloadProgressReceiver() {
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                intent?.let {

                    val streamVideoId = intent.getStringExtra(Constants.STREAM_VIDEO_ID)
                    val downloadStatus = intent.getIntExtra(Constants.DOWNLOAD_STATUS, -1)
                    Log.d("cnakcnkansc", "onReceive: downloadedddd $downloadStatus")

                    val msg = if(downloadStatus == Constants.DOWNLOAD_UNKNOWN_ERROR) getString(R.string.something_wrong) else "Insufficient Storage Space"
                    if(downloadStatus == Constants.DOWNLOAD_UNKNOWN_ERROR || downloadStatus == Constants.ERROR_INSUFFICIENT_SPACE_CODE){
                        val s = ErrorDialog.newInstance("", "Ok", msg)
                        s.show(supportFragmentManager)
                        s.setListener(object : ErrorDialog.IsOK {
                            override fun isOk() {
                                Log.d("cnakcnkansc", "onReceive: CLICKED ON  $downloadStatus")
                                sendIntentToPreviousActivity(true)
                            }
                        })

                        LocalBroadcastManager.getInstance(thisActivityContext).unregisterReceiver(
                                receiver
                        )
                    }


                    val progress = intent.getIntExtra(Constants.PROGRESS, 0)
                    val itemPosition = intent.getIntExtra(Constants.ITEM_POSITION, -1)
                    val workoutId = intent.getStringExtra(Constants.WORKOUT_ID)

                    Log.d("vcvccvcvcvcvcvc", "onReceive: OUTER ${progress}")
                    var loaderPos = -1
                    if (workoutId != null && workoutId.equals(videoCategoryModal.stream_workout_id)) {
                        for (i in 0..videoList.size - 1) {
                            val videoId = videoList[i].stream_video_id
                            if (videoId == streamVideoId) {
                                Log.d(
                                        "vxcvxcvxcvx",
                                        "FIST TIME:--$getItemPositionByIds --- $workoutId == ${videoCategoryModal.stream_workout_id} -- ${videoList.size}"
                                )
                                loaderPos = i
                                deletePos = i
                                getItemPositionByIds = i
                                break
                            }
                        }
                    }

                    if (loaderPos != -1) {
                        if (workout_vdo_rv.findViewHolderForAdapterPosition(loaderPos) != null) {
                            val holder =
                                    workout_vdo_rv.findViewHolderForAdapterPosition(
                                            loaderPos
                                    ) as TempDownLoadStreamWorkOutVideoAdapter.VideoHolder
                            holder.binding.loader.visibility = View.VISIBLE
                            holder.binding.downloadIcon.visibility = View.GONE
                            holder.binding.loader.max = 100
                            holder.binding.loader.progress = progress
                            Log.d("vcvccvcvcvcvcvc", "onReceive: INNER ${progress}")

                            Log.d("vcvccvcvcvcvcvc", "onReceive: ${progress}")
                            if (progress == 100) {
                                videoList[loaderPos].isAddedQueue = false
                                // holder.episode.text = "Downloaded"
                                Log.d("vcvccvcvcvcvcvc", "onReceive: SUCCESS ${progress}")
                                deletePos = -1
                                holder.binding.loader.visibility = View.GONE
                                holder.binding.downloadIcon.setImageResource(R.drawable.complete_downloaded)
                                Log.d(
                                        "Complete_download",
                                        "Complete download..." + progress
                                )
                                holder.binding.downloadIcon.visibility = View.VISIBLE
                            } else {

                            }
                        }
                    }

                }
            }
        }
    }




    fun sendIntentToPreviousActivity(shouldFinishActivity: Boolean) {
        Log.d("fanfklnfsklas", "sendIntent: ")
        Intent().apply {
            putExtra(Constants.SHOULD_REFRESH_LIST, true)
            setResult(Activity.RESULT_OK, this)
        }
        if(shouldFinishActivity){
            finish()
        }
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO


    override fun onClick(p0: View?) {
        if (R.id.iv_back == p0?.id) {
            onBackPressed()
        }
    }


//    private fun getItemPosition() = return{
//        for(i in 0 until videoList.size){
//
//        }
//    }


    private fun updateItemView(itemPosition: Int, progress: Int, isAddedInQueue: Boolean) {
        val holderView =
                binding.workoutVdoRv.findViewHolderForAdapterPosition(itemPosition) as TempDownLoadStreamWorkOutVideoAdapter.VideoHolder

        if (isAddedInQueue) {
            holderView.binding.downloadIcon.visibility = View.VISIBLE
            holderView.binding.downloadIcon.setImageDrawable(resources.getDrawable(R.drawable.download_wait))
        } else {

            holderView.binding.downloadIcon.visibility = View.VISIBLE
            holderView.binding.downloadIcon.setImageDrawable(resources.getDrawable(R.drawable.icon_download_new))
        }

        if (progress != 0 && progress != 100) {
            holderView.binding.loader.visibility = View.VISIBLE
            holderView.binding.downloadIcon.visibility = View.GONE
            holderView.binding.loader.max = 100
            holderView.binding.loader.progress = progress
        } else {
            holderView.binding.loader.visibility = View.GONE
            holderView.binding.downloadIcon.visibility = View.VISIBLE
            holderView.binding.downloadIcon.setImageDrawable(resources.getDrawable(R.drawable.download_wait))
            if (progress == 100) {
                holderView.binding.downloadIcon.visibility = View.VISIBLE
                holderView.binding.downloadIcon.setImageResource(R.drawable.complete_downloaded)
            }
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<MediaInfo>> {
        Log.e("CreateLoader Call-->", "Start" + " " + streamList.size)

        return VideoItemLoader(
                getActivity(), streamList[0],
                streamList[0].stream_workout_image_url+""+streamList[0].stream_workout_image
        )
        Log.e("CreateLoader Call-->", "image" + " " + streamList[0].stream_workout_image_url+""+streamList[0].stream_workout_image)

    }

    override fun onLoadFinished(loader: Loader<List<MediaInfo>>, data: List<MediaInfo>?) {

        if(castLoaderData.size>0)
        {
            castLoaderData.clear()
        }

        data?.let { getData->
            castLoaderData.addAll(getData)
        }
        Log.e("CreateLoader Call-->", "Finish" + " " + castLoaderData.size + " " + data)
        navigateToStreamVideoScreen(localStreamList)
    }

    override fun onLoaderReset(loader: Loader<List<MediaInfo>>) {
    }
    fun loadCastData(){
        supportLoaderManager.initLoader(0, null, this)

    }
}