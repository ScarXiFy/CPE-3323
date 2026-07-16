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

    val categories = listOf("All Events", "Academic", "Sports", "Cultural", "Workshop")

    private var allEvents by mutableStateOf<List<Event>>(emptyList())

    init {
        viewModelScope.launch {
            eventRepository.getEvents().collect { list ->
                allEvents = list
            }
        }
    }

    val events: List<Event>
        get() = allEvents.filter { event ->
            val matchesCategory = selectedCategory == "All Events" || event.category.equals(selectedCategory, ignoreCase = true)
            val matchesSearch = searchQuery.isBlank() || 
                    event.title.contains(searchQuery, ignoreCase = true) || 
                    event.location.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }

    fun onSearchQueryChange(query: String) {
        searchQuery = query
    }

    fun selectCategory(category: String) {
        selectedCategory = category
    }
}
