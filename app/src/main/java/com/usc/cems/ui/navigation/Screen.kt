package com.usc.cems.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Splash : Screen

    @Serializable
    data object Login : Screen

    @Serializable
    data object Register : Screen

    @Serializable
    data object Home : Screen

    @Serializable
    data class EventDetails(val eventId: String) : Screen

    @Serializable
    data class CreateEvent(val eventId: String? = null) : Screen

    /**
     * Repurposed as the "Registered Events" screen per the wireframe —
     * shows events the student has RSVP'd to (upcoming + past).
     */
    @Serializable
    data object MyEvents : Screen

    @Serializable
    data object Profile : Screen

    @Serializable
    data object AdminDashboard : Screen
}
