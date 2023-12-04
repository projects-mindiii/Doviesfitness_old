package com.doviesfitness.data.remote

import com.doviesfitness.BuildConfig

class Webservice {
    companion object {
        // val BASE_URL = "https://dev.doviesfitness.com/WS/"
        internal val USER_LOGIN_API = BuildConfig.BASE_URL + "WS/customer_login"

        val CONTENT_TYPE1 = "application/x-www-form-urlencoded"
        val EMAIL_AVAILABILITY = BuildConfig.BASE_URL + "WS/email_availability"
        val USERNAME_AVAILABILITY = BuildConfig.BASE_URL + "WS/user_availability"
        //val USER_LOGIN_API = BASE_URL+"customer_login"
        val USER_SIGN_UP_API = BuildConfig.BASE_URL + "WS/sign_up"
        val RESET_PASSWORD_API = BuildConfig.BASE_URL + "WS/forgot_password"
        val NEWS_FEED_LIST_API = BuildConfig.BASE_URL + "WS/news_feed"
        val LIKE_UNLIKE_API = BuildConfig.BASE_URL + "WS/like_unlike"
        val MAKE_FAVORITE_API = BuildConfig.BASE_URL + "WS/make_favorite"
        val GET_NEWS_FEED_COMMENTS_API = BuildConfig.BASE_URL + "WS/get_news_feed_comments"
        val POST_NEWS_FEED_COMMENTS_API = BuildConfig.BASE_URL + "WS/news_comment_post"
        val DELETE_COMMENTS_API = BuildConfig.BASE_URL + "WS/delete_comment"
        //  val REPORT_COMMENTS_API =BASE_URL+ "report_custom_comment"
        val REPORT_COMMENTS_API = BuildConfig.BASE_URL + "WS/report_news_feed_comment"
        val PLAN_DETAIL_API = BuildConfig.BASE_URL + "WS/plan_detail"
        val NEWS_FEED_DETAIL_API = BuildConfig.BASE_URL + "WS/news_feed_detail"

        val DIET_PLAN_DETAIL_API = BuildConfig.BASE_URL + "WS/diet_plan_detail"
        val WORKOUT_FILTER_DATA = BuildConfig.BASE_URL + "WS/filter_data"
        val FEATURED_WORKOUT = BuildConfig.BASE_URL + "WS/featured_workouts"
        val EXERCISE_LIBRARY = BuildConfig.BASE_URL + "WS/exercise_library"
        val EXERCISE_LISTING = BuildConfig.BASE_URL + "WS/exercise_listing"
        val GROUP_WORKOUT_LIST = BuildConfig.BASE_URL + "WS/group_workout_list"
        val DIET_PLAN_CATEGORIES_API = BuildConfig.BASE_URL + "WS/diet_plan_categories"
        val DIET_PLANS_API = BuildConfig.BASE_URL + "WS/diet_plans"
        val DIET_PLANS_DETAILS_API = BuildConfig.BASE_URL + "WS/diet_plan_detail"
        val GET_CUSTOMER_DIET_API = BuildConfig.BASE_URL + "WS/get_customer_diet"

        val ADD_WORKOUT_FEEDBACK = BuildConfig.BASE_URL + "WS/add_workout_feedback"

        val ADD_TO_MY_DIET_API = BuildConfig.BASE_URL + "WS/add_to_my_diet"
        val DELETE_MY_DIET_API = BuildConfig.BASE_URL + "WS/delete_my_diet"
      //  val GET_USER_DETAIL = BuildConfig.BASE_URL + "WS/get_customer_detail"
        val UPDATE_CUSTOMER_DETAIL = BuildConfig.BASE_URL + "WS/update_customer_detail"
        val GET_CUSTOMER_WORKOUT = BuildConfig.BASE_URL + "WS/get_customer_workouts"
        val DELETE_WORKOUT = BuildConfig.BASE_URL + "WS/delete_workout"
        val DELETE_WORKOUT_LOG = BuildConfig.BASE_URL + "WS/delete_workout_log"
        val WORKOUT_PLAN = BuildConfig.BASE_URL + "WS/programs"
        val WORKOUT_PLAN_DETAIL = BuildConfig.BASE_URL + "WS/program_detail"
        val ADD_TO_MY_PLAN = BuildConfig.BASE_URL + "WS/add_to_my_plan"

        //val GET_CUSTOMER_PLAN = BuildConfig.BASE_URL + "WS/get_customer_plans"
        val GET_CUSTOMER_PLAN = BuildConfig.BASE_URL + "webservice_v2/get_customer_plans_list"
        val ADD_TO_PLAN_FAV = BuildConfig.BASE_URL + "WS/make_favorite"
        val DELETE_PLAN = BuildConfig.BASE_URL + "WS/delete_plan"
        val PUBLISH_WORKOUT = BuildConfig.BASE_URL + "WS/publish_workout"
        val WORKOUT_CUSTOMER_LIST = BuildConfig.BASE_URL + "WS/get_customer_list_new"
        val CUSTOMER_FAV_EXERCISE = BuildConfig.BASE_URL + "WS/get_customer_favourites"

       /* val CREATE_WORKOUT = BuildConfig.BASE_URL + "WS/add_workout"
        val EDIT_WORKOUT = BuildConfig.BASE_URL + "WS/edit_workout"*/
       val CREATE_WORKOUT = BuildConfig.BASE_URL + "webservice_v2/add_workout"//new api
        val EDIT_WORKOUT = BuildConfig.BASE_URL + "webservice_v2/edit_workout"//new api
        val WORKOUT_DETAIL_API = BuildConfig.BASE_URL + "webservice_v2/workout_detail"// new api
         //val WORKOUT_DETAIL_API = BuildConfig.BASE_URL + "WS/workout_detail" // old api
        // val CREATE_WORKOUT = BuildConfig.BASE_URL +"WS/add_workout"// old api
        // val EDIT_WORKOUT = BuildConfig.BASE_URL +"WS/edit_workout"// old api

        val ADD_TO_COMPLETE_PROGRAM = BuildConfig.BASE_URL + "WS/add_complete_program"
        val PLAN_DETAIL = BuildConfig.BASE_URL + "WS/plan_detail"
        val PACKAGE_LIST = BuildConfig.BASE_URL + "WS/package_list"
        val PLAN_ACTIVE = BuildConfig.BASE_URL + "WS/plan_admin_status"
        val NOTIFICATION_LIST = BuildConfig.BASE_URL + "WS/notification_list"
        val CUSTOMER_WORKOUT_LOG = BuildConfig.BASE_URL + "WS/customer_workout_log"
        val UPDATE_COUNTRY = BuildConfig.BASE_URL + "WS/update_customer_country"

        val UPDATE_NOTIFICATION_STATUS = BuildConfig.BASE_URL + "WS/update_notification_status"
        val CUSTOM_COMMENT_POST = BuildConfig.BASE_URL + "WS/custom_comment_post"
        val DELETE_CUSTOM_COMMENT = BuildConfig.BASE_URL + "WS/delete_custom_comment"
        val EXERCISES_DETAIL = BuildConfig.BASE_URL + "WS/exercise_detail"
        val CONTACT_US = BuildConfig.BASE_URL + "WS/contact_us"
        val CHANGE_PASSWORD = BuildConfig.BASE_URL + "WS/change_password"
        val GET_FAQS = BuildConfig.BASE_URL + "WS/get_faq"


        //For Dev Payment Api    https://www.doviesfitness.com/
        val COMPLETE_PAYMENT = BuildConfig.BASE_URL + "/webservice_v2/plan/purchase_plan"

        //for live payment Api
        //val COMPLETE_PAYMENT = "https://www.doviesfitness.com/webservice_v2/plan/purchase_plan"
        val SUBSCRIPTION_STATUS = BuildConfig.BASE_URL + "WS/subscription_status"
        val ADD_TO_MY_WORKOUT = BuildConfig.BASE_URL + "WS/add_to_my_workout"
        val UPDATE_PROGRAM_WORKOUT_STATUS = BuildConfig.BASE_URL + "WS/update_program_workout_status"
        val DELETE_INBOX_NOTIFICATION = BuildConfig.BASE_URL + "WS/delete_notification"

        //only call in Admin case
        val DELETE_MY_WORKOUT_PLAN = BuildConfig.BASE_URL + "WS/delete_workout"
        val NOTIFICATION_UNREAD_COUNT = BuildConfig.BASE_URL + "webservice_v2/notification/get_user_unread_count"
        val GET_USER_DETAIL = BuildConfig.BASE_URL + "webservice_v2/customer/detail"
        val GET_NOTIFICATION_LIST = BuildConfig.BASE_URL + "webservice_v2/notification/list"
        val GET_NOTIFICATION_DETAIL = BuildConfig.BASE_URL + "webservice_v2/notification/detail"
        val UPDATE_PUSH_NOTIFICATION_STATUS = BuildConfig.BASE_URL + "webservice_v2/notification/read"

        val PREVIEW_SETTING = BuildConfig.BASE_URL + "webservice_v2/customer/preview_status"
        val UPDATE_WORKOUT_LOG = BuildConfig.BASE_URL + "webservice_v2/customer/update_workout_log"
        val CALORI = BuildConfig.BASE_URL + "webservice_v2/customer/calorie"

        // stream module
        val STREAM_WORKOUT_LIST = BuildConfig.BASE_URL + "webservice_v2/stream_workout/list"
        val STREAM_COLLECTION_DETAIL = BuildConfig.BASE_URL + "webservice_v2/stream_workout/collection/detail"
        val FAV_STREAM_WORKOUT = BuildConfig.BASE_URL + "webservice_v2/stream_workout/my_favorites"
        val STREAM_WORKOUT_DETAIL = BuildConfig.BASE_URL + "webservice_v2/stream_workout/detail"
        val STREAM_FAV_UN_FAV = BuildConfig.BASE_URL + "webservice_v2/stream_workout/favorite"
        val STREAM_TAG_LIST = BuildConfig.BASE_URL + "webservice_v2/stream_workout/tag_list"
        val STREAM_SEARCH = BuildConfig.BASE_URL + "webservice_v2/stream_workout/search"
        val RECENTLY_PLAYED =  "https://api.spotify.com/v1/me/player/recently-played"
        val PLAY_SONG =  "https://api.spotify.com/v1/me/player/play"
        val STREAM_WORKOUT_LOG = BuildConfig.BASE_URL + "webservice_v2/stream_workout/stream_workout_log/list"
        val DELETE_STREAM_LOG = BuildConfig.BASE_URL + "webservice_v2/stream_workout/stream_workout_log/delete_log"
        val CREATE_STREAM_LOG = BuildConfig.BASE_URL + "webservice_v2/stream_workout/stream_workout_log/create_log"
        val UPDATE_STREAM_WORKOUT_LOG = BuildConfig.BASE_URL + "webservice_v2/stream_workout/stream_workout_log/update_log"
        val CREATE_STREAM_VIDEO_PLAY_HISTORY = BuildConfig.BASE_URL + "webservice_v2/stream_workout/stream_workout_log/create_played_history"
        val DELETE_STREAM_VIDEO_PLAY_HISTORY = BuildConfig.BASE_URL + "webservice_v2/stream_workout/stream_workout_log/delete_history"
        val GET_STREAM_VIDEO_PLAY_HISTORY = BuildConfig.BASE_URL + "webservice_v2/stream_workout/stream_workout_log/history_list"
//delete Account -
            val DELETE_ACCOUNT_API=BuildConfig.BASE_URL+"webservice_v2/customer/delete_account"

//devendra
        val GET_HOME_SLIDER_IMAGE = BuildConfig.BASE_URL +"webservice_v2/slider_image_list"

    }
}
