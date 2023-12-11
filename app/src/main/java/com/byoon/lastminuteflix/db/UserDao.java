package com.byoon.lastminuteflix.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.byoon.lastminuteflix.entity.User;

import java.util.List;

/**
 * Data access object for User entity. Used to query Room database for User data.
 * @author Brian Yoon
 * @since 2023-12-04
 */
@Dao
public interface UserDao {
  @Insert
  void insert(User... users);

  @Update
  void update(User... users);

  @Delete
  void delete(User... users);

  @Query("SELECT * FROM " + AppDatabase.USER_TABLE)
  List<User> getAllUsers();

  @Query("SELECT * FROM " + AppDatabase.USER_TABLE + " WHERE mUserId = :userId")
  User getUserById(int userId);

  @Query("SELECT * FROM " + AppDatabase.USER_TABLE + " WHERE mUsername = :username")
  User getUserByUsername(String username);
}
