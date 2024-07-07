package com.tomtruyen.feature.workouts.history.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tomtruyen.core.common.extensions.format
import com.tomtruyen.core.common.utils.TimeUtils
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.data.entities.WorkoutHistoryWithWorkout
import com.tomtruyen.feature.workouts.history.WorkoutHistoryUiAction

@Composable
fun WorkoutHistoryItem(
    entry: WorkoutHistoryWithWorkout,
    onAction: (WorkoutHistoryUiAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .padding(
                horizontal = Dimens.Normal,
                vertical = Dimens.Small
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(0.dp),
        onClick = {
            onAction(WorkoutHistoryUiAction.OnDetailClicked(entry.history.id))
        }
    ) {
        // Just basic information like total weight, time
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(Dimens.Small)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = entry.workoutWithExercises.workout.name,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.W500
                    ),
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(Dimens.Small))

                Icon(
                    imageVector = Icons.Outlined.Timer,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    modifier = Modifier.size(Dimens.Normal)
                )

                Text(
                    text = entry.history.formattedDuration,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier.padding(start = Dimens.Tiny)
                )
            }

            Text(
                text = TimeUtils.formatDate(
                    dateMillis = entry.history.createdAt
                ),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            if(entry.totalWeight > 0) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.FitnessCenter,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        modifier = Modifier
                            .size(Dimens.Normal)
                            .rotate(-45f)
                    )

                    Text(
                        text = "${entry.totalWeight.format()} ${entry.weightUnit}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        ),
                        modifier = Modifier.weight(1f)
                            .padding(start = Dimens.Tiny)
                    )
                }
            }
        }
    }
}