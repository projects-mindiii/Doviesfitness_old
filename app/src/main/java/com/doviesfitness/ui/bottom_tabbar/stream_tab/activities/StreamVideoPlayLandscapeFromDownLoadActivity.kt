package com.doviesfitness.ui.bottom_tabbar.stream_tab.activities

import android.net.Uri
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.doviesfitness.R
import com.doviesfitness.databinding.ActivityStreamVideoPlayLandscapeBinding
import com.doviesfitness.databinding.ActivityStreamVideoPlayLandscapeFromDownLoadBinding
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.bottom_tabbar.stream_tab.adapters.StreamListAdapter
import com.doviesfitness.ui.bottom_tabbar.stream_tab.download_background.DownloadedModal
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.StreamWorkoutDetailModel
import com.doviesfitness.ui.room_db.DatabaseClient
import com.doviesfitness.ui.room_db.GithubTypeConverters
import com.doviesfitness.ui.room_db.LocalStream
import com.doviesfitness.ui.room_db.MyVideoList
import com.doviesfitness.utils.Constant
import com.facebook.FacebookSdk
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_video_detail.*


class StreamVideoPlayLandscapeFromDownLoadActivity : BaseActivity(), View.OnClickListener,
StreamListAdapter.OnVideoClick {
    lateinit var binding: ActivityStreamVideoPlayLandscapeFromDownLoadBinding
    lateinit var exerciseList: ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>
    private var player: SimpleExoPlayer? = null
    //  private var player: SimpleExoPlayer? = null
    private lateinit var mediaDataSourceFactory: DataSource.Factory
    private val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory()
    private val ivHideControllerButton: LinearLayout by lazy {
        findViewById<LinearLayout>(R.id.controller)
    }
    private var lastSeenTrackGroupArray: TrackGroupArray? = null
    private var trackSelector: DefaultTrackSelector? = null
    private var dataSourceFactory: DefaultDataSourceFactory? = null
    private var currentWindow: Int = 0
    private var playbackPosition: Long = 0
    private var previusPos: Int = 0
    lateinit var adapter: StreamListAdapter
    var workout_id = ""
    var local = ""
    var ready = true
    private lateinit var localStreamList: ArrayList<DownloadedModal.ProgressModal>
    private lateinit var allVideoList: ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>
    // Listener to seek video at first time and to play next video video automaticaly one by one
    var eventListener = object : Player.EventListener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            when (playbackState) {
                Player.STATE_IDLE -> {
                    Log.d("player state", "player state...STATE_IDLE")
                }
                Player.STATE_BUFFERING -> {
                    Log.d("player state", "player state...STATE_BUFFERING")
                }
                Player.STATE_READY -> {
                    if (ready) {
                        ready = false
                        player?.seekTo(allVideoList.get(0).seekTo)
                        Log.d("player state", "player state...STATE_READY")
                    }
                }
                Player.STATE_ENDED -> {
                    Log.d("player state", "player state...STATE_ENDED")
                    if (previusPos < (allVideoList.size - 1)) {
                        releasePlayer()
                        adapter.notifyItemChanged(previusPos)
                        previusPos = previusPos + 1
                        if (allVideoList.get(previusPos).seekTo >= (allVideoList.get(previusPos).MaxProgress * 1000L)) {
                            allVideoList.get(previusPos).seekTo = 0
                            adapter.notifyItemChanged(previusPos)
                        }
                        intializePlayer(allVideoList.get(previusPos).downLoadUrl, previusPos)
/*
                        if (allVideoList.get(previusPos).downLoadUrl != null && !allVideoList.get(previusPos).downLoadUrl.isEmpty()) {
                            */
/*
                              it depend on us we have userd in
                              in PRDounloder thaswhy we use it her if you remove it
                              from then remove also from there download player also {StreamVideoFragment}
                            *//*

                            intializePlayer(allVideoList.get(previusPos).downLoadUrl, previusPos)
                        } else {
                            intializePlayer(allVideoList.get(previusPos).stream_video, previusPos)
                        }
*/
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        hideNavStatusBar()
        super.onCreate(savedInstanceState)
        inItView()
    }

    private fun inItView() {
        allVideoList = ArrayList()
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_stream_video_play_landscape_from_down_load)
        landscapeFunctionality()

        if (intent.hasExtra("localStreamList")) {
            if (intent.getSerializableExtra("localStreamList") != null) {
                localStreamList = intent.getSerializableExtra("localStreamList") as ArrayList<DownloadedModal.ProgressModal>

                for (i in 0..localStreamList.size - 1) {
                    var localListDataModal = localStreamList.get(i)

                    var MaxProgress =
                        Constant.getExerciseTime(localListDataModal.video_duration)


                    val videoModal = StreamWorkoutDetailModel.Settings.Data.Video(
                        stream_video = "",
                        stream_video_description = localListDataModal.stream_video_description!!,
                        stream_video_id = localListDataModal.stream_video_id!!,
                        video_duration = localListDataModal.video_duration!!,
                        stream_video_image = localListDataModal.stream_video_image!!,
                        stream_video_image_url = localListDataModal.stream_video_image_url!!,
                        stream_video_name = localListDataModal.stream_video_name!!,
                        stream_video_subtitle = localListDataModal.stream_video_subtitle!!,
                        order = 1,
                        Progress = 0,

                        MaxProgress = MaxProgress,
                        seekTo = 0,
                        isPlaying = false,
                        isComplete = false,
                        isRestTime = false,
                        downLoadUrl = localListDataModal.downLoadUrl,
                        timeStempUrl = "",
                        hls_video = null,
                        mp4_video = null,is_workout =  localListDataModal.is_workout,
                            view_type = localListDataModal.view_type
                    )
                    allVideoList.add(videoModal)
                }
            }
        }
        workout_id = intent.getStringExtra("workout_id")!!
        local = intent.getStringExtra("local")!!
        initialisation()
    }

    fun hideNavStatusBar() {
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        )
    }

    private fun initialisation() {
        binding.episodes.setOnClickListener(this)
        binding.previous.setOnClickListener(this)
        binding.next.setOnClickListener(this)
        binding.videoRv.layoutManager = LinearLayoutManager(getActivity())

        //video list
        adapter = StreamListAdapter(getActivity(), allVideoList, this)
        binding.videoRv.adapter = adapter
        previusPos = 0

        // intializePlayer(exerciseList.get(0).stream_video, 0)
        player?.addListener(eventListener)
        // binding.videoRv.visibility = View.VISIBLE

        //getWorkVideoListUsingId(StreamDetailActivity.overViewTrailerData!!.stream_workout_id)
    }

    // method to initialise player and play video
    fun intializePlayer(news_video: String, pos: Int) {
        trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)
        var dataSourceFactory =
            DefaultDataSourceFactory(this, Util.getUserAgent(this, "mediaPlayerSample"))
        var dataSourceFactory1 = CacheDataSourceFactory(VideoCache.getInstance(), dataSourceFactory)
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector!!)

        val mediaSource = ExtractorMediaSource.Factory(dataSourceFactory1)
            .createMediaSource(Uri.parse(news_video))

/*
        val mediaSource = ExtractorMediaSource.Factory(dataSourceFactory1)
            .createMediaSource(Uri.parse("/storage/emulated/0/Android/data/stream/files/Download/Dovies/XIfCUVi2PYwLDnAO.mp4"))
*/

        Log.v("news_video", "" + news_video)
        with(player) {
            this?.prepare(mediaSource, false, false)
            this?.playWhenReady = true
        }
        ivHideControllerButton.visibility = View.VISIBLE
        playerView.showController()
        playerView.setShowBuffering(1)
        playerView.setShutterBackgroundColor(ContextCompat.getColor(this, R.color.colorBlack))
        playerView.player = player
        playerView.requestFocus()
        player?.seekTo(allVideoList.get(pos).seekTo)
        lastSeenTrackGroupArray = null
        player?.addListener(eventListener)
    }

    // method to play video on list item click
    override fun onVideoClick(pos: Int) {
        if (previusPos != pos) {
            releasePlayer()
            adapter.notifyItemChanged(previusPos)
            if (allVideoList.get(pos).seekTo >= (allVideoList.get(pos).MaxProgress * 1000L)) {
                allVideoList.get(pos).seekTo = 0
                adapter.notifyItemChanged(pos)
            }

            intializePlayer(allVideoList.get(pos).downLoadUrl , pos)

            /* if (allVideoList.get(previusPos).downLoadUrl != null && !allVideoList.get(previusPos).downLoadUrl.isEmpty()) {
                 intializePlayer(allVideoList.get(pos).downLoadUrl , pos)
             }
             else {
                 intializePlayer(allVideoList.get(pos).stream_video, pos)
             }*/
            previusPos = pos
        }
    }

    //get current position of video
    private fun updateStartPosition() {
        try {
            if (player != null)
                with(player) {
                    allVideoList.get(previusPos).seekTo = player!!.currentPosition
                    currentWindow = player!!.currentWindowIndex
                    player!!.playWhenReady = true
                }
        } catch (e: java.lang.Exception) {
        }
    }

    // release player
    private fun releasePlayer() {
        if (player != null) {
            updateStartPosition()
            player?.playWhenReady = false
            player?.stop()
            player?.release()
            trackSelector = null
        }
    }

    public override fun onStop() {
        //  exerciseList.get(previusPos).seekTo = player!!.currentPosition
        var UName = getDataManager().getUserInfo().customer_user_name
      //  SaveTask(allVideoList, UName, workout_id, local).execute()

        super.onStop()
        if (Util.SDK_INT > 23) releasePlayer()
    }

    fun landscapeFunctionality() {
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        val params =
            binding.playerView.getLayoutParams() as RelativeLayout.LayoutParams
        val RParams =
            binding.videoRv.getLayoutParams() as RelativeLayout.LayoutParams

        if (metrics.widthPixels == 3040 || metrics.heightPixels == 1440 || metrics.widthPixels > 3040 || metrics.heightPixels > 1440) {
            RParams.setMargins(resources.getDimension(R.dimen._25sdp).toInt(), 0, 0, 0)
            binding.videoRv.setLayoutParams(RParams)
            var percent = (10 * metrics.widthPixels) / 100
            params.width = (metrics.widthPixels) - percent
            params.height = metrics.heightPixels
            params.setMargins(resources.getDimension(R.dimen._25sdp).toInt(), 0, 0, 0)
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP)
            binding.playerView.setLayoutParams(params)

        } else if (metrics.heightPixels == 720 && metrics.widthPixels > 1280) {
            var margin = (metrics.widthPixels - 1280)
            RParams.setMargins(100, 0, 0, 0)
            binding.videoRv.setLayoutParams(RParams)
            var VWidth = (1280 + metrics.heightPixels - 720)
            var VParams = RelativeLayout.LayoutParams(VWidth, metrics.heightPixels)
            VParams.addRule(RelativeLayout.CENTER_IN_PARENT)
            binding.playerView.setLayoutParams(VParams)
        } else if (metrics.heightPixels <= 720 && metrics.widthPixels <= 1280) {
            params.width = metrics.widthPixels
            params.height = metrics.heightPixels
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP)
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            binding.playerView.setLayoutParams(params)
        } else if (metrics.heightPixels == 1080 && metrics.widthPixels > 1280) {
            RParams.setMargins(resources.getDimension(R.dimen._25sdp).toInt(), 0, 0, 0)
            binding.videoRv.setLayoutParams(RParams)
            var percent = (10 * metrics.widthPixels) / 100
            params.width = (metrics.widthPixels) - percent
            params.height = metrics.heightPixels
            params.setMargins(resources.getDimension(R.dimen._25sdp).toInt(), 0, 0, 0);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP)
            params.addRule(RelativeLayout.CENTER_HORIZONTAL)
            binding.playerView.setLayoutParams(params)
        } else {
            RParams.setMargins(resources.getDimension(R.dimen._25sdp).toInt(), 0, 0, 0)
            binding.videoRv.setLayoutParams(RParams)
            var percent = (10 * metrics.widthPixels) / 100
            params.width = (metrics.widthPixels) - percent
            params.height = metrics.heightPixels
            params.setMargins(resources.getDimension(R.dimen._25sdp).toInt(), 0, 0, 0);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP)
            params.addRule(RelativeLayout.CENTER_HORIZONTAL)
            binding.playerView.setLayoutParams(params)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                onBackPressed()
            }

            R.id.next -> {


                if (previusPos < (allVideoList.size - 1)) {
                    releasePlayer()
                    adapter.notifyItemChanged(previusPos)
                    previusPos = previusPos + 1
                    if (allVideoList.get(previusPos).seekTo >= (allVideoList.get(previusPos).MaxProgress * 1000L)) {
                        allVideoList.get(previusPos).seekTo = 0
                        adapter.notifyItemChanged(previusPos)
                    }

                    if (allVideoList.get(previusPos).downLoadUrl != null && !allVideoList.get(previusPos).downLoadUrl.isEmpty()) {
                        intializePlayer(
                            allVideoList.get(previusPos).downLoadUrl ,
                            previusPos
                        )
                    } else {
                        intializePlayer(allVideoList.get(previusPos).stream_video, previusPos)
                    }
                }
            }
            R.id.previous -> {
                if (previusPos > 0) {
                    releasePlayer()
                    adapter.notifyItemChanged(previusPos)
                    previusPos = previusPos - 1
                    if (allVideoList.get(previusPos).seekTo >= (allVideoList.get(previusPos).MaxProgress * 1000L)) {
                        allVideoList.get(previusPos).seekTo = 0
                        adapter.notifyItemChanged(previusPos)
                    }
                    //intializePlayer(exerciseList.get(previusPos).stream_video, previusPos)
                    if (allVideoList.get(previusPos).downLoadUrl != null && !allVideoList.get(previusPos).downLoadUrl.isEmpty()) {
                        intializePlayer(allVideoList.get(previusPos).downLoadUrl ,
                            previusPos
                        )
                    } else {
                        intializePlayer(allVideoList.get(previusPos).stream_video, previusPos)
                    }
                }
            }
            R.id.episodes -> {
                if (binding.videoRv.visibility == View.VISIBLE)
                    binding.videoRv.visibility = View.GONE
                else {
                    binding.videoRv.visibility = View.VISIBLE
                    allVideoList.get(previusPos).seekTo = player!!.currentPosition
                    adapter.notifyItemChanged(previusPos)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onResume() {
        if (binding.playerView != null) {
            //intializePlayer(exerciseList.get(previusPos).stream_video, previusPos)

            if (allVideoList.get(previusPos).downLoadUrl != null && !allVideoList.get(previusPos).downLoadUrl.isEmpty()) {
                intializePlayer(allVideoList.get(previusPos).downLoadUrl ,
                    previusPos
                )
            } else {
                intializePlayer(allVideoList.get(previusPos).stream_video, previusPos)
            }
        }
        super.onResume()
    }

/*
    val path =
        Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" + packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + "/" + downloadFileName
*/

    override fun onPause() {
        if (binding.playerView != null) {
            allVideoList.get(previusPos).seekTo = player!!.currentPosition
            releasePlayer()
        }
        super.onPause()
    }

    // class to save(insert if not exist or update if exist) list in local database(Room)
    internal class SaveTask(
        list: ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>,
        Name: String,
        workout_id: String,
        local: String
    ) : AsyncTask<Void, Void, String>() {
        var Wlist = list
        var Uname = Name
        var workoutId = workout_id
        var local = local
        override fun doInBackground(vararg params: Void): String {

            //Convert Simple modal List type to jsonArrary
           // val WStr = GithubTypeConverters.someObjectListToString(Wlist)
            //creating a localData
           /* val localData = LocalStream()
            localData.setTask("" + Uname)
            localData.workoutId = workoutId
            localData.setWList(WStr)
            localData.setUsername(Uname)
            localData.setFinished(false)
            //adding to database
            if (local != null && !local.isEmpty() && local.equals("yes")) {
                DatabaseClient.getInstance(FacebookSdk.getApplicationContext()).getAppDatabase().taskDao()
                    .updateList(WStr, Uname, workoutId)
                //DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().taskDao().updateStreamList(WStr, Uname, workoutId)
            } else {
                DatabaseClient.getInstance(FacebookSdk.getApplicationContext()).getAppDatabase().taskDao()
                    .insert(localData)
            }*/
            return ""
        }

        override fun onPostExecute(aVoid: String) {
            super.onPostExecute(aVoid)
            // Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show()
        }
    }


    //get all workoutsvideo list
    public fun getWorkVideoListUsingId(id: String) {

        class GetAllWorkoutListtt() :
            AsyncTask<Void, Void, List<MyVideoList>>() {
            override fun doInBackground(vararg voids: Void): List<MyVideoList> {
                val taskList =
                    DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().taskDao()
                        .getAllMyWorkOutListVideo(id)
                Log.v("ListActivity", "" + taskList)
                return taskList
            }

            override fun onPostExecute(taskList: List<MyVideoList>) {
                super.onPostExecute(taskList)
            }
        }
        GetAllWorkoutListtt().execute()
    }
}
