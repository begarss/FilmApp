package com.example.themovie.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.themovie.R
import com.example.themovie.view.activity.MainActivity
import com.example.themovie.view.fragment.MovieDetailFragment
import com.example.themovie.model.Movie
import java.text.SimpleDateFormat
import java.util.*


class MovieListAdapter(
    var moviesList: List<Movie>? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_LOADING = 0
    private val VIEW_TYPE_NORMAL = 1

    private var isLoaderVisible = false

    private val movies = ArrayList<Movie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_NORMAL -> MovieViewHolder(
                inflater.inflate(R.layout.movie_list_row, parent, false)
            )
            VIEW_TYPE_LOADING -> ProgressViewHolder(
                inflater.inflate(R.layout.layout_progress, parent, false)
            )
            else -> throw Throwable("invalid view")
        }
    }

    //    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MovieViewHolder {
//        val view = LayoutInflater.from(p0.context).inflate(R.layout.movie_list_row, p0, false)
//        return MovieViewHolder(view)
//    }
    override fun getItemViewType(position: Int): Int {
        return if (isLoaderVisible) {
            if (position == movies.size - 1) {
                VIEW_TYPE_LOADING
            } else {
                VIEW_TYPE_NORMAL
            }
        } else {
            VIEW_TYPE_NORMAL
        }
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MovieViewHolder) {
            holder.bind(movies[position])
        }
    }

    fun addItems(list: List<Movie>) {
        movies.addAll(list)
        notifyDataSetChanged()
    }

    fun setNewItems(list: List<Movie>) {
        movies.clear()
        addItems(list)
        isLoaderVisible = false
    }

    fun addLoading() {
        isLoaderVisible = true
        movies.add(Movie(id = -1))
        notifyItemInserted(movies.size - 1)
    }

    fun removeLoading() {
        isLoaderVisible = false
        val position = movies.size - 1
        if (movies.isNotEmpty()) {
            val item = getItem(position)
            if (item != null) {
                movies.removeAt(position)
                notifyItemRemoved(position)
            }
        }
    }

    fun getItem(position: Int): Movie? {
        return movies[position]
    }

    fun clearAll() {
        movies.clear()
        notifyDataSetChanged()
    }

    inner class MovieViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var dateFormat = SimpleDateFormat("MMMM d, YYYY", Locale.ENGLISH)
        var initialFormat = SimpleDateFormat("YY-MM-DD", Locale.ENGLISH)
        private val title: TextView
        private val description: TextView
        private val date: TextView
        private val commentsCount: TextView
        private val poster: ImageView

        init {
            title = view.findViewById(R.id.m_movie_title)
            description = view.findViewById(R.id.m_movie_overview)
            date = view.findViewById(R.id.m_movie_date)
            commentsCount = view.findViewById(R.id.m_movie_cnt)
            poster = view.findViewById(R.id.m_movie_poster)
        }

        fun bind(movie: Movie?) {
            title.text = movie?.originalTitle
            commentsCount.text = movie?.voteCount.toString()
            description.text = movie?.overview
//            val dateTime = initialFormat.parse(movie?.releaseDate)
            date.text = movie?.releaseDate
            Glide.with(view.context)
                .load(movie?.getPosterPath())
                .into(poster)
            view.setOnClickListener {
                if (view.context is MainActivity) {
                    val movieDetailFragment = MovieDetailFragment()
                    (view.context as MainActivity).fm?.beginTransaction()
                        ?.replace(R.id.fragment_container, movieDetailFragment)
                        ?.addToBackStack(null)?.commit()
                    if (movie != null) {
//                        movieDetailFragment.getMovieDetail(movie.id)
//                        movieDetailFragment.getMovieDetailCoroutine(movie.id)
                        movieDetailFragment.movieId = movie.id
                    }
//
                }

            }

        }

    }

    inner class ProgressViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    }
//    fun clearAll() {
//        (moviesList as? ArrayList<Movie>)?.clear()
//        notifyDataSetChanged()
//    }
}