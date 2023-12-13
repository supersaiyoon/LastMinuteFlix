package com.byoon.lastminuteflix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;

import androidx.room.Room;

import com.byoon.lastminuteflix.db.AppDatabase;
import com.byoon.lastminuteflix.db.GenreDao;
import com.byoon.lastminuteflix.db.MovieDao;
import com.byoon.lastminuteflix.db.ShoppingCartDao;
import com.byoon.lastminuteflix.db.TheaterDao;
import com.byoon.lastminuteflix.db.UserDao;
import com.byoon.lastminuteflix.entity.Genre;
import com.byoon.lastminuteflix.entity.Movie;
import com.byoon.lastminuteflix.entity.ShoppingCart;
import com.byoon.lastminuteflix.entity.Theater;
import com.byoon.lastminuteflix.entity.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

@RunWith(RobolectricTestRunner.class)
public class ShoppingCartDaoTest {
  private AppDatabase database;
  private ShoppingCartDao shoppingCartDao;
  TheaterDao theaterDao;
  private GenreDao genreDao;
  private MovieDao movieDao;
  // Necessary for foreign key constraints.
  private long testUserId;
  private long testTheaterId;
  ShoppingCart cart;
  private long cartId;

  @Before
  public void setUp() {
    Context context = RuntimeEnvironment.getApplication();
    database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
            .allowMainThreadQueries()
            .build();
    shoppingCartDao = database.getShoppingCartDao();
    UserDao userDao = database.getUserDao();
    movieDao = database.getMovieDao();
    theaterDao = database.getTheaterDao();
    genreDao = database.getGenreDao();

    // Set up theater to use in shopping cart.
    long testGenreId = genreDao.insert(new Genre("Comedy"));
    long testMovieId = movieDao.insert(new Movie(testGenreId, "Ace Ventura: When Nature Calls", 94, "PG-13"));

    // Need user and theater in database to create shopping cart because of foreign key constraints.
    testUserId = userDao.insert(new User("testuser", "password", false));
    testTheaterId = theaterDao.insert(new Theater(testMovieId, "Cinema Park", "Sacramento, CA", "7:30 PM", 4.99, 100));

    // Initialize shopping cart database.
    cart = new ShoppingCart(testUserId, testTheaterId);
    cartId = shoppingCartDao.insert(cart);
  }

  @After
  public void tearDown() {
    database.close();
    cart = null;
  }

  @Test
  public void givenShoppingCart_whenInserted_thenCartIsRetrievable() {
    // Given is handled in setUp().


    // When
    ShoppingCart retrievedCart = shoppingCartDao.getShoppingCartById(cartId);

    // Then
    assertNotNull(retrievedCart);
    assertEquals(testUserId, retrievedCart.getUserId());
    assertEquals(testTheaterId, retrievedCart.getTheaterId());
  }

  @Test
  public void givenExistingCart_whenUpdated_thenCartIsUpdatedInDatabase() {
    // Given - Ensure same cart is updated in database.
    cart.setShoppingCartId(cartId);
    assertEquals(testTheaterId, cart.getTheaterId());

    // Get new theater ID.
    long newGenreId = genreDao.insert(new Genre("Romantic Comedy"));
    long newMovieId = movieDao.insert(new Movie(newGenreId, "Me, Myself & Irene", 116, "R"));
    long newTheaterId = theaterDao.insert(new Theater(newMovieId, "Regal 16", "Natomas, CA", "4:00 PM", 7.99, 75));
    cart.setTheaterId(newTheaterId);
    assertEquals(newTheaterId, cart.getTheaterId());

    // When
    shoppingCartDao.update(cart);
    ShoppingCart updatedCart = shoppingCartDao.getShoppingCartById(cartId);

    // Then
    assertNotNull(updatedCart);
    assertEquals(newTheaterId, updatedCart.getTheaterId());
  }

  @Test
  public void givenExistingCart_whenDeleted_thenCartIsNotRetrievable() {
    // Given

    // When
    cart.setShoppingCartId(cartId);
    shoppingCartDao.delete(cart);
    ShoppingCart retrievedCart = shoppingCartDao.getShoppingCartById(cartId);

    // Then
    assertNull(retrievedCart);
  }
}
