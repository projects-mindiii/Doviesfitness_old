package com.doviesfitness.ui.bottom_tabbar.workout_plan.modal

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable


data class AddToWorkOutPLanModal(
    val `data`: List<Data>,
    val settings: Settings
):Serializable{
    data class Data(
        val program_access_level: String,
        var program_fav_status: String,
        val program_id: String,
        val program_image: String,
        val program_name: String,
        val show_in_app: String,
        val program_portrait_image: String,
        val program_sub_title: String,
        val program_share_url: String,
        val program_week_count: String,
        var status: String,
        var plan_type:String
    ):Serializable

    data class Settings(
        val count: String,
        val curr_page: String,
        val fields: List<String>,
        val message: String,
        val next_page: String,
        val per_page: String,
        val prev_page: String,
        val success: String
    ) :Serializable
}