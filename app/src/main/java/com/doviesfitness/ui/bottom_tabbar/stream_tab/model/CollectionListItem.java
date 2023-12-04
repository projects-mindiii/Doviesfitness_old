package com.doviesfitness.ui.bottom_tabbar.stream_tab.model;

import com.google.gson.annotations.SerializedName;

public class CollectionListItem{

	@SerializedName("stream_workout_collection_description")
	private String streamWorkoutCollectionDescription;

	@SerializedName("iSequenceNumber")
	private String iSequenceNumber;

	@SerializedName("collection_workout_count")
	private String collectionWorkoutCount;

	@SerializedName("stream_workout_collection_title")
	private String streamWorkoutCollectionTitle;

	@SerializedName("stream_workout_collection_image_url")
	private String streamWorkoutCollectionImageUrl;

	@SerializedName("stream_workout_collection_subtitle")
	private String streamWorkoutCollectionSubtitle;

	@SerializedName("stream_workout_collection_image")
	private String streamWorkoutCollectionImage;

	@SerializedName("stream_workout_collection_id")
	private String streamWorkoutCollectionId;

	public void setStreamWorkoutCollectionDescription(String streamWorkoutCollectionDescription){
		this.streamWorkoutCollectionDescription = streamWorkoutCollectionDescription;
	}

	public String getStreamWorkoutCollectionDescription(){
		return streamWorkoutCollectionDescription;
	}

	public void setISequenceNumber(String iSequenceNumber){
		this.iSequenceNumber = iSequenceNumber;
	}

	public String getISequenceNumber(){
		return iSequenceNumber;
	}

	public void setCollectionWorkoutCount(String collectionWorkoutCount){
		this.collectionWorkoutCount = collectionWorkoutCount;
	}

	public String getCollectionWorkoutCount(){
		return collectionWorkoutCount;
	}

	public void setStreamWorkoutCollectionTitle(String streamWorkoutCollectionTitle){
		this.streamWorkoutCollectionTitle = streamWorkoutCollectionTitle;
	}

	public String getStreamWorkoutCollectionTitle(){
		return streamWorkoutCollectionTitle;
	}

	public void setStreamWorkoutCollectionImageUrl(String streamWorkoutCollectionImageUrl){
		this.streamWorkoutCollectionImageUrl = streamWorkoutCollectionImageUrl;
	}

	public String getStreamWorkoutCollectionImageUrl(){
		return streamWorkoutCollectionImageUrl;
	}

	public void setStreamWorkoutCollectionSubtitle(String streamWorkoutCollectionSubtitle){
		this.streamWorkoutCollectionSubtitle = streamWorkoutCollectionSubtitle;
	}

	public String getStreamWorkoutCollectionSubtitle(){
		return streamWorkoutCollectionSubtitle;
	}

	public void setStreamWorkoutCollectionImage(String streamWorkoutCollectionImage){
		this.streamWorkoutCollectionImage = streamWorkoutCollectionImage;
	}

	public String getStreamWorkoutCollectionImage(){
		return streamWorkoutCollectionImage;
	}

	public void setStreamWorkoutCollectionId(String streamWorkoutCollectionId){
		this.streamWorkoutCollectionId = streamWorkoutCollectionId;
	}

	public String getStreamWorkoutCollectionId(){
		return streamWorkoutCollectionId;
	}
}