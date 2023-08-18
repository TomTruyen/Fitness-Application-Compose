package com.tomtruyen.fitnessapplication.ui.screens.main.workouts

import com.tomtruyen.fitnessapplication.base.BaseViewModel

class WorkoutOverviewViewModel: BaseViewModel<WorkoutOverviewNavigationType>() {
    fun onEvent(event: WorkoutOverviewUiEvent) {
        when(event) {
            is WorkoutOverviewUiEvent.OnCreateWorkoutClicked -> navigate(WorkoutOverviewNavigationType.CreateWorkout)
        }
    }
}
