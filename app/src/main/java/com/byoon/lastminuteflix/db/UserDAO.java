package com.byoon.lastminuteflix.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.byoon.lastminuteflix.entity.User;

@Dao
public interface UserDAO {
  @Insert
  void insert(User... users);

  @Update
  void update(User... users);

  @Delete
  void delete(User... users);
}
