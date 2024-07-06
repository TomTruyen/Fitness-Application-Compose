package com.tomtruyen.data.repositories

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.tomtruyen.data.dao.ExerciseDao
import com.tomtruyen.data.entities.Exercise
import com.tomtruyen.data.firebase.extensions.handleCompletionResult
import com.tomtruyen.data.firebase.models.FirebaseCallback
import com.tomtruyen.data.firebase.models.ExercisesResponse
import com.tomtruyen.data.firebase.models.UserExercisesResponse
import com.tomtruyen.data.repositories.interfaces.ExerciseRepository
import com.tomtruyen.models.DataFetchTracker

class ExerciseRepositoryImpl(
    private val exerciseDao: ExerciseDao
): ExerciseRepository() {
    override fun findExercises(query: String, filter: com.tomtruyen.models.ExerciseFilter) = exerciseDao.findAllAsync(
        query = query,
        filter = filter,
    )

    override suspend fun findUserExercises() = exerciseDao.findAllUserExercises()

    override fun findExerciseById(id: String) = exerciseDao.findByIdAsync(id)

    override suspend fun findUserExerciseById(id: String) = exerciseDao.findUserExerciseById(id)

    override fun findCategories() = exerciseDao.findCategories()

    override fun findEquipment() = exerciseDao.findEquipment()

    override fun getExercises(callback: FirebaseCallback<List<Exercise>>) = tryRequestWhenNotFetched(
        onStopLoading = callback::onStopLoading
    ) {
        db.collection(COLLECTION_NAME)
            .document(DOCUMENT_NAME)
            .get()
            .handleCompletionResult(
                context = context,
                setFetchSuccessful = ::setFetchSuccessful,
                callback = callback
            ) {
                val exercises = it.toObject(ExercisesResponse::class.java)?.data.orEmpty()

                launchWithTransaction {
                    exerciseDao.deleteAllNonUserExercises()
                    exerciseDao.saveAll(exercises)
                }

                callback.onSuccess(exercises)
            }
    }

    override fun getUserExercises(userId: String, callback: FirebaseCallback<List<Exercise>>) = tryRequestWhenNotFetched(
        onStopLoading = callback::onStopLoading,
        overrideIdentifier = DataFetchTracker.USER_EXERCISES
    ) {
        db.collection(USER_EXERCISE_COLLECTION_NAME)
            .document(userId)
            .get()
            .handleCompletionResult(
                context = context,
                setFetchSuccessful = {
                    setFetchSuccessful(DataFetchTracker.USER_EXERCISES)
                },
                callback = callback
            ) {
                val exercises = it.toObject(UserExercisesResponse::class.java)?.exercises.orEmpty()

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
                context = context,
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
                context = context,
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