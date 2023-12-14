package com.byoon.lastminuteflix.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.byoon.lastminuteflix.R;
import com.byoon.lastminuteflix.db.AppDatabase;
import com.byoon.lastminuteflix.db.ShoppingCartDao;
import com.byoon.lastminuteflix.entity.ShoppingCart;
import com.byoon.lastminuteflix.entity.Theater;
import com.byoon.lastminuteflix.utils.KeyConstants;

import java.util.List;

public class BrowseShowtimeAdapter extends RecyclerView.Adapter<BrowseShowtimeAdapter.ShowtimeViewHolder> {
  private final List<Theater> mTheaters;
  private final ShoppingCartDao mShoppingCartDao;
  private final Context mContext;

  public BrowseShowtimeAdapter(Context context, List<Theater> theaters) {
    mContext = context;
    mTheaters = theaters;
    AppDatabase appDatabase = AppDatabase.getInstance(context);
    mShoppingCartDao = appDatabase.getShoppingCartDao();
  }

  @NonNull
  @Override
  public ShowtimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_list_item_add, parent, false);
    return new ShowtimeViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ShowtimeViewHolder holder, int position) {
    Theater theater = mTheaters.get(position);
    holder.mRecyclerShowtimeTextView.setText(theater.getTheaterName());
    holder.mRecyclerBuyButton.setOnClickListener(v -> addTheaterToCart(position));
  }

  @Override
  public int getItemCount() {
    return mTheaters.size();
  }

  static class ShowtimeViewHolder extends RecyclerView.ViewHolder {
    TextView mRecyclerShowtimeTextView;
    Button mRecyclerBuyButton;

    ShowtimeViewHolder(View itemView) {
      super(itemView);
      mRecyclerShowtimeTextView = itemView.findViewById(R.id.fragment_buy_list_item_text);
      mRecyclerBuyButton = itemView.findViewById(R.id.fragment_list_item_button_add_to_cart);
    }
  }

  private void addTheaterToCart(int position) {
    // Get user ID from SharedPreferences.
    SharedPreferences sharedPreferences = mContext.getSharedPreferences(KeyConstants.PREFERENCES_KEY.getKey(), Context.MODE_PRIVATE);
    int userId = sharedPreferences.getInt(KeyConstants.USER_ID_KEY.getKey(), -1);

    if (userId != -1) {
      Theater theater = mTheaters.get(position);
      ShoppingCart cart = new ShoppingCart(userId, theater.getTheaterId());
      mShoppingCartDao.insert(cart);
    }
  }
}
