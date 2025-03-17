package com.tomtruyen.core.ui.wheeltimepicker.core

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tomtruyen.core.ui.Label
import com.tomtruyen.core.ui.R

@Composable
fun TimePickerHeader(
    textStyle: TextStyle,
    textColor: Color,
    components: List<TimeComponent>
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        if (components.contains(TimeComponent.HOUR)) {
            Label(
                modifier = Modifier
                    .weight(1f),
                label = stringResource(id = R.string.label_time_picker_hour).uppercase(),
                textAlign = TextAlign.Center
            )

            // Render them invisible so we keep the correct spacing
            TimeSeparator(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .width(0.dp)
                    .alpha(0f),
                textStyle = textStyle.copy(color = textColor),
            )
        }

        if (components.contains(TimeComponent.MINUTE)) {
            Label(
                modifier = Modifier
                    .weight(1f),
                label = stringResource(id = R.string.label_time_picker_minute).uppercase(),
                textAlign = TextAlign.Center
            )

            // Render them invisible so we keep the correct spacing
            TimeSeparator(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .width(0.dp)
                    .alpha(0f),
                textStyle = textStyle.copy(color = textColor),
            )
        }

        if (components.contains(TimeComponent.SECOND)) {
            Label(
                modifier = Modifier
                    .weight(1f),
                label = stringResource(id = R.string.label_time_picker_second).uppercase(),
                textAlign = TextAlign.Center
            )
        }
    }
}