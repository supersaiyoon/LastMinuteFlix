package com.byoon.lastminuteflix.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.byoon.lastminuteflix.db.AppDatabase;

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
}
