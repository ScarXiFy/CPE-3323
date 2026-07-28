package com.usc.cems.ui.screens.myevents

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usc.cems.data.model.Event
import com.usc.cems.data.repository.AuthRepository
import com.usc.cems.data.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.usc.cems.ui.components.isPastEvent
import com.usc.cems.ui.components.isOngoingEvent
import com.usc.cems.ui.components.isUpcomingEvent

@HiltViewModel
class MyEventsViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    var upcomingEvents by mutableStateOf<List<Event>>(emptyList())
        private set

    var ongoingEvents by mutableStateOf<List<Event>>(emptyList())
        private set

    var pastEvents by mutableStateOf<List<Event>>(emptyList())
        private set

    private var currentUserId: String? = null

    init {
        currentUserId = authRepository.getCurrentUser()?.uid ?: "mock_uid"
        loadRegisteredEvents()
    }

    fun loadRegisteredEvents() {
        val uid = currentUserId ?: return
        viewModelScope.launch {
            eventRepository.getRegisteredEvents(uid).collect { list ->
                // Split list into upcoming, ongoing, and completed (past) events
                upcomingEvents = list.filter { it.isUpcomingEvent() }
                ongoingEvents = list.filter { it.isOngoingEvent() }
                pastEvents = list.filter { it.isPastEvent() }
            }
        }
    }
}
