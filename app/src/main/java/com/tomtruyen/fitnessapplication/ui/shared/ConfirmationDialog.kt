package com.tomtruyen.fitnessapplication.ui.shared

import androidx.annotation.StringRes
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.tomtruyen.fitnessapplication.R

@Composable
fun ConfirmationDialog(
    @StringRes title: Int,
    @StringRes message: Int? = null,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    @StringRes confirmText: Int,
    @StringRes dismissText: Int = R.string.cancel,
    confirmButtonColors: ButtonColors = ButtonDefaults.textButtonColors(),
    dismissButtonColors: ButtonColors = ButtonDefaults.textButtonColors(
        contentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    )
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(title),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.W500
                )
            )
        },
        text = {
            if (message != null) {
                Text(text = stringResource(message))
            }
        },
        confirmButton = {
            Buttons.Text(
                text = stringResource(confirmText),
                onClick = onConfirm,
                colors = confirmButtonColors
            )
        },
        dismissButton = {
            Buttons.Text(
                text = stringResource(dismissText),
                onClick = onDismiss,
                colors = dismissButtonColors
            )
        }
    )
}
