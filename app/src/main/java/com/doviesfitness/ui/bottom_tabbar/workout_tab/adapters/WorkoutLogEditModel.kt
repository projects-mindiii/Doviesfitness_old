package com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters

import java.io.Serializable

data class WorkoutLogEditModel(
    val `data`: Data,
    val settings: Settings
) :Serializable{
    data class Data(
        val customer_calorie: String,
        val customer_weight: String,
        val feedback_status: String,
        val log_date: String,
        val workout_description: String,
        val workout_id: String,
        val workout_image: String,
        val workout_log_id: String,
        val workout_log_images: Any,
        val workout_name: String,
        val workout_note: String,
        val workout_total_time: String
    ):Serializable {
        data class WorkoutLogImage(
            val attachment_id: String,
            val log_date: String,
            val log_image: String
        ):Serializable
    }

    data class Settings(
        val message: String,
        val success: String
    )
}