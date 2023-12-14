package com.byoon.lastminuteflix.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.byoon.lastminuteflix.R;
import com.byoon.lastminuteflix.db.AppDatabase;
import com.byoon.lastminuteflix.db.OrderHistoryDao;
import com.byoon.lastminuteflix.entity.OrderHistory;
import com.byoon.lastminuteflix.entity.Theater;
import com.byoon.lastminuteflix.utils.KeyConstants;

import java.util.List;
import java.util.Locale;

public class BrowseShowtimeAdapter extends RecyclerView.Adapter<BrowseShowtimeAdapter.ShowtimeViewHolder> {
  private final List<Theater> mTheaters;
  private final OrderHistoryDao mOrderHistoryDao;
  private final Context mContext;

  public BrowseShowtimeAdapter(Context context, List<Theater> theaters) {
    mContext = context;
    mTheaters = theaters;
    AppDatabase appDatabase = AppDatabase.getInstance(context);
    mOrderHistoryDao = appDatabase.getOrderHistoryDao();
  }

  @NonNull
  @Override
  public ShowtimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_browse_showtime_list, parent, false);
    return new ShowtimeViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ShowtimeViewHolder holder, int position) {
    Theater theater = mTheaters.get(position);
    holder.mRecyclerShowtimeTheaterTextView.setText(theater.getTheaterName());
    holder.mRecyclerShowtimeMovieTitleTextView.setText(theater.getMovieTitle());
    holder.mRecyclerShowtimeShowtimeTextView.setText(theater.getShowTime());
    holder.mRecyclerShowtimeTicketPriceTextView.setText(String.format(Locale.getDefault(), "$%.2f", theater.getTicketPrice()));
    holder.mRecyclerBuyButton.setOnClickListener(v -> buyShowtimeTicket(position));
  }

  @Override
  public int getItemCount() {
    return mTheaters.size();
  }

  static class ShowtimeViewHolder extends RecyclerView.ViewHolder {
    TextView mRecyclerShowtimeTheaterTextView;
    TextView mRecyclerShowtimeMovieTitleTextView;
    TextView mRecyclerShowtimeShowtimeTextView;
    TextView mRecyclerShowtimeTicketPriceTextView;
    Button mRecyclerBuyButton;

    ShowtimeViewHolder(View itemView) {
      super(itemView);
      mRecyclerShowtimeTheaterTextView = itemView.findViewById(R.id.fragment_browse_showtime_list_theater_name);
      mRecyclerShowtimeMovieTitleTextView = itemView.findViewById(R.id.fragment_browse_showtime_list_movie_title);
      mRecyclerShowtimeShowtimeTextView = itemView.findViewById(R.id.fragment_browse_showtime_list_showtime);
      mRecyclerShowtimeTicketPriceTextView = itemView.findViewById(R.id.fragment_browse_showtime_list_ticket_price);
      mRecyclerBuyButton = itemView.findViewById(R.id.fragment_browse_showtime_list_buy_button);
    }
  }

  private void buyShowtimeTicket(int position) {
    // Get user ID from SharedPreferences.
    SharedPreferences sharedPreferences = mContext.getSharedPreferences(KeyConstants.PREFERENCES_KEY.getKey(), Context.MODE_PRIVATE);
    int userId = sharedPreferences.getInt(KeyConstants.USER_ID_KEY.getKey(), -1);

    if (userId != -1) {
      Theater theater = mTheaters.get(position);
      OrderHistory order = new OrderHistory(userId, theater.getTheaterId());
      if (theater.getRemainingSeats() > 0) {
        mOrderHistoryDao.insert(order);
        theater.decrementRemainingSeats();
      }
      else {
        Toast.makeText(mContext, "Sold out! No more seats available.", Toast.LENGTH_SHORT).show();
      }
    }
  }
}
