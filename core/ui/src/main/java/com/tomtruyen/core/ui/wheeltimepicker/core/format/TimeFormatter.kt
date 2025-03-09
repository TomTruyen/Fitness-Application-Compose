package com.tomtruyen.core.ui.wheeltimepicker.core.format

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.intl.Locale

@Stable
interface TimeFormatter {
  val formatHour: (Int) -> String
  val formatMinute: (Int) -> String
  val formatSeconds: (Int) -> String
}

private class TimeFormatterImpl(
  override val formatHour: (Int) -> String,
  override val formatMinute: (Int) -> String,
  override val formatSeconds: (Int) -> String
) : TimeFormatter

internal fun timeFormatter(
  formatHour: (Int) -> String = { hour ->
    hour.toString().padStart(2, '0')
  },
  formatMinute: (Int) -> String = { minute ->
    minute.toString().padStart(2, '0')
  },
  formatSeconds: (Int) -> String = { seconds ->
    seconds.toString().padStart(2, '0')
  }
): TimeFormatter = TimeFormatterImpl(
  formatHour = formatHour,
  formatMinute = formatMinute,
  formatSeconds = formatSeconds
)

@Composable
fun timeFormatter(
  locale: Locale,
): TimeFormatter {
  return remember(locale) {
    timeFormatter()
  }
}
