package com.tomtruyen.data

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDateTime
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

    @TypeConverter
    fun fromLocalDateTimeToString(dateTime: LocalDateTime): String {
        return dateTime.toString()
    }

    @TypeConverter
    fun fromStringToLocalDateTime(dateTime: String): LocalDateTime {
        return LocalDateTime.parse(dateTime)
    }
}