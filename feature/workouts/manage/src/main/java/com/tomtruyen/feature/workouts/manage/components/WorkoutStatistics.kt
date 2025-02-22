package com.tomtruyen.feature.workouts.manage.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.data.firebase.extensions.repsCount
import com.tomtruyen.data.firebase.extensions.setCount
import com.tomtruyen.data.firebase.extensions.totalVolume
import com.tomtruyen.data.firebase.models.WorkoutResponse
import com.tomtruyen.feature.workouts.manage.R

@Composable
fun WorkoutStatistics(
    workout: WorkoutResponse,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = Dimens.Small)
                .fillMaxWidth()
                .padding(Dimens.Normal),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            WorkoutStatisticsItem(
                name = stringResource(id = R.string.label_volume),
                value = "${workout.totalVolume()} ${workout.unit}"
            )

            WorkoutStatisticsItem(
                name = stringResource(id = R.string.label_sets),
                value = workout.setCount().toString()
            )

            WorkoutStatisticsItem(
                name = stringResource(id = R.string.label_reps),
                value = workout.repsCount().toString()
            )
        }

        HorizontalDivider()
    }
}

@Composable
private fun RowScope.WorkoutStatisticsItem(
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
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.typography.bodyMedium.color.copy(alpha = 0.5f)
            ),
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}