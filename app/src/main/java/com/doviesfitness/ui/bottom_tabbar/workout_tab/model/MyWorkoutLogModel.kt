package com.doviesfitness.ui.bottom_tabbar.workout_tab.model

import java.io.Serializable

data class MyWorkoutLogModel(
    var `data`: List<Data>,
    val settings: Settings
):Serializable
{
    data class Data(
        var customer_weight: String,
        var feedback_status: String,
        var log_date: String,
        var workout_description: String,
        var workout_id: String,
        var workout_image: String,
        var workout_log_id: String,
        var workout_log_images: Any,
        var workout_name: String,
        var workout_note: String,
        var customer_calorie: String,
        var workout_total_time: String
    ):Serializable
    {
        data class WorkoutLogImage(
            var attachment_id: String,
            var log_date: String,
            var log_image: String
        ):Serializable
    }

    data class Settings(
        val count: String,
        val curr_page: String,
        val fields: List<String>,
        val message: String,
        val next_page: String,
        val per_page: String,
        val prev_page: String,
        val success: String
    ):Serializable
}