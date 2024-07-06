package com.tomtruyen.fitnessapplication.ui.screens.main.profile

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
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.tomtruyen.fitnessapplication.BuildConfig
import com.tomtruyen.fitnessapplication.Dimens
import com.tomtruyen.fitnessapplication.R
import com.tomtruyen.fitnessapplication.extensions.navigateAndClearBackStack
import com.tomtruyen.fitnessapplication.ui.screens.destinations.LoginScreenDestination
import com.tomtruyen.fitnessapplication.ui.shared.BoxWithLoader
import com.tomtruyen.fitnessapplication.ui.shared.Buttons
import com.tomtruyen.fitnessapplication.ui.shared.dialogs.RestAlertDialog
import com.tomtruyen.fitnessapplication.ui.shared.dialogs.UnitAlertDialog
import com.tomtruyen.fitnessapplication.ui.shared.toolbars.CollapsingToolbar
import com.tomtruyen.fitnessapplication.ui.shared.listitems.ListItem
import com.tomtruyen.fitnessapplication.ui.shared.listitems.SwitchListItem
import com.tomtruyen.core.common.utils.EmailUtils
import com.tomtruyen.core.common.utils.TimeUtils
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@RootNavGraph
@Destination
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
                    navController.navigateAndClearBackStack(
                        destination = LoginScreenDestination
                    )
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

    var unitDialogVisible by remember { mutableStateOf(false) }
    var restDialogVisible by remember { mutableStateOf(false) }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    val emailLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {}

    Scaffold(
        snackbarHost = snackbarHost,
        topBar = {
            CollapsingToolbar(
                title = stringResource(id = R.string.profile),
                navController =  navController,
                scrollBehavior = scrollBehavior
            )
        },
        // nestedScroll modifier is required for the scroll behavior to work
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        BoxWithLoader(
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
                    text = stringResource(id = R.string.units),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.W500
                    ),
                    modifier = Modifier
                        .padding(horizontal = Dimens.Normal)
                        .padding(top = Dimens.Normal, bottom = Dimens.Tiny)
                )

                ListItem(
                    title = stringResource(id = R.string.weight_unit),
                    message = state.settings.unit,
                ) {
                    unitDialogVisible = true
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f))

                Text(
                    text = stringResource(id = R.string.rest_timer),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.W500
                    ),
                    modifier = Modifier
                        .padding(horizontal = Dimens.Normal)
                        .padding(top = Dimens.Normal, bottom = Dimens.Tiny)
                )

                ListItem(
                    title = stringResource(id = R.string.default_rest_timer),
                    message = TimeUtils.formatSeconds(state.settings.rest.toLong()),
                ) {
                    restDialogVisible = true
                }

                SwitchListItem(
                    title = stringResource(id = R.string.rest_timer_enabled),
                    checked = state.settings.restEnabled
                ) {
                    onAction(ProfileUiAction.RestEnabledChanged(it))
                }

                SwitchListItem(
                    title = stringResource(id = R.string.vibrate_upon_finish),
                    checked = state.settings.restVibrationEnabled
                ) {
                    onAction(ProfileUiAction.RestVibrationEnabledChanged(it))
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f))

                Text(
                    text = stringResource(id = R.string.contact_and_support),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.W500
                    ),
                    modifier = Modifier
                        .padding(horizontal = Dimens.Normal)
                        .padding(top = Dimens.Normal, bottom = Dimens.Tiny)
                )

                ListItem(
                    title = stringResource(id = R.string.report_an_issue),
                    message = stringResource(id = R.string.message_report_an_issue),
                ) {
                    emailLauncher.launch(
                        EmailUtils.getEmailIntent(context, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE)
                    )
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f))

                Spacer(modifier = Modifier.weight(1f))

                Buttons.Default(
                    text = stringResource(id = R.string.logout),
                    onClick = { onAction(ProfileUiAction.Logout) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimens.Normal)
                )

                HorizontalDivider(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f))

                Text(
                    text = stringResource(
                        id = R.string.version_and_build,
                        BuildConfig.VERSION_NAME,
                        BuildConfig.VERSION_CODE
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