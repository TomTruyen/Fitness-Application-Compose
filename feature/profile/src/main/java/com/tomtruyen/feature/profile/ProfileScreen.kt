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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.ui.Buttons
import com.tomtruyen.core.ui.toolbars.CollapsingToolbar
import com.tomtruyen.core.ui.listitems.ListItem
import com.tomtruyen.core.ui.listitems.SwitchListItem
import com.tomtruyen.core.common.utils.EmailUtils
import com.tomtruyen.core.common.utils.TimeUtils
import com.tomtruyen.core.ui.LoadingContainer
import com.tomtruyen.core.ui.dialogs.RestAlertDialog
import com.tomtruyen.core.ui.dialogs.UnitAlertDialog
import com.tomtruyen.data.entities.Settings
import com.tomtruyen.models.providers.BuildConfigFieldProvider
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
            viewModel.saveSettings(context)
        }
    }

    LaunchedEffect(viewModel, context) {
        viewModel.eventFlow.collectLatest { navigationType ->
            when(navigationType) {
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenLayout(
    snackbarHost: @Composable () -> Unit,
    navController: NavController,
    state: ProfileUiState,
    onAction: (ProfileUiAction) -> Unit
) {
    val context = LocalContext.current

    val buildConfigFieldProvider = koinInject<BuildConfigFieldProvider>()

    var unitDialogVisible by remember { mutableStateOf(false) }
    var restDialogVisible by remember { mutableStateOf(false) }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    val emailLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {}

    Scaffold(
        snackbarHost = snackbarHost,
        topBar = {
            CollapsingToolbar(
                title = stringResource(id = R.string.title_profile),
                navController =  navController,
                scrollBehavior = scrollBehavior
            )
        },
        // nestedScroll modifier is required for the scroll behavior to work
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        LoadingContainer(
            loading = state.loading,
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = stringResource(id = R.string.label_units),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.W500
                    ),
                    modifier = Modifier
                        .padding(horizontal = Dimens.Normal)
                        .padding(top = Dimens.Normal, bottom = Dimens.Tiny)
                )

                ListItem(
                    title = stringResource(id = R.string.label_weight_unit),
                    message = state.settings.unit,
                ) {
                    unitDialogVisible = true
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f))

                Text(
                    text = stringResource(id = R.string.label_rest_timer),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.W500
                    ),
                    modifier = Modifier
                        .padding(horizontal = Dimens.Normal)
                        .padding(top = Dimens.Normal, bottom = Dimens.Tiny)
                )

                ListItem(
                    title = stringResource(id = R.string.label_default_rest_timer),
                    message = TimeUtils.formatSeconds(state.settings.rest.toLong()),
                ) {
                    restDialogVisible = true
                }

                SwitchListItem(
                    title = stringResource(id = R.string.label_rest_timer_enabled),
                    checked = state.settings.restEnabled
                ) {
                    onAction(ProfileUiAction.RestEnabledChanged(it))
                }

                SwitchListItem(
                    title = stringResource(id = R.string.label_vibrate_upon_finish),
                    checked = state.settings.restVibrationEnabled
                ) {
                    onAction(ProfileUiAction.RestVibrationEnabledChanged(it))
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f))

                Text(
                    text = stringResource(id = R.string.label_contact_and_support),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.W500
                    ),
                    modifier = Modifier
                        .padding(horizontal = Dimens.Normal)
                        .padding(top = Dimens.Normal, bottom = Dimens.Tiny)
                )

                ListItem(
                    title = stringResource(id = R.string.label_report_an_issue),
                    message = stringResource(id = R.string.label_message_report_an_issue),
                ) {
                    emailLauncher.launch(
                        EmailUtils.getEmailIntent(context)
                    )
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f))

                Spacer(modifier = Modifier.weight(1f))

                Buttons.Default(
                    text = stringResource(id = R.string.button_logout),
                    onClick = { onAction(ProfileUiAction.Logout) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimens.Normal)
                )

                HorizontalDivider(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f))

                Text(
                    text = stringResource(
                        id = R.string.label_version_and_build,
                        buildConfigFieldProvider.versionName,
                        buildConfigFieldProvider.versionCode
                    ),
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = Dimens.Normal, top = Dimens.Small)
                        .padding(horizontal = Dimens.Normal)
                )
            }

            if(unitDialogVisible) {
                UnitAlertDialog(
                    units = Settings.UnitType.entries.map { it.value },
                    onDismiss = {
                        unitDialogVisible = false
                    },
                    onConfirm = { unit ->
                        onAction(ProfileUiAction.UnitChanged(unit))
                        unitDialogVisible = false
                    },
                    unit = state.settings.unit
                )
            }

            if(restDialogVisible) {
                RestAlertDialog(
                    onDismiss = {
                        restDialogVisible = false
                    },
                    onConfirm = { rest, _ ->
                        onAction(ProfileUiAction.RestChanged(rest))
                        restDialogVisible = false
                    },
                    rest = state.settings.rest
                )
            }
        }
    }
}