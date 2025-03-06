package com.tomtruyen.core.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import com.tomtruyen.core.common.models.FilterOption
import com.tomtruyen.core.designsystem.Dimens

@Composable
fun FilterSelectSheet(
    placeholder: String,
    options: List<FilterOption>,
    selectedOption: FilterOption,
    error: String? = null,
    onOptionSelected: (FilterOption) -> Unit
) {
    val rememberedOptions: List<String> = remember(options) {
        options.map { it.name }
    }

    SelectSheet(
        placeholder = placeholder,
        options = rememberedOptions,
        selectedOption = selectedOption.name,
        error = error,
        onOptionSelected = { option ->
            onOptionSelected(options.first { it.name == option })
        }
    )
}

@Composable
fun SelectSheet(
    placeholder: String,
    options: List<String>,
    selectedOption: String,
    error: String? = null,
    onOptionSelected: (String) -> Unit
) {
    var visible by remember { mutableStateOf(false) }

    val trailingRotation by animateFloatAsState(
        targetValue = if (visible) 180f else 0f,
        animationSpec = tween(200)
    )

    val items = remember(options) {
        options.map { option ->
            BottomSheetItem(
                title = option,
                onClick = {
                    onOptionSelected(option)
                }
            )
        }
    }

    val selectedIndex by remember(options, selectedOption) {
        mutableIntStateOf(options.indexOfFirst { it == selectedOption })
    }

    TextFields.Default(
        error = error,
        readOnly = true,
        enabled = false,
        withLabel = true,
        placeholder = placeholder,
        value = selectedOption.lowercase().replaceFirstChar { it.uppercase() },
        onValueChange = { },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier.rotate(trailingRotation)
            )
        },
        onClick = {
            visible = true
        },
        padding = PaddingValues(Dimens.Normal),
    )

    BottomSheetList(
        items = items,
        visible = visible,
        selectedIndex = selectedIndex,
        onDismiss = {
            visible = false
        }
    )
}