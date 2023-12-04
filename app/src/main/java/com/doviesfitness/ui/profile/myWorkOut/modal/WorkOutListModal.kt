package com.doviesfitness.ui.profile.myWorkOut.modal

import java.io.Serializable

data class WorkOutListModal(
    val `data`: List<Data>,
    val settings: Settings
) : Serializable {
    data class Settings(
        val count: String = "",
        val curr_page: String  = "",
        val fields: List<String> = mutableListOf(),
        val message: String  = "",
        val next_page: String  = "",
        val per_page: String = "",
        val prev_page: String = "",
        val success: String = ""
    ) : Serializable

    data class Data(
        val workout_access_level: String = "",
        val workout_category: String = "",
        var workout_fav_status: String = "",
        val workout_id: String = "",
        val workout_image: String = "",
        val workout_name: String = "",
        val workout_share_url: String = "",
        val workout_time: String = "",
        val workout_time1: String = "",
        var isSelected: String? = "No",
        var forDay: String? = "",
        var whichWeek: String? = "",
        var program_WorkOut_id: String? = ""
    ) : Serializable
}