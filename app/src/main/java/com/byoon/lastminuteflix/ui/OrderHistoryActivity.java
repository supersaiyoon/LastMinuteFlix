package com.byoon.lastminuteflix.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.byoon.lastminuteflix.R;
import com.byoon.lastminuteflix.db.AppDatabase;
import com.byoon.lastminuteflix.db.MovieDao;
import com.byoon.lastminuteflix.db.OrderHistoryDao;
import com.byoon.lastminuteflix.db.TheaterDao;
import com.byoon.lastminuteflix.entity.Movie;
import com.byoon.lastminuteflix.entity.OrderHistory;
import com.byoon.lastminuteflix.entity.Theater;

import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {
  RecyclerView mOrderHistoryRecyclerView;
  private OrderHistoryDao mOrderHistoryDao;
  private AppDatabase mAppDatabase;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_order_history);

    mAppDatabase = AppDatabase.getInstance(this);
    mOrderHistoryDao = mAppDatabase.getOrderHistoryDao();

    mOrderHistoryRecyclerView = findViewById(R.id.recycler_view_past_showtimes);
    mOrderHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    loadOrderHistory();
  }

  private void loadOrderHistory() {
    List<OrderHistory> orders = mOrderHistoryDao.getAllOrderHistory();
    TheaterDao theaterDao = mAppDatabase.getTheaterDao();
    MovieDao movieDao = mAppDatabase.getMovieDao();

    for (OrderHistory order : orders) {
      Theater theater = theaterDao.getTheaterById(order.getTheaterId());
      if (theater != null) {
        Movie movie = movieDao.getMovieById(theater.getMovieId());
        if (movie != null) {
          // Update order with additional details to use for displaying in order history.
          order.setTheaterName(theater.getTheaterName());
          order.setMovieTitle(movie.getTitle());
          order.setShowtime(theater.getShowTime());
          order.setTicketPrice(theater.getTicketPrice());
        }
      }
    }

    OrderHistoryAdapter orderHistoryAdapter = new OrderHistoryAdapter(this, orders);
    mOrderHistoryRecyclerView.setAdapter(orderHistoryAdapter);
  }
}