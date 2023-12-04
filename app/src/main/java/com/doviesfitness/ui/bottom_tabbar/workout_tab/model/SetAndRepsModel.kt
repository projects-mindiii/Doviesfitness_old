package com.doviesfitness.ui.bottom_tabbar.workout_tab.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class SetAndRepsModel(
    var strRoundCounts: String = "",
    var strRoundName: String = "",
    var strTargetSets: String = "",
    var strTargetReps: String = "",
    var strExerciseType: String = "",
    var strSetsCounts: String = "",
    var arrSets: ArrayList<SetSModel>,
    var isRoundPositionPopupOIsVisible: Boolean = false,
    var noteForExerciseInRound: String = "",
    var isPostnotifiedExerciseAdapter: Boolean = true,
    var isRoundSelectedForPlayer:Boolean=false,
    var isRoundLogSetCompletedForPlayer:Boolean=false

) :Parcelable  {

    public   fun deepCpoy(): SetAndRepsModel {
        return SetAndRepsModel(
            this.strRoundCounts,
            this.strRoundName,
            this.strTargetSets,
            this.strTargetReps,
            this.strExerciseType,
            this.strSetsCounts,
            this.arrSets.map { it.deepCopy() } as ArrayList<SetSModel>,
            this.isRoundPositionPopupOIsVisible,
            this.noteForExerciseInRound,
            this.isPostnotifiedExerciseAdapter)
    }
}


/**var strRoundCounts:String= ""
var strRoundName:String = ""
var strTargetSets:String = ""
var strTargetReps:String = ""
var strExerciseType:String = ""

var strSetsCounts:String = ""
var arrSets = [SetsModal]()*/