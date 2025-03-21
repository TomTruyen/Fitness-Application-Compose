package com.tomtruyen.feature.settings

import com.tomtruyen.core.common.ThemeMode
import com.tomtruyen.core.common.ThemePreferences
import com.tomtruyen.core.common.base.BaseViewModel
import com.tomtruyen.core.common.providers.KoinReloadProvider
import com.tomtruyen.data.repositories.interfaces.SettingsRepository
import com.tomtruyen.data.repositories.interfaces.UserRepository
import com.tomtruyen.feature.settings.manager.SheetStateManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val userRepository: UserRepository,
    private val settingsRepository: SettingsRepository,
    private val koinReloadProvider: KoinReloadProvider,
    private val themePreferences: ThemePreferences,
) : BaseViewModel<SettingsUiState, SettingsUiAction, SettingsUiEvent>(
    initialState = SettingsUiState()
) {
    private val sheetStateManager by lazy {
        SheetStateManager(
            updateState = ::updateState
        )
    }

    init {
        fetchSettings()

        observeSettings()
        observeLoading()
        observeRefreshing()
    }

    private fun fetchSettings(refresh: Boolean = false) = launchLoading(refresh) {
        val userId = userRepository.getUser()?.id ?: return@launchLoading

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

    private fun observeRefreshing() = vmScope.launch {
        refreshing.collectLatest { refreshing ->
            updateState { it.copy(refreshing = refreshing) }
        }
    }

    fun saveSettings() = launchLoading {
        if (uiState.value.settings == uiState.value.initialSettings) return@launchLoading
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

        triggerEvent(SettingsUiEvent.Logout)
    }

    private fun updateThemeMode(mode: ThemeMode) = vmScope.launch {
        themePreferences.setTheme(mode)
    }

    override fun onAction(action: SettingsUiAction) {
        when (action) {
            is SettingsUiAction.OnUnitChanged -> updateState {
                it.copy(settings = it.settings.copy(unit = action.value))
            }

            is SettingsUiAction.OnRestChanged -> updateState {
                it.copy(settings = it.settings.copy(rest = action.value))
            }

            is SettingsUiAction.OnRestEnabledChanged -> updateState {
                it.copy(settings = it.settings.copy(restEnabled = action.value))
            }

            is SettingsUiAction.OnRestVibrationEnabledChanged -> updateState {
                it.copy(settings = it.settings.copy(restVibrationEnabled = action.value))
            }

            is SettingsUiAction.Refresh -> fetchSettings(refresh = true)

            is SettingsUiAction.Logout -> logout()

            is SettingsUiAction.OnThemeModeChanged -> updateThemeMode(action.mode)

            is SettingsUiAction.Sheet -> sheetStateManager.onAction(action)
        }
    }

}
