package com.byoon.lastminuteflix.db;

import androidx.room.RoomDatabase;

public abstract class AppDatabase extends RoomDatabase {
  public static final String DB_NAME = "LASTMINUTEFLIX_DATABASE";
  public static final String USER_TABLE = "USER_TABLE";

  public abstract UserDAO getUserDAO();
}
