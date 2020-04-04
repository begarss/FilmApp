package com.example.themovie.Fav

import com.google.gson.annotations.SerializedName

 class FavMovieInfo(
     favorite: Boolean,
     media_id: Int?,
     media_type: String
){
    @SerializedName("favorite")  var favorite: Boolean = false
     @SerializedName("media_id")  var media_id: Int = 0
     @SerializedName("media_type")  var media_type:String=""
    init {
        this.favorite=favorite
        this.media_id= media_id!!
        this.media_type=media_type

    }
}