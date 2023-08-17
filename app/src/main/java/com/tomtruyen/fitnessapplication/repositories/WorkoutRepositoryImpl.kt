package com.tomtruyen.fitnessapplication.repositories

import com.tomtruyen.fitnessapplication.data.dao.WorkoutDao
import com.tomtruyen.fitnessapplication.data.dao.WorkoutExerciseDao
import com.tomtruyen.fitnessapplication.data.dao.WorkoutSetDao
import com.tomtruyen.fitnessapplication.repositories.interfaces.WorkoutRepository

class WorkoutRepositoryImpl(
    private val workoutDao: WorkoutDao,
    private val workoutExerciseDao: WorkoutExerciseDao,
    private val workoutSetDao: WorkoutSetDao
): WorkoutRepository() {
}