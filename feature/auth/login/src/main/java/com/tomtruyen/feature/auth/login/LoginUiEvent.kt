package com.tomtruyen.feature.auth.login

sealed class LoginUiEvent {
    sealed class Navigate : LoginUiEvent() {
        data object Home : Navigate()
        data object Register : Navigate()
    }
}
