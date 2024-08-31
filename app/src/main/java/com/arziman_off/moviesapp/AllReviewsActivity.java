package com.arziman_off.moviesapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class AllReviewsActivity extends AppCompatActivity {
    private final String LOG_TAG = "AllReviewsActivity";
    private static final String EXTRA_MOVIE = "movie";
    private AllReviewsViewModel viewModel;
    private RecyclerView recyclerViewBigReviews;
    private ReviewsBigAdapter bigReviewsAdapter;
    private RadioGroup reviewsTypeSortBox;
    private RadioButton reviewAllTypesBtn;
    private RadioButton reviewHighTypesBtn;
    private RadioButton reviewMediumTypesBtn;
    private RadioButton reviewLowTypesBtn;
    private TextView reviewsBoxPlaceholder;

    private ImageView goBackBtn;
    private ImageView favoritesListBtn;
    private TextView movieReviewsListTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_reviews);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        viewModel = new ViewModelProvider(this).get(AllReviewsViewModel.class);
        initViews();
        Movie movie = (Movie) getIntent().getSerializableExtra(EXTRA_MOVIE);
        assert movie != null;
        movieReviewsListTitle.setText("Рецензии на фильм: " + movie.getName());
        showAllMovieReviews(movie);
        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        favoritesListBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = SavedMoviesActivity.newIntent(v.getContext());
                        startActivity(intent);
                    }
                }
        );
    }

    private void showAllMovieReviews(@NonNull Movie movie) {
        bigReviewsAdapter = new ReviewsBigAdapter();
        recyclerViewBigReviews.setAdapter(bigReviewsAdapter);
        viewModel.loadAllMovieReviews(movie.getId());
        viewModel.getMovieBigReviews().observe(this, new Observer<List<MovieReview>>() {
            @Override
            public void onChanged(List<MovieReview> reviews) {
                Log.d(LOG_TAG, reviews.toString());
                if (!reviews.isEmpty()) {
                    reviewsBoxPlaceholder.setVisibility(View.GONE);
                    recyclerViewBigReviews.setVisibility(View.VISIBLE);
                    bigReviewsAdapter.setBigReviewsList(reviews);
                }
            }
        });
        bigReviewsAdapter.setOnReviewItemClickListener(new ReviewsBigAdapter.OnReviewItemClickListener() {
            @Override
            public void onReviewClick(MovieReview movieReview) {
                ReviewDetailBottomSheet reviewDetailBottomSheet = ReviewDetailBottomSheet.newInstance(movieReview);
                reviewDetailBottomSheet.show(getSupportFragmentManager(), "reviewAllDetails");
            }
        });
    }

    private void initViews() {
        recyclerViewBigReviews = findViewById(R.id.recyclerViewBigReviews);
        reviewsTypeSortBox = findViewById(R.id.reviewsTypeSortBox);
        reviewAllTypesBtn = findViewById(R.id.reviewAllTypesBtn);
        reviewHighTypesBtn = findViewById(R.id.reviewHighTypesBtn);
        reviewMediumTypesBtn = findViewById(R.id.reviewMediumTypesBtn);
        reviewLowTypesBtn = findViewById(R.id.reviewLowTypesBtn);
        reviewsBoxPlaceholder = findViewById(R.id.reviewsBoxPlaceholder);
        goBackBtn = findViewById(R.id.goBackBtn);
        favoritesListBtn = findViewById(R.id.favorites_list_btn);
        movieReviewsListTitle = findViewById(R.id.movieReviewsListTitle);
    }

    public static Intent newIntent(Context context, Movie movie) {
        Intent intent = new Intent(context, AllReviewsActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);
        return intent;
    }
}