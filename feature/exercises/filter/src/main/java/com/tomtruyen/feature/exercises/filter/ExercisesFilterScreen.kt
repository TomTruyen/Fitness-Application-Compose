package com.tomtruyen.feature.exercises.filter

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.tomtruyen.core.common.ObserveEvent
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.ui.Buttons
import com.tomtruyen.core.ui.Chip
import com.tomtruyen.core.ui.toolbars.Toolbar
import com.tomtruyen.feature.exercises.ExercisesUiAction
import com.tomtruyen.feature.exercises.ExercisesUiEvent
import com.tomtruyen.feature.exercises.ExercisesUiState
import com.tomtruyen.feature.exercises.ExercisesViewModel
import kotlinx.coroutines.flow.collectLatest
import com.tomtruyen.core.common.R as CommonR

@Composable
fun ExercisesFilterScreen(
    navController: NavController,
    viewModel: ExercisesViewModel
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    ObserveEvent(viewModel) { event ->
        when (event) {
            is ExercisesUiEvent.Navigate.Back -> navController.popBackStack()
            else -> Unit
        }
    }

    ExercisesFilterScreenLayout(
        navController = navController,
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ExercisesFilterScreenLayout(
    navController: NavController,
    state: ExercisesUiState,
    onAction: (ExercisesUiAction) -> Unit
) {
    Scaffold(
        topBar = {
            Toolbar(
                title = stringResource(id = R.string.title_filter),
                navController = navController,
            ) {
                Buttons.Text(
                    text = stringResource(id = CommonR.string.button_clear),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier.align(Alignment.CenterVertically),
                ) {
                    onAction(ExercisesUiAction.Filter.OnClearClicked)
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
                text = stringResource(id = R.string.label_categories),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.W500
                )
            )
            // Muscle Groups (Categories)
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.Small)
            ) {
                state.categories.forEach { category ->
                    Chip(category.name, state.filter.categories.contains(category)) {
                        onAction(ExercisesUiAction.Filter.OnCategoryFilterChanged(category))
                    }
                }
            }

            Spacer(modifier = Modifier.height(Dimens.Huge))

            Text(
                text = stringResource(id = R.string.label_equipment),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.W500
                )
            )
            // Equipment
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.Small)
            ) {
                state.equipment.forEach { equipment ->
                    Chip(equipment.name, state.filter.equipment.contains(equipment)) {
                        onAction(ExercisesUiAction.Filter.OnEquipmentFilterChanged(equipment))
                    }
                }
            }
        }
    }
}