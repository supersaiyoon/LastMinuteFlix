package com.byoon.lastminuteflix.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.byoon.lastminuteflix.db.AppDatabase;

@Entity(tableName = AppDatabase.USER_TABLE)
public class User {
  @PrimaryKey(autoGenerate = true)
  private int userId;

  private String username;
  private String password;
  private final boolean isAdmin;

  public User(String username, String password, boolean isAdmin) {
    this.username = username;
    this.password = password;
    this.isAdmin = isAdmin;
  }
}
