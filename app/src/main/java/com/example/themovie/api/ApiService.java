package com.example.themovie.api;

import android.app.Application;

import com.example.themovie.model.MovieResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public  class ApiService extends Application {

    private static MoviApi moviApi;
    private Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();

        retrofit = new Retrofit.Builder().baseUrl("https://api.themoviedb.org/3/").addConverterFactory(GsonConverterFactory.create()).build();
        moviApi = retrofit.create(MoviApi.class);
    }

    public static MoviApi getApi(){
        return moviApi;
    }
}