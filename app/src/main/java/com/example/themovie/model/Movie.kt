package com.example.themovie.model

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "movie_table")
data class Movie(
    @PrimaryKey
    @SerializedName("id") val id: Int,
    @SerializedName("vote_count") val vote_count: Int?=null,
    @SerializedName("poster_path") val poster_path: String?=null,
    @SerializedName("original_title") val original_title: String?=null,
    @SerializedName("overview") val overview: String?=null,
    @SerializedName("release_date") val release_date: String?=null,

    @ColumnInfo(name = "ListData")
    @TypeConverters(GenresConverter::class)
    val genres: List<Genre>? = null
)
{
    fun getPosterPath(): String {
        return "https://image.tmdb.org/t/p/w500" + poster_path
    }
}