package com.doviesfitness.temp

import android.app.DownloadManager
import android.content.Context.DOWNLOAD_SERVICE
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import com.doviesfitness.BuildConfig
import com.doviesfitness.Doviesfitness
import com.doviesfitness.Doviesfitness.Companion.getDataManager
import com.doviesfitness.Doviesfitness.Companion.instance

import com.doviesfitness.temp.modal.DownloadedVideoModal
import com.doviesfitness.temp.modal.VideoCategoryModal
import com.doviesfitness.ui.bottom_tabbar.stream_tab.activities.StreamDetailActivity
import com.doviesfitness.ui.bottom_tabbar.stream_tab.download_background.DownloadUtil
import com.doviesfitness.utils.Constants
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


/**Created by Yashank Rathore on 17,December,2020 yashank.mindiii@gmail.com **/

object DownloadVideosUtil {

    var downloadId: Long = 0
    var dmRefIdList = mutableListOf<Long>()
    fun setVideoQueue(
        modal: DownloadedVideoModal,
        listName: String?
    ) {
        val gson = Gson()
        val vjson = Doviesfitness.preferences.getString(listName, "")
        if (vjson != null && vjson.isNotEmpty()) { //for adding modal to stored list
            val videos =
                gson.fromJson<ArrayList<DownloadedVideoModal>>(
                    vjson,
                    object :
                        TypeToken<ArrayList<DownloadedVideoModal?>?>() {}.type
                )
            videos.add(modal)
            try {
                val videoJson = gson.toJson(videos)
                Log.d("kasfasmflassfa-->", "Already added Json-> n:${videoJson}")
                Doviesfitness.preferences.edit().putString(listName, videoJson).apply()
                Log.d("fnakfnkasfa", "setVideoQueue:${videos.size} --- UPPER ${getVideoQueueList(listName)!!.size}")
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                Log.d("fnakfnkasfa", "setVideoQueue FIRST: ${e.message}")

            }
        } else {
            try {
                val videos: ArrayList<DownloadedVideoModal> =
                    ArrayList()
                videos.add(modal)
                val videoJson = gson.toJson(videos)
                Log.d("fnakfnkasfasmflassfa", "FIST TIME JSON -> n:${videoJson}")
                Doviesfitness.preferences.edit().putString(listName, videoJson).apply()
                Log.d("fnakfnkasfa", "setVideoQueue:${videos.size} --- ${getVideoQueueList(listName)!!.size}")
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("fnakfnkasfa", "setVideoQueue: ${e.message}")
            }
        }
    }

    fun saveRemainingQueueVideosToLocal(list: ArrayList<DownloadedVideoModal>) {
        try {
            val gson = Gson()
            val category = gson.toJson(list)
            Log.d("vcvcvcvcvcvcv", "saveRemainingQueueVideosToLocal Json -> n:${category}")
            Doviesfitness.preferences.edit().putString(Constants.QUEUE_VIDEO, category).apply()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            Log.e("Exp---->",e.message!!)
        }
    }

    fun getVideoQueueList(listName: String?): ArrayList<DownloadedVideoModal>?{
        val gson = Gson()
        val vJson = Doviesfitness.preferences.getString(listName, "")
        Log.d("fnaskfnkasfsa", "setDownloadData: AFTER GET DATA ${vJson}")
        val videos = gson.fromJson<ArrayList<DownloadedVideoModal>>(vJson,
            object : TypeToken<ArrayList<DownloadedVideoModal?>>() {
            }.type)
        return videos
    }

    fun setDownloadedVideo(modal: VideoCategoryModal, listName: String?) {
        val gson1 = Gson()
        val vjson = Doviesfitness.preferences.getString(listName, "")
        if (vjson != null && !vjson.isEmpty()) {
            val videos =
                gson1.fromJson<MutableList<VideoCategoryModal>>(
                    vjson,
                    object :
                        TypeToken<List<VideoCategoryModal?>?>() {}.type
                )
            videos.add(modal)
            try {
                val gson = Gson()
                val category = gson.toJson(videos)
                Log.d("fnakfnkasfasmflassfa", "Already save download file  Json -> n:${category}")
                Doviesfitness.preferences.edit().putString(listName, category).apply()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        } else {
            try {
                val gson = Gson()
                val videos: MutableList<VideoCategoryModal> =
                    ArrayList()
                videos.add(modal)
                val category = gson.toJson(videos)

                Log.d("fnakfnkasfasmflassfa", "Start save download file  Json -> n:${category}")
                Doviesfitness.preferences.edit().putString(listName, category).apply()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setDownloadedData(list: ArrayList<VideoCategoryModal>?) {
        try {
            val gson = Gson()
            val category = gson.toJson(list)
            Doviesfitness.preferences.edit().putString(Constants.DOWNLOADED_VIDEO, category)
                .apply()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun getDownloadedVideo(listName: String?): ArrayList<VideoCategoryModal>? {
        val gson1 = Gson()
        val vjson = Doviesfitness.preferences.getString(listName, "")
        Log.d("fnaslfsssssssnasf", "getDownloadedVideo JSON->: ${vjson}")
        return gson1.fromJson(
            vjson,
            object :
                TypeToken<ArrayList<VideoCategoryModal>>() {}.type
        )
    }

    fun deleteCurrentDownloadingVideo() {
        TempAsynctask.isDownloadCancel = true
        val lr = GetDownloadManger.getInstance()?.remove(downloadId)
        Log.d("vcvcvcvcvcvcv", "BEFORE: ${DownloadVideosUtil.dmRefIdList.size}")
        dmRefIdList.remove(downloadId)
        Log.d("vcvcvcvcvcvcv", "AFTER: ${DownloadVideosUtil.dmRefIdList.size}")
        var instance = GetDownloadManger.getInstance()
        instance = null
        Log.d("vcvcvcvcvcvcv", "deleteCurrentDownloadingVideo: ONE "+lr)
    }

    object GetDownloadManger {
        fun getInstance() = instance!!.getSystemService(DOWNLOAD_SERVICE) as DownloadManager?
    }

    public fun deleteFromDownload(position: Int, currentScreenWorkoutId: String) {
        val downloadedList = DownloadVideosUtil.getDownloadedVideo(Constants.DOWNLOADED_VIDEO)
        downloadedList?.let { list ->
            if (list.size > 0) {
                //"-1" add by hemant jain  index out bound exp cresh
                for (i in 0 until list.size) {
                    if (list.get(i).stream_workout_id.equals(currentScreenWorkoutId)) {
                        for (j in 0 until list.get(i).download_list.size) {
                            if (list.get(i).download_list.get(j).stream_video_id.equals(
                                    StreamDetailActivity.videoList.get(position).stream_video_id
                                )
                            ) {
                                list.get(i).download_list.removeAt(j)
                                break
                            }
                        }
                        break
                    }
                }
                if (downloadedList != null && list.size > 0) {
                    var j = 0
                    while (j < list.size) {
                        if (list.get(j).download_list.size > 0) {
                            j++
                        } else {
                            list.removeAt(j)
                        }
                    }
                    if (downloadedList != null && list.size > 0) {
                        setDownloadedData(downloadedList)
                    } else
                        Doviesfitness.preferences.edit().putString(Constants.DOWNLOADED_VIDEO, "").apply()
                } else
                    Doviesfitness.preferences.edit().putString(Constants.DOWNLOADED_VIDEO, "").apply()
            }
        }


    }

    public fun deleteDownloadLocalFile(downloadFileUrl: String?) {
        var ret: File? = null
        val customerName = Doviesfitness.getDataManager().getUserInfo().customer_user_name
        try {
            if (downloadFileUrl != null && !TextUtils.isEmpty(downloadFileUrl)) {
                val lastIndex = downloadFileUrl.lastIndexOf("/")
                if (lastIndex > -1) {
                    val downloadFileName1 = downloadFileUrl.substring(lastIndex + 1)
                    val extensionName = downloadFileName1.split("_").toTypedArray()
                    val lastIndexName = extensionName[0]

                    val path =
                        Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" + BuildConfig.APPLICATION_ID + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + customerName + "//" + lastIndexName

                    ret = File(path)
                    Log.v("vcvcvcvcvcvcv", "PATH____${ret.exists()} " + ret)
                    if (ret.exists()) {
                        ret.delete()
                        Log.d("vcvcvcvcvcvcv", ": file DELETED")
                    }
                    ret.delete()
                    ///storage/emulated/0/Android/data/com.doviesfitness/files/.Download/Dovies/sandeshadmin/3.078298314124986
                }
            }
        } catch (ex: IOException) {
            Log.d("vcvcvcvcvcvcv", "EXCEPTION: " + ex.message)

            Log.e(DownloadUtil.TAG_DOWNLOAD_MANAGER, ex.message, ex)
        }
    }

    public fun deleteAndStartDownload(currentDownloadWorkoutId: String, presentScreenWorkoutId: String,getVideoUrl: String) {
        val getQueueVideoList = DownloadVideosUtil.getVideoQueueList(Constants.QUEUE_VIDEO)
        if (getQueueVideoList != null) {
            if (getQueueVideoList.size > 0) {
                if (currentDownloadWorkoutId == presentScreenWorkoutId) {
                    deleteDownloadLocalFile(getVideoUrl)
                    dmRefIdList.removeAt(0)
                    getQueueVideoList.removeAt(0)
                    TempAsynctask.isDownloadCancel = true
                }
            }
            if(getQueueVideoList.size>0){
                Handler().postDelayed({
                    saveRemainingQueueVideosToLocal(getQueueVideoList)
                    StartDownloadManager.beginDownload() //start download for remaining download video items
                }, 1000)
            }else{
                Doviesfitness.preferences.edit()
                    .putString(Constants.QUEUE_VIDEO, "")
                    .apply()
            }

        }
    }
}