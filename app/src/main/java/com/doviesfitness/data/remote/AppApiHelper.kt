package com.doviesfitness.data.remote

import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.ANRequest
import com.androidnetworking.common.Priority
import com.doviesfitness.Doviesfitness.Companion.getDataManager
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.data.model.SignUpInfo
import com.doviesfitness.data.remote.Webservice.Companion.CONTENT_TYPE1
import java.io.File

class AppApiHelper : ApiHelper {
    override fun getPackageList(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.PACKAGE_LIST)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }

    companion object {
        private val instance = AppApiHelper()

        fun getAppApiHelper(): AppApiHelper {
            return instance
        }
    }

    override fun doSignUp(param: SignUpInfo, profileImage: HashMap<String, File?>): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.upload(Webservice.USER_SIGN_UP_API)
            .addPathParameter(StringConstant.deviceToken,getDataManager().getUserStringData(AppPreferencesHelper.PREF_FIREBASE_TOKEN)!!)
            .addPathParameter(StringConstant.deviceType,StringConstant.android)
            .addHeaders(StringConstant.contentType, StringConstant.applicationJson)
            .addHeaders(StringConstant.accept, StringConstant.applicationJson)
            .addHeaders(StringConstant.apiKey, StringConstant.HBDEV)
            .addHeaders(StringConstant.apiVersion, StringConstant.apiVersionValue)
            .addMultipartParameter(param)
            .addMultipartFile(profileImage)
            .setPriority(Priority.HIGH)
            .build()
    }

    override fun doLogin(param: HashMap<String, String>): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.post(Webservice.USER_LOGIN_API)
            .addBodyParameter(param)
            .setPriority(Priority.MEDIUM)
            .build()
    }


    override fun checkUserNameAvailability(param: HashMap<String, String>): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.post(Webservice.USERNAME_AVAILABILITY)
            .addBodyParameter(param)
            .setPriority(Priority.MEDIUM)
            .build()
    }


    override fun checkEmailAvailability(param: HashMap<String, String>): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.post(Webservice.EMAIL_AVAILABILITY)
            .setContentType(CONTENT_TYPE1)
            .addBodyParameter(param)
            .setPriority(Priority.HIGH)
            .build()
    }

    override fun resetPassword(param: HashMap<String, String>): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.post(Webservice.RESET_PASSWORD_API)
            .addBodyParameter(param)
            .setPriority(Priority.MEDIUM)
            .build()
    }

    override fun feedListApi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.NEWS_FEED_LIST_API)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }
    override fun playlistApi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.RECENTLY_PLAYED)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }
    override fun playSong(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.put(Webservice.PLAY_SONG)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }

    override fun likeUnlikeApi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.LIKE_UNLIKE_API)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }

    override fun makeFavoriteApi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.MAKE_FAVORITE_API)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }

    override fun getNewsFeedCommentApi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.GET_NEWS_FEED_COMMENTS_API)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }

    override fun postNewsFeedCommentApi(
        param: HashMap<String, String>, header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.post(Webservice.POST_NEWS_FEED_COMMENTS_API)
            .setContentType(CONTENT_TYPE1)
            .addBodyParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }

     fun deleteAccountApi(
        param: HashMap<String, String>, header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.post(Webservice.DELETE_ACCOUNT_API)
            .setContentType(CONTENT_TYPE1)
            .addBodyParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }

    override fun deleteCommentApi(
        param: HashMap<String, String>, header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.DELETE_COMMENTS_API)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }

    override fun reportCommentApi(
        param: HashMap<String, String>, header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.REPORT_COMMENTS_API)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }

    override fun programDetailApi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.PLAN_DETAIL_API)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }

    override fun newsFeedDetailApi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.NEWS_FEED_DETAIL_API)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }

    override fun workoutDetailApi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.WORKOUT_DETAIL_API)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()

    }

    override fun dietPlanDetailApi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.DIET_PLAN_DETAIL_API)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()

    }

    override fun exerciseLibraryApi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.EXERCISE_LIBRARY)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()

    }

    override fun featuredWorkoutApi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.FEATURED_WORKOUT)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()

    }
    override fun filterWorkoutApi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.WORKOUT_FILTER_DATA)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()

    }
    override fun exerciseDetailListApi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.EXERCISE_LISTING)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()

    }
    override fun workoutGroupListApi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.GROUP_WORKOUT_LIST)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()

    }


    override fun dietPlanCategoriesListApi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.post(Webservice.DIET_PLAN_CATEGORIES_API)
            .setContentType(CONTENT_TYPE1)
            .addBodyParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }

    override fun dietPlanApi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.post(Webservice.DIET_PLANS_API)
            .setContentType(CONTENT_TYPE1)
            .addBodyParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }


    override fun dietPlanDetailsApi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.post(Webservice.DIET_PLANS_DETAILS_API)
            .setContentType(CONTENT_TYPE1)
            .addBodyParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }


    override fun getCustomerDietAPi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.post(Webservice.GET_CUSTOMER_DIET_API)
            .setContentType(CONTENT_TYPE1)
            .addBodyParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }


    override fun addToMyDietAPi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.post(Webservice.ADD_TO_MY_DIET_API)
            .setContentType(CONTENT_TYPE1)
            .addBodyParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }

    override fun deleteMyDietAPi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.post(Webservice.DELETE_MY_DIET_API)
            .setContentType(CONTENT_TYPE1)
            .addBodyParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }

    override fun getUSerDetailAPi(
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.GET_USER_DETAIL)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }

    override fun updateUserDetail(param: HashMap<String, String>, header: HashMap<String, String>, profileImage: HashMap<String, File?>): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.upload(Webservice.UPDATE_CUSTOMER_DETAIL)
            .addQueryParameter(param)
            .addHeaders(header)
            .addMultipartFile(profileImage)
            .setPriority(Priority.HIGH)
            .build()
    }


    override fun getCustomerWorkoutAPi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.GET_CUSTOMER_WORKOUT)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }
    override fun getCustomerWorkoutLog(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.CUSTOMER_WORKOUT_LOG)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }
    override fun getStreamWorkoutLog(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.STREAM_WORKOUT_LOG)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }
    override fun getStreamHistory(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.GET_STREAM_VIDEO_PLAY_HISTORY)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }

    override fun deleteWorkout(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.post(Webservice.DELETE_WORKOUT)
            .setContentType(CONTENT_TYPE1)
            .addBodyParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }
    override fun deleteLog(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.post(Webservice.DELETE_WORKOUT_LOG)
            .setContentType(CONTENT_TYPE1)
            .addBodyParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }
    override fun deleteStreamLog(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.post(Webservice.DELETE_STREAM_LOG)
            .setContentType(CONTENT_TYPE1)
            .addBodyParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }
    override fun deleteStreamLogHistory(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.post(Webservice.DELETE_STREAM_VIDEO_PLAY_HISTORY)
            .setContentType(CONTENT_TYPE1)
            .addBodyParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }




    override fun getWorkoutPlanAPi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.WORKOUT_PLAN)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }

    override fun getWorkoutPlanDetailAPi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.WORKOUT_PLAN_DETAIL)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }


    override fun addToMyPlanApi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.ADD_TO_MY_PLAN)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }

    override fun getCustomPlan(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.GET_CUSTOMER_PLAN)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }

    override fun addToMyPlanFavApi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.ADD_TO_PLAN_FAV)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }

    override fun deletePlanApi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.DELETE_PLAN)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }


    override fun publishWorkoutApi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.post(Webservice.PUBLISH_WORKOUT)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }
    override fun createPlayedHistory(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.post(Webservice.CREATE_STREAM_VIDEO_PLAY_HISTORY)
            .addBodyParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }

    override fun submitLog(
        param: HashMap<String, String>,
        header: HashMap<String, String>,
        FileArray: List<File>?
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.upload(Webservice.ADD_WORKOUT_FEEDBACK)
            .addHeaders(header)
            .addMultipartParameter(param)
            .addMultipartFileList("workout_images[]",FileArray)
            .setPriority(Priority.HIGH)
            .build()

    }
    override fun submitStreamLog(
        param: HashMap<String, String>,
        header: HashMap<String, String>,
        FileArray: List<File>?
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.upload(Webservice.CREATE_STREAM_LOG)
            .addHeaders(header)
            .addMultipartParameter(param)
            .addMultipartFileList("workout_images[]",FileArray)
            .setPriority(Priority.HIGH)
            .build()

    }


    override fun updateLog(
        param: HashMap<String, String>,
        header: HashMap<String, String>,
        FileArray: List<File>?
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.upload(Webservice.UPDATE_WORKOUT_LOG)
            .addHeaders(header)
            .addMultipartParameter(param)
            .addMultipartFileList("workout_images[]",FileArray)
            .setPriority(Priority.HIGH)
            .build()

    }
    override fun updateStreamLog(
        param: HashMap<String, String>,
        header: HashMap<String, String>,
        FileArray: List<File>?
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.upload(Webservice.UPDATE_STREAM_WORKOUT_LOG)
            .addHeaders(header)
            .addMultipartParameter(param)
            .addMultipartFileList("workout_images[]",FileArray)
            .setPriority(Priority.HIGH)
            .build()

    }
    override fun getCaloriList(
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.CALORI)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()

    }
    override fun workoutGetCustomerList(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.WORKOUT_CUSTOMER_LIST)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }

    override fun getCustomerFavourites(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.CUSTOMER_FAV_EXERCISE)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }
    override fun createWorkout(param: HashMap<String, String>, header: HashMap<String, String>, imageFile: java.util.HashMap<String, File?>): ANRequest<out ANRequest<*>>? {

      if (imageFile.size==0){
          return AndroidNetworking.post(Webservice.CREATE_WORKOUT)
              .addQueryParameter(param)
              .addHeaders(header)
              .setPriority(Priority.HIGH)
              .build()
      }
        else{
          return AndroidNetworking.upload(Webservice.CREATE_WORKOUT)
              .addQueryParameter(param)
              .addHeaders(header)
              .addMultipartFile(imageFile)
              .setPriority(Priority.HIGH)
              .build()
      }
    }
    override fun editWorkout(
        param: HashMap<String, String>,
        header: HashMap<String, String>,
        imageFile: java.util.HashMap<String, File?>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.upload(Webservice.EDIT_WORKOUT)
            .addQueryParameter(param)
            .addHeaders(header)
            .addMultipartFile(imageFile)
            .setPriority(Priority.HIGH)
            .build()
    }

    override fun addCompleteProgram(
        param: HashMap<String, String>,
        header: HashMap<String, String>,
        imageFile: java.util.HashMap<String, File?>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.upload(Webservice.ADD_TO_COMPLETE_PROGRAM)
            .addQueryParameter(param)
            .addHeaders(header)
            .addMultipartFile(imageFile)
            .setPriority(Priority.HIGH)
            .build()
    }

    override fun planDetail(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.PLAN_DETAIL)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }


    override fun activePlanApi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.PLAN_ACTIVE)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }



    override fun getNotificationList(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.NOTIFICATION_LIST)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }
    override fun getNotificationListNew(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.GET_NOTIFICATION_LIST)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }
    override fun getNotificationDetail(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.GET_NOTIFICATION_DETAIL)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }

    override fun notificationStatusChanged(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.UPDATE_NOTIFICATION_STATUS)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }
    override fun readPushNotificationCount(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.UPDATE_PUSH_NOTIFICATION_STATUS)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }

    override fun customCommentApi(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.post(Webservice.CUSTOM_COMMENT_POST)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }

    override fun changePassword(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.post(Webservice.CHANGE_PASSWORD)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }


    override fun contactUs(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.post(Webservice.CONTACT_US)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }


    override fun deleteCustomCommentApi(
        param: HashMap<String, String>, header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.DELETE_CUSTOM_COMMENT)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }
    override fun completePayment(
        param: HashMap<String, String>, header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.post(Webservice.COMPLETE_PAYMENT)
            .addBodyParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }
    override fun addToMyWorkout(
        param: HashMap<String, String>, header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.post(Webservice.ADD_TO_MY_WORKOUT)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }
    override fun updateCountry(
        param: HashMap<String, String>, header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.post(Webservice.UPDATE_COUNTRY)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }

    override fun getSubscriptionStatus(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.SUBSCRIPTION_STATUS)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }
    override fun getExercisesDetail(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.EXERCISES_DETAIL)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }


    override fun getFAQS(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.GET_FAQS)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }

    override fun updateProgramPlanStatus(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.UPDATE_PROGRAM_WORKOUT_STATUS)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }

    override fun deleteNotification(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.DELETE_INBOX_NOTIFICATION)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }


    override fun deleteMyWorkOut(
        param: HashMap<String, String>,
        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {

        return AndroidNetworking.get(Webservice.DELETE_MY_WORKOUT_PLAN)
            .addQueryParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }

    override fun getNotificationCount(header: HashMap<String, String>): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.NOTIFICATION_UNREAD_COUNT)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }

    override fun updatePreviewStatus(header: HashMap<String, String>, param: HashMap<String, String>): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.post(Webservice.PREVIEW_SETTING)
            .addBodyParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }

    override fun getStreamWorkOut(
        header: HashMap<String, String>,
        param: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.STREAM_WORKOUT_LIST)
            .addHeaders(header)
            .addQueryParameter(param)
            .setPriority(Priority.HIGH)
            .build()
    }
    override fun getCollectionDetail(param: HashMap<String, String>,
                                     header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.STREAM_COLLECTION_DETAIL)
            .addHeaders(header)
            .addQueryParameter(param)
            .setPriority(Priority.HIGH)
            .build()
    }
    override fun getStreamWorkoutDetail(param: HashMap<String, String>,
                                        header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.STREAM_WORKOUT_DETAIL)
            .addHeaders(header)
            .addQueryParameter(param)
            .setPriority(Priority.HIGH)
            .build()
    }
    override fun streamSearch(param: HashMap<String, String>,
                              header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.STREAM_SEARCH)
            .addHeaders(header)
            .addQueryParameter(param)
            .setPriority(Priority.HIGH)
            .build()
    }

    override fun getFavStreamWorkout(header: HashMap<String, String>,
                                     param: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.FAV_STREAM_WORKOUT)
            .addHeaders(header)
            .addQueryParameter(param)
            .setPriority(Priority.HIGH)
            .build()
    }
    override fun getStreamTagList(header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get(Webservice.STREAM_TAG_LIST)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }

    override fun streamFavUnFav(
        param: HashMap<String, String>, header: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.post(Webservice.STREAM_FAV_UN_FAV)
            .addBodyParameter(param)
            .addHeaders(header)
            .setPriority(Priority.HIGH)
            .build()
    }


    override fun homeSliderImageListApi(
        param: HashMap<String, String>
    ): ANRequest<out ANRequest<*>>? {
        return  AndroidNetworking.get(Webservice.GET_HOME_SLIDER_IMAGE)
            .addQueryParameter(param)
            .setPriority(Priority.HIGH)
            .build()
    }

}