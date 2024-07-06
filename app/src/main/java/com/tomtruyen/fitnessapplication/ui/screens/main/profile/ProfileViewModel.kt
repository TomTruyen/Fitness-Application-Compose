package com.tomtruyen.fitnessapplication.ui.screens.main.profile

import android.content.Context
import com.tomtruyen.fitnessapplication.R
import com.tomtruyen.core.common.base.BaseViewModel
import com.tomtruyen.core.common.base.SnackbarMessage
import com.tomtruyen.data.entities.Settings
import com.tomtruyen.data.firebase.models.FirebaseCallback
import com.tomtruyen.data.repositories.interfaces.SettingsRepository
import com.tomtruyen.data.repositories.interfaces.UserRepository
import com.tomtruyen.fitnessapplication.di.appModule
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val settingsRepository: SettingsRepository
): BaseViewModel<ProfileUiState, ProfileUiAction, ProfileUiEvent>(
    initialState = ProfileUiState()
) {
    init {
        fetchSettings()

        observeSettings()
        observeLoading()
    }

    private fun fetchSettings() {
        val userId = userRepository.getUser()?.uid ?: return
        isLoading(true)

        settingsRepository.getSettings(
            userId = userId,
            callback = object: FirebaseCallback<Settings> {
                override fun onError(error: String?) {
                    showSnackbar(SnackbarMessage.Error(error))
                }

                override fun onStopLoading() {
                    isLoading(false)
                }
            }
        )
    }

    private fun observeSettings() = vmScope.launch {
        settingsRepository.findSettings()
            .distinctUntilChanged()
            .filterNotNull()
            .collectLatest { settings ->
                updateState {
                    it.copy(
                        settings = settings,
                        initialSettings = it.initialSettings ?: settings
                    )
                }
            }
    }

    private fun observeLoading() = vmScope.launch {
        loading.collectLatest { loading ->
            updateState { it.copy(loading = loading) }
        }
    }

    fun saveSettings(context: Context) {
        if(uiState.value.settings == uiState.value.initialSettings) return

        val userId = userRepository.getUser()?.uid ?: return
        val settings = uiState.value.settings

        isLoading(true)

        settingsRepository.saveSettings(
            userId = userId,
            settings = settings,
            callback = object: FirebaseCallback<Settings> {
                override fun onSuccess(value: Settings) {
                    showSnackbar(
                        SnackbarMessage.Success(context.getString(R.string.settings_saved))
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

        unloadKoinModules(appModule)
        loadKoinModules(appModule)

        triggerEvent(ProfileUiEvent.Logout)
    }

    override fun onAction(action: ProfileUiAction) {
        when (action) {
            is ProfileUiAction.UnitChanged -> updateState {
                it.copy(settings = it.settings.copy(unit = action.value))
            }
            is ProfileUiAction.RestChanged -> updateState {
                it.copy(settings = it.settings.copy(rest = action.value))
            }
            is ProfileUiAction.RestEnabledChanged -> updateState {
                it.copy(settings = it.settings.copy(restEnabled = action.value))
            }
            is ProfileUiAction.RestVibrationEnabledChanged -> updateState {
                it.copy(settings = it.settings.copy(restVibrationEnabled = action.value))
            }
            is ProfileUiAction.Logout -> logout()
        }
    }

}
