package com.tomtruyen.feature.workouts.manage.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.tomtruyen.core.common.extensions.format
import com.tomtruyen.core.common.models.ExerciseType
import com.tomtruyen.core.common.models.RestAlertType
import com.tomtruyen.core.common.utils.TimeUtils
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.ui.TextFields
import com.tomtruyen.core.ui.dialogs.RestAlertDialog
import com.tomtruyen.data.models.ui.WorkoutExerciseSetUiModel
import com.tomtruyen.feature.workouts.manage.ManageWorkoutUiAction
import com.tomtruyen.feature.workouts.manage.models.ManageWorkoutMode
import com.tomtruyen.core.common.R as CommonR

@Composable
fun WorkoutExerciseSetRow(
    modifier: Modifier = Modifier,
    exerciseType: ExerciseType,
    workoutExerciseId: String,
    setIndex: Int,
    set: WorkoutExerciseSetUiModel,
    lastPerformedSet: WorkoutExerciseSetUiModel? = null,
    mode: ManageWorkoutMode,
    onAction: (ManageWorkoutUiAction) -> Unit,
    onSetClick: (id: String, setIndex: Int) -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            when (it) {
                SwipeToDismissBoxValue.EndToStart -> {
                    onAction(
                        ManageWorkoutUiAction.OnDeleteSet(
                            id = workoutExerciseId,
                            setIndex = setIndex
                        )
                    )
                }

                else -> Unit
            }

            return@rememberSwipeToDismissBoxState false
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.error)
                    .padding(end = Dimens.Normal),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.Tiny, Alignment.End)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(id = CommonR.string.button_delete),
                    tint = MaterialTheme.colorScheme.onError,
                )

                Text(
                    text = stringResource(id = CommonR.string.button_delete),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onError,
                        fontWeight = FontWeight.W500
                    ),
                )
            }
        }
    ) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "${setIndex + 1}",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.W500,
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .width(Dimens.MinButtonHeight)
                    .clip(CircleShape)
                    .clickable {
                        onSetClick(workoutExerciseId, setIndex)
                    }
            )

            if (mode == ManageWorkoutMode.EXECUTE) {
                PreviousSet(
                    lastPerformedSet = lastPerformedSet,
                    type = exerciseType,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(Dimens.Small))
            }


            when (exerciseType) {
                ExerciseType.WEIGHT -> WeightSet(
                    weight = set.weight,
                    reps = set.reps,
                    onRepsChanged = { reps ->
                        onAction(
                            ManageWorkoutUiAction.OnRepsChanged(
                                id = workoutExerciseId,
                                setIndex = setIndex,
                                reps = reps
                            )
                        )
                    },
                    onWeightChanged = { weight ->
                        onAction(
                            ManageWorkoutUiAction.OnWeightChanged(
                                id = workoutExerciseId,
                                setIndex = setIndex,
                                weight = weight
                            )
                        )
                    }
                )

                ExerciseType.TIME -> TimeSet(
                    time = set.time,
                    onTimeChanged = { time ->
                        onAction(
                            ManageWorkoutUiAction.OnTimeChanged(
                                id = workoutExerciseId,
                                setIndex = setIndex,
                                time = time
                            )
                        )
                    }
                )

                else -> Unit
            }

            if (mode == ManageWorkoutMode.EXECUTE) {
                WorkoutCheckbox(
                    checked = set.completed,
                    onClick = {
                        onAction(
                            ManageWorkoutUiAction.ToggleSetCompleted(
                                id = workoutExerciseId,
                                setIndex = setIndex
                            )
                        )
                    },
                )
            }
        }
    }
}

@Composable
private fun PreviousSet(
    lastPerformedSet: WorkoutExerciseSetUiModel?,
    type: ExerciseType,
    modifier: Modifier = Modifier
) {
    /**
     * Type Weight: Display reps and weight e.g.: 4x10
     * Type Time: Display time e.g.: 1m 30s
     * If there is no known last performed set: Display "-"
     */
    Text(
        text = lastPerformedSet?.let { set ->
            when (type) {
                ExerciseType.WEIGHT -> {
                    val weight = set.weight?.format()

                    "${set.reps}x${weight}"
                }

                ExerciseType.TIME -> {
                    TimeUtils.formatSeconds(set.time?.toLong() ?: 0L)
                }
            }
        } ?: "-",
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Center,
        modifier = modifier,
    )

}

@Composable
private fun RowScope.WeightSet(
    weight: Double?,
    reps: Int?,
    onRepsChanged: (String) -> Unit,
    onWeightChanged: (String) -> Unit
) {
    var inputReps by remember {
        mutableStateOf(reps?.toString().orEmpty())
    }

    var inputWeight by remember {
        mutableStateOf(weight?.toString().orEmpty())
    }

    TextFields.Default(
        border = false,
        padding = PaddingValues(Dimens.Small),
        value = inputReps,
        onValueChange = { newReps ->
            // Check if value can be cast to int, if not don't update the value
            if (newReps.isNotEmpty() && newReps.toIntOrNull() == null) return@Default

            inputReps = newReps

            onRepsChanged(newReps)
        },
        placeholder = "-",
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.NumberPassword,
            imeAction = ImeAction.Next
        ),
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            textAlign = TextAlign.Center
        ),
        modifier = Modifier.weight(1f)
    )

    Spacer(modifier = Modifier.width(Dimens.Small))

    TextFields.Default(
        border = false,
        padding = PaddingValues(Dimens.Small),
        value = inputWeight,
        onValueChange = { newWeight ->
            val filteredWeight = newWeight.replace(",", ".")

            // Check if the number can be cast to double, if not don't update the value
            if (filteredWeight.isNotEmpty() && filteredWeight.toDoubleOrNull() == null) return@Default

            inputWeight = filteredWeight

            onWeightChanged(filteredWeight)
        },
        placeholder = "-",
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        ),
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            textAlign = TextAlign.Center
        ),
        modifier = Modifier.weight(1f)
    )
}

@Composable
private fun RowScope.TimeSet(
    time: Int?,
    onTimeChanged: (Int) -> Unit
) {
    var timeDialogVisible by remember { mutableStateOf(false) }

    Text(
        text = TimeUtils.formatSeconds(time?.toLong() ?: 0L),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .weight(1f)
            .clip(MaterialTheme.shapes.small)
            .clickable {
                timeDialogVisible = true
            }
            .padding(Dimens.Small)
    )

    if (timeDialogVisible) {
        RestAlertDialog(
            onDismiss = {
                timeDialogVisible = false
            },
            onConfirm = { newTime, _ ->
                onTimeChanged(newTime)
                timeDialogVisible = false
            },
            rest = time ?: 0,
            type = RestAlertType.SET_TIME
        )
    }
}