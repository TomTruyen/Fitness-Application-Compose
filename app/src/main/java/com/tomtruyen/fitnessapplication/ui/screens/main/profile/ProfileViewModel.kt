package com.tomtruyen.fitnessapplication.ui.screens.main.profile

import com.tomtruyen.fitnessapplication.R
import com.tomtruyen.fitnessapplication.base.BaseViewModel
import com.tomtruyen.fitnessapplication.base.SnackbarMessage
import com.tomtruyen.fitnessapplication.data.entities.Settings
import com.tomtruyen.fitnessapplication.helpers.GlobalProvider
import com.tomtruyen.fitnessapplication.model.FirebaseCallback
import com.tomtruyen.fitnessapplication.repositories.interfaces.SettingsRepository
import com.tomtruyen.fitnessapplication.repositories.interfaces.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow

class ProfileViewModel(
    private val globalProvider: GlobalProvider,
    private val userRepository: UserRepository,
    private val settingsRepository: SettingsRepository
): BaseViewModel<ProfileNavigationType>() {
    val state = MutableStateFlow(ProfileUiState())

    val settings = settingsRepository.findSettings()

    init {
        getSettings()
    }

    fun setSettings(settings: Settings) {
        state.value = state.value.copy(
            settings = settings,
            initialSettings = if(state.value.initialSettings == null) settings else state.value.initialSettings
        )
    }

    private fun getSettings() {
        val userId = userRepository.getUser()?.uid ?: return
        isLoading(true)

        settingsRepository.getSettings(
            userId = userId,
            callback = object: FirebaseCallback<Settings> {
                override fun onSuccess(value: Settings) {}

                override fun onError(error: String?) {
                    showSnackbar(SnackbarMessage.Error(error))
                }

                override fun onStopLoading() {
                    isLoading(false)
                }
            }
        )
    }

    fun saveSettings() {
        if(state.value.settings == state.value.initialSettings) return

        val userId = userRepository.getUser()?.uid ?: return
        val settings = state.value.settings

        isLoading(true)

        settingsRepository.saveSettings(
            userId = userId,
            settings = settings,
            callback = object: FirebaseCallback<Settings> {
                override fun onSuccess(value: Settings) {
                    showSnackbar(
                        SnackbarMessage.Success(globalProvider.context.getString(R.string.settings_saved))
                    )
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

    private fun logout() {
        userRepository.logout()
        navigate(ProfileNavigationType.Logout)
    }

    fun onEvent(event: ProfileUiEvent) {
        when(event) {
            is ProfileUiEvent.UnitChanged -> {
                state.value = state.value.copy(
                    settings = state.value.settings.copy(
                        unit = event.value
                    )
                )
            }
            is ProfileUiEvent.RestChanged -> {
                state.value = state.value.copy(
                    settings = state.value.settings.copy(
                        rest = event.value
                    )
                )
            }
            is ProfileUiEvent.RestEnabledChanged -> {
                state.value = state.value.copy(
                    settings = state.value.settings.copy(
                        restEnabled = event.value
                    )
                )
            }
            is ProfileUiEvent.RestVibrationEnabledChanged -> {
                state.value = state.value.copy(
                    settings = state.value.settings.copy(
                        restVibrationEnabled = event.value
                    )
                )
            }
            is ProfileUiEvent.Logout -> logout()
        }
    }
}
