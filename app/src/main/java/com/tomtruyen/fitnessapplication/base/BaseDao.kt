package com.tomtruyen.fitnessapplication.base

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Upsert
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import kotlinx.coroutines.flow.Flow

@Dao
abstract class BaseDao<T>(private val tableName: String) {
    @Upsert
    abstract suspend fun save(data: T): Long

    @Upsert
    abstract suspend fun saveAll(data: List<T>): List<Long>

    @RawQuery
    abstract fun findAllAsync(query: SupportSQLiteQuery = findAllQuery()): Flow<List<T>>

    private fun findAllQuery(): SimpleSQLiteQuery {
        return SimpleSQLiteQuery("SELECT * FROM $tableName")
    }
}