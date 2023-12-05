package com.doviesfitness.ui.bottom_tabbar.workout_tab.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AccelerateInterpolator
import android.view.animation.TranslateAnimation
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.databinding.ActivityWorkoutVideoPlayBinding
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.bottom_tabbar.home_tab.activity.LinearLayoutManagerWithSmoothScroller
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.WorkoutDetail
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.WorkoutExercise
import com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters.WorkoutVideoPlayAdapter
import com.doviesfitness.utils.CommanUtils
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.Constant.Companion.releaseAudioFocusForMyApp
import com.doviesfitness.utils.Constant.Companion.requestAudioFocusClose
import com.doviesfitness.utils.Constant.Companion.requestAudioFocusForMyApp
import net.khirr.library.foreground.Foreground
import java.util.*

class WorkoutVideoPlayActivityNew : BaseActivity(), View.OnClickListener,
    WorkoutVideoPlayAdapter.OnItemClick {

    private var orientationrunnable: Runnable?=null
    private var forCheckRestTimeViewStatus: Boolean = false
    private var isPreview: String = ""
    private var from_which_frament: String ?= ""
    private var program_plan_id: String? = ""
    lateinit var binding: ActivityWorkoutVideoPlayBinding
    lateinit var adapter: WorkoutVideoPlayAdapter
    lateinit var exerciseList: ArrayList<WorkoutExercise>
    private var countdownRunnable: Runnable? = null
    private val countdownhandler = Handler()
    private var orientationHandler = Handler()
    var itemPosition: Int = 0
    lateinit var mPlayer: MediaPlayer
    var timerTime = 0;
    var isPause = false
    var stopPosition = 0
    var remainingTime = 0
    var RestremainingTime: Long = 0
    var mCountDownTimer: CountDownTimer? = null
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
    lateinit var WDetail: WorkoutDetail
    var smoothScroller: androidx.recyclerview.widget.RecyclerView.SmoothScroller? = null
    var orientation: Int = -10
    var isRun = true
    var isRunVideoPreview = true
    var isSamsungCase = false
    var isPreviewVisible = false


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        var dfgdjf   = CommanUtils.getWidthAndHeight(this)
        hideNavStatusBar()
         super.onCreate(savedInstanceState)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_workout_video_play)


        orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        landscapeFunctionality()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        Glide.with(getActivity()).load(R.drawable.shadowfull_screen_ico)
            .into(binding.orientationIcon)
        binding.landscapeExerciseTimeLayout.visibility = View.VISIBLE
        binding.playControlerLayout.visibility = View.GONE
        if (binding.pauseWorkoutLayout.visibility == View.VISIBLE) {
            binding.pauseWorkoutLayout1.visibility = View.VISIBLE
            binding.pauseWorkoutLayout.visibility = View.GONE
        }
        hideNavStatusBar()


        orientationrunnable= Runnable {

            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR

        }
        orientationHandler=Handler();



        smoothScroller = object : androidx.recyclerview.widget.LinearSmoothScroller(this) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }

        }
        exerciseList = intent.getSerializableExtra("exerciseList") as ArrayList<WorkoutExercise>
        WDetail = intent.getSerializableExtra("WDetail") as WorkoutDetail
        if (intent.hasExtra("from_ProgramPlan")) {
            if (intent.getStringExtra("from_ProgramPlan") != null) {
                program_plan_id = intent.getStringExtra("from_ProgramPlan")
            }
        }

        // from which fragment you are coming
        if (intent.hasExtra("from_which_frament")) {
            if (intent.getStringExtra("from_which_frament") != null) {
                from_which_frament = intent.getStringExtra("from_which_frament")
            }
        }

        workout_id = WDetail.workout_id
        workout_name = WDetail.workout_name
        initialisation()
    }

    fun checkOrientation():Boolean{
        return Settings.System.getInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) == 1

    }

    fun hideNavStatusBar() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.P)
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
        binding.rlPreviewLayout.setOnClickListener(this)

        binding.videoView.setOnClickListener(this)

        binding.view.setOnClickListener(this)
        binding.shadowLayout.setOnClickListener(this)
        binding.view.setOnClickListener(this)
        binding.view1.setOnClickListener(this)
        binding.landscapeExerciseTimeLayout.setOnClickListener(this)

        binding.videoView.isDrawingCacheEnabled = true
        binding.previewVideoView.isDrawingCacheEnabled = true

        //PreviewExercise(itemPosition)
        binding.txtNext.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen._25sdp))

        //ring when workout will start
        audioPlayer = MediaPlayer.create(this, R.raw.boxingbell)

        adapter = WorkoutVideoPlayAdapter(getActivity(), exerciseList, this)
        layoutManager = LinearLayoutManagerWithSmoothScroller(getActivity())
        binding.videoRv.layoutManager = layoutManager
        binding.videoRv.adapter = adapter


        //manage preview case according to status hide and show
        val previewStatus =
            getDataManager().getPreferanceStatus(AppPreferencesHelper.PREF_KEY_APP_PREVIEW_STATUS)
        if (previewStatus != null && !previewStatus.isEmpty()) {
            if (previewStatus.equals("Off")) {
                isPreview = "Off"
            } else {
                isPreview = "On"
            }
        }

        /*....................................Video player code...........................................*/
        binding.videoView.setOnPreparedListener { mp ->
            mPlayer = mp!!
            mPlayer.isLooping = true
            Log.d("video width", "video width...." + mp.videoWidth)
            Log.d("video height", "video height...." + mp.videoHeight)
        }
        binding.videoView.setOnCompletionListener { }

        /// under working of preview
        binding.previewVideoView.setOnPreparedListener { p0 ->
            // first show parent view
            Log.d("show preivew1", "show preivew1...")

            /*  if (isPreviewVisible){
                          binding.rlPreviewLayout.visibility=View.GONE
                        //  isPreviewVisible=false
                      }
                      else {*/
            if (isPreview == "On") {
                binding.rlPreviewLayout.visibility = View.VISIBLE
                binding.rlTxtNext.visibility = View.GONE
                binding.previewVideoView.visibility = View.VISIBLE
                binding.boundryFrame.setBackgroundResource(R.drawable.transparent);
                binding.previewVideoView.start()

                Handler().postDelayed({
                    // after delay show text and boundry becose of video loaded time can match
                    binding.txtNext.visibility = View.VISIBLE
                    binding.boundryFrame.setBackgroundResource(R.drawable.shadow);

                }, 300)
                mPlayer = p0!!
                mPlayer.isLooping = true
            } else {

            }
            //  }
        }

        binding.previewVideoView.setOnCompletionListener(object : MediaPlayer.OnCompletionListener {
            override fun onCompletion(mp: MediaPlayer?) {

                //isRunVideoPreview = true
                binding.rlPreviewLayout.visibility = View.GONE
                binding.txtNext.visibility = View.GONE
                binding.boundryFrame.visibility = View.GONE
            }
        })


        /*....................................Video player code...........................................*/
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

        // video player Start
        playExercise()

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

        animation = TranslateAnimation(0f, 0f, 300f, 0f);
        animation.interpolator = AccelerateInterpolator();
        animation.duration = 1000

        // binding.videoView.startAnimation(animation)
    }

    /*.........................landScape and navigation related work.......................*/
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideNavStatusBar()
            //   hideNavigationBar()
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
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

    /*.........................onClicon list manage using listener.................................*/
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
                    Glide.with(getActivity()).load(R.drawable.ic_gray_left_arrow).into(binding.previousIcon)
                else
                    Glide.with(getActivity()).load(R.drawable.ic_gray_left_arrow).into(binding.previousIcon)
                if (pos != exerciseList.size - 1)
                    Glide.with(getActivity()).load(R.drawable.ic_gray_right_arrow).into(binding.nextIcon)
                else
                    Glide.with(getActivity()).load(R.drawable.ic_gray_right_arrow).into(binding.nextIcon)
            }
            if (restTimeCountDownTimer != null)
                restTimeCountDownTimer!!.cancel()
            binding.restTimeLayout.visibility = View.GONE
            itemPosition = pos
            timerTime = 0
            mCountDownTimer!!.cancel()

            //for preview working case hide and stop video
            /*....................................................*/
            binding.previewVideoView.stopPlayback()
            binding.rlPreviewLayout.visibility = View.GONE
            /*....................................................*/
            playExercise()
        }
    }

    /*........................................on click ............................................*/
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
                playerPauseState()

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
                CommanUtils.lastClick()
                binding.shadowLayout.performClick()
            }
            R.id.view1 -> {
                CommanUtils.lastClick()
                binding.shadowLayout.performClick()
            }
            R.id.landscape_exercise_time_layout -> {
                binding.shadowLayout.performClick()
            }
            R.id.rl_preview_layout -> {
                //  CommanUtils.lastClick()
                binding.shadowLayout.performClick()
            }
            R.id.shadow_layout -> {
                // CommanUtils.lastClick()
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
                        WDetail
                    )
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
                        WDetail)
                        .putExtra("duration", duration)
                        .putExtra("from_ProgramPlan", program_plan_id)
                        .putExtra("from_which_frament", from_which_frament))
                finish()
            }
            R.id.previous_layout -> {
                if (itemPosition != 0 || itemPosition < 0) {
                    if (itemPosition < exerciseList.size) {
                        exerciseList.get(itemPosition).Progress = 0
                        exerciseList.get(itemPosition).isRestTime = false
                        adapter.notifyItemChanged(itemPosition)
                    }
                    Glide.with(getActivity()).load(R.drawable.ic_gray_right_arrow)
                        .into(binding.nextIcon)
                    itemPosition = itemPosition - 1
                    if (itemPosition != 0)
                        Glide.with(getActivity()).load(R.drawable.ic_gray_left_arrow).into(
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
                    playExercise()
                    // binding.flVideoView.startAnimation(animation)
                }
            }

            R.id.pause_layout -> {

                //shaow preview on pouse button
                //when RestTimeon full Screen
                if (forCheckRestTimeViewStatus) {
                    whenRestTimeCase(itemPosition)
                } else {
                    //when Video on Full screen Case then show preview
                    showPreviewAndRestTime(itemPosition)
                }
                pausePlayer()
                playerPauseState()
                isStoptoMusic = true
            }
            R.id.play_video -> {

                // pouse video when run again
                // isRunVideoPreview = true

                pousepPreviewVideo()
/*
                if (isPreviewVisible){
                    binding.rlPreviewLayout.visibility=View.VISIBLE
                    isPreviewVisible=false
                }
*/
                isStoptoMusic = false
                if (isRestTimePlaying == true) {
                    binding.restTimeLayout.visibility = View.VISIBLE
                    restTimeCountDown(RestremainingTime)
                    binding.repeat.visibility = View.GONE
                } else {
                    binding.videoView.seekTo(stopPosition)
                    binding.videoView.start()
                    //  countUpTimer(0)
                    // startTimer()
                    if (exerciseList.get(itemPosition).workout_exercise_type.equals("Repeat")) {
                        binding.repeat.visibility = View.VISIBLE
                    } else {
                        binding.repeat.visibility = View.GONE
                    }
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
                // pouse video when run again
                // isRunVideoPreview = true

                pousepPreviewVideo()
/*
                if (isPreviewVisible){
                    binding.rlPreviewLayout.visibility=View.VISIBLE
                    isPreviewVisible=false
                }
*/
                isStoptoMusic = false
                binding.landscapeExerciseTimeLayout.visibility = View.VISIBLE
                if (isRestTimePlaying == true) {
                    binding.restTimeLayout.visibility = View.VISIBLE
                    restTimeCountDown(RestremainingTime)
                    binding.repeat.visibility = View.GONE
                } else {
                    binding.videoView.seekTo(stopPosition)
                    binding.videoView.start()
                    //  countUpTimer(0)
                    // startTimer()
                    if (exerciseList.get(itemPosition).workout_exercise_type.equals("Repeat")) {
                        binding.repeat.visibility = View.VISIBLE
                    } else {
                        binding.repeat.visibility = View.GONE
                    }
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
                    Glide.with(getActivity()).load(R.drawable.ic_gray_left_arrow)
                        .into(binding.previousIcon)
                    itemPosition = itemPosition + 1
                    if (itemPosition != exerciseList.size - 1)
                        Glide.with(getActivity()).load(R.drawable.ic_gray_right_arrow).into(
                            binding.nextIcon
                        )
                    else
                        Glide.with(getActivity()).load(R.drawable.ic_gray_right_arrow).into(binding.nextIcon)
                    timerTime = 0
                    mCountDownTimer!!.cancel()

                    if (restTimeCountDownTimer != null)
                        restTimeCountDownTimer!!.cancel()
                    binding.restTimeLayout.visibility = View.GONE
                    playExercise()
                    if (!audioPlayer.isPlaying)
                        audioPlayer.start()
                    //   binding.flVideoView.startAnimation(animation)
                }
            }

            R.id.orientation_layout -> {
                val orientation = getActivity().getResources().getConfiguration().orientation
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                    Glide.with(getActivity()).load(R.drawable.shadowfull_screen_ico)
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
                //   if (checkOrientation())
                //   orientationHandler.postDelayed(orientationrunnable,6000)

            }
            R.id.orientation_layout1 -> {
                val orientation = getActivity().getResources().getConfiguration().orientation
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    Glide.with(getActivity()).load(R.drawable.shadowfull_screen_ico)
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
                //  if (checkOrientation())
                //   orientationHandler.postDelayed(orientationrunnable,6000)

            }
            R.id.landscape_orientation_layout -> {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                binding.landscapeExerciseTimeLayout.visibility = View.GONE
                binding.playControlerLayout.visibility = View.VISIBLE
                if (binding.pauseWorkoutLayout1.visibility == View.VISIBLE) {
                    binding.pauseWorkoutLayout.visibility = View.VISIBLE
                    binding.pauseWorkoutLayout1.visibility = View.GONE
                }
                // if (checkOrientation())
                // orientationHandler.postDelayed(orientationrunnable,6000)

            }
        }
    }

    private fun whenRestTimeCase(pos: Int) {
        if (isPreview.equals("On")) {
            if (pos < exerciseList.size - 1) {
                //dshtdsjgfsdjfgfdhjshafv

                Log.d("show preivew4","show preivew4...")

                if (exerciseList.get(pos + 1).workout_exercise_break_time.equals("00:00:00")) {

                    binding.rlPreviewLayout.visibility = View.VISIBLE
                    binding.rlTxtNext.visibility = View.GONE

                    binding.previewVideoView.visibility = View.VISIBLE

                    binding.boundryFrame.setBackgroundResource(R.drawable.transparent);

                    var previewUri = Uri.parse(exerciseList.get(pos + 1).workout_offline_video)
                    binding.previewVideoView.stopPlayback();
                    binding.previewVideoView.setVideoURI(previewUri)
                    binding.previewVideoView.start()
                }
            } else if (pos == exerciseList.size - 1) {
                binding.rlPreviewLayout.visibility = View.GONE
            }
        }
    }

    private fun pousepPreviewVideo() {
        if (isPreview.equals("On")) {
            if (forCheckRestTimeViewStatus) {

                binding.rlPreviewLayout.visibility = View.VISIBLE
                binding.rlTxtNext.visibility = View.GONE
                binding.previewVideoView.visibility = View.VISIBLE
                forCheckRestTimeViewStatus = false

            } else {
                binding.previewVideoView.stopPlayback()
                binding.rlPreviewLayout.visibility = View.GONE
                binding.rlTxtNext.visibility = View.GONE
                binding.previewVideoView.visibility = View.GONE
            }
        }
    }

    private fun showPreviewAndRestTime(pos: Int) {
        if (isPreview.equals("On")) {

            if (pos < exerciseList.size - 1) {

                if (exerciseList.get(pos).workout_exercise_break_time != "00:00:00") {
                    binding.rlPreviewLayout.visibility = View.VISIBLE
                    binding.rlTxtNext.visibility = View.VISIBLE
                    binding.txtNext.visibility = View.VISIBLE
                    binding.previewVideoView.visibility = View.GONE
                    binding.boundryFrame.setBackgroundResource(R.drawable.transparent)

                    try {
                        var timeInMinute = exerciseList.get(pos).workout_exercise_break_time.split(":")
                        binding.previewRestTimeTv.text = timeInMinute[1] + ":" + timeInMinute[2]
                    }catch (e:Exception){
                        e.printStackTrace()
                    }

                } else {
                    binding.rlPreviewLayout.visibility = View.VISIBLE
                    binding.rlTxtNext.visibility = View.GONE
                    binding.previewVideoView.visibility = View.VISIBLE

                    var previewUri = Uri.parse(exerciseList.get(pos + 1).workout_offline_video)
                    binding.previewVideoView.stopPlayback();
                    binding.previewVideoView.setVideoURI(previewUri)
                    binding.previewVideoView.start()
                }

            } else if (pos == exerciseList.size - 1) {
                binding.rlPreviewLayout.visibility = View.GONE
            }
        }
    }




    fun porttrailFunctionality(from: Int) {
        binding.countDownTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen._25sdp))

        //portraitPreviewVideoView()
        binding.txtNext.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen._25sdp))

        var params1 = RelativeLayout.LayoutParams(resources.getDimension(R.dimen._120sdp).toInt(), RelativeLayout.LayoutParams.WRAP_CONTENT)
        params1.setMargins(resources.getDimension(R.dimen._10sdp).toInt(), resources.getDimension(R.dimen._10sdp).toInt(), 0, 0)
        binding.countDownTime.setLayoutParams(params1);

        var param = LinearLayout.LayoutParams(resources.getDimension(R.dimen._120sdp).toInt(), RelativeLayout.LayoutParams.WRAP_CONTENT)
        param.setMargins(resources.getDimension(R.dimen._10sdp).toInt(), resources.getDimension(R.dimen._10sdp).toInt(), 0, 0);
        binding.landscapeExerciseTime.setLayoutParams(param);

        var params2 = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        params2.setMargins(0, resources.getDimension(R.dimen._10sdp).toInt(), resources.getDimension(R.dimen._10sdp).toInt(), 0);
        params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
        binding.repeat.setLayoutParams(params2);

        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)

        /*.................................video view size..............................................*/
        var params = binding.videoView.getLayoutParams() as RelativeLayout.LayoutParams
        params.width = metrics.widthPixels

        if (from == 0) {
            params.height = resources.getDimension(R.dimen._185sdp).toInt()
        } else {
            params.height = mHeight
        }

        params.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        params.removeRule(RelativeLayout.ALIGN_PARENT_TOP)
        params.removeRule(RelativeLayout.CENTER_IN_PARENT)
        params.removeRule(RelativeLayout.CENTER_HORIZONTAL)
        params.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT)
        params.removeRule(RelativeLayout.ALIGN_PARENT_LEFT)
        params.setMargins(0, 0, 0, 0);
        binding.videoView.setLayoutParams(params)
        binding.restTimeLayout.setLayoutParams(params)
        //binding.shadowLayout.setLayoutParams(params)

        params = binding.videoView.getLayoutParams() as RelativeLayout.LayoutParams


        params.width = metrics.widthPixels
        if (from == 0) {
            params.height = resources.getDimension(R.dimen._190sdp).toInt()
        } else {
            params.height = mHeight
        }
        binding.rlPreviewLayout.setLayoutParams(params)

        /**
         * This is for preview layout
         * start
         * */
        var pvParams = LinearLayout.LayoutParams(resources.getDimension(R.dimen._130sdp).toInt(), RelativeLayout.LayoutParams.WRAP_CONTENT)
        Handler().postDelayed({
            binding.previewVideoView.setLayoutParams(pvParams)
        }, 300) // 3 mili seconds delay task executio

        pvParams = LinearLayout.LayoutParams(resources.getDimension(R.dimen._120sdp).toInt(), resources.getDimension(R.dimen._65sdp).toInt())
        binding.rlTxtNext.setLayoutParams(pvParams);
        binding.landscapeExerciseTime.setLayoutParams(pvParams);
        // preview layout end

        val params3 = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        params3.setMargins(0, 0, 0, 0);
        params3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        params3.height = resources.getDimension(R.dimen._40sdp).toInt()
        binding.playControlerLayout.setLayoutParams(params3)

        Glide.with(getActivity()).load(R.drawable.newic_crop_ico).into(binding.orientationIcon)

        binding.restTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen._22sdp))
        binding.restTimeTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen._28sdp))

        binding.playControlerLayout.visibility = View.VISIBLE
        binding.playControlerLayout.setBackgroundResource(R.color.splash_screen_color)
        Glide.with(getActivity()).load(R.drawable.newic_crop_ico).into(binding.orientationIcon)
        binding.landscapeExerciseTimeLayout.visibility = View.GONE
        binding.playControlerLayout.visibility = View.VISIBLE
        if (binding.pauseWorkoutLayout1.visibility == View.VISIBLE) {
            binding.pauseWorkoutLayout.visibility = View.VISIBLE
            binding.pauseWorkoutLayout1.visibility = View.GONE
        }
    }

    fun playExercise() {
        isRun = true
        /*....................................................*/
        binding.previewVideoView.stopPlayback()
        binding.previewVideoView.visibility = View.GONE
        binding.rlPreviewLayout.visibility = View.GONE
        /*....................................................*/

        binding.restTimeLayout.visibility = View.GONE
        //binding.landscapeExerciseName.text = exerciseList.get(itemPosition).workout_exercise_name

        if (itemPosition < exerciseList.size) {
            if (!audioPlayer.isPlaying)
                audioPlayer.start()
            var uri = Uri.parse(exerciseList.get(itemPosition).workout_offline_video)
            //   var uri = Uri.parse("https://dovies-fitness-dev.s3.us-east-2.amazonaws.com/admin_image/2.77323047383155.m3u8")
            binding.videoView.stopPlayback();
            binding.videoView.setVideoURI(uri)
            binding.videoView.start()

            //condition for hide text
            if (exerciseList.get(itemPosition).workout_exercise_type.equals("Repeat")) {
                binding.repeat.visibility = View.VISIBLE
                var str=exerciseList.get(itemPosition).workout_repeat_text
                if (str.equals("01")||str.equals("1"))
                    binding.repeat.text = exerciseList.get(itemPosition).workout_repeat_text + " " + "Rep"
                else
                    binding.repeat.text = exerciseList.get(itemPosition).workout_repeat_text + " " + "Reps"
            } else {
                binding.repeat.visibility = View.GONE
            }
        }

        isRestTimePlaying = false
        //binding.videoRv.smoothScrollBy(0,300)
        /*smoothScroller?.targetPosition = itemPosition
        layoutManager.startSmoothScroll(smoothScroller)*/

        layoutManager.smoothScrollToPosition(
            binding.videoRv,
            androidx.recyclerview.widget.RecyclerView.State(),
            itemPosition
        )
        //  binding.videoRv.scrollBy(0,500)

        updateProgress(0)
    }

    /*............................start musing in with video.......................................*/
    private fun aboutToStartMusic() {
        try {
            aboutToStartMusic = true
            val intent = Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER)
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateProgress(remainingTime1: Int) {
        try {
            isRunVideoPreview=true
            val endTime = ((exerciseList.get(itemPosition).MaxProgress * 1000).toLong() - remainingTime1)
            mCountDownTimer = object : CountDownTimer(endTime, 10) {

                override fun onTick(millisUntilFinished: Long) {

                    remainingTime = (endTime - millisUntilFinished).toInt() + remainingTime1
                    exerciseList.get(itemPosition).isComplete = millisUntilFinished.toInt() < 30
                    exerciseList.get(itemPosition).Progress =
                        ((endTime - millisUntilFinished).toInt() + remainingTime1)
                    val mProgress =
                        exerciseList.get(itemPosition).MaxProgress - ((endTime - millisUntilFinished).toInt() + remainingTime1) / 1000

                    forCheckRestTimeViewStatus = false
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
                            if (mProgress <= 10) {

                                forCheckRestTimeViewStatus = false
                                if(mProgress < 10){
                                    binding.landscapeExerciseTime.text = "0:0" + (mProgress).toString()
                                }else{
                                    binding.landscapeExerciseTime.text = "0:" + (mProgress).toString()
                                }

                                Log.v("FirstStep1",""+isRunVideoPreview)


                            }
                            else
                                binding.landscapeExerciseTime.text = "0:" + (mProgress).toString()
                            if (mProgress <= 11&&isRunVideoPreview) {
                                forCheckRestTimeViewStatus = false
                                isRunVideoPreview=false
                                PreviewExercise(itemPosition)
                            }

                        }
                    }
                    adapter.notifyItemChanged(itemPosition)
                }

                override fun onFinish() {
                    //for video player
                    if (exerciseList.get(itemPosition).workout_exercise_break_time.equals("00:00:00")) {

                        exerciseList.get(itemPosition).isComplete = true
                        exerciseList.get(itemPosition).Progress = 0
                        exerciseList.get(itemPosition).isRestTime = false
                        adapter.notifyItemChanged(itemPosition)
                        itemPosition += 1;
                        if (itemPosition < exerciseList.size) {
                            Glide.with(getActivity()).load(R.drawable.ic_gray_left_arrow)
                                .into(binding.previousIcon)
                            if (itemPosition != exerciseList.size - 1)
                                Glide.with(getActivity()).load(R.drawable.ic_gray_right_arrow).into(
                                    binding.nextIcon
                                )
                            else
                                Glide.with(getActivity()).load(R.drawable.ic_gray_right_arrow).into(
                                    binding.nextIcon
                                )

                            mCountDownTimer!!.cancel()

                            //for preview working case hide and stop video
                            /*....................................................*/
                            binding.previewVideoView.stopPlayback()
                            binding.previewVideoView.visibility = View.GONE
                            binding.rlPreviewLayout.visibility = View.GONE
                            /*....................................................*/

                            playExercise()
                            if (!audioPlayer.isPlaying)
                                audioPlayer.start()
                            //   binding.flVideoView.startAnimation(animation)
                        } else {
                            binding.videoView.stopPlayback()
                            if (!audioPlayer.isPlaying)
                                audioPlayer.start()
                            startActivity(
                                Intent(
                                    getActivity(),
                                    WorkoutCompleteActivity::class.java
                                ).putExtra("WDetail", WDetail)
                                    .putExtra("duration", duration)
                                    .putExtra("from_ProgramPlan", program_plan_id)
                                    .putExtra("from_which_frament", from_which_frament)
                            )
                            mCountDownTimer!!.cancel()
                            finish()
                        }

                    } else {
                        //for timer counter reset
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
                    }
                }
            }
            mCountDownTimer?.start()
        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    //preview video
    fun PreviewExercise(pos: Int) {
        try {
            if (isPreview.equals("On")) {
                Log.d("show preivew2","show preivew2...")

                if (pos < exerciseList.size - 1) {
                    if (exerciseList.get(pos).workout_exercise_break_time.equals("00:00:00")) {
                        binding.rlPreviewLayout.visibility = View.VISIBLE
                        binding.rlTxtNext.visibility = View.GONE
                        binding.txtNext.visibility = View.GONE
                        binding.boundryFrame.setBackgroundResource(R.drawable.transparent)
                        binding.previewVideoView.visibility = View.VISIBLE
                        var previewUri = Uri.parse(exerciseList.get(pos + 1).workout_offline_video)
                        binding.previewVideoView.stopPlayback();
                        binding.previewVideoView.setVideoURI(previewUri)
                        binding.previewVideoView.start()

                    } else if (!exerciseList.get(pos).workout_exercise_break_time.equals("00:00:00")) {
                        binding.rlPreviewLayout.visibility = View.VISIBLE
                        binding.rlTxtNext.visibility = View.VISIBLE
                        binding.txtNext.visibility = View.VISIBLE
                        binding.previewVideoView.visibility = View.GONE

                        var timeInMinute = exerciseList.get(pos).workout_exercise_break_time.split(":")
                        binding.previewRestTimeTv.text = timeInMinute[1] + ":" + timeInMinute[2]
                    }
                } else if (pos == exerciseList.size - 1) {
                    binding.rlPreviewLayout.visibility = View.GONE
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    //for rest time counter code
    fun restTimeCountDown(endTime: Long) {
        restTimeCountDownTimer = object : CountDownTimer(endTime, 10) {
            override fun onTick(millisUntilFinished: Long) {
                binding.landscapeExerciseTimeLayout.visibility = View.GONE
                RestremainingTime = millisUntilFinished
                isRestTimePlaying = true
                // binding.rlPreviewLayout.visibility = View.GONE
                binding.restTimeLayout.visibility = View.VISIBLE
                binding.repeat.visibility = View.GONE
                val mProgress = millisUntilFinished / 1000
                //  Log.d("show preivew mProgress","show preivew mProgress..."+mProgress)

                if (mProgress > 60) {
                    binding.restTimeTv.text =
                        (mProgress / 60).toString() + ":" + (mProgress % 60).toString()
                    forCheckRestTimeViewStatus = true
                    //  binding.rlPreviewLayout.visibility = View.GONE
                    if (isRun) {
                        isRun = false
                        forRestVideoPreview()
                    }

                }
                else {
                    if (mProgress < 10) {
                        binding.restTimeTv.text = "00:0" + (mProgress).toString()
                        forCheckRestTimeViewStatus = true
                        if (isRun) {
                            isRun = false
                            forRestVideoPreview()
                        }
                    }
                    else {
                        binding.restTimeTv.text = "00:" + (mProgress).toString()
                        forCheckRestTimeViewStatus = true
                        // binding.rlPreviewLayout.visibility = View.GONE
                    }
                    if (isRun) {
                        isRun = false
                        forRestVideoPreview()
                    }
                }
            }

            override fun onFinish() {
                isRestTimePlaying = false
                // isRun=true
                binding.restTimeLayout.visibility = View.GONE
                /*.................work here..............*/

                //  binding.rlPreviewLayout.visibility = View.VISIBLE
                exerciseList.get(itemPosition).isComplete = true
                exerciseList.get(itemPosition).Progress = 0
                exerciseList.get(itemPosition).isRestTime = false
                adapter.notifyItemChanged(itemPosition)
                itemPosition += 1;
                if (itemPosition < exerciseList.size) {
                    Glide.with(getActivity()).load(R.drawable.ic_gray_left_arrow)
                        .into(binding.previousIcon)
                    if (itemPosition != exerciseList.size - 1)
                        Glide.with(getActivity()).load(R.drawable.ic_gray_right_arrow).into(
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
                    startActivity(
                        Intent(
                            getActivity(), WorkoutCompleteActivity::class.java
                        ).putExtra("WDetail", WDetail)
                            .putExtra("duration", duration)
                            .putExtra("from_ProgramPlan", program_plan_id)
                            .putExtra("from_which_frament", from_which_frament)
                    )
                    mCountDownTimer!!.cancel()
                    finish()
                }
                restTimeCountDownTimer!!.cancel()
            }
        }
        restTimeCountDownTimer?.start()
    }

    private fun forRestVideoPreview() {
        Log.d("show preivew","show preivew...")
        if (isPreview.equals("On")) {
            /* binding.previewVideoView.stopPlayback()
             binding.rlPreviewLayout.visibility = View.VISIBLE
             binding.rlTxtNext.visibility = View.GONE
             binding.previewVideoView.visibility = View.VISIBLE

             val previewUri = Uri.parse(exerciseList.get(itemPosition + 1).workout_offline_video)
             binding.previewVideoView.stopPlayback();
             binding.previewVideoView.setVideoURI(previewUri)
             binding.previewVideoView.start()*/
            ///
            binding.rlPreviewLayout.visibility = View.VISIBLE
            binding.rlTxtNext.visibility = View.GONE
            binding.txtNext.visibility = View.GONE
            binding.boundryFrame.setBackgroundResource(R.drawable.transparent)
            binding.previewVideoView.visibility = View.VISIBLE
            var previewUri = Uri.parse(exerciseList.get(itemPosition + 1).workout_offline_video)
            binding.previewVideoView.stopPlayback();
            binding.previewVideoView.setVideoURI(previewUri)
            binding.previewVideoView.start()
        }else{
            binding.txtNext.visibility = View.GONE
            binding.rlPreviewLayout.visibility = View.GONE
            binding.rlTxtNext.visibility = View.GONE
            binding.previewVideoView.visibility = View.GONE
        }
    }

    private var cp = 0
    override fun onPause() {

        orientation = getActivity().getResources().getConfiguration().orientation

        if (forCheckRestTimeViewStatus) {
            whenRestTimeCase(itemPosition)
        } else {
            //when Video on Full screen Case then show preview
            showPreviewAndRestTime(itemPosition)
        }

        if (binding.videoView != null) {
            binding.videoView.pause()
            cp = binding.videoView.currentPosition
            if (!aboutToStartMusic) {
                pausePlayer()
            }
            if (!isStoptoMusic) {
                playerPauseState()
            }

        }
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)

        @RequiresApi(Build.VERSION_CODES.P)
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            // porttrailFunctionality(1)
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
            // landscapeFunctionality()
        }

        super.onPause()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        requestAudioFocusForMyApp(this)
        if (mCountDownTimer != null)
            mCountDownTimer!!.cancel()
        if (restTimeCountDownTimer != null)
            restTimeCountDownTimer!!.cancel()
        // Foreground.removeListener(foregroundListener)
    }

    override fun onResume() {
        Log.d("show preivew resume","show preivew1...")

/*
        if (binding.rlPreviewLayout.visibility ==View.VISIBLE){
            binding.rlPreviewLayout.visibility=View.GONE
            isPreviewVisible=true
        }
*/


        isStoptoMusic = false
        if (binding.videoView != null) {
            binding.videoView.seekTo(cp)
            if (aboutToStartMusic) {
                binding.videoView.start()
                aboutToStartMusic = false
            }
        }

        if (binding.previewVideoView != null) {
            binding.previewVideoView.start()
        }

        //  if (checkOrientation())
        // orientationHandler.postDelayed(orientationrunnable,6000)

        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    val foregroundListener = object : Foreground.Listener {
        override fun background() {
            if (binding.videoView != null) {
                stopPosition = binding.videoView.getCurrentPosition();
                binding.videoView.pause()
                mTime += mCurrentLapse;
                //   mCountUpTimer!!.cancel()
                countdownhandler.removeCallbacks(countdownRunnable!!)
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
                    var sp21 = SoundPool.Builder()
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
                    /*  mStartTime = SystemClock.uptimeMillis();
                      countdownhandler.postDelayed(countdownRunnable, 0);
                      updateProgress(remainingTime)
                      binding.videoView.visibility = View.VISIBLE*/
                    Log.e("Foreground", "Go to foreground")
                }
            }
        }
    }

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
        }
    }

    /*......................................playerPauseState.....................................*/
    private fun playerPauseState() {
        if (itemPosition < exerciseList.size) {
            if (restTimeCountDownTimer != null)
                restTimeCountDownTimer!!.cancel()
            mTime += mCurrentLapse;
            countdownhandler.removeCallbacks(countdownRunnable!!);
            binding.countDownTime.visibility = View.GONE

            binding.repeat.visibility = View.GONE
            requestAudioFocusForMyApp(this)
            stopPosition = binding.videoView.getCurrentPosition();
            binding.videoView.pause()
            //   mCountUpTimer!!.cancel()
            // countdownhandler.removeCallbacks(countdownRunnable)
            isPause = true
            mCountDownTimer!!.cancel()

            binding.EName.text = exerciseList.get(itemPosition).workout_exercise_name
            binding.EName1.text = exerciseList.get(itemPosition).workout_exercise_name
        }
    }

    /*....................................//landscape code.......................................*/
    @RequiresApi(Build.VERSION_CODES.P)
    fun landscapeFunctionality() {

        binding.countDownTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen._30sdp))
        //next text view
        binding.txtNext.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen._45sdp))
        binding.landscapeExerciseTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen._50sdp))

        //COUNTER TIME ADJUSTMENT
        val params1 = RelativeLayout.LayoutParams(resources.getDimension(R.dimen._120sdp).toInt(), RelativeLayout.LayoutParams.WRAP_CONTENT)
        params1.setMargins(resources.getDimension(R.dimen._55sdp).toInt(), resources.getDimension(R.dimen._5sdp).toInt(), 0, 0)
        binding.countDownTime.setLayoutParams(params1)

        //LANDSCAPE COUNTER TIME ADJUSTMENT
        var prms = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        prms.setMargins(resources.getDimension(R.dimen._50sdp).toInt(), 0, 0, 0)
        binding.landscapeExerciseTime.setLayoutParams(prms)

        //CENTER COUNTER TIME ADJUSTMENT
        var params2 = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        params2.setMargins(0, resources.getDimension(R.dimen._30sdp).toInt(), resources.getDimension(R.dimen._65sdp).toInt(), 0)
        params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
        binding.repeat.setLayoutParams(params2)


        // PLAYER BUTTON IN BOTTOM
        var params3 = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        params3.setMargins(0, 0, resources.getDimension(R.dimen._22sdp).toInt(), 0);
        params3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        params3.height = resources.getDimension(R.dimen._40sdp).toInt()
        binding.playControlerLayout.setLayoutParams(params3)

        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        var params = binding.videoView.getLayoutParams() as RelativeLayout.LayoutParams

        if (metrics.widthPixels == 3040 || metrics.heightPixels == 1440 || metrics.widthPixels > 3040 || metrics.heightPixels > 1440) {
            var percent = (10 * metrics.widthPixels) / 100
            params.width = (metrics.widthPixels) - percent
            params.height = metrics.heightPixels
            params.setMargins(resources.getDimension(R.dimen._25sdp).toInt(), 0, 0, 0);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP)
            binding.videoView.setLayoutParams(params)
            binding.restTimeLayout.setLayoutParams(params)
            binding.rlPreviewLayout.setLayoutParams(params)

        }else if (metrics.heightPixels == 720 && metrics.widthPixels <= 1370){
            isSamsungCase = true
            params.width = metrics.widthPixels
            params.height = metrics.heightPixels
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP)
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            binding.videoView.setLayoutParams(params)
            binding.restTimeLayout.setLayoutParams(params)
            binding.rlPreviewLayout.setLayoutParams(params)
        } else if (metrics.heightPixels == 720 && metrics.widthPixels >= 1280) {
            var VWidth = (1280 + metrics.heightPixels - 720)
            var VParams = RelativeLayout.LayoutParams(VWidth, metrics.heightPixels)
            VParams.addRule(RelativeLayout.CENTER_IN_PARENT)
            binding.videoView.setLayoutParams(VParams)
            binding.restTimeLayout.setLayoutParams(VParams)
            binding.rlPreviewLayout.setLayoutParams(params)
        } else if (metrics.heightPixels <= 720 && metrics.widthPixels <= 1280) {
            params.width = metrics.widthPixels
            params.height = metrics.heightPixels
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP)
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            binding.videoView.setLayoutParams(params)
            binding.restTimeLayout.setLayoutParams(params)
            binding.rlPreviewLayout.setLayoutParams(params)
        } else if (metrics.heightPixels == 1080 && metrics.widthPixels >= 1280) {
            params1.setMargins(resources.getDimension(R.dimen._55sdp).toInt(), resources.getDimension(R.dimen._30sdp).toInt(), 0, 0);
            binding.countDownTime.setLayoutParams(params1)

            prms = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            prms.setMargins(resources.getDimension(R.dimen._50sdp).toInt(), 0, 0, 0)
            prms.gravity = Gravity.CENTER_VERTICAL
            binding.landscapeExerciseTime.setLayoutParams(prms)

            params3.setMargins(resources.getDimension(R.dimen._10sdp).toInt(), 0, resources.getDimension(R.dimen._5sdp).toInt(), 0);
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
            binding.videoView.setLayoutParams(params)
            binding.restTimeLayout.setLayoutParams(params)
            binding.rlPreviewLayout.setLayoutParams(params)
        } else {
            var percent = (10 * metrics.widthPixels) / 100
            params.width = (metrics.widthPixels) - percent
            params.height = metrics.heightPixels
            params.setMargins(resources.getDimension(R.dimen._25sdp).toInt(), 0, 0, 0);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP)
            params.addRule(RelativeLayout.CENTER_HORIZONTAL)
            binding.videoView.setLayoutParams(params)
            binding.restTimeLayout.setLayoutParams(params)
            binding.rlPreviewLayout.setLayoutParams(params)
        }

        if(isSamsungCase){
            var pvParams = LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, resources.getDimension(R.dimen._140sdp).toInt())
            // becouse of preview video effect
            Handler().postDelayed({ binding.previewVideoView.setLayoutParams(pvParams) }, 300) // 3 seconds delay task execution

            pvParams = LinearLayout.LayoutParams(resources.getDimension(R.dimen._170sdp).toInt(), resources.getDimension(R.dimen._95sdp).toInt())
            binding.rlTxtNext.setLayoutParams(pvParams)
            pvParams = LinearLayout.LayoutParams(resources.getDimension(R.dimen._170sdp).toInt(), resources.getDimension(R.dimen._95sdp).toInt())
            binding.landscapeExerciseTime.setLayoutParams(pvParams)
            isSamsungCase = false
        }else{
            /**
             * This is for preview layout
             * start
             * */
            var pvParams = LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, resources.getDimension(R.dimen._140sdp).toInt())
            // becouse of preview video effect
            Handler().postDelayed({ binding.previewVideoView.setLayoutParams(pvParams) }, 300) // 3 seconds delay task execution

            pvParams = LinearLayout.LayoutParams(resources.getDimension(R.dimen._170sdp).toInt(), resources.getDimension(R.dimen._95sdp).toInt())
            binding.rlTxtNext.setLayoutParams(pvParams)

            pvParams = LinearLayout.LayoutParams(resources.getDimension(R.dimen._170sdp).toInt(), resources.getDimension(R.dimen._95sdp).toInt())
            binding.landscapeExerciseTime.setLayoutParams(pvParams)
        }

        // preview layout end
        if (binding.pauseWorkoutLayout.visibility == View.VISIBLE) {
            binding.pauseWorkoutLayout1.visibility = View.VISIBLE
            binding.pauseWorkoutLayout.visibility = View.GONE
        }
        binding.playControlerLayout.setBackgroundResource(android.R.color.transparent)
        Glide.with(getActivity()).load(R.drawable.shadowfull_screen_ico).into(binding.orientationIcon)
        binding.landscapeExerciseTimeLayout.visibility = View.VISIBLE
        binding.playControlerLayout.visibility = View.GONE
        Glide.with(getActivity()).load(R.drawable.shadowfull_screen_ico).into(binding.orientationIcon)
        Glide.with(getActivity()).load(R.drawable.music_ico).into(binding.ivMusic)
        binding.restTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen._40sdp))
        binding.restTimeTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen._55sdp))
    }
}


