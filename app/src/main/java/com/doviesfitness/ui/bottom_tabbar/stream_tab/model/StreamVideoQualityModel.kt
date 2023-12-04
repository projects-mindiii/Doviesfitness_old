package com.doviesfitness.ui.bottom_tabbar.stream_tab.model

import com.google.gson.annotations.SerializedName

data class StreamVideoQualityModel(

    @field:SerializedName("settings")
    val settings: Settings? = null
)

data class HlsVideo(

    @field:SerializedName("vHlsMasterPlaylist")
    val vHlsMasterPlaylist: String? = null,

    @field:SerializedName("vHls720p")
    val vHls720p: String? = null,

    @field:SerializedName("vHls2K")
    val vHls2K: String? = null,

    @field:SerializedName("vHls1080p")
    val vHls1080p: String? = null,

    @field:SerializedName("vHls480p")
    val vHls480p: String? = null,

    @field:SerializedName("vHls360p")
    val vHls360p: String? = null
)

data class VideoListItem(

    @field:SerializedName("video_duration")
    val videoDuration: String? = null,

    @field:SerializedName("stream_video_description")
    val streamVideoDescription: String? = null,

    @field:SerializedName("stream_video")
    val streamVideo: String? = null,

    @field:SerializedName("stream_video_image")
    val streamVideoImage: String? = null,

    @field:SerializedName("stream_video_image_url")
    val streamVideoImageUrl: String? = null,

    @field:SerializedName("stream_video_subtitle")
    val streamVideoSubtitle: String? = null,

    @field:SerializedName("mp4_video")
    val mp4Video: Mp4Video? = null,

    @field:SerializedName("stream_video_id")
    val streamVideoId: String? = null,

    @field:SerializedName("stream_video_name")
    val streamVideoName: String? = null,

    @field:SerializedName("hls_video")
    val hlsVideo: HlsVideo? = null,

    @field:SerializedName("order")
    val order: Int? = null
)

data class Settings(

    @field:SerializedName("data")
    val data: Data? = null,

    @field:SerializedName("success")
    val success: String? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class Mp4Video(

    @field:SerializedName("vMpeg360p")
    val vMpeg360p: String? = null,

    @field:SerializedName("vMpeg480p")
    val vMpeg480p: String? = null,

    @field:SerializedName("vMpeg720p")
    val vMpeg720p: String? = null,

    @field:SerializedName("vMpeg2K")
    val vMpeg2K: String? = null,

    @field:SerializedName("vMpeg1080p")
    val vMpeg1080p: String? = null
)

data class Data(

    @field:SerializedName("stream_workout_name")
    val streamWorkoutName: String? = null,

    @field:SerializedName("display_new_tag")
    val displayNewTag: String? = null,

    @field:SerializedName("media_title_name")
    val mediaTitleName: String? = null,

    @field:SerializedName("stream_workout_trailer")
    val streamWorkoutTrailer: String? = null,

    @field:SerializedName("is_favourite")
    val isFavourite: String? = null,

    @field:SerializedName("access_level")
    val accessLevel: String? = null,

    @field:SerializedName("stream_workout_access_level")
    val streamWorkoutAccessLevel: String? = null,

    @field:SerializedName("tag_id")
    val tagId: String? = null,

    @field:SerializedName("other_media_title_name")
    val otherMediaTitleName: String? = null,

    @field:SerializedName("good_for_id")
    val goodForId: String? = null,

    @field:SerializedName("stream_workout_image")
    val streamWorkoutImage: String? = null,

    @field:SerializedName("equipment_name")
    val equipmentName: String? = null,

    @field:SerializedName("trailer_title")
    val trailerTitle: String? = null,

    @field:SerializedName("stream_workout_trailer_image")
    val streamWorkoutTrailerImage: String? = null,

    @field:SerializedName("stream_workout_share_url")
    val streamWorkoutShareUrl: String? = null,

    @field:SerializedName("other_media_list")
    val otherMediaList: List<Any?>? = null,

    @field:SerializedName("tag_name")
    val tagName: String? = null,

    @field:SerializedName("workout_video_count")
    val workoutVideoCount: String? = null,

    @field:SerializedName("stream_workout_description")
    val streamWorkoutDescription: String? = null,

    @field:SerializedName("display_new_tag_text")
    val displayNewTagText: String? = null,

    @field:SerializedName("stream_workout_image_url")
    val streamWorkoutImageUrl: String? = null,

    @field:SerializedName("video_list")
    val videoList: List<VideoListItem?>? = null,

    @field:SerializedName("stream_workout_subtitle")
    val streamWorkoutSubtitle: String? = null,

    @field:SerializedName("stream_workout_id")
    val streamWorkoutId: String? = null,

    @field:SerializedName("good_for_name")
    val goodForName: String? = null,

    @field:SerializedName("workout_level")
    val workoutLevel: String? = null,

    @field:SerializedName("trailer_duration")
    val trailerDuration: String? = null,

    @field:SerializedName("equipment_id")
    val equipmentId: String? = null
)
