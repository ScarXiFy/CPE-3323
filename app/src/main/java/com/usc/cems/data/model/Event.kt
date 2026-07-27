package com.usc.cems.data.model

import com.usc.cems.ui.components.computeStatus

data class Event(
    val id: String,
    val title: String,
    val category: String,
    //val imageUrl: String = "",
    val dateTime: String,
    val location: String,
    val description: String = "",
    val organizerName: String = "",
    //val organizerLogo: String = "",
    val attendingCount: String = "0 students are attending",
    //val registrationStatus: String = "Open",
) {
    val status: String
        get() = computeStatus()
}
