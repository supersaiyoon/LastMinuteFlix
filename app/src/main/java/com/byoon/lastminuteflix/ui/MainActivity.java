package com.byoon.lastminuteflix.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.byoon.lastminuteflix.R;
import com.byoon.lastminuteflix.db.AppDatabase;
import com.byoon.lastminuteflix.db.UserDAO;
import com.byoon.lastminuteflix.entity.User;
import com.byoon.lastminuteflix.utils.IntentFactory;
import com.byoon.lastminuteflix.utils.IntentKeys;

import java.util.List;

/**
 * Main activity user sees after logging in. Displays welcome message and buttons to perform actions.
 * Admin users will see Admin button to go to admin activity.
 * @author Brian Yoon
 * @since 2023-12-05
 */
public class MainActivity extends AppCompatActivity {
  private TextView welcomeMessageTextView;

  private Button mBrowseMoviesButton;
  private Button mShoppingCartButton;
  private Button mOrderHistoryButton;
  private Button mAdminButton;
  private Button mLogOutButton;

  private int mUserId = -1;
  private User mUser;

  private UserDAO mUserDAO;

  private SharedPreferences mPreferences = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    getDatabase();

    // Retrieve username from User with userId.
    int userId = getIntent().getIntExtra(IntentKeys.USER_ID_KEY.getKey(), -1);
    logInUser(userId);
    String username = mUserDAO.getUserById(userId).getUsername();

    // Update welcome message TextView.
    welcomeMessageTextView = findViewById(R.id.welcome_display);
    String welcomeWord = getString(R.string.welcome_text);
    String welcomeMessage = welcomeWord + ", " + username + "!";
    welcomeMessageTextView.setText(welcomeMessage);

    wireUpDisplay();

    checkForUser();

    addUserToPreferences(mUserId);

    // Check if user is admin to determine if admin button should be visible.
  }

  private void wireUpDisplay() {
    mBrowseMoviesButton = findViewById(R.id.browse_movies_button);
    mShoppingCartButton = findViewById(R.id.shopping_cart_button);
    mOrderHistoryButton = findViewById(R.id.order_history_button);
    mAdminButton = findViewById(R.id.admin_button);
    mLogOutButton = findViewById(R.id.logout_button);

    mAdminButton.setVisibility(View.INVISIBLE);
    // Show admin button only if user is admin.
    if (mUser.isAdmin()) {
      mAdminButton.setVisibility(View.VISIBLE);
    }

    // TODO: Set onClickLister for each button.
    mLogOutButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        logOutUser();
      }
    });
  }

  private void logOutUser() {
    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

    alertBuilder.setMessage(R.string.log_out_dialog_message);

    alertBuilder.setPositiveButton(getString(R.string.log_out_dialog_positive_button),
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                clearUserFromIntent();
                clearUserFromPreferences();
                mUserId = -1;
                dialog.dismiss();

                Intent intent = IntentFactory.createLoginActivityIntent(MainActivity.this);
                startActivity(intent);
              }
            });
    alertBuilder.setNegativeButton(getString(R.string.log_out_dialog_negative_button),
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
              }
            });

    // Show alert dialog.
    AlertDialog alertDialog = alertBuilder.create();
    alertDialog.show();
  }

  private void addUserToPreferences(int userId) {
    if (mPreferences == null) {
      getPrefs();
    }
    SharedPreferences.Editor editor = mPreferences.edit();
    editor.putInt(IntentKeys.USER_ID_KEY.getKey(), userId);
  }

  private void logInUser(int userId) {
    mUser = mUserDAO.getUserById(userId);
  }

  private void clearUserFromIntent() {
    getIntent().putExtra(IntentKeys.USER_ID_KEY.getKey(), -1);
  }

  private void clearUserFromPreferences() {
    addUserToPreferences(-1);
  }

  private void getDatabase() {
    mUserDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
            .allowMainThreadQueries()
            .build()
            .getUserDAO();
  }

  private void checkForUser() {
    // Do we have user intent?
    mUserId = getIntent().getIntExtra(IntentKeys.USER_ID_KEY.getKey(), -1);
    if (mUserId != -1) {
      return;
    }

    // Do we have user in preferences?
    if (mPreferences == null) {
      getPrefs();
    }

    mUserId = mPreferences.getInt(IntentKeys.USER_ID_KEY.getKey(), -1);
    if (mUserId != -1) {
      return;
    }

    // Do we have any users at all?
    List<User> users = mUserDAO.getAllUsers();
    if (users.size() <= 0) {
      User defaultUser = new User("defaultuser", "password123", false);
      mUserDAO.insert(defaultUser);
    }

    // No valid user found. Go to login activity.
    Intent intent = IntentFactory.createLoginActivityIntent(this);
    startActivity(intent);
  }

  private void getPrefs() {
    mPreferences = this.getSharedPreferences(IntentKeys.PREFERENCES_KEY.getKey(), Context.MODE_PRIVATE);
  }
}