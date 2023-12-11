package com.byoon.lastminuteflix.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.byoon.lastminuteflix.entity.User;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
  public static final String DB_NAME = "LASTMINUTEFLIX_DATABASE";
  public static final String USER_TABLE = "USER_TABLE";
  private static AppDatabase instance;

  public abstract UserDao getUserDao();

  // Ensures singleton design pattern.
  public static synchronized AppDatabase getInstance(Context context) {
    if (instance == null) {
      instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DB_NAME)
              .allowMainThreadQueries()
              .fallbackToDestructiveMigration()
              .build();
    }
    return instance;
  }
}
