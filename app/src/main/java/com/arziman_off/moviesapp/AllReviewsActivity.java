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
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
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
        List<MovieReview> allReviewsList = new ArrayList<>();
        List<MovieReview> highReviewsList = new ArrayList<>();
        List<MovieReview> mediumReviewsList = new ArrayList<>();
        List<MovieReview> lowReviewsList = new ArrayList<>();

        bigReviewsAdapter = new ReviewsBigAdapter();
        recyclerViewBigReviews.setAdapter(bigReviewsAdapter);
        viewModel.loadAllMovieReviews(movie.getId());
        viewModel.getMovieBigReviews().observe(this, new Observer<List<MovieReview>>() {
            @Override
            public void onChanged(List<MovieReview> reviews) {
                Log.d(LOG_TAG, reviews.toString());
                if (!reviews.isEmpty()) {
                    allReviewsList.addAll(reviews);
                    for (MovieReview r : reviews) {
                        String type = r.getType();
                        switch (type) {
                            case "Позитивный":
                                highReviewsList.add(r);
                                break;
                            case "Нейтральный":
                                mediumReviewsList.add(r);
                                break;
                            case "Негативный":
                                lowReviewsList.add(r);
                                break;
                        }
                    }
                    reviewsBoxPlaceholder.setVisibility(View.GONE);
                    recyclerViewBigReviews.setVisibility(View.VISIBLE);
                    bigReviewsAdapter.setBigReviewsList(allReviewsList);
                }
                reviewAllTypesBtn.setText("Все " + String.valueOf(allReviewsList.size()));
                reviewHighTypesBtn.setText(String.valueOf(highReviewsList.size()));
                reviewMediumTypesBtn.setText(String.valueOf(mediumReviewsList.size()));
                reviewLowTypesBtn.setText(String.valueOf(lowReviewsList.size()));
            }
        });
        reviewsTypeSortBox.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (reviewAllTypesBtn.isChecked()) {
                    bigReviewsAdapter.setBigReviewsList(allReviewsList);
                } else if (reviewHighTypesBtn.isChecked()) {
                    bigReviewsAdapter.setBigReviewsList(highReviewsList);
                } else if (reviewMediumTypesBtn.isChecked()) {
                    bigReviewsAdapter.setBigReviewsList(mediumReviewsList);
                } else {
                    bigReviewsAdapter.setBigReviewsList(lowReviewsList);
                }
                updateStyleChooseReviewsTypesRadioGroup();
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

    public void updateStyleChooseReviewsTypesRadioGroup() {
        int allReviewsRbTextColor = ContextCompat.getColor(this, R.color.accent_color);
        int highReviewsRbTextColor = ContextCompat.getColor(this, R.color.high_rating_color);
        int mediumReviewsRbTextColor = ContextCompat.getColor(this, R.color.medium_rating_color);
        int lowReviewsRbTextColor = ContextCompat.getColor(this, R.color.low_rating_color);
        int inactiveRbTextColor = ContextCompat.getColor(this, R.color.inactive_text_color);

        if (reviewAllTypesBtn.isChecked()) {
            // активируем нужные стили для текущего радиобокса
            reviewAllTypesBtn.setBackgroundResource(R.drawable.rb_active_btn_bg);
            reviewAllTypesBtn.setTextColor(allReviewsRbTextColor);

            // деактивируем нужные стили для остальных радиобоксов
            reviewHighTypesBtn.setBackgroundResource(R.drawable.movie_card_bg);
            reviewHighTypesBtn.setTextColor(inactiveRbTextColor);

            reviewMediumTypesBtn.setBackgroundResource(R.drawable.movie_card_bg);
            reviewMediumTypesBtn.setTextColor(inactiveRbTextColor);

            reviewLowTypesBtn.setBackgroundResource(R.drawable.movie_card_bg);
            reviewLowTypesBtn.setTextColor(inactiveRbTextColor);
        } else if (reviewHighTypesBtn.isChecked()) {
            // активируем нужные стили для текущего радиобокса
            reviewHighTypesBtn.setBackgroundResource(R.drawable.rating_box_bg_high);
            reviewHighTypesBtn.setTextColor(highReviewsRbTextColor);

            // деактивируем нужные стили для остальных радиобоксов
            reviewAllTypesBtn.setBackgroundResource(R.drawable.movie_card_bg);
            reviewAllTypesBtn.setTextColor(inactiveRbTextColor);

            reviewMediumTypesBtn.setBackgroundResource(R.drawable.movie_card_bg);
            reviewMediumTypesBtn.setTextColor(inactiveRbTextColor);

            reviewLowTypesBtn.setBackgroundResource(R.drawable.movie_card_bg);
            reviewLowTypesBtn.setTextColor(inactiveRbTextColor);
        } else if (reviewMediumTypesBtn.isChecked()) {
            // активируем нужные стили для текущего радиобокса
            reviewMediumTypesBtn.setBackgroundResource(R.drawable.rating_box_bg_medium);
            reviewMediumTypesBtn.setTextColor(mediumReviewsRbTextColor);

            // деактивируем нужные стили для остальных радиобоксов
            reviewAllTypesBtn.setBackgroundResource(R.drawable.movie_card_bg);
            reviewAllTypesBtn.setTextColor(inactiveRbTextColor);

            reviewHighTypesBtn.setBackgroundResource(R.drawable.movie_card_bg);
            reviewHighTypesBtn.setTextColor(inactiveRbTextColor);

            reviewLowTypesBtn.setBackgroundResource(R.drawable.movie_card_bg);
            reviewLowTypesBtn.setTextColor(inactiveRbTextColor);
        } else {
            // активируем нужные стили для текущего радиобокса
            reviewLowTypesBtn.setBackgroundResource(R.drawable.rating_box_bg_low);
            reviewLowTypesBtn.setTextColor(lowReviewsRbTextColor);

            // деактивируем нужные стили для остальных радиобоксов
            reviewAllTypesBtn.setBackgroundResource(R.drawable.movie_card_bg);
            reviewAllTypesBtn.setTextColor(inactiveRbTextColor);

            reviewHighTypesBtn.setBackgroundResource(R.drawable.movie_card_bg);
            reviewHighTypesBtn.setTextColor(inactiveRbTextColor);

            reviewMediumTypesBtn.setBackgroundResource(R.drawable.movie_card_bg);
            reviewMediumTypesBtn.setTextColor(inactiveRbTextColor);
        }
    }

    public static Intent newIntent(Context context, Movie movie) {
        Intent intent = new Intent(context, AllReviewsActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);
        return intent;
    }
}