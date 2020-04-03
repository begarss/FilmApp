package com.example.themovie.api;

import com.example.themovie.authorization.LoginData;
import com.example.themovie.authorization.RequestToken;
import com.example.themovie.model.Movie;
import com.example.themovie.model.MovieResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApi {
    @GET("movie/popular")
    Call<MovieResponse> getPopularMoviesList(@Query("api_key") String apiKey);

//    @POST("authentication/token/validate_with_login")
//    Call<RequestBody> getRequestBody(@Query("api_key") String apiKey);

    @POST("authentication/token/validate_with_login?api_key=2f0d69a585b1ec8a833e56046239144b")
    Call<RequestToken> login(@Body LoginData loginData);

    @GET("authentication/token/new")
    Call<RequestToken> getRequestToken(@Query("api_key") String apiKey);

    @GET("movie/{movie_id}")
    Call<Movie> getMovieDetail(@Path ("movie_id") int id, @Query("api_key") String apiKey);
}
