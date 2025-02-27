package com.tomtruyen.data.repositories

import android.util.Log
import com.tomtruyen.data.dao.ExerciseDao
import com.tomtruyen.data.entities.Exercise
import com.tomtruyen.data.firebase.models.FirebaseCallback
import com.tomtruyen.data.repositories.interfaces.ExerciseRepository
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExerciseRepositoryImpl(
    private val exerciseDao: ExerciseDao
): ExerciseRepository() {
    override fun findExercises(query: String, filter: com.tomtruyen.models.ExerciseFilter) = exerciseDao.findAllAsync(
        query = query,
        filter = filter,
    )

    override fun findExerciseById(id: String) = exerciseDao.findByIdAsync(id)

    override fun findCategories() = exerciseDao.findCategories()

    override fun findEquipment() = exerciseDao.findEquipment()

    override suspend fun getExercises(userId: String?, refresh: Boolean) = fetch(refresh = refresh) {
        val exercises = supabase.from(Exercise.TABLE_NAME)
            .select {
                filter {
                    Exercise::userId eq userId
                }

                filter {
                    Exercise::userId eq null
                }
            }.data

        Log.d("@@@", "Exercises: ${exercises}")

//        db.collection(COLLECTION_NAME)
//            .document(DOCUMENT_NAME)
//            .get()
//            .handleCompletionResult(
//                context = context,
//                callback = callback
//            ) {
//                val exercises = it.toObject(ExercisesResponse::class.java)?.data.orEmpty()
//
//                launchWithCacheTransactions {
//                    exerciseDao.deleteAllNonUserExercises()
//                    exerciseDao.saveAll(exercises)
//                }
//
//                callback.onSuccess(exercises)
//            }
    }

    override suspend fun getUserExercises(userId: String, refresh: Boolean, callback: FirebaseCallback<List<Exercise>>) = fetch(
        refresh = refresh,
        overrideIdentifier = USER_EXERCISE_COLLECTION_NAME
    ) {
//        db.collection(USER_EXERCISE_COLLECTION_NAME)
//            .document(userId)
//            .get()
//            .handleCompletionResult(
//                context = context,
//                callback = callback
//            ) {
//                val exercises = it.toObject(UserExercisesResponse::class.java)?.exercises.orEmpty()
//
//                launchWithCacheTransactions(USER_EXERCISE_COLLECTION_NAME) {
//                    exerciseDao.deleteAllUserExercises()
//                    exerciseDao.saveAll(exercises)
//                }
//
//                callback.onSuccess(exercises)
//            }
    }

    override suspend fun saveUserExercise(
        userId: String,
        exercise: Exercise,
        isUpdate: Boolean,
        callback: FirebaseCallback<Unit>
    ) = withContext(Dispatchers.IO) {
        val newExercise = exercise.copy(
            userId = userId,
            category = if(exercise.category == Exercise.DEFAULT_DROPDOWN_VALUE) null else exercise.category,
            equipment = if(exercise.equipment == Exercise.DEFAULT_DROPDOWN_VALUE) null else exercise.equipment
        )

//        val exercises = exerciseDao.findAllUserExercises().toMutableList().apply {
//            if(isUpdate) {
//                removeIf { it.id == exercise.id }
//            }
//
//            add(exercise)
//        }
//
//        db.collection(USER_EXERCISE_COLLECTION_NAME)
//            .document(userId)
//            .set(UserExercisesResponse(exercises))
//            .handleCompletionResult(
//                context = context,
//                callback = callback
//            ) {
//                launchWithTransaction {
//                    exerciseDao.save(exercise)
//                }
//
//                callback.onSuccess(Unit)
//            }
    }

    override suspend fun deleteUserExercise(
        userId: String,
        exerciseId: String,
        callback: FirebaseCallback<Unit>
    ) = withContext(Dispatchers.IO) {
//        val exercise = exerciseDao.findUserExerciseById(exerciseId) ?: return@withContext callback.onStopLoading()
//
//        // No need to check if document exists, because if it doesn't then this exercise shouldn't exist either
//        db.collection(USER_EXERCISE_COLLECTION_NAME)
//            .document(userId)
//            .update("exercises", FieldValue.arrayRemove(exercise))
//            .handleCompletionResult(
//                context = context,
//                callback = callback
//            ) {
//                launchWithTransaction {
//                    exerciseDao.deleteUserExerciseById(exerciseId)
//                }
//
//                callback.onSuccess(Unit)
//            }
    }

    companion object {
        private const val COLLECTION_NAME = "metadata"
        private const val DOCUMENT_NAME = "exercises"
        
        private const val USER_EXERCISE_COLLECTION_NAME = "user_exercises"
    }
}