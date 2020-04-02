package com.example.themovie.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.themovie.MovieDetails
import com.example.themovie.R
import com.example.themovie.model.Movie

class MovieAdapter(
    var context: Context,
    var moviesList: List<Movie>? = null
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
     override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MovieViewHolder {
         val view = LayoutInflater.from(p0.context).inflate(R.layout.movie_card, p0, false)
         return MovieViewHolder(view)
     }
     override fun getItemCount(): Int {
         return moviesList?.size ?:0
     }

     override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
         holder.bind(moviesList?.get(position))
//         holder.itemView.setOnClickListener { v ->
//             val context = v.context
//             val intent = Intent(context, MovieDetails::class.java)
//             intent.putExtra(MovieDetails.ARG_ITEM_ID, holder.destination!!.id)
//
//             context.startActivity(intent)
//         }
     }

    inner class MovieViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(post: Movie?) {
            val  title = view.findViewById<TextView>(R.id.tv_title)
            val description = view.findViewById<TextView>(R.id.tv_des)
            val date = view.findViewById<TextView>(R.id.tv_date)
            val commnetsCount = view.findViewById<TextView>(R.id.tv_comment)
            val poster = view.findViewById<ImageView>(R.id.poster)
            val movie_id=post?.id
            title.text=post?.original_title
            description.text=post?.overview
            date.text=post?.release_date
            Glide.with(context)
                .load(post?.getPosterPath())
                .into(poster)
            view.setOnClickListener {
                val intent = Intent(context, MovieDetails::class.java)
                intent.putExtra("movieId",movie_id)
                context.startActivity(intent)
            }
        }

    }



}