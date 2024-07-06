package com.tomtruyen.fitnessapplication.ui.shared.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.tomtruyen.fitnessapplication.Dimens
import com.tomtruyen.fitnessapplication.R
import com.tomtruyen.models.RestAlertType
import com.tomtruyen.fitnessapplication.ui.shared.Buttons
import com.tomtruyen.fitnessapplication.ui.shared.numberpickers.NumberPicker
import com.tomtruyen.fitnessapplication.ui.shared.listitems.SwitchListItem
import com.tomtruyen.fitnessapplication.ui.shared.numberpickers.MinutesAndSecondsPicker
import java.util.concurrent.TimeUnit

@Composable
fun RestAlertDialog(
    onConfirm: (Int, Boolean?) -> Unit,
    onDismiss: () -> Unit,
    rest: Int,
    restEnabled: Boolean? = null,
    type: com.tomtruyen.models.RestAlertType = com.tomtruyen.models.RestAlertType.REST_TIME
) {
    var selectedRestValue by remember { mutableIntStateOf(rest) }
    var selectedRestEnabled by remember { mutableStateOf(restEnabled) }

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
            Column(
                verticalArrangement = Arrangement.spacedBy(Dimens.Normal)
            ) {
                if(restEnabled != null) {
                    SwitchListItem(
                        title = stringResource(id = R.string.rest_timer_enabled),
                        checked = selectedRestEnabled ?: false,
                    ) {
                        selectedRestEnabled = it
                    }
                }

                when(type) {
                    com.tomtruyen.models.RestAlertType.REST_TIME -> RestTimeLayout(
                        value = selectedRestValue,
                        onValueChange = { rest ->
                            if(selectedRestEnabled == false) return@RestTimeLayout
                            selectedRestValue = rest
                        },
                        enabled = selectedRestEnabled ?: true
                    )

                    com.tomtruyen.models.RestAlertType.SET_TIME -> SetTimeLayout(
                        value = selectedRestValue,
                        onValueChange = { rest ->
                            if(selectedRestEnabled == false) return@SetTimeLayout
                            selectedRestValue = rest
                        },
                    )
                }
            }
        },
        confirmButton = {
            Buttons.Text(
                text = stringResource(id = android.R.string.ok),
                onClick = {
                    onConfirm(selectedRestValue, selectedRestEnabled)
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

@Composable
fun RestTimeLayout(
    value: Int,
    onValueChange: (Int) -> Unit,
    enabled: Boolean,
) {
    NumberPicker(
        enabled = enabled,
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
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
}

@Composable
fun SetTimeLayout(
    value: Int,
    onValueChange: (Int) -> Unit,
) {
    MinutesAndSecondsPicker(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        dividersColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
        textStyle = MaterialTheme.typography.bodyLarge,
    )
}