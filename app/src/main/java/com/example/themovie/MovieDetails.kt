package com.example.themovie

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.themovie.api.ApiService
import com.example.themovie.model.MovieDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetails : AppCompatActivity() {
    lateinit var title: TextView
    lateinit var description: TextView
    lateinit var genre:TextView
    lateinit var date:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_movie_detail)
        title= findViewById(R.id.m_movie_title)
        description = findViewById(R.id.m_movie_overview)
        genre = findViewById(R.id.m_movie_genre)
        date = findViewById(R.id.m_movie_date)
        val intent= intent

//        val bundle: Bundle? = intent.has
//        if(bundle?.containsKey("movieId")!!){
//            val id = intent.getIntExtra("movie_id")
//            loadDetails(id)
//        }
        if(intent.hasExtra("movieId")){
            val id = getIntent().extras!!.getInt("movieId")
            loadDetails(id)
        }else
            Log.d("LOL", "no intent")

    }

    private fun loadDetails(id:Int)  {
        try {
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty())
                return
            ApiService.getApi().getMovieDetail(id,BuildConfig.THE_MOVIE_DB_API_TOKEN)
                .enqueue(object:Callback<MovieDetailResponse>{
                    override fun onFailure(call: Call<MovieDetailResponse>, t: Throwable) {
                        Toast.makeText(this@MovieDetails,"Error api ket", Toast.LENGTH_LONG).show()

                    }

                    override fun onResponse(
                        call: Call<MovieDetailResponse>,
                        response: Response<MovieDetailResponse>
                    ) {
                        if (response.isSuccessful){
                            title.setText(response?.body()?.title)
                            description.setText(response?.body()?.overview)
                            genre.setText(response?.body()?.genres?.first()?.name)
                            date.setText(response?.body()?.release_date)

                        }else{
                            Toast.makeText(this@MovieDetails, response.message(), Toast.LENGTH_SHORT).show()
                            Log.d("LOL", response.toString())
                            Log.d("LOL", response.message())
                            Log.d("LOL", response.errorBody().toString())

                        }
                    }

                })


        }catch (e: Exception) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
            Log.d("LOL", e.toString())


        }
    }
}
