package com.byoon.lastminuteflix.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.byoon.lastminuteflix.R;
import com.byoon.lastminuteflix.db.AppDatabase;
import com.byoon.lastminuteflix.db.GenreDao;
import com.byoon.lastminuteflix.db.MovieDao;
import com.byoon.lastminuteflix.db.UserDao;
import com.byoon.lastminuteflix.entity.Genre;
import com.byoon.lastminuteflix.entity.Movie;
import com.byoon.lastminuteflix.entity.User;
import com.byoon.lastminuteflix.utils.IntentFactory;
import com.byoon.lastminuteflix.utils.KeyConstants;

import java.util.List;

/**
 * Displays welcome message and buttons to perform actions.
 * If user did not log out, user will be logged in automatically to this activity.
 * Admin users will see Admin button to go to admin activity.
 * @author Brian Yoon
 * @since 2023-12-05
 */
public class MainActivity extends AppCompatActivity {
  private static final int NO_USER_LOGGED_IN = -1;

  private TextView mWelcomeMessageTextView;
  private Button mBrowseMoviesButton;
  private Button mShoppingCartButton;
  private Button mOrderHistoryButton;
  private Button mAdminButton;
  private Button mLogOutButton;

  private int mUserId = NO_USER_LOGGED_IN;
  private User mUser;

  private UserDao mUserDao;
  private GenreDao mGenreDao;
  private MovieDao mMovieDao;
  private SharedPreferences mPreferences = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    initializeDatabase();
    getPrefs();

    initializeGenres();
    initializeMovies();

    if (!checkForUser()) {
      return;  // Exit if no user found.
    }

    initializeUser();
    initializeViews();

    String username = mUser.getUsername();
    updateWelcomeMessage(username);

    addUserToPreferences(mUserId);
  }

  private void updateWelcomeMessage(String username) {
    String welcomeWord = getString(R.string.welcome_text);
    String welcomeMessage = welcomeWord + ", " + username + "!";
    mWelcomeMessageTextView.setText(welcomeMessage);
  }

  private void initializeDatabase() {
    AppDatabase appDatabase = AppDatabase.getInstance(this);
    mUserDao = appDatabase.getUserDao();
    mGenreDao = appDatabase.getGenreDao();
    mMovieDao = appDatabase.getMovieDao();
  }

  private void initializeViews() {
    mWelcomeMessageTextView = findViewById(R.id.text_welcome);
    mBrowseMoviesButton = findViewById(R.id.button_browse_movies);
    mShoppingCartButton = findViewById(R.id.button_shopping_cart);
    mOrderHistoryButton = findViewById(R.id.button_order_history);
    mAdminButton = findViewById(R.id.button_admin);
    mLogOutButton = findViewById(R.id.button_logout);

    // Show admin button only if user is admin.
    mAdminButton.setVisibility(View.INVISIBLE);
    if (mUser.isAdmin()) {
      mAdminButton.setVisibility(View.VISIBLE);
    }

    mLogOutButton.setOnClickListener(v -> showLogoutConfirmationDialog());
    mAdminButton.setOnClickListener(v -> goToAdminActivity());
  }

  private void goToAdminActivity() {
    Intent intent = IntentFactory.createAdminActivityIntent(this);
    startActivity(intent);
  }

  private void showLogoutConfirmationDialog() {
    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
    alertBuilder.setMessage(R.string.log_out_dialog_message);

    alertBuilder.setPositiveButton(getString(R.string.log_out_dialog_positive_button),
            (dialog, which) -> {
              clearUser();
              checkForUser();
            });
    alertBuilder.setNegativeButton(getString(R.string.log_out_dialog_negative_button),
            (dialog, which) -> {
              // No need to do anything here.
            });

    // Show alert dialog.
    AlertDialog alertDialog = alertBuilder.create();
    alertDialog.show();
  }

  private void addUserToPreferences(int userId) {
    SharedPreferences.Editor editor = mPreferences.edit();
    editor.putInt(KeyConstants.USER_ID_KEY.getKey(), userId);
    editor.apply();
  }

  private void initializeUser() {
    // Get User from either Intent or SharedPreferences.
    int userId = getIntent().getIntExtra(KeyConstants.USER_ID_KEY.getKey(), NO_USER_LOGGED_IN);

    if (userId == NO_USER_LOGGED_IN) {
      userId = mPreferences.getInt(KeyConstants.USER_ID_KEY.getKey(), NO_USER_LOGGED_IN);
    }
    mUser = mUserDao.getUserById(userId);
  }

  private void clearUser() {
    clearUserFromIntent();
    clearUserFromPreferences();
    mUserId = NO_USER_LOGGED_IN;
  }
  private void clearUserFromIntent() {
    getIntent().putExtra(KeyConstants.USER_ID_KEY.getKey(), NO_USER_LOGGED_IN);
  }

  private void clearUserFromPreferences() {
    addUserToPreferences(NO_USER_LOGGED_IN);
  }

  /**
   * Checks if there is a user logged in. If not, goes to login activity.
   */
  private boolean checkForUser() {
    mUserId = getIntent().getIntExtra(KeyConstants.USER_ID_KEY.getKey(), NO_USER_LOGGED_IN);
    if (mUserId != NO_USER_LOGGED_IN) {
      mUser = mUserDao.getUserById(mUserId);
      return true;  // User found in intent.
    }

    mUserId = mPreferences.getInt(KeyConstants.USER_ID_KEY.getKey(), NO_USER_LOGGED_IN);
    if (mUserId != NO_USER_LOGGED_IN) {
      mUser = mUserDao.getUserById(mUserId);
      return true;  // User found in shared preferences.
    }

    // No valid user found, go to login activity.
    Intent intent = IntentFactory.createLoginActivityIntent(this);
    startActivity(intent);
    // End MainActivity.
    finish();
    return false;
  }

  private void getPrefs() {
    mPreferences = this.getSharedPreferences(KeyConstants.PREFERENCES_KEY.getKey(), Context.MODE_PRIVATE);
  }

  private void initializeGenres() {
    // Check if genres already exist in database to avoid duplicate entries.
    List<Genre> existingGenres = mGenreDao.getAllGenres();
    if (existingGenres == null || existingGenres.isEmpty()) {
      // Add predefined genres
      mGenreDao.insert(new Genre("Action"));
      mGenreDao.insert(new Genre("Comedy"));
      mGenreDao.insert(new Genre("Drama"));
    }
  }

  private void initializeMovies() {
    // Check if movies already exist in database to avoid duplicate entries.
    List<Movie> existingMovies = mMovieDao.getAllMovies();
    if (existingMovies == null || existingMovies.isEmpty()) {
      // Add predefined movies
      mMovieDao.insert(new Movie(1, "The Matrix", 136, "R"));
      mMovieDao.insert(new Movie(2, "The Hangover", 100, "R"));
      mMovieDao.insert(new Movie(3, "The Dark Knight", 152, "PG-13"));
    }
  }
}