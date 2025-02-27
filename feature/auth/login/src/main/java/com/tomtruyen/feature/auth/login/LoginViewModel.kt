package com.tomtruyen.feature.auth.login

import com.tomtruyen.core.common.base.BaseViewModel
import com.tomtruyen.core.common.base.SnackbarMessage
import com.tomtruyen.data.repositories.interfaces.UserRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userRepository: UserRepository
): BaseViewModel<LoginUiState, LoginUiAction, LoginUiEvent>(
    initialState = LoginUiState()
) {
    init {
        observeLoading()
    }

    private fun observeLoading() = vmScope.launch {
        loading.collectLatest { loading ->
            updateState { it.copy(loading = loading) }
        }
    }

    private fun login() = launchLoading {
        userRepository.login(
            email = uiState.value.email.orEmpty(),
            password = uiState.value.password.orEmpty(),
        )

        onAuth()
    }

    private fun loginWithGoogle(idToken: String) = launchLoading {
        userRepository.loginWithGoogle(idToken)

        onAuth()
    }

    private fun onAuth() {
        // TODO: Handle logic to call the SettingsRepository.getSettings post auth

        triggerEvent(LoginUiEvent.NavigateToHome)
    }

    override fun onAction(action: LoginUiAction) {
        when(action) {
            is LoginUiAction.EmailChanged -> updateState {
                it.copy(
                    email = action.email,
                    emailValidationResult = it.validateEmail(action.email)
                )
            }
            is LoginUiAction.PasswordChanged -> updateState {
                it.copy(
                    password = action.password,
                    passwordValidationResult = it.validatePassword(action.password)
                )
            }
            is LoginUiAction.OnGoogleSignInFailed -> showSnackbar(SnackbarMessage.Error(action.error))
            is LoginUiAction.OnGoogleSignInSuccess -> loginWithGoogle(action.idToken)
            LoginUiAction.OnLoginClicked -> login()
            LoginUiAction.OnRegisterClicked -> triggerEvent(LoginUiEvent.NavigateToRegister)
        }
    }
}
