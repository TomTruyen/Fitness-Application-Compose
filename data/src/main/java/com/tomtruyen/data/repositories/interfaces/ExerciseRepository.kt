package com.tomtruyen.data.repositories.interfaces

import com.tomtruyen.data.repositories.BaseRepository
import com.tomtruyen.data.entities.Exercise
import com.tomtruyen.data.firebase.models.FirebaseCallback
import com.tomtruyen.models.DataFetchTracker
import kotlinx.coroutines.flow.Flow

abstract class ExerciseRepository: BaseRepository(DataFetchTracker.EXERCISES) {
    abstract fun findExercises(query: String, filter: com.tomtruyen.models.ExerciseFilter): Flow<List<Exercise>>
    abstract suspend fun findUserExercises(): List<Exercise>
    abstract fun findExerciseById(id: String): Flow<Exercise?>
    abstract suspend fun findUserExerciseById(id: String): Exercise?
    abstract fun findEquipment(): Flow<List<String>>
    abstract fun findCategories(): Flow<List<String>>
    abstract fun getExercises(callback: FirebaseCallback<List<Exercise>>)
    abstract fun getUserExercises(userId: String, callback: FirebaseCallback<List<Exercise>>)
    abstract suspend fun saveUserExercise(userId: String, exercise: Exercise, isUpdate: Boolean, callback: FirebaseCallback<Unit>)
    abstract suspend fun deleteUserExercise(userId: String, exerciseId: String, callback: FirebaseCallback<Unit>)
}