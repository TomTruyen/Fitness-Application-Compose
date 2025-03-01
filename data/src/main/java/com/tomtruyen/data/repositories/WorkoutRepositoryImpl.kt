package com.tomtruyen.data.repositories

import com.tomtruyen.data.models.ui.WorkoutUiModel
import com.tomtruyen.data.repositories.interfaces.WorkoutRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest

class WorkoutRepositoryImpl: WorkoutRepository() {
    private val dao = database.workoutDao()

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
    ) {
//        val workouts = workoutDao.findWorkouts().map {
//            it.toWorkoutResponse()
//        }.toMutableList().apply {
//            if(isUpdate) {
//                removeIf { it.id == workout.id }
//            }
//
//            add(workout)
//        }
//
//        db.collection(USER_WORKOUT_COLLECTION_NAME)
//            .document(userId)
//            .set(WorkoutsResponse(workouts))
//            .handleCompletionResult(
//                context = context,
//                callback = callback
//            ) {
//                launchWithTransaction {
//                    saveWorkoutResponses(workouts)
//                }
//
//                callback.onSuccess(Unit)
//            }
    }

    override suspend fun deleteWorkout(
        userId: String,
        workoutId: String,
    ) {
//        val workout = workoutDao.findById(workoutId)?.toWorkoutResponse() ?: return@withContext callback.onStopLoading()
//
//        db.collection(USER_WORKOUT_COLLECTION_NAME)
//            .document(userId)
//            .update("data", FieldValue.arrayRemove(workout))
//            .handleCompletionResult(
//                context = context,
//                callback = callback
//            ) {
//                launchWithTransaction {
//                    workoutDao.deleteById(workoutId)
//                }
//
//                callback.onSuccess(Unit)
//            }
    }

//    suspend fun saveWorkoutResponses(responses: List<WorkoutResponse>) {
//        transaction {
//            val workouts = responses.map { it.toWorkout() }
//            val workoutExercises = responses.flatMap { it.exercises.map { exercise -> exercise.toWorkoutExercise(it.id) } }
//            val exercises = responses.flatMap { it.exercises.map { exercise -> exercise.exercise } }
//            val sets = responses.flatMap { it.exercises.flatMap { exercise -> exercise.sets } }
//
//            exerciseDao.saveAll(exercises)
//            workoutDao.saveAll(workouts)
//            workoutExerciseDao.saveAll(workoutExercises)
//            workoutExerciseSetDao.saveAll(sets)
//        }
//    }
}