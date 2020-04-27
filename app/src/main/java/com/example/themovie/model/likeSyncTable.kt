package com.example.themovie.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movies_statuses")
data class MovieStatus(
    @PrimaryKey
    @SerializedName("id") val id: Int,
    @SerializedName("favorite") var favorite: Boolean
)