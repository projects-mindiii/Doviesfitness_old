package com.doviesfitness.ui.profile.favourite.modal

data class FavWorkoutPlanModel(
    val `data`: List<Data>,
    val settings: Settings
) {
    data class Data(
        val is_customer_added: String,
        val is_program_like: String,
        val ph_purchase_history_id: String,
        val program_access_level: String,
        val program_description: String,
        val program_id: String,
        val program_image: String,
        val program_level: String,
        val program_name: String,
        val program_share_url: String,
        val program_week_count: String
    )

    data class Settings(
        val fields: List<String>,
        val message: String,
        val success: String
    )
}