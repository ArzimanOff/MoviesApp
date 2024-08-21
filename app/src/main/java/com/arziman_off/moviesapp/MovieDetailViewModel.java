package com.arziman_off.moviesapp;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MovieDetailViewModel extends AndroidViewModel {
    private final String LOG_TAG = "MovieDetailViewModel";
    private final MutableLiveData<List<MovieTrailer>> movieTrailers = new MutableLiveData<>();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    public MovieDetailViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<MovieTrailer>> getMovieTrailers() {
        return movieTrailers;
    }

    public void loadMovieTrailers(int filmId){
        Disposable disposable = ApiFactory.apiService
                .loadTrailers(filmId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<TrailerResponse, TrailersList>() {
                    @Override
                    public TrailersList apply(TrailerResponse trailerResponse) throws Throwable {
                        return trailerResponse.getTrailersList();
                    }
                })
                .subscribe(
                        new Consumer<TrailersList>() {
                            @Override
                            public void accept(TrailersList trailersList) throws Throwable {
                                if (trailersList != null){
                                    movieTrailers.setValue(trailersList.getTrailers());
                                }
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Throwable {
                                Log.d(LOG_TAG, throwable.toString());
                            }
                        }
                );
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
