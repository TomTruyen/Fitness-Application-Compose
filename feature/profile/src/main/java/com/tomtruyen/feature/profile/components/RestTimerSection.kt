package com.tomtruyen.feature.profile.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tomtruyen.core.common.utils.TimeUtils
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.ui.Label
import com.tomtruyen.core.ui.listitems.ListItem
import com.tomtruyen.core.ui.listitems.SwitchListItem
import com.tomtruyen.feature.profile.ProfileUiAction
import com.tomtruyen.feature.profile.R

@Composable
fun RestTimerSection(
    restTime: Int,
    restEnabled: Boolean,
    restVibrateEnabled: Boolean,
    onAction: (ProfileUiAction) -> Unit,
) {
    Label(
        label = stringResource(id = R.string.label_rest_timer),
        modifier = Modifier.padding(
            start = 12.dp,
            end = 12.dp,
            top = Dimens.Normal,
            bottom = Dimens.Tiny
        )
    )

    ListItem(
        title = stringResource(id = R.string.label_default_rest_timer),
        message = TimeUtils.formatSeconds(restTime.toLong()),
        onClick = {
            onAction(ProfileUiAction.Sheet.RestTime.Show)
        }
    )

    SwitchListItem(
        title = stringResource(id = R.string.label_rest_timer_enabled),
        checked = restEnabled
    ) {
        onAction(ProfileUiAction.OnRestEnabledChanged(it))
    }

    SwitchListItem(
        title = stringResource(id = R.string.label_vibrate_upon_finish),
        checked = restVibrateEnabled
    ) {
        onAction(ProfileUiAction.OnRestVibrationEnabledChanged(it))
    }
}