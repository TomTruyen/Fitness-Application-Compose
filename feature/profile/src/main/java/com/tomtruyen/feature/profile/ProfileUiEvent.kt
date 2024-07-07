package com.tomtruyen.feature.profile

sealed class ProfileUiEvent {
    data object Logout : ProfileUiEvent()
}
