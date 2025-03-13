package com.tomtruyen.data

import androidx.room.TypeConverter
import com.tomtruyen.data.entities.ChangeType
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.Json
import kotlin.collections.List

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

    @TypeConverter
    fun fromListChangeTypeToString(changeTypes: List<ChangeType>): String {
        return Json.encodeToString(changeTypes)
    }

    @TypeConverter
    fun fromStringToListChangeType(changeTypes: String): List<ChangeType> {
        return Json.decodeFromString(changeTypes)
    }
}