package com.doviesfitness.utils

import android.app.Dialog
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.doviesfitness.R
import java.io.File

class DownloadUtil {


    companion object {
        private var refid: Long = 0
        internal var list = ArrayList<Long>()
        private var downloadManager: DownloadManager? = null
        internal lateinit var mBroadCastReceiver: BroadcastReceiver
        var isRecieverRegistered = false
        internal var mFinishedFilesFromNotif = ArrayList<Long>()
        lateinit var mProgressThread: Thread
        var isDownloadSuccess = false
        lateinit var f: File

        fun isAllDownload(context: Context, videoList: ArrayList<String>): Boolean {
            var isDownload: Boolean = true
            for (i in 0..videoList.size - 1) {
                val lastIndex = videoList.get(i).lastIndexOf("/")
                if (lastIndex > -1) {
                    val downloadFileName = videoList.get(i).substring(lastIndex + 1)
                    val path =
                        Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" +
                                context.packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + "/" + downloadFileName
                    val f = File(path)
                    if (!f.exists()) {
                        isDownload = false
                        break
                    } else {

                    }
                }
            }
            return isDownload
        }


        fun downloadWorkout(
            context: Context,
            videoList: ArrayList<String>,
            loader: ProgressBar,
            playVideo: ImageView,
            downloadingTxt: View
        ) {
            downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            list.clear()
            for (i in 0..videoList.size - 1) {
                if (videoList.get(i) != null) {
                    Log.d("video url", "video url...." + videoList.get(i))
                    val lastIndex = videoList.get(i).lastIndexOf("/")
                    if (lastIndex > -1) {
                        val downloadFileName = videoList.get(i).substring(lastIndex + 1)
                        val subpath = "/Dovies//$downloadFileName"
                        val path = Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" +
                        context.packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + "/" + downloadFileName
                        val f = File(path)
                        Log.e("download file path", "file path..." + f.absolutePath)
                        if (!f.exists()) {
                            val Download_Uri = Uri.parse(videoList.get(i))
                            val request = DownloadManager.Request(Download_Uri)
                            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                            request.setAllowedOverRoaming(false)
                            request.setTitle("Dovies Downloading $i.mp4")
                            request.setDescription("Downloading $i.mp4")
                            request.setVisibleInDownloadsUi(false)
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
                            request.setDestinationInExternalFilesDir(context, "/." + Environment.DIRECTORY_DOWNLOADS, subpath)
                            refid = downloadManager!!.enqueue(request)
                            list.add(refid)
                        } else {

                        }
                    }
                }

            }
            if (!list.isEmpty() && list.size > 0) {
                loader.visibility = View.VISIBLE
                playVideo.visibility = View.GONE
               // downloadingTxt.visibility = View.VISIBLE
                startDownloadThread(context, videoList, loader, playVideo, downloadingTxt)
            } else {
                playVideo.isEnabled = true
            }

        }

        private fun startDownloadThread(
            context: Context,
            videoList: ArrayList<String>,
            loader: ProgressBar,
            playVideo: ImageView,
            downloadingTxt: View
        ) {
            // Initializing the broadcast receiver ...
            mBroadCastReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    isRecieverRegistered = true

                    for (i in 0..videoList.size - 1) {
                        if (videoList.get(i) != null) {
                            // if (exerciseList.get(i).exercise_access_level.equals("OPEN")) {
                            val lastIndex = videoList.get(i).lastIndexOf("/")
                            if (lastIndex > -1) {
                                val downloadFileName = videoList.get(i).substring(lastIndex + 1)
                                val path =
                                    Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" + context.packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + "/" + downloadFileName
                                val encryptedPath =
                                    Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" + context.packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/encrypted"
                                val f = File(path)
                                if (f.exists()) {
                                    // videoList.get(i).workout_offline_video = path
                                    // encrypt(path,encryptedPath,downloadFileName)
                                }
                            }
                            //  }
                        }

                    }
                    mFinishedFilesFromNotif.add(intent.extras!!.getLong(DownloadManager.EXTRA_DOWNLOAD_ID))
                    var referenceId = intent.extras!!.getLong(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                    Log.e("IN", "" + referenceId);
                    list.remove(referenceId);
                    if (list.isEmpty()) {

                        playVideo.isEnabled = true
                        if (isAllDownload(context, videoList)) {
                            Glide.with(context)
                                .load(ContextCompat.getDrawable(context, R.drawable.abc_radio_check))
                                .into(playVideo)
                            loader.visibility = View.GONE
                            downloadingTxt.visibility = View.GONE

                        } else {
                            Glide.with(context).load(
                                ContextCompat.getDrawable(
                                    context,
                                    R.drawable.download_log_ico
                                )
                            ).into(playVideo)
                            //  binding.loader.visibility = VISIBLE
                        }

                    }

                }
            }
            val intentFilter = IntentFilter("android.intent.action.DOWNLOAD_COMPLETE")
            context.registerReceiver(mBroadCastReceiver, intentFilter)


            // initializing the download manager instance ....
            // downloadManager = (DownloadManager).getSystemService(Context.DOWNLOAD_SERVICE);
            // starting the thread to track the progress of the download ..
            var isShown = true
            mProgressThread = Thread(Runnable {
                // Preparing the query for the download manager ...
                val q = DownloadManager.Query()
                val ids = LongArray(list.size)
                val idsArrList = java.util.ArrayList<Long>()
                var i = 0
                for (id in list) {
                    ids[i++] = id
                    idsArrList.add(id)
                }
                q.setFilterById(*ids)
                // getting the total size of the data ...
                var c: Cursor?

                while (true) {
                    // check if the downloads are already completed ...
                    // Here I have created a set of download ids from download manager to keep
                    // track of all the files that are dowloaded, which I populate by creating
                    //
                    if (mFinishedFilesFromNotif.containsAll(idsArrList)) {
                        isDownloadSuccess = true
                        // TODO - Take appropriate action. Download is finished successfully
                        return@Runnable
                    }
                    // start iterating and noting progress ..
                    c = downloadManager!!.query(q)

                    if (c != null) {
                        var filesDownloaded = 0
                        var fileFracs = 0f // this stores the fraction of all the files in
                        // download
                        val columnTotalSize = c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)
                        val columnStatus = c.getColumnIndex(DownloadManager.COLUMN_STATUS)
                        //final int columnId = c.getColumnIndex(DownloadManager.COLUMN_ID);
                        val columnDwnldSoFar = c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)

                        while (c.moveToNext()) {
                            // checking the progress ..
                            if (c.getInt(columnStatus) == DownloadManager.STATUS_SUCCESSFUL) {
                                filesDownloaded++
                                Log.d("progress...", "progress if..."+"STATUS_SUCCESSFUL.."+filesDownloaded)
                            } else if (c.getInt(columnTotalSize) > 0) {
                                fileFracs += c.getInt(columnDwnldSoFar) * 1.0f / c.getInt(columnTotalSize)
                                Log.d("progress...", "progress else if..."+fileFracs)

                            } else if (c.getInt(columnStatus) == DownloadManager.STATUS_FAILED) {
                                // TODO - Take appropriate action. Error in downloading one of the
                                // files.
                                return@Runnable
                            }// If the file is partially downloaded, take its fraction ..
                        }
                        c.close()
                        // calculate the progress to show ...
                        val progress = (filesDownloaded + fileFracs) / ids.size

                        // setting the progress text and bar...
                        val percentage = Math.round(progress * 100.0f)
                        val txt = "Loading ... $percentage%"
                        Log.d("progress...", "progress...$txt")
                        loader.setProgress(percentage)

/*
                        if (percentage == 100&&isShown) {
                            showPlay(percentage)
                            isShown=false
                        }
*/
                    }
                }
            })
            mProgressThread.start()
        }

        /*
                fun showPlay(percentage: Int) {
                    runOnUiThread(Runnable() {
                        fun run() {
                            if (percentage == 100) {
                                binding.playVideo.isEnabled = true
                                Glide.with(getActivity())
                                    .load(
                                        ContextCompat.getDrawable(
                                            getActivity(),
                                            R.drawable.new_play_icon
                                        )
                                    )
                                    .into(binding.playVideo)
                                Log.d("showPlay","showPlay...."+percentage)
                                binding.loader.visibility = View.GONE
                                binding.downloadingTxt.visibility = View.GONE

                            }
                        }
                    })

                }
        */
        fun unregisterReceivers(context: Context) {
            if (isRecieverRegistered == true)
                context.unregisterReceiver(mBroadCastReceiver)
        }

        fun sharePost(url : String,context: Context){
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, url)
            sendIntent.type = "text/plain"
            //  sendIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
            context.startActivity(Intent.createChooser(sendIntent, "choose one"))
        }


    }
}