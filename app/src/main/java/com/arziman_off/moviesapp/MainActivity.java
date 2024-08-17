package com.arziman_off.moviesapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

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
    private RecyclerView recyclerViewMovies;
    private LinearLayout loadingProgressBarBox;
    private MoviesAdapter moviesAdapter;
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
        viewModel.getIsNowLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isNowLoading) {
                if (isNowLoading){
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
    }
}