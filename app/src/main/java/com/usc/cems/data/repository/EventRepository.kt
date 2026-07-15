package com.usc.cems.data.repository

import com.usc.cems.data.model.Event
import kotlinx.coroutines.flow.StateFlow

interface EventRepository {
    fun getEvents(): StateFlow<List<Event>>
    fun getEventById(id: String): Event?
    suspend fun addEvent(event: Event): Result<Unit>
    
    // User registrations
    fun getRegisteredEvents(userId: String): StateFlow<List<Event>>
    suspend fun registerForEvent(userId: String, eventId: String): Result<Unit>
    suspend fun unregisterFromEvent(userId: String, eventId: String): Result<Unit>
    suspend fun isUserRegistered(userId: String, eventId: String): Boolean
}
