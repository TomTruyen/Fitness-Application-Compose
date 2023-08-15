package com.tomtruyen.fitnessapplication.ui.screens.main.exercises.filter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.tomtruyen.fitnessapplication.Dimens
import com.tomtruyen.fitnessapplication.R
import com.tomtruyen.fitnessapplication.navigation.ExercisesNavGraph
import com.tomtruyen.fitnessapplication.ui.screens.main.exercises.ExercisesNavigationType
import com.tomtruyen.fitnessapplication.ui.screens.main.exercises.ExercisesUiEvent
import com.tomtruyen.fitnessapplication.ui.screens.main.exercises.ExercisesUiState
import com.tomtruyen.fitnessapplication.ui.screens.main.exercises.ExercisesViewModel
import com.tomtruyen.fitnessapplication.ui.shared.Buttons
import com.tomtruyen.fitnessapplication.ui.shared.ExerciseFilterChip
import com.tomtruyen.fitnessapplication.ui.shared.Toolbar
import kotlinx.coroutines.flow.collectLatest

@ExercisesNavGraph
@Destination
@Composable
fun ExercisesFilterScreen(
    navController: NavController,
    viewModel: ExercisesViewModel
) {
    val context = LocalContext.current

    val state by viewModel.state.collectAsStateWithLifecycle()
    val equipment by viewModel.equipment.collectAsStateWithLifecycle(initialValue = emptyList())
    val categories by viewModel.categories.collectAsStateWithLifecycle(initialValue = emptyList())

    LaunchedEffect(viewModel, context) {
        viewModel.navigation.collectLatest { navigationType ->
            when (navigationType) {
                is ExercisesNavigationType.Back -> navController.popBackStack()
                else -> Unit
            }
        }
    }

    ExercisesFilterScreenLayout(
        snackbarHost = { viewModel.CreateSnackbarHost() },
        navController = navController,
        state = state,
        equipment = equipment,
        categories = categories,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ExercisesFilterScreenLayout(
    snackbarHost: @Composable () -> Unit,
    navController: NavController,
    state: ExercisesUiState,
    equipment: List<String>,
    categories: List<String>,
    onEvent: (ExercisesUiEvent) -> Unit
) {
    Scaffold(
        snackbarHost = snackbarHost,
        topBar = {
            Toolbar(
                title = stringResource(id = R.string.filter),
                navController = navController,
            ) {
                Buttons.Text(
                    text = stringResource(id = R.string.clear),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier.align(Alignment.CenterVertically),
                ) {
                    onEvent(ExercisesUiEvent.OnClearFilterClicked)
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .padding(Dimens.Normal)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(id = R.string.categories),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.W500
                )
            )
            // Muscle Groups (Categories)
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.Small)
            ) {
                categories.forEach { category ->
                    ExerciseFilterChip(category, state.filter.categories.contains(category)) {
                        onEvent(ExercisesUiEvent.OnCategoryFilterChanged(category))
                    }
                }
            }

            Spacer(modifier = Modifier.height(Dimens.Huge))

            Text(
                text = stringResource(id = R.string.equipment),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.W500
                )
            )
            // Equipment
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.Small)
            ) {
                equipment.forEach { equipment ->
                    ExerciseFilterChip(equipment, state.filter.equipment.contains(equipment)) {
                        onEvent(ExercisesUiEvent.OnEquipmentFilterChanged(equipment))
                    }
                }
            }
        }
    }
}