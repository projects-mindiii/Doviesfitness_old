package com.doviesfitness.ui.bottom_tabbar.workout_tab.model

data class DietPlanSubCategoryModel(
    val `data`: Data,
    val settings: Settings
) {
    data class Data(
        val diet_listing: List<DietListing>,
        val diet_static_content: List<DietStaticContent>
    ) {
        data class DietListing(
            val diet_plan_access_level: String,
            val diet_plan_amount: String,
            val diet_plan_amount_display: String,
            val diet_plan_description: String,
            val diet_plan_full_image: String,
            val diet_plan_id: String,
            val diet_plan_image: String,
            val diet_plan_pdf: String,
            val diet_plan_share_url: String,
            val diet_plan_title: String
        )

        data class DietStaticContent(
            val content: String,
            val title: String
        )
    }

    data class Settings(
        val count: String,
        val curr_page: String,
        val fields: List<String>,
        val message: String,
        val next_page: String,
        val per_page: String,
        val prev_page: String,
        val success: String
    )
}