package com.doviesfitness.temp

import android.app.DownloadManager
import android.content.Intent
import android.database.Cursor
import android.os.AsyncTask
import android.os.Handler
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.doviesfitness.Doviesfitness
import com.doviesfitness.temp.modal.DownloadedVideoModal
import com.doviesfitness.temp.modal.VideoCategoryModal
import com.doviesfitness.ui.bottom_tabbar.stream_tab.activities.StreamDetailActivity
import com.doviesfitness.utils.Constants

/**Created by Yashank Rathore on 25,December,2020 yashank.mindiii@gmail.com **/

class TempAsynctask(
    val queueList: ArrayList<DownloadedVideoModal>,
    val path: String
) : AsyncTask<Int, Void, Int>() {

    companion object {
        var finishDownload = false
        var isDownloadCancel = false
        var isDownloadStarted =
            false /*taking this flag only for maintain calling for-loop once when receiver called only getting item position i.e. may be item position varies so only for updating UI purpose*/
    }

    var progress = 0

    private lateinit var cursor: Cursor
    var isRunningCalled = false

    override fun doInBackground(vararg p0: Int?): Int {
        while (!TempAsynctask.finishDownload && !isDownloadCancel) {
            val q = DownloadManager.Query()
            val ids =
                LongArray(DownloadVideosUtil.dmRefIdList.size)
            Log.d("fnaskfnkasfas", "REACHED: ${DownloadVideosUtil.dmRefIdList.size}")
            val idsArrList = ArrayList<Long>()
            var id = 0
            for (i in 0 until DownloadVideosUtil.dmRefIdList.size) {
                ids[id++] =
                    DownloadVideosUtil.dmRefIdList[i]
                idsArrList.add(
                    DownloadVideosUtil.dmRefIdList[i]
                )
            }
            q.setFilterById(*ids)
            if (DownloadVideosUtil.GetDownloadManger.getInstance() != null && !isDownloadCancel) {
                cursor = DownloadVideosUtil.GetDownloadManger.getInstance()!!.query(q)
                if (cursor.moveToFirst()) {
                    val status =
                        cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                    val columnReason =
                        cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_REASON))
                    Log.d("cnxmkscnmxncmx", "run: ${columnReason} -- $status")



                    when (status) {
                        DownloadManager.STATUS_FAILED -> {
                            Log.d("cxxcxcxcxcxcxc", "STATUS_FAILED: PROGRESS===> } ${columnReason}")
                            deleteAndStartDownload(
                                queueList[0].videoUrl
                            )
                            finishDownload = true
                            sendDownloadProgressToDownloadedActivity(status, progress)
                            when (columnReason) {
                                DownloadManager.ERROR_INSUFFICIENT_SPACE -> {
                                    sendDownloadProgressToDownloadedActivity(
                                        Constants.ERROR_INSUFFICIENT_SPACE_CODE,
                                        0
                                    )
                                }
                                else -> {
                                    sendDownloadProgressToDownloadedActivity(
                                        Constants.DOWNLOAD_UNKNOWN_ERROR,
                                        0
                                    )
                                }
                            }
                        }
                        DownloadManager.STATUS_PAUSED -> {
                            Log.d("cxxcxcxcxcxcxc", "STATUS_PAUSED: PROGRESS===> } ${columnReason}")
                            deleteAndStartDownload(
                                queueList[0].videoUrl
                            )
                            when (columnReason) {
                                DownloadManager.PAUSED_UNKNOWN -> {
                                    sendDownloadProgressToDownloadedActivity(
                                        Constants.DOWNLOAD_UNKNOWN_ERROR,
                                        progress
                                    )
                                }
                                else -> {
                                    sendDownloadProgressToDownloadedActivity(
                                        Constants.DOWNLOAD_UNKNOWN_ERROR,
                                        progress
                                    )
                                }
                            }
                        }
                        DownloadManager.STATUS_PENDING -> {
                            Log.d("cxxcxcxcxcxcxc", "STATUS_PENDING: PROGRESS===> } ${progress}")
                            sendDownloadProgressToDownloadedActivity(status, progress)
                        }
                        DownloadManager.STATUS_RUNNING -> {
                            Log.d("cxxcxcxcxcxcxc", "STATUS_RUNNING: PROGRESS===> } ${progress}")
                            isRunningCalled = true
                            val total =
                                cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                            if (total >= 0) {
                                try {
                                    val downloaded =
                                        cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                                    progress = (downloaded * 100L / total).toInt()


                                    sendDownloadProgressToDownloadedActivity(status, progress)
                                }catch (e:Exception)
                                {
                                    Log.d("Download Excp",e.toString())
                                }

                            }
                        }
                        DownloadManager.STATUS_SUCCESSFUL -> {
                            TempAsynctask.finishDownload = true
                            progress = 100
                            Log.d("cxxcxcxcxcxcxc", "STATUS_SUCCESSFUL: PROGRESS===> } ${progress}")

                            //setDownloadRequiredDataToBroadcast(status, progress)
                            sendDownloadProgressToDownloadedActivity(status, progress)
                        }
                    }
                }
                cursor.close()
            }
        }

        return progress
    }

    override fun onPostExecute(result: Int?) {
        super.onPostExecute(result)
        if (!isDownloadCancel) {
            setDownloadData()
        }
    }


    private fun setDownloadData() {
        Log.d("fanslfnas", "OUT " + path)
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

        if (DownloadVideosUtil.getDownloadedVideo(Constants.DOWNLOADED_VIDEO) != null) {
            newList = DownloadVideosUtil.getDownloadedVideo(Constants.DOWNLOADED_VIDEO)
            var isAdded = false
            newList?.let {
                for (i in 0 until newList.size) {
                    if (newList[i].stream_workout_id.equals(availableQueueVideo.workout_id)) {
                        newList[i].download_list.add(availableQueueVideo)
                        DownloadVideosUtil.setDownloadedData(newList) // add downloaded video in existing category list
                        isAdded = true
                        break
                    }
                }
            }
            if (!isAdded) {
                DownloadVideosUtil.setDownloadedVideo(
                    videoCategoryList,
                    Constants.DOWNLOADED_VIDEO
                ) //add downloaded for n>1 category where n = category
            }
        } else {
            DownloadVideosUtil.setDownloadedVideo(
                videoCategoryList,
                Constants.DOWNLOADED_VIDEO
            ) //add downloaded first time video
        }

        startFurtherDownloadVideo(queueVideoList)
    }

    private fun startFurtherDownloadVideo(availableQueueVideo: ArrayList<DownloadedVideoModal>?) {
        Log.d("fnaslfnafsfsfss", "STARTED further download: ${availableQueueVideo!!.size}")

        availableQueueVideo.let {
            if (availableQueueVideo.isNotEmpty()) {
                Log.d(
                    "fnaslfnafsfsfss",
                    "STARTED further download: Delete ${availableQueueVideo.size}"
                )
                DownloadVideosUtil.dmRefIdList.removeAt(0)
                availableQueueVideo.removeAt(0)// must remove of zeroth  position of local saved video list data
            }
            if (availableQueueVideo.isNotEmpty()) {
                Log.d(
                    "sncmncmnbmcnbmncm",
                    "STARTED further download: -> Download begin ${availableQueueVideo.size} === ${availableQueueVideo[0].videoUrl}"
                )
                finishDownload = true
                progress = 0
                DownloadVideosUtil.saveRemainingQueueVideosToLocal(availableQueueVideo)//saved next all remain video
                Log.d(
                    "sncmncmnbmcnbmncm",
                    "THREAD NAME: -> Download begin ${Thread.currentThread().name} AFTER === ${availableQueueVideo[0].videoUrl}"
                )
                // job.cancel()

                // Set current thread priority lower than main thread priority, so main thread Pause, Continue and Cancel action will not be blocked.
                StartDownloadManager.beginDownload()


                Log.d(
                    "sncmncmnbmcnbmncm",
                    "START DOWNLOADING ${Thread.currentThread().name}  --  ${availableQueueVideo[0].videoUrl}"
                )
            } else {
                Doviesfitness.preferences.edit().putString(Constants.QUEUE_VIDEO, "").apply()
            }
        }
    }

    private fun sendDownloadProgressToDownloadedActivity(downloadStatus: Int, progress: Int) {
        /*this is also required receiver and will send this data to Downloaded activity*/
        Log.d("fnaskfnasfa", "WORKOUT ID : ${queueList[0].workout_id}")
        Log.d("cxxcxcxcxcxcxc", "SEND BROADCAST RECEIVER: ")

        val intent = Intent(Constants.DOWNLOADING_PROGRESS)
        intent.putExtra(
            Constants.DOWNLOAD_STATUS,
            downloadStatus
        )
        intent.putExtra(
            Constants.ITEM_POSITION,
            queueList[0].position
        )
        intent.putExtra(Constants.PROGRESS, progress)
        intent.putExtra(
            Constants.WORKOUT_ID,
            queueList[0].workout_id
        )

        intent.putExtra(
            Constants.STREAM_VIDEO_ID,
            queueList[0].stream_video_id
        )
        intent.putExtra(
            Constants.DOWNLOAD_URL_IN_ACCOURDING_TO_PIXEL,
            queueList[0].videoUrl
        )

        LocalBroadcastManager.getInstance(Doviesfitness.instance!!).sendBroadcast(intent)
    }

    public fun deleteAndStartDownload(getVideoUrl: String) {
        val getQueueVideoList = DownloadVideosUtil.getVideoQueueList(Constants.QUEUE_VIDEO)
        if (getQueueVideoList != null) {
            if (getQueueVideoList.size > 0) {
                DownloadVideosUtil.deleteDownloadLocalFile(getVideoUrl)
                DownloadVideosUtil.dmRefIdList.removeAt(0)
                getQueueVideoList.removeAt(0)
                isDownloadCancel = true
            }
            if(getQueueVideoList.size>0){
                try{
                    Handler().postDelayed({
                        DownloadVideosUtil.saveRemainingQueueVideosToLocal(getQueueVideoList)
                        StartDownloadManager.beginDownload() //start download for remaining download video items
                    }, 1000)
                }catch (e:Exception)
                {
                    Log.d("Temp Asyantask Hemant",e.toString())
                }

            }else{
                Doviesfitness.preferences.edit()
                    .putString(Constants.QUEUE_VIDEO, "")
                    .apply()
            }

        }
    }

}