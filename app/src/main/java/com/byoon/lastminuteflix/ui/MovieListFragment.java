package com.byoon.lastminuteflix.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.byoon.lastminuteflix.R;
import com.byoon.lastminuteflix.db.AppDatabase;
import com.byoon.lastminuteflix.db.MovieDao;
import com.byoon.lastminuteflix.entity.Movie;

import java.util.List;

public class MovieListFragment extends Fragment {

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_list, container, false);

    RecyclerView movieRecyclerView = view.findViewById(R.id.recycler_view);
    movieRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    // Fetch movie data from database
    MovieDao movieDao = AppDatabase.getInstance(getContext()).getMovieDao();
    List<Movie> movies = movieDao.getAllMovies();

    MovieAdapter adapter = new MovieAdapter(movies, movieDao);
    movieRecyclerView.setAdapter(adapter);

    return view;
  }
}
