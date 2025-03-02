package com.tomtruyen.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.tomtruyen.data.entities.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM ${Category.TABLE_NAME}")
    fun findCategories(): Flow<List<Category>>

    @Upsert
    suspend fun saveAll(categories: Set<Category>): List<Long>
}