package com.example.themovie.model

import com.google.gson.annotations.SerializedName

data class Movie(

    @SerializedName("popularity")
    val populatiry: Double?=null,
    @SerializedName("vote_count")
    val vote_count: Int?=null,
    @SerializedName("video")
    val video: Boolean?=null,
    @SerializedName("poster_path")
    val poster_path: String?=null,
    @SerializedName("id")
    val id: Int,
    @SerializedName("adult")
    val adult: Boolean?=null,
    @SerializedName("backdrop_path")
    val backdrop_path: String?=null,
    @SerializedName("original_language")
    val original_language: String?=null,
    @SerializedName("original_title")
    val original_title: String?=null,
    @SerializedName("genre_ids")
    val genre_ids: List<Int>?=null,
    @SerializedName("title")
    val title: String?=null,
    @SerializedName("vote_average")
    val vote_average: Double?=null,
    @SerializedName("overview")
    val overview: String?=null,
    @SerializedName("release_date")
    val release_date: String?=null
) {
    val baseImageUrl: String = "https://image.tmdb.org/t/p/w500"

    fun getPosterPath(): String {
        return "https://image.tmdb.org/t/p/w500" + poster_path
    }
}