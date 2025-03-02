package com.tomtruyen.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.tomtruyen.data.entities.Equipment
import kotlinx.coroutines.flow.Flow

@Dao
interface EquipmentDao {
    @Query("SELECT * FROM ${Equipment.TABLE_NAME}")
    fun findEquipment(): Flow<List<Equipment>>

    @Upsert
    suspend fun saveAll(equipment: Set<Equipment>): List<Long>
}