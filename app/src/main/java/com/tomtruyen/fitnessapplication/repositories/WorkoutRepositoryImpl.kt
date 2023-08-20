package com.tomtruyen.fitnessapplication.repositories

import com.tomtruyen.fitnessapplication.data.dao.ExerciseDao
import com.tomtruyen.fitnessapplication.data.dao.WorkoutDao
import com.tomtruyen.fitnessapplication.data.dao.WorkoutExerciseDao
import com.tomtruyen.fitnessapplication.data.dao.WorkoutSetDao
import com.tomtruyen.fitnessapplication.data.entities.WorkoutWithExercises
import com.tomtruyen.fitnessapplication.extensions.handleCompletionResult
import com.tomtruyen.fitnessapplication.helpers.GlobalProvider
import com.tomtruyen.fitnessapplication.model.FirebaseCallback
import com.tomtruyen.fitnessapplication.networking.UserExercisesResponse
import com.tomtruyen.fitnessapplication.networking.WorkoutResponse
import com.tomtruyen.fitnessapplication.repositories.interfaces.WorkoutRepository
import kotlinx.coroutines.flow.Flow
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

    override fun findWorkoutById(id: String) = workoutDao.findById(id)

    override fun getWorkouts(callback: FirebaseCallback<List<WorkoutResponse>>) {
        TODO("Not yet implemented")
    }

    override fun saveWorkout(
        userId: String,
        workouts: List<WorkoutResponse>,
        callback: FirebaseCallback<List<WorkoutResponse>>
    ) {
        db.collection(USER_WORKOUT_COLLECTION_NAME)
            .document(userId)
            .set(workouts)
            .handleCompletionResult(
                context = globalProvider.context,
                callback = callback
            ) {
                scope.launch {
                    if(workouts.isNotEmpty()) {
                        workoutDao.deleteAllWorkoutsExcept(workouts.map { it.id })
                    }

                    saveWorkoutResponses(workouts)
                }

                callback.onSuccess(workouts)
            }
    }

    private fun saveWorkoutResponses(responses: List<WorkoutResponse>) = scope.launch {
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