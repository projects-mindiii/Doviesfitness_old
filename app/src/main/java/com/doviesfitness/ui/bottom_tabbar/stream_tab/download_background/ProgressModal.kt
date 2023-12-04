package com.doviesfitness.ui.bottom_tabbar.stream_tab.download_background
import java.io.Serializable
data class DownloadedModal(
    val display_new_tag: String,
    val display_new_tag_text: String,
    val equipment_id: String,
    val equipment_name: String,
    val good_for_id: String,
    val good_for_name: String,
    var is_favourite: String,
    val media_title_name: String,
    val stream_workout_description: String,
    val stream_workout_id: String,
    val stream_workout_image: String,
    val stream_workout_image_url: String,
    val stream_workout_name: String,
    val stream_workout_share_url: String,
    val stream_workout_subtitle: String,
    val stream_workout_trailer: String,
    val stream_workout_trailer_image: String,
    val tag_id: String,
    val tag_name: String,
    val download_list: ArrayList<ProgressModal>,
    val workout_level: String,
    val workout_video_count: String

):Serializable {
    data class ProgressModal(
        var Position: Int,
        var VideoUrl: String,
        var workout_id: String,
        var stream_workout_image: String,
        var stream_workout_image_url: String,
        var stream_workout_name: String,
        var stream_workout_description: String,
        var isAddedQueue: Boolean = false,
        ///
        var stream_video_description: String,
        val stream_video_id: String,
        val video_duration: String,
        val stream_video_image: String,
        val stream_video_image_url: String,
        val stream_video_name: String,
        val stream_video_subtitle: String,
        var Progress: Int = 0,
        var MaxProgress: Int = 0,
        var seekTo: Long = 0,
        var isPlaying: Boolean = false,
        var isComplete: Boolean = false,
        var isRestTime: Boolean = false,
        var downLoadUrl: String = "",
        var timeStempUrl: String = "",
        var order: Int = 1,
        var is_workout: String = "",
        var view_type:String=""

        ):Serializable{
        override fun toString(): String {
            return "ProgressModal(Position=$Position, VideoUrl='$VideoUrl', workout_id='$workout_id', stream_workout_image='$stream_workout_image', stream_workout_image_url='$stream_workout_image_url', stream_workout_name='$stream_workout_name', stream_workout_description='$stream_workout_description', isAddedQueue=$isAddedQueue, stream_video_description='$stream_video_description', stream_video_id='$stream_video_id', video_duration='$video_duration', stream_video_image='$stream_video_image', stream_video_image_url='$stream_video_image_url', stream_video_name='$stream_video_name', stream_video_subtitle='$stream_video_subtitle', Progress=$Progress, MaxProgress=$MaxProgress, seekTo=$seekTo, isPlaying=$isPlaying, isComplete=$isComplete, isRestTime=$isRestTime, downLoadUrl='$downLoadUrl', timeStempUrl='$timeStempUrl', order=$order, is_workout='$is_workout')"
        }
    }

}