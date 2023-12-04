package com.doviesfitness.ui.bottom_tabbar.workout_plan.modal

data class FavFeedAdapter(
    val `data`: List<Data>,
    val settings: Settings
) {
    data class Data(
        val customer_likes: String,
        val feed_comments_count: String,
        val is_new: String,
        val news_comment_allow: String,
        val news_creator_name: String,
        val news_creator_profile_image: String,
        val news_description: String,
        val news_fav_status: String,
        val news_id: String,
        val news_image: String,
        val news_image_ratio: String,
        val news_like_count: String,
        val news_like_status: String,
        val news_media_type: String,
        val news_module_id: String,
        val news_module_type: String,
        val news_posted_date: String,
        val news_posted_days: String,
        val news_share_url: String,
        val news_title: String,
        val news_video: String
    )

    data class Settings(
        val fields: List<String>,
        val message: String,
        val success: String
    )
}