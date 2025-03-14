package com.tomtruyen.feature.workouts.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.ui.Buttons
import com.tomtruyen.core.ui.modifiers.BorderSide
import com.tomtruyen.core.ui.modifiers.directionalBorder
import com.tomtruyen.feature.workouts.R
import com.tomtruyen.feature.workouts.WorkoutsUiAction

@Composable
fun ActiveWorkoutBar(
    hasActiveWorkout: Boolean,
    onAction: (WorkoutsUiAction) -> Unit,
) {
    AnimatedVisibility(
        visible = hasActiveWorkout,
        enter = fadeIn() + slideInVertically(
            initialOffsetY = { it }
        ),
        exit = fadeOut() + slideOutVertically(
            targetOffsetY = { it }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .directionalBorder(
                    side = BorderSide.TOP
                )
                .padding(
                    start = Dimens.Normal,
                    end = Dimens.Normal,
                    top = Dimens.Small
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Buttons.Text(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.button_resume),
                icon = Icons.Rounded.PlayArrow,
                onClick = {
                    onAction(WorkoutsUiAction.ActiveWorkout.Resume)
                }
            )

            Buttons.Text(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.button_discard),
                icon = Icons.Rounded.Close,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                ),
                onClick = {
                    onAction(WorkoutsUiAction.Dialog.Discard.Show)
                }
            )
        }
    }
}