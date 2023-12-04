package com.doviesfitness.ui.bottom_tabbar.workout_tab.model

data class UserInfoModel(val `data`: List<Data>, val settings: Settings) {
    data class Settings(
        val fields: List<String>,
        val message: String,
        val success: String
    )

    data class Data(
        val customer_auth_token: String,
        val customer_country_id: String,
        val customer_country_name: String,
        val customer_email: String,
        val customer_email_verified: String,
        val customer_full_name: String,
        val customer_gender: String,
        val customer_height: String,
        val customer_id: String,
        val customer_isd_code: String,
        val customer_mobile_number: String,
        val customer_notify_remainder: String,
        val customer_profile_image: String,
        val customer_social_network: String,
        val customer_social_network_id: String,
        val customer_status: String,
        val customer_subscription_date: String,
        val customer_units: String,
        val customer_user_name: String,
        val customer_user_type: String,
        val customer_weight: String,
        val dob: String,
        val is_admin: String,
        val notification_status: String,
        val notifyCount: String
    )
}