package com.doviesfitness.ui.bottom_tabbar.stream_tab.model

import java.io.Serializable

data class StreamLogUpdatedModel(
    val `data`: Data,
    val settings: Settings
) :Serializable{
    data class Data(
        val customer_calorie: String,
        val customer_weight: String,
        val feedback_status: String,
        val log_created_date: String,
        val log_id: String,
        val note: String,
        val video_id: String,
        val stream_workout_id: String,
        val stream_workout_image: String,
        val stream_workout_name: String,
        val stream_workout_time: String,
        val workout_log_images: Any
    ) :Serializable{
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