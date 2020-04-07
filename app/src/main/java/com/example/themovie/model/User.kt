package com.example.themovie.model

import com.google.gson.annotations.SerializedName

data class User( //Описываем юзера, нужно добавить параметры
    @SerializedName("id") val postId: Int,
    @SerializedName("email") val userId: String,
    @SerializedName("token") val title: String
)


