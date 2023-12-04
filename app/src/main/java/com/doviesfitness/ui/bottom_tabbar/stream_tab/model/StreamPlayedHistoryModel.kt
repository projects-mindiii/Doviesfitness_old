package com.doviesfitness.ui.bottom_tabbar.stream_tab.model

import java.io.Serializable

data class StreamPlayedHistoryModel(
    val `data`: List<Data>,
    val settings: Settings
):Serializable {
    data class Data(
        val can_create_log: String,
        val created_date: String,
        val history_id: String,
        val hls_video: HlsVideo,
        val mp4_video: Mp4Video,
        val play_time: String,
        val stream_video: String,
        val stream_workout_id: String,
        val video_description: String,
        val video_duration: String,
        val video_id: String,
        val video_image: String,
        val stream_workout_image: String,
        val stream_workout_image_url: String,
        val video_subtitle: String,
        val video_title: String,
        var view_type:String

    ) :Serializable{
        data class HlsVideo(
                val stream_quality: String,
            val vHls1080p: String,
            val vHls2K: String,
            val vHls360p: String,
            val vHls480p: String,
            val vHls720p: String,
            val vHlsMasterPlaylist: String
        ):Serializable

        data class Mp4Video(
                val downaload_quality: String,
            val vMpeg1080p: String,
            val vMpeg2K: String,
            val vMpeg360p: String,
            val vMpeg480p: String,
            val vMpeg720p: String
        ):Serializable
    }

    data class Settings(
        val count: String,
        val message: String,
        val success: String
    )
}