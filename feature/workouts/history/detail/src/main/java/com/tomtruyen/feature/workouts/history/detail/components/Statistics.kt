package com.tomtruyen.feature.workouts.history.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.tomtruyen.core.common.R
import com.tomtruyen.core.common.models.UnitType
import com.tomtruyen.core.common.utils.TimeUtils
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.designsystem.theme.secondaryTextColor

@Composable
fun Statistics(
    duration: Long,
    volume: Double,
    sets: Int,
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
            value = "$volume ${unit.value}",
        )

        StatisticsItem(
            name = stringResource(id = R.string.label_sets),
            value = sets.toString()
        )

        // TBA -> Records
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