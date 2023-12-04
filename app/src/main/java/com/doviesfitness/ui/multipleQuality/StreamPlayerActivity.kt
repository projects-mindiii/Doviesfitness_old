package com.doviesfitness.ui.multipleQuality


import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Pair
import android.view.*
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.doviesfitness.R
import com.doviesfitness.databinding.PlayerActivityBinding
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.bottom_tabbar.stream_tab.activities.StreamDetailActivity
import com.doviesfitness.ui.bottom_tabbar.stream_tab.adapters.StreamListAdapter
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.StreamWorkoutDetailModel
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.drm.FrameworkMediaDrm
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer.DecoderInitializationException
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil.DecoderQueryException
import com.google.android.exoplayer2.source.BehindLiveWindowException
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSourceFactory
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector.ParametersBuilder
import com.google.android.exoplayer2.trackselection.MappingTrackSelector.MappedTrackInfo
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.DebugTextViewHelper
import com.google.android.exoplayer2.ui.StyledPlayerControlView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.util.Assertions
import com.google.android.exoplayer2.util.ErrorMessageProvider
import com.google.android.exoplayer2.util.EventLogger
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.player_activity.*
import java.lang.Exception
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy
import java.util.*


class StreamPlayerActivity : BaseActivity(),View.OnClickListener, PlaybackPreparer,
 StyledPlayerControlView.VisibilityListener {

 lateinit var binding: PlayerActivityBinding
 private lateinit var audioManager: AudioManager
 var timer: CountDownTimer? = null
 lateinit var cResolver: ContentResolver

 lateinit var exerciseList: ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>
 //  private var player: SimpleExoPlayer? = null
 private lateinit var mediaDataSourceFactory: DataSource.Factory
 private val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory()
 private val ivHideControllerButton: LinearLayout by lazy {
  findViewById<LinearLayout>(R.id.controller)
 }
 //private var lastSeenTrackGroupArray: TrackGroupArray? = null
  //private var trackSelector: DefaultTrackSelector? = null
 //private var dataSourceFactory: DefaultDataSourceFactory? = null
 private var currentWindow: Int = 0
 private var playbackPosition: Long = 0
 private var previusPos: Int = 0
 lateinit var adapter: StreamListAdapter
 var local = ""
 var trailer = ""
 var isVideoDownloaded = 0
 var LayoutVisibility = false
 var isPause = false
 var ready = true
 var seekbarTuch = false


 ////////////////
 protected var player: SimpleExoPlayer? = null
 private var isShowingTrackSelectionDialog = false
 private var dataSourceFactory: DataSource.Factory? = null
 private var mediaItems: List<MediaItem>? = null
 private var trackSelector: DefaultTrackSelector? = null
 private var trackSelectorParameters: DefaultTrackSelector.Parameters? = null
 private var debugViewHelper: DebugTextViewHelper? = null
 private var lastSeenTrackGroupArray: TrackGroupArray? = null
 private var startAutoPlay = false
 private var startWindow = 0
 private var startPosition: Long = 0
 private val KEY_TRACK_SELECTOR_PARAMETERS = "track_selector_parameters"
 private val KEY_WINDOW = "window"
 private val KEY_POSITION = "position"
 private val KEY_AUTO_PLAY = "auto_play"
 private var DEFAULT_COOKIE_MANAGER: CookieManager? = null
 var workout_id=""
 companion object
 {
 var DEFAULT_COOKIE_MANAGER =  CookieManager ()
 }

 override fun onCreate(savedInstanceState: Bundle?) {
  requestWindowFeature(Window.FEATURE_NO_TITLE)
  hideNavStatusBar()
  super.onCreate(savedInstanceState)
  getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
  DEFAULT_COOKIE_MANAGER?.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER)

  dataSourceFactory = DemoUtil.getDataSourceFactory( /* context= */this)
  if (CookieHandler.getDefault() !== DEFAULT_COOKIE_MANAGER) {
   CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER)
  }

  binding = DataBindingUtil.setContentView(this, R.layout.player_activity)
  var surfaceView = binding.playerView.videoSurfaceView as SurfaceView
  surfaceView.setSecure(true)
  if (intent.hasExtra("workout_id"))
  workout_id= intent.getStringExtra("workout_id")!!


  landscapeFunctionality()
  exerciseList = intent.getSerializableExtra("videoList")!! as ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>
  if (intent.hasExtra("local"))
  local = intent.getStringExtra("local")!!
  if (intent.hasExtra("trailer"))
  trailer = intent.getStringExtra("trailer")!!
  previusPos = 0
  if (intent.hasExtra("position"))
   previusPos = intent.getIntExtra("position", 0)

  if (intent.hasExtra("media_name")) {
   var media_name = intent.getStringExtra("media_name")!!
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
  binding.selectTracksButton!!.setOnClickListener(this)

  //binding.playerView.setControllerVisibilityListener(this)
  binding.playerView.setErrorMessageProvider(PlayerErrorMessageProvider(this))
  binding.playerView.requestFocus()

  if (savedInstanceState != null) {
   trackSelectorParameters =
    savedInstanceState.getParcelable(KEY_TRACK_SELECTOR_PARAMETERS)
   startAutoPlay = savedInstanceState.getBoolean(KEY_AUTO_PLAY)
   startWindow = savedInstanceState.getInt(KEY_WINDOW)
   startPosition = savedInstanceState.getLong(KEY_POSITION)
  } else {
   val builder = ParametersBuilder( /* context= */this)
   trackSelectorParameters = builder.build()
   clearStartPosition()
  }

 // initialisation()
 // vollumeControls()
/*
  timer = object : CountDownTimer(5000, 10) {
   override fun onTick(millisUntilFinished: Long) {
   }

   override fun onFinish() {
    if (!seekbarTuch) {
     binding.backword.visibility = View.GONE
     binding.
     -.visibility = View.GONE
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
    cancel()
   }
  }
*/
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

 //////
 protected fun clearStartPosition() {
  startAutoPlay = true
  startWindow = C.INDEX_UNSET
  startPosition = C.TIME_UNSET
 }

 override fun onNewIntent(intent: Intent?) {
  super.onNewIntent(intent)
  releasePlayer()
  clearStartPosition()
  setIntent(intent)
 }


 override fun onStart() {
  super.onStart()
  if (Util.SDK_INT > 23) {
   initializePlayer()
   if (binding.playerView != null) {
    binding.playerView!!.onResume()
   }
  }
 }

 override fun onResume() {
  super.onResume()
  if (Util.SDK_INT <= 23 || player == null) {
   initializePlayer()
   if (binding.playerView != null) {
    binding.playerView!!.onResume()
   }
  }
 }

 override fun onPause() {
  super.onPause()
  if (Util.SDK_INT <= 23) {
   if (binding.playerView != null) {
    binding.playerView!!.onPause()
   }
   releasePlayer()
  }
 }

 override fun onStop() {
  super.onStop()
  if (Util.SDK_INT > 23) {
   if (binding.playerView != null) {
    binding.playerView!!.onPause()
   }
   releasePlayer()
  }
 }

 override fun onRequestPermissionsResult(
  requestCode: Int, permissions: Array<String?>, grantResults: IntArray
 ) {
  super.onRequestPermissionsResult(requestCode, permissions, grantResults)
  if (grantResults.size == 0) {
   return
  }
  if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
   initializePlayer()
  } else {
   showToast(R.string.storage_permission_denied)
   finish()
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

 // Activity input
 override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
  // See whether the player view wants to handle media or DPAD keys events.
  return binding.playerView!!.dispatchKeyEvent(event!!) || super.dispatchKeyEvent(event)
 }

 // OnClickListener methods
 override fun onClick(view: View) {
  if (view === binding.selectTracksButton && !isShowingTrackSelectionDialog
   && TrackSelectionDialog.willHaveContent(trackSelector!!)
  ) {
  // binding.qualityContainer.visibility=View.VISIBLE
   isShowingTrackSelectionDialog = true

   val trackSelectionDialog = TrackSelectionDialog.createForTrackSelector(trackSelector !!
/* onDismissListener= */) { dismissedDialog: DialogInterface? ->
    isShowingTrackSelectionDialog = false
   }
   trackSelectionDialog.show(supportFragmentManager,  /* tag= */null)

  // val newFragment: Fragment = trackSelectionDialog.showFragment(R.id.quality_container,getActivity())
  // supportFragmentManager.beginTransaction().add(R.id.quality_container,newFragment).commit()

}
  else{
  // binding.qualityContainer.visibility=View.GONE

  }
 }
 override fun preparePlayback() {
  player!!.prepare()
 }
 // PlayerControlView.VisibilityListener implementation
 override fun onVisibilityChange(visibility: Int) {
  binding.controlsRoot!!.visibility = visibility
 }

 /** @return Whether initialization was successful.
  */
 @SuppressLint("WrongConstant")
 protected fun initializePlayer(): Boolean {
  if (player == null) {
   val intent = intent
   mediaItems = createMediaItems(intent)
   if (mediaItems!!.isEmpty()) {
    return false
   }
   val preferExtensionDecoders =
    intent.getBooleanExtra(IntentUtil.PREFER_EXTENSION_DECODERS_EXTRA, false)
   val renderersFactory =
    DemoUtil.buildRenderersFactory( /* context= */this, preferExtensionDecoders)

   val mediaSourceFactory: MediaSourceFactory = DefaultMediaSourceFactory(dataSourceFactory!!)

   trackSelector = DefaultTrackSelector( /* context= */this)
   trackSelector!!.setParameters(trackSelectorParameters!!)
   lastSeenTrackGroupArray = null
   //need to uncomment

   player = SimpleExoPlayer.Builder(
this, renderersFactory)
    .setMediaSourceFactory(mediaSourceFactory)
    .setTrackSelector(trackSelector!!)
    .build()

   player!!.addListener(PlayerEventListener(this))
   player!!.addAnalyticsListener(EventLogger(trackSelector))
   player!!.setAudioAttributes(
    AudioAttributes.DEFAULT,  /* handleAudioFocus= */
    true
   )
   player!!.playWhenReady = startAutoPlay
   binding.playerView!!.player = player
   binding.playerView!!.setShowBuffering(1)
   binding.playerView!!.setPlaybackPreparer(this)
   debugViewHelper = DebugTextViewHelper(player!!, binding.debugTextView!!)
   debugViewHelper!!.start()
  }
  val haveStartPosition = startWindow != C.INDEX_UNSET
  if (haveStartPosition) {
   player!!.seekTo(startWindow, startPosition)
  }
  player!!.setMediaItems(mediaItems!!,  /* resetPosition= */!haveStartPosition)
  player!!.prepare()
  updateButtonVisibility()
  return true
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


  val mediaItems = createMediaItems(intent, DemoUtil.getDownloadTracker( getActivity()))
  var hasAds = false
  for (i in mediaItems!!.indices) {
   val mediaItem = mediaItems[i]
   if (!Util.checkCleartextTrafficPermitted(mediaItem)) {
    showToast(R.string.error_cleartext_not_permitted)
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
     showToast(R.string.error_drm_unsupported_before_api_18)
     finish()
     return emptyList()
    } else if (!FrameworkMediaDrm.isCryptoSchemeSupported(drmConfiguration.uuid)) {
     showToast(R.string.error_drm_unsupported_scheme)
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
 protected fun releasePlayer() {
  if (player != null) {
   updateTrackSelectorParameters()
   updateStartPosition()
   debugViewHelper!!.stop()
   debugViewHelper = null
   player!!.release()
   player = null
   mediaItems = emptyList()
   trackSelector = null
  }
 }
 private fun updateTrackSelectorParameters() {
  if (trackSelector != null) {
   trackSelectorParameters = trackSelector!!.getParameters()
  }
 }

 private fun updateStartPosition() {
  if (player != null) {
   startAutoPlay = player!!.playWhenReady
   startWindow = player!!.currentWindowIndex
   startPosition = Math.max(0, player!!.contentPosition)
  }
 }
 private fun updateButtonVisibility() {
  binding.selectTracksButton!!.isEnabled = player != null && TrackSelectionDialog.willHaveContent(
   trackSelector!!
  )

 }

 public fun showControls() {
  binding.controlsRoot!!.visibility = View.VISIBLE
 }

 private fun showToast(messageId: Int) {
  showToast(getString(messageId))
 }

 private fun showToast(message: String) {
  Toast.makeText(applicationContext, message, Toast.LENGTH_LONG)
   .show()
 }

 private fun isBehindLiveWindow(e: ExoPlaybackException): Boolean {
  if (e.type != ExoPlaybackException.TYPE_SOURCE) {
   return false
  }
  var cause: Throwable? = e.sourceException
  while (cause != null) {
   if (cause is BehindLiveWindowException) {
    return true
   }
   cause = cause.cause
  }
  return false
 }

 private class PlayerEventListener(streamPlayerActivity: StreamPlayerActivity) : Player.EventListener {

  var streamPlayerActivity=streamPlayerActivity;
  override fun onPlaybackStateChanged(@Player.State playbackState: Int) {

   if (playbackState == Player.STATE_ENDED) {

    streamPlayerActivity.showControls()
   }
   streamPlayerActivity.updateButtonVisibility()

  }

  override fun onPlayerError(e: ExoPlaybackException) {

   if (streamPlayerActivity.isBehindLiveWindow(e)) {
    streamPlayerActivity.clearStartPosition()
    streamPlayerActivity.initializePlayer()
   } else {
    streamPlayerActivity.updateButtonVisibility()
    streamPlayerActivity.showControls()
   }

  }

  override fun onTracksChanged(
   trackGroups: TrackGroupArray, trackSelections: TrackSelectionArray
  ) {
   //updateButtonVisibility()
   Handler().postDelayed(Runnable {
    if (trackGroups !== streamPlayerActivity.lastSeenTrackGroupArray) {
     try {
      val mappedTrackInfo: MappedTrackInfo = streamPlayerActivity!!.trackSelector!!.getCurrentMappedTrackInfo()!!
      if (mappedTrackInfo != null) {
       if (mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_VIDEO)
        == MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS
       ) {

        streamPlayerActivity.showToast(R.string.error_unsupported_video)
       }
       if (mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_AUDIO)
        == MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS
       ) {
        streamPlayerActivity.showToast(R.string.error_unsupported_audio)
       }
      }
     }
     catch (ex:Exception){
      ex.printStackTrace()
     }
     streamPlayerActivity.lastSeenTrackGroupArray = trackGroups
    }

   },2000)


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

 public class PlayerErrorMessageProvider(streamPlayerActivity: StreamPlayerActivity) :
  ErrorMessageProvider<ExoPlaybackException?> {
  var streamPlayerActivity=streamPlayerActivity
  @SuppressLint("StringFormatInvalid")
  override fun getErrorMessage(e: ExoPlaybackException): Pair<Int, String> {
   streamPlayerActivity
  // var errorString: String = getString(R.string.error_generic)
   var errorString: String = streamPlayerActivity.getString(R.string.error_generic)
   if (e.type == ExoPlaybackException.TYPE_RENDERER) {
    val cause = e.rendererException
    if (cause is DecoderInitializationException) {
     // Special case for decoder initialization failures.
     val decoderInitializationException =
      cause
     if (decoderInitializationException.codecInfo == null) {
      if (decoderInitializationException.cause is DecoderQueryException) {
       errorString = streamPlayerActivity.getString(R.string.error_querying_decoders)
      } else if (decoderInitializationException.secureDecoderRequired) {
       errorString =streamPlayerActivity.getString(
        R.string.error_no_secure_decoder, decoderInitializationException.mimeType
       )
      } else {
       errorString = streamPlayerActivity.getString(R.string.error_no_decoder, decoderInitializationException.mimeType)
      }
     } else {
      errorString = streamPlayerActivity.getString(
       R.string.error_instantiating_decoder,
       decoderInitializationException.codecInfo!!.name
      )
     }
    }
   }
   return Pair.create(0, errorString)
  }
 }

 private fun createMediaItems(
  intent: Intent,
  downloadTracker: DownloadTracker
 ): List<MediaItem>? {
  val mediaItems: MutableList<MediaItem> =
   ArrayList()
  for (item in IntentUtil.createMediaItemsFromIntent(intent)) {
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


}