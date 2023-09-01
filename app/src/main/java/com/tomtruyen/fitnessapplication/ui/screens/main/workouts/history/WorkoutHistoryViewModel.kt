package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.history

import androidx.paging.cachedIn
import com.tomtruyen.fitnessapplication.base.BaseViewModel
import com.tomtruyen.fitnessapplication.repositories.interfaces.UserRepository
import com.tomtruyen.fitnessapplication.repositories.interfaces.WorkoutHistoryRepository

class WorkoutHistoryViewModel(
    userRepository: UserRepository,
    workoutHistoryRepository: WorkoutHistoryRepository
): BaseViewModel<WorkoutHistoryNavigationType>() {
    val history = workoutHistoryRepository.getWorkoutHistoriesPaginated(
        userRepository.getUser()?.uid!!
    ).cachedIn(vmScope)
}
