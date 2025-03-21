package com.tomtruyen.core.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tomtruyen.core.designsystem.Dimens
import kotlinx.coroutines.launch


object TextFields {
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun Default(
        modifier: Modifier = Modifier,
        textFieldModifier: Modifier = Modifier,
        value: String?,
        onValueChange: (String) -> Unit,
        placeholder: String = "",
        error: String? = null,
        obscureText: Boolean = false,
        readOnly: Boolean = false,
        enabled: Boolean = true,
        singleLine: Boolean = true,
        withLabel: Boolean = false,
        onClick: (() -> Unit)? = null,
        padding: PaddingValues = PaddingValues(Dimens.Normal),
        textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
        focusRequester: FocusRequester = remember { FocusRequester() },
        trailingIcon: (@Composable () -> Unit)? = null,
        keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
        containerColor: Color = MaterialTheme.colorScheme.surface,
        shape: Shape = MaterialTheme.shapes.small,
    ) {
        val focusManager = LocalFocusManager.current
        val bringIntoViewRequester = remember { BringIntoViewRequester() }
        val coroutineScope = rememberCoroutineScope()

        var obscureTextVisible by rememberSaveable { mutableStateOf(false) }

        val trailing = remember(obscureText) {
            if (obscureText) {
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

        Column(
            modifier = modifier.animateContentSize(),
            verticalArrangement = Arrangement.spacedBy(Dimens.Tiny)
        ) {
            if (withLabel) {
                Label(
                    modifier = Modifier.padding(start = Dimens.Tiny),
                    label = placeholder
                )
            }

            BasicTextField(
                value = value.orEmpty(),
                onValueChange = onValueChange,
                readOnly = readOnly,
                enabled = enabled,
                singleLine = singleLine,
                visualTransformation = if (!obscureText || obscureTextVisible) VisualTransformation.None else PasswordVisualTransformation(),
                textStyle = textStyle,
                cursorBrush = SolidColor(textStyle.color),
                keyboardOptions = keyboardOptions,
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                modifier = textFieldModifier
                    .fillMaxWidth()
                    .clip(shape)
                    .clickable(
                        enabled = onClick != null
                    ) {
                        onClick?.invoke()
                    }
                    .focusRequester(focusRequester)
                    .bringIntoViewRequester(bringIntoViewRequester)
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            coroutineScope.launch {
                                bringIntoViewRequester.bringIntoView()
                            }
                        }
                    }
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
                        modifier = Modifier
                            .weight(1f)
                            .animateContentSize(),
                        contentAlignment = if (textStyle.textAlign == TextAlign.Center) {
                            Alignment.TopCenter
                        } else {
                            Alignment.TopStart
                        }
                    ) {
                        TextFieldPlaceholder(
                            value = value,
                            placeholder = placeholder,
                            textStyle = MaterialTheme.typography.labelLarge.copy(
                                textAlign = textStyle.textAlign
                            )
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