package com.byoon.lastminuteflix.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.byoon.lastminuteflix.R;
import com.byoon.lastminuteflix.db.AppDatabase;
import com.byoon.lastminuteflix.db.MovieDao;
import com.byoon.lastminuteflix.db.TheaterDao;
import com.byoon.lastminuteflix.entity.Movie;
import com.byoon.lastminuteflix.entity.Theater;

import java.util.List;

public class BrowseShowtimeActivity extends AppCompatActivity {
  RecyclerView mShowtimeRecyclerView;
  private TheaterDao mTheaterDao;
  private AppDatabase appDatabase;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_browse_showtime);

    appDatabase = AppDatabase.getInstance(this);
    mTheaterDao = appDatabase.getTheaterDao();

    mShowtimeRecyclerView = findViewById(R.id.recycler_view_showtimes);
    mShowtimeRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    // Fetch showtimes from database and set up adapter.
    loadShowtimes();
  }

  private void loadShowtimes() {
    List<Theater> theaters = mTheaterDao.getAllTheaters();
    MovieDao movieDao = appDatabase.getMovieDao();

    // Fetch movie titles for each movie theater.
    for (Theater theater : theaters) {
      Movie movie = movieDao.getMovieById(theater.getMovieId());
      if (movie != null) {
        theater.setMovieTitle(movie.getTitle());
      }
    }

    BrowseShowtimeAdapter showTimeAdapter = new BrowseShowtimeAdapter(this, theaters);
    mShowtimeRecyclerView.setAdapter(showTimeAdapter);
  }
}