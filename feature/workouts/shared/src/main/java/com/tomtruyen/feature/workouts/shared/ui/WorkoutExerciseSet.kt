package com.tomtruyen.feature.workouts.shared.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.material3.ripple
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
import com.tomtruyen.core.common.utils.TimeUtils
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.ui.TextFields
import com.tomtruyen.core.ui.dialogs.RestAlertDialog
import com.tomtruyen.data.entities.Exercise
import com.tomtruyen.data.entities.WorkoutSet
import com.tomtruyen.data.firebase.models.WorkoutExerciseResponse
import com.tomtruyen.feature.workouts.shared.WorkoutExerciseUiAction
import com.tomtruyen.models.RestAlertType
import com.tomtruyen.core.common.R as CommonR

@Composable
fun WorkoutExerciseSet(
    modifier: Modifier = Modifier,
    workoutExercise: WorkoutExerciseResponse,
    setIndex: Int,
    set: WorkoutSet,
    lastPerformedSet: WorkoutSet? = null,
    isExecute: Boolean = false,
    onEvent: (WorkoutExerciseUiAction) -> Unit,
    onSetClick: (id: String, setIndex: Int) -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            when(it) {
                SwipeToDismissBoxValue.EndToStart -> {
                    onEvent(
                        WorkoutExerciseUiAction.OnDeleteSet(
                            id = workoutExercise.id,
                            setIndex = setIndex
                        )
                    )
                }
                else -> return@rememberSwipeToDismissBoxState false
            }

            return@rememberSwipeToDismissBoxState true
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
                        onSetClick(workoutExercise.id, setIndex)
                    }
            )

            if (isExecute) {
                PreviousSet(
                    lastPerformedSet = lastPerformedSet,
                    type = workoutExercise.exercise.typeEnum,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(Dimens.Small))
            }


            when (workoutExercise.exercise.typeEnum) {
                Exercise.ExerciseType.WEIGHT -> WeightSet(
                    set = set,
                    onRepsChanged = { reps ->
                        onEvent(
                            WorkoutExerciseUiAction.OnRepsChanged(
                                id = workoutExercise.id,
                                setIndex = setIndex,
                                reps = reps
                            )
                        )
                    },
                    onWeightChanged = { weight ->
                        onEvent(
                            WorkoutExerciseUiAction.OnWeightChanged(
                                id = workoutExercise.id,
                                setIndex = setIndex,
                                weight = weight
                            )
                        )
                    }
                )

                Exercise.ExerciseType.TIME -> TimeSet(
                    set = set,
                    onTimeChanged = { time ->
                        onEvent(
                            WorkoutExerciseUiAction.OnTimeChanged(
                                workoutExercise.id,
                                setIndex,
                                time
                            )
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun PreviousSet(
    lastPerformedSet: WorkoutSet?,
    type: Exercise.ExerciseType,
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
                Exercise.ExerciseType.WEIGHT -> {
                    if (set.reps == null || set.weight == null) {
                        return@let null
                    }

                    val weight = set.weight?.format()

                    "${set.reps}x${weight}"
                }

                Exercise.ExerciseType.TIME -> {
                    if (set.time == null) {
                        return@let null
                    }

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
    set: WorkoutSet,
    onRepsChanged: (String) -> Unit,
    onWeightChanged: (String) -> Unit
) {
    TextFields.Default(
        border = false,
        padding = PaddingValues(Dimens.Small),
        value = set.repsText.orEmpty(),
        onValueChange = { reps ->
            // Check if value can be cast to int, if not don't update the value
            if (reps.isNotEmpty() && reps.toIntOrNull() == null) return@Default

            onRepsChanged(reps)
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
        value = set.weightText.orEmpty(),
        onValueChange = { weight ->
            val filteredWeight = weight.replace(",", ".")

            // Check if the number can be cast to double, if not don't update the value
            if (filteredWeight.isNotEmpty() && filteredWeight.toDoubleOrNull() == null) return@Default

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
    set: WorkoutSet,
    onTimeChanged: (Int) -> Unit
) {
    var timeDialogVisible by remember { mutableStateOf(false) }

    Text(
        text = TimeUtils.formatSeconds(set.time?.toLong() ?: 0L),
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
            onConfirm = { time, _ ->
                onTimeChanged(time)
                timeDialogVisible = false
            },
            rest = set.time ?: 0,
            type = RestAlertType.SET_TIME
        )
    }
}