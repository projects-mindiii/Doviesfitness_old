package com.doviesfitness.ui.bottom_tabbar.stream_tab.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import androidx.mediarouter.app.MediaRouteChooserDialogFragment
import androidx.mediarouter.app.MediaRouteDialogFactory
import androidx.mediarouter.media.MediaRouteSelector
import androidx.mediarouter.media.MediaRouter
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import com.doviesfitness.Doviesfitness
import com.doviesfitness.R
import com.doviesfitness.allDialogs.ExclusiveWorkoutDialog
import com.doviesfitness.allDialogs.menu.ErrorDownloadViewTypeDialog
import com.doviesfitness.chromecast.browser.VideoItemLoader
import com.doviesfitness.chromecast.expandedcontrols.ExpandedControlsActivity
import com.doviesfitness.chromecast.settings.CustomChromeCastBottomSheetDialogNew
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.databinding.ActivityStreamDetailNewBinding
import com.doviesfitness.subscription.SubscriptionActivity
import com.doviesfitness.temp.DownloadVideosUtil
import com.doviesfitness.temp.TempSteamVideoFragmentNew
import com.doviesfitness.temp.modal.VideoCategoryModal
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.bottom_tabbar.stream_tab.adapters.StreamPagerAdapter
import com.doviesfitness.ui.bottom_tabbar.stream_tab.fragments.OtherMediaExerciseFragment
import com.doviesfitness.ui.bottom_tabbar.stream_tab.fragments.OtherMediaFragment
import com.doviesfitness.ui.bottom_tabbar.stream_tab.fragments.StreamOverviewFragment
import com.doviesfitness.ui.bottom_tabbar.stream_tab.fragments.StreamTrailerFragment
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.*
import com.doviesfitness.ui.multipleQuality.IntentUtil
import com.doviesfitness.ui.multipleQuality.StreamVideoPlayUrlActivityTemp
import com.doviesfitness.ui.room_db.DatabaseClient
import com.doviesfitness.ui.room_db.MyVideoList
import com.doviesfitness.utils.*
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.util.Assertions
import com.google.android.exoplayer2.util.Util
import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.MediaLoadRequestData
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.SessionManagerListener
import com.google.android.gms.cast.framework.media.RemoteMediaClient
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import eightbitlab.com.blurview.RenderScriptBlur
import kotlinx.android.synthetic.main.feed_view.*
import org.json.JSONObject
import java.io.IOException
import java.util.*


class StreamDetailActivity : BaseActivity(), View.OnClickListener,
    LoaderManager.LoaderCallbacks<List<MediaInfo>>,
    CustomChromeCastBottomSheetDialogNew.ChromeCastListener, ErrorDownloadViewTypeDialog.IsOK {

    lateinit var binding: ActivityStreamDetailNewBinding
    private var fragments = mutableListOf<androidx.fragment.app.Fragment>()
    lateinit var recentWorkout: StreamDataModel.Settings.Data.RecentWorkout
    var from = ""
    var exercisFra: OtherMediaExerciseFragment? = null
    private var isAdmin: String = ""
    var listFromLb = ArrayList<MyVideoList>()
    private var updateListInterface: UpdateListInterface? = null
    var tabPosition = -1
    private val preferExtensionDecodersMenuItem: MenuItem? = null
    var view_type: Int = 0
    lateinit var dialog: ErrorDownloadViewTypeDialog

    companion object {
        lateinit var streamDetailActivity: StreamDetailActivity

        var streamWorkoutId = ""
        var videoList = ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>()
        var otherMediaList = ArrayList<StreamDetailModel.Settings.Data.OtherMedia>()
        var overViewTrailerData: StreamWorkoutDetailModel.Settings.Data? = null
        var qualitymodel: StreamVideoQualityModel? = null
        var mediaName = ""
        var sInstance: RouteComparator? = null
        var isalloweduser: Boolean = false


    }

    var castLoaderData = ArrayList<MediaInfo>()
    private var mCastContext: CastContext? = null
    private var mCastSession: CastSession? = null
    private var mSessionManagerListener: SessionManagerListener<CastSession>? = null
    var isCastConnected = false

    // var openDialog:CustomChromeCastBottomSheetDialog?=null
    var openDialog: CustomChromeCastBottomSheetDialogNew? = null
    private var mRouter: MediaRouter? = null
    private var mCallback: MediaRouterCallback? = null
    private var mSelector = MediaRouteSelector.EMPTY
    var AllowUserList = ArrayList<String>()
    var downloadVideoList: ArrayList<VideoCategoryModal>? = ArrayList<VideoCategoryModal>()
    var play_pos: Int = 0
    var flag: Boolean = false
    fun turnOffToolbarScrolling() {
        //turn off scrolling
        val toolbarLayoutParams =
            binding.collapsingToolbar.layoutParams as AppBarLayout.LayoutParams
        toolbarLayoutParams.scrollFlags = 0
        binding.collapsingToolbar.layoutParams = toolbarLayoutParams

        val appBarLayoutParams = binding.appbar.layoutParams as CoordinatorLayout.LayoutParams
        appBarLayoutParams.behavior = null
        binding.appbar.layoutParams = appBarLayoutParams
    }

    fun turnOnToolbarScrolling() {
        val toolbarLayoutParams =
            binding.collapsingToolbar.layoutParams as AppBarLayout.LayoutParams
        toolbarLayoutParams.scrollFlags =
            AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
        binding.collapsingToolbar.layoutParams = toolbarLayoutParams

        val appBarLayoutParams = binding.appbar.layoutParams as CoordinatorLayout.LayoutParams
        appBarLayoutParams.behavior = AppBarLayout.Behavior()
        binding.appbar.layoutParams = appBarLayoutParams
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        streamDetailActivity = this
        hideNavigationBar()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_stream_detail_new)
        val windowBackground = window.decorView.background
        binding.transparentBlurView.setupWith(binding.containerView)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(this))
            .setBlurRadius(10f)
            .setHasFixedTransformationMatrix(true)

        inItView()
        //handleCastButton()
    }

    fun handleCastButton() {
        val fm: FragmentManager = supportFragmentManager
        val f: MediaRouteChooserDialogFragment =
            MediaRouteDialogFactory.getDefault().onCreateChooserDialogFragment()
        f.show(fm, "android.support.v7.mediarouter:MediaRouteChooserDialogFragment")
    }

    private fun inItView() {
        isAdmin = getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!
            if (intent.getStringExtra("download") != null && intent.getStringExtra("download")!! == "download"
        ) {
            streamWorkoutId =
                getDataManager().getUserStringData(AppPreferencesHelper.STEAM_WORKOUT_ID)!!
            getStreamWorkoutDetail(streamWorkoutId)
        } else {
            recentWorkout =
                intent.getSerializableExtra("data") as StreamDataModel.Settings.Data.RecentWorkout
            from = intent.getStringExtra("from")!!

            if (recentWorkout.stream_workout_image_url.isNotEmpty()) {
                Glide.with(getActivity())
                    .load("" + recentWorkout.stream_workout_image_url + recentWorkout.stream_workout_image)
                    .placeholder(R.color.colorBlack)
                    .into(binding.ivWorkout)
            }

            streamWorkoutId = recentWorkout.stream_workout_id
            getStreamWorkoutDetail(recentWorkout.stream_workout_id)
        }

        setOnClick(
            binding.ivBack,
            binding.ivFav,
            binding.ivShare,
            binding.playVideo,
            binding.lockImg,
            binding.chromeCast,binding.btnExclusive
        )
        // set Fragment as a list in viewPager adapter
        //chrome cast
        mCastContext = CastContext.getSharedInstance(getActivity())
        mCastSession = mCastContext?.sessionManager?.currentCastSession
        setupCastListener()
        mCastContext!!.sessionManager.addSessionManagerListener(
            mSessionManagerListener,
            CastSession::class.java
        )
        mRouter = MediaRouter.getInstance(getActivity())
        mCallback = MediaRouterCallback()
        mRouter!!.addCallback(
            mSelector,
            mCallback!!,
            MediaRouter.CALLBACK_FLAG_PERFORM_ACTIVE_SCAN
        )
        setRouteSelector(mCastContext!!.mergedSelector)

        if (mCastSession != null && mCastSession!!.isConnected) {
            isCastConnected = true
            Glide.with(getActivity()).load(R.drawable.ic_mr_button_connected_30_dark)
                .into(binding.chromeCast)
            binding.chromeCast.setColorFilter(
                ContextCompat.getColor(
                    getActivity(),
                    R.color.colorOrange1
                ), android.graphics.PorterDuff.Mode.MULTIPLY
            )

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
        //

        binding.planCategoryTablayout.setupWithViewPager(binding.planViewPager)
        if (from != null && !from.isEmpty() && from.equals("pinned")) {
            binding.planViewPager.currentItem = 1
        }
        binding.planCategoryTablayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                hideKeyboard()
                hideNavigationBar()

                if (otherMediaList != null && otherMediaList.size > 0) {
                    if (otherMediaList.get(0).type.equals("4")) {
                        exercisFra?.closeVideo()
                    }
                }
                tabPosition = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        videoList.clear()
    }

    fun setUpdateVideoListListener(updateListInterfaceData: UpdateListInterface) {
        updateListInterface = updateListInterfaceData
    }

    private fun setOnClick(vararg views: View) {
        for (view in views) {
            view.setOnClickListener(this)
        }
    }

    override fun onResume() {
        super.onResume()
        mCastContext = CastContext.getSharedInstance(getActivity())
        mCastSession = mCastContext?.sessionManager?.currentCastSession
        if (mCastSession != null && mCastSession!!.isConnected) {
            isCastConnected = true
            Glide.with(getActivity()).load(R.drawable.ic_mr_button_connected_30_dark)
                .into(binding.chromeCast)
            binding.chromeCast.setColorFilter(
                ContextCompat.getColor(
                    getActivity(),
                    R.color.colorOrange1
                ), android.graphics.PorterDuff.Mode.MULTIPLY
            )
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
                /* if (castLoaderData.size>0){
                    mSelectedMedia=castLoaderData.get(previusPos)
                 }

                 mCastSession = castSession
                 if (null != mSelectedMedia) {
                     loadRemoteMedia(0, true)
                 }
                 CastButtonFactory.setUpMediaRouteButton(applicationContext, binding.mediaRouteMenuItem1)
                 invalidateOptionsMenu()*/
            }

            private fun onApplicationDisconnected() {
                /*  CastButtonFactory.setUpMediaRouteButton(applicationContext, binding.mediaRouteMenuItem1)
                  invalidateOptionsMenu()*/

            }
        }
    }


    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.iv_back -> {
                onBackPressed()
            }

            R.id.chrome_cast -> {
                /*   val dialog =com.doviesfitness.chromecast.utils.MediaRouteChooserDialog(getActivity())
                    dialog.routeSelector = mCastContext!!.mergedSelector
                    dialog.show()*/


                //  var remoteClient1=    mCastContext?.sessionManager?.currentCastSession?.remoteMediaClient?.mediaStatus?.getQueueItem(0)?.customData
                //  var remoteClient=    mCastContext?.sessionManager?.currentCastSession?.

                /* var   mediaQueueItem = remoteClient?.getCurrentItem()
                    mediaQueueItem?.customData?.get(com.google.android.gms.cast.MediaMetadata.KEY_TITLE)
                    mediaInfo?.metadata?.getString(com.google.android.gms.cast.MediaMetadata.KEY_TITLE)
                    mediaInfo?.metadata?.keySet()
                    Log.d("metadata info","metadata info...info.."+ mediaInfo?.metadata?.getString(com.google.android.gms.cast.MediaMetadata.KEY_TITLE))
                    Log.d("metadata info","metadata info...subtitle.."+ mediaInfo?.metadata?.getString(com.google.android.gms.cast.MediaMetadata.KEY_SUBTITLE))
                    Log.d("metadata info","metadata info...key.."+  mediaInfo?.metadata?.keySet())
                    Log.d("metadata info","metadata info...item.."+ mediaQueueItem?.customData?.get(com.google.android.gms.cast.MediaMetadata.KEY_TITLE))*/

                val list = update()
                openDialog = CustomChromeCastBottomSheetDialogNew.newInstance(
                    this,
                    getActivity(),
                    isCastConnected,
                    list
                )
                //   openDialog?.setRouteSelector(mCastContext!!.mergedSelector)
                mCastContext = CastContext.getSharedInstance(getActivity())
                mCastSession = mCastContext?.sessionManager?.currentCastSession
                if (isCastConnected) {
                    var remoteClient =
                        mCastContext?.sessionManager?.currentCastSession?.remoteMediaClient

                    if (remoteClient?.mediaInfo != null && remoteClient.hasMediaSession()) {
                        Log.d(
                            "midea status",
                            "midea status..." + remoteClient.mediaStatus?.playerState + "..hassession.." + remoteClient.hasMediaSession() + "..status info.." + remoteClient.mediaStatus?.mediaInfo?.metadata?.getString(
                                com.google.android.gms.cast.MediaMetadata.KEY_TITLE
                            )
                        )


                        var mediaInfo = remoteClient.mediaInfo
                        var title =
                            mediaInfo?.metadata?.getString(com.google.android.gms.cast.MediaMetadata.KEY_TITLE)
                        var subtitle =
                            mediaInfo?.metadata?.getString(com.google.android.gms.cast.MediaMetadata.KEY_SUBTITLE)
                        var uri: Uri? = null
                        var isPlaying = mCastSession?.remoteMediaClient?.isPlaying

                        try {
                            if (mediaInfo!!.metadata.images[mediaInfo.metadata.images.size - 1].url != null)
                                uri =
                                    mediaInfo.metadata.images[mediaInfo.metadata.images.size - 1].url
                        } catch (exception: java.lang.Exception) {
                            exception.printStackTrace()
                        }
                        openDialog?.setDisconnectData(
                            mCastSession?.castDevice?.friendlyName,
                            title,
                            subtitle,
                            isPlaying,
                            uri
                        )

                    } else {
                        if (mCastSession?.castDevice?.friendlyName != null)
                            openDialog?.setDisconnectData(
                                mCastSession?.castDevice?.friendlyName,
                                "",
                                "",
                                null,
                                null
                            )

                    }

                }
                openDialog?.show(supportFragmentManager)
                update()

            }
            R.id.play_video -> {
                if (!"Yes".equals(isAdmin)) {
                    if ("LOCK" == overViewTrailerData?.stream_workout_access_level &&
                        "Exclusive" == overViewTrailerData?.access_level
                    ) {
                        // exclusive_navigation("https://www.doviesfitness.com/")
                        val dialog = ExclusiveWorkoutDialog.newInstance(
                            "${overViewTrailerData?.program_redirect_url}",
                            "",
                            getString(R.string.error_download_type)
                        )
                        dialog.show(supportFragmentManager)

                    } else if ("LOCK" == overViewTrailerData?.stream_workout_access_level &&
                        "Paid" == overViewTrailerData?.access_level
                    ) {
                        val dialog = ExclusiveWorkoutDialog.newInstance(
                            "${overViewTrailerData?.program_redirect_url}",
                            "",
                            getString(R.string.error_download_type)
                        )
                        dialog.show(supportFragmentManager)
                    } else if ("Yes" == isAdmin || getDataManager().getUserStringData(
                            AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED
                        )!! != "0" || "OPEN".equals(
                            overViewTrailerData?.stream_workout_access_level,
                            true
                        ) || isalloweduser
                    ) {
                        if (videoList.size > 0) {
                            Log.v("ListSize1", "" + videoList.size)
                            for (i in 0 until videoList.size) {
                                if (videoList.get(i) != null) {
                                    val MaxProgress =
                                        Constant.getExerciseTime(videoList.get(i).video_duration)
                                    videoList.get(i).MaxProgress = MaxProgress
                                    videoList.get(i).seekTo = 0
                                }
                            }

                            DownloadVideosUtil.getDownloadedVideo(Constants.DOWNLOADED_VIDEO)
                                ?.let { downloadedList ->
                                    if (downloadedList.size > 0) {
                                        for (i in 0..downloadedList.size - 1) {

                                            if (downloadedList.get(i).stream_workout_id.equals(
                                                    overViewTrailerData!!.stream_workout_id
                                                )
                                            ) {
                                                if (downloadedList.get(i).download_list != null && downloadedList.get(
                                                        i
                                                    ).download_list.size > 0
                                                ) {

                                                    for (j in 0..downloadedList.get(i).download_list.size - 1) {

                                                        for (k in 0..videoList.size - 1) {
                                                            if (downloadedList.get(i).download_list.get(
                                                                    j
                                                                ).stream_video_id.equals(
                                                                    videoList.get(
                                                                        k
                                                                    ).stream_video_id
                                                                )
                                                            ) {
                                                                videoList.get(k).downLoadUrl =
                                                                    downloadedList.get(i).download_list.get(
                                                                        j
                                                                    ).downLoadUrl
                                                                break
                                                            }
                                                        }
                                                    }
                                                }
                                                break
                                            }
                                        }
                                    }

                                }

//---------------------------Hemant Handel Play behalf of viewtype handeling--------------------------------
                            var st_viewtype: String = "0"
                            if (downloadVideoList != null) {
                                for (i in 0 until downloadVideoList!!.size) {
                                    for (k in 0 until downloadVideoList!!.get(i).download_list.size) {
                                        for (j in 0 until videoList.size) {
                                            if ((downloadVideoList!!.get(i).download_list.get(k).stream_video_id).equals(
                                                    videoList.get(j).stream_video_id
                                                )
                                            ) {


                                                videoList.get(j).Progress = 100
                                                break
                                            }

                                        }
                                    }
                                }

                            }
                            for (i in 0..videoList.size - 1) {
                                if (videoList.get(i).Progress == 100 || videoList.get(i).view_type.equals(
                                        "2"
                                    ) || videoList.get(i).view_type.equals("3")
                                ) {
                                    play_pos = i
                                    flag = true
                                    st_viewtype = "1"
                                    break
                                }
                            }
                            if (st_viewtype.equals("0")) {
                                binding.transparentLayout.visibility = View.VISIBLE
                                binding.transparentBlurView.visibility = View.VISIBLE
                                dialog = ErrorDownloadViewTypeDialog.newInstance(
                                    "",
                                    "",
                                    getString(R.string.error_download_type)
                                )
                                dialog.setListener(this)
                                dialog.show(supportFragmentManager)
                            }
                            //---------------------------------------------------------------------------------
                            else if (flag == true) {
                                ////
                                if (videoList != null && videoList.size > 0) {
                                    //  if (streamWorkoutId.equals("70") || streamWorkoutId.equals("71")) {
                                    if (qualitymodel?.settings?.data?.videoList!!.size > 0)
                                        loadSample(
                                            videoList,
                                            "" + recentWorkout.stream_workout_id,
                                            play_pos,
                                            qualitymodel?.settings?.data?.videoList
                                        )
                                    // }

                                }
                            } else {
                                binding.transparentLayout.visibility = View.VISIBLE
                                binding.transparentBlurView.visibility = View.VISIBLE
                                dialog = ErrorDownloadViewTypeDialog.newInstance(
                                    "",
                                    "",
                                    getString(R.string.error_download_type)
                                )
                                dialog.setListener(this)
                                dialog.show(supportFragmentManager)
                            }
                        }

                    } else {
                        var intent =
                            Intent(getActivity(), SubscriptionActivity::class.java).putExtra(
                                "home",
                                "no"
                            )
                                .putExtra("exercise", "yes")
                        startActivityForResult(intent, 2)
                    }
                }else{
                    if (videoList.size > 0) {
                        Log.v("ListSize1", "" + videoList.size)
                        for (i in 0 until videoList.size) {
                            if (videoList.get(i) != null) {
                                val MaxProgress =
                                    Constant.getExerciseTime(videoList.get(i).video_duration)
                                videoList.get(i).MaxProgress = MaxProgress
                                videoList.get(i).seekTo = 0
                            }
                        }

                        DownloadVideosUtil.getDownloadedVideo(Constants.DOWNLOADED_VIDEO)
                            ?.let { downloadedList ->
                                if (downloadedList.size > 0) {
                                    for (i in 0..downloadedList.size - 1) {

                                        if (downloadedList.get(i).stream_workout_id.equals(
                                                overViewTrailerData!!.stream_workout_id
                                            )
                                        ) {
                                            if (downloadedList.get(i).download_list != null && downloadedList.get(
                                                    i
                                                ).download_list.size > 0
                                            ) {

                                                for (j in 0..downloadedList.get(i).download_list.size - 1) {

                                                    for (k in 0..videoList.size - 1) {
                                                        if (downloadedList.get(i).download_list.get(
                                                                j
                                                            ).stream_video_id.equals(
                                                                videoList.get(
                                                                    k
                                                                ).stream_video_id
                                                            )
                                                        ) {
                                                            videoList.get(k).downLoadUrl =
                                                                downloadedList.get(i).download_list.get(
                                                                    j
                                                                ).downLoadUrl
                                                            break
                                                        }
                                                    }
                                                }
                                            }
                                            break
                                        }
                                    }
                                }

                            }

//---------------------------Hemant Handel Play behalf of viewtype handeling--------------------------------
                        var st_viewtype: String = "0"
                        if (downloadVideoList != null) {
                            for (i in 0 until downloadVideoList!!.size) {
                                for (k in 0 until downloadVideoList!!.get(i).download_list.size) {
                                    for (j in 0 until videoList.size) {
                                        if ((downloadVideoList!!.get(i).download_list.get(k).stream_video_id).equals(
                                                videoList.get(j).stream_video_id
                                            )
                                        ) {


                                            videoList.get(j).Progress = 100
                                            break
                                        }

                                    }
                                }
                            }

                        }
                        for (i in 0..videoList.size - 1) {
                            if (videoList.get(i).Progress == 100 || videoList.get(i).view_type.equals(
                                    "2"
                                ) || videoList.get(i).view_type.equals("3")
                            ) {
                                play_pos = i
                                flag = true
                                st_viewtype = "1"
                                break
                            }
                        }
                        if (st_viewtype.equals("0")) {
                            binding.transparentLayout.visibility = View.VISIBLE
                            binding.transparentBlurView.visibility = View.VISIBLE
                            dialog = ErrorDownloadViewTypeDialog.newInstance(
                                "",
                                "",
                                getString(R.string.error_download_type)
                            )
                            dialog.setListener(this)
                            dialog.show(supportFragmentManager)
                        }
                        //---------------------------------------------------------------------------------
                        else if (flag == true) {
                            ////
                            if (videoList != null && videoList.size > 0) {
                                //  if (streamWorkoutId.equals("70") || streamWorkoutId.equals("71")) {
                                if (qualitymodel?.settings?.data?.videoList!!.size > 0)
                                    loadSample(
                                        videoList,
                                        "" + recentWorkout.stream_workout_id,
                                        play_pos,
                                        qualitymodel?.settings?.data?.videoList
                                    )
                                // }

                            }
                        } else {
                            binding.transparentLayout.visibility = View.VISIBLE
                            binding.transparentBlurView.visibility = View.VISIBLE
                            dialog = ErrorDownloadViewTypeDialog.newInstance(
                                "",
                                "",
                                getString(R.string.error_download_type)
                            )
                            dialog.setListener(this)
                            dialog.show(supportFragmentManager)
                        }
                    }
                }
            }


            R.id.iv_share -> {
                binding.ivShare.isEnabled = false
                DownloadUtil.sharePost(
                    "" + overViewTrailerData!!.stream_workout_share_url,
                    getActivity()
                )
                Handler().postDelayed(Runnable { binding.ivShare.isEnabled = true }, 2000)
            }

            R.id.lock_img -> {
                if ("Paid".equals(overViewTrailerData?.access_level)) {
                    if (!getDataManager().getUserStringData(
                            AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED
                        )!!.equals("0") || isalloweduser == true
                    ) {
                        //Hide both images lock and dollor
                        binding.lockImg.visibility = View.GONE
                    } else {
                        //Show dollor image

                    }
                } else if ("Exclusive".equals(overViewTrailerData?.access_level)) {
                    //Show dollor image

                } else if ("Subscribers".equals(overViewTrailerData?.access_level)) {
                    //Show lock image
                    var intent =
                        Intent(getActivity(), SubscriptionActivity::class.java).putExtra("home", "no")
                            .putExtra("exercise", "yes")
                    startActivityForResult(intent, 2)
                } else {
                    //Show lock image
                    var intent =
                        Intent(getActivity(), SubscriptionActivity::class.java).putExtra("home", "no")
                            .putExtra("exercise", "yes")
                    startActivityForResult(intent, 2)

                }
            }

            R.id.iv_fav -> {
                if (overViewTrailerData?.is_favourite.equals("0")) {
                    Glide.with(getActivity()).load(R.drawable.ic_star_active).into(iv_fav)
                    favStream("1")
                } else {
                    Glide.with(getActivity()).load(R.drawable.ic_starinactivie).into(iv_fav)
                    favStream("0")
                }
            }


            R.id.btn_Exclusive->{
             //   exclusive_navigation("https://www.doviesfitness.com/")
                overViewTrailerData?.program_redirect_url?.let { exclusive_navigation(it) }
            /*    val dialog = overViewTrailerData?.program_redirect_url?.let {
                    ExclusiveWorkoutDialog.newInstance(
                        it,
                        "",
                        getString(R.string.error_download_type)
                    )
                }
                dialog?.show(supportFragmentManager)*/

            }
        }
    }

    private fun createDownloadList() {
        try {
            downloadVideoList!!.clear()
            downloadVideoList =
                DownloadVideosUtil.getDownloadedVideo(Constants.DOWNLOADED_VIDEO)
            Log.e("StreamLogHisstory-> ", "Workout History" + downloadVideoList!!.size)
        } catch (e: Exception) {
            e.message
        }
    }

    private fun downloadcheck(streamVideoId: String, pos: Int) {
        if (downloadVideoList != null) {
//            for (i in 0 until downloadVideoList!!.size) {
            for (i in 0 until downloadVideoList!!.size) {


//                                    if ((downloadVideoList!!.get(i).download_list.get(i).stream_video_id).equals(exerciseList.get(j).stream_video_id)) {
                for (k in 0 until downloadVideoList!!.get(i).download_list.size) {
                    if ((downloadVideoList!!.get(i).download_list.get(k).stream_video_id).equals(
                            streamVideoId
                        )
                    ) {
                        play_pos = pos
                        flag = true
                        break
                    }
//
                }
            }
//            }
        }
    }

    private class PlaylistHolder(title: String, mediaItems: List<MediaItem>) {
        val title: String
        val mediaItems: List<MediaItem>

        init {
            Assertions.checkArgument(!mediaItems.isEmpty())
            this.title = title
            this.mediaItems = Collections.unmodifiableList(
                ArrayList(mediaItems)
            )
        }
    }

    private fun loadSample(
        strList: ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>,
        workoutid: String,
        pos: Int,
        videoList: List<VideoListItem?>?
    ) {
//       var streamImage= "" + overViewTrailerData?.stream_workout_image_url+"medium/"+overViewTrailerData?.stream_workout_image
        var streamImage =
            "" + overViewTrailerData?.stream_workout_image_url + overViewTrailerData?.stream_workout_image
        val loaderTask: SampleListLoader = SampleListLoader(
            streamImage, this, strList, workoutid, pos,
            videoList, castLoaderData
        )
        loaderTask.execute("")
    }

    private fun isNonNullAndChecked(menuItem: MenuItem?): Boolean {
        return menuItem != null && menuItem.isChecked
    }

    private class SampleListLoader(
        streamImage: String,
        streamVideoFragmentNew: StreamDetailActivity,
        strList: ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>,
        workoutid: String,
        pos: Int,
        videoList: List<VideoListItem?>?,
        castLoaderData: ArrayList<MediaInfo>
    ) : AsyncTask<String?, Void?,
            PlaylistHolder?>() {
        private var sawError = false
        var streamVideoFragmentNew = streamVideoFragmentNew
        var strList = strList
        var workoutid = workoutid
        var pos = pos
        var streamImage = streamImage
        var videoList = videoList
        var castLoaderData = castLoaderData

        override fun doInBackground(vararg uris: String?): PlaylistHolder? {
            var result: PlaylistHolder? = null
            try {
                result = readEntry(videoList)
            } catch (e: Exception) {
                com.google.android.exoplayer2.util.Log.e(
                    "error",
                    "Error loading sample list: $e"
                )
                sawError = true
            } finally {
            }
            return result
        }

        override fun onPostExecute(result: PlaylistHolder?) {
            // onPlaylistGroups(result, sawError)
//            val intent = Intent(streamVideoFragmentNew.mContext, StreamPlayerActivity::class.java)
            val intent = Intent(
                streamVideoFragmentNew.getActivity(),
                StreamVideoPlayUrlActivityTemp::class.java
            )

            intent.putExtra(
                IntentUtil.PREFER_EXTENSION_DECODERS_EXTRA,
                streamVideoFragmentNew.isNonNullAndChecked(streamVideoFragmentNew.preferExtensionDecodersMenuItem)
            )
            intent.putExtra("videoList", strList)
            intent.putExtra("workout_id", "" + workoutid)
            intent.putExtra("local", "no")
            intent.putExtra("trailer", "no")
            intent.putExtra("media_name", "" + mediaName)
            intent.putExtra("castMedia", castLoaderData)
            intent.putExtra("name", "" + overViewTrailerData!!.stream_workout_name)
            intent.putExtra("position", pos)
            intent.putExtra("stream_image", "" + streamImage)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            IntentUtil.addToIntent(result!!.mediaItems, intent)
            streamVideoFragmentNew.startActivityForResult(intent, 50)
        }

        @Throws(IOException::class)
        private fun readEntry(videoList: List<VideoListItem?>?): PlaylistHolder {
            var mediaList = arrayListOf<MediaItem>()

            var vlist = arrayListOf<String>()
            vlist.add("https://d1n9vl26vbyc5s.cloudfront.net/stream_video/hls/6.095021958868067.m3u8")
            vlist.add("https://d1n9vl26vbyc5s.cloudfront.net/stream_video/hls/1.0349727709550933.m3u8")
            for (i in 0..videoList?.size!! - 1) {

                var uri: Uri? = null
                var extension: String? = null
                var title: String? = null
                val mediaItem = MediaItem.Builder()
                title = "Dovies video"
                uri = Uri.parse(videoList.get(i)?.hlsVideo?.vHlsMasterPlaylist)
                extension = "m3u8"
                //     uri = Uri.parse("https://www.youtube.com/api/manifest/dash/id/3aa39fa2cc27967f/source/youtube?as=fmp4_audio_clear,fmp4_sd_hd_clear&sparams=ip,ipbits,expire,source,id,as&ip=0.0.0.0&ipbits=0&expire=19000000000&signature=A2716F75795F5D2AF0E88962FFCD10DB79384F29.84308FF04844498CE6FBCE4731507882B8307798&key=ik0")
                //     extension = "mpd"

                /*
                                if (i == videoList.size) {
                                    uri =
                                        Uri.parse("https://www.youtube.com/api/manifest/dash/id/3aa39fa2cc27967f/source/youtube?as=fmp4_audio_clear,fmp4_sd_hd_clear&sparams=ip,ipbits,expire,source,id,as&ip=0.0.0.0&ipbits=0&expire=19000000000&signature=A2716F75795F5D2AF0E88962FFCD10DB79384F29.84308FF04844498CE6FBCE4731507882B8307798&key=ik0")
                                    extension = "mpd"
                                } else {
                                    uri = Uri.parse(videoList.get(i)?.hlsVideo?.vHlsMasterPlaylist)
                                    extension = "m3u8"
                                }
                */


                val adaptiveMimeType =
                    Util.getAdaptiveMimeTypeForContentType(
                        Util.inferContentType(
                            uri,
                            extension
                        )
                    )
                mediaItem
                    .setUri(uri)
                    .setVideoUrl("setVideoUrl")
                    .setMediaMetadata(
                        MediaMetadata.Builder().setTitle(title).build()
                    )
                    .setMimeType(adaptiveMimeType)

                mediaList.add(mediaItem.build())
            }
            //   return PlaylistHolder(title, listOf(mediaItem.build()))
            return PlaylistHolder("Dovies", mediaList)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2 && data != null) {
            getStreamWorkoutDetail(streamWorkoutId)
        } else if (requestCode == 50 && resultCode == Activity.RESULT_OK) {
            finish()
            startActivity(Intent(getActivity(), StreamLogHistoryActivity::class.java))

        } else {

        }

    }


    /*
        internal class GetTasks(
            UName: String,
            videoList: ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>,
            streamWorkoutId: String,
            activity: Activity
        ) : AsyncTask<Void, Void, List<LocalStream>>() {
            var WorkoutId = streamWorkoutId
            var VList = videoList
            val activity = activity
            var UserName = UName
            override fun doInBackground(vararg voids: Void): List<LocalStream> {
                val taskList =
                    DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().taskDao()
                        .getAllList(UserName, WorkoutId)
                //.getAll()

                Log.v("taskList", "" + taskList)
                return taskList
            }

            override fun onPostExecute(tasks: List<LocalStream>) {
                super.onPostExecute(tasks)
                if (tasks != null && tasks.size > 0) {
                    val t = tasks.get(0)
                    val strList =
                        GithubTypeConverters.stringToSomeObjectList(t.getWList()) as java.util.ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>

                    if (strList != null && strList.size > 0) {
                        val intent = Intent(activity, StreamVideoPlayLandscapeActivity::class.java)
                        intent.putExtra("videoList", strList)
                        intent.putExtra("workout_id", "" + WorkoutId)
                        intent.putExtra("local", "yes")
                        activity.startActivity(intent)
                    }
                } else {
                    val intent = Intent(activity, StreamVideoPlayLandscapeActivity::class.java)
                    intent.putExtra("videoList", VList)
                    intent.putExtra("workout_id", "" + WorkoutId)
                    intent.putExtra("local", "no")
                    activity.startActivity(intent)
                }
            }
        }
    */
//--------------------Api Details--------------------------------------------
    private fun getStreamWorkoutDetail(streamWorkoutId: String) {
        if (CommanUtils.isNetworkAvailable(getActivity())) {
            binding.rltvLoader.visibility = View.VISIBLE
            val params = HashMap<String, String>()
            params.put("workout_id", "" + streamWorkoutId)
            params.put(
                StringConstant.auth_customer_id,
                getDataManager().getUserInfo().customer_id
            )

            var customerType =
                getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_USER_TYPE)
            if (customerType != null && !customerType.isEmpty()) {
                params.put(StringConstant.auth_customer_subscription, customerType)
            } else {
                params.put(StringConstant.auth_customer_subscription, "free")
            }

            //  params.put("workout_id", "2" )
            val header = HashMap<String, String>()
            header.put("auth-token", getDataManager().getUserInfo().customer_auth_token)
            getDataManager().getStreamWorkoutDetail(params, header)
                ?.getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        binding.rltvLoader.visibility = View.GONE
                        binding.mainContent.visibility = View.VISIBLE
                        try {
                            Log.d("TAG", "response worlout Details...." + response!!.toString(4))
                            val jsonObject: JSONObject? =
                                response.getJSONObject(StringConstant.settings)
                            val success: String? = jsonObject?.getString(StringConstant.success)
                            val msg: String? = jsonObject?.getString(StringConstant.message)
                            if (success.equals("1")) {
                                val streamDetailModel = getDataManager().mGson?.fromJson(
                                    response.toString(),
                                    StreamWorkoutDetailModel::class.java

                                )
                                val detailModel = getDataManager().mGson?.fromJson(
                                    response.toString(),
                                    StreamDetailModel::class.java
                                )
                                //if (detailModel!!.settings.data.stream_workout_id.equals("71")||detailModel!!.settings.data.stream_workout_id.equals("70")){
                                qualitymodel = getDataManager().mGson?.fromJson(
                                    response.toString(),
                                    StreamVideoQualityModel::class.java
                                )

                                //   }
                                videoList.clear()
                                videoList.addAll(streamDetailModel!!.settings.data.video_list)

                                otherMediaList.clear()
                                for (i in 0..detailModel!!.settings.data.other_media_list.size - 1) {
                                    if (detailModel.settings.data.other_media_list.get(i) != null)
                                        otherMediaList.add(
                                            detailModel.settings.data.other_media_list.get(
                                                i
                                            )
                                        )
                                }


                                overViewTrailerData = streamDetailModel.settings.data
                                mediaName = overViewTrailerData!!.media_title_name
                                loadCastData()

                                if (overViewTrailerData?.stream_workout_image_url != null && !overViewTrailerData?.stream_workout_image_url?.isEmpty()!!) {
                                    Glide.with(getActivity())
//                                            .load("" + overViewTrailerData?.stream_workout_image_url+"medium/"+overViewTrailerData?.stream_workout_image)
                                        .load("" + overViewTrailerData?.stream_workout_image_url + "medium/" + overViewTrailerData?.stream_workout_image)
                                        .placeholder(R.color.colorBlack)
                                        .into(binding.ivWorkout)


                                }

                                //  getWorkVideoListUsingId(overViewTrailerData!!.stream_workout_id, videoList)

                                //  binding.workoutName.text = streamDetailModel.settings.data.stream_workout_name

                                var name =
                                    CommanUtils.capitaliseName(streamDetailModel.settings.data.stream_workout_name)
                                binding.workoutName.text = "" + name


                                //      binding.workoutDescription.text = streamDetailModel.settings.data.stream_workout_subtitle
                                var description =
                                    CommanUtils.capitaliseName(streamDetailModel.settings.data.stream_workout_subtitle)

                                binding.workoutDescription.text = "" + description

                                if (!this@StreamDetailActivity.isDestroyed) {
                                    if (overViewTrailerData?.is_favourite.equals("0")) {
                                        Glide.with(getActivity()).load(R.drawable.ic_starinactivie)
                                            .into(iv_fav)
                                    } else {
                                        Glide.with(getActivity()).load(R.drawable.ic_star_active)
                                            .into(iv_fav)
                                    }
                                }

                                var nameList = ArrayList<String>()

                                // fragments.add(StreamVideoFragmentNew())
                                fragments.add(TempSteamVideoFragmentNew())
                                fragments.add(StreamOverviewFragment())

                                nameList.add("" + overViewTrailerData!!.media_title_name)
                                nameList.add("Overview")

                                isCompleteDownload()
//---------------allow user-----------------------------------------------------------


                                val datajsonObject: JSONObject? = jsonObject?.optJSONObject("data")
                                val str = datajsonObject?.optString("allowed_users")
                                val lstValues: List<String> =
                                    str?.split(",")!!.map { it -> it.trim() }
                                lstValues.forEach { it ->
                                    Log.i("Values", "value=$it")
                                    AllowUserList.add(it)
                                    //Do Something
                                }

                                for (item in AllowUserList.indices) {
                                    println("marks[$item]: " + AllowUserList[item])
                                    val userInfoBean = Doviesfitness.getDataManager().getUserInfo()

                                    if (AllowUserList[item].equals(userInfoBean.customer_id)) {
                                        isalloweduser = true
                                        Log.e("allow User collection->", isalloweduser.toString())
                                        break
                                    } else {
                                        isalloweduser = false
                                    }
                                }

                                //---------------------------------------------------

                                /*Manage new work */
                                if (!"Yes".equals(isAdmin)){
                                    if ("LOCK".equals(
                                            overViewTrailerData?.stream_workout_access_level,
                                            true
                                        )
                                    ) {
                                        if ("Paid".equals(overViewTrailerData?.access_level)) {
                                            if (getDataManager().getUserStringData(
                                                    AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED
                                                )!! != "0" || isalloweduser == true
                                            ) {
                                                //Hide both images lock and dollor
                                                binding.lockImg.visibility = View.GONE
                                                binding.btnExclusive.visibility=View.GONE
                                            } else {
                                                //Show dollor image
                                                binding.lockImg.visibility = View.VISIBLE
                                                Glide.with(binding.lockImg).load(R.drawable.dollar_ico).into(binding.lockImg)
                                                binding.btnExclusive.visibility=View.VISIBLE
                                            }
                                        } else if ("Exclusive".equals(overViewTrailerData?.access_level)) {
                                            //Show dollor image
                                            binding.lockImg.visibility = View.VISIBLE
                                            Glide.with(binding.lockImg).load(R.drawable.dollar_ico).into(binding.lockImg)
                                            binding.btnExclusive.visibility=View.VISIBLE
                                        } else if ("Subscribers".equals(overViewTrailerData?.access_level)) {
                                            //Show lock image
                                            binding.lockImg.visibility = View.VISIBLE
                                            Glide.with(binding.lockImg).load(R.drawable.lock_ico_bg).into(binding.lockImg)
                                            binding.btnExclusive.visibility=View.GONE
                                        } else {
                                            //Show lock image
                                            binding.lockImg.visibility = View.VISIBLE
                                            Glide.with(binding.lockImg).load(R.drawable.lock_ico_bg).into(binding.lockImg)
                                            binding.btnExclusive.visibility=View.GONE
                                        }
                                    }



                                }else{
                                    binding.lockImg.visibility = View.GONE
                                    binding.btnExclusive.visibility=View.GONE
                                }



                                if (overViewTrailerData != null && overViewTrailerData!!.stream_workout_trailer != null && !overViewTrailerData!!.stream_workout_trailer.isEmpty()) {
                                    fragments.add(StreamTrailerFragment())
                                    nameList.add("Trailers")

                                } else {

                                }

                                if (otherMediaList != null && otherMediaList.size > 0) {
                                    if (otherMediaList.get(0).type.equals("4")) {
                                        exercisFra = OtherMediaExerciseFragment()
                                        fragments.add(exercisFra as OtherMediaExerciseFragment)

                                    } else
                                        fragments.add(OtherMediaFragment())

                                    nameList.add("" + overViewTrailerData!!.other_media_title_name)

                                }


                                if (fragments.size > 3) {
                                    binding.planCategoryTablayout.tabMode =
                                        TabLayout.MODE_SCROLLABLE
                                } else {
                                    binding.planCategoryTablayout.tabMode = TabLayout.MODE_FIXED

                                }

                                val showPlanCategoryAdapter =
                                    StreamPagerAdapter(supportFragmentManager, fragments, nameList)
                                binding.planViewPager.adapter = showPlanCategoryAdapter
                                binding.planViewPager.offscreenPageLimit = 3


                                //  val Vfrag = fragments[0] as StreamVideoFragmentNew
                                //  Vfrag.notifyList()

                                val Overviewfrag = fragments[1] as StreamOverviewFragment
                                Overviewfrag.setData(overViewTrailerData!!)


                                val trailerfrag = fragments[2] as StreamTrailerFragment
                                trailerfrag.setData(overViewTrailerData!!)


//                                    for (i in 0 until videoList.size) {
//                                        if (i == 0) {
//                                            videoList[i].view_type = 0
//                                        } else if (i % 2 == 0) {
//                                            videoList[i].view_type = 1
//                                        } else {
//                                            videoList[i].view_type = 2
//                                        }
//                                    }


                            } else {
                                finish()
                                Constant.showCustomToast(getActivity(), "" + msg)

                            }
                        } catch (ex: Exception) {
                            binding.rltvLoader.visibility = View.GONE
                            ex.printStackTrace()
                        }


                    }

                    override fun onError(anError: ANError) {
                        binding.rltvLoader.visibility = View.GONE
                        Constant.errorHandle(anError, getActivity())
                        Constant.showCustomToast(getActivity(), getString(R.string.something_wrong))
                    }
                })
        } else {
            binding.rltvLoader.visibility = View.GONE
            Constant.showInternetConnectionDialog(getActivity())
        }
    }

    private fun isCompleteDownload() {
        for (i in 0..videoList.size - 1) {
            val downloadLocalFile =
                com.doviesfitness.ui.bottom_tabbar.stream_tab.download_background.DownloadUtil.createDownloadLocalFile(
                    videoList.get(i).stream_video
                )

            if (downloadLocalFile != null) {
                val existLocalFileLength = downloadLocalFile.length()
                if (existLocalFileLength > 0) {
                    Log.d("Complete download", "Complete download activity...")

                    videoList.get(i).Progress = 100
                    videoList.get(i).MaxProgress = 100
                }
            }
//            for (i in 0 until videoList.size) {
//                if (i == 0) {
//                    videoList[i].view_type = 0
//                } else if (i % 2 == 0) {
//                    videoList[i].view_type = 1
//                } else {
//                    videoList[i].view_type = 2
//                }
//            }

        }

        val AList =
            com.doviesfitness.ui.bottom_tabbar.stream_tab.download_background.DownloadUtil.getData("video")
        if (AList != null && AList.size > 0) {
            for (i in 0..AList.size - 1) {
                if (AList[i].workout_id.equals(overViewTrailerData!!.stream_workout_id)) {
                    for (j in 0..videoList.size - 1) {
                        if (videoList.get(j).stream_video_id.equals(AList[i].stream_video_id)) {
                            videoList.get(j).isAddedToQueue = AList[i].isAddedQueue
                            break
                        }
                    }
                }
            }
        }
    }

    private fun forCheckDownLoadsVideo(
        localVideoList: List<MyVideoList>,
        videoList1: ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>
    ) {

        for (j in 0..localVideoList.size - 1) {
            val localDbId = localVideoList.get(j).stream_video_id
            val localListdata = localVideoList.get(j)
            for (i in 0..videoList1.size - 1) {
                val serverVideoId = videoList1.get(i).stream_video_id
                val serverStreamVideo = videoList1.get(i).stream_video
                if (localDbId.equals(serverVideoId)) {
                    var newVideoList = StreamWorkoutDetailModel.Settings.Data.Video(
                        stream_video = serverStreamVideo,
                        stream_video_description = localListdata.stream_video_description!!,
                        stream_video_id = localListdata.stream_video_id!!,
                        video_duration = localListdata.video_duration!!,
                        stream_video_image = localListdata.stream_video_image!!,
                        stream_video_image_url = localListdata.stream_video_image_url!!,
                        stream_video_name = localListdata.stream_video_name!!,
                        stream_video_subtitle = localListdata.stream_video_subtitle!!,
                        order = 1,
                        Progress = 0,
                        MaxProgress = 10,
                        seekTo = 0,
                        isPlaying = false,
                        isComplete = false,
                        isRestTime = false,
                        downLoadUrl = localListdata.downLoad_url!!,
                        timeStempUrl = localListdata.time_stemp_url!!,
                        hls_video = null,
                        mp4_video = null,
                        is_workout = localListdata.isworkout!!,
                        view_type = localListdata.view_type!!


                    )
                    videoList.set(i, newVideoList)
                }
            }
        }

        if (updateListInterface != null) {
            updateListInterface!!.setUpdateVideoList(videoList)
        }
    }

    fun setUpdateVideoList(): ArrayList<StreamWorkoutDetailModel.Settings.Data.Video> {
        return videoList
    }

    //get all workoutsvideo list
    fun getWorkVideoListUsingId(
        id: String,
        videoList: ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>
    ) {

        class GetAllWorkoutListtt :
            AsyncTask<Void, Void, List<MyVideoList>>() {
            override fun doInBackground(vararg voids: Void): List<MyVideoList> {
                val taskList =
                    DatabaseClient.getInstance(applicationContext).appDatabase.taskDao()
                        .getAllMyWorkOutListVideo(id)
                Log.v("ListActivity", "" + taskList)
                return taskList
            }

            override fun onPostExecute(taskList: List<MyVideoList>) {
                super.onPostExecute(taskList)
                listFromLb.addAll(taskList)
                forCheckDownLoadsVideo(taskList, videoList)
            }
        }
        GetAllWorkoutListtt().execute()
    }

    private fun favStream(action: String) {
        if (CommanUtils.isNetworkAvailable(getActivity())) {
            val params = HashMap<String, String>()
            params.put("stream_workout_id", "" + recentWorkout.stream_workout_id)
            params.put("action", action)
            val header = HashMap<String, String>()
            header.put("auth-token", getDataManager().getUserInfo().customer_auth_token)
            getDataManager().streamFavUnFav(params, header)
                ?.getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        Log.d("TAG", "response...." + response!!.toString(4))
                        val jsonObject: JSONObject? =
                            response.getJSONObject(StringConstant.settings)
                        val success: String? = jsonObject?.getString(StringConstant.success)
                        if (success.equals("1")) {
                            overViewTrailerData?.is_favourite = action
                        } else {

                            if (!this@StreamDetailActivity.isDestroyed) {
                                if (action.equals("1")) {
                                    Glide.with(getActivity()).load(R.drawable.ic_starinactivie)
                                        .into(iv_fav)
                                } else {
                                    Glide.with(getActivity()).load(R.drawable.ic_star_active)
                                        .into(iv_fav)
                                }
                            }
                        }


                    }

                    override fun onError(anError: ANError) {
                        Constant.errorHandle(anError, getActivity())
                        Constant.showCustomToast(getActivity(), getString(R.string.something_wrong))
                    }
                })
        } else {
            Constant.showInternetConnectionDialog(getActivity())
        }
    }

    //update videoListListener
    interface UpdateListInterface {
        fun setUpdateVideoList(videoList: ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>)
    }
    //////

    fun loadCastData() {
        supportLoaderManager.initLoader(0, null, this)

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
            //   openDialog?.refreshRoutes1(mRouter!!)
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<MediaInfo>> {
//           return VideoItemLoader(getActivity(),   qualitymodel?.settings?.data?.videoList,overViewTrailerData?.stream_workout_image_url+"medium/"+overViewTrailerData?.stream_workout_image)
        return VideoItemLoader(
            getActivity(),
            qualitymodel?.settings?.data?.videoList,
            overViewTrailerData?.stream_workout_image_url + overViewTrailerData?.stream_workout_image
        )
    }

    override fun onLoadFinished(loader: Loader<List<MediaInfo>>, data: List<MediaInfo>?) {
        Log.d("loader data...", "loader data..." + data?.size + "....data..." + data.toString())
        data?.let { castLoaderData.addAll(it) }
        Log.d(
            "loader data...",
            "loader data arraylist..." + castLoaderData.size + "....data..." + castLoaderData.toString()
        )

    }

    override fun onLoaderReset(loader: Loader<List<MediaInfo>>) {

    }

    override fun chromeCastListener(message: String) {
        if (message.equals("selected")) {
            isCastConnected = true
            Glide.with(applicationContext).load(R.drawable.ic_mr_button_connected_30_dark)
                .into(binding.chromeCast)
            binding.chromeCast.setColorFilter(
                ContextCompat.getColor(
                    getActivity(),
                    R.color.colorOrange1
                ), android.graphics.PorterDuff.Mode.MULTIPLY
            )
            openDialog?.dismiss()
        } else if (message.equals("disconnect")) {
            isCastConnected = false
            mCastContext!!.sessionManager.endCurrentSession(true)
            Glide.with(applicationContext).load(R.drawable.ic_mr_button_connecting_00_dark)
                .into(binding.chromeCast)
            binding.chromeCast.setColorFilter(
                ContextCompat.getColor(getActivity(), R.color.colorWhite),
                android.graphics.PorterDuff.Mode.MULTIPLY
            )

        } else if (message.equals("stop casting")) {
            mCastContext?.sessionManager?.currentCastSession?.remoteMediaClient?.stop()
        } else if (message.equals("play pause")) {
            if (mCastContext?.sessionManager?.currentCastSession?.remoteMediaClient?.isPlaying!!) {
                mCastContext?.sessionManager?.currentCastSession?.remoteMediaClient?.pause()
                openDialog?.setPlayPauseIcon("play")
            } else {
                mCastContext?.sessionManager?.currentCastSession?.remoteMediaClient?.play()
                openDialog?.setPlayPauseIcon("pause")
            }
            // openDialog?.dismiss()

        } else if (message.equals("start play")) {
            // loadRemoteMedia(0,true)
            val intent = Intent(getActivity(), ExpandedControlsActivity::class.java)
            startActivity(intent)
        } else if (message.equals("selectRoute")) {

        }
    }

    override fun setRouter(route: MediaRouter.RouteInfo) {
        mRouter?.selectRoute(route)
    }

    private fun loadRemoteMedia(position: Int, autoPlay: Boolean) {

        if (mCastSession == null) {
            return
        }
        val remoteMediaClient = mCastSession!!.remoteMediaClient ?: return
        remoteMediaClient.registerCallback(object : RemoteMediaClient.Callback() {
            override fun onStatusUpdated() {
                Log.d("expanded player", "expanded player")

                val intent = Intent(getActivity(), ExpandedControlsActivity::class.java)
                startActivity(intent)
                remoteMediaClient.unregisterCallback(this)
            }
        })
        remoteMediaClient.load(
            MediaLoadRequestData.Builder()
                .setMediaInfo(mCastContext?.sessionManager?.currentCastSession?.remoteMediaClient?.mediaInfo)
                .setAutoplay(autoPlay)
                .setCurrentTime(mCastContext?.sessionManager?.currentCastSession?.remoteMediaClient?.approximateStreamPosition!!)
                .build()
        )

    }

    private inner class MediaRouterCallback : MediaRouter.Callback() {
        override fun onRouteAdded(router: MediaRouter, info: MediaRouter.RouteInfo) {
            com.google.android.exoplayer2.util.Log.d("root selected", "root added")

            //  openDialog?.refreshRoutes1(mRouter!!)
        }

        override fun onRouteRemoved(router: MediaRouter, info: MediaRouter.RouteInfo) {
            com.google.android.exoplayer2.util.Log.d("root selected", "root Removed")

            // openDialog?.refreshRoutes1(mRouter!!)
        }

        override fun onRouteChanged(router: MediaRouter, info: MediaRouter.RouteInfo) {
            com.google.android.exoplayer2.util.Log.d("root selected", "root canged")

            //  openDialog?.refreshRoutes1(mRouter!!)
            update()
        }

        override fun onRouteSelected(router: MediaRouter, route: MediaRouter.RouteInfo) {
            com.google.android.exoplayer2.util.Log.d("root selected", "root selected")
            openDialog?.callBack?.chromeCastListener("selected")

        }
    }

    fun update(): ArrayList<MediaRouter.RouteInfo> {

        var temproutes = ArrayList<MediaRouter.RouteInfo>()
        val routes: List<MediaRouter.RouteInfo> = mRouter!!.routes
        val count = routes.size
        for (i in 0 until count) {
            val route = routes[i]
            if (onFilterRoute(route)) {
                temproutes.add(route)
                Log.d("root item", "root item..." + route.name)
                //  openDialog?.refreshRoutes2(temproutes)
                //  isitemAdded = true
            }
        }

        openDialog?.refreshRoutes2(temproutes)

        return temproutes

        // sort(sInstance)
    }

    open fun onFilterRoute(route: MediaRouter.RouteInfo): Boolean {
        return !route.isDefault && route.matchesSelector(mSelector)
    }

    inner class RouteComparator : Comparator<MediaRouter.RouteInfo?> {
        init {
            sInstance = RouteComparator()
        }


        override fun compare(lhs: MediaRouter.RouteInfo?, rhs: MediaRouter.RouteInfo?): Int {
            return lhs?.name?.compareTo(rhs!!.name)!!
        }
    }

    override fun isOk() {
        Log.e("Dissmiss call--->", "Call Ok")
        binding.transparentLayout.visibility = View.GONE
        binding.transparentBlurView.visibility = View.GONE
    }

    fun blureffect_view() {
        binding.transparentLayout.visibility = View.VISIBLE
        binding.transparentBlurView.visibility = View.VISIBLE
    }

    fun blureffect_gone() {
        binding.transparentLayout.visibility = View.GONE
        binding.transparentBlurView.visibility = View.GONE
    }

    private fun exclusive_navigation(url: String) {
        val uri = Uri.parse(url)
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        goToMarket.addFlags(
            Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        )
        ContextCompat.startActivity(this@StreamDetailActivity, goToMarket, null)


    }
}