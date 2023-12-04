package com.doviesfitness.ui.bottom_tabbar.home_tab.model

import java.io.Serializable



data class WorkoutDetailResponce(
    val `data`: Data01,
    val settings: Settings01
) : Serializable

data class Data01(
    val created_by: CreatedBy,
    val sets_reps_list: SetsRepsList?,
    val workout_detail: List<WorkoutDetail>,
    val workout_exercise_list: List<WorkoutExercise>

    ) : Serializable

data class Settings01(
    val fields: List<String>,
    val message: String,
    val success: String
) : Serializable

data class CreatedBy(
    val creator_name: String,
    val creator_profile_image: String,
    val dg_devios_guest_id: String,
    val social_media_type: List<SocialMediaType>,
    val sub_title: String
) : Serializable

data class SetsRepsList(
    val workoutGroupsData: List<WorkoutGroupsData>
) : Serializable

data class WorkoutDetail(
    val access_level: String,
    val allow_notification: String,
    val allowedUsersName: String,
    val allowed_users: String,
    val creator_id: String,
    val creator_name: String,
    val creator_profile_image: String,
    val dg_devios_guest_id: String,
    val dg_guest_id: String,
    val guest_name: String,
    val guest_profile_image: String,
    val is_featured: String,
    val notes: String,
    val workout_access_level: String,
    val workout_amount: String,
    val workout_amount_display: String,
    val workout_created_by: String,
    val workout_description: String,
    val workout_equipment: String?,
    val workout_equipment_ids: String,
    val workout_exercise_count: String,
    var workout_fav_status: String,
    val workout_full_image: String,
    val workout_good_for: String?,
    val workout_good_for_ids: String,
    val workout_id: String,
    val workout_image: String,
    val workout_level: String,
    val workout_name: String,
    val workout_package_code: String,
    val workout_share_url: String,
    val workout_total_time: String,
    val workout_type: String,
    val workout_zip_file: String
) : Serializable

data class SocialMediaType(
    val media_type: String,
    val media_user_name: String,
    val social_media_url: String
) : Serializable

data class WorkoutGroupsData(
    val dtAddedDate: String,
    val groupSetsData: List<GroupSetsData>,
    val iGroupType: String,
    val iSequenceNo: String,
    val iTargetReps: String,
    val iTargetSets: String,
    val iWorkoutGroupId: String,
    val iWorkoutMasterId: String,
    val tNotes: String
) : Serializable

data class GroupSetsData(
    val exerciseTransData: List<ExerciseTransData>,
    val iSet: String,
    val iWorkoutSetId: String
) : Serializable

data class ExerciseTransData(
    val exercise_access_level: String,
    val exercise_json_data: String,
    val exercise_share_url: String,
    val iReps: String,
    val iSequenceNumber: String,
    val iWeight: String,
    val iWorkoutExcerciseTransId: String,
    val is_liked: String,
    val newbody_part: String,
    val newequipment: String,
    val newexercise_category: String,
    val newtag: String,
    val workout_exercise_body_parts: String,
    val workout_exercise_body_parts_id: String,
    val workout_exercise_description: String,
    val workout_exercise_detail: String,
    val workout_exercise_equipments: String?,
    val workout_exercise_equipments_id: String,
    val workout_exercise_image: String,
    var workout_exercise_is_favourite: String,
    val workout_exercise_level: String,
    val workout_exercise_name: String,
    val workout_exercise_video: String,
    val workout_exercises_id: String,
    var workout_offline_video: String,
    var Progress: Int = 0,
    var MaxProgress: Int = 0,
    var isPlaying: Boolean = false,
    var isComplete: Boolean = false,
    var isRestTime: Boolean = false
) : Serializable

data class WorkoutExercise(
    val exercise_access_level: String,
    val exercise_share_url: String,
    val iSequenceNumber: String,
    val is_liked: String,
    val workout_exercise_body_parts: String?,
    val workout_exercise_body_parts_id: String,
    val workout_exercise_break_time: String,
    val workout_exercise_description: String,
    val workout_exercise_detail: String,
    val workout_exercise_equipments: String,
    val workout_exercise_equipments_id: String,
    val workout_exercise_image: String,
    var workout_exercise_is_favourite: String,
    val workout_exercise_level: String,
    val workout_exercise_name: String,
    val workout_exercise_time: String,
    val workout_exercise_type: String,
    val workout_exercise_video: String,
    val workout_exercises_id: String,
    val workout_repeat_text: String,
    var workout_offline_video: String,
    var Progress: Int = 0,
    var MaxProgress: Int = 0,
    var isPlaying: Boolean = false,
    var isComplete: Boolean = false,
    var isRestTime: Boolean = false

) : Serializable

