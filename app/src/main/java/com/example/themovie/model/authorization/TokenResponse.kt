package com.example.themovie.model.authorization

import com.google.gson.annotations.SerializedName

data class RequestToken(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("expires_at")
    val expires_at: String,
    @SerializedName("request_token")
    val requestToken: String
)