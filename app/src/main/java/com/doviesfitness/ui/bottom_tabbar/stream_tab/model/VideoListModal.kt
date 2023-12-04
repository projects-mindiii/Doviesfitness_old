package com.doviesfitness.ui.bottom_tabbar.stream_tab.model

data class VideoListModal( val MaxProgress: Int,
                           val Progress: Int,
                           val downLoadUrl: String,
                           val isComplete: Boolean,
                           val isPlaying: Boolean,
                           val isRestTime: Boolean,
                           val seekTo: Long,
                           val stream_video: String,
                           val stream_video_description: String,
                           val stream_video_id: String,
                           val stream_video_image: String,
                           val stream_video_image_url: String,
                           val stream_video_name: String,
                           val stream_video_subtitle: String,
                           val video_duration: String,
                           val timeStempUrl: String)


/*
"MaxProgress":0,
"Progress":0,
"downLoadUrl":"/storage/emulated/0/Android/data/com.doviesfitness.debug/files/.Download/Dovies//2KOIkHRSBE8bXwfM.mp4",
"isComplete":false,
"isPlaying":false,
"isRestTime":false,
"seekTo":0,
"stream_video":"https://s3.us-east-2.amazonaws.com/dovies-fitness-dev/stream_video/2KOIkHRSBE8bXwfM.mp4",
"stream_video_description":"Fourth Stream Video",
"stream_video_id":"12",
"stream_video_image":"vT1IZezFyMNEcPiU.png",
"stream_video_image_url":"https://s3.us-east-2.amazonaws.com/dovies-fitness-dev/stream_video_poster/",
"stream_video_name":"Fourth Stream Video",
"stream_video_subtitle":"Fourth Stream Video",
"video_duration":"00:00:10"
}*/
