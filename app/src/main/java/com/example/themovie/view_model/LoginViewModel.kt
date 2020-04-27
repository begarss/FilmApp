package com.example.themovie.view_model

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.themovie.BuildConfig
import com.example.themovie.model.Fav.RequestSession
import com.example.themovie.model.Fav.SessionId
import com.example.themovie.model.api.MovieApi
import com.example.themovie.model.api.RetrofitService
import com.example.themovie.model.authorization.LoginData
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class LoginViewModel(
    private val context: Context
) : ViewModel(), CoroutineScope {
    private val job = Job()

    val liveData = MutableLiveData<State>()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun getTkn() {
        liveData.value = State.ShowLoading
        launch {
            val api: MovieApi? = RetrofitService.getClient()?.create(MovieApi::class.java)
            val response = api?.getRequestToken2(BuildConfig.THE_MOVIE_DB_API_TOKEN)
            if (response?.body()?.success == true) {
                val requestedToken = response.body()?.requestToken
                if (requestedToken != null) {
                    liveData.value = State.startLogin(requestedToken)
                }
            } else {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun login2(email: String, password: String, requestedToken: String) {
        launch {
            val response =
                RetrofitService.getApi()?.login2(LoginData(email, password, requestedToken))
            if (response?.body()?.success == true) {
                val requestedToken = response.body()?.requestToken
                if (requestedToken != null) {
                    liveData.value = State.showActivity
                    liveData.value = State.saveUser
                    liveData.value = State.GetSession(requestedToken)
                    liveData.value = State.HideLoading
                }
            } else {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }
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
                            liveData.value = State.saveSession(response.body()?.session_id)
                        } else
                            Log.d("pusk", response.body().toString())
                    }
                })
        } catch (e: Exception) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    sealed class State {
        object ShowLoading : State()
        object HideLoading : State()
        class startLogin(val token: String) : State()
        object showActivity : State()
        object saveUser : State()
        class GetSession(val token: String) : State()
        class saveSession(val sessionId: String?) : State()
    }
}