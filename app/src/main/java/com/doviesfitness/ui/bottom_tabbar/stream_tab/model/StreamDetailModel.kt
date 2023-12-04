package com.doviesfitness.ui.bottom_tabbar.stream_tab.model

import java.io.Serializable

data class StreamDetailModel(
    val settings: Settings
) :Serializable{
    data class Settings(
        val `data`: Data,
        val message: String,
        val success: String
    ) {
        data class Data(
            val display_new_tag: String,
            val display_new_tag_text: String,
            val equipment_id: String,
            val equipment_name: String,
            val good_for_id: String,
            val good_for_name: String,
            val is_favourite: String,
            val media_title_name: String,
            val other_media_title_name: String,
            val other_media_list: List<OtherMedia>,
            val stream_workout_description: String,
            val stream_workout_id: String,
            val stream_workout_image: String,
            val stream_workout_image_url: String,
            val stream_workout_name: String,
            val stream_workout_share_url: String,
            val stream_workout_subtitle: String,
            val trailer_title: String,
            val stream_workout_trailer: String,
            val stream_workout_trailer_image: String,
            val tag_id: String,
            val tag_name: String,
            val trailer_duration: String,
            val video_list: List<Video>,
            val workout_level: String,
            val workout_video_count: String
        ):Serializable {
            data class OtherMedia(
                val exercise_access_level: String,
                val exercise_amount: String,
                val exercise_amount_display: String,
                val exercise_body_parts: String,
                val exercise_equipments: String,
                val exercise_is_favourite: String,
                val exercise_json_data: String,
                val exercise_level: String,
                val exercise_tags: String,
                val media_description: String,
                val media_id: String,
                val media_image: String,
                val media_level: String,
                val media_name: String,
                val media_video: String,
                val type: String,
                val pdf: String


            ):Serializable

            data class Video(
                val stream_video: String,
                val stream_video_description: String,
                val stream_video_id: String,
                val stream_video_image: String,
                val stream_video_image_url: String,
                val stream_video_name: String,
                val stream_video_subtitle: String,
                val video_duration: String,
                val order: Int
            ):Serializable
        }
    }
}