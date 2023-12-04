package com.doviesfitness.ui.bottom_tabbar.home_tab.fragment


import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.DisplayMetrics
import android.util.Log
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.annotation.Nullable
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.databinding.FragmentWorkoutPlanDetailBinding
import com.doviesfitness.subscription.SubscriptionActivity

import com.doviesfitness.ui.base.BaseFragment
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.FeaturedNews
import com.doviesfitness.ui.bottom_tabbar.workout_plan.modal.WorkOutPlanDetailModal
import com.doviesfitness.ui.profile.profile_my_plan.MyPlanFromProfileFragment
import com.doviesfitness.utils.CommanUtils
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.StringConstant
import org.json.JSONObject

private const val ARG_PARAM1 = "PROGRAM_DETAIL"

class WorkoutPlanDetailFragment : BaseFragment(), View.OnClickListener {
    private lateinit var binding: FragmentWorkoutPlanDetailBinding
    private lateinit var featuredNews: FeaturedNews
    private var shareUrl = ""
    private var mLastClickTime: Long = 0
    private var isAdmin: String = ""
    private lateinit var workoutDetailModal: WorkOutPlanDetailModal.Data
    private var fromNotification_module_id: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            featuredNews = it.getParcelable(ARG_PARAM1)!!
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_workout_plan_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvHeader.text = featuredNews.news_title
        Glide.with(binding.ivFeatured.context).load(featuredNews.news_image)
            .placeholder(R.color.colorBlack)
            .into(binding.ivFeatured)
        binding.ivBack.setOnClickListener(this)
        binding.ivShare.setOnClickListener(this)
        binding.btnStatus.setOnClickListener(this)

        val params = binding.ivFeatured.getLayoutParams() as RelativeLayout.LayoutParams
        val display = activity!!.windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)
        val dpWidth = outMetrics.widthPixels
        params.width = dpWidth
        params.height = dpWidth + 300
        binding.ivFeatured.setLayoutParams(params)

        //getProgramDetailApi(featuredNews.news_module_id)
        getDietPlanDetailsApi(featuredNews.news_module_id)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WorkoutPlanDetailFragment.
         */
        @JvmStatic
        fun newInstance(param1: FeaturedNews) =
            WorkoutPlanDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, param1)
                }
            }
    }


   /* private fun getProgramDetailApi(news_module_id: String) {
        val param = HashMap<String, String>()
        param.put(program_id, news_module_id)
        param.put(device_type, StringConstant.Android)
        param.put(auth_customer_id, getDataManager().getUserInfo().customer_auth_token)
        param.put(device_id, "")
        val header = HashMap<String, String>()
        header.put(StringConstant.AUTHTOKEN, getDataManager().getUserInfo().customer_auth_token)
        getDataManager().programDetailApi(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                    val status = json?.get(StringConstant.success)
                    val msg = json?.get(StringConstant.message)
                    if (status!!.equals("1")) {
                        binding.fullContainer.visibility = View.VISIBLE

                        val prgramDetail = getDataManager().mGson?.fromJson(
                            response.toString(),
                            PlanDetailsResponse::class.java
                        )
                        val programData = prgramDetail!!.data.get_customer_program_detail.get(0)
                        Glide.with(binding.ivFeatured.context).load(programData.program_image)
                            .placeholder(R.color.colorBlack).into(binding.ivFeatured)
                        binding.program = programData
                        shareUrl = programData.program_share_url

                        if (!programData.program_description.isEmpty()) {
                            binding.txtOverview.visibility = View.VISIBLE
                            binding.txtOverview.text = programData.program_description
                        } else {
                            binding.txtOverview.visibility = View.GONE
                        }


                    } else {
                        Constant.showCustomToast(context!!, "" + msg)
                    }
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, activity)
                }
            })
    }*/

    private fun getDietPlanDetailsApi(program_id: String) {

        if (CommanUtils.isNetworkAvailable(mContext)!!) {

            binding.myProgress.visibility = View.VISIBLE
            Log.v("myLog", "1")
            val param = HashMap<String, String>()
            param.put(StringConstant.device_type, StringConstant.Android)
            param.put(
                StringConstant.auth_customer_id,
                getDataManager().getUserInfo().customer_auth_token
            )
            param.put(StringConstant.program_id, program_id)
            param.put(StringConstant.device_id, "")
            param.put(
                StringConstant.device_token, getDataManager().getUserStringData(
                    AppPreferencesHelper.PREF_FIREBASE_TOKEN
                )!!
            )

            val header = HashMap<String, String>()
            header.put(StringConstant.apiKey, "HBDEV")
            header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
            header.put(StringConstant.apiVersion, "1")

            getDataManager().getWorkoutPlanDetailAPi(param, header)
                ?.getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                        val status = json?.get(StringConstant.success)
                        val msg = json?.get(StringConstant.message)
                        if (status!!.equals("1")) {
                            Log.v("myLog", "2")
                            binding.myProgress.visibility = View.GONE
                            val workOutPlanDetailModal =
                                getDataManager().mGson?.fromJson(
                                    response.toString(),
                                    WorkOutPlanDetailModal::class.java
                                )

                            //function for manage all status UI related
                            manageAllStatus(workOutPlanDetailModal!!.data[0])

                        } else {
                            //Constant.showCustomToast(mContext, "" + msg)
                        }
                    }

                    override fun onError(anError: ANError) {
                        Constant.errorHandle(anError, mContext as Activity)
                    }
                })
        } else {
            Constant.showInternetConnectionDialog(mContext as Activity)
        }
    }


    private fun manageAllStatus(data: WorkOutPlanDetailModal.Data) {
        isAdmin = getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!

        workoutDetailModal = data
        shareUrl = data.program_share_url
        binding.fullContainer.visibility = View.VISIBLE

        binding.tvGoodFor.visibility = View.VISIBLE
        binding.tvEquipment.visibility = View.VISIBLE
        binding.tvGoodFor.text = data.program_good_for.replace(" |",",")
        binding.tvEquipment.text = data.program_equipments.replace(" |",",")
        binding.tvHeader.text = data.program_name

        if (!data.program_description.isEmpty()) {
            binding.txtOverview.visibility = View.VISIBLE
            binding.txtOverview.text = data.program_description
        } else {
            binding.txtOverview.visibility = View.GONE
        }

        // button status
        if ("Yes".equals(isAdmin) || "OPEN".equals(data.program_access_level, true)) {
            binding.btnStatus.text = "Add to my plan"
        } else {
            if (data.access_level.equals("Paid", true)) {
                binding.btnStatus.text = "UPGRADE TO VIEW PLAN"
            } else if ("Subscribers".equals(data.access_level, true)) {
                binding.btnStatus.text = "Subscribe"
            } else if ("FREE".equals(data.access_level, true)) {
                binding.btnStatus.text = "Add to my plan"
            }
        }

        // lock icon visiblity
        if (data.program_access_level.equals("LOCK")) {
            if ("Yes".equals(isAdmin)) {
                binding.lockImg.visibility = View.GONE
            } else {
                binding.lockImg.visibility = View.VISIBLE
            }
        } else {
            binding.lockImg.visibility = View.GONE
        }


        if (!data.program_image.isEmpty()) {
            if (CommanUtils.isValidContextForGlide(mContext)) {
                Glide.with(binding.ivFeatured.context).load(data.program_image).listener(object :
                    RequestListener<Drawable> {
                    override fun onLoadFailed(
                        @Nullable e: GlideException?, model: Any,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.myProgress.setVisibility(View.GONE)
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.myProgress.setVisibility(View.GONE)
                        return false
                    }
                }).apply(RequestOptions())
                    .into(binding.ivFeatured)
            }
        }
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                activity?.onBackPressed()
            }
            R.id.iv_share -> {
                binding.ivShare.isEnabled=false
                getBaseActivity()?.sharePost(shareUrl)
                Handler().postDelayed(Runnable { binding.ivShare.isEnabled=true },2000)

            }

            R.id.btn_status -> {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                } else {
                    mLastClickTime = SystemClock.elapsedRealtime();
                }

                if (CommanUtils.isNetworkAvailable(mContext)!!) {

                    if (binding.btnStatus.text.toString().equals("Add to my plan")) {

                        if (!featuredNews.news_module_id.isEmpty()) {
                            addWorkOutPlanApi(featuredNews.news_module_id)
                        }
                    } else if (binding.btnStatus.text.toString().equals("UPGRADE TO VIEW PLAN")) {
                        Constant.showCustomToast(mContext, "Under Working")
                    } else if (binding.btnStatus.text.toString().equals("Subscribe")) {
                        var intent = Intent(getActivity(), SubscriptionActivity::class.java).putExtra("home", "no")
                        intent.putExtra("fromWorkoutDetailActivity", "fromWorkoutDetailActivity")
                        startActivityForResult(intent, 2)
                    }
                } else {
                    Constant.showInternetConnectionDialog(mContext as Activity)
                }
            }
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.v("myLog","0.0")
        /*Get the comment count from the CommentsActivity*/
        if (requestCode == 2 && data != null) {
            if(featuredNews.news_module_id != null){
                Log.v("treyrt","yre")
                if(!featuredNews.news_module_id.isEmpty()){
                    Log.v("treyrt","yre0")
                    binding.btnStatus.text = "Add to my plan"
                    binding.lockImg.visibility = View.GONE
                    //getDietPlanDetailsApi(feature_workout_module_id)
                }
            }
        }
    }

    /*override fun onResume() {
        super.onResume()

        if(featuredNews != null){
            if(!featuredNews.news_module_id.isEmpty()){
                getDietPlanDetailsApi(featuredNews.news_module_id)
            }
        }
    }*/

    private fun addWorkOutPlanApi(program_id: String) {
        val param = HashMap<String, String>()

        param.put(StringConstant.device_type, StringConstant.Android)
        param.put(StringConstant.device_id, "")
        param.put(StringConstant.program_id, program_id)
        param.put(StringConstant.device_token, "")
        param.put(
            StringConstant.auth_customer_id,
            getDataManager().getUserInfo().customer_auth_token
        )

        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        header.put(StringConstant.apiKey, "HBDEV")
        header.put(StringConstant.apiVersion, "1")

        getDataManager().addToMyPlanApi(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                    val status = json?.get(StringConstant.success)
                    val msg = json?.get(StringConstant.message)
                    if (status!!.equals("1")) {

                        /*  val intent = Intent(this@WorkOutPlanDetailActivity, AddToWorkOutPlanActivity::class.java)
                          intent.putExtra("data", program_id)
                          startActivity(intent)
                        */

                        val myPlanFromProfileFragment = MyPlanFromProfileFragment()
                        val args = Bundle()
                        myPlanFromProfileFragment.setArguments(args)
                        addFragment(
                            myPlanFromProfileFragment.newInstance("WorkOutPlanDetailActivity"),
                            R.id.container_id1,
                            true
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


    override fun onDestroy() {
        super.onDestroy()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mActivity!!.window.setStatusBarColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.colorWhite
                )
            )
        }
    }
}
