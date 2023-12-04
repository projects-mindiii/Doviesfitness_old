package com.doviesfitness.ui.bottom_tabbar.stream_tab.model

import android.widget.ImageView
import android.widget.ProgressBar
import com.doviesfitness.ui.bottom_tabbar.stream_tab.adapters.StreamVideoAdapter

data class DownloadVideoModel (public  var pos: Int,
                               public  var videoModal: StreamWorkoutDetailModel.Settings.Data.Video,
                               public  var view: ImageView,
                               public  var loader: ProgressBar,
                               public  var viewHolder: StreamVideoAdapter.MyViewHolder,
                               public  var status: String){

}

data class DownloadModel (public  var pos: Int, public  var status: String,public  var url: String, public  var path: String, public  var filename: String,
                          public var progress:Long,public var maxProgress:Long){

}