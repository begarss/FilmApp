package com.example.themovie.api


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitService {

    const val BASE_URL = "http://api.themoviedb.org/3/"
    private var retrofit: Retrofit? = null
    private var movieApi: MovieApi? = null

    fun getClient(): Retrofit? {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit
    }

    fun getApi(): MovieApi? {
        movieApi = getClient()?.create(MovieApi::class.java)
        return movieApi
    }
}

