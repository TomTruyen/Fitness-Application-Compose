package com.tomtruyen.data

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromListToString(list: List<String>): String {
        return Json.encodeToString(list)
    }

    @TypeConverter
    fun fromStringToList(string: String): List<String> {
        return Json.decodeFromString(string)
    }
}