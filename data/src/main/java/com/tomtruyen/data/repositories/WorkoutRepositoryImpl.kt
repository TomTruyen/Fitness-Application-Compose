package com.tomtruyen.data.repositories

import com.tomtruyen.data.entities.Category
import com.tomtruyen.data.entities.Equipment
import com.tomtruyen.data.entities.Exercise
import com.tomtruyen.data.entities.Workout
import com.tomtruyen.data.entities.WorkoutExercise
import com.tomtruyen.data.entities.WorkoutExerciseSet
import com.tomtruyen.data.entities.WorkoutWithExercises
import com.tomtruyen.data.models.mappers.WorkoutExerciseSetUiModelMapper
import com.tomtruyen.data.models.mappers.WorkoutExerciseUiModelMapper
import com.tomtruyen.data.models.mappers.WorkoutUiModelMapper
import com.tomtruyen.data.models.network.WorkoutNetworkModel
import com.tomtruyen.data.models.ui.WorkoutUiModel
import com.tomtruyen.data.repositories.interfaces.WorkoutRepository
import com.tomtruyen.data.worker.SyncWorker
import com.tomtruyen.data.worker.WorkoutSyncWorker
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest
import java.util.UUID

class WorkoutRepositoryImpl : WorkoutRepository() {
    private val workoutExerciseDao = database.workoutExerciseDao()
    private val workoutExerciseSetDao = database.workoutExerciseSetDao()
    private val exerciseDao = database.exerciseDao()
    private val categoryDao = database.categoryDao()
    private val equipmentDao = database.equipmentDao()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun findWorkoutsAsync() = dao.findWorkoutsAsync().mapLatest { workouts ->
        workouts.map(WorkoutUiModelMapper::fromEntity)
    }

    override suspend fun findWorkouts() = dao.findWorkouts().map(WorkoutUiModelMapper::fromEntity)

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun findWorkoutByIdAsync(id: String) = dao.findByIdAsync(id).mapLatest { workout ->
        workout?.let(WorkoutUiModelMapper::fromEntity)
    }

    override suspend fun findWorkoutById(id: String) =
        dao.findById(id)?.let(WorkoutUiModelMapper::fromEntity)

    override suspend fun getWorkouts(userId: String, refresh: Boolean) {
        fetch(refresh) {
            supabase.from(Workout.TABLE_NAME)
                .select(
                    columns = Columns.raw(
                        """
                        *,
                        ${WorkoutExercise.TABLE_NAME}(
                            *, 
                            ${WorkoutExerciseSet.TABLE_NAME}(*),
                            ${Exercise.TABLE_NAME}(
                                *,
                                ${Category.TABLE_NAME}(*),
                                ${Equipment.TABLE_NAME}(*)
                            )
                        )
                        
                    """.trimIndent()
                    )
                ) {
                    filter {
                        Workout::userId eq userId
                    }
                }
                .decodeList<WorkoutNetworkModel>()
                .let { response ->
                    // Using MutableList instead of mapping for each one to reduce amount of loops
                    val categories = mutableSetOf<Category>()
                    val equipment = mutableSetOf<Equipment>()
                    val exercises = mutableSetOf<Exercise>()
                    val workoutExercises = mutableSetOf<WorkoutExercise>()
                    val sets = mutableSetOf<WorkoutExerciseSet>()

                    val workouts = response.map { workout ->
                        workout.exercises.forEach { workoutExercise ->
                            workoutExercise.exercise.category?.let { categories.add(it) }
                            workoutExercise.exercise.equipment?.let { equipment.add(it) }

                            exercises.add(workoutExercise.exercise.toEntity())
                            sets.addAll(workoutExercise.sets)

                            workoutExercises.add(workoutExercise.toEntity())
                        }

                        workout.toEntity()
                    }

                    cacheTransaction {
                        // Clear Table
                        dao.deleteAllWorkoutsExcept(listOf(Workout.ACTIVE_WORKOUT_ID))

                        // Add the Entity
                        dao.saveAll(workouts)

                        // Add Referenced Items (Relations)
                        categoryDao.saveAll(categories.toList())
                        equipmentDao.saveAll(equipment.toList())
                        exerciseDao.saveAll(exercises.toList())
                        workoutExerciseDao.saveAll(workoutExercises.toList())
                        workoutExerciseSetDao.saveAll(sets.toList())
                    }
                }
        }
    }

    override suspend fun saveWorkout(
        userId: String,
        workout: WorkoutUiModel,
    ) {
        val workoutEntity = WorkoutUiModelMapper.toEntity(
            model = workout,
            userId = userId
        )

        val sets = mutableListOf<WorkoutExerciseSet>()
        val exercises = workout.exercises.mapIndexed { index, exercise ->
            WorkoutExerciseUiModelMapper.toEntity(
                model = exercise,
                workoutId = workoutEntity.id,
                index = index
            ).also { workoutExercise ->
                sets.addAll(
                    exercise.sets.mapIndexed { setIndex, set ->
                        WorkoutExerciseSetUiModelMapper.toEntity(
                            model = set,
                            workoutExerciseId = workoutExercise.id,
                            index = setIndex
                        )
                    }
                )
            }
        }

        transaction {
            dao.deleteById(workoutEntity.id)

            dao.save(workoutEntity)
            workoutExerciseDao.saveAll(exercises)
            workoutExerciseSetDao.saveAll(sets)
        }

        SyncWorker.schedule<WorkoutSyncWorker>()
    }

    override suspend fun deleteWorkout(workoutId: String) {
        supabase.from(Workout.TABLE_NAME).delete {
            filter {
                Workout::id eq workoutId
            }
        }

        dao.deleteById(workoutId)
    }

    // Each item should have "synced = true" since we don't want the SyncWorker to sync them to the backend
    override suspend fun saveActiveWorkout(workout: WorkoutUiModel) {
        val sets = mutableListOf<WorkoutExerciseSet>()

        val workoutEntity = WorkoutUiModelMapper.toEntity(
            model = workout,
        ).copy(
            id = Workout.ACTIVE_WORKOUT_ID,
            synced = true
        )

        // Exercises and Sets require a composed UUID since:
        // 1. They can't have their old id, since then that object will update causing it to no longer be connected to the original workout (if any)
        // 2. They can't have just some random id, since we don't want to update its id on every save as that causes too much rendering and database changes
        val exercises = workout.exercises.mapIndexed { index, exercise ->
            WorkoutExerciseUiModelMapper.toEntity(
                model = exercise,
                workoutId = workoutEntity.id,
                index = index
            ).let { newExercise ->
                if (newExercise.id.startsWith(Workout.ACTIVE_WORKOUT_ID)) return@let newExercise

                newExercise.copy(
                    id = "${Workout.ACTIVE_WORKOUT_ID}_${UUID.randomUUID()}",
                    synced = true
                )
            }.also { workoutExercise ->
                sets.addAll(
                    exercise.sets.mapIndexed { setIndex, set ->
                        WorkoutExerciseSetUiModelMapper.toEntity(
                            model = set,
                            workoutExerciseId = workoutExercise.id,
                            index = setIndex,
                            withChangeRecord = true
                        ).let { newSet ->
                            if (newSet.id.startsWith(Workout.ACTIVE_WORKOUT_ID)) return@let newSet

                            newSet.copy(
                                id = "${Workout.ACTIVE_WORKOUT_ID}_${UUID.randomUUID()}",
                                synced = true
                            )
                        }
                    }
                )
            }
        }

        transaction {
            // Delete Active Workout from Dao
            // This is to cleanup dangling exercises and sets
            // Since we use a Transaction this won't have an impact on UI
            // It will be a little more demanding in the Background, but that is fine
            dao.deleteById(workoutEntity.id)

            dao.save(workoutEntity)

            workoutExerciseDao.saveAll(exercises)
            workoutExerciseSetDao.saveAll(sets)
        }
    }

    override suspend fun deleteActiveWorkout() {
        transaction {
            dao.deleteById(Workout.ACTIVE_WORKOUT_ID)
        }
    }

    override suspend fun reorderWorkouts(workouts: List<WorkoutUiModel>) {
        val items = workouts.map(WorkoutUiModelMapper::toEntity)

        dao.saveAll(items)

        supabase.from(Workout.TABLE_NAME).upsert(items)
    }

    override suspend fun sync(item: WorkoutWithExercises) {
        val workout = item.workout.copy(synced = true)

        val exercises = item.exercises.map {
            it.workoutExercise.copy(
                synced = true
            )
        }

        val sets = item.exercises.flatMap { exercise ->
            exercise.sets.map {
                it.copy(synced = true)
            }
        }

        // Delete "Dangling" Exercises and Sets
        deleteSupabaseDangling(
            table = WorkoutExercise.TABLE_NAME,
            key = WorkoutExercise.KEY_ID,
            entitiesToKeep = exercises,
            foreignKeyConstraint = FilterOperation(
                column = WorkoutExercise.KEY_WORKOUT_ID,
                operator = FilterOperator.EQ,
                value = workout.id
            )
        )

        deleteSupabaseDangling(
            table = WorkoutExerciseSet.TABLE_NAME,
            key = WorkoutExerciseSet.KEY_ID,
            entitiesToKeep = sets,
            foreignKeyConstraint = FilterOperation(
                column = WorkoutExerciseSet.KEY_WORKOUT_EXERCISE_ID,
                operator = FilterOperator.IN,
                value = "(${sets.joinToString(",") { it.id }})"
            )
        )

        // Insert new values
        supabase.from(Workout.TABLE_NAME).upsert(workout)
        supabase.from(WorkoutExercise.TABLE_NAME).upsert(exercises)
        supabase.from(WorkoutExerciseSet.TABLE_NAME).upsert(sets)

        // Update the local items to be "synced"
        transaction {
            dao.save(workout)
            workoutExerciseDao.saveAll(exercises)
            workoutExerciseSetDao.saveAll(sets)
        }
    }
}