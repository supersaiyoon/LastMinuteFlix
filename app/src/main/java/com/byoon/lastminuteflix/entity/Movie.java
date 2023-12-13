package com.byoon.lastminuteflix.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.byoon.lastminuteflix.db.AppDatabase;

@Entity(tableName = AppDatabase.MOVIE_TABLE,
        foreignKeys = @ForeignKey(entity = Genre.class,
                                  parentColumns = "mGenreId",
                                  childColumns = "mGenreId",
                                  onDelete = ForeignKey.CASCADE),
        indices = {@Index(value = {"mGenreId"})})
public class Movie {
  @PrimaryKey(autoGenerate = true)
  private long mMovieId;

  // Foreign key
  private long mGenreId;

  private String mTitle;
  private int mDuration;  // In minutes
  private String mRating;

  public Movie(long genreId, String title, int duration, String rating) {
    mGenreId = genreId;
    mTitle = title;
    mDuration = duration;
    mRating = rating;
  }

  public long getMovieId() {
    return mMovieId;
  }

  public void setMovieId(long movieId) {
    mMovieId = movieId;
  }

  public long getGenreId() {
    return mGenreId;
  }

  public void setGenreId(long genreId) {
    mGenreId = genreId;
  }

  public String getTitle() {
    return mTitle;
  }

  public void setTitle(String title) {
    mTitle = title;
  }

  public int getDuration() {
    return mDuration;
  }

  public void setDuration(int duration) {
    mDuration = duration;
  }

  public String getRating() {
    return mRating;
  }

  public void setRating(String rating) {
    mRating = rating;
  }
}
