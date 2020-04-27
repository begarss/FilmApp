package com.example.themovie.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
@Dao
interface LikeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovieStatus(movieState: MovieStatus)

    @Query("SELECT * FROM movies_statuses")
    fun getMovieStatuses(): List<MovieStatus>

    @Query("DELETE FROM movies_statuses")
    fun deleteAll()
}