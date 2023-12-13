package com.byoon.lastminuteflix.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.byoon.lastminuteflix.R;
import com.byoon.lastminuteflix.db.UserDao;
import com.byoon.lastminuteflix.entity.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
  private final List<User> mUsers;
  private final UserDao mUserDao;

  public UserAdapter(List<User> users, UserDao userDao) {
    mUsers = users;
    mUserDao = userDao;
  }

  @NonNull
  @Override
  public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_user_list_item, parent, false);
    return new UserViewHolder(view);
  }

  @Override
  public void onBindViewHolder(UserViewHolder holder, int position) {
    User user = mUsers.get(position);
    holder.mRecyclerUserNameTextView.setText(user.getUsername());
    holder.mRecyclerDeleteButton.setOnClickListener(v -> deleteUser(position));
  }

  @Override
  public int getItemCount() {
    return mUsers.size();
  }

  static class UserViewHolder extends RecyclerView.ViewHolder {
    // UI elements like TextView for displaying user data and a delete button
    TextView mRecyclerUserNameTextView;
    Button mRecyclerDeleteButton;

    UserViewHolder(View itemView) {
      super(itemView);
      mRecyclerUserNameTextView = itemView.findViewById(R.id.fragment_text_username);
      mRecyclerDeleteButton = itemView.findViewById(R.id.fragment_button_delete_user);
    }
  }

  private void deleteUser(int position) {
    User user = mUsers.get(position);
    long userId = user.getUserId();

    // Delete user from database.
    mUserDao.deleteUserById(userId);

    // Remove user from list and update RecyclerView.
    mUsers.remove(position);
    notifyItemRemoved(position);
  }
}
