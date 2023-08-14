package com.tomtruyen.fitnessapplication.repositories.interfaces

import com.tomtruyen.fitnessapplication.FetchedData
import com.tomtruyen.fitnessapplication.base.BaseRepository
import com.tomtruyen.fitnessapplication.data.entities.Exercise
import com.tomtruyen.fitnessapplication.model.FirebaseCallback
import kotlinx.coroutines.flow.Flow

abstract class ExerciseRepository: BaseRepository(FetchedData.Type.EXERCISES) {
    abstract fun findExercises(query: String): Flow<List<Exercise>>
    abstract fun getExercises(callback: FirebaseCallback<List<Exercise>>)
}