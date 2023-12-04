package com.doviesfitness.ui.bottom_tabbar.workout_tab.model

import java.io.Serializable

data class CustomerListModel(
    val `data`: Data,
    val settings: Settings
) {
    data class Data(
        val doviesGuest: List<DoviesGuest>,
        val users: List<User>,
        val workoutGroup: List<WorkoutGroup>
    ) {
        data class DoviesGuest(
            val iDeviosGuestId: String,
            val vGuestName: String,
            var isCheck:Boolean
        ):Serializable

        data class User(
            val iCustomerId: String,
            val vName: String,
            val vUserName: String,
            var isCheck:Boolean
        ):Serializable

        data class WorkoutGroup(
            val iWorkoutGroupMasterId: String,
            val vGroupName: String,
            var isCheck: Boolean
        ):Serializable
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