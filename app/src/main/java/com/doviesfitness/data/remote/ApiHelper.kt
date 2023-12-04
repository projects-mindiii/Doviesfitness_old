package com.doviesfitness.data.remote

import com.androidnetworking.common.ANRequest
import com.doviesfitness.data.model.SignUpInfo
import java.io.File

interface ApiHelper {
    fun checkEmailAvailability(param: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun checkUserNameAvailability(param: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun doLogin(param: HashMap<String, String>): ANRequest<out ANRequest<*>>?

    fun doSignUp(param: SignUpInfo, profileImage: HashMap<String, File?>): ANRequest<out ANRequest<*>>?
    fun resetPassword(param: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun feedListApi(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun playlistApi(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun playSong(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun likeUnlikeApi(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun makeFavoriteApi(param: HashMap<String, String>, header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>?
    fun getNewsFeedCommentApi(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun postNewsFeedCommentApi(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun deleteCommentApi(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun programDetailApi(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun newsFeedDetailApi(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun workoutDetailApi(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun dietPlanDetailApi(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun exerciseLibraryApi(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun featuredWorkoutApi(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun filterWorkoutApi(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun exerciseDetailListApi(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun reportCommentApi(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun workoutGroupListApi(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun dietPlanCategoriesListApi(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun dietPlanApi(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun submitLog(param: HashMap<String, String>, header: HashMap<String, String>, FileArray:  List<File>?): ANRequest<out ANRequest<*>>?
    fun dietPlanDetailsApi(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun getCustomerDietAPi(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun addToMyDietAPi(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun deleteMyDietAPi(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun getUSerDetailAPi(header: HashMap<String, String>): ANRequest<out ANRequest<*>>?

    fun updateUserDetail(param: HashMap<String, String>, header: HashMap<String, String>, profileImage: HashMap<String, File?>): ANRequest<out ANRequest<*>>?
    fun getCustomerWorkoutAPi(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun deleteWorkout(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun getWorkoutPlanAPi(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun getWorkoutPlanDetailAPi(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun getCustomPlan(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun addToMyPlanApi(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun addToMyPlanFavApi(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun deletePlanApi(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun workoutGetCustomerList(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun getCustomerFavourites(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun createWorkout(param: HashMap<String, String>, header: HashMap<String, String>, imageFile: java.util.HashMap<String, File?>): ANRequest<out ANRequest<*>>?
    //Shubham
    fun addCompleteProgram(param: HashMap<String, String>, header: HashMap<String, String>, imageFile: java.util.HashMap<String, File?>): ANRequest<out ANRequest<*>>?
    fun planDetail(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun editWorkout(param: HashMap<String, String>, header: HashMap<String, String>, imageFile: java.util.HashMap<String, File?>
    ): ANRequest<out ANRequest<*>>?
    fun publishWorkoutApi(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun activePlanApi(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun getPackageList(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun getCustomerWorkoutLog(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?


    fun getNotificationList(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun getNotificationListNew(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun getNotificationDetail(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun notificationStatusChanged(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun readPushNotificationCount(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun customCommentApi(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun deleteCustomCommentApi(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun getExercisesDetail(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun deleteLog(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>?
    fun deleteStreamLogHistory(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>?
    fun completePayment(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun contactUs(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>?

    fun changePassword(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>?

    fun getFAQS(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>?
    fun getSubscriptionStatus(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>?
    fun addToMyWorkout(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>?
    fun updateCountry(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>?


    fun updateProgramPlanStatus(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun deleteNotification(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun deleteMyWorkOut(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun getNotificationCount(header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun updatePreviewStatus(header: HashMap<String, String>, param: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun getStreamWorkOut(header: HashMap<String, String>,param: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun getCollectionDetail(param: HashMap<String, String>,header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun getStreamWorkoutDetail(param: HashMap<String, String>,header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun streamFavUnFav(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun streamSearch(param: HashMap<String, String>,header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun getFavStreamWorkout(header: HashMap<String, String>, param: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun getStreamTagList(header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun updateLog(param: HashMap<String, String>, header: HashMap<String, String>, FileArray:  List<File>?): ANRequest<out ANRequest<*>>?
    fun updateStreamLog(param: HashMap<String, String>, header: HashMap<String, String>, FileArray:  List<File>?): ANRequest<out ANRequest<*>>?
    fun getCaloriList(header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun getStreamWorkoutLog(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun getStreamHistory(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun deleteStreamLog(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun createPlayedHistory(param: HashMap<String, String>, header: HashMap<String, String>): ANRequest<out ANRequest<*>>?
    fun submitStreamLog(param: HashMap<String, String>, header: HashMap<String, String>, FileArray:  List<File>?): ANRequest<out ANRequest<*>>?

    //devendra
    fun homeSliderImageListApi(param: HashMap<String, String>): ANRequest<out ANRequest<*>>?


}