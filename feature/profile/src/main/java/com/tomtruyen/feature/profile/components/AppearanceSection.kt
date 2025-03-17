package com.tomtruyen.feature.profile.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.tomtruyen.core.common.models.GlobalAppState
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.ui.Label
import com.tomtruyen.core.ui.listitems.ListItem
import com.tomtruyen.feature.profile.R

@Composable
fun AppearanceSection(
    onShowThemeSheet: () -> Unit,
) {
    val theme by GlobalAppState.theme

    Label(
        label = stringResource(id = R.string.label_appearance),
        modifier = Modifier.padding(
            start = Dimens.Normal,
            end = Dimens.Normal,
            top = Dimens.Normal,
            bottom = Dimens.Tiny
        )
    )

    ListItem(
        title = stringResource(id = R.string.label_theme_mode),
        message = theme.value,
        onClick = onShowThemeSheet
    )
}