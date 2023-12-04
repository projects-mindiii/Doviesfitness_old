package com.doviesfitness.ui.bottom_tabbar.stream_tab.model

import java.io.Serializable

data class StreamLogModel(
    val `data`: List<Data>,
    val settings: Settings
):Serializable {
    data class Data(
        var customer_calorie: String,
        var customer_weight: String,
        var feedback_status: String,
        var hls_video: HlsVideo,
        var log_created_date: String,
        var log_id: String,
        var mp4_video: Mp4Video,
        var note: String,
        var stream_video: String,
        var stream_workout_id: String,
        var stream_workout_image: String,
        var stream_workout_name: String,
        var stream_workout_time: String,
        var video_description: String,
        var video_duration: String,
        var video_id: String,
        var video_image: String,
        var video_subtitle: String,
        var video_title: String,
        var workout_log_images:Any,
        var view_type:String

    ):Serializable {
        data class HlsVideo(
            val vHls1080p: String,
            val vHls2K: String,
            val vHls360p: String,
            val vHls480p: String,
            val vHls720p: String,
            val vHlsMasterPlaylist: String,
            val stream_quality: String
        ):Serializable

        data class Mp4Video(
            val vMpeg1080p: String,
            val vMpeg2K: String,
            val vMpeg360p: String,
            val vMpeg480p: String,
            val vMpeg720p: String,
            val downaload_quality: String
        ):Serializable

        data class WorkoutLogImage(
            val attachment_id: String,
            val log_date: String,
            val log_image: String
        ):Serializable
    }

    data class Settings(
        val count: String,
        val message: String,
        val success: String
    ):Serializable


}