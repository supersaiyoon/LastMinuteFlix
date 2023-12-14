package com.byoon.lastminuteflix.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.byoon.lastminuteflix.R;
import com.byoon.lastminuteflix.db.MovieDao;
import com.byoon.lastminuteflix.entity.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
  private final List<Movie> mMovies;
  private final MovieDao mMovieDao;

  public MovieAdapter(List<Movie> movies, MovieDao movieDao) {
    mMovies = movies;
    mMovieDao = movieDao;
  }

  @NonNull
  @Override
  public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_list_item, parent, false);
    return new MovieViewHolder(view);
  }

  @Override
  public void onBindViewHolder(MovieViewHolder holder, int position) {
    Movie movie = mMovies.get(position);
    holder.mRecyclerMovieNameTextView.setText(movie.getTitle());
    holder.mRecyclerDeleteButton.setOnClickListener(v -> deleteMovie(position));
  }

  @Override
  public int getItemCount() {
    return mMovies.size();
  }

  static class MovieViewHolder extends RecyclerView.ViewHolder {
    // UI elements like TextView for displaying movie data and a delete button
    TextView mRecyclerMovieNameTextView;
    Button mRecyclerDeleteButton;

    MovieViewHolder(View itemView) {
      super(itemView);
      mRecyclerMovieNameTextView = itemView.findViewById(R.id.fragment_list_item_text);
      mRecyclerDeleteButton = itemView.findViewById(R.id.fragment_list_item_button_delete);
    }
  }

  private void deleteMovie(int position) {
    Movie movie = mMovies.get(position);
    long movieId = movie.getMovieId();

    // Delete movie from database.
    mMovieDao.deleteMovieByMovieId(movieId);

    // Remove movie from list and update RecyclerView.
    mMovies.remove(position);
    notifyItemRemoved(position);
  }
}