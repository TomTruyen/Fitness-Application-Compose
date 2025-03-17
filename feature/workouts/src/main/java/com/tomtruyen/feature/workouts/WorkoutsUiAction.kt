package com.tomtruyen.feature.workouts

sealed class WorkoutsUiAction {
    sealed class Sheet : WorkoutsUiAction() {
        data class Show(val id: String) : Sheet()

        data object Dismiss : Sheet()
    }

    sealed class Dialog : WorkoutsUiAction() {
        sealed class Discard : Dialog() {
            data object Show : Discard()

            data object Dismiss : Discard()
        }

        sealed class Workout : Dialog() {
            data object Show : Workout()

            data object Dismiss : Workout()
        }
    }

    sealed class ActiveWorkout : WorkoutsUiAction() {
        data object Resume : ActiveWorkout()

        data object Discard : ActiveWorkout()
    }

    data object OnCreateClicked : WorkoutsUiAction()

    data class OnDetailClicked(val id: String) : WorkoutsUiAction()

    data class Execute(val id: String) : WorkoutsUiAction()

    data object ExecuteEmpty : WorkoutsUiAction()

    data object Edit : WorkoutsUiAction()

    data object Delete : WorkoutsUiAction()

    data object Refresh : WorkoutsUiAction()

    data class Reorder(val from: Int, val to: Int) : WorkoutsUiAction()
}
