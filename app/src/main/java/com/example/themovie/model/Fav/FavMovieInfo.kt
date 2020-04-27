package com.example.themovie.model.Fav

import com.google.gson.annotations.SerializedName

class FavMovieInfo(
    favorite: Boolean,
    mediaId: Int?,
    mediaType: String
) {
    @SerializedName("favorite")
    var favorite: Boolean = false
    @SerializedName("media_id")
    var mediaId: Int = 0
    @SerializedName("media_type")
    var mediaType: String = ""

    init {
        this.favorite = favorite
        if (mediaId != null) {
            this.mediaId = mediaId
        }
        this.mediaType = mediaType

    }
}