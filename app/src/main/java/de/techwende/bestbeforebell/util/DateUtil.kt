package de.techwende.bestbeforebell.util

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

fun dateToMillis(date: LocalDate): Long {
    return date
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
}

fun millisToDate(millis: Long): LocalDate {
    return Instant
        .ofEpochMilli(millis)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}
