package com.tomtruyen.feature.workouts.manage.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.designsystem.theme.FynixTheme
import com.tomtruyen.core.designsystem.theme.rememberColorPalette

@Composable
fun WorkoutCheckbox(
    checked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    iconSize: Dp = 16.dp
) {
    val colorPalette by rememberColorPalette()
    val focusManager = LocalFocusManager.current

    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(Dimens.Small))
            .background(
                color = if (checked) {
                    colorPalette.Green
                } else {
                    colorPalette.Surface1
                },
            )
            .clickable {
                focusManager.clearFocus()
                onClick()
            },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            modifier = Modifier.size(iconSize),
            imageVector = Icons.Rounded.Check,
            contentDescription = null,
            tint = if(checked) {
                colorPalette.Base
            } else {
                colorPalette.Text
            },
        )
    }
}

@Preview
@Composable
fun WorkoutCheckboxPreview() {
    FynixTheme {
        WorkoutCheckbox(
            checked = false,
            onClick = {}
        )
    }
}