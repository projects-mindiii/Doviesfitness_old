package com.doviesfitness.ui.profile.inbox.modal

data class CustomNotificationModel(
    val `data`: List<Data>,
    val settings: Settings
) {
    data class Data(
        val notification_ago: String,
        val notification_button_title: String,
        val notification_button_url: String,
        val notification_code: String,
        val notification_comment: ArrayList<NotificationComment>,
        val notification_commentCount: String,
        val notification_connection_id: String,
        val notification_date: String,
        val notification_id: String,
        val notification_image: String,
        val notification_likeCount: String,
        val notification_message: String,
        val notification_module_id: String,
        val notification_module_name: String,
        val notification_myLike: String,
        val notification_status: String,
        val notification_title: String,
        val notification_type: String
    ) {
        data class NotificationComment(
            var comment: String = "",
            var comment_ago: String  = "",
            var customNotificationId: String  = "",
            var customNotificationLikeCommentId: String  = "",
            var dAddedDate: String  = "",
            var iCustomerId: String  = "",
            var iSysRecDeleted: String  = "",
            var isLike: String  = "",
            var userId: String  = "",
            var vName: String  = "",
            var vProfileImage: String  = ""
        )
    }

    data class Settings(
        val count: String,
        val curr_page: String,
        val fields: List<String>,
        val message: String,
        val next_page: String,
        val per_page: String,
        val prev_page: String,
        val success: String
    )
}