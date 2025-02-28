package com.tomtruyen.feature.auth.register

import com.tomtruyen.core.common.base.BaseViewModel
import com.tomtruyen.data.repositories.interfaces.UserRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val userRepository: UserRepository
) : BaseViewModel<RegisterUiState, RegisterUiAction, RegisterUiEvent>(
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

    private fun register() = launchLoading {
        userRepository.register(
            email = uiState.value.email.orEmpty(),
            password = uiState.value.password.orEmpty()
        )

        triggerEvent(RegisterUiEvent.NavigateToHome)
    }

    override fun onAction(action: RegisterUiAction) {
        when (action) {
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