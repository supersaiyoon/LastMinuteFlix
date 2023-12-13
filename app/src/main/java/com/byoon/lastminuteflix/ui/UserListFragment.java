package com.byoon.lastminuteflix.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.byoon.lastminuteflix.R;
import com.byoon.lastminuteflix.db.AppDatabase;
import com.byoon.lastminuteflix.db.UserDao;
import com.byoon.lastminuteflix.entity.User;

import java.util.List;

public class UserListFragment extends Fragment {
  private RecyclerView mUserRecyclerView;
  private UserAdapter mAdapter;
  private UserDao mUserDao;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_user_list, container, false);

    mUserRecyclerView = view.findViewById(R.id.recycler_view);
    mUserRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    // Fetch user data from database
    mUserDao = AppDatabase.getInstance(getContext()).getUserDao();
    List<User> users = mUserDao.getAllUsers();
    mAdapter = new UserAdapter(users, mUserDao);
    mUserRecyclerView.setAdapter(mAdapter);

    return view;
  }
}
