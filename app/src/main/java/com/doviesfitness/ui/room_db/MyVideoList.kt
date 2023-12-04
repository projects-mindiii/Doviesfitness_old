package com.doviesfitness.ui.room_db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Video_list")
class MyVideoList : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = "stream_workout_id")
    var  stream_workout_id: String? = null

    @ColumnInfo(name = "stream_video_id")
    var stream_video_id: String? = null

    @ColumnInfo(name = "stream_video_name")
    var stream_video_name: String? = null

    @ColumnInfo(name = "stream_video_subtitle")
    var stream_video_subtitle: String? = null

    @ColumnInfo(name = "stream_video_description")
    var stream_video_description: String? = null

    @ColumnInfo(name = "stream_video_image")
    var stream_video_image: String? = null

    @ColumnInfo(name = "video_duration")
    var video_duration: String?  = null

    @ColumnInfo(name = "stream_video_image_url")
    var stream_video_image_url: String?  = null

    @ColumnInfo(name = "stream_video")
    var stream_video:  String? = null

    @ColumnInfo(name = "down_load_url")
    var downLoad_url:  String? = null

    @ColumnInfo(name = "time_stemp_url")
    var time_stemp_url:  String? = null

    @ColumnInfo(name = "is_workout")
    var isworkout:  String? = null

    @ColumnInfo(name = "stream_video_url")
    var stream_video_url: String?  = null

    @ColumnInfo(name = "view_type")
    var view_type: String?  = null
}