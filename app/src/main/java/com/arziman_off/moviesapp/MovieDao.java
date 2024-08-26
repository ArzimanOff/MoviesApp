package com.arziman_off.moviesapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;


@Dao
public interface MovieDao {
    @Transaction
    @Query(
            "SELECT * FROM saved_movies " +
            "INNER JOIN movie_timestamps " +
            "ON saved_movies.id = movie_timestamps.movieId " +
            "ORDER BY movie_timestamps.addedTimestamp DESC"
    )
    LiveData<List<Movie>> getAllSavedMovies();

    @Query("SELECT * FROM saved_movies WHERE id = :movieId")
    LiveData<Movie> getSavedMovie(int movieId);

    @Insert
    Completable saveMovie(Movie movie);

    @Insert
    Completable saveMovieTimestamp(SavedMovieTimestamp savedMovieTimestamp);

    @Query("DELETE FROM saved_movies WHERE id = :movieId")
    Completable removeMovie(int movieId);

    @Query("DELETE FROM movie_timestamps WHERE movieId = :movieId")
    Completable removeMovieTimestamp(int movieId);
}
