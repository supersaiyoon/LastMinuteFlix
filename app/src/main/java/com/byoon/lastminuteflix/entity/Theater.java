package com.byoon.lastminuteflix.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.byoon.lastminuteflix.db.AppDatabase;

@Entity(tableName = AppDatabase.THEATER_TABLE,
        foreignKeys = @ForeignKey(entity = Movie.class,
                parentColumns = "mMovieId",
                childColumns = "mMovieId",
                onDelete = ForeignKey.CASCADE)
)
public class Theater {
  @PrimaryKey(autoGenerate = true)
  private int mTheaterId;

  // Foreign key
  private int mMovieId;

  private String mTheaterName;
  private String mTheaterCityState;  // e.g. "Sacramento, CA"
  private String mShowTime;  // e.g. "7:30 PM"
  private int nRemainingSeats;

  public Theater(int movieId, String theaterName, String theaterCityState, String showTime, int remainingSeats) {
    mMovieId = movieId;
    mTheaterName = theaterName;
    mTheaterCityState = theaterCityState;
    mShowTime = showTime;
    nRemainingSeats = remainingSeats;
  }

  public int getTheaterId() {
    return mTheaterId;
  }

  public void setTheaterId(int theaterId) {
    mTheaterId = theaterId;
  }

  public int getMovieId() {
    return mMovieId;
  }

  public void setMovieId(int movieId) {
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
    return nRemainingSeats;
  }

  public void setRemainingSeats(int nRemainingSeats) {
    this.nRemainingSeats = nRemainingSeats;
  }

  public void decrementRemainingSeats() {
    nRemainingSeats--;
  }
}
