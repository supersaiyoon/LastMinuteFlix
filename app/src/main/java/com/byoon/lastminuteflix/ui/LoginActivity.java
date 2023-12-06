package com.byoon.lastminuteflix.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.byoon.lastminuteflix.R;
import com.byoon.lastminuteflix.db.AppDatabase;
import com.byoon.lastminuteflix.db.UserDAO;
import com.byoon.lastminuteflix.entity.User;
import com.byoon.lastminuteflix.utils.IntentFactory;

/**
 * First activity user sees when app is opened.
 * @author Brian Yoon
 * @since 2023-12-04
 */
public class LoginActivity extends AppCompatActivity {
  private EditText mUsername;
  private EditText mPassword;
  private Button mLogInButton;
  private UserDAO mUserDAO;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    wireUpDisplay();

    getDatabase();
  }

  private void wireUpDisplay() {
    mUsername = findViewById(R.id.username_input);
    mPassword = findViewById(R.id.password_input);
    mLogInButton = findViewById(R.id.login_button);

    // Press login button to go to main activity.
    mLogInButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (isSuccessfulLogin()) {
          String usernameString = mUsername.getText().toString();
          User user = mUserDAO.getUserByUsername(usernameString);
          int userId = user.getUserId();
          Intent intent = IntentFactory.createMainActivityIntent(LoginActivity.this, userId);
          startActivity(intent);
        }
        else {
          // Invalid username and/or password.
          Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
      }
    });
  }

  private void getDatabase() {
    mUserDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
        .allowMainThreadQueries()
        .build()
        .getUserDAO();

    // Insert predefined users per project instructions.
    insertPredefinedUsers();
  }

  private void insertPredefinedUsers() {
    String testUsername = "testuser1";
    String testPassword = "testuser1";
    boolean testIsAdmin = false;

    String adminUsername = "admin2";
    String adminPassword = "admin2";
    boolean adminIsAdmin = true;

    if (!userExists(testUsername)){
      mUserDAO.insert(new User(testUsername, testPassword, testIsAdmin));
    }

    if (!userExists(adminUsername)){
      mUserDAO.insert(new User(adminUsername, adminPassword, adminIsAdmin));
    }
  }

  private boolean userExists(String username) {
    return mUserDAO.getUserByUsername(username) != null;
  }

  boolean isSuccessfulLogin() {
    // Username and password given by user input.
    String username = mUsername.getText().toString();
    String password = mPassword.getText().toString();

    User user = mUserDAO.getUserByUsername(username);

    // Check if user exists and password is correct.
    return user != null && user.getPassword().equals(password);
  }
}
