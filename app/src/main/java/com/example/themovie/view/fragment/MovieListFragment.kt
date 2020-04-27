package com.example.themovie.view.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.themovie.R
import com.example.themovie.model.Movie
import com.example.themovie.utils.NestedScroll
import com.example.themovie.utils.PaginationListener
import com.example.themovie.view.activity.MainActivity
import com.example.themovie.view.adapter.MovieListAdapter
import com.example.themovie.view_model.MoviesListViewModel
import com.example.themovie.view_model.ViewModelProviderFactory
import java.util.*

class MovieListFragment : BaseFragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var nestedScrollView: NestedScrollView
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    //    private var movieListAdapter: MovieListAdapter? = null
    private var movies: ArrayList<Movie>? = null
    lateinit var preferences: SharedPreferences
    private lateinit var bigIm: ImageView
    private var bigTitle: TextView? = null
    private var bigDate: TextView? = null
    private lateinit var movie: Movie

    private lateinit var movieListViewModel: MoviesListViewModel

    private var currentPage = NestedScroll.PAGE_START
    private var isLastPage = false
    private var isLoading = false
    private var itemCount = 0
    private val movieListAdapter by lazy {
        MovieListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_movie_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        preferences =
            requireActivity().getSharedPreferences("tkn", Context.MODE_PRIVATE)
        val viewModelProviderFactory = ViewModelProviderFactory(requireContext())
        movieListViewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(MoviesListViewModel::class.java)
        bindViews(view)

        setAdapter()
        setData()
    }

    override fun bindViews(view: View) = with(view) {
        bigIm = view.findViewById(R.id.first_im)
        bigIm.visibility = View.INVISIBLE
        bigTitle = view.findViewById(R.id.fm_movie_title)
        bigTitle?.visibility = View.INVISIBLE
        bigDate = view.findViewById(R.id.fm_movie_date)
        bigDate?.visibility = View.INVISIBLE
        swipeRefreshLayout = findViewById(R.id.main_content)
        recyclerView = findViewById(R.id.rvMovies)
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        nestedScrollView = findViewById(R.id.nsv)
        nestedScrollView.setOnScrollChangeListener(object : NestedScroll(layoutManager) {
            override fun loadMoreItems() {
                Log.d("load_more", "true")
                isLoading = true
                currentPage++
                Toast.makeText(context, "Page: $currentPage", Toast.LENGTH_SHORT).show()
                movieListViewModel.getMovies(page = currentPage)
            }

            override fun isLastPage(): Boolean = isLastPage

            override fun isLoading(): Boolean = isLoading
        })

        swipeRefreshLayout.setOnRefreshListener {
            movieListAdapter.clearAll()
            itemCount = 0
            currentPage = NestedScroll.PAGE_START
            isLastPage = false
            movieListViewModel.getMovies(page = currentPage)
        }
    }


    override fun setData() {
        movieListViewModel.liveData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { result ->
                when (result) {
                    is MoviesListViewModel.State.ShowLoading -> {
                        swipeRefreshLayout.isRefreshing = true
                    }
                    is MoviesListViewModel.State.HideLoading -> {
                        swipeRefreshLayout.isRefreshing = false
                    }
                    is MoviesListViewModel.State.Result -> {
                        itemCount = result.list.size
                        if (currentPage != NestedScroll.PAGE_START) {
                            movieListAdapter?.removeLoading()
                            Log.d("load_more", "remove loading")

                        }
                        movieListAdapter?.addItems(result.list)
                        Log.d("load_more", result.totalPage.toString())

                        if (currentPage < result.totalPage) {
                            movieListAdapter?.addLoading()
                            Log.d("load_more", "add loading")

                        } else {
                            isLastPage = true
                        }
                        isLoading = false
                        if (currentPage == 1)
                            bindFirstItem(result.list?.first())

                    }
                }
            })
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
        bigIm.setOnClickListener {
            if (view?.context is MainActivity) {
                val movieDetailFragment = MovieDetailFragment()
                (view?.context as MainActivity).fm?.beginTransaction()
                    ?.replace(R.id.fragment_container, movieDetailFragment)
                    ?.addToBackStack(null)?.commit()
                if (movie != null) {
                    movieDetailFragment.movieId = movie.id
                }
            }
        }
    }

    private fun setAdapter() {
        recyclerView.adapter = movieListAdapter
    }

}