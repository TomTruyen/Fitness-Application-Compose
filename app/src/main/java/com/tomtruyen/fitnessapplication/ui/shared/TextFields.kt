package com.tomtruyen.fitnessapplication.ui.shared

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import com.tomtruyen.fitnessapplication.Dimens
import com.tomtruyen.fitnessapplication.R
import com.tomtruyen.fitnessapplication.ui.theme.ChineseWhite


object TextFields {
    @Composable
    fun Default(
        value: String?,
        onValueChange: (String) -> Unit,
        placeholder: String = "",
        error: String? = null,
        obscureText: Boolean = false,
        search: Boolean = false,
        keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
        colors: TextFieldColors = TextFieldDefaults.colors(
            focusedContainerColor = ChineseWhite,
            unfocusedContainerColor = ChineseWhite,
            errorContainerColor = ChineseWhite,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            errorTextColor = MaterialTheme.colorScheme.onSurface,
        ),
        shape: Shape = MaterialTheme.shapes.medium,
        modifier: Modifier = Modifier
    ) {
        val focusManager = LocalFocusManager.current

        val textStyle = MaterialTheme.typography.bodyMedium

        var obscureTextVisible by rememberSaveable { mutableStateOf(false) }

        Column(modifier = modifier) {
            TextField(
                value = value ?: "",
                onValueChange = onValueChange,
                visualTransformation = if (!obscureText || obscureTextVisible) VisualTransformation.None else PasswordVisualTransformation(),
                textStyle = textStyle,
                placeholder = {
                    Text(
                        text = placeholder,
                        style = textStyle.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    )
                },
                isError = !error.isNullOrEmpty(),
                keyboardOptions = keyboardOptions,
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                singleLine = true,
                colors = colors,
                shape = shape,
                modifier = if(!error.isNullOrEmpty()) {
                    Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.error,
                            shape = shape
                        )
                } else {
                    Modifier.fillMaxWidth()
                },
                trailingIcon = {
                    if (obscureText) {
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
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }

                    if (search && !value.isNullOrBlank()) {
                        IconButton(onClick = { onValueChange("") }) {
                            Icon(
                                imageVector = Icons.Filled.Clear,
                                contentDescription = stringResource(id = R.string.content_description_clear),
                            )
                        }
                    }
                }
            )

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