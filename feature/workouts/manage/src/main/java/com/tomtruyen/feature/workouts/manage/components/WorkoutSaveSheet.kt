package com.tomtruyen.feature.workouts.manage.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.ui.Buttons
import com.tomtruyen.feature.workouts.manage.ManageWorkoutUiAction
import com.tomtruyen.feature.workouts.manage.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutSaveSheet(
    workoutName: String,
    visible: Boolean,
    onAction: (ManageWorkoutUiAction) -> Unit,
    onDismiss: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val state = rememberModalBottomSheetState()
    val listState = rememberLazyListState()

    if(visible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = state,
            containerColor = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.padding(Dimens.Normal),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens.Small)
            ) {
                Text(
                    text = stringResource(id = R.string.label_update_x, workoutName),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.W500
                    ),
                )

                Text(
                    text = stringResource(id = R.string.description_update_workout, workoutName),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                    ),
                )

                Spacer(modifier = Modifier.height(Dimens.Normal))

                Buttons.Default(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.button_update_workout),
                    onClick = {
                        onAction(ManageWorkoutUiAction.Workout.Save(true))

                        scope.launch {
                            state.hide()
                        }.invokeOnCompletion {
                            onDismiss()
                        }
                    }
                )

                Buttons.Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.button_keep_original_workout),
                    onClick = {
                        onAction(ManageWorkoutUiAction.Workout.Save())

                        scope.launch {
                            state.hide()
                        }.invokeOnCompletion {
                            onDismiss()
                        }
                    }
                )
            }
        }
    }
}