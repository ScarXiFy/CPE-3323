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
    data object CreateEvent : Screen

    @Serializable
    data object Bookmarks : Screen

    @Serializable
    data object MyEvents : Screen

    @Serializable
    data object Profile : Screen
}
