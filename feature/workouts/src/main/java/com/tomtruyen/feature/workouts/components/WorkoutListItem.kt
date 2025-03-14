package com.tomtruyen.feature.workouts.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.ui.Buttons
import com.tomtruyen.data.models.ui.WorkoutUiModel
import com.tomtruyen.feature.workouts.R
import com.tomtruyen.feature.workouts.WorkoutsUiAction

@Composable
fun WorkoutListItem(
    workout: WorkoutUiModel,
    onAction: (WorkoutsUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(0.dp),
        onClick = {
            onAction(WorkoutsUiAction.OnDetailClicked(workout.id))
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Name + Expand Icon
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = Dimens.Normal),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = workout.name,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.W500
                    ),
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = {
                        onAction(WorkoutsUiAction.Sheet.Show(workout.id))
                    }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.MoreVert,
                        contentDescription = null
                    )
                }
            }

            // Exercises
            Column(
                modifier = Modifier.padding(Dimens.Normal),
                verticalArrangement = Arrangement.spacedBy(Dimens.Small)
            ) {
                Text(
                    text = workout.exercises.joinToString { it.displayName },
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Buttons.Default(
                    text = stringResource(id = R.string.button_start_workout),
                    minButtonSize = 0.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    onAction(WorkoutsUiAction.Execute(workout.id))
                }
            }
        }
    }
}