package com.example.themovie.fragment

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
import com.bumptech.glide.Glide
import com.example.themovie.BuildConfig
import com.example.themovie.Fav.FavMovieInfo
import com.example.themovie.Fav.FavResponse
import com.example.themovie.R
import com.example.themovie.api.MovieApi
import com.example.themovie.api.RetrofitService
import com.example.themovie.model.Movie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class MovieDetailFragment : Fragment() {
    private var movieTitle: TextView? = null
    private var movieJanre: TextView? = null
    private var movieDate: TextView? = null
    private var movieYear: TextView? = null
    private var movieDescription: TextView? = null
    private var movieId: Int? = null
    private lateinit var poster: ImageView
    private var likeBtn: ImageView? = null
    private var isLiked: Boolean? = null
    private var backBtn: ImageButton? = null
    var sessionId: String? = null
    private var movie: Movie? = null

    companion object {
        fun newInstance(movie: Movie?): MovieDetailFragment? {
            val fragment = MovieDetailFragment()
            fragment.movie = movie
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = LayoutInflater.from(container?.context)
            .inflate(R.layout.fragment_movie_detail, container, false)
        bindViews(v)
        val pref =
            requireActivity().getSharedPreferences("tkn", Context.MODE_PRIVATE)
        sessionId = pref.getString("sessionID", "empty")
        return v
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

        backBtn?.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

    }

    fun getMovieDetail(id: Int) {
        val dateFormat = SimpleDateFormat("MMMM d, YYYY H:m", Locale.ENGLISH)
        val dateYearFormat = SimpleDateFormat("YYYY", Locale.ENGLISH)
        val initialFormat = SimpleDateFormat("YY-MM-DD", Locale.ENGLISH)
        val api: MovieApi? = RetrofitService.getClient()?.create(MovieApi::class.java)
        api?.getMovieDetail(id, BuildConfig.THE_MOVIE_DB_API_TOKEN)
            ?.enqueue(object : Callback<Movie> {
                override fun onResponse(
                    call: Call<Movie>,
                    response: Response<Movie>
                ) {
                    val dateTime = initialFormat.parse(response.body()?.releaseDate)
                    movieDate?.setText(dateFormat.format(dateTime))
                    movieYear?.setText(dateYearFormat.format(dateTime))
                    movieTitle?.setText(response.body()?.originalTitle)
                    movieDescription?.setText(response.body()?.overview)
                    movieJanre?.setText((response.body()?.genres?.first()?.name))
                    Glide.with(this@MovieDetailFragment)
                        .load(response.body()?.getPosterPath())
                        .into(this@MovieDetailFragment.poster)
                    movieId = response.body()?.id
                    isLiked = getState(movieId)
                    likeBtn?.setOnClickListener(View.OnClickListener {
                        if (isLiked == false) {
                            isLiked = true
                            likeBtn?.setImageResource(R.drawable.ic_favorite_black_24dp)
                            Toast.makeText(
                                activity,
                                "Film added to favList",
                                Toast.LENGTH_LONG
                            ).show()
                            markAsFav(FavMovieInfo(true, movieId, "movie"), sessionId)
                            likeBtn?.refreshDrawableState()
                        } else {
                            isLiked = false
                            likeBtn?.setImageResource(R.drawable.ic_favorite_border_black_24dp)
                            markAsFav(FavMovieInfo(false, movieId, "movie"), sessionId)
                            likeBtn?.refreshDrawableState()

                        }
                    })
                }

                override fun onFailure(call: Call<Movie>, t: Throwable) {}
            })
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
            Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
            Log.d("mark", e.toString())
        }
    }

    fun getState(movieId: Int?): Boolean? {
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
                                isLiked = response.body()?.favorite
                            if (isLiked == true) {
                                likeBtn?.setImageResource(R.drawable.ic_favorite_black_24dp)

                            } else
                                likeBtn?.setImageResource(R.drawable.ic_favorite_border_black_24dp)

                        }

                    })
            }
            return isLiked
        } catch (e: Exception) {
            Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
            Log.d("mark", e.toString())
        }
        return isLiked
    }

}