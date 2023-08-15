package com.tomtruyen.fitnessapplication.ui.screens.auth.register

import com.google.firebase.auth.FirebaseUser
import com.tomtruyen.fitnessapplication.base.BaseViewModel
import com.tomtruyen.fitnessapplication.base.SnackbarMessage
import com.tomtruyen.fitnessapplication.model.FirebaseCallback
import com.tomtruyen.fitnessapplication.repositories.interfaces.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow

class RegisterViewModel(
    private val userRepository: UserRepository
): BaseViewModel<RegisterNavigationType>() {
    val state = MutableStateFlow(RegisterUiState())

    fun register() {
        isLoading(true)
        userRepository.register(
            email = state.value.email ?: "",
            password = state.value.password ?: "",
            callback = object: FirebaseCallback<FirebaseUser?> {
                override fun onSuccess(value: FirebaseUser?) {
                    navigate(RegisterNavigationType.Home)
                }

                override fun onError(error: String?) {
                    showSnackbar(SnackbarMessage.Error(error))
                }

                override fun onStopLoading() {
                    isLoading(false)
                }
            }
        )
    }

    fun onEvent(event: RegisterUiEvent) {
        when(event) {
            is RegisterUiEvent.EmailChanged -> {
                state.value = state.value.copy(email = event.email)
            }
            is RegisterUiEvent.PasswordChanged -> {
                state.value = state.value.copy(password = event.password)
            }
            is RegisterUiEvent.ConfirmPasswordChanged -> {
                state.value = state.value.copy(confirmPassword = event.confirmPassword)
            }
            is RegisterUiEvent.OnRegisterClicked -> {
                register()
            }
            is RegisterUiEvent.OnLoginClicked -> {
                navigate(RegisterNavigationType.Login)
            }
        }
    }
}