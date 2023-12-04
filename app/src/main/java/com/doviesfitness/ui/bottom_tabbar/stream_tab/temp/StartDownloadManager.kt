package com.doviesfitness.temp

import android.app.DownloadManager
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.util.Log
import com.doviesfitness.BuildConfig
import com.doviesfitness.Doviesfitness

import com.doviesfitness.temp.modal.DownloadedVideoModal
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.DownloadVideoModel
import com.doviesfitness.utils.Constants
import kotlinx.coroutines.*
import java.lang.Exception

/**Created by Yashank Rathore on 23,December,2020 yashank.mindiii@gmail.com **/

object StartDownloadManager: TempInter {


    fun beginDownload() {
        val queueList = DownloadVideosUtil.getVideoQueueList(Constants.QUEUE_VIDEO)
        if(queueList != null) {
            try {
                TempAsynctask.isDownloadCancel = false
                TempAsynctask.finishDownload = false
                TempAsynctask.isDownloadStarted = true //taking this flag only for maintain calling for-loop once when receiver called only getting item position i.e. may be item position varies so only for updating UI purpose
                Log.d("faslfmlkasfa", "beginDownload: ")

                val videoUrl = queueList[0].videoUrl
                Log.d("fnaslfnafsfsfss", "beginDownload: URL  ${videoUrl}")
                Log.d("fmafklmas", "beginDownload: Video URL  ${videoUrl}")

                val customerName =
                        Doviesfitness.getDataManager().getUserInfo().customer_user_name

                val downloadFileName: String = videoUrl.substring(videoUrl.lastIndexOf('/') + 1)
                val extensionName =
                        downloadFileName.split("_".toRegex()).toTypedArray()
                val lastIndexName = extensionName[0] + ".mp4"

                Log.d("nfkanfknaskfasfasf", "downloadExercise: $videoUrl\n$lastIndexName")
                val path = Environment.getExternalStorageDirectory()
                        .absolutePath + "/Android/data/" + BuildConfig.APPLICATION_ID + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + customerName + "//" + lastIndexName

                val subpath = "/Dovies/$customerName//$lastIndexName"
                Log.d("nfkanfknaskfasfasf", "subpath --  $subpath")
                val downloadUri = Uri.parse(videoUrl)
                val request =
                        DownloadManager.Request(downloadUri)
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                request.setAllowedOverRoaming(false)
                request.setTitle("Dovies Downloading .mp4")
                request.setDescription("Downloading .mp4")
                request.setVisibleInDownloadsUi(false)

                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN)
                request.setDestinationInExternalFilesDir(
                        Doviesfitness.instance,
                        "/." + Environment.DIRECTORY_DOWNLOADS,
                        subpath
                )

                DownloadVideosUtil.dmRefIdList.clear()
                val getDownloadManagerId = DownloadVideosUtil.GetDownloadManger.getInstance()!!.enqueue(request)
                DownloadVideosUtil.downloadId = getDownloadManagerId
                DownloadVideosUtil.dmRefIdList.add(DownloadVideosUtil.downloadId)


//        val thread = CalculateDownloadProgress(
//            DownloadVideosUtil.downloadId,
//            itemPosition,
//            path
//        )`
                Log.d("sncmncmnbmcnbmncm", "START THRAD ${Thread.currentThread().name}  -- ")


//        DownloadVideoCoroutines( DownloadVideosUtil.downloadId,
//            itemPosition,
//            path,this as TempInter).startDownload()

                TempAsynctask(
                        queueList,
                        path).execute()

//        if(thread.isAlive){
//            Log.d("fnaslfnafsfsfss", "THREAD IS ALIVE")
//            try {
//                thread.interrupt()
//            }catch (e: Exception){
//                Log.d("fnaslfnafsfsfss", "beginDownload: ${e.message}")
//            }
//        }else{
//            Log.d("fnaslfnafsfsfss", "THREAD START")
//            thread.start()
//        }

            }catch (e:Exception)
            {
                e.printStackTrace()
            }
        }
    }

    override fun startDownload(list: ArrayList<DownloadedVideoModal>?) {
        list?.let {
            startFurtherDownloadVideo(it)
        }
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
                TempAsynctask.finishDownload = true

                DownloadVideosUtil.saveRemainingQueueVideosToLocal(availableQueueVideo)//saved next all remain video
                Log.d("sncmncmnbmcnbmncm", "THREAD NAME: -> Download begin ${Thread.currentThread().name} AFTER === ${availableQueueVideo[0].videoUrl}")
                // job.cancel()

                // Set current thread priority lower than main thread priority, so main thread Pause, Continue and Cancel action will not be blocked.

                    beginDownload()


                Log.d("sncmncmnbmcnbmncm", "START DOWNLOADING ${Thread.currentThread().name}  --  ${availableQueueVideo[0].videoUrl}")
            }else{
                Doviesfitness.preferences.edit().putString(Constants.QUEUE_VIDEO, "").apply()
            }
        }
    }
}