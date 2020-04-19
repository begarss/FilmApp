package com.example.themovie.view.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.themovie.R
import com.example.themovie.view.activity.MainActivity
import com.example.themovie.view.adapter.MovieListAdapter
import com.example.themovie.model.Movie
import com.example.themovie.view_model.MoviesListViewModel
import com.example.themovie.view_model.ViewModelProviderFactory
import java.util.*

class MovieListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var movieListAdapter: MovieListAdapter? = null
    private var movies: ArrayList<Movie>? = null
    lateinit var preferences: SharedPreferences
    private lateinit var bigIm: ImageView
    private var bigTitle: TextView? = null
    private var bigDate: TextView? = null
    private lateinit var movie: Movie
    private lateinit var movieListViewModel: MoviesListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val view: View = LayoutInflater.from(container?.context)
            .inflate(R.layout.fragment_movie_list, container, false)

        val viewModelProviderFactory = ViewModelProviderFactory(requireContext())
        movieListViewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(MoviesListViewModel::class.java)
        bindViews(view)
        preferences =
            requireActivity().getSharedPreferences("tkn", Context.MODE_PRIVATE)
        swipeRefreshLayout.setOnRefreshListener {
            bindViews(view)
            movieListViewModel.getMovies()
        }
        movieListViewModel.getMovies()
        movieListViewModel.liveData.observe(
            requireActivity(),
            androidx.lifecycle.Observer { result ->
                when (result) {
                    is MoviesListViewModel.State.ShowLoading -> {
                        swipeRefreshLayout.isRefreshing = true
                    }
                    is MoviesListViewModel.State.HideLoading -> {
                        swipeRefreshLayout.isRefreshing = false
                    }
                    is MoviesListViewModel.State.Result -> {
                        movieListAdapter?.moviesList = result.list

                        bindFirstItem(result.list?.first())
                        movieListAdapter?.notifyDataSetChanged()
                    }
                }
            })
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


    private fun bindFirstItem(movie: Movie?) {
        bigDate?.text = movie?.releaseDate
        bigTitle?.text = movie?.originalTitle
        bigIm.visibility = View.VISIBLE
        bigTitle?.visibility = View.VISIBLE
        bigDate?.visibility = View.VISIBLE
        Glide.with(this@MovieListFragment)
            .load(movie?.getPosterPath())
            .into(this@MovieListFragment.bigIm)
//        bigIm.setOnClickListener {
//            if (view?.context is MainActivity) {
//                val movieDetailFragment = MovieDetailFragment()
//                (view?.context as MainActivity).fm?.beginTransaction()
//                    ?.replace(R.id.fragment_container, movieDetailFragment)
//                    ?.addToBackStack(null)?.commit()
//                if (movie != null) {
//                    movieDetailFragment.getMovieDetailCoroutine(movie.id)
//                }
//            }
//        }
    }


}