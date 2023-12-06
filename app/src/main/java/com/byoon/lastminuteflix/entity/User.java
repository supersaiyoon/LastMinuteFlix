package com.byoon.lastminuteflix.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.byoon.lastminuteflix.db.AppDatabase;

/**
 * User entity class for Room database. Represents user of app.
 * @author Brian Yoon
 * @since 2023-12-04
 */
@Entity(tableName = AppDatabase.USER_TABLE)
public class User {
  @PrimaryKey(autoGenerate = true)
  private int mUserId;

  private String mUsername;
  private String mPassword;
  private final boolean mIsAdmin;

  public User(String username, String password, boolean isAdmin) {
    this.mUsername = username;
    this.mPassword = password;
    this.mIsAdmin = isAdmin;
  }

  public int getUserId() {
    return mUserId;
  }

  public void setUserId(int userId) {
    mUserId = userId;
  }

  public String getUsername() {
    return mUsername;
  }

  public void setUsername(String username) {
    mUsername = username;
  }

  public String getPassword() {
    return mPassword;
  }

  public void setPassword(String password) {
    mPassword = password;
  }

  public boolean isAdmin() {
    return mIsAdmin;
  }
}
