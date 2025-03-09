package com.tomtruyen.core.ui.wheeltimepicker.core

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.ui.wheeltimepicker.core.format.TimeFormatter
import com.tomtruyen.core.ui.wheeltimepicker.core.format.timeFormatter
import kotlinx.datetime.LocalTime

@Composable
internal fun DefaultWheelTimePicker(
  modifier: Modifier = Modifier,
  startTime: LocalTime = LocalTime.Companion.now(),
  minTime: LocalTime = LocalTime.MIN,
  maxTime: LocalTime = LocalTime.MAX,
  timeFormatter: TimeFormatter = timeFormatter(Locale.current),
  size: DpSize = DpSize(128.dp, 128.dp),
  rowCount: Int = 3,
  textStyle: TextStyle = MaterialTheme.typography.titleMedium,
  textColor: Color = LocalContentColor.current,
  selectorProperties: SelectorProperties = WheelPickerDefaults.selectorProperties(),
  onSnappedTime: (snappedTime: SnappedTime) -> Int? = { _, -> null },
) {
  require(selectorProperties.components().value.isNotEmpty()) {
    "TimeComponent list can't be empty"
  }

  val itemWidth = remember { size.width / 2 }

  val hours = rememberHours(timeFormatter)
  val minutes = rememberMinutes(timeFormatter)
  val seconds = rememberSeconds(timeFormatter)

  var snappedTime by remember { mutableStateOf(LocalTime(startTime.hour, startTime.minute)) }

  Column(
    verticalArrangement = Arrangement.spacedBy(Dimens.Small)
  ) {
    TimePickerHeader(
      textStyle = textStyle,
      textColor = textColor,
      components = selectorProperties.components().value
    )

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
      if (selectorProperties.enabled().value) {
        Surface(
          modifier = Modifier
            .fillMaxWidth()
            .height(size.height / rowCount),
          shape = selectorProperties.shape().value,
          color = selectorProperties.color().value,
          border = selectorProperties.border().value
        ) {}
      }

      Row(modifier = Modifier.height(size.height)) {
        if (selectorProperties.components().value.contains(TimeComponent.HOUR)) {
          //Hour
          WheelTextPicker(
            modifier = Modifier.weight(1f),
            size = DpSize(
              width = itemWidth,
              height = size.height
            ),
            texts = hours.map { it.text },
            rowCount = rowCount,
            style = textStyle,
            color = textColor,
            startIndex = hours.find { it.value == startTime.hour }?.index ?: 0,
            selectorProperties = WheelPickerDefaults.selectorProperties(
              enabled = false
            ),
            onScrollFinished = { snappedIndex ->
              val newHour = hours.find { it.index == snappedIndex }?.value

              newHour?.let {

                val newTime = snappedTime.withHour(newHour)

                if (!newTime.isBefore(minTime) && !newTime.isAfter(maxTime)) {
                  snappedTime = newTime
                }

                val newIndex = hours.find { it.value == snappedTime.hour }?.index

                newIndex?.let {
                  onSnappedTime(
                    SnappedTime.Hour(
                      localTime = snappedTime,
                      index = newIndex
                    ),
                  )?.let { return@WheelTextPicker it }
                }
              }

              return@WheelTextPicker hours.find { it.value == snappedTime.hour }?.index
            }
          )

          //Colon
          TimeSeparator(
            modifier = Modifier
              .align(Alignment.CenterVertically)
              .width(0.dp),
            textStyle = textStyle.copy(color = textColor),
          )
        }

        if (selectorProperties.components().value.contains(TimeComponent.MINUTE)) {
          //Minute
          WheelTextPicker(
            modifier = Modifier.weight(1f),
            size = DpSize(
              width = itemWidth,
              height = size.height
            ),
            texts = minutes.map { it.text },
            rowCount = rowCount,
            style = textStyle,
            color = textColor,
            startIndex = minutes.find { it.value == startTime.minute }?.index ?: 0,
            selectorProperties = WheelPickerDefaults.selectorProperties(
              enabled = false
            ),
            onScrollFinished = { snappedIndex ->

              val newMinute = minutes.find { it.index == snappedIndex }?.value

              val newHour = hours.find { it.value == snappedTime.hour }?.value

              newMinute?.let {
                newHour?.let {
                  val newTime = snappedTime.withMinute(newMinute).withHour(newHour)

                  if (!newTime.isBefore(minTime) && !newTime.isAfter(maxTime)) {
                    snappedTime = newTime
                  }

                  val newIndex = minutes.find { it.value == snappedTime.minute }?.index

                  newIndex?.let {
                    onSnappedTime(
                      SnappedTime.Minute(
                        localTime = snappedTime,
                        index = newIndex
                      ),
                    )?.let { return@WheelTextPicker it }
                  }
                }
              }

              return@WheelTextPicker minutes.find { it.value == snappedTime.minute }?.index
            }
          )

          //Colon
          TimeSeparator(
            modifier = Modifier
              .align(Alignment.CenterVertically)
              .width(0.dp),
            textStyle = textStyle.copy(color = textColor),
          )
        }

        if (selectorProperties.components().value.contains(TimeComponent.SECOND)) {
          //Second
          WheelTextPicker(
            modifier = Modifier.weight(1f),
            size = DpSize(
              width = itemWidth,
              height = size.height
            ),
            texts = seconds.map { it.text },
            rowCount = rowCount,
            style = textStyle,
            color = textColor,
            startIndex = seconds.find { it.value == startTime.second }?.index ?: 0,
            selectorProperties = WheelPickerDefaults.selectorProperties(
              enabled = false
            ),
            onScrollFinished = { snappedIndex ->

              val newSecond = seconds.find { it.index == snappedIndex }?.value

              val newMinute = minutes.find { it.value == snappedTime.minute }?.value

              val newHour = hours.find { it.value == snappedTime.hour }?.value

              newSecond?.let {
                newMinute?.let {
                  newHour?.let {
                    val newTime =
                      snappedTime.withSecond(newSecond).withMinute(newMinute).withHour(newHour)

                    if (!newTime.isBefore(minTime) && !newTime.isAfter(maxTime)) {
                      snappedTime = newTime
                    }

                    val newIndex = seconds.find { it.value == snappedTime.second }?.index

                    newIndex?.let {
                      onSnappedTime(
                        SnappedTime.Second(
                          localTime = snappedTime,
                          index = newIndex
                        ),
                      )?.let { return@WheelTextPicker it }
                    }
                  }
                }
              }

              return@WheelTextPicker seconds.find { it.value == snappedTime.second }?.index
            }
          )
        }
      }
    }
  }
}

@Composable
fun TimeSeparator(
  modifier: Modifier = Modifier,
  textStyle: TextStyle = TextStyle.Default,
  dotSizeRatio: Float = 0.135f,
  spacingRatio: Float = 0.25f
) {
  val density = LocalDensity.current

  val fontSize = textStyle.fontSize
  val fontWeight = textStyle.fontWeight ?: FontWeight.Normal
  val color = textStyle.color

  val fontSizePx = with(density) { fontSize.toPx() }
  val baseDotSizePx = fontSizePx * dotSizeRatio

  val weightFactor = when (fontWeight) {
    FontWeight.Thin -> 0.7f
    FontWeight.ExtraLight -> 0.8f
    FontWeight.Light -> 0.9f
    FontWeight.Normal -> 1.0f
    FontWeight.Medium -> 1.1f
    FontWeight.SemiBold -> 1.2f
    FontWeight.Bold -> 1.3f
    FontWeight.ExtraBold -> 1.4f
    FontWeight.Black -> 1.5f
    else -> 1.0f
  }

  val dotSizePx = baseDotSizePx * weightFactor
  val spacingPx = fontSizePx * spacingRatio
  val totalHeightPx = dotSizePx * 2 + spacingPx

  Box(
    modifier = modifier,
    contentAlignment = Alignment.Center
  ) {
    Canvas(
      modifier = Modifier.size(
        width = with(density) { dotSizePx.toDp() },
        height = with(density) { totalHeightPx.toDp() }
      )
    ) {
      drawCircle(
        color = color,
        radius = dotSizePx / 2,
        center = Offset(size.width / 2, dotSizePx / 2),
        style = Fill
      )

      drawCircle(
        color = color,
        radius = dotSizePx / 2,
        center = Offset(size.width / 2, totalHeightPx - dotSizePx / 2),
        style = Fill
      )
    }
  }

}

private data class Hour(
  val text: String,
  val value: Int,
  val index: Int
)

private data class Minute(
  val text: String,
  val value: Int,
  val index: Int
)

private data class Second(
  val text: String,
  val value: Int,
  val index: Int
)

@Composable
private fun rememberHours(timeFormatter: TimeFormatter) = remember(timeFormatter) {
  (0..23).map {
    Hour(
      text = timeFormatter.formatHour(it),
      value = it,
      index = it
    )
  }
}

@Composable
private fun rememberMinutes(timeFormatter: TimeFormatter) = remember(timeFormatter) {
  (0..59).map {
    Minute(
      text = timeFormatter.formatMinute(it),
      value = it,
      index = it
    )
  }
}

@Composable
private fun rememberSeconds(timeFormatter: TimeFormatter) = remember(timeFormatter) {
  (0..59).map {
    Second(
      text = timeFormatter.formatSeconds(it),
      value = it,
      index = it
    )
  }
}