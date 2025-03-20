package com.tomtruyen.feature.workouts.history.detail.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.tomtruyen.core.common.extensions.toFormat
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.ui.Label
import kotlinx.datetime.LocalDateTime

@Composable
fun Header(
    workoutName: String,
    date: LocalDateTime,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth(1f)
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
            label = date.toFormat("EEEE, d MMMM yyyy"),
            style = MaterialTheme.typography.labelMedium
        )
    }
}