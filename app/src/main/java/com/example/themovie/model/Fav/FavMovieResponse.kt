package com.example.themovie.model.Fav

import com.example.themovie.model.FavMovies
import com.google.gson.annotations.SerializedName

data class FavMovieResponse(
    @SerializedName("results")
    val results: List<FavMovies>
)