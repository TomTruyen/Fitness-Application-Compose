package com.tomtruyen.fitnessapplication.data

import androidx.room.TypeConverter
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun fromListToString(list: List<String>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromStringToList(string: String): List<String> {
        return Gson().fromJson(string, Array<String>::class.java).toList()
    }
}