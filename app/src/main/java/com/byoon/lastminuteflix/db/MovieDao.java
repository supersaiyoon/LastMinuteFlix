package com.byoon.lastminuteflix.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.byoon.lastminuteflix.entity.Movie;

/**
 * Data access object for Movie entity. Used to query Room database for movie data.
 * @author Brian Yoon
 * @since 2023-12-11
 */
@Dao
public interface MovieDao {
  @Insert
  long insert(Movie movie);

  @Update
  void update(Movie... movies);

  @Delete
  void delete(Movie... movies);

  @Query("SELECT * FROM " + AppDatabase.MOVIE_TABLE + " WHERE mMovieId = :movieId")
  Movie getMovieById(long movieId);

  @Query("SELECT * FROM " + AppDatabase.MOVIE_TABLE + " WHERE mTitle = :title")
  Movie getMovieByTitle(String title);
}
