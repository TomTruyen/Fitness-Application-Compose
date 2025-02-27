package com.tomtruyen.data.repositories.interfaces

import com.tomtruyen.data.repositories.BaseRepository
import com.tomtruyen.data.entities.Exercise
import com.tomtruyen.data.firebase.models.FirebaseCallback
import kotlinx.coroutines.flow.Flow

abstract class ExerciseRepository: BaseRepository() {
    override val identifier: String
        get() = "exercises"

    abstract fun findExercises(query: String, filter: com.tomtruyen.models.ExerciseFilter): Flow<List<Exercise>>
    abstract fun findExerciseById(id: String): Flow<Exercise?>
    abstract fun findEquipment(): Flow<List<String>>
    abstract fun findCategories(): Flow<List<String>>
    abstract suspend fun getExercises(userId: String?, refresh: Boolean)
    abstract suspend fun getUserExercises(userId: String, refresh: Boolean, callback: FirebaseCallback<List<Exercise>>)
    abstract suspend fun saveUserExercise(userId: String, exercise: Exercise, isUpdate: Boolean, callback: FirebaseCallback<Unit>)
    abstract suspend fun deleteUserExercise(userId: String, exerciseId: String, callback: FirebaseCallback<Unit>)
}