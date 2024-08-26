package com.arziman_off.moviesapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

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

public class SavedMoviesActivity extends AppCompatActivity {

    private SavedMoviesViewModel savedMoviesViewModel;
    private ImageView goBackBtn;
    private RecyclerView recyclerViewSavedMovies;
    private MoviesAdapter savedMoviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_saved_movies);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initMainViews();
        savedMoviesAdapter = new MoviesAdapter();
        recyclerViewSavedMovies.setAdapter(savedMoviesAdapter);
        recyclerViewSavedMovies.setLayoutManager(new GridLayoutManager(this, 2));
        savedMoviesAdapter.setOnMovieItemClickListener(
                new MoviesAdapter.OnMovieItemClickListener() {
                    @Override
                    public void onMovieClick(Movie movie) {
                        Intent intent = MovieDetailActivity.newIntent(
                                SavedMoviesActivity.this,
                                movie
                        );
                        startActivity(intent);
                    }
                }
        );
        savedMoviesViewModel = new ViewModelProvider(this)
                .get(SavedMoviesViewModel.class);
        savedMoviesViewModel.showSavedMoviesList().observe(
                this,
                new Observer<List<Movie>>() {
                    @Override
                    public void onChanged(List<Movie> movies) {
                        savedMoviesAdapter.setMovies(movies);
                    }
                }
        );
        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initMainViews() {
        recyclerViewSavedMovies = findViewById(R.id.recyclerViewSavedMovies);
        goBackBtn = findViewById(R.id.goBackBtn);
    }

    public static Intent newIntent(Context context){
        return new Intent(context, SavedMoviesActivity.class);
    }

}