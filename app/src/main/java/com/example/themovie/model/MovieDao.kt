package com.example.themovie.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<Movie>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: Movie)

    @Query("Select * from movie_table")
    fun getAll(): List<Movie>

    @Query("Select * from movie_table where id like :detail_id")
    fun getMovie(detail_id: Int): Movie

}