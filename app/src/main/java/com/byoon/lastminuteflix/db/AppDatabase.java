package com.byoon.lastminuteflix.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.byoon.lastminuteflix.entity.Genre;
import com.byoon.lastminuteflix.entity.Movie;
import com.byoon.lastminuteflix.entity.OrderHistory;
import com.byoon.lastminuteflix.entity.Theater;
import com.byoon.lastminuteflix.entity.User;

@Database(entities = {User.class, Genre.class, Movie.class, Theater.class, OrderHistory.class}, version = 6)
public abstract class AppDatabase extends RoomDatabase {
  public static final String DB_NAME = "LASTMINUTEFLIX_DATABASE";
  public static final String USER_TABLE = "USER_TABLE";
  public static final String GENRE_TABLE = "GENRE_TABLE";
  public static final String MOVIE_TABLE = "MOVIE_TABLE";
  public static final String THEATER_TABLE = "THEATER_TABLE";
  public static final String ORDER_HISTORY_TABLE = "ORDER_HISTORY_TABLE";
  private static AppDatabase instance;

  public abstract UserDao getUserDao();
  public abstract GenreDao getGenreDao();
  public abstract MovieDao getMovieDao();
  public abstract TheaterDao getTheaterDao();
  public abstract OrderHistoryDao getOrderHistoryDao();

  // Ensure singleton design pattern.
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
