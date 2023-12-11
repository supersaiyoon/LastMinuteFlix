package com.byoon.lastminuteflix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.byoon.lastminuteflix.db.AppDatabase;
import com.byoon.lastminuteflix.db.UserDao;
import com.byoon.lastminuteflix.entity.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class UserTableInstrumentedTest {
  private AppDatabase appDatabase;
  private UserDao userDao;

  @Before
  public void setup() {
    Context context = ApplicationProvider.getApplicationContext();
    // Don't mess with real database.
    appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
    userDao = appDatabase.getUserDao();
  }

  @After
  public void cleanup() {
    appDatabase.close();
  }

  @Test
  public void givenUser_whenInserted_thenUserIsRetrievable() {
    // Given
    String username = "testuser";
    String password = "password123";
    boolean isAdmin = false;

    User user = new User(username, password, isAdmin);
    userDao.insert(user);

    // When
    User retrievedUser = userDao.getUserByUsername(username);

    // Then
    assertNotNull(retrievedUser);
    assertEquals(username, retrievedUser.getUsername());
    assertEquals(password, retrievedUser.getPassword());
    assertEquals(isAdmin, retrievedUser.isAdmin());
  }

  @Test
  public void givenUser_whenUpdated_thenPasswordIsUpdated() {
    // Given
    String username = "testuser";
    String initialPassword = "password123";
    String updatedPassword = "newpassword";
    boolean isAdmin = false;

    User user = new User(username, initialPassword, isAdmin);
    userDao.insert(user);

    // When
    int userId = userDao.getUserByUsername(username).getUserId();
    user.setUserId(userId);

    user.setPassword(updatedPassword);
    userDao.update(user);

    // Then
    User retrievedUser = userDao.getUserById(user.getUserId());
    assertNotNull(retrievedUser);
    assertEquals(updatedPassword, retrievedUser.getPassword());
  }

  @Test
  public void givenUser_whenDeleted_thenUserIsNotRetrievable() {
    // Given
    String username = "testuser";
    String password = "password123";
    boolean isAdmin = false;

    User user = new User(username, password, isAdmin);
    userDao.insert(user);

    // When
    userDao.delete(user);

    // Then
    User retrievedUser = userDao.getUserById(user.getUserId());
    assertNull(retrievedUser);
  }

  @Test
  public void givenMultipleUsers_whenGetAllUsers_thenCorrectNumberOfUsersRetrieved() {
    // Given
    User user1 = new User("user1", "password1", false);
    User user2 = new User("user2", "password2", true);
    userDao.insert(user1, user2);

    // When
    List<User> allUsers = userDao.getAllUsers();

    // Then
    int expectedNumberOfUsers = 2;
    assertEquals(expectedNumberOfUsers, allUsers.size());
  }
}
