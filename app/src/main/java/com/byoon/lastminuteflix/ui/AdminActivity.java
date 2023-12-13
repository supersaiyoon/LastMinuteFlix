package com.byoon.lastminuteflix.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.byoon.lastminuteflix.R;
import com.byoon.lastminuteflix.utils.IntentFactory;

/**
 * Displays buttons to perform admin actions.
 * @author Brian Yoon
 * @since 2023-12-13
 */
public class AdminActivity extends AppCompatActivity {
  private TextView mWelcomeMessageTextView;
  private Button mLogOutButton;
  private Button mDeleteUserButton;
  private Button mAddMovieGenreButton;
  private Button mAddMovieButton;
  private Button mDeleteMovieButton;
  private Button mAddShowtimeButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin);

    initializeViews();

    mLogOutButton.setOnClickListener(v -> showLogoutConfirmationDialog());
  }

  private void initializeViews() {
    mWelcomeMessageTextView = findViewById(R.id.text_admin);
    mLogOutButton = findViewById(R.id.button_logout);
    mDeleteUserButton = findViewById(R.id.button_delete_user);
    mAddMovieGenreButton = findViewById(R.id.button_add_movie_genre);
    mAddMovieButton = findViewById(R.id.button_add_movie);
    mDeleteMovieButton = findViewById(R.id.button_delete_movie);
    mAddShowtimeButton = findViewById(R.id.button_add_showtime);
  }

  private void showLogoutConfirmationDialog() {
    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
    alertBuilder.setMessage(R.string.log_out_dialog_message);

    alertBuilder.setPositiveButton(getString(R.string.log_out_dialog_positive_button),
            (dialog, which) -> {
              Intent intent = IntentFactory.createLoginActivityIntent(this);
              startActivity(intent);
            });
    alertBuilder.setNegativeButton(getString(R.string.log_out_dialog_negative_button),
            (dialog, which) -> {
              // No need to do anything here.
            });

    // Show alert dialog.
    AlertDialog alertDialog = alertBuilder.create();
    alertDialog.show();
  }
}