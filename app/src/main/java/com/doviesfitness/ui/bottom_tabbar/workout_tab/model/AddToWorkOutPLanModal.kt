package com.doviesfitness.ui.bottom_tabbar.workout_tab.model

data class AddToWorkOutPLanModal(
    val `data`: List<Data>,
    val settings: Settings
) {
    data class Data(
        val program_access_level: String,
        val program_fav_status: String,
        val program_id: String,
        val program_image: String,
        val program_name: String,
        val program_share_url: String,
        val program_week_count: String,
        val status: String
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