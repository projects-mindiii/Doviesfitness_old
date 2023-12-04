package com.doviesfitness.ui.bottom_tabbar.stream_tab.activities

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import androidx.databinding.DataBindingUtil
import android.net.Uri
import com.doviesfitness.R
import com.doviesfitness.ui.base.BaseActivity
import java.util.*
import android.os.*
import android.provider.MediaStore
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.mediarouter.media.MediaRouteSelector
import androidx.mediarouter.media.MediaRouter
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import com.doviesfitness.chromecast.expandedcontrols.ExpandedControlsActivity
import com.doviesfitness.chromecast.utils.CustomChromeCastBottomSheetDialog
import com.doviesfitness.databinding.ActivityStreamVideoPlayLandscapeBinding
import com.doviesfitness.ui.bottom_tabbar.stream_tab.adapters.StreamListAdapter
import com.doviesfitness.ui.bottom_tabbar.stream_tab.download_background.DownloadedModal
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.StreamWorkoutDetailModel
import com.doviesfitness.ui.multipleQuality.StreamVideoPlayUrlActivityTemp
import com.doviesfitness.utils.CommanUtils
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.StringConstant
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.MediaLoadRequestData
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.SessionManagerListener
import com.google.android.gms.cast.framework.media.RemoteMediaClient
import com.google.android.gms.common.images.WebImage
import kotlinx.android.synthetic.main.activity_video_detail.*
import kotlinx.android.synthetic.main.stream_exo_playback_control_view.view.*
import org.json.JSONObject
import java.lang.Exception
import java.util.concurrent.TimeUnit


class StreamDownloadedVideoActivity : BaseActivity(), View.OnClickListener,
    StreamListAdapter.OnVideoClick, CustomChromeCastBottomSheetDialog.ChromeCastListener {
    lateinit var binding: ActivityStreamVideoPlayLandscapeBinding
     var exerciseList=ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>()
    private  var player: SimpleExoPlayer?=null
    //  private var player: SimpleExoPlayer? = null
    private lateinit var mediaDataSourceFactory: DataSource.Factory
    private val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory()
    private val ivHideControllerButton: LinearLayout by lazy {
        findViewById<LinearLayout>(R.id.controller)
    }
    private var lastSeenTrackGroupArray: TrackGroupArray? = null
    private var trackSelector: DefaultTrackSelector? = null
    private var  dataSourceFactory: DefaultDataSourceFactory? = null
    private var currentWindow: Int = 0
    private var playbackPosition: Long = 0
    private var previusPos: Int = 0
    lateinit var adapter: StreamListAdapter
    var workout_id=""
    var local=""
    var ready=true
    var isPause = false
    var isLast = true
    var timer: CountDownTimer?=null
    lateinit var cResolver: ContentResolver
    var historyId = ""
    var duration = ""
    var streamImg = ""

    private lateinit var  audioManager: AudioManager

    var remainingRunnable= Runnable {
        updateProgress()
    }
    lateinit var mHandler:Handler

    companion object{
        var sInstance: RouteComparator? = null
    }

    fun updateProgress() {
        var millis = player!!.duration-player!!.currentPosition
        Log.d("time in mili","time in mili..."+millis)
        var hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
            TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
            TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        if (millis>0)
            binding.playerView.remaining_time.text= ""+hms
        var delayMs = TimeUnit.SECONDS.toMillis(1);
        mHandler.postDelayed(remainingRunnable, delayMs);
    }



    // Listener to seek video at first time and to play next video video automaticaly one by one
    var eventListener = object:Player.EventListener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            when (playbackState) {
                Player.STATE_IDLE -> {
                    Log.d("player state","player state...STATE_IDLE")
                }
                Player.STATE_BUFFERING -> {
                    Log.d("player state","player state...STATE_BUFFERING")
                }
                Player.STATE_READY -> {
                    if (ready){
                        ready=false
                        player?.seekTo(exerciseList.get(0).seekTo)
                        Log.d("player state","player state...STATE_READY")
                    }

                        getVideoTimeDuration()

                        /*  var hisId=""
                          if (from.isNotEmpty() && from.equals("history",true)){
                              hisId=historyId
                          }
                          else
                              hisId=""
                          Log.d("player state", "player state...STATE_READY...save history...id.."+hisId)
*/
                        createPlayedHistory(
                            exerciseList.get(previusPos).stream_video_id,
                            workout_id,
                            "",
                            false
                        )
                        Log.d("player state", "player state...STATE_READY...save history")



                }
                Player.STATE_ENDED -> {
                    Log.d("player state","player state...STATE_ENDED")
                    if (previusPos<(exerciseList.size-1)) {
                        releasePlayerWithoutrestart()
                        adapter.notifyItemChanged(previusPos)
//                        previusPos=previusPos+1
                        if ( exerciseList.get(previusPos).seekTo>=(exerciseList.get(previusPos).MaxProgress*1000L)){
                            exerciseList.get(previusPos).seekTo=0
                            adapter.notifyItemChanged(previusPos)
                        }
                        callCompleteLog()

                        intializePlayer(exerciseList.get(previusPos).downLoadUrl, previusPos,0)
                    }
                    else {

                        callCompleteLog()
                        intializePlayer(exerciseList.get(previusPos).downLoadUrl, 0,0)

//                        getVideoTimeDuration()
//                        createPlayedHistory(
//                            exerciseList.get(previusPos).stream_video_id,
//                            workout_id,
//                            historyId,
//                            isLast
//                        )
//                        finish()
                    }
//                    else{
//                        finish()
//                    }
                }
            }
        }
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

    /*below identifers belongs to chrome cast*/

    var isCastConnected = false
    var castLoaderData = ArrayList<MediaInfo>()
    private var mSelectedMedia: MediaInfo? = null
    private var mCastContext: CastContext? = null
    private var mCastSession: CastSession? = null
    private var mSessionManagerListener: SessionManagerListener<CastSession>? = null

    private var mRouter: MediaRouter? = null
    private var mCallback: MediaRouterCallback? = null
    private var mSelector = MediaRouteSelector.EMPTY
    private var mAdapter: RouteAdapter? = null
    var isitemAdded = false
    var streamImage = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        hideNavStatusBar()
        super.onCreate(savedInstanceState)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
     //   getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_stream_video_play_landscape)
        landscapeFunctionality()

        binding.episodes.setOnClickListener(this)
        binding.previous.setOnClickListener(this)
        binding.next.setOnClickListener(this)
        binding.backword.setOnClickListener(this)
        binding.ivForword.setOnClickListener(this)
        binding.playerView.setOnClickListener(this)
        binding.ivBack.setOnClickListener(this)
        binding.ivMusic.setOnClickListener(this)
        binding.ivPlay.setOnClickListener(this)
        binding.ivPause.setOnClickListener(this)
        binding.vollumeIcon.setOnClickListener(this)
        binding.chromeCast.setOnClickListener(this)
        binding.ivCancleDialog.setOnClickListener(this)
        binding.disconnect.setOnClickListener(this)
        binding.disconnect1.setOnClickListener(this)
        if (intent.hasExtra("streamImage")!!) {
            streamImage = intent.getStringExtra("streamImage")!!
        }
        if (intent.hasExtra("localStreamList")) {
            if (intent.getSerializableExtra("localStreamList") != null) {
             var   localStreamList = intent.getSerializableExtra("localStreamList") as ArrayList<DownloadedModal.ProgressModal>
                exerciseList.clear()
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
                        mp4_video = null,is_workout = localListDataModal.is_workout,
                            view_type = localListDataModal.view_type
                    )
//                    streamImage = localListDataModal.stream_video_image_url
                    exerciseList.add(videoModal)
                }
            }
        }
        workout_id = intent.getStringExtra("workout_id")!!
        local = intent.getStringExtra("local")!!
        previusPos = 0
        if (intent.hasExtra("position"))
            previusPos = intent.getIntExtra("position", 0)
        binding.videoRv.layoutManager = LinearLayoutManager(getActivity())

        //video list
        adapter = StreamListAdapter(getActivity(), exerciseList, this)
        binding.videoRv.adapter = adapter
        binding.playerView.controllerShowTimeoutMs = 30000
        binding.playerView.controllerHideOnTouch = false
       // brightnessSeekbar()
        vollumeControls()

        timer= object : CountDownTimer(2500, 10) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                try {
                    binding.backword.visibility=View.GONE
                    binding.ivForword.visibility=View.GONE
                    binding.seekBar.visibility=View.GONE
                    binding.title.visibility=View.GONE
                    binding.episodeLayout.visibility=View.GONE
                    ivHideControllerButton.visibility=View.GONE
                    binding.ivBack.visibility=View.GONE
                    binding.transparentLayout.visibility = View.GONE
                    binding.vollumeIcon.visibility = View.GONE
                    Glide.with(getActivity())
                        .load(R.drawable.ic_active_speaker_ico)
                        .into( binding.vollumeIcon)

                    binding.ivPlay.visibility = View.GONE
                    binding.ivPause.visibility = View.GONE
                    binding.ivMusic.visibility = View.GONE
                    binding.chromeCast.visibility = View.GONE

                    // binding.videoRv.visibility=View.GONE

                }
                catch (Ex:Exception){
                    Ex.printStackTrace()
                }

                cancel()
            }
        }

        mHandler =  Handler()
        mHandler.post(remainingRunnable);
        //  intializePlayer(exerciseList.get(0).downLoadUrl, 0)
        player?.addListener(eventListener)
     //   binding.videoRv.visibility = View.VISIBLE
        chromeCastInit()

    }

    fun vollumeControls() {
        try {
            audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
            binding.seekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            binding.seekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
            Log.d("audio progres vollume","player audio vollume..."+audioManager.getStreamVolume(AudioManager.STREAM_MUSIC))
            Log.d("audio max vollume","player max vollume..."+audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC))
            var currentvolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
            var PVal=(currentvolume/15.0)
            Log.d("player seekbar vollume","player seekbar vollume..."+PVal)

            if (currentvolume/15==1)
                player?.volume= 1.0f
            else
                player?.volume= PVal.toFloat()
            binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {

/*
                   audioManager.setStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        progress, 0
                    )
*/


                    ////////
                    //  var currentvolume = player?.getVolume();
                    // var currentvolume = player?.audioAttributes();
                    //  Log.d("player vollume","player vollume..."+currentvolume)

                    var PVal=(progress/15.0)
                    Log.d("player seekbar vollume","player seekbar vollume..."+PVal)

                    if (progress/15==1)
                        player?.volume= 1.0f
                    else
                        player?.volume= PVal.toFloat()


                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }

            })

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }


/*
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        Log.d("keyCode","keyCode down..."+keyCode)
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP)
        {
            var index = binding.seekBar.getProgress();
            binding.seekBar.setProgress(index + 1);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
        {
            var index =  binding.seekBar.getProgress();
            binding.seekBar.setProgress(index - 1);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
*/

    private fun brightnessSeekbar() {
        cResolver = getContentResolver();
        var window = getWindow();
        binding.seekBar.setMax(255)
        var brightness = 100
        try {
            brightness = Settings.System.getInt(cResolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (e: Settings.SettingNotFoundException) {
            Log.e("Error", "Cannot access system brightness");
            e.printStackTrace();
        }
        binding.seekBar.setProgress(brightness)
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (progress <= 20) {
                    brightness = 20
                } else
                {
                    brightness = progress;
                }
                //Calculate the brightness percentage
                // var perc = (brightness / 255 * 100).toFloat()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                var retVal= checkSystemWritePermission()
                Log.d(tag, "brightness...: " + brightness);
                if (retVal) {
                    Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
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

    fun checkSystemWritePermission():Boolean {
        var retVal = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            retVal = Settings.System.canWrite(this);
            Log.d(tag, "Can Write Settings: " + retVal);
        }
        return retVal

    }

    // method to initialise player and play video
    fun intializePlayer(news_video: String, pos: Int, idForSeekTo: Int) {
       // binding.backword.visibility=View.VISIBLE
      //  binding.ivForword.visibility=View.VISIBLE
        binding.title.text=exerciseList.get(pos).stream_video_name

        trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)
        var dataSourceFactory = DefaultDataSourceFactory(this, Util.getUserAgent(this, "mediaPlayerSample"))
        var dataSourceFactory1 = CacheDataSourceFactory(VideoCache.getInstance(), dataSourceFactory)
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector!!)

        val mediaSource = ExtractorMediaSource.Factory(dataSourceFactory1)
            .createMediaSource(Uri.parse(news_video))
        with(player) {
            this?.prepare(mediaSource, false, false)
            this?.playWhenReady = true
        }
       // ivHideControllerButton.visibility = View.VISIBLE
      //  playerView.showController()
        playerView.setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING)
        playerView.setShutterBackgroundColor(ContextCompat.getColor(this, R.color.colorBlack))
        playerView.player = player
        playerView.requestFocus()
       // playerView.player.seekTo(exerciseList.get(pos).seekTo)
        if(idForSeekTo == 0){
            player?.seekTo(0)
        }
        else{
            player?.seekTo(exerciseList.get(pos).seekTo)
        }

        lastSeenTrackGroupArray = null
        player?.addListener(eventListener)

    }

    // method to play video on list item click
    override fun onVideoClick(pos: Int) {
        if (previusPos!=pos) {
            releasePlayer()
            adapter.notifyItemChanged(previusPos)
            if ( exerciseList.get(pos).seekTo>=(exerciseList.get(pos).MaxProgress*1000L)){
                exerciseList.get(pos).seekTo=0
                adapter.notifyItemChanged(pos)
            }
            intializePlayer(exerciseList.get(pos).downLoadUrl, pos,0)
            previusPos = pos
        }
        else {
            binding.playerView.exo_play.performClick()
            binding.playerView.exo_play.visibility = View.GONE
            binding.playerView.exo_pause.visibility = View.GONE
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            isPause = false
        }
    }

    //get current position of video
    private fun updateStartPosition() {
        try {
            if (player != null)
                with(player) {
                    exerciseList.get(previusPos).seekTo =  player!!.currentPosition
                    currentWindow = player!!.currentWindowIndex
                    player!!.playWhenReady = true
                }
        } catch (e: java.lang.Exception) {
            Log.e("updateStartPosition",e.toString())
        }
    }
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

    // release player
    private fun releasePlayer() {
        if (player != null) {
            updateStartPosition()
            player?.playWhenReady=false
            player?.stop()
            player?.release()

            trackSelector = null
        }
    }

    public override fun onStop() {
        mHandler.removeCallbacks(remainingRunnable);
      //  exerciseList.get(previusPos).seekTo = player!!.currentPosition
       // var UName=  getDataManager().getUserInfo().customer_user_name
     //   SaveTask(exerciseList,UName,workout_id,local).execute()
        super.onStop()
        if (Util.SDK_INT > 23) releasePlayer()
    }

    fun landscapeFunctionality() {
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        val params =
            binding.playerView.getLayoutParams() as android.widget.RelativeLayout.LayoutParams
      //  val RParams = binding.videoRv.getLayoutParams() as android.widget.RelativeLayout.LayoutParams

        if (metrics.widthPixels == 3040 || metrics.heightPixels == 1440 || metrics.widthPixels > 3040 || metrics.heightPixels > 1440) {
          //  RParams.setMargins(resources.getDimension(R.dimen._25sdp).toInt(), 0, 0, 0)
          //  binding.videoRv.setLayoutParams(RParams)
            var percent = (10 * metrics.widthPixels) / 100
            params.width = (metrics.widthPixels) - percent
            params.height = metrics.heightPixels
            params.setMargins(resources.getDimension(R.dimen._25sdp).toInt(), 0, 0, 0)
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP)
            binding.playerView.setLayoutParams(params)

        }
        else if (metrics.heightPixels == 720 && metrics.widthPixels > 1280) {
           var margin= (metrics.widthPixels-1280)
           // RParams.setMargins(100, 0, 0, 0)
         //   binding.videoRv.setLayoutParams(RParams)
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
          //  RParams.setMargins(resources.getDimension(R.dimen._25sdp).toInt(), 0, 0, 0)
           // binding.videoRv.setLayoutParams(RParams)
            var percent = (10 * metrics.widthPixels) / 100
            params.width = (metrics.widthPixels) - percent
            params.height = metrics.heightPixels
            params.setMargins(resources.getDimension(R.dimen._25sdp).toInt(), 0, 0, 0);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP)
            params.addRule(RelativeLayout.CENTER_HORIZONTAL)
            binding.playerView.setLayoutParams(params)
        } else {
          //  RParams.setMargins(resources.getDimension(R.dimen._25sdp).toInt(), 0, 0, 0)
          //  binding.videoRv.setLayoutParams(RParams)
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
        setResult(50)
        finish()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_cancle_dialog -> {
                binding.castDialogLayout.visibility = View.GONE
            }
            R.id.disconnect -> {
                chromeCastSelectDisconnect("disconnect")
            }
            R.id.disconnect1 -> {
                chromeCastSelectDisconnect("disconnect")
            }

            R.id.iv_back -> {
                Log.d("Crosscclick------->", "Press...")
//                onBackPressed()
//sdfg
//                net condition or isworkout==true
                callCompleteLog()
            }

            R.id.chrome_cast->{
                if (isCastConnected) {

                    var remoteClient =
                        mCastContext?.sessionManager?.currentCastSession?.remoteMediaClient

                    if (remoteClient?.mediaInfo != null && remoteClient?.hasMediaSession()) {
                        if (castLoaderData.size > 0) {
                            mSelectedMedia = castLoaderData.get(0)
                        }
                        mCastSession = mCastContext?.sessionManager?.currentCastSession
                        if (null != mSelectedMedia) {

                            val intent = Intent(getActivity(), ExpandedControlsActivity::class.java).putExtra(
                                "from",
                                "player"
                            )
                            startActivityForResult(intent, Constant.CHORME_EXPANDED_CODE)

/*
                            loadRemoteMedia(
                                mCastContext?.sessionManager?.currentCastSession?.remoteMediaClient?.approximateStreamPosition!!,
                                true
                            )
*/
                        }
                    }
                    else {
                        mCastSession = mCastContext?.sessionManager?.currentCastSession
                        if(mCastSession?.castDevice?.friendlyName!=null)
                            binding.deviceName.text = "" + mCastSession?.castDevice?.friendlyName
                        binding.castDialogLayout.visibility = View.VISIBLE
                        binding.mediaLayout.visibility = View.VISIBLE
                    }
                } else {
                    binding.castDialogLayout.visibility = View.VISIBLE
                    binding.disconnect.visibility = View.GONE
                    binding.title1.visibility = View.VISIBLE
                    binding.mediaLayout.visibility = View.GONE
                    binding.seperatorLine.visibility = View.VISIBLE
                    if (isitemAdded) {
                        binding.dataLayout.visibility = View.VISIBLE
                        binding.noDeviceFoundLayout.visibility = View.GONE

                    } else {
                        binding.dataLayout.visibility = View.GONE
                        binding.noDeviceFoundLayout.visibility = View.VISIBLE
                    }
                }

                binding.loaderLayout.visibility = View.GONE

            }

            R.id.vollume_icon->{
                binding.seekBar.visibility=View.VISIBLE
                Glide.with(getActivity())
                    .load(R.drawable.ic_inactive_speaker_ico)
                    .into( binding.vollumeIcon)
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
                isPause = false
                binding.playerView.exo_play.performClick()
                binding.ivPlay.visibility = View.GONE
                binding.ivPause.visibility = View.VISIBLE
                binding.playerView.exo_play.visibility = View.GONE
                binding.playerView.exo_pause.visibility = View.GONE
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                Constant.releaseAudioFocusForMyApp(this)

            }
            R.id.next -> {

                if (previusPos<(exerciseList.size-1)) {
                    releasePlayer()
                    adapter.notifyItemChanged(previusPos)
                    previusPos=previusPos+1
                   if ( exerciseList.get(previusPos).seekTo>=(exerciseList.get(previusPos).MaxProgress*1000L)){
                       exerciseList.get(previusPos).seekTo=0
                       adapter.notifyItemChanged(previusPos)
                   }
                    intializePlayer(exerciseList.get(previusPos).downLoadUrl, previusPos,0)
                }
                isPause = false
            }
            R.id.previous -> {
                if (previusPos>0) {
                    releasePlayer()
                    adapter.notifyItemChanged(previusPos)
                    previusPos=previusPos-1
                    if ( exerciseList.get(previusPos).seekTo>=(exerciseList.get(previusPos).MaxProgress*1000L)){
                        exerciseList.get(previusPos).seekTo=0
                        adapter.notifyItemChanged(previusPos)
                    }
                    intializePlayer(exerciseList.get(previusPos).downLoadUrl, previusPos,0)
                }
                isPause = false
            }
            R.id.episodes -> {
                if (binding.videoRv.visibility == View.VISIBLE)
                    binding.videoRv.visibility = View.GONE
                else{
                    binding.videoRv.visibility = View.VISIBLE
                    exerciseList.get(previusPos).seekTo = player!!.currentPosition
                    adapter.notifyItemChanged(previusPos)
                    binding.seekBar.visibility = View.INVISIBLE
                    binding.vollumeIcon.visibility = View.GONE
                    Glide.with(getActivity())
                        .load(R.drawable.ic_active_speaker_ico)
                        .into( binding.vollumeIcon)
                    binding.ivBack.visibility = View.GONE

                   /* binding.title.visibility = View.GONE
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
                    binding.playerView.exo_pause.visibility = View.GONE*/
                    window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

                    isPause = true


                }
            }
            R.id.playerView -> {

                if (binding.backword.visibility == View.GONE) {
                    binding.transparentLayout.visibility = View.VISIBLE
                    binding.backword.visibility = View.VISIBLE
                    binding.ivForword.visibility = View.VISIBLE
                    binding.title.visibility = View.VISIBLE
                    binding.episodeLayout.visibility = View.GONE
                    ivHideControllerButton.visibility = View.VISIBLE



                    if (isPause) {
                        binding.ivPlay.visibility = View.VISIBLE
                        binding.ivPause.visibility = View.GONE
                    } else {
                        binding.ivPause.visibility = View.VISIBLE
                        binding.ivPlay.visibility = View.GONE
                    }

                    binding.seekBar.visibility = View.VISIBLE
                    binding.vollumeIcon.visibility = View.VISIBLE
                    Glide.with(getActivity())
                        .load(R.drawable.ic_active_speaker_ico)
                        .into( binding.vollumeIcon)
                    binding.playerView.useController = true
                    binding.playerView.controller.visibility = View.VISIBLE
                    binding.playerView.showController()
                    binding.playerView.exo_play.visibility = View.GONE
                    binding.playerView.exo_pause.visibility = View.GONE
                    binding.ivBack.visibility = View.VISIBLE
                    binding.ivMusic.visibility = View.VISIBLE
                    binding.chromeCast.visibility = View.VISIBLE
                    (timer as CountDownTimer).start()

                } else {
                    binding.backword.visibility=View.GONE
                    binding.ivForword.visibility=View.GONE
                    binding.seekBar.visibility=View.GONE
                    binding.title.visibility=View.GONE
                    binding.episodeLayout.visibility=View.GONE
                    ivHideControllerButton.visibility=View.GONE
                    binding.ivBack.visibility=View.GONE
                    binding.chromeCast.visibility = View.GONE

                    binding.transparentLayout.visibility = View.GONE
                    binding.vollumeIcon.visibility = View.GONE
                    Glide.with(getActivity())
                        .load(R.drawable.ic_active_speaker_ico)
                        .into( binding.vollumeIcon)

                    binding.ivPlay.visibility = View.GONE
                    binding.ivPause.visibility = View.GONE
                    binding.ivMusic.visibility = View.GONE

                    timer?.cancel()
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

    private fun callCompleteLog() {
        getVideoTimeDuration()
        streamImg =
            exerciseList.get(previusPos).stream_video_image_url + "thumb/" + exerciseList.get(
                previusPos
            ).stream_video_image
        if (CommanUtils.isNetworkAvailable(getActivity())!!) {
            if (exerciseList.get(previusPos).is_workout != null) {
                if (exerciseList.get(previusPos).is_workout.equals("Yes")) {
                    startActivityForResult(
                        Intent(getActivity(), StreamCompleteActivity::class.java)
                            .putExtra("workout_id", workout_id)
                            .putExtra("duration", duration)
                            .putExtra("isLast", "true")
                            .putExtra("historyId", historyId)
                            .putExtra("from", "create")
                            .putExtra(
                                "video_id",
                                exerciseList.get(previusPos).stream_video_id
                            )
                            .putExtra(
                                "streamImage", streamImage
                            )
//            .putExtra(
//                "streamImage",
//                exerciseList.get(previusPos).stream_video_image
//            )
                            .putExtra(
                                "name",
                                exerciseList.get(previusPos).stream_video_name
                            ), 50
                    )
                    finish()
                } else {
                    finish()
                }
            } else {
                finish()
            }
        } else {
            finish()
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

    }

    override fun onResume() {
        if (binding.playerView != null) {
            intializePlayer(exerciseList.get(previusPos).downLoadUrl, previusPos,1)
            if (binding.videoRv.visibility == View.VISIBLE || isPause) {
                binding.playerView.exo_pause.performClick()
                binding.playerView.exo_play.visibility = View.GONE
                binding.playerView.exo_pause.visibility = View.GONE
                window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                isPause = true
                Constant.requestAudioFocusForMyApp(this)
                binding.chromeCast.visibility = View.GONE
            }
            else {
                Constant.releaseAudioFocusForMyApp(this)
            }

        }
        super.onResume()
    }
    override fun onPause() {
        if (binding.playerView != null) {
            exerciseList.get(previusPos).seekTo = player!!.currentPosition
            releasePlayer()
        }
        Constant.requestAudioFocusForMyApp(this)
        mCastContext!!.sessionManager.removeSessionManagerListener(
            mSessionManagerListener,
            CastSession::class.java
        )
        binding.castDialogLayout.visibility = View.GONE

        super.onPause()
    }


    // class to save(insert if not exist or update if exist) list in local database(Room)

    private fun createPlayedHistory(
        streamVideoId: String,
        workoutId: String,
        idHistory: String,
        isLast: Boolean
    ) {
        Log.d(
            "player state",
            "player state...api...save history" + streamVideoId + "..wid" + workoutId + "..historyid.." + idHistory + "..duration.." + duration
        )
        val param = java.util.HashMap<String, String>()
        param.put("video_id", streamVideoId)
        param.put("stream_workout_id", workoutId)
        param.put("history_id", idHistory)
        param.put("play_time", "" + duration)
        val header = java.util.HashMap<String, String>()
        header.put(StringConstant.AuthToken1, getDataManager().getUserInfo().customer_auth_token)
        getDataManager().createPlayedHistory(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Log.d("response", "response .." + response?.toString(4))
                    try {
                        val json: JSONObject? = response?.getJSONObject("settings")
                        val success = json!!.getString("success")
                        if (success.equals("1")) {
                            if (response.has("data")) {
                                val data: JSONObject? = response?.getJSONObject("data")
                                if (data?.has("history_id")!!) {
                                    historyId = data?.getString("history_id")

                                    exerciseList.get(previusPos).sethistory(historyId)
//                                    if (isLast) {
//                                        finish()
//                                    }
                                }
                            }

                        }

                    } catch (exce: java.lang.Exception) {
                        //  Constant.showCustomToast(getActivity(), getString(R.string.something_wrong))
                    }
                }

                override fun onError(anError: ANError?) {
                    Log.e("Error", "" + anError?.localizedMessage)
                    //   Constant.errorHandle(anError!!, this@StreamVideoPlayUrlActivityTemp)
                }
            })
    }
    fun getVideoTimeDuration() {

        val displayTime = player!!.currentPosition
        var seconds = (displayTime / 1000);
        var minutes = seconds / 60;
        var hour = minutes / 60;
        var timeInMin = ""
        var timeInSec = ""
        //    var  mseconds = displayTime % 60
        if (hour > 0) {
            if (minutes > 0) {
                if (minutes < 10) {
                    timeInMin = "0" + minutes
                    if ((seconds % 60) < 10)
                        timeInSec = "0" + (seconds % 60)
                    else timeInSec = "" + (seconds % 60)

                } else {
                    timeInMin = "" + minutes
                    if ((seconds % 60) < 10)
                        timeInSec = "0" + (seconds % 60)
                    else
                        timeInSec = "" + (seconds % 60)
                }
            } else {
                timeInMin = "00"
                if (seconds > 0 && seconds < 10) {
                    timeInSec = "0" + seconds
                } else {
                    timeInSec = "" + seconds
                }
            }
        } else if (minutes > 0) {
            if (minutes < 10) {
                timeInMin = "0" + minutes
                if ((seconds % 60) < 10)
                    timeInSec = "0" + (seconds % 60)
                else timeInSec = "" + (seconds % 60)

            } else {
                timeInMin = "" + minutes
                if ((seconds % 60) < 10)
                    timeInSec = "0" + (seconds % 60)
                else
                    timeInSec = "" + (seconds % 60)
            }
        } else {
            timeInMin = "00"
            if (seconds > 0 && seconds < 10) {
                timeInSec = "0" + seconds
            } else {
                timeInSec = "" + seconds
            }
        }

        if (hour >= 1) {
            duration = "0" + hour + ":" + minutes + " : " + seconds

        } else {
            duration = "00:" + timeInMin + ":" + timeInSec
        }
    }

    override fun chromeCastListener(message: String) {

        if (message.equals("selected")) {
            isCastConnected = true
            Glide.with(getActivity()).load(R.drawable.ic_mr_button_connected_30_dark)
                .into(binding.chromeCast)
            binding.chromeCast.setColorFilter(
                ContextCompat.getColor(
                    getActivity(),
                    R.color.colorOrange1
                ), android.graphics.PorterDuff.Mode.MULTIPLY
            );
        } else if (message.equals("disconnect")) {
            isCastConnected = false
            mCastContext!!.sessionManager.endCurrentSession(true);
            Glide.with(getActivity()).load(R.drawable.ic_mr_button_connecting_00_dark)
                .into(binding.chromeCast)
            binding.chromeCast.setColorFilter(
                ContextCompat.getColor(
                    getActivity(),
                    R.color.colorWhite
                ), android.graphics.PorterDuff.Mode.MULTIPLY
            );

        }

    }

    private inner class MediaRouterCallback : MediaRouter.Callback() {
        override fun onRouteAdded(router: MediaRouter, info: MediaRouter.RouteInfo) {
            com.google.android.exoplayer2.util.Log.d("root selected", "root added")

            refreshRoutes(false)
        }

        override fun onRouteRemoved(router: MediaRouter, info: MediaRouter.RouteInfo) {
            com.google.android.exoplayer2.util.Log.d("root selected", "root Removed")

            refreshRoutes(false)
        }

        override fun onRouteChanged(router: MediaRouter, info: MediaRouter.RouteInfo) {
            com.google.android.exoplayer2.util.Log.d("root selected", "root canged")

            refreshRoutes(false)
        }

        override fun onRouteSelected(router: MediaRouter, route: MediaRouter.RouteInfo) {
            com.google.android.exoplayer2.util.Log.d("root selected", "root selected")
            chromeCastSelectDisconnect("selected")
        }
    }

    private inner class RouteAdapter(context: Context?) :
        ArrayAdapter<MediaRouter.RouteInfo?>(context!!, 0),
        AdapterView.OnItemClickListener {
        private val mInflater: LayoutInflater
        fun update(isStart: Boolean) {
            clear()
            isitemAdded = false
            val routes: List<MediaRouter.RouteInfo> = mRouter!!.getRoutes()
            val count = routes.size
            for (i in 0 until count) {
                val route = routes[i]
                if (onFilterRoute(route)) {
                    add(route)
                    isitemAdded = true
                }
            }
            try {
                CustomChromeCastBottomSheetDialog.sInstance?.let { sort(it) }
            }catch (e:Exception)
            {
                e.printStackTrace()

            }
            notifyDataSetChanged()

        }

        override fun areAllItemsEnabled(): Boolean {
            return false
        }

        override fun isEnabled(position: Int): Boolean {
            return getItem(position)!!.isEnabled
        }

        override fun getView(
            position: Int,
            convertView: View?,
            parent: ViewGroup
        ): View {
            var view = convertView
            if (view == null) {
                view = mInflater.inflate(R.layout.custom_chrome_cast_item, parent, false)
            }
            val route = getItem(position)
            val text1 = view!!.findViewById<View>(R.id.title) as TextView
            val text2 = view.findViewById<View>(R.id.subtitle) as TextView
            val loader = view.findViewById<View>(R.id.loader) as ProgressBar
            val connectedIcon = view.findViewById<View>(R.id.connected_icon) as ImageView
            text1.text = route!!.name
            val description = route.description
            // Glide.with(mActivity).load(route.iconUri).into(connectedIcon)
/*
                if (TextUtils.isEmpty(description)) {
                    text2.visibility = View.GONE
                    text2.text = ""
                } else {
                    text2.visibility = View.VISIBLE
                    text2.text = description
                }
*/
            view.isEnabled = route.isEnabled
            view.setOnClickListener {
                val route = getItem(position)
                if (route!!.isEnabled) {
                    // route.select();
                    if (!isCastConnected) {
                        mRouter!!.selectRoute(route)
                        loader.visibility=View.VISIBLE
                    }
                    else
                        binding.castDialogLayout.visibility = View.GONE
                    //   mRouter!!.unselect(0)
                    //    dismiss();
                }
            }
            return view
        }

        override fun onItemClick(
            parent: AdapterView<*>?,
            view: View,
            position: Int,
            id: Long
        ) {
            val route = getItem(position)
            if (route!!.isEnabled) {
                route.select()
                binding.castDialogLayout.visibility = View.GONE
            }
        }

        init {
            mInflater = LayoutInflater.from(context)
        }
    }

    open fun onFilterRoute(route: MediaRouter.RouteInfo): Boolean {
        return !route.isDefault && route.matchesSelector(mSelector)
    }
    fun setRouteSelector(selector: MediaRouteSelector?) {
        requireNotNull(selector) { "selector must not be null" }
        if (mSelector != selector) {
            mSelector = selector
            //  if (mAttachedToWindow) {
            mCallback?.let { mRouter!!.removeCallback(it) }
            mCallback?.let {
                mRouter!!.addCallback(
                        selector,
                        it, MediaRouter.CALLBACK_FLAG_PERFORM_ACTIVE_SCAN
                )
            }
            // }
            refreshRoutes(true)
        }
    }

    public fun refreshRoutes(isStart: Boolean) {
        mAdapter?.update(isStart)
    }

    private fun loadRemoteMedia(position: Long, autoPlay: Boolean) {
        if (mCastSession == null) {
            return
        }
        val remoteMediaClient = mCastSession!!.remoteMediaClient ?: return
        remoteMediaClient.registerCallback(object : RemoteMediaClient.Callback() {
            override fun onStatusUpdated() {
                Log.d("expanded player", "expanded player")

                val intent = Intent(getActivity(), ExpandedControlsActivity::class.java).putExtra(
                    "from",
                    "player"
                ).putExtra("streamImage",streamImage)
                startActivityForResult(intent, Constant.CHORME_EXPANDED_CODE)
                remoteMediaClient.unregisterCallback(this)
            }
        })

        mSelectedMedia!!.metadata.addImage(WebImage(Uri.parse(streamImage)))

        remoteMediaClient.load(
            MediaLoadRequestData.Builder()
                .setMediaInfo(mSelectedMedia)
                .setAutoplay(autoPlay)
                .setCurrentTime(position).build()
        )
        binding.ivPause.performClick()
        binding.ivPlay.visibility = View.VISIBLE
        binding.ivPause.visibility = View.GONE
    }

    public inner class RouteComparator : Comparator<MediaRouter.RouteInfo?> {
        init {
            sInstance = RouteComparator()
        }

        override fun compare(lhs: MediaRouter.RouteInfo?, rhs: MediaRouter.RouteInfo?): Int {
            return lhs?.name?.compareTo(rhs!!.name)!!
        }
    }


    private fun setupCastListener(): Unit {
        mSessionManagerListener = object : SessionManagerListener<CastSession> {
            override fun onSessionEnded(session: CastSession, error: Int) {
                Log.d("on session ", "on session...Ended")

                onApplicationDisconnected()
            }

            override fun onSessionResumed(session: CastSession, wasSuspended: Boolean) {
                Log.d("on session ", "on session...Resumed")
                onApplicationConnected(session)
            }

            override fun onSessionResumeFailed(session: CastSession, error: Int) {
                Log.d("on session ", "on session...failed")
                onApplicationDisconnected()
            }

            override fun onSessionStarted(session: CastSession, sessionId: String) {
                Log.d("on session ", "on session...started")
                onApplicationConnected(session)
            }

            override fun onSessionStartFailed(session: CastSession, error: Int) {
                Log.d("on session ", "on session...StartFailed")
                onApplicationDisconnected()
            }

            override fun onSessionStarting(session: CastSession) {
                Log.d("on session ", "on session...Starting")

            }

            override fun onSessionEnding(session: CastSession) {
                Log.d("on session ", "on session...Ending")

            }

            override fun onSessionResuming(session: CastSession, sessionId: String) {
                Log.d("on session ", "on session...Resuming")

            }

            override fun onSessionSuspended(session: CastSession, reason: Int) {
                Log.d("on session ", "on session...Suspended")

            }

            private fun onApplicationConnected(castSession: CastSession) {
                Log.d("on session ", "on session...ApplicationConnected")
                hideNavStatusBar()

                if (castLoaderData.size > 0) {
                    mSelectedMedia = castLoaderData.get(0)
                }

                mCastSession = castSession
                if (null != mSelectedMedia) {
                    loadRemoteMedia(0, true)
                    /* if (mPlaybackState == LocalPlayerActivity.PlaybackState.PLAYING) {
                         player!!.pause()
                         loadRemoteMedia(0, true)
                         return
                     } else {
                         mPlaybackState = LocalPlayerActivity.PlaybackState.IDLE
                         updatePlaybackLocation(PlaybackLocation.REMOTE)
                     }*/
                }
                //   CastButtonFactory.setUpMediaRouteButton(applicationContext, binding.mediaRouteMenuItem1)
                //   invalidateOptionsMenu()

                //  updatePlayButton(mPlaybackState)
                // invalidateOptionsMenu()
            }

            private fun onApplicationDisconnected() {
                hideNavStatusBar()
                isCastConnected = false
                binding.chromeCast.setColorFilter(
                    ContextCompat.getColor(
                        getActivity(),
                        R.color.colorWhite
                    ), android.graphics.PorterDuff.Mode.MULTIPLY
                );

                /*  updatePlaybackLocation(PlaybackLocation.LOCAL)
                  mPlaybackState = LocalPlayerActivity.PlaybackState.IDLE
                  mLocation = PlaybackLocation.LOCAL
                  updatePlayButton(mPlaybackState)
                  invalidateOptionsMenu()*/
                //   CastButtonFactory.setUpMediaRouteButton(applicationContext, binding.mediaRouteMenuItem1)
                //   invalidateOptionsMenu()

            }
        }
    }

    private fun chromeCastInit(){
        if (intent.hasExtra("castMedia")) {
            castLoaderData = intent.getSerializableExtra("castMedia") as ArrayList<MediaInfo>
            if (castLoaderData.size > 0) {
                mSelectedMedia = castLoaderData.get(0)
            }
            Log.e("CreateLoader Call-->","Count List"+castLoaderData.size)
        }

        mCastContext = CastContext.getSharedInstance(this)
        mCastSession = mCastContext?.sessionManager?.currentCastSession
        setupCastListener()
        mCastContext?.sessionManager?.addSessionManagerListener(
            mSessionManagerListener, CastSession::class.java
        )


        invalidateOptionsMenu()
        mAdapter = RouteAdapter(getActivity())
        mRouter = MediaRouter.getInstance(getActivity())
        mCallback = MediaRouterCallback()
        setRouteSelector(mCastContext!!.mergedSelector)


        binding.listView.adapter = mAdapter
        binding.listView.onItemClickListener = mAdapter

        if (mCastSession != null && mCastSession!!.isConnected) {
            if (castLoaderData.size > 0) {
                mSelectedMedia = castLoaderData.get(0)
            }
            if (null != mSelectedMedia) {
                loadRemoteMedia(0, true)
            }
            isCastConnected = true
            Glide.with(getActivity()).load(R.drawable.ic_mr_button_connected_30_dark)
                .into(binding.chromeCast)
            binding.chromeCast.setColorFilter(
                ContextCompat.getColor(
                    getActivity(),
                    R.color.colorOrange1
                ), android.graphics.PorterDuff.Mode.MULTIPLY
            );

        } else {
            isCastConnected = false
            Glide.with(getActivity()).load(R.drawable.ic_mr_button_connecting_00_dark)
                .into(binding.chromeCast)
            binding.chromeCast.setColorFilter(
                ContextCompat.getColor(
                    getActivity(),
                    R.color.colorWhite
                ), android.graphics.PorterDuff.Mode.MULTIPLY
            )

        }
    }

    fun chromeCastSelectDisconnect(message: String) {
        try {
            if (message.equals("selected")) {
                isCastConnected = true
                Glide.with(getActivity()).load(R.drawable.ic_mr_button_connected_30_dark)
                        .into(binding.chromeCast)
                binding.chromeCast.setColorFilter(
                        ContextCompat.getColor(
                                getActivity(),
                                R.color.colorOrange1
                        ), android.graphics.PorterDuff.Mode.MULTIPLY
                )
                binding.castDialogLayout.visibility = View.GONE

            } else if (message.equals("disconnect")) {
                isCastConnected = false
                mCastContext!!.sessionManager.endCurrentSession(true);
                Glide.with(getActivity()).load(R.drawable.ic_mr_button_connecting_00_dark)
                        .into(binding.chromeCast)
                binding.chromeCast.setColorFilter(
                        ContextCompat.getColor(
                                getActivity(),
                                R.color.colorWhite
                        ), android.graphics.PorterDuff.Mode.MULTIPLY
                );

            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
        binding.castDialogLayout.visibility = View.GONE
    }

}


