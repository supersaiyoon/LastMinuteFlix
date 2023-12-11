package com.byoon.lastminuteflix.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.byoon.lastminuteflix.R;
import com.byoon.lastminuteflix.db.AppDatabase;
import com.byoon.lastminuteflix.db.UserDao;
import com.byoon.lastminuteflix.entity.User;
import com.byoon.lastminuteflix.utils.IntentFactory;

/**
 * First activity user sees when app is opened.
 * @author Brian Yoon
 * @since 2023-12-04
 */
public class LoginActivity extends AppCompatActivity {
  private EditText mUsernameEditText;
  private EditText mPasswordEditText;
  private UserDao mUserDao;

  private static final String TEST_USERNAME = "testuser1";
  private static final String TEST_PASSWORD = "testuser1";
  private static final boolean TEST_IS_ADMIN = false;

  private static final String ADMIN_USERNAME = "admin2";
  private static final String ADMIN_PASSWORD = "admin2";
  private static final boolean ADMIN_IS_ADMIN = true;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    // Wire up views and database.
    initializeViews();

    initializeDatabase();

    // Insert predefined users per project instructions.
    insertPredefinedUsers();
  }

  private void initializeViews() {
    mUsernameEditText = findViewById(R.id.edittext_username);
    mPasswordEditText = findViewById(R.id.edittext_password);
    Button logInButton = findViewById(R.id.button_log_in);

    // Press `Log In` button to go to main activity.
    logInButton.setOnClickListener(v -> handleLogInButtonPress());
  }

  private void handleLogInButtonPress() {
    String mUsernameString = mUsernameEditText.getText().toString();
    String mPasswordString = mPasswordEditText.getText().toString();

    if (mUsernameString.isEmpty() || mPasswordString.isEmpty()) {
      Toast.makeText(LoginActivity.this, "Username and password cannot be empty", Toast.LENGTH_SHORT).show();
      return;
    }

    User user = mUserDao.getUserByUsername(mUsernameString);

    if (isSuccessfulLogin(user, mPasswordString)) {
      int userId = user.getUserId();
      Intent intent = IntentFactory.createMainActivityIntent(LoginActivity.this, userId);
      startActivity(intent);
    }
    else {
      Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
    }
  }

  private void initializeDatabase() {
    AppDatabase appDatabase = AppDatabase.getInstance(this);
    mUserDao = appDatabase.getUserDao();
  }

  private void insertPredefinedUsers() {
    if (userDoesNotExist(TEST_USERNAME)){
      mUserDao.insert(new User(TEST_USERNAME, TEST_PASSWORD, TEST_IS_ADMIN));
    }

    if (userDoesNotExist(ADMIN_USERNAME)){
      mUserDao.insert(new User(ADMIN_USERNAME, ADMIN_PASSWORD, ADMIN_IS_ADMIN));
    }
  }

  private boolean userDoesNotExist(String username) {
    return mUserDao.getUserByUsername(username) == null;
  }

  private boolean isSuccessfulLogin(User user, String password) {
    // Check if user exists and password is correct.
    return user != null && user.getPassword().equals(password);
  }

  public UserDao getUserDao() {
    return mUserDao;
  }
}
