package com.byoon.lastminuteflix.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.byoon.lastminuteflix.db.AppDatabase;

/**
 * Movie genre entity class for Room database. Represents possible movie genres.
 * @author Brian Yoon
 * @since 2023-12-11
 */
@Entity(tableName = AppDatabase.GENRE_TABLE)
public class Genre {
  @PrimaryKey(autoGenerate = true)
  private long mGenreId;
  private String mGenreName;

  public Genre(String genreName) {
    mGenreName = genreName;
  }

  public long getGenreId() {
    return mGenreId;
  }

  public void setGenreId(long genreId) {
    mGenreId = genreId;
  }

  public String getGenreName() {
    return mGenreName;
  }

  public void setGenreName(String genreName) {
    mGenreName = genreName;
  }
}
