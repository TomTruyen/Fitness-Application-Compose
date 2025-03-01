package com.tomtruyen.data.repositories

import android.util.Log
import com.tomtruyen.data.entities.Exercise
import com.tomtruyen.data.entities.Workout
import com.tomtruyen.data.entities.WorkoutExercise
import com.tomtruyen.data.entities.WorkoutExerciseSet
import com.tomtruyen.data.extensions.transaction
import com.tomtruyen.data.models.ui.WorkoutUiModel
import com.tomtruyen.data.repositories.interfaces.WorkoutRepository
import io.github.jan.supabase.postgrest.from
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
        // TODO: Join all child tables like:
        /*
         - Workout
         - WorkoutExercise
         - WorkoutExerciseSet
         - Exercise
         - Category
         - Equipment
         */
//        db.collection(USER_WORKOUT_COLLECTION_NAME)
//            .document(userId)
//            .get()
//            .handleCompletionResult(
//                context = context,
//                callback = callback,
//            ) {
//                val workouts = it.toObject(WorkoutsResponse::class.java)?.data.orEmpty()
//
//                launchWithCacheTransactions {
//                    workoutDao.deleteAll()
//                    saveWorkoutResponses(workouts)
//                }
//
//                callback.onSuccess(workouts)
//            }
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

        launchWithTransaction {
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