package com.doviesfitness.ui.bottom_tabbar.home_tab.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide

import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.databinding.FragmentDietPlanDetailBinding
import com.doviesfitness.subscription.SubscriptionActivity
import com.doviesfitness.ui.base.BaseFragment
import com.doviesfitness.ui.bottom_tabbar.diet_plan.fragment.MyDietPlanFragment
import com.doviesfitness.ui.bottom_tabbar.diet_plan.modal.DietPlanDetails
import com.doviesfitness.ui.bottom_tabbar.diet_plan.modal.DietPlanSubCategoryModal
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.DietPlanDetailResponce
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.FeaturedNews
import com.doviesfitness.utils.CommanUtils
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.StringConstant
import com.squareup.picasso.Picasso
import org.json.JSONObject

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "PROGRAM_DETAIL"

class DietPlanDetailFragment : BaseFragment(), View.OnClickListener {
    lateinit var binding: FragmentDietPlanDetailBinding
    private lateinit var newsDetail: FeaturedNews
    lateinit var newsFeedDetail: DietPlanDetailResponce
    private var dietPLanCategoryList: ArrayList<DietPlanSubCategoryModal.Data.DietListing>? = null
    private var isAddOrNot: Boolean = false
    var btnStatus: String = ""
    var haveRun: String = ""
    private var fromAddDietPlan: String? = null
    private var mLastClickTime: Long = 0
    var diet_plan_id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            newsDetail = it.getParcelable(ARG_PARAM1)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_diet_plan_detail, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(binding.ivFeatured.context).load(newsDetail.news_image).into(binding.ivFeatured)

        binding.ivBack.setOnClickListener(this)
        binding.ivShare.setOnClickListener(this)
        binding.btnStatus.setOnClickListener(this)
        //getProgramDetail(newsDetail.news_module_id)

        diet_plan_id = newsDetail.news_module_id
        getDietPlanDetailsApi(newsDetail.news_module_id)
    }


    /* private fun getProgramDetail(news_module_id: String) {
         val param = HashMap<String, String>()
         param.put("diet_plan_id", news_module_id)
         param.put(StringConstant.device_type, StringConstant.Android)
         param.put(StringConstant.auth_customer_id, getDataManager().getUserInfo().customer_auth_token)
         param.put(StringConstant.device_id, "")
         val header = HashMap<String, String>()
         header.put(StringConstant.AUTHTOKEN, getDataManager().getUserInfo().customer_auth_token)
         getDataManager().dietPlanDetailApi(param, header)
             ?.getAsJSONObject(object : JSONObjectRequestListener {
                 override fun onResponse(response: JSONObject?) {
                     val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                     val status = json?.get(StringConstant.success)
                     val msg = json?.get(StringConstant.message)
                     if (status!!.equals("1")) {
                         newsFeedDetail =
                             getDataManager().mGson!!.fromJson(response.toString(), DietPlanDetailResponce::class.java)

                         binding.featuredData = newsFeedDetail.data.get(0)

                         binding.tvGoodFor.visibility = View.VISIBLE
                         binding.btnLogin.visibility = View.VISIBLE

                     } else {
                         Constant.showCustomToast(context!!, "" + msg)
                     }
                 }

                 override fun onError(anError: ANError) {
                     Constant.errorHandle(anError, activity!!)
                 }
             })
     }*/


    private fun getDietPlanDetailsApi(diet_plan_id: String) {

        if (CommanUtils.isNetworkAvailable(mContext)!!) {
            binding.myProgress.visibility = View.VISIBLE

            val param = HashMap<String, String>()

            param.put(StringConstant.device_token, "")
           var customerType=getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_USER_TYPE)
            if (customerType!=null&& !customerType!!.isEmpty()){
                param.put(StringConstant.auth_customer_subscription, customerType)
            }
            else{
                param.put(StringConstant.auth_customer_subscription, "Paid")
            }
            param.put(
                StringConstant.auth_customer_id,
                getDataManager().getUserInfo().customer_auth_token
            )
            param.put(StringConstant.device_id, "")
            param.put(StringConstant.device_type, StringConstant.Android)
            param.put(StringConstant.diet_plan_id, diet_plan_id)

            val header = HashMap<String, String>()
            header.put(StringConstant.apiKey, "HBDEV")
            header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
            header.put(StringConstant.apiVersion, "1")

            getDataManager().dietPlanDetailApi(param, header)
                ?.getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                        val status = json?.get(StringConstant.success)
                        val msg = json?.get(StringConstant.message)
                        if (status!!.equals("1")) {
                            binding.myProgress.visibility = View.GONE
                            val dietPlanDetail =
                                getDataManager().mGson?.fromJson(response.toString(), DietPlanDetails::class.java)

                            //binding.dpTitleName.setText( dietPlanDetail.data[0].diet_plan_name)
                            val dietPlanData = dietPlanDetail!!.data[0]
                            //function for manage all status UI related
                            manageAllStatus(dietPlanData)

                        } else {
                            Constant.showCustomToast(context!!, "" + msg)
                            //binding.noRecordFound.visibility = View.VISIBLE
                        }
                    }

                    override fun onError(anError: ANError) {
                        Constant.errorHandle(anError, activity)
                    }
                })
        } else {
            Constant.showInternetConnectionDialog(mContext as Activity)
        }

    }


    private fun manageAllStatus(dietPlanData: DietPlanDetails.Data) {

        if (dietPLanCategoryList != null && dietPLanCategoryList!!.size != 0) {
            for (data in dietPLanCategoryList!!) {
                if (data.diet_plan_title.equals(dietPlanData.diet_plan_name)) {
                    isAddOrNot = true
                }
            }
        }

        binding.tvHeading.setText(dietPlanData.diet_plan_name)

        // for show locak icon when acess level lock
        if (dietPlanData.diet_plan_access_level.equals("LOCK")) {
            binding.lockImg.visibility = View.VISIBLE
        } else {
            binding.lockImg.visibility = View.GONE
        }

        if (!dietPlanData.diet_plan_description.isEmpty()) {
            binding.txtDietDiscription.visibility = View.VISIBLE
            binding.txtDietDiscription.setText(dietPlanData.diet_plan_description)
        } else {
            binding.txtDietDiscription.visibility = View.GONE
        }

        if (!dietPlanData.diet_plan_image.isEmpty()) {

            if (!activity!!.isDestroyed) {
                Picasso.with(mContext).load(dietPlanData.diet_plan_image)
                    .into(binding.ivFeatured)
            }
        }

        //manage button Status
        if (dietPlanData.is_added.equals("1") && !fromAddDietPlan.equals("0")) {
            binding.btnStatus.text = "ALREADY ADDED"
            binding.btnStatus.visibility = View.VISIBLE
            haveRun = "itsRun"
        }

        if (fromAddDietPlan.equals("0")) {
            if (dietPlanData.diet_plan_access_level.equals("LOCK")) {
                var chaeckStatus: String = dietPlanData.diet_access_level.toUpperCase()
                if (dietPlanData.diet_access_level.toUpperCase().equals("FREE")) {

                    // from arrary list check

                    if (isAddOrNot) {
                        binding.btnStatus.text = "DELETE"
                        binding.btnStatus.visibility = View.VISIBLE
                    } else {
                        binding.btnStatus.text = "ADD"
                        binding.btnStatus.visibility = View.VISIBLE
                    }

                } else if (dietPlanData.diet_access_level.toUpperCase().equals("SUBSCRIBERS")) {
                    binding.btnStatus.text = "Subscribe"
                    binding.btnStatus.visibility = View.VISIBLE
                } else if (dietPlanData.diet_access_level.toUpperCase().equals("PAID")) {
                    binding.btnStatus.text = "PURCHASE"
                    binding.btnStatus.visibility = View.VISIBLE
                }
            } else {

                if (isAddOrNot) {
                    binding.btnStatus.text = "DELETE"
                    binding.btnStatus.text = "DELETE"
                    binding.btnStatus.visibility = View.VISIBLE
                } else {
                    binding.btnStatus.text = "ADD"
                    binding.btnStatus.text = "ADD"
                    binding.btnStatus.visibility = View.VISIBLE
                }
            }

        } else {

            // based on aboue condition  depandes on above stauts
            if (!haveRun.equals("itsRun")) {
                if (dietPlanData.diet_plan_access_level.equals("LOCK")) {
                    var chaeckStatus: String = dietPlanData.diet_access_level.toUpperCase()

                    if (dietPlanData.diet_access_level.toUpperCase().equals("FREE")) {
                        binding.btnStatus.text = "ADD TO MY DIET PLAN"
                        binding.btnStatus.visibility = View.VISIBLE
                    } else if (dietPlanData.diet_access_level.toUpperCase().equals("SUBSCRIBERS")) {
                        binding.btnStatus.text = "Subscribe"
                        binding.btnStatus.visibility = View.VISIBLE
                    } else if (dietPlanData.diet_access_level.toUpperCase().equals("PAID")) {
                        binding.btnStatus.text = "PURCHASE"
                        binding.btnStatus.visibility = View.VISIBLE
                    }
                } else {
                    binding.btnStatus.text = "ADD TO MY DIET PLAN"
                    btnStatus = "ADD TO MY DIET PLAN"
                    binding.btnStatus.visibility = View.VISIBLE
                }
            }
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 4 && data != null) {
            //startActivity(intent)
            if(newsDetail != null){
                Log.v("egje","dshyu")
                if(newsDetail.news_module_id != null && !newsDetail.news_module_id.isEmpty()){

                    binding.btnStatus.text = "ADD TO MY DIET PLAN"
                    binding.lockImg.visibility = View.GONE
                    //getDietPlanDetailsApi(dietPlanSubCatgory.diet_plan_id)
                }
            }
        }

        if (requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {

                if (data!!.getStringExtra("comeAndBackFromDietDetail") != null) {
                    val comeAndBackFromDietDetail = data.getStringExtra("comeAndBackFromDietDetail")
                    if (comeAndBackFromDietDetail!!.equals("yes")) {
                        btnStatus = ""
                        binding.btnStatus.text = "ALREADY ADDED"
                    }
                }
            }
        }
    }




    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DietPlanDetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: FeaturedNews) =
            DietPlanDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, param1)
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
                getBaseActivity()?.sharePost(newsFeedDetail.data.get(0).diet_plan_share_url)
                Handler().postDelayed(Runnable { binding.ivShare.isEnabled=true },2000)

            }

            R.id.btn_status -> {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                } else {
                    mLastClickTime = SystemClock.elapsedRealtime();
                }

                if (CommanUtils.isNetworkAvailable(mContext)!!) {
                    if ( binding.btnStatus.text.equals("ADD TO MY DIET PLAN")) {
                        if (!diet_plan_id.isEmpty()) {
                            addToMyDietPlan(diet_plan_id)
                        }
                    } else if (binding.btnStatus.text.toString().equals("PURCHASE", true)) {
                        if (getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED)!!.equals(
                                "0"
                            ) &&
                            getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!.equals(
                                "No",
                                true
                            )
                        ) {
                            startActivity(Intent(getActivity(), SubscriptionActivity::class.java).putExtra("home", "no")
                            )
                        }
                    } else if (binding.btnStatus.text.toString().equals("Subscribers", true)) {
                        if (getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED)!!.equals("0") &&
                            getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!.equals("No", true)) {

                            var intent = Intent(getActivity(), SubscriptionActivity::class.java).putExtra("home", "no")
                            intent.putExtra("fromDietPlan", "fromDietPlan")
                            startActivityForResult(intent, 4);// Activity is started with requestCode 2
                        }
                    } /*else if (btnStatus.equals("ADD")) {

                        dietPlanSubCatgory.isAdd = "isAdd"

                        val intent = Intent()
                        intent.putExtra("addDietPlan", dietPlanSubCatgory)
                        mActivity!!.setResult(2, intent)
                        mActivity!!.finish()//finishing activity

                    } else if (btnStatus.equals("DELETE")) {
                        //customDeleteDialog(dietPlanSubCatgory)
                    }*/
                } else {
                    Constant.showInternetConnectionDialog(mContext as Activity)
                }
            }

        }
    }


    private fun addToMyDietPlan(diet_plan_id: String) {

        val param = HashMap<String, String>()
        param.put(
            StringConstant.auth_customer_id,
            getDataManager().getUserInfo().customer_auth_token
        )
        param.put(StringConstant.diet_plan_id, diet_plan_id)
        param.put(StringConstant.device_id, "")
        param.put(StringConstant.device_token, "")
        param.put(StringConstant.device_type, StringConstant.Android)

        val header = HashMap<String, String>()
        header.put(StringConstant.apiKey, "HBDEV")
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        header.put(StringConstant.apiVersion, "1")

        getDataManager().addToMyDietAPi(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                    val status = json?.get(StringConstant.success)
                    val msg = json?.get(StringConstant.message)
                    if (status!!.equals("1")) {

                        binding.btnStatus.text = "ALREADY ADDED"
                        /* val intent = Intent(getBaseActivity(), MyDietPlanActivity::class.java)
                         startActivityForResult(intent, 3);*/

                        val myDietPalnFragment = MyDietPlanFragment()
                        val args = Bundle()
                        args.putInt("data", 1)
                        myDietPalnFragment.setArguments(args)
                        getBaseActivity()?.addFragment(myDietPalnFragment, R.id.container_id1, true)

                    } else {
                        Constant.showCustomToast(context!!, "" + msg)
                    }
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, activity)
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
