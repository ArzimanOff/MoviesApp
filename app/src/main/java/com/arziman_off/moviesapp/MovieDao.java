package com.arziman_off.moviesapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM saved_movies")
    LiveData<List<Movie>> getAllSavedMovies();

    @Query("SELECT * FROM saved_movies WHERE id = :movieId")
    LiveData<Movie> getSavedMovie(int movieId);

    @Insert
    Completable saveMovie(Movie movie);

    @Query("DELETE FROM saved_movies WHERE id = :movieId")
    Completable removeMovie(int movieId);
}
