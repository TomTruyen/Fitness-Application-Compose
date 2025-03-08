package com.tomtruyen.data.dao

import androidx.room.Dao
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
abstract class SyncDao<T>(
    private val tableName: String,
) {
    @Transaction
    @RawQuery
    protected abstract suspend fun findItems(query: SupportSQLiteQuery): List<T>?

    suspend fun findSyncItems(synced: Boolean = false): List<T> {
        return try {
            val query = SimpleSQLiteQuery("SELECT * FROM $tableName WHERE synced = $synced")
            findItems(query).orEmpty()
        } catch (e: Exception) {
            // If the item does not have "synced" item might throw an exception
            emptyList()
        }
    }
}