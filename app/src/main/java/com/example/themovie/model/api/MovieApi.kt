package com.example.themovie.model.api

import com.example.themovie.model.Fav.*
import com.example.themovie.model.authorization.LoginData
import com.example.themovie.model.authorization.RequestToken
import com.example.themovie.model.Movie
import com.example.themovie.model.MovieResponse
import com.example.themovie.model.MovieStatus
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface MovieApi {

    @GET("movie/popular")
    fun getPopularMoviesList(@Query("api_key") apiKey: String, @Query("page") page: Int): Call<MovieResponse>

    @POST("authentication/token/validate_with_login?api_key=2f0d69a585b1ec8a833e56046239144b")
    fun login(@Body loginData: LoginData): Call<RequestToken>
    @POST("authentication/token/validate_with_login?api_key=2f0d69a585b1ec8a833e56046239144b")
    suspend fun login2(@Body loginData: LoginData): Response<RequestToken>
    @POST("authentication/session/new?api_key=2f0d69a585b1ec8a833e56046239144b")
    fun getSession(@Body sessionId: SessionId): Call<RequestSession>

    @GET("authentication/token/new")
    fun getRequestToken(@Query("api_key") apiKey: String): Call<RequestToken>
    @GET("authentication/token/new")
    suspend fun getRequestToken2(@Query("api_key") apiKey: String): Response<RequestToken>

    @GET("movie/{movie_id}")
    fun getMovieDetail(@Path("movie_id") id: Int, @Query("api_key") apiKey: String): Call<Movie>

    @Headers("Content-Type:application/json; charset=UTF-8")
    @POST("account/9178480/favorite?api_key=2f0d69a585b1ec8a833e56046239144b")
    suspend fun addFavList(@Body movie: FavMovieInfo, @Query("session_id") session: String?): Response<FavResponse>

    @GET("account/9178480/favorite/movies?api_key=2f0d69a585b1ec8a833e56046239144b")
    fun getFavList(@Query("session_id") session: String?): Call<MovieResponse>

    @GET("movie/{movie_id}/account_states")
    suspend fun getMovieState(@Path("movie_id") id: Int, @Query("api_key") apiKey: String?, @Query("session_id") session: String?): Response<MovieStatus>


    @GET("movie/popular")
    suspend fun getPopularMoviesListCoroutine(@Query("api_key") apiKey: String, @Query("page") page: Int): Response<MovieResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetailCoroutine(@Path("movie_id") id: Int, @Query("api_key") apiKey: String): Response<Movie>

    @GET("account/9178480/favorite/movies?api_key=2f0d69a585b1ec8a833e56046239144b")
    suspend fun getFavListCoroutine(@Query("session_id") session: String?): Response<FavMovieResponse>

}