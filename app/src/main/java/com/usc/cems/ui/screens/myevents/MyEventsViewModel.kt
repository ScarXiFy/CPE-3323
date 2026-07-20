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

@HiltViewModel
class MyEventsViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    var upcomingEvents by mutableStateOf<List<Event>>(emptyList())
        private set

    var pastEvents by mutableStateOf<List<Event>>(emptyList())
        private set

    private var currentUserId: String? = null

    init {
        currentUserId = authRepository.getCurrentUser()?.uid ?: "mock_uid"
        loadRegisteredEvents()
    }

    private fun isPastEvent(event: Event): Boolean {
        if (event.id.startsWith("past_") || 
            event.status.equals("completed", ignoreCase = true) || 
            event.registrationStatus.equals("completed", ignoreCase = true)) {
            return true
        }
        val datePart = event.dateTime.split(" • ").getOrNull(0) ?: ""
        val firstToken = datePart.split(" ").getOrNull(0) ?: ""
        if (firstToken.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))) {
            try {
                val eventDate = java.time.LocalDate.parse(firstToken)
                if (eventDate.isBefore(java.time.LocalDate.now())) {
                    return true
                }
            } catch (e: Exception) {
                // ignored
            }
        }
        return false
    }

    fun loadRegisteredEvents() {
        val uid = currentUserId ?: return
        viewModelScope.launch {
            eventRepository.getRegisteredEvents(uid).collect { list ->
                // Split list into upcoming (active) and completed (past) events
                upcomingEvents = list.filter { !isPastEvent(it) }
                pastEvents = list.filter { isPastEvent(it) }
            }
        }
    }
}
