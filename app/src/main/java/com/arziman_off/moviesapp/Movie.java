package com.arziman_off.moviesapp;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Movie implements Serializable {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("year")
    private int year;

    @SerializedName("ageRating")
    private int ageRating;

    @SerializedName("poster")
    private Poster poster;

    @SerializedName("rating")
    private Rating rating;

    public Movie(int id, String name, String description, int year, int ageRating, Poster poster, Rating rating) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.year = year;
        this.ageRating = ageRating;
        this.poster = poster;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getYear() {
        return year;
    }

    public int getAgeRating() {
        return ageRating;
    }

    public Poster getPoster() {
        return poster;
    }

    public Rating getRating() {
        return rating;
    }

    @NonNull
    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", year=" + year +
                ", ageRating=" + ageRating +
                ", poster=" + poster.toString() +
                ", rating=" + rating.toString() +
                '}';
    }
}
