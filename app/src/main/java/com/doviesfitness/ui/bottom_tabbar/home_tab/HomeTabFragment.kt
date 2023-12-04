package com.doviesfitness.ui.bottom_tabbar.home_tab


import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.databinding.DataBindingUtil
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.android.billingclient.api.*
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.clubz.helper.SyncWorker

import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.data.local.AppPreferencesHelper.Companion.PACKAGE_MASTER_ID
import com.doviesfitness.data.local.AppPreferencesHelper.Companion.PREF_KEY_APP_CUSTOMER_NOTIFICATION_COUNT
import com.doviesfitness.data.local.AppPreferencesHelper.Companion.PREF_KEY_APP_CUSTOMER_USER_TYPE
import com.doviesfitness.ui.authentication.signup.model.UserInfoModal
import com.doviesfitness.ui.base.BaseFragment
import com.doviesfitness.ui.base.EndlessRecyclerViewScrollListener
import com.doviesfitness.ui.bottom_tabbar.HomeTabActivity
import com.doviesfitness.ui.bottom_tabbar.HomeTabActivity.Companion.fromNotificationClick
import com.doviesfitness.ui.bottom_tabbar.home_tab.activity.*
import com.doviesfitness.ui.bottom_tabbar.home_tab.adapter.FeatureAdapter
import com.doviesfitness.ui.bottom_tabbar.home_tab.adapter.FeedAdapter
import com.doviesfitness.ui.bottom_tabbar.home_tab.fragment.DietPlanDetailFragment
import com.doviesfitness.ui.bottom_tabbar.home_tab.fragment.WorkoutPlanDetailFragment
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.AllOtherThenFeatured
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.FeaturedNews
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.FeedListResponce
import com.doviesfitness.ui.bottom_tabbar.stream_tab.activities.StreamDetailActivity
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.StreamDataModel
import com.doviesfitness.ui.profile.inbox.activity.InboxActivity
import com.doviesfitness.utils.CommanUtils
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.Constant.Companion.COMMENT_COUNT_CONSTANT
import com.doviesfitness.utils.Constant.Companion.LIKE_COMMENT_COUNT_CONSTANT
import com.doviesfitness.utils.StringConstant
import com.doviesfitness.utils.StringConstant.Companion.authToken
import com.doviesfitness.utils.StringConstant.Companion.Android
import com.doviesfitness.utils.StringConstant.Companion.newsFeed
import com.doviesfitness.utils.StringConstant.Companion.auth_customer_id
import com.doviesfitness.utils.StringConstant.Companion.device_id
import com.doviesfitness.utils.StringConstant.Companion.device_token
import com.doviesfitness.utils.StringConstant.Companion.device_type
import com.doviesfitness.utils.StringConstant.Companion.module_name
import com.doviesfitness.utils.StringConstant.Companion.page_index
import eightbitlab.com.blurview.RenderScriptBlur
import kotlinx.android.synthetic.main.activity_home_tab.*
import org.json.JSONObject

 class HomeTabFragment : BaseFragment(), View.OnClickListener, FeedAdapter.FeedListOnClick,
    FeatureAdapter.FeaturedItemClick, PurchasesUpdatedListener {


    var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            var str = getDataManager().getUserStringData(PREF_KEY_APP_CUSTOMER_NOTIFICATION_COUNT)

            if (!str!!.isEmpty()&& !str.equals("0")){
                var count =str.toInt()
                binding.msgCount.visibility = View.VISIBLE
                binding.msgCount.setText("" + count)
            }
            else{
                binding.msgCount.visibility = View.GONE

            }
        }
    }

    override fun onPause() {
        super.onPause()
        activity1.unregisterReceiver(broadcastReceiver)
    }

    override fun onPurchasesUpdated(p0: BillingResult, purchases: MutableList<Purchase>?) {
        //To change body of created functions use File | Settings | File Templates.
    }

    private lateinit var homeTabActivity: HomeTabActivity
    private var batchCount: String = ""
    lateinit private var featureAdapter: FeatureAdapter
    lateinit private var feedAdapter: FeedAdapter
    var feedList = ArrayList<AllOtherThenFeatured>()
    var featureList = ArrayList<FeaturedNews>()
    private var page1: Int = 1
    private var module_id = ""
    lateinit var activity1: Activity
    lateinit var binding: com.doviesfitness.databinding.FragmentHomeTabBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity!!.window.setStatusBarColor(
                ContextCompat.getColor(mContext, R.color.splash_screen_color)
            )
        }

        val view = activity!!.window.decorView
        view.systemUiVisibility =
            view.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()

        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        /*  val view = activity!!.window.decorView
          view.systemUiVisibility = view.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR*/

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_tab, container, false)
        initialization()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity1 = activity as HomeTabActivity
    }

    private fun initialization() {


        binding.ivNavigation.setOnClickListener(this)
        binding.inboxIcon.setOnClickListener(this)

        val windowBackground = activity!!.window.decorView.background
        binding.topBlurView.setupWith(binding.containerId)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(mContext))
            .setBlurRadius(25f)
            .setHasFixedTransformationMatrix(true)

        arguments?.let {
            module_id = arguments!!.getString("module_id")!!
            if (!module_id.isEmpty() || !module_id.equals("") || !module_id.equals("0")) {
                val intent = Intent(mContext, FeedDetailsActivity::class.java)
                intent.putExtra("module_id", module_id)
                startActivity(intent)
            }
        }

        //mergeCode

        setAdapter()
        binding.rltvLoader.visibility=View.VISIBLE
        getFeturedListApi(page1)

        //*Swipe to refresh*//*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            binding.swipeRefresh.setProgressViewOffset(
                false,
                resources.getDimension(R.dimen._80sdp).toInt(),
                resources.getDimension(R.dimen._120sdp).toInt()
            )
        }

        binding.swipeRefresh.setOnRefreshListener(androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener {
            //swipe to refresh rcyclerview data
            page1 = 1
            binding.swipeRefresh.setRefreshing(false)
            getFeturedListApi(page1)
        })

        // becoz of manage batch count only

        // manage batch count
        val userInfoBean = getDataManager().getUserInfo()
        if (getDataManager().getUserStringData(PREF_KEY_APP_CUSTOMER_NOTIFICATION_COUNT).equals("0")) {
            binding.msgCount.visibility = View.GONE
        } else {
            binding.msgCount.visibility = View.VISIBLE
            if (getDataManager().getUserStringData(PREF_KEY_APP_CUSTOMER_NOTIFICATION_COUNT).equals(
                    "on",
                    true
                )
            ) {

            } else {
                binding.msgCount.setText("" + getDataManager().getUserStringData(PREF_KEY_APP_CUSTOMER_NOTIFICATION_COUNT))
            }
        }


        getUSerDetail()
         if (!fromNotificationClick.equals("3"))
        getNotificationCount()


    }

    // to get data  when we come from notification
    fun newInstance(module_id: String): HomeTabFragment {
        val myFragment = HomeTabFragment()
        val args = Bundle()
        args.putString("module_id", module_id)
        myFragment.setArguments(args)

        return myFragment
    }

    /*  companion object {
          @JvmStatic
          fun newInstance(param1: String, param2: String) =
              HomeTabFragment().apply {
                  arguments = Bundle().apply {
                  }
              }
      }*/

    fun scrollUp() {
        if (feedList.size > 0)
            binding.nestedSv.smoothScrollTo(0, 0)
    }


    override fun onClick(view: View?) {

        when (view!!.id) {

            R.id.iv_navigation -> {

                if ((activity as HomeTabActivity).drawer_layout.isDrawerOpen(Gravity.START)) {
                    (activity as HomeTabActivity).drawer_layout.closeDrawer(Gravity.START)
                } else {
                    (activity as HomeTabActivity).drawer_layout.openDrawer(Gravity.START)
                }
            }
            R.id.inbox_icon -> {
                //if (!binding.msgCount.text.toString().equals("0")) {
                fromNotificationClick = "1"
                val intent = Intent(mContext, InboxActivity::class.java)
                startActivity(intent)
                //}
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

                        val userInfoModal = getDataManager().mGson?.fromJson(response.toString(), UserInfoModal::class.java)

                        val parseUserData = userInfoModal!!.data.get(0)
                        val userInfoBean = getDataManager().getUserInfo()
                        userInfoBean.customer_full_name = parseUserData.customer_full_name
                        userInfoBean.customer_user_name = parseUserData.customer_user_name
                        userInfoBean.customer_profile_image = parseUserData.customer_profile_image
                        userInfoBean.customer_gender = parseUserData.customer_gender
                        userInfoBean.customer_email = parseUserData.customer_email
                        var previewStatus = parseUserData.workout_preview_status
                        getDataManager().setPreferanceStatus(AppPreferencesHelper.PREF_KEY_APP_PREVIEW_STATUS, previewStatus)

                        userInfoBean.notification_status = ""
                        if (parseUserData.dob!=null)
                        userInfoBean.dob = parseUserData.dob
                        else
                        userInfoBean.dob = ""
                        userInfoBean.customer_status = parseUserData.customer_status
                        userInfoBean.customer_height = parseUserData.customer_height
                        userInfoBean.customer_weight = parseUserData.customer_weight
                        getDataManager().setUserInfo(userInfoBean)
                        getDataManager().setUserStringData(PREF_KEY_APP_CUSTOMER_USER_TYPE, parseUserData.customer_user_type)

                        Log.v("response", "response customer_status.." + parseUserData.customer_status)

                    } else {
                        Constant.showCustomToast(mContext, "" + msg)
                    }
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, mContext as Activity)
                }
            })
    }

    private fun getNotificationCount() {

        val header = HashMap<String, String>()
        header.put("auth-token", getDataManager().getUserInfo().customer_auth_token)
        getDataManager().getNotificationCount(header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                    val status = json?.get(StringConstant.success)
                    val msg = json?.get(StringConstant.message)
                    if (status!!.equals("1")) {
                        val dataObj: JSONObject? = json.getJSONObject("data")
                        var notificationCount = dataObj!!.getString("unreadCount")
                        getDataManager().setUserStringData(
                            PREF_KEY_APP_CUSTOMER_NOTIFICATION_COUNT,
                            notificationCount
                        )
                        Log.d("count", "count...api" + notificationCount)
                        if (notificationCount.equals("0")) {
                            binding.msgCount.visibility = View.GONE
                        } else {
                            binding.msgCount.visibility = View.VISIBLE
                            binding.msgCount.setText("" + notificationCount)
                        }
                        if (fromNotificationClick.equals("2")) {
                            homeTabActivity = activity as HomeTabActivity
                            homeTabActivity.updateUi()
                        }

                    } else {
                    }
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, mContext as Activity)
                }
            })
    }

    override fun onResume() {
        super.onResume()
        var filter =  IntentFilter("INTENT_FILTER_NAME")
        activity1.registerReceiver(broadcastReceiver, filter)
        getNotificationCount()
        if (fromNotificationClick.equals("1") || fromNotificationClick.equals("2")) {
            fromNotificationClick = "0"
            Log.v("Shubham", "2")
            //  getUSerDetail()
          //  getNotificationCount()
        }
        else if (fromNotificationClick.equals("3")) {
            fromNotificationClick="0"
            var str=getDataManager().getUserStringData(PREF_KEY_APP_CUSTOMER_NOTIFICATION_COUNT)
            if (!str!!.isEmpty()&& !str.equals("0")){
                var count =str.toInt()
                var MCount = (count - 1)
                if (MCount!=0){
                    binding.msgCount.visibility = View.VISIBLE
                    binding.msgCount.setText("" + MCount)
                }
                else{
                    binding.msgCount.visibility = View.GONE
                }
                getDataManager().setUserStringData(PREF_KEY_APP_CUSTOMER_NOTIFICATION_COUNT,MCount.toString())
            }
            else{
                binding.msgCount.visibility = View.GONE

            }

        } else {
            var str=getDataManager().getUserStringData(PREF_KEY_APP_CUSTOMER_NOTIFICATION_COUNT)
            if (!str!!.isEmpty()&& !str.equals("0")){
                var count =str.toInt()
                    binding.msgCount.visibility = View.VISIBLE
                    binding.msgCount.setText("" + count)
            }
            else{
                binding.msgCount.visibility = View.GONE

            }

        }
    }

    private fun setAdapter() {
        featureAdapter = FeatureAdapter(context!!, this.featureList, this)
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen._5sdp)
       binding.rvFeatured.addItemDecoration(MyItemDecoration(spacingInPixels))
        val gridLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            context,
            androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.rvFeatured.layoutManager = gridLayoutManager
        binding.rvFeatured.adapter = featureAdapter

        feedAdapter = FeedAdapter(context!!, this.feedList, this)
        val layoutManager1 = androidx.recyclerview.widget.LinearLayoutManager(context)
        binding.rvFeed.layoutManager = layoutManager1
        binding.rvFeed.adapter = feedAdapter


        ///********Pagination Feed List*********////
        binding.rvFeed.addOnScrollListener(object :
            EndlessRecyclerViewScrollListener(layoutManager1) {
            override fun onLoadMore(
                page: Int,
                totalItemsCount: Int,
                view: androidx.recyclerview.widget.RecyclerView
            ) {
                if (page != 0) {
                    feedAdapter.showLoading(true)
                    page1 = page
                    getFeturedListApi(page1)
                }
            }
        })


        /*  *//*Pagination Featured List*//*
        rv_featured.addOnScrollListener(object : EndlessRecyclerViewScrollListener(gridLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                if (page != 1) {
                    page1 = page
                    getFeturedListApi(page1)
                }
            }
        })*/
    }


    /*FEATURED AND FEED LIST API CALL*/
    private fun getFeturedListApi(page: Int) {

        if (CommanUtils.isNetworkAvailable(mContext)!!) {
            val param = HashMap<String, String>()
            param.put(auth_customer_id, getDataManager().getUserInfo().customer_auth_token)
            param.put(device_id, "")
            param.put(
                device_token,
                getDataManager().getUserStringData(AppPreferencesHelper.PREF_FIREBASE_TOKEN)!!
            )
            param.put(device_type, Android)
            param.put(page_index, "" + page)
            val header = HashMap<String, String>()
            header.put(authToken, getDataManager().getUserInfo().customer_auth_token)
            getDataManager().feedListApi(param, header)
                ?.getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        binding.rltvLoader.visibility=View.GONE

                        Log.d("resonse...", "response data..." + response!!.toString(3))
                        try {
                            binding.swipeRefresh.setRefreshing(false)
                            featureList.clear()
                            feedList.clear()
                            feedAdapter.notifyDataSetChanged()
                            featureAdapter.notifyDataSetChanged()
                            val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                            val status = json?.get(StringConstant.success)
                            val msg = json?.get(StringConstant.message)
                            if (status!!.equals("1")) {
                                val feedList1 = getDataManager().mGson?.fromJson(
                                    response.toString(),
                                    FeedListResponce::class.java
                                )
                                Log.d(
                                    "subscribed data...",
                                    "isReceiptReceived..." + feedList1?.data?.isReceiptReceived + "....ios_package_id..."
                                            + feedList1?.data?.ios_package_id + "...subscribe user id..." + getDataManager().getStringData(
                                        AppPreferencesHelper.PREF_KEY_SUBSCRIBED_USER_ID
                                    )
                                            + "....is subscribed...." + getDataManager().getUserInfo().is_subscribed
                                )

                                if (feedList1?.data?.isReceiptReceived.equals("0")) {
                                    if (!feedList1?.data?.ios_package_id?.isEmpty()!!) {
                                        setupBillingClient()
                                    }
                                } else if (feedList1?.data?.isReceiptReceived.equals("2")) {
                                    if (getDataManager().getStringData(AppPreferencesHelper.PREF_KEY_SUBSCRIBED_USER_ID) != null
                                        && getDataManager().getStringData(AppPreferencesHelper.PREF_KEY_SUBSCRIBED_USER_ID)
                                            .equals(getDataManager().getUserInfo().customer_id)
                                    ) {
                                        setupBillingClient()
                                    }
                                } else {

                                }

                                feedList.addAll(feedList1!!.data.all_other_then_featured)
                                featureList.addAll(feedList1.data.featured_news)
                                    //  featureList.addAll(feedList1.data.featured_news)
                            //    featureList.addAll(feedList1.data.featured_news)



                                //hide heade feature view from top
                                if (featureList.isEmpty() || featureList.size == 0) {
                                    binding.rlFeatured.visibility = View.GONE
                                } else {
                                    binding.rlFeatured.visibility = View.VISIBLE
                                    feedAdapter.notifyDataSetChanged()
                                }

                                if (getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!.equals(
                                        "No",
                                        true
                                    )
                                ) {
                                    val mWorkManager = WorkManager.getInstance()
                                    mWorkManager.enqueue(OneTimeWorkRequest.from(SyncWorker::class.java))
                                }
                                hideFooterLoiader()

                            } else {
                                binding.rltvLoader.visibility=View.GONE

                                //  Constant.showCustomToast(mContext, "" + msg)
                            }
                        } catch (ec: Exception) {
                            binding.rltvLoader.visibility=View.GONE

                            if (mContext != null && isAdded)
                                Constant.showCustomToast(
                                    mContext,
                                    getString(R.string.something_wrong)
                                )
                        }
                    }

                    override fun onError(anError: ANError) {
                        binding.rltvLoader.visibility=View.GONE

                        hideFooterLoiader()
                        if (mActivity != null && isAdded)
                            Constant.errorHandle(anError, mActivity!!)
                    }

                })
        } else {
            binding.rltvLoader.visibility=View.GONE
            binding.swipeRefresh.setRefreshing(false)
            Constant.showInternetConnectionDialog(mContext as Activity)
        }
    }

    private fun setupBillingClient() {
        var billingClient: BillingClient
        billingClient = BillingClient.newBuilder(mContext)
            .enablePendingPurchases()
            .setListener(this)
            .build()


        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    Log.d("Home fragment", "Setup Billing Done")
                    billingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.SUBS) { responseCode, result ->
                        Log.d("Home fragment", "PurchaseHistory..." + result.toString())
                    }
                    var purchaseResult = billingClient.queryPurchases(BillingClient.SkuType.SUBS)
                    Log.d(
                        "Home fragment",
                        "Purchase result size..." + purchaseResult.purchasesList!!.size
                    )
                    Log.d(
                        "Home fragment",
                        "Purchase result..." + purchaseResult.purchasesList.toString()
                    )

                    purchaseResult.purchasesList!!.get(0).originalJson
                    if (!purchaseResult.purchasesList!!.get(0).originalJson.equals("")) {
                        /*var transactionJson=JSONObject()
                        transactionJson.put("quantity","1")
                        transactionJson.put("product_id",purchaseResult.purchasesList.get(0).sku)
                        transactionJson.put("transaction_id",purchaseResult.purchasesList.get(0).orderId)
                        transactionJson.put("package_name",purchaseResult.purchasesList.get(0).packageName)
                        transactionJson.put("encoded_key",purchaseResult.purchasesList.get(0).purchaseToken)*/
                        completePayment(
                            purchaseResult.purchasesList!!.get(0).originalJson.toString(),
                            purchaseResult.purchasesList!!.get(0)
                        )
                    }

                }
            }

            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                Log.d("Home fragment", "Failed")

            }
        })


    }


    private fun hideFooterLoiader() {
        if (featureList != null && featureList.size > 2) {
            //binding.rightArrow.visibility = View.VISIBLE
        } else {
            // binding.rightArrow.visibility = View.GONE
        }
        feedAdapter.showLoading(false)
        feedAdapter.notifyDataSetChanged()
        featureAdapter.notifyDataSetChanged()
    }


    override fun tvCommentsOnclick(featuredData: AllOtherThenFeatured, pos: Int) {
        val intent = Intent(mContext, CommentsActivity::class.java)
        intent.putExtra("newsId", featuredData.news_id)
        intent.putExtra("position", pos)
        startActivityForResult(intent, COMMENT_COUNT_CONSTANT)
    }

    override fun ivLikeOnclick(featuredData: AllOtherThenFeatured, pos: Int, ivHeart: ImageView) {
        ivHeart.isEnabled = false
        val param = HashMap<String, String>()
        param.put(auth_customer_id, getDataManager().getUserInfo().customer_auth_token)
        param.put(module_name, newsFeed)
        param.put(StringConstant.module_id, featuredData.news_id)
        val header = HashMap<String, String>()
        header.put(authToken, getDataManager().getUserInfo().customer_auth_token)
        getDataManager().likeUnlikeApi(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                    val success: String? = jsonObject?.getString(StringConstant.success)
                    val message: String? = jsonObject?.getString(StringConstant.message)
                    if (success.equals("1")) {

                        if (featuredData.news_like_status.equals("0")) {
                            featuredData.news_like_status = "1"
                            val likeCount = featuredData.customer_likes.toInt() + 1
                            featuredData.customer_likes = likeCount.toString()
                            feedAdapter.notifyDataSetChanged()
                        } else {
                            featuredData.news_like_status = "0"
                            val likeCount = featuredData.customer_likes.toInt() - 1
                            featuredData.customer_likes = likeCount.toString()
                            feedAdapter.notifyDataSetChanged()
                        }
                        ivHeart.isEnabled = true

                    } else Constant.showCustomToast(context!!, "" + message)
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, activity!!)
                }
            })
    }

    override fun ivShareOnclick(featuredData: AllOtherThenFeatured, pos: Int) {
        mActivity?.sharePost(featuredData.news_share_url)
    }

    override fun ivFavOnclick(featuredData: AllOtherThenFeatured, pos: Int) {
        val param = HashMap<String, String>()
        param.put(auth_customer_id, getDataManager().getUserInfo().customer_auth_token)
        param.put(module_name, newsFeed)
        param.put(StringConstant.module_id, featuredData.news_id)
        val header = HashMap<String, String>()
        header.put(authToken, getDataManager().getUserInfo().customer_auth_token)
        getDataManager().makeFavoriteApi(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                    val success: String? = jsonObject?.getString(StringConstant.success)
                    val message: String? = jsonObject?.getString(StringConstant.message)
                    if (success.equals("1")) {
                        if (featuredData.news_fav_status.equals("0")) {
                            featuredData.news_fav_status = "1"
                            feedAdapter.notifyDataSetChanged()
                        } else {
                            featuredData.news_fav_status = "0"
                            feedAdapter.notifyDataSetChanged()
                        }
                    } else Constant.showCustomToast(context!!, "" + message)
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, activity!!)
                    Constant.showCustomToast(context!!, getString(R.string.something_wrong))
                }
            })
    }

    override fun featureItemOnClick(featuredNews: FeaturedNews) {
        when {
            featuredNews.news_media_type.equals("WORKOUT") -> {
                val intent = Intent(mContext, WorkOutDetailActivity::class.java)
                intent.putExtra("PROGRAM_DETAIL", featuredNews.news_module_id)
                intent.putExtra("fromHomeScreen", "fromHomeScreen")
                intent.putExtra("isMyWorkout", "no")
                intent.putExtra("fromDeepLinking", "")
                startActivity(intent)
            }
            featuredNews.news_media_type.equals("PROGRAM") -> {
                getBaseActivity()?.addFragment(
                    WorkoutPlanDetailFragment.newInstance(featuredNews),
                    R.id.container_id1,
                    true
                )
            }

            featuredNews.news_media_type.equals("Video") -> {
                val intent = Intent(mContext, VideoDetailActivity::class.java)
                intent.putExtra("PROGRAM_DETAIL", featuredNews)
                intent.putExtra("fromHomeScreen", "fromHomeScreen")
                startActivity(intent)
            }
            featuredNews.news_media_type.equals("STREAM_WORKOUT") -> {
                val intent = Intent(mContext, StreamDetailActivity::class.java)
                var data = StreamDataModel.Settings.Data.RecentWorkout(
                    "", "", "","", "",
                    featuredNews.news_module_id + "", "", "", "", "", "","","")
                intent.putExtra("data", data)
                intent.putExtra("from", "no")
                startActivity(intent)
            }


            featuredNews.news_media_type.equals("Image") -> {
                // Constant.showCustomToast(mContext, featuredNews.news_media_type)
            }
            featuredNews.news_media_type.equals("DIET_PLAN") -> {
                getBaseActivity()?.addFragment(
                    DietPlanDetailFragment.newInstance(featuredNews),
                    R.id.container_id1,
                    true
                )
            }
        }
    }

    override fun tvDescriptioOnclick(featuredData: AllOtherThenFeatured, pos: Int) {
        val intent = Intent(mContext, FeedDetailsActivity::class.java)
        intent.putExtra("FeaturedData", featuredData)
        intent.putExtra("Position", pos)
        startActivityForResult(intent, LIKE_COMMENT_COUNT_CONSTANT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == COMMENT_COUNT_CONSTANT && data != null) {
            val CommentCount = data.getIntExtra("CommentCount", 0)
            val position = data.getIntExtra("position", 0)

            //Caused by: java.lang.IndexOutOfBoundsException: Index: 13, Size: 4
            //        at java.util.ArrayList.get(ArrayList.java:437)
            //        at com.doviesfitness.ui.bottom_tabbar.home_tab.HomeTabFragment.onActivityResult(HomeTabFragment.kt:566)

            ////will solve it
            if (position < feedList.size)
                feedList.get(position).feed_comments_count = CommentCount.toString()
            feedAdapter.notifyDataSetChanged()
        }
        if (requestCode == LIKE_COMMENT_COUNT_CONSTANT && data != null) {
            val featuredData = data.getParcelableExtra<AllOtherThenFeatured>("FeaturedData")!!
            //val featuredData = data.getParcelableExtra<String>("FeaturedData")!! as AllOtherThenFeatured
            val position = data.getIntExtra("position", 0)
            feedList.get(position).feed_comments_count = featuredData.feed_comments_count
            feedList.get(position).customer_likes = featuredData.customer_likes
            feedList.get(position).news_fav_status = featuredData.news_fav_status
            feedList.get(position).news_like_status = featuredData.news_like_status
            feedAdapter.notifyDataSetChanged()
        }
    }

    class MyItemDecoration(space: Int) :
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
            outRect.top = space
            outRect.bottom = space
            //  outRect.left=space/2
            //   outRect.right=space/2

            val position = parent.getChildAdapterPosition(view)
            // Add top margin only for the first item to avoid double space between items
            /*  if (parent.getChildLayoutPosition(view) == 0) {
                  outRect.right = space
              }
              else*/
            if (parent.getChildLayoutPosition(view) == 0) {
                outRect.left = space + space / 2
                outRect.right = space / 2
            } else if (position == parent.getAdapter()!!.getItemCount() - 1) {
                outRect.right = space + space / 2
                outRect.left = space

            } else {
                outRect.right = space / 2
                outRect.left = space
            }

            /* if (parent.getChildLayoutPosition(view)== 0||parent.getChildLayoutPosition(view)== 1) { }
             else{ outRect.top = space*2 }
             if (parent.getChildLayoutPosition(view)%2 == 0) { outRect.right = space }
             else{ outRect.left = space }*/
        }
    }

    private fun completePayment(transactionDetail: String, purchase: Purchase) {
        val param = HashMap<String, String>()
        param.put(
            "package_master_id",
            "" + getDataManager().getStringData(PACKAGE_MASTER_ID).toString()
        )
        param.put("platform_type", "1")
        param.put("payment_platform_type", "1")
        param.put("package_name", "com.doviesfitness")
        param.put("platform_product_id", purchase.skus.toString())
        //param.put("platform_product_id", purchase.getProducts().get(0))
        param.put("transaction_detail", transactionDetail)
        param.put("purchase_token", purchase.purchaseToken)
        val header = HashMap<String, String>()
        header.put("auth-token", getDataManager().getUserInfo().customer_auth_token)
        getDataManager().completePayment(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Log.d("Home fragment", "complete payment...." + response.toString())


                    // binding.progressLayout.visibility=View.GONE
                    //   binding.mainLayout.visibility=View.VISIBLE

                    val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                    val success: String? = jsonObject?.getString(StringConstant.success)
                    val message: String? = jsonObject?.getString(StringConstant.message)
                    if (success.equals("1")) {

                        //   Constant.showCustomToast(mContext,""+message )
                        //  getSubscriptionStatus()

                    } else {
                        //    binding.progressLayout.visibility=View.GONE
                        //   binding.mainLayout.visibility=View.VISIBLE
                        //   Constant.showCustomToast(mContext, getString(R.string.something_wrong))
                    }
                }

                override fun onError(anError: ANError) {
                    Log.d("Home fragment", "complete paymente error...." + anError.toString())
                    //  binding.progressLayout.visibility=View.GONE
                    //  binding.mainLayout.visibility=View.VISIBLE
                    //    Constant.errorHandle(anError, getActivity())
                    //  Constant.showCustomToast(mContext, getString(R.string.something_wrong))
                }
            })
    }

}
