package com.tomtruyen.feature.profile.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.designsystem.theme.datastore.ThemePreferencesDatastore
import com.tomtruyen.core.ui.Label
import com.tomtruyen.core.ui.listitems.ListItem
import com.tomtruyen.feature.profile.R

@Composable
fun AppearanceSection(
    onShowThemeSheet: () -> Unit,
) {
    val themeMode by ThemePreferencesDatastore.themeMode.collectAsState(ThemePreferencesDatastore.Mode.SYSTEM)

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
        message = themeMode.value,
        onClick = onShowThemeSheet
    )
}