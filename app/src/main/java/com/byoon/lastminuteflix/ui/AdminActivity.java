package com.byoon.lastminuteflix.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.byoon.lastminuteflix.R;
import com.byoon.lastminuteflix.db.AppDatabase;
import com.byoon.lastminuteflix.db.GenreDao;
import com.byoon.lastminuteflix.db.MovieDao;
import com.byoon.lastminuteflix.db.TheaterDao;
import com.byoon.lastminuteflix.entity.Genre;
import com.byoon.lastminuteflix.entity.Movie;
import com.byoon.lastminuteflix.entity.Theater;
import com.byoon.lastminuteflix.utils.IntentFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays buttons to perform admin actions.
 *
 * @author Brian Yoon
 * @since 2023-12-13
 */
public class AdminActivity extends AppCompatActivity {
  private Button mLogOutButton;
  private Button mDeleteUserButton;
  private Button mAddMovieGenreButton;
  private Button mAddMovieButton;
  private Button mDeleteMovieButton;
  private Button mAddShowtimeButton;

  List<Genre> genres;
  List<Movie> movies;

  AppDatabase mDatabase = AppDatabase.getInstance(this);
  GenreDao genreDao = mDatabase.getGenreDao();
  MovieDao movieDao = mDatabase.getMovieDao();
  TheaterDao theaterDao = mDatabase.getTheaterDao();

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

    mAddMovieGenreButton.setOnClickListener(v -> showAddGenreDialog());

    mAddMovieButton.setOnClickListener(v -> showAddMovieDialog());

    mDeleteMovieButton.setOnClickListener(v -> getSupportFragmentManager().beginTransaction()
            .replace(R.id.fragment_container_admin, new MovieListFragment())
            .addToBackStack(null)
            .commit());

    mAddShowtimeButton.setOnClickListener(v -> showAddTheaterDialog());
  }

  private void initializeViews() {
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

  private void showAddGenreDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    LayoutInflater inflater = getLayoutInflater();
    View dialogView = inflater.inflate(R.layout.dialog_admin_add_genre, null);
    builder.setView(dialogView);

    builder.setPositiveButton("Add", (dialog, id) -> handleAddGenreSaveAction(dialogView));
    builder.setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());

    AlertDialog dialog = builder.create();
    dialog.show();
  }

  private void showAddMovieDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    LayoutInflater inflater = getLayoutInflater();
    View dialogView = inflater.inflate(R.layout.dialog_admin_add_movie, null);
    builder.setView(dialogView);

    Spinner genreSpinner = setupGenreSpinner(dialogView);

    builder.setPositiveButton("Add", (dialog, id) -> handleAddMovieSaveAction(dialogView, genreSpinner));
    builder.setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());

    AlertDialog dialog = builder.create();
    dialog.show();
  }

  private void showAddTheaterDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    LayoutInflater inflater = getLayoutInflater();
    View dialogView = inflater.inflate(R.layout.dialog_admin_add_theater, null);
    builder.setView(dialogView);

    Spinner movieSpinner = setupMovieSpinner(dialogView);

    builder.setPositiveButton("Add", (dialog, id) -> handleAddTheaterSaveAction(dialogView, movieSpinner));
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

  private Spinner setupMovieSpinner(View dialogView) {
    Spinner theaterSpinner = dialogView.findViewById(R.id.theater_movie_spinner);
    List<String> movieNames = getMovieNames();
    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, movieNames);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    theaterSpinner.setAdapter(adapter);
    return theaterSpinner;
  }

  private List<String> getGenreNames() {
    genres = genreDao.getAllGenres();
    List<String> genreNames = new ArrayList<>();
    genreNames.add("Select a genre");
    for (Genre genre : genres) {
      genreNames.add(genre.getGenreName());
    }
    return genreNames;
  }

  private List<String> getMovieNames() {
    movies = movieDao.getAllMovies();
    List<String> movieNames = new ArrayList<>();
    movieNames.add("Select a movie");
    for (Movie movie : movies) {
      movieNames.add(movie.getTitle());
    }
    return movieNames;
  }

  private void handleAddGenreSaveAction(View dialogView) {
    String genreName = getTextFromEditText(dialogView, R.id.edittext_movie_genre);

    if (validateGenreInput(genreName)) {
      saveGenreToDatabase(genreName);
    }
  }

  private void handleAddMovieSaveAction(View dialogView, Spinner genreSpinner) {
    String movieTitle = getTextFromEditText(dialogView, R.id.edittext_movie_title);
    String movieDurationString = getTextFromEditText(dialogView, R.id.edittext_movie_duration);
    String movieRating = getTextFromEditText(dialogView, R.id.edittext_movie_rating);

    int selectedGenreIndex = genreSpinner.getSelectedItemPosition();
    long genreId = selectedGenreIndex > 0 ? genres.get(selectedGenreIndex - 1).getGenreId() : -1;
    int movieDuration = parseToInt(movieDurationString);

    if (validateMovieInput(movieTitle, movieDuration, movieRating, genreId)) {
      saveMovieToDatabase(genreId, movieTitle, movieDuration, movieRating);
    }
  }

  private void handleAddTheaterSaveAction(View dialogView, Spinner movieSpinner) {
    String theaterName = getTextFromEditText(dialogView, R.id.edittext_theater_name);
    String theaterCityState = getTextFromEditText(dialogView, R.id.edittext_theater_city_state);
    String showTime = getTextFromEditText(dialogView, R.id.edittext_showtime);
    String ticketPriceString = getTextFromEditText(dialogView, R.id.edittext_ticket_price);
    String remainingSeatsString = getTextFromEditText(dialogView, R.id.edittext_num_seats);

    int selectedMovieIndex = movieSpinner.getSelectedItemPosition();
    long movieId = selectedMovieIndex > 0 ? movies.get(selectedMovieIndex - 1).getMovieId() : -1;
    double ticketPrice = parseToDouble(ticketPriceString);
    int remainingSeats = parseToInt(remainingSeatsString);

    if (validateTheaterInput(theaterName, theaterCityState, showTime, ticketPrice, remainingSeats, movieId)) {
      saveTheaterToDatabase(theaterName, theaterCityState, showTime, ticketPrice, remainingSeats, movieId);
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

  private double parseToDouble(String numberString) {
    try {
      return Double.parseDouble(numberString);
    } catch (NumberFormatException e) {
      return -1;
    }
  }

  private boolean validateGenreInput(String genreName) {
    // Verify genre field isn't blank.
    if (genreName.isEmpty()) {
      Toast.makeText(this, "Please enter a genre name", Toast.LENGTH_SHORT).show();
      return false;
    }

    // Verify genre doesn't already exist.
    if (genreDao.getGenreByName(genreName) != null) {
      Toast.makeText(this, "\"" + genreName + "\"" + " already exists", Toast.LENGTH_SHORT).show();
      return false;
    }
    return true;
  }

  private boolean validateMovieInput(String title, int duration, String rating, long genreId) {
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

    // Verify movie doesn't already exist.
    Movie existingMovie = movieDao.getMovieByTitle(title);
    if (existingMovie != null) {
      Toast.makeText(this, "\"" + title + "\"" + " already exists", Toast.LENGTH_SHORT).show();
      return false;
    }
    return true;
  }

  private boolean validateTheaterInput(String theaterName, String theaterCityState, String showTime, double ticketPrice, int remainingSeats, long movieId) {
    if (theaterName.isEmpty()) {
      Toast.makeText(this, "Please enter a theater name", Toast.LENGTH_SHORT).show();
      return false;
    } else if (theaterCityState.isEmpty()) {
      Toast.makeText(this, "Please enter a theater city and state", Toast.LENGTH_SHORT).show();
      return false;
    } else if (showTime.isEmpty()) {
      Toast.makeText(this, "Please enter a showtime", Toast.LENGTH_SHORT).show();
      return false;
    } else if (ticketPrice < 0) {
      Toast.makeText(this, "Please enter a valid ticket price", Toast.LENGTH_SHORT).show();
      return false;
    } else if (remainingSeats < 0) {
      Toast.makeText(this, "Please enter a valid number of seats", Toast.LENGTH_SHORT).show();
      return false;
    } else if (movieId < 0) {
      Toast.makeText(this, "Please select a movie", Toast.LENGTH_SHORT).show();
      return false;
    }
    return true;
  }

  private void saveGenreToDatabase(String genreName) {
    // Genre doesn't exist, so safe to add.
    Genre genre = new Genre(genreName);
    genreDao.insert(genre);
    Toast.makeText(this, "Genre successfully added", Toast.LENGTH_SHORT).show();
  }

  private void saveMovieToDatabase(long genreId, String title, int duration, String rating) {
    // Safe to add movie at this point.
    Movie movie = new Movie(genreId, title, duration, rating);
    movieDao.insert(movie);
    Toast.makeText(this, "Movie successfully added", Toast.LENGTH_SHORT).show();
  }

  private void saveTheaterToDatabase(String theaterName, String theaterCityState, String showTime, double ticketPrice, int remainingSeats, long movieId) {
    // Safe to add theater at this point.
    Theater theater = new Theater(movieId, theaterName, theaterCityState, showTime, ticketPrice, remainingSeats);
    theaterDao.insert(theater);
    Toast.makeText(this, "Theater successfully added", Toast.LENGTH_SHORT).show();
  }
}