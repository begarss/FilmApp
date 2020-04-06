package com.example.themovie.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.themovie.R
import com.example.themovie.adapter.MovieListAdapter
import com.example.themovie.api.MovieApi
import com.example.themovie.api.RetrofitService
import com.example.themovie.model.Movie
import com.example.themovie.model.MovieDao
import com.example.themovie.model.MovieDatabase
import com.example.themovie.model.MovieResponse
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.coroutines.CoroutineContext


class MovieListFragment:Fragment(),CoroutineScope {

    lateinit var recyclerView: RecyclerView
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val API_KEY: String = "d118a5a4e56930c8ce9bd2321609d877"
    private var movieListAdapter: MovieListAdapter? = null
    private var moviesList: List<Movie>? = null

    val job = Job()
    var movieDao: MovieDao? = null
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view: View = LayoutInflater.from(container?.context)
            .inflate(R.layout.fragment_movie_list, container, false)

        movieDao = MovieDatabase.getDatabase(context = view.context).movieDao()

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        // recyclerView.itemAnimator = DefaultItemAnimator()


        swipeRefreshLayout = view.findViewById(R.id.main_content)

        swipeRefreshLayout.setOnRefreshListener {
            movieListAdapter?.clearAll()
//            recyclerView.layoutManager = GridLayoutManager(activity, 1)
//            recyclerView.itemAnimator = DefaultItemAnimator()
//            movies = ArrayList<Movie>()
//
//            movieListAdapter = MovieListAdapter(movies)
//            movieListAdapter?.notifyDataSetChanged()
            getMovieCoroutine()
        }
        moviesList = ArrayList()
        movieListAdapter = MovieListAdapter(moviesList)
        recyclerView.adapter = movieListAdapter
        getMovieCoroutine()
        return view
    }

    private fun getMovieList() {
        swipeRefreshLayout.isRefreshing = true
        val api: MovieApi? = RetrofitService.getClient()?.create(MovieApi::class.java)
        api?.getPopularMoviesList(API_KEY, 1)?.enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful()) {
                    val movies = response.body()
                    moviesList = movies?.results
                    movieListAdapter?.notifyDataSetChanged()
                }
                swipeRefreshLayout.isRefreshing = false
            }

            override fun onFailure(
                call: Call<MovieResponse>,
                t: Throwable
            ) {
                Log.e(MovieListFragment::class.java.simpleName, t.toString())
                swipeRefreshLayout.isRefreshing = false
            }
        })
    }

    private fun getMovieCoroutine() {
        Log.e(MovieListFragment::class.java.simpleName, "coroutines")
        launch {
            swipeRefreshLayout.isRefreshing = true
            Log.e(MovieListFragment::class.java.simpleName, "coroutines launch")
            val list = withContext(Dispatchers.IO) {
                try {
                    val api: MovieApi? = RetrofitService.getClient()?.create(MovieApi::class.java)
                    val response = api?.getPopularMoviesListCoroutine()
                    if (response!!.isSuccessful) {
                        Log.d("List", "on success")
                        val result = response.body()
                        if (!result.isNullOrEmpty()) {
                            movieDao?.insertAll(result)
                        }
                        result
                    } else {
                        movieDao?.getAll() ?: emptyList()
                    }
                } catch (e: Exception) {
                    movieDao?.getAll() ?: emptyList<Movie>()
                }
            }
            movieListAdapter?.moviesList = list
            movieListAdapter?.notifyDataSetChanged()
            swipeRefreshLayout.isRefreshing = false
        }
    }
}

