package com.doviesfitness.ui.room_db;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import java.lang.IllegalStateException;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;

@SuppressWarnings("unchecked")
public final class AppDatabase_Impl extends AppDatabase {
  private volatile TaskDao _taskDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `LocalStream` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `user_id` TEXT, `user_name` TEXT, `w_list` TEXT)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `LocalStreamVideoDataModal` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `user_id` TEXT, `user_name` TEXT, `w_list` TEXT)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `Video_list` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `stream_workout_id` TEXT, `stream_video_id` TEXT, `stream_video_name` TEXT, `stream_video_subtitle` TEXT, `stream_video_description` TEXT, `stream_video_image` TEXT, `video_duration` TEXT, `stream_video_image_url` TEXT, `stream_video` TEXT, `down_load_url` TEXT, `time_stemp_url` TEXT, `is_workout` TEXT, `stream_video_url` TEXT, `view_type` TEXT)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"278188eda882436190ff7e79a1abd99f\")");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `LocalStream`");
        _db.execSQL("DROP TABLE IF EXISTS `LocalStreamVideoDataModal`");
        _db.execSQL("DROP TABLE IF EXISTS `Video_list`");
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      protected void validateMigration(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsLocalStream = new HashMap<String, TableInfo.Column>(4);
        _columnsLocalStream.put("id", new TableInfo.Column("id", "INTEGER", true, 1));
        _columnsLocalStream.put("user_id", new TableInfo.Column("user_id", "TEXT", false, 0));
        _columnsLocalStream.put("user_name", new TableInfo.Column("user_name", "TEXT", false, 0));
        _columnsLocalStream.put("w_list", new TableInfo.Column("w_list", "TEXT", false, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysLocalStream = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesLocalStream = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoLocalStream = new TableInfo("LocalStream", _columnsLocalStream, _foreignKeysLocalStream, _indicesLocalStream);
        final TableInfo _existingLocalStream = TableInfo.read(_db, "LocalStream");
        if (! _infoLocalStream.equals(_existingLocalStream)) {
          throw new IllegalStateException("Migration didn't properly handle LocalStream(com.doviesfitness.ui.room_db.LocalStream).\n"
                  + " Expected:\n" + _infoLocalStream + "\n"
                  + " Found:\n" + _existingLocalStream);
        }
        final HashMap<String, TableInfo.Column> _columnsLocalStreamVideoDataModal = new HashMap<String, TableInfo.Column>(4);
        _columnsLocalStreamVideoDataModal.put("id", new TableInfo.Column("id", "INTEGER", true, 1));
        _columnsLocalStreamVideoDataModal.put("user_id", new TableInfo.Column("user_id", "TEXT", false, 0));
        _columnsLocalStreamVideoDataModal.put("user_name", new TableInfo.Column("user_name", "TEXT", false, 0));
        _columnsLocalStreamVideoDataModal.put("w_list", new TableInfo.Column("w_list", "TEXT", false, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysLocalStreamVideoDataModal = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesLocalStreamVideoDataModal = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoLocalStreamVideoDataModal = new TableInfo("LocalStreamVideoDataModal", _columnsLocalStreamVideoDataModal, _foreignKeysLocalStreamVideoDataModal, _indicesLocalStreamVideoDataModal);
        final TableInfo _existingLocalStreamVideoDataModal = TableInfo.read(_db, "LocalStreamVideoDataModal");
        if (! _infoLocalStreamVideoDataModal.equals(_existingLocalStreamVideoDataModal)) {
          throw new IllegalStateException("Migration didn't properly handle LocalStreamVideoDataModal(com.doviesfitness.ui.room_db.LocalStreamVideoDataModal).\n"
                  + " Expected:\n" + _infoLocalStreamVideoDataModal + "\n"
                  + " Found:\n" + _existingLocalStreamVideoDataModal);
        }
        final HashMap<String, TableInfo.Column> _columnsVideoList = new HashMap<String, TableInfo.Column>(15);
        _columnsVideoList.put("id", new TableInfo.Column("id", "INTEGER", true, 1));
        _columnsVideoList.put("stream_workout_id", new TableInfo.Column("stream_workout_id", "TEXT", false, 0));
        _columnsVideoList.put("stream_video_id", new TableInfo.Column("stream_video_id", "TEXT", false, 0));
        _columnsVideoList.put("stream_video_name", new TableInfo.Column("stream_video_name", "TEXT", false, 0));
        _columnsVideoList.put("stream_video_subtitle", new TableInfo.Column("stream_video_subtitle", "TEXT", false, 0));
        _columnsVideoList.put("stream_video_description", new TableInfo.Column("stream_video_description", "TEXT", false, 0));
        _columnsVideoList.put("stream_video_image", new TableInfo.Column("stream_video_image", "TEXT", false, 0));
        _columnsVideoList.put("video_duration", new TableInfo.Column("video_duration", "TEXT", false, 0));
        _columnsVideoList.put("stream_video_image_url", new TableInfo.Column("stream_video_image_url", "TEXT", false, 0));
        _columnsVideoList.put("stream_video", new TableInfo.Column("stream_video", "TEXT", false, 0));
        _columnsVideoList.put("down_load_url", new TableInfo.Column("down_load_url", "TEXT", false, 0));
        _columnsVideoList.put("time_stemp_url", new TableInfo.Column("time_stemp_url", "TEXT", false, 0));
        _columnsVideoList.put("is_workout", new TableInfo.Column("is_workout", "TEXT", false, 0));
        _columnsVideoList.put("stream_video_url", new TableInfo.Column("stream_video_url", "TEXT", false, 0));
        _columnsVideoList.put("view_type", new TableInfo.Column("view_type", "TEXT", false, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysVideoList = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesVideoList = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoVideoList = new TableInfo("Video_list", _columnsVideoList, _foreignKeysVideoList, _indicesVideoList);
        final TableInfo _existingVideoList = TableInfo.read(_db, "Video_list");
        if (! _infoVideoList.equals(_existingVideoList)) {
          throw new IllegalStateException("Migration didn't properly handle Video_list(com.doviesfitness.ui.room_db.MyVideoList).\n"
                  + " Expected:\n" + _infoVideoList + "\n"
                  + " Found:\n" + _existingVideoList);
        }
      }
    }, "278188eda882436190ff7e79a1abd99f", "f41bec750ef82f73c01a2de1414c3dc6");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    return new InvalidationTracker(this, "LocalStream","LocalStreamVideoDataModal","Video_list");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `LocalStream`");
      _db.execSQL("DELETE FROM `LocalStreamVideoDataModal`");
      _db.execSQL("DELETE FROM `Video_list`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  public TaskDao taskDao() {
    if (_taskDao != null) {
      return _taskDao;
    } else {
      synchronized(this) {
        if(_taskDao == null) {
          _taskDao = new TaskDao_Impl(this);
        }
        return _taskDao;
      }
    }
  }
}
