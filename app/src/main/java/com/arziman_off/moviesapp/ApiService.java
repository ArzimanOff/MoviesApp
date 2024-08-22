package com.arziman_off.moviesapp;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiService {
    String TOKEN = "S2KXNRX-NEW4DBH-MJKS18F-W58DB7W";

    @GET("movie?rating.kp=1-10&sortField=votes.kp&sortType=-1&limit=30")
    @Headers("X-API-KEY:" + TOKEN)
    Single<MovieResponse> loadMovies(@Query("page") int page);

    @GET("movie/{movieId}")
    @Headers("X-API-KEY:" + TOKEN)
    Single<TrailerResponse> loadTrailers(@Path("movieId") int movieId);

    @GET("review?limit=10")
    @Headers("X-API-KEY:" + TOKEN)
    Single<MovieReviewResponse> loadReviews(@Query("movieId") int movieId);
}
