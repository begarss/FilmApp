package com.example.themovie.fragment

import android.content.Context
import android.content.SharedPreferences
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
import com.example.themovie.model.MovieResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MovieListFragment:Fragment(){

    lateinit var recyclerView: RecyclerView
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val API_KEY: String = "d118a5a4e56930c8ce9bd2321609d877"
    private var movieListAdapter: MovieListAdapter? = null
    private var movies: ArrayList<Movie>? = null
    lateinit var preferences: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view: View = LayoutInflater.from(container?.context).inflate(R.layout.fragment_movie_list, container, false)
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.itemAnimator = DefaultItemAnimator()
        movies = ArrayList()
        movieListAdapter = MovieListAdapter(movies)
        recyclerView.adapter = movieListAdapter
        movieListAdapter?.notifyDataSetChanged()
        swipeRefreshLayout = view.findViewById(R.id.main_content)
        preferences =
            activity!!.getSharedPreferences("tkn", Context.MODE_PRIVATE)
        getMovieList()
        swipeRefreshLayout.setOnRefreshListener {
            recyclerView.layoutManager = GridLayoutManager(activity, 1)
            recyclerView.itemAnimator = DefaultItemAnimator()
            movies = ArrayList<Movie>()

            movieListAdapter = MovieListAdapter(movies)
            movieListAdapter?.notifyDataSetChanged()
            getMovieList()
        }

        return view
    }
     private fun getMovieList(){
         swipeRefreshLayout.isRefreshing = true
         val api: MovieApi? = RetrofitService.getClient()?.create(MovieApi::class.java)
         api?.getPopularMoviesList(API_KEY,1)?.enqueue(object : Callback<MovieResponse> {
             override fun onResponse(
                 call: Call<MovieResponse>,
                 response: Response<MovieResponse>
             ) {
                 if (response.isSuccessful()) {
                     val movies= response.body()
                     movieListAdapter?.moviesList = movies?.results
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
}