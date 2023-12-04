package com.doviesfitness.ui.bottom_tabbar.stream_tab.activities

import android.content.Context
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.*
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.databinding.ActivityWorkoutVideoPlayBinding
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.WorkoutExercise
import com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters.WorkoutVideoPlayAdapter
import com.doviesfitness.utils.Constant
import net.khirr.library.foreground.Foreground
import java.util.*
import android.content.pm.ActivityInfo
import android.os.*
import com.doviesfitness.utils.Constant.Companion.releaseAudioFocusForMyApp
import com.doviesfitness.utils.Constant.Companion.requestAudioFocusClose
import com.doviesfitness.utils.Constant.Companion.requestAudioFocusForMyApp
import java.lang.Exception
import android.content.res.Configuration
import android.media.AudioAttributes
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.Window
import android.widget.RelativeLayout
import com.doviesfitness.ui.bottom_tabbar.home_tab.activity.LinearLayoutManagerWithSmoothScroller
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.WorkoutDetail
import android.media.SoundPool
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.doviesfitness.databinding.ActivityStreamVideoPlayBinding
import com.doviesfitness.ui.bottom_tabbar.stream_tab.adapters.StreamVideoPlayAdapter
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.StreamWorkoutDetailModel
import com.doviesfitness.ui.bottom_tabbar.workout_tab.activity.WorkoutCompleteActivity
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_video_detail.*


class StreamVideoPlayActivity : BaseActivity(), View.OnClickListener,
    StreamVideoPlayAdapter.OnItemClick {



    private var from_which_frament: String = ""
    private var program_plan_id: String? = ""
    lateinit var binding: ActivityStreamVideoPlayBinding
    lateinit var adapter: StreamVideoPlayAdapter
    lateinit var exerciseList: ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>
    private var countdownRunnable: Runnable? = null
    private val countdownhandler = Handler()
    var itemPosition: Int = 0
     var mPlayer: MediaPlayer?=null
    var timerTime = 0;
    var TotalTime = 0;
    var isPause = false
    var stopPosition = 0
    var remainingTime = 0
    var RestremainingTime: Long = 0
    var remainingUpTime = 0
    var mCountDownTimer: CountDownTimer? = null
    var mCountUpTimer: CountDownTimer? = null
    var restTimeCountDownTimer: CountDownTimer? = null
    var breakTime = 0;
    var isRestTimePlaying = false
    lateinit var audioPlayer: MediaPlayer
    lateinit var animation: TranslateAnimation
    lateinit var animation1: TranslateAnimation
    var mStartTime: Long = SystemClock.uptimeMillis();
    var mCurrentLapse: Long = 0
    var mTime: Long = 0
    lateinit var layoutManager: LinearLayoutManagerWithSmoothScroller
    var mHeight: Int = 0
    var workout_id = ""
    var workout_name = ""
    var duration = ""
    var aboutToStartMusic = false
    var isStoptoMusic = false
    var WDetail: WorkoutDetail? = null
    var smoothScroller: androidx.recyclerview.widget.RecyclerView.SmoothScroller? = null
    var orientation: Int = -10
    //////
    private lateinit var player: SimpleExoPlayer
    private lateinit var mediaDataSourceFactory: DataSource.Factory
    private val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory()
    private val ivHideControllerButton: LinearLayout by lazy {
        findViewById<LinearLayout>(R.id.controller) }
    private var lastSeenTrackGroupArray: TrackGroupArray? = null
    private var trackSelector: DefaultTrackSelector? = null
    private var currentWindow: Int = 0
    private var playbackPosition: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        hideNavStatusBar()
        super.onCreate(savedInstanceState)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_stream_video_play)
        orientation = getActivity().getResources().getConfiguration().orientation
       /* if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            landscapeFunctionality()
            hideNavStatusBar()
        } else {
            porttrailFunctionality(0)
        }*/
        landscapeFunctionality()
        smoothScroller = object : androidx.recyclerview.widget.LinearSmoothScroller(this) {
            override fun getVerticalSnapPreference(): Int {
                return androidx.recyclerview.widget.LinearSmoothScroller.SNAP_TO_START
            }
        }
        exerciseList = intent.getSerializableExtra("videoList") as ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>
        ///      WDetail = intent.getSerializableExtra("WDetail") as WorkoutDetail
        if (intent.hasExtra("from_ProgramPlan")) {
            if (intent.getStringExtra("from_ProgramPlan") != null) {
                program_plan_id = intent.getStringExtra("from_ProgramPlan")!!
            }
        }

        initialisation()
    }

    fun hideNavStatusBar() {
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        )
    }

    private fun initialisation() {
        binding.musicLayout.setOnClickListener(this)
        binding.previousLayout.setOnClickListener(this)
        binding.pauseLayout.setOnClickListener(this)
        binding.nextLayout.setOnClickListener(this)
        binding.playVideo.setOnClickListener(this)
        binding.playVideo1.setOnClickListener(this)
        binding.musicLayout11.setOnClickListener(this)
        binding.musicLayout1.setOnClickListener(this)
        binding.endWorkoutLayout.setOnClickListener(this)
        binding.endWorkoutLayout1.setOnClickListener(this)
        binding.pauseWorkoutLayout.setOnClickListener(this)
        binding.orientationLayout.setOnClickListener(this)
        binding.orientationLayout1.setOnClickListener(this)
        binding.landscapeOrientationLayout.setOnClickListener(this)
        binding.videoView.setOnClickListener(this)
        binding.view.setOnClickListener(this)
        binding.shadowLayout.setOnClickListener(this)
        binding.view.setOnClickListener(this)
        binding.view1.setOnClickListener(this)
        binding.videoView.setDrawingCacheEnabled(true);
        audioPlayer = MediaPlayer.create(this, R.raw.boxingbell)
        adapter = StreamVideoPlayAdapter(getActivity(), exerciseList, this)
        layoutManager = LinearLayoutManagerWithSmoothScroller(getActivity())
        binding.videoRv.layoutManager = layoutManager
        binding.videoRv.adapter = adapter

        binding.videoView.setOnPreparedListener(object : MediaPlayer.OnPreparedListener {
            override fun onPrepared(mp: MediaPlayer?) {
                mPlayer = mp!!
                mPlayer?.isLooping = true
                Log.d("video width", "video width...." + mp.videoWidth)
                Log.d("video height", "video height...." + mp.videoHeight)
            }
        })
        binding.videoView.setOnCompletionListener(object : MediaPlayer.OnCompletionListener {
            override fun onCompletion(mp: MediaPlayer?) {

            }
        })

        binding.shadowLayout.viewTreeObserver.addOnGlobalLayoutListener {
            if (mHeight == 0) {
                mHeight = binding.shadowLayout.height
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            var sp21 = SoundPool.Builder()
            sp21.setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
        }
        /* if (!audioPlayer.isPlaying)
             audioPlayer.start()*/
     /*   var uri = Uri.parse(exerciseList.get(0).stream_video)
        binding.videoView.stopPlayback()
        binding.videoView.setVideoURI(uri)
        binding.videoView.start()*/

        intializePlayer(exerciseList.get(0).stream_video)

         // playExercise()
          mStartTime = SystemClock.uptimeMillis()
          countdownRunnable = object : Runnable {
              override fun run() {
                  mCurrentLapse = SystemClock.uptimeMillis() - mStartTime;
                  val displayTime = mTime + mCurrentLapse
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
                      binding.countDownTime.text = "0" + hour + ":" + minutes + " : " + seconds
                      binding.totaltime.text = "0" + hour + ":" + minutes + " : " + seconds
                      binding.totaltime1.text = "0" + hour + ":" + minutes + " : " + seconds
                      duration = "0" + hour + ":" + minutes + " : " + seconds

                  } else {
                      binding.countDownTime.text = timeInMin + ":" + timeInSec
                      binding.totaltime.text = timeInMin + ":" + timeInSec
                      binding.totaltime1.text = timeInMin + ":" + timeInSec
                      duration = "00:" + timeInMin + ":" + timeInSec
                  }
                  countdownhandler.postDelayed(this, 0)
              }
          }
          countdownhandler.postDelayed(countdownRunnable!!, 0);

         /* animation = TranslateAnimation(0f, 0f, 300f, 0f);
          animation.setInterpolator(AccelerateInterpolator());
          animation.setDuration(1000)

          binding.playerView.startAnimation(animation)*/

    }
    private fun updateStartPosition() {
        try {
            if (player!=null)
                with(player) {
                    playbackPosition = currentPosition
                    currentWindow = currentWindowIndex
                    playWhenReady = true
                }
        }catch (e:java.lang.Exception){

        }
    }

    private fun releasePlayer() {
        if(player != null){
            updateStartPosition()
            player.release()
            trackSelector = null
        }
    }

  /*  public override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) releasePlayer()
    }*/

    public override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) releasePlayer()
    }
    fun intializePlayer(news_video: String) {
        trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)
        mediaDataSourceFactory = DefaultDataSourceFactory(this, Util.getUserAgent(this, "mediaPlayerSample"))
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector!!)
        val mediaSource = ExtractorMediaSource.Factory(mediaDataSourceFactory)
            .createMediaSource(Uri.parse(news_video))
        with(player) {
            prepare(mediaSource, false, false)
            playWhenReady = true
        }
        ivHideControllerButton.visibility = View.VISIBLE
        playerView.showController()
        playerView.setShowBuffering(1!!)
        playerView.setShutterBackgroundColor(ContextCompat.getColor(this,R.color.colorBlack))
        playerView.player = player
        playerView.requestFocus()

       // playerView.hideController()

       /*  playerView.setOnClickListener {
         playerView.showController()
         }*/

      //  ivHideControllerButton.visibility = View.VISIBLE
        lastSeenTrackGroupArray = null
/*
        layoutManager.smoothScrollToPosition(
            binding.videoRv,
            androidx.recyclerview.widget.RecyclerView.State(),
            itemPosition
        )
*/

        updateProgress(0)

    }

    public override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideNavStatusBar()
            //   hideNavigationBar()
        }
    }

    fun landscapeFunctionality() {
        var params1 = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        params1.setMargins(
            resources.getDimension(R.dimen._50sdp).toInt(),
            resources.getDimension(R.dimen._30sdp).toInt(),
            0,
            0
        );
        binding.countDownTime.setLayoutParams(params1)
        var params2 = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        params2.setMargins(
            0,
            resources.getDimension(R.dimen._30sdp).toInt(),
            resources.getDimension(R.dimen._65sdp).toInt(),
            0
        );
        params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
        binding.repeat.setLayoutParams(params2)

        var params3 = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        params3.setMargins(0, 0, resources.getDimension(R.dimen._22sdp).toInt(), 0);
        params3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        params3.height = resources.getDimension(R.dimen._40sdp).toInt()
        binding.playControlerLayout.setLayoutParams(params3)

        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        val params =
            binding.playerView.getLayoutParams() as android.widget.RelativeLayout.LayoutParams

        if (metrics.widthPixels == 3040 || metrics.heightPixels == 1440 || metrics.widthPixels > 3040 || metrics.heightPixels > 1440) {
            var percent = (10 * metrics.widthPixels) / 100
            params.width = (metrics.widthPixels) - percent
            params.height = metrics.heightPixels
            params.setMargins(resources.getDimension(R.dimen._25sdp).toInt(), 0, 0, 0);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP)
            binding.playerView.setLayoutParams(params)
            binding.shadowLayout.setLayoutParams(params)
            binding.restTimeLayout.setLayoutParams(params)
        } else if (metrics.heightPixels == 720 && metrics.widthPixels > 1280) {
            var VWidth = (1280 + metrics.heightPixels - 720)
            var VParams = RelativeLayout.LayoutParams(VWidth, metrics.heightPixels)
            VParams.addRule(RelativeLayout.CENTER_IN_PARENT)
            binding.playerView.setLayoutParams(VParams)
            binding.shadowLayout.setLayoutParams(VParams)
            binding.restTimeLayout.setLayoutParams(VParams)
        } else if (metrics.heightPixels <= 720 && metrics.widthPixels <= 1280) {
            params.width = metrics.widthPixels
            params.height = metrics.heightPixels
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP)
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            binding.playerView.setLayoutParams(params)
            binding.shadowLayout.setLayoutParams(params)
            binding.restTimeLayout.setLayoutParams(params)
        } else if (metrics.heightPixels == 1080 && metrics.widthPixels > 1280) {
            params1.setMargins(
                resources.getDimension(R.dimen._60sdp).toInt(),
                resources.getDimension(R.dimen._30sdp).toInt(),
                0,
                0
            );
            binding.countDownTime.setLayoutParams(params1)

            params3.setMargins(
                resources.getDimension(R.dimen._10sdp).toInt(),
                0,
                resources.getDimension(R.dimen._20sdp).toInt(),
                0
            );
            params3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            params3.height = resources.getDimension(R.dimen._40sdp).toInt()
            binding.playControlerLayout.setLayoutParams(params3)


            var percent = (10 * metrics.widthPixels) / 100
            params.width = (metrics.widthPixels) - percent
            params.height = metrics.heightPixels
            params.setMargins(resources.getDimension(R.dimen._25sdp).toInt(), 0, 0, 0);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP)
            params.addRule(RelativeLayout.CENTER_HORIZONTAL)
            binding.playerView.setLayoutParams(params)
            binding.shadowLayout.setLayoutParams(params)
            binding.restTimeLayout.setLayoutParams(params)
        } else {
            var percent = (10 * metrics.widthPixels) / 100
            params.width = (metrics.widthPixels) - percent
            params.height = metrics.heightPixels
            params.setMargins(resources.getDimension(R.dimen._25sdp).toInt(), 0, 0, 0);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP)
            params.addRule(RelativeLayout.CENTER_HORIZONTAL)
            binding.playerView.setLayoutParams(params)
            binding.shadowLayout.setLayoutParams(params)
            binding.restTimeLayout.setLayoutParams(params)
        }


        if (binding.pauseWorkoutLayout.visibility == View.VISIBLE) {
            binding.pauseWorkoutLayout1.visibility = View.VISIBLE
            binding.pauseWorkoutLayout.visibility = View.GONE
        }
        binding.playControlerLayout.setBackgroundResource(android.R.color.transparent)
        Glide.with(getActivity()).load(R.drawable.full_screen_ico).into(binding.orientationIcon)
        binding.landscapeExerciseTimeLayout.visibility = View.VISIBLE
        binding.playControlerLayout.visibility = View.GONE
        Glide.with(getActivity()).load(R.drawable.full_screen_ico).into(binding.orientationIcon)
        binding.restTv.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            getResources().getDimension(R.dimen._40sdp)
        )
        binding.restTimeTv.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            getResources().getDimension(R.dimen._55sdp)
        )
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        hideNavStatusBar()
        //  hideNavigationBar()
        orientation = getActivity().getResources().getConfiguration().orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            porttrailFunctionality(1)
        } else {
            landscapeFunctionality()
        }
        //   setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }


    override fun onLayoutClick(pos: Int) {
          if (exerciseList.get(pos).Progress != 0) {
          } else if (itemPosition == pos) {
          } else {
        if (itemPosition < exerciseList.size) {
            if (itemPosition < pos) {
                for (i in 0..pos - 1) {
                    exerciseList.get(i).isComplete = true
                    exerciseList.get(i).Progress = 0
                    exerciseList.get(i).isRestTime = false
                    adapter.notifyItemChanged(i)
                }
            } else {
                for (i in pos + 1..exerciseList.size - 1) {
                    exerciseList.get(i).isComplete = false
                    exerciseList.get(i).Progress = 0
                    exerciseList.get(i).isRestTime = false
                    adapter.notifyItemChanged(i)
                }
            }
            if (!audioPlayer.isPlaying)
                audioPlayer.start()

            if (pos != 0)
                Glide.with(getActivity()).load(R.drawable.ic_white_left_arrow_ico).into(binding.previousIcon)
            else
                Glide.with(getActivity()).load(R.drawable.ic_gray_left_arrow).into(binding.previousIcon)
            if (pos != exerciseList.size - 1)
                Glide.with(getActivity()).load(R.drawable.ic_white_right_arrow_ico).into(binding.nextIcon)
            else
                Glide.with(getActivity()).load(R.drawable.ic_gray_right_arrow).into(binding.nextIcon)
        }
        if (restTimeCountDownTimer != null)
            restTimeCountDownTimer!!.cancel()
        binding.restTimeLayout.visibility = View.GONE
        itemPosition = pos
             timerTime = 0
           mCountDownTimer!!.cancel()
       // playExercise()
        intializePlayer(exerciseList.get(itemPosition).stream_video)
        //   binding.flVideoView.startAnimation(animation)
         }
    }


    fun porttrailFunctionality(from: Int) {
        var params1 = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        params1.setMargins(
            resources.getDimension(R.dimen._10sdp).toInt(),
            resources.getDimension(R.dimen._20sdp).toInt(),
            0,
            0
        );
        binding.countDownTime.setLayoutParams(params1);

        var params2 = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        params2.setMargins(
            0,
            resources.getDimension(R.dimen._20sdp).toInt(),
            resources.getDimension(R.dimen._10sdp).toInt(),
            0
        );
        params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
        binding.repeat.setLayoutParams(params2);

        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        //  getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        val params =
            binding.playerView.getLayoutParams() as android.widget.RelativeLayout.LayoutParams
        params.width = metrics.widthPixels



        if (from == 0) {
            params.height = resources.getDimension(R.dimen._185sdp).toInt()
        } else {
            params.height = mHeight
        }

        params.setMargins(0, 0, 0, 0);

        params.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        params.removeRule(RelativeLayout.ALIGN_PARENT_TOP)
        params.removeRule(RelativeLayout.CENTER_IN_PARENT)
        params.removeRule(RelativeLayout.CENTER_HORIZONTAL)
        params.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT)
        params.removeRule(RelativeLayout.ALIGN_PARENT_LEFT)


        binding.playerView.setLayoutParams(params)
        binding.restTimeLayout.setLayoutParams(params)
        binding.shadowLayout.setLayoutParams(params)

        var params3 = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        params3.setMargins(0, 0, 0, 0);
        params3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        params3.height = resources.getDimension(R.dimen._40sdp).toInt()
        binding.playControlerLayout.setLayoutParams(params3)

        /* var params4 = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
         params4.setMargins(0, 0, 0, 0);
         params4.addRule(RelativeLayout.CENTER_IN_PARENT)

         binding.pauseWorkoutLayout.setLayoutParams(params4)*/



        Glide.with(getActivity()).load(R.drawable.ic_crop_ico).into(binding.orientationIcon)

        binding.restTv.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            getResources().getDimension(R.dimen._28sdp)
        )
        binding.restTimeTv.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            getResources().getDimension(R.dimen._40sdp)
        )
        binding.playControlerLayout.visibility = View.VISIBLE
        binding.playControlerLayout.setBackgroundResource(R.color.splash_screen_color)
        Glide.with(getActivity()).load(R.drawable.ic_crop_ico).into(binding.orientationIcon)
        binding.landscapeExerciseTimeLayout.visibility = View.GONE
        binding.playControlerLayout.visibility = View.VISIBLE
        if (binding.pauseWorkoutLayout1.visibility == View.VISIBLE) {
            binding.pauseWorkoutLayout.visibility = View.VISIBLE
            binding.pauseWorkoutLayout1.visibility = View.GONE
        }

    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                onBackPressed()
                requestAudioFocusClose(this)
            }

            R.id.music_layout11 -> {
                aboutToStartMusic()
                aboutToStartMusic = false
                isStoptoMusic = true
                playerPauseState()
            }
            R.id.music_layout -> {
                aboutToStartMusic()
                aboutToStartMusic = false
                isStoptoMusic = true
               // playerPauseState()

            }
            R.id.music_layout1 -> {
                isStoptoMusic = true
                try {
                    val intent = Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER)
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            R.id.pause_workout_layout -> {

            }
            R.id.view -> {
                binding.shadowLayout.performClick()
            }
            R.id.view1 -> {
                binding.shadowLayout.performClick()
            }
            R.id.shadow_layout -> {
                hideNavStatusBar()
                //    hideNavigationBar()
                //  getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

                if (binding.pauseWorkoutLayout1.visibility == View.GONE) {
                    var orientation = getActivity().getResources().getConfiguration().orientation

                    binding.playControlerLayout.visibility = View.VISIBLE
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        object : CountDownTimer(3000, 10) {
                            override fun onTick(millisUntilFinished: Long) {
                            }

                            override fun onFinish() {
                                orientation =
                                    getActivity().getResources().getConfiguration().orientation
                                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                                    binding.playControlerLayout.visibility = View.VISIBLE
                                } else {
                                    binding.playControlerLayout.visibility = View.GONE
                                }

                                cancel()
                            }
                        }.start()
                    }
                }
            }
            R.id.end_workout_layout -> {
                requestAudioFocusClose(this)
                countdownhandler.removeCallbacks(countdownRunnable!!)
                mCountDownTimer!!.cancel()
                if (restTimeCountDownTimer != null)
                    restTimeCountDownTimer!!.cancel()

                startActivity(
                    Intent(getActivity(), WorkoutCompleteActivity::class.java).putExtra(
                        "WDetail",
                        WDetail)
                        .putExtra("duration", duration)
                        .putExtra("from_ProgramPlan", program_plan_id)
                        .putExtra("from_which_frament", from_which_frament)
                )
                finish()
            }
            R.id.end_workout_layout1 -> {
                requestAudioFocusClose(this)
                countdownhandler.removeCallbacks(countdownRunnable!!)
                mCountDownTimer!!.cancel()
                if (restTimeCountDownTimer != null)
                    restTimeCountDownTimer!!.cancel()
                startActivity(
                    Intent(getActivity(), WorkoutCompleteActivity::class.java).putExtra(
                        "WDetail",
                        WDetail
                    )
                        .putExtra("duration", duration)
                        .putExtra("from_ProgramPlan", program_plan_id)
                        .putExtra("from_which_frament", from_which_frament)
                )
                finish()
            }
            R.id.previous_layout -> {
                if (itemPosition != 0 || itemPosition < 0) {
                    if (itemPosition < exerciseList.size) {
                        exerciseList.get(itemPosition).Progress = 0
                        exerciseList.get(itemPosition).isRestTime = false
                        adapter.notifyItemChanged(itemPosition)
                    }
                    Glide.with(getActivity()).load(R.drawable.ic_white_right_arrow_ico)
                        .into(binding.nextIcon)
                    itemPosition = itemPosition - 1
                    if (itemPosition != 0)
                        Glide.with(getActivity()).load(R.drawable.ic_white_left_arrow_ico).into(
                            binding.previousIcon
                        )
                    else
                        Glide.with(getActivity()).load(R.drawable.ic_gray_left_arrow).into(binding.previousIcon)
                    timerTime = 0
                    mCountDownTimer!!.cancel()

                    if (restTimeCountDownTimer != null)
                        restTimeCountDownTimer!!.cancel()
                    binding.restTimeLayout.visibility = View.GONE
                    if (!audioPlayer.isPlaying)
                        audioPlayer.start()
                   // playExercise()
                    intializePlayer(exerciseList.get(itemPosition).stream_video)

                    // binding.flVideoView.startAnimation(animation)
                }
            }
            R.id.pause_layout -> {
                pausePlayer()
                playerPauseState()
                isStoptoMusic = true
            }
            R.id.play_video -> {
                isStoptoMusic = false
                if (isRestTimePlaying == true) {
                    binding.restTimeLayout.visibility = View.VISIBLE
                   // restTimeCountDown(RestremainingTime)
                    binding.repeat.visibility = View.GONE
                } else {

                    player.seekTo(stopPosition.toLong())
                    player.setPlayWhenReady(true);
                    player.getPlaybackState()
                  //  binding.videoView.seekTo(stopPosition)
                //    binding.videoView.start()
                    //  countUpTimer(0)
                    // startTimer()

                    updateProgress(remainingTime)
                }
                mStartTime = SystemClock.uptimeMillis();
                countdownhandler.postDelayed(countdownRunnable!!, 0);
                binding.countDownTime.visibility = View.VISIBLE
                releaseAudioFocusForMyApp(this)
                isPause = false
                binding.pauseWorkoutLayout.visibility = View.GONE
            }
            R.id.play_video1 -> {
                isStoptoMusic = false
                binding.landscapeExerciseTimeLayout.visibility = View.VISIBLE
                if (isRestTimePlaying == true) {
                    binding.restTimeLayout.visibility = View.VISIBLE
                   // restTimeCountDown(RestremainingTime)
                    binding.repeat.visibility = View.GONE
                } else {
                    binding.videoView.seekTo(stopPosition)
                    binding.videoView.start()
                    //  countUpTimer(0)
                    // startTimer()
/*
                    if (exerciseList.get(itemPosition).workout_exercise_type.equals("Repeat")) {
                        binding.repeat.visibility = View.VISIBLE
                    } else {
                        binding.repeat.visibility = View.GONE
                    }
*/
                    updateProgress(remainingTime)
                }
                mStartTime = SystemClock.uptimeMillis();
                countdownhandler.postDelayed(countdownRunnable!!, 0);
                binding.countDownTime.visibility = View.VISIBLE
                releaseAudioFocusForMyApp(this);
                isPause = false
                binding.pauseWorkoutLayout1.visibility = View.GONE
            }
            R.id.next_layout -> {
                if (itemPosition != exerciseList.size - 1 && itemPosition != exerciseList.size) {
                    if (itemPosition < exerciseList.size) {
                        exerciseList.get(itemPosition).isComplete = true
                        exerciseList.get(itemPosition).Progress = 0
                        exerciseList.get(itemPosition).isRestTime = false
                        adapter.notifyItemChanged(itemPosition)
                    }
                    Glide.with(getActivity()).load(R.drawable.ic_white_left_arrow_ico)
                        .into(binding.previousIcon)
                    itemPosition = itemPosition + 1
                    if (itemPosition != exerciseList.size - 1)
                        Glide.with(getActivity()).load(R.drawable.ic_white_right_arrow_ico).into(
                            binding.nextIcon
                        )
                    else
                        Glide.with(getActivity()).load(R.drawable.ic_gray_right_arrow).into(binding.nextIcon)
                    timerTime = 0
                   mCountDownTimer!!.cancel()

                    if (restTimeCountDownTimer != null)
                        restTimeCountDownTimer!!.cancel()
                    binding.restTimeLayout.visibility = View.GONE
                    intializePlayer(exerciseList.get(itemPosition).stream_video)
                    if (!audioPlayer.isPlaying)
                        audioPlayer.start()
                    //   binding.flVideoView.startAnimation(animation)
                }
            }
            R.id.orientation_layout -> {
                val orientation = getActivity().getResources().getConfiguration().orientation
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                    Glide.with(getActivity()).load(R.drawable.full_screen_ico)
                        .into(binding.orientationIcon)
                    binding.landscapeExerciseTimeLayout.visibility = View.VISIBLE
                    binding.playControlerLayout.visibility = View.GONE

                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    Glide.with(getActivity()).load(R.drawable.ic_crop_ico)
                        .into(binding.orientationIcon)
                    binding.landscapeExerciseTimeLayout.visibility = View.GONE
                    binding.playControlerLayout.visibility = View.VISIBLE
                }
            }
            R.id.orientation_layout1 -> {
                val orientation = getActivity().getResources().getConfiguration().orientation
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    Glide.with(getActivity()).load(R.drawable.full_screen_ico)
                        .into(binding.orientationIcon)
                    binding.landscapeExerciseTimeLayout.visibility = View.VISIBLE
                    binding.playControlerLayout.visibility = View.GONE
                    if (binding.pauseWorkoutLayout.visibility == View.VISIBLE) {
                        binding.pauseWorkoutLayout1.visibility = View.VISIBLE
                        binding.pauseWorkoutLayout.visibility = View.GONE
                    }

                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    Glide.with(getActivity()).load(R.drawable.ic_crop_ico)
                        .into(binding.orientationIcon)
                    binding.landscapeExerciseTimeLayout.visibility = View.GONE
                    binding.playControlerLayout.visibility = View.VISIBLE
                }
            }
            R.id.landscape_orientation_layout -> {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                binding.landscapeExerciseTimeLayout.visibility = View.GONE
                binding.playControlerLayout.visibility = View.VISIBLE
                if (binding.pauseWorkoutLayout1.visibility == View.VISIBLE) {
                    binding.pauseWorkoutLayout.visibility = View.VISIBLE
                    binding.pauseWorkoutLayout1.visibility = View.GONE
                }

            }
        }
    }



/*
    fun playExercise() {
        binding.restTimeLayout.visibility = View.GONE
        binding.landscapeExerciseName.text = exerciseList.get(itemPosition).stream_video_name

        if (itemPosition < exerciseList.size) {
            if (!audioPlayer.isPlaying)
                audioPlayer.start()
            var uri = Uri.parse(exerciseList.get(itemPosition).stream_video)

            // for Repeat text

*/
/*
            if (exerciseList.get(itemPosition).workout_exercise_type.equals("Repeat")) {
                binding.repeat.visibility = View.VISIBLE
                binding.repeat.text = exerciseList.get(itemPosition).workout_repeat_text + " " + "Reps"
            } else {
                binding.repeat.visibility = View.GONE
            }
*//*



            binding.videoView.stopPlayback();

            binding.videoView.setVideoURI(uri)
            binding.videoView.start()
        }
        //   isRestTimePlaying = false
        //binding.videoRv.smoothScrollBy(0,300)

        smoothScroller?.targetPosition = itemPosition
        layoutManager.startSmoothScroll(smoothScroller)
        layoutManager.smoothScrollToPosition(
            binding.videoRv,
            androidx.recyclerview.widget.RecyclerView.State(),
            itemPosition
        )
        //  binding.videoRv.scrollBy(0,500)
          updateProgress(0)
    }
*/


    private fun aboutToStartMusic() {
        try {
            aboutToStartMusic = true
            val intent = Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER)
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    fun updateProgress(remainingTime1: Int) {
        val endTime =
            ((exerciseList.get(itemPosition).MaxProgress * 1000).toLong() - remainingTime1)
           // ((30 * 1000).toLong() - remainingTime1)
        mCountDownTimer = object : CountDownTimer(endTime, 10) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTime = (endTime - millisUntilFinished).toInt() + remainingTime1
                Log.d("complete time....", "complete time....." + millisUntilFinished)
                if (millisUntilFinished.toInt() < 30) {
                    exerciseList.get(itemPosition).isComplete = true
                } else {
                    exerciseList.get(itemPosition).isComplete = false
                }
                exerciseList.get(itemPosition).Progress =
                    ((endTime - millisUntilFinished).toInt() + remainingTime1)
                val mProgress =
                    exerciseList.get(itemPosition).MaxProgress - ((endTime - millisUntilFinished).toInt() + remainingTime1) / 1000
                //  val mProgress = (millisUntilFinished + remainingTime1) / 1000

                if (mProgress >= 60) {
                    var min = "00"
                    var sec = "00"
                    if ((mProgress / 60) < 10) {
                        min = (mProgress / 60).toString()
                    } else {
                        min = (mProgress / 60).toString()
                    }
                    if ((mProgress % 60) < 10) {
                        sec = "0" + (mProgress % 60).toString()
                    } else {
                        sec = (mProgress % 60).toString()
                    }

                    binding.landscapeExerciseTime.text = min + ":" + sec
                } else {
                    if (mProgress > -1) {
                        if (mProgress < 10) {
                            binding.landscapeExerciseTime.text = "0:0" + (mProgress).toString()
                        } else
                            binding.landscapeExerciseTime.text = "0:" + (mProgress).toString()
                    }
                }

                adapter.notifyItemChanged(itemPosition)

            }

            override fun onFinish() {
               // if (exerciseList.get(itemPosition).workout_exercise_break_time.equals("00:00:00")) {
                    exerciseList.get(itemPosition).isComplete = true
                    exerciseList.get(itemPosition).Progress = 0
                    exerciseList.get(itemPosition).isRestTime = false
                    adapter.notifyItemChanged(itemPosition)
                    itemPosition += 1;
                    if (itemPosition < exerciseList.size) {
                        Glide.with(getActivity()).load(R.drawable.ic_white_left_arrow_ico)
                            .into(binding.previousIcon)
                        if (itemPosition != exerciseList.size - 1)
                            Glide.with(getActivity()).load(R.drawable.ic_white_right_arrow_ico).into(
                                binding.nextIcon
                            )
                        else
                            Glide.with(getActivity()).load(R.drawable.ic_gray_right_arrow).into(
                                binding.nextIcon
                            )

                        mCountDownTimer!!.cancel()
                     //   playExercise()
                        intializePlayer(exerciseList.get(itemPosition).stream_video)
                        if (!audioPlayer.isPlaying)
                            audioPlayer.start()
                        //   binding.flVideoView.startAnimation(animation)
                    } else {
                        binding.videoView.stopPlayback()
                        if (!audioPlayer.isPlaying)
                            audioPlayer.start()

/*
                        startActivity(
                            Intent(
                                getActivity(),
                                WorkoutCompleteActivity::class.java
                            ).putExtra("WDetail", WDetail)
                                .putExtra("duration", duration)
                                .putExtra("from_ProgramPlan", program_plan_id)
                                .putExtra("from_which_frament", from_which_frament)
                        )
*/

                        mCountDownTimer!!.cancel()
                        finish()
                    }
              //  }
               /* else {
                    exerciseList.get(itemPosition).isComplete = true
                    exerciseList.get(itemPosition).Progress = 0
                    exerciseList.get(itemPosition).isRestTime = true
                    adapter.notifyItemChanged(itemPosition)
                    mCountDownTimer!!.cancel()
                    if (!audioPlayer.isPlaying)
                        audioPlayer.start()
                    breakTime =
                        Constant.getExerciseTime(exerciseList.get(itemPosition).workout_exercise_break_time)
                    breakTime = breakTime + 1
                    restTimeCountDown((breakTime * 1000).toLong())
                    //////////rest time
                }*/
            }
        }
        mCountDownTimer?.start()
    }


/*
    fun restTimeCountDown(endTime: Long) {
        restTimeCountDownTimer = object : CountDownTimer(endTime, 10) {
            override fun onTick(millisUntilFinished: Long) {
                binding.landscapeExerciseTimeLayout.visibility = View.GONE
                RestremainingTime = millisUntilFinished
                isRestTimePlaying = true
                binding.restTimeLayout.visibility = View.VISIBLE
                binding.repeat.visibility = View.GONE
                val mProgress = millisUntilFinished / 1000

                if (mProgress > 60) {
                    binding.restTimeTv.text =
                        (mProgress / 60).toString() + ":" + (mProgress % 60).toString()
                } else {
                    if (mProgress < 10) {
                        binding.restTimeTv.text = "00:0" + (mProgress).toString()
                    } else
                        binding.restTimeTv.text = "00:" + (mProgress).toString()
                }
            }

            override fun onFinish() {
                isRestTimePlaying = false
                binding.restTimeLayout.visibility = View.GONE
                exerciseList.get(itemPosition).isComplete = true
                exerciseList.get(itemPosition).Progress = 0
                exerciseList.get(itemPosition).isRestTime = false
                adapter.notifyItemChanged(itemPosition)
                itemPosition += 1;
                if (itemPosition < exerciseList.size) {
                    Glide.with(getActivity()).load(R.drawable.ic_white_left_arrow_ico)
                        .into(binding.previousIcon)
                    if (itemPosition != exerciseList.size - 1)
                        Glide.with(getActivity()).load(R.drawable.ic_white_right_arrow_ico).into(
                            binding.nextIcon
                        )
                    else
                        Glide.with(getActivity()).load(R.drawable.ic_gray_right_arrow).into(binding.nextIcon)
                    val orientation = getActivity().getResources().getConfiguration().orientation
                    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                        binding.landscapeExerciseTimeLayout.visibility = View.GONE
                    } else {
                        binding.landscapeExerciseTimeLayout.visibility = View.VISIBLE
                    }

                    playExercise()
                    if (!audioPlayer.isPlaying)
                        audioPlayer.start()
                    //   binding.flVideoView.startAnimation(animation)
                } else {
                    binding.videoView.stopPlayback()
*/
/*
                    startActivity(
                        Intent(
                            getActivity(),
                            WorkoutCompleteActivity::class.java
                        ).putExtra("WDetail", WDetail)
                            .putExtra("duration", duration)
                            .putExtra("from_ProgramPlan", program_plan_id)
                            .putExtra("from_which_frament", from_which_frament)
                    )
*//*

                    mCountDownTimer!!.cancel()
                    finish()
                }
                restTimeCountDownTimer!!.cancel()
            }
        }
        restTimeCountDownTimer?.start()
    }
*/

/*
    override fun onBackPressed() {
        super.onBackPressed()
        requestAudioFocusForMyApp(this)
        if (mCountDownTimer != null)
            mCountDownTimer!!.cancel()
        if (restTimeCountDownTimer != null)
            restTimeCountDownTimer!!.cancel()
        // Foreground.removeListener(foregroundListener)
    }
*/



    override fun onResume() {
        isStoptoMusic = false
        if (binding.playerView!= null) {

            player.setPlayWhenReady(true);
            player.getPlaybackState()
            player.seekTo(cp.toLong())
            if (aboutToStartMusic) {
               // player.st()
                aboutToStartMusic = false
            }
        }
        super.onResume()
    }

    private var cp = 0

      override fun onPause() {
          if (binding.playerView != null) {
           //   binding.videoView.pause()
              player.setPlayWhenReady(false);
              player.getPlaybackState()
              cp = player.currentPosition.toInt()
              if (!aboutToStartMusic) {
                  pausePlayer()
              }
              if (!isStoptoMusic) {
                  playerPauseState()
              }

          }
          super.onPause()
      }


    /*
    * @Override
public void onRestoreInstanceState(Bundle savedInstanceState) {
      super.onRestoreInstanceState(savedInstanceState);
      position = savedInstanceState.getInt("Position");
      myVideoView.seekTo(position);
}
    *
    *
    * @Override
public void onSaveInstanceState(Bundle savedInstanceState) {
      super.onSaveInstanceState(savedInstanceState);
      savedInstanceState.putInt("Position", myVideoView.getCurrentPosition());
      myVideoView.pause();
}
    *
    * */

/*
    val foregroundListener = object : Foreground.Listener {
        override fun background() {
            if (binding.videoView != null) {
                stopPosition = binding.videoView.getCurrentPosition();
                binding.videoView.pause()
                mTime += mCurrentLapse;
                //   mCountUpTimer!!.cancel()
                countdownhandler.removeCallbacks(countdownRunnable)
                mCountDownTimer!!.cancel()
                Log.e("Foreground", "Go to background")
            }
        }

        override fun foreground() {

            if (binding.pauseWorkoutLayout.visibility == View.VISIBLE) {
                binding.videoView.visibility = View.VISIBLE
                binding.videoView.seekTo(stopPosition)

            } else if (binding.pauseWorkoutLayout1.visibility == View.VISIBLE) {
                binding.videoView.visibility = View.VISIBLE
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    var sp21 =  SoundPool.Builder()
                    sp21.setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                    )
                }

                if (binding.videoView != null && itemPosition != exerciseList.size) {
                    binding.videoView.seekTo(stopPosition)
                    //  binding.videoView.start()
                    //  countUpTimer(0)
                    //  startTimer()
                    */
/*  mStartTime = SystemClock.uptimeMillis();
                      countdownhandler.postDelayed(countdownRunnable, 0);
                      updateProgress(remainingTime)
                      binding.videoView.visibility = View.VISIBLE*//*

                    Log.e("Foreground", "Go to foreground")
                }

            }
        }
    }
*/



    private fun pausePlayer() {
        val orientation = getActivity().getResources().getConfiguration().orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            binding.pauseWorkoutLayout.visibility = View.VISIBLE
            binding.playControlerLayout.visibility = View.VISIBLE
        } else {
            binding.pauseWorkoutLayout1.visibility = View.VISIBLE
            binding.landscapeExerciseTimeLayout.visibility = View.GONE
            binding.playControlerLayout.visibility = View.GONE
        }

        if (binding.restTimeLayout.visibility == View.VISIBLE) {
            binding.restTimeLayout.visibility = View.GONE
            // isRestTimePlaying=false
        }
    }



    private fun playerPauseState() {
        if (itemPosition < exerciseList.size) {
            if (restTimeCountDownTimer != null)
                restTimeCountDownTimer!!.cancel()
            mTime += mCurrentLapse;
            countdownhandler.removeCallbacks(countdownRunnable!!);
            binding.countDownTime.visibility = View.GONE


           // binding.repeat.visibility = View.GONE
            requestAudioFocusForMyApp(this)
            stopPosition = player.getCurrentPosition().toInt()

                player.setPlayWhenReady(false);
            player.getPlaybackState()
            //   mCountUpTimer!!.cancel()
            // countdownhandler.removeCallbacks(countdownRunnable)
            isPause = true
            mCountDownTimer!!.cancel()

            binding.EName.text = exerciseList.get(itemPosition).stream_video_name
            binding.EName1.text = exerciseList.get(itemPosition).stream_video_name
        }
    }

}


