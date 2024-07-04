package com.tomtruyen.fitnessapplication.ui.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tomtruyen.fitnessapplication.Dimens
import com.tomtruyen.fitnessapplication.ui.theme.BlueGrey
import com.tomtruyen.fitnessapplication.ui.theme.LavenderMist

@Composable
fun TextDivider(
    text: String,
    modifier: Modifier = Modifier,
    dividerColor: Color = LavenderMist,
    textColor: Color = BlueGrey
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.Small)
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = dividerColor,
            thickness = 1.dp
        )

        Text(
            text = text.uppercase(),
            color = textColor
        )

        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = dividerColor,
            thickness = 1.dp
        )
    }
}