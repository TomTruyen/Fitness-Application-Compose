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
import com.tomtruyen.fitnessapplication.Dimens
import com.tomtruyen.fitnessapplication.Dimens.MinButtonHeight
import com.tomtruyen.fitnessapplication.data.entities.Exercise
import com.tomtruyen.fitnessapplication.data.entities.WorkoutSet
import com.tomtruyen.fitnessapplication.model.RestAlertType
import com.tomtruyen.fitnessapplication.ui.shared.TextFields
import com.tomtruyen.fitnessapplication.ui.shared.dialogs.RestAlertDialog
import com.tomtruyen.fitnessapplication.utils.TimeUtils

@Composable
fun WorkoutExerciseSet(
    modifier: Modifier = Modifier,
    exerciseIndex: Int,
    setIndex: Int,
    set: WorkoutSet,
    type: Exercise.ExerciseType,
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
                color = MaterialTheme.colorScheme.primary
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.width(Dimens.MinButtonHeight)
        )

        when(type) {
            // Weight
            Exercise.ExerciseType.WEIGHT -> {
                TextFields.Default(
                    value = set.repsText ?: "",
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
                    value = set.weightText ?: "",
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
            Exercise.ExerciseType.TIME -> Text(
                text = TimeUtils.formatSeconds(set.time ?: 0),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
                    .animateContentSize()
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = MaterialTheme.shapes.medium
                    )
                    .clip(MaterialTheme.shapes.medium)
                    .clickable {
                        timeDialogVisible = true
                    }
                    .padding(Dimens.Small)
            )
        }

        AnimatedVisibility(
            modifier = Modifier.width(Dimens.MinButtonHeight),
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
                type = RestAlertType.SET_TIME
            )
        }
    }
}