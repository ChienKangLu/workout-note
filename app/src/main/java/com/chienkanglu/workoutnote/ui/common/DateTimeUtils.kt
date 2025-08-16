package com.chienkanglu.workoutnote.ui.common

import androidx.compose.runtime.Composable
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toJavaZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun dateFormatted(instant: Instant): String =
    DateTimeFormatter
        .ofPattern("MMM d, uuuu 'at' HH:mm:ss")
        .withLocale(Locale.getDefault())
        .withZone(LocalTimeZone.current.toJavaZoneId())
        .format(instant.toJavaInstant())
