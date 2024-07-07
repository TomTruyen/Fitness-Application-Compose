package com.tomtruyen.feature.workouts.create.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tomtruyen.core.common.utils.TimeUtils
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.data.firebase.models.WorkoutExerciseResponse

@Composable
fun RestTimeSelector(
    modifier: Modifier = Modifier,
    workoutExercise: WorkoutExerciseResponse,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .wrapContentWidth()
            .padding(vertical = Dimens.Normal)
            .defaultMinSize(
                minWidth = 100.dp
            )
            .animateContentSize()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.small
            )
            .clip(MaterialTheme.shapes.small)
            .clickable {
                onClick()
            }
            .padding(Dimens.Normal)
            .alpha(if (workoutExercise.restEnabled) 1f else 0.5f),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.Timer,
            contentDescription = null,
        )

        Text(
            text = TimeUtils.formatSeconds(workoutExercise.rest.toLong()),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(start = Dimens.Small)
        )
    }
}
