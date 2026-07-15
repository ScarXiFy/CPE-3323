package com.usc.cems.data.model

data class Event(
    val id: String,
    val title: String,
    val category: String,
    val imageUrl: String,
    val dateTime: String,
    val location: String,
    val spotsLeft: String? = null
)
