package com.byoon.lastminuteflix;

import static junit.framework.TestCase.assertEquals;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;

import androidx.room.Room;

import com.byoon.lastminuteflix.db.AppDatabase;
import com.byoon.lastminuteflix.db.GenreDao;
import com.byoon.lastminuteflix.db.MovieDao;
import com.byoon.lastminuteflix.db.TheaterDao;
import com.byoon.lastminuteflix.entity.Genre;
import com.byoon.lastminuteflix.entity.Movie;
import com.byoon.lastminuteflix.entity.Theater;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

@RunWith(RobolectricTestRunner.class)
public class TheaterDaoTest {
  private AppDatabase database;
  private TheaterDao theaterDao;
  String theaterName = "Cinema Park";
  Theater theater;

  @Before
  public void setUp() {
    Context context = RuntimeEnvironment.getApplication();
    database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
            .allowMainThreadQueries()
            .build();
    theaterDao = database.getTheaterDao();
    MovieDao movieDao = database.getMovieDao();
    GenreDao genreDao = database.getGenreDao();

    // Need genre and movie in database to create theater because of foreign key constraints.
    long testGenreId = genreDao.insert(new Genre("Comedy"));
    long testMovieId = movieDao.insert(new Movie(testGenreId, "Dumb and Dumber", 113, "PG-13"));

    // Initialize theater database.
    theater = new Theater(testMovieId, theaterName, "Sacramento, CA", "7:30 PM", 4.99, 100);
  }

  @After
  public void tearDown() {
    database.close();
    theater = null;
  }

  @Test
  public void givenTheater_whenInserted_thenTheaterIsRetrievable() {
    // Given Theater created in setUp().

    // When
    long theaterId = theaterDao.insert(theater);
    Theater retrievedTheater = theaterDao.getTheaterById(theaterId);

    // Then
    assertNotNull(retrievedTheater);
    assertEquals(theaterName, retrievedTheater.getTheaterName());
  }

  @Test
  public void givenExistingTheater_whenUpdated_thenTheaterIsUpdatedInDatabase() {
    // Given Theater in database.
    long theaterId = theaterDao.insert(theater);
    // Ensure same theater is updated in database.
    theater.setTheaterId(theaterId);
    int newRemainingSeats = 50;
    theater.setRemainingSeats(newRemainingSeats);

    // When
    theaterDao.update(theater);
    Theater updatedTheater = theaterDao.getTheaterById(theaterId);

    // Then
    assertNotNull(updatedTheater);
    assertEquals(newRemainingSeats, updatedTheater.getRemainingSeats());
  }

  @Test
  public void givenExistingTheater_whenDeleted_thenTheaterIsNotRetrievable() {
    // Given Theater in database
    long theaterId = theaterDao.insert(theater);
    // Ensure correct theater is deleted from database.
    theater.setTheaterId(theaterId);

    // When
    theaterDao.delete(theater);
    Theater retrievedTheater = theaterDao.getTheaterById(theaterId);

    // Then
    assertNull(retrievedTheater);
  }
}
