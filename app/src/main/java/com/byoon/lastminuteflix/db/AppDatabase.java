package com.byoon.lastminuteflix.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.byoon.lastminuteflix.entity.User;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
  public static final String DB_NAME = "LASTMINUTEFLIX_DATABASE";
  public static final String USER_TABLE = "USER_TABLE";

  public abstract UserDAO getUserDAO();
}
