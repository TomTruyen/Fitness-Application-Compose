package com.tomtruyen.feature.workouts.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tomtruyen.core.common.R
import com.tomtruyen.core.common.extensions.rounded
import com.tomtruyen.core.common.extensions.toRelativeTimeString
import com.tomtruyen.core.common.models.UnitType
import com.tomtruyen.core.common.utils.TimeUtils
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.designsystem.theme.secondaryLabelColor
import com.tomtruyen.core.designsystem.theme.secondaryTextColor
import com.tomtruyen.core.ui.Avatar
import com.tomtruyen.core.ui.Label
import com.tomtruyen.data.models.ui.WorkoutHistoryExerciseUiModel
import com.tomtruyen.data.models.ui.WorkoutHistoryUiModel
import com.tomtruyen.feature.workouts.history.WorkoutHistoryUiAction
import kotlinx.datetime.LocalDateTime

const val VISIBLE_EXERCISE_COUNT = 3

@Composable
fun WorkoutHistoryItem(
    history: WorkoutHistoryUiModel,
    onAction: (WorkoutHistoryUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val visibleExercises = remember(history.exercises) {
        history.exercises.take(VISIBLE_EXERCISE_COUNT)
    }

    val otherExerciseCount = remember(visibleExercises) {
        history.exercises.size - VISIBLE_EXERCISE_COUNT
    }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(0.dp),
        onClick = {
            onAction(WorkoutHistoryUiAction.Navigate.Detail(history.id))
        }
    ) {
        Column(
            modifier = Modifier.padding(
                top = Dimens.Small,
                bottom = Dimens.Normal
            ),
            verticalArrangement = Arrangement.spacedBy(Dimens.Small)
        ) {
            Header(
                workoutName = history.name,
                date = history.createdAt,
                onOpenSheet = {
                    onAction(WorkoutHistoryUiAction.Sheet.Show(history.id))
                }
            )

            Statistics(
                duration = history.duration,
                volume = history.volume,
                unit = history.unit
            )

            Spacer(modifier = Modifier.height(Dimens.Normal))

            visibleExercises.forEach { exercise ->
                ExerciseItem(exercise)
            }

            if (otherExerciseCount > 0) {
                Text(
                    modifier = Modifier
                        .padding(top = Dimens.Small)
                        .fillMaxWidth(),
                    text = stringResource(
                        id = R.string.see_x_more_exercises,
                        otherExerciseCount
                    ),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.secondaryLabelColor.value
                    )
                )
            }
        }
    }
}

@Composable
private fun Header(
    workoutName: String,
    date: LocalDateTime,
    onOpenSheet: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = Dimens.Normal),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = workoutName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.W500
                ),
            )

            Label(
                label = date.toRelativeTimeString(),
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.secondaryTextColor.value,
                )
            )
        }

        IconButton(
            onClick = onOpenSheet
        ) {
            Icon(
                imageVector = Icons.Rounded.MoreVert,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun Statistics(
    duration: Long,
    volume: Double,
    unit: UnitType,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.Normal),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        StatisticsItem(
            name = stringResource(id = R.string.label_time),
            value = TimeUtils.formatDuration(duration),
        )

        StatisticsItem(
            name = stringResource(id = R.string.label_volume),
            value = "${volume.rounded()} ${unit.value}",
        )

        // To be replaced with Records once we have that
        Spacer(
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun RowScope.StatisticsItem(
    name: String,
    value: String
) {
    Column(
        modifier = Modifier.weight(1f),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.secondaryTextColor.value
            ),
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun ExerciseItem(
    exercise: WorkoutHistoryExerciseUiModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.Normal),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.Normal)
    ) {
        Avatar(
            imageUrl = exercise.imageUrl,
            contentDescription = exercise.displayName,
        )

        Text(
            text = "${
                stringResource(
                    id = R.string.label_x_sets,
                    exercise.sets.size
                )
            } ${exercise.displayName}",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}