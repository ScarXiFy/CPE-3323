package com.usc.cems.ui.components

import com.usc.cems.data.model.Event
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

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

/**
 * Evaluates whether an event is in the past based on:
 * 1. Explicit ID/status flags ("past_", "completed").
 * 2. Full scheduled end time (or start time if no end time exists).
 */
fun Event.isPastEvent(now: LocalDateTime = LocalDateTime.now()): Boolean {
    if (this.id.startsWith("past_") ||
        this.status.equals("completed", ignoreCase = true) ||
        this.registrationStatus.equals("completed", ignoreCase = true)
    ) {
        return true
    }

    val eventEndDateTime = parseEventEndDateTime(this.dateTime, now)
    return if (eventEndDateTime != null) {
        now.isAfter(eventEndDateTime)
    } else {
        false
    }
}

/**
 * Returns formatted attendance count string.
 * For past events, formats as "<number> students attended".
 * For upcoming events, returns the original [Event.attendingCount].
 */
fun Event.formattedAttendingCount(isPast: Boolean = this.isPastEvent()): String {
    val count = extractAttendeeCount(this.attendingCount)
    return if (isPast) {
        if (count == 1) "1 student attended" else "$count students attended"
    } else {
        this.attendingCount
    }
}

fun extractAttendeeCount(attendingCountStr: String): Int {
    val firstToken = attendingCountStr.trim().split(" ").firstOrNull() ?: ""
    return firstToken.toIntOrNull()
        ?: attendingCountStr.filter { it.isDigit() }.toIntOrNull()
        ?: 0
}

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

/**
 * Parses the end date and time of an event from [dateTime].
 * Handles formats like:
 * - "yyyy-MM-dd HH:mm • HH:mm"
 * - "yyyy-MM-dd HH:mm"
 * - "Oct 12 • 2:30 PM - 4:00 PM"
 * - "Feb 14, 2024 • 6:30 PM"
 */
fun parseEventEndDateTime(dateTime: String, currentReferenceDateTime: LocalDateTime = LocalDateTime.now()): LocalDateTime? {
    if (dateTime.isBlank()) return null

    val parts = dateTime.split("•")
    val startPart = parts.getOrNull(0)?.trim() ?: ""
    val endPart = parts.getOrNull(1)?.trim() ?: ""

    // 1. Try parsing standard stored format "yyyy-MM-dd HH:mm" in startPart
    val startTokens = startPart.split(" ").filter { it.isNotBlank() }
    val dateStr = startTokens.getOrNull(0) ?: ""
    val startTimeStr = startTokens.getOrNull(1) ?: ""

    val parsedLocalDate: LocalDate? = try {
        LocalDate.parse(dateStr, STORED_DATE_FORMAT)
    } catch (e: Exception) {
        // Fallback for legacy date formats like "Feb 14, 2024" or "Oct 12"
        parseLegacyDate(startPart, currentReferenceDateTime.year)
    }

    if (parsedLocalDate == null) return null

    // Determine target end time or start time
    val parsedTime: LocalTime? = parseEndTimeFromPart(endPart)
        ?: parseTimeFromToken(startTimeStr)
        ?: parseLegacyTimeFromPart(startPart, isEnd = true)
        ?: parseLegacyTimeFromPart(endPart, isEnd = true)

    return if (parsedTime != null) {
        LocalDateTime.of(parsedLocalDate, parsedTime)
    } else {
        // If date exists but no time can be parsed, assume end of day (23:59:59)
        parsedLocalDate.atTime(23, 59, 59)
    }
}

private fun parseEndTimeFromPart(endPart: String): LocalTime? {
    if (endPart.isBlank()) return null
    val tokens = endPart.split(" ").filter { it.isNotBlank() }

    // If endPart is "yyyy-MM-dd HH:mm"
    if (tokens.size >= 2 && tokens[0].matches(Regex("\\d{4}-\\d{2}-\\d{2}"))) {
        return parseTimeFromToken(tokens[1])
    }
    // If endPart is just "HH:mm"
    if (tokens.isNotEmpty()) {
        parseTimeFromToken(tokens[0])?.let { return it }
    }
    return parseLegacyTimeFromPart(endPart, isEnd = true)
}

private fun parseTimeFromToken(token: String): LocalTime? {
    if (token.isBlank()) return null
    return try {
        LocalTime.parse(token, STORED_TIME_FORMAT)
    } catch (e: Exception) {
        null
    }
}

private fun parseLegacyDate(text: String, defaultYear: Int): LocalDate? {
    val cleanText = text.substringBefore("-").substringBefore("•").trim()
    val regexWithYear = Regex("([A-Za-z]+)\\s+(\\d{1,2}),?\\s+(\\d{4})")
    val matchWithYear = regexWithYear.find(cleanText)
    if (matchWithYear != null) {
        val (month, day, year) = matchWithYear.destructured
        return try {
            val dateStr = "$month $day $year"
            LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("MMM d yyyy", Locale.US))
        } catch (e: Exception) {
            null
        }
    }

    val regexNoYear = Regex("([A-Za-z]+)\\s+(\\d{1,2})")
    val matchNoYear = regexNoYear.find(cleanText)
    if (matchNoYear != null) {
        val (month, day) = matchNoYear.destructured
        return try {
            val dateStr = "$month $day $defaultYear"
            LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("MMM d yyyy", Locale.US))
        } catch (e: Exception) {
            null
        }
    }
    return null
}

private fun parseLegacyTimeFromPart(text: String, isEnd: Boolean): LocalTime? {
    if (text.isBlank()) return null
    val legacyTimeFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale.US)

    if (text.contains("-")) {
        val rangeParts = text.split("-")
        val targetPart = if (isEnd && rangeParts.size > 1) rangeParts[1].trim() else rangeParts[0].trim()
        val timeMatch = Regex("\\d{1,2}:\\d{2}\\s*(?:AM|PM|am|pm)", RegexOption.IGNORE_CASE).find(targetPart)
        if (timeMatch != null) {
            return try {
                LocalTime.parse(timeMatch.value.uppercase(Locale.US), legacyTimeFormatter)
            } catch (e: Exception) { null }
        }
    }

    val timeMatch = Regex("\\d{1,2}:\\d{2}\\s*(?:AM|PM|am|pm)", RegexOption.IGNORE_CASE).find(text)
    if (timeMatch != null) {
        return try {
            LocalTime.parse(timeMatch.value.uppercase(Locale.US), legacyTimeFormatter)
        } catch (e: Exception) { null }
    }
    return null
}

