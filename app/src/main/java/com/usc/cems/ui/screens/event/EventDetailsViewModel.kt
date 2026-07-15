package com.usc.cems.ui.screens.event

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.usc.cems.data.model.Event
import com.usc.cems.data.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EventDetailsViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {

    var event by mutableStateOf<Event?>(null)
        private set

    fun loadEvent(id: String) {
        event = eventRepository.getEventById(id) ?: Event(
            id = "hackathon",
            title = "University Hackathon 2024",
            category = "Technology",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuC-I1nZGdJFlM9v3BflkRsFGD56h_alsClbpZY01mhTE3Cjj6bEdsUx7h39k8fwEN93kXk_qi2KlMh1TLwGW4jsinMr554i8uOJXuY1aYA05BbGph0e3depru9o2uX3HRR425sM2d-H3nt7TQQ6uPJ80B_R7ZnjKbNcZiT01ORBsF59NdBbtPkMLuj2zUsZKNrjFIlSGZeCoED8xtBnLKdBBCvpJa_FsnOY16mJ7uJvGaeuIT27nIUAoA",
            dateTime = "Saturday, Oct 12 • 09:00 AM - 09:00 PM EST",
            location = "Innovation Commons, Building 4, Floor 2 • Campus East",
            spotsLeft = "42 spots left",
            description = "Join us for the annual University Hackathon! Bring your ideas and coding skills to compete for prizes, network with industry professionals, and collaborate with your fellow Carolinians.\n\nWhether you're a seasoned developer or a first-time coder, there's a place for you here. We'll provide the fuel (food and drinks), high-speed internet, and a suite of mentors to help you build the next big thing. Don't miss out on this 12-hour innovation marathon!",
            organizerName = "Computer Engineering Council",
            organizerLogo = "https://lh3.googleusercontent.com/aida-public/AB6AXuDBkuM5btIeSGlZYkviOI_ikadaa7meJOX_vVgO0WFCh5PsjNAAqu5bZsfixtExgIjvBFWz_jS7Q67ardG8KKf-FK4oEZEdzW9ClrnnVFFPhgdelnlE8H6Ul2FeMYCWGilxdj2UU7U1Q_kofBpiY28RqlOuM0rdQYKPxOAdpvj6WTx5EZ3MkAFSUAa7NQQrYYwPXPe7eaGw6wA4BL4Sg_phOxO4WChvmlhNA3v6tdEMBq-jlcDdGeE0FQ",
            attendingCount = "42 students are attending",
            registrationStatus = "Open until Oct 10"
        )
    }
}
