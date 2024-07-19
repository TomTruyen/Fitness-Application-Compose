package com.tomtruyen.core.ui

import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun Chip(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    trailingIcon: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    FilterChip(
        modifier = modifier,
        selected = selected,
        onClick = {
            onClick?.invoke()
        },
        trailingIcon = trailingIcon,
        label = {
            Text(text = text)
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.tertiary,
            selectedLabelColor = MaterialTheme.colorScheme.onTertiary,
        )
    )
}