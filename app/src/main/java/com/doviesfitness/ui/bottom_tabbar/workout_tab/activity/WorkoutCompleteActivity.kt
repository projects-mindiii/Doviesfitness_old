package com.doviesfitness.ui.bottom_tabbar.workout_tab.activity

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.data.remote.Webservice
import com.doviesfitness.databinding.ActivityStreamWorkoutCompleteBinding
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.bottom_tabbar.home_tab.dialog.SaveEditWorkoutDialog
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.WorkoutDetail
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.WorkoutDetailResponce
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.WorkoutExercise
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.SetAndRepsModel
import com.doviesfitness.ui.showDietPlanDetail.weeklyFragments.ShowWeekFourFragment
import com.doviesfitness.ui.showDietPlanDetail.weeklyFragments.ShowWeekOneFragment
import com.doviesfitness.ui.showDietPlanDetail.weeklyFragments.ShowWeekThreeFragment
import com.doviesfitness.ui.showDietPlanDetail.weeklyFragments.ShowWeekTwoFragment
import com.doviesfitness.utils.CommanUtils.Companion.removeDuplicates
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.StringConstant
import org.json.JSONArray
import org.json.JSONObject
import java.io.File


class WorkoutCompleteActivity : BaseActivity(), View.OnClickListener,
    SaveEditWorkoutDialog.CommentCallBack {
    companion object {
        var userImageFile: File? = null
    }

    private var from_which_frament: String = ""
    private var program_plan_id: String? = ""
    lateinit var binding: ActivityStreamWorkoutCompleteBinding
    var workout_id = ""
    lateinit var exerciseList: ArrayList<WorkoutExercise>
    var workout_name = ""
    var duration = ""
    var durationNewPlayer = ""
    lateinit var WDetail: WorkoutDetail
    var orientation: Int = -10
    var isAdmin = "No"
    var buttonClicked = ""
    lateinit var roundList: java.util.ArrayList<SetAndRepsModel>
    var goodFoIds = ""
    var equipmentIdStr = ""
    lateinit var complete_workoutDetail: WorkoutDetailResponce

    override fun onCreate(savedInstanceState: Bundle?) {
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        hideNavigationBar()
        super.onCreate(savedInstanceState)
        hideNavStatusBar()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_stream_workout_complete)
        isAdmin = getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!

        initialisation()
    }

    fun initialisation() {

        WDetail = intent.getSerializableExtra("WDetail") as WorkoutDetail

        if (intent.getStringExtra("duration")!=null)
            durationNewPlayer=intent.getStringExtra("duration")!!

        if (intent.getSerializableExtra("complete_workoutDetail") != null)
            complete_workoutDetail =
                intent.getSerializableExtra("complete_workoutDetail") as WorkoutDetailResponce

        if (intent.hasExtra("from_ProgramPlan")) {
            if (intent.getStringExtra("from_ProgramPlan") != null) {
                program_plan_id = intent.getStringExtra("from_ProgramPlan")!!
            }
        }

        if (intent.hasExtra("from_which_frament")) {
            if (intent.getStringExtra("from_which_frament") != null) {
                from_which_frament = intent.getStringExtra("from_which_frament")!!
            }
        }



        if (intent.getParcelableArrayListExtra<SetAndRepsModel>("RoundsList") != null) {
            roundList = intent.getParcelableArrayListExtra<SetAndRepsModel>("RoundsList")!!
        }



        if (intent.getStringExtra("duration") != null)
            duration = intent.getStringExtra("duration")!!

        workout_id = WDetail.workout_id
        workout_name = WDetail.workout_name
        binding.noBtn.setOnClickListener(this)
        // binding.noBtn1.setOnClickListener(this)
        binding.yesBtn.setOnClickListener(this)
        //  binding.yesBtn1.setOnClickListener(this)
        binding.description.visibility = View.GONE
        binding.noBtn.text = "NO"
        binding.yesBtn.text = "YES"

        MyAsyncTask(this, WDetail).execute()


    }

    class MyAsyncTask(var context: Context, var WDetail: WorkoutDetail) :
        AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void?): Void? {
            try {
                userImageFile =
                    Glide.with(context).asFile().load(WDetail.workout_image).submit().get()

            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            // ...
        }
    }


    fun hideNavStatusBar() {
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    }

    override fun onResume() {
        super.onResume()
        hideNavStatusBar()
    }

    /**need to call this api on end workout and if going back on workout detail */
    private fun editWorkout(exerciseArray: String, saveOrOverrite: String) {


        Log.d("gdjsgdjgdg", "withDuplicateEQP: $equipmentIdStr")
        Log.d("gdjsgdjgdg", "withoutDuplicateEQP: ${removeDuplicates(equipmentIdStr)}")
        Log.d("gdjsgdjgdg", "withDuplicateGDFR: $goodFoIds")
        Log.d("gdjsgdjgdg", "withoutDuplicateGDFR: ${removeDuplicates(goodFoIds)}")


        //   userImageFile = File(cacheDir, "dovies")
        //   userImageFile!!.createNewFile()


        var goodForIds = removeDuplicates(goodFoIds)
        var equipmentId = removeDuplicates(equipmentIdStr)

        val totalWorkoutTime =
            WDetail.workout_total_time//binding.timeExerciseTxt1.text.toString().trim()
        // binding.progressLayout.visibility = View.VISIBLE
        val multiPartBuilder = AndroidNetworking.upload(Webservice.EDIT_WORKOUT)

        if (userImageFile != null) {
            multiPartBuilder.addMultipartFile("workout_image", userImageFile)
        } else {
            multiPartBuilder.addMultipartParameter("workout_image", userImageFile)
        }

        multiPartBuilder.addMultipartParameter(
            StringConstant.device_token, getDataManager().getUserInfo().customer_auth_token
        )
        multiPartBuilder.addMultipartParameter(StringConstant.device_id, "")
        multiPartBuilder.addMultipartParameter(StringConstant.device_type, StringConstant.Android)
        multiPartBuilder.addMultipartParameter(
            StringConstant.auth_customer_id, getDataManager().getUserInfo().customer_auth_token
        )
        multiPartBuilder.addMultipartParameter("workout_equipment", equipmentId)
        multiPartBuilder.addMultipartParameter(
            "workout_description",
            WDetail.workout_description/* binding.overview.text.toString().trim()*/
        )


        if (WDetail.workout_type == "1") {
            multiPartBuilder.addMultipartParameter("groups", exerciseArray)
            multiPartBuilder.addMultipartParameter("workoutType", "1")

        } else {
            multiPartBuilder.addMultipartParameter("exercise", exerciseArray)
            multiPartBuilder.addMultipartParameter("workoutType", "0")
        }

        multiPartBuilder.addMultipartParameter("exercise", exerciseArray)
        multiPartBuilder.addMultipartParameter("totalWorkoutTime", "00:$totalWorkoutTime")
        multiPartBuilder.addMultipartParameter("workout_good_for", goodForIds)
        multiPartBuilder.addMultipartParameter(
            "workout_name", WDetail.workout_name/*binding.workoutName.text.toString().trim()*/
        )
        if (WDetail.workout_level == "All Levels") {
            multiPartBuilder.addMultipartParameter("workout_level", "All")
        } else multiPartBuilder.addMultipartParameter(
            "workout_level", WDetail.workout_level//binding.levelName.text.toString().trim()
        )


        if (isAdmin.equals("Yes", true)) {
            multiPartBuilder.addMultipartParameter(
                "createdById",
                complete_workoutDetail.data.created_by.dg_devios_guest_id/*createById*/
            )
            multiPartBuilder.addMultipartParameter(
                "addedBy",
                complete_workoutDetail.data.created_by.creator_name /*createByStr*/
            )
            multiPartBuilder.addMultipartParameter(
                "addedById",
                complete_workoutDetail.data.created_by.dg_devios_guest_id /*createById*/
            )
            multiPartBuilder.addMultipartParameter(
                "accessLevel",
                WDetail.access_level /*accessLevelValue*/
            )
            multiPartBuilder.addMultipartParameter(
                "allowNotification",
                WDetail.allow_notification /*allowNotificationvalue*/
            )
            multiPartBuilder.addMultipartParameter(
                "allowedUsers", WDetail.allowed_users
            )

        }


        if (saveOrOverrite == "Overrite") {
            multiPartBuilder.addMultipartParameter("workout_id", "" + WDetail.workout_id)
            multiPartBuilder.addMultipartParameter("parent_workout_id", "" + WDetail.workout_id)
        }



        multiPartBuilder.addHeaders(
            StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token
        )
        multiPartBuilder.addHeaders(StringConstant.apiKey, "HBDEV")
        multiPartBuilder.addHeaders(StringConstant.apiVersion, "1")
        multiPartBuilder.build().getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                val jsonObject: JSONObject? = response!!.getJSONObject(StringConstant.settings)
                val success: String? = jsonObject?.getString(StringConstant.success)
                val message: String? = jsonObject?.getString(StringConstant.message)

                if ("1" == success) {
                    if (buttonClicked == "No") {
                        if (!program_plan_id!!.isEmpty()) {
                            updatStatusOfProgramPlan(program_plan_id!!)
                        } else {
                            finish()
                        }
                    } else if (buttonClicked == "Yes") {
                        startActivity(
                            Intent(getActivity(), WorkoutLogActivity::class.java).putExtra(
                                "WDetail",
                                WDetail
                            )
                                .putExtra("duration", duration)
                                .putExtra("from_ProgramPlan", program_plan_id)
                                .putExtra("from_which_frament", from_which_frament)
                        )
                        finish()
                    }


                } else Constant.showCustomToast(getActivity(), "fail...$message")
            }

            override fun onError(anError: ANError?) {
                //  binding.progressLayout.visibility = View.GONE
                /*     Constant.showCustomToast(
                         this@WorkoutCompleteActivity, getString(R.string.something_wrong)
                     )
                     if (anError != null) {
                         Constant.errorHandle(anError, this@WorkoutCompleteActivity)
                     }*/
            }
        })
    }

    /** making json for sending in api for add */
    fun makeJsonArrayOfRoundsAndExercises(): JSONArray {
        equipmentIdStr = ""
        goodFoIds = ""
        val mainJsonArray = JSONArray()


        for (i in 0 until roundList.size) {  // round level loop
            val main = roundList[i]
            var mainJsonObject = JSONObject()
            val setsJsonArray = JSONArray()
            main.arrSets[0].exerciseList.forEach {
                if (equipmentIdStr.isEmpty())
                    equipmentIdStr = it.exercise_equipments
                else
                    equipmentIdStr += ",${it.exercise_equipments}"

                if (goodFoIds.isEmpty())
                    goodFoIds = it.exercise_body_parts
                else
                    goodFoIds += ",${it.exercise_body_parts}"
            }
            for (j in 0 until main.arrSets.size) {  // sets level loop
                val set = main.arrSets[j]
                val setsJsonObject = JSONObject()
                val ExerciseJsonArray = JSONArray()

                for (k in 0 until set.exerciseList.size) {  // exercise level loop


                    val exercise = set.exerciseList[k]
                    var reps = "Select"

                    if (exercise.selected_exercise_reps_number != null && exercise.selected_exercise_reps_number!!.isNotEmpty() && exercise.selected_exercise_reps_number != "0") reps =
                        exercise.selected_exercise_reps_number!!

                    val ExerciseJsonObject = JSONObject()
                    ExerciseJsonObject.put("exercise_id", exercise.exercise_id)
                    ExerciseJsonObject.put("reps", reps)
                    ExerciseJsonObject.put("weight", exercise.selected_exercise_weight_number)
                    ExerciseJsonObject.put("sequence", (k + 1).toString())
                    ExerciseJsonArray.put(ExerciseJsonObject)
                }

                setsJsonObject.put("exercise", ExerciseJsonArray)
                setsJsonObject.put("set", "SET " + (j + 1))
                setsJsonArray.put(setsJsonObject)
            }

            // mainJsonObject.put("group_type", main.strRoundCounts)
            mainJsonObject.put("target_sets", main.strTargetSets)
            mainJsonObject.put("target_reps", main.strTargetReps)
            mainJsonObject.put("sequence_no", (i + 1))
            // Left & Right SetAndReps

            if (main.strRoundName == "Left & Right") mainJsonObject.put("group_type", "2")
            else if (main.strRoundName == "SuperSet") mainJsonObject.put("group_type", "1")
            else mainJsonObject.put("group_type", "0")


            mainJsonObject.put("notes", main.noteForExerciseInRound)
            mainJsonObject.put("sets", setsJsonArray)
            mainJsonArray.put(mainJsonObject)
        }



        return mainJsonArray
    }


    /*
        override fun onConfigurationChanged(newConfig: Configuration) {
            super.onConfigurationChanged(newConfig)
            hideNavStatusBar()
            //  hideNavigationBar()
            orientation = getActivity().getResources().getConfiguration().orientation
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                binding.portraitLayout.visibility = View.VISIBLE
                binding.landscapeLayout.visibility = View.GONE
            } else {
                binding.portraitLayout.visibility = View.GONE
                binding.landscapeLayout.visibility = View.VISIBLE
            }
            //   setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
    */

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.no_btn1 -> {
                if (!program_plan_id!!.isEmpty()) {
                    /*
                                         editWorkout(makeJsonArrayOfRoundsAndExercises().toString())
                    */

                    Log.d("sdfgfds", "onClick: 1")
                    updatStatusOfProgramPlan(program_plan_id!!)
                }
                finish()
            }

            R.id.yes_btn1 -> {
                Log.d("sdfgfds", "onClick: 3")

                startActivity(
                    Intent(getActivity(), WorkoutLogActivity::class.java).putExtra(
                        "WDetail",
                        WDetail
                    )
                        .putExtra("duration", duration)
                        .putExtra("from_ProgramPlan", program_plan_id)
                )
                finish()
            }

            R.id.no_btn -> {
                buttonClicked = "No"
                if (WDetail.workout_type == "1") {// if workout type==1 means set and reps then edit workout api
                    SaveEditWorkoutDialog.newInstance("edit", this).show(supportFragmentManager)
                    // editWorkout(makeJsonArrayOfRoundsAndExercises().toString())
                } else {
                    if (!program_plan_id!!.isEmpty()) {
                        updatStatusOfProgramPlan(program_plan_id!!)
                    } else {
                        finish()
                    }
                }


            }

            R.id.yes_btn -> {
                buttonClicked = "Yes"
                if (WDetail.workout_type == "1") {// if workout type==1 means set and reps then hit edit workout api
                    //   editWorkout(makeJsonArrayOfRoundsAndExercises().toString())
                    SaveEditWorkoutDialog.newInstance("edit", this).show(supportFragmentManager)
                } else {
                    startActivity(
                        Intent(getActivity(), WorkoutLogActivity::class.java).putExtra(
                            "WDetail",
                            WDetail
                        )
                            .putExtra("duration", duration)
                            .putExtra("from_ProgramPlan", program_plan_id)
                            .putExtra("from_which_frament", from_which_frament)
                    )
                    finish()
                }


            }
        }
    }


    private fun updatStatusOfProgramPlan(programPlanId: String) {
        val param = HashMap<String, String>()
        param.put(StringConstant.device_type, StringConstant.Android)
        param.put("program_id", WDetail.workout_id)
        param.put("program_workout_id", programPlanId)
        param.put(
            StringConstant.device_token,
            getDataManager().getUserStringData(AppPreferencesHelper.PREF_FIREBASE_TOKEN)!!
        )
        param.put(StringConstant.device_id, "")
        param.put(StringConstant.device_type, StringConstant.Android)
        param.put(
            StringConstant.auth_customer_id,
            getDataManager().getUserInfo().customer_auth_token
        )


        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        header.put(StringConstant.apiKey, "HBDEV")
        header.put(StringConstant.apiVersion, "1")

        getDataManager().updateProgramPlanStatus(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    try {
                        val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                        val status = json?.get(StringConstant.success)
                        val msg = json?.get(StringConstant.message)
                        if (status!!.equals("1")) {

                            //ChangeStatusCode
                            //manage statically status thats why we manage it bys using session]


                            if (from_which_frament.equals("FromWeekOne")) {
                                ShowWeekOneFragment.forweek = "1"
                            } else if (from_which_frament.equals("FromWeekTwo")) {
                                ShowWeekTwoFragment.forweek = "1"
                            } else if (from_which_frament.equals("FromWeekThree")) {
                                ShowWeekThreeFragment.forweek = "1"
                            } else if (from_which_frament.equals("FromWeekFour")) {
                                ShowWeekFourFragment.forweek = "1"
                            }

                            getDataManager().setUserStringData(
                                AppPreferencesHelper.COMPLETE_WORKOUT_PLAN_STATUS,
                                "Update"
                            )

                            val isUpdateOrNOt =
                                getDataManager().getUserStringData(AppPreferencesHelper.COMPLETE_WORKOUT_PLAN_STATUS)

                            Log.v("isUpdateOrNOt", "" + isUpdateOrNOt)

                            finish()

                        } else {
                            Constant.showCustomToast(this@WorkoutCompleteActivity, "" + msg)
                        }
                    } catch (ex: Exception) {
                        Constant.showCustomToast(
                            this@WorkoutCompleteActivity,
                            getString(R.string.something_wrong)
                        )
                    }
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, this@WorkoutCompleteActivity)
                }
            })
    }

    override fun textOnClick1(type: String) {
        editWorkout(makeJsonArrayOfRoundsAndExercises().toString(), "SaveNew")
    }

    override fun overwriteClick(type: String) {
        editWorkout(makeJsonArrayOfRoundsAndExercises().toString(), "Overrite")

    }

    /* private fun createWorkout(exerciseArray: String) {
         //groups  if set and reps then send array in given groups key
         */
    /** total_time,
     * exercise_count,isFeatured,workoutType,notes *//*
        // goodForIdStr = getGoodForIds(exerciseList[4].list)
        // equipmentIdStr =  getEquipmentsIds(exerciseList[1].list)


        Log.d(
            "params",
            "createWorkout: " + " workout_image-$userImageFile," + "device_token-${getDataManager().getUserInfo().customer_auth_token}" + "device_id-device_id" + "device_type-${StringConstant.Android}" + "auth_customer_id - ${getDataManager().getUserInfo().customer_auth_token}" + "workout_equipment-$equipmentIdStr" + "workout_description-${
                binding.overview.text.toString().trim()
            }" + "groups-$exerciseArray" + "workoutType-0" + "notes-$noteForExercise" + "workout_name-${
                binding.workoutName.text.toString().trim()
            }" + "workout_level-${
                binding.levelName.text.toString().trim()
            }" + "addedBy-$createByStr" + "addedById-$createById" + "accessLevel-$accessLevelValue" + "allowNotification-$allowNotificationvalue" + "allowedUsers-${
                allowed_user_id.replace(
                    " | ", ","
                )
            }" + "authToken-${getDataManager().getUserInfo().customer_auth_token}"
        )

        totalWorkoutTime = "00:${binding.timeExerciseTxt1.text.toString().trim()}"
        binding.progressLayout.visibility = View.VISIBLE

        val multiPartBuilder = AndroidNetworking.upload(Webservice.CREATE_WORKOUT)
        if (userImageFile != null) {
            multiPartBuilder.addMultipartFile("workout_image", userImageFile)
        } else {
            multiPartBuilder.addMultipartParameter("workout_image", userImageFile)
        }
        multiPartBuilder.addMultipartParameter(
            StringConstant.device_token, getDataManager().getUserInfo().customer_auth_token
        )
        multiPartBuilder.addMultipartParameter(StringConstant.device_id, "")
        multiPartBuilder.addMultipartParameter(StringConstant.device_type, StringConstant.Android)
        multiPartBuilder.addMultipartParameter(
            StringConstant.auth_customer_id, getDataManager().getUserInfo().customer_auth_token
        )
        multiPartBuilder.addMultipartParameter("workout_equipment", equipmentIdStr)
        multiPartBuilder.addMultipartParameter(
            "workout_description", binding.overview.text.toString().trim()
        )

        if (exerciseType == "SetAndReps") {
            multiPartBuilder.addMultipartParameter("groups", exerciseArray)
            multiPartBuilder.addMultipartParameter("workoutType", "1")

        } else {
            multiPartBuilder.addMultipartParameter("exercise", exerciseArray)
            multiPartBuilder.addMultipartParameter("workoutType", "0")
        }

        multiPartBuilder.addMultipartParameter("workout_good_for", goodForIdStr)
        multiPartBuilder.addMultipartParameter("notes", noteForExercise)
        multiPartBuilder.addMultipartParameter("totalWorkoutTime", totalWorkoutTime)

        multiPartBuilder.addMultipartParameter(
            "workout_name", binding.workoutName.text.toString().trim()
        )
        if (binding.levelName.text.toString().trim() == "All Levels") {
            multiPartBuilder.addMultipartParameter("workout_level", "All")
        } else multiPartBuilder.addMultipartParameter(
            "workout_level", binding.levelName.text.toString().trim()
        )
        if (isAdmin.equals("Yes", true)) {
            multiPartBuilder.addMultipartParameter("createdById", createById)

            multiPartBuilder.addMultipartParameter("addedBy", createByStr)
            multiPartBuilder.addMultipartParameter("addedById", createById)
            multiPartBuilder.addMultipartParameter("accessLevel", accessLevelValue)
            multiPartBuilder.addMultipartParameter("allowNotification", WDetail.allow_notification)
            multiPartBuilder.addMultipartParameter(
                "allowedUsers", WDetail.allowed_users.replace(" | ", ",")
            )
        }
        multiPartBuilder.addHeaders(
            StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token
        )
        multiPartBuilder.addHeaders(StringConstant.apiKey, "HBDEV")
        multiPartBuilder.addHeaders(StringConstant.apiVersion, "1")
        Log.d("allParams", "createWorkout: $multiPartBuilder")
        multiPartBuilder.build().getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                binding.progressLayout.visibility = View.GONE
                Log.d("sdgsdfgdfgdgdsgdgdsgsdg", "filter response...." + response!!.toString(4))
                val jsonObject: JSONObject? = response.getJSONObject(StringConstant.settings)
                val success: String? = jsonObject?.getString(StringConstant.success)
                val message: String? = jsonObject?.getString(StringConstant.message)
                if ("1".equals(success)) {
                    // Constant.showCustomToast(getActivity(), "success..." + message)
                    if (isEdit.equals("edit", true)) {
                        setResult(12345)
                    }

                    // removeFragment3(HomeTabActivity., R.id.container_id, true)

                    else if (myWorkoutFragment != null && !myWorkoutFragment.isEmpty() && myWorkoutFragment.equals(
                            "myWorkoutFragment"
                        )
                    ) setResult(1234, Intent().putExtra("myFrag", "myFrag"))
                    else {
                        startActivity(
                            Intent(getActivity(), ActivityMyWorkoutList::class.java).setFlags(
                                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                            )
                        )
                    }
                    finish()
                } else Constant.showCustomToast(getActivity(), "fail..." + message)
            }

            override fun onError(anError: ANError?) {
                binding.progressLayout.visibility = View.GONE
                Constant.showCustomToast(
                    this@CreateWorkoutActivity, getString(R.string.something_wrong)
                )
                if (anError != null) {
                    Constant.errorHandle(anError, this@CreateWorkoutActivity)
                }
            }
        })
    }*/

}