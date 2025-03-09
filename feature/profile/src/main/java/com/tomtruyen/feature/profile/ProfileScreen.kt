package com.tomtruyen.feature.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.tomtruyen.core.common.models.UnitType
import com.tomtruyen.core.common.providers.BuildConfigFieldProvider
import com.tomtruyen.core.common.utils.EmailUtils
import com.tomtruyen.core.common.utils.TimeUtils
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.designsystem.theme.secondaryLabelColor
import com.tomtruyen.core.ui.BottomSheetList
import com.tomtruyen.core.ui.Buttons
import com.tomtruyen.core.ui.Label
import com.tomtruyen.core.ui.LoadingContainer
import com.tomtruyen.core.ui.dialogs.RestAlertDialog
import com.tomtruyen.core.ui.listitems.ListItem
import com.tomtruyen.core.ui.listitems.SwitchListItem
import com.tomtruyen.core.ui.toolbars.Toolbar
import com.tomtruyen.core.designsystem.theme.datastore.ThemePreferencesDatastore
import com.tomtruyen.feature.profile.remember.rememberThemeModeActions
import com.tomtruyen.feature.profile.remember.rememberUnitActions
import com.tomtruyen.navigation.Screen
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val context = LocalContext.current

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    DisposableEffect(Unit) {
        onDispose {
            viewModel.saveSettings()
        }
    }

    LaunchedEffect(viewModel, context) {
        viewModel.eventFlow.collectLatest { navigationType ->
            when (navigationType) {
                is ProfileUiEvent.Logout -> {
                    navController.navigate(Screen.Auth.Login)
                }
            }
        }
    }

    ProfileScreenLayout(
        snackbarHost = { viewModel.CreateSnackbarHost() },
        navController = navController,
        state = state,
        onAction = viewModel::onAction
    )

    BottomSheetList(
        items = rememberThemeModeActions(
            onAction = viewModel::onAction
        ),
        visible = state.showThemeModeSheet,
        onDismiss = {
            viewModel.onAction(ProfileUiAction.Sheet.ThemeMode.Dismiss)
        }
    )

    BottomSheetList(
        items = rememberUnitActions(
            onAction = viewModel::onAction,
        ),
        selectedIndex = UnitType.entries.indexOf(state.settings.unit),
        visible = state.showWeightUnitSheet,
        onDismiss = {
            viewModel.onAction(ProfileUiAction.Sheet.WeightUnit.Dismiss)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileScreenLayout(
    snackbarHost: @Composable () -> Unit,
    navController: NavController,
    state: ProfileUiState,
    onAction: (ProfileUiAction) -> Unit
) {
    val buildConfigFieldProvider = koinInject<BuildConfigFieldProvider>()

    val context = LocalContext.current

    val refreshState = rememberPullToRefreshState()

    val themeMode by ThemePreferencesDatastore.themeMode.collectAsState(ThemePreferencesDatastore.Mode.SYSTEM)

    var restDialogVisible by remember { mutableStateOf(false) }

    val emailLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {}

    Scaffold(
        snackbarHost = snackbarHost,
        topBar = {
            Toolbar(
                title = stringResource(id = R.string.title_profile),
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
                    onAction(ProfileUiAction.Refresh)
                },
                state = refreshState,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                ) {
                    Label(
                       label = stringResource(id = R.string.label_units),
                        modifier = Modifier.padding(
                            start = 12.dp,
                            end = 12.dp,
                            bottom = Dimens.Tiny
                        )
                    )

                    ListItem(
                        title = stringResource(id = R.string.label_weight_unit),
                        message = state.settings.unit.value,
                        onClick = {
                            onAction(ProfileUiAction.Sheet.WeightUnit.Show)
                        }
                    )

                    HorizontalDivider()

                    Label(
                        label = stringResource(id = R.string.label_rest_timer),
                        modifier = Modifier.padding(
                            start = 12.dp,
                            end = 12.dp,
                            top = Dimens.Normal,
                            bottom = Dimens.Tiny
                        )
                    )

                    ListItem(
                        title = stringResource(id = R.string.label_default_rest_timer),
                        message = TimeUtils.formatSeconds(state.settings.rest.toLong()),
                        onClick = {
                            restDialogVisible = true
                        }
                    )

                    SwitchListItem(
                        title = stringResource(id = R.string.label_rest_timer_enabled),
                        checked = state.settings.restEnabled
                    ) {
                        onAction(ProfileUiAction.OnRestEnabledChanged(it))
                    }

                    SwitchListItem(
                        title = stringResource(id = R.string.label_vibrate_upon_finish),
                        checked = state.settings.restVibrationEnabled
                    ) {
                        onAction(ProfileUiAction.OnRestVibrationEnabledChanged(it))
                    }

                    HorizontalDivider()

                    Label(
                        label = stringResource(id = R.string.label_appearance),
                        modifier = Modifier.padding(
                            start = 12.dp,
                            end = 12.dp,
                            top = Dimens.Normal,
                            bottom = Dimens.Tiny
                        )
                    )

                    ListItem(
                        title = stringResource(id = R.string.label_theme_mode),
                        message = themeMode.value,
                        onClick = {
                            onAction(ProfileUiAction.Sheet.ThemeMode.Show)
                        }
                    )

                    HorizontalDivider()

                    Label(
                        label = stringResource(id = R.string.label_contact_and_support),
                        modifier = Modifier.padding(
                            start = 12.dp,
                            end = 12.dp,
                            top = Dimens.Normal,
                            bottom = Dimens.Tiny
                        )
                    )

                    ListItem(
                        title = stringResource(id = R.string.label_report_an_issue),
                        message = stringResource(id = R.string.label_message_report_an_issue),
                        onClick = {
                            emailLauncher.launch(
                                EmailUtils.getEmailIntent(context)
                            )
                        }
                    )

                    HorizontalDivider()

                    Spacer(modifier = Modifier.weight(1f))

                    Buttons.Default(
                        text = stringResource(id = R.string.button_logout),
                        onClick = { onAction(ProfileUiAction.Logout) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Dimens.Normal)
                    )

                    HorizontalDivider()

                    Text(
                        text = stringResource(
                            id = R.string.label_version_and_build,
                            buildConfigFieldProvider.versionName,
                            buildConfigFieldProvider.versionCode
                        ),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.secondaryLabelColor
                        ),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = Dimens.Small)
                            .padding(horizontal = Dimens.Normal)
                    )
                }
            }

            if (restDialogVisible) {
                RestAlertDialog(
                    onDismiss = {
                        restDialogVisible = false
                    },
                    onConfirm = { rest, _ ->
                        onAction(ProfileUiAction.OnRestChanged(rest))
                        restDialogVisible = false
                    },
                    rest = state.settings.rest
                )
            }
        }
    }
}