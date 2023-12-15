package com.byoon.lastminuteflix.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.byoon.lastminuteflix.R;
import com.byoon.lastminuteflix.db.AppDatabase;
import com.byoon.lastminuteflix.db.UserDao;
import com.byoon.lastminuteflix.entity.User;
import com.byoon.lastminuteflix.utils.IntentFactory;

/**
 * Where user creates new accounts.
 *
 * @author Brian Yoon
 * @since 2023-12-11
 */
public class CreateAccountActivity extends AppCompatActivity {
  EditText mNewUsernameEditText;
  EditText mNewPasswordEditText;
  EditText mNewPasswordConfirmEditText;
  Button mCreateAccountButton;
  UserDao mUserDao;

  private static final boolean NEW_ACCOUNT_IS_NOT_ADMIN = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_account);

    // Wire up views and database.
    initializeViews();

    // Need to insert new user account info into database.
    initializeDatabase();
    System.out.println(mUserDao.getAllUsers());

    // Press `Create Account` button to create new account.
    mCreateAccountButton.setOnClickListener(v -> handleCreateAccountButtonPress());
  }

  private void handleCreateAccountButtonPress() {
    String newUsernameString = mNewUsernameEditText.getText().toString();
    String newPasswordString = mNewPasswordEditText.getText().toString();
    String newPasswordConfirmString = mNewPasswordConfirmEditText.getText().toString();

    // Check if username already exists.
    if (mUserDao.getUserByUsername(newUsernameString) != null) {
      Toast.makeText(this, "Username already exists.", Toast.LENGTH_SHORT).show();
      return;
    }

    // Check if password and password confirm match.
    if (!newPasswordString.equals(newPasswordConfirmString)) {
      Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
      return;
    }

    // Check if username and password are empty.
    if (newUsernameString.isEmpty() || newPasswordString.isEmpty()) {
      Toast.makeText(this, "Username and password cannot be empty.", Toast.LENGTH_SHORT).show();
      return;
    }

    // Insert new user into database.
    User newUser = new User(newUsernameString, newPasswordString, NEW_ACCOUNT_IS_NOT_ADMIN);
    mUserDao.insert(newUser);

    // Display success message.
    Toast.makeText(CreateAccountActivity.this, "Account created successfully!", Toast.LENGTH_LONG).show();

    // Go back to login activity.
    Intent intent = IntentFactory.createLoginActivityIntent(this);
    startActivity(intent);
  }

  private void initializeViews() {
    mNewUsernameEditText = findViewById(R.id.edittext_new_username);
    mNewPasswordEditText = findViewById(R.id.edittext_new_password);
    mNewPasswordConfirmEditText = findViewById(R.id.edittext_new_password_confirm);
    mCreateAccountButton = findViewById(R.id.button_create_account);
  }

  private void initializeDatabase() {
    AppDatabase appDatabase = AppDatabase.getInstance(this);
    mUserDao = appDatabase.getUserDao();
  }
}