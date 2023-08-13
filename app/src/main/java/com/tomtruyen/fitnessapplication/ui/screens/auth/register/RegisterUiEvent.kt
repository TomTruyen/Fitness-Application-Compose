package com.tomtruyen.fitnessapplication.ui.screens.auth.register


sealed class RegisterUiEvent {
    data class EmailChanged(val email: String): RegisterUiEvent()
    data class PasswordChanged(val password: String): RegisterUiEvent()
    data class ConfirmPasswordChanged(val confirmPassword: String): RegisterUiEvent()
    data object OnLoginClicked: RegisterUiEvent()
    data object OnRegisterClicked: RegisterUiEvent()
}