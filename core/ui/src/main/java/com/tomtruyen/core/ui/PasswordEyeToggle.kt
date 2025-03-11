package com.tomtruyen.core.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.tomtruyen.core.designsystem.theme.textFieldIcon

@Composable
fun PasswordEyeToggle(
    obscureTextVisible: Boolean,
    onToggle: () -> Unit
) {
    IconButton(
        onClick = onToggle,
    ) {
        val icon =
            if (obscureTextVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
        val contentDescription = if (obscureTextVisible) {
            stringResource(id = R.string.content_description_hide_password)
        } else {
            stringResource(id = R.string.content_description_show_password)
        }

        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.textFieldIcon.value
        )
    }
}