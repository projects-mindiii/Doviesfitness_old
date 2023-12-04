package com.doviesfitness.ui.bottom_tabbar.stream_tab.model;

import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;

public class DModel implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    public String UserId="";
    public String UserName="";

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String display_new_tag="";
    public String equipment_id="";
    public String equipment_name="";
    public String good_for_id="";
    public String good_for_name="";
    public String is_favourite="";
    public String media_title_name="";
    public String stream_workout_description="";
    public String stream_workout_id="";
    public String stream_workout_image="";
    public String stream_workout_image_url="";
    public String stream_workout_name="";
    public String stream_workout_share_url="";
    public String stream_workout_subtitle="";
    public String stream_workout_trailer="";
    public String stream_workout_trailer_image="";
    public String tag_id="";
    public String tag_name="";
    public ArrayList<ProgressModal> download_list=new ArrayList<>();
    public String workout_level="";
    public String workout_video_count="";

    public String getDisplay_new_tag() {
        return display_new_tag;
    }

    public void setDisplay_new_tag(String display_new_tag) {
        this.display_new_tag = display_new_tag;
    }

    public String getEquipment_id() {
        return equipment_id;
    }

    public void setEquipment_id(String equipment_id) {
        this.equipment_id = equipment_id;
    }

    public String getEquipment_name() {
        return equipment_name;
    }

    public void setEquipment_name(String equipment_name) {
        this.equipment_name = equipment_name;
    }

    public String getGood_for_id() {
        return good_for_id;
    }

    public void setGood_for_id(String good_for_id) {
        this.good_for_id = good_for_id;
    }

    public String getGood_for_name() {
        return good_for_name;
    }

    public void setGood_for_name(String good_for_name) {
        this.good_for_name = good_for_name;
    }

    public String getIs_favourite() {
        return is_favourite;
    }

    public void setIs_favourite(String is_favourite) {
        this.is_favourite = is_favourite;
    }

    public String getMedia_title_name() {
        return media_title_name;
    }

    public void setMedia_title_name(String media_title_name) {
        this.media_title_name = media_title_name;
    }

    public String getStream_workout_description() {
        return stream_workout_description;
    }

    public void setStream_workout_description(String stream_workout_description) {
        this.stream_workout_description = stream_workout_description;
    }

    public String getStream_workout_id() {
        return stream_workout_id;
    }

    public void setStream_workout_id(String stream_workout_id) {
        this.stream_workout_id = stream_workout_id;
    }

    public String getStream_workout_image() {
        return stream_workout_image;
    }

    public void setStream_workout_image(String stream_workout_image) {
        this.stream_workout_image = stream_workout_image;
    }

    public String getStream_workout_image_url() {
        return stream_workout_image_url;
    }

    public void setStream_workout_image_url(String stream_workout_image_url) {
        this.stream_workout_image_url = stream_workout_image_url;
    }

    public String getStream_workout_name() {
        return stream_workout_name;
    }

    public void setStream_workout_name(String stream_workout_name) {
        this.stream_workout_name = stream_workout_name;
    }

    public String getStream_workout_share_url() {
        return stream_workout_share_url;
    }

    public void setStream_workout_share_url(String stream_workout_share_url) {
        this.stream_workout_share_url = stream_workout_share_url;
    }

    public String getStream_workout_subtitle() {
        return stream_workout_subtitle;
    }

    public void setStream_workout_subtitle(String stream_workout_subtitle) {
        this.stream_workout_subtitle = stream_workout_subtitle;
    }

    public String getStream_workout_trailer() {
        return stream_workout_trailer;
    }

    public void setStream_workout_trailer(String stream_workout_trailer) {
        this.stream_workout_trailer = stream_workout_trailer;
    }

    public String getStream_workout_trailer_image() {
        return stream_workout_trailer_image;
    }

    public void setStream_workout_trailer_image(String stream_workout_trailer_image) {
        this.stream_workout_trailer_image = stream_workout_trailer_image;
    }

    public String getTag_id() {
        return tag_id;
    }

    public void setTag_id(String tag_id) {
        this.tag_id = tag_id;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public ArrayList<ProgressModal> getDownload_list() {
        return download_list;
    }

    public void setDownload_list(ArrayList<ProgressModal> download_list) {
        this.download_list = download_list;
    }

    public String getWorkout_level() {
        return workout_level;
    }

    public void setWorkout_level(String workout_level) {
        this.workout_level = workout_level;
    }

    public String getWorkout_video_count() {
        return workout_video_count;
    }

    public void setWorkout_video_count(String workout_video_count) {
        this.workout_video_count = workout_video_count;
    }



    class ProgressModal implements Serializable{

        public int Position=0;
        public String VideoUrl="";
        public String workout_id="";
        public String stream_workout_image="";
        public String stream_workout_image_url="";
        public String stream_workout_name="";
        public String stream_workout_description="";
        public boolean isAddedQueue=false;
        public String stream_video_description="";
        public String stream_video_id="";
        public String video_duration="";
        public String stream_video_image="";
        public String stream_video_image_url="";
        public String stream_video_name="";
        public String stream_video_subtitle="";
        public int Progress=0;
        public int MaxProgress=0;
        public long seekTo=0;
        public boolean isPlaying=false;
        public boolean isComplete=false;
        public boolean isRestTime=false;
        public String downLoadUrl="";
        public String timeStempUrl="";

        public int getPosition() {
            return Position;
        }

        public void setPosition(int position) {
            Position = position;
        }

        public String getVideoUrl() {
            return VideoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            VideoUrl = videoUrl;
        }

        public String getWorkout_id() {
            return workout_id;
        }

        public void setWorkout_id(String workout_id) {
            this.workout_id = workout_id;
        }

        public String getStream_workout_image() {
            return stream_workout_image;
        }

        public void setStream_workout_image(String stream_workout_image) {
            this.stream_workout_image = stream_workout_image;
        }

        public String getStream_workout_image_url() {
            return stream_workout_image_url;
        }

        public void setStream_workout_image_url(String stream_workout_image_url) {
            this.stream_workout_image_url = stream_workout_image_url;
        }

        public String getStream_workout_name() {
            return stream_workout_name;
        }

        public void setStream_workout_name(String stream_workout_name) {
            this.stream_workout_name = stream_workout_name;
        }

        public String getStream_workout_description() {
            return stream_workout_description;
        }

        public void setStream_workout_description(String stream_workout_description) {
            this.stream_workout_description = stream_workout_description;
        }

        public boolean isAddedQueue() {
            return isAddedQueue;
        }

        public void setAddedQueue(boolean addedQueue) {
            isAddedQueue = addedQueue;
        }

        public String getStream_video_description() {
            return stream_video_description;
        }

        public void setStream_video_description(String stream_video_description) {
            this.stream_video_description = stream_video_description;
        }

        public String getStream_video_id() {
            return stream_video_id;
        }

        public void setStream_video_id(String stream_video_id) {
            this.stream_video_id = stream_video_id;
        }

        public String getVideo_duration() {
            return video_duration;
        }

        public void setVideo_duration(String video_duration) {
            this.video_duration = video_duration;
        }

        public String getStream_video_image() {
            return stream_video_image;
        }

        public void setStream_video_image(String stream_video_image) {
            this.stream_video_image = stream_video_image;
        }

        public String getStream_video_image_url() {
            return stream_video_image_url;
        }

        public void setStream_video_image_url(String stream_video_image_url) {
            this.stream_video_image_url = stream_video_image_url;
        }

        public String getStream_video_name() {
            return stream_video_name;
        }

        public void setStream_video_name(String stream_video_name) {
            this.stream_video_name = stream_video_name;
        }

        public String getStream_video_subtitle() {
            return stream_video_subtitle;
        }

        public void setStream_video_subtitle(String stream_video_subtitle) {
            this.stream_video_subtitle = stream_video_subtitle;
        }

        public int getProgress() {
            return Progress;
        }

        public void setProgress(int progress) {
            Progress = progress;
        }

        public int getMaxProgress() {
            return MaxProgress;
        }

        public void setMaxProgress(int maxProgress) {
            MaxProgress = maxProgress;
        }

        public long getSeekTo() {
            return seekTo;
        }

        public void setSeekTo(long seekTo) {
            this.seekTo = seekTo;
        }

        public boolean isPlaying() {
            return isPlaying;
        }

        public void setPlaying(boolean playing) {
            isPlaying = playing;
        }

        public boolean isComplete() {
            return isComplete;
        }

        public void setComplete(boolean complete) {
            isComplete = complete;
        }

        public boolean isRestTime() {
            return isRestTime;
        }

        public void setRestTime(boolean restTime) {
            isRestTime = restTime;
        }

        public String getDownLoadUrl() {
            return downLoadUrl;
        }

        public void setDownLoadUrl(String downLoadUrl) {
            this.downLoadUrl = downLoadUrl;
        }

        public String getTimeStempUrl() {
            return timeStempUrl;
        }

        public void setTimeStempUrl(String timeStempUrl) {
            this.timeStempUrl = timeStempUrl;
        }


    }
}


