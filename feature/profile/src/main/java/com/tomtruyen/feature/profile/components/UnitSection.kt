package com.tomtruyen.feature.profile.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.tomtruyen.core.common.models.UnitType
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.ui.Label
import com.tomtruyen.core.ui.listitems.ListItem
import com.tomtruyen.feature.profile.R

@Composable
fun UnitSection(
    unit: UnitType,
    onShowUnitSheet: () -> Unit,
) {
    Label(
        label = stringResource(id = R.string.label_units),
        modifier = Modifier.padding(
            start = Dimens.Normal,
            end = Dimens.Normal,
            bottom = Dimens.Tiny
        )
    )

    ListItem(
        title = stringResource(id = R.string.label_weight_unit),
        message = unit.value,
        onClick = onShowUnitSheet
    )
}