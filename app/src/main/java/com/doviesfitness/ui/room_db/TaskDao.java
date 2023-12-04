package com.doviesfitness.ui.room_db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {


    /*
    * ..............................WorkOutsList.................................................*/
    @Insert
    void insertStreamWorkOutList(LocalStreamVideoDataModal localStreamVideoDataModal);

    //get all workout list in table
    @Query("SELECT * FROM LocalStreamVideoDataModal")
    List<LocalStreamVideoDataModal> getAllWorkList();

    //get list on givenworkout list
    @Query("SELECT * FROM LocalStreamVideoDataModal WHERE user_name LIKE :name AND user_id LIKE :workoutId ")
    List<LocalStreamVideoDataModal> getAllWorkoutList(String name, String workoutId);


    @Query("DELETE FROM  LocalStreamVideoDataModal WHERE user_id LIKE :stream_workout_id")
    void deleteWorkOutVideo(String stream_workout_id);

    /*
     * ..............................WorkOut'sVideoList.................................................*/
    @Insert
    void insertVideoList(MyVideoList videoList);

    @Query("DELETE FROM  Video_list WHERE stream_video_name LIKE :stream_video_name AND stream_video_id LIKE :stream_video_id")
    void deleteWorkOutVideoListItem(String stream_video_name, String stream_video_id);


    @Query("SELECT * FROM LocalStream")
    List<LocalStream> getAll();


    @Insert
    void insert(LocalStream task);

    @Insert
    void insert(LocalStreamVideoDataModal task);

    @Query("SELECT * FROM LocalStream WHERE user_name LIKE :name AND user_id LIKE :UserId ")
    List<LocalStream> getAllList(String name, String UserId);


  @Query("SELECT * FROM LocalStreamVideoDataModal WHERE user_name LIKE :name AND user_id LIKE :UserId ")
    List<LocalStreamVideoDataModal> getDownloadedList(String name, String UserId);



  /*  @Query("UPDATE LocalStreamVideoDataModal SET downLoad = :downLoad WHERE stream_workout_name LIKE :stream_workout_name AND stream_workout_id LIKE :stream_workout_id")
    void updateStreamWorkOutList(Boolean downLoad, String stream_workout_name,String stream_workout_id);
*/
    @Query("SELECT * FROM video_list")
    List<MyVideoList> getAllVideo();



    @Query("SELECT * FROM video_list WHERE stream_workout_id LIKE :workoutId")
    List<MyVideoList> getAllMyWorkOutListVideo(String workoutId);


    /*@Query("UPDATE LocalStreamVideoDataModal SET video_list = :video_list WHERE stream_workout_name LIKE :stream_workout_name AND stream_workout_id LIKE :stream_workout_id")
    void updateStreamList(String video_list, String stream_workout_name,String stream_workout_id);*/

    @Delete
    void deleteWorkOutFromList(LocalStreamVideoDataModal localStreamVideoDataModal);

    @Delete
    void delete(LocalStream task);

    @Update
    void update(LocalStream task);

    @Query("UPDATE LocalStream SET w_list = :wList WHERE user_name LIKE :name AND user_id LIKE :workoutId ")
    void updateList(String wList, String name, String workoutId);

    @Query("UPDATE LocalStreamVideoDataModal SET w_list = :wList WHERE user_name LIKE :name AND user_id LIKE :workoutId ")
    void updateDownloadList(String wList, String name, String workoutId);
}
