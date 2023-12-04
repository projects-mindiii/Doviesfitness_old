package com.doviesfitness.ui.room_db;

import androidx.room.TypeConverter;

import com.doviesfitness.temp.modal.VideoCategoryModal;
import com.doviesfitness.ui.bottom_tabbar.stream_tab.download_background.DownloadedModal;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class GithubTypeConverters {

    static Gson gson = new Gson();

    @TypeConverter
    public static List<DownloadedModal.ProgressModal> stringToSomeObjectList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<DownloadedModal.ProgressModal>>() {}.getType();

        return gson.fromJson(data, listType);
    }


    @TypeConverter
    public static List<DownloadedModal> stringToDownloadList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<DownloadedModal>>() {}.getType();

        return gson.fromJson(data, listType);
    }
    @TypeConverter
    public static List<VideoCategoryModal> stringToDownloadList1(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<DownloadedModal>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String someObjectListToString(List<DownloadedModal.ProgressModal> someObjects) {
        return gson.toJson(someObjects);
    }

    @TypeConverter
    public static String someDownloadToString(List<DownloadedModal> someObjects) {
        return gson.toJson(someObjects);
    }
    @TypeConverter
    public static String someDownloadToString1(List<VideoCategoryModal> someObjects) {
        return gson.toJson(someObjects);
    }
}