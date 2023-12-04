package com.doviesfitness.ui.showDietPlanDetail

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import com.google.android.material.tabs.TabLayout
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import com.doviesfitness.Doviesfitness
import com.doviesfitness.R
import com.doviesfitness.allDialogs.menu.DialogMenu
import com.doviesfitness.allDialogs.menu.ItemListDialogFragment
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.databinding.ActivityShowDietPlanDetailBinding
import com.doviesfitness.subscription.SubscriptionActivity
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.createAndEditDietPlan.activity.CreateWorkOutPlanActivty
import com.doviesfitness.ui.createAndEditDietPlan.modal.ShowWorkoutDetailModel
import com.doviesfitness.ui.showDietPlanDetail.fragment.ShowPagerDietPlanFragment
import com.doviesfitness.ui.showDietPlanDetail.fragment.ShowPagerPlanOverViewFragment
import com.doviesfitness.ui.showDietPlanDetail.fragment.ShowPagerWorkOutPlanFragment
import com.doviesfitness.ui.showDietPlanDetail.viewPAger.ShowPlanCategoryAdapter
import com.doviesfitness.utils.CommanUtils
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.StringConstant
import org.json.JSONObject
import java.util.*

class ShowDietPlanDetailActivity : BaseActivity(), View.OnClickListener,
    ItemListDialogFragment.DialogEventListener {

    private var mLastClickTime: Long = 0
    public var program_id: String = ""
    public var from: String = ""
    private lateinit var dialogFragment: ItemListDialogFragment
    private lateinit var menus: MutableList<DialogMenu>
    private lateinit var programDetailData: ShowWorkoutDetailModel.Data.GetCustomerProgramDetail
    private lateinit var binding: ActivityShowDietPlanDetailBinding
    private var weekOneLinstenr: WeekOneLinstenr? = null
    private var weekTwoLinstenr: WeekTwoLinstenr? = null
    private var weekThreeLinstenr: WeekThreeLinstenr? = null
    private var weekFourLinstenr: WeekFourLinstenr? = null
    private var getProgramDietPlanLitener: GetProgramDietPlanLitener? = null
    private var getOverViewLitener: GetOverViewLitener? = null
    private var forLOadFragmentLitener: ForLOadFragmentLitener? = null

    private var fragments = mutableListOf<androidx.fragment.app.Fragment>()
    var AllowUserList = ArrayList<String>()

    companion object{
        public  var program_id: String = ""
        public var isalloweduser:Boolean=false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideNavigationBar()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_diet_plan_detail)

        inItView()
    }

    private fun inItView() {

        if (intent.hasExtra("FromListToSeeDetail")) {
            if (intent.getStringExtra("FromListToSeeDetail") != null) {
                program_id = intent.getStringExtra("FromListToSeeDetail")!!
                if (!program_id.isEmpty()) {
                    getDataForDetail(program_id)
                }
            }
        }
        if (intent.hasExtra("from")) {
            if (intent.getStringExtra("from") != null) {
                from = intent.getStringExtra("from")!!
            }
        }

        fragments.add(ShowPagerWorkOutPlanFragment())
        fragments.add(ShowPagerDietPlanFragment())
        fragments.add(ShowPagerPlanOverViewFragment())

        //set Fragment as a list in viewPager adapter
        val showPlanCategoryAdapter = ShowPlanCategoryAdapter(supportFragmentManager, fragments)
        binding.planViewPager.setAdapter(showPlanCategoryAdapter)
        binding.planViewPager.setOffscreenPageLimit(3)

        binding.planCategoryTablayout.setupWithViewPager(binding.planViewPager)

        binding.planCategoryTablayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                hideKeyboard()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        setOnClick(binding.ivBack, binding.ivSettingProfile)
    }

    private fun setOnClick(vararg views: View) {
        for (view in views) {
            view.setOnClickListener(this)
        }
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.iv_back -> {
                onBackPressed()
            }

            R.id.iv_setting_profile -> {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return
                } else {
                    mLastClickTime = SystemClock.elapsedRealtime()
                }

                showDialogForEditAndOthers()
            }
        }
    }

    private fun showDialogForEditAndOthers() {
        menus = mutableListOf<DialogMenu>()
        menus.add(DialogMenu("Edit", R.drawable.ic_edit_workout))
        menus.add(DialogMenu("Share", R.drawable.ic_share))


        try {
            if(programDetailData != null) {
                if ("1".equals(programDetailData.is_program_favourite)) {
                    menus.add(DialogMenu("Unfavourite", R.drawable.ic_star_active))
                } else {
                    menus.add(DialogMenu("Favourite", R.drawable.ic_starinactivie))
                }
            }

            menus.add(DialogMenu("Delete", R.drawable.ic_recycling_bin))
            dialogFragment = ItemListDialogFragment.newInstance("NormalUser")
            dialogFragment.addMenu(menus)
            dialogFragment.addDialogEventListener(this)
            dialogFragment.show(supportFragmentManager!!, "NormalUser")
        }
        catch (ex:Exception){
            ex.printStackTrace()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun getDataForDetail(program_id: String) {
        binding.loader.visibility = View.VISIBLE
        binding.rltvLoader.visibility = View.VISIBLE
        val param = HashMap<String, String>()
        param.put(StringConstant.program_id, program_id)
        param.put(
            StringConstant.auth_customer_id,
            getDataManager().getUserInfo().customer_auth_token
        )

        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        header.put(StringConstant.apiKey, "HBDEV")
        header.put(StringConstant.apiVersion, "1")

        getDataManager().planDetail(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Log.v("response6", "" + response.toString())
                    val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                    val success: String? = jsonObject?.getString(StringConstant.success)
                    val message: String? = jsonObject?.getString(StringConstant.message)
                    if (success.equals("1")) {
                        try {
                            val showWorkoutDetailModel = getDataManager().mGson?.fromJson(
                                response.toString(),
                                ShowWorkoutDetailModel::class.java
                            )
                            setDataInDetails(showWorkoutDetailModel)
                            Log.d("TAG", "response...list size..." + showWorkoutDetailModel.toString())

                        }
                        catch (ex:java.lang.Exception){
                            ex.printStackTrace()
                        }

                    } else Constant.showCustomToast(getActivity(), "fail..." + message)
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, getActivity()!!)
                    Constant.showCustomToast(getActivity()!!, getString(R.string.something_wrong))
                }
            })
    }


    private fun setDataInDetails(showWorkoutDetailModel: ShowWorkoutDetailModel?) {

        binding.loader.visibility = View.GONE
        binding.rltvLoader.visibility = View.GONE
        //...................................get Detail Data.........................................................//
        val get_customer_program_detail = showWorkoutDetailModel!!.data.get_customer_program_detail
        programDetailData = get_customer_program_detail.get(0)
if(programDetailData.show_in_app.equals("No"))
{
    binding.txtUserName.visibility=View.GONE
}else{
    binding.txtUserName.visibility=View.VISIBLE
}
        binding.txtUserName.setText(programDetailData.program_name)
        binding.tvSubHeader.setText( CommanUtils.capitalize(programDetailData.program_sub_title))

        if (!programDetailData.program_full_image.isEmpty()) {

            if (!this.isDestroyed) {
                Glide.with(binding.ivUserProfile).load(programDetailData.program_portrait_image)
                    .placeholder(R.drawable.black_bg).into(binding.ivUserProfile)
            }
        }

        //...................................get WorkOut plan Data.........................................................//
//---------------------Allow User----------------------------------------
        val str = programDetailData.allowed_users
//
        val lstValues: List<String> = str.split(",").map { it -> it.trim() }
        lstValues.forEach { it ->
            Log.i("Values", "value=$it")
            AllowUserList.add(it)
            //Do Something
        }

        for (item in AllowUserList.indices) {
            println("marks[$item]: " + AllowUserList[item])
            val userInfoBean = Doviesfitness.getDataManager().getUserInfo()

            if (AllowUserList[item].equals(userInfoBean.customer_id)) {
                isalloweduser=true
                break
            }else{
                isalloweduser=false
            }
        }

        //------------------------------------------------------------------------
        val allWeekData = showWorkoutDetailModel.data.get_program_workouts
        var weekOne = allWeekData.Week1
        weekOneLinstenr?.getWeekOneData(allWeekData)
        weekTwoLinstenr?.getWeekTwoData(allWeekData)
        weekThreeLinstenr?.getWeekThreeData(allWeekData)
        weekFourLinstenr?.getWeekFourData(allWeekData)

        Handler().postDelayed(
            {
                try {
                    forLOadFragmentLitener?.forLoadFragment(allWeekData)
                }
                catch (ex:java.lang.Exception){
                    ex.printStackTrace()
                }
            }, 20
        )


        //overview
        getOverViewLitener?.getDietPlanData(programDetailData)

        // DietPlan
        val getDietPlanData = showWorkoutDetailModel.data.get_program_diet_plans
        getProgramDietPlanLitener?.getDietPlanData(getDietPlanData)
    }

    public fun forFragmentLoadListenr(forLOadFragmentLitener: ForLOadFragmentLitener) {
        this.forLOadFragmentLitener = forLOadFragmentLitener
    }

    public fun setDietPlanListenr(getProgramDietPlanLitener: GetProgramDietPlanLitener) {
        this.getProgramDietPlanLitener = getProgramDietPlanLitener
    }

    public fun setWeekOneDataListenr(weekOneLinstenr: WeekOneLinstenr) {
        this.weekOneLinstenr = weekOneLinstenr
    }

    public fun setWeekTwoDataListenr(weekTwoLinstenr: WeekTwoLinstenr) {
        this.weekTwoLinstenr = weekTwoLinstenr
    }

    public fun setWeekThreeDataListenr(weekThreeLinstenr: WeekThreeLinstenr) {
        this.weekThreeLinstenr = weekThreeLinstenr
    }

    public fun setWeekFourDataListenr(weekFourLinstenr: WeekFourLinstenr) {
        this.weekFourLinstenr = weekFourLinstenr
    }

    public fun setOverViewListenr(getOverViewLitener: GetOverViewLitener) {
        this.getOverViewLitener = getOverViewLitener
    }


    public interface WeekOneLinstenr {
        fun getWeekOneData(allWeekData: ShowWorkoutDetailModel.Data.GetProgramWorkouts)
    }

    public interface WeekTwoLinstenr {
        fun getWeekTwoData(allWeekData: ShowWorkoutDetailModel.Data.GetProgramWorkouts)
    }

    public interface WeekThreeLinstenr {
        fun getWeekThreeData(allWeekData: ShowWorkoutDetailModel.Data.GetProgramWorkouts)
    }

    public interface WeekFourLinstenr {
        fun getWeekFourData(allWeekData: ShowWorkoutDetailModel.Data.GetProgramWorkouts)
    }

    public interface GetProgramDietPlanLitener {
        fun getDietPlanData(dietPlanData: List<ShowWorkoutDetailModel.Data.GetProgramDietPlan>)
    }

    public interface GetOverViewLitener {
        fun getDietPlanData(overView: ShowWorkoutDetailModel.Data.GetCustomerProgramDetail)
    }


    public interface ForLOadFragmentLitener {
        fun forLoadFragment(allWeekData: ShowWorkoutDetailModel.Data.GetProgramWorkouts)
    }


    override fun onItemClicked(mCategoryTag: String, mMenuTag: String, position: Int) {
        if ("Admin".equals(mCategoryTag)) {
            if (mMenuTag.equals("Edit")) {
                val intent = Intent(this, CreateWorkOutPlanActivty::class.java)
                intent.putExtra("FromListToSeeDetail", programDetailData!!.program_id)
                intent.putExtra("programName", programDetailData!!.program_name)
                startActivity(intent)
            } else if (mMenuTag.equals("Publish")) {
                //publishWorkPlanApi(mData!!.program_id)
                // mActivity!!.sharePost(dataValue.program_share_url)
            } else if (mMenuTag.equals("Active")) {
                //addToFavApi(dataValue, pos)
            } else if (mMenuTag.equals("Delete")) {
                showDeleteDialog(this, programDetailData)
            } else {
                Constant.showCustomToast(this, mMenuTag)
            }
        } else if ("NormalUser".equals(mCategoryTag)) {

            if (mMenuTag.equals("Edit")) {

                if (getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED)!!.equals(
                        "0"
                    ) &&
                    getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!.equals(
                        "No",
                        true
                    )
                ) {
                    startActivity(
                        Intent(
                            getActivity(),
                            SubscriptionActivity::class.java
                        ).putExtra("home", "no")
                    )
                } else {
                    val intent = Intent(this, CreateWorkOutPlanActivty::class.java)
                    intent.putExtra("FromListToSeeDetail", programDetailData!!.program_id)
                    intent.putExtra("programName", programDetailData!!.program_name)
                    startActivity(intent)
                }
            } else if (mMenuTag.equals("Share")) {
                if (!programDetailData!!.program_share_url.isEmpty()) {
                    sharePost(programDetailData!!.program_share_url)
                }

            } else if (mMenuTag.equals("Favourite") || mMenuTag.equals("Unfavourite")) {
                addToFavApi()
            } else if (mMenuTag.equals("Delete")) {
                showDeleteDialog(this, programDetailData)
            } else {
                Constant.showCustomToast(this, mMenuTag)
            }
        }
    }

    private fun addToFavApi() {
        /*Delete plan api*/
        val param = HashMap<String, String>()

        param.put(StringConstant.module_id, programDetailData.program_id)
        param.put(StringConstant.module_name, "PROGRAM")
        param.put(StringConstant.device_token, "")
        param.put(StringConstant.device_type, StringConstant.Android)
        param.put(StringConstant.device_id, "")
        param.put(
            StringConstant.auth_customer_id,
            getDataManager().getUserInfo().customer_auth_token
        )

        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        getDataManager().addToMyPlanFavApi(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    try {
                        val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                        val status = json?.get(StringConstant.success)
                        val msg = json?.get(StringConstant.message)
                        if (status!!.equals("1")) {
                            if ("1".equals(programDetailData.is_program_favourite)) {
                                programDetailData.is_program_favourite = "0"

                            } else {
                                programDetailData.is_program_favourite = "1"
                            }

                            val toast = Toast.makeText(
                                this@ShowDietPlanDetailActivity,
                                "Done",
                                Toast.LENGTH_LONG
                            )
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else {
                            Constant.showCustomToast(this@ShowDietPlanDetailActivity, "" + msg)
                        }
                    } catch (ex: Exception) {
                        Constant.showCustomToast(
                            this@ShowDietPlanDetailActivity,
                            getString(R.string.something_wrong)
                        )
                    }
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, this@ShowDietPlanDetailActivity as Activity)
                }
            })
    }

    override fun onDialogDismiss() {

    }


/* override fun onResume() {
     super.onResume()
     if (!program_id.isEmpty()) {
         getDataForDetail(program_id)
     }
 }*/

    /*.............................//Custom dialog for Delete....................................*/
    fun showDeleteDialog(
        activity: Activity,
        programDetailData: ShowWorkoutDetailModel.Data.GetCustomerProgramDetail
    ) {
        val dialog = Dialog(activity)

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
            deletePlanApi(programDetailData)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun deletePlanApi(programDetailData: ShowWorkoutDetailModel.Data.GetCustomerProgramDetail) {
        /*Delete plan api*/
        val param = HashMap<String, String>()
        param.put(
            StringConstant.auth_customer_id,
            getDataManager().getUserInfo().customer_auth_token
        )
        param.put(StringConstant.program_id, programDetailData.program_id)
        param.put(StringConstant.device_token, "")
        param.put(StringConstant.device_type, StringConstant.Android)
        param.put(StringConstant.device_id, "")

        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        getDataManager().deletePlanApi(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    try {
                        val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                        val status = json?.get(StringConstant.success)
                        val msg = json?.get(StringConstant.message)
                        if (status!!.equals("1")) {
                            val toast = Toast.makeText(
                                this@ShowDietPlanDetailActivity,
                                "Done",
                                Toast.LENGTH_LONG
                            )
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show()
                            if (from.equals("favorite"))
                                setResult(Activity.RESULT_OK)
                            onBackPressed()
                        }
                    } catch (ex: Exception) {
                        Constant.showCustomToast(
                            this@ShowDietPlanDetailActivity,
                            getString(R.string.something_wrong)
                        )
                    }
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, this@ShowDietPlanDetailActivity)
                }
            })
    }
}
