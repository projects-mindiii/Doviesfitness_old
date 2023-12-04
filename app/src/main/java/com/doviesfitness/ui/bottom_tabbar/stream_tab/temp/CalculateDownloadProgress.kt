package com.doviesfitness.temp

import android.app.DownloadManager
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.doviesfitness.Doviesfitness
import com.doviesfitness.Doviesfitness.Companion.instance
import com.doviesfitness.temp.modal.DownloadedVideoModal
import com.doviesfitness.temp.modal.VideoCategoryModal
import com.doviesfitness.ui.bottom_tabbar.stream_tab.activities.StreamDetailActivity
import com.doviesfitness.utils.Constants
import com.facebook.FacebookSdk
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList


/**Created by Yashank Rathore on 17,December,2020 yashank.mindiii@gmail.com **/

class CalculateDownloadProgress(
    val downloadId: Long,
    val adapterItemPosition: Int,
    val path: String
) : Thread() {
    companion object{

    }
    var finishDownload = false
    var isDownloadCancel = false
    var progress = 0
    private lateinit var cursor: Cursor
    var isRunningCalled = false

    override fun run() {
        super.run()
        while (!finishDownload && !isDownloadCancel) {
            val q = DownloadManager.Query()
            val ids =
                LongArray(DownloadVideosUtil.dmRefIdList.size)
            val idsArrList = ArrayList<Long>()
            var id = 0
            for (i in DownloadVideosUtil.dmRefIdList.indices) {
                ids[id++] =
                    DownloadVideosUtil.dmRefIdList[i]
                idsArrList.add(
                    DownloadVideosUtil.dmRefIdList[i]
                )
            }
            q.setFilterById(*ids)
            if(DownloadVideosUtil.GetDownloadManger.getInstance() != null && !isDownloadCancel) {

                cursor = DownloadVideosUtil.GetDownloadManger.getInstance()!!.query(q)


                if (cursor.moveToFirst()) {
                    val status =
                        cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                    val columnReason =
                        cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_REASON))
                    Log.d("cnxmkscnmxncmx", "run: ${columnReason}")
                    if(columnReason == DownloadManager.ERROR_INSUFFICIENT_SPACE){
                        Log.d("cnxmkscnmxncmx", "ERROR_INSUFFICIENT_SPACE: ${columnReason} -- ")
                        sendDownloadProgressToDownloadedActivity(Constants.ERROR_INSUFFICIENT_SPACE_CODE, 0)
                        cursor.close()
                        return
                    }


                    when (status) {
                        DownloadManager.STATUS_FAILED -> {
                            finishDownload = true
                            //setDownloadRequiredDataToBroadcast(status, progress)
                            sendDownloadProgressToDownloadedActivity(status, progress)
                        }
                        DownloadManager.STATUS_PAUSED -> {
                            //setDownloadRequiredDataToBroadcast(status, progress)
                            sendDownloadProgressToDownloadedActivity(status, progress)
                        }
                        DownloadManager.STATUS_PENDING -> {
                            //setDownloadRequiredDataToBroadcast(status, progress)
                            sendDownloadProgressToDownloadedActivity(status, progress)
                        }
                        DownloadManager.STATUS_RUNNING -> {
                            isRunningCalled = true
                            val total =
                                cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                            if (total >= 0) {
                                val downloaded =
                                    cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                                progress = (downloaded * 100L / total).toInt()
                              //  setDownloadRequiredDataToBroadcast(status, progress)
                                sendDownloadProgressToDownloadedActivity(status, progress)
                            }
                        }
                        DownloadManager.STATUS_SUCCESSFUL -> {
                            finishDownload = true
                            progress = 100
                            //setDownloadRequiredDataToBroadcast(status, progress)
                            sendDownloadProgressToDownloadedActivity(status, progress)
                            setDownloadData()
                        }
                    }

                }

                cursor.close()
            }
        }
    }

    private fun setDownloadRequiredDataToBroadcast(downloadStatus: Int, progress: Int){
        /*this is required receiver and will send this data to video list*/
        val intent = Intent(Constants.DOWNLOAD_VIDEO_ACTION)
        intent.putExtra(
            Constants.DOWNLOAD_STATUS,
            downloadStatus
        )
        intent.putExtra(
            Constants.ITEM_POSITION,
            adapterItemPosition
        )
        intent.putExtra(Constants.PROGRESS, progress)
        intent.putExtra(
            Constants.WORKOUT_ID,
            StreamDetailActivity.overViewTrailerData!!.stream_workout_id
        )

        LocalBroadcastManager.getInstance(instance!!).sendBroadcast(intent)
    }

    private fun sendDownloadProgressToDownloadedActivity(downloadStatus: Int, progress: Int) {

        val intent = Intent(Constants.DOWNLOADING_PROGRESS)
        intent.putExtra(
            Constants.DOWNLOAD_STATUS,
            downloadStatus
        )
        intent.putExtra(
            Constants.ITEM_POSITION,
            adapterItemPosition
        )
        intent.putExtra(Constants.PROGRESS, progress)
        intent.putExtra(
            Constants.WORKOUT_ID,
            StreamDetailActivity.overViewTrailerData!!.stream_workout_id
        )

        LocalBroadcastManager.getInstance(instance!!).sendBroadcast(intent)
    }

    private fun setDownloadData() {
        Log.d("fanslfnas", "OUT "+path)
        Log.d("fnaskfnkasfsa", "setDownloadData: BEFORE SET DATA")
        val queueVideoList = DownloadVideosUtil.getVideoQueueList(Constants.QUEUE_VIDEO)
        val availableQueueVideo = queueVideoList!![0]
        Log.d("fanslfnas", "IN ")
        availableQueueVideo.downLoadUrl = path
        availableQueueVideo.isAddedQueue = false
        val list =
            ArrayList<DownloadedVideoModal>()
        list.add(availableQueueVideo)
        val videoCategoryList = VideoCategoryModal(
            availableQueueVideo.stream_workout_description,
            availableQueueVideo.workout_id,
            availableQueueVideo.stream_workout_image,
            availableQueueVideo.stream_workout_image_url,
            availableQueueVideo.stream_workout_name,
            list
        )
        var newList: ArrayList<VideoCategoryModal>? = null

        if(DownloadVideosUtil.getDownloadedVideo(Constants.DOWNLOADED_VIDEO) != null){
            newList = DownloadVideosUtil.getDownloadedVideo(Constants.DOWNLOADED_VIDEO)
            var isAdded = false
            newList?.let {
                for(i in 0 until newList.size){
                    if (newList[i].stream_workout_id.equals(availableQueueVideo.workout_id)) {
                        newList[i].download_list.add(availableQueueVideo);
                        DownloadVideosUtil.setDownloadedData(newList);
                        isAdded = true;
                        break;
                    }
                }
                if(!isAdded){
                    DownloadVideosUtil.setDownloadedVideo(videoCategoryList,Constants.DOWNLOADED_VIDEO)
                }
            }
        }else{
            DownloadVideosUtil.setDownloadedVideo(videoCategoryList,Constants.DOWNLOADED_VIDEO)
        }
        startFurtherDownloadVideo(queueVideoList)
    }

    private fun startFurtherDownloadVideo(availableQueueVideo: ArrayList<DownloadedVideoModal>?){
        Log.d("fnaslfnafsfsfss", "STARTED further download: ${availableQueueVideo!!.size}")
        availableQueueVideo?.let {
            if(availableQueueVideo.isNotEmpty()){
                Log.d("fnaslfnafsfsfss", "STARTED further download: Delete ${availableQueueVideo.size}")
                availableQueueVideo.removeAt(0)// must remove of zeroth  position of local saved video list data
            }
            if(availableQueueVideo.isNotEmpty()){
                Log.d("sncmncmnbmcnbmncm", "STARTED further download: -> Download begin ${availableQueueVideo.size} === ${availableQueueVideo[0].videoUrl}")
                finishDownload = true

                DownloadVideosUtil.saveRemainingQueueVideosToLocal(availableQueueVideo)//saved next all remain video
                Log.d("sncmncmnbmcnbmncm", "THREAD NAME: -> Download begin ${Thread.currentThread().name} AFTER === ${availableQueueVideo[0].videoUrl}")
                StartDownloadManager.beginDownload()
                Log.d("sncmncmnbmcnbmncm", "START DOWNLOADING ${currentThread().name}  --  ${availableQueueVideo[0].videoUrl}")
            }else{
                Doviesfitness.preferences.edit().putString(Constants.QUEUE_VIDEO, "").apply()
            }
        }
    }
}