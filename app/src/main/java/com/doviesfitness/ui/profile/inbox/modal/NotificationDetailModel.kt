package com.doviesfitness.ui.profile.inbox.modal

import java.io.Serializable

data class NotificationDetailModel(
    val `data`: List<Data>,
    val settings: Settings
) :Serializable{
    data class Data(
        val notification_ago: String,
        val notification_button_title: String,
        val notification_button_url: String,
        val notification_code: String,
        val notification_comment: List<NotificationComment>,
        val notification_commentCount: String,
        val notification_connection_id: String,
        val notification_date: String,
        val notification_id: String,
        val notification_image: String,
        var notification_likeCount: String,
        val notification_message: String,
        val notification_module_id: String,
        val notification_module_name: String,
        var notification_myLike: String,
        val notification_title: String,
        val notification_type: String
    ) :Serializable{
        data class NotificationComment(
            val comment: String,
            val comment_ago: String,
            val customNotificationId: String,
            val customNotificationLikeCommentId: String,
            val dAddedDate: String,
            val iCustomerId: String,
            val iSysRecDeleted: String,
            val isLike: String,
            val userId: String,
            val vName: String,
            val vProfileImage: String
        ):Serializable
    }

    data class Settings(
        val fields: List<String>,
        val message: String,
        val success: String
    )
}