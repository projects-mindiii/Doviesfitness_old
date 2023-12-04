package com.doviesfitness.ui.bottom_tabbar.home_tab.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.doviesfitness.Doviesfitness
import com.doviesfitness.R
import com.doviesfitness.allDialogs.ExclusiveWorkoutDialog
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.databinding.ActivityWorkOutPlanDetailBinding
import com.doviesfitness.subscription.SubscriptionActivity
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.FeaturedNews
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.PlanDetailsResponse
import com.doviesfitness.ui.bottom_tabbar.workout_plan.modal.WorkOutPlanDetailModal
import com.doviesfitness.ui.bottom_tabbar.workout_plan.modal.WorkOutPlanModal
import com.doviesfitness.ui.profile.inbox.modal.NotificationModel
import com.doviesfitness.ui.profile.profile_my_plan.MyPlanFromProfileFragment
import com.doviesfitness.utils.CommanUtils
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.StringConstant
import com.doviesfitness.utils.StringConstant.Companion.Android
import com.doviesfitness.utils.StringConstant.Companion.authToken
import com.doviesfitness.utils.StringConstant.Companion.auth_customer_id
import com.doviesfitness.utils.StringConstant.Companion.device_id
import com.doviesfitness.utils.StringConstant.Companion.device_type
import com.doviesfitness.utils.StringConstant.Companion.program_id
import org.json.JSONObject

class WorkOutPlanDetailActivity : BaseActivity(), View.OnClickListener {
    private var featuredNewsNews_module_id: String = ""
    private var feature_workout_module_id: String = ""
    private var fromNotification_module_id: String = ""
    private lateinit var workoutDetailModal: WorkOutPlanDetailModal.Data
    private var fromDeepLinking: String = ""
    private var isAdmin: String = ""
    private var workOutPlanModal: WorkOutPlanModal.Data.GetAllProgram? = null
    private lateinit var featuredNews: FeaturedNews
    lateinit var binding: ActivityWorkOutPlanDetailBinding
    var shareUrl = ""
    var from = ""
    private var mLastClickTime: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inItView()
    }
    var exclusiveUrl = "https://www.doviesfitness.com/"




    private fun inItView() {
        hideNavigationBar()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_work_out_plan_detail)

        binding.ivBack.setOnClickListener(this)
        binding.ivShare.setOnClickListener(this)
        binding.btnAddToMyPlan.setOnClickListener(this)

        binding.btnExclusive.setOnClickListener(this)

        val params = binding.ivFeatured.layoutParams as RelativeLayout.LayoutParams
        val dpWidth = CommanUtils.getWidthAndHeight(this)
        params.height = dpWidth + 300
        binding.ivFeatured.layoutParams = params

        if (intent.hasExtra("from")) {
            from = intent.getStringExtra("from")!!
        }

        if (intent.hasExtra("PROGRAM_DETAIL")) {
            featuredNews = intent.getParcelableExtra<FeaturedNews>("PROGRAM_DETAIL")!!

            binding.tvHeader.text = featuredNews.news_title
            Glide.with(binding.ivFeatured.context).load(featuredNews.news_image)
                .placeholder(R.color.colorBlack).into(binding.ivFeatured)

            featuredNewsNews_module_id = featuredNews.news_module_id
            getProgramDetailActvitity(featuredNews.news_module_id)

        } else if (intent.hasExtra("FROM_WORKOUT_PLAN")) {
            workOutPlanModal =
                intent.getParcelableExtra<WorkOutPlanModal.Data.GetAllProgram>("FROM_WORKOUT_PLAN")!!
            binding.tvHeader.text = workOutPlanModal?.program_name

            feature_workout_module_id = workOutPlanModal!!.program_id
            getDietPlanDetailsApi(workOutPlanModal!!.program_id)
        } else if (intent.hasExtra("FromNotifications")) {
            // when come from notificatin list not direct
            val fromNOtificationModal =
                intent.getSerializableExtra("FromNotifications") as NotificationModel.Data

            fromNotification_module_id = fromNOtificationModal.notification_connection_id
            getDietPlanDetailsApi(fromNOtificationModal.notification_connection_id)

        } else if (intent.hasExtra("FromDirectNotification")) {
            // when come from notificatin direct
            feature_workout_module_id = intent.getStringExtra("FromDirectNotification")!!
            fromDeepLinking = intent.getStringExtra("fromDeepLinking")!!
            getDietPlanDetailsApi(feature_workout_module_id)
        }
    }

    private fun getDietPlanDetailsApi(program_id: String) {

        if (CommanUtils.isNetworkAvailable(this)) {

            binding.rlLoader.visibility = View.VISIBLE
            Log.v("myLog", "1")
            val param = HashMap<String, String>()
            param.put(device_type, Android)
            param.put(
                auth_customer_id, getDataManager().getUserInfo().customer_auth_token
            )
            param.put(StringConstant.program_id, program_id)
            param.put(device_id, "")
            param.put(
                StringConstant.device_token,
                getDataManager().getUserStringData(AppPreferencesHelper.PREF_FIREBASE_TOKEN)!!
            )

            val header = HashMap<String, String>()
            header.put(StringConstant.apiKey, "HBDEV")
            header.put(authToken, getDataManager().getUserInfo().customer_auth_token)
            header.put(StringConstant.apiVersion, "1")

            getDataManager().getWorkoutPlanDetailAPi(param, header)
                ?.getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        binding.rlLoader.visibility = View.GONE

                        val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                        val status = json?.get(StringConstant.success)
                        val msg = json?.get(StringConstant.message)
                        if (status!!.equals("1")) {
                            Log.v("myLog", "2")
                            binding.myProgress.visibility = View.GONE

                            val workOutPlanDetailModal = getDataManager().mGson?.fromJson(
                                response.toString(), WorkOutPlanDetailModal::class.java
                            )
                            //function for manage all status UI related
                            manageAllStatus(workOutPlanDetailModal!!.data[0])
                        } else {
                            Constant.showCustomToast(this@WorkOutPlanDetailActivity, "" + msg)
                        }
                    }

                    override fun onError(anError: ANError) {
                        binding.rlLoader.visibility = View.GONE

                        Constant.errorHandle(anError, this@WorkOutPlanDetailActivity)
                    }
                })
        } else {
            binding.rlLoader.visibility = View.GONE

            Constant.showInternetConnectionDialog(this)
        }

    }

    private fun manageAllStatus(data: WorkOutPlanDetailModal.Data) {
        isAdmin = getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!

        workoutDetailModal = data
        workOutPlanModal = WorkOutPlanModal.Data.GetAllProgram(
            "", "", data.program_id, "", "", "", ""
        )

        shareUrl = data.program_share_url
        binding.fullContainer.visibility = View.VISIBLE

        binding.tvGoodFor.visibility = View.VISIBLE
        binding.tvEquipment.visibility = View.VISIBLE
        //  binding.tvGoodFor.text = data.program_good_for
        binding.tvGoodFor.text = (data.program_good_for.replace(" |", ","))

        //  binding.tvEquipment.text = data.program_equipments
        binding.tvEquipment.text = (data.program_equipments.replace(" |", ","))
        if (data.show_in_app.equals("No")) {
            binding.tvHeader.visibility = View.GONE
        } else {
            binding.tvHeader.visibility = View.VISIBLE
        }
        binding.tvHeader.text = data.program_name
        binding.tvSubHeader.text = data.program_sub_title

        if (!data.program_description.isEmpty()) {
            binding.txtOverview.visibility = View.VISIBLE
            binding.txtOverview.text = data.program_description
        } else {
            binding.txtOverview.visibility = View.GONE
        }

        if (data.program_access_level == "LOCK") {
            if ("Yes" == isAdmin) {
                binding.lockImg.visibility = View.GONE
                binding.dollorImg.visibility = View.GONE
            } else {
                if ("Paid" == data.access_level) {
                    if (Doviesfitness.getDataManager().getUserStringData(
                            AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED
                        )!! != "0"
                    ) {
                        binding.lockImg.visibility = View.GONE
                        binding.dollorImg.visibility = View.GONE
                    } else {
                        binding.lockImg.visibility = View.GONE
                        binding.dollorImg.visibility = View.VISIBLE
                    }
                } else if ("Exclusive" == data.access_level) {
                    binding.lockImg.visibility = View.GONE
                    binding.dollorImg.visibility = View.VISIBLE
                } else {
                    binding.lockImg.visibility = View.VISIBLE
                    binding.dollorImg.visibility = View.GONE
                }
            }
        } else {
            binding.lockImg.visibility = View.GONE
            binding.dollorImg.visibility = View.GONE
        }
        exclusiveUrl = data.program_redirect_url

        if ("Yes".equals(isAdmin) || "OPEN".equals(data.program_access_level, true)) {
            binding.btnAddToMyPlan.text = "ADD TO MY PLAN"
            binding.btnExclusive.visibility = View.GONE
            binding.btnAddToMyPlan.visibility = View.VISIBLE
        } else {
            if (data.access_level.equals("Paid", true)) {
                binding.btnAddToMyPlan.text = "EXCLUSIVE PLAN - PURCHASE TO VIEW"
                binding.btnExclusive.visibility = View.VISIBLE
                binding.btnAddToMyPlan.visibility = View.GONE
            } else if ("Subscribers".equals(data.access_level, true)) {
                binding.btnAddToMyPlan.text = "SUBSCRIBE TO VIEW PLAN"
                binding.btnExclusive.visibility = View.GONE
                binding.btnAddToMyPlan.visibility = View.VISIBLE
            } else if ("EXCLUSIVE".equals(data.access_level, true)) {
                binding.btnAddToMyPlan.text = "EXCLUSIVE PLAN - PURCHASE TO VIEW"
                binding.btnExclusive.visibility = View.VISIBLE
                binding.btnAddToMyPlan.visibility = View.GONE
            } else if ("FREE".equals(data.access_level, true)) {
                binding.btnAddToMyPlan.text = "ADD TO MY PLAN"
                binding.btnExclusive.visibility = View.GONE
                binding.btnAddToMyPlan.visibility = View.VISIBLE
            }
        }

        // when we come from deep linking in that wee need to ad plan according to conditions
        if (!fromDeepLinking.isEmpty()) {
            comeFromDeepLinkingForPlanAdd()
        }

        if (!data.program_portrait_image.isEmpty()) {
            if (CommanUtils.isValidContextForGlide(this)) {
                Glide.with(binding.ivFeatured.context).load(data.program_portrait_image)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            @Nullable e: GlideException?,
                            model: Any,
                            target: Target<Drawable>,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.myProgress.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable,
                            model: Any,
                            target: Target<Drawable>,
                            dataSource: DataSource,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.myProgress.visibility = View.GONE
                            return false
                        }
                    }).apply(RequestOptions()).into(binding.ivFeatured)


                /*    Glide.with(binding.ivFeatured).load(data.program_image)
                        .into(binding.ivFeatured)*/
            }
        }
    }

    private fun getProgramDetailActvitity(news_module_id: String) {
        binding.rlLoader.visibility = View.VISIBLE

        if (CommanUtils.isNetworkAvailable(this)) {
            val param = HashMap<String, String>()
            param.put(program_id, news_module_id)
            param.put(device_type, Android)
            param.put(auth_customer_id, getDataManager().getUserInfo().customer_auth_token)
            param.put(device_id, "")
            val header = HashMap<String, String>()
            header.put(authToken, getDataManager().getUserInfo().customer_auth_token)
            getDataManager().programDetailApi(param, header)
                ?.getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        binding.rlLoader.visibility = View.GONE
                        val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                        val status = json?.get(StringConstant.success)
                        val msg = json?.get(StringConstant.message)
                        if (status!!.equals("1")) {
                            val prgramDetail = getDataManager().mGson?.fromJson(
                                response.toString(), PlanDetailsResponse::class.java
                            )
                            val programData = prgramDetail!!.data.get_customer_program_detail.get(0)

                            Glide.with(binding.ivFeatured.context).load(programData.program_image)
                                .listener(object : RequestListener<Drawable> {
                                    override fun onLoadFailed(
                                        @Nullable e: GlideException?,
                                        model: Any,
                                        target: Target<Drawable>,
                                        isFirstResource: Boolean
                                    ): Boolean {
                                        binding.myProgress.visibility = View.GONE
                                        return false
                                    }

                                    override fun onResourceReady(
                                        resource: Drawable,
                                        model: Any,
                                        target: Target<Drawable>,
                                        dataSource: DataSource,
                                        isFirstResource: Boolean
                                    ): Boolean {
                                        binding.myProgress.visibility = View.GONE
                                        return false
                                    }
                                }).apply(RequestOptions()).into(binding.ivFeatured)


                            /*Glide.with(binding.ivFeatured.context).load(programData.program_image)
                                .placeholder(R.color.colorBlack).into(binding.ivFeatured)*/
                            binding.program = programData
                            shareUrl = programData.program_share_url
                            binding.fullContainer.visibility = View.VISIBLE

                        } else {
                            binding.rlLoader.visibility = View.GONE
                            Constant.showCustomToast(this@WorkOutPlanDetailActivity, "" + msg)
                        }
                    }

                    override fun onError(anError: ANError) {
                        binding.rlLoader.visibility = View.GONE
                        Constant.errorHandle(anError, this@WorkOutPlanDetailActivity)
                    }
                })
        } else {
            binding.rlLoader.visibility = View.GONE
            Constant.showInternetConnectionDialog(this)
        }
    }

    /*.............................//Custom dialog for Delete....................................*/
    fun comeFromDeepLinkingForPlanAdd() {
        val dialog = Dialog(this)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog.setContentView(R.layout.add_diet_plan_view)
        val tv_no = dialog.findViewById<TextView>(R.id.tv_no)
        val tv_yes = dialog.findViewById<TextView>(R.id.tv_yes)

        tv_no.setOnClickListener { v -> dialog.dismiss() }

        tv_yes.setOnClickListener { v ->
            binding.btnAddToMyPlan.callOnClick()
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                onBackPressed()
            }

            R.id.iv_share -> {
                if (!shareUrl.isEmpty()) {
                    binding.ivShare.isEnabled = false
                    sharePost(shareUrl)
                    Handler().postDelayed(Runnable { binding.ivShare.isEnabled = true }, 2000)

                }
            }

            R.id.btn_Exclusive -> {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return
                } else {
                    mLastClickTime = SystemClock.elapsedRealtime()
                }
                if (CommanUtils.isNetworkAvailable(this)) {
                    val dialog = ExclusiveWorkoutDialog.newInstance(
                        exclusiveUrl,
                        "",
                        getString(R.string.error_download_type)
                    )
                    dialog.show(supportFragmentManager)
                } else {
                    Constant.showInternetConnectionDialog(this)
                }
            }

            R.id.btn_addToMyPlan -> {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return
                } else {
                    mLastClickTime = SystemClock.elapsedRealtime()
                }

                if (CommanUtils.isNetworkAvailable(this)) {
                    //when we come from deeplinking then work will manage from this way
                    if (!fromDeepLinking.isEmpty()) {
                        if (binding.btnAddToMyPlan.text.toString().equals("Add to my plan", true))
                        {
                            if (!workoutDetailModal.program_id.isEmpty()) {
                                addWorkOutPlanApi(workoutDetailModal.program_id)
                            }
                        }
                        else if (binding.btnAddToMyPlan.text.toString()
                                .equals("SUBSCRIBE TO VIEW PLAN")
                        ) {
                            var intent = Intent(getActivity(), SubscriptionActivity::class.java).putExtra("home", "no")
                                .putExtra("exercise", "yes")
                            startActivityForResult(intent, 2)
                        } else if (binding.btnAddToMyPlan.text.toString().equals("Subscribe")) {
                            var intent = Intent(
                                getActivity(), SubscriptionActivity::class.java
                            ).putExtra("home", "no")
                            intent.putExtra(
                                "fromWorkoutDetailActivity", "fromWorkoutDetailActivity"
                            )
                            startActivityForResult(
                                intent, 2
                            )
                        }
                    } else {
                        if (binding.btnAddToMyPlan.text.toString().equals("Add to my plan", true)) {
                            if (!fromNotification_module_id.isEmpty()) {
                                addWorkOutPlanApi(fromNotification_module_id)
                            } else {
                                if (!workOutPlanModal!!.program_id.isEmpty()) {
                                    addWorkOutPlanApi(workOutPlanModal!!.program_id)
                                }
                            }
                        } else if (binding.btnAddToMyPlan.text.toString()
                                .equals("SUBSCRIBE TO VIEW PLAN")
                        ) {
                            var intent = Intent(getActivity(), SubscriptionActivity::class.java).putExtra("home", "no")
                                .putExtra("exercise", "yes")
                            startActivityForResult(intent, 2)
                        } else if (binding.btnAddToMyPlan.text.toString().equals("Subscribe")) {
                            var intent = Intent(
                                getActivity(), SubscriptionActivity::class.java
                            ).putExtra("home", "no")
                            intent.putExtra(
                                "fromWorkoutDetailActivity", "fromWorkoutDetailActivity"
                            )
                            startActivityForResult(
                                intent, 2
                            )
                        }
                    }

                } else {
                    Constant.showInternetConnectionDialog(this)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.v("myLog", "0.0")/*Get the comment count from the CommentsActivity*/
        if (requestCode == 2 && data != null) {
            if (feature_workout_module_id != null) {
                Log.v("treyrt", "yre")
                if (!feature_workout_module_id.isEmpty()) {
                    Log.v("treyrt", "yre0")
                    binding.btnAddToMyPlan.text = "Add to my plan"
                    binding.lockImg.visibility = View.GONE
                    //getDietPlanDetailsApi(feature_workout_module_id)
                }
            }

            if (featuredNewsNews_module_id != null && !featuredNewsNews_module_id.isEmpty()) {
                Log.v("treyrt", "yre1")
                binding.btnAddToMyPlan.text = "Add to my plan"
                binding.lockImg.visibility = View.GONE
                //getProgramDetailActvitity(featuredNewsNews_module_id)
            }
        }
    }

    private fun addWorkOutPlanApi(program_id: String) {
        val param = HashMap<String, String>()
        param.put(device_type, Android)
        param.put(device_id, "")
        param.put(StringConstant.program_id, program_id)
        param.put(StringConstant.device_token, "")
        param.put(
            auth_customer_id, getDataManager().getUserInfo().customer_auth_token
        )

        val header = HashMap<String, String>()
        header.put(authToken, getDataManager().getUserInfo().customer_auth_token)
        header.put(StringConstant.apiKey, "HBDEV")
        header.put(StringConstant.apiVersion, "1")

        getDataManager().addToMyPlanApi(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                    val status = json?.get(StringConstant.success)
                    val msg = json?.get(StringConstant.message)
                    if (status!!.equals("1")) {
                        val myPlanFromProfileFragment = MyPlanFromProfileFragment()
                        val args = Bundle()
                        myPlanFromProfileFragment.arguments = args
                        if ("other media".equals(from)) {
                            addFragment(
                                myPlanFromProfileFragment.newInstance("other media"),
                                R.id.container_id1,
                                true
                            )
                        } else {
                            addFragment(
                                myPlanFromProfileFragment.newInstance("WorkOutPlanDetailActivity"),
                                R.id.container_id1,
                                true
                            )
                        }
                        //finish()
                    } else {
                        Constant.showCustomToast(this@WorkOutPlanDetailActivity, "" + msg)
                    }
                }
                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, this@WorkOutPlanDetailActivity)
                }
            })
    }
}