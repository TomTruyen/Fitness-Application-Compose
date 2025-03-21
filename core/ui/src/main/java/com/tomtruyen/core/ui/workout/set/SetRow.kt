package com.tomtruyen.core.ui.workout.set

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.tomtruyen.core.common.extensions.rounded
import com.tomtruyen.core.common.extensions.tryIntString
import com.tomtruyen.core.common.models.BaseSet
import com.tomtruyen.core.common.models.ChangeType
import com.tomtruyen.core.common.models.ExerciseType
import com.tomtruyen.core.common.models.WorkoutMode
import com.tomtruyen.core.common.utils.TimeUtils
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.ui.TextFields
import com.tomtruyen.core.ui.wheeltimepicker.WheelTimerPickerSheet
import com.tomtruyen.core.ui.wheeltimepicker.core.TimeComponent
import com.tomtruyen.core.common.models.ExerciseSet
import com.tomtruyen.core.common.models.actions.SetActions
import com.tomtruyen.core.common.remember.rememberSetInputValue

@Composable
fun SetRow(
    modifier: Modifier = Modifier,
    exerciseType: ExerciseType,
    workoutExerciseId: String,
    setIndex: Int,
    set: ExerciseSet,
    previousSet: BaseSet?,
    mode: WorkoutMode,
    onAction: SetActions?,
    onSetClick: (id: String, setIndex: Int) -> Unit,
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
                .clickable(
                    enabled = !mode.isView
                ) {
                    onSetClick(workoutExerciseId, setIndex)
                }
        )

        if (mode.isExecute) {
            PreviousSet(
                previousSet = previousSet,
                type = exerciseType,
                modifier = Modifier
                    .weight(1f)
                    .clip(MaterialTheme.shapes.small)
                    .clickable(
                        enabled = !mode.isView && previousSet != null
                    ) {
                        previousSet?.let {
                            onAction?.fillPreviousSet(
                                id = workoutExerciseId,
                                setIndex = setIndex,
                                previousSet = it
                            )
                        }

                    }
            )

            Spacer(modifier = Modifier.width(Dimens.Small))
        }


        when (exerciseType) {
            ExerciseType.WEIGHT -> WeightSet(
                weight = set.weight,
                reps = set.reps,
                changeRecord = set.changeRecord,
                mode = mode,
                completed = set.completed,
                onRepsChanged = { reps ->
                    onAction?.repsChanged(
                        exerciseId = workoutExerciseId,
                        setIndex = setIndex,
                        reps = reps
                    )
                },
                onWeightChanged = { weight ->
                    onAction?.weightChanged(
                        exerciseId = workoutExerciseId,
                        setIndex = setIndex,
                        weight = weight
                    )
                }
            )

            ExerciseType.TIME -> TimeSet(
                time = set.time,
                changeRecord = set.changeRecord,
                mode = mode,
                completed = set.completed,
                onTimeChanged = { time ->
                    onAction?.timeChanged(
                        exerciseId = workoutExerciseId,
                        setIndex = setIndex,
                        time = time
                    )
                }
            )
        }

        if (mode.isExecute) {
            SetCheckbox(
                checked = set.completed,
                onClick = {
                    onAction?.toggleCompleted(
                        exerciseId = workoutExerciseId,
                        setIndex = setIndex,
                        previousSet = previousSet
                    )
                },
            )
        }
    }
}

@Composable
private fun PreviousSet(
    previousSet: BaseSet?,
    type: ExerciseType,
    modifier: Modifier = Modifier
) {
    /**
     * Type Weight: Display reps and weight e.g.: 4x10
     * Type Time: Display time e.g.: 1m 30s
     * If there is no known last performed set: Display "-"
     */
    Text(
        text = previousSet?.let { set ->
            when (type) {
                ExerciseType.WEIGHT -> {
                    if (set.reps == null && set.weight == null) return@let null

                    "${set.reps ?: 0}x${set.weight?.rounded() ?: 0}"
                }

                ExerciseType.TIME -> {
                    if (set.time == null) return@let null

                    TimeUtils.formatSeconds(set.time?.toLong() ?: 0L)
                }
            }
        } ?: "-",
        style = MaterialTheme.typography.labelLarge,
        textAlign = TextAlign.Center,
        modifier = modifier,
    )

}

@Composable
private fun RowScope.WeightSet(
    weight: Double?,
    reps: Int?,
    changeRecord: Set<ChangeType>,
    mode: WorkoutMode,
    completed: Boolean,
    onRepsChanged: (String) -> Unit,
    onWeightChanged: (String) -> Unit
) {
    val (inputReps, initialReps) = rememberSetInputValue<Int?, String>(
        value = reps,
        defaultValue = "",
        mode = mode,
        didChange = {
            changeRecord.contains(ChangeType.REP) || completed
        },
        transform = { it?.toString().orEmpty() }
    )

    val (inputWeight, initialWeight) = rememberSetInputValue<Double?, String>(
        value = weight,
        defaultValue = "",
        mode = mode,
        didChange = {
            changeRecord.contains(ChangeType.WEIGHT) || completed
        },
        transform = { it?.tryIntString().orEmpty() }
    )

    TextFields.Default(
        readOnly = mode.isView,
        padding = PaddingValues(Dimens.Small),
        placeholder = initialReps?.toString() ?: "-",
        value = inputReps.value,
        onValueChange = { newReps ->
            val reps = newReps.replace(",", ".").trim()

            // Check if value can be cast to int, if not don't update the value
            if (reps.isNotEmpty() && reps.toIntOrNull() == null) return@Default

            onRepsChanged(reps)
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.W500,
        ),
        containerColor = Color.Transparent,
        modifier = Modifier.weight(1f)
    )

    Spacer(modifier = Modifier.width(Dimens.Small))

    TextFields.Default(
        readOnly = mode.isView,
        padding = PaddingValues(Dimens.Small),
        placeholder = initialWeight?.tryIntString() ?: "-",
        value = inputWeight.value,
        onValueChange = { newWeight ->
            val weight = newWeight.replace(",", ".").trim()

            // Check if the number can be cast to double, if not don't update the value
            if (weight.isNotEmpty() && weight.toDoubleOrNull() == null) return@Default

            // Must be set like this, otherwise we can't set the decimal separator
            inputWeight.value = weight

            onWeightChanged(weight)
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        ),
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.W500,
        ),
        containerColor = Color.Transparent,
        modifier = Modifier.weight(1f)
    )
}

@Composable
private fun RowScope.TimeSet(
    time: Int?,
    changeRecord: Set<ChangeType>,
    mode: WorkoutMode,
    completed: Boolean,
    onTimeChanged: (Int) -> Unit
) {
    var timeSheetVisible by remember { mutableStateOf(false) }

    val (inputTime, _) = rememberSetInputValue<Long?, String>(
        value = time?.toLong(),
        defaultValue = TimeUtils.formatSeconds(time?.toLong() ?: 0L),
        mode = mode,
        didChange = {
            changeRecord.contains(ChangeType.TIME) || completed
        },
        transform = { TimeUtils.formatSeconds(it ?: 0L) },
    )

    Text(
        text = inputTime.value,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyMedium.copy(
            color = if (!mode.isExecute || changeRecord.contains(ChangeType.TIME) || completed) {
                MaterialTheme.typography.bodyMedium.color
            } else {
                MaterialTheme.typography.labelLarge.color
            },
            fontWeight = FontWeight.W500
        ),
        modifier = Modifier
            .weight(1f)
            .clip(MaterialTheme.shapes.small)
            .clickable(
                enabled = !mode.isView
            ) {
                timeSheetVisible = true
            }
            .padding(Dimens.Small)
    )

    WheelTimerPickerSheet(
        seconds = time ?: 0,
        visible = timeSheetVisible,
        components = listOf(TimeComponent.HOUR, TimeComponent.MINUTE, TimeComponent.SECOND),
        onSubmit = {
            onTimeChanged(it)
        },
        onDismiss = {
            timeSheetVisible = false
        }
    )
}