package com.doviesfitness.ui.bottom_tabbar.workout_tab.model

data class CaloriModel(
    val `data`: Data,
    val settings: Settings
) {
    data class Data(
        val calorie_list: List<String>
    )

    data class Settings(
        val message: String,
        val success: String
    )
}