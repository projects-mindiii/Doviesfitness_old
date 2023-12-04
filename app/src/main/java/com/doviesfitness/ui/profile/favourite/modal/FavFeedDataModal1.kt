package com.doviesfitness.ui.profile.favourite.modal

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class FavFeedDataModal1(
    var `data`: List<Data>,
    var settings: Settings
)  {

    data class Data(
        var customer_likes: String,
        var feed_comments_count: String,
        var is_new: String,
        var news_comment_allow: String,
        var news_creator_name: String,
        var news_creator_profile_image: String,
        var news_description: String,
        var news_fav_status: String,
        var news_id: String,
        var news_image: String,
        var news_image_ratio: String,
        var news_like_count: String,
        var news_like_status: String,
        var news_media_type: String,
        var news_module_id: String,
        var news_module_type: String,
        var news_posted_date: String,
        var news_posted_days: String,
        var news_share_url: String,
        var news_title: String,
        var news_video: String
    )
    data class Settings(
        var fields: List<String>,
        var message: String,
        var success: String
    )
}