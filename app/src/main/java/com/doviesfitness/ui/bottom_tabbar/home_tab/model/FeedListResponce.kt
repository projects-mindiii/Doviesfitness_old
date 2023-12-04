package com.doviesfitness.ui.bottom_tabbar.home_tab.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class FeedListResponce(
    val `data`: Data,
    val settings: Settings
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

data class Data(
    val all_other_then_featured: List<AllOtherThenFeatured>,
    val featured_news: List<FeaturedNews>,
    val ios_package_id: String,
    val isReceiptReceived: String,
    val notifyCount: String,
    val package_master_id: String
)

@Parcelize
data class FeaturedNews(
    val feed_comments_count: String,
    val is_new: String,
    val news_comment_allow: String,
    val news_creator_name: String,
    val news_creator_profile_image: String,
    val news_description: String,
    var news_fav_status: String,
    val news_id: String,
    val news_image: String,
    val news_image_ratio: String,
    var news_like_count: String,
    var news_like_status: String,
    val news_media_type: String,
    val news_module_id: String,
    val news_module_type: String,
    val news_posted_date: String,
    val news_posted_days: String,
    val news_share_url: String,
    val news_title: String,
    val news_video: String
) : Parcelable

@Parcelize
data class AllOtherThenFeatured(
    var customer_likes: String = "",
    var feed_comments_count: String,
    val is_new: String = "",
    val news_comment_allow: String = "",
    val news_creator_name: String = "",
    val news_creator_profile_image: String = "",
    val news_description: String = "",
    var news_fav_status: String = "",
    val news_id: String = "",
    val news_image: String = "",
    val news_image_ratio: String = "",
    var news_like_status: String = "",
    val news_media_type: String = "",
    val news_module_id: String = "",
    val news_module_type: String = "",
    val news_posted_date: String = "",
    val news_posted_days: String = "",
    val news_share_url: String = "",
    val news_title: String = "",
    val news_video: String = ""
) : Parcelable