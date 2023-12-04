package com.doviesfitness.ui.bottom_tabbar.stream_tab.model

data class FavStreamModel(val settings: Settings) {
    data class Settings(
        val `data`: Data,
        val message: String,
        val success: String) {
        data class Data(
            val favourite_list: List<Favourite>,
            val total_favourite_workout_count: String
        ) {
            data class Favourite(
                val access_level: String,
                val display_new_tag: String,
                val display_new_tag_text: String,
                val stream_workout_description: String,
                val stream_workout_access_level: String,
                val stream_workout_id: String,
                val stream_workout_image: String,
                val stream_workout_image_url: String,
                val stream_workout_name: String,
                val stream_workout_subtitle: String,
                val workout_level: String
            )
        }
    }
}