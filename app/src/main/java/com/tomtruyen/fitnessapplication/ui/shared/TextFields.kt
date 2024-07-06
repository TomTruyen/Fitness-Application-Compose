package com.tomtruyen.fitnessapplication.ui.shared

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tomtruyen.fitnessapplication.Dimens
import com.tomtruyen.fitnessapplication.R
import com.tomtruyen.core.designsystem.theme.BlueGrey
import com.tomtruyen.core.designsystem.theme.LavenderMist


object TextFields {
    @Composable
    fun Default(
        value: String?,
        onValueChange: (String) -> Unit,
        placeholder: String = "",
        error: String? = null,
        obscureText: Boolean = false,
        readOnly: Boolean = false,
        enabled: Boolean = true,
        singleLine: Boolean = true,
        padding: PaddingValues = PaddingValues(Dimens.Normal),
        textStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyMedium,
        trailingIcon: @Composable (() -> Unit)? = null,
        keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
        containerColor: Color = Color.White,
        shape: Shape = MaterialTheme.shapes.small,
        modifier: Modifier = Modifier
    ) {
        val focusManager = LocalFocusManager.current

        var obscureTextVisible by rememberSaveable { mutableStateOf(false) }

        val trailing = if(obscureText) {
            {
                IconButton(
                    onClick = { obscureTextVisible = !obscureTextVisible },
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
                        tint = BlueGrey
                    )
                }
            }
        } else {
            trailingIcon
        }

        Column(modifier = modifier) {
            BasicTextField(
                value = value.orEmpty(),
                onValueChange = onValueChange,
                readOnly = readOnly,
                enabled = enabled,
                singleLine = singleLine,
                visualTransformation = if (!obscureText || obscureTextVisible) VisualTransformation.None else PasswordVisualTransformation(),
                textStyle = textStyle,
                keyboardOptions = keyboardOptions,
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = if(error.isNullOrBlank()) {
                            LavenderMist
                        } else {
                            MaterialTheme.colorScheme.error
                        },
                        shape = shape
                    )
                    .animateContentSize(),
            ) { innerTextField ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = containerColor,
                            shape = shape
                        )
                        .padding(padding)
                ) {
                    Box(
                        modifier = Modifier.weight(1f)
                            .animateContentSize(),
                        contentAlignment = if(textStyle.textAlign == TextAlign.Center) {
                            Alignment.Center
                        } else {
                            Alignment.CenterStart
                        }
                    ) {
                        if(value.isNullOrBlank()) {
                            Text(
                                text = placeholder,
                                style = textStyle.copy(
                                    color = BlueGrey
                                ),
                                textAlign = textStyle.textAlign,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        innerTextField.invoke()
                    }

                    if(trailing != null) {
                        Box(
                            modifier = Modifier.size(20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            trailing.invoke()
                        }
                    }
                }
            }

            if(!error.isNullOrEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(top = Dimens.Tiny),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.ErrorOutline,
                        tint = MaterialTheme.colorScheme.error,
                        contentDescription = null
                    )
                    Text(
                        text = error,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(start = Dimens.Tiny)
                    )
                }
            }
        }
    }
}