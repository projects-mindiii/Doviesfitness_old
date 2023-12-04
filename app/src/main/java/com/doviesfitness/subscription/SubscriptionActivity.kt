package com.doviesfitness.subscription

import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.graphics.Rect
import android.graphics.Typeface
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.View
import com.android.billingclient.api.*
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.databinding.ActivitySubscriptionBinding
import com.doviesfitness.ui.authentication.signup.activity.PrivacyPolicyAndTACActivity
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.StringConstant
import eightbitlab.com.blurview.RenderScriptBlur
import kotlinx.android.synthetic.main.activity_subscription.*
import org.json.JSONObject
import android.os.SystemClock


class SubscriptionActivity : BaseActivity(), View.OnClickListener,
    MembershipAdapter.OnClickListener, PurchasesUpdatedListener {

    private var fromWorkOUtPlan: String = ""
    private lateinit var billingClient: BillingClient
    private var mLastClickTime: Long = 0
    private val skuList = listOf(
        "com.doviesfitness.1month4.99",
        "com.doviesfitness.3month14.99",
        "com.doviesfitness.12month.49.99"
    )
    var itemPos = 0
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.cancel_button -> {
                onBackPressed()
            }
            R.id.iv_back -> {
                onBackPressed()
            }
        }
    }

    //  {"settings":{"success":"0","message":"The Package ID field is required.\nThe Platform type field is required.\nThe Payment Platform type field is required.\nThe Package Name field is required.\nThe Platform Product ID field is required.\nThe Transaction Detail Json field is required.\nThe Purchase Token field is required.\n","fields":[]},"data":[]}


    lateinit var binding: ActivitySubscriptionBinding
    lateinit var adapter: MembershipAdapter
    lateinit var contentAdapter: StaticContentAdapter
    lateinit var successAdapter: SuccessStoryAdapter
    lateinit var packageListModel: PackageListModel.Data
    var packageMasterId = ""
    var platformProductId = ""

    var allPackageList = ArrayList<PackageListModel.Data.GetAllPackage>()
    var staticContentList = ArrayList<PackageListModel.Data.GetStaticContent>()
    var successStoryList = ArrayList<PackageListModel.Data.GetSuccessStory>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_subscription)
        initialization()
    }

    private fun initialization() {


        if (intent.hasExtra("fromWorkoutDetailActivity")) {
            if (intent.getStringExtra("fromWorkoutDetailActivity") != null) {
                fromWorkOUtPlan = intent.getStringExtra("fromWorkoutDetailActivity")!!
            }
        }

        if (intent.hasExtra("fromDietPlan")) {
            if (intent.getStringExtra("fromDietPlan") != null) {
                fromWorkOUtPlan = intent.getStringExtra("fromDietPlan")!!
            }
        }
        if (intent.hasExtra("exercise")) {
            if (intent.getStringExtra("exercise") != null) {
                fromWorkOUtPlan = intent.getStringExtra("exercise")!!
            }
        }
        hideNavigationBar()
        val windowBackground = window.decorView.background
        binding.topBlurView.setupWith(binding.containerId)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(this))
            .setBlurRadius(25f)
            .setHasFixedTransformationMatrix(true)

        if (intent.getStringExtra("home") != null && intent.getStringExtra("home").equals("yes")) {
            binding.cancelButton.visibility = View.VISIBLE
            binding.ivBack.visibility = View.GONE
        } else {
            binding.cancelButton.visibility = View.GONE
            binding.ivBack.visibility = View.VISIBLE
        }

        binding.cancelButton.setOnClickListener(this)
        binding.ivBack.setOnClickListener(this)
        if (Constant.isNetworkAvailable(this, main_layout)) {
            binding.progressLayout.visibility = View.VISIBLE
            getPackageList()
        }
        adapter = MembershipAdapter(getActivity(), allPackageList, this)
        val LManager = androidx.recyclerview.widget.LinearLayoutManager(
            getActivity(),
            androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.membershipRv.layoutManager = LManager
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen._6sdp)
        binding.membershipRv.addItemDecoration(SpacesItemDecoration(spacingInPixels))
        binding.membershipRv.adapter = adapter

        contentAdapter = StaticContentAdapter(getActivity(), staticContentList)
        val LManager2 = androidx.recyclerview.widget.LinearLayoutManager(getActivity())
        binding.staticRv.layoutManager = LManager2
        binding.staticRv.adapter = contentAdapter

        successAdapter = SuccessStoryAdapter(getActivity(), successStoryList)
        val LManager1 = androidx.recyclerview.widget.LinearLayoutManager(
            getActivity(),
            androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.succesStoryRv.layoutManager = LManager1
        val spacingInPixels1 = resources.getDimensionPixelSize(R.dimen._5sdp)
        binding.succesStoryRv.addItemDecoration(SpacesItemDecoration(spacingInPixels1))
        binding.succesStoryRv.adapter = successAdapter

        Log.d("auth token...", "auth token..." + getDataManager().getUserInfo().customer_auth_token)

        // completePayment("","")
    }


    override fun onItemCLick(pos: Int) {
        itemPos = pos

        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return
        } else {
            mLastClickTime = SystemClock.elapsedRealtime()
        }

        setupBillingClient()
        // startActivity(Intent(getActivity(),PurchaseActivity::class.java))
    }

    private fun setupBillingClient() {
        billingClient = BillingClient.newBuilder(this)
            .enablePendingPurchases()
            .setListener(this)
            .build()
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    logger("Setup Billing Done")
                    loadAllSKUs()
                    /// restore previous purchase
                    /* billingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.SUBS) {
                             responseCode, result ->
                         logger("PurchaseHistory..."+result.toString())
                     }
              var purchaseResult= billingClient.queryPurchases(BillingClient.SkuType.SUBS)
                     logger("Purchase result size..."+purchaseResult.purchasesList.size)
                     logger("Purchase result..."+purchaseResult.purchasesList.toString())*/

                    //   [PurchaseHistoryRecord. Json: {"productId":"dovies.subscription3months","purchaseToken":"mcnjedhljkmpfmhacnlnkdbi.AO-J1OylidWhJeA86xXkEbErqka3jXwhBcZkO6xXfINuLWAeS_YwqA2ONnkfSltH3CUpCtM5pQez1RM1aqrmfKjN92Ak8aD0WpX1l0qEmBtP9ZqoN-3FKQlqm6Af3RZZNWeXwJsDYmDr","purchaseTime":1574760167485,"developerPayload":null}, PurchaseHistoryRecord. Json: {"productId":"one_month","purchaseToken":"aampfanmcaahoccpkdijpbdc.AO-J1OwJY_R9Yx67ZF5JitXsMKjotSNgUdlIQvkWT8CXPnVRzXcCAvzSgQMhCFeyjp5M55uWthfXM-nJT9XdZ0h3A6DWa8BXohy_WFXiThTe0CuyxTifkXI","purchaseTime":1574759005607,"developerPayload":null}]
                }
            }

            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                logger("Failed")
            }
        })
    }

    private fun loadAllSKUs() = if (billingClient.isReady) {
        val params = SkuDetailsParams
            .newBuilder()
            .setSkusList(skuList)
            .setType(BillingClient.SkuType.SUBS)
            .build()
        billingClient.querySkuDetailsAsync(params) { billingResult, skuDetailsList ->
            // Process the result.
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && skuDetailsList!!.isNotEmpty()) {
                for (skuDetails in skuDetailsList!!) {
                    if (skuDetails.sku == allPackageList.get(itemPos).android_package_id) {
                        packageMasterId = allPackageList.get(itemPos).package_master_id
                        platformProductId = allPackageList.get(itemPos).android_package_id
                        val billingFlowParams = BillingFlowParams
                            .newBuilder()
                            .setSkuDetails(skuDetails)
//client billing dependency 3.0 related changes --------------------
                            //.setReplaceSkusProrationMode(BillingFlowParams.ProrationMode.IMMEDIATE_WITHOUT_PRORATION)
//------------------------------------------------------
                            .build()
                        billingClient.launchBillingFlow(this, billingFlowParams)
                    }

                }
            }
            if (skuDetailsList!!.size > 0) {
                logger("Desc: " + skuDetailsList!!.get(0).description)
                logger("Sku..: " + skuDetailsList.toString())
            } else {
                logger("Sku: " + skuDetailsList.toString())
            }
        }

    } else {
        println("Billing Client not ready")
    }

    /*  [SkuDetails: {"skuDetailsToken":"AEuhp4JQLtkqydt1_T7c75RVGWU-guxWEpSRtYYYj1cy_crjvtfDieUadPjr7n3iOW83","productId":"dovies.subscription3months","type":"subs","price":"₹380.00","price_amount_micros":380000000,"price_currency_code":"INR","subscriptionPeriod":"P3M","freeTrialPeriod":"P1W1D","title":"3 Month Premium Membership (Doviesfitness)","description":"Access to everything on the app."}, SkuDetails: {"skuDetailsToken":"AEuhp4Iir4Jx1GPNFregQyefBaIFiJ1bl5-BoOVnAM6AUjGJuGpdyaz06pGs6jIBAbTg","productId":"one_month","type":"subs","price":"₹150.00","price_amount_micros":150000000,"price_currency_code":"INR","subscriptionPeriod":"P1M","freeTrialPeriod":"P1W1D","title":"1 Month Premium Membership (Doviesfitness)","description":"Access to everything on the app."}, SkuDetails: {"skuDetailsToken":"AEuhp4JSiu8-tAwJIGwRWD6LRSpDnzZiye0mhewKCqNudq_I-wSHV8zL7gjKT0taYtDb","productId":"two_month","type":"subs","price":"₹1,150.00","price_amount_micros":1150000000,"price_currency_code":"INR","subscriptionPeriod":"P3M","freeTrialPeriod":"P1W1D","title":"1 Year Premium Membership (Doviesfitness)","description":"Access to everything on the app."}]

      PurchaseHistory...[PurchaseHistoryRecord. Json: {"productId":"dovies.subscription3months","purchaseToken":"mcnjedhljkmpfmhacnlnkdbi.AO-J1OylidWhJeA86xXkEbErqka3jXwhBcZkO6xXfINuLWAeS_YwqA2ONnkfSltH3CUpCtM5pQez1RM1aqrmfKjN92Ak8aD0WpX1l0qEmBtP9ZqoN-3FKQlqm6Af3RZZNWeXwJsDYmDr","purchaseTime":1574760167485,"developerPayload":null}, PurchaseHistoryRecord. Json: {"productId":"one_month","purchaseToken":"aampfanmcaahoccpkdijpbdc.AO-J1OwJY_R9Yx67ZF5JitXsMKjotSNgUdlIQvkWT8CXPnVRzXcCAvzSgQMhCFeyjp5M55uWthfXM-nJT9XdZ0h3A6DWa8BXohy_WFXiThTe0CuyxTifkXI","purchaseTime":1574759005607,"developerPayload":null}]

      {"orderId":"GPA.3357-5148-1375-98044","packageName":"com.doviesfitness","productId":"one_month","purchaseTime":1575013267601,"purchaseState":0,"purchaseToken":"afjppjkifndpegipgbphocnk.AO-J1OzU-vfLZo5KdpbiB7F19U-qV53L0viMrH-d_KdDrEFVkcNPkSDQmG_gutxJT0zRMPLo2Bex0TmSOl8QMDKffKPpNfpmglqGoONz2a8GO1GzoUmJ_gc","autoRenewing":true,"acknowledged":false}
  */


    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {
        if (billingResult?.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                logger("purchase data..." + purchase.toString())
                logger("purchase originalJson..." + purchase.originalJson)

                getDataManager().setStringData(
                    AppPreferencesHelper.PREF_KEY_SUBSCRIBED_USER_ID,
                    getDataManager().getUserInfo().customer_id
                )
                getDataManager().setStringData(
                    AppPreferencesHelper.PACKAGE_MASTER_ID,
                    packageMasterId
                )
                acknowledgePurchase(purchase.purchaseToken)
                binding.progressLayout.visibility = View.VISIBLE
                if (!purchase.originalJson.equals("")) {
                    var transactionJson = JSONObject()
                    transactionJson.put(StringConstant.quantity, "1")
                 //   transactionJson.put(StringConstant.productId, purchase.sku)//client billing 3.0 dependency
                    transactionJson.put(StringConstant.productId, purchase.skus)//changes client billing 4.0 dependency
                    transactionJson.put(StringConstant.transactionId, purchase.orderId)
                    transactionJson.put(StringConstant.packageName, purchase.packageName)
                    transactionJson.put(StringConstant.encodedKey, purchase.purchaseToken)
                    completePayment(purchase.originalJson.toString(), purchase.purchaseToken)
                    logger("transaction Json..." + transactionJson.toString())
                    logger("purchase original Json..." + purchase.originalJson)
                    logger("package_master_id..." + packageMasterId)
                    logger("platform_product_id..." + platformProductId)
                    logger("auth token..." + getDataManager().getUserInfo().customer_auth_token)
                } else {
                    completePayment("", "")
                }
            }
        } else if (billingResult?.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
            logger("User Cancelled")
            logger(billingResult?.debugMessage.toString())
        } else {
            logger(billingResult?.debugMessage.toString())
            // Handle any other error codes.
        }
    }

    private fun acknowledgePurchase(purchaseToken: String) {
        val params = AcknowledgePurchaseParams.newBuilder()
            .setPurchaseToken(purchaseToken)
            .build()
        billingClient.acknowledgePurchase(params) { billingResult ->
            val responseCode = billingResult.responseCode
            val debugMessage = billingResult.debugMessage
            logger("debugMessage..." + debugMessage)
            logger("responseCode.." + responseCode)
        }
    }

    class SpacesItemDecoration(space: Int) :
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
            outRect.right = space
            outRect.bottom = space
            val position = parent.getChildAdapterPosition(view)
            if (parent.getChildLayoutPosition(view) == 0) {
                outRect.left = space
            }
        }
    }

    private fun getPackageList() {
        val param = HashMap<String, String>()
        param.put(StringConstant.device_token, getDataManager().getUserInfo().customer_auth_token)
        param.put(StringConstant.device_id, "")
        param.put(StringConstant.device_type, StringConstant.Android)
        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        header.put(StringConstant.apiKey, StringConstant.HBDEV)
        header.put(StringConstant.apiVersion, StringConstant.apiVersionValue)
        getDataManager().getPackageList(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    binding.progressLayout.visibility = View.GONE
                    binding.mainLayout.visibility = View.VISIBLE
                    Log.d("TAG", "response...." + response!!.toString(4))
                    val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                    val data: JSONObject? = response?.getJSONObject(StringConstant.data)
                    val success: String? = jsonObject?.getString(StringConstant.success)
                    val message: String? = jsonObject?.getString(StringConstant.message)
                    if (success.equals("1")) {
                        val packageList = getDataManager().mGson?.fromJson(
                            response.toString(),
                            PackageListModel::class.java
                        )
                        packageListModel = packageList!!.data
                        setData(packageListModel)

                    } else {
                        binding.progressLayout.visibility = View.GONE
                        binding.mainLayout.visibility = View.VISIBLE
                        Constant.showCustomToast(getActivity(), getString(R.string.something_wrong))
                    }
                }
                override fun onError(anError: ANError) {
                    binding.progressLayout.visibility = View.GONE
                    binding.mainLayout.visibility = View.VISIBLE
                    Constant.errorHandle(anError, getActivity())
                    Constant.showCustomToast(getActivity(), getString(R.string.something_wrong))
                }
            })
    }

    private fun setData(packageListModel: PackageListModel.Data) {
        binding.successStoriesTxt.text = getString(R.string.success_stories)
        privacyPolicyAndTemsOfUse()
        binding.termsTitle.text = packageListModel.get_subscription_terms.get(0).title
        binding.termsDescription.text = packageListModel.get_subscription_terms.get(0).content
        allPackageList.addAll(packageListModel.get_all_packages)
        adapter.notifyDataSetChanged()
        staticContentList.addAll(packageListModel.get_static_content)
        contentAdapter.notifyDataSetChanged()
        successStoryList.addAll(packageListModel.get_success_story)
        successAdapter.notifyDataSetChanged()
        if (successStoryList.size>3)
            binding.ivSwipe.visibility=View.VISIBLE
        else
            binding.ivSwipe.visibility=View.GONE
    }

    private fun privacyPolicyAndTemsOfUse() {
        val builder = SpannableStringBuilder()
        val privacyPolicy = SpannableString(getString(R.string.privacy_policy))

        privacyPolicy.setSpan(StyleSpan(Typeface.BOLD), 0, privacyPolicy.length, 0)
        privacyPolicy.setSpan(UnderlineSpan(), 0, privacyPolicy.length, 0)
        privacyPolicy.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                //showCustomToast(this@SignupActivity,"Privacy Policy")
                startTCActivity("Privacy Policy")
            }
        }, 0, privacyPolicy.length, 0)
        privacyPolicy.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorWhite)),
            0,
            privacyPolicy.length,
            0
        )
        builder.append(privacyPolicy)
        val and = SpannableString(" " + getString(R.string.and) + " ")
        builder.append(and)
        val terms = SpannableString(getString(R.string.term_and_condition))
        terms.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                //showCustomToast(this@SignupActivity,"Terms of use")
                startTCActivity("tnc")
            }
        }, 0, terms.length, 0)
        terms.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorWhite)), 0,
            terms.length,
            0
        )
        terms.setSpan(UnderlineSpan(), 0, terms.length, 0)
        terms.setSpan(StyleSpan(Typeface.BOLD), 0, terms.length, 0)
        builder.append(terms)
        binding.tvTermsAndPrivacy.text = builder
        binding.tvTermsAndPrivacy.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun startTCActivity(str: String) {
        intent = Intent(this, PrivacyPolicyAndTACActivity::class.java)
        if (str.equals("tnc")) {
            intent.putExtra("from", "tnc")
        } else {
            intent.putExtra("from", "privacy policy")
        }
        startActivity(intent)
    }

    private fun completePayment(transactionDetail: String, purchase: String) {
        val param = HashMap<String, String>()
        param.put(StringConstant.packageMasterId, packageMasterId)
        param.put(StringConstant.platformType, "1")
        param.put(StringConstant.paymentPlatformType, "1")
        param.put(StringConstant.packageName, "com.doviesfitness")
        param.put(StringConstant.platformProductId, platformProductId)
        param.put(StringConstant.transactionDetail, transactionDetail)
        param.put(StringConstant.purchaseToken, purchase)
        val header = HashMap<String, String>()
        header.put(StringConstant.purchaseAuthToken, getDataManager().getUserInfo().customer_auth_token)
        getDataManager().completePayment(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    logger("complete payment...." + response.toString())
                    binding.progressLayout.visibility = View.GONE
                    binding.mainLayout.visibility = View.VISIBLE

                    val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                    val success: String? = jsonObject?.getString(StringConstant.success)
                    val message: String? = jsonObject?.getString(StringConstant.message)
                    if (success.equals("1")) {
                        Constant.showCustomToast(getActivity(), "" + message)
                        getSubscriptionStatus()
                    } else {
                        binding.progressLayout.visibility = View.GONE
                        binding.mainLayout.visibility = View.VISIBLE
                        //complete payment....{"settings":{"success":"0","message":"You have already purchased a plan, Please canceled your running plan.","fields":[]},"data":[]}
                        Constant.showCustomToast(getActivity(), "" + message)
                    }
                }

                override fun onError(anError: ANError) {
                    logger("complete payment body...." + anError.errorBody.toString())
                    logger("complete payment code...." + anError.errorCode.toString())
                    logger("complete payment detail...." + anError.errorDetail.toString())
                    binding.progressLayout.visibility = View.GONE
                    binding.mainLayout.visibility = View.VISIBLE
                    Constant.errorHandle(anError, getActivity())
                    Constant.showCustomToast(getActivity(), "" + anError.errorBody.toString())
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
        header.put(StringConstant.apiKey, StringConstant.HBDEV)
        header.put(StringConstant.apiVersion, "1")
        getDataManager().getSubscriptionStatus(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    binding.progressLayout.visibility = View.GONE
                    binding.mainLayout.visibility = View.VISIBLE
                    logger("Subscription status...." + response.toString())

                    val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                    val success: String? = jsonObject?.getString(StringConstant.success)
                    val message: String? = jsonObject?.getString(StringConstant.message)
                    if (success.equals("1")) {
                        val SubsResponse = getDataManager().mGson?.fromJson(
                            response.toString(),
                            SubscriptionModel::class.java
                        )
                        getFeaturedListApi()
                        getDataManager().setUserStringData(
                            AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED,
                            SubsResponse!!.data.is_subscribed
                        )
                        getDataManager().setUserStringData(
                            AppPreferencesHelper.PREF_KEY_APP_SUBSCRIPTION_TITLE,
                            SubsResponse!!.data.title
                        )

                        if (fromWorkOUtPlan.equals("fromWorkoutDetailActivity")) {
                            val intent = Intent()
                            intent.putExtra("MESSAGE", message)
                            setResult(2, intent)
                            finish()
                        }
                        else if (fromWorkOUtPlan.equals("fromDietPlan")) {
                            val intent = Intent()
                            intent.putExtra("MESSAGE", message)
                            setResult(4, intent)
                            finish()
                        }
                        else if (fromWorkOUtPlan.equals("yes")) {
                            val intent = Intent()
                            intent.putExtra("MESSAGE", message)
                            setResult(2, intent)
                            finish()
                        }
                        else {
                            finish()
                        }
                    } else {

                    }
                }

                override fun onError(anError: ANError) {
                    logger("Subscription error...." + anError.toString())
                    binding.progressLayout.visibility = View.GONE
                    binding.mainLayout.visibility = View.VISIBLE
                    Constant.errorHandle(anError, getActivity())
                    Constant.showCustomToast(getActivity(), "subscription status..." + anError.errorBody.toString())
                }
            })
    }


    private fun getFeaturedListApi() {
        val param = HashMap<String, String>()
        param.put(
            StringConstant.auth_customer_id,
            getDataManager().getUserInfo().customer_auth_token
        )
        param.put(StringConstant.device_id, "")
        param.put(
            StringConstant.device_token,
            getDataManager().getUserStringData(AppPreferencesHelper.PREF_FIREBASE_TOKEN)!!
        )
        param.put(StringConstant.device_type, StringConstant.Android)
        param.put(StringConstant.page_index, "1")
        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        getDataManager().feedListApi(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Log.d("response...", "response data..." + response!!.toString(3))
                }

                override fun onError(anError: ANError) {

                }
            })
    }


}