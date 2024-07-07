package com.tomtruyen.core.ui.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.tomtruyen.core.ui.Buttons
import com.tomtruyen.core.ui.R
import com.tomtruyen.core.ui.listitems.RadioListItem

@Composable
fun UnitAlertDialog(
    units: List<String>,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
    unit: String,
) {
    var selectedUnit by remember { mutableStateOf(unit) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(id = R.string.label_weight_unit),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.W500
                )
            )
        },
        text = {
            Column {
                units.forEach { unit ->
                    RadioListItem(
                        title = unit,
                        selected = selectedUnit == unit,
                        onCheckedChange = {
                            selectedUnit = unit
                        }
                    )
                }
            }
        },
        confirmButton = {
            Buttons.Text(
                text = stringResource(id = android.R.string.ok),
                onClick = {
                    onConfirm(selectedUnit)
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