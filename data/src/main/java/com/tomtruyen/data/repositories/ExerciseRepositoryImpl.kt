package com.tomtruyen.data.repositories

import com.tomtruyen.data.dao.ExerciseDao
import com.tomtruyen.data.entities.Exercise
import com.tomtruyen.data.entities.ExerciseWithCategoryAndEquipment
import com.tomtruyen.data.models.ExerciseFilter
import com.tomtruyen.data.models.ui.ExerciseUiModel
import com.tomtruyen.data.repositories.interfaces.ExerciseRepository
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest

class ExerciseRepositoryImpl(
    private val exerciseDao: ExerciseDao
) : ExerciseRepository() {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun findExercises(query: String, filter: ExerciseFilter) = exerciseDao.findAllAsync(
        query = query,
        filter = filter,
    ).mapLatest { exercises ->
        exercises.map(ExerciseUiModel::fromEntity)
    }

    override suspend fun findExerciseById(id: String) = exerciseDao.findById(id)?.let(ExerciseUiModel::fromEntity)

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun findExerciseByIdAsync(id: String) = exerciseDao.findByIdAsync(id).mapLatest { exercise ->
        exercise?.let(ExerciseUiModel::fromEntity)
    }

    override suspend fun getExercises(userId: String?, refresh: Boolean) =
        fetch(refresh = refresh) {
            supabase.from(Exercise.TABLE_NAME)
                .select {
                    filter {
                        or {
                            Exercise::userId eq userId
                            Exercise::userId isExact null
                        }
                    }

                    // TODO: Join to get actual Category and Equipment

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
        exercise: ExerciseUiModel,
    ) {
        val newExercise = exercise.toEntity().exercise.copy(
            userId = userId
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