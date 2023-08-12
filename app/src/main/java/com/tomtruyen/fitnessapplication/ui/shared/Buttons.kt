package com.tomtruyen.fitnessapplication.ui.shared

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.tomtruyen.fitnessapplication.Dimens
import com.tomtruyen.fitnessapplication.R

object Buttons {
    @Composable
    fun Default(
        text: String,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        shape: Shape = MaterialTheme.shapes.medium,
        onClick: () -> Unit,
    ) {
        Button(
            onClick = onClick,
            enabled = enabled,
            shape = shape,
            modifier = modifier
        ) {
            Text(
                text = text
            )
        }
    }

    @Composable
    fun Text(
        text: String,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        shape: Shape = MaterialTheme.shapes.medium,
        onClick: () -> Unit,
    ) {
        TextButton(
            onClick = onClick,
            enabled = enabled,
            shape = shape,
            modifier = modifier,
        ) {
            Text(
                text = text
            )
        }
    }
}