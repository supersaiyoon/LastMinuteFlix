package com.byoon.lastminuteflix.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.byoon.lastminuteflix.db.AppDatabase;

@Entity(tableName = AppDatabase.MOVIE_TABLE,
        foreignKeys = @ForeignKey(entity = Genre.class,
                                  parentColumns = "mGenreId",
                                  childColumns = "mGenreId",
                                  onDelete = ForeignKey.CASCADE)
)
public class Movie {
  @PrimaryKey(autoGenerate = true)
  private int mMovieId;

  // Foreign key
  private int mGenreId;

  private String mTitle;
  private int mDuration;  // In minutes
  private String mRating;

  public Movie(int genreId, String title, int duration, String rating) {
    mGenreId = genreId;
    mTitle = title;
    mDuration = duration;
    mRating = rating;
  }

  public int getMovieId() {
    return mMovieId;
  }

  public void setMovieId(int movieId) {
    mMovieId = movieId;
  }

  public int getGenreId() {
    return mGenreId;
  }

  public void setGenreId(int genreId) {
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
