package com.arziman_off.moviesapp;

import com.google.gson.annotations.SerializedName;

public class Rating {

    @SerializedName("kp")
    private String kp;

    @SerializedName("imdb")
    private String imdb;

    public Rating(String kp, String imdb) {
        this.kp = kp;
        this.imdb = imdb;
    }

    public String getKp() {
        return kp;
    }

    public String getImdb() {
        return imdb;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "kp='" + kp + '\'' +
                ", imdb='" + imdb + '\'' +
                '}';
    }
}
