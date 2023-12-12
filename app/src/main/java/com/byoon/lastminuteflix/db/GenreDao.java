package com.byoon.lastminuteflix.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.byoon.lastminuteflix.entity.Genre;

import java.util.List;

/**
 * Data access object for Genre entity. Used to query Room database for movie genre data.
 * @author Brian Yoon
 * @since 2023-12-11
 */
@Dao
public interface GenreDao {
  @Insert
  void insert(Genre... genres);

  @Update
  void update(Genre... genres);

  @Delete
  void delete(Genre... genres);

  @Query("SELECT * FROM " + AppDatabase.GENRE_TABLE)
  List<Genre> getAllGenres();

  @Query("SELECT * FROM " + AppDatabase.GENRE_TABLE + " WHERE mGenreId = :genreId")
  Genre getGenreById(int genreId);
}