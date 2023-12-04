package com.doviesfitness.ui.bottom_tabbar.diet_plan.modal

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DietPlanCateGoriesModal (
    val `data`: Data,
    val settings: Settings
): Parcelable {
    @Parcelize
    data class Data(
        val diet_plan_categories: List<DietPlanCategory>,
        val get_static_content: List<GetStaticContent>
    ) : Parcelable  {
        @Parcelize
        data class GetStaticContent(
            val content: String,
            val title: String
        ): Parcelable


        @Parcelize
        data class DietPlanCategory(
            val diet_plan_category_description: String,
            val diet_plan_category_id: String,
            val diet_plan_category_image: String,
            val diet_plan_category_name: String
        ): Parcelable
    }

    @Parcelize
    data class Settings(
        val count: String,
        val curr_page: String,
        val fields: List<String>,
        val message: String,
        val next_page: String,
        val per_page: String,
        val prev_page: String,
        val success: String
    ): Parcelable
}