package com.doviesfitness.ui.bottom_tabbar.workout_tab.model

data class WorkoutListModel(
    val `data`: List<Data>,
    val settings: Settings
) {
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
        val workout_access_level: String,
        val workout_category: String,
        val workout_fav_status: String,
        val workout_id: String,
        val workout_image: String,
        val workout_name: String,
        val workout_share_url: String,
        val workout_time: String,
        val workout_time1: String
    )
}