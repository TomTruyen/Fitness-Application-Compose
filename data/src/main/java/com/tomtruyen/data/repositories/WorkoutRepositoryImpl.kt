package com.tomtruyen.data.repositories

import com.google.firebase.firestore.FieldValue
import com.tomtruyen.data.firebase.extensions.handleCompletionResult
import com.tomtruyen.data.firebase.models.FirebaseCallback
import com.tomtruyen.data.firebase.models.WorkoutResponse
import com.tomtruyen.data.firebase.models.WorkoutsResponse
import com.tomtruyen.data.repositories.interfaces.WorkoutRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WorkoutRepositoryImpl(
    private val workoutDao: com.tomtruyen.data.dao.WorkoutDao,
    private val workoutExerciseDao: com.tomtruyen.data.dao.WorkoutExerciseDao,
    private val workoutSetDao: com.tomtruyen.data.dao.WorkoutSetDao,
    private val exerciseDao: com.tomtruyen.data.dao.ExerciseDao
): WorkoutRepository() {
    override fun findWorkoutsAsync() = workoutDao.findWorkoutsAsync()

    override suspend fun findWorkouts() = workoutDao.findWorkouts()

    override fun findWorkoutByIdAsync(id: String) = workoutDao.findByIdAsync(id)

    override fun findWorkoutById(id: String) = workoutDao.findById(id)

    override suspend fun getWorkouts(userId: String, callback: FirebaseCallback<List<WorkoutResponse>>) = fetch(
        onStopLoading = callback::onStopLoading
    ) {
        db.collection(USER_WORKOUT_COLLECTION_NAME)
            .document(userId)
            .get()
            .handleCompletionResult(
                context = context,
                callback = callback,
            ) {
                val workouts = it.toObject(WorkoutsResponse::class.java)?.data.orEmpty()

                launchWithCacheTransactions {
                    workoutDao.deleteAll()
                    saveWorkoutResponses(workouts)
                }

                callback.onSuccess(workouts)
            }
    }

    override suspend fun saveWorkout(
        userId: String,
        workout: WorkoutResponse,
        isUpdate: Boolean,
        callback: FirebaseCallback<Unit>
    ) = withContext(Dispatchers.IO) {
        val workouts = workoutDao.findWorkouts().map {
            it.toWorkoutResponse()
        }.toMutableList().apply {
            if(isUpdate) {
                removeIf { it.id == workout.id }
            }

            add(workout)
        }

        db.collection(USER_WORKOUT_COLLECTION_NAME)
            .document(userId)
            .set(WorkoutsResponse(workouts))
            .handleCompletionResult(
                context = context,
                callback = callback
            ) {
                launchWithTransaction {
                    saveWorkoutResponses(workouts)
                }

                callback.onSuccess(Unit)
            }
    }

    override suspend fun deleteWorkout(
        userId: String,
        workoutId: String,
        callback: FirebaseCallback<Unit>
    ) = withContext(Dispatchers.IO) {
        val workout = workoutDao.findById(workoutId)?.toWorkoutResponse() ?: return@withContext callback.onStopLoading()

        db.collection(USER_WORKOUT_COLLECTION_NAME)
            .document(userId)
            .update("data", FieldValue.arrayRemove(workout))
            .handleCompletionResult(
                context = context,
                callback = callback
            ) {
                launchWithTransaction {
                    workoutDao.deleteById(workoutId)
                }

                callback.onSuccess(Unit)
            }
    }

    override suspend fun saveWorkoutResponses(responses: List<WorkoutResponse>) {
        transaction {
            val workouts = responses.map { it.toWorkout() }
            val workoutExercises = responses.flatMap { it.exercises.map { exercise -> exercise.toWorkoutExercise(it.id) } }
            val exercises = responses.flatMap { it.exercises.map { exercise -> exercise.exercise } }
            val sets = responses.flatMap { it.exercises.flatMap { exercise -> exercise.sets } }

            exerciseDao.saveAll(exercises)
            workoutDao.saveAll(workouts)
            workoutExerciseDao.saveAll(workoutExercises)
            workoutSetDao.saveAll(sets)
        }
    }

    companion object {
        private const val USER_WORKOUT_COLLECTION_NAME = "workouts"
    }
}