package com.doviesfitness.ui.bottom_tabbar.stream_tab.activities

import android.app.Activity
import android.app.Dialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.AudioManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.provider.MediaStore
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.provider.Settings.System
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.databinding.ActivityStreamVideoPlayLandscapeBinding
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.bottom_tabbar.stream_tab.adapters.StreamListAdapter
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.StreamWorkoutDetailModel
import com.doviesfitness.utils.CommanUtils
import com.doviesfitness.utils.Constant
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
import kotlinx.android.synthetic.main.stream_exo_playback_control_view.view.*
import java.util.*
import java.util.concurrent.TimeUnit


class StreamVideoPlayUrlActivity : BaseActivity(), View.OnClickListener,
    StreamListAdapter.OnVideoClick {
    private lateinit var audioManager: AudioManager
    var timer: CountDownTimer? = null
    lateinit var cResolver: ContentResolver

    lateinit var binding: ActivityStreamVideoPlayLandscapeBinding
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
    var trailer = ""
    var isVideoDownloaded = 0
    var LayoutVisibility = false
    var isPause = false
    var ready = true
    var seekbarTuch = false
    // Listener to seek video at first time and to play next video video automaticaly one by one


    var remainingRunnable = Runnable {
        updateProgress()

    }
    lateinit var mHandler: Handler

    fun updateProgress() {

        if (player!=null&&player!!.duration > 0) {
            var millis = player!!.duration - player!!.currentPosition

            Log.d("time in mili", "time in mili..." + millis)
       var hrs=     TimeUnit.MILLISECONDS.toHours(millis)
        var mins=    TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis))
        var secs=    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
            var hms=""
            if (compareValues(hrs,0)==0){

                hms = String.format("%02d:%02d",mins,secs)

/*
                if ( compareValues(mins,0)==0 ){
                    hms = String.format(
                        "%02d",secs)
                }
                else {
                     hms = String.format("%02d:%02d",mins,secs)
                 }
*/
            }

            else {
                hms = String.format("%02d:%02d:%02d", hrs,mins,secs )
            }

            if (millis > 0)
                binding.playerView.remaining_time.text = "" + hms
            binding.remainingTime1.text = "" + hms

        }
        binding.playerView.exo_play.visibility = View.GONE
        binding.playerView.exo_pause.visibility = View.GONE
        var delayMs = TimeUnit.SECONDS.toMillis(1);
        mHandler.postDelayed(remainingRunnable, delayMs);

    }

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
                       // player?.seekTo(exerciseList.get(0).seekTo)
                        player?.seekTo(0)
                        Log.d("player state", "player state...STATE_READY")
                    }
                }
                Player.STATE_ENDED -> {
                    Log.d("player state", "player state...STATE_ENDED")
                    if (previusPos < (exerciseList.size - 1)) {
                        // releasePlayer()
                        releasePlayerWithoutrestart()
                        adapter.notifyItemChanged(previusPos)
                        previusPos = previusPos + 1
                        if (exerciseList.get(previusPos).seekTo >= (exerciseList.get(previusPos).MaxProgress * 1000L)) {
                            exerciseList.get(previusPos).seekTo = 0
                            adapter.notifyItemChanged(previusPos)
                        }

                        if (exerciseList.get(previusPos).stream_video_name!=null&& !exerciseList.get(previusPos).stream_video_name.isEmpty()) {
                            var titlestr = exerciseList.get(previusPos).stream_video_name
                            val cap: String =
                                titlestr.substring(0, 1).toUpperCase() + titlestr.substring(1)
                            binding.title.text = cap
                        }
                        if (exerciseList.get(previusPos).downLoadUrl!=null&&!exerciseList.get(previusPos).downLoadUrl.isEmpty())
                            intializePlayer(exerciseList.get(previusPos).downLoadUrl, previusPos,1,0)
                        else {
                            intializePlayer(exerciseList.get(previusPos).stream_video, previusPos,0,0)

/*
                            if (CommanUtils.isNetworkAvailable(getActivity())!!)
                                intializePlayer(exerciseList.get(previusPos).stream_video, previusPos)
                            else
                                Constant.showInternateConnectionDialog(getActivity())
*/

                        }

                        if (previusPos < (exerciseList.size - 1)) {
                            binding.nextLayout.visibility = View.VISIBLE
                        } else {
                            binding.nextLayout.visibility = View.INVISIBLE
                        }
                        if (previusPos > 0) {
                            binding.previousLayout.visibility = View.VISIBLE
                        } else {
                            binding.previousLayout.visibility = View.INVISIBLE

                        }
                        if (exerciseList.size > 1) {
                            binding.videoLayout.visibility = View.VISIBLE
                        } else {
                            binding.videoLayout.visibility = View.INVISIBLE

                        }


                    } else {
                        finish()
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        hideNavStatusBar()
        super.onCreate(savedInstanceState)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE)


        //  getWindow().addFlags(WindowManager.LayoutParams.TYPE_PRIVATE_PRESENTATION)

        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_stream_video_play_landscape)
        //  ((SurfaceView())binding.playerView.getVideoSurfaceView()).setSecure(true);


        var surfaceView = binding.playerView.videoSurfaceView as SurfaceView
        surfaceView.setSecure(true)


        landscapeFunctionality()
        exerciseList =
            intent.getSerializableExtra("videoList") as ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>
        workout_id = intent.getStringExtra("workout_id")!!
        local = intent.getStringExtra("local")!!
        trailer = intent.getStringExtra("trailer")!!
        previusPos = 0
        if (intent.hasExtra("position")!!)
            previusPos = intent.getIntExtra("position", 0)
        if (intent.hasExtra("media_name")) {
            var media_name = intent.getStringExtra("media_name")
            if (media_name != null && !media_name.isEmpty()) {
                binding.episodes.text = "" + media_name
                binding.previous.text = "Previous " + media_name
                binding.next.text = "Next " + media_name
            }
        } else {
            binding.episodes.text = "" + StreamDetailActivity.overViewTrailerData!!.media_title_name
            binding.previous.text =
                "Previous " + StreamDetailActivity.overViewTrailerData!!.media_title_name
            binding.next.text =
                "Next " + StreamDetailActivity.overViewTrailerData!!.media_title_name
        }
        initialisation()
        //  brightnessSeekbar()
        vollumeControls()
        timer = object : CountDownTimer(5000, 10) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                if (!seekbarTuch) {
                    binding.backword.visibility = View.GONE
                    binding.ivForword.visibility = View.GONE
                    binding.seekBar.visibility = View.INVISIBLE
                    binding.vollumeIcon.visibility = View.GONE

                    binding.title.visibility = View.GONE
                    binding.episodeLayout.visibility = View.GONE
                    binding.transparentLayout.visibility = View.GONE
                    binding.ivPlay.visibility = View.GONE
                    binding.ivPause.visibility = View.GONE

                    binding.ivBack.visibility = View.GONE
                    binding.ivMusic.visibility = View.GONE

                    if (binding.videoRv.visibility == View.GONE) {
                        ivHideControllerButton.visibility = View.GONE
                    }
                }
                // binding.videoRv.visibility=View.GONE
                cancel()

            }
        }


        val windowBackground = window.decorView.background

        //    binding.playerView.exo_progress.

/*
        binding.topBlurView
            .setupWith(binding.mainLayout)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(this))
            .setBlurRadius(25f)
            .setHasFixedTransformationMatrix(true)
*/


    }


    fun vollumeControls() {
        try {

            audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),0)
            binding.seekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            //binding.seekBar.setProgress(6);
            binding.seekBar.setProgress(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            var currentvolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
            var PVal = (currentvolume / 15.0)
            Log.d("player max vollume", "player max vollume..." + (audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)))
            player?.volume = 0.5f

/*
            if (currentvolume / 15 == 1)
                player?.volume = 1.0f
            else
                player?.volume = PVal.toFloat()
*/
            binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {

                    var PVal = (progress / 15.0)
                    Log.d("player seekbar vollume", "player seekbar vollume..." + PVal)

                    if (progress / 15 == 1)
                        player?.volume = 1.0f
                    else
                        player?.volume = PVal.toFloat()

                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    Log.d("seekbar vollume", "seekbar onStartTrackingTouc...")

                    seekbarTuch = true

                    /*    binding.transparentLayout.visibility = View.VISIBLE

                        if (isPause) {
                            binding.ivPlay.visibility = View.VISIBLE
                            binding.ivPause.visibility = View.GONE
                        } else {
                            binding.ivPause.visibility = View.VISIBLE
                            binding.ivPlay.visibility = View.GONE
                        }

                        binding.backword.visibility = View.VISIBLE
                        binding.ivForword.visibility = View.VISIBLE
                        binding.vollumeIcon.visibility = View.VISIBLE


                        if (trailer != null && trailer.equals("yes")) {
                            binding.episodeLayout.visibility = View.GONE
                            binding.title.visibility = View.GONE
                        } else {
                            binding.episodeLayout.visibility = View.VISIBLE
                            binding.title.visibility = View.VISIBLE

                            if (previusPos < (exerciseList.size - 1)) {
                                binding.nextLayout.visibility = View.VISIBLE
                            } else {
                                binding.nextLayout.visibility = View.INVISIBLE
                            }
                            if (previusPos > 0) {
                                binding.previousLayout.visibility = View.VISIBLE
                            } else {
                                binding.previousLayout.visibility = View.INVISIBLE

                            }
                        }



                        ivHideControllerButton.visibility = View.VISIBLE


                        binding.playerView.useController = true
                        binding.playerView.controller.visibility = View.VISIBLE
                        binding.playerView.showController()
                        binding.playerView.exo_play.visibility = View.GONE
                        binding.playerView.exo_pause.visibility = View.GONE
                        binding.ivBack.visibility = View.VISIBLE
                        binding.ivMusic.visibility = View.VISIBLE
*/


                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {

                    seekbarTuch = false
                    Log.d("seekbar vollume", "seekbar onStopTrackingTouch...")
                    binding.backword.visibility = View.GONE
                    binding.ivForword.visibility = View.GONE
                    binding.seekBar.visibility = View.INVISIBLE
                    binding.vollumeIcon.visibility = View.GONE

                    binding.title.visibility = View.GONE
                    binding.episodeLayout.visibility = View.GONE
                    binding.transparentLayout.visibility = View.GONE
                    binding.ivPlay.visibility = View.GONE
                    binding.ivPause.visibility = View.GONE

                    binding.ivBack.visibility = View.GONE
                    binding.ivMusic.visibility = View.GONE

                    if (binding.videoRv.visibility == View.GONE) {
                        ivHideControllerButton.visibility = View.GONE
                    }

                }

            })

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        Log.d("keyCode", "keyCode down..." + keyCode)
        var vol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {

            vol = vol + 1
            audioManager.setStreamVolume(
                AudioManager.STREAM_MUSIC, vol, 0
            )
            var index = binding.seekBar.getProgress();

            if (index / 15 == 1)
                player?.volume = 1.0f
            else{
                var PVal = (index / 15.0)
                player?.volume = (PVal).toFloat()
                println(""+PVal+"--Pval and index value --"+index)
            }

            binding.seekBar.setProgress(index + 1)
            // var progress = binding.seekBar.getProgress()
            //  var PVal=(progress/15.0)
            //  Log.d("player seekbar vollume","player seekbar hardware..."+PVal)

            // player?.volume= PVal.toFloat()
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            vol = vol - 1
            audioManager.setStreamVolume(
                AudioManager.STREAM_MUSIC, vol, 0
            )

            var index = binding.seekBar.getProgress();

            if (index / 15 == 1)
                player?.volume = 1.0f
            else{
                var PVal = (index / 15.0)
                player?.volume = (PVal).toFloat()
                println(""+PVal+"--Pval and index value --"+index)
            }

            binding.seekBar.setProgress(index - 1)
            //  var progress = binding.seekBar.getProgress()
            //  var PVal=(progress/15.0)
            //   Log.d("player seekbar vollume","player seekbar hardware..."+PVal)

            //   player?.volume= PVal.toFloat()
            return true;
        }
        return false
    }


    private fun brightnessSeekbar() {
        cResolver = getContentResolver();
        var window = getWindow();
        binding.seekBar.setMax(255)
        var brightness = 100
        try {
            brightness = System.getInt(cResolver, System.SCREEN_BRIGHTNESS);
        } catch (e: SettingNotFoundException) {
            Log.e("Error", "Cannot access system brightness");
            e.printStackTrace();
        }
        binding.seekBar.setProgress(brightness)
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (progress <= 20) {
                    brightness = 20
                } else {
                    brightness = progress
                }
                //Calculate the brightness percentage
                // var perc = (brightness / 255 * 100).toFloat()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                var retVal = checkSystemWritePermission()
                Log.d(tag, "brightness...: " + brightness);
                if (retVal) {
                    System.putInt(cResolver, System.SCREEN_BRIGHTNESS, brightness);
                    var layoutpars = window!!.getAttributes();
                    layoutpars.screenBrightness = brightness.toFloat();
                    window.setAttributes(layoutpars)

                } else {
                    var intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                    intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                    startActivity(intent);
                }
            }
        })
    }

    fun checkSystemWritePermission(): Boolean {
        var retVal = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            retVal = Settings.System.canWrite(this);
            Log.d(tag, "Can Write Settings: " + retVal);
        }
        return retVal

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
        binding.videoLayout.setOnClickListener(this)
        binding.previousLayout.setOnClickListener(this)
        binding.nextLayout.setOnClickListener(this)
        binding.backword.setOnClickListener(this)
        binding.ivForword.setOnClickListener(this)
        binding.playerView.setOnClickListener(this)
        binding.ivBack.setOnClickListener(this)
        binding.ivMusic.setOnClickListener(this)
        binding.ivPlay.setOnClickListener(this)
        binding.ivPause.setOnClickListener(this)
        binding.vollumeIcon.setOnClickListener(this)
        binding.videoRv.layoutManager = LinearLayoutManager(getActivity())
        adapter = StreamListAdapter(getActivity(), exerciseList, this)
        binding.videoRv.adapter = adapter
        binding.playerView.controllerShowTimeoutMs = 30000
        binding.playerView.controllerHideOnTouch = false
        binding.playerView.exo_play.visibility = View.GONE
        binding.playerView.exo_pause.visibility = View.GONE
        mHandler = Handler()
        mHandler.post(remainingRunnable)


        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                it.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                      //  Toast.makeText(getActivity(),"conn gain",Toast.LENGTH_SHORT).show()
                      //  showInternateConnectionDialog1(getActivity())
                        //take action when network connection is gained
                    }
                    override fun onLost(network: Network) {

                        runOnUiThread {
                            try {
                                if (isVideoDownloaded==0) {
                                    isPause = true
                                    binding.playerView.exo_pause.performClick()
                                    //   binding.ivPlay.visibility = View.VISIBLE
                                    binding.ivPause.visibility = View.GONE
                                    binding.playerView.exo_play.visibility = View.GONE
                                    binding.playerView.exo_pause.visibility = View.GONE
                                    window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                                    Constant.requestAudioFocusForMyApp(getActivity())
                                    showInternateConnectionDialog1(getActivity())
                                }

                            }
                            catch (ex:java.lang.Exception){
                                ex.printStackTrace()
                            }
                        }

/*
                        Handler().postDelayed(Runnable {
                            if (isVideoDownloaded==0) {
                                isPause = true
                                binding.playerView.exo_pause.performClick()
                                binding.ivPlay.visibility = View.VISIBLE
                                binding.ivPause.visibility = View.GONE
                                binding.playerView.exo_play.visibility = View.GONE
                                binding.playerView.exo_pause.visibility = View.GONE
                                window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                                Constant.requestAudioFocusForMyApp(getActivity())
                                showInternateConnectionDialog1(getActivity())
                            }
                        },500)
*/

                       // Toast.makeText(getActivity(),"conn lost",Toast.LENGTH_SHORT).show()
                        //take action when network connection is lost
                    }
                })
            }
        }


        // binding.playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
        //   binding.playerView.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);


/*
        ivHideControllerButton.setOnSystemUiVisibilityChangeListener(object :View.OnSystemUiVisibilityChangeListener{
            override fun onSystemUiVisibilityChange(visibility: Int) {
                Log.d("visibility", "onSystemUiVisibilityChange..." +visibility)

            }

        })
*/

        //  Blurry.with(this).radius(25).sampling(2).color(Color.argb(66, 0, 255, 255)).onto(binding.playerView)

        /*  Blurry.with(this)
              .radius(25)
              .sampling(1)
              .color(Color.argb(66, 0, 255, 255))
              .async()
              .capture(findViewById(R.id.playerView))
              .into(findViewById(R.id.playerView))

          Blurry.with(this)
              .radius(10)
              .sampling(8)
              .async()
              .capture(findViewById(R.id.transparent_layout))
              .into(findViewById(R.id.transparent_layout))

          Blurry.with(this)
              .radius(25)
              .sampling(1)
              .color(Color.argb(66, 255, 255, 0))
              .async()
              .capture(findViewById(R.id.episode_layout))
              .into(findViewById(R.id.episode_layout))
  */

        // intializePlayer(exerciseList.get(0).stream_video, 0)
        player?.addListener(eventListener)
        //   binding.videoRv.visibility = View.VISIBLE

    }

    // method to initialise player and play video
    fun intializePlayer(news_video: String, pos: Int,isDownload:Int, idForSeekTo: Int) {
        isVideoDownloaded=isDownload
        //   binding.backword.visibility = View.VISIBLE
        //  binding.ivForword.visibility = View.VISIBLE

        trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)
        var dataSourceFactory =
            DefaultDataSourceFactory(this, Util.getUserAgent(this, "mediaPlayerSample"))
        var dataSourceFactory1 = CacheDataSourceFactory(VideoCache.getInstance(), dataSourceFactory)
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector!!)



        val mediaSource = ExtractorMediaSource.Factory(dataSourceFactory1)
            .createMediaSource(Uri.parse(news_video))



/*
        val mediaSource = ExtractorMediaSource.Factory(DefaultExtractorsFactory(this))
            .createMediaSource(Uri.parse("https://s3.us-east-2.amazonaws.com/dev.koobi.co.uk/feeds/4n2sc9lkfc74j6a.mp4"))
*/
      /*  var uri = Uri.parse("https://s3.us-east-2.amazonaws.com/dev.koobi.co.uk/feeds/4n2sc9lkfc74j6a.mp4")

        val mediaSource = ExtractorMediaSource.Factory(dataSourceFactory)
            .setExtractorsFactory(DefaultExtractorsFactory())
            .createMediaSource(uri)
*/

/*
        val mediaSource = ExtractorMediaSource.Factory(dataSourceFactory1)
            .createMediaSource(Uri.parse("https://dovies-fitness-dev.s3.us-east-2.amazonaws.com/cover_image/2.77323047383155.m3u8"))
*/
        with(player) {
            this?.prepare(mediaSource, false, false)
            this?.playWhenReady = true

            //this?.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
        }


        // ivHideControllerButton.visibility = View.VISIBLE
        //  playerView.showController()
        playerView.setShowBuffering(1!!)
        playerView.setShutterBackgroundColor(ContextCompat.getColor(this, R.color.colorBlack))
        playerView.player = player
        playerView.requestFocus()
        // playerView.player.seekTo(exerciseList.get(pos).seekTo)
        if(idForSeekTo ==0){
            player?.seekTo(0)
        }
        else{
            player?.seekTo(exerciseList.get(pos).seekTo)
        }

        lastSeenTrackGroupArray = null
        player?.addListener(eventListener)
        var progress = binding.seekBar.getProgress()
        var PVal = (progress / 15.0)
        player?.volume = PVal.toFloat()
        binding.playerView.exo_play.visibility = View.GONE
        binding.playerView.exo_pause.visibility = View.GONE

        if (isDownload==0) {
            if (CommanUtils.isNetworkAvailable(getActivity())!!) {
            }
            else {
                isPause = true
                binding.playerView.exo_pause.performClick()
                binding.ivPlay.visibility = View.VISIBLE
                binding.ivPause.visibility = View.GONE
                binding.playerView.exo_play.visibility = View.GONE
                binding.playerView.exo_pause.visibility = View.GONE
                window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                Constant.requestAudioFocusForMyApp(this)
                showInternateConnectionDialog1(getActivity())
            }
        }

    }

    fun showInternateConnectionDialog1(activity: Activity) {

        try {
            val dialog = Dialog(activity)

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)

            dialog.setCanceledOnTouchOutside(true)
            dialog.setContentView(R.layout.no_internate_connection_view)
            val tvOk = dialog.findViewById<TextView>(R.id.tv_ok)
            val ll_ok = dialog.findViewById<LinearLayout>(R.id.ll_ok)

            ll_ok.setOnClickListener { v ->
                dialog.dismiss()
                //  onBackPressed()
            }

            dialog.show()

        }
        catch (Ex:java.lang.Exception){
            Ex.printStackTrace()
        }
    }


    // method to play video on list item click
    override fun onVideoClick(pos: Int) {
        binding.videoRv.visibility = View.GONE
        binding.topBlurView.visibility = View.GONE
        ivHideControllerButton.visibility = View.GONE
        //  binding.topBlurView1.visibility = View.GONE
        if (previusPos != pos) {

            releasePlayer()
            adapter.notifyItemChanged(previusPos)
            if (exerciseList.get(pos).seekTo >= (exerciseList.get(pos).MaxProgress * 1000L)) {
                exerciseList.get(pos).seekTo = 0
                adapter.notifyItemChanged(pos)
            }

            if (exerciseList.get(pos).stream_video_name!=null&& !exerciseList.get(pos).stream_video_name.isEmpty()) {
                var titlestr = exerciseList.get(pos).stream_video_name
                val cap: String =
                    titlestr.substring(0, 1).toUpperCase() + titlestr.substring(1)
                binding.title.text = cap
            }


         //   binding.title.text = exerciseList.get(pos).stream_video_name

            if (exerciseList.get(pos).downLoadUrl!=null&&!exerciseList.get(pos).downLoadUrl.isEmpty())
                intializePlayer(exerciseList.get(pos).downLoadUrl, pos,1,0)
            else {
                intializePlayer(exerciseList.get(pos).stream_video, pos,0,0)
            }

            previusPos = pos
        } else {
            binding.playerView.exo_play.performClick()
            binding.playerView.exo_play.visibility = View.GONE
            binding.playerView.exo_pause.visibility = View.GONE
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
        isPause = false

    }

    //get current position of video
    private fun updateStartPosition() {
        try {
            if (player != null)
                with(player) {
                    exerciseList.get(previusPos).seekTo = player!!.currentPosition
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


    // release player
    private fun releasePlayerWithoutrestart() {
        if (player != null) {
            try {
                if (player != null)
                    with(player) {
                        exerciseList.get(previusPos).seekTo = player!!.currentPosition
                        currentWindow = player!!.currentWindowIndex
                        player?.stop()
                        player?.release()
                        trackSelector = null
                    }
            } catch (e: java.lang.Exception) {
            }
        }
    }

    public override fun onStop() {
        isPause = true
        // Toast.makeText(getApplicationContext(), "onStop", Toast.LENGTH_LONG).show()
        //  Log.d("onStop...","onStop...")

        mHandler.removeCallbacks(remainingRunnable);
        //  exerciseList.get(previusPos).seekTo = player!!.currentPosition
        var UName = getDataManager().getUserInfo().customer_user_name
        //  SaveTask(exerciseList,UName,workout_id,local).execute()
        super.onStop()
        if (Util.SDK_INT > 23) releasePlayer()
    }

    fun landscapeFunctionality() {


        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        val params =
            binding.playerView.getLayoutParams() as android.widget.RelativeLayout.LayoutParams
        val RParams =
            //  binding.videoRv.getLayoutParams() as android.widget.RelativeLayout.LayoutParams
            binding.topBlurView.getLayoutParams() as android.widget.RelativeLayout.LayoutParams
        //1080 x 2340 pixels

        if (metrics.widthPixels == 3040 || metrics.heightPixels == 1440 || metrics.widthPixels > 3040 || metrics.heightPixels > 1440) {
            RParams.setMargins(resources.getDimension(R.dimen._15sdp).toInt(), 0, 0, 0)
            // binding.videoRv.setLayoutParams(RParams)
            var percent = (10 * metrics.widthPixels) / 100
            RParams.width = ((metrics.widthPixels) - percent) / 2
            binding.topBlurView.setLayoutParams(RParams)
            //    binding.topBlurView1.setLayoutParams(RParams)
            params.width = (metrics.widthPixels) - percent
            params.height = metrics.heightPixels
            params.setMargins(resources.getDimension(R.dimen._25sdp).toInt(), 0, 0, 0)
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP)
            binding.playerView.setLayoutParams(params)
        } else if (metrics.heightPixels == 720 && metrics.widthPixels > 1280) {
            var margin = (metrics.widthPixels - 1280)
            RParams.setMargins(100, 0, 0, 0)
            //   binding.videoRv.setLayoutParams(RParams)

            // binding.topBlurView1.setLayoutParams(RParams)
            var VWidth = (1280 + metrics.heightPixels - 720)
            RParams.width = VWidth / 2
            binding.topBlurView.setLayoutParams(RParams)
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
        } else if (metrics.heightPixels == 1080 && metrics.widthPixels == 2280) {
            RParams.setMargins(resources.getDimension(R.dimen._15sdp).toInt(), 0, 0, 0)
            //  binding.videoRv.setLayoutParams(RParams)
            var percent = (10 * metrics.widthPixels) / 100
            RParams.width = ((metrics.widthPixels) - percent) / 2
            binding.topBlurView.setLayoutParams(RParams)
            //   binding.topBlurView1.setLayoutParams(RParams)

            params.width = (metrics.widthPixels) - percent
            params.height = metrics.heightPixels
            params.setMargins(resources.getDimension(R.dimen._25sdp).toInt(), 0, 0, 0);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP)
            params.addRule(RelativeLayout.CENTER_HORIZONTAL)
            binding.playerView.setLayoutParams(params)
        } else if (metrics.heightPixels == 1080 && metrics.widthPixels > 1280) {
            RParams.setMargins(resources.getDimension(R.dimen._25sdp).toInt(), 0, 0, 0)
            //  binding.videoRv.setLayoutParams(RParams)
            var percent = (10 * metrics.widthPixels) / 100
            RParams.width = ((metrics.widthPixels) - percent) / 2
            binding.topBlurView.setLayoutParams(RParams)
            //   binding.topBlurView1.setLayoutParams(RParams)

            params.width = (metrics.widthPixels) - percent
            params.height = metrics.heightPixels
            params.setMargins(resources.getDimension(R.dimen._25sdp).toInt(), 0, 0, 0);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP)
            params.addRule(RelativeLayout.CENTER_HORIZONTAL)
            binding.playerView.setLayoutParams(params)
        } else {
            RParams.setMargins(resources.getDimension(R.dimen._25sdp).toInt(), 0, 0, 0)
            //   binding.videoRv.setLayoutParams(RParams)

            var percent = (10 * metrics.widthPixels) / 100
            RParams.width = ((metrics.widthPixels) - percent) / 2
            binding.topBlurView.setLayoutParams(RParams)
            //  binding.topBlurView1.setLayoutParams(RParams)
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
        try {
            Constant.requestAudioFocusClose(this)

        } catch (ex: Exception) {

        }
        super.onBackPressed()
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                onBackPressed()
                Constant.requestAudioFocusClose(this)
            }
            R.id.vollume_icon -> {
                /* binding.seekBar.visibility=View.VISIBLE
                 Glide.with(getActivity())
                     .load(R.drawable.ic_inactive_speaker_ico)
                     .into( binding.vollumeIcon)*/
            }

            R.id.iv_pause -> {

                isPause = true
                binding.playerView.exo_pause.performClick()
                binding.ivPlay.visibility = View.VISIBLE
                binding.ivPause.visibility = View.GONE
                binding.playerView.exo_play.visibility = View.GONE
                binding.playerView.exo_pause.visibility = View.GONE
                window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                Constant.requestAudioFocusForMyApp(this)


            }
            R.id.iv_play -> {


               if (isVideoDownloaded==0 && !CommanUtils.isNetworkAvailable(getActivity())!!) {
                    isPause = true
                    binding.playerView.exo_pause.performClick()
                    binding.ivPlay.visibility = View.VISIBLE
                    binding.ivPause.visibility = View.GONE
                    binding.playerView.exo_play.visibility = View.GONE
                    binding.playerView.exo_pause.visibility = View.GONE
                    window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                    Constant.requestAudioFocusForMyApp(getActivity())
                    showInternateConnectionDialog1(getActivity())

                }
                else {
                    isPause = false
                    binding.playerView.exo_play.performClick()
                    binding.ivPlay.visibility = View.GONE
                    binding.ivPause.visibility = View.VISIBLE
                    binding.playerView.exo_play.visibility = View.GONE
                    binding.playerView.exo_pause.visibility = View.GONE
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                    Constant.releaseAudioFocusForMyApp(this)
                }


            }
            R.id.next_layout -> {

                if (previusPos < (exerciseList.size - 1)) {
                    releasePlayer()
                    adapter.notifyItemChanged(previusPos)
                    previusPos = previusPos + 1
                    if (exerciseList.get(previusPos).seekTo >= (exerciseList.get(previusPos).MaxProgress * 1000L)) {
                        exerciseList.get(previusPos).seekTo = 0
                        adapter.notifyItemChanged(previusPos)
                    }

                    if (exerciseList.get(previusPos).stream_video_name!=null&& !exerciseList.get(previusPos).stream_video_name.isEmpty()) {
                        var titlestr = exerciseList.get(previusPos).stream_video_name
                        val cap: String =
                            titlestr.substring(0, 1).toUpperCase() + titlestr.substring(1)
                        binding.title.text = cap
                    }

                  //  binding.title.text = exerciseList.get(previusPos).stream_video_name

                    if (exerciseList.get(previusPos).downLoadUrl!=null&&!exerciseList.get(previusPos).downLoadUrl.isEmpty()) {
                        isVideoDownloaded=1
                        intializePlayer(exerciseList.get(previusPos).downLoadUrl, previusPos, 1,0)
                    }
                    else {
                        isVideoDownloaded=0
                        intializePlayer(exerciseList.get(previusPos).stream_video, previusPos,0,0)

/*
                       if (CommanUtils.isNetworkAvailable(getActivity())!!)
                            intializePlayer(exerciseList.get(previusPos).stream_video, previusPos)
                        else
                            Constant.showInternateConnectionDialog(getActivity())
*/
                    }

                    if (previusPos < (exerciseList.size - 1)) {
                        binding.nextLayout.visibility = View.VISIBLE
                    } else {
                        binding.nextLayout.visibility = View.INVISIBLE
                    }
                }

                if (previusPos > 0) {
                    binding.previousLayout.visibility = View.VISIBLE
                } else {
                    binding.previousLayout.visibility = View.INVISIBLE

                }
                if (isVideoDownloaded==0 && !CommanUtils.isNetworkAvailable(getActivity())!!) {
                    isPause = true
                    if (binding.backword.visibility == View.VISIBLE) {
                        if (isPause) {
                            binding.ivPlay.visibility = View.VISIBLE
                            binding.ivPause.visibility = View.GONE
                        } else {
                            binding.ivPause.visibility = View.VISIBLE
                            binding.ivPlay.visibility = View.GONE
                        }
                    }
                    }
                else {
                    isPause = false
                    if (binding.backword.visibility == View.VISIBLE) {
                        if (isPause) {
                            binding.ivPlay.visibility = View.VISIBLE
                            binding.ivPause.visibility = View.GONE
                        } else {
                            binding.ivPause.visibility = View.VISIBLE
                            binding.ivPlay.visibility = View.GONE
                        }
                    }

                }
                }
            R.id.previous_layout -> {
                if (previusPos > 0) {
                    releasePlayer()
                    adapter.notifyItemChanged(previusPos)
                    previusPos = previusPos - 1
                    if (exerciseList.get(previusPos).seekTo >= (exerciseList.get(previusPos).MaxProgress * 1000L)) {
                        exerciseList.get(previusPos).seekTo = 0
                        adapter.notifyItemChanged(previusPos)
                    }
                    if (exerciseList.get(previusPos).stream_video_name!=null&& !exerciseList.get(previusPos).stream_video_name.isEmpty()) {
                        var titlestr = exerciseList.get(previusPos).stream_video_name
                        val cap: String =
                            titlestr.substring(0, 1).toUpperCase() + titlestr.substring(1)
                        binding.title.text = cap
                    }


                //    binding.title.text = exerciseList.get(previusPos).stream_video_name
                    if (exerciseList.get(previusPos).downLoadUrl!=null&&!exerciseList.get(previusPos).downLoadUrl.isEmpty()) {
                        isVideoDownloaded=1
                        intializePlayer(exerciseList.get(previusPos).downLoadUrl, previusPos, 1,0)
                    }
                    else {
                        isVideoDownloaded=0

                        intializePlayer(exerciseList.get(previusPos).stream_video, previusPos,0,0)

/*
                        if (CommanUtils.isNetworkAvailable(getActivity())!!)
                            intializePlayer(exerciseList.get(previusPos).stream_video, previusPos)
                        else
                            Constant.showInternateConnectionDialog(getActivity())
*/
                    }
                    if (previusPos > 0) {
                        binding.previousLayout.visibility = View.VISIBLE
                    } else {
                        binding.previousLayout.visibility = View.INVISIBLE

                    }
                }
                if (previusPos < (exerciseList.size - 1)) {
                    binding.nextLayout.visibility = View.VISIBLE
                } else {
                    binding.nextLayout.visibility = View.INVISIBLE
                }
              /*  if (isVideoDownloaded==0 && !CommanUtils.isNetworkAvailable(getActivity())!!)
                    isPause = true
                else
                    isPause = false*/
                if (isVideoDownloaded==0 && !CommanUtils.isNetworkAvailable(getActivity())!!) {
                    isPause = true
                    if (binding.backword.visibility == View.VISIBLE) {
                        if (isPause) {
                            binding.ivPlay.visibility = View.VISIBLE
                            binding.ivPause.visibility = View.GONE
                        } else {
                            binding.ivPause.visibility = View.VISIBLE
                            binding.ivPlay.visibility = View.GONE
                        }
                    }
                }
                else {
                    isPause = false
                    if (binding.backword.visibility == View.VISIBLE) {
                        if (isPause) {
                            binding.ivPlay.visibility = View.VISIBLE
                            binding.ivPause.visibility = View.GONE
                        } else {
                            binding.ivPause.visibility = View.VISIBLE
                            binding.ivPlay.visibility = View.GONE
                        }
                    }

                }



            }
            R.id.video_layout -> {
                if (binding.videoRv.visibility == View.VISIBLE) {
                    binding.videoRv.visibility = View.GONE
                    binding.topBlurView.visibility = View.GONE
                    // binding.topBlurView1.visibility = View.GONE
                } else {
                    binding.videoRv.visibility = View.VISIBLE
                    binding.topBlurView.visibility = View.VISIBLE
                    ivHideControllerButton.visibility = View.GONE

                    //  binding.topBlurView1.visibility = View.VISIBLE
                    binding.backword.visibility = View.GONE
                    binding.ivForword.visibility = View.GONE
                    binding.seekBar.visibility = View.INVISIBLE
                    binding.vollumeIcon.visibility = View.GONE
                    Glide.with(getActivity())
                        .load(R.drawable.ic_active_speaker_ico)
                        .into(binding.vollumeIcon)

                    binding.title.visibility = View.GONE
                    binding.ivBack.visibility = View.GONE
                    binding.ivMusic.visibility = View.GONE
                    binding.episodeLayout.visibility = View.GONE
                    binding.transparentLayout.visibility = View.GONE
                    binding.ivPlay.visibility = View.GONE
                    binding.ivPause.visibility = View.GONE
                    exerciseList.get(previusPos).seekTo = player!!.currentPosition
                    adapter.notifyItemChanged(previusPos)
                    binding.playerView.exo_pause.performClick()
                    binding.playerView.exo_play.visibility = View.GONE
                    binding.playerView.exo_pause.visibility = View.GONE
                    window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

                    isPause = true


                    // if (Util.SDK_INT > 23) releasePlayer()
                }
            }
            R.id.playerView -> {
                //  Blurry.with(this).radius(25).sampling(100).color(Color.argb(66, 0, 255, 255)).onto(binding.playerView)
/*
                Blurry.with(this)
                    .radius(25)
                    .sampling(1)
                    .color(Color.argb(66, 255, 255, 0))
                    .async()
                    .capture(findViewById(R.id.episode_layout))
                    .into(findViewById(R.id.episode_layout))
*/

                if (binding.videoRv.visibility == View.VISIBLE) {
                    binding.videoRv.visibility = View.GONE
                    binding.transparentLayout.visibility = View.GONE
                    binding.ivPlay.visibility = View.GONE
                    binding.ivPause.visibility = View.GONE
                    binding.topBlurView.visibility = View.GONE
                    ivHideControllerButton.visibility = View.GONE
                    binding.seekBar.visibility = View.INVISIBLE
                    binding.vollumeIcon.visibility = View.GONE
                    Glide.with(getActivity())
                        .load(R.drawable.ic_active_speaker_ico)
                        .into(binding.vollumeIcon)

                    //  binding.topBlurView1.visibility = View.GONE
                    binding.playerView.exo_play.performClick()
                    binding.playerView.exo_play.visibility = View.GONE
                    binding.playerView.exo_pause.visibility = View.GONE
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                    isPause = false

/*
                    if (binding.playerView != null) {
                        binding.title.text=exerciseList.get(previusPos).stream_video_name
                        intializePlayer(exerciseList.get(previusPos).stream_video, previusPos)
                    }
*/
                } else {


                    if (binding.backword.visibility == View.GONE) {
                        binding.transparentLayout.visibility = View.VISIBLE

                        if (isPause) {
                            binding.ivPlay.visibility = View.VISIBLE
                            binding.ivPause.visibility = View.GONE
                        } else {
                            binding.ivPause.visibility = View.VISIBLE
                            binding.ivPlay.visibility = View.GONE
                        }

                        binding.backword.visibility = View.VISIBLE
                        binding.ivForword.visibility = View.VISIBLE
                        binding.seekBar.visibility = View.VISIBLE
                        Glide.with(getActivity())
                            .load(R.drawable.ic_active_speaker_ico)
                            .into(binding.vollumeIcon)

                        binding.vollumeIcon.visibility = View.VISIBLE


                        if (trailer != null && trailer.equals("yes")) {
                            binding.episodeLayout.visibility = View.GONE
                            binding.title.visibility = View.VISIBLE
                        } else {
                            binding.episodeLayout.visibility = View.VISIBLE
                            binding.title.visibility = View.VISIBLE

                            if (previusPos < (exerciseList.size - 1)) {
                                binding.nextLayout.visibility = View.VISIBLE
                            } else {
                                binding.nextLayout.visibility = View.INVISIBLE
                            }
                            if (previusPos > 0) {
                                binding.previousLayout.visibility = View.VISIBLE
                            } else {
                                binding.previousLayout.visibility = View.INVISIBLE

                            }
                            if (exerciseList.size > 1) {
                                binding.videoLayout.visibility = View.VISIBLE
                            } else {
                                binding.videoLayout.visibility = View.INVISIBLE

                            }
                        }



                        ivHideControllerButton.visibility = View.VISIBLE


                        binding.playerView.useController = true
                        binding.playerView.controller.visibility = View.VISIBLE
                        binding.playerView.showController()
                        binding.playerView.exo_play.visibility = View.GONE
                        binding.playerView.exo_pause.visibility = View.GONE
                        binding.ivBack.visibility = View.VISIBLE
                        binding.ivMusic.visibility = View.VISIBLE
                        (timer as CountDownTimer).start()

                    } else {
                        binding.backword.visibility = View.GONE
                        binding.ivForword.visibility = View.GONE
                        binding.seekBar.visibility = View.INVISIBLE
                        binding.vollumeIcon.visibility = View.GONE

                        binding.title.visibility = View.GONE
                        binding.episodeLayout.visibility = View.GONE
                        binding.transparentLayout.visibility = View.GONE
                        binding.ivPlay.visibility = View.GONE
                        binding.ivPause.visibility = View.GONE

                        binding.ivBack.visibility = View.GONE
                        binding.ivMusic.visibility = View.GONE
                        ivHideControllerButton.visibility = View.GONE
                        timer?.cancel()

                    }
                }


            }
            R.id.backword -> {
                Log.d("player duration", "duration..." + player!!.duration)
                var CP = player!!.currentPosition
                CP = CP - 10000
                if (CP > 0)
                    player?.seekTo(CP)
                //  else
                //   player?.seekTo(0)
            }
            R.id.iv_forword -> {
                Log.d("player duration", "duration..." + player!!.duration)
                var CP = player!!.currentPosition
                CP = CP + 10000
                if (CP <= player!!.duration)
                    player?.seekTo(CP)
                //  else
                //  player?.seekTo(player!!.duration)
            }
            R.id.iv_music -> {
                aboutToStartMusic()

            }
        }
    }


    private fun aboutToStartMusic() {
        try {
            // aboutToStartMusic = true
            val intent = Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER)
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        //  window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        // window.clearFlags(WindowManager.LayoutParams.TYPE_PRIVATE_PRESENTATION)
    }

    override fun onResume() {
        hideNavStatusBar()
        if (binding.playerView != null) {
            if (exerciseList.get(previusPos).stream_video_name!=null&& !exerciseList.get(previusPos).stream_video_name.isEmpty()) {
                var titlestr = exerciseList.get(previusPos).stream_video_name
                val cap: String =
                    titlestr.substring(0, 1).toUpperCase() + titlestr.substring(1)
                binding.title.text = cap
            }

          //  binding.title.text = exerciseList.get(previusPos).stream_video_name

            if (exerciseList.get(previusPos).downLoadUrl!=null&&!exerciseList.get(previusPos).downLoadUrl.isEmpty())
                intializePlayer(exerciseList.get(previusPos).downLoadUrl, previusPos,1,1)
            else {

                intializePlayer(exerciseList.get(previusPos).stream_video, previusPos,0,1)

/*
                if (CommanUtils.isNetworkAvailable(getActivity())!!)
                    intializePlayer(exerciseList.get(previusPos).stream_video, previusPos)
                else
                    Constant.showInternateConnectionDialog(getActivity())
*/
            }

            if (binding.videoRv.visibility == View.VISIBLE || isPause) {
                binding.playerView.exo_pause.performClick()
                binding.playerView.exo_play.visibility = View.GONE
                binding.playerView.exo_pause.visibility = View.GONE
                window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                binding.transparentLayout.visibility = View.VISIBLE
                binding.ivPlay.visibility = View.VISIBLE
                binding.ivPause.visibility = View.GONE
                binding.backword.visibility = View.VISIBLE
                binding.ivForword.visibility = View.VISIBLE
                binding.seekBar.visibility = View.VISIBLE
                binding.vollumeIcon.visibility = View.VISIBLE
                if (trailer != null && trailer.equals("yes")) {
                    binding.episodeLayout.visibility = View.GONE
                    binding.title.visibility = View.GONE
                } else {
                    binding.episodeLayout.visibility = View.VISIBLE
                    binding.title.visibility = View.VISIBLE

                    if (previusPos < (exerciseList.size - 1)) {
                        binding.nextLayout.visibility = View.VISIBLE
                    } else {
                        binding.nextLayout.visibility = View.INVISIBLE
                    }
                    if (previusPos > 0) {
                        binding.previousLayout.visibility = View.VISIBLE
                    } else {
                        binding.previousLayout.visibility = View.INVISIBLE
                    }
                    if (exerciseList.size > 1) {
                        binding.videoLayout.visibility = View.VISIBLE
                    } else {
                        binding.videoLayout.visibility = View.INVISIBLE

                    }

                }
                ivHideControllerButton.visibility = View.VISIBLE
                binding.playerView.useController = true
                binding.playerView.controller.visibility = View.VISIBLE
                binding.playerView.showController()
                binding.playerView.exo_play.visibility = View.GONE
                binding.playerView.exo_pause.visibility = View.GONE
                binding.ivBack.visibility = View.VISIBLE
                binding.ivMusic.visibility = View.VISIBLE
                (timer as CountDownTimer).start()
                isPause = true
                Constant.requestAudioFocusForMyApp(this)
            } else {
                Constant.releaseAudioFocusForMyApp(this)
            }
            var progress = binding.seekBar.getProgress()
            var PVal = (progress / 15.0)
            Log.d("player seekbar vollume", "player seekbar hardware..." + PVal)

            player?.volume = PVal.toFloat()
        }
        var delayMs = TimeUnit.SECONDS.toMillis(1);
        mHandler.postDelayed(remainingRunnable, delayMs);

        super.onResume()
    }

    override fun onPause() {
        if (binding.playerView != null) {
            exerciseList.get(previusPos).seekTo = player!!.currentPosition
            releasePlayer()
        }
        Constant.requestAudioFocusForMyApp(this)
        super.onPause()
    }



    // class to save(insert if not exist or update if exist) list in local database(Room)
/*
    internal class SaveTask(
        list: ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>,
        Name: String,
        workout_id: String,
        local: String
    ):AsyncTask<Void, Void, String>() {
        var Wlist=list
        var Uname=Name
        var workoutId=workout_id
        var local=local
         override fun doInBackground(vararg params: Void): String {
            val WStr = GithubTypeConverters.someObjectListToString(Wlist)
            //creating a localData
            val localData = LocalStream()
             localData.setTask(""+Uname)
             localData.workoutId=workoutId
             localData.setWList(WStr)
             localData.setUsername(Uname)
             localData.setFinished(false)
            //adding to database
             if (local!=null&&!local.isEmpty()&&local.equals("yes")) {
                 DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                     .taskDao()
                     .updateList(WStr, Uname, workoutId)
             }
             else {
                 DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                     .taskDao()
                     .insert(localData)
             }
            return ""
        }
        override fun onPostExecute(aVoid:String) {
            super.onPostExecute(aVoid)
           // Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show()
        }
    }
*/
}


