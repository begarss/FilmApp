package com.example.themovie.model.authorization

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
import androidx.lifecycle.ViewModelProvider
import com.example.themovie.BuildConfig
import com.example.themovie.model.Fav.RequestSession
import com.example.themovie.model.Fav.SessionId
import com.example.themovie.view.activity.MainActivity
import com.example.themovie.R
import com.example.themovie.model.api.MovieApi
import com.example.themovie.model.api.RetrofitService
import com.example.themovie.view_model.LoginViewModel
import com.example.themovie.view_model.MoviesListViewModel
import com.example.themovie.view_model.ViewModelProviderFactory
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
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        val viewModelProviderFactory = ViewModelProviderFactory(this)
        loginViewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(LoginViewModel::class.java)
        email = findViewById(R.id.tv_login)
        password = findViewById(R.id.tv_psw)
        btnlogin = findViewById(R.id.b_login)
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.INVISIBLE
        if (LoginSharedPref().getUserName(this)?.length == 0) {
            // call Login Activity
        } else {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)

        }
        preferences =
            getSharedPreferences("tkn", Context.MODE_PRIVATE)
        btnlogin.setOnClickListener {
            loginViewModel.getTkn()
            loginViewModel.liveData.observe(
                this,
                androidx.lifecycle.Observer { result ->
                    when (result) {
                        is LoginViewModel.State.ShowLoading -> {
                            progressBar.visibility = View.VISIBLE
                        }
                        is LoginViewModel.State.HideLoading -> {
                            progressBar.visibility = View.GONE
                        }
                        is LoginViewModel.State.startLogin -> {
                            loginViewModel.login2(
                                email = email.text.toString(),
                                password = password.text.toString(),
                                requestedToken = result.token
                            )
                        }
                        is LoginViewModel.State.showActivity -> {
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                        }
                        is LoginViewModel.State.saveUser -> {
                            LoginSharedPref().setUserName(this@LoginActivity, email.text.toString())

                        }
                        is LoginViewModel.State.GetSession -> {
                            loginViewModel.getSessionId(result.token)
                        }
                        is LoginViewModel.State.saveSession -> {
                            val edt = preferences.edit()
                            edt.putString("sessionID", result.sessionId)
                            edt.apply()
                        }
                    }
                })
        }
    }
}

