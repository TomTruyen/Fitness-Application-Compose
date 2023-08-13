package com.tomtruyen.fitnessapplication.base

import androidx.room.Dao
import androidx.room.Upsert

@Dao
abstract class BaseDao<T>(private val tableName: String) {
    @Upsert
    abstract suspend fun save(data: T): Long

    @Upsert
    abstract suspend fun saveAll(data: List<T>): List<Long>
}