package com.example.themovie.model

import androidx.room.ColumnInfo

data class Genre(
    @ColumnInfo(name = "genre_id")
    val id: Int,
    @ColumnInfo(name = "genre_name")
    val name: String
)