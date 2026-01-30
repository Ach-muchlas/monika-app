package com.sss.monikaapps.common.formatter

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

object FormatterDate {

    fun getCurrentDate(): String {
        val date = Date()
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(date)
    }

    fun getCurrentDateTime(): String {
        val date = Date()
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return format.format(date)
    }


    fun formatDateTimeToDisplayDateTime(input: String): String {
        val outputFormatter = DateTimeFormatter.ofPattern(
            "EEEE, dd MMM yyyy. HH:mm",
            Locale("id", "ID")
        )

        val possibleInputFormats = listOf(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        )

        val date = possibleInputFormats.asSequence()
            .mapNotNull { formatter ->
                try {
                    LocalDateTime.parse(input, formatter)
                } catch (e: Exception) {
                    null
                }
            }
            .firstOrNull() ?: return ""

        return date.format(outputFormatter)
    }

    fun formatDateToIndoDisplay(input: String): String {
        return try {
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val outputFormatter = DateTimeFormatter.ofPattern(
                "EEEE, dd MMM yyyy",
                Locale("id", "ID")
            )

            val date = LocalDate.parse(input, inputFormatter)
            date.format(outputFormatter)
        } catch (e: Exception) {
            ""
        }
    }

    //    kamis, 01 sept 2025
    fun formatTimestampToIndoDisplay(timestampMillis: Long?): String {
        return try {
            if (timestampMillis == null) return ""

            val date = Date(timestampMillis)
            val formatter = SimpleDateFormat(
                "EEEE, dd MMM yyyy",
                Locale("id", "ID")
            )
            formatter.format(date)
        } catch (e: Exception) {
            ""
        }
    }

    //    yyyy-mm-dd
    fun formatTimestampToDateString(timestampMillis: Long?): String {
        return try {
            if (timestampMillis == null) return ""

            val date = Date(timestampMillis)
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            formatter.format(date)
        } catch (e: Exception) {
            ""
        }
    }

}