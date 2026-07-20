package com.usc.cems.ui.screens.home

import androidx.compose.foundation.horizontalScroll
import com.usc.cems.ui.components.formattedDate
import com.usc.cems.ui.components.formattedTimeRange
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.usc.cems.ui.components.BottomNavDestination
import com.usc.cems.ui.components.CategoryChip
import com.usc.cems.ui.components.CemsBottomNavBar
import com.usc.cems.ui.components.CemsTopAppBar
import com.usc.cems.ui.components.EventCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onEventClick: (String) -> Unit = {},
    onNavigateToRegistered: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToCreateEvent: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    Scaffold(
        topBar = {
            CemsTopAppBar(title = "CarolinianEvents")
        },
        bottomBar = {
            CemsBottomNavBar(
                currentDestination = BottomNavDestination.Home,
                isAdmin = viewModel.isAdmin,
                onDestinationChanged = { destination ->
                    when (destination) {
                        BottomNavDestination.Home -> { /* Already here */ }
                        BottomNavDestination.Registered -> onNavigateToRegistered()
                        BottomNavDestination.Profile -> onNavigateToProfile()
                    }
                }
            )
        },

        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Search Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = viewModel.searchQuery,
                    onValueChange = viewModel::onSearchQueryChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    placeholder = {
                        Text(
                            text = "Search events, clubs, or venues",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.outline
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Icon",
                            tint = MaterialTheme.colorScheme.outline
                        )
                    },
                    shape = CircleShape,
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        disabledBorderColor = Color.Transparent
                    )
                )
            }

            // Category Chips Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                viewModel.categories.forEach { category ->
                    CategoryChip(
                        label = category,
                        selected = viewModel.selectedCategory == category,
                        onClick = { viewModel.selectCategory(category) }
                    )
                }
            }

            // Upcoming Events Section Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Upcoming Events",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            }

            // Upcoming Events List
            val upcomingList = viewModel.upcomingEvents
            if (upcomingList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No upcoming events found matching criteria",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    upcomingList.forEach { event ->
                        val (categoryColor, categoryOnColor) = when (event.category.lowercase()) {
                            "workshop" -> MaterialTheme.colorScheme.primary to MaterialTheme.colorScheme.onPrimary
                            "sports" -> MaterialTheme.colorScheme.secondary to MaterialTheme.colorScheme.onSecondary
                            "cultural" -> MaterialTheme.colorScheme.tertiary to MaterialTheme.colorScheme.onTertiary
                            else -> MaterialTheme.colorScheme.primary to MaterialTheme.colorScheme.onPrimary
                        }

                        EventCard(
                            category = event.category,
                            categoryColor = categoryColor,
                            categoryOnColor = categoryOnColor,
                            title = event.title,
                            date = event.formattedDate(),
                            time = event.formattedTimeRange(),
                            location = event.location,
                            onClick = { onEventClick(event.id) }
                        )
                    }
                }
            }

            // Past Events Section Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Past Events",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            }

            // Past Events List
            val pastList = viewModel.pastEvents
            if (pastList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No past events found matching criteria",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    pastList.forEach { event ->
                        val (categoryColor, categoryOnColor) = when (event.category.lowercase()) {
                            "workshop" -> MaterialTheme.colorScheme.primary to MaterialTheme.colorScheme.onPrimary
                            "sports" -> MaterialTheme.colorScheme.secondary to MaterialTheme.colorScheme.onSecondary
                            "cultural" -> MaterialTheme.colorScheme.tertiary to MaterialTheme.colorScheme.onTertiary
                            else -> MaterialTheme.colorScheme.primary to MaterialTheme.colorScheme.onPrimary
                        }

                        EventCard(
                            category = event.category,
                            categoryColor = categoryColor.copy(alpha = 0.6f),
                            categoryOnColor = categoryOnColor,
                            title = event.title,
                            date = event.formattedDate(),
                            time = event.formattedTimeRange(),
                            location = event.location,
                            onClick = { onEventClick(event.id) }
                        )
                    }
                }
            }
        }
    }
}
