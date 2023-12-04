package com.doviesfitness.ui.bottom_tabbar.stream_tab.model.tv;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class PinnedWorkout{

    @SerializedName("stream_workout_image")
    private String streamWorkoutImage;

    @SerializedName("stream_workout_share_url")
    private String streamWorkoutShareUrl;

    @SerializedName("stream_workout_name")
    private String streamWorkoutName;

    @SerializedName("workout_video_count")
    private String workoutVideoCount;

    @SerializedName("display_new_tag")
    private String displayNewTag;

    @SerializedName("media_title_name")
    private String mediaTitleName;

    @SerializedName("stream_workout_image_url")
    private String streamWorkoutImageUrl;

    @SerializedName("display_new_tag_text")
    private String displayNewTagText;

    @SerializedName("is_favourite")
    private String isFavourite;

    @SerializedName("stream_workout_status")
    private String streamWorkoutStatus;

    @SerializedName("video_list")
    private List<VideoListItem> videoList;

    @SerializedName("access_level")
    private String accessLevel;

    @SerializedName("stream_workout_subtitle")
    private String streamWorkoutSubtitle;

    @SerializedName("stream_workout_access_level")
    private String streamWorkoutAccessLevel;

    @SerializedName("stream_workout_id")
    private String streamWorkoutId;

    public void setStreamWorkoutImage(String streamWorkoutImage){
        this.streamWorkoutImage = streamWorkoutImage;
    }

    public String getStreamWorkoutImage(){
        return streamWorkoutImage;
    }

    public void setStreamWorkoutShareUrl(String streamWorkoutShareUrl){
        this.streamWorkoutShareUrl = streamWorkoutShareUrl;
    }

    public String getStreamWorkoutShareUrl(){
        return streamWorkoutShareUrl;
    }

    public void setStreamWorkoutName(String streamWorkoutName){
        this.streamWorkoutName = streamWorkoutName;
    }

    public String getStreamWorkoutName(){
        return streamWorkoutName;
    }

    public void setWorkoutVideoCount(String workoutVideoCount){
        this.workoutVideoCount = workoutVideoCount;
    }

    public String getWorkoutVideoCount(){
        return workoutVideoCount;
    }

    public void setDisplayNewTag(String displayNewTag){
        this.displayNewTag = displayNewTag;
    }

    public String getDisplayNewTag(){
        return displayNewTag;
    }

    public void setMediaTitleName(String mediaTitleName){
        this.mediaTitleName = mediaTitleName;
    }

    public String getMediaTitleName(){
        return mediaTitleName;
    }

    public void setStreamWorkoutImageUrl(String streamWorkoutImageUrl){
        this.streamWorkoutImageUrl = streamWorkoutImageUrl;
    }

    public String getStreamWorkoutImageUrl(){
        return streamWorkoutImageUrl;
    }

    public void setDisplayNewTagText(String displayNewTagText){
        this.displayNewTagText = displayNewTagText;
    }

    public String getDisplayNewTagText(){
        return displayNewTagText;
    }

    public void setIsFavourite(String isFavourite){
        this.isFavourite = isFavourite;
    }

    public String getIsFavourite(){
        return isFavourite;
    }

    public void setStreamWorkoutStatus(String streamWorkoutStatus){
        this.streamWorkoutStatus = streamWorkoutStatus;
    }

    public String getStreamWorkoutStatus(){
        return streamWorkoutStatus;
    }

    public void setVideoList(List<VideoListItem> videoList){
        this.videoList = videoList;
    }

    public List<VideoListItem> getVideoList(){
        return videoList;
    }

    public void setAccessLevel(String accessLevel){
        this.accessLevel = accessLevel;
    }

    public String getAccessLevel(){
        return accessLevel;
    }

    public void setStreamWorkoutSubtitle(String streamWorkoutSubtitle){
        this.streamWorkoutSubtitle = streamWorkoutSubtitle;
    }

    public String getStreamWorkoutSubtitle(){
        return streamWorkoutSubtitle;
    }

    public void setStreamWorkoutAccessLevel(String streamWorkoutAccessLevel){
        this.streamWorkoutAccessLevel = streamWorkoutAccessLevel;
    }

    public String getStreamWorkoutAccessLevel(){
        return streamWorkoutAccessLevel;
    }

    public void setStreamWorkoutId(String streamWorkoutId){
        this.streamWorkoutId = streamWorkoutId;
    }

    public String getStreamWorkoutId(){
        return streamWorkoutId;
    }
}