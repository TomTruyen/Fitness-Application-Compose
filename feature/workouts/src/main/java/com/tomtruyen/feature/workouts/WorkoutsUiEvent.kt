package com.tomtruyen.feature.workouts

sealed class WorkoutsUiEvent {
    sealed class Navigate : WorkoutsUiEvent() {
        data object Create : Navigate()

        data class Edit(val id: String) : Navigate()

        data class Execute(val id: String?) : Navigate()

        data class Detail(val id: String) : Navigate()
    }
}
