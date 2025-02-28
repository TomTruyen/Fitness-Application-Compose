package com.tomtruyen.data.repositories

import com.tomtruyen.data.dao.ExerciseDao
import com.tomtruyen.data.entities.Exercise
import com.tomtruyen.data.entities.ExerciseWithCategoryAndEquipment
import com.tomtruyen.data.repositories.interfaces.ExerciseRepository
import com.tomtruyen.data.models.ExerciseFilter
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order

class ExerciseRepositoryImpl(
    private val exerciseDao: ExerciseDao
): ExerciseRepository() {
    override fun findExercises(query: String, filter: ExerciseFilter) = exerciseDao.findAllAsync(
        query = query,
        filter = filter,
    )

    override suspend fun findExerciseById(id: String) = exerciseDao.findById(id)

    override fun findExerciseByIdAsync(id: String) = exerciseDao.findByIdAsync(id)

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
            }
            .decodeList<Exercise>()
            .let { exercises ->
                launchWithCacheTransactions {
                    exerciseDao.deleteAll()
                    exerciseDao.saveAll(exercises)
                }
            }
    }

    override suspend fun saveExercise(
        userId: String,
        userExercise: ExerciseWithCategoryAndEquipment,
    ) {
        val (exercise, category, equipment) = userExercise

        val newExercise = exercise.copy(
            userId = userId,
            categoryId = if(category?.isDefault == true) {
                null
            } else {
                category?.id
            },
            equipmentId = if(equipment?.isDefault == true) {
                null
            } else {
                equipment?.id
            }
        )

        supabase.from(Exercise.TABLE_NAME).upsert(newExercise)

        launchWithCacheTransactions {
            exerciseDao.save(newExercise)
        }
    }

    override suspend fun deleteExercise(
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