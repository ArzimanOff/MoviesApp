package com.arziman_off.moviesapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class SavedMoviesViewModel extends AndroidViewModel{
    private final String LOG_TAG = "SavedMoviesViewModel";
    private final MovieDao movieDao;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    public SavedMoviesViewModel(@NonNull Application application) {
        super(application);
        movieDao = SavedMovieDatabase.getInstance(application).movieDao();
    }

    public LiveData<List<Movie>> showSavedMoviesList(){
        return movieDao.getAllSavedMovies();
    }

}
