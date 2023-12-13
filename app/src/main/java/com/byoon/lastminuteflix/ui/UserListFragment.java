package com.byoon.lastminuteflix.ui;

import android.os.Bundle;
import android.util.Log;
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

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_user_list, container, false);

    RecyclerView userRecyclerView = view.findViewById(R.id.recycler_view);
    userRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    // Fetch user data from database
    UserDao userDao = AppDatabase.getInstance(getContext()).getUserDao();
    List<User> users = userDao.getAllUsers();

    // DEBUG: Check the size of user list.
    Log.d("UserListFragment", "Number of users = " + users.size());

    UserAdapter adapter = new UserAdapter(users, userDao);
    userRecyclerView.setAdapter(adapter);

    return view;
  }
}
