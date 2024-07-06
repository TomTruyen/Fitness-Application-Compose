package com.tomtruyen.fitnessapplication.ui.screens.auth.register

import com.google.firebase.auth.FirebaseUser
import com.tomtruyen.core.common.base.BaseViewModel
import com.tomtruyen.core.common.base.SnackbarMessage
import com.tomtruyen.data.firebase.models.FirebaseCallback
import com.tomtruyen.data.repositories.interfaces.UserRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val userRepository: UserRepository
): BaseViewModel<RegisterUiState, RegisterUiAction, RegisterUiEvent>(
    initialState = RegisterUiState()
) {
    init {
        observeLoading()
    }

    private fun observeLoading() = vmScope.launch {
        loading.collectLatest { loading ->
            updateState { it.copy(loading = loading) }
        }
    }

    fun register() {
        isLoading(true)
        userRepository.register(
            email = uiState.value.email.orEmpty(),
            password = uiState.value.password.orEmpty(),
            callback = object: FirebaseCallback<FirebaseUser?> {
                override fun onSuccess(value: FirebaseUser?) {
                    triggerEvent(RegisterUiEvent.NavigateToHome)
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

    override fun onAction(action: RegisterUiAction) {
        when(action) {
            is RegisterUiAction.EmailChanged -> updateState {
                it.copy(
                    email = action.email,
                    emailValidationResult = it.validateEmail(action.email)
                )
            }
            is RegisterUiAction.PasswordChanged -> updateState {
                it.copy(
                    password = action.password,
                    passwordValidationResult = it.validatePassword(action.password)
                )
            }
            is RegisterUiAction.ConfirmPasswordChanged -> updateState {
                it.copy(
                    confirmPassword = action.confirmPassword,
                    confirmPasswordValidationResult = it.validateConfirmPassword(action.confirmPassword)
                )
            }
            is RegisterUiAction.OnRegisterClicked -> register()
            is RegisterUiAction.OnLoginClicked -> triggerEvent(RegisterUiEvent.NavigateToLogin)
        }
    }
}