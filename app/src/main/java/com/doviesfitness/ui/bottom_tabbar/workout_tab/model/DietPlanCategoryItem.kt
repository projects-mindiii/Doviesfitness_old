package com.doviesfitness.ui.bottom_tabbar.workout_tab.model

data class DietPlanCategoryItem(
    val `data`: Data,
    val settings: Settings
) {
    data class Data(
        val diet_plan_categories: List<DietPlanCategory>,
        val get_static_content: List<GetStaticContent>
    ) {
        data class GetStaticContent(
            val content: String,
            val title: String
        )

        data class DietPlanCategory(
            val diet_plan_category_description: String,
            val diet_plan_category_id: String,
            val diet_plan_category_image: String,
            val diet_plan_category_name: String
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