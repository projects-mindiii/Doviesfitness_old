package com.doviesfitness.ui.bottom_tabbar.home_tab.activity

import androidx.databinding.DataBindingUtil

import android.os.Bundle
import androidx.core.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.databinding.ActivityVideoDetailBinding
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.StringConstant
import org.json.JSONObject
import android.net.Uri
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Handler
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.widget.LinearLayout
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.*
import com.doviesfitness.utils.Constant.Companion.COMMENT_COUNT_CONSTANT
import com.doviesfitness.utils.StringConstant.Companion.authToken
import com.doviesfitness.utils.StringConstant.Companion.module_id
import com.doviesfitness.utils.StringConstant.Companion.module_name
import com.doviesfitness.utils.StringConstant.Companion.news_id
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_feed_details.et_message
import kotlinx.android.synthetic.main.activity_feed_details.tv_description
import kotlinx.android.synthetic.main.activity_video_detail.*
import org.json.JSONArray

class VideoDetailActivity : BaseActivity(), View.OnClickListener {
    private var fromHomeTab: String = ""
    lateinit var newsFeedDetail : FeedDetailResponce
    lateinit var featuredNews: FeaturedNews
    lateinit var binding: ActivityVideoDetailBinding

    private lateinit var player: SimpleExoPlayer
    private lateinit var mediaDataSourceFactory: DataSource.Factory
    private val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory()
    private val ivHideControllerButton: LinearLayout by lazy {
        findViewById<LinearLayout>(R.id.controller) }
    private var lastSeenTrackGroupArray: TrackGroupArray? = null
    private var currentWindow: Int = 0
    private var playbackPosition: Long = 0
    private var trackSelector: DefaultTrackSelector? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video_detail)
        initViews()
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

        playerView.setShutterBackgroundColor(ContextCompat.getColor(this,R.color.colorGray4))

        playerView.player = player

        playerView.requestFocus()
       // ivHideControllerButton.visibility = View.VISIBLE

       /* ivHideControllerButton.setOnClickListener {
            playerView.hideController() }*/
       // ivHideControllerButton.visibility = View.VISIBLE

        lastSeenTrackGroupArray = null

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

    public override fun onStart() {
        super.onStart()
        // if (Util.SDK_INT > 23) intializePlayer(newsFeedDetail.data.get_news_feed_detail.get(0).news_video)
    }

    public override fun onResume() {
        super.onResume()
    }

    public override fun onPause() {
        super.onPause()
     //   setVideoLoading(false)
        binding.progressLayout.visibility=View.GONE
        if (Util.SDK_INT <= 23) releasePlayer()
    }

    public override fun onStop() {
        super.onStop()

        if (Util.SDK_INT > 23) releasePlayer()
    }


    private fun initViews() {
        featuredNews = intent.getParcelableExtra<FeaturedNews>("PROGRAM_DETAIL")  !!
        if(intent.hasExtra("fromHomeScreen")){
            if(intent.getStringExtra("fromHomeScreen") != null){
                fromHomeTab  = intent.getStringExtra("fromHomeScreen")!!
            }

        }

        feedDetailApi()
        binding.ivBack.setOnClickListener(this)
        binding.ivFav.setOnClickListener(this)
        binding.ivHeart.setOnClickListener(this)
        binding.ivShare.setOnClickListener(this)
        binding.tvComments.setOnClickListener(this)
        binding.ivComment.setOnClickListener(this)
        binding.sendMsgButton.setOnClickListener(this)

      //  setVideoLoading(true)
        binding.progressLayout.visibility=View.VISIBLE
        surfView.visibility = View.GONE

        Glide.with(binding.ivProfile.context).load(featuredNews.news_creator_profile_image).into(binding.ivProfile)
        // Glide.with(binding.vvFeatured.context).load(featuredNews.news_image).into(binding.vvFeatured)


        binding.etMessage.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!s.toString().isEmpty()) {
                    binding.sendMsgButton.setImageDrawable(
                        ContextCompat.getDrawable(this@VideoDetailActivity, R.drawable.ic_send_black))
                } else {
                    binding.sendMsgButton.setImageDrawable(
                        ContextCompat.getDrawable(this@VideoDetailActivity, R.drawable.ic_send_gray))
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


    /*Feed detail api*/
    fun feedDetailApi() {
        val param = HashMap<String, String>()
        param.put(news_id, featuredNews.news_id)
        val header = HashMap<String, String>()
        header.put(authToken, getDataManager().getUserInfo().customer_auth_token)
        getDataManager().newsFeedDetailApi(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                  //  setVideoLoading(false)
                    binding.progressLayout.visibility=View.GONE
                    val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                    val success: String? = jsonObject?.getString(StringConstant.success)
                    val message: String? = jsonObject?.getString(StringConstant.message)
                    if (success.equals("1")) {
                        binding.mainLayout.visibility = View.VISIBLE
                         newsFeedDetail = getDataManager().mGson!!.fromJson(response.toString(), FeedDetailResponce::class.java)

                        setData()

                        binding.featuredData = newsFeedDetail.data

                    } else {
                      //  setVideoLoading(false)
                        binding.progressLayout.visibility=View.GONE
                        Constant.showCustomToast(this@VideoDetailActivity, "" + message)
                    }
                }

                override fun onError(anError: ANError) {
                   // setVideoLoading(false)
                    binding.progressLayout.visibility=View.GONE
                    Constant.errorHandle(anError, this@VideoDetailActivity)
                }

            })
    }

    private fun setData() {
        tv_description.text = changeFontFamily(
            newsFeedDetail.data.get_news_feed_detail.get(0).news_creator_name,
            newsFeedDetail.data.get_news_feed_detail.get(0).news_description, false
        )

        intializePlayer(newsFeedDetail.data.get_news_feed_detail.get(0).news_video)

        val commentData = newsFeedDetail.data.news_feed_comments
        /*Comment data show """"""""""
    * only two comment cell will be show
     * */
        if (newsFeedDetail.data.get_news_feed_detail.get(0).news_comment_allow.equals("1")) {
            if (commentData.size == 1) {
                binding.rlComment1.visibility = View.VISIBLE
                binding.rlComment2.visibility = View.GONE
                Glide.with(binding.ivProfile1.context).load(commentData.get(0).customer_profile_image)
                    .into(binding.ivProfile1)
                binding.tvUsername1.text =
                    changeFontFamily(commentData.get(0).customer_name, commentData.get(0).news_comment, true)

            }
            else if (commentData.size >= 2) {
                binding.rlComment2.visibility = View.VISIBLE
                binding.rlComment1.visibility = View.VISIBLE

                Glide.with(binding.ivProfile1.context).load(commentData.get(0).customer_profile_image)
                    .into(binding.ivProfile1)
                binding.tvUsername1.text =
                    changeFontFamily(commentData.get(0).customer_name, commentData.get(0).news_comment, true)

                Glide.with(binding.ivProfile2.context).load(commentData.get(1).customer_profile_image)
                    .into(binding.ivProfile2)
                binding.tvUsernam2.text =
                    changeFontFamily(commentData.get(1).customer_name, commentData.get(1).news_comment, true)
            }
            else {
                binding.rlComment1.visibility = View.GONE
                binding.rlComment2.visibility = View.GONE
            }

        }
        else {
            binding.rlComment1.visibility = View.GONE
            binding.rlComment2.visibility = View.GONE
        }


        if (newsFeedDetail.data.get_news_feed_detail.get(0).news_comment_allow.equals("1")) {
            binding.tvComments.visibility = View.VISIBLE
            if (newsFeedDetail.data.get_news_feed_detail.get(0).news_comment_count.equals("0")) {
               // binding.tvComments.text = getString(R.string.write_a_comment)
                binding.tvComments.text = ""
            } else {
                if (newsFeedDetail.data.get_news_feed_detail.get(0).news_comment_count.toInt()<=2) {
                    binding.tvComments.text ="";
                     //   "View " + newsFeedDetail.data.get_news_feed_detail.get(0).news_comment_count + " comment"
                } else binding.tvComments.text =
                    "View all " + newsFeedDetail.data.get_news_feed_detail.get(0).news_comment_count + " comments"
            }
        } else {
            binding.tvComments.visibility = View.GONE
        }
    }

    fun changeFontFamily(boldString: String, regularString: String, value : Boolean): SpannableStringBuilder {
        val builder = SpannableStringBuilder()
        val name = SpannableString(boldString)
        name.setSpan(StyleSpan(Typeface.BOLD), 0, name.length, 0)
        builder.append(name)
        builder.append(" ")
        val description = SpannableString(regularString)
        if (value) {
            description.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorGray3)),
                0,
                description.length,
                0
            )
        }
        builder.append(description)

        return builder
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_share -> {
                binding.ivShare.isEnabled=false
                sharePost(featuredNews.news_share_url)
                Handler().postDelayed(Runnable { binding.ivShare.isEnabled=true },2000)

            }
            R.id.iv_back -> {
                onBackPressed()
            }
            R.id.iv_heart -> {
                ivLikeOnclick()
            }
            R.id.iv_fav -> {
                ivFavOnclick()
            }
            R.id.tv_comments -> {
                commentActivity()
            }
            R.id.iv_comment -> {
                commentActivity()
            }
            R.id.send_msg_button ->{
                if(binding.etMessage.text.toString().trim().isEmpty()||binding.etMessage.text.toString().trim().length<1){

                }
                else {
                    Constant.hideSoftKeyBoard(this, et_message)
                    postCommentApi()
                }
            }
        }
    }

    private fun commentActivity() {
        val intent = Intent(this, CommentsActivity::class.java)
        intent.putExtra("newsId", featuredNews.news_id)
        intent.putExtra("position", 0)
        startActivityForResult(intent, COMMENT_COUNT_CONSTANT)
    }


    /*Lile API*/
    fun ivLikeOnclick() {
        val param = HashMap<String, String>()
        param.put(StringConstant.auth_customer_id, getDataManager().getUserInfo().customer_auth_token)
        param.put(module_name, StringConstant.newsFeed)
        param.put(module_id, featuredNews.news_id)
        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        getDataManager().likeUnlikeApi(param, header)?.getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                val success: String? = jsonObject?.getString(StringConstant.success)
                val message: String? = jsonObject?.getString(StringConstant.message)
                if (success.equals("1")) {
                    if (featuredNews.news_like_status.equals("0")) {
                        featuredNews.news_like_status = "1"
                        val likeCount = featuredNews.news_like_count.toInt() + 1
                        featuredNews.news_like_count = likeCount.toString()
                        binding.ivHeart.setImageDrawable(
                            ContextCompat.getDrawable(
                                this@VideoDetailActivity,
                                R.drawable.ic_favorite_active
                            )
                        )
                    } else {
                        featuredNews.news_like_status = "0"
                        val likeCount = featuredNews.news_like_count.toInt() - 1
                        featuredNews.news_like_count = likeCount.toString()
                        binding.ivHeart.setImageDrawable(ContextCompat.getDrawable(
                                this@VideoDetailActivity,
                                R.drawable.ic_favorite
                            )
                        )
                    }

                    /*Check the customer Likes count and then according to change name*/
                    if (featuredNews.news_like_count.equals("0") || featuredNews.news_like_count.equals("1")) {
                        binding.tvLikes.text = featuredNews.news_like_count + " " + getString(R.string.like)
                    } else {
                        binding.tvLikes.text = featuredNews.news_like_count + " " + getString(R.string.likes)
                    }
                } else Constant.showCustomToast(this@VideoDetailActivity, "" + message)
            }

            override fun onError(anError: ANError) {
                Constant.errorHandle(anError, this@VideoDetailActivity)
            }
        })
    }


    /*Favrouit API*/
    fun ivFavOnclick() {
        val param = HashMap<String, String>()
        param.put(StringConstant.auth_customer_id, getDataManager().getUserInfo().customer_auth_token)
        param.put(module_name, StringConstant.newsFeed)
        param.put(module_id, featuredNews.news_id)
        val header = HashMap<String, String>()
        header.put(authToken, getDataManager().getUserInfo().customer_auth_token)
        getDataManager().makeFavoriteApi(param, header)?.getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {

                val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                val success: String? = jsonObject?.getString(StringConstant.success)
                val message: String? = jsonObject?.getString(StringConstant.message)
                if (success.equals("1")) {
                    if (featuredNews.news_fav_status.equals("0")) {
                        featuredNews.news_fav_status = "1"
                        binding.ivFav.setImageDrawable(
                            ContextCompat.getDrawable(
                                this@VideoDetailActivity, R.drawable.ic_star_active
                            )
                        )
                    } else {
                        featuredNews.news_fav_status = "0"
                        binding.ivFav.setImageDrawable(
                            ContextCompat.getDrawable(
                                this@VideoDetailActivity,
                                R.drawable.ic_star
                            )
                        )
                    }
                } else Constant.showCustomToast(this@VideoDetailActivity, "" + message)
            }

            override fun onError(anError: ANError) {
                Constant.errorHandle(anError, this@VideoDetailActivity)

                //Constant.showCustomToast(this@VideoDetailActivity, getString(R.string.something_wrong))
            }
        })
    }

    /*comment post*/
    private fun postCommentApi() {
        val param = HashMap<String, String>()
        param.put(StringConstant.auth_customer_id, getDataManager().getUserInfo().customer_auth_token)
        param.put(StringConstant.comment_text, binding.etMessage.text.toString().trim())
        param.put(news_id, featuredNews.news_id)
        val header = HashMap<String, String>()
        header.put(authToken,getDataManager().getUserInfo().customer_auth_token)
        getDataManager().postNewsFeedCommentApi(param, header)?.getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                try {
                    val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                    val status = json?.get(StringConstant.success)
                    val msg = json?.get(StringConstant.message)
                    if (status!!.equals("1")) {
                        val dataArray: JSONArray = response.getJSONArray(StringConstant.data)
                        val commentId = dataArray.getJSONObject(0).get("comment_id")
                        val commentCount : Int = newsFeedDetail.data.get_news_feed_detail.get(0).news_comment_count.toInt() + 1
                        newsFeedDetail.data.get_news_feed_detail.get(0).news_comment_count = commentCount.toString()
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
                        newsFeedDetail.data.news_feed_comments.add(0,coment)
                        setData()
                    } else {

                        Constant.showCustomToast(this@VideoDetailActivity, "" + msg)
                    }

                } catch (ex: Exception) {
                    Constant.showCustomToast(this@VideoDetailActivity, getString(com.doviesfitness.R.string.something_wrong))
                }
            }

            override fun onError(anError: ANError) {
                Constant.errorHandle(anError,this@VideoDetailActivity)
                //To change body of created functions use File | Settings | File Templates.
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        /*Get the comment count from the CommentsActivity*/
        if (requestCode == COMMENT_COUNT_CONSTANT && data != null) {
            binding.mainLayout.visibility = View.GONE
            feedDetailApi()
            val CommentCount = data.getIntExtra("CommentCount", 0)
            val position = data.getIntExtra("position", 0)
            newsFeedDetail.data.get_news_feed_detail.get(position).news_comment_count = CommentCount.toString()

            if (newsFeedDetail.data.get_news_feed_detail.get(0).news_comment_allow.equals("1")) {

                if (newsFeedDetail.data.get_news_feed_detail.get(0).news_comment_count.equals("0") || newsFeedDetail.data.get_news_feed_detail.get(0).news_comment_count.equals("1") || newsFeedDetail.data.get_news_feed_detail.get(0).news_comment_count.equals("2")) {
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

    override fun onDestroy() {
        super.onDestroy()

        if(!fromHomeTab.isEmpty()){
            if(fromHomeTab.equals("fromHomeScreen")){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.colorWhite))
                }
            }else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.splash_screen_color))
                }
            }
        }
    }
}
