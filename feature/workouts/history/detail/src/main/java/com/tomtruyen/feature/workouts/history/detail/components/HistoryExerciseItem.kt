package com.tomtruyen.feature.workouts.history.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tomtruyen.core.common.extensions.tryIntString
import com.tomtruyen.core.common.models.ExerciseType
import com.tomtruyen.core.common.models.UnitType
import com.tomtruyen.core.common.utils.TimeUtils
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.ui.Avatar
import com.tomtruyen.core.ui.TextFields
import com.tomtruyen.data.models.ui.WorkoutHistoryExerciseSetUiModel
import com.tomtruyen.data.models.ui.WorkoutHistoryExerciseUiModel
import com.tomtruyen.feature.workouts.history.detail.R

@Composable
fun HistoryExerciseItem(
    exercise: WorkoutHistoryExerciseUiModel,
    unit: UnitType,
    onExerciseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Dimens.Normal)
    ) {
        HistoryExerciseItemHeader(
            name = exercise.displayName,
            imageUrl = exercise.imageUrl,
            onExerciseDetail = onExerciseClick
        )

        // Notes
        TextFields.Default(
            modifier = Modifier.padding(
                start = Dimens.Normal,
                end = Dimens.Normal,
                bottom = Dimens.Small
            ),
            readOnly = true,
            singleLine = false,
            border = true,
            padding = PaddingValues(Dimens.Small),
            placeholder = stringResource(id = R.string.placeholder_notes),
            value = exercise.notes,
            onValueChange = {}
        )

        HistoryExerciseItemSetTable(
            sets = exercise.sets,
            exerciseType = exercise.type,
            unit = unit
        )
    }
}

@Composable
private fun HistoryExerciseItemHeader(
    name: String,
    imageUrl: String?,
    onExerciseDetail: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
        modifier = modifier.padding(
            horizontal = Dimens.Normal,
        )
    ) {
        Avatar(
            imageUrl = imageUrl,
            contentDescription = name,
        )

        Text(
            text = name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.W500
            ),
            modifier = Modifier
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { onExerciseDetail() }
                    )
                }
        )
    }
}

@Composable
private fun ColumnScope.HistoryExerciseItemSetTable(
    sets: List<WorkoutHistoryExerciseSetUiModel>,
    exerciseType: ExerciseType,
    unit: UnitType,
) {
    Column {
        HistoryExerciseItemSetTableHeader(
            exerciseType = exerciseType,
            unit = unit
        )

        sets.forEachIndexed { setIndex, set ->
            HistoryExerciseItemSetRow(
                set = set,
                exerciseType = exerciseType,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = if (setIndex % 2 == 0) {
                            MaterialTheme.colorScheme.background
                        } else {
                            MaterialTheme.colorScheme.surface
                        }
                    )
                    .padding(
                        horizontal = Dimens.Normal,
                        vertical = Dimens.Tiny
                    ),
            )
        }
    }
}

@Composable
private fun HistoryExerciseItemSetTableHeader(
    exerciseType: ExerciseType,
    unit: UnitType,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = Dimens.Normal
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(id = R.string.label_set).uppercase(),
            textAlign = TextAlign.Center,
            modifier = Modifier.width(Dimens.MinButtonHeight),
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.W500,
            )
        )

        when (exerciseType) {
            ExerciseType.WEIGHT -> {
                Text(
                    text = stringResource(id = R.string.label_reps).uppercase(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.W500,
                    )
                )

                Spacer(modifier = Modifier.width(Dimens.Small))

                Text(
                    text = unit.value.uppercase(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.W500,
                    )
                )
            }

            ExerciseType.TIME -> {
                Text(
                    text = stringResource(id = R.string.label_time).uppercase(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.W500,
                    )
                )
            }
        }
    }
}

@Composable
private fun HistoryExerciseItemSetRow(
    set: WorkoutHistoryExerciseSetUiModel,
    exerciseType: ExerciseType,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "${set.sortOrder + 1}",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.W500,
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.width(Dimens.MinButtonHeight)
        )

        when (exerciseType) {
            ExerciseType.WEIGHT -> WeightSet(
                weight = set.weight,
                reps = set.reps,
            )

            ExerciseType.TIME -> TimeSet(
                time = set.time,
            )
        }
    }
}

@Composable
private fun RowScope.WeightSet(
    weight: Double?,
    reps: Int?,
) {
    TextFields.Default(
        border = false,
        readOnly = true,
        padding = PaddingValues(Dimens.Small),
        value = (reps ?: 0).toString(),
        onValueChange = {},
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.W500,
            color = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier.weight(1f)
    )

    Spacer(modifier = Modifier.width(Dimens.Small))

    TextFields.Default(
        border = false,
        readOnly = true,
        padding = PaddingValues(Dimens.Small),
        value = (weight ?: 0.0).tryIntString(),
        onValueChange = {},
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.W500,
            color = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier.weight(1f)
    )
}

@Composable
private fun RowScope.TimeSet(
    time: Int?
) {
    Text(
        text = TimeUtils.formatSeconds(time?.toLong() ?: 0L),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyMedium.copy(
            fontWeight = FontWeight.W500
        ),
        modifier = Modifier
            .weight(1f)
            .padding(Dimens.Small)
    )
}
