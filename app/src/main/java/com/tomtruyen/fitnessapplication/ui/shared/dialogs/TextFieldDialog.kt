package com.tomtruyen.fitnessapplication.ui.shared.dialogs

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.tomtruyen.fitnessapplication.Dimens
import com.tomtruyen.fitnessapplication.R
import com.tomtruyen.fitnessapplication.ui.shared.Buttons
import com.tomtruyen.fitnessapplication.ui.shared.TextFields
import com.tomtruyen.core.validation.TextValidator
import com.tomtruyen.core.validation.ValidationResult
import com.tomtruyen.core.validation.errorMessage
import com.tomtruyen.core.validation.rules.RequiredRule

@Composable
fun TextFieldDialog(
    @StringRes title: Int,
    @StringRes message: Int? = null,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
    @StringRes confirmText: Int,
    @StringRes dismissText: Int = android.R.string.cancel,
    confirmButtonColors: ButtonColors = ButtonDefaults.textButtonColors(
        contentColor = MaterialTheme.colorScheme.primary
    ),
    dismissButtonColors: ButtonColors = ButtonDefaults.textButtonColors(
        contentColor = MaterialTheme.colorScheme.onSurface
    ),
    validator: TextValidator = TextValidator.withRules(
        RequiredRule()
    ),
) {
    val context = LocalContext.current

    var text: String? by remember { mutableStateOf(null) }
    var textValidation: ValidationResult? by remember { mutableStateOf(null) }

    LaunchedEffect(text) {
        if(text != null) {
            textValidation = validator.validate(text)
        }
    }

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
            Column {
                if (message != null) {
                    Text(text = stringResource(message))
                }

                TextFields.Default(
                    value = text,
                    onValueChange = {
                        text = it
                    },
                    placeholder = stringResource(R.string.title_workout_name),
                    error = textValidation.errorMessage(),
                    modifier = Modifier.padding(top = Dimens.Small)
                )
            }
        },
        confirmButton = {
            Buttons.Text(
                text = stringResource(confirmText),
                onClick = {
                    text?.let { onConfirm(it) }
                },
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
