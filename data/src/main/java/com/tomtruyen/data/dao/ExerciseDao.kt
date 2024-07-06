package com.tomtruyen.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Upsert
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.tomtruyen.data.entities.Exercise
import com.tomtruyen.models.ExerciseFilter
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ExerciseDao {
    @Query("SELECT DISTINCT category FROM ${Exercise.TABLE_NAME} WHERE category IS NOT NULL AND category != ''")
    abstract fun findCategories(): Flow<List<String>>

    @Query("SELECT DISTINCT equipment FROM ${Exercise.TABLE_NAME} WHERE equipment IS NOT NULL AND equipment != ''")
    abstract fun findEquipment(): Flow<List<String>>

    @Upsert
    abstract fun save(exercise: Exercise): Long

    @Upsert
    abstract fun saveAll(exercises: List<Exercise>): List<Long>

    @Query("DELETE FROM ${Exercise.TABLE_NAME} WHERE isUserCreated = 0")
    abstract fun deleteAllNonUserExercises(): Int

    @Query("DELETE FROM ${Exercise.TABLE_NAME} WHERE isUserCreated = 1")
    abstract fun deleteAllUserExercises(): Int

    @Query("DELETE FROM ${Exercise.TABLE_NAME} WHERE NOT id IN (:ids) AND isUserCreated = 1")
    abstract fun deleteAllUserExercisesExcept(ids: List<String>): Int

    @Query("SELECT * FROM ${Exercise.TABLE_NAME} WHERE id = :id")
    abstract fun findByIdAsync(id: String): Flow<Exercise?>

    @Query("SELECT * FROM ${Exercise.TABLE_NAME} WHERE id = :id AND isUserCreated = 1")
    abstract fun findUserExerciseById(id: String): Exercise?

    @Query("DELETE FROM ${Exercise.TABLE_NAME} WHERE id = :id AND isUserCreated = 1")
    abstract fun deleteUserExerciseById(id: String): Int

    @Query("SELECT * FROM ${Exercise.TABLE_NAME} WHERE isUserCreated = 1")
    abstract fun findAllUserExercises(): List<Exercise>

    fun findAllAsync(query: String, filter: ExerciseFilter): Flow<List<Exercise>> {
        return findAllAsync(findAllQuery(query, filter))
    }

    @RawQuery(observedEntities = [Exercise::class])
    abstract fun findAllAsync(query: SupportSQLiteQuery): Flow<List<Exercise>>

    private fun findAllQuery(query: String, filter: ExerciseFilter): SupportSQLiteQuery {
        val sql = buildString {
            append("SELECT * FROM ${Exercise.TABLE_NAME}")

            val filters = mutableListOf<String>()

            if (query.isNotEmpty()) {
                filters.add("name LIKE '%$query%'")
            }

            if (filter.categories.isNotEmpty()) {
                val categoriesString = filter.categories.joinToString(",") { "'$it'" }
                filters.add("category IN ($categoriesString)")
            }

            if (filter.equipment.isNotEmpty()) {
                val equipmentString = filter.equipment.joinToString(",") { "'$it'" }
                filters.add("equipment IN ($equipmentString)")
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