package com.example.themovie.api;

import com.example.themovie.model.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MoviApi {
    @GET("movie/popular")
    Call<MovieResponse> getPopularMoviesList(@Query("api_key") String apiKey);


}
