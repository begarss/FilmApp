package com.example.themovie.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.themovie.R
import com.example.themovie.view.adapter.FavListAdapter
import com.example.themovie.model.api.MovieApi
import com.example.themovie.model.api.RetrofitService
import com.example.themovie.model.*
import com.example.themovie.view_model.MoviesListViewModel
import com.example.themovie.view_model.ViewModelProviderFactory
import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.CoroutineContext

class FavouritesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var movieListAdapter: FavListAdapter? = null
    private var movies: ArrayList<FavMovies>? = null
    var sessionId: String? = null
    private lateinit var movieListViewModel: MoviesListViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = LayoutInflater.from(container?.context)
            .inflate(R.layout.favourites_fragment, container, false)

        val viewModelProviderFactory = ViewModelProviderFactory(requireContext())
        movieListViewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(MoviesListViewModel::class.java)

        val pref =
            requireActivity().getSharedPreferences("tkn", Context.MODE_PRIVATE)
        sessionId = pref.getString("sessionID", "empty")

        bindViews(view)
        swipeRefreshLayout.setOnRefreshListener {
            bindViews(view)
            movieListViewModel.getFavMovies(sessionId)

        }
        movieListViewModel.getFavMovies(sessionId)
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
                    is MoviesListViewModel.State.FavResult -> {
                        movieListAdapter?.moviesList = result.list
                        movieListAdapter?.notifyDataSetChanged()
                    }
                }
            })
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

}





