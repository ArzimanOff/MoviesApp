package com.arziman_off.moviesapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;


import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private List<Movie> movies = new ArrayList<>();

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.movie_item_view,
                        parent,
                        false
                );
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        Glide.with(holder.itemView)
                .load(movie.getPoster().getUrl())
                .into(holder.imageViewMoviePoster);
        holder.movieName.setText(movie.getName());

        double kpRatingValue = Double.parseDouble(movie.getRating().getKp());
        holder.kpRatingText.setText(
                String.valueOf(
                        roundRating(kpRatingValue, 2)
                )
        );
        setBg(holder.kpRatingText, movie.getRating().getKp());

        double imdbRatingValue = Double.parseDouble(movie.getRating().getImdb());
        holder.imdbRatingText.setText(
                String.valueOf(
                        roundRating(imdbRatingValue, 2)
                )
        );
        setBg(holder.imdbRatingText, movie.getRating().getImdb());

        holder.ageRatingBox.setText(movie.getAgeRating() + "+");
    }

    private void setBg(TextView textView, String ratingValue) {
        if (Double.parseDouble(ratingValue) >= 7){
            textView.setBackgroundResource(R.drawable.rating_box_bg_high);
        } else if (Double.parseDouble(ratingValue) >= 5){
            textView.setBackgroundResource(R.drawable.rating_box_bg_medium);
        } else {
            textView.setBackgroundResource(R.drawable.rating_box_bg_low);
        }
    }

    public static double roundRating(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {

        private final TextView kpRatingText;
        private final TextView imdbRatingText;
        private final TextView ageRatingBox;
        private final ShapeableImageView imageViewMoviePoster;
        private final TextView movieName;
        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            kpRatingText = itemView.findViewById(R.id.kp_rating_text);
            imdbRatingText = itemView.findViewById(R.id.imdb_rating_text);
            ageRatingBox = itemView.findViewById(R.id.age_rating_box);
            imageViewMoviePoster = itemView.findViewById(R.id.imageViewMoviePoster);
            movieName = itemView.findViewById(R.id.movie_name);
        }
    }
}
