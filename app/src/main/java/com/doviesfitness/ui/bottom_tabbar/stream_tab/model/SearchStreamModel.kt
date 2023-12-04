package com.doviesfitness.ui.bottom_tabbar.stream_tab.model

data class SearchStreamModel(
    val settings: Settings
) {
    data class Settings(
        val count: String,
        val `data`: List<Data>,
        val message: String,
        val success: String
    ) {
        data class Data(
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
            val tag_id: String
        )
    }
}