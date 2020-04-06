package com.example.themovie.api;

import com.example.themovie.Fav.FavMovieInfo;
import com.example.themovie.Fav.FavResponse;
import com.example.themovie.Fav.RequestSession;
import com.example.themovie.Fav.SessionId;
import com.example.themovie.authorization.LoginData;
import com.example.themovie.authorization.RequestToken;
import com.example.themovie.model.Movie;
import com.example.themovie.model.MovieResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApi {
    @GET("movie/popular")
    Call<MovieResponse> getPopularMoviesList(@Query("api_key") String apiKey,@Query("page") int page);

//    @POST("authentication/token/validate_with_login")
//    Call<RequestBody> getRequestBody(@Query("api_key") String apiKey);

    @POST("authentication/token/validate_with_login?api_key=2f0d69a585b1ec8a833e56046239144b")
    Call<RequestToken> login(@Body LoginData loginData);

    @POST("authentication/session/new?api_key=2f0d69a585b1ec8a833e56046239144b")
    Call<RequestSession> getSession(@Body SessionId sessionId);

    @GET("authentication/token/new")
    Call<RequestToken> getRequestToken(@Query("api_key") String apiKey);

    @GET("movie/{movie_id}")
    Call<Movie> getMovieDetail(@Path ("movie_id") int id, @Query("api_key") String apiKey);
    @GET("movie/{movie_id}/account_states")
    Call<Movie> getMovieState(@Path ("movie_id") int id, @Query("api_key") String apiKey,@Query("session_id") String session);

    @Headers("Content-Type:application/json; charset=UTF-8")
    @POST("account/9178480/favorite?api_key=2f0d69a585b1ec8a833e56046239144b")
    Call<FavResponse> addFavList(@Body FavMovieInfo movie,  @Query("session_id") String session);

    @GET("account/9178480/favorite/movies?api_key=2f0d69a585b1ec8a833e56046239144b")
    Call<MovieResponse> getFavList( @Query("session_id") String session);
}
