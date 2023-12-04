package com.doviesfitness.data

import android.app.Activity
import android.content.Context
import com.androidnetworking.common.ANRequest
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.data.model.SignUpInfo
import com.doviesfitness.data.model.UserInfoBean
import com.doviesfitness.data.remote.AppApiHelper
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.FilterExerciseResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class AppDataManager(context: Context) : DataManager {
    private var instanceApi: AppApiHelper? = null
    private var instancePref: AppPreferencesHelper? = null
    var mGson: Gson? = null

    init {
        instanceApi = AppApiHelper.getAppApiHelper()
        instancePref = AppPreferencesHelper.getAppPreferencesHelper(context)
        mGson = GsonBuilder().create()
    }

    companion object {
        private var instanceApp: AppDataManager? = null

        fun getAppDataManager(context: Context): AppDataManager {
            instanceApp = AppDataManager(context)
            return instanceApp as AppDataManager
        }
    }

    override fun doSignUp(
        param: SignUpInfo,
        profileImage: HashMap<String, File?>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.doSignUp(param, profileImage)
    }


    override fun doLogin(param: HashMap<String, String>): ANRequest<out ANRequest<*>>? {
        return instanceApi?.doLogin(param)
    }


    override fun resetPassword(param: HashMap<String, String>): ANRequest<out ANRequest<*>>? {
        return instanceApi?.resetPassword(param)
    }


    override fun feedListApi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.feedListApi(param, header)
    }
    override fun playlistApi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.playlistApi(param, header)
    }
    override fun playSong(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.playSong(param, header)
    }


    override fun likeUnlikeApi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.likeUnlikeApi(param, header)
    }

    override fun makeFavoriteApi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.makeFavoriteApi(param, header)
    }

    override fun getNewsFeedCommentApi(
        param: HashMap<String, String>, header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.getNewsFeedCommentApi(param, header)
    }

    override fun postNewsFeedCommentApi(
        param: HashMap<String, String>, header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.postNewsFeedCommentApi(param, header)
    }
     fun deleteAccountApi(
        param: HashMap<String, String>, header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.deleteAccountApi(param, header)
    }

    override fun deleteCommentApi(
        param: HashMap<String, String>, header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.deleteCommentApi(param, header)
    }

    override fun reportCommentApi(
        param: HashMap<String, String>, header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.reportCommentApi(param, header)
    }


    override fun programDetailApi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.programDetailApi(param, header)
    }

    override fun updateCountry(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.updateCountry(param, header)
    }


    override fun newsFeedDetailApi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.newsFeedDetailApi(param, header)

    }

    override fun workoutDetailApi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.workoutDetailApi(param, header)
    }

    override fun dietPlanDetailApi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.dietPlanDetailApi(param, header)
    }


    override fun checkUserNameAvailability(param: HashMap<String, String>): ANRequest<out ANRequest<*>>? {
        return instanceApi?.checkUserNameAvailability(param)
    }


    override fun checkEmailAvailability(param: HashMap<String, String>): ANRequest<out ANRequest<*>>? {
        return instanceApi?.checkEmailAvailability(param)
    }


    override fun statusUpdateOfWorkout(): Boolean {
        return instancePref?.statusUpdateOfWorkout()!!
    }


    override fun isLoggedIn(): Boolean {
        return instancePref?.isLoggedIn()!!
    }

    override fun setRememberMe(email: String, pwd: String) {
        return instancePref!!.setRememberMe(email, pwd)
    }

    override fun getRememberMe(): Array<String?> {
        return instancePref!!.getRememberMe()
    }

    override fun getHeader(): java.util.HashMap<String, String> {
        return instancePref!!.getHeader()
    }

    override fun logout(activity: Activity) {
        return instancePref!!.logout(activity)
    }

    override fun setInfo(info: String) {
        instancePref?.setInfo(info)
    }

    override fun getStringData(Key: String): String? {
        return instancePref!!.getStringData(Key)
    }

    override fun setStringData(Key: String, info: String) {
        instancePref?.setStringData(Key, info)
    }



    override fun setPreferanceStatus(Key : String, status: String) {
        instancePref?.setPreferanceStatus(Key, status)
    }

    override fun getPreferanceStatus(Key : String): String? {
        return instancePref!!.getPreferanceStatus(Key)
    }


    override fun getUserStringData(Key: String): String? {
        return instancePref!!.getUserStringData(Key)
    }

    override fun setUserStringData(Key: String, info: String) {
        instancePref?.setUserStringData(Key, info)

    }

    override fun saveFilterExerciseList(list: List<FilterExerciseResponse.Data>) {
        instancePref?.saveFilterExerciseList(list)
    }

    override fun getFilterExerciseList(): ArrayList<FilterExerciseResponse.Data>? {
        return instancePref!!.getFilterExerciseList()
    }

    override fun getUserInfo(): UserInfoBean {
        return instancePref!!.getUserInfo()
    }

    override fun setUserInfo(userInfo: UserInfoBean) {
        instancePref?.setUserInfo(userInfo)
    }

    override fun exerciseLibraryApi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.exerciseLibraryApi(param, header)
    }

    override fun featuredWorkoutApi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.featuredWorkoutApi(param, header)
    }

    override fun filterWorkoutApi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.filterWorkoutApi(param, header)
    }

    override fun exerciseDetailListApi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.exerciseDetailListApi(param, header)
    }

    override fun workoutGroupListApi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.workoutGroupListApi(param, header)
    }

    override fun dietPlanCategoriesListApi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.dietPlanCategoriesListApi(param, header)
    }

    override fun dietPlanApi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.dietPlanApi(param, header)
    }

    override fun dietPlanDetailsApi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.dietPlanDetailsApi(param, header)
    }

    override fun getCustomerDietAPi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.getCustomerDietAPi(param, header)
    }

    override fun addToMyDietAPi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.addToMyDietAPi(param, header)
    }

    override fun deleteMyDietAPi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.deleteMyDietAPi(param, header)
    }


    override fun getUSerDetailAPi(
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.getUSerDetailAPi(header)
    }

    override fun updateUserDetail(
        param: HashMap<String, String>,
        header: HashMap<String, String>,
        profileImage: HashMap<String, File?>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.updateUserDetail(param, header, profileImage)
    }

    override fun deleteWorkout(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.deleteWorkout(param, header)
    }

    override fun deleteLog(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.deleteLog(param, header)
    }
    override fun deleteStreamLog(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.deleteStreamLog(param, header)
    }
    override fun deleteStreamLogHistory(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.deleteStreamLogHistory(param, header)
    }

    override fun getCustomerWorkoutAPi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.getCustomerWorkoutAPi(param, header)
    }

    override fun getCustomerWorkoutLog(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.getCustomerWorkoutLog(param, header)
    }
    override fun getStreamWorkoutLog(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.getStreamWorkoutLog(param, header)
    }
    override fun createPlayedHistory(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.createPlayedHistory(param, header)
    }
    override fun getStreamHistory(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.getStreamHistory(param, header)
    }


    override fun submitLog(
        param: HashMap<String, String>,
        header: HashMap<String, String>,
        FileArray: List<File>?
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.submitLog(param, header, FileArray)
    }
    override fun submitStreamLog(
        param: HashMap<String, String>,
        header: HashMap<String, String>,
        FileArray: List<File>?
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.submitStreamLog(param, header, FileArray)
    }


    override fun updateLog(
        param: HashMap<String, String>,
        header: HashMap<String, String>,
        FileArray: List<File>?
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.updateLog(param, header, FileArray)
    }
    override fun updateStreamLog(
        param: HashMap<String, String>,
        header: HashMap<String, String>,
        FileArray: List<File>?
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.updateStreamLog(param, header, FileArray)
    }
    override fun getCaloriList(
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.getCaloriList( header)
    }

    override fun getWorkoutPlanAPi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.getWorkoutPlanAPi(param, header)
    }

    override fun getWorkoutPlanDetailAPi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.getWorkoutPlanDetailAPi(param, header)
    }

    override fun addToMyPlanApi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.addToMyPlanApi(param, header)
    }

    override fun getCustomPlan(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.getCustomPlan(param, header)
    }


    override fun addToMyPlanFavApi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.addToMyPlanFavApi(param, header)
    }

    override fun deletePlanApi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.deletePlanApi(param, header)
    }

    override fun publishWorkoutApi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.publishWorkoutApi(param, header)
    }

    override fun workoutGetCustomerList(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.workoutGetCustomerList(param, header)
    }

    override fun getCustomerFavourites(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.getCustomerFavourites(param, header)
    }

    override fun createWorkout(
        param: HashMap<String, String>,
        header: HashMap<String, String>,
        imageFile: java.util.HashMap<String, File?>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.createWorkout(param, header, imageFile)
    }

    override fun editWorkout(
        param: HashMap<String, String>,
        header: HashMap<String, String>,
        imageFile: java.util.HashMap<String, File?>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.editWorkout(param, header, imageFile)
    }

    override fun addCompleteProgram(
        param: HashMap<String, String>,
        header: HashMap<String, String>,
        imageFile: java.util.HashMap<String, File?>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.addCompleteProgram(param, header, imageFile)
    }


    override fun planDetail(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.planDetail(param, header)
    }


    override fun activePlanApi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.activePlanApi(param, header)
    }

    override fun getPackageList(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.getPackageList(param, header)
    }


    override fun getNotificationList(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.getNotificationList(param, header)
    }

    override fun getNotificationListNew(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.getNotificationListNew(param, header)
    }

    override fun getNotificationDetail(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.getNotificationDetail(param, header)
    }

    override fun notificationStatusChanged(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.notificationStatusChanged(param, header)
    }

    override fun readPushNotificationCount(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.readPushNotificationCount(param, header)
    }


    override fun customCommentApi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.customCommentApi(param, header)
    }

    override fun changePassword(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.changePassword(param, header)
    }

    override fun contactUs(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.contactUs(param, header)
    }

    override fun deleteCustomCommentApi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.deleteCustomCommentApi(param, header)
    }

    override fun completePayment(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.completePayment(param, header)
    }

    override fun getSubscriptionStatus(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.getSubscriptionStatus(param, header)
    }

    override fun getExercisesDetail(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.getExercisesDetail(param, header)
    }

    override fun getFAQS(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.getFAQS(param, header)
    }

    override fun addToMyWorkout(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.addToMyWorkout(param, header)
    }

    override fun updateProgramPlanStatus(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.updateProgramPlanStatus(param, header)
    }

    override fun deleteNotification(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.deleteNotification(param, header)
    }

    override fun deleteMyWorkOut(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.deleteMyWorkOut(param, header)
    }



    override fun getNotificationCount(header: HashMap<String, String>): ANRequest<out ANRequest<*>>? {
        return instanceApi?.getNotificationCount(header)
    }

    override fun updatePreviewStatus(header: HashMap<String, String>, param: HashMap<String, String>): ANRequest<out ANRequest<*>>? {
        return instanceApi?.updatePreviewStatus(header, param)
    }

    override fun getStreamWorkOut(
        header: HashMap<String, String>,
        param: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.getStreamWorkOut(header,param)
    }

    override fun getCollectionDetail(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.getCollectionDetail(param, header)
    }

    override fun getStreamWorkoutDetail(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.getStreamWorkoutDetail(param, header)
    }

    override fun streamSearch(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.streamSearch(param, header)
    }

    override fun streamFavUnFav(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.streamFavUnFav(param, header)
    }

    override fun getFavStreamWorkout(
        header: HashMap<String, String>,
        param: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.getFavStreamWorkout(header,param)
    }

    override fun getStreamTagList(
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.getStreamTagList(header)
    }

    //devendra
    override fun homeSliderImageListApi(
        param: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return instanceApi?.homeSliderImageListApi(param)
    }

}