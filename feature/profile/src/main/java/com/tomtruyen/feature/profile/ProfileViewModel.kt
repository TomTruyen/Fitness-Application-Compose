package com.tomtruyen.feature.profile

import com.tomtruyen.core.common.base.BaseViewModel
import com.tomtruyen.data.repositories.interfaces.SettingsRepository
import com.tomtruyen.data.repositories.interfaces.UserRepository
import com.tomtruyen.models.providers.KoinReloadProvider
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val settingsRepository: SettingsRepository,
    private val koinReloadProvider: KoinReloadProvider,
): BaseViewModel<ProfileUiState, ProfileUiAction, ProfileUiEvent>(
    initialState = ProfileUiState()
) {
    init {
        fetchSettings()

        observeSettings()
        observeLoading()
    }

    private fun fetchSettings(refresh: Boolean = false) = vmScope.launch {
        val userId = userRepository.getUser()?.id ?: return@launch

        updateState {
            it.copy(
                refreshing = refresh,
                loading = !refresh
            )
        }

        settingsRepository.getSettings(
            userId = userId,
            refresh = refresh,
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

    fun saveSettings() = launchLoading {
        if(uiState.value.settings == uiState.value.initialSettings) return@launchLoading
        val userId = userRepository.getUser()?.id ?: return@launchLoading

        val settings = uiState.value.settings

        settingsRepository.saveSettings(
            userId = userId,
            settings = settings,
        )
    }

    private fun logout() = launch {
        userRepository.logout()

        koinReloadProvider.reload()

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

            is ProfileUiAction.OnRefresh -> fetchSettings(refresh = true)

            is ProfileUiAction.Logout -> logout()
        }
    }

}
