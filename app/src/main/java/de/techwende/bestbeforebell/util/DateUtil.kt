package de.techwende.bestbeforebell.util

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

fun dateToMillis(date: LocalDate): Long =
    date
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()

fun millisToDate(millis: Long): LocalDate =
    Instant
        .ofEpochMilli(millis)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
