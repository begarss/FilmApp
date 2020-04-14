package com.example.themovie.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class GenresConverter {
//    val gson = Gson()
//    @TypeConverter
//    fun stringToList(data: String?): List<Genre?>? {
//        if (data == null) {
//            return emptyList()
//        }
//        val listType =
//            object : TypeToken<List<Genre?>?>() {}.type
//        return gson.fromJson(data, listType)
//    }
//
//    @TypeConverter
//    fun ListToString(someObjects: List<Genre?>?): String? {
//        return gson.toJson(someObjects)
//    }

    @TypeConverter
    fun toTorrent(json: String?): List<Genre?>? {
        val type = object : TypeToken<List<Genre?>?>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toJson(torrent: List<Genre>): String {
        val type = object : TypeToken<List<Genre>>() {}.type
        return Gson().toJson(torrent, type)
    }
}