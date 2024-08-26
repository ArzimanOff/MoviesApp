package com.arziman_off.moviesapp;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Movie.class, SavedMovieTimestamp.class}, version = 2, exportSchema = false)
public abstract class SavedMovieDatabase extends RoomDatabase {
    private static final String DB_NAME = "saved_movie.db";
    private static SavedMovieDatabase instance = null;
    public static SavedMovieDatabase getInstance(Application application){
        if (instance == null){
            instance = Room.databaseBuilder(
                    application,
                    SavedMovieDatabase.class,
                    DB_NAME
            ).build();
        }
        return instance;
    }

    abstract MovieDao movieDao();
}
