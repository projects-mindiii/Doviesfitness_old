package com.doviesfitness.temp

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Rect
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.doviesfitness.Doviesfitness
import com.doviesfitness.R
import com.doviesfitness.allDialogs.ErrorDialog
import com.doviesfitness.databinding.ActivityTempDownloadStreamBinding
import com.doviesfitness.subscription.IsSubscribed
import com.doviesfitness.subscription.SubscriptionActivity
import com.doviesfitness.temp.adapter.TempDownLoadStreamWorkOutAdapter
import com.doviesfitness.temp.modal.DownloadedVideoModal
import com.doviesfitness.temp.modal.VideoCategoryModal
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.bottom_tabbar.workout_tab.dialog.FinishActivityDialog
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.Constants
import com.showTost
import kotlinx.android.synthetic.main.activity_downloads_stream.*
import kotlinx.android.synthetic.main.activity_downloads_stream.btn_explore
import kotlinx.android.synthetic.main.activity_downloads_stream.edit_icon
import kotlinx.android.synthetic.main.activity_downloads_stream.no_data_found_layout
import kotlinx.coroutines.*
import java.io.File
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class TempDownloadStreamActivity : BaseActivity(), IsSubscribed, View.OnClickListener,
        CoroutineScope, FinishActivityDialog.IsDelete {
    val job: Job by lazy {
        Job()
    }
    lateinit var thisActivityContext: Context
    var workoutId = ""
    lateinit var binding: ActivityTempDownloadStreamBinding
    var videoCategoryList = mutableListOf<VideoCategoryModal>()
    lateinit var adapter: TempDownLoadStreamWorkOutAdapter
    private var onEditButtonClickOn: Boolean = false
    var downloadVideoList: ArrayList<VideoCategoryModal>? = ArrayList<VideoCategoryModal>()
    var videoList = ArrayList<DownloadedVideoModal>()
    var categoryList = ArrayList<VideoCategoryModal>()
    lateinit var receiver: BroadcastReceiver
    private var mLastClickTime: Long = 0
    var progressVal = 0
    var isSubscribe = ""
    var itemPosition = -1
    lateinit var errorDialog: ErrorDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideNavigationBar()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_temp_download_stream)
        binding.ivBack.setOnClickListener(this)
        binding.editIcon.setOnClickListener(this)
        binding.btnExplore.setOnClickListener(this)
        thisActivityContext = this
        if (intent.hasExtra("isSubscribe")) {
            isSubscribe = intent.getStringExtra("isSubscribe")!!
        }

        initialization()
    }

    var shouldRefreshCategoryVideoList = false
    private fun initialization() {
        val layoutManager = GridLayoutManager(this, 3)
        binding.workoutRv.layoutManager = layoutManager
        val spacing = Constant.deviceSize(this) / 2
        //binding.workoutRv.setPadding(spacing, spacing, spacing, spacing)
        binding.workoutRv.setPadding(10, 30, 10, 10)
        binding.workoutRv.clipToPadding = false
        binding.workoutRv.clipChildren = false
        binding.workoutRv.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
            ) {
                outRect.set(spacing, spacing, spacing, spacing)
            }
        })
        adapter =
                object :
                        TempDownLoadStreamWorkOutAdapter(videoCategoryList, onEditButtonClickOn, this) {
                    override fun onClickWorkOut(status: String, pos: Int) {
                        Log.d("fnaslfas", "onClickWorkOut: ")
                        Log.d("fnaslfssssssssssnasf", "onClickWorkOut: ${downloadVideoList!!.size}")
                        Intent(
                                this@TempDownloadStreamActivity,
                                TempDownWorkOutsVideoActvity::class.java
                        ).apply {
                            putExtra(
                                    Constants.LOCAL_STREAM_VIDEO_DATA_MODAL,
                                    downloadVideoList!!.get(pos)
                            )
                            startActivityForResult(this, Constants.ACTIVITY_REQUEST_CODE)
                        }
                    }

                    override fun onDeleteWorkOut(pos: Int) {
                        itemPosition = pos
                        launchDeleteDialog()
                    }
                }
        binding.workoutRv.adapter = adapter
        showProgressReceiver()
        LocalBroadcastManager.getInstance(this).registerReceiver(
                receiver,
                IntentFilter(Constants.DOWNLOADING_PROGRESS)
        )

        launch {
            setData()
        }

    }

    private fun launchDeleteDialog() {
        val dialog = FinishActivityDialog.newInstance(
                "Delete",
                "No",
                "Are you sure, you want to delete this video?"
        )
        dialog.setListener(this)
        dialog.show(supportFragmentManager)
    }

    override fun isSubscribed() {
        Intent(getActivity(), SubscriptionActivity::class.java).putExtra("home", "no").apply {
            this.putExtra("exercise", "yes")
            startActivityForResult(this, 2)
        }
    }

    suspend fun setData() {
        coroutineScope {
            launch {

                downloadVideoList =
                        DownloadVideosUtil.getDownloadedVideo(Constants.DOWNLOADED_VIDEO)
//        Log.d("fnaslfssssssssssnasf", "DownloladStreamTempActivity: ${downloadVideoList!!.size}")
//
//        downloadVideoList?.also { downloadedList ->
//            if (downloadedList.isNotEmpty()) {
//                for (i in 0 until downloadedList.size) {
//                    if (downloadedList[i].download_list.isNotEmpty()) {
//                        var count = 0
//                        while (count < downloadedList[i].download_list.size) {
//                            downloadedList[i].download_list[count].isAddedQueue = false
//                            val isdownloaded = downloadedList[i].download_list[count].downLoadUrl
//                            val DFile = File(isdownloaded)
//                            Log.d(
//                                "nfkasnfkas",
//                                "${downloadedList[i].stream_workout_id} ------- ${DFile.exists()} setData: ${isdownloaded}"
//                            )
//                            if (!DFile.exists()) {
//                                downloadedList.get(i).download_list.removeAt(count)
//                            } else
//                                count++
//                        }
//                    }
//                }
//            }
//        }

                Log.d("nvmcnmcvnm", "QUEUE LIST SIZE  OUTER--> : $")
                DownloadVideosUtil.getVideoQueueList(Constants.QUEUE_VIDEO)?.let { queueList ->
                    Log.d("nvmcnmcvnm", "QUEUE LIST SIZE --> : ${queueList.size}")
                    if (queueList.isNotEmpty()) {
                        for (i in 0 until queueList.size) {
                            if (downloadVideoList != null) {
                                var isVideoAvailable = false
                                for (j in 0 until downloadVideoList!!.size) {
                                    if (queueList[i].workout_id.equals(downloadVideoList!![j].stream_workout_id)) {
                                        Log.d("nvmcnmcvnm", "MATCHED: ONE ${downloadVideoList!![j].download_list.size}")
                                        downloadVideoList!![j].download_list.add(queueList[i])
                                        Log.d("nvmcnmcvnm", "MATCHED: TWO ${downloadVideoList!![j].download_list.size}")
                                        isVideoAvailable = true
                                        break
                                    }
                                }

                                if(!isVideoAvailable){
                                    Log.d("nvmcnmcvnm", "Not Matched:  ${downloadVideoList!!.size}")
                                    val getQueueData = queueList[i]
                                    val getQueueList = arrayListOf<DownloadedVideoModal>()
                                    getQueueList.add(getQueueData)
                                    val categoryVideoData = VideoCategoryModal(
                                            getQueueData.stream_workout_description,
                                            getQueueData.workout_id,
                                            getQueueData.stream_workout_image,
                                            getQueueData.stream_workout_image_url,
                                            getQueueData.stream_workout_name, getQueueList
                                    )
                                    downloadVideoList!!.add(categoryVideoData)
                                }

                            }else{
                                downloadVideoList = arrayListOf()
                                Log.d("nvmcnmcvnm", "Empty  ${downloadVideoList!!.size}")
                                val getQueueData = queueList[i]
                                val getQueueList = arrayListOf<DownloadedVideoModal>()
                                getQueueList.add(getQueueData)
                                val categoryVideoData = VideoCategoryModal(
                                        getQueueData.stream_workout_description,
                                        getQueueData.workout_id,
                                        getQueueData.stream_workout_image,
                                        getQueueData.stream_workout_image_url,
                                        getQueueData.stream_workout_name, getQueueList
                                )
                                downloadVideoList!!.add(categoryVideoData)
                            }
                        }
                    }
                }

                if (!downloadVideoList.isNullOrEmpty()) {
                    Log.d("bcvnbcvnbcnv", "setData--: ${downloadVideoList!!.size}")
                    var count = 0
                    while (count < downloadVideoList!!.size) {
                        if (!downloadVideoList!![count].download_list.isNullOrEmpty()) {
                            count++
                        } else {
                            downloadVideoList!!.removeAt(count)
                        }
                    }
                }
            }.join()

            launch(Dispatchers.Main){
                downloadVideoList?.let {
                    adapter.refreshList(downloadVideoList!!)
                }

                uiVisibility()
            }
        }
        Log.d("fanfklnlas", "setDasssta: ${downloadVideoList}")
    }

    private fun uiVisibility() {
        if(downloadVideoList!=null){
            if(!downloadVideoList.isNullOrEmpty()){
                edit_icon.visibility = View.VISIBLE
                no_data_found_layout.visibility = View.GONE
                btn_explore.visibility = View.GONE
                workout_rv.visibility = View.VISIBLE
            }else{
                edit_icon.visibility = View.GONE
                no_data_found_layout.visibility = View.VISIBLE
                btn_explore.visibility = View.VISIBLE
                workout_rv.visibility = View.GONE
            }
        }else{
            edit_icon.visibility = View.GONE
            no_data_found_layout.visibility = View.VISIBLE
            btn_explore.visibility = View.VISIBLE
            workout_rv.visibility = View.GONE
        }
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                onBackPressed()
            }
            R.id.btn_explore -> {
                onBackPressed()
            }
            R.id.edit_icon -> {
                lastClick()
                if (onEditButtonClickOn) {
                    onEditButtonClickOn = false
                    edit_icon.setImageResource(R.drawable.ic_edit_workout)

                } else {
                    onEditButtonClickOn = true
                    if (downloadVideoList!!.size > 0) {
                        edit_icon.setImageResource(R.drawable.circle_right_click)
                    }
                }
                adapter.showDeleteIcon(onEditButtonClickOn)

            }
        }
    }

    fun lastClick() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return
        } else {
            mLastClickTime = SystemClock.elapsedRealtime()
        }
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO


    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                receiver)
    }

    override fun isDelete() { //delete video category
        performDeleteOperation()
    }

    private fun performDeleteOperation() {
        downloadVideoList?.let {
            if (downloadVideoList!!.isNotEmpty()) {
//                if (workout_id.equals(downloadedList.get(deletePosition).stream_workout_id)&&progressVal<100) {
//                    Toast.makeText(this, "Can not delete downloading workout", Toast.LENGTH_SHORT)
//                        .show()
//                }


                if (workoutId.equals(downloadVideoList!![itemPosition].stream_workout_id) && progressVal < 100) {
                    showTost("Can not delete downloading workout")
                } else {
                    val queueList =
                            DownloadVideosUtil.getVideoQueueList(Constants.QUEUE_VIDEO)
                    if (downloadVideoList!![itemPosition].download_list != null && downloadVideoList!![itemPosition].download_list.size > 0) {
                        Log.d("nfaksfnkas", "performDeleteOperation: ${downloadVideoList!!.size}")
                        for (i in 0 until downloadVideoList!![itemPosition].download_list.size) {
                            val availableVideo =
                                    downloadVideoList!![itemPosition].download_list[i]
                            val avialableVideoFile = File(availableVideo.downLoadUrl)
                            if (avialableVideoFile.exists()) {
                                avialableVideoFile.delete()
                            }
                            queueList?.let {
                                if (queueList.isNotEmpty()) {
                                    var count = 0
                                    while (count < queueList.size) {
                                        if (queueList[count].stream_video_id.equals(
                                                        downloadVideoList!!.get(
                                                                itemPosition
                                                        ).download_list[count].stream_video_id
                                                )
                                        ) {
                                            queueList.removeAt(count)
                                        } else {
                                            count++
                                        }
                                    }
                                }
                            }
                        }
                    }


                    downloadVideoList!!.removeAt(itemPosition)

                    if (downloadVideoList != null && downloadVideoList!!.size > 0)
                        DownloadVideosUtil.setDownloadedData(downloadVideoList)
                    else
                        Doviesfitness.preferences.edit()
                                .putString(Constants.DOWNLOADED_VIDEO, "").apply()

                    if (queueList != null && queueList.size > 0)
                        DownloadVideosUtil.saveRemainingQueueVideosToLocal(queueList)
                    else
                        Doviesfitness.preferences.edit()
                                .putString(Constants.QUEUE_VIDEO, "").apply()


                    shouldRefreshCategoryVideoList = true

                    refreshList()
                }
            }
        }


    }

    private fun showProgressReceiver() {
        receiver = object : BroadcastReceiver() {
            override fun onReceive(p0: Context, intent: Intent) {

                progressVal = intent.getIntExtra(Constants.PROGRESS, 0)
                val itemPosition = intent.getIntExtra(Constants.ITEM_POSITION, -1)
                workoutId = intent.getStringExtra(Constants.WORKOUT_ID)!!
                val downloadStatus = intent.getIntExtra(Constants.DOWNLOAD_STATUS,-1)
                Log.d("fbaskfbkjfa", "onReceive: Fist Screen  ->>> ${downloadStatus}")
                val msg = if(downloadStatus == Constants.DOWNLOAD_UNKNOWN_ERROR) getString(R.string.something_wrong) else "Insufficient Storage Space"
                if(downloadStatus == Constants.DOWNLOAD_UNKNOWN_ERROR || downloadStatus == Constants.ERROR_INSUFFICIENT_SPACE_CODE){
                    try {
                        errorDialog = ErrorDialog.newInstance("", "Ok", msg)
                        errorDialog.show(supportFragmentManager)
                        errorDialog.setListener(object : ErrorDialog.IsOK{
                            override fun isOk() {
                                shouldRefreshCategoryVideoList = true
                                refreshList()
                            }
                        })

                        LocalBroadcastManager.getInstance(thisActivityContext).unregisterReceiver(receiver)
                    }catch (e: Exception){
                        Log.d("njinjnjnjnjnj", "onReceive: ${e.message}")
                    }
                }

//                workoutId = intent.getStringExtra(Constants.WORKOUT_ID)
//
//
//                if (intent.hasExtra(Constants.PROGRESS)) {
//                    val progressstr = intent.getStringExtra(Constants.PROGRESS)
//                    progressVal = progressstr.toInt()
//                    Log.d(
//                        "fbaskfbkjfa",
//                        "onReceive: DownloadVideo Started Receiver Progress -> ${progressVal}"
//                    )
//                } else if (intent.hasExtra(Constants.WORKOUT_ID)) {
//                    if (intent.getStringExtra(Constants.CANCEL) != null && !intent.getStringExtra(
//                            Constants.CANCEL
//                        ).isEmpty()
//                    ) {
//                        progressVal = 100
//                    }
//                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.ACTIVITY_REQUEST_CODE) {
                data?.let {
                    shouldRefreshCategoryVideoList =
                            it.getBooleanExtra(Constants.SHOULD_REFRESH_LIST, false)
                    Log.d("fanfklnfsklas", "onActivityResult: ${shouldRefreshCategoryVideoList}")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }

    private fun refreshList(){
        if(::errorDialog.isInitialized && errorDialog.dialog != null){
            if(errorDialog.dialog!!.isShowing){
                errorDialog.dialog!!.dismiss()
            }
        }
        if (shouldRefreshCategoryVideoList) {
            TempSteamVideoFragmentNew.shouldRefreshList = true
            downloadVideoList?.let {
                if (downloadVideoList!!.size > 0) {
                    downloadVideoList!!.clear()
                }
            }
            launch {
                setData()
            }
        }
    }


}