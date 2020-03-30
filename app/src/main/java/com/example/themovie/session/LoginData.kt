package com.example.themovie.session

import com.google.gson.annotations.SerializedName

class LoginData(username:String, password:String, token: String?){
    @SerializedName("username")  var username: String =""
    @SerializedName("password")  var password: String =""
    @SerializedName("request_token")  var request_token:String=""
    init {
        this.username=username
        this.password=password
        request_token=token!!
        
    }
}