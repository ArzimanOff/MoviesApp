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

public class AllReviewsViewModel extends AndroidViewModel {
    private final String LOG_TAG = "AllReviewsViewModel";
    private final MutableLiveData<List<MovieReview>> movieBigReviews = new MutableLiveData<>();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public LiveData<List<MovieReview>> getMovieBigReviews() {
        return movieBigReviews;
    }

    public AllReviewsViewModel(@NonNull Application application) {
        super(application);
    }

    public void loadAllMovieReviews(int filmId) {
        Disposable disposable = ApiFactory.apiService
                .loadAllReviews(filmId)
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
                                movieBigReviews.setValue(reviewsList);
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
