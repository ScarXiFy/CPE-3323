package com.usc.cems.ui.screens.event

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.usc.cems.ui.components.CategoryBadge
import com.usc.cems.ui.components.CemsTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailsScreen(
    eventId: String,
    onNavUp: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: EventDetailsViewModel = hiltViewModel(),
) {
    // Load event details on initialization
    LaunchedEffect(eventId) {
        viewModel.loadEvent(eventId)
    }

    val event = viewModel.event

    Scaffold(
        topBar = {
            CemsTopAppBar(
                title = "Event Details",
                onNavUp = onNavUp
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        if (event == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Loading event details...",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            val (categoryColor, categoryOnColor) = when (event.category.lowercase()) {
                "workshop" -> MaterialTheme.colorScheme.primary to MaterialTheme.colorScheme.onPrimary
                "sports" -> MaterialTheme.colorScheme.secondary to MaterialTheme.colorScheme.onSecondary
                "cultural" -> MaterialTheme.colorScheme.tertiary to MaterialTheme.colorScheme.onTertiary
                else -> MaterialTheme.colorScheme.primary to MaterialTheme.colorScheme.onPrimary
            }

            val statusColor = when (event.status.lowercase()) {
                "upcoming" -> MaterialTheme.colorScheme.primary
                "ongoing" -> MaterialTheme.colorScheme.secondary
                "completed" -> MaterialTheme.colorScheme.outline
                else -> MaterialTheme.colorScheme.primary
            }

            val parts = event.dateTime.split(" • ")
            val startPart = parts.getOrNull(0) ?: ""
            val endPart = parts.getOrNull(1) ?: ""

            val startSplit = startPart.split(" ")
            val startDateVal = startSplit.getOrNull(0) ?: startPart
            val startTimeVal = startSplit.getOrNull(1) ?: ""

            val endSplit = endPart.split(" ")
            val endDateVal = endSplit.getOrNull(0) ?: endPart
            val endTimeVal = endSplit.getOrNull(1) ?: ""

            val dateDisplay = if (startDateVal == endDateVal || endDateVal.isBlank()) {
                startDateVal
            } else {
                "$startDateVal to $endDateVal"
            }

            val timeDisplay = if (startTimeVal.isNotBlank() && endTimeVal.isNotBlank()) {
                "$startTimeVal - $endTimeVal"
            } else {
                startTimeVal.ifBlank { "All Day" }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                // Title, Category & Status badges
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CategoryBadge(
                            label = event.category,
                            containerColor = categoryColor,
                            contentColor = categoryOnColor
                        )
                        
                        // Status Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(statusColor.copy(alpha = 0.12f))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = event.status.uppercase(),
                                style = MaterialTheme.typography.labelMedium,
                                color = statusColor,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Text(
                        text = event.title,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 36.sp
                    )
                }

                // Metadata Details Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(16.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Organizer
                        Column {
                            Text(
                                text = "ORGANIZER",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.outline,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = event.organizerName,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Date
                        Column {
                            Text(
                                text = "DATE",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.outline,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = dateDisplay,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Time
                        Column {
                            Text(
                                text = "TIME",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.outline,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = timeDisplay,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Location
                        Column {
                            Text(
                                text = "LOCATION",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.outline,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = event.location,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Description Segment
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "About Event",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = event.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 24.sp
                    )
                }

                // Join/Cancel Registration Button
                val isRegistered = viewModel.isRegistered
                val buttonColor = if (isRegistered) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.primary
                }
                val buttonText = if (isRegistered) "Cancel Joining" else "Join Event"

                Button(
                    onClick = { viewModel.toggleRegistration() },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = buttonColor
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(
                        text = buttonText,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
