package com.tomtruyen.fitnessapplication.repositories

import com.google.firebase.firestore.FieldValue
import com.tomtruyen.fitnessapplication.data.dao.ExerciseDao
import com.tomtruyen.fitnessapplication.data.dao.WorkoutDao
import com.tomtruyen.fitnessapplication.data.dao.WorkoutExerciseDao
import com.tomtruyen.fitnessapplication.data.dao.WorkoutSetDao
import com.tomtruyen.fitnessapplication.extensions.handleCompletionResult
import com.tomtruyen.fitnessapplication.helpers.GlobalProvider
import com.tomtruyen.fitnessapplication.model.FetchedData
import com.tomtruyen.fitnessapplication.model.FirebaseCallback
import com.tomtruyen.fitnessapplication.networking.models.WorkoutResponse
import com.tomtruyen.fitnessapplication.networking.models.WorkoutsResponse
import com.tomtruyen.fitnessapplication.repositories.interfaces.WorkoutRepository
import kotlinx.coroutines.launch

class WorkoutRepositoryImpl(
    private val workoutDao: WorkoutDao,
    private val workoutExerciseDao: WorkoutExerciseDao,
    private val workoutSetDao: WorkoutSetDao,
    private val exerciseDao: ExerciseDao
): WorkoutRepository() {
    override fun findWorkoutsAsync() = workoutDao.findWorkoutsAsync()

    override suspend fun findWorkouts() = workoutDao.findWorkouts()

    override fun findWorkoutByIdAsync(id: String) = workoutDao.findByIdAsync(id)

    override fun findWorkoutById(id: String) = workoutDao.findById(id)

    override fun getWorkouts(userId: String, callback: FirebaseCallback<List<WorkoutResponse>>) = tryRequestWhenNotFetched(
        identifier = FetchedData.Type.WORKOUTS.identifier,
        onStopLoading = callback::onStopLoading
    ) {
        db.collection(USER_WORKOUT_COLLECTION_NAME)
            .document(userId)
            .get()
            .handleCompletionResult(
                context = globalProvider.context,
                callback = callback,
                setFetchSuccessful = {
                    setFetchSuccessful(FetchedData.Type.WORKOUTS.identifier)
                }
            ) {
                val workouts = it.toObject(WorkoutsResponse::class.java)?.data ?: emptyList()

                launchWithTransaction {
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
    ) {
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
                context = globalProvider.context,
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
    ) {
        val workout = workoutDao.findById(workoutId)?.toWorkoutResponse() ?: return callback.onStopLoading()

        db.collection(USER_WORKOUT_COLLECTION_NAME)
            .document(userId)
            .update("data", FieldValue.arrayRemove(workout))
            .handleCompletionResult(
                context = globalProvider.context,
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