package com.arziman_off.moviesapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

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
    private EditText search_edit_text;
    private ImageView search_bar_cleaner;

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

        viewModel.loadMovies();
        viewModel.getLoadedMovies().observe(this, new Observer<List<Movie>>() {
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
        viewModel.getSearchedMoviesLiveData().observe(this, new Observer<List<Movie>>() {
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
    }

    private void initViews() {
        loadingProgressBarBox = findViewById(R.id.loadingProgressBarBox);
        recyclerViewMovies = findViewById(R.id.recyclerViewMovies);
        ImageView openSavedListBtn = findViewById(R.id.openSavedListBtn);
        openSavedListBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = SavedMoviesActivity.newIntent(v.getContext());
                        startActivity(intent);
                    }
                }
        );
        search_edit_text = findViewById(R.id.search_edit_text);
        search_bar_cleaner = findViewById(R.id.search_bar_cleaner);
        search_bar_cleaner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moviesAdapter.setSearchMode(false);
                moviesAdapter.setMovies(viewModel.getLoadedMovies().getValue());
                search_edit_text.setText("");

                // закрываем клавиатуру и убираем фокус с поля ввода
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                search_edit_text.clearFocus();
            }
        });
        MaterialButton search_btn = findViewById(R.id.search_btn);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userQuery = search_edit_text.getText().toString();
//                if (userQuery.trim().length() <= 2)
                moviesAdapter.setSearchMode(true);

                // закрываем клавиатуру и убираем фокус с поля ввода
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                search_edit_text.clearFocus();

                viewModel.searchMovie(userQuery);
            }
        });
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