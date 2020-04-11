package com.example.themovie.authorization

import com.google.gson.annotations.SerializedName

class LoginData(username:String, password:String, token: String?){
    @SerializedName("username")  var username: String =""
    @SerializedName("password")  var password: String =""
    @SerializedName("request_token")  var requestToken:String=""
    init {
        this.username=username
        this.password=password
        if (token != null) {
            requestToken=token
        }
    }
}