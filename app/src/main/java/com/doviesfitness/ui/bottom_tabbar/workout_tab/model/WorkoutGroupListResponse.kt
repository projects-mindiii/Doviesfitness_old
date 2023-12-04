package com.doviesfitness.ui.bottom_tabbar.workout_tab.model

data class WorkoutGroupListResponse(
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
        val level_name: String,
        val workout_count: String,
        var viewAll:Boolean=false,
        val workout_list: List<Workout>
    ) {
        data class Workout(
            val cf_customer_favorites_id: String,
            val is_display_new_tag1: String,
            val is_new: String,
            val level_name: String,
            val workout_access_level: String,
            val workout_category: String,
            val workout_count: String,
            val workout_description: String,
            val workout_exercise_count: String,
            val workout_fav_status: String,
            val workout_group_description: String,
            val workout_group_id: String,
            val workout_group_name: String,
            val workout_id: String,
            val workout_image: String,
            val workout_level: String,
            val workout_list: String,
            val workout_name: String,
            val workout_share_url: String,
            val workout_time: String,
            val workout_total_work_time: String
        )
    }
}