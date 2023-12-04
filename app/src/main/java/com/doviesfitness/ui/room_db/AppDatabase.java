package com.doviesfitness.ui.room_db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {LocalStream.class, LocalStreamVideoDataModal.class, MyVideoList.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
}