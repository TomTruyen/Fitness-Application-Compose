package com.tomtruyen.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.tomtruyen.data.entities.CacheTTL

@Dao
interface CacheTTLDao {
    @Upsert
    suspend fun save(cache: CacheTTL): Long

    @Query("SELECT * FROM ${CacheTTL.TABLE_NAME} WHERE id = :id")
    suspend fun findById(id: String?): CacheTTL?
}