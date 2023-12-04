package com.doviesfitness.ui.bottom_tabbar.workout_tab.model

data class WorkoutPlanDetailModel(
    val `data`: List<Data>,
    val settings: Settings
) {
    data class Data(
        val access_level: String,
        val is_added: String,
        val is_program_favourite: String,
        val program_access_level: String,
        val program_amount: String,
        val program_description: String,
        val program_equipments: String,
        val program_full_image: String,
        val program_good_for: String,
        val program_id: String,
        val program_image: String,
        val program_name: String,
        val program_package_code: String,
        val program_share_url: String,
        val program_week_count: String
    )

    data class Settings(
        val fields: List<String>,
        val message: String,
        val success: String
    )
}