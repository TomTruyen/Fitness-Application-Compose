package com.tomtruyen.fitnessapplication.base

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.tomtruyen.fitnessapplication.R

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
        override val backgroundColor: Color? = Color.Red,
    ) : SnackbarMessage(message, icon, backgroundColor)
}