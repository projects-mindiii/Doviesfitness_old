package com.doviesfitness.ui.bottom_tabbar.home_tab.model

import java.io.Serializable

data class CommentsResponce(
    var `data`: List<Data1>,
    val settings: Settings1
) : Serializable

data class Settings1(
    val message: String,
    val success: String
) : Serializable

data class Data1(
    var customer_id: String = "",
    var customer_name: String = "",
    var customer_profile_image: String = "",
    var news_comment: String = "",
    var news_comment_delete_access: String = "",
    var news_comment_posted_days: String = "",
    var news_comments_id: String = ""

) : Serializable