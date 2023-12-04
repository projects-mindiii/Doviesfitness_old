package com.doviesfitness.ui.createAndEditDietPlan.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.text.Html
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.doviesfitness.R
import com.doviesfitness.allDialogs.ErrorDialog
import com.doviesfitness.allDialogs.SelectionForEditAndCreatDialog
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.data.remote.Webservice
import com.doviesfitness.databinding.ActivityCreateWorkOutPlanActivtyBinding
import com.doviesfitness.ui.authentication.signup.dialog.ImagePickerDialog
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.bottom_tabbar.HomeTabActivity
import com.doviesfitness.ui.bottom_tabbar.diet_plan.modal.DietPlanSubCategoryModal
import com.doviesfitness.ui.bottom_tabbar.workout_tab.activity.ActivityGoodFor
import com.doviesfitness.ui.bottom_tabbar.workout_tab.dialog.FinishActivityDialog
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.CustomerListModel
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.FilterExerciseResponse
import com.doviesfitness.ui.createAndEditDietPlan.fragment.PagerDietPlanFragment
import com.doviesfitness.ui.createAndEditDietPlan.fragment.PagerPlanOverViewFragment
import com.doviesfitness.ui.createAndEditDietPlan.fragment.PagerWorkOutPlanFragment
import com.doviesfitness.ui.createAndEditDietPlan.modal.ShowWorkoutDetailModel
import com.doviesfitness.ui.createAndEditDietPlan.viewPagerAdapters.PlanCategoryAdapter
import com.doviesfitness.ui.profile.myWorkOut.modal.WorkOutListModal
import com.doviesfitness.utils.*
import com.doviesfitness.utils.Utility.Companion.toEditable
import com.google.android.material.tabs.TabLayout
import com.squareup.picasso.Picasso
import com.yalantis.ucrop.UCrop
import eightbitlab.com.blurview.RenderScriptBlur
import kotlinx.android.synthetic.main.activity_create_work_out_plan_activty.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.collections.ArrayList
class CreateWorkOutPlanActivty : BaseActivity(), View.OnClickListener,
    WorkoutLevelDialog.HeightWeightCallBack,
    SelectionForEditAndCreatDialog.WorkPlanEditAndCreatCallBack, ErrorDialog.IsOK {
    override fun isOk() {}

    private var selectedIndex: Int = 0
    private var isTrueWhenEdit: Boolean = false
    private var fromApiCall: Boolean = true
    private lateinit var allWeekData: ShowWorkoutDetailModel.Data.GetProgramWorkouts
    private lateinit var programDetailData: ShowWorkoutDetailModel.Data.GetCustomerProgramDetail
    private var fromEditDetail: Boolean = false
    private lateinit var binding: ActivityCreateWorkOutPlanActivtyBinding

    private var allowed_user_id: String = ""
    private var workoutPLanListArray: JSONArray? = null
    private var dietListInComa: String = ""
    private var overView: String = ""

    var accessLevelStr = ""
    var displayNewsfeedstr = ""
    var displayNewsfeedValue = ""
    var allowNotificationstr = ""
    var allowNotificationvalue = ""
    var accessLevelValue = ""
    var equipmentIdStr = ""
    var goodForIdStr = ""
    var showInApp = "Yes"

    var userImageUrl = ""

    private var userImageFile: File? = null
    private lateinit var dietPlanSubCatgory: DietPlanSubCategoryModal.Data.DietListing
    var tempAllowedUserList = java.util.ArrayList<CustomerListModel.Data.User>()
    private var fragments = mutableListOf<androidx.fragment.app.Fragment>()
    private var isAdmin = "No"
    private var program_id = ""
    private var mLastClickTime: Long = 0

    private var exerciseList = ArrayList<FilterExerciseResponse.Data>()
    private var value = ""
    private var weekOneLinstenr: WeekOneLinstenr? = null
    private var weekTwoLinstenr: WeekTwoLinstenr? = null
    private var weekThreeLinstenr: WeekThreeLinstenr? = null
    private var weekFourLinstenr: WeekFourLinstenr? = null
    private var getProgramDietPlanLitener: GetProgramDietPlanLitener? = null
    private var getOverViewLitener: GetOverViewLitener? = null
var checkaddlist:Boolean=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_create_work_out_plan_activty)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }

        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_create_work_out_plan_activty)
        inItView()
    }

    private fun inItView() {
        hideNavigationBar()
        val windowBackground = window.decorView.background
        binding.topBlurView.setupWith(binding.containerId)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(this))
            .setBlurRadius(25f)
            .setHasFixedTransformationMatrix(true)


        isAdmin = getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!
        // for admin view only for admin
        isAdminUser(isAdmin)

        if (intent.hasExtra("FromListToSeeDetail")) {
            if (intent.getStringExtra("FromListToSeeDetail") != null) {
                program_id = intent.getStringExtra("FromListToSeeDetail")!!
                val programNAme = intent.getStringExtra("programName")
                if (!program_id.isEmpty()) {
                    binding.dpTitleName.text = "" + programNAme
                    getDataForDetail(program_id)
                }
            }
        } else {
            // get list for goods for and others that way we call
            getFilterWorkoutData(isTrueWhenEdit)
        }

        binding.workoutName.hint = (Html.fromHtml(getString(R.string.Diet_workout_name)))
        binding.workoutSubTitle.hint = (Html.fromHtml(getString(R.string.Diet_workout_sub_title)))
        binding.levelName.hint = (Html.fromHtml(getString(R.string.level_str)))
        binding.goodFor.hint = (Html.fromHtml(getString(R.string.good_for_str)))
        binding.tvEquipment.hint = (Html.fromHtml(getString(R.string.equipments_str)))
        binding.accessLevel.hint = (Html.fromHtml(getString(R.string.access_level)))
        binding.displayNewsfeed.hint = (Html.fromHtml(getString(R.string.display_in_newsfeed)))
        binding.appShow.hint = (Html.fromHtml(getString(R.string.show_in_App)))

        binding.allowNotification.hint = (Html.fromHtml(getString(R.string.allow_notification)));
        binding.txtAmount.hint = (Html.fromHtml(getString(R.string.amount)));


        if (intent.hasExtra("dietPlanAdd")) {
            if (intent.extras!!.getParcelable<DietPlanSubCategoryModal.Data.DietListing>("dietPlanAdd") != null) {
                dietPlanSubCatgory =
                    intent.getParcelableExtra<DietPlanSubCategoryModal.Data.DietListing>("dietPlanAdd")!!

                fragments.add(PagerWorkOutPlanFragment())
                val f1 = PagerDietPlanFragment()
                val b = Bundle().apply { putParcelable("dietPlanAdd", dietPlanSubCatgory) }
                f1.arguments = b
                fragments.add(f1)

                fragments.add(PagerPlanOverViewFragment())
            }
        } else {
            fragments.add(PagerWorkOutPlanFragment())
            fragments.add(PagerDietPlanFragment())
            fragments.add(PagerPlanOverViewFragment())
        }

        //set Fragment as a list india viewPager adapter
        val planCategoryAdapter = PlanCategoryAdapter(supportFragmentManager, fragments)
        binding.planViewPager.setAdapter(planCategoryAdapter)
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

        setOnClick(
            binding.txtCancel,
            binding.levelLayout,
            binding.goodForLayout,
            binding.equipmentLayout,
            binding.allowedUsersLayout,
            binding.accessLevelLayout,
            binding.dpAmountLayout,
            binding.displayNewsfeedLayout,
            binding.llShowApp,
            binding.txtAmount,
            binding.allowNotificationLayout,
            binding.imgLayout,
            binding.ivSubmitData,
            binding.containerId
        )

        //for without select Acsess level {free} it is not editable
        binding.txtAmount.isEnabled = false

        getJSonArrayForWeekPlanWhenNoData()
    }

    private fun getDataForDetail(program_id: String) {
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
                    Log.v("response", "" + response)
                    Log.v("response6", "" + response.toString())
                    val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                    val success: String? = jsonObject?.getString(StringConstant.success)
                    val message: String? = jsonObject?.getString(StringConstant.message)
                    if (success.equals("1")) {
                        val showWorkoutDetailModel = getDataManager().mGson?.fromJson(
                            response.toString(),
                            ShowWorkoutDetailModel::class.java
                        )
                        setDataInDetails(showWorkoutDetailModel)

                    } else Constant.showCustomToast(getActivity(), "fail..." + message)
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, getActivity()!!)
                    Constant.showCustomToast(getActivity()!!, getString(R.string.something_wrong))
                }
            })
    }

    private fun setDataInDetails(showWorkoutDetailModel: ShowWorkoutDetailModel?) {


        fromEditDetail = true  // when come freom Edit Workout
        //...................................get Detail Data.........................................................//
        val get_customer_program_detail = showWorkoutDetailModel!!.data.get_customer_program_detail
        programDetailData = get_customer_program_detail.get(0)
        isTrueWhenEdit = true
        getFilterWorkoutData(isTrueWhenEdit)

        binding.workoutName.hint = (Html.fromHtml(getString(R.string.workout_name)));
        binding.tvEquipment.text = getSpanningString(programDetailData.equipmentMasterName)
        binding.equipmemnts.visibility = View.VISIBLE
        binding.goodFor.text = getSpanningString(programDetailData.goodforMasterName)
        binding.txtGoodFor.visibility = View.VISIBLE
        binding.workoutName.setText("" + programDetailData.program_name)
        binding.workoutSubTitle.setText("" + programDetailData.program_sub_title)
        //userImageUrl = programDetailData.program_image
        overView = programDetailData.program_description
        // get url to file of image

        userImageUrl = programDetailData.program_portrait_image
        getImageInFile(programDetailData.program_portrait_image)

        binding.image1.visibility = View.GONE
        binding.imgTxt.visibility = View.GONE
        binding.levelName.text = getSpanningString(programDetailData.program_level)
        binding.goodLevel.visibility = View.VISIBLE
        if (isAdmin.equals("Yes", true)) {
            binding.allowedUsersTxt.text = getSpanningString(programDetailData.allowedUsersName)
            allowed_user_id = programDetailData.allowed_users
            getUsersIds(programDetailData.allowed_users)
            binding.txtAllowed.visibility = View.VISIBLE

            var displayValue =
                getPickerValue("Access Level \n", programDetailData.program_access_level_select)
            binding.accessLevel.text = displayValue
            //------------------new changes------------------------------
      /*      var show_app =
                getPickerValue("Show In App \n", programDetailData.program_access_level_select)
            binding.accessLevel.text = displayValue
            value = ""
            accessLevelValue = programDetailData.program_access_level_select*/

            //for without select Acsess level {free} it is not editable
            if (programDetailData.program_access_level_select.equals("Paid") || programDetailData.program_access_level_select.equals("Exclusive")) {
                binding.txtAmount.isEnabled = true
                binding.txtAmount.text=programDetailData.program_amount.toEditable()
                binding.dpAmountLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorBlack
                    )
                )
            } else {
                binding.txtAmount.isEnabled = false
                binding.dpAmountLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorBlack
                    )
                )
            }

            displayValue =
                getPickerValue("Allow Notification \n", programDetailData.allow_notification)
            binding.allowNotification.text = displayValue
            allowNotificationvalue = programDetailData.allow_notification

            displayValue = getPickerValue("Display in Newsfeed? \n", programDetailData.is_featured)
            binding.displayNewsfeed.text = displayValue
            displayNewsfeedValue = programDetailData.is_featured

            displayValue = getPickerValue("Show Name In App? \n", programDetailData.show_in_app)
            binding.appShow.text = displayValue
            showInApp = programDetailData.show_in_app
        }

        //...................................get WorkOut plan Data.........................................................//


        //Handler().postDelayed({
        allWeekData = showWorkoutDetailModel.data.get_program_workouts
        //}, 500)

        //allWeekData = showWorkoutDetailModel.data.get_program_workouts
        //var weekOne = allWeekData.Week1
        weekOneLinstenr?.getWeekOneData(allWeekData)
        weekTwoLinstenr?.getWeekTwoData(allWeekData)
        weekThreeLinstenr?.getWeekThreeData(allWeekData)
        weekFourLinstenr?.getWeekFourData(allWeekData)

        //get json Array from weekplan
        getJSonArrayForWeekPlan(allWeekData)

        //overview
        getOverViewLitener?.getDietPlanData(programDetailData.program_description)

        // DietPlan
        val getDietPlanData = showWorkoutDetailModel.data.get_program_diet_plans
        getProgramDietPlanLitener?.getDietPlanData(getDietPlanData)


        //setDataIN Diet list for Edit
        val addDietPlanList = ArrayList<DietPlanSubCategoryModal.Data.DietListing>()
        if (getDietPlanData != null) {

            for (i in getDietPlanData.indices) {
                val dietPlanList = getDietPlanData.get(i)

                val dietDietPlanLISt = DietPlanSubCategoryModal.Data.DietListing(
                    diet_plan_id = dietPlanList.program_diet_id,
                    diet_plan_title = dietPlanList.program_diet_name,
                    diet_plan_pdf = dietPlanList.diet_plan_pdf,
                    diet_plan_full_image = dietPlanList.program_diet_image,
                    diet_plan_image = dietPlanList.program_diet_image
                )
                addDietPlanList.add(dietDietPlanLISt)
            }

            //GEt Diet PLan List
            getDietPlanListInComa(addDietPlanList)
        }
    }


    private fun getUsersIds(allowedUserIds: String) {
        tempAllowedUserList.clear()
        var tempList = allowedUserIds.split(",")
        for (k in 0..tempList.size - 1) {
            tempAllowedUserList.add(CustomerListModel.Data.User(tempList.get(k), "", "", true))
        }
    }


    private fun getSpanningString(workout_good_for: String): SpannableString {
        val builder = SpannableStringBuilder()
        val spanStr = SpannableString(workout_good_for)
        spanStr.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.created_by_color)),
            0,
            spanStr.length,
            0
        )
        builder.append(spanStr)
        var ss = SpannableString(builder)
        return ss
    }

    private fun getImageInFile(program_image: String) {

        Glide.with(getActivity()).asBitmap().load(program_image)
            .listener(object : RequestListener<Bitmap?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap?>?,
                    isFirstResource: Boolean
                ): Boolean {

                    return true
                }

                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: Target<Bitmap?>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {

                    binding.image.setImageBitmap(resource)
                    userImageFile = File(getCacheDir(), "dovies");
                    userImageFile!!.createNewFile();
                    val bitmap = resource
                    val bos = ByteArrayOutputStream();
                    bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    val bitmapdata = bos.toByteArray();
                    val fos = FileOutputStream(userImageFile);
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();
                    return true
                }
            }).into(binding.image)

        binding.image1.visibility = View.GONE
        binding.imgTxt.visibility = View.GONE
    }


    fun getJSonArrayForWeekPlan(allWeekData: ShowWorkoutDetailModel.Data.GetProgramWorkouts) {
        val weekOneList = allWeekData.Week1
        val weekTwoList = allWeekData.Week2
        val weekThreeList = allWeekData.Week3
        val weekFourList = allWeekData.Week4

        val weekPlanOneList = ArrayList<WorkOutListModal.Data>()
        val weekPlanTwoList = ArrayList<WorkOutListModal.Data>()
        val weekPlanThreeList = ArrayList<WorkOutListModal.Data>()
        val weekPlanFourList = ArrayList<WorkOutListModal.Data>()
        val tempWeekPlanList = ArrayList<WorkOutListModal.Data>()

        if (weekOneList != null) {

            for (i in weekOneList.indices) {
                val weekDataObject = weekOneList.get(i)
                val myWorkOutData = WorkOutListModal.Data(
                    workout_name = weekDataObject.program_workout_name,
                    workout_category = weekDataObject.program_workout_good_for,
                    workout_time = weekDataObject.program_workout_time,
                    workout_image = weekDataObject.program_workout_image,
                    workout_fav_status = weekDataObject.program_workout_status,
                    workout_id = weekDataObject.workout_id,
                    workout_share_url = weekDataObject.program_workout_flag,
                    workout_time1 = weekDataObject.program_workout_time,
                    forDay = weekDataObject.program_day_number,
                    whichWeek = weekDataObject.program_week_number
                )

                weekPlanOneList.add(myWorkOutData)
            }
        } else {
            for (i in 0..6) {
                val myWorkOutData = WorkOutListModal.Data(
                    workout_id = "0",
                    forDay = "" + i,
                    whichWeek = "1"
                )
                weekPlanOneList.add(myWorkOutData)
            }
        }

        if (weekTwoList != null) {

            for (i in weekTwoList.indices) {
                val weekDataObject = weekTwoList.get(i)
                val myWorkOutData = WorkOutListModal.Data(
                    workout_name = weekDataObject.program_workout_name,
                    workout_category = weekDataObject.program_workout_good_for,
                    workout_time = weekDataObject.program_workout_time,
                    workout_image = weekDataObject.program_workout_image,
                    workout_fav_status = weekDataObject.program_workout_status,
                    workout_id = weekDataObject.workout_id,
                    workout_share_url = weekDataObject.program_workout_flag,
                    workout_time1 = weekDataObject.program_workout_time,
                    forDay = weekDataObject.program_day_number,
                    whichWeek = weekDataObject.program_week_number
                )
                weekPlanTwoList.add(myWorkOutData)
            }
        } else {
            for (i in 0..6) {
                val myWorkOutData = WorkOutListModal.Data(
                    workout_id = "0",
                    forDay = "" + i,
                    whichWeek = "2"
                )
                weekPlanTwoList.add(myWorkOutData)
            }
        }

        if (weekThreeList != null) {

            for (i in weekThreeList.indices) {
                val weekDataObject = weekThreeList.get(i)
                val myWorkOutData = WorkOutListModal.Data(
                    workout_name = weekDataObject.program_workout_name,
                    workout_category = weekDataObject.program_workout_good_for,
                    workout_time = weekDataObject.program_workout_time,
                    workout_image = weekDataObject.program_workout_image,
                    workout_fav_status = weekDataObject.program_workout_status,
                    workout_id = weekDataObject.workout_id,
                    workout_share_url = weekDataObject.program_workout_flag,
                    workout_time1 = weekDataObject.program_workout_time,
                    forDay = weekDataObject.program_day_number,
                    whichWeek = weekDataObject.program_week_number
                )

                weekPlanThreeList.add(myWorkOutData)
            }
        } else {
            for (i in 0..6) {
                val myWorkOutData = WorkOutListModal.Data(
                    workout_id = "0",
                    forDay = "" + i,
                    whichWeek = "3"
                )
                weekPlanThreeList.add(myWorkOutData)
            }
        }

        if (weekFourList != null) {

            for (i in weekFourList.indices) {
                val weekDataObject = weekFourList.get(i)
                val myWorkOutData = WorkOutListModal.Data(
                    workout_name = weekDataObject.program_workout_name,
                    workout_category = weekDataObject.program_workout_good_for,
                    workout_time = weekDataObject.program_workout_time,
                    workout_image = weekDataObject.program_workout_image,
                    workout_fav_status = weekDataObject.program_workout_status,
                    workout_id = weekDataObject.workout_id,
                    workout_share_url = weekDataObject.program_workout_flag,
                    workout_time1 = weekDataObject.program_workout_time,
                    forDay = weekDataObject.program_day_number,
                    whichWeek = weekDataObject.program_week_number
                )

                weekPlanFourList.add(myWorkOutData)
            }
        } else {
            for (i in 0..6) {
                val myWorkOutData = WorkOutListModal.Data(
                    workout_id = "0",
                    forDay = "" + i,
                    whichWeek = "4"
                )
                weekPlanFourList.add(myWorkOutData)
            }
        }

        tempWeekPlanList.addAll(weekPlanOneList)
        tempWeekPlanList.addAll(weekPlanTwoList)
        tempWeekPlanList.addAll(weekPlanThreeList)
        tempWeekPlanList.addAll(weekPlanFourList)

        //get json value from this
        getJsonArrayOfWeekPlan(tempWeekPlanList)
    }

    fun getJSonArrayForWeekPlanWhenNoData() {

        val weekPlanOneList = ArrayList<WorkOutListModal.Data>()
        val weekPlanTwoList = ArrayList<WorkOutListModal.Data>()
        val weekPlanThreeList = ArrayList<WorkOutListModal.Data>()
        val weekPlanFourList = ArrayList<WorkOutListModal.Data>()
        val tempWeekPlanList = ArrayList<WorkOutListModal.Data>()


        for (i in 0..6) {
            val myWorkOutData = WorkOutListModal.Data(
                workout_id = "0",
                forDay = "" + i,
                whichWeek = "1"
            )
            weekPlanOneList.add(myWorkOutData)
        }

        for (i in 0..6) {
            val myWorkOutData = WorkOutListModal.Data(
                workout_id = "0",
                forDay = "" + i,
                whichWeek = "2"
            )
            weekPlanTwoList.add(myWorkOutData)
        }

        for (i in 0..6) {
            val myWorkOutData = WorkOutListModal.Data(
                workout_id = "0",
                forDay = "" + i,
                whichWeek = "3"
            )
            weekPlanThreeList.add(myWorkOutData)
        }

        for (i in 0..6) {
            val myWorkOutData = WorkOutListModal.Data(
                workout_id = "0",
                forDay = "" + i,
                whichWeek = "4"
            )
            weekPlanFourList.add(myWorkOutData)
        }

        tempWeekPlanList.addAll(weekPlanOneList)
        tempWeekPlanList.addAll(weekPlanTwoList)
        tempWeekPlanList.addAll(weekPlanThreeList)
        tempWeekPlanList.addAll(weekPlanFourList)

        //get json value from this
        getJsonArrayOfWeekPlan(tempWeekPlanList)
    }

    // allowed user
    fun getAllowedUSerList(allowedUserIds: String) {
        tempAllowedUserList.clear()
        val tempList = allowedUserIds.split(",")
        for (k in 0..tempList.size - 1) {
            tempAllowedUserList.add(CustomerListModel.Data.User(tempList.get(k), "", "", true))
        }
    }

    private fun getJsonArrayOfWeekPlan(completeWeekPlanList: ArrayList<WorkOutListModal.Data>) {
        workoutPLanListArray = JSONArray()

        for (i in completeWeekPlanList.indices) {
            val workoutJSonObject = JSONObject()
            val workout_day_num = completeWeekPlanList.get(i).forDay
            val workout_id = completeWeekPlanList.get(i).workout_id
            val workout_week_num = completeWeekPlanList.get(i).whichWeek

            workoutJSonObject.put("workout_day_num", workout_day_num)
            workoutJSonObject.put("workout_id", workout_id)
            workoutJSonObject.put("workout_week_num", workout_week_num)
            workoutPLanListArray!!.put(workoutJSonObject)

            Log.v("json1", "" + workoutPLanListArray.toString())
        }
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

    // fo binding.txtAmount.hint admin view only for admin
    private fun isAdminUser(admin: String) {
        if (admin.equals("Yes", true)) {
            binding.llForAdminUseOnly.visibility = View.VISIBLE
            binding.llShowApp.visibility=View.VISIBLE
        } else {
            binding.llForAdminUseOnly.visibility = View.GONE
            binding.llShowApp.visibility=View.GONE
        }
    }

    private fun getFilterWorkoutData(isTrueWhenEdit: Boolean) {
        val param = HashMap<String, String>()
        param.put(StringConstant.device_token, "")
        param.put(StringConstant.device_id, "")
        param.put(StringConstant.device_type, StringConstant.Android)
        param.put(StringConstant.module_type, "")
        param.put(StringConstant.auth_customer_id, "")

        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        header.put(StringConstant.apiKey, "HBDEV")
        header.put(StringConstant.apiVersion, "1")

        getDataManager().filterWorkoutApi(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Log.d("TAG", "filter response...." + response!!.toString(4))

                    val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                    val success: String? = jsonObject?.getString(StringConstant.success)
                    val message: String? = jsonObject?.getString(StringConstant.message)
                    if (success.equals("1")) {
                        //  Constant.showCustomToast(getActivity(), "success..." + message)
                        val exerciseData =
                            getDataManager().mGson?.fromJson(
                                response.toString(),
                                FilterExerciseResponse::class.java
                            )
                        exerciseList.addAll(exerciseData!!.data)

                        if (isTrueWhenEdit == true) {
                            equipmentIdStr = getIds(programDetailData!!.program_equipments, 1)
                            goodForIdStr = getIds(programDetailData!!.program_good_for, 4)
                        }

                        Log.d("TAG", "response...list size..." + exerciseData.toString())

                    } else Constant.showCustomToast(getActivity(), "fail..." + message)
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, getActivity()!!)
                    Constant.showCustomToast(getActivity()!!, getString(R.string.something_wrong))
                }
            })
    }

    private fun setOnClick(vararg views: View) {
        for (view in views) {
            view.setOnClickListener(this)
        }
    }

    override fun onClick(view: View?) {

        when (view!!.id) {

            R.id.container_id -> {
                hideNavigationBar()
                hideKeyboard()
            }

            R.id.iv_submit_data -> {
                lastClick()

                if (CommanUtils.isNetworkAvailable(this)!!) {
                    // when he will come from edit then
                    if (fromEditDetail) {
                        if ("Yes".equals(isAdmin)) {
                            SelectionForEditAndCreatDialog.newInstance(this)
                                .show(supportFragmentManager)
                        } else {
                            if (program_id != null && !program_id.isEmpty()) {
                                if (isValidData()) {
                                    addANdEditWorkout(program_id)
                                    //addCompletePlan(program_id)
                                }
                            }
                        }
                    } else {
                        if (isValidData()) {
                            //addCompletePlan(program_id)
                            addANdEditWorkout(program_id)
                        }
                    }
                } else {
                    Constant.showInternetConnectionDialog(this)
                }

            }

            R.id.txt_cancel -> {
                hideKeyboard()
                hideKeyboard()
                hideNavigationBar()
                checkaddlist=false
                for (k in 0..workoutPLanListArray!!.length()-1) {
                    val workoutJSonObject = workoutPLanListArray!!.getJSONObject(k)
                    val workoutid = workoutJSonObject.getString("workout_id")
                    if (!workoutid.equals("0")) {
                        checkaddlist=true
                    }
                }

                if (isAnyValue() == true) {
                    if(checkaddlist==true) {

                        FinishActivityDialog.newInstance(
                            "Yes",
                            "No",
                            "All changes done to the plan will be lost. Are you sure you want to cancel?"
                        ).show(supportFragmentManager)
                    }else{
                        finish()
                    }

                } else {
                    FinishActivityDialog.newInstance(
                        "Yes",
                        "No",
                        "All changes done to the plan will be lost. Are you sure you want to cancel?"
                    ).show(supportFragmentManager)
                }
            }


            R.id.good_for_layout -> {
                lastClick()
                if (exerciseList.size>0) {
                    val intent = Intent(getActivity(), ActivityGoodFor::class.java)
                    intent.putExtra("itemList", exerciseList.get(4).list)
                    intent.putExtra("from", "")
                    startActivityForResult(intent, 1)
                }
            }
            R.id.level_layout -> {
                lastClick()
                value = "level"
                val values = arrayListOf<String>()
                values.add("Advance")
                values.add("Basic")
                values.add("Moderate")
                values.add("All Levels")
                values.toTypedArray()
                if (!binding.levelName.text.toString().trim().isEmpty()) {
                    for (i in 0..values.size - 1) {
                        if (binding.levelName.text.toString().trim().equals(values.get(i))) {
                            selectedIndex = i
                        }
                    }
                } else {
                    selectedIndex = 0
                }
                val openDialog =
                    WorkoutLevelDialog.newInstance(values, this, getActivity(), selectedIndex)
                        .show(supportFragmentManager)

            }
            R.id.equipment_layout -> {
                lastClick()
                if (exerciseList.size>0) {
                    val intent = Intent(getActivity(), ActivityGoodFor::class.java)
                    intent.putExtra("itemList", exerciseList.get(1).list)
                    intent.putExtra("from", "")
                    startActivityForResult(intent, 2)
                }
            }

            R.id.allowed_users_layout -> {
                lastClick()
                val intent = Intent(getActivity(), ActivityGoodFor::class.java)
                intent.putExtra("from", "users")
                intent.putExtra("itemList", tempAllowedUserList)
                startActivityForResult(intent, 3)
            }
            R.id.access_level_layout -> {

                lastClick()

                value = "access"
                val values = arrayListOf<String>()
                values.add("Free")
                values.add("Subscribers")
                values.add("Paid")
                values.add("Exclusive")
                values.toTypedArray()
                if (!accessLevelValue.isEmpty()) {
                    for (i in 0..values.size - 1) {
                        if (accessLevelValue.equals(values.get(i))) {
                            selectedIndex = i
                        }
                    }
                } else {
                    selectedIndex = 0
                }
                val openDialog =
                    WorkoutLevelDialog.newInstance(values, this, getActivity(), selectedIndex)
                        .show(supportFragmentManager)
            }
            R.id.ll_show_app->{
                lastClick()

                value = "name"
                val values = arrayListOf<String>()
                values.add("Yes")
                values.add("No")
                values.toTypedArray()

                if (!showInApp.isEmpty()) {
                    for (i in 0..values.size - 1) {
                        if (showInApp.equals(values.get(i))) {
                            selectedIndex = i
                        }
                    }
                } else {
                    selectedIndex = 0
                }

                val openDialog =
                    WorkoutLevelDialog.newInstance(values, this, getActivity(), selectedIndex)
                        .show(supportFragmentManager)
            }

            R.id.display_newsfeed_layout -> {

                lastClick()

                value = "news"
                val values = arrayListOf<String>()
                values.add("Yes")
                values.add("No")
                values.toTypedArray()

                if (!displayNewsfeedValue.isEmpty()) {
                    for (i in 0..values.size - 1) {
                        if (displayNewsfeedValue.equals(values.get(i))) {
                            selectedIndex = i
                        }
                    }
                } else {
                    selectedIndex = 0
                }

                val openDialog =
                    WorkoutLevelDialog.newInstance(values, this, getActivity(), selectedIndex)
                        .show(supportFragmentManager)
            }
            R.id.allow_notification_layout -> {
                lastClick()
                value = "notification"
                val values = arrayListOf<String>()
                values.add("Yes")
                values.add("No")
                values.toTypedArray()

                if (!allowNotificationvalue.isEmpty()) {
                    for (i in 0..values.size - 1) {
                        if (allowNotificationvalue.equals(values.get(i))) {
                            selectedIndex = i
                        }
                    }
                } else {
                    selectedIndex = 0
                }

                val openDialog =
                    WorkoutLevelDialog.newInstance(values, this, getActivity(), selectedIndex)
                        .show(supportFragmentManager)
            }
            R.id.img_layout -> {
                ImagePickerDialog.newInstance(this).show(supportFragmentManager)
            }
        }
    }

    private fun isAnyValue(): Boolean {
        var isAnyValue = false

        if (isAdmin.equals("Yes", true)) {
//            binding.workoutName.text.toString().isEmpty() &&
                    if (userImageFile == null && binding.levelName.text.toString().isEmpty()
                && binding.goodFor.text.toString().isEmpty() && binding.tvEquipment.text.toString().isEmpty() && binding.txtAmount.text.toString().isEmpty()
                && binding.accessLevel.text.toString().isEmpty() && binding.displayNewsfeed.text.toString().isEmpty() &&
                binding.allowNotification.text.toString().isEmpty()
            ) {
                isAnyValue = true
            }
        } else {
            if (binding.workoutName.text.toString().isEmpty() && userImageFile == null && binding.levelName.text.toString().isEmpty()
                && binding.goodFor.text.toString().isEmpty() && binding.tvEquipment.text.toString().isEmpty()
            ) {
                isAnyValue = true
            }
        }
        return isAnyValue
    }

    private fun addANdEditWorkout(programId: String) {
        binding.loader.visibility = View.VISIBLE
        val multiPartBuilder = AndroidNetworking.upload(Webservice.ADD_TO_COMPLETE_PROGRAM)
        if (userImageFile != null) {
            multiPartBuilder.addMultipartFile("program_image", userImageFile)
        } else {
            multiPartBuilder.addMultipartParameter("program_image", userImageUrl)
        }

        //first time when you create plan then it will be blank
        if (!program_id.isEmpty()) {
            multiPartBuilder.addMultipartParameter("program_id", "" + programId)
        }

        multiPartBuilder.addMultipartParameter(StringConstant.device_token, "")
        multiPartBuilder.addMultipartParameter(StringConstant.device_id, "")
        multiPartBuilder.addMultipartParameter(StringConstant.device_type, StringConstant.Android)
        multiPartBuilder.addMultipartParameter(StringConstant.auth_customer_id, "")

        multiPartBuilder.addMultipartParameter(
            "program_name",
            binding.workoutName.text.toString().trim()
        )
        multiPartBuilder.addMultipartParameter(
            "program_sub_title",
            binding.workoutSubTitle.text.toString().trim()
        )

        multiPartBuilder.addMultipartParameter(
            "program_level",
            binding.levelName.text.toString().trim()
        )
        multiPartBuilder.addMultipartParameter("program_goodfor", goodForIdStr)
        multiPartBuilder.addMultipartParameter("program_equipments", "" + equipmentIdStr)

        multiPartBuilder.addMultipartParameter("program_description", "" + overView)
        multiPartBuilder.addMultipartParameter("program_diet_plan", dietListInComa)
        multiPartBuilder.addMultipartParameter("workout_list", "" + workoutPLanListArray.toString())


        //When user is Admin then include this
        if (isAdmin.equals("Yes", true)) {
            multiPartBuilder.addMultipartParameter("program_access_level", "" + accessLevelValue)
            multiPartBuilder.addMultipartParameter(
                "program_amount",
                "" + binding.txtAmount.text.toString()
            )
            multiPartBuilder.addMultipartParameter(
                "display_in_news_fedd",
                "" + displayNewsfeedValue
            )

            multiPartBuilder.addMultipartParameter(
                "show_in_app",
                "" + showInApp
            )
            multiPartBuilder.addMultipartParameter(
                "allow_notification",
                "" + allowNotificationvalue
            )
            multiPartBuilder.addMultipartParameter(
                "allow_user",
                allowed_user_id.replace("|", ",")
            )
            multiPartBuilder.addMultipartParameter("added_by_type", "Admin")
        }


        multiPartBuilder.addHeaders(
            StringConstant.authToken,
            getDataManager().getUserInfo().customer_auth_token
        )
        multiPartBuilder.addHeaders(StringConstant.apiKey, "HBDEV")
        multiPartBuilder.addHeaders(StringConstant.apiVersion, "1")


        multiPartBuilder.build().getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                binding.loader.visibility = View.GONE

                val jsonObject: JSONObject? =
                    response!!.getJSONObject(StringConstant.settings)
                val success: String? = jsonObject?.getString(StringConstant.success)
                val message: String? = jsonObject?.getString(StringConstant.message)
                if (success.equals("1")) {
                    binding.loader.visibility = View.GONE
                    fromApiCall = true

                    val intent = Intent(this@CreateWorkOutPlanActivty, HomeTabActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.putExtra("WhenPlanCreate", "WhenPlanCreate")
                    startActivity(intent)
                    finish()
                }else{
                    if (message != null) {
                        ErrorDialog.newInstance("", "Ok", message)
                            .show(supportFragmentManager)
                    }
                }
            }

            override fun onError(anError: ANError?) {
                Constant.showCustomToast(
                    this@CreateWorkOutPlanActivty,
                    getString(R.string.something_wrong)
                )
                if (anError != null) {
                    Constant.errorHandle(anError, this@CreateWorkOutPlanActivty)
                }
            }
        })
    }

    private fun lastClick() {

        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return
        } else {
            mLastClickTime = SystemClock.elapsedRealtime()
        }
    }

    private fun isValidData(): Boolean {
        var isValid = false

//        if (binding.workoutName.text.toString().isEmpty()) {
//            ErrorDialog.newInstance("", "Ok", "Please enter workout plan name")
//                .show(supportFragmentManager)
//            isValid = false
//        } else
//            if (userImageFile == null) {
//            ErrorDialog.newInstance("", "Ok", "Please select image for plan")
//                .show(supportFragmentManager)
//            isValid = false
//        } else
            if (binding.levelName.text.toString().isEmpty()) {
            ErrorDialog.newInstance("", "Ok", "Please select level")
                .show(supportFragmentManager)
            isValid = false
        } else if (binding.goodFor.text.toString().isEmpty()) {
            ErrorDialog.newInstance("", "Ok", "Please select workout good for")
                .show(supportFragmentManager)
            isValid = false
        } else if (binding.tvEquipment.text.toString().isEmpty()) {
            ErrorDialog.newInstance("", "Ok", "Please select workout equipments")
                .show(supportFragmentManager)
            isValid = false
        } else {
            if (isAdmin.equals("Yes", true)) {
                accessLevelStr = binding.accessLevel.text.toString()
                displayNewsfeedstr = binding.displayNewsfeed.text.toString()
                allowNotificationstr = binding.allowNotification.text.toString()
                if (accessLevelStr.isEmpty()) {
                    ErrorDialog.newInstance("", "Ok", "Please select access level")
                        .show(supportFragmentManager)
                    isValid = false
                } else if (accessLevelValue.equals("Paid") || accessLevelValue.equals("Exclusive")) {
                    val amount = binding.txtAmount.text.toString()
                    if (amount.isEmpty()) {
                        ErrorDialog.newInstance("", "Ok", "Please select amount")
                            .show(supportFragmentManager)
                        isValid = false
                    } else if (displayNewsfeedstr.isEmpty()) {
                        FinishActivityDialog.newInstance(
                            "",
                            "Ok",
                            "Please select display in Newsfeed"
                        )
                            .show(supportFragmentManager)
                        isValid = false
                    } else if (allowNotificationstr.isEmpty()) {
                        FinishActivityDialog.newInstance(
                            "",
                            "Ok",
                            "Please select allow Notification"
                        )
                            .show(supportFragmentManager)
                        isValid = false
                    } else {
                        isValid = true
                    }

                } else if (displayNewsfeedstr.isEmpty()) {
                    ErrorDialog.newInstance("", "Ok", "Please select display in Newsfeed")
                        .show(supportFragmentManager)
                    isValid = false
                } else if (allowNotificationstr.isEmpty()) {
                    ErrorDialog.newInstance("", "Ok", "Please select allow Notification")
                        .show(supportFragmentManager)
                    isValid = false
                } else {
                    isValid = true
                }
            } else {
                accessLevelStr = ""
                displayNewsfeedstr = ""
                allowNotificationstr = ""
                isValid = true
            }
        }
        return isValid
    }

    override fun levelOnClick(index: Int, str: String) {
        if (value.equals("level")) {
            val builder = SpannableStringBuilder()
            // var str1 = "Level \n"
            // builder.append(str1)
            val spanStr = SpannableString(str)
            spanStr.setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        getActivity(),
                        R.color.created_by_color
                    )
                ),
                0,
                spanStr.length,
                0
            )
            builder.append(spanStr)
            var ss = SpannableString(builder)
            binding.levelName.text = ss

            if (!ss.isEmpty()) {
                binding.goodLevel.visibility = View.VISIBLE
            } else {
                binding.goodLevel.visibility = View.GONE
            }

            value = ""
        }

        if (value.equals("access")) {
            accessLevelValue = str
            var displayValue = getPickerValue("Access Level \n", str)
            binding.accessLevel.text = displayValue
            value = ""

            //for without select Acsess level {free} it is not editable
            if (str.equals("Paid") || str.equals("Exclusive")) {
                binding.txtAmount.isEnabled = true
                binding.dpAmountLayout.visibility=View.VISIBLE
                binding.dpAmountLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorBlack
                    )
                )
            } else {
                binding.txtAmount.isEnabled = false
                binding.dpAmountLayout.visibility=View.GONE
                binding.dpAmountLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorBlack
                    )
                )
            }
        }
        if (value.equals("news")) {
            displayNewsfeedValue = str
            val builder = SpannableStringBuilder()
            var str1 = "Display in Newsfeed? \n"
            builder.append(str1)
            val spanStr = SpannableString(str)
            spanStr.setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        getActivity(),
                        R.color.created_by_color
                    )
                ),
                0,
                spanStr.length,
                0
            )
            builder.append(spanStr)
            var ss = SpannableString(builder)
            binding.displayNewsfeed.text = ss
            value = ""
        }
        if (value.equals("name")) {
            showInApp = str
            val builder = SpannableStringBuilder()
            var str1 = "Show Name In App? \n"
            builder.append(str1)
            val spanStr = SpannableString(str)
            spanStr.setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        getActivity(),
                        R.color.created_by_color
                    )
                ),
                0,
                spanStr.length,
                0
            )
            builder.append(spanStr)
            var ss = SpannableString(builder)
            binding.appShow.text = ss
            value = ""
        }
        if (value.equals("notification")) {
            allowNotificationvalue = str
            val builder = SpannableStringBuilder()
            var str1 = "Allow Notification \n"
            builder.append(str1)
            val spanStr = SpannableString(str)
            spanStr.setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        getActivity(),
                        R.color.created_by_color
                    )
                ),
                0,
                spanStr.length,
                0
            )
            builder.append(spanStr)
            var ss = SpannableString(builder)
            binding.allowNotification.text = ss
            value = ""
        }
    }

    fun getPickerValue(title: String, level: String): SpannableString {
        val builder = SpannableStringBuilder()
        builder.append(title)
        val spanStr = SpannableString(level)
        spanStr.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.created_by_color)),
            0,
            spanStr.length,
            0
        )
        builder.append(spanStr)
        var ss = SpannableString(builder)
        return ss
    }


    private fun handleCropResult(@NonNull result: Intent) {
        val resultUri = UCrop.getOutput(result)
        if (resultUri != null) {
            Picasso.with(getActivity()).load(resultUri).into(binding.image)
            binding.image1.visibility = View.GONE
            binding.imgTxt.visibility = View.GONE
            userImageFile = File(resultUri.path)
        } else {
            Toast.makeText(getActivity(), "cannot_retrieve_cropped_image", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.GALLERY && data != null) {
            if (resultCode == Activity.RESULT_OK) {
                tmpUri = data.data!!
                val selectedImage = data.data
                var time = System.currentTimeMillis();
                var str = time.toString()
                var destinatiomPath = str + "dovies.jpg"
                val options = UCrop.Options()
                options.setHideBottomControls(true)
                UCrop.of(tmpUri, Uri.fromFile(File(cacheDir, destinatiomPath)))
                    .withAspectRatio(3f, 4f)
                    .withOptions(options)
                    .start(getActivity());
                // CommanUtils.beginCrop(selectedImage, getActivity())
            } else {

            }
        } else if (requestCode == UCrop.REQUEST_CROP) {
            if (data != null) {
                handleCropResult(data)
            }
        }
        /* else if (requestCode == Crop.REQUEST_CROP) {
             if (data != null) {
                 userImageFile = CommanUtils.handleCropimage(resultCode, data!!, getActivity())
                 Picasso.with(getActivity()).load(userImageFile).into(binding.image)
                 binding.image1.visibility = View.GONE
                 binding.imgTxt.visibility = View.GONE
             }
         } */
        else if (requestCode == Constant.CAMERA && resultCode == Activity.RESULT_OK) {
            val imageFile = getTemporalFile(this)
            val photoURI = Uri.fromFile(imageFile)
            var bm = Constant.getImageResized(this, photoURI) ///Image resizer
            val rotation = ImageRotator.getRotation(this, photoURI, true)
            bm = ImageRotator.rotate(bm, rotation)
            val profileImagefile =
                File(this.externalCacheDir, UUID.randomUUID().toString() + ".jpg")
            tmpUri = FileProvider.getUriForFile(
                this,
                this.applicationContext.packageName + ".fileprovider",
                profileImagefile
            )
            var time = System.currentTimeMillis();
            var str = time.toString()
            var destinatiomPath = str + "dovies.jpg"
            val options = UCrop.Options()
            options.setHideBottomControls(true)
            UCrop.of(photoURI, Uri.fromFile(File(cacheDir, destinatiomPath)))
                .withAspectRatio(1f, 1f)
                .withOptions(options)
                .start(getActivity());
            //   val imageUri = CommanUtils.getPickImageResultUri(data, getActivity())
            //  CommanUtils.beginCrop(photoURI!!, getActivity())

        } else if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            var tempList =
                data!!.getSerializableExtra("list") as java.util.ArrayList<FilterExerciseResponse.Data.X>
            val builder = SpannableStringBuilder()
            // var str1 = "Good For \n"
            //builder.append(str1)
            var str = ""
            goodForIdStr = ""
            if (tempList != null && tempList.size > 0) {
                for (i in 0..exerciseList.size - 1) {
                    if (exerciseList.get(i).group_name.equals(tempList.get(0).group_name)) {

                        for (j in 0..exerciseList.get(i).list.size - 1) {
                            exerciseList.get(i).list.get(j).isCheck = false
                        }

                        for (j in 0..exerciseList.get(i).list.size - 1) {
                            for (k in 0..tempList.size - 1) {
                                if (tempList.get(k).id.equals(exerciseList.get(i).list.get(j).id)) {
                                    exerciseList.get(i).list.get(j).isCheck = true
                                    // str = str + exerciseList.get(i).list.get(j).display_name + " | "
                                    str = str + tempList.get(k).display_name + " | "
                                    goodForIdStr = goodForIdStr + tempList.get(k).id + ","
                                }
                            }
                        }
                    }
                }
                if (!str.isEmpty()) {
                    str = str.substring(0, str.length - 2);
                    goodForIdStr = goodForIdStr.substring(0, goodForIdStr.length - 1);
                }
                val spanStr = SpannableString(str)
                spanStr.setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(
                            getActivity(),
                            R.color.created_by_color
                        )
                    ),
                    0,
                    spanStr.length,
                    0
                )
                builder.append(spanStr)
                var ss = SpannableString(builder)
                binding.goodFor.text = ss

                if (!ss.isEmpty()) {
                    binding.txtGoodFor.visibility = View.VISIBLE
                } else {
                    binding.txtGoodFor.visibility = View.GONE
                }

            } else {
                binding.txtGoodFor.visibility = View.GONE
                binding.goodFor.text = ""
                binding.goodFor.hint = (Html.fromHtml(getString(R.string.good_for_str)));
                for (j in 0..exerciseList.get(4).list.size - 1) {
                    exerciseList.get(4).list.get(j).isCheck = false
                }
            }
        } else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            var tempList =
                data!!.getSerializableExtra("list") as java.util.ArrayList<FilterExerciseResponse.Data.X>
            val builder = SpannableStringBuilder()
            //var str1 = "Equipments \n"
            // builder.append(str1)
            var str = ""
            equipmentIdStr = ""
            if (tempList != null && tempList.size > 0) {

                for (i in 0..exerciseList.size - 1) {
                    if (exerciseList.get(i).group_name.equals(tempList.get(0).group_name)) {

                        for (j in 0..exerciseList.get(i).list.size - 1) {
                            exerciseList.get(i).list.get(j).isCheck = false
                        }

                        for (j in 0..exerciseList.get(i).list.size - 1) {
                            for (k in 0..tempList.size - 1) {
                                if (tempList.get(k).id.equals(exerciseList.get(i).list.get(j).id)) {
                                    exerciseList.get(i).list.get(j).isCheck = true
                                    equipmentIdStr = equipmentIdStr + tempList.get(k).id + ","
                                    str = str + exerciseList.get(i).list.get(j).display_name + " | "
                                }
                            }
                        }
                    }
                }
                if (!str.isEmpty()) {
                    str = str.substring(0, str.length - 2);
                    equipmentIdStr = equipmentIdStr.substring(0, equipmentIdStr.length - 1);
                }
                val spanStr = SpannableString(str)
                spanStr.setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(
                            getActivity(),
                            R.color.created_by_color
                        )
                    ),
                    0,
                    spanStr.length,
                    0
                )
                builder.append(spanStr)
                var ss = SpannableString(builder)
                binding.tvEquipment.text = ss

                if (!ss.isEmpty()) {
                    binding.equipmemnts.visibility = View.VISIBLE
                } else {
                    binding.equipmemnts.visibility = View.GONE
                }

            } else {
                binding.equipmemnts.visibility = View.GONE
                binding.tvEquipment.text = ""
                binding.tvEquipment.hint = (Html.fromHtml(getString(R.string.equipments_str)));
                for (j in 0..exerciseList.get(1).list.size - 1) {
                    exerciseList.get(1).list.get(j).isCheck = false
                }

            }
        } else if (requestCode == 3 && resultCode == Activity.RESULT_OK) {
            tempAllowedUserList.clear()
            tempAllowedUserList =
                data!!.getSerializableExtra("list") as java.util.ArrayList<CustomerListModel.Data.User>
            val builder = SpannableStringBuilder()
            //var str1 = "Allowed Users \n"
            //builder.append(str1)
            var str = ""
            var str_id = ""
            if (tempAllowedUserList != null && tempAllowedUserList.size > 0) {
                for (i in 0..tempAllowedUserList.size - 1)
                    str = str + tempAllowedUserList.get(i).vName + " | "
            }

            if (tempAllowedUserList != null && tempAllowedUserList.size > 0) {
                for (j in 0..tempAllowedUserList.size - 1)
                    str_id = str_id + tempAllowedUserList.get(j).iCustomerId + " | "
            }

            if (!str.isEmpty()) {
                str = str.substring(0, str.length - 2);
                val spanStr = SpannableString(str)
                spanStr.setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(
                            getActivity(),
                            R.color.created_by_color
                        )
                    ),
                    0,
                    spanStr.length,
                    0
                )
                builder.append(spanStr)
                var ss = SpannableString(builder)
                binding.allowedUsersTxt.text = ss
                allowed_user_id = str_id

                if (!ss.isEmpty()) {
                    binding.txtAllowed.visibility = View.VISIBLE
                } else {
                    binding.txtAllowed.visibility = View.GONE
                }
            } else {
                binding.txtAllowed.visibility = View.GONE
                binding.allowedUsersTxt.text = ""
                allowed_user_id = ""
            }
        }
    }


    override fun onBackPressed() {
        if (isAnyValue() == true) {
            finish()
        } else if (fromApiCall == true) {
            finish()
        } else {
            FinishActivityDialog.newInstance(
                "Yes",
                "No",
                "All changes done to the workout will be lost. Are you sure you want to cancel?"
            ).show(supportFragmentManager)
        }
    }

    // get data from dietPLan To show in DietPLan List
    interface AddDietPlanListener {
        fun getDitPlanData(dietPlanSubCatgory: DietPlanSubCategoryModal.Data.DietListing)
    }

/*
*
* getData from All Fragments here using methods all particulaer all fragments
*
* */

    public fun fragmentone_workoutPlan(completeList: ArrayList<WorkOutListModal.Data>) {
        getJsonArrayOfWeekPlan(completeList)
    }

    public fun fragmenttwo_dietPlan(addDietPlanList: ArrayList<DietPlanSubCategoryModal.Data.DietListing>) {
        getDietPlanListInComa(addDietPlanList)
    }

    private fun getDietPlanListInComa(addDietPlanList: ArrayList<DietPlanSubCategoryModal.Data.DietListing>) {

        if (addDietPlanList != null && addDietPlanList.size != 0) {
            val tempTypesidList = CommanUtils.removeDuplicates(addDietPlanList)
            var saperateByComa: String = ""
            for (i in tempTypesidList.indices) {
                tempTypesidList.get(i).diet_plan_id
                saperateByComa = saperateByComa + tempTypesidList.get(i).diet_plan_id + " , "
            }

            dietListInComa = saperateByComa
        }
    }

    public fun fragmentthree_overview(txt_overView: String) {
        if (!txt_overView.isEmpty()) {
            overView = txt_overView.trim()
            Log.v("log_overview", "" + txt_overView)
        }
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
        fun getDietPlanData(overView: String)
    }

    private fun getIds(workout_equipment_ids: String, pos: Int): String {
        var idStr = ""
        val tempList = workout_equipment_ids.split(",")
        for (k in 0..tempList.size - 1) {
            for (i in 0..exerciseList.get(pos).list.size - 1) {
                if (tempList.get(k).equals(exerciseList.get(pos).list.get(i).id)) {
                    exerciseList.get(pos).list.get(i).isCheck = true
                    idStr = idStr + tempList.get(k) + ","
                }
            }
        }
        return idStr
    }

    override fun workPlanEditAndCreaOnClick(type: String) {
        lastClick()
        if (type.equals("FORNEWPLAN")) {

            //when user save as new workout plan then program id will be ""
            //addCompletePlan("")
            addANdEditWorkout(program_id)
            /* val intent = Intent(this, CreateWorkOutPlanActivty::class.java)
             startActivity(intent)*/
        } else if (type.equals("FOROVERWRITE")) {
            if (program_id != null && !program_id.isEmpty()) {

                if (isValidData()) {
                    //addCompletePlan(program_id)
                    addANdEditWorkout(program_id)
                }
            }
        }
    }
}