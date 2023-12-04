package com.doviesfitness.ui.bottom_tabbar.stream_tab.model

import java.io.Serializable

data class StreamDataModel(
    val settings: Settings
):Serializable {
    data class Settings(
        val `data`: Data,
        val message: String,
        val success: String
    ) :Serializable{
        data class Data(
            val collection_list: List<Collection>,
            val pinned_workout: PinnedWorkout,
            val popular_collection_workouts: Any,
            val recent_workouts: List<RecentWorkout>
        ) :Serializable{
            data class Collection(
                val collection_workout_count: String,
                val iSequenceNumber: String,
                val stream_workout_collection_description: String,
                val stream_workout_collection_id: String,
                val show_in: String,
                val stream_workout_collection_image: String,
                val stream_workout_collection_image_url: String,
                val stream_workout_collection_subtitle: String,
                val stream_workout_collection_title: String,
                val subtitle_show_in_app: String,
                val title_show_in_app: String,

                val workout_list: List<PopularCollectionWorkouts.Workout>

            ):Serializable

            data class PinnedWorkout(
                val access_level: String,
                val display_new_tag: String,
                val display_new_tag_text: String,
                val is_favourite: String,
                val media_title_name: String,
                val stream_workout_access_level: String,
                val stream_workout_id: String,
                val stream_workout_image: String,
                val stream_workout_image_url: String,
                val stream_workout_name: String,
                val stream_workout_share_url: String,
                val stream_workout_status: String,
                val stream_workout_subtitle: String,
                val video_list: List<Video>,
                val workout_video_count: String
            ) :Serializable{
                data class Video(
                        val order: Int,
                        val stream_video: String,
                        val stream_video_description: String,
                        val stream_video_id: String,
                        val stream_video_image: String,
                        val stream_video_image_url: String,
                        val stream_video_name: String,
                        val stream_video_subtitle: String,
                        val video_duration: String,
                        val is_workout: String,
                        val view_type:String,
                        var hls_video: StreamWorkoutDetailModel.Settings.Data.Video.HlsVideo?,
                        var mp4_video: StreamWorkoutDetailModel.Settings.Data.Video.Mp4Video?

                        ):Serializable
            }

            data class PopularCollectionWorkouts(
                val iSequenceNumber: String,
                val stream_workout_collection_description: String,
                val stream_workout_collection_id: String,
                val stream_workout_collection_image: String,
                val stream_workout_collection_image_url: String,
                val stream_workout_collection_subtitle: String,
                val stream_workout_collection_title: String,
                val workout_list: List<Workout>
            ) :Serializable{
                data class Workout(
                    val access_level: String,
                    val display_new_tag: String,
                    val display_new_tag_text: String,
                    val media_title_name: String,
                    val stream_workout_access_level: String,
                    val stream_workout_description: String,
                    val stream_workout_id: String,
                    val stream_workout_image: String,
                    val stream_workout_image_url: String,
                    val stream_workout_name: String,
                    val stream_workout_subtitle: String,
                    val subtitle_show_in_app: String,
                    val title_show_in_app: String
                ):Serializable
            }

            data class RecentWorkout(
                val access_level: String,
                val display_new_tag: String,
                val display_new_tag_text: String,
                val media_title_name: String,
                val stream_workout_access_level: String,
                val stream_workout_id: String,
                val stream_workout_image: String,
                val stream_workout_image_url: String,
                val stream_workout_name: String,
                val stream_workout_status: String,
                val stream_workout_subtitle: String,
                val subtitle_show_in_app: String,
                val title_show_in_app: String

            ):Serializable
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