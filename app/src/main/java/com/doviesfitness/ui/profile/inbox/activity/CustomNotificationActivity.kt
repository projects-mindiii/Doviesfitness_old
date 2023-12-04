package com.doviesfitness.ui.profile.inbox.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.allDialogs.menu.DialogMenu
import com.doviesfitness.allDialogs.menu.ItemListDialogFragment
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.databinding.ActivityCustomNotificationBinding
import com.doviesfitness.databinding.CustomNotificationBinding
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.bottom_tabbar.home_tab.activity.FeedDetailsActivity
import com.doviesfitness.ui.bottom_tabbar.home_tab.activity.WorkOutDetailActivity
import com.doviesfitness.ui.bottom_tabbar.home_tab.activity.WorkOutPlanDetailActivity
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.Data1
import com.doviesfitness.ui.profile.inbox.addapter.NotificationCommentsAdapter
import com.doviesfitness.ui.profile.inbox.modal.NotificationDetailModel
import com.doviesfitness.ui.profile.inbox.modal.NotificationModel
import com.doviesfitness.utils.CommanUtils
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.StringConstant
import com.doviesfitness.utils.StringConstant.Companion.authToken
import com.r0adkll.slidr.Slidr
import kotlinx.android.synthetic.main.activity_comments.et_message
import kotlinx.android.synthetic.main.activity_comments.rv_comments
import kotlinx.android.synthetic.main.activity_feed_details.iv_heart
import org.json.JSONArray
import org.json.JSONObject

class CustomNotificationActivity : BaseActivity(), View.OnClickListener,
    NotificationCommentsAdapter.CommentOnClick,
    ItemListDialogFragment.DialogEventListener {

    private lateinit var params: CoordinatorLayout.LayoutParams
    private var likeStatus: String = "No"
    private var position: Int = 0
    private lateinit var ReportAndDeleteData: Data1
    private  var fromNOtificationModal: NotificationDetailModel.Data?=null
    private  var Modal: NotificationModel.Data?=null
    private lateinit var binding: CustomNotificationBinding
    private lateinit var commentsAdapter: NotificationCommentsAdapter
    lateinit var commentsList: ArrayList<Data1>
    private lateinit var menus: MutableList<DialogMenu>
    private lateinit var dialogFragment: ItemListDialogFragment
    var notificationModel:NotificationDetailModel?=null
    var NotificationCode=""
    var reference_type=""
   //private lateinit var customNotificationModel:CustomNotificationModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  hideNavigationBar()
        hideNavStatusBar()
        binding = DataBindingUtil.setContentView(this, R.layout.custom_notification)
         val toolbar = findViewById<Toolbar>(R.id.toolbar)
         setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        inItView()
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


    private fun inItView() {
        binding.progressLayout.visibility = View.VISIBLE
        Slidr.attach(this)
        setOnClick(
            binding.ivBackIcon,
            binding.tvReadAllComments,
            binding.sendMsgButton,
            binding.tvCommments,
            binding.ivHeart,
            binding.btnStatus,
            binding.ivComment,
            binding.completeView
        )
        commentsList = ArrayList()

        /*Set Adapter*/
        val userDataInfo = getDataManager().getUserInfo()

        commentsAdapter = NotificationCommentsAdapter(this, this.commentsList, this, userDataInfo, "NotFull")
        rv_comments.layoutManager = LinearLayoutManager(this)
        rv_comments.adapter = commentsAdapter

        if (intent.hasExtra("FromNotifications")) {
           // fromNOtificationModal = intent.getSerializableExtra("FromNotifications") as NotificationDetailModel.Data
            Modal = intent.getSerializableExtra("FromNotifications") as NotificationModel.Data
            NotificationCode=Modal!!.notification_code
            reference_type="notification"
            if (NotificationCode.equals("WelCome")){
                getNotificationDetail(Modal!!.notification_id,"welcome")
            }
            else {
                getNotificationDetail(Modal!!.notification_id,NotificationCode)
            }
          //  updateNotificationStatus(Modal!!.notification_id)
        }
        else if (intent.hasExtra("FromNotificationsFromBAckAndFore")) {
            val notification_id = intent.getStringExtra("FromNotificationsFromBAckAndFore")!!
             NotificationCode = intent.getStringExtra("Notification_code")!!
            reference_type="connection"
            if (NotificationCode.equals("WelCome")){
                getNotificationDetail(notification_id,"welcome")
            }
            else {
                getNotificationDetail(notification_id,NotificationCode)
            }

          //  updateNotificationStatus(notification_id)
        }

     //   getNotificationDetail(fromNOtificationModal.notification_id)

        /*  binding.ivCustomImage.setOnTouchListener(object:View.OnTouchListener{
              override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                  when(event!!.action){
                      MotionEvent.ACTION_DOWN->{

                      }
                      MotionEvent.ACTION_MOVE->{
                          val animZoomIn = AnimationUtils.loadAnimation(applicationContext, R.anim.custom_bounce_anim)
                          binding.completeView.startAnimation(animZoomIn)
                      }
                      MotionEvent.ACTION_UP->{

                      }
                  }
                  return true
              }

          })*/



        binding.etMessage.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!s.toString().isEmpty()) {
                    binding.sendMsgButton.setImageDrawable(
                        ContextCompat.getDrawable(
                            this@CustomNotificationActivity,
                            R.drawable.black_them_send_ico
                        )
                    )
                } else {
                    binding.sendMsgButton.setImageDrawable(
                        ContextCompat.getDrawable(
                            this@CustomNotificationActivity,
                            R.drawable.new_graysend_ico
                        )
                    )
                }
                hideNavStatusBar()
              //  hideNavigationBar()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //To change body of created functions use File | Settings | File Templates.
            }
        })


    }
//    vhjkhkjjkj

    private fun setOnClick(vararg views: View) {
        for (view in views) {
            view.setOnClickListener(this)
        }
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.iv_backIcon -> {
                onBackPressed()
            }

            R.id.complete_view -> {
                hideKeyboard()
            }

            R.id.iv_heart -> {
                if ("0".equals(notificationModel?.data?.get(0)?.notification_myLike)) {
                    likeStatus = "YES"
                    customLikeApi(likeStatus)
                } else {
                    likeStatus = "NO"
                    customLikeApi(likeStatus)
                }
            }

            R.id.btn_status -> {

                if ("other".equals(fromNOtificationModal?.notification_module_name) || "".equals(
                        fromNOtificationModal?.notification_module_name
                    )
                ) {
                    val openURL = Intent(android.content.Intent.ACTION_VIEW)
                    openURL.data = Uri.parse(notificationModel?.data?.get(0)?.notification_button_url)
                    startActivity(openURL)
                } else {
                    //redirection on Screen according to conditions
                    if (!fromNOtificationModal!!.notification_module_name?.isEmpty() && !fromNOtificationModal!!.notification_module_id?.isEmpty()) {
                        redirctionOnScreen(fromNOtificationModal!!)
                    }

                }
            }

            R.id.iv_comment -> {
                if (notificationModel?.data?.get(0)?.notification_comment?.size != 0) {
                    val intent = Intent(this, CustomCommentActivity::class.java)
                    intent.putExtra("fromNotionList", fromNOtificationModal)
                    intent.putExtra("reference_type", reference_type)
                    intent.putExtra("NotificationCode", NotificationCode)
                    startActivity(intent)
                }
            }

            R.id.tv_commments -> {
                if (notificationModel?.data?.get(0)?.notification_comment?.size != 0) {
                    val intent = Intent(this, CustomCommentActivity::class.java)
                    intent.putExtra("reference_type", reference_type)
                    intent.putExtra("NotificationCode", NotificationCode)
                    intent.putExtra("fromNotionList", fromNOtificationModal)
                    startActivity(intent)
                }
            }

            R.id.send_msg_button -> {

                if (binding.etMessage.text.toString().trim().isEmpty() || binding.etMessage.text.toString().trim().length < 1) {

                } else {
                    Constant.hideSoftKeyBoard(this, et_message)
                    customNotificationCommentApi(fromNOtificationModal!!)
                }
            }

            R.id.tv_read_all_comments -> {
                val intent = Intent(this, CustomCommentActivity::class.java)
                intent.putExtra("fromNotionList", fromNOtificationModal)
                intent.putExtra("reference_type", reference_type)
                intent.putExtra("NotificationCode", NotificationCode)
                startActivity(intent)
            }
        }
    }

    private fun redirctionOnScreen(fromNOtificationModal: NotificationDetailModel.Data) {
        if ("News_feed".equals(fromNOtificationModal.notification_code)) {
            val intent = Intent(this, FeedDetailsActivity::class.java)
            intent.putExtra("FromNotifications", fromNOtificationModal)
            startActivity(intent)

        } else if ("Program_plan".equals(fromNOtificationModal.notification_code)) {
            val intent = Intent(this, WorkOutPlanDetailActivity::class.java)
            intent.putExtra("FromNotifications", fromNOtificationModal)
            startActivity(intent)

        } else if ("Workout".equals(fromNOtificationModal.notification_code)) {
            val intent = Intent(this, WorkOutDetailActivity::class.java)
            intent.putExtra("PROGRAM_DETAIL", fromNOtificationModal.notification_connection_id)
            intent.putExtra("fromDeepLinking", "")
            intent.putExtra("isMyWorkout", "no")
            startActivity(intent)

        } else if ("Exercise".equals(fromNOtificationModal.notification_code)) {
            val intent = Intent(this, NotificationExerciesActivity::class.java)
            intent.putExtra("category_id", fromNOtificationModal.notification_connection_id)
            intent.putExtra("name", fromNOtificationModal.notification_code + " library")
            startActivity(intent)

        } else if ("".equals(fromNOtificationModal.notification_code)) {

        }
    }

    private fun updateNotificationStatus(notificationId: String) {

        val param = HashMap<String, String>()
        param.put(StringConstant.device_token, "")
        param.put(StringConstant.notificationId, notificationId)
        param.put(StringConstant.device_id, "")
        param.put(StringConstant.device_type, StringConstant.Android)

        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        header.put(StringConstant.apiKey, "HBDEV")
        header.put(StringConstant.apiVersion, "1")
        header.put("Content-Type", "application/json")
        header.put("Accept", "application/json")

        getDataManager().notificationStatusChanged(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Log.d("resonse","response status..."+response!!.toString())
                    val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                    val success: String? = jsonObject?.getString(StringConstant.success)
                    val message: String? = jsonObject?.getString(StringConstant.message)
                    if (success.equals("1")) {
                    }
                }
                override fun onError(anError: ANError) {
                    Log.d("resonse","response error..."+anError!!.toString())
                }
            })
    }



    private fun getNotificationDetail(id:String,NotificationCode:String) {
        if (CommanUtils.isNetworkAvailable(this)!!) {
            val param = HashMap<String, String>()
            param.put("reference_id", ""+id)
            param.put("reference_type", ""+reference_type)
            param.put("type", ""+NotificationCode)
            val header = HashMap<String, String>()
            header.put("auth-token", getDataManager().getUserInfo().customer_auth_token)

            getDataManager().getNotificationDetail(param, header)
                ?.getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        Log.d("response","response..."+response?.toString(4))
                        val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                        val success: String? = jsonObject?.getString(StringConstant.success)
                        val message: String? = jsonObject?.getString(StringConstant.message)
                        if (success.equals("1")) {
                            binding.progressLayout.visibility =View.GONE
                                commentsList.clear()
                                notificationModel = getDataManager().mGson?.fromJson(response.toString(), NotificationDetailModel::class.java)
                                Log.v("customNotificationModel", "" + notificationModel)
                                fromNOtificationModal= notificationModel?.data?.get(0)!!
                                if (notificationModel != null) {
                                    updateNotificationStatus(""+notificationModel!!.data.get(0).notification_id)
                                    setDataInCustomView(notificationModel!!)
                                }
                        }
                        else{
                            binding.progressLayout.visibility =View.GONE
                        }
                    }
                    override fun onError(anError: ANError) {
                        try {
                            Constant.errorHandle(anError!!, getActivity())
                        }
                        catch (e: java.lang.Exception){
                        }
                    }
                })
        }else{
            Constant.showInternetConnectionDialog(this)
        }
    }

/*
    private fun notificationStatus(notificationId: String) {
        val param = HashMap<String, String>()
        param.put("device_token", getDataManager().getUserStringData(AppPreferencesHelper.PREF_FIREBASE_TOKEN)!!)
        param.put(StringConstant.page_index, "1")
        param.put("notification_id", notificationId)
        param.put(StringConstant.device_id, "")
        param.put(StringConstant.device_type, StringConstant.Android)
        param.put(StringConstant.auth_customer_id, getDataManager().getUserInfo().customer_auth_token)
        val header = HashMap<String, String>()
        header.put(StringConstant.AUTHTOKEN, getDataManager().getUserInfo().customer_auth_token)
        header.put(StringConstant.APIKEY, "HBDEV")
        header.put(StringConstant.APIVERSION, "1")

        getDataManager().getNotificationList(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                    val success: String? = jsonObject?.getString(StringConstant.success)
                    val message: String? = jsonObject?.getString(StringConstant.message)

                    if (success.equals("1")) {
                        commentsList.clear()
                       var  customNotificationModel = getDataManager().mGson?.fromJson(response.toString(), CustomNotificationModel::class.java)!!

                        Log.v("customNotificationModel", "" + customNotificationModel)
                        if (customNotificationModel != null) {
                            setDataInCustomView(customNotificationModel)
                        }
                    }
                }

                override fun onError(anError: ANError) {
                    Constant.showCustomToast(
                        this@CustomNotificationActivity,
                        getString(R.string.something_wrong)
                    )
                }
            })
    }
*/

    private fun setDataInCustomView(fromNOtificationModal: NotificationDetailModel) {
        val fromNOtificationModal = fromNOtificationModal.data.get(0)
        //binding.completeView.visibility = View.VISIBLE
        //binding.coordinateLayout.visibility = View.VISIBLE
        binding.progressLayout.visibility = View.GONE
        binding.customTitleHeading.setText(fromNOtificationModal.notification_title)


        if (fromNOtificationModal.notification_type.equals("Portrait")) {

            params = binding.appBar.getLayoutParams() as CoordinatorLayout.LayoutParams
            val dpWidth = CommanUtils.getWidthAndHeight(this)
            params.width = dpWidth
            params.height = dpWidth +  resources.getDimension(R.dimen._70sdp).toInt()
            binding.appBar.setLayoutParams(params)

        }
        else if (fromNOtificationModal.notification_type.equals("Square")) {

            params = binding.appBar.getLayoutParams() as CoordinatorLayout.LayoutParams
            val dpWidth = CommanUtils.getWidthAndHeight(this)
            params.width = dpWidth
            params.height = dpWidth
            binding.appBar.setLayoutParams(params)
        }
        else{
            params = binding.appBar.getLayoutParams() as CoordinatorLayout.LayoutParams
            val dpWidth = CommanUtils.getWidthAndHeight(this)
            params.width = dpWidth
            params.height = dpWidth
            binding.appBar.setLayoutParams(params)
            binding.likeCommentLayout.visibility=View.GONE
            binding.llMessage.visibility=View.GONE
            binding.rvComments.visibility=View.GONE
        }


        if (fromNOtificationModal.notification_button_title.isEmpty()) {
            binding.btnStatus.visibility = View.GONE
            binding.btnStatus.setText(fromNOtificationModal.notification_button_title)
        } else {
            binding.btnStatus.visibility = View.VISIBLE
            binding.btnStatus.setText(fromNOtificationModal.notification_button_title)
        }

        if (fromNOtificationModal.notification_comment != null && !fromNOtificationModal.notification_comment.isEmpty() && fromNOtificationModal.notification_comment.size >= 3) {
            binding.tvReadAllComments.visibility = View.VISIBLE
        } else {
            binding.tvReadAllComments.visibility = View.GONE
        }

        if ("0".equals(fromNOtificationModal.notification_myLike)) {
            binding.ivHeart.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.ic_favorite)
            )
        } else {
            binding.ivHeart.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.ic_favorite_active)
            )
        }

        if (fromNOtificationModal.notification_comment!=null) {
            for (data in fromNOtificationModal.notification_comment) {
                val d1 = Data1()
                d1.customer_id = data.iCustomerId
                d1.customer_name = data.vName
                d1.customer_profile_image = data.vProfileImage
                d1.news_comment = data.comment
                d1.news_comment_delete_access = data.iSysRecDeleted
                d1.news_comment_posted_days = data.comment_ago
                d1.news_comments_id = data.customNotificationLikeCommentId

                commentsList.add(d1)
            }
            commentsAdapter.notifyDataSetChanged()
        }

        binding.txtDiscription.setText(
            HtmlCompat.fromHtml(
                fromNOtificationModal.notification_message,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        )
        if (fromNOtificationModal.notification_likeCount!=null){
            if (fromNOtificationModal.notification_likeCount.equals("0") || fromNOtificationModal.notification_likeCount.equals(
                    "1")) {
                binding.tvLikes.setText(fromNOtificationModal.notification_likeCount + " like")
            } else {
                binding.tvLikes.setText(fromNOtificationModal.notification_likeCount + " likes")
            }

        }
        if (fromNOtificationModal.notification_commentCount!=null){
            if (fromNOtificationModal.notification_commentCount.equals("0") || fromNOtificationModal.notification_commentCount.equals(
                    "1")) {
                binding.tvCommments.setText(fromNOtificationModal.notification_commentCount + " comment")
            } else {
                binding.tvCommments.setText(fromNOtificationModal.notification_commentCount + " comments")
            }
        }

        if (!fromNOtificationModal.notification_image.isEmpty()) {

            if (!this@CustomNotificationActivity.isDestroyed()) {
                Glide.with(binding.ivCustomImage).load(fromNOtificationModal.notification_image)
                    .into(binding.ivCustomImage)
            }
        }
        else{
            if (!this@CustomNotificationActivity.isDestroyed()) {
                binding.ivCustomImage.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorWhite));

                Glide.with(binding.ivCustomImage).load(R.drawable.user_placeholder)
                    .into(binding.ivCustomImage)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        hideKeyboard()
    }

    override fun onResume() {
        super.onResume()
       /* if (::fromNOtificationModal.isInitialized) {
            getNotificationDetail(fromNOtificationModal!!.notification_id)
          //  notificationStatus(fromNOtificationModal.notification_id)
        }*/
        hideNavStatusBar()
     //   hideNavigationBar()

    }

    // adapter listener to gt data here
    /*OnClick of Three dot india adapter*/
    override fun moreOnClick(data: Data1, position: Int) {
        hideKeyboard()
        ReportAndDeleteData = data
        this.position = position
        if (data.customer_id.equals(getDataManager().getUserInfo().customer_id)) {
            deleteComment()
        } else {
            reportComment()
        }
    }

    fun deleteComment() {
        menus = mutableListOf<DialogMenu>()
        menus.add(DialogMenu("Delete", R.drawable.ic_recycling_bin))
        dialogFragment = ItemListDialogFragment.newInstance("Delete")
        dialogFragment.addMenu(menus)
        dialogFragment.addDialogEventListener(this)
        dialogFragment.show(supportFragmentManager!!, "Delete")
    }

    fun reportComment() {
        menus = mutableListOf<DialogMenu>()
        menus.add(DialogMenu("Report Comment", R.drawable.ic_information))
        dialogFragment = ItemListDialogFragment.newInstance("Report")
        dialogFragment.addMenu(menus)
        dialogFragment.addDialogEventListener(this)
        dialogFragment.show(supportFragmentManager!!, "Report")
    }

    //*comment post*//*
    private fun customNotificationCommentApi(fromNOtificationModal: NotificationDetailModel.Data) {

        val param = HashMap<String, String>()
        param.put(
            StringConstant.auth_customer_id,
            getDataManager().getUserInfo().customer_auth_token
        )
        param.put("comment", binding.etMessage.text.toString().trim())
        param.put("device_type", "Android")
        param.put("like", "")
        param.put(
            "device_token",
            getDataManager().getUserStringData(AppPreferencesHelper.PREF_FIREBASE_TOKEN)!!
        )
        param.put("device_id", "")
        param.put("connectionId", fromNOtificationModal.notification_connection_id)

        val header = HashMap<String, String>()
        header.put(authToken, getDataManager().getUserInfo().customer_auth_token)
        getDataManager().customCommentApi(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    try {
                        val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                        val status = json?.get(StringConstant.success)
                        val msg = json?.get(StringConstant.message)
                        if (status!!.equals("1")) {
                            val dataArray: JSONArray = response.getJSONArray(StringConstant.data)
                            val commentId = dataArray.getJSONObject(0).get("comment_id")

                            val d1 = Data1()
                            d1.customer_id = getDataManager().getUserInfo().customer_id
                            d1.customer_name = getDataManager().getUserInfo().customer_user_name
                            d1.customer_profile_image =
                                getDataManager().getUserInfo().customer_profile_image
                            d1.news_comment = binding.etMessage.text.toString()
                            d1.news_comment_delete_access = "1"
                            d1.news_comment_posted_days = "1 sec ago"
                            d1.news_comments_id = commentId.toString()

                            binding.etMessage.text = null
                            commentsList.add(0, d1)
                            commentsAdapter.notifyDataSetChanged()

                            getNotificationDetail(this@CustomNotificationActivity.fromNOtificationModal!!.notification_id,NotificationCode)

                        } else {

                            Constant.showCustomToast(this@CustomNotificationActivity, "" + msg)
                        }

                    } catch (ex: Exception) {
                        Constant.showCustomToast(
                            this@CustomNotificationActivity,
                            getString(com.doviesfitness.R.string.something_wrong)
                        )
                    }
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, this@CustomNotificationActivity)
                    //To change body of created functions use File | Settings | File Templates.
                }

            })
    }

    //*comment post*//*
    private fun customLikeApi(likeStatus: String) {

        val param = HashMap<String, String>()
        param.put(
            StringConstant.auth_customer_id,
            getDataManager().getUserInfo().customer_auth_token
        )
        param.put("comment", "")
        param.put("device_type", "Android")
        param.put("like", likeStatus)
        param.put(
            "device_token",
            getDataManager().getUserStringData(AppPreferencesHelper.PREF_FIREBASE_TOKEN)!!
        )
        param.put("device_id", "")
        param.put("connectionId", fromNOtificationModal!!.notification_connection_id)


        val header = HashMap<String, String>()
        header.put(authToken, getDataManager().getUserInfo().customer_auth_token)
        getDataManager().customCommentApi(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    try {
                        val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                        val status = json?.get(StringConstant.success)
                        val msg = json?.get(StringConstant.message)
                        if (status!!.equals("1")) {

                            // when come from notifcation it will get tru either false
                            //if ("YES".equals(likeStatus)) {
                            if ("0".equals(notificationModel?.data?.get(0)?.notification_myLike)) {
                                this@CustomNotificationActivity.likeStatus = "NO"
                                notificationModel?.data?.get(0)?.notification_myLike = "1"
                                val likeCount =
                                    notificationModel?.data?.get(0)?.notification_likeCount!!.toInt() + 1
                                notificationModel?.data?.get(0)?.notification_likeCount = likeCount.toString()
                                iv_heart.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        this@CustomNotificationActivity,
                                        R.drawable.ic_favorite_active
                                    )
                                )
                            } else {
                                this@CustomNotificationActivity.likeStatus = "YES"
                                notificationModel?.data?.get(0)?.notification_myLike = "0"
                                val likeCount =
                                    notificationModel?.data?.get(0)?.notification_likeCount!!.toInt() - 1
                                notificationModel?.data?.get(0)?.notification_likeCount = likeCount.toString()
                                iv_heart.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        this@CustomNotificationActivity,
                                        R.drawable.ic_favorite
                                    )
                                )
                            }

                            /*Check the customer Likes count and then according to change name*/
                            if (notificationModel?.data?.get(0)?.notification_likeCount.equals("0") || notificationModel?.data?.get(0)?.notification_likeCount.equals(
                                    "1"
                                )
                            ) {
                                binding.tvLikes.text =
                                    notificationModel?.data?.get(0)?.notification_likeCount + " " + getString(R.string.like)
                            } else {
                                binding.tvLikes.text =
                                    notificationModel?.data?.get(0)?.notification_likeCount + " " + getString(R.string.likes)
                            }

                            //notificationStatus(fromNOtificationModal.notification_id)


                        } else {

                            Constant.showCustomToast(this@CustomNotificationActivity, "" + msg)
                        }

                    } catch (ex: Exception) {
                        Constant.showCustomToast(
                            this@CustomNotificationActivity,
                            getString(com.doviesfitness.R.string.something_wrong)
                        )
                    }
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, this@CustomNotificationActivity)
                    //To change body of created functions use File | Settings | File Templates.
                }
            })
    }


    /*Delete comment api*/
    private fun deleteComment_Api(id: Data1, pos: Int) {
        val param = HashMap<String, String>()
        param.put(
            StringConstant.auth_customer_id,
            getDataManager().getUserInfo().customer_auth_token
        )
        param.put(StringConstant.comment_id, id.news_comments_id)
        val header = HashMap<String, String>()
        header.put(authToken, getDataManager().getUserInfo().customer_auth_token)
        getDataManager().deleteCustomCommentApi(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    try {
                        val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                        val status = json?.get(StringConstant.success)
                        val msg = json?.get(StringConstant.message)
                        if (status!!.equals("1")) {
                            commentsList.removeAt(pos)
                            commentsAdapter.notifyDataSetChanged()
                            getNotificationDetail(fromNOtificationModal!!.notification_id,NotificationCode)
                        } else {
                            Constant.showCustomToast(this@CustomNotificationActivity, "" + msg)
                        }
                    } catch (ex: Exception) {
                        Constant.showCustomToast(
                            this@CustomNotificationActivity,
                            getString(R.string.something_wrong)
                        )
                    }
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, this@CustomNotificationActivity)
                }
            })
    }

    private fun reportComment_Api(data: Data1, pos: Int) {
        Log.d(
            "auth token....",
            "auth token...." + getDataManager().getUserInfo().customer_auth_token + "...comment id..." + data.news_comments_id + "...comment..." + data.news_comment
        )
        val param = HashMap<String, String>()
        param.put(
            StringConstant.auth_customer_id,
            getDataManager().getUserInfo().customer_auth_token
        )
        param.put(StringConstant.custom_notification_id, "")
        param.put(StringConstant.comment_id, data.news_comments_id)
        param.put(StringConstant.report_text, data.news_comment)
        val header = HashMap<String, String>()
        header.put(authToken, getDataManager().getUserInfo().customer_auth_token)
        header.put(StringConstant.apiKey, "HBDEV")
        header.put(StringConstant.apiVersion, "1")
        getDataManager().reportCommentApi(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    try {
                        val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                        val status = json?.get(StringConstant.success)
                        val msg = json?.get(StringConstant.message)
                        if (status!!.equals("1")) {
                            Constant.showCustomToast(this@CustomNotificationActivity, "" + msg)

                        } else {
                            Constant.showCustomToast(this@CustomNotificationActivity, "" + msg)
                        }
                    } catch (ex: Exception) {
                        Constant.showCustomToast(
                            this@CustomNotificationActivity,
                            getString(R.string.something_wrong)
                        )
                    }
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, this@CustomNotificationActivity)
                }
            })
    }


    override fun onItemClicked(mCategoryTag: String, mMenuTag: String, pos: Int) {

        if ("Delete".equals(mCategoryTag)) {
            if (mMenuTag.equals("Delete")) {
                deleteComment_Api(ReportAndDeleteData, position)
            }
        } else if ("Report".equals(mCategoryTag)) {
            if (mMenuTag.equals("Report Comment")) {
                reportComment_Api(ReportAndDeleteData, position)
            }
        }
    }

    override fun onDialogDismiss() {

    }
}
