package com.byoon.lastminuteflix.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.byoon.lastminuteflix.R;
import com.byoon.lastminuteflix.db.AppDatabase;
import com.byoon.lastminuteflix.db.OrderHistoryDao;
import com.byoon.lastminuteflix.db.TheaterDao;
import com.byoon.lastminuteflix.entity.OrderHistory;
import com.byoon.lastminuteflix.entity.Theater;

import java.util.List;
import java.util.Locale;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder> {
  private final List<OrderHistory> mPastOrders;
  private final OrderHistoryDao mOrderHistoryDao;
  private final TheaterDao mTheaterDao;
  private final Context mContext;

  public OrderHistoryAdapter(Context context, List<OrderHistory> pastOrders) {
    mContext = context;
    mPastOrders = pastOrders;
    AppDatabase appDatabase = AppDatabase.getInstance(context);
    mOrderHistoryDao = appDatabase.getOrderHistoryDao();
    mTheaterDao = appDatabase.getTheaterDao();
  }

  @NonNull
  @Override
  public OrderHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_order_history_list, parent, false);
    return new OrderHistoryViewHolder(view);
  }

  @Override
  public void onBindViewHolder(OrderHistoryViewHolder holder, int position) {
    OrderHistory orderHistory = mPastOrders.get(position);
    holder.mRecyclerOrderHistoryTheaterTextView.setText(orderHistory.getTheaterName());
    holder.mRecyclerOrderHistoryMovieTitleTextView.setText(orderHistory.getMovieTitle());
    holder.mRecyclerOrderHistoryShowtimeTextView.setText(orderHistory.getShowtime());
    holder.mRecyclerOrderHistoryTicketPriceTextView.setText(String.format(Locale.getDefault(), "$%.2f", orderHistory.getTicketPrice()));
    holder.mRecyclerCancelButton.setOnClickListener(v -> cancelOrder(position));
  }

  @Override
  public int getItemCount() {
    return mPastOrders.size();
  }

  static class OrderHistoryViewHolder extends RecyclerView.ViewHolder {
    TextView mRecyclerOrderHistoryTheaterTextView;
    TextView mRecyclerOrderHistoryMovieTitleTextView;
    TextView mRecyclerOrderHistoryShowtimeTextView;
    TextView mRecyclerOrderHistoryTicketPriceTextView;
    Button mRecyclerCancelButton;

    OrderHistoryViewHolder(View itemView) {
      super(itemView);
      mRecyclerOrderHistoryTheaterTextView = itemView.findViewById(R.id.fragment_order_history_list_theater_name);
      mRecyclerOrderHistoryMovieTitleTextView = itemView.findViewById(R.id.fragment_order_history_list_movie_title);
      mRecyclerOrderHistoryShowtimeTextView = itemView.findViewById(R.id.fragment_order_history_list_showtime);
      mRecyclerOrderHistoryTicketPriceTextView = itemView.findViewById(R.id.fragment_order_history_list_ticket_price);
      mRecyclerCancelButton = itemView.findViewById(R.id.fragment_order_history_list_cancel_button);
    }
  }

  private void cancelOrder(int position) {
    OrderHistory orderHistory = mPastOrders.get(position);

    // Fetch theater and update available seats before deleting order history.
    Theater theater = mTheaterDao.getTheaterById(orderHistory.getTheaterId());
    if (theater != null) {
      theater.setRemainingSeats(theater.getRemainingSeats() + 1);
      mTheaterDao.update(theater);
    }

    // Delete order from order history.
    mOrderHistoryDao.delete(orderHistory);

    // Update RecyclerView.
    mPastOrders.remove(position);
    notifyItemRemoved(position);
  }
}
