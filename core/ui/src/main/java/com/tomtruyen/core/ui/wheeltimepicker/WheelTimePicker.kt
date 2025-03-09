package com.tomtruyen.core.ui.wheeltimepicker

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.tomtruyen.core.ui.wheeltimepicker.core.DefaultWheelTimePicker
import com.tomtruyen.core.ui.wheeltimepicker.core.MAX
import com.tomtruyen.core.ui.wheeltimepicker.core.MIN
import com.tomtruyen.core.ui.wheeltimepicker.core.SelectorProperties
import com.tomtruyen.core.ui.wheeltimepicker.core.WheelPickerDefaults
import com.tomtruyen.core.ui.wheeltimepicker.core.format.TimeFormatter
import com.tomtruyen.core.ui.wheeltimepicker.core.format.timeFormatter
import com.tomtruyen.core.ui.wheeltimepicker.core.now
import kotlinx.datetime.LocalTime

@Composable
fun WheelTimePicker(
  modifier: Modifier = Modifier,
  startTime: LocalTime = LocalTime.now(),
  minTime: LocalTime = LocalTime.MIN,
  maxTime: LocalTime = LocalTime.MAX,
  timeFormatter: TimeFormatter = timeFormatter(Locale.current),
  size: DpSize = DpSize(128.dp, 128.dp),
  rowCount: Int = 3,
  textStyle: TextStyle = MaterialTheme.typography.titleMedium,
  textColor: Color = LocalContentColor.current,
  selectorProperties: SelectorProperties = WheelPickerDefaults.selectorProperties(),
  onSnappedTime: (snappedTime: LocalTime) -> Unit = {},
) {
  DefaultWheelTimePicker(
    modifier,
    startTime,
    minTime,
    maxTime,
    timeFormatter,
    size,
    rowCount,
    textStyle,
    textColor,
    selectorProperties,
    onSnappedTime = { snappedTime ->
      onSnappedTime(snappedTime.snappedLocalTime)
      snappedTime.snappedIndex
    }
  )
}