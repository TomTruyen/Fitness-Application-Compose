package com.tomtruyen.feature.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.tomtruyen.core.common.ObserveEvent
import com.tomtruyen.core.common.ThemeMode
import com.tomtruyen.core.common.models.GlobalAppState
import com.tomtruyen.core.common.models.UnitType
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.ui.BottomSheetList
import com.tomtruyen.core.ui.Buttons
import com.tomtruyen.core.ui.LoadingContainer
import com.tomtruyen.core.ui.toolbars.Toolbar
import com.tomtruyen.core.ui.wheeltimepicker.WheelTimerPickerSheet
import com.tomtruyen.core.ui.wheeltimepicker.core.TimeComponent
import com.tomtruyen.feature.settings.components.AppearanceSection
import com.tomtruyen.feature.settings.components.BuildInfoSection
import com.tomtruyen.feature.settings.components.ContactSection
import com.tomtruyen.feature.settings.components.RestTimerSection
import com.tomtruyen.feature.settings.components.UnitSection
import com.tomtruyen.feature.settings.remember.rememberThemeModeActions
import com.tomtruyen.feature.settings.remember.rememberUnitActions
import com.tomtruyen.navigation.Screen
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    DisposableEffect(Unit) {
        onDispose {
            viewModel.saveSettings()
        }
    }

    ObserveEvent(viewModel) { event ->
        when (event) {
            is SettingsUiEvent.Logout -> {
                navController.navigate(Screen.Auth.Login)
            }
        }
    }

    SettingsScreenLayout(
        navController = navController,
        state = state,
        onAction = viewModel::onAction
    )

    BottomSheetList(
        items = rememberThemeModeActions(
            onAction = viewModel::onAction
        ),
        visible = state.showThemeModeSheet,
        selectedIndex = ThemeMode.entries.indexOf(GlobalAppState.theme.value),
        onDismiss = {
            viewModel.onAction(SettingsUiAction.Sheet.ThemeMode.Dismiss)
        }
    )

    BottomSheetList(
        items = rememberUnitActions(
            onAction = viewModel::onAction,
        ),
        selectedIndex = UnitType.entries.indexOf(state.settings.unit),
        visible = state.showWeightUnitSheet,
        onDismiss = {
            viewModel.onAction(SettingsUiAction.Sheet.WeightUnit.Dismiss)
        }
    )

    WheelTimerPickerSheet(
        seconds = state.settings.rest,
        visible = state.showRestTimeSheet,
        components = listOf(TimeComponent.MINUTE, TimeComponent.SECOND),
        onSubmit = {
            viewModel.onAction(SettingsUiAction.OnRestChanged(it))
        },
        onDismiss = {
            viewModel.onAction(SettingsUiAction.Sheet.RestTime.Dismiss)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreenLayout(
    navController: NavController,
    state: SettingsUiState,
    onAction: (SettingsUiAction) -> Unit
) {
    val refreshState = rememberPullToRefreshState()

    Scaffold(
        topBar = {
            Toolbar(
                title = stringResource(id = R.string.title_settings),
                navController = navController,
            )
        },
    ) {
        LoadingContainer(
            loading = state.loading,
            scaffoldPadding = it
        ) {
            PullToRefreshBox(
                isRefreshing = state.refreshing,
                onRefresh = {
                    onAction(SettingsUiAction.Refresh)
                },
                state = refreshState,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                ) {
                    UnitSection(
                        unit = state.settings.unit,
                        onShowUnitSheet = {
                            onAction(SettingsUiAction.Sheet.WeightUnit.Show)
                        }
                    )

                    HorizontalDivider()

                    RestTimerSection(
                        restTime = state.settings.rest,
                        restEnabled = state.settings.restEnabled,
                        restVibrateEnabled = state.settings.restVibrationEnabled,
                        onAction = onAction
                    )

                    HorizontalDivider()

                    AppearanceSection(
                        onShowThemeSheet = {
                            onAction(SettingsUiAction.Sheet.ThemeMode.Show)
                        }
                    )

                    HorizontalDivider()

                    ContactSection()

                    HorizontalDivider()

                    Spacer(modifier = Modifier.weight(1f))

                    Buttons.Default(
                        text = stringResource(id = R.string.button_logout),
                        onClick = { onAction(SettingsUiAction.Logout) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Dimens.Normal)
                    )

                    HorizontalDivider()

                    BuildInfoSection()
                }
            }
        }
    }
}