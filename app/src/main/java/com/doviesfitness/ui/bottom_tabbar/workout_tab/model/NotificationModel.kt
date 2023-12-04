package com.doviesfitness.ui.bottom_tabbar.workout_tab.model

data class NotificationModel(
    val `data`: List<Data>,
    val settings: Settings
) {
    data class Data(
        val notification_ago: String,
        val notification_button_title: String,
        val notification_button_url: String,
        val notification_code: String,
        val notification_comment: List<Any>,
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
    )

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