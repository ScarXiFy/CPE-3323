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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import androidx.navigation.toRoute
import javax.inject.Inject

@HiltViewModel
class CreateEventViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val eventId: String? = try {
        savedStateHandle.toRoute<com.usc.cems.ui.navigation.Screen.UpdateEvent>().eventId
    } catch (e: Exception) {
        savedStateHandle.get<String>("eventId")
    }
    val isEditMode: Boolean
        get() = eventId != null

    private var originalEvent: Event? = null

    init {
        eventId?.let { id ->
            viewModelScope.launch {
                eventRepository.getEvents().collect { events ->
                    val event = events.find { it.id == id } ?: eventRepository.getEventById(id)
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

        val timeParts = datePart.trim().split(" ")
        if (timeParts.size == 2 && timeParts[0].matches(Regex("\\d{4}-\\d{2}-\\d{2}")) && timeParts[1].matches(Regex("\\d{2}:\\d{2}"))) {
            date = timeParts[0]
            startTime = timeParts[1]
        } else {
            date = if (timeParts.isNotEmpty()) timeParts[0] else datePart
            startTime = if (timeParts.size > 1) timeParts[1] else ""
        }

        val endTimeParts = timePart.trim().split(" ")
        if (endTimeParts.size == 2 && endTimeParts[1].matches(Regex("\\d{2}:\\d{2}"))) {
            endTime = endTimeParts[1]
        } else if (endTimeParts.isNotEmpty() && endTimeParts[0].matches(Regex("\\d{2}:\\d{2}"))) {
            endTime = endTimeParts[0]
        } else {
            endTime = timePart.trim()
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

    var customCategory by mutableStateOf("")
        private set
    var customCategoryError by mutableStateOf<String?>(null)
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

    var imageUrl by mutableStateOf("")
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
        if (value != "Other") {
            customCategoryError = null
        }
    }

    fun onCustomCategoryChange(value: String) {
        customCategory = value
        customCategoryError = null
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

    fun onLocationChange(value: String) {
        location = value
        locationError = null
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
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            val startDateTime = LocalDateTime.parse("$date $startTime", formatter)
            val endDateTime = LocalDateTime.parse("$date $endTime", formatter)
            val calculatedStatus = calculateEventStatus(startDateTime, endDateTime)

            val finalCategory = if (category == "Other") customCategory.trim() else category
            
            val newEvent = Event(
                id = UUID.randomUUID().toString(),
                title = title.trim(),
                category = finalCategory,
                //imageUrl = imageUrl,
                dateTime = "$date $startTime • $endTime",
                location = location.trim(),
                //spotsLeft = "Unlimited spots",
                description = description.trim(),
                organizerName = organizer.trim(),
                organizerLogo = "https://lh3.googleusercontent.com/aida-public/AB6AXuDBkuM5btIeSGlZYkviOI_ikadaa7meJOX_vVgO0WFCh5PsjNAAqu5bZsfixtExgIjvBFWz_jS7Q67ardG8KKf-FK4oEZEdzW9ClrnnVFFPhgdelnlE8H6Ul2FeMYCWGilxdj2UU7U1Q_kofBpiY28RqlOuM0rdQYKPxOAdpvj6WTx5EZ3MkAFSUAa7NQQrYYwPXPe7eaGw6wA4BL4Sg_phOxO4WChvmlhNA3v6tdEMBq-jlcDdGeE0FQ",
                attendingCount = "0 students attending",
                registrationStatus = calculatedStatus,
                status = calculatedStatus
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
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            val startDateTime = LocalDateTime.parse("$date $startTime", formatter)
            val endDateTime = LocalDateTime.parse("$date $endTime", formatter)
            val calculatedStatus = calculateEventStatus(startDateTime, endDateTime)

            val finalCategory = if (category == "Other") customCategory.trim() else category
            
            val updatedEvent = Event(
                id = eventId!!,
                title = title.trim(),
                category = finalCategory,
                //imageUrl = imageUrl,
                dateTime = "$date $startTime • $endTime",
                location = location.trim(),
                //spotsLeft = originalEvent?.spotsLeft ?: "Unlimited spots",
                description = description.trim(),
                organizerName = organizer.trim(),
                organizerLogo = originalEvent?.organizerLogo ?: "https://lh3.googleusercontent.com/aida-public/AB6AXuDBkuM5btIeSGlZYkviOI_ikadaa7meJOX_vVgO0WFCh5PsjNAAqu5bZsfixtExgIjvBFWz_jS7Q67ardG8KKf-FK4oEZEdzW9ClrnnVFFPhgdelnlE8H6Ul2FeMYCWGilxdj2UU7U1Q_kofBpiY28RqlOuM0rdQYKPxOAdpvj6WTx5EZ3MkAFSUAa7NQQrYYwPXPe7eaGw6wA4BL4Sg_phOxO4WChvmlhNA3v6tdEMBq-jlcDdGeE0FQ",
                attendingCount = originalEvent?.attendingCount ?: "0 students attending",
                registrationStatus = calculatedStatus,
                status = calculatedStatus
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

    private fun calculateEventStatus(start: LocalDateTime, end: LocalDateTime): String {
        val now = LocalDateTime.now()
        return when {
            now.isBefore(start) -> "Upcoming"
            now.isAfter(end) -> "Completed"
            else -> "Ongoing"
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true
        titleError = null
        categoryError = null
        customCategoryError = null
        organizerError = null
        dateError = null
        startTimeError = null
        endTimeError = null
        descriptionError = null
        locationError = null
        validationError = null

        if (title.isBlank()) {
            titleError = "Event Name is required"
            isValid = false
        }
        if (category.isBlank()) {
            categoryError = "Category selection is required"
            isValid = false
        }
        if (category == "Other" && customCategory.isBlank()) {
            customCategoryError = "Custom Category name is required"
            isValid = false
        }
        if (organizer.isBlank()) {
            organizerError = "Organizer name is required"
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
            val currentDateTime = LocalDateTime.now()

            var enforceStartNotInPast = true
            if (isEditMode && originalEvent != null) {
                val parts = originalEvent!!.dateTime.split(" • ")
                val origStart = parts.getOrNull(0) ?: ""
                val origStartParts = origStart.split(" ")
                if (date == origStartParts.getOrNull(0) && startTime == origStartParts.getOrNull(1)) {
                    enforceStartNotInPast = false
                }
            }

            if (enforceStartNotInPast && startDateTime.isBefore(currentDateTime)) {
                dateError = "Start Time cannot be in the past"
                validationError = "Start Time cannot be in the past"
                isValid = false
            }
            if (!endDateTime.isAfter(startDateTime)) {
                endTimeError = "End Time must be after Start Time"
                validationError = "End Time must be after Start Time"
                isValid = false
            }
        } catch (e: Exception) {
            validationError = "Invalid Date or Time format selected"
            isValid = false
        }

        return isValid
    }
}
