package com.tomtruyen.feature.workouts

sealed class WorkoutsUiAction {
    sealed class Sheet: WorkoutsUiAction() {
        data class Show(val id: String): Sheet()

        data object Dismiss: Sheet()
    }

    data object OnCreateClicked : WorkoutsUiAction()

    data class OnDetailClicked(val id: String) : WorkoutsUiAction()

    data class Execute(val id: String) : WorkoutsUiAction()

    data object Edit: WorkoutsUiAction()

    data object Delete: WorkoutsUiAction()

    data object Refresh: WorkoutsUiAction()
}
