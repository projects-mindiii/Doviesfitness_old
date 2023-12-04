package com.doviesfitness.ui.profile.inbox.activity

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.databinding.ActivityInboxBinding
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.bottom_tabbar.home_tab.activity.FeedDetailsActivity
import com.doviesfitness.ui.bottom_tabbar.home_tab.activity.WorkOutDetailActivity
import com.doviesfitness.ui.bottom_tabbar.home_tab.activity.WorkOutPlanDetailActivity
import com.doviesfitness.ui.bottom_tabbar.stream_tab.activities.StreamDetailActivity
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.StreamDataModel
import com.doviesfitness.ui.profile.inbox.addapter.NewInboxAdapter
import com.doviesfitness.ui.profile.inbox.modal.NotificationModel
import com.doviesfitness.utils.CommanUtils
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.StringConstant
import eightbitlab.com.blurview.RenderScriptBlur
import kotlinx.android.synthetic.main.dialog_delete.*
import org.json.JSONObject

class InboxActivity : BaseActivity(), View.OnClickListener, NewInboxAdapter.InboxClickListener {
    private lateinit var binding: ActivityInboxBinding
    //lateinit private var adapter: InboxAdapter
    lateinit private var adapter: NewInboxAdapter
    private var mNotificationList = ArrayList<NotificationModel.Data>()
    private var count: String? = ""
    private var mLastClickTime: Long = 0
    private var offset: Int = 0
    // private var offset: Int = 10


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_inbox)
        inItView()
    }

    private fun inItView() {
        binding.progressLayout.visibility = View.VISIBLE
        hideNavigationBar()
        val windowBackground = window.decorView.background
        binding.topBlurView.setupWith(binding.containerId)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(this))
            .setBlurRadius(25f)
            .setHasFixedTransformationMatrix(true)

        val layoutManager1 = LinearLayoutManager(this)
        binding.myInboxRv.layoutManager = layoutManager1
        adapter = NewInboxAdapter(this, mNotificationList, this)
        binding.myInboxRv.adapter = adapter


        getNotificationList()


        //*Swipe to refresh*//*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            binding.swipeRefresh.setProgressViewOffset(
                false,
                resources.getDimension(R.dimen._40sdp).toInt(),
                resources.getDimension(R.dimen._80sdp).toInt()
            )
        }
        binding.swipeRefresh.setOnRefreshListener {
            offset = 0
            count = ""
            mNotificationList.clear()
            getNotificationList()
        }

        binding.myInboxMain.setOnScrollChangeListener(object :
            NestedScrollView.OnScrollChangeListener {
            override fun onScrollChange(
                v: NestedScrollView?,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                if (v!!.getChildAt(v!!.getChildCount() - 1) != null) {
                    if (scrollY >= v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight() && scrollY > oldScrollY) {
                        //code to fetch more data for endless scrolling
                        offset = offset + 10
                        if (!count!!.isEmpty()) {
                            if (offset <= count!!.toInt()) {
                                //  offset = offset + 10
                                adapter.showLoading(true)
                                adapter.notifyDataSetChanged()
                                getNotificationList()
                            }
                        }
                    }
                }
            }
        })

        setOnClick(binding.ivBack)
    }

    private fun setOnClick(vararg views: View) {

        for (view in views) {
            view.setOnClickListener(this)
        }
    }

    override fun onResume() {
        super.onResume()
        val view = window.decorView
        view.systemUiVisibility =
            view.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        hideNavigationBar()
    }

    /* private fun getNotificationList(page: Int) {

         if (CommanUtils.isNetworkAvailable(this)!!) {

             //binding.progressLayout.visibility =View.VISIBLE
             val param = HashMap<String, String>()
             param.put("device_token", getDataManager().getUserStringData(AppPreferencesHelper.PREF_FIREBASE_TOKEN)!!)
             param.put(StringConstant.page_index, "" + page)
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
                         val next_page = jsonObject?.getString(StringConstant.next_page)
                         nextPage = next_page!!.toInt()

                         if (success.equals("1")) {
                             binding.progressLayout.visibility =View.GONE
                             binding.swipeRefresh.setRefreshing(false)
                             binding.noInboxFound.visibility = View.GONE
                             val notificationModel =
                                 getDataManager().mGson?.fromJson(
                                     response.toString(),
                                     NotificationModel::class.java
                                 )
                             mNotificationList.addAll(notificationModel!!.data);
                             hideFooterLoiader()
                         }
                         if (mNotificationList.size == 0 && mNotificationList.isEmpty()) {
                             binding.swipeRefresh.setRefreshing(false)
                             binding.noInboxFound.visibility = View.VISIBLE
                             binding.progressLayout.visibility =View.GONE
                         }
                     }

                     override fun onError(anError: ANError) {
                         hideFooterLoiader()
                         binding.swipeRefresh.setRefreshing(false)
                         // binding.loader.visibility = View.GONE
                         Constant.showCustomToast(
                             this@InboxActivity,
                             getString(R.string.something_wrong)
                         )
                         binding.noInboxFound.visibility = View.VISIBLE
                     }
                 })

         }else{
             binding.swipeRefresh.setRefreshing(false)
             Constant.showInternateConnectionDialog(this)
         }
     }*/

    private fun getNotificationList() {

        if (CommanUtils.isNetworkAvailable(this)!!) {
            val param = HashMap<String, String>()
            param.put("limit", "10")
            param.put("offset", "" + offset)
            val header = HashMap<String, String>()
            header.put("auth-token", getDataManager().getUserInfo().customer_auth_token)

            getDataManager().getNotificationListNew(param, header)
                ?.getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        Log.d("response", "response..." + response?.toString(4))
                        val jsonObject: JSONObject? =
                            response?.getJSONObject(StringConstant.settings)
                        val success: String? = jsonObject?.getString(StringConstant.success)
                        val message: String? = jsonObject?.getString(StringConstant.message)

                        if (success.equals("1")) {
                            count = jsonObject?.getString("count")
                            binding.progressLayout.visibility = View.GONE
                            binding.swipeRefresh.setRefreshing(false)
                            binding.noInboxFound.visibility = View.GONE
                            val notificationModel = getDataManager().mGson?.fromJson(
                                response.toString(),
                                NotificationModel::class.java
                            )
                            mNotificationList.addAll(notificationModel!!.data);
                            hideFooterLoiader()
                        }
                        if (mNotificationList.size == 0 && mNotificationList.isEmpty()) {
                            binding.swipeRefresh.setRefreshing(false)
                            binding.noInboxFound.visibility = View.VISIBLE
                            binding.progressLayout.visibility = View.GONE
                        }
                    }

                    override fun onError(anError: ANError) {
                        hideFooterLoiader()
                        binding.swipeRefresh.setRefreshing(false)
                        Constant.errorHandle(anError, getActivity())
                        // binding.noInboxFound.visibility = View.VISIBLE
                    }
                })

        } else {
            binding.swipeRefresh.setRefreshing(false)
            Constant.showInternetConnectionDialog(this)
        }
    }

    private fun hideFooterLoiader() {
        adapter.showLoading(false)
        adapter.notifyDataSetChanged()
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.iv_back -> {
                onBackPressed()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun getInboxInfo(
        notificationModal: NotificationModel.Data,
        position: Int,
        whichClick: String
    ) {

        if ("custom".equals(notificationModal.notification_code) || "Welcome".equals(
                notificationModal.notification_code,
                true
            )
        ) {
            val intent = Intent(this, CustomNotificationActivity::class.java)
            intent.putExtra("FromNotifications", notificationModal)
            startActivity(intent)
            //update NOtification read unread status
            updateNotificationStatus(notificationModal.notification_id, position)
        } else if ("News_feed".equals(notificationModal.notification_code)) {
            val intent = Intent(this, FeedDetailsActivity::class.java)
            intent.putExtra("FromNotifications", notificationModal)
            startActivity(intent)
            //update NOtification read unread status
            updateNotificationStatus(notificationModal.notification_id, position)
        } else if ("Program_plan".equals(notificationModal.notification_code)) {
            val intent = Intent(this, WorkOutPlanDetailActivity::class.java)
            intent.putExtra("FromNotifications", notificationModal)
            startActivity(intent)
            //update NOtification read unread status
            updateNotificationStatus(notificationModal.notification_id, position)

        } else if ("Workout".equals(notificationModal.notification_code)) {
            val intent = Intent(this, WorkOutDetailActivity::class.java)
            intent.putExtra("PROGRAM_DETAIL", notificationModal.notification_connection_id)
            intent.putExtra("isMyWorkout", "no")
            intent.putExtra("fromDeepLinking", "")
            startActivity(intent)

            //update NOtification read unread status
            updateNotificationStatus(notificationModal.notification_id, position)

        } else if ("Exercise".equals(notificationModal.notification_code)) {
            val intent = Intent(this, NotificationExerciesActivity::class.java)
            intent.putExtra("category_id", notificationModal.notification_connection_id)
            intent.putExtra("name", notificationModal.notification_code + " library")
            startActivity(intent)
            //update NOtification read unread status
            updateNotificationStatus(notificationModal.notification_id, position)
        } else if ("Stream_workout".equals(notificationModal.notification_code)) {
            val intent = Intent(this, StreamDetailActivity::class.java)
            var data = StreamDataModel.Settings.Data.RecentWorkout(
                "", "", "","", "",
                notificationModal.notification_connection_id + "", "", "", "", "", "","","")
            intent.putExtra("data", data)
            intent.putExtra("from", "no")
            startActivity(intent)
            //update NOtification read unread status
            updateNotificationStatus(notificationModal.notification_id, position)
        } else {

        }
        if ("Unread".equals(notificationModal.notification_status)) {
            var str=getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_NOTIFICATION_COUNT)
            if (!str!!.isEmpty()&& !str.equals("0")){
                var count =str.toInt()
                var MCount = (count - 1)
                getDataManager().setUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_NOTIFICATION_COUNT,MCount.toString())

            }
            else{
            }
        }

        /* //update NOtification read unread status
         updateNotificationStatus(notificationModal.notification_id, position)*/
    }

    private fun updateNotificationStatus(notificationId: String, position: Int) {

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
                    val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                    val success: String? = jsonObject?.getString(StringConstant.success)
                    val message: String? = jsonObject?.getString(StringConstant.message)
                    if (success.equals("1")) {

                        if (position < mNotificationList.size) {
                            val notificationModel = mNotificationList.get(position)
                            notificationModel.notification_status = "Read"
                            mNotificationList.set(position, notificationModel)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }

                override fun onError(anError: ANError) {
                    Constant.showCustomToast(
                        this@InboxActivity!!,
                        getString(R.string.something_wrong)
                    )
                    binding.noInboxFound.visibility = View.VISIBLE
                }
            })
    }


    override fun deleteInboxItem(data: NotificationModel.Data, position: Int, whichClick: String) {
        showDeleteDialog(position, data)
    }

    fun showDeleteDialog(pos: Int, data: NotificationModel.Data) {
        val dialog = Dialog(this)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )

        dialog.setContentView(R.layout.dialog_delete)
        val tv_no = dialog.findViewById<TextView>(R.id.tv_no)
        val tv_delete = dialog.findViewById<TextView>(R.id.tv_delete)

        dialog.tv_header.setText("Are you sure you want to delete this message?")
        tv_no.setOnClickListener { v -> dialog.dismiss() }

        tv_delete.setOnClickListener { v ->

            if (data.notification_id != null && !data.notification_id.isEmpty())
                deleteLog(pos, data)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun deleteLog(pos: Int, data: NotificationModel.Data) {

        val param = HashMap<String, String>()
        param.put(StringConstant.device_id, "")
        param.put(StringConstant.device_token, "")
        param.put(StringConstant.device_type, StringConstant.Android)
        param.put(
            StringConstant.auth_customer_id,
            getDataManager().getUserInfo().customer_auth_token
        )
        param.put("notification_id", data.notification_id)

        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        getDataManager().deleteNotification(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    try {
                        val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                        val status = json?.get(StringConstant.success)
                        val msg = json?.get(StringConstant.message)
                        if (status!!.equals("1")) {
                            mNotificationList.removeAt(pos)
                            adapter.notifyDataSetChanged()
                        }
                    } catch (ex: Exception) {
                        Constant.showCustomToast(
                            this@InboxActivity,
                            getString(R.string.something_wrong)
                        )
                    }
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, getActivity() as Activity)
                }
            })
    }
}
