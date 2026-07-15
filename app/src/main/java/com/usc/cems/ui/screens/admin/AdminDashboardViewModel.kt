package com.usc.cems.ui.screens.admin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usc.cems.data.model.Event
import com.usc.cems.data.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

import com.usc.cems.data.repository.AuthRepository

@HiltViewModel
class AdminDashboardViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    var isAuthorized by mutableStateOf(false)
        private set

    init {
        isAuthorized = authRepository.getCurrentUser()?.role.equals("admin", ignoreCase = true)
    }

    var searchQuery by mutableStateOf("")
        private set

    var selectedCategory by mutableStateOf("All Events")
        private set

    val events: StateFlow<List<Event>> = eventRepository.getEvents()

    val filteredEvents: StateFlow<List<Event>> = combine(
        eventRepository.getEvents(),
        viewModelScope.run {
            // Create state flows for compose states to combine them reactively
            val qFlow = kotlinx.coroutines.flow.MutableStateFlow(searchQuery)
            val cFlow = kotlinx.coroutines.flow.MutableStateFlow(selectedCategory)
            
            // Keep them updated
            this@AdminDashboardViewModel.javaClass.getDeclaredFields() // Triggering compiler helpers if needed
            
            // Let's use custom helper functions to update them
            // Actually, we can just expose a combined flow
            qFlow
        }
    ) { _, _ ->
        // To be simple and robust with Compose states, we can just compute it directly using Compose state variables!
        // But since we want to be reactive, let's keep it extremely clean:
        val list = events.value
        list.filter { event ->
            val matchesSearch = event.title.contains(searchQuery, ignoreCase = true) ||
                    event.location.contains(searchQuery, ignoreCase = true) ||
                    event.category.contains(searchQuery, ignoreCase = true)
            val matchesCategory = selectedCategory == "All Events" ||
                    event.category.lowercase() == selectedCategory.lowercase() ||
                    (selectedCategory == "Workshops" && event.category.lowercase() == "workshop")
            matchesSearch && matchesCategory
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onSearchQueryChange(newQuery: String) {
        searchQuery = newQuery
    }

    fun onCategorySelect(category: String) {
        selectedCategory = category
    }

    // Direct helper to filter list for compose UI
    fun getFilteredEventsList(): List<Event> {
        return events.value.filter { event ->
            val matchesSearch = event.title.contains(searchQuery, ignoreCase = true) ||
                    event.location.contains(searchQuery, ignoreCase = true) ||
                    event.category.contains(searchQuery, ignoreCase = true)
            val matchesCategory = selectedCategory == "All Events" ||
                    event.category.lowercase() == selectedCategory.lowercase() ||
                    (selectedCategory == "Workshops" && event.category.lowercase() == "workshop")
            matchesSearch && matchesCategory
        }
    }
}
