package com.byoon.lastminuteflix.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.byoon.lastminuteflix.R;
import com.byoon.lastminuteflix.db.AppDatabase;
import com.byoon.lastminuteflix.db.GenreDao;
import com.byoon.lastminuteflix.db.MovieDao;
import com.byoon.lastminuteflix.entity.Genre;
import com.byoon.lastminuteflix.entity.Movie;
import com.byoon.lastminuteflix.utils.IntentFactory;

import java.util.ArrayList;
import java.util.List;

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
  GenreDao genreDao;
  MovieDao movieDao;
  List<Genre> genres;

  AppDatabase mDatabase = AppDatabase.getInstance(this);

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin);

    initializeViews();

    mLogOutButton.setOnClickListener(v -> showLogoutConfirmationDialog());

    mDeleteUserButton.setOnClickListener(v -> getSupportFragmentManager().beginTransaction()
            .replace(R.id.fragment_container_admin, new UserListFragment())
            .addToBackStack(null)
            .commit());

    mAddMovieButton.setOnClickListener(v -> showAddMovieDialog());
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
              // Return to login activity.
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

  private void showAddMovieDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    LayoutInflater inflater = getLayoutInflater();
    View dialogView = inflater.inflate(R.layout.dialog_admin_add_movie, null);
    builder.setView(dialogView);

    Spinner genreSpinner = setupGenreSpinner(dialogView);

    builder.setPositiveButton("Save", (dialog, id) -> handleSaveAction(dialogView, genreSpinner));
    builder.setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());

    AlertDialog dialog = builder.create();
    dialog.show();
  }

  private Spinner setupGenreSpinner(View dialogView) {
    Spinner genreSpinner = dialogView.findViewById(R.id.genre_spinner);
    List<String> genreNames = getGenreNames();
    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genreNames);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    genreSpinner.setAdapter(adapter);
    return genreSpinner;
  }

  private List<String> getGenreNames() {
    genreDao = mDatabase.getGenreDao();
    genres = genreDao.getAllGenres();
    List<String> genreNames = new ArrayList<>();
    genreNames.add("Select a genre");
    for (Genre genre : genres) {
      genreNames.add(genre.getGenreName());
    }
    return genreNames;
  }

  private void handleSaveAction(View dialogView, Spinner genreSpinner) {
    String movieTitle = getTextFromEditText(dialogView, R.id.edittext_movie_title);
    int selectedGenreIndex = genreSpinner.getSelectedItemPosition();
    String movieDurationString = getTextFromEditText(dialogView, R.id.edittext_movie_duration);
    String movieRating = getTextFromEditText(dialogView, R.id.edittext_movie_rating);

    long genreId = selectedGenreIndex > 0 ? genres.get(selectedGenreIndex - 1).getGenreId() : -1;
    int movieDuration = parseToInt(movieDurationString);

    if (validateInput(movieTitle, movieDuration, movieRating, genreId)) {
      saveMovieToDatabase(genreId, movieTitle, movieDuration, movieRating);
    }
  }

  private String getTextFromEditText(View view, int editTextId) {
    EditText editText = view.findViewById(editTextId);
    return editText.getText().toString();
  }

  private int parseToInt(String numberString) {
    try {
      return Integer.parseInt(numberString);
    } catch (NumberFormatException e) {
      return -1;
    }
  }

  private boolean validateInput(String title, int duration, String rating, long genreId) {
    if (title.isEmpty()) {
      Toast.makeText(this, "Please enter a movie title", Toast.LENGTH_SHORT).show();
      return false;
    } else if (genreId < 0) {
      Toast.makeText(this, "Please select a genre", Toast.LENGTH_SHORT).show();
      return false;
    } else if (duration < 0) {
      Toast.makeText(this, "Please enter a valid movie duration", Toast.LENGTH_SHORT).show();
      return false;
    } else if (rating.isEmpty()) {
      Toast.makeText(this, "Please enter a movie rating", Toast.LENGTH_SHORT).show();
      return false;
    }
    return true;
  }

  private void saveMovieToDatabase(long genreId, String title, int duration, String rating) {
    movieDao = mDatabase.getMovieDao();

    // Verify movie doesn't already exist.
    Movie existingMovie = movieDao.getMovieByTitle(title);
    if (existingMovie != null) {
      Toast.makeText(this, "\"" + title + "\"" + " already exists", Toast.LENGTH_SHORT).show();
      return;
    }

    // Movie doesn't exist, so safe to add.
    Movie movie = new Movie(genreId, title, duration, rating);
    movieDao.insert(movie);
    Toast.makeText(this, "Movie successfully added", Toast.LENGTH_SHORT).show();
  }
}