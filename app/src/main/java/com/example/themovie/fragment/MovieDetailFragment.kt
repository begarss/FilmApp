package com.example.themovie.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.themovie.R
import com.example.themovie.api.MovieApi
import com.example.themovie.api.RetrofitService
import com.example.themovie.model.Movie
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


   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v: View = LayoutInflater.from(container?.context).inflate(R.layout.fragment_movie_detail, container, false)
        movieTitle = v.findViewById(R.id.m_movie_title)
        movieJanre = v.findViewById(R.id.m_movie_genre)
        movieDate = v.findViewById(R.id.m_movie_date)
        movieDescription = v.findViewById(R.id.m_movie_overview)
        return v
    }

    fun getMovieDetail(id: Int){
        val api: MovieApi? = RetrofitService.getClient()?.create(MovieApi::class.java)
        api?.getMovieDetail(id,API_KEY)?.enqueue(object : Callback<Movie> {
            override fun onResponse(
                call: Call<Movie>,
                response: Response<Movie>
            ) {
                movieTitle?.setText(response.body()?.title)
                movieDescription?.setText(response.body()?.overview)
                movieJanre?.setText((response.body()?.genre_ids).toString())
            }

            override fun onFailure(call: Call<Movie>, t: Throwable) {}
        })
    }
}