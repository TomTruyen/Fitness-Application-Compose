package com.tomtruyen.fitnessapplication.ui.shared.workout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.designsystem.Dimens.MinButtonHeight
import com.tomtruyen.core.common.extensions.format
import com.tomtruyen.core.ui.TextFields
import com.tomtruyen.fitnessapplication.ui.shared.dialogs.RestAlertDialog
import com.tomtruyen.core.common.utils.TimeUtils

@Composable
fun WorkoutExerciseSet(
    modifier: Modifier = Modifier,
    exerciseIndex: Int,
    setIndex: Int,
    set: com.tomtruyen.data.entities.WorkoutSet,
    lastPerformedSet: com.tomtruyen.data.entities.WorkoutSet? = null,
    type: com.tomtruyen.data.entities.Exercise.ExerciseType,
    hasMultipleSets: Boolean = false,
    isExecute: Boolean = false,
    onEvent: (WorkoutExerciseEvent) -> Unit,
) {
    var timeDialogVisible by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .defaultMinSize(
                minHeight = MinButtonHeight
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "${setIndex + 1}",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.W500,
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.width(MinButtonHeight)
        )

        if(isExecute) {
            /**
             * Type Weight: Display reps and weight e.g.: 4x10
             * Type Time: Display time e.g.: 1m 30s
             * If there is no known last performed set: Display "-"
             */
            Text(
                text = if(lastPerformedSet == null) {
                    "-"
                } else {
                    when (type) {
                        com.tomtruyen.data.entities.Exercise.ExerciseType.WEIGHT -> {
                            val weight = lastPerformedSet.weight?.format()

                            if(lastPerformedSet.reps == null || weight == null) {
                                "-"
                            } else {
                                "${lastPerformedSet.reps}x${weight}"
                            }
                        }
                        com.tomtruyen.data.entities.Exercise.ExerciseType.TIME -> {
                            if(lastPerformedSet.time == null) {
                                "-"
                            } else {
                                TimeUtils.formatSeconds(lastPerformedSet.time?.toLong() ?: 0L)
                            }
                        }
                    }
                },
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
                    .animateContentSize()
            )
        }

        when(type) {
            // Weight
            com.tomtruyen.data.entities.Exercise.ExerciseType.WEIGHT -> {
                TextFields.Default(
                    value = set.repsText.orEmpty(),
                    onValueChange = { reps ->
                        // Check if value can be cast to int, if not don't update the value
                        if (reps.isNotEmpty() && reps.toIntOrNull() == null) return@Default

                        onEvent(
                            WorkoutExerciseEvent.OnRepsChanged(
                                exerciseIndex = exerciseIndex,
                                setIndex = setIndex,
                                reps = reps
                            )
                        )
                    },
                    placeholder = "0",
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.NumberPassword,
                        imeAction = ImeAction.Next
                    ),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        textAlign = TextAlign.Center
                    ),
                    padding = PaddingValues(Dimens.Small),
                    modifier = Modifier
                        .weight(1f)
                        .animateContentSize()
                )

                Spacer(modifier = Modifier.width(Dimens.Small))

                TextFields.Default(
                    value = set.weightText.orEmpty(),
                    onValueChange = { weight ->
                        val filteredWeight = weight.replace(",", ".")

                        // Check if the number can be cast to double, if not don't update the value
                        if (filteredWeight.isNotEmpty() && filteredWeight.toDoubleOrNull() == null) return@Default

                        onEvent(
                            WorkoutExerciseEvent.OnWeightChanged(
                                exerciseIndex = exerciseIndex,
                                setIndex = setIndex,
                                weight = filteredWeight
                            )
                        )
                    },
                    placeholder = "0",
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        textAlign = TextAlign.Center
                    ),
                    padding = PaddingValues(Dimens.Small),
                    modifier = Modifier
                        .weight(1f)
                        .animateContentSize()
                )
            }

            // Time
            com.tomtruyen.data.entities.Exercise.ExerciseType.TIME -> Text(
                text = TimeUtils.formatSeconds(set.time?.toLong() ?: 0L),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
                    .animateContentSize()
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = MaterialTheme.shapes.small
                    )
                    .clip(MaterialTheme.shapes.small)
                    .clickable {
                        timeDialogVisible = true
                    }
                    .padding(Dimens.Small)
            )
        }

        AnimatedVisibility(
            modifier = Modifier.width(MinButtonHeight),
            visible = hasMultipleSets && !isExecute
        ) {
            IconButton(
                onClick = {
                    onEvent(WorkoutExerciseEvent.OnDeleteSetClicked(exerciseIndex, setIndex))
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = null
                )
            }
        }

        if(timeDialogVisible) {
            RestAlertDialog(
                onDismiss = {
                    timeDialogVisible = false
                },
                onConfirm = { time, _ ->
                    onEvent(WorkoutExerciseEvent.OnTimeChanged(exerciseIndex, setIndex, time))
                    timeDialogVisible = false
                },
                rest = set.time ?: 0,
                type = com.tomtruyen.models.RestAlertType.SET_TIME
            )
        }
    }
}