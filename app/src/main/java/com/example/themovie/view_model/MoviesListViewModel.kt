package com.example.themovie.view_model

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.themovie.BuildConfig
import com.example.themovie.model.*
import com.example.themovie.model.Fav.FavMovieInfo
import com.example.themovie.model.Fav.FavResponse
import com.example.themovie.model.api.MovieApi
import com.example.themovie.model.api.RetrofitService
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class MoviesListViewModel(
    private val context: Context
) : ViewModel(), CoroutineScope {
    private val job = Job()
    private val movieDao: MovieDao
    private val favDao: FavDao
    var isLiked:Boolean = false

    val liveData = MutableLiveData<State>()


    init {
        movieDao = MovieDatabase.getDatabase(context).movieDao()
        favDao = MovieDatabase.getDatabase(context).favDao()
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun getMovies() {
        launch {
            liveData.value = State.ShowLoading
            val list = withContext(Dispatchers.IO) {
                try {
                    val api: MovieApi? = RetrofitService.getClient()?.create(MovieApi::class.java)
                    val response =
                        api?.getPopularMoviesListCoroutine(BuildConfig.THE_MOVIE_DB_API_TOKEN, 1)
                    if (response?.isSuccessful!!) {
                        val result = response.body()
                        if (!result?.results.isNullOrEmpty()) {
                            movieDao.insertAll(result?.results!!)
                        }
                        result?.results
                    } else {
                        movieDao.getAll() ?: emptyList<Movie>()
                    }
                } catch (e: Exception) {
                    Log.e("Moviedatabase", e.toString())
                    movieDao.getAll() ?: emptyList<Movie>()
                }
            }
            liveData.value = State.HideLoading
            liveData.value = State.Result(list)
        }
    }

    fun getFavMovies(sessionId: String?) {
        launch {
            liveData.value = State.ShowLoading
            val list = withContext(Dispatchers.IO) {
                try {
                    val api: MovieApi? = RetrofitService.getClient()?.create(MovieApi::class.java)
                    val response = api?.getFavListCoroutine(sessionId)
                    if (response?.isSuccessful!!) {
                        val result = response.body()
                        if (!result?.results.isNullOrEmpty()) {
                            favDao?.insertFav(result?.results)
                        }
                        result?.results
                    } else {
                        favDao?.getFav() ?: emptyList<Movie>()
                    }
                } catch (e: Exception) {
                    Log.e("Moviedatabase", e.toString())
                    favDao?.getFav() ?: emptyList<Movie>()
                }

            }
            liveData.value = State.HideLoading
            liveData.value = State.FavResult(list as List<FavMovies>?)
        }
    }

    fun getMovieDetailCoroutine(id: Int) {
        launch {
            val movie = withContext(Dispatchers.IO) {
                try {
                    val api: MovieApi? = RetrofitService.getClient()?.create(MovieApi::class.java)
                    val response =
                        api?.getMovieDetailCoroutine(id, BuildConfig.THE_MOVIE_DB_API_TOKEN)
                    if (response!!.isSuccessful) {
                        val result = response.body()
                        result
                    } else {
                        movieDao?.getMovie(id)
                    }
                } catch (e: java.lang.Exception) {
                    movieDao?.getMovie(id)
                }
            }
            liveData.value = State.MovieDetails(movie)

        }
    }

    fun markAsFav(info: FavMovieInfo, sessionId: String?) {
        try {
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()) {
                return
            }
            RetrofitService.getApi()
                ?.addFavList(info, sessionId)
                ?.enqueue(object : Callback<FavResponse> {
                    override fun onFailure(call: Call<FavResponse>, t: Throwable) {
                        Log.d("fav", "lol")
                    }

                    override fun onResponse(
                        call: Call<FavResponse>,
                        response: Response<FavResponse>
                    ) {
                        Log.d("pusk", response.toString())

                    }

                })
        } catch (e: Exception) {
            Log.d("mark", e.toString())
        }
    }

    fun getState(movieId: Int?, sessionId: String?): Boolean? {
        try {
            if (movieId != null) {
                RetrofitService.getApi()
                    ?.getMovieState(movieId, BuildConfig.THE_MOVIE_DB_API_TOKEN, sessionId)
                    ?.enqueue(object : Callback<Movie?> {
                        override fun onFailure(call: Call<Movie?>, t: Throwable) {
                            Log.d("fav", "lol")
                        }

                        override fun onResponse(call: Call<Movie?>, response: Response<Movie?>) {
                            Log.d("pusk", response.toString())
                            if (response.body()?.id == movieId)
//                                isLiked = response.body()?.favorite
//                            if (isLiked == true) {
//                                likeBtn?.setImageResource(R.drawable.ic_favorite_black_24dp)
//
//                            } else
//                                likeBtn?.setImageResource(R.drawable.ic_favorite_border_black_24dp)
                                 this@MoviesListViewModel.isLiked = response.body()?.favorite!!
                                liveData.value=State.liked(response?.body()?.favorite!!)
                        }

                    })
            }
        } catch (e: Exception) {
            Log.d("mark", e.toString())
        }
        Log.d("pusk", isLiked.toString())

        return this.isLiked
    }

    sealed class State {
        object ShowLoading : State()
        object HideLoading : State()
        data class Result(val list: List<Movie>?) : State()
        data class FavResult(val list: List<FavMovies>?) : State()
        data class MovieDetails(val movie: Movie?) : State()
        data class liked(val movieLiked: Boolean):State()
    }
}
