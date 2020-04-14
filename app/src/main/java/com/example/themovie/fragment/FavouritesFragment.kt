package com.example.themovie.fragment

import android.content.Context
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
import com.example.themovie.adapter.FavListAdapter
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

class FavouritesFragment : Fragment(), CoroutineScope {

    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var movieListAdapter: FavListAdapter? = null
    private var movies: ArrayList<Movie>? = null
    var sessionId: String? = null
    var movieDao: MovieDao? = null
    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = LayoutInflater.from(container?.context)
            .inflate(R.layout.favourites_fragment, container, false)
        val pref =
            requireActivity().getSharedPreferences("tkn", Context.MODE_PRIVATE)
        sessionId = pref.getString("sessionID", "empty")
        bindViews(view)

        movieDao = MovieDatabase.getDatabase(view.context).movieDao()
        getFavListCoroutine(sessionId)
        swipeRefreshLayout.setOnRefreshListener {
            recyclerView.layoutManager = GridLayoutManager(activity, 1)
            recyclerView.itemAnimator = DefaultItemAnimator()
            movies = ArrayList<Movie>()
            movieListAdapter = FavListAdapter(movies)
            movieListAdapter?.notifyDataSetChanged()
            getFavListCoroutine(sessionId)
        }
        return view
    }

    private fun bindViews(view: View) {
        recyclerView = view.findViewById(R.id.Frecycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.itemAnimator = DefaultItemAnimator()
        movies = ArrayList()
        movieListAdapter = FavListAdapter(movies)
        recyclerView.adapter = movieListAdapter
        movieListAdapter?.notifyDataSetChanged()
        swipeRefreshLayout = view.findViewById(R.id.main_content)
    }

    private fun getFavList(sessionId: String?) {

        swipeRefreshLayout.isRefreshing = true
        val api: MovieApi? = RetrofitService.getClient()?.create(MovieApi::class.java)
        api?.getFavList(sessionId)
            ?.enqueue(object : Callback<MovieResponse> {
                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    Log.d("FavouritesFragment", "OnFailure")
                }

                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    Log.d("FavouritesFragment", response.toString())
                    if (response.isSuccessful()) {
                        val movies = response.body()
                        movieListAdapter?.moviesList = movies?.results
                        movieListAdapter?.notifyDataSetChanged()
                    }
                    swipeRefreshLayout.isRefreshing = false

                }

            })
    }
    private fun getFavListCoroutine(sessionId: String?) {
        launch {
            swipeRefreshLayout.isRefreshing = true
            val movies = withContext(Dispatchers.IO) {
                try {
                    val api: MovieApi? = RetrofitService.getClient()?.create(MovieApi::class.java)
                    val response = api?.getFavListCoroutine(sessionId)
                    if (response!!.isSuccessful()) {
                        val result = response.body()
                        if (!(result?.results.isNullOrEmpty())) {
                            movieDao?.insertAll(result!!.results)
                        }
                        result!!.results
                    } else {
                        movieDao?.getAll() ?: emptyList<Movie>()
                    }
                } catch (e: Exception) {
                    Log.e("Moviedatabase", e.toString())
                    movieDao?.getAll() ?: emptyList<Movie>()
                }

            }
            movieListAdapter?.moviesList = movies
            movieListAdapter?.notifyDataSetChanged()
            swipeRefreshLayout.isRefreshing = false
        }

    }

}





