package com.doviesfitness.ui.bottom_tabbar.workout_tab.model

data class MyDietPlan(
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
        val exercise_access_level: String,
        val exercise_amount: String,
        val exercise_amount_display: String,
        val exercise_body_parts: String,
        val exercise_description: String,
        val exercise_equipments: String,
        val exercise_id: String,
        val exercise_image: String,
        val exercise_is_favourite: String,
        val exercise_level: String,
        val exercise_name: String,
        val exercise_share_url: String,
        val exercise_tags: String,
        val exercise_video: String,
        val is_liked: String
    )
}