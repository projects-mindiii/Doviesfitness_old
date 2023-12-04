package com.doviesfitness.ui.bottom_tabbar.workout_tab.model

import java.io.Serializable

data public class FilterExerciseResponse (val `data`: List<Data>, val settings: Settings):Serializable {
    data class Settings(
        val fields: List<String>,
        val message: String,
        val success: String
    ): Serializable

    data class Data(
        val group_key: String,
        val group_name: String,
        val list: ArrayList<X>
    ) :Serializable
    {
        data class X(
            val display_name: String,
            val group_key: String,
            val group_name: String,
            val id: String,
            var isCheck: Boolean=false
        ):Serializable
    }
}