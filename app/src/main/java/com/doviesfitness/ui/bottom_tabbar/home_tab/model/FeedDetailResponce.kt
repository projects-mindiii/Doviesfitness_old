package com.doviesfitness.ui.bottom_tabbar.home_tab.model

import java.io.Serializable

data class FeedDetailResponce(
    val `data`: Data3,
    val settings: Settings1
)

data class Data3(
    val get_news_feed_detail: List<GetNewsFeedDetail>,
    val news_feed_comments: ArrayList<NewsFeedComment>
)

data class NewsFeedComment(
    val customer_id: String,
    val customer_name: String,
    val customer_profile_image: String,
    val news_comment: String,
    val news_comment_delete_access: String,
    val news_comment_posted_days: String,
    val news_comments_id: String
)

data class GetNewsFeedDetail(
    val news_added_date: String,
    val news_comment_allow: String,
    var news_comment_count: String,
    val news_content_type: String,
    val news_creator_name: String,
    val news_creator_profile_image: String,
    val news_description: String,
    var news_favourite_status: String,
    val news_full_image: String,
    val news_image: String,
    var news_like_count: String,
    var news_like_status: String,
    val news_name: String,
    val news_share_url: String,
    val news_video: String,
    val nf_news_feed_id: String
):Serializable