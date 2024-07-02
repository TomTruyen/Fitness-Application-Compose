package com.tomtruyen.fitnessapplication.ui.shared.dialogs

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.tomtruyen.fitnessapplication.R
import com.tomtruyen.fitnessapplication.data.entities.Exercise
import com.tomtruyen.fitnessapplication.data.entities.Settings
import com.tomtruyen.fitnessapplication.ui.shared.Buttons
import com.tomtruyen.fitnessapplication.ui.shared.listitems.RadioListItem

@Composable
fun UnitAlertDialog(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
    unit: String,
) {
    var selectedUnit by remember { mutableStateOf(unit) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(id = R.string.weight_unit),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.W500
                )
            )
        },
        text = {
            Column {
                Settings.UnitType.entries.forEach { unit ->
                    RadioListItem(
                        title = unit.value,
                        selected = selectedUnit == unit.value,
                        onCheckedChange = {
                            selectedUnit = unit.value
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