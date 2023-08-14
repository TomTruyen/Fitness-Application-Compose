package com.tomtruyen.fitnessapplication.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Upsert
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.tomtruyen.fitnessapplication.data.entities.Exercise
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ExerciseDao {

    @Query("SELECT DISTINCT category FROM ${Exercise.TABLE_NAME} WHERE category IS NOT NULL AND category != ''")
    abstract fun findCategories(): Flow<List<String>>

    @Query("SELECT DISTINCT equipment FROM ${Exercise.TABLE_NAME} WHERE equipment IS NOT NULL AND equipment != ''")
    abstract fun findEquipment(): Flow<List<String>>

    @Upsert
    abstract fun saveAll(exercises: List<Exercise>): List<Long>

    fun findAllAsync(query: String, categories: List<String>, equipment: List<String>): Flow<List<Exercise>> {
        return findAllAsync(findAllQuery(query, categories, equipment))
    }

    @RawQuery(observedEntities = [Exercise::class])
    abstract fun findAllAsync(query: SupportSQLiteQuery): Flow<List<Exercise>>

    private fun findAllQuery(query: String, categories: List<String>, equipment: List<String>): SupportSQLiteQuery {
        val sql = buildString {
            append("SELECT * FROM ${Exercise.TABLE_NAME}")

            val filters = mutableListOf<String>()

            if (query.isNotEmpty()) {
                filters.add("name LIKE '%$query%'")
            }

            if (categories.isNotEmpty()) {
                val categoriesString = categories.joinToString(",") { "'$it'" }
                filters.add("category IN ($categoriesString)")
            }

            if (equipment.isNotEmpty()) {
                val equipmentString = equipment.joinToString(",") { "'$it'" }
                filters.add("equipment IN ($equipmentString)")
            }

            if (filters.isNotEmpty()) {
                append(" WHERE ")
                append(filters.joinToString(" AND "))
            }
        }

        return SimpleSQLiteQuery(sql)
    }
}