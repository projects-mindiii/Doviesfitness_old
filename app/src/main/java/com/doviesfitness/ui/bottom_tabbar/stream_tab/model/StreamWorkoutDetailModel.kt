package com.doviesfitness.ui.bottom_tabbar.stream_tab.model

import java.io.Serializable

data class StreamWorkoutDetailModel(
    val settings: Settings
) : Serializable {
    data class Settings(
        val `data`: Data,
        val message: String,
        val success: String
    ) : Serializable {
        data class Data(
            val display_new_tag: String,
            val display_new_tag_text: String,
            val equipment_id: String,
            val equipment_name: String,
            val good_for_id: String,
            val good_for_name: String,
            var is_favourite: String,
            val media_title_name: String,
            val other_media_title_name: String,
            val stream_workout_description: String,
            val stream_workout_id: String,
            val stream_workout_image: String,
            val stream_workout_image_url: String,
            val stream_workout_name: String,
            val stream_workout_share_url: String,
            val stream_workout_subtitle: String,
            val trailer_title: String,
            val stream_workout_trailer: String,
            val stream_workout_trailer_image: String,
            val tag_id: String,
            val tag_name: String,
            val video_list: List<Video>,
            val workout_level: String,
            val access_level: String,
            val workout_video_count: String,
            val stream_workout_access_level:String,
            val program_redirect_url:String
        ) : Serializable

        {
            data class Video(
                var stream_video: String,
                var stream_video_description: String,
                val stream_video_id: String,
                val video_duration: String,
                val stream_video_image: String,
                val stream_video_image_url: String,
                val stream_video_name: String,
                val stream_video_subtitle: String,
                val order: Int,
                var Progress: Int = 0,
                var MaxProgress: Int = 0,
                var seekTo: Long = 0,
                var isPlaying: Boolean = false,
                var isComplete: Boolean = false,
                var isRestTime: Boolean = false,
                var downLoadUrl: String = "",
                var timeStempUrl: String = "",
                var isAddedToQueue: Boolean = false,
                var hls_video: HlsVideo?,
                var mp4_video: Mp4Video?,
                var is_workout: String,
                var view_type:String


            ) : Serializable{
                var historyId:String=""

                fun sethistory(historyId:String){
                   this.historyId=historyId
                }
                fun gethistory():String{
                  return historyId
                }


                data class HlsVideo(
                    var stream_quality:String,
                    var vHlsMasterPlaylist: String,
                    var vHls2K: String,
                    var vHls1080p: String,
                    var vHls720p: String,
                    var vHls480p: String,
                    var vHls360p: String
                ) : Serializable

                data class Mp4Video(
                    var downaload_quality:String,
                    var vMpeg2K: String,
                    var vMpeg1080p: String,
                    var vMpeg720p: String,
                    var vMpeg480p: String,
                    var vMpeg360p: String
                ) : Serializable
            }
        }

    }
}