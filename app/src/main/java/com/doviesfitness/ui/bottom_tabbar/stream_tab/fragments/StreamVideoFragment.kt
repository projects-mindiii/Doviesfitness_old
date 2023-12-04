package com.doviesfitness.ui.bottom_tabbar.stream_tab.fragments

import android.annotation.TargetApi
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.doviesfitness.R
import com.doviesfitness.databinding.FragmentStreamVideoBinding
import com.doviesfitness.ui.base.BaseFragment
import com.doviesfitness.ui.bottom_tabbar.stream_tab.activities.StreamDetailActivity
import com.doviesfitness.ui.bottom_tabbar.stream_tab.activities.StreamDetailActivity.Companion.overViewTrailerData
import com.doviesfitness.ui.bottom_tabbar.stream_tab.activities.StreamDetailActivity.Companion.videoList
import com.doviesfitness.ui.bottom_tabbar.stream_tab.adapters.StreamVideoAdapter
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.DownloadVideoModel
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.StreamWorkoutDetailModel
import com.doviesfitness.ui.room_db.DatabaseClient
import com.doviesfitness.ui.room_db.LocalStreamVideoDataModal
import com.doviesfitness.ui.room_db.MyVideoList
import com.doviesfitness.utils.CommanUtils
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.DownloadUtil
import com.facebook.FacebookSdk.getApplicationContext
import java.io.File
import android.app.job.JobScheduler
import android.app.job.JobInfo
import android.content.*
import android.content.Context.BIND_AUTO_CREATE
import android.os.*
import androidx.work.*
import com.doviesfitness.ui.bottom_tabbar.stream_tab.download_background.BoundService
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.DownloadModel
import com.google.gson.Gson
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class StreamVideoFragment : BaseFragment(), StreamVideoAdapter.OnViewClick,
    StreamDetailActivity.UpdateListInterface {

    private var timeStempUrl: String = ""
    private var pathget: String = ""
    private var workoutid: String = ""
    lateinit var binding: FragmentStreamVideoBinding
    lateinit var adapter: StreamVideoAdapter
    var videoUrlList = arrayListOf<String>()
    private var dirPath: String? = null
    private var strList = ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>()
    private var downloadVideoList = ArrayList<DownloadVideoModel>()
    var isDownloading = false
    private val JOBID = 110

    private lateinit var myScheduler: JobScheduler
    private lateinit var myjobInfo: JobInfo

    //private var videoList = ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>()
    var listFromLb = ArrayList<MyVideoList>()

//BoundService class Objet
    var boundService: BoundService ? = null //boolean variable to keep a check on service bind and unbind event
     var isBound = false

    private var forUpdate: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_stream_video, container, false)
        EventBus.getDefault().register(this)

         var intent =  Intent(mContext , BoundService::class.java)
         mContext.startService(intent)
         mContext.bindService(intent , boundServiceConnection,BIND_AUTO_CREATE);

        initialization()
        return binding.root
    }

    private fun initialization() {
        val layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)

       // adapter = StreamVideoAdapter(mContext, videoList)
        binding.videoRv.layoutManager = layoutManager
        binding.videoRv.adapter = adapter

        var runnable =  Runnable() {
            @Override
            fun run() {
                Toast.makeText(mContext, "number.."+boundService!!.randomGenerator().toString(),Toast.LENGTH_SHORT).show();

            }
        }

        var handler =  Handler()
        handler.postDelayed(runnable,3000);
    }

    override fun onResume() {
        super.onResume()

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
    }

/*
    override fun downloadVideo(
        pos: Int,
        videoModal: StreamWorkoutDetailModel.Settings.Data.Video,
        view: ImageView,
        loader: ProgressBar,
        viewHolder: StreamVideoAdapter.MyViewHolder,
        status: String
    ) {
        CommanUtils.lastClick()// manage last click
        //Shubham Code
        if (status.equals("forDownload")) {
           */
/* if (isJobServiceOn(mContext)){
               Log.d("isJobServiceOn","isJobServiceOn...true")
            }
            else{
                Log.d("isJobServiceOn","isJobServiceOn...false")
            }*//*

            randomdownLoadVideo(pos, videoModal, view, loader, viewHolder)

            */
/* if (isDownloading) {
                 var obj = DownloadVideoModel(pos, videoModal, view, viewHolder.loader, viewHolder, status)
                 downloadVideoList.add(obj)
                 Toast.makeText(mContext, "Waiting", Toast.LENGTH_SHORT).show()
                 viewHolder.downloadIcon.setImageResource(R.drawable.watch_ico)
             } else {
                 randomdownLoadVideo(pos, videoModal, view, loader, viewHolder)
             }*//*

        }
        else if (status.equals("forPlay")) {
            if (videoModal.downLoadUrl != null && !videoModal.downLoadUrl.isEmpty()) {
                val lastIndex = videoModal.downLoadUrl.lastIndexOf("/")
                if (lastIndex > -1) {
                    val downloadFileName = videoModal.downLoadUrl.substring(lastIndex + 1)
                    pathget =
                        Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" + context!!.packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + "/" + downloadFileName
                    val f = File(pathget)
                    if (f.exists()) {
                        if (pathget != null && !pathget.isEmpty()) {
                            val MaxProgress = Constant.getExerciseTime(videoModal.video_duration)
                            val tempListModal = StreamWorkoutDetailModel.Settings.Data.Video(
                                stream_video = videoModal.stream_video,
                                stream_video_description = videoModal.stream_video_description,
                                stream_video_id = videoModal.stream_video_id,
                                video_duration = videoModal.video_duration,
                                stream_video_image = videoModal.stream_video_image,
                                stream_video_image_url = videoModal.stream_video_image_url,
                                stream_video_name = videoModal.stream_video_name,
                                stream_video_subtitle = videoModal.stream_video_subtitle,
                                Progress = videoModal.Progress,
                                MaxProgress = MaxProgress,
                                seekTo = videoModal.seekTo,
                                isPlaying = videoModal.isPlaying,
                                isComplete = videoModal.isComplete,
                                isRestTime = videoModal.isRestTime,
                                downLoadUrl = pathget
                            )
                            workoutid = overViewTrailerData!!.stream_workout_id

                            */
/* if (strList != null) {
                                 strList.clear()
                             }
                             strList.add(tempListModal)
                             if (strList != null && strList.size > 0) {
                                 val intent = Intent(activity, StreamVideoPlayLandscapeActivity::class.java)
                                 //intent.putExtra("localStreamList", strList)
                                 intent.putExtra("videoList", strList)
                                 intent.putExtra("workout_id", "" + workoutid)
                                 intent.putExtra("local", "no")
                                 activity!!.startActivity(intent)
                                 ewrgydffdtyfdy fydfswy
                             }*//*

                        }
                    }
                }
            } else {
                val lastIndex = videoModal.stream_video.lastIndexOf("/")
                if (lastIndex > -1) {
                    val downloadFileName = videoModal.stream_video.substring(lastIndex + 1)
                    pathget =
                            // Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" + context!!.packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + "/" +CommanUtils.getTimeStamp()+downloadFileName
                        Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" + context!!.packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + "/" + downloadFileName
                    val f = File(pathget)
                    if (f.exists()) {
                        if (pathget != null && !pathget.isEmpty()) {
                            val MaxProgress = Constant.getExerciseTime(videoModal.video_duration)
                            val tempListModal = StreamWorkoutDetailModel.Settings.Data.Video(
                                stream_video = videoModal.stream_video,
                                stream_video_description = videoModal.stream_video_description,
                                stream_video_id = videoModal.stream_video_id,
                                video_duration = videoModal.video_duration,
                                stream_video_image = videoModal.stream_video_image,
                                stream_video_image_url = videoModal.stream_video_image_url,
                                stream_video_name = videoModal.stream_video_name,
                                stream_video_subtitle = videoModal.stream_video_subtitle,
                                Progress = videoModal.Progress,
                                MaxProgress = MaxProgress,
                                seekTo = videoModal.seekTo,
                                isPlaying = videoModal.isPlaying,
                                isComplete = videoModal.isComplete,
                                isRestTime = videoModal.isRestTime,
                                downLoadUrl = pathget
                            )
                            workoutid = overViewTrailerData!!.stream_workout_id

                            if (strList != null) {
                                strList.clear()
                            }
                            strList.add(tempListModal)
                            if (strList != null && strList.size > 0) {
                                val intent =
                                    Intent(activity, StreamVideoPlayLandscapeActivity::class.java)
                                intent.putExtra("videoList", strList)
                                intent.putExtra("workout_id", "" + workoutid)
                                intent.putExtra("local", "no")
                                activity!!.startActivity(intent)
                            }
                        }
                    } else {
                        val tempListModal = StreamWorkoutDetailModel.Settings.Data.Video(
                            stream_video = videoModal.stream_video,
                            stream_video_description = videoModal.stream_video_description,
                            stream_video_id = videoModal.stream_video_id,
                            video_duration = videoModal.video_duration,
                            stream_video_image = videoModal.stream_video_image,
                            stream_video_image_url = videoModal.stream_video_image_url,
                            stream_video_name = videoModal.stream_video_name,
                            stream_video_subtitle = videoModal.stream_video_subtitle,
                            Progress = videoModal.Progress,
                            MaxProgress = videoModal.MaxProgress,
                            seekTo = videoModal.seekTo,
                            isPlaying = videoModal.isPlaying,
                            isComplete = videoModal.isComplete,
                            isRestTime = videoModal.isRestTime,
                            downLoadUrl = ""
                        )
                        workoutid = overViewTrailerData!!.stream_workout_id

                        if (strList != null) {
                            strList.clear()
                        }
                        strList.add(tempListModal)
                        if (strList != null && strList.size > 0) {
                            val intent =
                                Intent(activity, StreamVideoPlayLandscapeActivity::class.java)
                            intent.putExtra("videoList", strList)
                            intent.putExtra("workout_id", "" + workoutid)
                            intent.putExtra("local", "no")
                            activity!!.startActivity(intent)
                        }
                    }
                }
            }
        }
    }
*/

/*
    private fun randomdownLoadVideo(
        pos: Int,
        videoModal: StreamWorkoutDetailModel.Settings.Data.Video,
        view: ImageView,
        loader: ProgressBar,
        viewHolder: StreamVideoAdapter.MyViewHolder
    ) {
        videoModal.stream_video
        videoUrlList.clear()
        videoUrlList.add(videoList.get(pos).stream_video)

       // dirPath = PRDownloadUtil.getRootDirPath(getApplicationContext())
        val lastIndex = videoModal.stream_video.lastIndexOf("/")

        if (lastIndex > -1) {
            var downloadFileName = videoModal.stream_video.substring(lastIndex + 1)

            if (videoList.get(pos).timeStempUrl != null && !videoList.get(pos).timeStempUrl.isEmpty()) {
                var spliteTimeSteamp = videoList.get(pos).timeStempUrl.split("_")
                var restString = spliteTimeSteamp[1]
                if (!downloadFileName.equals(restString)) {

                    Log.v("download", "" + downloadFileName + "" + restString)
                    pathget =
                        Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" +
                                context!!.packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + "/" + CommanUtils.getTimeStamp() + "_" + downloadFileName
                    val f = File(pathget)

                    //get last indesxt of url with time stemp
                    val directory = pathget
                    val getDirectory = directory.split("//")
                    timeStempUrl = getDirectory[1]
                    Log.v("dirPath", "" + f)

                    if (!f.exists()) {
                        isDownloading = true
                       // startJob(pos, "", videoModal.stream_video, pathget, "dovies_video")

                        */
/*val workManager = WorkManager.getInstance()
                        val constraints = Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            .build()
                        val uploadPhotoRequest = OneTimeWorkRequest.Builder(DownloadWorkManager::class.java)
                            .setConstraints(constraints)
                            .setInputData(createInputData(pos, "", videoModal.stream_video, pathget, "dovies_video"))
                            .build()
                        workManager.enqueue(uploadPhotoRequest)*//*



                        PRDownloader.download(videoModal.stream_video, pathget, "dovies_video")
                            .build()
                            .setOnStartOrResumeListener { }
                            .setOnPauseListener { }
                            .setOnCancelListener(object : OnCancelListener {
                                override fun onCancel() {
                                    viewHolder.loader.visibility = View.VISIBLE
                                    viewHolder.loader.setIndeterminate(true)
                                    isDownloading = false
                                }
                            })

                            .setOnProgressListener(object : OnProgressListener {
                                override fun onProgress(progress: Progress) {
                                    viewHolder.loader.visibility = View.VISIBLE
                                    viewHolder.downloadIcon.visibility = View.GONE
                                    viewHolder.loader.max = progress.totalBytes.toInt()
                                    viewHolder.loader.setProgress(progress.currentBytes.toInt())
                                    Log.d("loader position", "loader position progress..." + pos)
                                }
                            })

                            .start(object : OnDownloadListener {
                                override fun onError(error: com.downloader.Error?) {
                                    viewHolder.loader.visibility = View.GONE
                                    viewHolder.downloadIcon.visibility = View.VISIBLE
                                    isDownloading = false
                                    //Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                                }

                                override fun onDownloadComplete() {
                                    viewHolder.loader.visibility = View.GONE
                                    viewHolder.downloadIcon.visibility = View.VISIBLE
                                    viewHolder.downloadIcon.setImageResource(R.drawable.abc_radio_check)
                                    isDownloading = false
                                    Log.d("loader position", "loader position complete..." + pos)

                                    if (downloadVideoList.size > 0) {
                                        randomdownLoadVideo(
                                            downloadVideoList.get(0).pos,
                                            downloadVideoList.get(0).videoModal,
                                            downloadVideoList.get(0).view
                                            ,
                                            downloadVideoList.get(0).loader,
                                            downloadVideoList.get(0).viewHolder
                                        )
                                        downloadVideoList.removeAt(0)
                                    }

                                    val tempListModal =
                                        StreamWorkoutDetailModel.Settings.Data.Video(
                                            stream_video = videoModal.stream_video,
                                            stream_video_description = videoModal.stream_video_description,
                                            stream_video_id = videoModal.stream_video_id,
                                            video_duration = videoModal.video_duration,
                                            stream_video_image = videoModal.stream_video_image,
                                            stream_video_image_url = videoModal.stream_video_image_url,
                                            stream_video_name = videoModal.stream_video_name,
                                            stream_video_subtitle = videoModal.stream_video_subtitle,
                                            order = 0,
                                            Progress = videoModal.Progress,
                                            MaxProgress = videoModal.MaxProgress,
                                            seekTo = videoModal.seekTo,
                                            isPlaying = videoModal.isPlaying,
                                            isComplete = videoModal.isComplete,
                                            isRestTime = videoModal.isRestTime,
                                            downLoadUrl = pathget,
                                            timeStempUrl = timeStempUrl
                                        )

                                    videoList.set(pos, tempListModal)
                                    adapter.notifyItemChanged(pos)

                                    //download here video in local data base when he down load
                                    getAllWorkOutList(
                                        overViewTrailerData!!.stream_workout_name,
                                        overViewTrailerData!!.stream_workout_id,
                                        videoList
                                    )
                                }
                            })
                     //   PRDownloader.getStatus(downloadId)

                    }

                }
            }
            else {
                pathget =
                    Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" +
                            context!!.packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + "/" + CommanUtils.getTimeStamp() + "_" + downloadFileName
                val f = File(pathget)

                //get last indesxt of url with time stemp
                val directory = pathget
                val getDirectory = directory.split("//")
                timeStempUrl = getDirectory[1]
                Log.v("dirPath", "" + f)

                if (!f.exists()) {
                    isDownloading = true

                    val workManager = WorkManager.getInstance()
                    val constraints = Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                    val uploadPhotoRequest = OneTimeWorkRequest.Builder(DownloadWorkManager::class.java)
                        .setConstraints(constraints)
                        .setInputData(createInputData(pos, "", videoModal.stream_video, pathget, "dovies_video"))
                        .build()
                    workManager.enqueue(uploadPhotoRequest)
               //     startJob(pos, "", videoModal.stream_video, pathget, "dovies_video")

*/
/*
                    PRDownloader.download(videoModal.stream_video, pathget, "dovies_video")
                        .build()
                        .setOnStartOrResumeListener { }
                        .setOnPauseListener { }
                        .setOnCancelListener(object : OnCancelListener {
                            override fun onCancel() {
                                viewHolder.loader.visibility = View.VISIBLE
                                viewHolder.loader.setIndeterminate(true)
                                isDownloading = false
                            }
                        })

                        .setOnProgressListener(object : OnProgressListener {
                            override fun onProgress(progress: Progress) {
                                viewHolder.loader.visibility = View.VISIBLE
                                viewHolder.downloadIcon.visibility = View.GONE
                                viewHolder.loader.max = progress.totalBytes.toInt()
                                viewHolder.loader.setProgress(progress.currentBytes.toInt())
                                Log.d("loader position", "loader position progress..." + pos)
                            }
                        })

                        .start(object : OnDownloadListener {
                            override fun onError(error: com.downloader.Error?) {
                                viewHolder.loader.visibility = View.GONE
                                viewHolder.downloadIcon.visibility = View.VISIBLE
                                isDownloading = false
                                //Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                            }

                            override fun onDownloadComplete() {
                                viewHolder.loader.visibility = View.GONE
                                viewHolder.downloadIcon.visibility = View.VISIBLE
                                viewHolder.downloadIcon.setImageResource(R.drawable.abc_radio_check)
                                isDownloading = false
                                Log.d("loader position", "loader position complete..." + pos)
                                val tempListModal =
                                    StreamWorkoutDetailModel.Settings.Data.Video(
                                        stream_video = videoModal.stream_video,
                                        stream_video_description = videoModal.stream_video_description,
                                        stream_video_id = videoModal.stream_video_id,
                                        video_duration = videoModal.video_duration,
                                        stream_video_image = videoModal.stream_video_image,
                                        stream_video_image_url = videoModal.stream_video_image_url,
                                        stream_video_name = videoModal.stream_video_name,
                                        stream_video_subtitle = videoModal.stream_video_subtitle,
                                        Progress = videoModal.Progress,
                                        MaxProgress = videoModal.MaxProgress,
                                        seekTo = videoModal.seekTo,
                                        isPlaying = videoModal.isPlaying,
                                        isComplete = videoModal.isComplete,
                                        isRestTime = videoModal.isRestTime,
                                        downLoadUrl = pathget,
                                        timeStempUrl = timeStempUrl
                                    )

                                videoList.set(pos, tempListModal)
                                adapter.notifyDataSetChanged()

                                //download here video in local data base when he down load
                                getAllWorkOutList(
                                    overViewTrailerData!!.stream_workout_name,
                                    overViewTrailerData!!.stream_workout_id,
                                    videoList
                                )
                                if (downloadVideoList.size > 0) {
                                    randomdownLoadVideo(
                                        downloadVideoList.get(0).pos,
                                        downloadVideoList.get(0).videoModal,
                                        downloadVideoList.get(0).view
                                        ,
                                        downloadVideoList.get(0).loader,
                                        downloadVideoList.get(0).viewHolder
                                    )
                                    downloadVideoList.removeAt(0)
                                }

                            }
                        })
*//*

                }
            }
        }

        fun downloadVideos(pos: Int, view: ImageView, loader: ProgressBar, downloadingTxt: View) {
            videoUrlList.clear()
            videoUrlList.add(videoList.get(pos).stream_video)
            if (DownloadUtil.isAllDownload(context!!, videoUrlList)) {
                Constant.showCustomToast(context!!, "downloaded")
            } else {
                DownloadUtil.downloadWorkout(context!!, videoUrlList, loader, view, downloadingTxt)
            }
        }
    }
*/

    /*
     *
     * for save data in db
     *
     *  */
    // class to save(insert if not exist or update if exist) list in local database(Room)
    internal class saveTaskOfStreamVideo(
        data: StreamWorkoutDetailModel.Settings.Data,
        videoList: ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>
    ) : AsyncTask<Boolean, Void, String>() {

        var video_list = videoList
        var streamWorkoutName = data.stream_workout_name
        var streamWorkoutImageUrl = data.stream_workout_image_url
        var streamWorkoutImage = data.stream_workout_image
        var streamWorkoutId = data.stream_workout_id

        override fun doInBackground(vararg p0: Boolean?): String {
            var forChck = p0[0]
            //Convert Simple modal List type to jsonArrary
            //creating a localData
           /* val localStreamVideoDataModal = LocalStreamVideoDataModal()
            localStreamVideoDataModal.stream_workout_id = streamWorkoutId
            localStreamVideoDataModal.stream_workout_name = streamWorkoutName
            localStreamVideoDataModal.stream_workout_image_url = (streamWorkoutImageUrl)
            localStreamVideoDataModal.stream_workout_image = (streamWorkoutImage)

            //insert and update to database
            if (forChck!!) {
                Log.v("updateIn", "" + streamWorkoutName + "---------" + streamWorkoutId)
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().taskDao()
                    .updateStreamWorkOutList(true, streamWorkoutName, streamWorkoutId)
                for (video in videoList) {
                    val newVideoList = MyVideoList()
                    if (video.downLoadUrl != null && !video.downLoadUrl.isEmpty()) {
                        newVideoList.stream_workout_id = streamWorkoutId
                        newVideoList.stream_video_id = video.stream_video_id
                        newVideoList.stream_video_name = video.stream_video_name
                        newVideoList.stream_video_subtitle = video.stream_video_subtitle
                        newVideoList.stream_video_description = video.stream_video_description
                        newVideoList.stream_video_image = video.stream_video_image
                        newVideoList.video_duration = video.video_duration
                        newVideoList.stream_video_image_url = video.stream_video_image_url
                        newVideoList.downLoad_url = video.downLoadUrl
                        newVideoList.time_stemp_url = video.timeStempUrl
                        DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                            .taskDao().insertVideoList(newVideoList)
                        video.downLoadUrl = ""
                        getvideoList()
                    }
                }

            } else {
                Log.v("updateInsert", "" + streamWorkoutName + "---------" + streamWorkoutId)
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().taskDao()
                    .insertStreamWorkOutList(localStreamVideoDataModal)
                for (video in videoList) {
                    val newVideoList = MyVideoList()
                    if (video.downLoadUrl != null && !video.downLoadUrl.isEmpty()) {
                        newVideoList.stream_workout_id = streamWorkoutId
                        newVideoList.stream_video_id = video.stream_video_id
                        newVideoList.stream_video_name = video.stream_video_name
                        newVideoList.stream_video_subtitle = video.stream_video_subtitle
                        newVideoList.stream_video_description = video.stream_video_description
                        newVideoList.stream_video_image = video.stream_video_image
                        newVideoList.video_duration = video.video_duration
                        newVideoList.stream_video_image_url = video.stream_video_image_url
                        newVideoList.downLoad_url = video.downLoadUrl
                        newVideoList.time_stemp_url = video.timeStempUrl
                        DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                            .taskDao().insertVideoList(newVideoList)
                        video.downLoadUrl = ""
                        getvideoList()
                    }
                }
            }*/
            return ""
        }

        override fun onPostExecute(aVoid: String) {
            super.onPostExecute(aVoid)
            Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show()
        }

        private fun getvideoList() {
            class GetAllWorkoutListtt() :
                AsyncTask<Void, Void, List<MyVideoList>>() {
                override fun doInBackground(vararg voids: Void): List<MyVideoList> {
                    val taskList =
                        DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                            .taskDao().getAllVideo()
                    Log.v("WprkoutVideoList", "" + taskList)
                    return taskList
                }

                override fun onPostExecute(taskList: List<MyVideoList>) {
                    super.onPostExecute(taskList)
                }
            }
            GetAllWorkoutListtt().execute()
        }
    }

    // get particuler workout video list
    fun getAllWorkOutList(
        name: String,
        WorkoutId1: String,
        videoList: ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>){
        class GetAllWorkoutList(name: String, WorkoutId: String) :
            AsyncTask<Void, Void, List<LocalStreamVideoDataModal>>() {
            var WorkoutId = WorkoutId
            var namee = name
            override fun doInBackground(vararg voids: Void): List<LocalStreamVideoDataModal> {
                val taskList =
                    DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().taskDao()
                        .getAllWorkoutList(namee, WorkoutId)
                Log.v("Workout", "" + taskList)
                return taskList
            }

            override fun onPostExecute(taskList: List<LocalStreamVideoDataModal>) {
                super.onPostExecute(taskList)
                if (taskList != null && taskList.size != 0) {
                    forUpdate = true;
                    saveTaskOfStreamVideo(overViewTrailerData!!, videoList).execute(forUpdate)
                } else {
                    forUpdate = false;
                    saveTaskOfStreamVideo(overViewTrailerData!!, videoList).execute(forUpdate)
                }
            }
        }
        GetAllWorkoutList(name, WorkoutId1).execute()
    }

    override fun setUpdateVideoList(videoList: ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>) {
        notifyList()
    }

    fun createInputData( pos: Int,
                         status: String,
                         streamVideo: String,
                         pathget: String,
                         s2: String): Data {
        var g = Gson();
        var json = g.toJson(DownloadModel(pos, status, streamVideo, pathget, s2,0,0))
        return Data.Builder()
            .putString("list",json)
            .build()
    }

/*
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun startJob(
        pos: Int,
        status: String,
        streamVideo: String,
        pathget: String,
        s2: String

    ) {
        val myComp = ComponentName(mContext, MyJobScheduler::class.java)
        val myBuilder = JobInfo.Builder(JOBID, myComp)
        // myBuilder.setPeriodic((5 * 60 * 1000).toLong())
        myBuilder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
        myBuilder.setPersisted(true)
        myScheduler = activity!!.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

        var g = Gson();
        var json = g.toJson(DownloadModel(pos, status, streamVideo, pathget, s2,0,0))
        var bundle = PersistableBundle()
        bundle.putLong("lat", 40)
        bundle.putString("list", json)

        myjobInfo = myBuilder.setExtras(bundle).build()
        myScheduler.schedule(myjobInfo)
    }
*/

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun isJobServiceOn(context: Context): Boolean {
        var scheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        var hasBeenScheduled = false
        scheduler.getAllPendingJobs()
        for (jobInfo in scheduler.getAllPendingJobs()) {
            if (jobInfo.getId() == JOBID) {
                hasBeenScheduled = true
                break
            }
        }
        return hasBeenScheduled
    }

    class UpdateUIReceiver : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {

        }
    }


    override fun onStop() {
        super.onStop()
        if(isBound){
          //  unbindService(boundServiceConnection);
          //  isBound = false;


        }
      //  EventBus.getDefault().unregister(this)
    }

    override fun onStart() {
        super.onStart()
    }

    @Subscribe
    fun onEvent(downloadModel: DownloadModel)
    {
        Log.d("onEvent..","onEvent.."+downloadModel.progress)
        Toast.makeText(mContext, "position.."+downloadModel.progress, Toast.LENGTH_SHORT).show();
       var holder= binding.videoRv.findViewHolderForAdapterPosition(downloadModel.pos) as StreamVideoAdapter.MyViewHolder
        holder.loader.visibility = View.VISIBLE
        holder.downloadIcon.visibility = View.GONE
        holder.loader.max = downloadModel.maxProgress.toInt()
        holder.loader.setProgress(downloadModel.progress.toInt())
    }


   /*  var boundServiceConnection =  ServiceConnection(){
        @Override
         fun onServiceConnected(name:ComponentName , service:IBinder ) {

            var binderBridge =  service as (BoundService.MyBinder)
            boundService = binderBridge.getService()
            isBound = true

        }

        @Override
         fun onServiceDisconnected(name:ComponentName ) {

            isBound = false;
            boundService= null;


        }
    }*/

    private val boundServiceConnection = object: ServiceConnection {
        override fun onServiceConnected(name:ComponentName, service:IBinder) {
            val binderBridge = service as BoundService.MyBinder
            boundService = binderBridge.getService()
            isBound = true
        }
        override fun onServiceDisconnected(name:ComponentName) {
            isBound = false
            boundService = null
        }
    }
}
