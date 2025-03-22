package com.tomtruyen.data

import androidx.room.TypeConverter
import com.tomtruyen.core.common.JsonInstance
import com.tomtruyen.core.common.models.ChangeType
import kotlinx.datetime.LocalDateTime

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
    fun fromSetChangeTypeToString(changeTypes: Set<ChangeType>): String {
        return JsonInstance.encodeToString(changeTypes)
    }

    @TypeConverter
    fun fromStringToSetChangeType(changeTypes: String): Set<ChangeType> {
        return JsonInstance.decodeFromString(changeTypes)
    }
}