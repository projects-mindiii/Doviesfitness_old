package com.doviesfitness.data.model

import java.io.Serializable

data class UserInfoBean (
    val is_admin: String,
    val customer_id: String,
    var customer_profile_image: String,
    val customer_auth_token: String,
    val customer_email_verified: String,
    var customer_status: String,
    var customer_notification: String,
    var customer_full_name: String,
    var customer_weight: String,
    var customer_height: String,
    val customer_mobile_number: String,
    var customer_gender: String,
    var customer_email: String,
    val customer_social_network: String,
    val customer_social_network_id: String,
    val customer_notify_remainder: String,
    var customer_user_name: String,
    val customer_units: String,
    val customer_country_id: String,
    val customer_country_name: String,
    val customer_isd_code: String,
    var notification_status: String = "0",
    var dob: String,
    val title: String,
    val is_subscribed: String,
    var firebaseToken: String
):Serializable