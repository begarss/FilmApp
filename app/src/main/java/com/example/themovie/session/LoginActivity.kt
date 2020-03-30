package com.example.themovie.session

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.themovie.BuildConfig
import com.example.themovie.MainActivity
import com.example.themovie.R
import com.example.themovie.api.ApiService
import kotlinx.android.synthetic.main.login_activity.*
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var btnlogin: Button
    lateinit var register: Button
    lateinit var preferences: SharedPreferences
    var requestedToken : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        email = findViewById(R.id.tv_login)
        password = findViewById(R.id.tv_psw)
        btnlogin = findViewById(R.id.b_login)

        btnlogin.setOnClickListener {
            getToken()
        }


    }

    fun getToken()  {

        try {
            if(BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()){
                return
            }
            ApiService.getApi().getRequestToken(BuildConfig.THE_MOVIE_DB_API_TOKEN).enqueue(object : Callback<RequestToken>{
                override fun onFailure(call: Call<RequestToken>, t: Throwable) {
                    Toast.makeText(this@LoginActivity,"Error api ket", Toast.LENGTH_LONG)
                }

                override fun onResponse(
                    call: Call<RequestToken>,
                    response: Response<RequestToken>
                ) {
                    if(response.body()?.success==true){
                        requestedToken = response.body()?.request_token
                        Toast.makeText(this@LoginActivity, "Accessed ", Toast.LENGTH_LONG).show()
                        login()
                    }
                }

            })
        } catch (e: Exception) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT)

        }
    }

    fun login(){
        try {
            if(BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()){
                return
            }
           ApiService.getApi().login(LoginData(email.text.toString(),tv_psw.text.toString(),requestedToken))
               .enqueue(object : Callback<RequestToken>{
                   override fun onFailure(call: Call<RequestToken>, t: Throwable) {
                       Toast.makeText(this@LoginActivity, "Incorrect data", Toast.LENGTH_SHORT).show()

                   }

                   override fun onResponse(
                       call: Call<RequestToken>,
                       response: Response<RequestToken>
                   ) {
                       if(response.body()?.success==true){
                           val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                           //Toast.makeText(this@LoginActivity, requestedToken, Toast.LENGTH_LONG).show()
                       }
                   }

               })
        } catch (e: Exception) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT)

        }
    }

}