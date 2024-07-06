package com.tomtruyen.fitnessapplication.ui.shared.workout

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
import com.tomtruyen.fitnessapplication.Dimens
import com.tomtruyen.fitnessapplication.R
import com.tomtruyen.data.entities.Exercise

@Composable
fun WorkoutExerciseSetHeader(
    exercise: com.tomtruyen.data.entities.Exercise,
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
            text = stringResource(id = R.string.set).uppercase(),
            textAlign = TextAlign.Center,
            modifier = Modifier.width(Dimens.MinButtonHeight),
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.W500,
            )
        )

        if(isExecute) {
            Text(
                text = stringResource(id = R.string.last).uppercase(),
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
            com.tomtruyen.data.entities.Exercise.ExerciseType.WEIGHT -> {
                Text(
                    text = stringResource(id = R.string.reps).uppercase(),
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

            com.tomtruyen.data.entities.Exercise.ExerciseType.TIME -> {
                Text(
                    text = stringResource(id = R.string.time).uppercase(),
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