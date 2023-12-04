package com.doviesfitness.ui.bottom_tabbar.home_tab.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.bottom_tabbar.home_tab.adapter.CommentsAdapter
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.CommentsResponce
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.StringConstant
import com.doviesfitness.utils.StringConstant.Companion.authToken
import com.doviesfitness.utils.StringConstant.Companion.newsFeed
import com.doviesfitness.utils.StringConstant.Companion.auth_customer_id
import com.doviesfitness.utils.StringConstant.Companion.comment_id
import com.doviesfitness.utils.StringConstant.Companion.comment_text
import com.doviesfitness.utils.StringConstant.Companion.data
import com.doviesfitness.utils.StringConstant.Companion.module_name
import com.doviesfitness.utils.StringConstant.Companion.news_id
import kotlinx.android.synthetic.main.activity_comments.*
import org.json.JSONArray
import org.json.JSONObject
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.doviesfitness.R
import com.doviesfitness.allDialogs.menu.DialogMenu
import com.doviesfitness.allDialogs.menu.ItemListDialogFragment
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.Data1
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.GetNewsFeedDetail
import com.doviesfitness.utils.StringConstant.Companion.apiKey
import com.doviesfitness.utils.StringConstant.Companion.apiVersion
import com.doviesfitness.utils.StringConstant.Companion.custom_notification_id
import com.doviesfitness.utils.StringConstant.Companion.report_text
import kotlinx.android.synthetic.main.activity_comments.et_message
import kotlinx.android.synthetic.main.activity_comments.iv_back
import kotlinx.android.synthetic.main.activity_comments.send_msg_button

class CommentsActivity : BaseActivity(), View.OnClickListener, CommentsAdapter.CommentOnClick,
    ItemListDialogFragment.DialogEventListener {

    private var comentPosition: Int = 0
    private var whenComeFromNotification: Boolean = false
    private lateinit var commentsAdapter: CommentsAdapter
    lateinit var commentsList: ArrayList<Data1>
    var newsId = ""
    var position = 0
    private lateinit var menus: MutableList<DialogMenu>
    private lateinit var dialogFragment: ItemListDialogFragment
    private lateinit var ReportAndDeleteData: Data1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideNavStatusBar()
        setContentView(R.layout.activity_comments)
        initViews()
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

    override fun onResume() {
        super.onResume()
        hideNavStatusBar()
    }
    private fun initViews() {

        commentsList = ArrayList()
        iv_back.setOnClickListener(this)
        send_msg_button.setOnClickListener(this)

/*Set Adapter*/
        commentsAdapter = CommentsAdapter(this, this.commentsList, this)
        rv_comments.layoutManager = LinearLayoutManager(this)
        rv_comments.adapter = commentsAdapter


        if (intent.hasExtra("newsId")) {
            if (intent.getStringExtra("newsId") != null) {
                newsId = intent.getStringExtra("newsId")!!
                this.position = intent.getIntExtra("position", 0)
                getNewsfeedCommentApi(newsId)
            }
        } else if (intent.hasExtra("fromNotionList")) {
            whenComeFromNotification = true

            if (intent.getSerializableExtra("fromNotionList") != null) {
                val notificationModel =
                    intent.getSerializableExtra("fromNotionList") as ArrayList<Data1>

                Log.v("notificationModel35", "" + notificationModel)

                for (i in 0..notificationModel.size - 1) {

                    val d1 = Data1()
                    d1.customer_id = notificationModel.get(i).customer_id
                    d1.customer_name = notificationModel.get(i).customer_name
                    d1.customer_profile_image = notificationModel.get(i).customer_profile_image
                    d1.news_comment = notificationModel.get(i).news_comment
                    d1.news_comment_delete_access =
                        notificationModel.get(i).news_comment_delete_access
                    d1.news_comment_posted_days = notificationModel.get(i).news_comment_posted_days
                    d1.news_comments_id = notificationModel.get(i).news_comments_id

                    commentsList.add(d1)
                }
                commentsAdapter.notifyDataSetChanged()
            }
        } else if (intent.hasExtra("fromNotionListNewsFeed")) {
            whenComeFromNotification = true

            if (intent.getSerializableExtra("fromNotionListNewsFeed") != null) {
                val notificationModel =
                    intent.getSerializableExtra("fromNotionListNewsFeed") as GetNewsFeedDetail
                Log.v("notificationModel35", "" + notificationModel)
                getNewsfeedCommentApi(notificationModel.nf_news_feed_id)
            }
        }

/*Swipe to refresh*/
        swipe_refresh.setOnRefreshListener(
            SwipeRefreshLayout.OnRefreshListener
            {
                if (whenComeFromNotification) {
                    swipe_refresh.isRefreshing = false
                } else {
                    getNewsfeedCommentApi(newsId)
                }
            })

        et_message.addTextChangedListener(
            object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (!s.toString().isEmpty()) {
                        send_msg_button.setImageDrawable(
                            ContextCompat.getDrawable(
                                this@CommentsActivity,
                                R.drawable.black_them_send_ico
                            )
                        )
                    } else {
                        send_msg_button.setImageDrawable(
                            ContextCompat.getDrawable(
                                this@CommentsActivity,
                                R.drawable.new_graysend_ico
                            )
                        )
                    }

                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
//To change body of created functions use File | Settings | File Templates.
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//To change body of created functions use File | Settings | File Templates.
                }
            })

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                Constant.hideSoftKeyBoard(this, et_message)
                onBackPressed()
            }
            R.id.send_msg_button -> {


                if (whenComeFromNotification) {
                    if (!et_message.text.toString().isEmpty()) {
                        customNotificationCommentApi()
                    }


                } else {
                    Constant.hideSoftKeyBoard(this, et_message)
                    if (!et_message.text.toString().isEmpty()) {
                        postCommentApi()
                    }
                }
            }
        }
    }

    /*comment post*/
    private fun postCommentApi() {
        val param = HashMap<String, String>()
        param.put(auth_customer_id, getDataManager().getUserInfo().customer_auth_token)
        param.put(comment_text, et_message.text.toString().trim())
        param.put(news_id, newsId)
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
                            val dataArray: JSONArray = response.getJSONArray(data)
                            val commentId = dataArray.getJSONObject(0).get("comment_id")
                            val coment = Data1(
                                getDataManager().getUserInfo().customer_id,
                                getDataManager().getUserInfo().customer_user_name
                                , getDataManager().getUserInfo().customer_profile_image
                                , et_message.text.toString().trim(),
                                "1",
                                "1 sec ago",
                                commentId.toString()
                            )
                            et_message.text = null
                            commentsList.add(0, coment)
                            commentsAdapter.notifyDataSetChanged()
                        } else {

                            Constant.showCustomToast(this@CommentsActivity, "" + msg)
                        }

                    } catch (ex: Exception) {
                        Constant.showCustomToast(
                            this@CommentsActivity,
                            getString(com.doviesfitness.R.string.something_wrong)
                        )
                    }
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, this@CommentsActivity)
//To change body of created functions use File | Settings | File Templates.
                }

            })
    }


    //*comment post*//*
    private fun customNotificationCommentApi() {

        val param = HashMap<String, String>()
        param.put(
            StringConstant.auth_customer_id,
            getDataManager().getUserInfo().customer_auth_token
        )
        param.put("comment", et_message.text.toString().trim())
        param.put("device_type", "Android")
        param.put("like", "")
        param.put(
            "device_token",
            getDataManager().getUserStringData(AppPreferencesHelper.PREF_FIREBASE_TOKEN)!!
        )
        param.put("device_id", "")
        param.put("connectionId", "226")

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
                            d1.news_comment = et_message.text.toString()
                            d1.news_comment_delete_access = "0"
                            d1.news_comment_posted_days = "1 sec ago"
                            d1.news_comments_id = commentId.toString()

                            et_message.text = null
                            commentsList.add(0, d1)
                            commentsAdapter.notifyDataSetChanged()

                        } else {
                            Constant.showCustomToast(this@CommentsActivity, "" + msg)
                        }

                    } catch (ex: Exception) {
                        Constant.showCustomToast(
                            this@CommentsActivity,
                            getString(com.doviesfitness.R.string.something_wrong)
                        )
                    }
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, this@CommentsActivity)
//To change body of created functions use File | Settings | File Templates.
                }

            })
    }


    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra("CommentCount", commentsList.size)
        intent.putExtra("position", position)
        setResult(Constant.COMMENT_COUNT_CONSTANT, intent)
        finish()
    }

    /*Get news feed comment api*/
    private fun getNewsfeedCommentApi(newsId: String) {
        val param = HashMap<String, String>()
        param.put(auth_customer_id, getDataManager().getUserInfo().customer_auth_token)
        param.put(module_name, newsFeed)
        param.put(news_id, newsId)
        param.put("rec_limit", "")
        val header = HashMap<String, String>()
        header.put(authToken, getDataManager().getUserInfo().customer_auth_token)
        getDataManager().getNewsFeedCommentApi(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    try {
                        val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                        val status = json?.get(StringConstant.success)
                        val msg = json?.get(StringConstant.message)
                        if (status!!.equals("1")) {
                            commentsList.clear()
                            commentsAdapter.notifyDataSetChanged()
                            swipe_refresh.setRefreshing(false)
                            val commentsResponce = getDataManager().mGson?.fromJson(response.toString(), CommentsResponce::class.java)
                            commentsList.addAll(commentsResponce!!.data)
                            commentsAdapter.notifyDataSetChanged()
                        } else {
                            swipe_refresh.setRefreshing(false)
                           //Constant.showCustomToast(this@CommentsActivity, "" + msg)
                        }

                    } catch (ex: Exception) {
                        Constant.showCustomToast(
                            this@CommentsActivity,
                            getString(R.string.something_wrong)
                        )
                    }
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, this@CommentsActivity)
                }

            })
    }


    override fun moreOnClick(data: Data1, position: Int) {
        ReportAndDeleteData = data
        comentPosition = position
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

    override fun onItemClicked(mCategoryTag: String, mMenuTag: String, position: Int) {
        if ("Delete".equals(mCategoryTag)) {
            if (mMenuTag.equals("Delete")) {
                deleteComment(ReportAndDeleteData, comentPosition)
            }
        } else if ("Report".equals(mCategoryTag)) {

            if (mMenuTag.equals("Report Comment")) {
                reportComment(ReportAndDeleteData, comentPosition)
            }
        }
    }

    override fun onDialogDismiss() {

    }

    /*Delete comment api*/
    private fun deleteComment(id: Data1, pos: Int) {
        if(!id.news_comments_id.isEmpty() && !getDataManager().getUserInfo().customer_auth_token.isEmpty()){
            val param = HashMap<String, String>()
            param.put(auth_customer_id, getDataManager().getUserInfo().customer_auth_token)
            param.put(comment_id, id.news_comments_id)
            val header = HashMap<String, String>()
            header.put(authToken, getDataManager().getUserInfo().customer_auth_token)
            getDataManager().deleteCommentApi(param, header)
                ?.getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        try {
                            val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                            val status = json?.get(StringConstant.success)
                            val msg = json?.get(StringConstant.message)
                            if (status!!.equals("1")) {
                                commentsList.removeAt(pos)
                                commentsAdapter.notifyDataSetChanged()

                                if(commentsList.size == 0){
                                    swipe_refresh.setRefreshing(false)
                                }

                            } else {
                                //Constant.showCustomToast(this@CommentsActivity, "" + msg)
                            }
                        } catch (ex: Exception) {
                            Constant.showCustomToast(
                                this@CommentsActivity,
                                getString(R.string.something_wrong)
                            )
                        }
                    }

                    override fun onError(anError: ANError) {
                        Constant.errorHandle(anError, this@CommentsActivity)
                    }
                })
        }

    }

    private fun reportComment(data: Data1, pos: Int) {
        Log.d(
            "auth token....",
            "auth token...." + getDataManager().getUserInfo().customer_auth_token + "...comment id..." + data.news_comments_id + "...comment..." + data.news_comment
        )
        val param = HashMap<String, String>()
        param.put(auth_customer_id, getDataManager().getUserInfo().customer_auth_token)
        param.put(custom_notification_id, "")
        param.put(comment_id, data.news_comments_id)
        param.put(report_text, data.news_comment)
        val header = HashMap<String, String>()
        header.put(authToken, getDataManager().getUserInfo().customer_auth_token)
        header.put(apiKey, "HBDEV")
        header.put(apiVersion, "1")
        getDataManager().reportCommentApi(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    try {
                        val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                        val status = json?.get(StringConstant.success)
                        val msg = json?.get(StringConstant.message)
                        if (status!!.equals("1")) {
                            Constant.showCustomToast(this@CommentsActivity, "" + msg)

                        } else {
                            Constant.showCustomToast(this@CommentsActivity, "" + msg)
                        }
                    } catch (ex: Exception) {
                        Constant.showCustomToast(
                            this@CommentsActivity,
                            getString(R.string.something_wrong)
                        )
                    }
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, this@CommentsActivity)
                }
            })
    }

}