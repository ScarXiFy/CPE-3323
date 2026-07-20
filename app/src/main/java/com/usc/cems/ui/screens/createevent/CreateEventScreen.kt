package com.usc.cems.ui.screens.createevent

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Title
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.usc.cems.ui.components.CemsTextField
import com.usc.cems.ui.components.CemsTopAppBar
import com.usc.cems.ui.components.PrimaryButton
import kotlinx.coroutines.flow.collectLatest
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventScreen(
    eventId: String? = null,
    onNavUp: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: CreateEventViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var showCategoryDropdown by remember { mutableStateOf(false) }
    val categories = listOf("Academic", "Sports", "Workshop", "Social", "Other")

    // Listen to success state
    LaunchedEffect(key1 = true) {
        viewModel.creationSuccess.collectLatest {
            onNavUp()
        }
    }

    Scaffold(
        topBar = {
            CemsTopAppBar(
                title = if (viewModel.isEditMode) "Update Event" else "Create Event",
                onNavUp = onNavUp
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Validation Error Box
            viewModel.validationError?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.errorContainer,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp),
                    textAlign = TextAlign.Center
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = if (viewModel.isEditMode) "Edit Event" else "Create New Event",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = if (viewModel.isEditMode) "Modify the details of your event below." else "Fill in the details below to post the event to the Carolinian community.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Event Name
            CemsTextField(
                value = viewModel.title,
                onValueChange = viewModel::onTitleChange,
                label = "Event Name",
                leadingIcon = Icons.Outlined.Title,
                isError = viewModel.titleError != null,
                supportingText = viewModel.titleError?.let { { Text(it) } }
            )

            // Category Selector Dropdown
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Category",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.outline,
                    fontWeight = FontWeight.SemiBold
                )
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = viewModel.category,
                        onValueChange = {},
                        readOnly = true,
                        placeholder = { Text("Select category") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Category,
                                contentDescription = "Category",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.ArrowDropDown,
                                contentDescription = "Dropdown",
                                modifier = Modifier.clickable { showCategoryDropdown = true }
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showCategoryDropdown = true },
                        shape = RoundedCornerShape(12.dp),
                        isError = viewModel.categoryError != null,
                        supportingText = viewModel.categoryError?.let { { Text(it) } }
                    )

                    DropdownMenu(
                        expanded = showCategoryDropdown,
                        onDismissRequest = { showCategoryDropdown = false },
                        modifier = Modifier.fillMaxWidth(0.85f)
                    ) {
                        categories.forEach { categoryName ->
                            DropdownMenuItem(
                                text = { Text(categoryName) },
                                onClick = {
                                    viewModel.onCategoryChange(categoryName)
                                    showCategoryDropdown = false
                                }
                            )
                        }
                    }
                }
            }

            // Custom Category Text Box (Visible ONLY if Category == "Other")
            if (viewModel.category == "Other") {
                CemsTextField(
                    value = viewModel.customCategory,
                    onValueChange = viewModel::onCustomCategoryChange,
                    label = "Custom Category Name",
                    leadingIcon = Icons.Outlined.Category,
                    isError = viewModel.customCategoryError != null,
                    supportingText = viewModel.customCategoryError?.let { { Text(it) } }
                )
            }

            // Event Organizer
            CemsTextField(
                value = viewModel.organizer,
                onValueChange = viewModel::onOrganizerChange,
                label = "Event Organizer",
                leadingIcon = Icons.Outlined.Group,
                isError = viewModel.organizerError != null,
                supportingText = viewModel.organizerError?.let { { Text(it) } }
            )

            // Date Picker Field
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Date",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.outline,
                    fontWeight = FontWeight.SemiBold
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val calendar = Calendar.getInstance()
                            DatePickerDialog(
                                context,
                                { _, year, month, dayOfMonth ->
                                    val formattedDate = String.format("%d-%02d-%02d", year, month + 1, dayOfMonth)
                                    viewModel.onDateChange(formattedDate)
                                },
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                            ).show()
                        }
                ) {
                    OutlinedTextField(
                        value = viewModel.date,
                        onValueChange = {},
                        readOnly = true,
                        enabled = false,
                        placeholder = { Text("Select event date") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.CalendarToday,
                                contentDescription = "Date",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledBorderColor = if (viewModel.dateError != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline,
                            disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        ),
                        isError = viewModel.dateError != null,
                        supportingText = viewModel.dateError?.let { { Text(it) } }
                    )
                }
            }

            // Start Time Picker Field
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Start Time",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.outline,
                    fontWeight = FontWeight.SemiBold
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val calendar = Calendar.getInstance()
                            TimePickerDialog(
                                context,
                                { _, hourOfDay, minute ->
                                    val formattedTime = String.format("%02d:%02d", hourOfDay, minute)
                                    viewModel.onStartTimeChange(formattedTime)
                                },
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                true
                            ).show()
                        }
                ) {
                    OutlinedTextField(
                        value = viewModel.startTime,
                        onValueChange = {},
                        readOnly = true,
                        enabled = false,
                        placeholder = { Text("Select start time") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Schedule,
                                contentDescription = "Start Time",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledBorderColor = if (viewModel.startTimeError != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline,
                            disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        ),
                        isError = viewModel.startTimeError != null,
                        supportingText = viewModel.startTimeError?.let { { Text(it) } }
                    )
                }
            }

            // End Time Picker Field
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "End Time",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.outline,
                    fontWeight = FontWeight.SemiBold
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val calendar = Calendar.getInstance()
                            TimePickerDialog(
                                context,
                                { _, hourOfDay, minute ->
                                    val formattedTime = String.format("%02d:%02d", hourOfDay, minute)
                                    viewModel.onEndTimeChange(formattedTime)
                                },
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                true
                            ).show()
                        }
                ) {
                    OutlinedTextField(
                        value = viewModel.endTime,
                        onValueChange = {},
                        readOnly = true,
                        enabled = false,
                        placeholder = { Text("Select end time") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Schedule,
                                contentDescription = "End Time",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledBorderColor = if (viewModel.endTimeError != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline,
                            disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        ),
                        isError = viewModel.endTimeError != null,
                        supportingText = viewModel.endTimeError?.let { { Text(it) } }
                    )
                }
            }

            // Location
            CemsTextField(
                value = viewModel.location,
                onValueChange = viewModel::onLocationChange,
                label = "Location",
                leadingIcon = Icons.Outlined.LocationOn,
                isError = viewModel.locationError != null,
                supportingText = viewModel.locationError?.let { { Text(it) } }
            )

            // Event Description Box
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Event Description",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.outline,
                    fontWeight = FontWeight.SemiBold
                )
                OutlinedTextField(
                    value = viewModel.description,
                    onValueChange = viewModel::onDescriptionChange,
                    placeholder = { Text("Describe your event...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Description,
                            contentDescription = "Description",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 5,
                    isError = viewModel.descriptionError != null,
                    supportingText = viewModel.descriptionError?.let { { Text(it) } }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action submit button
            PrimaryButton(
                text = if (viewModel.isEditMode) "Save Changes" else "Create Event",
                onClick = viewModel::saveEvent,
                isLoading = viewModel.isLoading,
                enabled = !viewModel.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
            )

            if (viewModel.isEditMode) {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = viewModel::deleteEvent,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    ),
                    shape = RoundedCornerShape(99.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    enabled = !viewModel.isLoading
                ) {
                    Text(
                        text = "Delete Event",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
