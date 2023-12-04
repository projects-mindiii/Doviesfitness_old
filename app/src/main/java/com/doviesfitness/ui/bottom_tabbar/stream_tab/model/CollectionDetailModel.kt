package com.doviesfitness.ui.bottom_tabbar.stream_tab.model

data class CollectionDetailModel(
    val settings: Settings
) {
    data class Settings(
        val `data`: Data,
        val message: String,
        val success: String
    ) {
        data class Data(
            val collection_detail: CollectionDetail,
            val total_workout_count: String,
            val workout_list: List<Workout>
        ) {
            data class CollectionDetail(
                val stream_workout_collection_description: String,
                val stream_workout_collection_id: String,
                val stream_workout_collection_image: String,
                val stream_workout_collection_image_url: String,
                val stream_workout_collection_status: String,
                val stream_workout_collection_subtitle: String,
                val stream_workout_collection_title: String
            )

            data class Workout(
                val access_level: String,
                val display_new_tag: String,
                val display_new_tag_text: String,
                val media_title_name: String,
                val stream_workout_access_level: String,
                val stream_workout_id: String,
                val stream_workout_image: String,
                val stream_workout_image_url: String,
                val stream_workout_name: String,
                val stream_workout_status: String,
                val stream_workout_subtitle: String,
                val workout_video_count: String,
                val subtitle_show_in_app: String,
                val title_show_in_app: String

            )
        }
    }
}