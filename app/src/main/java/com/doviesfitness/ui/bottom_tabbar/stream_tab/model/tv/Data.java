package com.doviesfitness.ui.bottom_tabbar.stream_tab.model.tv;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data{

    @SerializedName("recent_workouts")
    private List<RecentWorkoutsItem> recentWorkouts;

    @SerializedName("popular_collection_workouts")
    private String popularCollectionWorkouts;

    @SerializedName("pinned_workout")
    private PinnedWorkout pinnedWorkout;

    @SerializedName("collection_list")
    private List<CollectionListItem> collectionList;

    public void setRecentWorkouts(List<RecentWorkoutsItem> recentWorkouts){
        this.recentWorkouts = recentWorkouts;
    }

    public List<RecentWorkoutsItem> getRecentWorkouts(){
        return recentWorkouts;
    }

  /*  public void setPopularCollectionWorkouts(PopularCollectionWorkouts popularCollectionWorkouts){
        this.popularCollectionWorkouts = popularCollectionWorkouts;
    }

    public PopularCollectionWorkouts getPopularCollectionWorkouts(){
        return popularCollectionWorkouts;
    }*/

    public void setPinnedWorkout(PinnedWorkout pinnedWorkout){
        this.pinnedWorkout = pinnedWorkout;
    }

    public PinnedWorkout getPinnedWorkout(){
        return pinnedWorkout;
    }

    public void setCollectionList(List<CollectionListItem> collectionList){
        this.collectionList = collectionList;
    }

    public List<CollectionListItem> getCollectionList(){
        return collectionList;
    }
}