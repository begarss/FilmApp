package com.example.themovie.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.themovie.BuildConfig
import com.example.themovie.model.Fav.FavMovieInfo
import com.example.themovie.model.Fav.FavResponse
import com.example.themovie.R
import com.example.themovie.model.api.MovieApi
import com.example.themovie.model.api.RetrofitService
import com.example.themovie.model.Movie
import com.example.themovie.model.MovieDao
import com.example.themovie.model.MovieDatabase
import com.example.themovie.view_model.MoviesListViewModel
import com.example.themovie.view_model.ViewModelProviderFactory
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext


class MovieDetailFragment : Fragment() {

    private var movieTitle: TextView? = null
    private var movieJanre: TextView? = null
    private var movieDate: TextView? = null
    private var movieYear: TextView? = null
    private var movieDescription: TextView? = null
    var movieId: Int = 0
    private lateinit var poster: ImageView
    private var likeBtn: ImageView? = null
    private var isLiked: Boolean? = null
    private var backBtn: ImageButton? = null
    var sessionId: String? = null
    private var movie: Movie? = null

    private lateinit var movieListViewModel: MoviesListViewModel


    companion object {
        fun newInstance(movie: Movie?): MovieDetailFragment? {
            val fragment = MovieDetailFragment()
            fragment.movie = movie
            return fragment
        }
    }

    private val dateFormat = SimpleDateFormat("MMMM d, YYYY H:m", Locale.ENGLISH)
    private val dateYearFormat = SimpleDateFormat("YYYY", Locale.ENGLISH)
    private val initialFormat = SimpleDateFormat("YY-MM-DD", Locale.ENGLISH)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = LayoutInflater.from(container?.context)
            .inflate(R.layout.fragment_movie_detail, container, false)


        bindViews(v)
        getDetails(movieId)
        val pref =
            requireActivity().getSharedPreferences("tkn", Context.MODE_PRIVATE)
        sessionId = pref.getString("sessionID", "empty")

        return v

    }

    fun getDetails(movieId: Int) {
        val viewModelProviderFactory = ViewModelProviderFactory(requireActivity())
        movieListViewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(MoviesListViewModel::class.java)
        movieListViewModel.getMovieDetailCoroutine(movieId)
        movieListViewModel.liveData.observe(
            requireActivity(),
            androidx.lifecycle.Observer { result ->
                when (result) {
                    is MoviesListViewModel.State.liked -> {
                        isLiked = result.movieLiked
                        if (isLiked == true) {
                            likeBtn?.setImageResource(R.drawable.ic_favorite_black_24dp)

                        } else
                            likeBtn?.setImageResource(R.drawable.ic_favorite_border_black_24dp)
                    }
                    is MoviesListViewModel.State.MovieDetails -> {

                        if (result.movie?.releaseDate != null) {
                            val dateTime = initialFormat.parse(result.movie.releaseDate)
                            movieDate?.text = dateFormat.format(dateTime)
                            movieYear?.text = dateYearFormat.format(dateTime)
                        } else {
                            movieDate?.text = dateFormat.format(Date())
                            movieYear?.text = dateYearFormat.format(Date())
                        }
                        movieTitle?.text = result?.movie?.originalTitle
                        movieDescription?.text = result?.movie?.overview
                        movieJanre?.text = result?.movie?.genres?.first()?.name
                        Glide.with(this@MovieDetailFragment)
                            .load(result?.movie?.getPosterPath())
                            .into(this@MovieDetailFragment.poster)

                        isLiked = movieListViewModel.getState(movieId, sessionId)
                        likeBtn?.setOnClickListener(View.OnClickListener {
                            if (isLiked == false) {
                                isLiked = true
                                likeBtn?.setImageResource(R.drawable.ic_favorite_black_24dp)
                                Toast.makeText(
                                    activity,
                                    "Film added to favList",
                                    Toast.LENGTH_LONG
                                ).show()
                                movieListViewModel.markAsFav(
                                    FavMovieInfo(
                                        true,
                                        movieId,
                                        "movie"
                                    ), sessionId
                                )
                                movie?.favorite = isLiked
                                likeBtn?.refreshDrawableState()
                            } else {
                                isLiked = false
                                movie?.favorite = isLiked

                                likeBtn?.setImageResource(R.drawable.ic_favorite_border_black_24dp)
                                movieListViewModel.markAsFav(
                                    FavMovieInfo(
                                        false,
                                        movieId,
                                        "movie"
                                    ), sessionId
                                )
                                likeBtn?.refreshDrawableState()

                            }
                        })
                    }
                }

            })
    }

    private fun bindViews(v: View) {
        movieTitle = v.findViewById(R.id.m_movie_title)
        movieJanre = v.findViewById(R.id.m_movie_genre)
        movieDate = v.findViewById(R.id.m_movie_date_detail)
        movieDescription = v.findViewById(R.id.m_movie_overview)
        poster = v.findViewById(R.id.m_avatar_detail)
        likeBtn = v.findViewById(R.id.fav_btn)
        movieYear = v.findViewById(R.id.m_movie_release_date)
        backBtn = v.findViewById(R.id.back_btn)

    }


}