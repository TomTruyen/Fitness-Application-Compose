package com.tomtruyen.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.tomtruyen.data.entities.SyncCache

@Dao
interface SyncCacheDao {
    @Upsert
    suspend fun save(cache: SyncCache): Long

    @Upsert
    suspend fun saveAll(cache: List<SyncCache>)

    @Query("SELECT * FROM ${SyncCache.TABLE_NAME} WHERE id = :id")
    suspend fun findById(id: String?): SyncCache?

    @Query("SELECT id FROM ${SyncCache.TABLE_NAME}")
    suspend fun findAll(): List<String>

    @Query("DELETE FROM ${SyncCache.TABLE_NAME} WHERE id LIKE :startsWith || '%'")
    suspend fun deleteStartsWith(startsWith: String): Int
}