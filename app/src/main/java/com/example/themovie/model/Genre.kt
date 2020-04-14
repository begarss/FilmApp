package com.example.themovie.model

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class Genre(
    @SerializedName ("id")val id: Int,
    @SerializedName ("name")val name: String
)