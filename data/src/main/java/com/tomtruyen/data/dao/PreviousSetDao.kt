package com.tomtruyen.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.tomtruyen.data.entities.PreviousSet

@Dao
interface PreviousSetDao {
    @Upsert
    suspend fun saveAll(sets: List<PreviousSet>)

    @Query("SELECT * FROM ${PreviousSet.TABLE_NAME}")
    suspend fun findAllAsync(): List<PreviousSet>
}