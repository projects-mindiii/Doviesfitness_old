package com.doviesfitness.ui.bottom_tabbar.workout_tab.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class SetSModel(
    var setName: String,
    var exerciseList: ArrayList<ExerciseListingResponse.Data>,
    var strExerciseType: String,
    var isSelected: Boolean = false,
    var weightForExercise:Int =0,
    var repsForExercise:Int =0,
    var isFirstAutometicallyPerformedListener: Boolean? = false,
    var isRoundCompleted:Boolean=false
):Parcelable{
    public fun deepCopy(): SetSModel {
        return SetSModel(this.setName,
            this.exerciseList.map { it.deepCopy() } as ArrayList<ExerciseListingResponse.Data>,
            this.strExerciseType,
            this.isSelected,
            this.weightForExercise,
            this.repsForExercise,
        this.isFirstAutometicallyPerformedListener)
    }
}
/**class RoundModal: NSObject {

var strRoundCounts = ""
var strRoundName = ""
var strTargetSets = ""
var strTargetReps = ""
var strExerciseType = ""

var strSetsCounts = ""
var arrSets = [SetsModal]()


init(dict : [String:Any])
{
if let roundCounts = dict["roundCounts"] as? String
{
self.strRoundCounts = roundCounts
}
if let roundNames = dict["roundNames"] as? String
{
self.strRoundName = roundNames
}
if let targetSets = dict["targetSets"] as? String
{
self.strTargetSets = targetSets
}
if let targetReps = dict["targetReps"] as? String
{
self.strTargetReps = targetReps
}
if let type = dict["type"] as? String
{
self.strExerciseType = type
}

if let sets = dict["strSetsNumber"] as? [SetsModal]
{
self.arrSets = sets

}
}

}


class SetsModal: NSObject {

var strName = ""
var arrExercise = [ExerciseDetails]()
var strExerciseType = ""
var isSelected = false

init(dictSets : [String:Any])
{
if let strSetsNumber = dictSets["strSetsNumber"] as? String
{
self.strName = strSetsNumber
}
}
}

 */