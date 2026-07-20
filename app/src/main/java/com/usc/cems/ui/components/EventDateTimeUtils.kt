package com.usc.cems.ui.components

import com.usc.cems.data.model.Event
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * Shared parsing for [com.usc.cems.data.model.Event.dateTime].
 *
 * Events now store a single date plus a start/end time, encoded as:
 *   "yyyy-MM-dd HH:mm • HH:mm"
 * e.g. "2026-01-20 14:00 • 16:00"
 *
 * These helpers split that string into a friendly date line and a friendly
 * time-range line so every screen (Admin Dashboard, My Events, etc.) shows
 * dates and times the same way, in separate rows.
 */
private val STORED_DATE_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
private val STORED_TIME_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
private val DISPLAY_DATE_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy")
private val DISPLAY_TIME_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("h:mm a")

/**
 * Returns a friendly date string, e.g. "Jan 20, 2026".
 * Falls back to the raw stored date text if it can't be parsed.
 */
fun Event.formattedDate(): String = formatEventDate(this.dateTime)

/**
 * Returns a friendly time range string, e.g. "2:00 PM - 4:00 PM".
 * Falls back to "TBA" if no time information is present.
 */
fun Event.formattedTimeRange(): String = formatEventTimeRange(this.dateTime)

fun formatEventDate(dateTime: String): String {
    val datePart = dateTime.substringBefore("•").trim().substringBefore(" ").trim()
    return try {
        LocalDate.parse(datePart, STORED_DATE_FORMAT).format(DISPLAY_DATE_FORMAT)
    } catch (e: DateTimeParseException) {
        datePart.ifBlank { "TBA" }
    }
}

fun formatEventTimeRange(dateTime: String): String {
    val beforeSeparator = dateTime.substringBefore("•").trim()
    val startTimeRaw = beforeSeparator.substringAfter(" ", "").trim()
    val endTimeRaw = dateTime.substringAfter("•", "").trim()

    val startFormatted = formatStoredTime(startTimeRaw)
    val endFormatted = formatStoredTime(endTimeRaw)

    return when {
        startFormatted != null && endFormatted != null -> "$startFormatted - $endFormatted"
        startFormatted != null -> startFormatted
        else -> "TBA"
    }
}

private fun formatStoredTime(raw: String): String? {
    if (raw.isBlank()) return null
    return try {
        LocalTime.parse(raw, STORED_TIME_FORMAT).format(DISPLAY_TIME_FORMAT)
    } catch (e: DateTimeParseException) {
        null
    }
}
