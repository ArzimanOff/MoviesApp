package com.arziman_off.moviesapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movie_timestamps")
public class SavedMovieTimestamp {
    @PrimaryKey
    private int movieId;

    private long addedTimestamp;

    public SavedMovieTimestamp(int movieId, long addedTimestamp) {
        this.movieId = movieId;
        this.addedTimestamp = addedTimestamp;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public long getAddedTimestamp() {
        return addedTimestamp;
    }

    public void setAddedTimestamp(long addedTimestamp) {
        this.addedTimestamp = addedTimestamp;
    }
}

