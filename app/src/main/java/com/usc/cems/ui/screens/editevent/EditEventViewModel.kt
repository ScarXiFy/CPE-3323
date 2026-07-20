package com.usc.cems.ui.screens.editevent

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.usc.cems.data.model.Event
import com.usc.cems.data.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class EditEventViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val eventId: String = savedStateHandle.get<String>("eventId")
        ?: try {
            savedStateHandle.toRoute<com.usc.cems.ui.navigation.Screen.UpdateEvent>().eventId
        } catch (e: Exception) {
            ""
        }

    private var originalEvent: Event? = null

    var title by mutableStateOf("")
        private set
    var titleError by mutableStateOf<String?>(null)
        private set

    var category by mutableStateOf("Academic")
        private set
    var customCategory by mutableStateOf("")
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

    var location by mutableStateOf("")
        private set
    var locationError by mutableStateOf<String?>(null)
        private set

    var description by mutableStateOf("")
        private set
    var descriptionError by mutableStateOf<String?>(null)
        private set

    var validationError by mutableStateOf<String?>(null)
        private set

    var isSaving by mutableStateOf(false)
        private set

    var isLoading by mutableStateOf(true)
        private set

    private val _eventSavedSuccess = MutableSharedFlow<Boolean>()
    val eventSavedSuccess: SharedFlow<Boolean> = _eventSavedSuccess.asSharedFlow()

    init {
        if (eventId.isNotBlank()) {
            viewModelScope.launch {
                eventRepository.getEvents().collect { events ->
                    val event = events.find { it.id == eventId } ?: eventRepository.getEventById(eventId)
                    if (event != null && originalEvent == null) {
                        populateEventDetails(event)
                    }
                }
            }
        }
    }

    private fun populateEventDetails(event: Event) {
        originalEvent = event
        title = event.title

        val defaultCategories = listOf("Academic", "Sports", "Workshop", "Social")
        if (event.category in defaultCategories) {
            category = event.category
            customCategory = ""
        } else {
            category = "Other"
            customCategory = event.category
        }

        organizer = event.organizerName
        location = event.location
        description = event.description

        val parts = event.dateTime.split(" • ")
        val datePart = parts.getOrNull(0) ?: ""
        val timePart = parts.getOrNull(1) ?: ""

        val dateTokens = datePart.split(" ")
        if (dateTokens.isNotEmpty() && dateTokens[0].matches(Regex("\\d{4}-\\d{2}-\\d{2}"))) {
            date = dateTokens[0]
        } else {
            date = datePart
        }

        val timeTokens = timePart.split(" - ")
        if (timeTokens.size == 2) {
            startTime = timeTokens[0].trim()
            endTime = timeTokens[1].trim()
        } else {
            val fullTimeTokens = datePart.split(" ")
            if (fullTimeTokens.size >= 2) {
                startTime = fullTimeTokens[1].trim()
            }
            if (timePart.isNotBlank()) {
                endTime = timePart.trim()
            }
        }

        isLoading = false
    }

    fun onTitleChange(newTitle: String) {
        title = newTitle
        titleError = null
        validationError = null
    }

    fun onCategoryChange(newCategory: String) {
        category = newCategory
        categoryError = null
        validationError = null
    }

    fun onCustomCategoryChange(newCustomCategory: String) {
        customCategory = newCustomCategory
        categoryError = null
        validationError = null
    }

    fun onOrganizerChange(newOrganizer: String) {
        organizer = newOrganizer
        organizerError = null
        validationError = null
    }

    fun onDateChange(newDate: String) {
        date = newDate
        dateError = null
        validationError = null
    }

    fun onStartTimeChange(newStartTime: String) {
        startTime = newStartTime
        startTimeError = null
        validationError = null
    }

    fun onEndTimeChange(newEndTime: String) {
        endTime = newEndTime
        endTimeError = null
        validationError = null
    }

    fun onLocationChange(newLocation: String) {
        location = newLocation
        locationError = null
        validationError = null
    }

    fun onDescriptionChange(newDescription: String) {
        description = newDescription
        descriptionError = null
        validationError = null
    }

    private fun validateInputs(): Boolean {
        titleError = null
        categoryError = null
        organizerError = null
        dateError = null
        startTimeError = null
        endTimeError = null
        locationError = null
        descriptionError = null
        validationError = null

        var isValid = true

        if (title.isBlank()) {
            titleError = "Event Name is required"
            isValid = false
        }
        val finalCategory = if (category == "Other") customCategory else category
        if (finalCategory.isBlank()) {
            categoryError = "Category is required"
            isValid = false
        }
        if (organizer.isBlank()) {
            organizerError = "Organizer Name is required"
            isValid = false
        }
        if (date.isBlank()) {
            dateError = "Date is required"
            isValid = false
        }
        if (startTime.isBlank()) {
            startTimeError = "Start Time is required"
            isValid = false
        }
        if (endTime.isBlank()) {
            endTimeError = "End Time is required"
            isValid = false
        }
        if (location.isBlank()) {
            locationError = "Location is required"
            isValid = false
        }
        if (description.isBlank()) {
            descriptionError = "Event Description is required"
            isValid = false
        }

        if (!isValid) return false

        try {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            val startDateTime = LocalDateTime.parse("$date $startTime", formatter)
            val endDateTime = LocalDateTime.parse("$date $endTime", formatter)

            if (!endDateTime.isAfter(startDateTime)) {
                endTimeError = "End Time must be after Start Time"
                validationError = "End Time must be after Start Time"
                isValid = false
            }
        } catch (e: Exception) {
            // Ignore format parsing issues if user typed non-standard strings
        }

        return isValid
    }

    fun updateEvent() {
        if (!validateInputs()) return

        val finalCategory = if (category == "Other") customCategory else category
        val formattedDateTime = "$date $startTime • $endTime"

        val updatedEvent = (originalEvent ?: Event(
            id = eventId,
            title = title.trim(),
            category = finalCategory.trim(),
            dateTime = formattedDateTime,
            location = location.trim()
        )).copy(
            title = title.trim(),
            category = finalCategory.trim(),
            organizerName = organizer.trim(),
            dateTime = formattedDateTime,
            location = location.trim(),
            description = description.trim(),
        )

        viewModelScope.launch {
            isSaving = true
            try {
                eventRepository.updateEvent(updatedEvent)
                _eventSavedSuccess.emit(true)
            } catch (e: Exception) {
                validationError = "Failed to update event: ${e.localizedMessage}"
            } finally {
                isSaving = false
            }
        }
    }
}
