package com.tomtruyen.feature.profile

import com.tomtruyen.core.common.base.BaseViewModel
import com.tomtruyen.core.common.providers.KoinReloadProvider
import com.tomtruyen.core.designsystem.theme.datastore.ThemePreferencesDatastore
import com.tomtruyen.data.repositories.interfaces.SettingsRepository
import com.tomtruyen.data.repositories.interfaces.UserRepository
import com.tomtruyen.feature.profile.manager.SheetStateManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val settingsRepository: SettingsRepository,
    private val koinReloadProvider: KoinReloadProvider,
) : BaseViewModel<ProfileUiState, ProfileUiAction, ProfileUiEvent>(
    initialState = ProfileUiState()
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

        triggerEvent(ProfileUiEvent.Logout)
    }

    private fun updateThemeMode(mode: ThemePreferencesDatastore.Mode) = vmScope.launch {
        ThemePreferencesDatastore.setTheme(mode)
    }

    override fun onAction(action: ProfileUiAction) {
        when (action) {
            is ProfileUiAction.OnUnitChanged -> updateState {
                it.copy(settings = it.settings.copy(unit = action.value))
            }

            is ProfileUiAction.OnRestChanged -> updateState {
                it.copy(settings = it.settings.copy(rest = action.value))
            }

            is ProfileUiAction.OnRestEnabledChanged -> updateState {
                it.copy(settings = it.settings.copy(restEnabled = action.value))
            }

            is ProfileUiAction.OnRestVibrationEnabledChanged -> updateState {
                it.copy(settings = it.settings.copy(restVibrationEnabled = action.value))
            }

            is ProfileUiAction.Refresh -> fetchSettings(refresh = true)

            is ProfileUiAction.Logout -> logout()

            is ProfileUiAction.OnThemeModeChanged -> updateThemeMode(action.mode)

            is ProfileUiAction.Sheet -> sheetStateManager.onAction(action)
        }
    }

}
