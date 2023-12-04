package com.doviesfitness.ui.profile.inbox.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.doviesfitness.R
import com.doviesfitness.allDialogs.menu.DialogMenu
import com.doviesfitness.allDialogs.menu.ItemListDialogFragment
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.Data1
import com.doviesfitness.ui.profile.inbox.addapter.NotificationCommentsAdapter
import com.doviesfitness.ui.profile.inbox.modal.NotificationDetailModel
import com.doviesfitness.utils.CommanUtils
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.StringConstant
import kotlinx.android.synthetic.main.activity_comments.*
import org.json.JSONArray
import org.json.JSONObject


class CustomCommentActivity : BaseActivity(), View.OnClickListener, NotificationCommentsAdapter.CommentOnClick,
    ItemListDialogFragment.DialogEventListener {

    private var notificationModel: NotificationDetailModel.Data?=null
    private var whenComeFromNotification: Boolean = false
    private lateinit var commentsAdapter: NotificationCommentsAdapter
    lateinit var commentsList: ArrayList<Data1>
    var newsId = ""
    var position = 0
    private lateinit var menus: MutableList<DialogMenu>
    private lateinit var dialogFragment: ItemListDialogFragment
    private lateinit var ReportAndDeleteData: Data1

    protected var pro :String =""
    protected var reference_type :String =""
    protected var NotificationCode :String =""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_comment)
        initViews()
    }

    private fun initViews() {
        commentsList = ArrayList()
        iv_back.setOnClickListener(this)
        send_msg_button.setOnClickListener(this)

        /*Set Adapter*/
        val userDataInfo = getDataManager().getUserInfo()
        commentsAdapter = NotificationCommentsAdapter(this, this.commentsList, this, userDataInfo, "FullScreen")
        rv_comments.layoutManager = LinearLayoutManager(this)
        rv_comments.adapter = commentsAdapter


        if (intent.hasExtra("fromNotionList")!!) {
            whenComeFromNotification = true
            if (intent.hasExtra("reference_type")!!)
                reference_type=intent.getStringExtra("reference_type")!!
            if (intent.hasExtra("NotificationCode")!!)
                NotificationCode=intent.getStringExtra("NotificationCode")!!

            if (intent.getSerializableExtra("fromNotionList") != null) {
                //val notificationModel = intent.getSerializableExtra("fromNotionList") as NotificationModel.Data
                 notificationModel = intent.getSerializableExtra("fromNotionList") as NotificationDetailModel.Data
                Log.v("notificationModel35", "" + notificationModel)
                getNotificationDetail(notificationModel!!.notification_id)

            }
             }

        /*if (intent.hasExtra("fromNotionList")) {
            whenComeFromNotification = true

            if (intent.getSerializableExtra("fromNotionList") != null) {
                val notificationModel =
                    intent.getSerializableExtra("fromNotionList") as ArrayList<Data1>

                Log.v("notificationModel35", "" + notificationModel)

                for (i india 0..notificationModel.size - 1) {

                    val d1 = Data1()
                    d1.customer_id = notificationModel.get(i).customer_id
                    d1.customer_name = notificationModel.get(i).customer_name
                    d1.customer_profile_image = notificationModel.get(i).customer_profile_image
                    d1.news_comment = notificationModel.get(i).news_comment
                    d1.news_comment_delete_access = notificationModel.get(i).news_comment_delete_access
                    d1.news_comment_posted_days = notificationModel.get(i).news_comment_posted_days
                    d1.news_comments_id = notificationModel.get(i).news_comments_id

                    commentsList.add(d1)
                }
                commentsAdapter.notifyDataSetChanged()
            }
        }*/

        et_message.addTextChangedListener(
            object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (!s.toString().isEmpty()) {
                        send_msg_button.setImageDrawable(
                            ContextCompat.getDrawable(
                                this@CustomCommentActivity,
                                R.drawable.black_them_send_ico
                            )
                        )
                    } else {
                        send_msg_button.setImageDrawable(
                            ContextCompat.getDrawable(
                                this@CustomCommentActivity,
                                R.drawable.new_graysend_ico
                            )
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

        getDataManager().getNotificationListNew(param, header)?.getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                val success: String? = jsonObject?.getString(StringConstant.success)
                val message: String? = jsonObject?.getString(StringConstant.message)

                if (success.equals("1")) {
                    val customNotificationModel =
                        getDataManager().mGson?.fromJson(response.toString(), CustomNotificationModel::class.java)

                    Log.v("customNotificationModel", "" + customNotificationModel)
                    if (customNotificationModel != null) {
                        setDataInCustomView(customNotificationModel)
                    }
                }
            }

            override fun onError(anError: ANError) {
                Constant.showCustomToast(this@CustomCommentActivity, getString(R.string.something_wrong))
            }
        })
    }
*/

    private fun getNotificationDetail(id:String) {
        if (CommanUtils.isNetworkAvailable(this)!!) {
            val param = HashMap<String, String>()
            /////
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

                            if (success.equals("1")) {
                                commentsList.clear()
                               var notificationModel = getDataManager().mGson?.fromJson(response.toString(), NotificationDetailModel::class.java)
                                Log.v("customNotificationModel", "" + notificationModel)
                                if (notificationModel != null) {
                                    setDataInCustomView(notificationModel!!)
                                }
                            }

                            // mNotificationList.addAll(notificationModel!!.data);
                        }
                    }
                    override fun onError(anError: ANError) {
                    }
                })

        }else{
            Constant.showInternetConnectionDialog(this)
        }
    }


    private fun setDataInCustomView(fromNOtificationModal: NotificationDetailModel) {

        val fromNOtificationModal = fromNOtificationModal.data.get(0)

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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                Constant.hideSoftKeyBoard(this, et_message)
                onBackPressed()
            }
            R.id.send_msg_button -> {

                if (whenComeFromNotification) {

                    if (et_message.text.toString().trim().isEmpty() || et_message.text.toString().trim().length < 1) {

                    } else {
                        Constant.hideSoftKeyBoard(this, et_message)
                        customNotificationCommentApi()
                    }
                }
            }
        }
    }

    //*comment post*//*
    private fun customNotificationCommentApi() {
        val param = HashMap<String, String>()
        param.put(StringConstant.auth_customer_id, getDataManager().getUserInfo().customer_auth_token)
        param.put("comment", et_message.text.toString().trim())
        param.put("device_type", "Android")
        param.put("like", "")
        param.put("device_token", getDataManager().getUserStringData(AppPreferencesHelper.PREF_FIREBASE_TOKEN)!!)
        param.put("device_id", "")
        param.put("connectionId", ""+notificationModel?.notification_connection_id)


        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        getDataManager().customCommentApi(param, header)?.getAsJSONObject(object : JSONObjectRequestListener {
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
                        d1.customer_profile_image = getDataManager().getUserInfo().customer_profile_image
                        d1.news_comment = et_message.text.toString()
                        d1.news_comment_delete_access = "0"
                        d1.news_comment_posted_days = "1 sec ago"
                        d1.news_comments_id = commentId.toString()

                        et_message.text = null
                        commentsList.add(0, d1)
                        commentsAdapter.notifyDataSetChanged()

                    } else {

                        Constant.showCustomToast(this@CustomCommentActivity, "" + msg)
                    }

                } catch (ex: Exception) {
                    Constant.showCustomToast(
                        this@CustomCommentActivity,
                        getString(com.doviesfitness.R.string.something_wrong)
                    )
                }
            }

            override fun onError(anError: ANError) {
                Constant.errorHandle(anError, this@CustomCommentActivity)
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

    /*OnClick of Three dot india adapter*/
    override fun moreOnClick(data: Data1, position: Int) {
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


    /*Delete comment api*/
    private fun deleteComment_Api(id: Data1, pos: Int) {
        val param = HashMap<String, String>()
        param.put(StringConstant.auth_customer_id, getDataManager().getUserInfo().customer_auth_token)
        param.put(StringConstant.comment_id, id.news_comments_id)
        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
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
                        } else {
                            // Constant.showCustomToast(this@CustomCommentActivity, "" + msg)
                        }
                    } catch (ex: Exception) {
                        Constant.showCustomToast(this@CustomCommentActivity, getString(R.string.something_wrong))
                    }
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, this@CustomCommentActivity)
                }
            })
    }

    private fun reportComment_Api(data: Data1, pos: Int) {
        Log.d(
            "auth token....",
            "auth token...." + getDataManager().getUserInfo().customer_auth_token + "...comment id..." + data.news_comments_id + "...comment..." + data.news_comment
        )
        val param = HashMap<String, String>()
        param.put(StringConstant.auth_customer_id, getDataManager().getUserInfo().customer_auth_token)
        param.put(StringConstant.custom_notification_id, "")
        param.put(StringConstant.comment_id, data.news_comments_id)
        param.put(StringConstant.report_text, data.news_comment)
        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
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
                            Constant.showCustomToast(this@CustomCommentActivity, "" + msg)

                        } else {
                            Constant.showCustomToast(this@CustomCommentActivity, "" + msg)
                        }
                    } catch (ex: Exception) {
                        Constant.showCustomToast(this@CustomCommentActivity, getString(R.string.something_wrong))
                    }
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, this@CustomCommentActivity)
                }
            })
    }


    ///////
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
