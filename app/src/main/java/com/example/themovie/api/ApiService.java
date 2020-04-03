package com.example.themovie.api;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public  class ApiService extends Application {
    private static MovieApi movieApi;

    @Override
    public void onCreate() {
        super.onCreate();

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.themoviedb.org/3/").addConverterFactory(GsonConverterFactory.create()).build();
        movieApi = retrofit.create(MovieApi.class);
    }

    public static MovieApi getApi(){
        return movieApi;
    }
}