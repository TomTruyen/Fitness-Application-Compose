package com.tomtruyen.feature.workouts

import com.tomtruyen.data.models.ui.WorkoutUiModel

sealed class WorkoutsUiAction {
    sealed class Sheet: WorkoutsUiAction() {
        data class Show(val id: String): Sheet()

        data object Dismiss: Sheet()
    }

    data object OnCreateClicked : WorkoutsUiAction()

    data class OnDetailClicked(val id: String) : WorkoutsUiAction()

    data class Execute(val id: String) : WorkoutsUiAction()

    data object ExecuteEmpty: WorkoutsUiAction()

    data object Edit: WorkoutsUiAction()

    data object Delete: WorkoutsUiAction()

    data object Refresh: WorkoutsUiAction()
}
