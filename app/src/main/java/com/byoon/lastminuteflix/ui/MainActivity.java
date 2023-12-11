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
import com.byoon.lastminuteflix.db.UserDao;
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
  private static final int NO_USER_LOGGED_IN = -1;

  private TextView welcomeMessageTextView;

  private Button mBrowseMoviesButton;
  private Button mShoppingCartButton;
  private Button mOrderHistoryButton;
  private Button mAdminButton;
  private Button mLogOutButton;

  private int mUserId = NO_USER_LOGGED_IN;
  private User mUser;

  private UserDao mUserDao;

  private SharedPreferences mPreferences = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    getDatabase();

    // Retrieve username from User with userId.
    int userId = getIntent().getIntExtra(IntentKeys.USER_ID_KEY.getKey(), NO_USER_LOGGED_IN);

    logInUser(userId);

    String username = mUserDao.getUserById(userId).getUsername();

    // Update welcome message TextView.
    welcomeMessageTextView = findViewById(R.id.welcome_display);
    String welcomeWord = getString(R.string.welcome_text);
    String welcomeMessage = welcomeWord + ", " + username + "!";
    welcomeMessageTextView.setText(welcomeMessage);

    wireUpDisplay();

    checkForUser();

    addUserToPreferences(mUserId);
  }

  private void wireUpDisplay() {
    mBrowseMoviesButton = findViewById(R.id.browse_movies_button);
    mShoppingCartButton = findViewById(R.id.shopping_cart_button);
    mOrderHistoryButton = findViewById(R.id.order_history_button);
    mAdminButton = findViewById(R.id.admin_button);
    mLogOutButton = findViewById(R.id.logout_button);

    // Show admin button only if user is admin.
    mAdminButton.setVisibility(View.INVISIBLE);
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
                mUserId = NO_USER_LOGGED_IN;
                checkForUser();
              }
            });
    alertBuilder.setNegativeButton(getString(R.string.log_out_dialog_negative_button),
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                // No need to do anything here.
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
    editor.apply();
  }

  private void logInUser(int userId) {
    mUser = mUserDao.getUserById(userId);
  }

  private void clearUserFromIntent() {
    getIntent().putExtra(IntentKeys.USER_ID_KEY.getKey(), NO_USER_LOGGED_IN);
  }

  private void clearUserFromPreferences() {
    addUserToPreferences(NO_USER_LOGGED_IN);
  }

  private void getDatabase() {
    mUserDao = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
            .allowMainThreadQueries()
            .build()
            .getUserDao();
  }

  /**
   * Checks if there is a user logged in. If not, goes to login activity.
   */
  private void checkForUser() {
    // Do we have user in intent?
    mUserId = getIntent().getIntExtra(IntentKeys.USER_ID_KEY.getKey(), NO_USER_LOGGED_IN);
    if (mUserId != NO_USER_LOGGED_IN) {
      // There is user logged in.
      return;
    }

    // Do we have user in preferences?
    if (mPreferences == null) {
      getPrefs();
    }

    mUserId = mPreferences.getInt(IntentKeys.USER_ID_KEY.getKey(), NO_USER_LOGGED_IN);
    if (mUserId != NO_USER_LOGGED_IN) {
      return;
    }

    // Do we have any users at all?
    List<User> users = mUserDao.getAllUsers();
    if (users.isEmpty()) {
      User defaultUser = new User("defaultuser", "password123", false);
      mUserDao.insert(defaultUser);
    }

    // No valid user found. Go to login activity.
    Intent intent = IntentFactory.createLoginActivityIntent(this);
    startActivity(intent);
  }

  private void getPrefs() {
    mPreferences = this.getSharedPreferences(IntentKeys.PREFERENCES_KEY.getKey(), Context.MODE_PRIVATE);
  }
}