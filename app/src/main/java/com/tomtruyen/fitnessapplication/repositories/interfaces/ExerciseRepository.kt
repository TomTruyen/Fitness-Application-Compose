package com.tomtruyen.fitnessapplication.repositories.interfaces

import com.tomtruyen.fitnessapplication.base.BaseRepository
import com.tomtruyen.fitnessapplication.data.entities.Exercise
import com.tomtruyen.fitnessapplication.model.ExerciseFilter
import com.tomtruyen.fitnessapplication.model.FirebaseCallback
import kotlinx.coroutines.flow.Flow

abstract class ExerciseRepository: BaseRepository() {
    abstract fun findExercises(query: String, filter: ExerciseFilter): Flow<List<Exercise>>
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