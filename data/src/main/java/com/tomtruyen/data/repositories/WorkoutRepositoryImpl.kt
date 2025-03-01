package com.tomtruyen.data.repositories

import com.tomtruyen.data.entities.Category
import com.tomtruyen.data.entities.Equipment
import com.tomtruyen.data.entities.Exercise
import com.tomtruyen.data.entities.Workout
import com.tomtruyen.data.entities.WorkoutExercise
import com.tomtruyen.data.entities.WorkoutExerciseSet
import com.tomtruyen.data.extensions.transaction
import com.tomtruyen.data.models.network.WorkoutNetworkModel
import com.tomtruyen.data.models.ui.WorkoutUiModel
import com.tomtruyen.data.repositories.interfaces.WorkoutRepository
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest

class WorkoutRepositoryImpl: WorkoutRepository() {
    private val dao = database.workoutDao()
    private val workoutExerciseDao = database.workoutExerciseDao()
    private val workoutExerciseSetDao = database.workoutExerciseSetDao()
    private val exerciseDao = database.exerciseDao()
    private val categoryDao = database.categoryDao()
    private val equipmentDao = database.equipmentDao()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun findWorkoutsAsync() = dao.findWorkoutsAsync().mapLatest { workouts ->
        workouts.map(WorkoutUiModel::fromEntity)
    }

    override suspend fun findWorkouts() = dao.findWorkouts().map(WorkoutUiModel::fromEntity)

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun findWorkoutByIdAsync(id: String) = dao.findByIdAsync(id).mapLatest { workout ->
        workout?.let(WorkoutUiModel::fromEntity)
    }

    override suspend fun findWorkoutById(id: String) = dao.findById(id)?.let(WorkoutUiModel::fromEntity)

    override suspend fun getWorkouts(userId: String, refresh: Boolean) = fetch(refresh) {
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
                val categories = mutableListOf<Category>()
                val equipment = mutableListOf<Equipment>()
                val exercises = mutableListOf<Exercise>()
                val workoutExercises = mutableListOf<WorkoutExercise>()
                val sets = mutableListOf<WorkoutExerciseSet>()

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
                    dao.deleteAll()

                    // Add the Entity
                    dao.saveAll(workouts)

                    // Add Referenced Items (Relations)
                    categoryDao.saveAll(categories)
                    equipmentDao.saveAll(equipment)
                    exerciseDao.saveAll(exercises)
                    workoutExerciseDao.saveAll(workoutExercises)
                    workoutExerciseSetDao.saveAll(sets)
                }
            }
    }

    override suspend fun saveWorkout(
        userId: String,
        workout: WorkoutUiModel,
    ) = supabase.transaction(
        onRollback = {
            supabase.from(Workout.TABLE_NAME).delete {
                filter {
                    Workout::id eq workout.id
                }
            }

            dao.deleteById(workout.id)
        }
    ) {
        val workoutEntity = workout.toEntity(userId)

        val exercises = workout.exercises.mapIndexed { index, exercise ->
            exercise.toEntity(workout.id, index)
        }

        val sets = workout.exercises.flatMap { exercise ->
            exercise.sets.mapIndexed { index, set ->
                set.toEntity(exercise.id, index)
            }
        }

        supabase.from(Workout.TABLE_NAME).upsert(workoutEntity)
        supabase.from(WorkoutExercise.TABLE_NAME).upsert(exercises)
        supabase.from(WorkoutExerciseSet.TABLE_NAME).upsert(sets)

        transaction {
            dao.save(workoutEntity)
            workoutExerciseDao.saveAll(exercises)
            workoutExerciseSetDao.saveAll(sets)
        }
    }

    override suspend fun deleteWorkout(workoutId: String) {
        supabase.from(Workout.TABLE_NAME).delete {
            filter {
                Workout::id eq workoutId
            }
        }

        dao.deleteById(workoutId)
    }
}