package com.tomtruyen.feature.workouts.manage.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tomtruyen.core.common.models.ExerciseType
import com.tomtruyen.core.common.models.ManageWorkoutMode
import com.tomtruyen.core.common.models.UnitType
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.feature.workouts.manage.R

@Composable
fun WorkoutExerciseSetHeader(
    exerciseType: ExerciseType,
    mode: ManageWorkoutMode,
    unit: UnitType,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
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

        if (mode.isExecute) {
            Text(
                text = stringResource(id = R.string.label_previous).uppercase(),
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.W500,
                )
            )

            Spacer(modifier = Modifier.width(Dimens.Small))
        }

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


        if (mode.isExecute) {
            Box(
                modifier = Modifier.width(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = Icons.Rounded.Check,
                    contentDescription = null,
                )
            }
        }
    }
}