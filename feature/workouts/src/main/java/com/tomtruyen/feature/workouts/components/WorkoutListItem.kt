package com.tomtruyen.feature.workouts.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.ui.Buttons
import com.tomtruyen.feature.workouts.R
import com.tomtruyen.feature.workouts.WorkoutsUiAction

@Composable
fun WorkoutListItem(
    workoutWithExercises: com.tomtruyen.data.entities.WorkoutWithExercises,
    onAction: (WorkoutsUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    Card(
        modifier = modifier
            .padding(
                horizontal = Dimens.Normal,
                vertical = Dimens.Small
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(0.dp),
        onClick = {
            onAction(WorkoutsUiAction.OnDetailClicked(workoutWithExercises.workout.id))
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
                    text = workoutWithExercises.workout.name,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.W500
                    ),
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = {
                        expanded = !expanded
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.rotate(
                            if(expanded) 180f else 0f
                        )
                    )
                }
            }

            // Exercises
            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier.padding(
                        start = Dimens.Normal,
                        end = Dimens.Normal,
                        bottom = Dimens.Normal,
                        top = Dimens.Small,
                    )
                ) {
                    workoutWithExercises.exercises
                        .sortedBy { it.workoutExercise.sortOrder }
                        .forEach { exerciseWithSets ->
                            Text(
                                text = "${exerciseWithSets.sets.size} x ${exerciseWithSets.exercise.displayName}",
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }

                    Buttons.Default(
                        text = stringResource(id = R.string.button_start_workout),
                        minButtonSize = 0.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = Dimens.Normal)
                    ) {
                        onAction(WorkoutsUiAction.OnStartWorkoutClicked(workoutWithExercises.workout.id))
                    }
                }
            }
        }
    }
}