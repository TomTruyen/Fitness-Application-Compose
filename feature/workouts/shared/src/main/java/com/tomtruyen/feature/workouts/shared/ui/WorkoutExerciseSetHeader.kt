package com.tomtruyen.feature.workouts.shared.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.data.entities.Exercise
import com.tomtruyen.feature.workouts.shared.R

@Composable
fun WorkoutExerciseSetHeader(
    exercise: Exercise,
    hasMultipleSets: Boolean,
    isExecute: Boolean = false,
    unit: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = Dimens.Tiny),
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

        if(isExecute) {
            Text(
                text = stringResource(id = R.string.label_last).uppercase(),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
                    .animateContentSize(),
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.W500,
                )
            )

            Spacer(modifier = Modifier.width(Dimens.Small))
        }

        when(exercise.typeEnum) {
            Exercise.ExerciseType.WEIGHT -> {
                Text(
                    text = stringResource(id = R.string.label_reps).uppercase(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f)
                        .animateContentSize(),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.W500,
                    )
                )

                Spacer(modifier = Modifier.width(Dimens.Small))

                Text(
                    text = unit.uppercase(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f)
                        .animateContentSize(),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.W500,
                    )
                )
            }

            Exercise.ExerciseType.TIME -> {
                Text(
                    text = stringResource(id = R.string.label_time).uppercase(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f)
                        .animateContentSize(),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.W500,
                    )
                )
            }
        }


        AnimatedVisibility(visible = hasMultipleSets && !isExecute) {
            Spacer(modifier = Modifier.width(Dimens.MinButtonHeight))
        }
    }
}