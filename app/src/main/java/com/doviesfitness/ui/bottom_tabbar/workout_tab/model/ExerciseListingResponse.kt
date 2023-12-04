package com.doviesfitness.ui.bottom_tabbar.workout_tab.model

import android.os.Parcelable
import bolts.Bolts
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class ExerciseListingResponse(
    val `data`: List<Data> = listOf(),
    val settings: Settings
):Parcelable {


    @Parcelize
    data   class Data(
        val exercise_access_level: String,
        val exercise_amount: String,
        val exercise_amount_display: String,
        val exercise_body_parts: String,
        val exercise_description: String,
        val exercise_equipments: String,
        val exercise_id: String,
        val exercise_image: String,
        var exercise_is_favourite: String,
        val exercise_level: String,
        val exercise_name: String,
        val exercise_share_url: String,
        val exercise_tags: String,
        val exercise_video: String,
        val is_liked: String,//15
        var isPlaying: Boolean = false,
        var isSelected: Boolean = false,
        var isReps: Boolean = true,
        var exercise_timer_time: String? =null,
        var exercise_reps_time: String? =null,
        var exercise_reps_number: String? =null,
        var selected_exercise_reps_number: String? =null,
        var selected_exercise_weight_number: String? =null,
        var exercise_rest_time: String? =null,//2
        var isValid: Boolean = true,
        var showLoader: Boolean = false,
        var isClicked: Boolean = false,
        var videoLength: Long = 0,
        var isSelectedExercise: Boolean = false,
        var leftAndRightOrSuperSetOrAddExercise: String? =null,
        var isExercisePopupVisible:Boolean=false,
        var isRoundIsComplete:Boolean=false,
        var isRoundSelectedForPlayer:Boolean=false

    ) : Parcelable
    {
            fun deepCopy(): Data {
                return Data( this.exercise_access_level,
                    this.exercise_amount,
                    this. exercise_amount_display,
                    this. exercise_body_parts,
                    this. exercise_description,
                    this. exercise_equipments,
                    this. exercise_id,
                    this.exercise_image,
                    this. exercise_is_favourite,
                    this. exercise_level,
                    this. exercise_name,
                    this. exercise_share_url,
                    this. exercise_tags,
                    this. exercise_video,
                    this.is_liked,//15
                    this. isPlaying,
                    this. isSelected,
                    this. isReps,
                    this. exercise_timer_time,
                    this. exercise_reps_time,
                    this. exercise_reps_number,
                    this. selected_exercise_reps_number,
                    this. selected_exercise_weight_number,
                    this.exercise_rest_time,//2
                    this. isValid,
                    this. showLoader,
                    this. isClicked,
                    this. videoLength,
                    this. isSelectedExercise,
                    this. leftAndRightOrSuperSetOrAddExercise,
                    this.isExercisePopupVisible)

        }
    }


    fun deepCopy(): ExerciseListingResponse {
        val list: ArrayList<Data> = arrayListOf()
        for (type in `data`) {
            list.add(type)
        }

        val mData =
            ExerciseListingResponse(
                list, this.settings
            )
        return mData
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
    ):Parcelable


}