package com.doviesfitness.ui.bottom_tabbar.workout_plan.modal

data class FavWorkoutModel(
    val `data`: List<Data>,
    val settings: Settings
) {
    data class Data(
        val access_level: String,
        val is_workout_like: String,
        val total_workout_time: String,
        val workout_access_level: String,
        val workout_exercise_count: String,
        val workout_fav_status: String,
        val workout_group_description: String,
        val workout_group_id: String,
        val workout_group_name: String,
        val workout_id: String,
        val workout_image: String,
        val workout_level: String,
        val workout_name: String,
        val workout_share_url: String
    )

    data class Settings(
        val fields: List<String>,
        val message: String,
        val success: String
    )
}