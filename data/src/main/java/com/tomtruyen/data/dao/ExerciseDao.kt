package com.tomtruyen.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.room.Upsert
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.tomtruyen.data.entities.Exercise
import com.tomtruyen.data.entities.ExerciseWithCategoryAndEquipment
import com.tomtruyen.data.models.ExerciseFilter
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ExerciseDao {
    @Upsert
    abstract suspend fun save(exercise: Exercise): Long

    @Upsert
    abstract suspend fun saveAll(exercises: List<Exercise>): List<Long>

    @Query("DELETE FROM ${Exercise.TABLE_NAME}")
    abstract suspend fun deleteAll(): Int

    @Query("DELETE FROM ${Exercise.TABLE_NAME} WHERE id = :id")
    abstract suspend fun deleteById(id: String): Int

    @Transaction
    @Query("SELECT * FROM ${Exercise.TABLE_NAME} WHERE id = :id")
    abstract fun findById(id: String): ExerciseWithCategoryAndEquipment?

    @Transaction
    @Query("SELECT * FROM ${Exercise.TABLE_NAME} WHERE id = :id")
    abstract fun findByIdAsync(id: String): Flow<ExerciseWithCategoryAndEquipment?>

    fun findAllAsync(query: String, filter: ExerciseFilter): Flow<List<ExerciseWithCategoryAndEquipment>> {
        return findAllAsync(findAllQuery(query, filter))
    }

    @Transaction
    @RawQuery(observedEntities = [Exercise::class])
    abstract fun findAllAsync(query: SupportSQLiteQuery): Flow<List<ExerciseWithCategoryAndEquipment>>

    private fun findAllQuery(query: String, filter: ExerciseFilter): SupportSQLiteQuery {
        val sql = buildString {
            append("SELECT * FROM ${Exercise.TABLE_NAME}")

            val filters = mutableListOf<String>()

            if (query.isNotEmpty()) {
                filters.add("name LIKE '%$query%'")
            }

            if (filter.categories.isNotEmpty()) {
                val categoriesString = filter.categories.joinToString(",") { "'${it.id}'" }
                filters.add("categoryId IN ($categoriesString)")
            }

            if (filter.equipment.isNotEmpty()) {
                val equipmentString = filter.equipment.joinToString(",") { "'${it.id}'" }
                filters.add("equipmentId IN ($equipmentString)")
            }

            if (filters.isNotEmpty()) {
                append(" WHERE ")
                append(filters.joinToString(" AND "))
            }

            append(" ORDER BY LOWER(name) ASC")
        }

        return SimpleSQLiteQuery(sql)
    }
}