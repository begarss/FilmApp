package com.example.themovie.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.themovie.BuildConfig
import com.example.themovie.R
import com.example.themovie.api.MovieApi
import com.example.themovie.api.RetrofitService
import com.example.themovie.model.Movie
import com.example.themovie.model.MovieDetailResponse
import com.example.themovie.model.MovieResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailFragment: Fragment() {
    private val API_KEY = "50433cbb6c47b22aabd51bf88ddd11c0"
    private var movieTitle: TextView? = null
    private  var movieJanre:TextView? = null
    private  var movieDate:TextView? = null
    private  var movieDescription:TextView? = null
    private var poster:ImageView?=null

   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v: View = LayoutInflater.from(container?.context).inflate(R.layout.fragment_movie_detail, container, false)
        movieTitle = v.findViewById(R.id.m_movie_title)
        movieJanre = v.findViewById(R.id.m_movie_genre)
        movieDate = v.findViewById(R.id.m_movie_date)
        movieDescription = v.findViewById(R.id.m_movie_overview)
       poster = v.findViewById(R.id.m_avatar_detail)
        return v
    }

    fun getMovieDetail(id: Int){
        val api: MovieApi? = RetrofitService.getClient()?.create(MovieApi::class.java)
        api?.getMovieDetail(id,BuildConfig.THE_MOVIE_DB_API_TOKEN)?.enqueue(object : Callback<MovieDetailResponse> {
            override fun onResponse(
                call: Call<MovieDetailResponse>,
                response: Response<MovieDetailResponse>
            ) {
                movieTitle?.setText(response.body()?.title)
                movieDescription?.setText(response.body()?.overview)
                movieJanre?.setText((response.body()?.genres?.first()?.name))
                Glide.with(view?.context!!)
                    .load(response?.body()?.getPosterPath())
                    .into(this@MovieDetailFragment!!.poster!!)

            }

            override fun onFailure(call: Call<MovieDetailResponse>, t: Throwable) {}
        })
    }
}