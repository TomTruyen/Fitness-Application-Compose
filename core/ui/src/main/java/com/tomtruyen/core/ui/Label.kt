package com.tomtruyen.core.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.designsystem.theme.secondaryTextColor

@Composable
fun Label(
    label: String
) {
    Text(
        text = label,
        style = MaterialTheme.typography.labelMedium.copy(
            color = MaterialTheme.colorScheme.secondaryTextColor
        ),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = Dimens.Tiny)
    )
}