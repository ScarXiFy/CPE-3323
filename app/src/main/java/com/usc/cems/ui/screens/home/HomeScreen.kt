package com.usc.cems.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

// TODO: Milestone 5 – implement full home screen design
@Composable
fun HomeScreen(
    onEventClick: (String) -> Unit = {},
    onNavigateToRegistered: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToCreateEvent: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "Home Screen", style = MaterialTheme.typography.headlineMedium)
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(onClick = onNavigateToRegistered) {Text("Registered")}
            Button(onClick = onNavigateToProfile) {Text("Profile")}
        }

    }
}
