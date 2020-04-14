package com.example.themovie.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.themovie.BuildConfig
import com.example.themovie.R
import com.example.themovie.activity.MainActivity
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

class MovieListFragment : Fragment(), CoroutineScope {

    private lateinit var recyclerView: RecyclerView
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var movieListAdapter: MovieListAdapter? = null
    private var movies: ArrayList<Movie>? = null
    lateinit var preferences: SharedPreferences
    private lateinit var bigIm: ImageView
    private var bigTitle: TextView? = null
    private var bigDate: TextView? = null
    private lateinit var movie: Movie
    val job = Job()
    var movieDao: MovieDao? = null

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val view: View = LayoutInflater.from(container?.context)
            .inflate(R.layout.fragment_movie_list, container, false)
        movieDao = MovieDatabase.getDatabase(requireActivity()).movieDao()

        bindViews(view)
        preferences =
            requireActivity().getSharedPreferences("tkn", Context.MODE_PRIVATE)
        getMovieCoroutine()
        swipeRefreshLayout.setOnRefreshListener {
            recyclerView.layoutManager = GridLayoutManager(activity, 1)
            recyclerView.itemAnimator = DefaultItemAnimator()
            movies = ArrayList<Movie>()
            bigIm.visibility = View.INVISIBLE
            movieListAdapter = MovieListAdapter(movies)
            movieListAdapter?.notifyDataSetChanged()
            getMovieCoroutine()
        }

        return view
    }

    private fun bindViews(view: View) {
        bigIm = view.findViewById(R.id.first_im)
        bigIm.visibility = View.INVISIBLE
        bigTitle = view.findViewById(R.id.fm_movie_title)
        bigTitle?.visibility = View.INVISIBLE
        bigDate = view.findViewById(R.id.fm_movie_date)
        bigDate?.visibility = View.INVISIBLE
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.itemAnimator = DefaultItemAnimator()
        movies = ArrayList()
        movieListAdapter = MovieListAdapter(movies)
        recyclerView.adapter = movieListAdapter
        movieListAdapter?.notifyDataSetChanged()
        swipeRefreshLayout = view.findViewById(R.id.main_content)
    }

    private fun getMovieList() {
        swipeRefreshLayout.isRefreshing = true
        val api: MovieApi? = RetrofitService.getClient()?.create(MovieApi::class.java)
        api?.getPopularMoviesList(BuildConfig.THE_MOVIE_DB_API_TOKEN, 1)
            ?.enqueue(object : Callback<MovieResponse> {
                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    if (response.isSuccessful()) {
                        val list = response.body()?.results
                        val list2 = list?.subList(1, list.lastIndex)
                        if (list != null) {
                            movie = list.first()
                        }
                        bigDate?.text = movie.releaseDate
                        bigTitle?.text = movie.originalTitle
                        bigIm.visibility = View.VISIBLE
                        bigTitle?.visibility = View.VISIBLE
                        bigDate?.visibility = View.VISIBLE
                        Glide.with(this@MovieListFragment)
                            .load(movie.getPosterPath())
                            .into(this@MovieListFragment.bigIm)
                        //movieListAdapter?.moviesList = list2
                        movieListAdapter?.notifyDataSetChanged()
                        bigIm.setOnClickListener {
                            if (view?.context is MainActivity) {
                                val movieDetailFragment = MovieDetailFragment()
                                (view?.context as MainActivity).fm?.beginTransaction()
                                    ?.replace(R.id.fragment_container, movieDetailFragment)
                                    ?.addToBackStack(null)?.commit()
                                movieDetailFragment.getMovieDetailCoroutine(movie.id)
                            }

                        }
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
        launch {
            swipeRefreshLayout.isRefreshing = true
            val list = withContext(Dispatchers.IO) {
                try {
                    val api: MovieApi? = RetrofitService.getClient()?.create(MovieApi::class.java)
                    val response =
                        api?.getPopularMoviesListCoroutine(BuildConfig.THE_MOVIE_DB_API_TOKEN, 1)
                    if (response?.isSuccessful!!) {
                        val result = response.body()
                        if (!result?.results.isNullOrEmpty()) {
                            movieDao?.insertAll(result?.results!!)
                        }
                        result?.results
                    } else {
                        movieDao?.getAll() ?: emptyList<Movie>()
                    }
                } catch (e: Exception) {
                    Log.e("Moviedatabase", e.toString())
                    movieDao?.getAll() ?: emptyList<Movie>()
                }
            }
            movie = list?.first()!!
            bigDate?.text = movie.releaseDate
            bigTitle?.text = movie.originalTitle
            bigIm.visibility = View.VISIBLE
            bigTitle?.visibility = View.VISIBLE
            bigDate?.visibility = View.VISIBLE
            Glide.with(this@MovieListFragment)
                .load(movie.getPosterPath())
                .into(this@MovieListFragment.bigIm)
            bigIm.setOnClickListener {
                if (view?.context is MainActivity) {
                    val movieDetailFragment = MovieDetailFragment()
                    (view?.context as MainActivity).fm?.beginTransaction()
                        ?.replace(R.id.fragment_container, movieDetailFragment)
                        ?.addToBackStack(null)?.commit()
                    movieDetailFragment.getMovieDetailCoroutine(movie.id)
                }

            }
            movieListAdapter?.moviesList = list
            movieListAdapter?.notifyDataSetChanged()
            swipeRefreshLayout.isRefreshing = false
        }
    }
}