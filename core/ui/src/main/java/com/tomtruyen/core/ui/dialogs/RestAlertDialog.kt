package com.tomtruyen.core.ui.dialogs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.text.intl.Locale
import com.tomtruyen.core.common.models.RestAlertType
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.ui.Buttons
import com.tomtruyen.core.ui.NumberPicker
import com.tomtruyen.core.ui.R
import com.tomtruyen.core.ui.RestTimePicker
import com.tomtruyen.core.ui.listitems.SwitchListItem
import java.util.concurrent.TimeUnit

@Composable
fun RestAlertDialog(
    onConfirm: (Int, Boolean?) -> Unit,
    onDismiss: () -> Unit,
    rest: Int,
    restEnabled: Boolean? = null,
    type: RestAlertType = RestAlertType.REST_TIME
) {
    var selectedRestValue by remember { mutableIntStateOf(rest) }
    var selectedRestEnabled by remember { mutableStateOf(restEnabled) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(id = R.string.title_rest_timer),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.W500
                )
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(Dimens.Normal)
            ) {
                AnimatedVisibility(
                    visible = restEnabled != null,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    SwitchListItem(
                        title = stringResource(id = R.string.label_rest_timer_enabled),
                        checked = selectedRestEnabled ?: false,
                    ) {
                        selectedRestEnabled = it
                    }
                }

                when (type) {
                    RestAlertType.REST_TIME -> RestTimeLayout(
                        value = selectedRestValue,
                        onValueChange = { rest ->
                            if (selectedRestEnabled == false) return@RestTimeLayout
                            selectedRestValue = rest
                        },
                        enabled = selectedRestEnabled ?: true
                    )

                    RestAlertType.SET_TIME -> SetTimeLayout(
                        value = selectedRestValue,
                        onValueChange = { rest ->
                            if (selectedRestEnabled == false) return@SetTimeLayout
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
        },
        containerColor = MaterialTheme.colorScheme.background,
        iconContentColor = MaterialTheme.colorScheme.onBackground,
        textContentColor = MaterialTheme.colorScheme.onBackground,
        titleContentColor = MaterialTheme.colorScheme.onBackground
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

            String.format(Locale.current.platformLocale, "%d:%02d", minutes, seconds)
        },
        list = (0..300 step 5).toList() // 0 to 300 seconds, step of 5 per
    )
}

@Composable
fun SetTimeLayout(
    value: Int,
    onValueChange: (Int) -> Unit,
) {
    RestTimePicker(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        dividersColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
        textStyle = MaterialTheme.typography.bodyLarge,
    )
}