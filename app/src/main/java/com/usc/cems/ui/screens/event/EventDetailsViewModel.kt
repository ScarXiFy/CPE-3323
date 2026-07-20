package com.usc.cems.ui.screens.event

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
class EventDetailsViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    var event by mutableStateOf<Event?>(null)
        private set

    var isRegistered by mutableStateOf(false)
        private set

    val isAdmin: Boolean
        get() = authRepository.getCurrentUser()?.role?.lowercase() == "admin"

    private var currentUserId: String? = null

    init {
        currentUserId = authRepository.getCurrentUser()?.uid ?: "mock_uid"
    }

    fun loadEvent(id: String) {
        viewModelScope.launch {
            eventRepository.getEvents().collect { list ->
                val found = list.find { it.id == id } ?: eventRepository.getEventById(id)
                if (found != null) {
                    event = found
                }
                currentUserId?.let { uid ->
                    isRegistered = eventRepository.isUserRegistered(uid, id)
                }
            }
        }
    }

    fun toggleRegistration() {
        val eventId = event?.id ?: return
        val uid = currentUserId ?: return

        viewModelScope.launch {
            if (isRegistered) {
                eventRepository.unregisterFromEvent(uid, eventId).onSuccess {
                    isRegistered = false
                }
            } else {
                eventRepository.registerForEvent(uid, eventId).onSuccess {
                    isRegistered = true
                }
            }
        }
    }
}
