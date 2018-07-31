package com.michael.Com6510.photogallary.database;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityDeletionOrUpdateAdapter;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.database.Cursor;
import com.michael.Com6510.photogallary.database.entity.Image;
import com.michael.Com6510.photogallary.database.entity.Location;
import java.lang.Override;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;

public class ImageDao_Impl implements ImageDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfImage;

  private final EntityInsertionAdapter __insertionAdapterOfLocation;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfImage;

  private final EntityDeletionOrUpdateAdapter __updateAdapterOfImage;

  public ImageDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfImage = new EntityInsertionAdapter<Image>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `Image`(`id`,`image`,`title`,`description`,`date`,`city`,`country`,`location_id`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Image value) {
        stmt.bindLong(1, value.getId());
        if (value.getImage() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getImage());
        }
        if (value.getTitle() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getTitle());
        }
        if (value.getDescription() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getDescription());
        }
        if (value.getDate() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getDate());
        }
        if (value.getCity() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getCity());
        }
        if (value.getCountry() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getCountry());
        }
        stmt.bindLong(8, value.getLocationId());
      }
    };
    this.__insertionAdapterOfLocation = new EntityInsertionAdapter<Location>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `Location`(`id`,`latitude`,`longitude`,`accuracy`) VALUES (?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Location value) {
        stmt.bindLong(1, value.id);
        stmt.bindDouble(2, value.latitude);
        stmt.bindDouble(3, value.longitude);
        stmt.bindDouble(4, value.accuracy);
      }
    };
    this.__deletionAdapterOfImage = new EntityDeletionOrUpdateAdapter<Image>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `Image` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Image value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfImage = new EntityDeletionOrUpdateAdapter<Image>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `Image` SET `id` = ?,`image` = ?,`title` = ?,`description` = ?,`date` = ?,`city` = ?,`country` = ?,`location_id` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Image value) {
        stmt.bindLong(1, value.getId());
        if (value.getImage() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getImage());
        }
        if (value.getTitle() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getTitle());
        }
        if (value.getDescription() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getDescription());
        }
        if (value.getDate() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getDate());
        }
        if (value.getCity() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getCity());
        }
        if (value.getCountry() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getCountry());
        }
        stmt.bindLong(8, value.getLocationId());
        stmt.bindLong(9, value.getId());
      }
    };
  }

  @Override
  public void insertImage(Image image) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfImage.insert(image);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertLocation(Location location) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfLocation.insert(location);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteImage(Image image) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfImage.handle(image);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(Image image) {
    __db.beginTransaction();
    try {
      __updateAdapterOfImage.handle(image);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Image> findImageByArea(double latitude, double longitude, int radius) {
    final String _sql = "SELECT * FROM Image INNER JOIN  LOCATION ON location.id = Image.location_id  WHERE location.latitude <= (? + ?) AND location.latitude >= (? - ?) AND location.longitude <= (? + ?) AND location.longitude >= (? - ?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 8);
    int _argIndex = 1;
    _statement.bindDouble(_argIndex, latitude);
    _argIndex = 2;
    _statement.bindLong(_argIndex, radius);
    _argIndex = 3;
    _statement.bindDouble(_argIndex, latitude);
    _argIndex = 4;
    _statement.bindLong(_argIndex, radius);
    _argIndex = 5;
    _statement.bindDouble(_argIndex, longitude);
    _argIndex = 6;
    _statement.bindLong(_argIndex, radius);
    _argIndex = 7;
    _statement.bindDouble(_argIndex, longitude);
    _argIndex = 8;
    _statement.bindLong(_argIndex, radius);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfImage = _cursor.getColumnIndexOrThrow("image");
      final int _cursorIndexOfTitle = _cursor.getColumnIndexOrThrow("title");
      final int _cursorIndexOfDescription = _cursor.getColumnIndexOrThrow("description");
      final int _cursorIndexOfDate = _cursor.getColumnIndexOrThrow("date");
      final int _cursorIndexOfCity = _cursor.getColumnIndexOrThrow("city");
      final int _cursorIndexOfCountry = _cursor.getColumnIndexOrThrow("country");
      final int _cursorIndexOfLocationId = _cursor.getColumnIndexOrThrow("location_id");
      final int _cursorIndexOfId_1 = _cursor.getColumnIndexOrThrow("id");
      final List<Image> _result = new ArrayList<Image>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Image _item;
        final String _tmpImage;
        _tmpImage = _cursor.getString(_cursorIndexOfImage);
        _item = new Image(_tmpImage);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpTitle;
        _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        _item.setTitle(_tmpTitle);
        final String _tmpDescription;
        _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        _item.setDescription(_tmpDescription);
        final String _tmpDate;
        _tmpDate = _cursor.getString(_cursorIndexOfDate);
        _item.setDate(_tmpDate);
        final String _tmpCity;
        _tmpCity = _cursor.getString(_cursorIndexOfCity);
        _item.setCity(_tmpCity);
        final String _tmpCountry;
        _tmpCountry = _cursor.getString(_cursorIndexOfCountry);
        _item.setCountry(_tmpCountry);
        final int _tmpLocationId;
        _tmpLocationId = _cursor.getInt(_cursorIndexOfLocationId);
        _item.setLocationId(_tmpLocationId);
        final int _tmpId_1;
        _tmpId_1 = _cursor.getInt(_cursorIndexOfId_1);
        _item.setId(_tmpId_1);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Image> findImageByTitle(String title) {
    final String _sql = "SELECT * FROM Image WHERE title = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (title == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, title);
    }
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfImage = _cursor.getColumnIndexOrThrow("image");
      final int _cursorIndexOfTitle = _cursor.getColumnIndexOrThrow("title");
      final int _cursorIndexOfDescription = _cursor.getColumnIndexOrThrow("description");
      final int _cursorIndexOfDate = _cursor.getColumnIndexOrThrow("date");
      final int _cursorIndexOfCity = _cursor.getColumnIndexOrThrow("city");
      final int _cursorIndexOfCountry = _cursor.getColumnIndexOrThrow("country");
      final int _cursorIndexOfLocationId = _cursor.getColumnIndexOrThrow("location_id");
      final List<Image> _result = new ArrayList<Image>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Image _item;
        final String _tmpImage;
        _tmpImage = _cursor.getString(_cursorIndexOfImage);
        _item = new Image(_tmpImage);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpTitle;
        _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        _item.setTitle(_tmpTitle);
        final String _tmpDescription;
        _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        _item.setDescription(_tmpDescription);
        final String _tmpDate;
        _tmpDate = _cursor.getString(_cursorIndexOfDate);
        _item.setDate(_tmpDate);
        final String _tmpCity;
        _tmpCity = _cursor.getString(_cursorIndexOfCity);
        _item.setCity(_tmpCity);
        final String _tmpCountry;
        _tmpCountry = _cursor.getString(_cursorIndexOfCountry);
        _item.setCountry(_tmpCountry);
        final int _tmpLocationId;
        _tmpLocationId = _cursor.getInt(_cursorIndexOfLocationId);
        _item.setLocationId(_tmpLocationId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Image> getAllImages() {
    final String _sql = "SELECT * FROM Image";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfImage = _cursor.getColumnIndexOrThrow("image");
      final int _cursorIndexOfTitle = _cursor.getColumnIndexOrThrow("title");
      final int _cursorIndexOfDescription = _cursor.getColumnIndexOrThrow("description");
      final int _cursorIndexOfDate = _cursor.getColumnIndexOrThrow("date");
      final int _cursorIndexOfCity = _cursor.getColumnIndexOrThrow("city");
      final int _cursorIndexOfCountry = _cursor.getColumnIndexOrThrow("country");
      final int _cursorIndexOfLocationId = _cursor.getColumnIndexOrThrow("location_id");
      final List<Image> _result = new ArrayList<Image>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Image _item;
        final String _tmpImage;
        _tmpImage = _cursor.getString(_cursorIndexOfImage);
        _item = new Image(_tmpImage);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpTitle;
        _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        _item.setTitle(_tmpTitle);
        final String _tmpDescription;
        _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        _item.setDescription(_tmpDescription);
        final String _tmpDate;
        _tmpDate = _cursor.getString(_cursorIndexOfDate);
        _item.setDate(_tmpDate);
        final String _tmpCity;
        _tmpCity = _cursor.getString(_cursorIndexOfCity);
        _item.setCity(_tmpCity);
        final String _tmpCountry;
        _tmpCountry = _cursor.getString(_cursorIndexOfCountry);
        _item.setCountry(_tmpCountry);
        final int _tmpLocationId;
        _tmpLocationId = _cursor.getInt(_cursorIndexOfLocationId);
        _item.setLocationId(_tmpLocationId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Image> getImage(String image) {
    final String _sql = "SELECT * FROM Image WHERE image = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (image == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, image);
    }
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfImage = _cursor.getColumnIndexOrThrow("image");
      final int _cursorIndexOfTitle = _cursor.getColumnIndexOrThrow("title");
      final int _cursorIndexOfDescription = _cursor.getColumnIndexOrThrow("description");
      final int _cursorIndexOfDate = _cursor.getColumnIndexOrThrow("date");
      final int _cursorIndexOfCity = _cursor.getColumnIndexOrThrow("city");
      final int _cursorIndexOfCountry = _cursor.getColumnIndexOrThrow("country");
      final int _cursorIndexOfLocationId = _cursor.getColumnIndexOrThrow("location_id");
      final List<Image> _result = new ArrayList<Image>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Image _item;
        final String _tmpImage;
        _tmpImage = _cursor.getString(_cursorIndexOfImage);
        _item = new Image(_tmpImage);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpTitle;
        _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        _item.setTitle(_tmpTitle);
        final String _tmpDescription;
        _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        _item.setDescription(_tmpDescription);
        final String _tmpDate;
        _tmpDate = _cursor.getString(_cursorIndexOfDate);
        _item.setDate(_tmpDate);
        final String _tmpCity;
        _tmpCity = _cursor.getString(_cursorIndexOfCity);
        _item.setCity(_tmpCity);
        final String _tmpCountry;
        _tmpCountry = _cursor.getString(_cursorIndexOfCountry);
        _item.setCountry(_tmpCountry);
        final int _tmpLocationId;
        _tmpLocationId = _cursor.getInt(_cursorIndexOfLocationId);
        _item.setLocationId(_tmpLocationId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
