package com.tomtruyen.fitnessapplication.repositories

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.tomtruyen.fitnessapplication.data.dao.ExerciseDao
import com.tomtruyen.fitnessapplication.data.entities.Exercise
import com.tomtruyen.fitnessapplication.extensions.handleCompletionResult
import com.tomtruyen.fitnessapplication.helpers.GlobalProvider
import com.tomtruyen.fitnessapplication.model.ExerciseFilter
import com.tomtruyen.fitnessapplication.model.FetchedData
import com.tomtruyen.fitnessapplication.model.FirebaseCallback
import com.tomtruyen.fitnessapplication.networking.models.ExercisesResponse
import com.tomtruyen.fitnessapplication.networking.models.UserExercisesResponse
import com.tomtruyen.fitnessapplication.repositories.interfaces.ExerciseRepository
import kotlinx.coroutines.launch

class ExerciseRepositoryImpl(
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
        identifier = FetchedData.Type.EXERCISES.identifier,
        onStopLoading = callback::onStopLoading
    ) {
        db.collection(COLLECTION_NAME)
            .document(DOCUMENT_NAME)
            .get()
            .handleCompletionResult(
                context = globalProvider.context,
                setFetchSuccessful = {
                    setFetchSuccessful(FetchedData.Type.EXERCISES.identifier)
                },
                callback = callback
            ) {
                val exercises = it.toObject(ExercisesResponse::class.java)?.data ?: emptyList()

                launchWithTransaction {
                    exerciseDao.deleteAllNonUserExercises()
                    exerciseDao.saveAll(exercises)
                }

                callback.onSuccess(exercises)
            }
    }

    override fun getUserExercises(userId: String, callback: FirebaseCallback<List<Exercise>>) = tryRequestWhenNotFetched(
        identifier = FetchedData.Type.USER_EXERCISES.identifier,
        onStopLoading = callback::onStopLoading
    ) {
        db.collection(USER_EXERCISE_COLLECTION_NAME)
            .document(userId)
            .get()
            .handleCompletionResult(
                context = globalProvider.context,
                setFetchSuccessful = {
                    setFetchSuccessful(FetchedData.Type.USER_EXERCISES.identifier)
                },
                callback = callback
            ) {
                val exercises = it.toObject(UserExercisesResponse::class.java)?.exercises ?: emptyList()

                launchWithTransaction {
                    exerciseDao.deleteAllUserExercises()
                    exerciseDao.saveAll(exercises)
                }

                callback.onSuccess(exercises)
            }
    }

    override suspend fun saveUserExercise(
        userId: String,
        exercise: Exercise,
        isUpdate: Boolean,
        callback: FirebaseCallback<Unit>
    ) {
        exercise.isUserCreated = true

        val exercises = exerciseDao.findAllUserExercises().toMutableList().apply {
            if(isUpdate) {
                removeIf { it.id == exercise.id }
            }

            add(exercise)
        }

        db.collection(USER_EXERCISE_COLLECTION_NAME)
            .document(userId)
            .set(UserExercisesResponse(exercises))
            .handleCompletionResult(
                context = globalProvider.context,
                callback = callback
            ) {
                launchWithTransaction {
                    exerciseDao.save(exercise)
                }

                callback.onSuccess(Unit)
            }
    }

    override suspend fun deleteUserExercise(
        userId: String,
        exerciseId: String,
        callback: FirebaseCallback<Unit>
    ) {
        val exercise = exerciseDao.findUserExerciseById(exerciseId) ?: return callback.onStopLoading()

        // No need to check if document exists, because if it doesn't then this exercise shouldn't exist either
        db.collection(USER_EXERCISE_COLLECTION_NAME)
            .document(userId)
            .update("exercises", FieldValue.arrayRemove(exercise))
            .handleCompletionResult(
                context = globalProvider.context,
                callback = callback
            ) {
                launchWithTransaction {
                    exerciseDao.deleteUserExerciseById(exerciseId)
                }

                callback.onSuccess(Unit)
            }
    }

    companion object {
        private const val COLLECTION_NAME = "metadata"
        private const val DOCUMENT_NAME = "exercises"
        
        private const val USER_EXERCISE_COLLECTION_NAME = "user_exercises"
    }
}