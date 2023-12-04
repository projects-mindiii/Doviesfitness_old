package com.doviesfitness.chromecast.settings

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.*
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import androidx.mediarouter.media.MediaRouteSelector
import androidx.mediarouter.media.MediaRouter
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import com.doviesfitness.BuildConfig
import com.doviesfitness.Doviesfitness
import com.doviesfitness.R
import com.doviesfitness.allDialogs.ExclusiveWorkoutDialog
import com.doviesfitness.allDialogs.menu.ErrorDownloadViewTypeDialog
import com.doviesfitness.chromecast.browser.VideoItemLoader
import com.doviesfitness.chromecast.expandedcontrols.ExpandedControlsActivity
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.databinding.FragmentStreamCollectionNewBinding
import com.doviesfitness.subscription.SubscriptionActivity
import com.doviesfitness.subscription.SubscriptionModel
import com.doviesfitness.temp.DownloadVideosUtil
import com.doviesfitness.temp.TempDownloadStreamActivity
import com.doviesfitness.temp.modal.VideoCategoryModal
import com.doviesfitness.ui.authentication.signup.model.UserInfoModal
import com.doviesfitness.ui.base.BaseFragment
import com.doviesfitness.ui.bottom_tabbar.stream_tab.activities.*
import com.doviesfitness.ui.bottom_tabbar.stream_tab.adapters.DynamicStreamWorkoutAdapter
import com.doviesfitness.ui.bottom_tabbar.stream_tab.adapters.RecentWorkoutAdapter
import com.doviesfitness.ui.bottom_tabbar.stream_tab.adapters.StreamCollectionAdapter
import com.doviesfitness.ui.bottom_tabbar.stream_tab.adapters.popularStreamWorkoutAdapter
import com.doviesfitness.ui.bottom_tabbar.stream_tab.download_background.DownloadUtil
import com.doviesfitness.ui.bottom_tabbar.stream_tab.fragments.StreamCollectionDetailFragment
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.StreamDataModel
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.StreamVideoQualityModel
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.StreamWorkoutDetailModel
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.tv.TvHlsVideo
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.tv.VideoListItem
import com.doviesfitness.ui.multipleQuality.IntentUtil
import com.doviesfitness.ui.multipleQuality.StreamVideoPlayUrlActivityTemp
import com.doviesfitness.ui.room_db.LocalStreamVideoDataModal
import com.doviesfitness.utils.CommanUtils
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.Constants
import com.doviesfitness.utils.DownloadUtil.Companion.sharePost
import com.doviesfitness.utils.DownloadUtil.Companion.unregisterReceivers
import com.doviesfitness.utils.StringConstant
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
import com.google.gson.Gson
import eightbitlab.com.blurview.RenderScriptBlur
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class StreamCollectionNew : BaseFragment(), StreamCollectionAdapter.OnCollectionClick,
        popularStreamWorkoutAdapter.OnItemCLick, View.OnClickListener,
        DynamicStreamWorkoutAdapter.OnDynamicWorkoutCLick,
        LoaderManager.LoaderCallbacks<List<MediaInfo>>,
        CustomChromeCastBottomSheetDialogNew.ChromeCastListener, ErrorDownloadViewTypeDialog.IsOK {
    var exclusiveUrl="https://www.doviesfitness.com/"
    private var tvHlsVideo: TvHlsVideo? = null
    private var qualitymodel: StreamVideoQualityModel? = null
    private lateinit var binding: FragmentStreamCollectionNewBinding
    private lateinit var collectionAdapter: StreamCollectionAdapter
    private lateinit var popularWorkoutAdapter: popularStreamWorkoutAdapter;
    private lateinit var recentWorkoutAdapter: RecentWorkoutAdapter;
    private var recentWorkoutList = ArrayList<StreamDataModel.Settings.Data.RecentWorkout>()
    private var collectionList = ArrayList<StreamDataModel.Settings.Data.Collection>()
    private var popularWorkoutList =
            ArrayList<StreamDataModel.Settings.Data.PopularCollectionWorkouts.Workout>()
    private var pinedVideoList = ArrayList<StreamDataModel.Settings.Data.PinnedWorkout.Video>()
    private var videoUrlList = ArrayList<String>()
    private var mLastClickTime: Long = 0
    var shareUrl = ""
    var WorkoutId = ""
    var streamImage = ""
    var imag = ""
    var img_url = ""
    var streamWorkoutName = ""
    var media_title_name = ""
    private val preferExtensionDecodersMenuItem: MenuItem? = null
    var pinned_workout_access_level = ""
    var access_level = ""
    var castLoaderData = ArrayList<MediaInfo>()
    private var mCastContext: CastContext? = null
    private var mCastSession: CastSession? = null
    private var mSessionManagerListener: SessionManagerListener<CastSession>? = null
    var isCastConnected = false
    var openDialog: CustomChromeCastBottomSheetDialogNew? = null
    public var isalloweduser: Boolean = false
    var AllowUserList = java.util.ArrayList<String>()
    var downloadVideoList: ArrayList<VideoCategoryModal>? = ArrayList<VideoCategoryModal>()
    var play_pos: Int = 0;
    var flag: Boolean = false

    lateinit var dialog: ErrorDownloadViewTypeDialog
    val isAdmin = getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!
    companion object {
        var sInstance: RouteComparator? = null

    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_stream_collection_new,
                container,
                false
        )


        initialization()
        return binding.root
    }


    fun newInstance(module_id: String): StreamCollectionNew {
        val myFragment = StreamCollectionNew()
        val args = Bundle()
        args.putString("module_id", module_id)
        myFragment.setArguments(args)

        return myFragment
    }

    fun loadCastData() {
        loaderManager.initLoader(0, null, this)

    }

    override fun onResume() {
        super.onResume()
        mCastContext = CastContext.getSharedInstance(mContext)
        mCastSession = mCastContext?.sessionManager?.currentCastSession
        if (mCastSession != null && mCastSession!!.isConnected) {
            isCastConnected = true
            Glide.with(mContext).load(R.drawable.ic_mr_button_connected_30_dark)
                    .into(binding.chromeCast)
            binding.chromeCast.setColorFilter(
                    ContextCompat.getColor(
                            mContext,
                            R.color.colorOrange1
                    ), android.graphics.PorterDuff.Mode.MULTIPLY
            );

        } else {
            isCastConnected = false
            Glide.with(mContext).load(R.drawable.ic_mr_button_connecting_00_dark)
                    .into(binding.chromeCast)
            binding.chromeCast.setColorFilter(
                    ContextCompat.getColor(mContext, R.color.colorWhite),
                    android.graphics.PorterDuff.Mode.MULTIPLY
            );

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


    private fun initialization() {

        val windowBackground = activity!!.window.decorView.background
        binding.transparentBlurView.setupWith(binding.containerId)
                .setFrameClearDrawable(windowBackground)
                .setBlurAlgorithm(RenderScriptBlur(context!!))
                .setBlurRadius(10f)
                .setHasFixedTransformationMatrix(true)

        arguments?.let {
            val module_id = arguments!!.getString("module_id", "")
            if (!module_id.isEmpty()) {
                var intent = Intent(activity!!, StreamDetailActivity::class.java)
                var data = StreamDataModel.Settings.Data.RecentWorkout(
                        "", "", "", "", "",
                        module_id + "", "", "", "", "", "", "", ""
                )
                intent.putExtra("data", data)
                intent.putExtra("from", "no")
                startActivity(intent)
            }
        }
        //loaderManager.initLoader(0, null, this)
        mCastContext = CastContext.getSharedInstance(mContext)
        mCastSession = mCastContext?.sessionManager?.currentCastSession
        setupCastListener()
        mCastContext!!.sessionManager.addSessionManagerListener(
                mSessionManagerListener,
                CastSession::class.java
        )
        mRouter = MediaRouter.getInstance(mContext)
        mCallback = MediaRouterCallback()
        mRouter!!.addCallback(mSelector,
                mCallback!!,
                MediaRouter.CALLBACK_FLAG_PERFORM_ACTIVE_SCAN
        )
        setRouteSelector(mCastContext!!.mergedSelector)

        if (mCastSession != null && mCastSession!!.isConnected) {
            isCastConnected = true
            Glide.with(mContext).load(R.drawable.ic_mr_button_connected_30_dark)
                    .into(binding.chromeCast)
            binding.chromeCast.setColorFilter(
                    ContextCompat.getColor(
                            mContext,
                            R.color.colorOrange1
                    ), android.graphics.PorterDuff.Mode.MULTIPLY
            );

        } else {
            isCastConnected = false
            Glide.with(mContext).load(R.drawable.ic_mr_button_connecting_00_dark)
                    .into(binding.chromeCast)
            binding.chromeCast.setColorFilter(
                    ContextCompat.getColor(mContext, R.color.colorWhite),
                    android.graphics.PorterDuff.Mode.MULTIPLY
            );

        }



        popularWorkoutAdapter = popularStreamWorkoutAdapter(context!!, popularWorkoutList, this)
        recentWorkoutAdapter = RecentWorkoutAdapter(context!!, recentWorkoutList, this)
        collectionAdapter =
                StreamCollectionAdapter(binding.rvWorkout.context, collectionList, this, this)

        val gridLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(
                context,
                androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL,
                false
        )

        binding.rvFeatured.layoutManager = gridLayoutManager
//        val spacingInPixels = resources.getDimensionPixelSize(R.dimen._10sdp)
//        val spacingInPixels2 = resources.getDimensionPixelSize(R.dimen._2sdp)
//        binding.rvFeatured.addItemDecoration(
//            SpacesItemDecoration(
//                spacingInPixels,
//                spacingInPixels2
//            )
//        )
        binding.rvFeatured.adapter = popularWorkoutAdapter

        val recentLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(
                context,
                androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL,
                false
        )
        binding.rvRecent.layoutManager = recentLayoutManager
//        binding.rvRecent.addItemDecoration(SpacesItemDecoration(spacingInPixels, spacingInPixels2))
        binding.rvRecent.adapter = recentWorkoutAdapter

        var layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
                context,
                androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
                false
        )
//        val spacingInPixels1 = resources.getDimensionPixelSize(R.dimen._8sdp)
//        binding.rvWorkout.addItemDecoration(MySpacesItemDecoration(spacingInPixels1))
        binding.rvWorkout.layoutManager = layoutManager
        binding.rvWorkout.adapter = collectionAdapter

        getWorkoutData()

        binding.swipeRefresh.setOnRefreshListener {
            collectionList.clear()
            popularWorkoutList.clear()
            recentWorkoutList.clear()
            binding.txtAllWorkoutCollection.visibility = View.GONE
            binding.tvMostRecent.visibility = View.GONE
            binding.nextIcon1.visibility = View.GONE
            binding.firstCollectionName.visibility = View.GONE
            binding.nextIcon2.visibility = View.GONE
            binding.levelName.visibility = View.GONE

            popularWorkoutAdapter.notifyDataSetChanged()
            collectionAdapter.notifyDataSetChanged()
            recentWorkoutAdapter.notifyDataSetChanged()
            getUSerDetail()
            getSubscriptionStatus()
            getWorkoutData()
            createdownloadlist()
        }
        binding.favorite.setOnClickListener(this)
        binding.ivSearch.setOnClickListener(this)
        binding.download.setOnClickListener(this)
        binding.playVideo.setOnClickListener(this)
        binding.ivShare.setOnClickListener(this)
        binding.ivOverview.setOnClickListener(this)
        binding.logIcon.setOnClickListener(this)
        binding.chromeCast.setOnClickListener(this)


    }

    private fun showDownload() {
        if ("Yes".equals(getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!) ||
                !getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED)!!
                        .equals(
                                "0"
                        )
        ) {
            //show
            binding.download.visibility = View.VISIBLE
        } else {
            if (setData()) {
                //show
                binding.download.visibility = View.VISIBLE
            } else {
                //hide
                binding.download.visibility = View.GONE
            }
        }

    }

    override fun onItemCLick(pos: Int, str: String, workoutID: String) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return
        } else {
            mLastClickTime = SystemClock.elapsedRealtime()
        }

        val intent = Intent(mContext, StreamDetailActivity::class.java)

        if (str != null && !str.isEmpty() && str.equals("recent", true)) {
            intent.putExtra("data", recentWorkoutList.get(pos))
            intent.putExtra("from", str)
            startActivity(intent)
        } else {
            val data = StreamDataModel.Settings.Data.RecentWorkout(
                    popularWorkoutList[pos].access_level,
                    popularWorkoutList[pos].display_new_tag,
                    popularWorkoutList[pos].display_new_tag_text,
                    popularWorkoutList[pos].media_title_name,
                    popularWorkoutList[pos].stream_workout_access_level,
                    popularWorkoutList[pos].stream_workout_id,
                    popularWorkoutList[pos].stream_workout_image,
                    popularWorkoutList[pos].stream_workout_image_url,
                    popularWorkoutList[pos].stream_workout_name,
                    "",
                    popularWorkoutList[pos].stream_workout_subtitle,
                    popularWorkoutList[pos].subtitle_show_in_app,
                    popularWorkoutList[pos].title_show_in_app
            )
            intent.putExtra("data", data)
            intent.putExtra("from", str)
            startActivity(intent)
        }
    }

    fun setData(): Boolean {

        var isAlreadySubscribed = false

        if (DownloadUtil.getDownloadedData("downloaded") != null) {
            var downloadedList = DownloadUtil.getDownloadedData("downloaded")
            val taskList = ArrayList<LocalStreamVideoDataModal>()


            if (downloadedList.size > 0) {
                for (i in 0..downloadedList.size - 1) {
                    if (downloadedList.get(i).download_list != null && downloadedList.get(i).download_list.size > 0) {
                        var j = 0
                        while (j < downloadedList.get(i).download_list.size) {
                            //  downloadedList.get(i).download_list.get(j).isAddedQueue = false

                            var str =
                                    isAlreadyDownloaded(downloadedList.get(i).download_list.get(j).VideoUrl)
                            if (str.equals(downloadedList.get(i).download_list.get(j).downLoadUrl)) {
                                isAlreadySubscribed = true
                                break
                            }
                            j++
                        }
                    }
                }
            }
        }
        return isAlreadySubscribed
    }

    fun isAlreadyDownloaded(videoUrl: String): String {
        //check if user was subscribed
        var url = ""
        val customerName = getDataManager().getUserInfo().customer_user_name
        if (videoUrl != null && !TextUtils.isEmpty(videoUrl)) {
            val lastIndex = videoUrl.lastIndexOf("/")
            if (lastIndex > -1) {
                val downloadFileName = videoUrl.substring(lastIndex + 1)
                url = Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" +
                        BuildConfig.APPLICATION_ID + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + customerName + "//" + downloadFileName

            }
        }
        return url

    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.favorite -> {
                val intent = Intent(mContext, FavoriteStreamActivity::class.java)
                startActivity(intent)
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


                var list = update()
                openDialog = CustomChromeCastBottomSheetDialogNew.newInstance(this, mContext, isCastConnected, list)
                //   openDialog?.setRouteSelector(mCastContext!!.mergedSelector)
                mCastContext = CastContext.getSharedInstance(mContext)
                mCastSession = mCastContext?.sessionManager?.currentCastSession
                if (isCastConnected) {
                    var remoteClient = mCastContext?.sessionManager?.currentCastSession?.remoteMediaClient

                    if (remoteClient?.mediaInfo != null && remoteClient?.hasMediaSession()) {
                        Log.d("midea status", "midea status..." + remoteClient?.mediaStatus?.playerState + "..hassession.." + remoteClient?.hasMediaSession() + "..status info.." + remoteClient?.mediaStatus?.mediaInfo?.metadata?.getString(com.google.android.gms.cast.MediaMetadata.KEY_TITLE))


                        var mediaInfo = remoteClient?.mediaInfo
                        var title =
                                mediaInfo?.metadata?.getString(com.google.android.gms.cast.MediaMetadata.KEY_TITLE)
                        var subtitle =
                                mediaInfo?.metadata?.getString(com.google.android.gms.cast.MediaMetadata.KEY_SUBTITLE)
                        var uri: Uri? = null
                        var isPlaying = mCastSession?.remoteMediaClient?.isPlaying

                        try {
                            if (mediaInfo!!.metadata.images[mediaInfo!!.metadata.images.size - 1].url != null)
                                uri =
                                        mediaInfo!!.metadata.images[mediaInfo!!.metadata.images.size - 1].url
                        } catch (exception: java.lang.Exception) {
                            exception.printStackTrace()
                        }
                        openDialog?.setDisconnectData(mCastSession?.castDevice?.friendlyName, title, subtitle, isPlaying, uri)

                    } else {
                        if (mCastSession?.castDevice?.friendlyName != null)
                            openDialog?.setDisconnectData(mCastSession?.castDevice?.friendlyName, "", "", null, null)

                    }

                }
                openDialog?.show(childFragmentManager)
                update()

            }
            R.id.log_icon -> {
                val intent = Intent(mContext, StreamLogHistoryActivity::class.java)
                startActivity(intent)
            }
            R.id.download -> {

                if ("Yes".equals(getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!)) {
                    val intent = Intent(mContext, TempDownloadStreamActivity::class.java)
                    startActivity(intent)
                } else {

                    if (!getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED)!!
                                    .equals("0")
                    ) {
                        val intent = Intent(mContext, TempDownloadStreamActivity::class.java)
                        startActivity(intent)
                    } else {

                        if (setData()) {
                            val intent = Intent(mContext, TempDownloadStreamActivity::class.java)
                                    .putExtra("isSubscribe", "false")
                            startActivity(intent)
                        } else {

                            var intent =
                                    Intent(getActivity(), SubscriptionActivity::class.java).putExtra(
                                            "home",
                                            "no"
                                    )
                                            .putExtra("exercise", "yes")
                            startActivityForResult(intent, 2)
                        }
                    }
                }
            }
            R.id.iv_search -> {
                val intent = Intent(mContext, SearchStreamActivity::class.java)
                startActivity(intent)
            }
            R.id.iv_share -> {
                binding.ivShare.isEnabled = false
                sharePost(shareUrl, mContext)
                Handler().postDelayed(Runnable { binding.ivShare.isEnabled = true }, 2000)

            }
            R.id.iv_overview -> {
                val intent = Intent(mContext, StreamDetailActivity::class.java)
                var data = StreamDataModel.Settings.Data.RecentWorkout(
                        "", "", "", "", "", WorkoutId, imag, img_url,
                        "", "", "", "", ""
                )
                intent.putExtra("data", data)
                intent.putExtra("from", "pinned")
                startActivity(intent)
            }
            R.id.play_video -> {
                if (!"Yes".equals(isAdmin)){
                    if ("LOCK" == pinned_workout_access_level &&
                        "Exclusive" == access_level
                    ) {
                        // exclusive_navigation("https://www.doviesfitness.com/")
                        val dialog = ExclusiveWorkoutDialog.newInstance(
                            exclusiveUrl,
                            "",
                            getString(R.string.error_download_type)
                        )
                        dialog.show(childFragmentManager)

                    } else if ("LOCK" == pinned_workout_access_level &&
                        "Paid" == access_level
                    ) {
                        val dialog = ExclusiveWorkoutDialog.newInstance(
                            exclusiveUrl,
                            "",
                            getString(R.string.error_download_type)
                        )
                        dialog.show(childFragmentManager)
                    }else{
                        downloadVideoList?.clear()
                        downloadVideoList =
                            DownloadVideosUtil.getDownloadedVideo(Constants.DOWNLOADED_VIDEO)
                        if ("Yes".equals(getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!) ||
                            "OPEN".equals(pinned_workout_access_level, true) ||
                            !getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED)!!
                                .equals("0") || isalloweduser == true
                        ) {
                            if (pinedVideoList.size > 0) {
                                val exerciseList: ArrayList<StreamWorkoutDetailModel.Settings.Data.Video> =
                                    arrayListOf<StreamWorkoutDetailModel.Settings.Data.Video>()
                                exerciseList.clear()
                                for (i in 0..pinedVideoList.size - 1) {

                                    var MaxProgress =
                                        Constant.getExerciseTime(pinedVideoList.get(i).video_duration)

                                    var data = StreamWorkoutDetailModel.Settings.Data.Video(
                                        pinedVideoList.get(i).stream_video,
                                        pinedVideoList.get(i).stream_video_description,
                                        pinedVideoList.get(i).stream_video_id,
                                        pinedVideoList.get(i).video_duration,
                                        pinedVideoList.get(i).stream_video_image,
                                        pinedVideoList.get(i).stream_video_image_url,
                                        pinedVideoList.get(i).stream_video_name,
                                        pinedVideoList.get(i).stream_video_subtitle,
                                        0,

                                        MaxProgress,
                                        0,
                                        0,
                                        false,
                                        false,
                                        hls_video = pinedVideoList.get(i).hls_video,
                                        mp4_video = pinedVideoList.get(i).mp4_video,
                                        is_workout = pinedVideoList.get(i).is_workout,
                                        view_type = pinedVideoList.get(i).view_type
                                    )
                                    exerciseList.add(data)
                                }
                                DownloadVideosUtil.getDownloadedVideo(Constants.DOWNLOADED_VIDEO)
                                    ?.let { downloadedList ->
                                        if (downloadedList.size > 0) {
                                            for (i in 0..downloadedList.size - 1) {

                                                if (downloadedList.get(i).stream_workout_id.equals(WorkoutId)) {
                                                    if (downloadedList.get(i).download_list != null && downloadedList.get(
                                                            i
                                                        ).download_list.size > 0
                                                    ) {

                                                        for (j in 0..downloadedList.get(i).download_list.size - 1) {

                                                            for (k in 0..exerciseList.size - 1) {
                                                                if (downloadedList.get(i).download_list.get(
                                                                        j
                                                                    ).stream_video_id.equals(
                                                                        exerciseList.get(
                                                                            k
                                                                        ).stream_video_id
                                                                    )
                                                                ) {
                                                                    exerciseList.get(k).downLoadUrl =
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
                                var UName = getDataManager().getUserInfo().customer_user_name
                                var st_viewtype: String = "0"
                                flag = false
                                if (downloadVideoList != null) {
                                    for (i in 0 until downloadVideoList!!.size) {
                                        for (k in 0 until downloadVideoList!!.get(i).download_list!!.size) {
                                            for (j in 0 until exerciseList.size) {
                                                if ((downloadVideoList!!.get(i).download_list!!.get(k).stream_video_id).equals(exerciseList.get(j).stream_video_id)) {

                                                    exerciseList.get(j).Progress = 100
                                                    break
                                                }
                                            }
                                        }
                                    }

                                }
                                for (i in 0..exerciseList.size - 1) {

                                    if (exerciseList.get(i).Progress == 100 || exerciseList.get(i).view_type.equals("2") || exerciseList.get(i).view_type.equals("3")) {
                                        play_pos = i
                                        flag = true
                                        st_viewtype = "1"
                                        break
                                    }
                                }
                                if (st_viewtype.equals("0")) {
                                    binding.transparentLayout.visibility = View.VISIBLE
                                    binding.transparentBlurView.visibility = View.VISIBLE
                                    dialog = ErrorDownloadViewTypeDialog.newInstance("", "", getString(R.string.error_download_type))
                                    dialog.setListener(this)
                                    dialog.show(childFragmentManager)

                                }
                                else if (flag == true) {
                                    if (exerciseList != null && exerciseList.size > 0) {
                                        //   if (WorkoutId.equals("70") || WorkoutId.equals("71")) {
                                        if (tvHlsVideo?.settings?.data?.pinnedWorkout?.videoList!!.size > 0)
                                            loadSample(
                                                exerciseList, "" + WorkoutId, play_pos,
                                                tvHlsVideo?.settings?.data?.pinnedWorkout?.videoList!!, media_title_name
                                            )
                                        //  }
                                        else {

                                            val intent =
                                                Intent(activity, StreamVideoPlayUrlActivity::class.java)
                                            intent.putExtra("videoList", exerciseList)
                                            intent.putExtra("workout_id", "" + WorkoutId)
                                            intent.putExtra("local", "no")
                                            intent.putExtra("trailer", "no")
                                            intent.putExtra("media_name", "" + media_title_name)
                                            intent.putExtra("position", play_pos)
                                            startActivity(intent)

                                        }
                                    }
                                } else {
                                    binding.transparentLayout.visibility = View.VISIBLE
                                    binding.transparentBlurView.visibility = View.VISIBLE
                                    dialog = ErrorDownloadViewTypeDialog.newInstance("", "", getString(R.string.error_download_type))
                                    dialog.setListener(this)
                                    dialog.show(childFragmentManager)
                                }


                            }
                        } else {
                            var intent = Intent(getActivity(), SubscriptionActivity::class.java).putExtra(
                                "home",
                                "yes"
                            )
                                .putExtra("exercise", "yes")
                            startActivityForResult(intent, 2)
                        }
                    }
                }
                else{
                    downloadVideoList?.clear()
                    downloadVideoList =
                        DownloadVideosUtil.getDownloadedVideo(Constants.DOWNLOADED_VIDEO)
                    if ("Yes".equals(getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!) ||
                        "OPEN".equals(pinned_workout_access_level, true) ||
                        !getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED)!!
                            .equals("0") || isalloweduser == true
                    ) {
                        if (pinedVideoList.size > 0) {
                            val exerciseList: ArrayList<StreamWorkoutDetailModel.Settings.Data.Video> =
                                arrayListOf<StreamWorkoutDetailModel.Settings.Data.Video>()
                            exerciseList.clear()
                            for (i in 0..pinedVideoList.size - 1) {

                                var MaxProgress =
                                    Constant.getExerciseTime(pinedVideoList.get(i).video_duration)

                                var data = StreamWorkoutDetailModel.Settings.Data.Video(
                                    pinedVideoList.get(i).stream_video,
                                    pinedVideoList.get(i).stream_video_description,
                                    pinedVideoList.get(i).stream_video_id,
                                    pinedVideoList.get(i).video_duration,
                                    pinedVideoList.get(i).stream_video_image,
                                    pinedVideoList.get(i).stream_video_image_url,
                                    pinedVideoList.get(i).stream_video_name,
                                    pinedVideoList.get(i).stream_video_subtitle,
                                    0,

                                    MaxProgress,
                                    0,
                                    0,
                                    false,
                                    false,
                                    hls_video = pinedVideoList.get(i).hls_video,
                                    mp4_video = pinedVideoList.get(i).mp4_video,
                                    is_workout = pinedVideoList.get(i).is_workout,
                                    view_type = pinedVideoList.get(i).view_type
                                )
                                exerciseList.add(data)
                            }
                            DownloadVideosUtil.getDownloadedVideo(Constants.DOWNLOADED_VIDEO)
                                ?.let { downloadedList ->
                                    if (downloadedList.size > 0) {
                                        for (i in 0..downloadedList.size - 1) {

                                            if (downloadedList.get(i).stream_workout_id.equals(WorkoutId)) {
                                                if (downloadedList.get(i).download_list != null && downloadedList.get(
                                                        i
                                                    ).download_list.size > 0
                                                ) {

                                                    for (j in 0..downloadedList.get(i).download_list.size - 1) {

                                                        for (k in 0..exerciseList.size - 1) {
                                                            if (downloadedList.get(i).download_list.get(
                                                                    j
                                                                ).stream_video_id.equals(
                                                                    exerciseList.get(
                                                                        k
                                                                    ).stream_video_id
                                                                )
                                                            ) {
                                                                exerciseList.get(k).downLoadUrl =
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
                            var UName = getDataManager().getUserInfo().customer_user_name
                            var st_viewtype: String = "0"
                            flag = false
                            if (downloadVideoList != null) {
                                for (i in 0 until downloadVideoList!!.size) {
                                    for (k in 0 until downloadVideoList!!.get(i).download_list!!.size) {
                                        for (j in 0 until exerciseList.size) {
                                            if ((downloadVideoList!!.get(i).download_list!!.get(k).stream_video_id).equals(exerciseList.get(j).stream_video_id)) {

                                                exerciseList.get(j).Progress = 100
                                                break
                                            }
                                        }
                                    }
                                }

                            }
                            for (i in 0..exerciseList.size - 1) {

                                if (exerciseList.get(i).Progress == 100 || exerciseList.get(i).view_type.equals("2") || exerciseList.get(i).view_type.equals("3")) {
                                    play_pos = i
                                    flag = true
                                    st_viewtype = "1"
                                    break
                                }
                            }
                            if (st_viewtype.equals("0")) {
                                binding.transparentLayout.visibility = View.VISIBLE
                                binding.transparentBlurView.visibility = View.VISIBLE
                                dialog = ErrorDownloadViewTypeDialog.newInstance("", "", getString(R.string.error_download_type))
                                dialog.setListener(this)
                                dialog.show(childFragmentManager)

                            }
                            else if (flag == true) {
                                if (exerciseList != null && exerciseList.size > 0) {
                                    //   if (WorkoutId.equals("70") || WorkoutId.equals("71")) {
                                    if (tvHlsVideo?.settings?.data?.pinnedWorkout?.videoList!!.size > 0)
                                        loadSample(
                                            exerciseList, "" + WorkoutId, play_pos,
                                            tvHlsVideo?.settings?.data?.pinnedWorkout?.videoList!!, media_title_name
                                        )
                                    //  }
                                    else {

                                        val intent =
                                            Intent(activity, StreamVideoPlayUrlActivity::class.java)
                                        intent.putExtra("videoList", exerciseList)
                                        intent.putExtra("workout_id", "" + WorkoutId)
                                        intent.putExtra("local", "no")
                                        intent.putExtra("trailer", "no")
                                        intent.putExtra("media_name", "" + media_title_name)
                                        intent.putExtra("position", play_pos)
                                        startActivity(intent)

                                    }
                                }
                            } else {
                                binding.transparentLayout.visibility = View.VISIBLE
                                binding.transparentBlurView.visibility = View.VISIBLE
                                dialog = ErrorDownloadViewTypeDialog.newInstance("", "", getString(R.string.error_download_type))
                                dialog.setListener(this)
                                dialog.show(childFragmentManager)
                            }


                        }
                    } else {
                        var intent = Intent(getActivity(), SubscriptionActivity::class.java).putExtra(
                            "home",
                            "yes"
                        )
                            .putExtra("exercise", "yes")
                        startActivityForResult(intent, 2)
                    }
                }
            }
        }
    }

    private fun createdownloadlist() {


        try {
            downloadVideoList!!.clear()
            downloadVideoList =
                    DownloadVideosUtil.getDownloadedVideo(Constants.DOWNLOADED_VIDEO)
            Log.e("StreamLogHisstory-> ", "Workout History" + downloadVideoList!!.size)
        } catch (e: Exception) {
            e.message
        }
    }

    private fun downloadcheck(exerciseList: ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>) {
        if (downloadVideoList != null) {
            for (i in 0 until downloadVideoList!!.size) {
//                                    if ((downloadVideoList!!.get(i).download_list.get(i).stream_video_id).equals(exerciseList.get(j).stream_video_id)) {
                for (k in 0 until downloadVideoList!!.get(i).download_list!!.size) {
                    for (j in 0 until exerciseList.size) {
                        if ((downloadVideoList!!.get(i).download_list!!.get(k).stream_video_id).equals(exerciseList.get(j).stream_video_id)) {
//                            play_pos = j

                            exerciseList.get(j).Progress = 100
                            break
                        }
//
                    }
                }
            }

        }
    }


    private fun loadSample(
            strList: ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>,
            workoutid: String,
            pos: Int,
            videoList: MutableList<com.doviesfitness.ui.bottom_tabbar.stream_tab.model.tv.VideoListItem>,
            media_title_name: String
    ) {
        val loaderTask = SampleListLoader(
                streamWorkoutName, streamImage, this, strList, workoutid, pos,
                videoList, media_title_name, castLoaderData
        )
        loaderTask.execute("")
    }

    private fun isNonNullAndChecked(menuItem: MenuItem?): Boolean {
        return menuItem != null && menuItem.isChecked
    }

    private class PlaylistHolder(title: String, mediaItems: List<MediaItem>) {
        val title: String
        val mediaItems: List<MediaItem>

        init {
            Assertions.checkArgument(!mediaItems.isEmpty())
            this.title = title
            this.mediaItems = Collections.unmodifiableList(
                    java.util.ArrayList(mediaItems)
            )
        }
    }

    private class SampleListLoader(
            streamWorkoutName: String,
            streamImage: String,
            streamVideoFragmentNew: StreamCollectionNew,
            strList: ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>,
            workoutid: String,
            pos: Int,
            videoList: List<VideoListItem>?,
            media_title_name: String,
            castLoaderData: ArrayList<MediaInfo>
    ) : AsyncTask<String?, Void?,
            PlaylistHolder?>() {
        private var sawError = false
        var streamVideoFragmentNew = streamVideoFragmentNew
        var strList = strList
        var workoutid = workoutid
        var pos = pos
        var streamImage = streamImage
        var media_title_name = media_title_name
        var streamWorkoutName = streamWorkoutName
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
            intent.putExtra("media_name", "" + media_title_name)
            intent.putExtra("position", pos)
            intent.putExtra("name", "" + streamWorkoutName)
            intent.putExtra("castMedia", castLoaderData)
            intent.putExtra("stream_image", "" + streamImage)
            IntentUtil.addToIntent(result!!.mediaItems, intent)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            streamVideoFragmentNew.startActivityForResult(intent, 50)

        }

        @Throws(IOException::class)
        private fun readEntry(videoList: List<com.doviesfitness.ui.bottom_tabbar.stream_tab.model.tv.VideoListItem>?): PlaylistHolder {
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
                                Util.inferContentType(uri, extension)
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

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceivers(mContext)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 50 && resultCode == Activity.RESULT_OK) {
            startActivity(Intent(getActivity(), StreamLogHistoryActivity::class.java))
        } else {

        }

    }

    override fun onCollectionClick(data: StreamDataModel.Settings.Data.Collection, pos: Int) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return
        } else {
            mLastClickTime = SystemClock.elapsedRealtime()
        }

        val collectionDetailFragment = StreamCollectionDetailFragment()
        val args = Bundle()
        args.putSerializable("data", data)
        collectionDetailFragment.setArguments(args)
        getBaseActivity()?.addFragment(collectionDetailFragment, R.id.container_id1, true)
    }

    private fun getWorkoutData() {
        if (CommanUtils.isNetworkAvailable(mContext)) {
            val header = HashMap<String, String>()
            header.put("auth-token", getDataManager().getUserInfo().customer_auth_token)
            val param = HashMap<String, String>()

            param.put(
                    StringConstant.auth_customer_id,
                    getDataManager().getUserInfo().customer_id
            )

            var customerType =
                    getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_USER_TYPE)
            if (customerType != null && customerType!!.isNotEmpty()) {
                param[StringConstant.auth_customer_subscription] = customerType
            } else {
                param[StringConstant.auth_customer_subscription] = "free"
            }

            getDataManager().getStreamWorkOut(header, param)
                    ?.getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject?) {
                            Log.d("TdsfsdAG", "response worlout collection...." + response!!.toString(4))
                            pinedVideoList.clear()
                            try {
                                val jsonObject: JSONObject? =
                                        response?.getJSONObject(StringConstant.settings)
                                val success: String? = jsonObject?.getString(StringConstant.success)
                                val message: String? = jsonObject?.getString(StringConstant.message)
                                binding.swipeRefresh.isRefreshing = false
                                if (success.equals("1")) {
                                    binding.nestedSv.visibility = View.VISIBLE
                                    binding.txtNoDataFound.visibility = View.GONE
                                    val data: JSONObject? = jsonObject?.getJSONObject("data")
                                    //  binding.txtAllWorkoutCollection.visibility = View.VISIBLE

                                    val streamModel = getDataManager().mGson?.fromJson(
                                            response.toString(),
                                            StreamDataModel::class.java
                                    )
                                    tvHlsVideo = getDataManager().mGson?.fromJson(
                                            response.toString(),
                                            TvHlsVideo::class.java
                                    )

                                    loadCastData()
                                    if (streamModel!!.settings.data.pinned_workout.equals("")
                                            || streamModel!!.settings.data.pinned_workout.workout_video_count.equals(
                                                    "0"
                                            )
                                    ) {
                                        binding.rlPinned.visibility = View.GONE
                                        // binding.playLayout.visibility = View.GONE
                                        // binding.topHiddenView.visibility = View.VISIBLE
                                        binding.streamListLayout.setPadding(
                                                0,
                                                resources.getDimension(R.dimen._57sdp).toInt(),
                                                0,
                                                0
                                        )
                                        binding.topHiddenView.visibility=View.VISIBLE
                                    } else {
                                        binding.topHiddenView.visibility=View.GONE
                                        binding.streamListLayout.setPadding(0, 0, 0, 0)
                                        try {
                                            val pinedJObject = data!!.getJSONObject("pinned_workout")
                                            WorkoutId =
                                                    pinedJObject?.getString("stream_workout_id").toString()
                                            binding.workoutDescription.text =
                                                    pinedJObject?.getString("stream_workout_subtitle")
                                            streamWorkoutName =
                                                    pinedJObject?.getString("stream_workout_name")
                                                            .toString()

                                            var name =
                                                    pinedJObject?.getString("stream_workout_name")?.let {
                                                        CommanUtils.capitaliseName(
                                                                it
                                                        )
                                                    }

                                            binding.workoutName.text = "" + name

                                            streamImage =
//                                            pinedJObject?.getString("stream_workout_image_url") + "medium/" + pinedJObject?.getString(
                                                    pinedJObject?.getString("stream_workout_image_url") + pinedJObject?.getString(
                                                            "stream_workout_image"
                                                    )

                                            Glide.with(mContext).load(
//                                            pinedJObject?.getString("stream_workout_image_url") + "medium/" + pinedJObject?.getString(
                                                    pinedJObject?.getString("stream_workout_image_url") + pinedJObject?.getString(
                                                            "stream_workout_image"
                                                    )
                                            ).into(binding.ivPined)
                                            img_url =
                                                    pinedJObject?.getString("stream_workout_image_url")
                                                            .toString()
                                            imag =
                                                    pinedJObject?.getString("stream_workout_image")
                                                            .toString()

                                            pinedJObject?.getString("stream_workout_status")
                                            pinedJObject?.getString("display_new_tag")
                                            media_title_name =
                                                    pinedJObject?.getString("media_title_name").toString()
                                            shareUrl =
                                                    pinedJObject?.getString("stream_workout_share_url")
                                                            .toString()
                                            pinedJObject?.getString("is_favourite")
                                            pinedJObject?.getString("workout_video_count")
                                            pinned_workout_access_level =
                                                    pinedJObject?.getString("stream_workout_access_level")
                                                            .toString()

                                            exclusiveUrl= pinedJObject?.getString("program_redirect_url")
                                                .toString()

                                            access_level= pinedJObject?.getString("access_level")
                                                .toString()
//---------------allow user-----------------------------------------------------------
                                            var allowed_users = pinedJObject?.getString("allowed_users")

                                            val str = allowed_users
//
                                            val lstValues: List<String> = str?.split(",")!!.map { it -> it.trim() }
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
                                                }
                                            }

                                            //---------------------------------------------------

                                            var videosArray = pinedJObject?.getJSONArray("video_list")

                                            if (videosArray != null && videosArray!!.length() > 0) {
                                                for (i in 0..videosArray.length() - 1) {
                                                    var obj = videosArray.getJSONObject(i)
                                                    val hlsGson = Gson().fromJson(obj.getJSONObject("hls_video").toString(), StreamWorkoutDetailModel.Settings.Data.Video.HlsVideo::class.java)
                                                    val mp4VideoGson = Gson().fromJson(obj.getJSONObject("mp4_video").toString(), StreamWorkoutDetailModel.Settings.Data.Video.Mp4Video::class.java)
                                                    var hlsVideo = obj.getJSONObject("hls_video")
                                                    var id = obj.getString("stream_video_id")
                                                    var name = obj.getString("stream_video_name")
                                                    var subtitle =
                                                            obj.getString("stream_video_subtitle")
                                                    var des = obj.getString("stream_video_description")
                                                    var img = obj.getString("stream_video_image")
                                                    var isWorkout = obj.getString("is_workout")
                                                    var img_url =
                                                            obj.getString("stream_video_image_url")
                                                    var video = obj.getString("stream_video")
                                                    var video_duration = obj.getString("video_duration")
                                                    var view_type = obj.getString("view_type")
                                                    videoUrlList.add(video)
                                                    pinedVideoList.add(
                                                            StreamDataModel.Settings.Data.PinnedWorkout.Video(
                                                                    1,
                                                                    video,
                                                                    des,
                                                                    id,
                                                                    img,
                                                                    img_url,
                                                                    name,
                                                                    subtitle,
                                                                    video_duration, isWorkout, view_type,
                                                                    hlsGson,
                                                                    mp4VideoGson

                                                            )
                                                    )
                                                }
                                            }

                                            if (WorkoutId.equals("71") || WorkoutId.equals("70")) {
                                                // qualitymodel = getDataManager().mGson?.fromJson(response.toString(), StreamVideoQualityModel::class.java)

                                            }


                                        } catch (e: Exception) {
                                        }
                                        binding.rlPinned.visibility = View.VISIBLE
                                         binding.topHiddenView.visibility = View.GONE
                                        streamModel!!.settings.data.pinned_workout
                                    }
                                    recentWorkoutList.addAll(streamModel!!.settings.data.recent_workouts)
                                     collectionList.addAll(streamModel!!.settings.data.collection_list)
                                     collectionAdapter.notifyDataSetChanged()
                                    recentWorkoutAdapter.notifyDataSetChanged()
                                    //  collectionInGridOrSquare()
                                    if (recentWorkoutList.size > 0) {
                                        binding.tvMostRecent.visibility = View.VISIBLE
                                        binding.nextIcon1.visibility = View.VISIBLE
                                        binding.rvRecent.visibility = View.VISIBLE
                                    } else {
                                        binding.tvMostRecent.visibility = View.GONE
                                        binding.nextIcon1.visibility = View.GONE
                                        binding.rvRecent.visibility = View.GONE
                                    }
                                    if (popularWorkoutList.size > 0) {
                                        //  binding.firstCollectionName.setText("Trainers Choice")
                                        //   binding.firstCollectionName.setText("" + streamModel.settings.data.popular_collection_workouts.stream_workout_collection_title)
                                        binding.firstCollectionName.visibility = View.VISIBLE
/*
                                  if (streamModel!!.settings.data.popular_collection_workouts.stream_workout_collection_subtitle!=null&&
                                     !streamModel!!.settings.data.popular_collection_workouts.stream_workout_collection_subtitle.isEmpty()){
                                      binding.levelName.visibility = View.VISIBLE
                                      binding.levelName.text=""+streamModel!!.settings.data.popular_collection_workouts.stream_workout_collection_subtitle
                                  }
*/
                                        //  binding.levelName.visibility = View.VISIBLE
                                        binding.nextIcon2.visibility = View.VISIBLE
                                        binding.rlFeatured.visibility = View.VISIBLE

                                        val params =
                                                binding.rlFeatured.getLayoutParams() as android.widget.LinearLayout.LayoutParams

                                        if (collectionList.size > 0) {
                                            params.setMargins(0, 0, 0, 0)
                                            binding.rlFeatured.setLayoutParams(params)
                                            //  binding.txtAllWorkoutCollection.visibility = View.VISIBLE

                                        } else {
                                            binding.txtAllWorkoutCollection.visibility = View.GONE
                                            params.setMargins(
                                                    0,
                                                    0,
                                                    0,
                                                    resources.getDimension(R.dimen._30sdp).toInt()
                                            )
                                            binding.rlFeatured.setLayoutParams(params)
                                        }

                                    } else {
                                        binding.firstCollectionName.visibility = View.GONE
                                        binding.nextIcon2.visibility = View.GONE
                                        binding.levelName.visibility = View.GONE
                                        binding.rlFeatured.visibility = View.GONE
                                    }
                                } else {
                                    binding.txtAllWorkoutCollection.visibility = View.GONE
                                    binding.nestedSv.visibility = View.VISIBLE
                                    binding.streamListLayout.visibility = View.GONE
                                    binding.rlPinned.visibility = View.GONE
                                    binding.actionBarHeader.visibility = View.VISIBLE
                                    binding.txtNoDataFound.visibility = View.VISIBLE
                                    // Constant.showCustomToast(mContext!!, getString(R.string.something_wrong))

                                }
                            } catch (ex: Exception) {
                                ex.printStackTrace()
                                binding.txtAllWorkoutCollection.visibility = View.GONE
                                Constant.showCustomToast(
                                        mContext!!,
                                        getString(R.string.something_wrong)
                                )
                            }
                        }

                        override fun onError(anError: ANError) {
                            binding.txtAllWorkoutCollection.visibility = View.GONE
                            Constant.errorHandle(anError, activity!!)
                            Constant.showCustomToast(mContext!!, getString(R.string.something_wrong))
                        }
                    })
        } else {
            binding.swipeRefresh.setRefreshing(false)
            Constant.showInternetConnectionDialog(mContext as Activity)
        }
    }

    class MySpacesItemDecoration(space: Int) :
            androidx.recyclerview.widget.RecyclerView.ItemDecoration() {
        private val space: Int

        init {
            this.space = space
        }

        override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: androidx.recyclerview.widget.RecyclerView,
                state: androidx.recyclerview.widget.RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view)
            if (position == parent.getAdapter()!!.getItemCount() - 1) {
                outRect.bottom = space * 6
            } else {
                outRect.bottom = 0
            }
        }
    }


    class SpacesItemDecoration(space: Int, spacingInPixels2: Int) :
            androidx.recyclerview.widget.RecyclerView.ItemDecoration() {
        private val space: Int
        private val spacingInPixels2: Int

        init {
            this.space = space
            this.spacingInPixels2 = spacingInPixels2
        }

        override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: androidx.recyclerview.widget.RecyclerView,
                state: androidx.recyclerview.widget.RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view)
            if (position == 0) {
                // outRect.left = space
                outRect.left = spacingInPixels2 + (space / 2)
            } else if (position == parent.getAdapter()!!.getItemCount() - 1) {
                //   outRect.right = space
                outRect.right = spacingInPixels2 + (space / 2)
                outRect.left = spacingInPixels2 + (space / 2)
            } else {
                outRect.left = spacingInPixels2 + (space / 2)
            }
        }
    }

    private fun getUSerDetail() {
        Log.v("getUSerDetail", "getUSerDetail")
        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        getDataManager().getUSerDetailAPi(header)
                ?.getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                        Log.v("response", "response.." + response)
                        val status = json?.get(StringConstant.success)
                        val msg = json?.get(StringConstant.message)
                        if (status!!.equals("1")) {

                            val userInfoModal = getDataManager().mGson?.fromJson(
                                    response.toString(),
                                    UserInfoModal::class.java
                            )

                            val parseUserData = userInfoModal!!.data.get(0)
                            val userInfoBean = getDataManager().getUserInfo()
                            userInfoBean.customer_full_name = parseUserData.customer_full_name
                            userInfoBean.customer_user_name = parseUserData.customer_user_name
                            userInfoBean.customer_profile_image = parseUserData.customer_profile_image
                            userInfoBean.customer_gender = parseUserData.customer_gender
                            userInfoBean.customer_email = parseUserData.customer_email
                            var previewStatus = parseUserData.workout_preview_status
                            getDataManager().setPreferanceStatus(
                                    AppPreferencesHelper.PREF_KEY_APP_PREVIEW_STATUS,
                                    previewStatus
                            )

                            userInfoBean.notification_status = ""
                            if (parseUserData.dob != null)
                                userInfoBean.dob = parseUserData.dob
                            else
                                userInfoBean.dob = ""
                            userInfoBean.customer_status = parseUserData.customer_status
                            userInfoBean.customer_height = parseUserData.customer_height
                            userInfoBean.customer_weight = parseUserData.customer_weight
                            getDataManager().setUserInfo(userInfoBean)
                            getDataManager().setUserStringData(
                                    AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_USER_TYPE,
                                    parseUserData.customer_user_type
                            )

                            Log.v(
                                    "response",
                                    "response customer_status.." + parseUserData.customer_status
                            )

                        } else {
                            Constant.showCustomToast(mContext, "" + msg)
                        }
                    }

                    override fun onError(anError: ANError) {
                        Constant.errorHandle(anError, mContext as Activity)
                    }
                })
    }

    private fun getSubscriptionStatus() {
        val param = HashMap<String, String>()
        param.put(StringConstant.device_token, getDataManager().getUserInfo().customer_auth_token)
        param.put(
                StringConstant.auth_customer_id,
                getDataManager().getUserInfo().customer_auth_token
        )
        param.put(StringConstant.device_id, "")
        param.put(StringConstant.device_type, StringConstant.Android)
        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        header.put(StringConstant.apiKey, "HBDEV")
        header.put(StringConstant.apiVersion, "1")
        getDataManager().getSubscriptionStatus(param, header)
                ?.getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {

                        val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                        val success: String? = jsonObject?.getString(StringConstant.success)
                        val message: String? = jsonObject?.getString(StringConstant.message)
                        if (success.equals("1")) {
                            val SubsResponse = getDataManager().mGson?.fromJson(
                                    response.toString(),
                                    SubscriptionModel::class.java
                            )

                            //aganin calling news feed for database Entry for database user
                            getDataManager().setUserStringData(
                                    AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED,
                                    SubsResponse!!.data.is_subscribed
                            )
                            getDataManager().setUserStringData(
                                    AppPreferencesHelper.PREF_KEY_APP_SUBSCRIPTION_TITLE,
                                    SubsResponse!!.data.title
                            )

                        } else {

                        }
                    }

                    override fun onError(anError: ANError) {
                        Constant.errorHandle(anError, getActivity())
                        //  Constant.showCustomToast(getActivity(), getString(R.string.something_wrong))
                    }
                })
    }

    override fun onDynamicWorkoutCLick(listPosition: Int, itemPosition: Int) {
        val intent = Intent(mContext, StreamDetailActivity::class.java)
        var item = collectionList.get(listPosition).workout_list.get(itemPosition)

        var data = StreamDataModel.Settings.Data.RecentWorkout(
                item.access_level,
                item.display_new_tag,
                item.display_new_tag_text,
                item.media_title_name,
                item.stream_workout_access_level,
                item.stream_workout_id,
                item.stream_workout_image,
                item.stream_workout_image_url,
                item.stream_workout_name,
                "",
                item.stream_workout_subtitle,
                item.subtitle_show_in_app,
                item.title_show_in_app
        )
        intent.putExtra("data", data)
        intent.putExtra("from", "")
        startActivity(intent)
    }

    //////////
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<MediaInfo>> {
        // return VideoItemLoader(activity, CATALOG_URL)
        return VideoItemLoader(activity, tvHlsVideo?.settings?.data?.pinnedWorkout, streamImage)
    }

    override fun onLoadFinished(loader: Loader<List<MediaInfo>>, data: List<MediaInfo>?) {
        Log.d("loader data...", "loader data..." + data?.size + "....data..." + data.toString())
        castLoaderData.clear()
        data?.let { castLoaderData.addAll(it) }
        Log.d(
                "loader data...",
                "loader data arraylist..." + castLoaderData?.size + "....data..." + castLoaderData.toString()
        )
        loaderManager.destroyLoader(0)
    }

    override fun onLoaderReset(loader: Loader<List<MediaInfo>>) {

    }

    override fun setRouter(route: MediaRouter.RouteInfo) {
        mRouter?.selectRoute(route)

    }

    override fun chromeCastListener(message: String) {
        if (message.equals("selected")) {
            isCastConnected = true
            Glide.with(mContext).load(R.drawable.ic_mr_button_connected_30_dark)
                    .into(binding.chromeCast)
            binding.chromeCast.setColorFilter(
                    ContextCompat.getColor(
                            mContext,
                            R.color.colorOrange1
                    ), android.graphics.PorterDuff.Mode.MULTIPLY
            )
            openDialog?.dismiss()
        } else if (message.equals("disconnect")) {
            isCastConnected = false
            mCastContext!!.sessionManager.endCurrentSession(true);
            Glide.with(mContext).load(R.drawable.ic_mr_button_connecting_00_dark)
                    .into(binding.chromeCast)
            binding.chromeCast.setColorFilter(
                    ContextCompat.getColor(mContext, R.color.colorWhite),
                    android.graphics.PorterDuff.Mode.MULTIPLY
            );

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

        // mSelectedMedia!!.metadata.addImage(WebImage(Uri.parse(streamImage)))

        //  mCastContext?.sessionManager?.currentCastSession?.remoteMediaClient?.
        //  Log.d("player duration", "player duration..."+ mCastContext?.sessionManager?.currentCastSession?.remoteMediaClient?.streamDuration)
        // Log.d("player position", "player position..."+ mCastContext?.sessionManager?.currentCastSession?.remoteMediaClient?.approximateStreamPosition)

        remoteMediaClient.load(
                MediaLoadRequestData.Builder()
                        .setMediaInfo(mCastContext?.sessionManager?.currentCastSession?.remoteMediaClient?.mediaInfo)
                        .setAutoplay(autoPlay)
                        .setCurrentTime(mCastContext?.sessionManager?.currentCastSession?.remoteMediaClient?.approximateStreamPosition!!).build()
        )

        /*     binding.ivPause.performClick()
             binding.ivPlay.visibility = View.VISIBLE
             binding.ivPause.visibility = View.GONE*/

    }

    private var mRouter: MediaRouter? = null
    private var mCallback: MediaRouterCallback? = null
    private var mSelector = MediaRouteSelector.EMPTY
    //  private var mAdapter: RouteAdapter? = null

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

    fun setRouteSelector(selector: MediaRouteSelector?) {
        requireNotNull(selector) { "selector must not be null" }
        if (mSelector != selector) {
            mSelector = selector
            //  if (mAttachedToWindow) {
            mCallback?.let { mRouter!!.removeCallback(it) }
            mCallback?.let {
                mRouter!!.addCallback(selector,
                        it, MediaRouter.CALLBACK_FLAG_PERFORM_ACTIVE_SCAN)
            }
            // }
            //   openDialog?.refreshRoutes1(mRouter!!)
        }
    }

    fun update(): ArrayList<MediaRouter.RouteInfo> {

        var temproutes = ArrayList<MediaRouter.RouteInfo>()
        val routes: List<MediaRouter.RouteInfo> = mRouter!!.getRoutes()
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

    public inner class RouteComparator : Comparator<MediaRouter.RouteInfo?> {
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


}