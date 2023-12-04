package com.doviesfitness.ui.bottom_tabbar.stream_tab.model;

import com.google.gson.annotations.SerializedName;

public class RecentWorkoutsItem{

	@SerializedName("stream_workout_image")
	private String streamWorkoutImage;

	@SerializedName("access_level")
	private String accessLevel;

	@SerializedName("stream_workout_name")
	private String streamWorkoutName;

	@SerializedName("stream_workout_subtitle")
	private String streamWorkoutSubtitle;

	@SerializedName("display_new_tag")
	private String displayNewTag;

	@SerializedName("stream_workout_access_level")
	private String streamWorkoutAccessLevel;

	@SerializedName("stream_workout_id")
	private String streamWorkoutId;

	@SerializedName("media_title_name")
	private String mediaTitleName;

	@SerializedName("display_new_tag_text")
	private String displayNewTagText;

	@SerializedName("stream_workout_image_url")
	private String streamWorkoutImageUrl;

	@SerializedName("stream_workout_status")
	private String streamWorkoutStatus;

	public void setStreamWorkoutImage(String streamWorkoutImage){
		this.streamWorkoutImage = streamWorkoutImage;
	}

	public String getStreamWorkoutImage(){
		return streamWorkoutImage;
	}

	public void setAccessLevel(String accessLevel){
		this.accessLevel = accessLevel;
	}

	public String getAccessLevel(){
		return accessLevel;
	}

	public void setStreamWorkoutName(String streamWorkoutName){
		this.streamWorkoutName = streamWorkoutName;
	}

	public String getStreamWorkoutName(){
		return streamWorkoutName;
	}

	public void setStreamWorkoutSubtitle(String streamWorkoutSubtitle){
		this.streamWorkoutSubtitle = streamWorkoutSubtitle;
	}

	public String getStreamWorkoutSubtitle(){
		return streamWorkoutSubtitle;
	}

	public void setDisplayNewTag(String displayNewTag){
		this.displayNewTag = displayNewTag;
	}

	public String getDisplayNewTag(){
		return displayNewTag;
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

	public void setMediaTitleName(String mediaTitleName){
		this.mediaTitleName = mediaTitleName;
	}

	public String getMediaTitleName(){
		return mediaTitleName;
	}

	public void setDisplayNewTagText(String displayNewTagText){
		this.displayNewTagText = displayNewTagText;
	}

	public String getDisplayNewTagText(){
		return displayNewTagText;
	}

	public void setStreamWorkoutImageUrl(String streamWorkoutImageUrl){
		this.streamWorkoutImageUrl = streamWorkoutImageUrl;
	}

	public String getStreamWorkoutImageUrl(){
		return streamWorkoutImageUrl;
	}

	public void setStreamWorkoutStatus(String streamWorkoutStatus){
		this.streamWorkoutStatus = streamWorkoutStatus;
	}

	public String getStreamWorkoutStatus(){
		return streamWorkoutStatus;
	}
}