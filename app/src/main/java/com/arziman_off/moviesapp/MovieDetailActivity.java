package com.arziman_off.moviesapp;

import static com.arziman_off.moviesapp.MainActivity.setStyles;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MovieDetailActivity extends AppCompatActivity {
    private final String LOG_TAG = "MovieDetailActivity";
    private static final String EXTRA_MOVIE = "movie";

    private MovieDetailViewModel viewModel;
    private ShapeableImageView movieDetailsPoster;
    private ImageView goBackBtn;
    private TextView kpRating;
    private TextView imdbRating;
    private TextView yearText;
    private TextView nameText;
    private TextView descriptionText;
    private TextView ageRatingBox;
    private TextView trailersBoxPlaceholder;
    private TextView reviewsBoxPlaceholder;
    private RecyclerView recyclerViewTrailers;
    private TrailersAdapter trailersAdapter;
    private RecyclerView recyclerViewSmallReviews;
    private ImageButton likeThisMovieBtn;
    private ReviewsSmallAdapter smallReviewsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_movie_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(this).get(MovieDetailViewModel.class);
        initViews();

        Movie movie = (Movie) getIntent().getSerializableExtra(EXTRA_MOVIE);
        assert movie != null;
        setDetailsContent(movie);
        showMovieTrailer(movie);
        showSomeMovieReviews(movie);
        Drawable likeOn = ContextCompat.getDrawable(MovieDetailActivity.this, R.drawable.active_like_icon);
        Drawable likeOff = ContextCompat.getDrawable(MovieDetailActivity.this, R.drawable.inactive_like_icon);
        viewModel.getSavedMovie(movie.getId()).observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(Movie movieFromDB) {
                if (movieFromDB == null){
                    likeThisMovieBtn.setImageDrawable(likeOff);
                    likeThisMovieBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewModel.insertMovieIntoSavedList(movie);
                        }
                    });
                } else {
                    likeThisMovieBtn.setImageDrawable(likeOn);
                    likeThisMovieBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewModel.removeMovieFromSavedList(movie.getId());
                        }
                    });
                }
            }
        });

        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void setDetailsContent(@NonNull Movie movie) {
        Glide.with(this)
                .load(movie.getPoster().getUrl())
                .into(movieDetailsPoster);
        kpRating.setText(movie.getRating().getKp());
        setStyles(kpRating, movie.getRating().getKp());
        imdbRating.setText(movie.getRating().getImdb());
        setStyles(imdbRating, movie.getRating().getImdb());
        ageRatingBox.setText(
                getString(
                        R.string.ageRatingPlaceholder,
                        String.valueOf(movie.getAgeRating())
                )
        );
        yearText.setText(String.valueOf(movie.getYear()));
        nameText.setText(movie.getName());
        if (movie.getName().length() >= 30){
            nameText.setTextSize(16);
        }
        descriptionText.setText(movie.getDescription());
    }

    private void showMovieTrailer(@NonNull Movie movie) {
        trailersAdapter = new TrailersAdapter();
        recyclerViewTrailers.setAdapter(trailersAdapter);

        viewModel.loadMovieTrailers(movie.getId());
        viewModel.getMovieTrailers().observe(this, new Observer<List<MovieTrailer>>() {
            @Override
            public void onChanged(List<MovieTrailer> movieTrailers) {
                Log.d(LOG_TAG, movieTrailers.toString());
                if (!movieTrailers.isEmpty()) {
                    trailersBoxPlaceholder.setVisibility(View.GONE);
                    recyclerViewTrailers.setVisibility(View.VISIBLE);
                    trailersAdapter.setTrailers(movieTrailers);
                } else {
                    
                }
            }
        });
        trailersAdapter.setOnTrailerClickListener(new TrailersAdapter.OnTrailerClickListener() {
            @Override
            public void onTrailerClick(MovieTrailer trailer) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(trailer.getUrl()));
                startActivity(intent);
            }
        });
    }

    private void showSomeMovieReviews(@NonNull Movie movie) {
        smallReviewsAdapter = new ReviewsSmallAdapter();
        recyclerViewSmallReviews.setAdapter(smallReviewsAdapter);
        viewModel.loadSomeMovieReviews(movie.getId());
        viewModel.getMovieReviews().observe(this, new Observer<List<MovieReview>>() {
            @Override
            public void onChanged(List<MovieReview> reviews) {
                Log.d(LOG_TAG, reviews.toString());
                if (!reviews.isEmpty()) {
                    reviewsBoxPlaceholder.setVisibility(View.GONE);
                    recyclerViewSmallReviews.setVisibility(View.VISIBLE);
                    smallReviewsAdapter.setSmallReviewsList(reviews);
                }
            }
        });
        smallReviewsAdapter.setOnReviewItemClickListener(new ReviewsSmallAdapter.OnReviewItemClickListener() {
            @Override
            public void onReviewClick(MovieReview movieReview) {
                ReviewDetailBottomSheet reviewDetailBottomSheet = ReviewDetailBottomSheet.newInstance(movieReview);
                reviewDetailBottomSheet.show(getSupportFragmentManager(), "reviewAllDetails");
            }
        });
    }

    private void initViews() {
        goBackBtn = findViewById(R.id.goBackBtn);
        movieDetailsPoster = findViewById(R.id.movie_details_poster);
        kpRating = findViewById(R.id.movie_details_kp_rating);
        imdbRating = findViewById(R.id.movie_details_imdb_rating);
        yearText = findViewById(R.id.year_text);
        nameText = findViewById(R.id.name_text);
        descriptionText = findViewById(R.id.description_text);
        ageRatingBox = findViewById(R.id.age_rating_box);
        recyclerViewTrailers = findViewById(R.id.recyclerViewTrailers);
        recyclerViewSmallReviews = findViewById(R.id.reviews_recycler_view);
        trailersBoxPlaceholder = findViewById(R.id.trailersBoxPlaceholder);
        reviewsBoxPlaceholder = findViewById(R.id.reviewsBoxPlaceholder);
        likeThisMovieBtn = findViewById(R.id.likeThisMovieBtn);
    }

    public static Intent newIntent(Context context, Movie movie) {
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);
        return intent;
    }
}