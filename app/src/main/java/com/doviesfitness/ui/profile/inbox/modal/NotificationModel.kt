package com.doviesfitness.ui.profile.inbox.modal

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

data class NotificationModel(
    val `data`: List<Data>,
    val settings: Settings
) : Serializable {

    data class Data(
        val notification_ago: String,
        val notification_code: String,
        val notification_connection_id: String,
        val notification_date: String,
        val notification_id: String,
        val notification_image: String,
        val notification_message: String,
        val notification_module_id: String,
        val notification_module_name: String,
        val notification_title: String,
        val notification_type: String,
        var notification_status: String =""
        ):Serializable

    data class Settings(
        val count: String,
        val fields: List<String>,
        val message: String,
        val success: String
    ):Serializable
}