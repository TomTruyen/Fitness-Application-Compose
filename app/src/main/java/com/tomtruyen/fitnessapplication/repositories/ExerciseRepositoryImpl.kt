package com.tomtruyen.fitnessapplication.repositories

import com.tomtruyen.fitnessapplication.data.dao.ExerciseDao
import com.tomtruyen.fitnessapplication.data.entities.Exercise
import com.tomtruyen.fitnessapplication.repositories.interfaces.ExerciseRepository
import kotlinx.coroutines.flow.Flow

class ExerciseRepositoryImpl(
    private val exerciseDao: ExerciseDao
): ExerciseRepository {
    override fun findExercises() = exerciseDao.findAllAsync()

    override suspend fun getExercises(): List<Exercise> {
        // TODO: Make Firestore call
    }
}