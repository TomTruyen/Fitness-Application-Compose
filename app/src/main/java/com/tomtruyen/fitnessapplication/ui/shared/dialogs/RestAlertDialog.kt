package com.tomtruyen.fitnessapplication.ui.shared.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.tomtruyen.fitnessapplication.R
import com.tomtruyen.fitnessapplication.ui.shared.Buttons
import com.tomtruyen.fitnessapplication.ui.shared.NumberPicker
import java.util.concurrent.TimeUnit

@Composable
fun RestAlertDialog(
    onConfirm: (Int) -> Unit,
    onDismiss: () -> Unit,
    rest: Int,
) {
    var selectedRestValue by remember { mutableIntStateOf(rest) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(id = R.string.rest_timer),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.W500
                )
            )
        },
        text = {
            NumberPicker(
                modifier = Modifier.fillMaxWidth(),
                value = selectedRestValue,
                onValueChange = {
                    selectedRestValue = it
                },
                dividersColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                textStyle = MaterialTheme.typography.bodyLarge,
                label = {
                    // Format the number to be displayed
                    val minutes = TimeUnit.SECONDS.toMinutes(it.toLong())
                    val seconds = it - TimeUnit.MINUTES.toSeconds(minutes)

                    String.format("%d:%02d", minutes, seconds)
                },
                list = (0..300 step 5).toList() // 0 to 300 seconds, step of 5 per
            )
        },
        confirmButton = {
            Buttons.Text(
                text = stringResource(id = android.R.string.ok),
                onClick = {
                    onConfirm(selectedRestValue)
                },
            )
        },
        dismissButton = {
            Buttons.Text(
                text = stringResource(id = android.R.string.cancel),
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    )
}