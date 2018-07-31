package com.michael.Com6510.photogallary.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Callback;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Configuration;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomOpenHelper;
import android.arch.persistence.room.RoomOpenHelper.Delegate;
import android.arch.persistence.room.util.TableInfo;
import android.arch.persistence.room.util.TableInfo.Column;
import android.arch.persistence.room.util.TableInfo.ForeignKey;
import android.arch.persistence.room.util.TableInfo.Index;
import java.lang.IllegalStateException;
import java.lang.Override;
import java.lang.String;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class AppDataBase_Impl extends AppDataBase {
  private volatile ImageDao _imageDao;

  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(2) {
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `Image` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `image` TEXT, `title` TEXT, `description` TEXT, `date` TEXT, `city` TEXT, `country` TEXT, `location_id` INTEGER NOT NULL, FOREIGN KEY(`location_id`) REFERENCES `Location`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `Location` (`id` INTEGER NOT NULL, `latitude` REAL NOT NULL, `longitude` REAL NOT NULL, `accuracy` REAL NOT NULL, PRIMARY KEY(`id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"2ec1e5161e3fe6ab07147eae644ce729\")");
      }

      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `Image`");
        _db.execSQL("DROP TABLE IF EXISTS `Location`");
      }

      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        _db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      protected void validateMigration(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsImage = new HashMap<String, TableInfo.Column>(8);
        _columnsImage.put("id", new TableInfo.Column("id", "INTEGER", true, 1));
        _columnsImage.put("image", new TableInfo.Column("image", "TEXT", false, 0));
        _columnsImage.put("title", new TableInfo.Column("title", "TEXT", false, 0));
        _columnsImage.put("description", new TableInfo.Column("description", "TEXT", false, 0));
        _columnsImage.put("date", new TableInfo.Column("date", "TEXT", false, 0));
        _columnsImage.put("city", new TableInfo.Column("city", "TEXT", false, 0));
        _columnsImage.put("country", new TableInfo.Column("country", "TEXT", false, 0));
        _columnsImage.put("location_id", new TableInfo.Column("location_id", "INTEGER", true, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysImage = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysImage.add(new TableInfo.ForeignKey("Location", "CASCADE", "NO ACTION",Arrays.asList("location_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesImage = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoImage = new TableInfo("Image", _columnsImage, _foreignKeysImage, _indicesImage);
        final TableInfo _existingImage = TableInfo.read(_db, "Image");
        if (! _infoImage.equals(_existingImage)) {
          throw new IllegalStateException("Migration didn't properly handle Image(com.michael.Com6510.photogallary.database.entity.Image).\n"
                  + " Expected:\n" + _infoImage + "\n"
                  + " Found:\n" + _existingImage);
        }
        final HashMap<String, TableInfo.Column> _columnsLocation = new HashMap<String, TableInfo.Column>(4);
        _columnsLocation.put("id", new TableInfo.Column("id", "INTEGER", true, 1));
        _columnsLocation.put("latitude", new TableInfo.Column("latitude", "REAL", true, 0));
        _columnsLocation.put("longitude", new TableInfo.Column("longitude", "REAL", true, 0));
        _columnsLocation.put("accuracy", new TableInfo.Column("accuracy", "REAL", true, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysLocation = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesLocation = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoLocation = new TableInfo("Location", _columnsLocation, _foreignKeysLocation, _indicesLocation);
        final TableInfo _existingLocation = TableInfo.read(_db, "Location");
        if (! _infoLocation.equals(_existingLocation)) {
          throw new IllegalStateException("Migration didn't properly handle Location(com.michael.Com6510.photogallary.database.entity.Location).\n"
                  + " Expected:\n" + _infoLocation + "\n"
                  + " Found:\n" + _existingLocation);
        }
      }
    }, "2ec1e5161e3fe6ab07147eae644ce729");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    return new InvalidationTracker(this, "Image","Location");
  }

  @Override
  public ImageDao imageDao() {
    if (_imageDao != null) {
      return _imageDao;
    } else {
      synchronized(this) {
        if(_imageDao == null) {
          _imageDao = new ImageDao_Impl(this);
        }
        return _imageDao;
      }
    }
  }
}
