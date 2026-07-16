package com.usc.cems.ui.screens.createevent

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usc.cems.data.model.Event
import com.usc.cems.data.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CreateEventViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val eventId: String? = savedStateHandle.get<String>("eventId")
    val isEditMode: Boolean
        get() = eventId != null

    private var originalEvent: Event? = null

    init {
        eventId?.let { id ->
            eventRepository.getEventById(id)?.let { event ->
                originalEvent = event
                title = event.title
                category = event.category
                organizer = event.organizerName
                
                val parts = event.dateTime.split(" • ")
                date = parts.getOrNull(0) ?: ""
                val timeRange = parts.getOrNull(1) ?: ""
                val times = timeRange.split(" - ")
                startTime = times.getOrNull(0) ?: ""
                endTime = times.getOrNull(1) ?: ""
                
                val locParts = event.location.split(", ")
                building = locParts.getOrNull(0) ?: ""
                room = locParts.getOrNull(1) ?: ""
                
                description = event.description
                imageUrl = event.imageUrl
            }
        }
    }

    var title by mutableStateOf("")
        private set
    var titleError by mutableStateOf<String?>(null)
        private set

    var category by mutableStateOf("")
        private set
    var categoryError by mutableStateOf<String?>(null)
        private set

    var organizer by mutableStateOf("")
        private set
    var organizerError by mutableStateOf<String?>(null)
        private set

    var date by mutableStateOf("")
        private set
    var dateError by mutableStateOf<String?>(null)
        private set

    var startTime by mutableStateOf("")
        private set
    var startTimeError by mutableStateOf<String?>(null)
        private set

    var endTime by mutableStateOf("")
        private set
    var endTimeError by mutableStateOf<String?>(null)
        private set

    var building by mutableStateOf("")
        private set
    var buildingError by mutableStateOf<String?>(null)
        private set

    var room by mutableStateOf("")
        private set
    var roomError by mutableStateOf<String?>(null)
        private set

    var description by mutableStateOf("")
        private set
    var descriptionError by mutableStateOf<String?>(null)
        private set

    var imageUrl by mutableStateOf("https://images.unsplash.com/photo-1540575467063-178a50c2df87?w=800&auto=format&fit=crop&q=60")
        private set

    var isLoading by mutableStateOf(false)
        private set

    private val _creationSuccess = MutableSharedFlow<Unit>()
    val creationSuccess: SharedFlow<Unit> = _creationSuccess.asSharedFlow()

    fun onTitleChange(value: String) {
        title = value
        titleError = null
    }

    fun onCategoryChange(value: String) {
        category = value
        categoryError = null
    }

    fun onOrganizerChange(value: String) {
        organizer = value
        organizerError = null
    }

    fun onDateChange(value: String) {
        date = value
        dateError = null
    }

    fun onStartTimeChange(value: String) {
        startTime = value
        startTimeError = null
    }

    fun onEndTimeChange(value: String) {
        endTime = value
        endTimeError = null
    }

    fun onBuildingChange(value: String) {
        building = value
        buildingError = null
    }

    fun onRoomChange(value: String) {
        room = value
        roomError = null
    }

    fun onDescriptionChange(value: String) {
        description = value
        descriptionError = null
    }

    fun saveEvent() {
        if (isEditMode) updateEvent() else createEvent()
    }

    fun createEvent() {
        if (!validateInputs()) return

        isLoading = true
        viewModelScope.launch {
            val eventDateFormatted = date // e.g. "2026-07-20"
            val timeRange = if (startTime.isNotBlank() && endTime.isNotBlank()) {
                "$startTime - $endTime"
            } else {
                startTime.ifBlank { "All Day" }
            }
            
            val newEvent = Event(
                id = UUID.randomUUID().toString(),
                title = title,
                category = category,
                imageUrl = imageUrl,
                dateTime = "$eventDateFormatted • $timeRange",
                location = "${building.ifBlank { "Main Campus" }}, ${room.ifBlank { "TBA" }}",
                spotsLeft = "Unlimited spots",
                description = description,
                organizerName = organizer,
                organizerLogo = "https://lh3.googleusercontent.com/aida-public/AB6AXuDBkuM5btIeSGlZYkviOI_ikadaa7meJOX_vVgO0WFCh5PsjNAAqu5bZsfixtExgIjvBFWz_jS7Q67ardG8KKf-FK4oEZEdzW9ClrnnVFFPhgdelnlE8H6Ul2FeMYCWGilxdj2UU7U1Q_kofBpiY28RqlOuM0rdQYKPxOAdpvj6WTx5EZ3MkAFSUAa7NQQrYYwPXPe7eaGw6wA4BL4Sg_phOxO4WChvmlhNA3v6tdEMBq-jlcDdGeE0FQ",
                attendingCount = "0 students attending",
                registrationStatus = "Open"
            )

            eventRepository.addEvent(newEvent)
                .onSuccess {
                    _creationSuccess.emit(Unit)
                }
            isLoading = false
        }
    }

    private fun updateEvent() {
        if (!validateInputs()) return
        isLoading = true
        viewModelScope.launch {
            val eventDateFormatted = date
            val timeRange = if (startTime.isNotBlank() && endTime.isNotBlank()) {
                "$startTime - $endTime"
            } else {
                startTime.ifBlank { "All Day" }
            }
            val updatedEvent = Event(
                id = eventId!!,
                title = title,
                category = category,
                imageUrl = imageUrl,
                dateTime = "$eventDateFormatted • $timeRange",
                location = "${building.ifBlank { "Main Campus" }}, ${room.ifBlank { "TBA" }}",
                spotsLeft = originalEvent?.spotsLeft ?: "Unlimited spots",
                description = description,
                organizerName = organizer,
                organizerLogo = originalEvent?.organizerLogo ?: "https://lh3.googleusercontent.com/aida-public/AB6AXuDBkuM5btIeSGlZYkviOI_ikadaa7meJOX_vVgO0WFCh5PsjNAAqu5bZsfixtExgIjvBFWz_jS7Q67ardG8KKf-FK4oEZEdzW9ClrnnVFFPhgdelnlE8H6Ul2FeMYCWGilxdj2UU7U1Q_kofBpiY28RqlOuM0rdQYKPxOAdpvj6WTx5EZ3MkAFSUAa7NQQrYYwPXPe7eaGw6wA4BL4Sg_phOxO4WChvmlhNA3v6tdEMBq-jlcDdGeE0FQ",
                attendingCount = originalEvent?.attendingCount ?: "0 students attending",
                registrationStatus = originalEvent?.registrationStatus ?: "Open"
            )
            eventRepository.updateEvent(updatedEvent)
                .onSuccess {
                    _creationSuccess.emit(Unit)
                }
            isLoading = false
        }
    }

    fun deleteEvent() {
        if (!isEditMode) return
        isLoading = true
        viewModelScope.launch {
            eventRepository.deleteEvent(eventId!!)
                .onSuccess {
                    _creationSuccess.emit(Unit)
                }
            isLoading = false
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        if (title.isBlank()) {
            titleError = "Event Title is required"
            isValid = false
        }
        if (category.isBlank()) {
            categoryError = "Category selection is required"
            isValid = false
        }
        if (organizer.isBlank()) {
            organizerError = "Organizer name is required"
            isValid = false
        }
        if (date.isBlank()) {
            dateError = "Event Date is required"
            isValid = false
        }
        if (description.isBlank()) {
            descriptionError = "Event Description is required"
            isValid = false
        }

        return isValid
    }
}
