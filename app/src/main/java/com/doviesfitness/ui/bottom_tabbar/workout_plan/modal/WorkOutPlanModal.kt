package com.doviesfitness.ui.bottom_tabbar.workout_plan.modal


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WorkOutPlanModal(
    val `data`: Data,
    val settings: Settings
) : Parcelable {

    @Parcelize
    data class Data(
        val get_all_programs: List<GetAllProgram>,
        val get_program_static_content: List<GetProgramStaticContent>
    ) : Parcelable {

        @Parcelize
        data class GetAllProgram(
            val new_tag: String = "",
            val program_access_level: String = "",
            var program_id: String = "",
            val program_image: String = "",
            val program_portrait_image: String = "",
            val program_name: String = "",
            val program_sub_title: String = "",
            val show_in_app: String = "",

            val program_share_url: String = "",
            val program_week_count: String = "",
            val access_level: String = ""
        ) : Parcelable

        @Parcelize
        data class GetProgramStaticContent(
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