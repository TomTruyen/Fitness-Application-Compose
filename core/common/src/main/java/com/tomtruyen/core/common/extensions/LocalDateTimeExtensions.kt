package com.tomtruyen.core.common.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.tomtruyen.core.common.R
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
fun LocalDateTime.toRelativeTimeString(): String {
    val date = toJavaLocalDateTime()
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime()

    val daysBetween = ChronoUnit.DAYS.between(date, now)
    val monthsBetween = ChronoUnit.MONTHS.between(date, now)
    val yearsBetween = ChronoUnit.YEARS.between(date, now)

    return when {
        daysBetween == 0L -> stringResource(R.string.relative_time_today)
        daysBetween == 1L -> stringResource(R.string.relative_time_yesterday)
        daysBetween < 7 -> stringResource(R.string.relative_time_days_ago, daysBetween)
        daysBetween == 7L -> stringResource(R.string.relative_time_week_ago)
        monthsBetween < 1L -> stringResource(R.string.relative_time_weeks_ago, daysBetween / 7)
        monthsBetween == 1L -> stringResource(R.string.relative_time_month_ago)
        monthsBetween < 12 -> stringResource(R.string.relative_time_months_ago, monthsBetween)
        yearsBetween == 1L -> stringResource(R.string.relative_time_year_ago)
        else -> stringResource(R.string.relative_time_years_ago, yearsBetween)
    }
}

fun LocalDateTime.toFormat(format: String): String {
    val formatter = DateTimeFormatter.ofPattern(format)

    return formatter.format(this.toJavaLocalDateTime())
}