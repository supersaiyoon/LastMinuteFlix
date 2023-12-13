package com.byoon.lastminuteflix;

import static junit.framework.TestCase.assertEquals;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;

import androidx.room.Room;

import com.byoon.lastminuteflix.db.AppDatabase;
import com.byoon.lastminuteflix.db.GenreDao;
import com.byoon.lastminuteflix.db.MovieDao;
import com.byoon.lastminuteflix.entity.Genre;
import com.byoon.lastminuteflix.entity.Movie;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

@RunWith(RobolectricTestRunner.class)
public class MovieDaoTest {
  private AppDatabase database;
  private MovieDao movieDao;
  private int testGenreId;

  @Before
  public void createDb() {
    // Use in-memory database for testing.
    Context context = RuntimeEnvironment.getApplication();
    database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
            .allowMainThreadQueries()
            .build();
    movieDao = database.getMovieDao();
    GenreDao genreDao = database.getGenreDao();

    String testGenreName = "Comedy";
    Genre testGenre = new Genre(testGenreName);
    genreDao.insert(testGenre);
    testGenreId = genreDao.getGenreByName(testGenreName).getGenreId();
  }

  @After
  public void closeDb() {
    database.close();
  }

  @Test
  public void givenMovie_whenInserting_thenMovieIsRetrievable() {
    // Given
    String movieTitle = "Dumb and Dumber";
    int duration = 113;
    String rating = "PG-13";

    Movie movie = new Movie(testGenreId, movieTitle, duration, rating);

    // When
    movieDao.insert(movie);

    // Then
    Movie retrievedMovie = movieDao.getMovieByTitle(movieTitle);
    assertNotNull(retrievedMovie);
    assertEquals(movie.getTitle(), retrievedMovie.getTitle());
  }

  @Test
  public void givenExistingMovie_whenUpdated_thenMovieIsUpdatedInDatabase() {
    // Given
    String movieTitle = "The Truman Show";
    int duration = 107;
    String rating = "PG";

    Movie movie = new Movie(testGenreId, movieTitle, duration, rating);
    movieDao.insert(movie);

    // Ensure the same movie is retrieved from database.
    int movieId = movieDao.getMovieByTitle(movieTitle).getMovieId();
    movie.setMovieId(movieId);

    // When
    String newMovieTitle = "The Cable Guy";
    movie.setTitle(newMovieTitle);
    movieDao.update(movie);

    // Then
    Movie updatedMovie = movieDao.getMovieById(movieId);
    assertNotNull(updatedMovie);
    assertEquals(newMovieTitle, updatedMovie.getTitle());
  }

  @Test
  public void givenExistingMovie_whenDeleted_thenMovieIsNotRetrievable() {
    // Given
    String movieTitle = "Ace Ventura: Pet Detective";
    int duration = 86;
    String rating = "PG-13";

    Movie movie = new Movie(testGenreId, movieTitle, duration, rating);
    movieDao.insert(movie);

    // Ensure the same movie is retrieved from database.
    int movieId = movieDao.getMovieByTitle(movieTitle).getMovieId();
    movie.setMovieId(movieId);

    // When
    movieDao.delete(movie);

    // Then
    Movie retrievedMovie = movieDao.getMovieById(movieId);
    assertNull(retrievedMovie);
  }

}
