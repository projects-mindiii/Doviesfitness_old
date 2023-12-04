package com.doviesfitness.ui.multipleQuality

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ContentResolver
import android.content.Context
import android.content.DialogInterface
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
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.mediarouter.media.MediaRouteSelector
import androidx.mediarouter.media.MediaRouter
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.allDialogs.ErrorDialog
import com.doviesfitness.chromecast.expandedcontrols.ExpandedControlsActivity
import com.doviesfitness.chromecast.utils.CustomChromeCastBottomSheetDialog
import com.doviesfitness.databinding.ActivityStreamVideoPlayLandscapeTempBinding
import com.doviesfitness.temp.DownloadVideosUtil
import com.doviesfitness.temp.modal.VideoCategoryModal
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.bottom_tabbar.stream_tab.activities.StreamCompleteActivity
import com.doviesfitness.ui.bottom_tabbar.stream_tab.activities.StreamDetailActivity
import com.doviesfitness.ui.bottom_tabbar.stream_tab.activities.VideoCache
import com.doviesfitness.ui.bottom_tabbar.stream_tab.adapters.StreamListAdapter
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.StreamWorkoutDetailModel
import com.doviesfitness.ui.bottom_tabbar.workout_tab.dialog.FinishActivityDialog
import com.doviesfitness.utils.CommanUtils
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.Constants
import com.doviesfitness.utils.StringConstant
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.drm.FrameworkMediaDrm
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSourceFactory
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.*
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.android.exoplayer2.util.Assertions
import com.google.android.exoplayer2.util.EventLogger
import com.google.android.exoplayer2.util.Util
import com.google.android.exoplayer2.video.VideoListener
import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.MediaLoadRequestData
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.SessionManagerListener
import com.google.android.gms.cast.framework.media.RemoteMediaClient
import com.google.android.gms.common.images.WebImage
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_video_detail.*
import kotlinx.android.synthetic.main.stream_exo_playback_control_view.view.*
import org.json.JSONObject
import java.util.*
import java.util.concurrent.TimeUnit


public class StreamVideoPlayUrlActivityTemp : BaseActivity(), View.OnClickListener,
    StreamListAdapter.OnVideoClick, PlaybackPreparer,
    CustomChromeCastBottomSheetDialog.ChromeCastListener, ErrorDialog.IsOK,
    FinishActivityDialog.IsYesClick, FinishActivityDialog.IsDelete {
    private var autoHandler: Handler? = null
    private var autoRunnable: Runnable? = null
    var timer1: CountDownTimer? = null
    var trackSelectionDialog: TrackSelectionDialogNew? = null
    private lateinit var audioManager: AudioManager
    var timer: CountDownTimer? = null
    lateinit var cResolver: ContentResolver
    private var mediaItems: List<MediaItem>? = null
    lateinit var binding: ActivityStreamVideoPlayLandscapeTempBinding
    lateinit var exerciseList: ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>
    private var player: SimpleExoPlayer? = null

    //  private var player: SimpleExoPlayer? = nulls
    var defaultbandwidhtmeter = DefaultBandwidthMeter()
    private lateinit var mediaDataSourceFactory: DataSource.Factory
    var downloadVideoList: ArrayList<VideoCategoryModal>? = ArrayList<VideoCategoryModal>()

    //changed
    private var videoTrackSelectionFactory: TrackSelection.Factory =
        AdaptiveTrackSelection.Factory(4000, 4000, 4000, 1f)


    //  private val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory()
    private val ivHideControllerButton: LinearLayout by lazy {
        findViewById<LinearLayout>(R.id.controller)
    }
    private var lastSeenTrackGroupArray: TrackGroupArray? = null
    private var trackSelector: DefaultTrackSelector? = null
    private var trackSelectorParameters: DefaultTrackSelector.Parameters? = null
    private var startAutoPlay = false
    private var startWindow = 0
    private var startPosition: Long = 0
    private val KEY_TRACK_SELECTOR_PARAMETERS = "track_selector_parameters"
    private val KEY_WINDOW = "window"
    private val KEY_POSITION = "position"
    private val KEY_AUTO_PLAY = "auto_play"
    var play_button_type: String = "next"

    companion object {
        var videoTackIndex = 6
        var videoAutoResolution = "0"
        var videoResolution = ""
        var stream_qty = ""
        var videoResolutionOnBandwith = ""
        var stream_qty_list: ArrayList<String>? = ArrayList<String>()
        var sInstance: RouteComparator? = null
        var width = 854
        var height = 480

    }

    private var dataSourceFactory: DataSource.Factory? = null
    private var currentWindow: Int = 0
    private var playbackPosition: Long = 0
    private var previusPos: Int = 0
    lateinit var adapter: StreamListAdapter
    var workout_id = ""
    var local = ""
    var streamImage = ""
    var from = ""
    var streamWorkoutName = ""
    var trailer = ""
    var historyId = ""
    var isFromResume = "yes"
    var isVideoDownloaded = 0
    var LayoutVisibility = false
    var isPause = false
    var ready = true
    var seekbarTuch = false
    var fromDownloaded = false

    var duration = ""
    var isHistorySaved = true
    var isVideoEnded = true

    private var isShowingTrackSelectionDialog = false
    var handler = Handler()

    var remainingRunnable = Runnable {
        updateProgress()
    }
    lateinit var mHandler: Handler
    var isFromExpandedView = false
    var flag: Boolean = false

    ////
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
    lateinit var dialog: ErrorDialog

    //////////
    fun setupCastListener(): Unit {
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
                    mSelectedMedia = castLoaderData.get(previusPos)
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
                ).putExtra("streamImage", streamImage)
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


    //////
    fun updateProgress() {

        if (player != null && player!!.duration > 0) {
            var millis = player!!.duration - player!!.currentPosition

            Log.d("time in mili", "time in mili..." + millis)
            var hrs = TimeUnit.MILLISECONDS.toHours(millis)
            var mins = TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(
                TimeUnit.MILLISECONDS.toHours(
                    millis
                )
            )
            var secs = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(
                    millis
                )
            )
            var hms = ""
            if (compareValues(hrs, 0) == 0) {

                hms = String.format("%02d:%02d", mins, secs)
            } else {
                hms = String.format("%02d:%02d:%02d", hrs, mins, secs)
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
                    runOnUiThread(Runnable {
                        binding.rlPlaypause.visibility = View.GONE
                    })


/*
                        if (videoTackIndex != 6)
                            autoHandler?.postDelayed(autoRunnable, 5000)
*/


                }
                Player.STATE_READY -> {
                    isVideoEnded = true
                    if (isHistorySaved) {
                        isHistorySaved = false
                        if (ready) {
                            player?.seekTo(0)
                        }
                        /*  if (from.isNotEmpty() && from.equals("history",true)){
                          }
                          else */
                        if (trailer != null && trailer.equals("yes")) {
                        } else {
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
                    }
                    autoRunnable?.let { autoHandler?.removeCallbacks(it) }
                    if (ready) {
                        player?.seekTo(0)
                    }
                    Handler().postDelayed(
                        {
                            runOnUiThread(Runnable {
                                binding.rlPlaypause.visibility = View.VISIBLE
                            })
                        }, 500
                    )

                }
                Player.STATE_ENDED -> {
                    /*   if (from.isNotEmpty() && from.equals("history",true)){
                           onBackPressed()
                       }
                       else*/ if (trailer != null && trailer.equals("yes")) {
                        onBackPressed()
                    } else {
                        isHistorySaved = true
                        Log.d("player state", "player state...STATE_ENDED")
                        if (isVideoEnded) {
                            isVideoEnded = false
                            Log.d(
                                "player state",
                                "player state...STATE_ENDED...save history...id.." + historyId
                            )


                            if (exerciseList.get(previusPos).is_workout.equals("Yes", true)) {
                                isFromResume = "no"
                                var streamImg = ""
                                if (from.isNotEmpty() && from.equals("history", true))
                                    streamImg = streamImage
                                else
                                    streamImg =
                                        exerciseList.get(previusPos).stream_video_image_url + "thumb/" + exerciseList.get(
                                            previusPos
                                        ).stream_video_image
                                getVideoTimeDuration()

                                if (binding.playerView != null) {
                                    releasePlayer()
                                }
                                Constant.requestAudioFocusForMyApp(getActivity())
                                Constant.requestAudioFocusClose(getActivity())

                                var isLast = "no"
                                if (previusPos < exerciseList.size - 1) {
                                    isLast = "no"
                                } else {
                                    isLast = "yes"
                                }
                                startActivityForResult(
                                    Intent(getActivity(), StreamCompleteActivity::class.java)
                                        .putExtra("workout_id", workout_id)
                                        .putExtra("duration", duration)
                                        .putExtra("isLast", isLast)
                                        .putExtra("historyId", historyId)
                                        .putExtra("from", "end")
                                        .putExtra(
                                            "video_id",
                                            exerciseList.get(previusPos).stream_video_id
                                        )
                                        .putExtra("streamImage", streamImage)
                                        .putExtra(
                                            "name",
                                            exerciseList.get(previusPos).stream_video_name
                                        ), 50
                                )
                            } else {
                                getVideoTimeDuration()
                                var isLast = false
                                if (previusPos == (exerciseList.size - 1)) {
                                    isLast = true
                                } else {
                                    isLast = false
                                }
                                createPlayedHistory(
                                    exerciseList.get(previusPos).stream_video_id,
                                    workout_id,
                                    historyId,
                                    isLast
                                )

                                playNextVideo()
                            }
                        }
                    }
                }
            }
        }

        override fun onTracksChanged(
            trackGroups: TrackGroupArray, trackSelections: TrackSelectionArray
        ) {
//updateButtonVisibility()
            Handler().postDelayed(Runnable {
                if (trackGroups !== lastSeenTrackGroupArray) {
                    try {
                        val mappedTrackInfo: MappingTrackSelector.MappedTrackInfo =
                            trackSelector!!.getCurrentMappedTrackInfo()!!
                        if (mappedTrackInfo != null) {
                            if (mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_VIDEO)
                                == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS
                            ) {

//streamPlayerActivity.showToast(R.string.error_unsupported_video)
                            }
                            if (mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_AUDIO)
                                == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS
                            ) {
// streamPlayerActivity.showToast(R.string.error_unsupported_audio)
                            }
                        }
                    } catch (ex: java.lang.Exception) {
                        ex.printStackTrace()
                    }
                    lastSeenTrackGroupArray = trackGroups
                }

            }, 2000)
        }
    }

    private fun callAutoclick() {
        Log.d("cnvmzzzzzzzzzz", "callAutoclick one: " + trackSelector)
        Log.d("cnvmzzzzzzzzzz", "onCreate: TWO $trackSelector")
        try {
            if (TrackSelectionDialogNew.willHaveContent(trackSelector!!)) {
                trackSelectionDialog =
                    TrackSelectionDialogNew.createForTrackSelector(this, trackSelector!!)
                    { dismissedDialog: DialogInterface? ->
                        Log.d("kllklklklkklk", "onCreate: THREE $trackSelector")
                        isShowingTrackSelectionDialog = false
                        binding.qualityContainer.visibility = View.GONE
                        Log.d("UrlPlayActivity", "callAutoclick: " + trackSelector)
                    }

                val newFragment =
                    trackSelectionDialog?.showFragment(this) as TrackSelectionDialogNew.TrackSelectionViewFragment
                //  binding.qualityContainer.visibility = View.VISIBLE
                supportFragmentManager.beginTransaction()
                    .replace(R.id.quality_container, newFragment).commit()
                Handler().postDelayed(Runnable {
                    Log.d("onVideoSizeChanged", "VIDEORESOL :RUN  " + videoAutoResolution)
                    newFragment.trackSelectionView.autoClick()
                }, 3000)
                //  newFragment.trackSelectionView.autoClick()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            Log.d("kllklklklkklk", "Exception: THREE ${ex.message}")
        }


    }

    fun playNextVideo() {
        if (previusPos < (exerciseList.size - 1)) {
// releasePlayer()
            // releasePlayerWithoutrestart()
            adapter.notifyItemChanged(previusPos)
            previusPos = previusPos + 1
            if (exerciseList.get(previusPos).seekTo >= (exerciseList.get(previusPos).MaxProgress * 1000L)) {
                exerciseList.get(previusPos).seekTo = 0
                adapter.notifyItemChanged(previusPos)
            }

            if (exerciseList.get(previusPos).stream_video_name != null && !exerciseList.get(
                    previusPos
                ).stream_video_name.isEmpty()
            ) {
                var titlestr = exerciseList.get(previusPos).stream_video_name
                val cap: String =
                    titlestr.substring(0, 1).toUpperCase() + titlestr.substring(1)
                binding.title.text = cap
            }
            if (exerciseList.get(previusPos).downLoadUrl != null && exerciseList.get(
                    previusPos
                ).downLoadUrl.isNotEmpty()
            ) {
                fromDownloaded = true
                binding.ivSelectTrack.visibility = View.GONE
                dismissQwalityDialog()

                initializePlayer(
                    exerciseList.get(previusPos).downLoadUrl,
                    previusPos,
                    1,
                    0,
                    "play next"
                )
            } else {
                fromDownloaded = false
                dismissQwalityDialog()
                trackSelectionDialog?.onDismissListener?.onDismiss(trackSelectionDialog?.dialog1);

                initializePlayer(
                    exerciseList.get(previusPos).stream_video,
                    previusPos,
                    0,
                    0,
                    "play next"
                )

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
            binding.playerView.exo_play.performClick()
        } else onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("is pause", "is pause..." + isPause)
        if (requestCode == 50 && resultCode == Activity.RESULT_OK) {
            isFromExpandedView = false
            Glide.with(getActivity()).load(R.drawable.close).into(binding.ivBack)

            isFromResume = "yes"
            ready = true
            if (data != null && data.hasExtra("from")) {
                if ("create log".equals(data.getStringExtra("from")!!)) {
                    if (data.getStringExtra("isLast")!!.equals("no", true))
//                        binding.next.performClick()
                        NextPlay()
                    else if (previusPos < exerciseList.size - 1)
//                        binding.next.performClick()
                        NextPlay()
                    else
                        finish()
                } else if ("back".equals(data.getStringExtra("from")!!)) {

                } else {
                    finish()
                }
            } else if (data != null && data.hasExtra("createlog")) {
                finish()
            } else {
                setResult(Activity.RESULT_OK)
                finish()
            }
        } else if (requestCode == Constant.CHORME_EXPANDED_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null && data.hasExtra("expand")) {
                isFromExpandedView = true
                Glide.with(getActivity()).load(R.drawable.ic_back_arrow_ico).into(binding.ivBack)
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        hideNavStatusBar()
        super.onCreate(savedInstanceState)
        //getDeviseVolume()
        Log.d("awaj", "onCreate: StreamVideoPlayUrlActivityTemp")
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        dataSourceFactory = DemoUtil.getDataSourceFactory( /* context= */this)
        videoTackIndex = 6
        //  getWindow().addFlags(WindowManager.LayoutParams.TYPE_PRIVATE_PRESENTATION)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_stream_video_play_landscape_temp)


        //  ((SurfaceView())binding.playerView.getVideoSurfaceView()).setSecure(true);


        var surfaceView = binding.playerView.videoSurfaceView as SurfaceView
        surfaceView.setSecure(true)

        //  binding.mediaRouteMenuItem1.isActivated=true
        mCastContext = CastContext.getSharedInstance(this)
        mCastSession = mCastContext?.sessionManager?.currentCastSession
        setupCastListener()
        //   CastButtonFactory.setUpMediaRouteButton(applicationContext, binding.mediaRouteMenuItem1)
//---------------------------Hemant Handel Play behalf of viewtype handeling--------------------------------


        try {
            downloadVideoList!!.clear()
            downloadVideoList =
                DownloadVideosUtil.getDownloadedVideo(Constants.DOWNLOADED_VIDEO)
            Log.e("StreamLogHisstory-> ", "Workout History" + downloadVideoList!!.size)
        } catch (e: Exception) {
            e.message
        }
        //------------------DownLoad case handel hemant-----
        mCastContext?.sessionManager?.addSessionManagerListener(
            mSessionManagerListener, CastSession::class.java
        )
        if (intent.hasExtra("position"))
            previusPos = intent.getIntExtra("position", 0)

        if (intent.hasExtra("castMedia")) {
            castLoaderData = intent.getSerializableExtra("castMedia") as ArrayList<MediaInfo>
            if (castLoaderData.size > 0) {
                mSelectedMedia = castLoaderData.get(0)
            }
        }


        landscapeFunctionality()
        exerciseList =
            intent.getSerializableExtra("videoList") as ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>

        Log.d("fanfknafka", "onCreate: ex ${Gson().toJson(exerciseList)}")
        workout_id = intent.getStringExtra("workout_id")!!
        local = intent.getStringExtra("local")!!
        trailer = intent.getStringExtra("trailer")!!
        Log.e("exerciseList-->", "" + exerciseList)
        if (intent.hasExtra("name"))
            streamWorkoutName = intent.getStringExtra("name")!!
        if (intent.hasExtra("history_id")!!)
            historyId = intent.getStringExtra("history_id")!!
        if (intent.hasExtra("stream_image")!!)
            streamImage = intent.getStringExtra("stream_image")!!
        if (intent.hasExtra("media_name")!!) {
            var media_name = intent.getStringExtra("media_name")!!
            if (media_name != null && !media_name.isEmpty()) {
                binding.episodes.text = "" + media_name
                binding.previous.text = "Previous " + media_name
                binding.next.text = "Next " + media_name
            }
        } else if (intent.hasExtra("from")) {
            from = intent.getStringExtra("from")!!
        } else {
            try {
                binding.episodes.text =
                    "" + StreamDetailActivity.overViewTrailerData!!.media_title_name
                binding.previous.text =
                    "Previous " + StreamDetailActivity.overViewTrailerData!!.media_title_name
                binding.next.text =
                    "Next " + StreamDetailActivity.overViewTrailerData!!.media_title_name
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }

        }


        if (savedInstanceState != null) {
            trackSelectorParameters =
                savedInstanceState.getParcelable(KEY_TRACK_SELECTOR_PARAMETERS)
            startAutoPlay = savedInstanceState.getBoolean(KEY_AUTO_PLAY)
            startWindow = savedInstanceState.getInt(KEY_WINDOW)
            startPosition = savedInstanceState.getLong(KEY_POSITION)
        } else {
            val builder = DefaultTrackSelector.ParametersBuilder( /* context= */this)
            trackSelectorParameters = builder.build()
            clearStartPosition()
        }
        autoHandler = Handler()
        autoRunnable = Runnable {
            Log.d("player state", "player state auto...STATE_BUFFERING")

            Log.d("kllklklklkklk", "onCreate: ONE $videoTackIndex")
            if (videoTackIndex != 6) {
                TrackSelectionDialogNew.isVisible = false
                callAutoclick()
            }

        }


        initialisation()
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
                    binding.seekbarValue.visibility = View.GONE

                    binding.title.visibility = View.GONE
                    binding.episodeLayout.visibility = View.GONE
                    binding.transparentLayout.visibility = View.GONE
                    binding.ivPlay.visibility = View.GONE
                    binding.ivSelectTrack.visibility = View.GONE
                    binding.ivPause.visibility = View.GONE

                    binding.ivBack.visibility = View.GONE
                    binding.mediaRouteMenuItem1.visibility = View.GONE
                    binding.chromeCast.visibility = View.GONE
                    binding.ivMusic.visibility = View.GONE

                    if (binding.videoRv.visibility == View.GONE) {
                        ivHideControllerButton.visibility = View.GONE
                    }
                }
                cancel()

            }
        }
    }

    private fun updateTrackSelectorParameters() {
        if (trackSelector != null) {
            trackSelectorParameters = trackSelector!!.getParameters()
        }
    }

    protected fun clearStartPosition() {
        startAutoPlay = true
        startWindow = C.INDEX_UNSET
        startPosition = C.TIME_UNSET
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        updateTrackSelectorParameters()
        updateStartPosition()
        outState.putParcelable(KEY_TRACK_SELECTOR_PARAMETERS, trackSelectorParameters)
        outState.putBoolean(KEY_AUTO_PLAY, startAutoPlay)
        outState.putInt(KEY_WINDOW, startWindow)
        outState.putLong(KEY_POSITION, startPosition)
    }


    fun vollumeControls() {
        try {

            audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        /*    audioManager.setStreamVolume(
                AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(
                    AudioManager.STREAM_MUSIC
                ), 0
            )*/

           /* println("awaj --  STREAM_MUSIC : ${AudioManager.STREAM_MUSIC}")
            println("awaj   getStreamMaxVolume: ${audioManager.getStreamMaxVolume(
                AudioManager.STREAM_MUSIC
            )}")*/


      //      val audioManager = this.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            val currentVolume: Int =getDeviseVolume() //audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
            val maxVolume: Int = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

            binding.seekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            binding.seekBar.setProgress(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            // var currentvolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
            val PVal = (currentVolume / 15.0)
            Log.d(
                "player max vollume", "player max vollume..." + (audioManager.getStreamMaxVolume(
                    AudioManager.STREAM_MUSIC
                ))
            )
            player?.volume = PVal.toFloat()//0.5f
           // player?.se = currentVolume.toFloat()//0.5f
            binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    Log.d("progresscurrent...", progress.toString())

                    seekBarAndVolumeSet(progress.toFloat())

                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    Log.d("seekbar vollume", "seekbar onStartTrackingTouc...")

                    seekbarTuch = true
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
                    binding.ivSelectTrack.visibility = View.GONE

                    binding.ivBack.visibility = View.GONE
                    binding.mediaRouteMenuItem1.visibility = View.GONE
                    binding.chromeCast.visibility = View.GONE
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

    fun seekBarAndVolumeSet(progress: Float) {
        var PVal = 1.0f
        if (progress > 14) {
            PVal = (progress / 15.0f)
            Log.d("player seekbar vollume", "player seekbar vollume...14-- " + PVal)
        } else
            if (progress > 13) {
                PVal = (progress / 17.0f)
                Log.d("player seekbar vollume", "player seekbar vollume...13-- " + PVal)
            } else
                if (progress > 12) {
                    PVal = (progress / 20.0f)
                    Log.d("player seekbar vollume", "player seekbar vollume...12-- " + PVal)
                } else
                    if (progress > 11) {
                        PVal = (progress / 24.0f)
                        Log.d("player seekbar vollume", "player seekbar vollume...11-- " + PVal)
                    } else
                        if (progress > 10) {
                            PVal = (progress / 29.0f)
                            Log.d(
                                "player seekbar vollume",
                                "player seekbar vollume...10-- " + PVal
                            )
                        } else
//----------------
                            if (progress > 9) {
                                PVal = (progress / 35.0f)
                                Log.d(
                                    "player seekbar vollume",
                                    "player seekbar vollume...9-- " + PVal
                                )
                            } else
                                if (progress > 8) {
                                    PVal = (progress / 42.0f)
                                    Log.d(
                                        "player seekbar vollume",
                                        "player seekbar vollume...8-- " + PVal
                                    )
                                } else
                                    if (progress > 7) {
                                        PVal = (progress / 50.0f)
                                        Log.d(
                                            "player seekbar vollume",
                                            "player seekbar vollume...7-- " + PVal
                                        )
                                    } else
                                        if (progress > 6) {
                                            PVal = (progress / 59.0f)
                                            Log.d(
                                                "player seekbar vollume",
                                                "player seekbar vollume...6-- " + PVal
                                            )
                                        } else
                                            if (progress > 5) {
                                                PVal = (progress / 69.0f)
                                                Log.d(
                                                    "player seekbar vollume",
                                                    "player seekbar vollume...5-- " + PVal
                                                )
                                            } else
                                                if (progress > 4) {
                                                    PVal = (progress / 80.0f)
                                                    Log.d(
                                                        "player seekbar vollume",
                                                        "player seekbar vollume...4-- " + PVal
                                                    )
                                                } else {
                                                    PVal = (progress / 92.0f)
                                                    Log.d(
                                                        "player seekbar vollume",
                                                        "player seekbar vollume...other--- " + PVal
                                                    )
                                                }
        if (progress == 15.0f)
            player?.volume = 1.0f
        else
            player?.volume = PVal.toFloat()
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
        binding.ivPrevious.setOnClickListener(this)
        binding.previous.setOnClickListener(this)

        binding.ivSelectTrack.setOnClickListener(this)
        binding.next.setOnClickListener(this)
        binding.ivNext.setOnClickListener(this)
        binding.episodes.setOnClickListener(this)
        binding.ivVlayout.setOnClickListener(this)

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
        binding.chromeCast.setOnClickListener(this)
        binding.ivCancleDialog.setOnClickListener(this)
        binding.disconnect.setOnClickListener(this)
        binding.disconnect1.setOnClickListener(this)
        binding.videoRv.layoutManager = LinearLayoutManager(getActivity())
        adapter = StreamListAdapter(getActivity(), exerciseList, this)
        binding.videoRv.adapter = adapter
        binding.playerView.controllerShowTimeoutMs = 30000
        binding.playerView.controllerHideOnTouch = false
        binding.playerView.exo_play.visibility = View.GONE
        binding.playerView.exo_pause.visibility = View.GONE
        mHandler = Handler()
        mHandler.post(remainingRunnable)
        stream_qty = exerciseList[previusPos].hls_video!!.stream_quality

        getStream_qty(stream_qty)


        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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
                                if (isVideoDownloaded == 0) {
                                    isPause = true
                                    binding.playerView.exo_pause.performClick()
                                    //  countdownhandler.removeCallbacks(countdownRunnable)

                                    //   binding.ivPlay.visibility = View.VISIBLE
                                    binding.ivPause.visibility = View.GONE
                                    binding.playerView.exo_play.visibility = View.GONE
                                    binding.playerView.exo_pause.visibility = View.GONE
                                    window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                                    Constant.requestAudioFocusForMyApp(getActivity())
                                    showInternateConnectionDialog1(getActivity())
                                }

                            } catch (ex: java.lang.Exception) {
                                ex.printStackTrace()
                            }
                        }

                    }
                })
            }
        }

        player?.addListener(eventListener)
        invalidateOptionsMenu()
        mAdapter = RouteAdapter(getActivity())
        mRouter = MediaRouter.getInstance(getActivity())
        mCallback = MediaRouterCallback()
        setRouteSelector(mCastContext!!.mergedSelector)

        binding.listView.setAdapter(mAdapter)
        binding.listView.setOnItemClickListener(mAdapter)

        if (mCastSession != null && mCastSession!!.isConnected) {
            if (castLoaderData.size > 0) {
                mSelectedMedia = castLoaderData.get(previusPos)
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

        //  isCastConnected=isConnected
    }

    // method to initialise player and play video
    fun initializePlayer(
        news_video: String,
        pos: Int,
        isDownload: Int,
        idForSeekTo: Int,
        from: String = ""
    ) {
        getStream_qty(stream_qty)
        player?.playWhenReady = false
        player?.stop()
        player?.release()
        player = null

        trackSelector = null

        isVideoDownloaded = isDownload
        val intent = intent
        mediaItems = createMediaItems(intent)


        /* for(i in 0..mediaItems!!.size){
             mediaItems?.let {
                 Log.d("popopopopoo", " === ${mediaItems!!.get(i).mediaMetadata.title}\n${it[i].playbackProperties!!.adTagUri}")
             }

         }*/
        if (mediaItems!!.isEmpty()) {
            return
        }
        val preferExtensionDecoders =
            intent.getBooleanExtra(IntentUtil.PREFER_EXTENSION_DECODERS_EXTRA, false)
        val renderersFactory = DemoUtil.buildRenderersFactory(this, preferExtensionDecoders)
        defaultbandwidhtmeter = DefaultBandwidthMeter.Builder(getActivity()).build()
        trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)



        Log.d("qwtrqtwqt", "initializePlayer:vR $videoAutoResolution")

        if (stream_qty_list!!.size == 5) {
            //video will play as per the master link in case of list size 5 else will start playing from minimum resolution as per in list
            trackSelector!!.setParameters(trackSelectorParameters!!)
        } else {

            /*this line handel to Video Qulity and the Run time in play Video first time we are set minimum and maximum video Qulity*/
            val pair: Pair<Int, Int> = getAutoVideoMinWidthHeight(videoAutoResolution)
            trackSelector!!.setParameters(
                trackSelector!!.buildUponParameters().setMinVideoSize(pair.first, pair.second)
                    .setMaxVideoSize(pair.first, pair.second)
            );
            Log.d("qwtrqtwqt", "setMinVideoSize:vR ${pair.first}+" + "${pair.second}")

        }

        Log.d(
            "jfhgfjhgf",
            " $trackSelector initializePlaySEer: ${trackSelector!!.parameters} == $videoTrackSelectionFactory \n $trackSelectorParameters"
        )

        //changed
        val httpDataSourceFactory = DefaultHttpDataSourceFactory(
            Util.getUserAgent(this, "mediaPlayerSample"),
            defaultbandwidhtmeter,
            DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
            DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
            true
        )
        var dataSourceFactory =
            DefaultDataSourceFactory(this, defaultbandwidhtmeter, httpDataSourceFactory)
        val mediaSourceFactory: MediaSourceFactory = DefaultMediaSourceFactory(dataSourceFactory!!)

        player = SimpleExoPlayer.Builder(this, renderersFactory)
            .setMediaSourceFactory(mediaSourceFactory)
            .setTrackSelector(trackSelector!!)
            .build()
        Log.d("mvncmvc", "initializePlayer: SELECTIOR $trackSelector")
        val haveStartPosition = startWindow != C.INDEX_UNSET
        if (haveStartPosition) {
            player!!.seekTo(startWindow, startPosition)
        }
        Log.d(
            "popopopopoo",
            "$news_video itializePlayer:$pos ONE $isDownload == $mediaItems == $from"
        )
        if (isDownload == 1) {
            with(player) {
                val dataSourceFactory1 =
                    CacheDataSourceFactory(VideoCache.getInstance(), dataSourceFactory)
                val mediaSource = ExtractorMediaSource.Factory(dataSourceFactory1)
                    .createMediaSource(Uri.parse(news_video))

                player!!.setMediaSource(mediaSource, haveStartPosition)
                this?.prepare()
                this?.playWhenReady = true
            }

        } else {
            with(player) {
                player!!.setMediaItem(
                    mediaItems!!.get(pos)!!,
                    !haveStartPosition
                )

                this?.prepare()
                this?.playWhenReady = true
            }
        }
        // ivHideControllerButton.visibility = View.VISIBLE
        //  playerView.showController()
        playerView.setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING)
        playerView.setShutterBackgroundColor(ContextCompat.getColor(this, R.color.colorBlack))
        playerView.player = player
        playerView.requestFocus()


        updateButtonVisibility()
        // playerView.player.seekTo(exerciseList.get(pos).seekTo)
        if (idForSeekTo == 0) {
            player?.seekTo(0)
        } else {
            player?.seekTo(exerciseList.get(pos).seekTo)
        }

        lastSeenTrackGroupArray = null
        // if (ready) {
        player?.removeListener(eventListener)
        player?.addListener(eventListener)
        ready = false
        Log.d("listener...", "listener added...")
        //  }VideoRendererEventListener
        player?.addVideoListener(object : VideoListener {
            override fun onVideoSizeChanged(
                width: Int,
                height: Int,
                unappliedRotationDegrees: Int,
                pixelWidthHeightRatio: Float
            ) {
                /*this height will tell you @ what resolution current video is playing*/
                videoResolutionOnBandwith = "$height"


                Log.d(
                    tag,
                    "qwtrqtwqt:...width.." + width + "...height..." + height + "...unappliedRotationDegrees...." + unappliedRotationDegrees
                            + "....pixelWidthHeightRatio..." + pixelWidthHeightRatio
                )

                /*Log.d("onVideoSizeChanged", "VIDEORESOL :RUN  " + videoAutoResolution)*/
//                if (videoTackIndex == 6) {
//                    videoAutoResolution = "" + 480
//                }
                Log.d(
                    "qwtrqtwqt",
                    "$videoAutoResolution nVideoSizeChanged: trackResolutionItem ${stream_qty_list.toString()}"
                )
                /*   videoAutoResolution = when {
                       getRequiredVideoResolution("360p") -> {
                           "360"
                       }
                       getRequiredVideoResolution("480p") -> {
                           "480"
                       }
                       else -> {
                           stream_qty_list!![stream_qty_list!!.size - 1].replace("p", "")
                       }
                   }*/


                /* if (stream_qty_list!!.size != 0) {
                        for (i in 0 until stream_qty_list!!.size) {
                            if (stream_qty_list!![i] == "480p") {
                                videoAutoResolution = "" + 480
                                Log.d("videoAutoResolution", "one: 480" + videoAutoResolution)
                                break
                            }
                            if (stream_qty_list!![i] == "360p") {
                                videoAutoResolution = "" + 360
                                Log.d("videoAutoResolution", " two 360: " + videoAutoResolution)
                                break
                            } else {
                                videoAutoResolution =
                                    "" + stream_qty_list!![stream_qty_list!!.size - 1].replace("p", "")
                                Log.d("videoAutoResolution", "else : " + videoAutoResolution)
                                break

                            }
                        }
                    }*/
                Log.d(
                    "onVideoSizeChanged",
                    "VIDEORESOL Changed: " + trackSelector!!.parameters.maxVideoHeight + " -- " + trackSelector!!.parameters.minVideoWidth
                )


            }
        })

        defaultbandwidhtmeter.addEventListener(handler, object : BandwidthMeter.EventListener {
            override fun onBandwidthSample(
                elapsedMs: Int,
                bytesTransferred: Long,
                bitrateEstimate: Long
            ) {
                Log.d(
                    tag,
                    "defaultbandwidhtmeter bitrateEstimate: " + defaultbandwidhtmeter.bitrateEstimate
                )
                Log.d(tag, "defaultbandwidhtmeter elapsedMs: $elapsedMs")
                Log.d(tag, "defaultbandwidhtmeter bytes transferred: $bytesTransferred")
                Log.d(
                    tag,
                    "defaultbandwidhtmeter Average bitrate (bps) = " + (bytesTransferred * 8).toDouble() / (elapsedMs / 1000)
                )
            }

        })
        player!!.addAnalyticsListener(EventLogger(trackSelector))
        // val audioManager = this.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val currentVolume: Int = getDeviseVolume() //audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        var progress = binding.seekBar.progress
        var PVal = (currentVolume / 15.0)
        player?.volume = PVal.toFloat()
        binding.playerView.exo_play.visibility = View.GONE
        binding.playerView.exo_pause.visibility = View.GONE

        if (isDownload == 0) {
            if (CommanUtils.isNetworkAvailable(getActivity())!!) {
            } else {
                isPause = true
                binding.playerView.exo_pause.performClick()
                // countdownhandler.removeCallbacks(countdownRunnable)
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

    private fun getRequiredVideoResolution(resolution: String): Boolean {
        stream_qty_list?.let {
            it.forEach { videoResolution ->
                if (videoResolution == resolution) {
                    return true
                }
            }
        }
        return false
    }

    @SuppressLint("StringFormatInvalid")
    private fun createMediaItems(intent: Intent): List<MediaItem> {
        val action = intent.action
        val actionIsListView = IntentUtil.ACTION_VIEW_LIST == action
        if (!actionIsListView && IntentUtil.ACTION_VIEW != action) {
            showToast(getString(R.string.unexpected_intent_action, action))
            finish()
            return emptyList()
        }


        val mediaItems = createMediaItems(intent, DemoUtil.getDownloadTracker(getActivity()))
        var hasAds = false
        for (i in mediaItems!!.indices) {
            val mediaItem = mediaItems[i]
            if (!Util.checkCleartextTrafficPermitted(mediaItem)) {
                showToast(getString(R.string.error_cleartext_not_permitted))
                return emptyList()
            }
            if (Util.maybeRequestReadExternalStoragePermission( /* activity= */
                    this,
                    mediaItem
                )
            ) {
                // The player will be reinitialized if the permission is granted.
                return emptyList()
            }
            val drmConfiguration =
                Assertions.checkNotNull(mediaItem.playbackProperties).drmConfiguration
            if (drmConfiguration != null) {
                if (Util.SDK_INT < 18) {
                    showToast(getString(R.string.error_drm_unsupported_before_api_18))
                    finish()
                    return emptyList()
                } else if (!FrameworkMediaDrm.isCryptoSchemeSupported(drmConfiguration.uuid)) {
                    showToast(getString(R.string.error_drm_unsupported_scheme))
                    finish()
                    return emptyList()
                }
            }
            hasAds = hasAds or (mediaItem.playbackProperties!!.adTagUri != null)
        }
/*
  if (!hasAds) {
   releaseAdsLoader()
  }
*/

        return mediaItems
    }


    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG)
            .show()
    }

    private fun createMediaItems(
        intent: Intent,
        downloadTracker: DownloadTracker
    ): List<MediaItem>? {
        val mediaItems: MutableList<MediaItem> =
            ArrayList()
        for (item in IntentUtil.createMediaItemsFromIntent(intent)) {
            Log.d("uiiyyiyi", "createMediaItems: ${item}")
            val downloadRequest =
                downloadTracker.getDownloadRequest(Assertions.checkNotNull(item.playbackProperties).uri)
            if (downloadRequest != null) {
                val builder = item.buildUpon()
                builder
                    .setMediaId(downloadRequest.id)
                    .setUri(downloadRequest.uri)
                    .setCustomCacheKey(downloadRequest.customCacheKey)
                    .setMimeType(downloadRequest.mimeType)
                    .setStreamKeys(downloadRequest.streamKeys)
                    .setDrmKeySetId(downloadRequest.keySetId)
                mediaItems.add(builder.build())
            } else {
                mediaItems.add(item)
            }
        }
        return mediaItems
    }

    private fun updateButtonVisibility() {
        binding.ivSelectTrack!!.isEnabled = true

    }

    fun showInternateConnectionDialog1(activity: Activity) {

        try {
            val dialog = Dialog(activity)

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )

            dialog.setCanceledOnTouchOutside(true)
            dialog.setContentView(R.layout.no_internate_connection_view)
            val tvOk = dialog.findViewById<TextView>(R.id.tv_ok)
            val ll_ok = dialog.findViewById<LinearLayout>(R.id.ll_ok)

            ll_ok.setOnClickListener { v ->
                dialog.dismiss()
                //  onBackPressed()
            }

            dialog.show()

        } catch (Ex: java.lang.Exception) {
            Ex.printStackTrace()
        }
    }

    // method to play video on list item click
    override fun onVideoClick(pos: Int) {
        Log.d("cnmvncmvnc", "onVideoClick: $pos")

        //-----------------------Hemant check Download View type--------
        if ((exerciseList.get(previusPos).view_type.equals("1")) && (exerciseList.get(pos).Progress == 100)) {
            Log.e(
                "View_type",
                "Download onVideoClick-->" + pos + " " + exerciseList.get(pos).view_type
            )
            videoListclickPlayVideo(pos)

        } else if (exerciseList.get(previusPos).view_type.equals("2") || exerciseList.get(pos).view_type.equals(
                "3"
            )
        ) {
            Log.e("View_type", "View onVideoClick-->" + pos + " " + exerciseList.get(pos).view_type)

            videoListclickPlayVideo(pos)

        } else {
            Log.e("View_type", "Both onVideoClick-->" + pos + " " + exerciseList.get(pos).view_type)
            dialog = ErrorDialog.newInstance("", "Ok", getString(R.string.error_download_type))
            dialog.setListener(this)
            dialog.show(supportFragmentManager)

        }

    }

    private fun videoListclickPlayVideo(pos: Int) {
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

            if (exerciseList.get(pos).stream_video_name != null && !exerciseList.get(pos).stream_video_name.isEmpty()) {
                var titlestr = exerciseList.get(pos).stream_video_name
                val cap: String =
                    titlestr.substring(0, 1).toUpperCase() + titlestr.substring(1)
                binding.title.text = cap
            }


            //   binding.title.text = exerciseList.get(pos).stream_video_name

            if (exerciseList.get(pos).downLoadUrl != null && !exerciseList.get(pos).downLoadUrl.isEmpty()) {
                fromDownloaded = true
                binding.ivSelectTrack.visibility = View.GONE
                dismissQwalityDialog()
                initializePlayer(exerciseList.get(pos).downLoadUrl, pos, 1, 0, "video click")
            } else {
                fromDownloaded = false
                dismissQwalityDialog()
                trackSelectionDialog?.onDismissListener?.onDismiss(trackSelectionDialog?.dialog1);

                initializePlayer(exerciseList.get(pos).stream_video, pos, 0, 0, "video click")
            }

            previusPos = pos
        } else {
            binding.playerView.exo_play.performClick()
            //   countdownhandler.postDelayed(countdownRunnable, 0);
            binding.playerView.exo_play.visibility = View.GONE
            binding.playerView.exo_pause.visibility = View.GONE
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
        if (mCastSession != null && mCastSession!!.isConnected) {
            Log.d("session connection", "session connection...connected")
            if (castLoaderData.size > 0) {
                mSelectedMedia = castLoaderData.get(previusPos)
            }
            if (null != mSelectedMedia) {
                loadRemoteMedia(0, true)

            }
        } else {
            Log.d("session connection", "session connection...not connected")
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
            updateTrackSelectorParameters()
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
        Log.d("on stop", "on stop...")
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

        if (v === binding.ivSelectTrack && !isShowingTrackSelectionDialog
            && TrackSelectionDialogNew.willHaveContent(trackSelector!!) //tripple dot icon click listener
        ) {

            isShowingTrackSelectionDialog = true
            trackSelectionDialog = TrackSelectionDialogNew.createForTrackSelector(
                this,
                trackSelector!!
            )
            { dismissedDialog: DialogInterface? ->
                isShowingTrackSelectionDialog = false
                binding.qualityContainer.visibility = View.GONE
                Log.d("kkjfkjaskfa", "onClick: DIALOG")

            }
            binding.qualityContainer.visibility = View.VISIBLE
            var newFragment =
                trackSelectionDialog?.showFragment(this) as TrackSelectionDialogNew.TrackSelectionViewFragment
            if (newFragment != null) {
                TrackSelectionDialogNew.isVisible = true
                supportFragmentManager.beginTransaction().replace(
                    R.id.quality_container,
                    newFragment
                ).commit()
                /*   binding.backword.visibility = View.GONE
                   binding.ivForword.visibility = View.GONE
                   binding.seekBar.visibility = View.INVISIBLE
                   binding.vollumeIcon.visibility = View.GONE
                   binding.seekbarValue.visibility = View.GONE

                   binding.title.visibility = View.GONE
                   binding.episodeLayout.visibility = View.GONE
                   binding.transparentLayout.visibility = View.GONE
                   binding.ivPlay.visibility = View.GONE
                   binding.ivSelectTrack.visibility = View.GONE
                   binding.ivPause.visibility = View.GONE

                   binding.ivBack.visibility = View.GONE
                   binding.ivMusic.visibility = View.GONE

                   if (binding.videoRv.visibility == View.GONE) {
                       ivHideControllerButton.visibility = View.GONE
                   }
   */
            }

        }


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
            R.id.chrome_cast -> {


                /*  val openDialog = CustomChromeCastBottomSheetDialog.newInstance(this,getActivity(),isCastConnected)
                  openDialog.setRouteSelector(mCastContext!!.mergedSelector)
                */ // openDialog.show(supportFragmentManager)

                if (isCastConnected) {

                    var remoteClient =
                        mCastContext?.sessionManager?.currentCastSession?.remoteMediaClient

                    if (remoteClient?.mediaInfo != null && remoteClient?.hasMediaSession()) {
                        if (castLoaderData.size > 0) {
                            mSelectedMedia = castLoaderData.get(previusPos)
                        }
                        mCastSession = mCastContext?.sessionManager?.currentCastSession
                        if (null != mSelectedMedia) {

                            val intent = Intent(
                                getActivity(),
                                ExpandedControlsActivity::class.java
                            ).putExtra(
                                "from",
                                "player"
                            ).putExtra("streamImage", streamImage)
                            startActivityForResult(intent, Constant.CHORME_EXPANDED_CODE)

/*
                            loadRemoteMedia(
                                mCastContext?.sessionManager?.currentCastSession?.remoteMediaClient?.approximateStreamPosition!!,
                                true
                            )
*/
                        }
                    } else {
                        mCastSession = mCastContext?.sessionManager?.currentCastSession
                        if (mCastSession?.castDevice?.friendlyName != null)
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

            R.id.iv_back -> {
                /*  if (from.isNotEmpty() && from.equals("history",true)){
                      onBackPressed()
                  }
                  */
                if (trailer != null && trailer.equals("yes")) {
                    onBackPressed()
                }
                //  else if (binding.ivBack.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.black_arrow_ico).getConstantState())
                else if (isFromExpandedView) {
                    onBackPressed()
                } else {
                    if (exerciseList.get(previusPos).is_workout.equals("Yes", true)) {
                        isFromResume = "no"
                        Constant.requestAudioFocusClose(this)
                        getVideoTimeDuration()
                        if (binding.playerView != null) {
                            releasePlayer()
                        }
                        var streamImg = ""
                        if (from.isNotEmpty() && from.equals("history", true))
                            streamImg = streamImage
                        else
                            streamImg =
                                exerciseList.get(previusPos).stream_video_image_url + "thumb/" + exerciseList.get(
                                    previusPos
                                ).stream_video_image

                        var isLast = "no"
                        if (previusPos < exerciseList.size - 1) {
                            isLast = "no"
                        } else {
                            isLast = "yes"
                        }
                        startActivityForResult(
                            Intent(getActivity(), StreamCompleteActivity::class.java)
                                .putExtra("workout_id", workout_id)
                                .putExtra("duration", duration)
                                .putExtra("isLast", isLast)
                                .putExtra("historyId", historyId)
                                .putExtra("from", "cancel")
                                .putExtra("video_id", exerciseList.get(previusPos).stream_video_id)
                                .putExtra("streamImage", streamImage)
                                .putExtra("name", exerciseList.get(previusPos).stream_video_name),
                            50
                        )
                    } else {
                        getVideoTimeDuration()
                        createPlayedHistory(
                            exerciseList.get(previusPos).stream_video_id,
                            workout_id,
                            historyId,
                            true
                        )
                    }
                }
            }
            R.id.vollume_icon -> {

            }


            R.id.iv_pause -> {
                dismissQwalityDialog()

                isPause = true
                binding.playerView.exo_pause.performClick()
                //  countdownhandler.removeCallbacks(countdownRunnable)

                binding.ivPlay.visibility = View.VISIBLE
                binding.ivPause.visibility = View.GONE
                binding.playerView.exo_play.visibility = View.GONE
                binding.playerView.exo_pause.visibility = View.GONE
                window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                Constant.requestAudioFocusForMyApp(this)


            }
            R.id.iv_play -> {

                /*    val dialog =com.doviesfitness.chromecast.utils.MediaRouteChooserDialog(getActivity())
                      dialog.routeSelector = mCastContext!!.mergedSelector
                      dialog.show()*/

                dismissQwalityDialog()

                if (isVideoDownloaded == 0 && !CommanUtils.isNetworkAvailable(getActivity())!!) {
                    isPause = true
                    binding.playerView.exo_pause.performClick()
                    //  countdownhandler.removeCallbacks(countdownRunnable)
                    binding.ivPlay.visibility = View.VISIBLE
                    binding.ivPause.visibility = View.GONE
                    binding.playerView.exo_play.visibility = View.GONE
                    binding.playerView.exo_pause.visibility = View.GONE
                    window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                    Constant.requestAudioFocusForMyApp(getActivity())
                    showInternateConnectionDialog1(getActivity())

                } else {
                    isPause = false
                    binding.playerView.exo_play.performClick()
                    //   countdownhandler.postDelayed(countdownRunnable, 0);
                    binding.ivPlay.visibility = View.GONE
                    binding.ivPause.visibility = View.VISIBLE
                    binding.playerView.exo_play.visibility = View.GONE
                    binding.playerView.exo_pause.visibility = View.GONE
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                    Constant.releaseAudioFocusForMyApp(this)
                }
            }

            R.id.iv_next,
            R.id.next -> {
//                play_button_type="next"
//                val dialog = FinishActivityDialog.newInstance(
//                    "Yes",
//                    "No",
//                    "Do you want to play the next workout?"
//                )
//                dialog.setListener(this@StreamVideoPlayUrlActivityTemp)
//                dialog.setListener2(this@StreamVideoPlayUrlActivityTemp)
//                dialog.show(supportFragmentManager)


                showNextPlayDialog("Do you want to play the next workout?")

            }
            R.id.iv_previous,
            R.id.previous -> {
//                play_button_type="previous"
//                val dialog = FinishActivityDialog.newInstance(
//                    "Yes",
//                    "No",
//                    "Do you want to play the previous workout?"
//                )
//                dialog.setListener(this@StreamVideoPlayUrlActivityTemp)
//                dialog.setListener2(this@StreamVideoPlayUrlActivityTemp)
//                dialog.show(supportFragmentManager)

                showPreviousPlayDialog("Do you want to play the previous workout?")

            }
            R.id.iv_vlayout,
            R.id.episodes -> {
                dismissQwalityDialog()

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
                    binding.seekbarValue.visibility = View.GONE
                    Glide.with(getActivity())
                        .load(R.drawable.ic_active_speaker_ico)
                        .into(binding.vollumeIcon)

                    binding.title.visibility = View.GONE
                    binding.ivBack.visibility = View.GONE
                    binding.mediaRouteMenuItem1.visibility = View.GONE
                    binding.chromeCast.visibility = View.GONE
                    binding.ivMusic.visibility = View.GONE
                    binding.episodeLayout.visibility = View.GONE
                    binding.transparentLayout.visibility = View.GONE
                    binding.ivPlay.visibility = View.GONE
                    binding.ivSelectTrack.visibility = View.GONE
                    binding.ivPause.visibility = View.GONE
                    exerciseList.get(previusPos).seekTo = player!!.currentPosition
                    adapter.notifyItemChanged(previusPos)
                    binding.playerView.exo_pause.performClick()
                    //   countdownhandler.removeCallbacks(countdownRunnable)

                    binding.playerView.exo_play.visibility = View.GONE
                    binding.playerView.exo_pause.visibility = View.GONE
                    window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

                    isPause = true
                }
            }
            R.id.playerView -> {
                dismissQwalityDialog()

                if (binding.videoRv.visibility == View.VISIBLE) {
                    binding.videoRv.visibility = View.GONE
                    binding.transparentLayout.visibility = View.GONE
                    binding.ivPlay.visibility = View.GONE
                    binding.ivPause.visibility = View.GONE
                    binding.ivSelectTrack.visibility = View.GONE
                    binding.topBlurView.visibility = View.GONE
                    ivHideControllerButton.visibility = View.GONE
                    binding.seekBar.visibility = View.INVISIBLE
                    binding.vollumeIcon.visibility = View.GONE
                    binding.seekbarValue.visibility = View.GONE
                    Glide.with(getActivity())
                        .load(R.drawable.ic_active_speaker_ico)
                        .into(binding.vollumeIcon)

                    //  binding.topBlurView1.visibility = View.GONE
                    binding.playerView.exo_play.performClick()
                    //   countdownhandler.postDelayed(countdownRunnable, 0);

                    binding.playerView.exo_play.visibility = View.GONE
                    binding.playerView.exo_pause.visibility = View.GONE
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                    isPause = false


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
                        //  binding.ivSelectTrack.visibility = View.VISIBLE
                        if (trailer != null && trailer.equals("yes"))
                            binding.ivSelectTrack.visibility = View.GONE
                        else {
                            if (fromDownloaded)
                                binding.ivSelectTrack.visibility = View.GONE
                            else

                                if (stream_qty_list!!.size == 1) {
                                    binding.ivSelectTrack.visibility = View.GONE
                                } else {
                                    binding.ivSelectTrack.visibility = View.VISIBLE
                                }
                        }

                        binding.ivForword.visibility = View.VISIBLE
                        binding.seekBar.visibility = View.VISIBLE
                        binding.seekbarValue.visibility = View.VISIBLE
                        dismissQwalityDialog()
                        trackSelectionDialog?.onDismissListener?.onDismiss(trackSelectionDialog?.dialog1);

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
                        binding.mediaRouteMenuItem1.visibility = View.GONE
                        binding.chromeCast.visibility = View.VISIBLE
                        binding.ivMusic.visibility = View.VISIBLE
                        (timer as CountDownTimer).start()

                    } else {
                        binding.backword.visibility = View.GONE
                        binding.ivForword.visibility = View.GONE
                        binding.seekBar.visibility = View.INVISIBLE
                        binding.vollumeIcon.visibility = View.GONE
                        binding.seekbarValue.visibility = View.GONE

                        binding.title.visibility = View.GONE
                        binding.episodeLayout.visibility = View.GONE
                        binding.transparentLayout.visibility = View.GONE
                        binding.ivPlay.visibility = View.GONE
                        binding.ivPause.visibility = View.GONE
                        binding.ivSelectTrack.visibility = View.GONE

                        binding.ivBack.visibility = View.GONE
                        binding.mediaRouteMenuItem1.visibility = View.GONE
                        binding.chromeCast.visibility = View.GONE
                        binding.ivMusic.visibility = View.GONE
                        ivHideControllerButton.visibility = View.GONE
                        timer?.cancel()

                    }
                }


            }
            R.id.backword -> {
                dismissQwalityDialog()

                Log.d("player duration", "duration..." + player!!.duration)
                var CP = player!!.currentPosition
                CP = CP - 10000
                if (CP > 0)
                    player?.seekTo(CP)
                //  else
                //   player?.seekTo(0)
            }
            R.id.iv_forword -> {
                dismissQwalityDialog()

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

    private fun getStream_qty(s: String) {
        val strs = s?.split(",")?.toTypedArray()
        Log.d("qwtrqtwqt", "$s =ONEE= ${strs.toString()}")
        stream_qty_list!!.clear()
        for (k in 0 until strs!!.size) {
            stream_qty_list!!.add(strs[k])
        }
        /*  if (stream_qty_list!!.size <= 1) {
              binding.ivSelectTrack.visibility = View.GONE
              videoAutoResolution = "" + stream_qty_list!!.get(0)
              Log.d("videoAutoResolution", "three : " + videoAutoResolution)
          }*/

        videoAutoResolution = if (stream_qty_list!!.size != 5) {
            when {
                getRequiredVideoResolution("360p") -> {
                    "360"
                }
                getRequiredVideoResolution("480p") -> {
                    "480"
                }
                else -> {
                    stream_qty_list!![stream_qty_list!!.size - 1].replace("p", "")
                }
            }
        } else {
            stream_qty_list!![stream_qty_list!!.size - 1].replace("p", "")
        }


        // callAutoclick()
        Log.d("qwtrqtwqt", "$videoAutoResolution =TWOO= ")

    }


    private fun previousplayclick() {


        dismissQwalityDialog()
        trackSelectionDialog?.onDismissListener?.onDismiss(trackSelectionDialog?.dialog1);
        isHistorySaved = true

        binding.rlPlaypause.visibility = View.GONE
        if (previusPos > 0) {
            ready = true
            releasePlayer()
            adapter.notifyItemChanged(previusPos)
            previusPos = previusPos - 1


            //-----------------------Hemant check Previous play View type--------
            if ((exerciseList.get(previusPos).view_type.equals("1")) && (exerciseList.get(
                    previusPos
                ).Progress != 100)
            ) {

                downloadcheck(exerciseList.get(previusPos).stream_video_id, previusPos)
            }
            if ((exerciseList.get(previusPos).view_type.equals("1")) && (exerciseList.get(
                    previusPos
                ).Progress == 100)
            ) {
                Log.e(
                    "View_type",
                    "Download Type-->" + previusPos + " " + exerciseList.get(previusPos).view_type
                )
                previusVideoPlay()

            } else


                if (exerciseList.get(previusPos).view_type.equals("2") || exerciseList.get(
                        previusPos
                    ).view_type.equals("3")
                ) {
                    Log.e(
                        "View_type",
                        "View Type-->" + previusPos + " " + exerciseList.get(previusPos).view_type
                    )

                    previusVideoPlay()

                } else {
                    Log.e(
                        "View_type",
                        "Both-->" + previusPos + " " + exerciseList.get(previusPos).view_type
                    )



                    dialog =
                        ErrorDialog.newInstance("", "Ok", getString(R.string.error_download_type))
                    dialog.setListener(this)
                    dialog.show(supportFragmentManager)

                }
            //-----------------------------------------
        } else {
            previusVideoPlay()
        }

    }

    private fun previusVideoPlay() {
        if (previusPos >= 0) {

            stream_qty = exerciseList[previusPos].hls_video!!.stream_quality
            getStream_qty(stream_qty)
            if (exerciseList.get(previusPos).seekTo >= (exerciseList.get(previusPos).MaxProgress * 1000L)) {
                exerciseList.get(previusPos).seekTo = 0
                adapter.notifyItemChanged(previusPos)
            }
            if (exerciseList.get(previusPos).stream_video_name != null && !exerciseList.get(
                    previusPos
                ).stream_video_name.isEmpty()
            ) {
                var titlestr = exerciseList.get(previusPos).stream_video_name
                val cap: String =
                    titlestr.substring(0, 1).toUpperCase() + titlestr.substring(1)
                binding.title.text = cap
            }


            //    binding.title.text = exerciseList.get(previusPos).stream_video_name
            if (exerciseList.get(previusPos).downLoadUrl != null && !exerciseList.get(
                    previusPos
                ).downLoadUrl.isEmpty()
            ) {
                fromDownloaded = true
                binding.ivSelectTrack.visibility = View.GONE
                dismissQwalityDialog()
                isVideoDownloaded = 1
                initializePlayer(exerciseList.get(previusPos).downLoadUrl, previusPos, 1, 0, "pre")
            } else {
                fromDownloaded = false
                isVideoDownloaded = 0

                initializePlayer(
                    exerciseList.get(previusPos).stream_video,
                    previusPos,
                    0,
                    0, "pre"
                )

            }
            if (previusPos > 0) {
                binding.previousLayout.visibility = View.VISIBLE
            } else {
                binding.previousLayout.visibility = View.INVISIBLE

            }
            if (mCastSession != null && mCastSession!!.isConnected) {
                Log.d("session connection", "session connection...connected")
                if (castLoaderData.size > 0) {
                    mSelectedMedia = castLoaderData.get(previusPos)
                }
                if (null != mSelectedMedia) {
                    loadRemoteMedia(0, true)
                }
            } else {
                Log.d("session connection", "session connection...not connected")
            }

        }
//        else{
//            initializePlayer(
//                exerciseList.get(previusPos).stream_video,
//                0,
//                0,
//                0
//            )
//        }
        if (previusPos < (exerciseList.size - 1)) {
            binding.nextLayout.visibility = View.VISIBLE
        } else {
            binding.nextLayout.visibility = View.INVISIBLE
        }
        /*  if (isVideoDownloaded==0 && !CommanUtils.isNetworkAvailable(getActivity())!!)
            isPause = true
        else
            isPause = false*/
        if (isVideoDownloaded == 0 && !CommanUtils.isNetworkAvailable(getActivity())!!) {
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
        } else {
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

    private fun NextPlay() {

        dismissQwalityDialog()
        trackSelectionDialog?.onDismissListener?.onDismiss(trackSelectionDialog?.dialog1);
        isHistorySaved = true
        ready = true
        binding.rlPlaypause.visibility = View.GONE


        if (previusPos < (exerciseList.size - 1)) {
            releasePlayer()
            adapter.notifyItemChanged(previusPos)
            if (previusPos != exerciseList.size) {
                previusPos = previusPos + 1
            }
            stream_qty = exerciseList[previusPos].hls_video!!.stream_quality
            getStream_qty(stream_qty)

            if ((exerciseList.get(previusPos).view_type.equals("1")) && (exerciseList.get(
                    previusPos
                ).Progress != 100)
            ) {

                downloadcheck(exerciseList.get(previusPos).stream_video_id, previusPos)
            }

            //--------------------new---------------
            //-----------------------Hemant check Previous play View type--------
            if ((exerciseList.get(previusPos).view_type.equals("1")) && (exerciseList.get(
                    previusPos
                ).Progress == 100)
            ) {
                Log.e(
                    "View_type",
                    "Download Type-->" + previusPos + " " + exerciseList.get(previusPos).view_type
                )
                NextVideoPlay()

            } else if (exerciseList.get(previusPos).view_type.equals("2") || exerciseList.get(
                    previusPos
                ).view_type.equals("3")
            ) {
                Log.e(
                    "View_type",
                    "View Type-->" + previusPos + " " + exerciseList.get(previusPos).view_type
                )

                NextVideoPlay()

            } else {
                Log.e(
                    "View_type",
                    "Both-->" + previusPos + " " + exerciseList.get(previusPos).view_type
                )
                dialog = ErrorDialog.newInstance("", "Ok", getString(R.string.error_download_type))
                dialog.setListener(this)
                dialog.show(supportFragmentManager)

            }


        }


    }

    private fun downloadcheck(streamVideoId: String, pos: Int) {
        if (downloadVideoList != null) {
            for (i in 0 until downloadVideoList!!.size) {
                for (i in 0 until downloadVideoList!!.size) {


//                                    if ((downloadVideoList!!.get(i).download_list.get(i).stream_video_id).equals(exerciseList.get(j).stream_video_id)) {
                    for (k in 0 until downloadVideoList!!.get(i).download_list!!.size) {
                        if ((downloadVideoList!!.get(i).download_list!!.get(k).stream_video_id).equals(
                                streamVideoId
                            )
                        ) {
                            exerciseList.get(pos).Progress = 100
                            break
                        }
//
                    }
                }
            }
        }
    }

    private fun NextVideoPlay() {


//        if (previusPos <= (exerciseList.size )) {

        //-------------------------------------------------------------------


        if (exerciseList.get(previusPos).seekTo >= (exerciseList.get(previusPos).MaxProgress * 1000L)) {
            exerciseList.get(previusPos).seekTo = 0
            adapter.notifyItemChanged(previusPos)
        }

        if (exerciseList.get(previusPos).stream_video_name != null && !exerciseList.get(
                previusPos
            ).stream_video_name.isEmpty()
        ) {
            var titlestr = exerciseList.get(previusPos).stream_video_name
            val cap: String =
                titlestr.substring(0, 1).toUpperCase() + titlestr.substring(1)
            binding.title.text = cap
        }

        //  binding.title.text = exerciseList.get(previusPos).stream_video_name

        if (exerciseList.get(previusPos).downLoadUrl != null && !exerciseList.get(
                previusPos
            ).downLoadUrl.isEmpty()
        ) {
            fromDownloaded = true
            binding.ivSelectTrack.visibility = View.GONE
            dismissQwalityDialog()
            isVideoDownloaded = 1
            initializePlayer(
                exerciseList.get(previusPos).downLoadUrl,
                previusPos,
                1,
                0,
                "NextVideoPlay"
            )
        } else {
            fromDownloaded = false
            isVideoDownloaded = 0
            initializePlayer(
                exerciseList.get(previusPos).stream_video,
                previusPos,
                0,
                0, "NextVideoPlay"
            )

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
        if (mCastSession != null && mCastSession!!.isConnected) {
            Log.d("session connection", "session connection...connected")
            if (castLoaderData.size > 0) {
                mSelectedMedia = castLoaderData.get(previusPos)
            }
            if (null != mSelectedMedia) {
                loadRemoteMedia(0, true)

            }
        } else {
            Log.d("session connection", "session connection...not connected")
        }


        //}

        if (previusPos > 0) {
            binding.previousLayout.visibility = View.VISIBLE
        } else {
            binding.previousLayout.visibility = View.INVISIBLE

        }
        if (isVideoDownloaded == 0 && !CommanUtils.isNetworkAvailable(getActivity())!!) {
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
        } else {
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

    public fun dismissQwalityDialog() {
        binding.qualityContainer.visibility = View.GONE
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

    }

    override fun onResume() {
        Log.d("on resume", "on resume...")
        //  CastButtonFactory.setUpMediaRouteButton(applicationContext, binding.mediaRouteMenuItem1)


        hideNavStatusBar()
        //---------------Hemant handel Dialog Box Next Play Button---------
        if ((exerciseList.get(previusPos).view_type.equals("1")) && (exerciseList.get(
                previusPos
            ).Progress != 100)
        ) {

            downloadcheck(exerciseList.get(previusPos).stream_video_id, previusPos)
        }

        //--------------------new---------------
        //-----------------------Hemant check Previous play View type--------
        if ((exerciseList.get(previusPos).view_type.equals("1")) && (exerciseList.get(
                previusPos
            ).Progress == 100)
        ) {
            Log.e(
                "View_type",
                "Download Type-->" + previusPos + " " + exerciseList.get(previusPos).view_type
            )
            callOnResumeNextPlay()
        } else if (exerciseList.get(previusPos).view_type.equals("2") || exerciseList.get(previusPos).view_type.equals(
                "3"
            )
        ) {
            Log.e(
                "View_type",
                "View Type-->" + previusPos + " " + exerciseList.get(previusPos).view_type
            )

            callOnResumeNextPlay()
        } else {
            Log.e(
                "View_type",
                "Both-->" + previusPos + " " + exerciseList.get(previusPos).view_type
            )
            ErrorDialog.newInstance("", "Ok", getString(R.string.error_download_type))
                .show(supportFragmentManager)

        }


        //------------------------------------------------------


        //  invalidateOptionsMenu()
        super.onResume()
    }

    private fun callOnResumeNextPlay() {
        if (isFromResume.equals("yes", true)) {
            binding.rlPlaypause.visibility = View.VISIBLE
            if (binding.playerView != null) {

                if (exerciseList.get(previusPos).stream_video_name != null && !exerciseList.get(
                        previusPos
                    ).stream_video_name.isEmpty()
                ) {
                    var titlestr = exerciseList.get(previusPos).stream_video_name
                    val cap: String =
                        titlestr.substring(0, 1).toUpperCase() + titlestr.substring(1)
                    binding.title.text = cap
                }

                //  binding.title.text = exerciseList.get(previusPos).stream_video_name

                if (exerciseList.get(previusPos).downLoadUrl != null && !exerciseList.get(previusPos).downLoadUrl.isEmpty()) {
                    fromDownloaded = true
                    binding.ivSelectTrack.visibility = View.GONE
                    dismissQwalityDialog()
                    initializePlayer(
                        exerciseList.get(previusPos).downLoadUrl,
                        previusPos,
                        1,
                        1,
                        "callOnResumeNextPlay"
                    )
                } else {
                    fromDownloaded = false
                    initializePlayer(
                        exerciseList.get(previusPos).stream_video,
                        previusPos,
                        0,
                        1,
                        "callOnResumeNextPlay"
                    )
                }

                if (binding.videoRv.visibility == View.VISIBLE || isPause) {
                    binding.playerView.exo_pause.performClick()
                    //  countdownhandler.removeCallbacks(countdownRunnable)

                    binding.playerView.exo_play.visibility = View.GONE
                    binding.playerView.exo_pause.visibility = View.GONE
                    window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                    binding.transparentLayout.visibility = View.VISIBLE
                    if (trailer != null && trailer.equals("yes"))
                        binding.ivSelectTrack.visibility = View.GONE
                    else {
                        if (fromDownloaded)
                            binding.ivSelectTrack.visibility = View.GONE
                        else
                            if (stream_qty_list!!.size <= 1)
                                binding.ivSelectTrack.visibility = View.GONE
                            else
                                binding.ivSelectTrack.visibility = View.VISIBLE
                    }

                    binding.ivPlay.visibility = View.VISIBLE
                    binding.ivPause.visibility = View.GONE
                    binding.backword.visibility = View.VISIBLE
                    binding.ivForword.visibility = View.VISIBLE
                    binding.seekBar.visibility = View.VISIBLE
                    binding.vollumeIcon.visibility = View.VISIBLE
                    binding.seekbarValue.visibility = View.VISIBLE
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
                    binding.mediaRouteMenuItem1.visibility = View.GONE
                    binding.chromeCast.visibility = View.VISIBLE
                    binding.ivMusic.visibility = View.VISIBLE
                    (timer as CountDownTimer).start()
                    isPause = true
                    Constant.requestAudioFocusForMyApp(this)
                } else {
                    Constant.releaseAudioFocusForMyApp(this)
                }

                //val audioManager = this.getSystemService(Context.AUDIO_SERVICE) as AudioManager
                val currentVolume: Int = getDeviseVolume() //audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
                var progress = binding.seekBar.getProgress()
                var PVal = (currentVolume / 15.0)
                Log.d("player seekbar vollume", "player seekbar hardware..." + PVal)

                player?.volume = PVal.toFloat()
                //  countdownhandler.postDelayed(countdownRunnable, 0);

            }
            var delayMs = TimeUnit.SECONDS.toMillis(1);
            mHandler.postDelayed(remainingRunnable, delayMs);
        } else {
            isFromResume = "yes"
        }
        mCastContext = CastContext.getSharedInstance(this)
        mCastSession = mCastContext?.sessionManager?.currentCastSession

        mCastContext!!.sessionManager.addSessionManagerListener(
            mSessionManagerListener, CastSession::class.java
        )
        if (mCastSession != null && mCastSession!!.isConnected) {
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
            );

        }
    }

    override fun onPause() {
        Log.d("on pause", "on pause...")

        if (binding.playerView != null) {
            exerciseList.get(previusPos).seekTo = player!!.currentPosition
            releasePlayer()
        }
        Constant.requestAudioFocusForMyApp(this)
        //  mTime += mCurrentLapse;
        //    countdownhandler.removeCallbacks(countdownRunnable)
        mCastContext!!.sessionManager.removeSessionManagerListener(
            mSessionManagerListener,
            CastSession::class.java
        )
        binding.castDialogLayout.visibility = View.GONE

        super.onPause()
    }

    override fun preparePlayback() {
        player!!.prepare()
    }

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
                                    if (isLast) {
                                        finish()
                                    }
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
    //////////dialog

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


    open fun onFilterRoute(route: MediaRouter.RouteInfo): Boolean {
        return !route.isDefault && route.matchesSelector(mSelector)
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
            } catch (e: Exception) {
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
                        loader.visibility = View.VISIBLE
                    } else
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

    public fun refreshRoutes(isStart: Boolean) {

        mAdapter?.update(isStart)

    }

    public inner class RouteComparator : Comparator<MediaRouter.RouteInfo?> {
        init {
            sInstance = RouteComparator()
        }


        override fun compare(lhs: MediaRouter.RouteInfo?, rhs: MediaRouter.RouteInfo?): Int {
            return lhs?.name?.compareTo(rhs!!.name)!!
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

    override fun isOk() {
        finish()
    }


    private fun showNextPlayDialog(title: String) {
        val root = RelativeLayout(this)
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialog.setContentView(root)
        if (dialog.window != null) {
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

        }
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_cancel)
        val body = dialog.findViewById(R.id.tv_header) as TextView
        body.text = title
        val yesBtn = dialog.findViewById(R.id.tv_logout) as TextView
        val noBtn = dialog.findViewById(R.id.tv_cancel) as TextView
        noBtn.setText("No")
        yesBtn.setText("Yes")
        yesBtn.setOnClickListener {
            NextPlay()
            dialog.dismiss()
        }
        noBtn.setOnClickListener {
            finish()
            dialog.dismiss()
        }
        dialog.show()

    }

    private fun showPreviousPlayDialog(title: String) {
        val root = RelativeLayout(this)
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(root)
        if (dialog.window != null) {
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

        }
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_cancel)
        val body = dialog.findViewById(R.id.tv_header) as TextView
        body.text = title
        val yesBtn = dialog.findViewById(R.id.tv_logout) as TextView
        val noBtn = dialog.findViewById(R.id.tv_cancel) as TextView
        noBtn.setText("No")
        yesBtn.setText("Yes")
        yesBtn.setOnClickListener {
            previousplayclick()
            dialog.dismiss()
        }
        noBtn.setOnClickListener {
            finish()
            dialog.dismiss()
        }
        dialog.show()

    }

    override fun isYesClick() {
        finish()
    }

    override fun hideTransparentView() {
        Log.d("Dialog-->", "hideTransparentView: ")
    }

    override fun isDelete() {
        if (play_button_type.equals("next")) {
            NextPlay()
        } else {
            previousplayclick()
        }
    }

    private fun getAutoVideoMinWidthHeight(videoResolution: String): Pair<Int, Int> {
        return when (videoResolution) {
            "1440" -> Pair(2560, 1440)
            "1080" -> Pair(1920, 1080)
            "720" -> Pair(1280, 720)
            "480" -> Pair(854, 480)
            else -> {
                Pair(640, 360) //consider it 360p
            }
        }
    }


}


