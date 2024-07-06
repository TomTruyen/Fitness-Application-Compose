package com.tomtruyen.core.common.base

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

sealed class SnackbarMessage(
    open val message: String? = null,
    open val icon: ImageVector? = null,
    open val backgroundColor: Color? = null,
) {
    data object Empty : SnackbarMessage()
    class Success(
        override val message: String?,
        override val icon: ImageVector? = Icons.Filled.Check,
        override val backgroundColor: Color? = Color.Green,
    ) : SnackbarMessage(message, icon, backgroundColor)

    class Error(
        override val message: String?,
        override val icon: ImageVector? = Icons.Filled.ErrorOutline,
        override val backgroundColor: Color? = Color(0xFFFF5555),
    ) : SnackbarMessage(message, icon, backgroundColor)
}