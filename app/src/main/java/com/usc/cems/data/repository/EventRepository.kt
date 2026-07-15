package com.usc.cems.data.repository

import com.usc.cems.data.model.Event
import kotlinx.coroutines.flow.StateFlow

interface EventRepository {
    fun getEvents(): StateFlow<List<Event>>
    fun getEventById(id: String): Event?
    suspend fun addEvent(event: Event): Result<Unit>
}
