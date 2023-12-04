package com.doviesfitness.temp.modal

import java.io.Serializable

/**Created by Yashank Rathore on 18,December,2020 yashank.mindiii@gmail.com **/

data class DownloadedVideoModal(
    var position: Int,
    var videoUrl: String,
    var workout_id: String,
    var stream_workout_image: String,
    var stream_workout_image_url: String,
    var stream_workout_name: String,
    var stream_workout_description: String,
    var isAddedQueue: Boolean = false,
    var stream_video_description: String,
    val stream_video_id: String,
    val video_duration: String,
    val stream_video_image: String,
    val stream_video_image_url: String,
    val stream_video_name: String,
    val stream_video_subtitle: String,
    var progress: Int = 0,
    var maxProgress: Int = 0,
    var seekTo: Long = 0,
    var isPlaying: Boolean = false,
    var isComplete: Boolean = false,
    var isRestTime: Boolean = false,
    var downLoadUrl: String = "",
    var timeStempUrl: String = "",
    var order: Int = 1,
    var is_workout:String=""
) : Serializable