package com.doviesfitness.ui.bottom_tabbar.diet_plan.modal

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DietPlanSubCategoryModal(
    val `data`: Data,
    val settings: Settings
) : Parcelable {
    @Parcelize
    data class Data(
        val diet_listing: List<DietListing>,
        val diet_static_content: List<DietStaticContent>
    ) : Parcelable {
        @Parcelize
        data class DietListing(
            var diet_plan_access_level: String ="",
            var diet_plan_amount: String ="",
            var diet_plan_amount_display: String ="",
            var diet_plan_description: String ="",
            var diet_plan_full_image: String ="",
            var diet_plan_id: String ="",
            var diet_plan_image: String ="",
            var diet_plan_pdf: String ="",
            var diet_plan_share_url: String ="",
            var diet_plan_title: String ="",
            var isAdd: String =""

        ) : Parcelable

        @Parcelize
        data class DietStaticContent(
            val content: String,
            val title: String
        ) : Parcelable
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
    ) : Parcelable
}