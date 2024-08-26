package com.arziman_off.moviesapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.function.Consumer;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private final String LOG_TAG = "MainActivity";
    private MainViewModel viewModel;
    private LinearLayout loadingProgressBarBox;
    private RecyclerView recyclerViewMovies;
    private MoviesAdapter moviesAdapter;
    private ImageView openSavedListBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();
        moviesAdapter = new MoviesAdapter();
        recyclerViewMovies.setAdapter(moviesAdapter);
        recyclerViewMovies.setLayoutManager(new GridLayoutManager(this, 2));

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                moviesAdapter.setMovies(movies);
                movies.forEach(new Consumer<Movie>() {
                    @Override
                    public void accept(Movie movie) {
                        Log.d(LOG_TAG, movie.toString());
                    }
                });
            }
        });
        moviesAdapter.setOnReachEndListener(new MoviesAdapter.OnReachEndListener() {
            @Override
            public void onReachEnd() {
                viewModel.loadMovies();
            }
        });
        moviesAdapter.setOnMovieItemClickListener(new MoviesAdapter.OnMovieItemClickListener() {
            @Override
            public void onMovieClick(Movie movie) {
                Intent intent = MovieDetailActivity.newIntent(
                        MainActivity.this,
                        movie
                );
                startActivity(intent);
            }
        });
        viewModel.getIsNowLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isNowLoading) {
                if (isNowLoading && moviesAdapter.getItemCount() == 0) {
                    loadingProgressBarBox.setVisibility(View.VISIBLE);
                    recyclerViewMovies.setVisibility(View.GONE);
                } else {
                    loadingProgressBarBox.setVisibility(View.GONE);
                    recyclerViewMovies.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initViews() {
        loadingProgressBarBox = findViewById(R.id.loadingProgressBarBox);
        recyclerViewMovies = findViewById(R.id.recyclerViewMovies);
        openSavedListBtn = findViewById(R.id.openSavedListBtn);
        openSavedListBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = SavedMoviesActivity.newIntent(v.getContext());
                        startActivity(intent);
                    }
                }
        );
    }

    public static void setStyles(TextView textView, String ratingValue) {
        if (Double.parseDouble(ratingValue) >= 7) {
            textView.setBackgroundResource(R.drawable.rating_box_bg_high);
            textView.setTextColor(
                    textView.getContext()
                            .getResources()
                            .getColor(R.color.high_rating_color)
            );
        } else if (Double.parseDouble(ratingValue) >= 5) {
            textView.setBackgroundResource(R.drawable.rating_box_bg_medium);
            textView.setTextColor(
                    textView.getContext()
                            .getResources()
                            .getColor(R.color.medium_rating_color)
            );
        } else {
            textView.setBackgroundResource(R.drawable.rating_box_bg_low);
            textView.setTextColor(
                    textView.getContext()
                            .getResources()
                            .getColor(R.color.low_rating_color)
            );
        }
    }
}