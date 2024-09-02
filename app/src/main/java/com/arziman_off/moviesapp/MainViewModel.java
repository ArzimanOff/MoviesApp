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
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainViewModel extends AndroidViewModel {
    private final String LOG_TAG = "MainViewModel";
    private final MutableLiveData<List<Movie>> loadedMoviesLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Movie>> searchedMoviesLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isNowLoading = new MutableLiveData<>(false);
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private int page = 1;
    public MainViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData<List<Movie>> getLoadedMovies() {
        return loadedMoviesLiveData;
    }

    public MutableLiveData<List<Movie>> getSearchedMoviesLiveData() {
        return searchedMoviesLiveData;
    }

    public LiveData<Boolean> getIsNowLoading() {
        return isNowLoading;
    }
    public void loadMovies(){
        Boolean nowLoading = isNowLoading.getValue();
        if (nowLoading != null && nowLoading){
            return;
        }
        Disposable disposable = ApiFactory.apiService.loadMovies(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Throwable {
                        isNowLoading.setValue(true);
                    }
                })
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Throwable {
                        isNowLoading.setValue(false);
                    }
                })
                .map(new Function<MovieResponse, List<Movie>>() {
                    @Override
                    public List<Movie> apply(MovieResponse movieResponse) throws Throwable {
                        return movieResponse.getMovies();
                    }
                })
                .subscribe(
                        new Consumer<List<Movie>>() {
                            @Override
                            public void accept(List<Movie> moviesList) throws Throwable {
                                List<Movie> loadedMovies = loadedMoviesLiveData.getValue();
                                if (loadedMovies != null){
                                    loadedMovies.addAll(moviesList);
                                    loadedMoviesLiveData.setValue(loadedMovies);
                                } else {
                                    loadedMoviesLiveData.setValue(moviesList);
                                }
                                Log.d(LOG_TAG,"loaded: "+ page);
                                page++;
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


    public void searchMovie(String query){
        Disposable disposable = ApiFactory.apiService
                .searchMovieByName(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Throwable {
                        isNowLoading.setValue(true);
                    }
                })
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Throwable {
                        isNowLoading.setValue(false);
                    }
                })
                .map(new Function<MovieResponse, List<Movie>>() {
                    @Override
                    public List<Movie> apply(MovieResponse movieResponse) throws Throwable {
                        return movieResponse.getMovies();
                    }
                })
                .subscribe(
                        new Consumer<List<Movie>>() {
                            @Override
                            public void accept(List<Movie> moviesList) throws Throwable {
                                searchedMoviesLiveData.setValue(moviesList);
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
