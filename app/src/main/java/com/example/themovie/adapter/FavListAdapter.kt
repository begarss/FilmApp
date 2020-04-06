package com.example.themovie.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.themovie.activity.MainActivity
import com.example.themovie.R
import com.example.themovie.fragment.MovieDetailFragment
import com.example.themovie.model.Movie
import java.text.SimpleDateFormat
import java.util.*

class FavListAdapter(
    var moviesList: List<Movie>? = null
) : RecyclerView.Adapter<FavListAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MovieViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.movie_list_row, p0, false)
        return MovieViewHolder(view)
    }
    override fun getItemCount(): Int {
        return moviesList?.size ?:0
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(moviesList?.get(position))
    }

    inner class MovieViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var dateFormat = SimpleDateFormat("MMMM d, YYYY", Locale.ENGLISH)
        var initialFormat = SimpleDateFormat("YY-MM-DD", Locale.ENGLISH)
        fun bind(movie: Movie?) {
            val  title = view.findViewById<TextView>(R.id.m_movie_title)
            val description = view.findViewById<TextView>(R.id.m_movie_overview)
            val date = view.findViewById<TextView>(R.id.m_movie_date)
            val commentsCount = view.findViewById<TextView>(R.id.m_movie_cnt)
            val poster = view.findViewById<ImageView>(R.id.m_movie_poster)
            val movie_id=movie?.id
            title.text=movie?.original_title
            commentsCount.text= movie?.vote_count.toString()
            description.text=movie?.overview
            val dateTime = initialFormat.parse(movie?.release_date)
            date.text=dateFormat.format(dateTime)
            Glide.with(view.context)
                .load(movie?.getPosterPath())
                .into(poster)
            view.setOnClickListener {
                if (view.context is MainActivity) {
                    val movieDetailFragment = MovieDetailFragment()
                    (view.context as MainActivity).fm?.beginTransaction()?.replace(R.id.fragment_container,movieDetailFragment)?.addToBackStack(null)?.commit()
                    movieDetailFragment.getMovieDetail(movie!!.id)
                }

            }
        }

    }
}