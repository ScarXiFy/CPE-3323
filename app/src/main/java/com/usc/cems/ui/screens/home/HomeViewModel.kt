package com.usc.cems.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usc.cems.data.model.Event
import com.usc.cems.data.repository.EventRepository
import com.usc.cems.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    val isAdmin: Boolean
        get() = authRepository.getCurrentUser()?.role?.equals("admin", ignoreCase = true) == true

    var searchQuery by mutableStateOf("")
        private set

    var selectedCategory by mutableStateOf("All Events")
        private set

    val categories = listOf("All Events", "Academic", "Sports", "Social", "Workshop", "Other")

    private var allEvents by mutableStateOf<List<Event>>(emptyList())

    init {
        viewModelScope.launch {
            eventRepository.getEvents().collect { list ->
                allEvents = list
            }
        }
    }

    val filteredEvents: List<Event>
        get() = allEvents.filter { event ->
            val matchesCategory = when (selectedCategory) {
                "All Events" -> true
                "Other" -> {
                    val predefinedCategories = listOf("Academic", "Sports", "Social", "Workshop")
                    !predefinedCategories.any { it.equals(event.category, ignoreCase = true) }
                }
                else -> event.category.equals(selectedCategory, ignoreCase = true)
            }
            val matchesSearch = searchQuery.isBlank() || 
                    event.title.contains(searchQuery, ignoreCase = true) || 
                    event.location.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }

    val events: List<Event>
        get() = upcomingEvents

    val upcomingEvents: List<Event>
        get() = filteredEvents.filter { !isPastEvent(it) }

    val pastEvents: List<Event>
        get() = filteredEvents.filter { isPastEvent(it) }

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

    fun onSearchQueryChange(query: String) {
        searchQuery = query
    }

    fun selectCategory(category: String) {
        selectedCategory = category
    }
}
