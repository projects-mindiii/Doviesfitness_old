package com.doviesfitness.ui.bottom_tabbar.diet_plan.fragment

import android.content.Context
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.doviesfitness.R
import com.doviesfitness.databinding.FragmentDietDetailBinding
import com.doviesfitness.ui.base.BaseFragment
import com.doviesfitness.ui.bottom_tabbar.diet_plan.modal.DietPlanDetails
import com.doviesfitness.ui.bottom_tabbar.diet_plan.modal.DietPlanSubCategoryModal
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.StringConstant
import org.json.JSONObject
import android.app.Activity
import android.os.SystemClock
import android.widget.RelativeLayout
import com.doviesfitness.utils.CommanUtils
import com.squareup.picasso.Picasso
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.TextView
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.subscription.SubscriptionActivity
import java.io.File

class DietDetailFragment : BaseFragment(), View.OnClickListener {

    private var isAddOrNot: Boolean = false
    private lateinit var dietPlanSubCatgory: DietPlanSubCategoryModal.Data.DietListing
    lateinit var binding: FragmentDietDetailBinding
    var haveRun: String = ""
    var btnStatus: String = ""
    var diet_plan_id: String = ""
    private var mLastClickTime: Long = 0
    private var fromAddDietPlan: String? = null
    var userImageFile: File? = null
    private var dietPLanCategoryList: ArrayList<DietPlanSubCategoryModal.Data.DietListing>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_diet_detail, container, false)
        initialization()
        return binding.root
    }

    private fun initialization() {
        EventBus.getDefault().register(this);
        binding.ivBack.setOnClickListener(this)
        binding.btnStatus.setOnClickListener(this)
        binding.ivShare.setOnClickListener(this)

        dietPlanSubCatgory = (arguments!!.getParcelable("data") as? DietPlanSubCategoryModal.Data.DietListing)!!
        fromAddDietPlan = arguments!!.getString("fromAddDietPlan") as String
        // when list has item then get also list for check status otherwise null

        dietPLanCategoryList =
            arguments!!.getParcelableArrayList<DietPlanSubCategoryModal.Data.DietListing>("addDietPlanList")

        if (fromAddDietPlan.equals("0")) {
            fromAddDietPlan = "0"
        } else {
            fromAddDietPlan = ""
        }

        //val params = binding.rltvImage.getLayoutParams() as RelativeLayout.LayoutParams
        val params = binding.ivDpSubImage.getLayoutParams() as RelativeLayout.LayoutParams
        val dpWidth = CommanUtils.getWidthAndHeight(mContext as Activity)
        params.height = dpWidth + 250
        binding.ivDpSubImage.setLayoutParams(params)

        diet_plan_id = dietPlanSubCatgory.diet_plan_id

        if (!dietPlanSubCatgory.diet_plan_image.isEmpty()) {
            /*  Glide.with(binding.ivDpSubImage).load(dietPlanSubCatgory.diet_plan_image)
                  .into(binding.ivDpSubImage)*/
            Picasso.with(mContext).load(dietPlanSubCatgory.diet_plan_image)
                .into(binding.ivDpSubImage)
        }
        getDietPlanDetailsApi(dietPlanSubCatgory.diet_plan_id)
    }

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
                                getDataManager().mGson?.fromJson(
                                    response.toString(),
                                    DietPlanDetails::class.java
                                )

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

        binding.dpTitleHeading.setText(dietPlanData.diet_plan_name)

        // for show locak icon when acess level lock


        if (getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!.equals("Yes")
        ) {
            binding.lockImg.visibility = View.GONE
        } else {

            if (dietPlanData.diet_plan_access_level.equals("LOCK")) {
                binding.lockImg.visibility = View.VISIBLE
            } else {
                binding.lockImg.visibility = View.GONE
            }
        }

        if (!dietPlanData.diet_plan_description.isEmpty()) {
            binding.txtDietDiscription.visibility = View.VISIBLE
            binding.txtDietDiscription.setText(dietPlanData.diet_plan_description)
        } else {
            binding.txtDietDiscription.visibility = View.GONE
        }

        if (!dietPlanData.diet_plan_image.isEmpty()) {
            /*Glide.with(binding.ivDpSubImage).load(dietPlanData.diet_plan_image)
                .into(binding.ivDpSubImage)*/

            if (getActivity()!!.isDestroyed) {
                Picasso.with(getActivity()).load(dietPlanData.diet_plan_image)
                    .into(binding.ivDpSubImage)
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

                }

                else if (getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!.equals("Yes")
                ) {
                    if (isAddOrNot) {
                        binding.btnStatus.text = "DELETE"
                        btnStatus = "DELETE"
                        binding.btnStatus.visibility = View.VISIBLE
                    } else {
                        binding.btnStatus.text = "ADD"
                        btnStatus = "ADD"
                        binding.btnStatus.visibility = View.VISIBLE
                    }

                }

                else if (dietPlanData.diet_access_level.toUpperCase().equals("SUBSCRIBERS")) {
                    binding.btnStatus.text = "Subscribe"
                    binding.btnStatus.visibility = View.VISIBLE
                } else if (dietPlanData.diet_access_level.toUpperCase().equals("PAID")) {
                    binding.btnStatus.text = "PURCHASE"
                    binding.btnStatus.visibility = View.VISIBLE
                }
            }
            else {

                if (isAddOrNot) {
                    binding.btnStatus.text = "DELETE"
                    btnStatus = "DELETE"
                    binding.btnStatus.visibility = View.VISIBLE
                } else {
                    binding.btnStatus.text = "ADD"
                    btnStatus = "ADD"
                    binding.btnStatus.visibility = View.VISIBLE
                }
            }

        } else {

            // based on aboue condition  depandes on above stauts
            if (!haveRun.equals("itsRun")) {
                if (dietPlanData.diet_plan_access_level.equals("LOCK")) {
                    var chaeckStatus: String = dietPlanData.diet_access_level.toUpperCase()

                    if (getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!.equals("Yes")
                    ) {
                        binding.btnStatus.text = "ADD TO MY DIET PLAN"
                        btnStatus = "ADD TO MY DIET PLAN"
                        binding.btnStatus.visibility = View.VISIBLE

                    }

                    else if (dietPlanData.diet_access_level.toUpperCase().equals("FREE")) {
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


    override fun onClick(view: View?) {

        when (view!!.id) {
            R.id.iv_back -> {
                activity?.onBackPressed()
            }
            R.id.iv_share -> {
                if (!dietPlanSubCatgory.diet_plan_share_url.isEmpty()) {
                    binding.ivShare.isEnabled=false
                    mActivity!!.sharePost(dietPlanSubCatgory.diet_plan_share_url)
                    Handler().postDelayed(Runnable { binding.ivShare.isEnabled=true },2000)

                }
            }

            R.id.btn_status -> {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                } else {
                    mLastClickTime = SystemClock.elapsedRealtime();
                }

                if (CommanUtils.isNetworkAvailable(mContext)!!) {
                    if (btnStatus.equals("ADD TO MY DIET PLAN")) {
                        if (!diet_plan_id.isEmpty()) {
                            addToMyDietPlan(diet_plan_id)
                        }
                    } else if (binding.btnStatus.text.toString().equals("PURCHASE", true)) {
                        if (getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED)!!.equals(
                                "0"
                            ) &&
                            getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!.equals("No", true)
                        ) {
                            startActivity(Intent(getActivity(), SubscriptionActivity::class.java).putExtra("home", "no"))
                        }
                    } else if (binding.btnStatus.text.toString().equals("Subscribers", true)) {
                        if (getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED)!!.equals(
                                "0"
                            ) &&
                            getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!.equals(
                                "No",
                                true
                            )
                        ) {

                            var intent = Intent(getActivity(), SubscriptionActivity::class.java).putExtra("home", "no")
                            intent.putExtra("fromDietPlan","fromDietPlan")
                            startActivityForResult(intent, 4);// Activity is started with requestCode 2
                        }
                    } else if (btnStatus.equals("ADD")) {

                        dietPlanSubCatgory.isAdd = "isAdd"

                        val intent = Intent()
                        intent.putExtra("addDietPlan", dietPlanSubCatgory)
                        mActivity!!.setResult(2, intent)
                        mActivity!!.finish()//finishing activity

                    } else if (btnStatus.equals("DELETE")) {
                        customDeleteDialog(dietPlanSubCatgory)
                    }
                } else {
                    Constant.showInternetConnectionDialog(mContext as Activity)
                }
            }
        }
    }

    private fun customDeleteDialog(dietPlanSubCatgory: DietPlanSubCategoryModal.Data.DietListing) {
        val dialog = Dialog(mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )

        dialog.setContentView(R.layout.delete_diet_plan_view)
        val tv_no = dialog.findViewById<TextView>(R.id.tv_no)
        val tv_delete = dialog.findViewById<TextView>(R.id.tv_delete)

        tv_no.setOnClickListener { v -> dialog.dismiss() }

        tv_delete.setOnClickListener { v ->

            dietPlanSubCatgory.isAdd = "isDelete"
            val intent = Intent()
            intent.putExtra("addDietPlan", dietPlanSubCatgory)
            mActivity!!.setResult(2, intent)
            mActivity!!.finish()

            dialog.dismiss()
        }
        dialog.show()
    }

    @Subscribe
    fun onEvent(changeButtonStatus: String) {
        if (changeButtonStatus.equals("Yes")) {
            btnStatus = ""
            binding.btnStatus.text = "ALREADY ADDED"
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 4 && data != null) {
            //startActivity(intent)
                if(dietPlanSubCatgory != null){
                    Log.v("egje","dshyu")
                    if(dietPlanSubCatgory.diet_plan_id != null && !dietPlanSubCatgory.diet_plan_id.isEmpty()){

                        binding.btnStatus.text = "ADD TO MY DIET PLAN"
                        binding.lockImg.visibility = View.GONE
                        //getDietPlanDetailsApi(dietPlanSubCatgory.diet_plan_id)
                    }
                }
        }

        if (requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {

                if (data!!.getStringExtra("comeAndBackFromDietDetail") != null) {
                    val comeAndBackFromDietDetail = data.getStringExtra("comeAndBackFromDietDetail")!!
                    if (comeAndBackFromDietDetail!!.equals("yes")) {
                        btnStatus = ""
                        binding.btnStatus.text = "ALREADY ADDED"
                    }
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }
}
