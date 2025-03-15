package com.tomtruyen.data

import androidx.room.TypeConverter
import com.tomtruyen.core.common.JsonInstance
import com.tomtruyen.data.entities.ChangeType
import kotlinx.datetime.LocalDateTime
import kotlin.collections.List

class Converters {
    @TypeConverter
    fun fromListToString(list: List<String>): String {
        return JsonInstance.encodeToString(list)
    }

    @TypeConverter
    fun fromStringToList(string: String): List<String> {
        return JsonInstance.decodeFromString(string)
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
        return JsonInstance.encodeToString(changeTypes)
    }

    @TypeConverter
    fun fromStringToListChangeType(changeTypes: String): List<ChangeType> {
        return JsonInstance.decodeFromString(changeTypes)
    }
}