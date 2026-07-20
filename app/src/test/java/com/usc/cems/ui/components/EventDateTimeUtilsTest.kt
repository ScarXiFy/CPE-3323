package com.usc.cems.ui.components

import com.usc.cems.data.model.Event
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDateTime

class EventDateTimeUtilsTest {

    private val referenceNow = LocalDateTime.of(2026, 7, 20, 14, 0) // July 20, 2026 2:00 PM

    @Test
    fun testIsPastEvent_withDateAndTime() {
        // July 20, 2026 5:00 PM -> Upcoming (2:00 PM is before 5:00 PM)
        val event1 = Event(
            id = "test1",
            title = "Test Event 1",
            category = "Academic",
            dateTime = "2026-07-20 17:00",
            location = "Main Hall"
        )
        assertFalse(event1.isPastEvent(referenceNow))

        // July 20, 2026 1:00 PM -> Past (2:00 PM is after 1:00 PM)
        val event2 = Event(
            id = "test2",
            title = "Test Event 2",
            category = "Academic",
            dateTime = "2026-07-20 13:00",
            location = "Main Hall"
        )
        assertTrue(event2.isPastEvent(referenceNow))

        // July 19, 2026 5:00 PM -> Past
        val event3 = Event(
            id = "test3",
            title = "Test Event 3",
            category = "Sports",
            dateTime = "2026-07-19 17:00",
            location = "Stadium"
        )
        assertTrue(event3.isPastEvent(referenceNow))

        // July 21, 2026 9:00 AM -> Upcoming
        val event4 = Event(
            id = "test4",
            title = "Test Event 4",
            category = "Social",
            dateTime = "2026-07-21 09:00",
            location = "Lounge"
        )
        assertFalse(event4.isPastEvent(referenceNow))
    }

    @Test
    fun testIsPastEvent_withStartAndEndTime() {
        // Event scheduled 10:00 AM to 4:00 PM today (refTime is 2:00 PM -> end time 4:00 PM is not past)
        val ongoingEvent = Event(
            id = "test_ongoing",
            title = "Ongoing Workshop",
            category = "Workshop",
            dateTime = "2026-07-20 10:00 • 16:00",
            location = "Lab 101"
        )
        assertFalse(ongoingEvent.isPastEvent(referenceNow))

        // Event scheduled 10:00 AM to 1:00 PM today (refTime is 2:00 PM -> end time 1:00 PM has passed)
        val finishedTodayEvent = Event(
            id = "test_finished",
            title = "Finished Workshop",
            category = "Workshop",
            dateTime = "2026-07-20 10:00 • 13:00",
            location = "Lab 101"
        )
        assertTrue(finishedTodayEvent.isPastEvent(referenceNow))
    }

    @Test
    fun testIsPastEvent_explicitPastIdOrStatus() {
        val pastIdEvent = Event(
            id = "past_999",
            title = "Legacy Past Event",
            category = "Social",
            dateTime = "2026-07-25 18:00",
            location = "Auditorium"
        )
        assertTrue(pastIdEvent.isPastEvent(referenceNow))

        val completedStatusEvent = Event(
            id = "test_completed",
            title = "Completed Event",
            category = "Social",
            dateTime = "2026-07-25 18:00",
            location = "Auditorium",
            status = "Completed"
        )
        assertTrue(completedStatusEvent.isPastEvent(referenceNow))
    }

    @Test
    fun testFormattedAttendingCount() {
        val event0 = Event(id = "1", title = "T", category = "C", dateTime = "", location = "", attendingCount = "0 students are attending")
        val event1 = Event(id = "2", title = "T", category = "C", dateTime = "", location = "", attendingCount = "1 student is attending")
        val event15 = Event(id = "3", title = "T", category = "C", dateTime = "", location = "", attendingCount = "15 students are attending")
        val event128 = Event(id = "4", title = "T", category = "C", dateTime = "", location = "", attendingCount = "128 students are attending")

        // Past event formatting
        assertEquals("0 students attended", event0.formattedAttendingCount(isPast = true))
        assertEquals("1 student attended", event1.formattedAttendingCount(isPast = true))
        assertEquals("15 students attended", event15.formattedAttendingCount(isPast = true))
        assertEquals("128 students attended", event128.formattedAttendingCount(isPast = true))

        // Upcoming event formatting
        assertEquals("15 students are attending", event15.formattedAttendingCount(isPast = false))
    }
}
