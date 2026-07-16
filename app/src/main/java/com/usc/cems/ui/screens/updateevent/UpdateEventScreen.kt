package com.usc.cems.ui.screens.updateevent

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.usc.cems.ui.screens.createevent.CreateEventScreen

@Composable
fun UpdateEventScreen(
    eventId: String,
    onNavUp: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    CreateEventScreen(
        eventId = eventId,
        onNavUp = onNavUp,
        modifier = modifier
    )
}
