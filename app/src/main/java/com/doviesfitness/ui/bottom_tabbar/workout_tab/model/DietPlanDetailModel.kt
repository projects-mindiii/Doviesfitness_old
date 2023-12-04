package com.doviesfitness.ui.bottom_tabbar.workout_tab.model

data class DietPlanDetailModel(
    val `data`: List<Data>,
    val settings: Settings
) {
    data class Settings(
        val fields: List<String>,
        val message: String,
        val success: String
    )

    data class Data(
        val allowed_users: String,
        val diet_access_level: String,
        val diet_plan_access_level: String,
        val diet_plan_description: String,
        val diet_plan_fav_status: String,
        val diet_plan_full_image: String,
        val diet_plan_image: String,
        val diet_plan_name: String,
        val diet_plan_package_code: String,
        val diet_plan_pdf: String,
        val diet_plan_share_url: String,
        val is_added: String
    )
}