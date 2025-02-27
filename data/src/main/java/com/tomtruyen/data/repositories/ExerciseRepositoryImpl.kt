package com.tomtruyen.data.repositories

import android.util.Log
import com.tomtruyen.data.dao.ExerciseDao
import com.tomtruyen.data.entities.Exercise
import com.tomtruyen.data.repositories.interfaces.ExerciseRepository
import com.tomtruyen.models.ExerciseFilter
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order

class ExerciseRepositoryImpl(
    private val exerciseDao: ExerciseDao
): ExerciseRepository() {
    override fun findExercises(query: String, filter: ExerciseFilter) = exerciseDao.findAllAsync(
        query = query,
        filter = filter,
    )

    override fun findExerciseById(id: String) = exerciseDao.findByIdAsync(id)

    override fun findCategories() = exerciseDao.findCategories()

    override fun findEquipment() = exerciseDao.findEquipment()

    override suspend fun getExercises(userId: String?, refresh: Boolean) = fetch(refresh = refresh) {
        supabase.from(Exercise.TABLE_NAME)
            .select {
                filter {
                    or {
                        Exercise::userId eq userId
                        Exercise::userId isExact null
                    }
                }

                order(
                    column = "name",
                    order = Order.ASCENDING
                )
            }.also {
                Log.d("@@@", it.data)
            }
            .decodeList<Exercise>()
            .let { exercises ->
                launchWithCacheTransactions {
                    exerciseDao.deleteAll()
                    exerciseDao.saveAll(exercises)
                }
            }
    }

    override suspend fun saveUserExercise(
        userId: String,
        exercise: Exercise,
    ) {
        val newExercise = exercise.copy(
            userId = userId,
            category = if(exercise.category == Exercise.DEFAULT_DROPDOWN_VALUE) null else exercise.category,
            equipment = if(exercise.equipment == Exercise.DEFAULT_DROPDOWN_VALUE) null else exercise.equipment
        )

        supabase.from(Exercise.TABLE_NAME).upsert(newExercise)

        launchWithCacheTransactions {
            exerciseDao.save(newExercise)
        }
    }

    override suspend fun deleteUserExercise(
        userId: String,
        exerciseId: String,
    ) {
      supabase.from(Exercise.TABLE_NAME)
          .delete {
              filter {
                  Exercise::id eq exerciseId
              }
          }

        launchWithTransaction {
            exerciseDao.deleteById(exerciseId)
        }
    }
}