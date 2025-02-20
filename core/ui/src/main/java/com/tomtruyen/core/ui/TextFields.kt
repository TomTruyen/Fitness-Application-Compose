package com.tomtruyen.core.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.designsystem.theme.LavenderMist


object TextFields {
    @Composable
    fun Default(
        modifier: Modifier = Modifier,
        value: String?,
        onValueChange: (String) -> Unit,
        placeholder: String = "",
        error: String? = null,
        obscureText: Boolean = false,
        readOnly: Boolean = false,
        enabled: Boolean = true,
        singleLine: Boolean = true,
        border: Boolean = true,
        padding: PaddingValues = PaddingValues(Dimens.Normal),
        textStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyMedium,
        trailingIcon: (@Composable () -> Unit)? = null,
        keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
        containerColor: Color = Color.White,
        shape: Shape = MaterialTheme.shapes.small,
    ) {
        val focusManager = LocalFocusManager.current

        var obscureTextVisible by rememberSaveable { mutableStateOf(false) }

        val trailing = remember(obscureText) {
            if(obscureText) {
                {
                    PasswordEyeToggle(
                        obscureTextVisible = obscureTextVisible,
                        onToggle = {
                            obscureTextVisible = !obscureTextVisible
                        }
                    )
                }
            } else {
                trailingIcon
            }
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
                    .then(
                        if (border) {
                            Modifier.border(
                                width = 1.dp,
                                color = if(error.isNullOrBlank()) {
                                    LavenderMist
                                } else {
                                    MaterialTheme.colorScheme.error
                                },
                                shape = shape
                            )
                        } else {
                            Modifier
                        }
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
                        TextFieldPlaceholder(
                            value = value,
                            placeholder = placeholder,
                            textStyle = textStyle
                        )

                        innerTextField.invoke()
                    }

                    trailing?.let {
                        Box(
                            modifier = Modifier.size(20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            it.invoke()
                        }
                    }

                }
            }

            ErrorLabel(message = error)
        }
    }
}