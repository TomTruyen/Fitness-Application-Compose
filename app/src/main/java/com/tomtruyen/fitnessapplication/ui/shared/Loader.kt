package com.tomtruyen.fitnessapplication.ui.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Loader(
    loading: Boolean,
    modifier: Modifier = Modifier
) {
    if(loading) {
        LinearProgressIndicator(
            modifier = modifier
                .padding(vertical = 2.dp)
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary
        )
    }
}