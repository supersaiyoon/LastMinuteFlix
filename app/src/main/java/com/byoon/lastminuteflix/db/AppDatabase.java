package com.byoon.lastminuteflix.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.byoon.lastminuteflix.entity.Genre;
import com.byoon.lastminuteflix.entity.Movie;
import com.byoon.lastminuteflix.entity.User;

@Database(entities = {User.class, Genre.class, Movie.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
  public static final String DB_NAME = "LASTMINUTEFLIX_DATABASE";
  public static final String USER_TABLE = "USER_TABLE";
  public static final String GENRE_TABLE = "GENRE_TABLE";
  public static final String MOVIE_TABLE = "MOVIE_TABLE";
  private static AppDatabase instance;

  public abstract UserDao getUserDao();
  public abstract GenreDao getGenreDao();
  public abstract MovieDao getMovieDao();

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
