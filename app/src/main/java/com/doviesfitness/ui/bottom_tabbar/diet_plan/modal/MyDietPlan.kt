package com.doviesfitness.ui.bottom_tabbar.diet_plan.modal

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MyDietPlan(
    val `data`: List<Data>,
    val settings: Settings
) : Parcelable {
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

    @Parcelize
    data class Data(
        val diet_plan_access_level: String,
        val diet_plan_fav_status: String,
        val diet_plan_id: String,
        val diet_plan_image: String,
        val diet_plan_pdf: String,
        val diet_plan_purchase_date: String,
        val diet_plan_title: String
    ) : Parcelable
}