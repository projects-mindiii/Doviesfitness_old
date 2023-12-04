package com.doviesfitness.ui.bottom_tabbar.workout_tab.model

data class ExerciseLibResponse(
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
        val display_name: String,
        val ec_order_by: String,
        val exercise_category_id: String,
        val image: String
    )
}