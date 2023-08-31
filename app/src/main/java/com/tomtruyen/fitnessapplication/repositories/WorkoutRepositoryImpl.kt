package com.tomtruyen.fitnessapplication.repositories

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
    private val globalProvider: GlobalProvider,
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
        type = FetchedData.Type.WORKOUTS,
        onStopLoading = {
            callback.onStopLoading()
        }
    ) {
        db.collection(USER_WORKOUT_COLLECTION_NAME)
            .document(userId)
            .get()
            .handleCompletionResult(
                context = globalProvider.context,
                callback = callback
            ) {
                val workouts = it.toObject(WorkoutsResponse::class.java)?.data ?: emptyList()

                scope.launch {
                    workoutDao.deleteAll()
                    saveWorkoutResponses(workouts)
                }

                callback.onSuccess(workouts)
            }
    }

    override fun saveWorkouts(
        userId: String,
        workouts: List<WorkoutResponse>,
        callback: FirebaseCallback<List<WorkoutResponse>>
    ) {
        db.collection(USER_WORKOUT_COLLECTION_NAME)
            .document(userId)
            .set(WorkoutsResponse(workouts))
            .handleCompletionResult(
                context = globalProvider.context,
                callback = callback
            ) {
                scope.launch {
                    if(workouts.isEmpty()) {
                        workoutDao.deleteAll()
                    } else {
                        workoutDao.deleteAllWorkoutsExcept(workouts.map { it.id })
                    }

                    saveWorkoutResponses(workouts)
                }

                callback.onSuccess(workouts)
            }
    }

    override suspend fun deleteWorkout(
        userId: String,
        workoutId: String,
        callback: FirebaseCallback<List<WorkoutResponse>>
    ) {
        val workouts = workoutDao.findWorkouts().toMutableList().apply {
            val workout = find { it.workout.id == workoutId } ?: return@apply
            remove(workout)
        }.map { it.toWorkoutResponse() }

        saveWorkouts(userId, workouts, callback)
    }

    override suspend fun saveWorkoutResponses(responses: List<WorkoutResponse>) {
        val workouts = responses.map { it.toWorkout() }
        val workoutExercises = responses.flatMap { it.exercises.map { exercise -> exercise.toWorkoutExercise(it.id) } }
        val exercises = responses.flatMap { it.exercises.map { exercise -> exercise.exercise } }
        val sets = responses.flatMap { it.exercises.flatMap { exercise -> exercise.sets } }

        exerciseDao.saveAll(exercises)
        workoutDao.saveAll(workouts)
        workoutExerciseDao.saveAll(workoutExercises)
        workoutSetDao.saveAll(sets)
    }

    companion object {
        private const val USER_WORKOUT_COLLECTION_NAME = "workouts"
    }
}