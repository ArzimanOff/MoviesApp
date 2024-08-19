package com.arziman_off.moviesapp;

import static com.arziman_off.moviesapp.MainActivity.setStyles;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

public class MovieDetailActivity extends AppCompatActivity {
    private static final String EXTRA_MOVIE = "movie";
    private ShapeableImageView movieDetailsPoster;
    private ImageView goBackBtn;
    private TextView kpRating;
    private TextView imdbRating;
    private TextView yearText;
    private TextView nameText;
    private TextView descriptionText;
    private TextView ageRatingBox;
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
        initViews();
        Movie movie = (Movie) getIntent().getSerializableExtra(EXTRA_MOVIE);

        assert movie != null;
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
        descriptionText.setText(movie.getDescription());
        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews(){
        goBackBtn = findViewById(R.id.goBackBtn);
        movieDetailsPoster = findViewById(R.id.movie_details_poster);
        kpRating = findViewById(R.id.movie_details_kp_rating);
        imdbRating = findViewById(R.id.movie_details_imdb_rating);
        yearText = findViewById(R.id.year_text);
        nameText = findViewById(R.id.name_text);
        descriptionText = findViewById(R.id.description_text);
        ageRatingBox = findViewById(R.id.age_rating_box);
    }

    public static Intent newIntent(Context context, Movie movie){
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);
        return intent;
    }
}