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
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainViewModel extends AndroidViewModel {
    private final String LOG_TAG = "MainViewModel";
    private final MutableLiveData<List<Movie>> movies = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isNowLoading = new MutableLiveData<>();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private int page = 1;
    public MainViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData<List<Movie>> getMovies() {
        return movies;
    }
    public LiveData<Boolean> getIsNowLoading() {
        return isNowLoading;
    }
    public void loadMovies(){
        Disposable disposable = ApiFactory.apiService.loadMovies(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Throwable {
                        isNowLoading.setValue(true);
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Throwable {
                        isNowLoading.setValue(false);
                    }
                })
                .subscribe(
                        new Consumer<MovieResponse>() {
                            @Override
                            public void accept(MovieResponse movieResponse) throws Throwable {
                                page++;
                                movies.setValue(movieResponse.getMovies());
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
