package com.doviesfitness.ui.room_db;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public final class TaskDao_Impl implements TaskDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfLocalStreamVideoDataModal;

  private final EntityInsertionAdapter __insertionAdapterOfMyVideoList;

  private final EntityInsertionAdapter __insertionAdapterOfLocalStream;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfLocalStreamVideoDataModal;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfLocalStream;

  private final EntityDeletionOrUpdateAdapter __updateAdapterOfLocalStream;

  private final SharedSQLiteStatement __preparedStmtOfDeleteWorkOutVideo;

  private final SharedSQLiteStatement __preparedStmtOfDeleteWorkOutVideoListItem;

  private final SharedSQLiteStatement __preparedStmtOfUpdateList;

  private final SharedSQLiteStatement __preparedStmtOfUpdateDownloadList;

  public TaskDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfLocalStreamVideoDataModal = new EntityInsertionAdapter<LocalStreamVideoDataModal>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `LocalStreamVideoDataModal`(`id`,`user_id`,`user_name`,`w_list`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, LocalStreamVideoDataModal value) {
        stmt.bindLong(1, value.getId());
        if (value.getUserid() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getUserid());
        }
        if (value.getUsername() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getUsername());
        }
        if (value.getWList() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getWList());
        }
      }
    };
    this.__insertionAdapterOfMyVideoList = new EntityInsertionAdapter<MyVideoList>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `Video_list`(`id`,`stream_workout_id`,`stream_video_id`,`stream_video_name`,`stream_video_subtitle`,`stream_video_description`,`stream_video_image`,`video_duration`,`stream_video_image_url`,`stream_video`,`down_load_url`,`time_stemp_url`,`is_workout`,`stream_video_url`,`view_type`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, MyVideoList value) {
        stmt.bindLong(1, value.getId());
        if (value.getStream_workout_id() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getStream_workout_id());
        }
        if (value.getStream_video_id() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getStream_video_id());
        }
        if (value.getStream_video_name() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getStream_video_name());
        }
        if (value.getStream_video_subtitle() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getStream_video_subtitle());
        }
        if (value.getStream_video_description() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getStream_video_description());
        }
        if (value.getStream_video_image() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getStream_video_image());
        }
        if (value.getVideo_duration() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getVideo_duration());
        }
        if (value.getStream_video_image_url() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getStream_video_image_url());
        }
        if (value.getStream_video() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.getStream_video());
        }
        if (value.getDownLoad_url() == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, value.getDownLoad_url());
        }
        if (value.getTime_stemp_url() == null) {
          stmt.bindNull(12);
        } else {
          stmt.bindString(12, value.getTime_stemp_url());
        }
        if (value.getIsworkout() == null) {
          stmt.bindNull(13);
        } else {
          stmt.bindString(13, value.getIsworkout());
        }
        if (value.getStream_video_url() == null) {
          stmt.bindNull(14);
        } else {
          stmt.bindString(14, value.getStream_video_url());
        }
        if (value.getView_type() == null) {
          stmt.bindNull(15);
        } else {
          stmt.bindString(15, value.getView_type());
        }
      }
    };
    this.__insertionAdapterOfLocalStream = new EntityInsertionAdapter<LocalStream>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `LocalStream`(`id`,`user_id`,`user_name`,`w_list`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, LocalStream value) {
        stmt.bindLong(1, value.getId());
        if (value.getUserid() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getUserid());
        }
        if (value.getUsername() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getUsername());
        }
        if (value.getWList() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getWList());
        }
      }
    };
    this.__deletionAdapterOfLocalStreamVideoDataModal = new EntityDeletionOrUpdateAdapter<LocalStreamVideoDataModal>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `LocalStreamVideoDataModal` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, LocalStreamVideoDataModal value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__deletionAdapterOfLocalStream = new EntityDeletionOrUpdateAdapter<LocalStream>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `LocalStream` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, LocalStream value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfLocalStream = new EntityDeletionOrUpdateAdapter<LocalStream>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `LocalStream` SET `id` = ?,`user_id` = ?,`user_name` = ?,`w_list` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, LocalStream value) {
        stmt.bindLong(1, value.getId());
        if (value.getUserid() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getUserid());
        }
        if (value.getUsername() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getUsername());
        }
        if (value.getWList() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getWList());
        }
        stmt.bindLong(5, value.getId());
      }
    };
    this.__preparedStmtOfDeleteWorkOutVideo = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM  LocalStreamVideoDataModal WHERE user_id LIKE ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteWorkOutVideoListItem = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM  Video_list WHERE stream_video_name LIKE ? AND stream_video_id LIKE ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateList = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE LocalStream SET w_list = ? WHERE user_name LIKE ? AND user_id LIKE ? ";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateDownloadList = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE LocalStreamVideoDataModal SET w_list = ? WHERE user_name LIKE ? AND user_id LIKE ? ";
        return _query;
      }
    };
  }

  @Override
  public void insertStreamWorkOutList(LocalStreamVideoDataModal localStreamVideoDataModal) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfLocalStreamVideoDataModal.insert(localStreamVideoDataModal);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertVideoList(MyVideoList videoList) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfMyVideoList.insert(videoList);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insert(LocalStream task) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfLocalStream.insert(task);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insert(LocalStreamVideoDataModal task) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfLocalStreamVideoDataModal.insert(task);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteWorkOutFromList(LocalStreamVideoDataModal localStreamVideoDataModal) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfLocalStreamVideoDataModal.handle(localStreamVideoDataModal);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(LocalStream task) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfLocalStream.handle(task);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(LocalStream task) {
    __db.beginTransaction();
    try {
      __updateAdapterOfLocalStream.handle(task);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteWorkOutVideo(String stream_workout_id) {
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteWorkOutVideo.acquire();
    __db.beginTransaction();
    try {
      int _argIndex = 1;
      if (stream_workout_id == null) {
        _stmt.bindNull(_argIndex);
      } else {
        _stmt.bindString(_argIndex, stream_workout_id);
      }
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteWorkOutVideo.release(_stmt);
    }
  }

  @Override
  public void deleteWorkOutVideoListItem(String stream_video_name, String stream_video_id) {
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteWorkOutVideoListItem.acquire();
    __db.beginTransaction();
    try {
      int _argIndex = 1;
      if (stream_video_name == null) {
        _stmt.bindNull(_argIndex);
      } else {
        _stmt.bindString(_argIndex, stream_video_name);
      }
      _argIndex = 2;
      if (stream_video_id == null) {
        _stmt.bindNull(_argIndex);
      } else {
        _stmt.bindString(_argIndex, stream_video_id);
      }
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteWorkOutVideoListItem.release(_stmt);
    }
  }

  @Override
  public void updateList(String wList, String name, String workoutId) {
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateList.acquire();
    __db.beginTransaction();
    try {
      int _argIndex = 1;
      if (wList == null) {
        _stmt.bindNull(_argIndex);
      } else {
        _stmt.bindString(_argIndex, wList);
      }
      _argIndex = 2;
      if (name == null) {
        _stmt.bindNull(_argIndex);
      } else {
        _stmt.bindString(_argIndex, name);
      }
      _argIndex = 3;
      if (workoutId == null) {
        _stmt.bindNull(_argIndex);
      } else {
        _stmt.bindString(_argIndex, workoutId);
      }
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateList.release(_stmt);
    }
  }

  @Override
  public void updateDownloadList(String wList, String name, String workoutId) {
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateDownloadList.acquire();
    __db.beginTransaction();
    try {
      int _argIndex = 1;
      if (wList == null) {
        _stmt.bindNull(_argIndex);
      } else {
        _stmt.bindString(_argIndex, wList);
      }
      _argIndex = 2;
      if (name == null) {
        _stmt.bindNull(_argIndex);
      } else {
        _stmt.bindString(_argIndex, name);
      }
      _argIndex = 3;
      if (workoutId == null) {
        _stmt.bindNull(_argIndex);
      } else {
        _stmt.bindString(_argIndex, workoutId);
      }
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateDownloadList.release(_stmt);
    }
  }

  @Override
  public List<LocalStreamVideoDataModal> getAllWorkList() {
    final String _sql = "SELECT * FROM LocalStreamVideoDataModal";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfUserid = _cursor.getColumnIndexOrThrow("user_id");
      final int _cursorIndexOfUsername = _cursor.getColumnIndexOrThrow("user_name");
      final int _cursorIndexOfWList = _cursor.getColumnIndexOrThrow("w_list");
      final List<LocalStreamVideoDataModal> _result = new ArrayList<LocalStreamVideoDataModal>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final LocalStreamVideoDataModal _item;
        _item = new LocalStreamVideoDataModal();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpUserid;
        _tmpUserid = _cursor.getString(_cursorIndexOfUserid);
        _item.setUserid(_tmpUserid);
        final String _tmpUsername;
        _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
        _item.setUsername(_tmpUsername);
        final String _tmpWList;
        _tmpWList = _cursor.getString(_cursorIndexOfWList);
        _item.setWList(_tmpWList);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<LocalStreamVideoDataModal> getAllWorkoutList(String name, String workoutId) {
    final String _sql = "SELECT * FROM LocalStreamVideoDataModal WHERE user_name LIKE ? AND user_id LIKE ? ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (name == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, name);
    }
    _argIndex = 2;
    if (workoutId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, workoutId);
    }
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfUserid = _cursor.getColumnIndexOrThrow("user_id");
      final int _cursorIndexOfUsername = _cursor.getColumnIndexOrThrow("user_name");
      final int _cursorIndexOfWList = _cursor.getColumnIndexOrThrow("w_list");
      final List<LocalStreamVideoDataModal> _result = new ArrayList<LocalStreamVideoDataModal>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final LocalStreamVideoDataModal _item;
        _item = new LocalStreamVideoDataModal();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpUserid;
        _tmpUserid = _cursor.getString(_cursorIndexOfUserid);
        _item.setUserid(_tmpUserid);
        final String _tmpUsername;
        _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
        _item.setUsername(_tmpUsername);
        final String _tmpWList;
        _tmpWList = _cursor.getString(_cursorIndexOfWList);
        _item.setWList(_tmpWList);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<LocalStream> getAll() {
    final String _sql = "SELECT * FROM LocalStream";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfUserid = _cursor.getColumnIndexOrThrow("user_id");
      final int _cursorIndexOfUsername = _cursor.getColumnIndexOrThrow("user_name");
      final int _cursorIndexOfWList = _cursor.getColumnIndexOrThrow("w_list");
      final List<LocalStream> _result = new ArrayList<LocalStream>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final LocalStream _item;
        _item = new LocalStream();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpUserid;
        _tmpUserid = _cursor.getString(_cursorIndexOfUserid);
        _item.setUserid(_tmpUserid);
        final String _tmpUsername;
        _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
        _item.setUsername(_tmpUsername);
        final String _tmpWList;
        _tmpWList = _cursor.getString(_cursorIndexOfWList);
        _item.setWList(_tmpWList);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<LocalStream> getAllList(String name, String UserId) {
    final String _sql = "SELECT * FROM LocalStream WHERE user_name LIKE ? AND user_id LIKE ? ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (name == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, name);
    }
    _argIndex = 2;
    if (UserId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, UserId);
    }
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfUserid = _cursor.getColumnIndexOrThrow("user_id");
      final int _cursorIndexOfUsername = _cursor.getColumnIndexOrThrow("user_name");
      final int _cursorIndexOfWList = _cursor.getColumnIndexOrThrow("w_list");
      final List<LocalStream> _result = new ArrayList<LocalStream>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final LocalStream _item;
        _item = new LocalStream();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpUserid;
        _tmpUserid = _cursor.getString(_cursorIndexOfUserid);
        _item.setUserid(_tmpUserid);
        final String _tmpUsername;
        _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
        _item.setUsername(_tmpUsername);
        final String _tmpWList;
        _tmpWList = _cursor.getString(_cursorIndexOfWList);
        _item.setWList(_tmpWList);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<LocalStreamVideoDataModal> getDownloadedList(String name, String UserId) {
    final String _sql = "SELECT * FROM LocalStreamVideoDataModal WHERE user_name LIKE ? AND user_id LIKE ? ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (name == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, name);
    }
    _argIndex = 2;
    if (UserId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, UserId);
    }
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfUserid = _cursor.getColumnIndexOrThrow("user_id");
      final int _cursorIndexOfUsername = _cursor.getColumnIndexOrThrow("user_name");
      final int _cursorIndexOfWList = _cursor.getColumnIndexOrThrow("w_list");
      final List<LocalStreamVideoDataModal> _result = new ArrayList<LocalStreamVideoDataModal>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final LocalStreamVideoDataModal _item;
        _item = new LocalStreamVideoDataModal();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpUserid;
        _tmpUserid = _cursor.getString(_cursorIndexOfUserid);
        _item.setUserid(_tmpUserid);
        final String _tmpUsername;
        _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
        _item.setUsername(_tmpUsername);
        final String _tmpWList;
        _tmpWList = _cursor.getString(_cursorIndexOfWList);
        _item.setWList(_tmpWList);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<MyVideoList> getAllVideo() {
    final String _sql = "SELECT * FROM video_list";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfStreamWorkoutId = _cursor.getColumnIndexOrThrow("stream_workout_id");
      final int _cursorIndexOfStreamVideoId = _cursor.getColumnIndexOrThrow("stream_video_id");
      final int _cursorIndexOfStreamVideoName = _cursor.getColumnIndexOrThrow("stream_video_name");
      final int _cursorIndexOfStreamVideoSubtitle = _cursor.getColumnIndexOrThrow("stream_video_subtitle");
      final int _cursorIndexOfStreamVideoDescription = _cursor.getColumnIndexOrThrow("stream_video_description");
      final int _cursorIndexOfStreamVideoImage = _cursor.getColumnIndexOrThrow("stream_video_image");
      final int _cursorIndexOfVideoDuration = _cursor.getColumnIndexOrThrow("video_duration");
      final int _cursorIndexOfStreamVideoImageUrl = _cursor.getColumnIndexOrThrow("stream_video_image_url");
      final int _cursorIndexOfStreamVideo = _cursor.getColumnIndexOrThrow("stream_video");
      final int _cursorIndexOfDownLoadUrl = _cursor.getColumnIndexOrThrow("down_load_url");
      final int _cursorIndexOfTimeStempUrl = _cursor.getColumnIndexOrThrow("time_stemp_url");
      final int _cursorIndexOfIsworkout = _cursor.getColumnIndexOrThrow("is_workout");
      final int _cursorIndexOfStreamVideoUrl = _cursor.getColumnIndexOrThrow("stream_video_url");
      final int _cursorIndexOfViewType = _cursor.getColumnIndexOrThrow("view_type");
      final List<MyVideoList> _result = new ArrayList<MyVideoList>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final MyVideoList _item;
        _item = new MyVideoList();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpStream_workout_id;
        _tmpStream_workout_id = _cursor.getString(_cursorIndexOfStreamWorkoutId);
        _item.setStream_workout_id(_tmpStream_workout_id);
        final String _tmpStream_video_id;
        _tmpStream_video_id = _cursor.getString(_cursorIndexOfStreamVideoId);
        _item.setStream_video_id(_tmpStream_video_id);
        final String _tmpStream_video_name;
        _tmpStream_video_name = _cursor.getString(_cursorIndexOfStreamVideoName);
        _item.setStream_video_name(_tmpStream_video_name);
        final String _tmpStream_video_subtitle;
        _tmpStream_video_subtitle = _cursor.getString(_cursorIndexOfStreamVideoSubtitle);
        _item.setStream_video_subtitle(_tmpStream_video_subtitle);
        final String _tmpStream_video_description;
        _tmpStream_video_description = _cursor.getString(_cursorIndexOfStreamVideoDescription);
        _item.setStream_video_description(_tmpStream_video_description);
        final String _tmpStream_video_image;
        _tmpStream_video_image = _cursor.getString(_cursorIndexOfStreamVideoImage);
        _item.setStream_video_image(_tmpStream_video_image);
        final String _tmpVideo_duration;
        _tmpVideo_duration = _cursor.getString(_cursorIndexOfVideoDuration);
        _item.setVideo_duration(_tmpVideo_duration);
        final String _tmpStream_video_image_url;
        _tmpStream_video_image_url = _cursor.getString(_cursorIndexOfStreamVideoImageUrl);
        _item.setStream_video_image_url(_tmpStream_video_image_url);
        final String _tmpStream_video;
        _tmpStream_video = _cursor.getString(_cursorIndexOfStreamVideo);
        _item.setStream_video(_tmpStream_video);
        final String _tmpDownLoad_url;
        _tmpDownLoad_url = _cursor.getString(_cursorIndexOfDownLoadUrl);
        _item.setDownLoad_url(_tmpDownLoad_url);
        final String _tmpTime_stemp_url;
        _tmpTime_stemp_url = _cursor.getString(_cursorIndexOfTimeStempUrl);
        _item.setTime_stemp_url(_tmpTime_stemp_url);
        final String _tmpIsworkout;
        _tmpIsworkout = _cursor.getString(_cursorIndexOfIsworkout);
        _item.setIsworkout(_tmpIsworkout);
        final String _tmpStream_video_url;
        _tmpStream_video_url = _cursor.getString(_cursorIndexOfStreamVideoUrl);
        _item.setStream_video_url(_tmpStream_video_url);
        final String _tmpView_type;
        _tmpView_type = _cursor.getString(_cursorIndexOfViewType);
        _item.setView_type(_tmpView_type);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<MyVideoList> getAllMyWorkOutListVideo(String workoutId) {
    final String _sql = "SELECT * FROM video_list WHERE stream_workout_id LIKE ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (workoutId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, workoutId);
    }
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfStreamWorkoutId = _cursor.getColumnIndexOrThrow("stream_workout_id");
      final int _cursorIndexOfStreamVideoId = _cursor.getColumnIndexOrThrow("stream_video_id");
      final int _cursorIndexOfStreamVideoName = _cursor.getColumnIndexOrThrow("stream_video_name");
      final int _cursorIndexOfStreamVideoSubtitle = _cursor.getColumnIndexOrThrow("stream_video_subtitle");
      final int _cursorIndexOfStreamVideoDescription = _cursor.getColumnIndexOrThrow("stream_video_description");
      final int _cursorIndexOfStreamVideoImage = _cursor.getColumnIndexOrThrow("stream_video_image");
      final int _cursorIndexOfVideoDuration = _cursor.getColumnIndexOrThrow("video_duration");
      final int _cursorIndexOfStreamVideoImageUrl = _cursor.getColumnIndexOrThrow("stream_video_image_url");
      final int _cursorIndexOfStreamVideo = _cursor.getColumnIndexOrThrow("stream_video");
      final int _cursorIndexOfDownLoadUrl = _cursor.getColumnIndexOrThrow("down_load_url");
      final int _cursorIndexOfTimeStempUrl = _cursor.getColumnIndexOrThrow("time_stemp_url");
      final int _cursorIndexOfIsworkout = _cursor.getColumnIndexOrThrow("is_workout");
      final int _cursorIndexOfStreamVideoUrl = _cursor.getColumnIndexOrThrow("stream_video_url");
      final int _cursorIndexOfViewType = _cursor.getColumnIndexOrThrow("view_type");
      final List<MyVideoList> _result = new ArrayList<MyVideoList>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final MyVideoList _item;
        _item = new MyVideoList();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpStream_workout_id;
        _tmpStream_workout_id = _cursor.getString(_cursorIndexOfStreamWorkoutId);
        _item.setStream_workout_id(_tmpStream_workout_id);
        final String _tmpStream_video_id;
        _tmpStream_video_id = _cursor.getString(_cursorIndexOfStreamVideoId);
        _item.setStream_video_id(_tmpStream_video_id);
        final String _tmpStream_video_name;
        _tmpStream_video_name = _cursor.getString(_cursorIndexOfStreamVideoName);
        _item.setStream_video_name(_tmpStream_video_name);
        final String _tmpStream_video_subtitle;
        _tmpStream_video_subtitle = _cursor.getString(_cursorIndexOfStreamVideoSubtitle);
        _item.setStream_video_subtitle(_tmpStream_video_subtitle);
        final String _tmpStream_video_description;
        _tmpStream_video_description = _cursor.getString(_cursorIndexOfStreamVideoDescription);
        _item.setStream_video_description(_tmpStream_video_description);
        final String _tmpStream_video_image;
        _tmpStream_video_image = _cursor.getString(_cursorIndexOfStreamVideoImage);
        _item.setStream_video_image(_tmpStream_video_image);
        final String _tmpVideo_duration;
        _tmpVideo_duration = _cursor.getString(_cursorIndexOfVideoDuration);
        _item.setVideo_duration(_tmpVideo_duration);
        final String _tmpStream_video_image_url;
        _tmpStream_video_image_url = _cursor.getString(_cursorIndexOfStreamVideoImageUrl);
        _item.setStream_video_image_url(_tmpStream_video_image_url);
        final String _tmpStream_video;
        _tmpStream_video = _cursor.getString(_cursorIndexOfStreamVideo);
        _item.setStream_video(_tmpStream_video);
        final String _tmpDownLoad_url;
        _tmpDownLoad_url = _cursor.getString(_cursorIndexOfDownLoadUrl);
        _item.setDownLoad_url(_tmpDownLoad_url);
        final String _tmpTime_stemp_url;
        _tmpTime_stemp_url = _cursor.getString(_cursorIndexOfTimeStempUrl);
        _item.setTime_stemp_url(_tmpTime_stemp_url);
        final String _tmpIsworkout;
        _tmpIsworkout = _cursor.getString(_cursorIndexOfIsworkout);
        _item.setIsworkout(_tmpIsworkout);
        final String _tmpStream_video_url;
        _tmpStream_video_url = _cursor.getString(_cursorIndexOfStreamVideoUrl);
        _item.setStream_video_url(_tmpStream_video_url);
        final String _tmpView_type;
        _tmpView_type = _cursor.getString(_cursorIndexOfViewType);
        _item.setView_type(_tmpView_type);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
