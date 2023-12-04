package com.doviesfitness.ui.bottom_tabbar.home_tab.model

data class DietPlanDetailResponce(
    val `data`: List<Data5>,
    val settings: Settings
)


data class Data5(
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