package com.doviesfitness.ui.bottom_tabbar.workout_tab.model

import android.graphics.Bitmap
import java.io.File
import java.io.Serializable

data class ProgressPicsModel(val fileName: File/*,val imageBitmap:Bitmap*/,val picTime:String,val imageUrl:String,val imageURlTime:String,val from:String,val atachId:String):Serializable
{

}