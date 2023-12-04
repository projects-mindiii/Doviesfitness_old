package com.doviesfitness.data.local

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.doviesfitness.data.model.UserInfoBean
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.FilterExerciseResponse
import com.doviesfitness.ui.splash.IntroSliderActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.ArrayList
import java.util.HashMap

class AppPreferencesHelper(context: Context) : PreferencesHelper {

    private var mPrefs: SharedPreferences
    private var mRemPrefs: SharedPreferences
    //Remember me
    private val PREF_KEY_APP_REM_EMAIL = "PREF_KEY_APP_REM_EMAIL"
    private val PREF_KEY_APP_REM_PWD = "PREF_KEY_APP_REM_PWD"
    private val PREF_KEY_AUTH_TOKEN = "PREF_KEY_AUTH_TOKEN"


    //User info
    private val PREF_KEY_APP_CUSTOMER_ID = "PREF_KEY_APP_CUSTOMER_ID"
    private val PREF_KEY_APP_CUSTOMER_PROFILE_IMAGE = "PREF_KEY_APP_CUSTOMER_PROFILE_IMAGE"
    private val PREF_KEY_APP_CUSTOMER_AUTH_TOKEN   = "PREF_KEY_APP_CUSTOMER_AUTH_TOKEN"
    private val PREF_KEY_APP_CUSTOMER_EMAIL_VARIFIED   = "PREF_KEY_APP_CUSTOMER_EMAIL_VARIFIED"
    private val PREF_KEY_APP_CUSTOMER_STATUS   = "PREF_KEY_APP_CUSTOMER_STATUS"
    private val PREF_KEY_APP_CUSTOMER_NOTIFICATION   = "PREF_KEY_APP_CUSTOMER_NOTIFICATION"
    private val PREF_KEY_APP_CUSTOMER_FULL_NAME   = "PREF_KEY_APP_CUSTOMER_FULL_NAME"
    private val PREF_KEY_APP_CUSTOMER_WEIGHT   = "PREF_KEY_APP_CUSTOMER_WEIGHT"
    private val PREF_KEY_APP_CUSTOMER_HEIGHT   = "PREF_KEY_APP_CUSTOMER_HEIGHT"
    private val PREF_KEY_APP_CUSTOMER_MOBILE_NUMBER   = "PREF_KEY_APP_CUSTOMER_MOBILE_NUMBER"
    private val PREF_KEY_APP_CUSTOMER_GENDER   = "PREF_KEY_APP_CUSTOMER_GENDER"
    private val PREF_KEY_APP_CUSTOMER_EMAIL   = "PREF_KEY_APP_CUSTOMER_EMAIL"
    private val PREF_KEY_APP_CUSTOMER_SOCIAL_NETWORK   = "PREF_KEY_APP_CUSTOMER_SOCIAL_NETWORK"
    private val PREF_KEY_APP_CUSTOMER_SOCIAL_NETWORK_ID   = "PREF_KEY_APP_CUSTOMER_SOCIAL_NETWORK_ID"
    private val PREF_KEY_APP_CUSTOMER_NOTIFY_REMAINDER = "PREF_KEY_APP_CUSTOMER_NOTIFY_REMAINDER"
    private val PREF_KEY_APP_CUSTOMER_USER_NAME = "PREF_KEY_APP_CUSTOMER_USER_NAME"
    private val PREF_KEY_APP_CUSTOMER_UNITS = "PREF_KEY_APP_CUSTOMER_UNITS"
    private val PREF_KEY_APP_CUSTOMER_COUNTRY_ID = "PREF_KEY_APP_CUSTOMER_COUNTRY_ID"
    private val PREF_KEY_APP_CUSTOMER_ISD_CODE = "PREF_KEY_APP_CUSTOMER_ISD_CODE"
    private val PREF_KEY_APP_CUSTOMER_DOB = "PREF_KEY_APP_CUSTOMER_DOB"
    private val PREF_KEY_APP_CUSTOMER_TITLE = "PREF_KEY_APP_CUSTOMER_TITLE"
    private val PREF_KEY_FILTER_EXERCISE_LIST = "PREF_KEY_FILTER_EXERCISE_LIST"
    private val COMPLETE_WORKOUT_PLAN_STATUS = "COMPLETE_WORKOUT_PLAN_STATUS"
    companion object {
        private var instance: AppPreferencesHelper? = null
        private val APP_PREFERENCE = "AppPreference"
         val PREF_KEY_REPS_FINISH_TIME = "PREF_KEY_REPS_FINISH_TIME"
         val PREF_KEY_EXERCISE_TIME = "PREF_KEY_EXERCISE_TIME"
         val PREF_KEY_EXERCISE_REST_TIME = "PREF_KEY_EXERCISE_REST_TIME"
         val PREF_KEY_NUMBER_OF_REPS = "PREF_KEY_NUMBER_OF_REPS"
         val PREF_KEY_APP_IS_ADMIN = "PREF_KEY_APP_IS_ADMIN"
         val PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED = "PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED"
         val PREF_KEY_APP_SUBSCRIPTION_TITLE = "PREF_KEY_APP_SUBSCRIPTION_TITLE"
         val PREF_KEY_SUBSCRIBED_USER_ID = "PREF_KEY_SUBSCRIBED_USER_ID"
         val PACKAGE_MASTER_ID = "PACKAGE_MASTER_ID"
        private val PREF_KEY_USER_LOGGED_IN_MODE = "PREF_KEY_USER_LOGGED_IN_MODE"
         val COMPLETE_WORKOUT_PLAN_STATUS = "COMPLETE_WORKOUT_PLAN_STATUS"
        private val APP_REM_PREFERENCE = "AppRemPreference"
         val PREF_FIREBASE_TOKEN = "PREF_FIREBASE_TOKEN"
         val PREF_UNIT_VALUE = "PREF_UNIT_VALUE"
         val PREF_REMINDER_TIME = "PREF_REMINDER_TIME"
         val PREF_IS_NOTIFICATION = "PREF_IS_NOTIFICATION"
         val PREF_KEY_APP_PREVIEW_STATUS = "PREF_IS_PREVIEW"
         val PREF_KEY_APP_CUSTOMER_COUNTRY_NAME = "PREF_KEY_APP_CUSTOMER_COUNTRY_NAME"
         val PREF_KEY_APP_CUSTOMER_NOTIFICATION_STATUS = "PREF_KEY_APP_CUSTOMER_NOTIFICATION_STATUS"
         val PREF_KEY_APP_CUSTOMER_NOTIFICATION_COUNT = "PREF_KEY_APP_CUSTOMER_NOTIFICATION_COUNT"
         val PREF_KEY_APP_CUSTOMER_USER_TYPE = "PREF_KEY_APP_CUSTOMER_USER_TYPE"
         val STEAM_WORKOUT_ID = "STEAM_WORKOUT_ID"
         val IS_RESUME = "IS_RESUME"

        val PREF_KEY_EXERCISE_TIME_SETANDREPS = "PREF_KEY_EXERCISE_TIME_SETANDREPS"
        val PREF_KEY_EXERCISE_REST_TIME_SETANDREPS = "PREF_KEY_EXERCISE_REST_TIME_SETANDREPS"
        val PREF_KEY_NUMBER_OF_SETS_SETANDREPS = "PREF_KEY_NUMBER_OF_SETS_SETANDREPS"


        fun getAppPreferencesHelper(context: Context): AppPreferencesHelper {
            instance = AppPreferencesHelper(context)
            return instance as AppPreferencesHelper
        }
    }

    override fun setInfo(info: String) {
        mPrefs.edit().putString(PREF_KEY_USER_LOGGED_IN_MODE, info).apply()
    }

    init {
        mPrefs = context.getSharedPreferences(APP_PREFERENCE, Context.MODE_PRIVATE)
        mRemPrefs = context.getSharedPreferences(APP_REM_PREFERENCE, Context.MODE_PRIVATE)
    }

    override fun statusUpdateOfWorkout(): Boolean {
        return  mPrefs.getBoolean(COMPLETE_WORKOUT_PLAN_STATUS,false)
    }


    override fun isLoggedIn(): Boolean? {
        return  mPrefs.getBoolean(PREF_KEY_USER_LOGGED_IN_MODE,false)
    }

   override fun getStringData(Key:String): String? {
        return  mRemPrefs.getString(Key, "")
    }

    override fun setStringData(Key:String,info: String) {
        mRemPrefs.edit().putString(Key, info).apply()
    }
  override fun getUserStringData(Key:String): String? {
        return  mPrefs.getString(Key, "")
    }

    override fun setUserStringData(Key:String,info: String) {
        mPrefs.edit().putString(Key, info).apply()
    }

    override fun setRememberMe(email: String, pwd: String) {
        mRemPrefs.edit().putString(PREF_KEY_APP_REM_EMAIL, email).apply()
        mRemPrefs.edit().putString(PREF_KEY_APP_REM_PWD, pwd).apply()
    }

    override fun getRememberMe(): Array<String?> {
        val cre = arrayOfNulls<String>(2)
        cre[0] = mRemPrefs.getString(PREF_KEY_APP_REM_EMAIL, "")
        cre[1] = mRemPrefs.getString(PREF_KEY_APP_REM_PWD, "")
        return cre
    }

    override fun getHeader(): HashMap<String, String> {
        val param: HashMap<String, String> = HashMap()
        param.put("authToken", mPrefs.getString(PREF_KEY_AUTH_TOKEN, "")!!)
        return param
    }


    override fun getUserInfo(): UserInfoBean {

        val userInfo = UserInfoBean(
            mPrefs.getString(PREF_KEY_APP_IS_ADMIN, "")!!,
            mPrefs.getString(PREF_KEY_APP_CUSTOMER_ID, "")!!,
            mPrefs.getString(PREF_KEY_APP_CUSTOMER_PROFILE_IMAGE, "")!!,
            mPrefs.getString(PREF_KEY_APP_CUSTOMER_AUTH_TOKEN, "")!!,
            mPrefs.getString(PREF_KEY_APP_CUSTOMER_EMAIL_VARIFIED, "")!!,
            mPrefs.getString(PREF_KEY_APP_CUSTOMER_STATUS, "")!!,
            mPrefs.getString(PREF_KEY_APP_CUSTOMER_NOTIFICATION, "")!!,
            mPrefs.getString(PREF_KEY_APP_CUSTOMER_FULL_NAME, "")!!,
            mPrefs.getString(PREF_KEY_APP_CUSTOMER_WEIGHT, "")!!,
            mPrefs.getString(PREF_KEY_APP_CUSTOMER_HEIGHT, "")!!,
            mPrefs.getString(PREF_KEY_APP_CUSTOMER_MOBILE_NUMBER, "")!!,
            mPrefs.getString(PREF_KEY_APP_CUSTOMER_GENDER, "")!!,
            mPrefs.getString(PREF_KEY_APP_CUSTOMER_EMAIL, "")!!,
            mPrefs.getString(PREF_KEY_APP_CUSTOMER_SOCIAL_NETWORK, "")!!,
            mPrefs.getString(PREF_KEY_APP_CUSTOMER_SOCIAL_NETWORK_ID, "")!!,
            mPrefs.getString(PREF_KEY_APP_CUSTOMER_NOTIFY_REMAINDER, "")!!,
            mPrefs.getString(PREF_KEY_APP_CUSTOMER_USER_NAME, "")!!,
            mPrefs.getString(PREF_KEY_APP_CUSTOMER_UNITS, "")!!,
            mPrefs.getString(PREF_KEY_APP_CUSTOMER_COUNTRY_ID, "")!!,
            mPrefs.getString(PREF_KEY_APP_CUSTOMER_COUNTRY_NAME, "")!!,
            mPrefs.getString(PREF_KEY_APP_CUSTOMER_ISD_CODE, "")!!,
            mPrefs.getString(PREF_KEY_APP_CUSTOMER_NOTIFICATION_STATUS, "")!!,
            mPrefs.getString(PREF_KEY_APP_CUSTOMER_DOB, "")!!,
            mPrefs.getString(PREF_KEY_APP_CUSTOMER_TITLE, "")!!,
            mPrefs.getString(PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED, "")!!,
            mPrefs.getString(PREF_FIREBASE_TOKEN, "")!!)


        return userInfo
    }

    override fun setUserInfo(userInfo: UserInfoBean) {
        mPrefs.edit().putString(PREF_KEY_APP_IS_ADMIN, userInfo.is_admin).apply()
        mPrefs.edit().putString(PREF_KEY_APP_CUSTOMER_ID, userInfo.customer_id).apply()
        mPrefs.edit().putString(PREF_KEY_APP_CUSTOMER_PROFILE_IMAGE, userInfo.customer_profile_image).apply()
        mPrefs.edit().putString(PREF_KEY_APP_CUSTOMER_AUTH_TOKEN, userInfo.customer_auth_token).apply()
        mPrefs.edit().putString(PREF_KEY_APP_CUSTOMER_EMAIL_VARIFIED, userInfo.customer_email_verified).apply()
        mPrefs.edit().putString(PREF_KEY_APP_CUSTOMER_STATUS    , userInfo.customer_status).apply()
        mPrefs.edit().putString(PREF_KEY_APP_CUSTOMER_NOTIFICATION, userInfo.customer_notification).apply()
        mPrefs.edit().putString(PREF_KEY_APP_CUSTOMER_FULL_NAME, userInfo.customer_full_name).apply()
        mPrefs.edit().putString(PREF_KEY_APP_CUSTOMER_WEIGHT, userInfo.customer_weight).apply()
        mPrefs.edit().putString(PREF_KEY_APP_CUSTOMER_HEIGHT, userInfo.customer_height).apply()
        mPrefs.edit().putString(PREF_KEY_APP_CUSTOMER_MOBILE_NUMBER, userInfo.customer_mobile_number).apply()
        mPrefs.edit().putString(PREF_KEY_APP_CUSTOMER_GENDER, userInfo.customer_gender).apply()
        mPrefs.edit().putString(PREF_KEY_APP_CUSTOMER_EMAIL, userInfo.customer_email).apply()
        mPrefs.edit().putString(PREF_KEY_APP_CUSTOMER_SOCIAL_NETWORK, userInfo.customer_social_network).apply()
        mPrefs.edit().putString(PREF_KEY_APP_CUSTOMER_SOCIAL_NETWORK_ID, userInfo.customer_social_network_id).apply()
        mPrefs.edit().putString(PREF_KEY_APP_CUSTOMER_NOTIFY_REMAINDER, userInfo.customer_notify_remainder).apply()
        mPrefs.edit().putString(PREF_KEY_APP_CUSTOMER_USER_NAME, userInfo.customer_user_name).apply()
        mPrefs.edit().putString(PREF_KEY_APP_CUSTOMER_UNITS, userInfo.customer_units).apply()
        mPrefs.edit().putString(PREF_KEY_APP_CUSTOMER_COUNTRY_ID, userInfo.customer_country_id).apply()
        mPrefs.edit().putString(PREF_KEY_APP_CUSTOMER_COUNTRY_NAME, userInfo.customer_country_name).apply()
        mPrefs.edit().putString(PREF_KEY_APP_CUSTOMER_ISD_CODE, userInfo.customer_isd_code).apply()
        mPrefs.edit().putString(PREF_KEY_APP_CUSTOMER_NOTIFICATION_STATUS, userInfo.notification_status).apply()
        mPrefs.edit().putString(PREF_KEY_APP_CUSTOMER_DOB, userInfo.dob).apply()
        mPrefs.edit().putString(PREF_KEY_APP_CUSTOMER_TITLE, userInfo.title).apply()
        mPrefs.edit().putString(PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED, userInfo.is_subscribed).apply()
        mPrefs.edit().putBoolean(PREF_KEY_USER_LOGGED_IN_MODE, true).apply()
        mPrefs.edit().putString(PREF_FIREBASE_TOKEN, userInfo.firebaseToken).apply()

    }

    override fun logout(activity: Activity) {
        mPrefs.edit().putBoolean(PREF_KEY_USER_LOGGED_IN_MODE, false).apply()
        mPrefs.edit().clear().apply()
        activity.finishAffinity()
        //val intent = Intent(activity, LoginActivity::class.java)
        val intent = Intent(activity, IntroSliderActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        activity.startActivity(intent)
        activity.finish()
    }
    override fun saveFilterExerciseList(exerciseList: List<FilterExerciseResponse.Data>) {
        try {
            val gson = Gson()
            val category = gson.toJson(exerciseList)
            mPrefs.edit().putString(PREF_KEY_FILTER_EXERCISE_LIST, category).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override  fun getFilterExerciseList(): ArrayList<FilterExerciseResponse.Data>? {
        var exerciseList = ArrayList<FilterExerciseResponse.Data>()
        val gson = Gson()
        val vallueJson =  mPrefs.getString(PREF_KEY_FILTER_EXERCISE_LIST, "")
        exerciseList = gson.fromJson<ArrayList<FilterExerciseResponse.Data>>(vallueJson, object : TypeToken<List<FilterExerciseResponse.Data>>() {}.type)
        return exerciseList
    }


    override fun setPreferanceStatus(Key:String, status: String) {
        mPrefs.edit().putString(Key, status).apply()
    }

    override fun getPreferanceStatus(Key:String): String? {
        return  mPrefs.getString(Key, "")
    }
}
