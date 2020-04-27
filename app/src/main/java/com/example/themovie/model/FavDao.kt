package com.example.themovie.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFav(list: List<FavMovies>?)

    @Query("SELECT * FROM fav_movie_table WHERE isLiked=1")
    fun getFavouriteMovies(): List<Movie>
    @Query("Select * from fav_movie_table ")
    fun getFav(): List<FavMovies>
    @Query("DELETE FROM fav_movie_table")
    fun deleteAll()
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavOne(movie: FavMovies)

}