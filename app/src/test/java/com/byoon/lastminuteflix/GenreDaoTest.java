package com.byoon.lastminuteflix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;

import androidx.room.Room;

import com.byoon.lastminuteflix.db.AppDatabase;
import com.byoon.lastminuteflix.db.GenreDao;
import com.byoon.lastminuteflix.entity.Genre;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.robolectric.RobolectricTestRunner;

import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;

@RunWith(RobolectricTestRunner.class)
public class GenreDaoTest {
  private AppDatabase database;
  private GenreDao genreDao;

  @Before
  public void createDb() {
    // Use in-memory database for testing.
    Context context = RuntimeEnvironment.getApplication();
    database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
            .allowMainThreadQueries()
            .build();
    genreDao = database.getGenreDao();
  }

  @After
  public void closeDb() {
    database.close();
  }

  @Test
  public void givenGenre_whenInserting_thenFoundInDatabase() {
    // Given
    String genreName = "Action";
    Genre genre = new Genre(genreName);

    // When
    genreDao.insert(genre);

    // Then
    Genre retrievedGenre = genreDao.getGenreByName(genreName);
    assertNotNull(retrievedGenre);
    assertEquals(genre.getGenreName(), retrievedGenre.getGenreName());
  }

  @Test
  public void givenExistingGenre_whenUpdating_thenUpdatedInDatabase() {
    // Given
    String genreName = "Comedy";
    Genre genre = new Genre(genreName);
    long genreId = genreDao.insert(genre);

    // Ensure the same genre is retrieved from database.
    genre.setGenreId(genreId);

    // When
    String newGenreName = "Romantic Comedy";
    genre.setGenreName(newGenreName);
    genreDao.update(genre);

    // Then
    Genre updatedGenre = genreDao.getGenreById(genreId);
    assertNotNull(updatedGenre);
    assertEquals(newGenreName, updatedGenre.getGenreName());
  }

  @Test
  public void givenExistingGenre_whenDeleting_thenNotFoundInDatabase() {
    // Given
    String genreName = "Horror";
    Genre genre = new Genre(genreName);
    long genreId = genreDao.insert(genre);

    // Ensure the same genre is retrieved from database.
    genre.setGenreId(genreId);

    // When
    genreDao.delete(genre);

    // Then
    Genre retrievedGenre = genreDao.getGenreByName(genreName);
    assertNull(retrievedGenre);
  }

}
