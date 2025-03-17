package com.tomtruyen.data.repositories

import com.tomtruyen.data.entities.Category
import com.tomtruyen.data.entities.Equipment
import com.tomtruyen.data.entities.Exercise
import com.tomtruyen.data.models.ExerciseFilter
import com.tomtruyen.data.models.mappers.ExerciseUiModelMapper
import com.tomtruyen.data.models.network.ExerciseNetworkModel
import com.tomtruyen.data.models.ui.ExerciseUiModel
import com.tomtruyen.data.repositories.interfaces.ExerciseRepository
import com.tomtruyen.data.repositories.interfaces.PreviousSetRepository
import com.tomtruyen.data.worker.ExerciseSyncWorker
import com.tomtruyen.data.worker.SyncWorker
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest

class ExerciseRepositoryImpl(
    private val previousSetRepository: PreviousSetRepository
) : ExerciseRepository() {
    private val categoryDao = database.categoryDao()
    private val equipmentDao = database.equipmentDao()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun findExercises(query: String, filter: ExerciseFilter) = dao.findAllAsync(
        query = query,
        filter = filter,
    ).mapLatest { exercises ->
        exercises.map(ExerciseUiModelMapper::fromEntity)
    }

    override suspend fun findExerciseById(id: String) =
        dao.findById(id)?.let(ExerciseUiModelMapper::fromEntity)

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun findExerciseByIdAsync(id: String) = dao.findByIdAsync(id).mapLatest { exercise ->
        exercise?.let(ExerciseUiModelMapper::fromEntity)
    }

    override suspend fun getExercises(userId: String?, refresh: Boolean) {
        fetch(refresh = refresh) {
            supabase.from(Exercise.TABLE_NAME)
                .select(
                    columns = Columns.list(
                        "*",
                        "${Category.TABLE_NAME}(*)",
                        "${Equipment.TABLE_NAME}(*)"
                    )
                ) {
                    filter {
                        or {
                            Exercise::userId eq userId
                            Exercise::userId isExact null
                        }
                    }
                }
                .decodeList<ExerciseNetworkModel>()
                .let { response ->
                    // Using MutableList instead of mapping for each one to reduce amount of loops
                    val categories = mutableListOf<Category>()
                    val equipment = mutableListOf<Equipment>()

                    val exercises = response.map { exercise ->
                        exercise.category?.let(categories::add)
                        exercise.equipment?.let(equipment::add)

                        exercise.toEntity()
                    }

                    cacheTransaction {
                        // Add Referenced Items (Relations)
                        categoryDao.saveAll(categories)
                        equipmentDao.saveAll(equipment)

                        // Add the Entity
                        dao.saveAll(exercises)
                    }
                }
        }

        previousSetRepository.getPreviousSetsForExercises()
    }

    override suspend fun saveExercise(
        userId: String,
        exercise: ExerciseUiModel,
    ) {
        val newExercise =  ExerciseUiModelMapper.toEntity(
            model = exercise,
            userId = userId
        ).exercise

        dao.save(newExercise)

        SyncWorker.schedule<ExerciseSyncWorker>()
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

        transaction {
            dao.deleteById(exerciseId)
        }
    }

    override suspend fun sync(item: Exercise) {
        supabase.from(Exercise.TABLE_NAME).upsert(item)

        dao.save(item.copy(synced = true))
    }
}