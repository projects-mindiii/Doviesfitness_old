package com.doviesfitness.ui.bottom_tabbar.home_tab.activity

import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.databinding.ActivityFeedDetailsBinding
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.*
import com.doviesfitness.ui.profile.favourite.modal.FavFeedDataModal
import com.doviesfitness.ui.profile.inbox.modal.NotificationModel
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.Constant.Companion.COMMENT_COUNT_CONSTANT
import com.doviesfitness.utils.Constant.Companion.LIKE_COMMENT_COUNT_CONSTANT
import com.doviesfitness.utils.StringConstant
import com.doviesfitness.utils.StringConstant.Companion.authToken
import com.doviesfitness.utils.StringConstant.Companion.module_id
import com.doviesfitness.utils.StringConstant.Companion.module_name
import com.doviesfitness.utils.double_click.DoubleClick
import com.doviesfitness.utils.double_click.DoubleClickListener
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import eightbitlab.com.blurview.RenderScriptBlur
import kotlinx.android.synthetic.main.activity_feed_details.et_message
import kotlinx.android.synthetic.main.activity_feed_details.iv_heart
import kotlinx.android.synthetic.main.activity_feed_details.playerView
import kotlinx.android.synthetic.main.activity_feed_details.tv_description
import org.json.JSONArray
import org.json.JSONObject

class FeedDetailsActivity : BaseActivity(), View.OnClickListener {
    private lateinit var fromNotification: GetNewsFeedDetail
    private var nf_news_feed_id: String = ""
    private var fromNotificationList: Boolean = false
    private lateinit var binding: ActivityFeedDetailsBinding
    private lateinit var featuredData: AllOtherThenFeatured
    private var listPosition = 0
    private lateinit var newsFeedDetail: FeedDetailResponce
    private lateinit var fromNOtificationModal: NotificationModel.Data

    private lateinit var player: SimpleExoPlayer
    private lateinit var mediaDataSourceFactory: DataSource.Factory
    private val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory()
    private val ivHideControllerButton: LinearLayout by lazy {
        findViewById<LinearLayout>(R.id.controller)
    }
    private var lastSeenTrackGroupArray: TrackGroupArray? = null
    private var currentWindow: Int = 0
    private var playbackPosition: Long = 0
    private var trackSelector: DefaultTrackSelector? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }

*/
     //   hideNavStatusBar()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(
                ContextCompat.getColor(
                    getActivity(),
                    R.color.colorBlack
                )
            )
        }

        val view = window.decorView
        view.systemUiVisibility =
            view.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_feed_details)
        initViewAndSetData()
    }
    fun hideNavStatusBar() {
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(
                ContextCompat.getColor(
                    getActivity(),
                    R.color.colorBlack
                )
            )
        }

        val view = window.decorView
        view.systemUiVisibility =
            view.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
    }


    /*Intialize Player*/
    fun intializePlayer(news_video: String) {

       // hideNavigationBar()
       val windowBackground = window.decorView.background
        binding.topBlurView.setupWith(binding.containerId)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(this))
            .setBlurRadius(25f)
            .setHasFixedTransformationMatrix(true)


        trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)
        mediaDataSourceFactory =
            DefaultDataSourceFactory(this, Util.getUserAgent(this, "mediaPlayerSample"))
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector!!)

        val mediaSource = ExtractorMediaSource.Factory(mediaDataSourceFactory)
            .createMediaSource(Uri.parse(news_video))

        with(player) {
            prepare(mediaSource, false, false)
            playWhenReady = true
        }


        playerView.setShutterBackgroundColor(ContextCompat.getColor(this, R.color.colorGray4))
        playerView.player = player
        playerView.requestFocus()
      //  ivHideControllerButton.visibility = View.VISIBLE

        //ivHideControllerButton.setOnClickListener { playerView.hideController() }
        lastSeenTrackGroupArray = null

    }


    private fun updateStartPosition() {

        try {
            with(player) {
                playbackPosition = currentPosition
                currentWindow = currentWindowIndex
                playWhenReady = playWhenReady
            }
        } catch (e: java.lang.Exception) {

        }
    }

    private fun releasePlayer() {
        try {
            updateStartPosition()
            player.release()
            trackSelector = null
        } catch (e: java.lang.Exception) {

        }

    }

    public override fun onStart() {
        super.onStart()
        // if (Util.SDK_INT > 23) intializePlayer(newsFeedDetail.data.get_news_feed_detail.get(0).news_video)
    }

    public override fun onResume() {
        super.onResume()
      //  hideNavStatusBar()
        // if (Util.SDK_INT <= 23) intializePlayer(newsFeedDetail.data.get_news_feed_detail.get(0).news_video)
    }

    public override fun onPause() {
        super.onPause()

        if (Util.SDK_INT <= 23) releasePlayer()
    }

    public override fun onStop() {
        super.onStop()

        if (Util.SDK_INT > 23) releasePlayer()
    }

    private fun notificationStatus(notificationId: String) {
        val param = HashMap<String, String>()
        param.put(
            "device_token",
            getDataManager().getUserStringData(AppPreferencesHelper.PREF_FIREBASE_TOKEN)!!
        )
        param.put(StringConstant.page_index, "1")
        param.put("notification_id", notificationId)
        param.put(StringConstant.device_id, "")
        param.put(StringConstant.device_type, StringConstant.Android)
        param.put(
            StringConstant.auth_customer_id,
            getDataManager().getUserInfo().customer_auth_token
        )

        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        header.put(StringConstant.apiKey, "HBDEV")
        header.put(StringConstant.apiVersion, "1")

        getDataManager().getNotificationList(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                    val success: String? = jsonObject?.getString(StringConstant.success)
                    val message: String? = jsonObject?.getString(StringConstant.message)

                    if (success.equals("1")) {
                        val notificationModel = getDataManager().mGson?.fromJson(
                            response.toString(),
                            NotificationModel::class.java
                        )
                        Log.v("customNotificationModel", "" + notificationModel)
                        if (notificationModel != null) {
                            setDataInCustomView(notificationModel)
                        }
                    }
                }

                override fun onError(anError: ANError) {
                    Constant.showCustomToast(
                        this@FeedDetailsActivity,
                        getString(R.string.something_wrong)
                    )
                }
            })
    }

    private fun setDataInCustomView(notificationModel: NotificationModel) {

        val fromNotificationList = notificationModel.data.get(0)

        //tv_description.text = changeFontFamily(featuredData.news_creator_name, fromNotificationList.news_description, false)
        //Glide.with(binding.ivFeed.context).load(fromNotificationList.notification_image).into(binding.ivFeed)
        Glide.with(binding.ivProfile.context).load(fromNotificationList.notification_image)
            .placeholder(R.drawable.app_icon)
            .into(binding.ivProfile)

        if (fromNotificationList.notification_type.equals("Video")) {
            binding.playerView.visibility = View.VISIBLE
            binding.ivFeed.visibility = View.GONE
            binding.tvVideo.visibility = View.VISIBLE
            //    setVideoLoading(true)
        } else {
            binding.playerView.visibility = View.GONE
            binding.ivFeed.visibility = View.VISIBLE
            binding.tvVideo.visibility = View.GONE
        }
    }


    private fun initViewAndSetData() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )

        binding.ivBack.setOnClickListener(this)
        binding.containerId.setOnClickListener(this)
        binding.ivHeart.setOnClickListener(this)
        binding.ivComment.setOnClickListener(this)
        binding.tvComments.setOnClickListener(this)
        binding.ivShare.setOnClickListener(this)
        binding.ivFav.setOnClickListener(this)
        binding.sendMsgButton.setOnClickListener(this)

        if (intent.hasExtra("FeaturedData")) {
            if (intent.getParcelableExtra<AllOtherThenFeatured>("FeaturedData")   != null) {
                featuredData = intent.getParcelableExtra<AllOtherThenFeatured>("FeaturedData")!!
                listPosition = intent.getIntExtra("Position", 0)

                setDataInFields(featuredData)
                feedDetailApi(featuredData.news_id)
            }
        }
        else if (intent.hasExtra("FeaturedDataFromFav")) {
            if (intent.getParcelableExtra<FavFeedDataModal.Data>("FeaturedDataFromFav")   != null) {
                val favFeedDataModal =
                    intent.getParcelableExtra<FavFeedDataModal.Data>("FeaturedDataFromFav")!!

                featuredData = AllOtherThenFeatured(
                    customer_likes = favFeedDataModal.customer_likes,
                    feed_comments_count = favFeedDataModal.feed_comments_count,
                    is_new = favFeedDataModal.is_new,
                    news_comment_allow = favFeedDataModal.news_comment_allow,
                    news_creator_name = favFeedDataModal.news_creator_name,
                    news_creator_profile_image = favFeedDataModal.news_creator_profile_image,
                    news_description = favFeedDataModal.news_description,
                    news_fav_status = favFeedDataModal.news_fav_status,
                    news_id = favFeedDataModal.news_id,
                    news_image = favFeedDataModal.news_image,
                    news_image_ratio = favFeedDataModal.news_image_ratio,
                    news_like_status = favFeedDataModal.news_like_status,
                    news_media_type = favFeedDataModal.news_media_type,
                    news_module_id = favFeedDataModal.news_module_id,
                    news_module_type = favFeedDataModal.news_module_type,
                    news_posted_date = favFeedDataModal.news_posted_date,
                    news_posted_days = favFeedDataModal.news_posted_days,
                    news_share_url = favFeedDataModal.news_share_url,
                    news_title = favFeedDataModal.news_title,
                    news_video = favFeedDataModal.news_video
                )

                setDataInFields(featuredData)

                listPosition = intent.getIntExtra("Position", 0)
                feedDetailApi(featuredData.news_id)
            }
        }
        else if (intent.hasExtra("module_id")) {
            fromNotificationList = true
            if (intent.getStringExtra("module_id") != null) {
                val module_id = intent.getStringExtra("module_id")
                feedDetailApi(module_id!!)
            }
        }
        else if (intent.hasExtra("FromNotifications")) {
            //when Come From NOtification List india this i used boolen velue india every condition and APi Also so remenber this otherwise you get issues
            fromNotificationList = true
            if (intent.getSerializableExtra("FromNotifications")   != null) {
                val notificationData =
                    intent.getSerializableExtra("FromNotifications")!! as NotificationModel.Data
                feedDetailApi(notificationData.notification_connection_id)
            }
        }

        binding.ivFeed.setOnClickListener(DoubleClick(object : DoubleClickListener {
            override fun onSingleClick(view: View) {
                //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDoubleClick(view: View) {

                if (fromNotificationList) {
                    if (!nf_news_feed_id.isEmpty()) {
                        ivLikeOnclick(nf_news_feed_id)
                    }
                } else if (featuredData.news_like_status.equals("0")) {
                    ivLikeOnclick(featuredData.news_id)
                }
            }
        }))


        binding.etMessage.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!s.toString().isEmpty()) {
                    binding.sendMsgButton.setImageDrawable(
                        ContextCompat.getDrawable(
                            this@FeedDetailsActivity,
                            R.drawable.black_them_send_ico
                        )
                    )
                } else {
                    binding.sendMsgButton.setImageDrawable(
                        ContextCompat.getDrawable(this@FeedDetailsActivity, R.drawable.new_graysend_ico)
                    )
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //To change body of created functions use File | Settings | File Templates.
            }
        })
    }


    private fun setDataInFields(featuredData: AllOtherThenFeatured) {
        tv_description.text =
            changeFontFamily(featuredData.news_creator_name, featuredData.news_description, false)
        Glide.with(binding.ivFeed.context).load(featuredData.news_image)
            .placeholder(R.drawable.black_bg).into(binding.ivFeed)
        Glide.with(binding.ivProfile.context).load(featuredData.news_creator_profile_image)
            .placeholder(R.drawable.user_img)
            .into(binding.ivProfile)

        binding.progressLayout.visibility = View.VISIBLE

        if (featuredData.news_media_type.equals("Video")) {
            binding.playerView.visibility = View.VISIBLE
            binding.ivFeed.visibility = View.GONE
            binding.tvVideo.visibility = View.VISIBLE
            //    setVideoLoading(true)
        } else {
            binding.playerView.visibility = View.GONE
            binding.ivFeed.visibility = View.VISIBLE
            binding.tvVideo.visibility = View.GONE
        }
    }

    private fun setDataInFieldsFromNOtification(data: Data3) {
        val featuredData = data.get_news_feed_detail.get(0)

        tv_description.text =
            changeFontFamily(featuredData.news_creator_name, featuredData.news_description, false)
        Glide.with(binding.ivFeed.context).load(featuredData.news_image)
            .placeholder(R.drawable.black_bg).into(binding.ivFeed)
        Glide.with(binding.ivProfile.context).load(featuredData.news_creator_profile_image)
            .placeholder(R.drawable.user_img)
            .into(binding.ivProfile)

        binding.progressLayout.visibility = View.GONE

        if (featuredData.news_content_type.equals("Video")) {
            binding.playerView.visibility = View.VISIBLE
            binding.ivFeed.visibility = View.GONE
            binding.tvVideo.visibility = View.VISIBLE
            //    setVideoLoading(true)
        } else {
            binding.playerView.visibility = View.GONE
            binding.ivFeed.visibility = View.VISIBLE
            binding.tvVideo.visibility = View.GONE
        }
    }

    /*Feed and Feature API*/
    fun feedDetailApi(news_id: String) {
        val param = HashMap<String, String>()
        param.put(StringConstant.news_id, news_id)
        val header = HashMap<String, String>()

        header.put(authToken, getDataManager().getUserInfo().customer_auth_token)
        getDataManager().newsFeedDetailApi(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    binding.mainLayout.visibility = View.VISIBLE
                    // setVideoLoading(false)
                    binding.progressLayout.visibility = View.GONE
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                    val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                    val success: String? = jsonObject?.getString(StringConstant.success)
                    val message: String? = jsonObject?.getString(StringConstant.message)
                    if (success.equals("1")) {
                        newsFeedDetail =
                            getDataManager().mGson!!.fromJson(
                                response.toString(),
                                FeedDetailResponce::class.java
                            )

                        setData(newsFeedDetail.data)

                        //When Come From notification List
                        if (fromNotificationList) {
                            setDataInFieldsFromNOtification(newsFeedDetail.data)
                           // updateNotificationStatus(newsFeedDetail.data.get_news_feed_detail.get(0).nf_news_feed_id)
                        }

                        binding.featuredData = newsFeedDetail.data

                    } else {
                        //setVideoLoading(false)
                        binding.progressLayout.visibility = View.GONE
                        Constant.showCustomToast(this@FeedDetailsActivity, "" + message)
                    }
                }

                override fun onError(anError: ANError) {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    //   setVideoLoading(false)
                    binding.progressLayout.visibility = View.GONE
                    Constant.errorHandle(anError, this@FeedDetailsActivity)
                }

            })
    }

    private fun setData(data: Data3) {
        val commentData = data.news_feed_comments
        val getNewFeedFromNotification = data.get_news_feed_detail

        fromNotification = data.get_news_feed_detail.get(0)
        nf_news_feed_id = data.get_news_feed_detail.get(0).nf_news_feed_id
        intializePlayer(data.get_news_feed_detail.get(0).news_video)

        /*Comment data show """"""""""
    * only two comment cell will be show
     * */
        if (data.get_news_feed_detail.get(0).news_comment_allow.equals("1")) {
            if (commentData.size == 1) {
                binding.rlComment1.visibility = View.VISIBLE
                binding.rlComment2.visibility = View.GONE
                Glide.with(binding.ivProfile1.context)
                    .load(commentData.get(0).customer_profile_image)
                    .placeholder(R.drawable.user_img)
                    .into(binding.ivProfile1)
                binding.tvUsername1.text =
                    changeFontFamily(
                        commentData.get(0).customer_name,
                        commentData.get(0).news_comment,
                        true
                    )

            } else if (commentData.size >= 2) {
                binding.rlComment2.visibility = View.VISIBLE
                binding.rlComment1.visibility = View.VISIBLE

                Glide.with(binding.ivProfile1.context)
                    .load(commentData.get(0).customer_profile_image)
                    .placeholder(R.drawable.user_img)
                    .into(binding.ivProfile1)
                binding.tvUsername1.text =
                    changeFontFamily(
                        commentData.get(0).customer_name,
                        commentData.get(0).news_comment,
                        true
                    )

                Glide.with(binding.ivProfile2.context)
                    .load(commentData.get(1).customer_profile_image)
                    .placeholder(R.drawable.user_img)
                    .into(binding.ivProfile2)
                binding.tvUsernam2.text =
                    changeFontFamily(
                        commentData.get(1).customer_name,
                        commentData.get(1).news_comment,
                        true
                    )
            } else {
                binding.rlComment1.visibility = View.GONE
                binding.rlComment2.visibility = View.GONE

            }
        } else {
            binding.rlComment1.visibility = View.GONE
            binding.rlComment2.visibility = View.GONE
        }


        if (fromNotificationList) {
            if (getNewFeedFromNotification.get(0).news_comment_allow.equals("1")) {
                binding.tvComments.visibility = View.VISIBLE
                if (getNewFeedFromNotification.get(0).news_comment_count.equals("0")) {
                    // binding.tvComments.text = getString(R.string.write_a_comment)
                    binding.tvComments.visibility = View.GONE;
                } else {
                    if (getNewFeedFromNotification.get(0).news_comment_count.toInt() <= 2) {
                        binding.tvComments.text = "";
                        // "View " + newsFeedDetail.data.get_news_feed_detail.get(0).news_comment_count + " comment"
                    } else binding.tvComments.text =
                        "View all " + getNewFeedFromNotification.get(0).news_comment_count + " comments"
                }
            } else {
                binding.tvComments.visibility = View.GONE
            }
        } else {
            //"""""""news feed comments""""""""""""//
            if (featuredData.news_comment_allow.equals("1")) {
                binding.tvComments.visibility = View.VISIBLE
                if (data.get_news_feed_detail.get(0).news_comment_count.equals("0")) {
                    // binding.tvComments.text = getString(R.string.write_a_comment)
                    binding.tvComments.visibility = View.GONE;
                } else {
                    if (data.get_news_feed_detail.get(0).news_comment_count.toInt() <= 2) {
                        binding.tvComments.text = "";
                        // "View " + newsFeedDetail.data.get_news_feed_detail.get(0).news_comment_count + " comment"
                    } else binding.tvComments.text =
                        "View all " + data.get_news_feed_detail.get(0).news_comment_count + " comments"
                }
            } else {
                binding.tvComments.visibility = View.GONE
            }
        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.container_id -> {
               // hideNavigationBar()
            }

            R.id.iv_back -> {
                onBackPressed()
            }
            R.id.iv_comment -> {
                tvCommentsOnclick()
            }
            R.id.tv_comments -> {
                tvCommentsOnclick()
            }
            R.id.iv_heart -> {

                if (fromNotificationList) {
                    if (!nf_news_feed_id.isEmpty()) {
                        ivLikeOnclick(nf_news_feed_id)
                    }
                } else {
                    ivLikeOnclick(featuredData.news_id)
                }
            }
            R.id.iv_share -> {
                binding.ivShare.isEnabled=true
                if (fromNotificationList) {

                    if (!fromNotification.news_share_url.isEmpty()) {
                        sharePost(fromNotification.news_share_url)
                    }
                } else {
                    sharePost(featuredData.news_share_url)
                }
                Handler().postDelayed(Runnable { binding.ivShare.isEnabled=true },2000)

            }
            R.id.iv_fav -> {

                if (fromNotificationList) {
                    if (!nf_news_feed_id.isEmpty()) {
                        ivFavOnclick(nf_news_feed_id)
                    }
                } else {
                    ivFavOnclick(featuredData.news_id)
                }


            }
            R.id.send_msg_button -> {
                if (binding.etMessage.text.toString().trim().isEmpty() || binding.etMessage.text.toString().trim().length < 1) {

                } else {
                    Constant.hideSoftKeyBoard(this, et_message)

                    if (fromNotificationList) {
                        if (!nf_news_feed_id.isEmpty()) {
                            postCommentApi(nf_news_feed_id)
                        }
                    } else {
                        postCommentApi(featuredData.news_id)
                    }

                }
            }
        }
    }

    override fun onBackPressed() {

        if (fromNotificationList) {
            super.onBackPressed()
        } else {
            val intent = Intent()
            intent.putExtra("FeaturedData", featuredData)
            intent.putExtra("position", listPosition)
            setResult(LIKE_COMMENT_COUNT_CONSTANT, intent)
            super.onBackPressed()
        }
        hideKeyboard()
    }

    fun changeFontFamily(
        boldString: String,
        regularString: String,
        value: Boolean
    ): SpannableStringBuilder {
        val builder = SpannableStringBuilder()
        val name = SpannableString(boldString)
        name.setSpan(StyleSpan(Typeface.BOLD), 0, name.length, 0)
        builder.append(name)
        builder.append(" ")
        val description = SpannableString(regularString)
        if (value) {
            description.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorGray5)),
                0,
                description.length,
                0
            )
        }
        builder.append(description)

        return builder
    }

    /*Favroit API*/
    fun ivFavOnclick(news_id: String) {
        val param = HashMap<String, String>()
        param.put(
            StringConstant.auth_customer_id,
            getDataManager().getUserInfo().customer_auth_token
        )
        param.put(module_name, StringConstant.newsFeed)
        param.put(module_id, news_id)
        val header = HashMap<String, String>()
        header.put(authToken, getDataManager().getUserInfo().customer_auth_token)
        getDataManager().makeFavoriteApi(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {

                    val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                    val success: String? = jsonObject?.getString(StringConstant.success)
                    val message: String? = jsonObject?.getString(StringConstant.message)
                    if (success.equals("1")) {

                        if (fromNotificationList) {

                            if (fromNotification.news_favourite_status.equals("0")) {
                                fromNotification.news_favourite_status = "1"
                                binding.ivFav.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        this@FeedDetailsActivity,
                                        R.drawable.ic_star_active
                                    )
                                )
                            } else {
                                fromNotification.news_favourite_status = "0"
                                binding.ivFav.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        this@FeedDetailsActivity,
                                        R.drawable.ic_star
                                    )
                                )
                            }

                        } else {

                            if (featuredData.news_fav_status.equals("0")) {
                                featuredData.news_fav_status = "1"
                                binding.ivFav.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        this@FeedDetailsActivity,
                                        R.drawable.ic_star_active
                                    )
                                )
                            } else {
                                featuredData.news_fav_status = "0"
                                binding.ivFav.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        this@FeedDetailsActivity,
                                        R.drawable.ic_star
                                    )
                                )
                            }
                        }
                    } else Constant.showCustomToast(this@FeedDetailsActivity, "" + message)
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, this@FeedDetailsActivity)

                    //Constant.showCustomToast(this@FeedDetailsActivity, getString(R.string.something_wrong))
                }
            })
    }

    /*LIKE API*/
    fun ivLikeOnclick(new_id: String) {
        val param = HashMap<String, String>()
        param.put(
            StringConstant.auth_customer_id,
            getDataManager().getUserInfo().customer_auth_token
        )
        param.put(module_name, StringConstant.newsFeed)
        param.put(module_id, new_id)
        val header = HashMap<String, String>()
        header.put(authToken, getDataManager().getUserInfo().customer_auth_token)
        getDataManager().likeUnlikeApi(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                    val success: String? = jsonObject?.getString(StringConstant.success)
                    val message: String? = jsonObject?.getString(StringConstant.message)
                    if (success.equals("1")) {

                        // when come from notifcation it will get tru either false
                        if (fromNotificationList) {
                            if (fromNotification.news_like_status.equals("0")) {
                                fromNotification.news_like_status = "1"
                                val likeCount = fromNotification.news_like_count.toInt() + 1
                                fromNotification.news_like_count = likeCount.toString()
                                iv_heart.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        this@FeedDetailsActivity,
                                        R.drawable.ic_favorite_active
                                    )
                                )
                            } else {
                                fromNotification.news_like_status = "0"
                                val likeCount = fromNotification.news_like_count.toInt() - 1
                                fromNotification.news_like_count = likeCount.toString()
                                iv_heart.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        this@FeedDetailsActivity,
                                        R.drawable.ic_favorite
                                    )
                                )
                            }

                            /*Check the customer Likes count and then according to change name*/
                            if (fromNotification.news_like_count.equals("0") || fromNotification.news_like_count.equals(
                                    "1"
                                )
                            ) {
                                binding.tvLikes.text =
                                    fromNotification.news_like_count + " " + getString(R.string.like)
                            } else {
                                binding.tvLikes.text =
                                    fromNotification.news_like_count + " " + getString(R.string.likes)
                            }


                        } else {
                            if (featuredData.news_like_status.equals("0")) {
                                featuredData.news_like_status = "1"
                                val likeCount = featuredData.customer_likes.toInt() + 1
                                featuredData.customer_likes = likeCount.toString()
                                iv_heart.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        this@FeedDetailsActivity,
                                        R.drawable.ic_favorite_active
                                    )
                                )
                            } else {
                                featuredData.news_like_status = "0"
                                val likeCount = featuredData.customer_likes.toInt() - 1
                                featuredData.customer_likes = likeCount.toString()
                                iv_heart.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        this@FeedDetailsActivity,
                                        R.drawable.ic_favorite
                                    )
                                )
                            }

                            /*Check the customer Likes count and then according to change name*/
                            if (featuredData.customer_likes.equals("0") || featuredData.customer_likes.equals(
                                    "1"
                                )
                            ) {
                                binding.tvLikes.text =
                                    featuredData.customer_likes + " " + getString(R.string.like)
                            } else {
                                binding.tvLikes.text =
                                    featuredData.customer_likes + " " + getString(R.string.likes)
                            }
                        }

                    } else Constant.showCustomToast(this@FeedDetailsActivity, "" + message)
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, this@FeedDetailsActivity)

                    /// Constant.showCustomToast(this@FeedDetailsActivity, getString(R.string.something_wrong))
                }
            })
    }

    fun tvCommentsOnclick() {

        if (fromNotificationList) {
            val intent = Intent(this, CommentsActivity::class.java)
            intent.putExtra("fromNotionListNewsFeed", fromNotification)
            startActivity(intent)


        } else {
            val intent = Intent(this, CommentsActivity::class.java)
            intent.putExtra("newsId", featuredData.news_id)
            intent.putExtra("position", 0)
            startActivityForResult(intent, COMMENT_COUNT_CONSTANT)
        }
    }

    fun ivShareOnclick(featuredData: AllOtherThenFeatured) {
        sharePost(featuredData.news_share_url)
        //To change body of created functions use File | Settings | File Templates.
    }



    /*comment post*/
    private fun postCommentApi(news_id: String) {
        val param = HashMap<String, String>()
        param.put(
            StringConstant.auth_customer_id,
            getDataManager().getUserInfo().customer_auth_token
        )
        param.put(StringConstant.comment_text, binding.etMessage.text.toString().trim())
        param.put(StringConstant.news_id, news_id)
        val header = HashMap<String, String>()
        header.put(authToken, getDataManager().getUserInfo().customer_auth_token)
        getDataManager().postNewsFeedCommentApi(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    try {
                        val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                        val status = json?.get(StringConstant.success)
                        val msg = json?.get(StringConstant.message)
                        if (status!!.equals("1")) {
                            val dataArray: JSONArray = response.getJSONArray(StringConstant.data)
                            val commentId = dataArray.getJSONObject(0).get("comment_id")
                            val commentCount: Int =
                                newsFeedDetail.data.get_news_feed_detail.get(0).news_comment_count.toInt() + 1
                            newsFeedDetail.data.get_news_feed_detail.get(0).news_comment_count =
                                commentCount.toString()
                            val coment = NewsFeedComment(
                                "",
                                getDataManager().getUserInfo().customer_user_name
                                , getDataManager().getUserInfo().customer_profile_image
                                , et_message.text.toString().trim(),
                                "1",
                                "1 sec ago",
                                commentId.toString()
                            )
                            et_message.text = null
                            newsFeedDetail.data.news_feed_comments.add(0, coment)
                            setData(newsFeedDetail.data)
                        } else {

                            Constant.showCustomToast(this@FeedDetailsActivity, "" + msg)
                        }

                    } catch (ex: Exception) {
                        Constant.showCustomToast(
                            this@FeedDetailsActivity,
                            getString(com.doviesfitness.R.string.something_wrong)
                        )
                    }
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, this@FeedDetailsActivity)
                    //To change body of created functions use File | Settings | File Templates.
                }

            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        /*Get the comment count from the CommentsActivity*/
        if (requestCode == COMMENT_COUNT_CONSTANT && data != null) {
            feedDetailApi(featuredData.news_id)
            val CommentCount = data.getIntExtra("CommentCount", 0)
            val position = data.getIntExtra("position", 0)
            if (position < newsFeedDetail.data.get_news_feed_detail.size)
                newsFeedDetail.data.get_news_feed_detail.get(position).news_comment_count =
                    CommentCount.toString()

            if (newsFeedDetail.data.get_news_feed_detail.get(0).news_comment_allow.equals("1")) {

                if (newsFeedDetail.data.get_news_feed_detail.get(0).news_comment_count.equals("0") || newsFeedDetail.data.get_news_feed_detail.get(
                        0
                    ).news_comment_count.equals("1") || newsFeedDetail.data.get_news_feed_detail.get(
                        0
                    ).news_comment_count.equals(
                        "2"
                    )
                ) {
                    /* binding.tvComments.text =
                         "View " + newsFeedDetail.data.get_news_feed_detail.get(0).news_comment_count + " comment"*/
                    binding.tvComments.visibility = View.GONE
                } else {
                    binding.tvComments.text =
                        "View all " + newsFeedDetail.data.get_news_feed_detail.get(0).news_comment_count + " comments"
                    binding.tvComments.visibility = View.VISIBLE
                }
            } else {
                binding.tvComments.visibility = View.GONE
            }
        }
    }


}
