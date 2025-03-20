package com.tomtruyen.core.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
fun PasswordEyeToggle(
    obscureTextVisible: Boolean,
    onToggle: () -> Unit
) {
    IconButton(
        onClick = onToggle,
    ) {
        val icon =
            if (obscureTextVisible) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff
        val contentDescription = if (obscureTextVisible) {
            stringResource(id = R.string.content_description_hide_password)
        } else {
            stringResource(id = R.string.content_description_show_password)
        }

        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
        )
    }
}