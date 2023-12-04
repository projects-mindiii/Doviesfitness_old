package com.doviesfitness.ui.createAndEditDietPlan.modal

data class ShowWorkoutDetailModel(
    val `data`: Data,
    val settings: Settings
) {
    data class Data(
        val get_customer_program_detail: List<GetCustomerProgramDetail>,
        val get_program_diet_plans: List<GetProgramDietPlan>,
        val get_program_workouts: GetProgramWorkouts
    ) {
        data class GetCustomerProgramDetail(
            val allow_notification: String,
            val allowedUsersName: String,
            val allowed_users: String,
            val equipmentMasterName: String,
            val goodforMasterName: String,
            val is_featured: String,
            var is_program_favourite: String,
            val program_access_level: String,
            val program_access_level_select: String,
            val program_amount: String,
            val program_description: String,
            val program_equipments: String,
            val program_full_image: String,
            val program_good_for: String,
            val program_id: String,
            val program_image: String,
            val program_level: String,
            val program_name: String,
            val program_sub_title: String,
            val program_portrait_image: String,
            val show_in_app: String,
            val program_share_url: String,
            val program_week_count: String,
            val program_week_num: String
        )

        data class GetProgramDietPlan(
            val diet_plan_pdf: String,
            val program_diet_id: String,
            val program_diet_image: String,
            val program_diet_name: String
        )

        data class GetProgramWorkouts(
            val Week1: List<WeekOne>,
            val Week2: List<WeekTwo>,
            val Week3: List<WeekThree>,
            val Week4: List<WeekFour>
        ) {
            data class WeekOne(
                val program_day_number: String,
                val program_week_number: String,
                val program_workout_flag: String,
                val program_workout_good_for: String,
                val program_workout_id: String,
                val program_workout_image: String,
                val program_workout_name: String,
                val program_workout_status: String,
                val program_workout_time: String,
                val workout_id: String
            )

            data class WeekTwo(
                val program_day_number: String,
                val program_week_number: String,
                val program_workout_flag: String,
                val program_workout_good_for: String,
                val program_workout_id: String,
                val program_workout_image: String,
                val program_workout_name: String,
                val program_workout_status: String,
                val program_workout_time: String,
                val workout_id: String
            )

            data class WeekThree(
                val program_day_number: String,
                val program_week_number: String,
                val program_workout_flag: String,
                val program_workout_good_for: String,
                val program_workout_id: String,
                val program_workout_image: String,
                val program_workout_name: String,
                val program_workout_status: String,
                val program_workout_time: String,
                val workout_id: String
            )

            data class WeekFour(
                val program_day_number: String,
                val program_week_number: String,
                val program_workout_flag: String,
                val program_workout_good_for: String,
                val program_workout_id: String,
                val program_workout_image: String,
                val program_workout_name: String,
                val program_workout_status: String,
                val program_workout_time: String,
                val workout_id: String
            )
        }
    }

    data class Settings(
        val fields: List<String>,
        val message: String,
        val success: String
    )
}