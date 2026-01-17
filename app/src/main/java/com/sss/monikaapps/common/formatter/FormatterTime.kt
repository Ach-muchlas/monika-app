package com.sss.monikaapps.common.formatter

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object FormatterTime {
    private val apiDateTimeFormatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    private val timeOnlyFormatter =
        DateTimeFormatter.ofPattern("HH:mm")

    fun formatTimeOnly(dateTime: String): String {
        return try {
            val cleanedDateTime = dateTime.substringBefore(".")

            LocalDateTime
                .parse(cleanedDateTime, apiDateTimeFormatter)
                .format(timeOnlyFormatter)
        } catch (e: Exception) {
            ""
        }
    }
}