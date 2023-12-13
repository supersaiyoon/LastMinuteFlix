package com.byoon.lastminuteflix.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.byoon.lastminuteflix.entity.Theater;

/**
 * Data access object for Theater entity. Used to display movie showtimes for users to purchase.
 * @author Brian Yoon
 * @since 2023-12-12
 */
@Dao
public interface TheaterDao {
  @Insert
  long insert(Theater theater);

  @Update
  void update(Theater... theaters);

  @Delete
  void delete(Theater... theaters);

  @Query("SELECT * FROM " + AppDatabase.THEATER_TABLE + " WHERE mTheaterId = :theaterId")
  Theater getTheaterById(long theaterId);

  @Query("SELECT * FROM " + AppDatabase.THEATER_TABLE + " WHERE mTheaterName = :theaterName")
  Theater getTheaterByName(String theaterName);
}
