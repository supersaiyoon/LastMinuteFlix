package com.byoon.lastminuteflix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;

import androidx.room.Room;

import com.byoon.lastminuteflix.db.AppDatabase;
import com.byoon.lastminuteflix.db.GenreDao;
import com.byoon.lastminuteflix.db.MovieDao;
import com.byoon.lastminuteflix.db.OrderHistoryDao;
import com.byoon.lastminuteflix.db.TheaterDao;
import com.byoon.lastminuteflix.db.UserDao;
import com.byoon.lastminuteflix.entity.Genre;
import com.byoon.lastminuteflix.entity.Movie;
import com.byoon.lastminuteflix.entity.OrderHistory;
import com.byoon.lastminuteflix.entity.Theater;
import com.byoon.lastminuteflix.entity.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

@RunWith(RobolectricTestRunner.class)
public class OrderHistoryDaoTest {
  private AppDatabase database;
  private OrderHistoryDao mOrderHistoryDao;
  TheaterDao theaterDao;
  private GenreDao genreDao;
  private MovieDao movieDao;
  // Necessary for foreign key constraints.
  private long testUserId;
  private long testTheaterId;
  OrderHistory orderHistory;
  private long orderHistoryId;

  @Before
  public void setUp() {
    Context context = RuntimeEnvironment.getApplication();
    database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
            .allowMainThreadQueries()
            .build();
    mOrderHistoryDao = database.getOrderHistoryDao();
    UserDao userDao = database.getUserDao();
    movieDao = database.getMovieDao();
    theaterDao = database.getTheaterDao();
    genreDao = database.getGenreDao();

    // Set up theater to use in order history.
    long testGenreId = genreDao.insert(new Genre("Comedy"));
    long testMovieId = movieDao.insert(new Movie(testGenreId, "Ace Ventura: When Nature Calls", 94, "PG-13"));

    // Need user and theater in database to create order history because of foreign key constraints.
    testUserId = userDao.insert(new User("testuser", "password", false));
    testTheaterId = theaterDao.insert(new Theater(testMovieId, "Cinema Park", "Sacramento, CA", "7:30 PM", 4.99, 100));

    // Initialize order history database.
    orderHistory = new OrderHistory(testUserId, testTheaterId);
    orderHistoryId = mOrderHistoryDao.insert(orderHistory);
  }

  @After
  public void tearDown() {
    database.close();
    orderHistory = null;
  }

  @Test
  public void givenOrderHistory_whenInserted_thenOrderIsRetrievable() {
    // Given is handled in setUp().

    // When
    OrderHistory retrievedOrder = mOrderHistoryDao.getOrderHistoryByOrderId(orderHistoryId);

    // Then
    assertNotNull(retrievedOrder);
    assertEquals(testUserId, retrievedOrder.getUserId());
    assertEquals(testTheaterId, retrievedOrder.getTheaterId());
  }

  @Test
  public void givenExistingOrderHistory_whenUpdated_thenOrderIsUpdatedInDatabase() {
    // Given - Ensure same cart is updated in database.
    orderHistory.setOrderHistoryId(orderHistoryId);
    assertEquals(testTheaterId, orderHistory.getTheaterId());

    // Get new theater ID.
    long newGenreId = genreDao.insert(new Genre("Romantic Comedy"));
    long newMovieId = movieDao.insert(new Movie(newGenreId, "Me, Myself & Irene", 116, "R"));
    long newTheaterId = theaterDao.insert(new Theater(newMovieId, "Regal 16", "Natomas, CA", "4:00 PM", 7.99, 75));
    orderHistory.setTheaterId(newTheaterId);
    assertEquals(newTheaterId, orderHistory.getTheaterId());

    // When
    mOrderHistoryDao.update(orderHistory);
    OrderHistory updatedOrder = mOrderHistoryDao.getOrderHistoryByOrderId(orderHistoryId);

    // Then
    assertNotNull(updatedOrder);
    assertEquals(newTheaterId, updatedOrder.getTheaterId());
  }

  @Test
  public void givenExistingOrderHistory_whenDeleted_thenOrderIsNotRetrievable() {
    // Given

    // When
    orderHistory.setOrderHistoryId(orderHistoryId);
    mOrderHistoryDao.delete(orderHistory);
    OrderHistory retrievedOrder = mOrderHistoryDao.getOrderHistoryByOrderId(orderHistoryId);

    // Then
    assertNull(retrievedOrder);
  }
}
