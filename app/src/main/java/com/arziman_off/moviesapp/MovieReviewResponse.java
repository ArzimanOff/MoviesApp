package com.arziman_off.moviesapp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieReviewResponse {
    @SerializedName("docs")
    private List<MovieReview> reviews;

    @SerializedName("total")
    private int totalReviews;

    @SerializedName("limit")
    private int pageLimit;

    @SerializedName("page")
    private int currentPage;

    @SerializedName("pages")
    private int totalPages;

    public MovieReviewResponse(List<MovieReview> reviews, int totalReviews, int pageLimit, int currentPage, int totalPages) {
        this.reviews = reviews;
        this.totalReviews = totalReviews;
        this.pageLimit = pageLimit;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
    }

    public List<MovieReview> getReviews() {
        return reviews;
    }

    public int getTotalReviews() {
        return totalReviews;
    }

    public int getPageLimit() {
        return pageLimit;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    @Override
    public String toString() {
        return "MovieReviewResponse{" +
                "reviews=" + reviews +
                ", totalReviews=" + totalReviews +
                ", pageLimit=" + pageLimit +
                ", currentPage=" + currentPage +
                ", totalPages=" + totalPages +
                '}';
    }
}
