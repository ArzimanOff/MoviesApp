package com.arziman_off.moviesapp;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.function.Consumer;

public class TrailersList {
    @SerializedName("trailers")
    private List<MovieTrailer> trailers;

    public TrailersList(List<MovieTrailer> trailers) {
        this.trailers = trailers;
    }

    public List<MovieTrailer> getTrailers() {
        return trailers;
    }

    @Override
    public String toString() {
        return "TrailersList{" +
                "trailers=" + trailers +
                '}';
    }
}
