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

public class MovieDetailViewModel extends AndroidViewModel {
    private final String LOG_TAG = "MovieDetailViewModel";
    private final MutableLiveData<List<MovieTrailer>> movieTrailers = new MutableLiveData<>();
    private final MutableLiveData<List<MovieReview>> movieReviews = new MutableLiveData<>();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final MovieDao movieDao;

    public MovieDetailViewModel(@NonNull Application application) {
        super(application);
        movieDao = SavedMovieDatabase.getInstance(application).movieDao();
    }

    public LiveData<Movie> getSavedMovie(int movieId) {
        return movieDao.getSavedMovie(movieId);
    }

    public LiveData<List<MovieTrailer>> getMovieTrailers() {
        return movieTrailers;
    }

    public LiveData<List<MovieReview>> getMovieReviews() {
        return movieReviews;
    }

    public void insertMovieIntoSavedList(Movie movie) {
        long currentTime = System.currentTimeMillis();
        SavedMovieTimestamp movieTimestamp = new SavedMovieTimestamp(movie.getId(), currentTime);
        Disposable disposable = movieDao.saveMovie(movie)
                .andThen(movieDao.saveMovieTimestamp(movieTimestamp))
                .subscribeOn(Schedulers.io())
                .subscribe(
                        new Action() {
                            @Override
                            public void run() throws Throwable {
                                //TODO показать Toast о том что фильм сохранён в список
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Throwable {
                                //TODO показать Toast о том что фильм НЕ сохранён в список (ОШИБКА!)
                            }
                        }
                );
        compositeDisposable.add(disposable);
    }

    public void removeMovieFromSavedList(int movieId) {
        Disposable disposable = movieDao.removeMovie(movieId)
                .andThen(movieDao.removeMovieTimestamp(movieId))
                .subscribeOn(Schedulers.io())
                .subscribe(
                        new Action() {
                            @Override
                            public void run() throws Throwable {
                                //TODO показать Toast о том что фильм был удалён
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Throwable {
                                //TODO показать Toast о том что фильм НЕ удалился (ОШИБКА!)
                            }
                        }
                );
        compositeDisposable.add(disposable);
    }

    public void loadMovieTrailers(int filmId) {
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
                                if (trailersList != null) {
                                    movieTrailers.setValue(trailersList.getTrailers());
                                }
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Throwable {
                                Log.d(LOG_TAG, "TrailersList:" + throwable.toString());
                            }
                        }
                );
        compositeDisposable.add(disposable);
    }

    public void loadSomeMovieReviews(int filmId) {
        Disposable disposable = ApiFactory.apiService
                .loadSomeReviews(filmId, 5)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<MovieReviewResponse, List<MovieReview>>() {
                    @Override
                    public List<MovieReview> apply(MovieReviewResponse movieReviewResponse) throws Throwable {
                        return movieReviewResponse.getReviews();
                    }
                })
                .subscribe(
                        new Consumer<List<MovieReview>>() {
                            @Override
                            public void accept(List<MovieReview> reviewsList) throws Throwable {
                                for (MovieReview r : reviewsList) {
                                    Log.d("ReviewInfo", r.toString());
                                }
                                movieReviews.setValue(reviewsList);
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Throwable {
                                Log.d(LOG_TAG, "MovieReview:" + throwable.toString());
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
