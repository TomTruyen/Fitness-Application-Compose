package com.tomtruyen.fitnessapplication.repositories

import com.tomtruyen.fitnessapplication.data.dao.ExerciseDao
import com.tomtruyen.fitnessapplication.data.entities.Exercise
import com.tomtruyen.fitnessapplication.extensions.handleCompletionResult
import com.tomtruyen.fitnessapplication.helpers.GlobalProvider
import com.tomtruyen.fitnessapplication.model.ExerciseFilter
import com.tomtruyen.fitnessapplication.model.FetchedData
import com.tomtruyen.fitnessapplication.model.FirebaseCallback
import com.tomtruyen.fitnessapplication.networking.ExercisesResponse
import com.tomtruyen.fitnessapplication.networking.UserExercisesResponse
import com.tomtruyen.fitnessapplication.repositories.interfaces.ExerciseRepository
import kotlinx.coroutines.launch

class ExerciseRepositoryImpl(
    private val globalProvider: GlobalProvider,
    private val exerciseDao: ExerciseDao
): ExerciseRepository() {
    override fun findExercises(query: String, filter: ExerciseFilter) = exerciseDao.findAllAsync(
        query = query,
        filter = filter,
    )

    override suspend fun findUserExercises() = exerciseDao.findAllUserExercises()

    override fun findExerciseById(id: String) = exerciseDao.findByIdAsync(id)

    override suspend fun findUserExerciseById(id: String) = exerciseDao.findUserExerciseById(id)

    override fun findCategories() = exerciseDao.findCategories()

    override fun findEquipment() = exerciseDao.findEquipment()

    override fun getExercises(callback: FirebaseCallback<List<Exercise>>) = tryRequestWhenNotFetched(
        type = FetchedData.Type.EXERCISES,
        onStopLoading = {
            callback.onStopLoading()
        }
    ) {
        db.collection(COLLECTION_NAME)
            .document(DOCUMENT_NAME)
            .get()
            .handleCompletionResult(
                context = globalProvider.context,
                setFetchSuccessful = {
                    setFetchSuccessful(FetchedData.Type.EXERCISES)
                },
                callback = callback
            ) {
                val exercises = it.toObject(ExercisesResponse::class.java)?.data ?: emptyList()

                scope.launch {
                    exerciseDao.saveAll(exercises)
                }

                callback.onSuccess(exercises)
            }
    }

    override fun getUserExercises(userId: String, callback: FirebaseCallback<List<Exercise>>) = tryRequestWhenNotFetched(
        type = FetchedData.Type.USER_EXERCISES,
        onStopLoading = {
            callback.onStopLoading()
        }
    ) {
        db.collection(USER_EXERCISE_COLLECTION_NAME)
            .document(userId)
            .get()
            .handleCompletionResult(
                context = globalProvider.context,
                setFetchSuccessful = {
                    setFetchSuccessful(FetchedData.Type.USER_EXERCISES)
                },
                callback = callback
            ) {
                val exercises = it.toObject(UserExercisesResponse::class.java)?.exercises ?: emptyList()

                scope.launch {
                    exerciseDao.saveAll(exercises)
                }

                callback.onSuccess(exercises)
            }
    }

    override fun saveUserExercises(
        userId: String,
        exercises: List<Exercise>,
        callback: FirebaseCallback<List<Exercise>>
    ) {
        val userExercises = exercises.map {
            it.apply {
                isUserCreated = true
            }
        }

        db.collection(USER_EXERCISE_COLLECTION_NAME)
            .document(userId)
            .set(
                UserExercisesResponse(
                    exercises = userExercises
                )
            )
            .handleCompletionResult(
                context = globalProvider.context,
                callback = callback
            ) {
                scope.launch {
                    if(userExercises.isNotEmpty()) {
                        exerciseDao.deleteAllUserExercisesExcept(userExercises.map { it.id })
                    }

                    exerciseDao.saveAll(userExercises)
                }

                callback.onSuccess(userExercises)
            }
    }

    override suspend fun deleteUserExercise(
        userId: String,
        exerciseId: String,
        callback: FirebaseCallback<List<Exercise>>
    ) {
        val userExercises = exerciseDao.findAllUserExercises().toMutableList().apply {
            val exercise = find { it.id == exerciseId } ?: return@apply
            remove(exercise)
        }

        saveUserExercises(userId, userExercises, callback)
    }

    companion object {
        private const val COLLECTION_NAME = "metadata"
        private const val DOCUMENT_NAME = "exercises"
        
        private const val USER_EXERCISE_COLLECTION_NAME = "user_exercises"
    }
}