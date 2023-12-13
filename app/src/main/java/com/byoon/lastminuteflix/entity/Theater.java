package com.byoon.lastminuteflix.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.byoon.lastminuteflix.db.AppDatabase;

@Entity(tableName = AppDatabase.THEATER_TABLE,
        foreignKeys = @ForeignKey(entity = Movie.class,
                parentColumns = "mMovieId",
                childColumns = "mMovieId",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index(value = {"mMovieId"})})
public class Theater {
  @PrimaryKey(autoGenerate = true)
  private long mTheaterId;

  // Foreign key
  private long mMovieId;

  private String mTheaterName;
  private String mTheaterCityState;  // e.g. "Sacramento, CA"
  private String mShowTime;  // e.g. "7:30 PM"
  private int mRemainingSeats;

  public Theater(long movieId, String theaterName, String theaterCityState, String showTime, int remainingSeats) {
    mMovieId = movieId;
    mTheaterName = theaterName;
    mTheaterCityState = theaterCityState;
    mShowTime = showTime;
    mRemainingSeats = remainingSeats;
  }

  public long getTheaterId() {
    return mTheaterId;
  }

  public void setTheaterId(long theaterId) {
    mTheaterId = theaterId;
  }

  public long getMovieId() {
    return mMovieId;
  }

  public void setMovieId(long movieId) {
    mMovieId = movieId;
  }

  public String getTheaterName() {
    return mTheaterName;
  }

  public void setTheaterName(String theaterName) {
    mTheaterName = theaterName;
  }

  public String getTheaterCityState() {
    return mTheaterCityState;
  }

  public void setTheaterCityState(String theaterCityState) {
    mTheaterCityState = theaterCityState;
  }

  public String getShowTime() {
    return mShowTime;
  }

  public void setShowTime(String showTime) {
    mShowTime = showTime;
  }

  public int getRemainingSeats() {
    return mRemainingSeats;
  }

  public void setRemainingSeats(int remainingSeats) {
    this.mRemainingSeats = remainingSeats;
  }

  public void decrementRemainingSeats() {
    mRemainingSeats--;
  }
}
