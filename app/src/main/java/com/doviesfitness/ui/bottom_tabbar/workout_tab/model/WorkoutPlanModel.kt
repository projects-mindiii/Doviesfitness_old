package com.doviesfitness.ui.bottom_tabbar.workout_tab.model

data class WorkoutPlanModel(
    val `data`: Data,
    val settings: Settings
) {
    data class Data(
        val get_all_programs: List<GetAllProgram>,
        val get_program_static_content: List<GetProgramStaticContent>
    ) {
        data class GetAllProgram(
            val new_tag: String,
            val program_access_level: String,
            val program_id: String,
            val program_image: String,
            val program_name: String,
            val program_share_url: String,
            val program_week_count: String
        )

        data class GetProgramStaticContent(
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