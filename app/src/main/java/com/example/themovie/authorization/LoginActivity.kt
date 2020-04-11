package com.example.themovie.authorization

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.themovie.BuildConfig
import com.example.themovie.Fav.RequestSession
import com.example.themovie.Fav.SessionId
import com.example.themovie.activity.MainActivity
import com.example.themovie.R
import com.example.themovie.api.MovieApi
import com.example.themovie.api.RetrofitService
import kotlinx.android.synthetic.main.login_activity.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var btnlogin: Button
    lateinit var preferences: SharedPreferences
    private lateinit var progressBar: ProgressBar
    var requestedToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        email = findViewById(R.id.tv_login)
        password = findViewById(R.id.tv_psw)
        btnlogin = findViewById(R.id.b_login)
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.INVISIBLE

        preferences =
            getSharedPreferences("tkn", Context.MODE_PRIVATE)
        btnlogin.setOnClickListener {
            getToken()
            progressBar.visibility = View.VISIBLE
        }


    }

    fun getToken() {
        try {
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()) {
                return
            }
            val api: MovieApi? = RetrofitService.getClient()?.create(MovieApi::class.java)
            api?.getRequestToken(BuildConfig.THE_MOVIE_DB_API_TOKEN)
                ?.enqueue(object : Callback<RequestToken> {
                    override fun onFailure(call: Call<RequestToken>, t: Throwable) {
                        Toast.makeText(this@LoginActivity, "Error api ket", Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(
                        call: Call<RequestToken>,
                        response: Response<RequestToken>
                    ) {
                        if (response.body()?.success == true) {
                            requestedToken = response.body()?.requestToken
                            login()
                        }
                    }
                })
        } catch (e: Exception) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    fun login() {
        try {
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()) {
                return
            }
            val api: MovieApi? = RetrofitService.getClient()?.create(MovieApi::class.java)
            api?.login(LoginData(email.text.toString(), tv_psw.text.toString(), requestedToken))
                ?.enqueue(object : Callback<RequestToken> {
                    override fun onFailure(call: Call<RequestToken>, t: Throwable) {
                        Toast.makeText(this@LoginActivity, "Incorrect data", Toast.LENGTH_SHORT)
                            .show()
                    }

                    override fun onResponse(
                        call: Call<RequestToken>,
                        response: Response<RequestToken>
                    ) {
                        if (response.body()?.success == true) {
                            Log.d("login", "good")
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            getSessionId(requestedToken)
                            startActivity(intent)
                            progressBar.visibility = View.GONE
                            Toast.makeText(this@LoginActivity, "Accessed ", Toast.LENGTH_LONG)
                                .show()
                        }
                    }

                })
        } catch (e: Exception) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()

        }
    }

    fun getSessionId(token: String?) {
        Log.d("pusk", token)
        try {
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()) {
                return
            }
            val api: MovieApi? = RetrofitService.getClient()?.create(MovieApi::class.java)
            api?.getSession(SessionId(token))
                ?.enqueue(object : Callback<RequestSession> {
                    override fun onFailure(call: Call<RequestSession>, t: Throwable) {
                        Log.d("pusk", "failure occured")
                    }

                    override fun onResponse(
                        call: Call<RequestSession>,
                        response: Response<RequestSession>
                    ) {
                        if (response.body()?.success == true) {

                            Log.d("pusk", response.body()?.session_id)
                            val edt = preferences.edit()
                            edt.putString("sessionID", response.body()?.session_id)
                            edt.apply()

                        } else
                            Log.d("pusk", response.body().toString())

                    }

                })
        } catch (e: Exception) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()

        }
    }

}