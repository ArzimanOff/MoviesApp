package com.arziman_off.moviesapp;

import com.google.gson.annotations.SerializedName;


public class MovieTrailer{
    @SerializedName("url")
    private String url;

    @SerializedName("name")
    private String name;

    public MovieTrailer(String url, String name) {
        this.url = url;
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "MovieTrailer{" +
                "url='" + url + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
