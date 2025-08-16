package com.chienkanglu.workoutnote.ui.common

import androidx.compose.runtime.Composable
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toJavaZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

enum class DateTimePattern(
    val pattern: String,
) {
    DateWithTime("MMM d, uuuu 'at' HH:mm:ss"),
    DateOnly("MMM d, uuuu"),
    TimeOnly("HH:mm:ss"),
}

@Composable
fun dateFormatted(
    instant: Instant,
    pattern: DateTimePattern = DateTimePattern.DateWithTime,
): String =
    DateTimeFormatter
        .ofPattern(pattern.pattern)
        .withLocale(Locale.getDefault())
        .withZone(LocalTimeZone.current.toJavaZoneId())
        .format(instant.toJavaInstant())
