package com.doviesfitness.ui.bottom_tabbar.home_tab.model

data class PlanDetailsResponse(
    val `data`: Data2,
    val settings: Settings2
)

data class Settings2(
    val message: String,
    val success: String
)

data class Data2(
    val get_customer_program_detail: List<GetCustomerProgramDetail>
   /* val get_program_diet_plans: List<Any>,
    val get_program_workouts: List<Any>*/
)

data class GetCustomerProgramDetail(
    val allow_notification: String,
    val allowedUsersName: String,
    val allowed_users: String,
    val equipmentMasterName: String,
    val goodforMasterName: String,
    val is_featured: String,
    val is_program_favourite: String,
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
    val program_share_url: String,
    val program_week_count: String,
    val program_week_num: String
)