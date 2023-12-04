package com.doviesfitness.ui.bottom_tabbar.stream_tab.model.tv;

import com.google.gson.annotations.SerializedName;

public class VideoListItem{

    @SerializedName("video_duration")
    private String videoDuration;

    @SerializedName("stream_video_description")
    private String streamVideoDescription;

    @SerializedName("stream_video")
    private String streamVideo;

    @SerializedName("stream_video_image")
    private String streamVideoImage;

    @SerializedName("stream_video_image_url")
    private String streamVideoImageUrl;

    @SerializedName("stream_video_subtitle")
    private String streamVideoSubtitle;

    @SerializedName("mp4_video")
    private Mp4Video mp4Video;

    @SerializedName("stream_video_id")
    private String streamVideoId;

    @SerializedName("stream_video_name")
    private String streamVideoName;

    @SerializedName("hls_video")
    private HlsVideo hlsVideo;

    @SerializedName("order")
    private int order;

    public void setVideoDuration(String videoDuration){
        this.videoDuration = videoDuration;
    }

    public String getVideoDuration(){
        return videoDuration;
    }

    public void setStreamVideoDescription(String streamVideoDescription){
        this.streamVideoDescription = streamVideoDescription;
    }

    public String getStreamVideoDescription(){
        return streamVideoDescription;
    }

    public void setStreamVideo(String streamVideo){
        this.streamVideo = streamVideo;
    }

    public String getStreamVideo(){
        return streamVideo;
    }

    public void setStreamVideoImage(String streamVideoImage){
        this.streamVideoImage = streamVideoImage;
    }

    public String getStreamVideoImage(){
        return streamVideoImage;
    }

    public void setStreamVideoImageUrl(String streamVideoImageUrl){
        this.streamVideoImageUrl = streamVideoImageUrl;
    }

    public String getStreamVideoImageUrl(){
        return streamVideoImageUrl;
    }

    public void setStreamVideoSubtitle(String streamVideoSubtitle){
        this.streamVideoSubtitle = streamVideoSubtitle;
    }

    public String getStreamVideoSubtitle(){
        return streamVideoSubtitle;
    }

    public void setMp4Video(Mp4Video mp4Video){
        this.mp4Video = mp4Video;
    }

    public Mp4Video getMp4Video(){
        return mp4Video;
    }

    public void setStreamVideoId(String streamVideoId){
        this.streamVideoId = streamVideoId;
    }

    public String getStreamVideoId(){
        return streamVideoId;
    }

    public void setStreamVideoName(String streamVideoName){
        this.streamVideoName = streamVideoName;
    }

    public String getStreamVideoName(){
        return streamVideoName;
    }

    public void setHlsVideo(HlsVideo hlsVideo){
        this.hlsVideo = hlsVideo;
    }

    public HlsVideo getHlsVideo(){
        return hlsVideo;
    }

    public void setOrder(int order){
        this.order = order;
    }

    public int getOrder(){
        return order;
    }
}