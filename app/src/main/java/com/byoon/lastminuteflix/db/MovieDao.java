package com.byoon.lastminuteflix.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
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
  void insert(Movie... movies);

  @Update
  void update(Movie... movies);

  @Delete
  void delete(Movie... movies);
}
