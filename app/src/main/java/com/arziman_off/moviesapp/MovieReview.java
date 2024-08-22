package com.arziman_off.moviesapp;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class MovieReview {
    @SerializedName("id")
    private int id;

    @SerializedName("movieId")
    private int movieId;

    @SerializedName("title")
    private String title;

    @SerializedName("type")
    private String type;

    @SerializedName("review")
    private String text;

    @SerializedName("author")
    private String author;

    @SerializedName("reviewLikes")
    private int reviewLikes;

    @SerializedName("reviewDislikes")
    private int reviewDislikes;

    public MovieReview(int id, int movieId, String title, String type, String text, String author, int reviewLikes, int reviewDislikes) {
        this.id = id;
        this.movieId = movieId;
        this.title = title;
        this.type = type;
        this.text = text;
        this.author = author;
        this.reviewLikes = reviewLikes;
        this.reviewDislikes = reviewDislikes;
    }

    public int getId() {
        return id;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public String getAuthor() {
        return author;
    }

    public int getReviewLikes() {
        return reviewLikes;
    }

    public int getReviewDislikes() {
        return reviewDislikes;
    }

    @NonNull
    @Override
    public String toString() {
        return "MovieReview{" +
                "id=" + id +
                ", movieId=" + movieId +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", text='" + text + '\'' +
                ", author='" + author + '\'' +
                ", reviewLikes=" + reviewLikes +
                ", reviewDislikes=" + reviewDislikes +
                '}';
    }
}
