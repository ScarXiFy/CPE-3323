package com.usc.cems.ui.screens.editevent

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Title
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.usc.cems.ui.components.CemsTextField
import com.usc.cems.ui.components.CemsTopAppBar
import com.usc.cems.ui.components.PrimaryButton
import kotlinx.coroutines.flow.collectLatest
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEventScreen(
    eventId: String,
    onNavUp: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: EditEventViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var showCategoryDropdown by remember { mutableStateOf(false) }
    val categories = listOf("Academic", "Sports", "Workshop", "Social", "Other")

    LaunchedEffect(key1 = true) {
        viewModel.eventSavedSuccess.collectLatest {
            onNavUp()
        }
    }

    Scaffold(
        topBar = {
            CemsTopAppBar(
                title = "Edit Event",
                onNavUp = onNavUp
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        if (viewModel.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
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
                                MaterialTheme.colorScheme.errorContainer,
                                RoundedCornerShape(8.dp)
                            )
                            .padding(12.dp)
                    )
                }

                // Event Name
                CemsTextField(
                    value = viewModel.title,
                    onValueChange = viewModel::onTitleChange,
                    label = "Event Name",
                    leadingIcon = Icons.Outlined.Title,
                    isError = viewModel.titleError != null,
                    supportingText = viewModel.titleError?.let { msg -> { Text(msg) } }
                )

                // Category Dropdown
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = viewModel.category,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Category,
                                contentDescription = "Category"
                            )
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.ArrowDropDown,
                                contentDescription = "Dropdown"
                            )
                        },
                        isError = viewModel.categoryError != null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showCategoryDropdown = true },
                        enabled = false,
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledBorderColor = if (viewModel.categoryError != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline,
                            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Overlay clickable box
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable { showCategoryDropdown = true }
                    )

                    DropdownMenu(
                        expanded = showCategoryDropdown,
                        onDismissRequest = { showCategoryDropdown = false },
                        modifier = Modifier.fillMaxWidth(0.85f)
                    ) {
                        categories.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(item) },
                                onClick = {
                                    viewModel.onCategoryChange(item)
                                    showCategoryDropdown = false
                                }
                            )
                        }
                    }
                }

                // Custom Category Field (if "Other" is selected)
                if (viewModel.category == "Other") {
                    CemsTextField(
                        value = viewModel.customCategory,
                        onValueChange = viewModel::onCustomCategoryChange,
                        label = "Custom Category",
                        leadingIcon = Icons.Outlined.Category,
                        isError = viewModel.categoryError != null,
                        supportingText = viewModel.categoryError?.let { msg -> { Text(msg) } }
                    )
                }

                // Organizer Field
                CemsTextField(
                    value = viewModel.organizer,
                    onValueChange = viewModel::onOrganizerChange,
                    label = "Organizer Name / Department",
                    leadingIcon = Icons.Outlined.Group,
                    isError = viewModel.organizerError != null,
                    supportingText = viewModel.organizerError?.let { msg -> { Text(msg) } }
                )

                // Date Picker Field
                val calendar = Calendar.getInstance()
                val datePickerDialog = DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        val selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                        viewModel.onDateChange(selectedDate)
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = viewModel.date,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Event Date") },
                        placeholder = { Text("YYYY-MM-DD") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.CalendarToday,
                                contentDescription = "Date"
                            )
                        },
                        isError = viewModel.dateError != null,
                        supportingText = viewModel.dateError?.let { msg -> { Text(msg) } },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = false,
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledBorderColor = if (viewModel.dateError != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline,
                            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable { datePickerDialog.show() }
                    )
                }

                // Start Time & End Time
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val startTimePickerDialog = TimePickerDialog(
                        context,
                        { _, hourOfDay, minute ->
                            val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                            viewModel.onStartTimeChange(selectedTime)
                        },
                        12, 0, true
                    )

                    val endTimePickerDialog = TimePickerDialog(
                        context,
                        { _, hourOfDay, minute ->
                            val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                            viewModel.onEndTimeChange(selectedTime)
                        },
                        13, 0, true
                    )

                    // Start Time Box
                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = viewModel.startTime,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Start Time") },
                            placeholder = { Text("HH:mm") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Schedule,
                                    contentDescription = "Start Time"
                                )
                            },
                            isError = viewModel.startTimeError != null,
                            supportingText = viewModel.startTimeError?.let { msg -> { Text(msg) } },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = false,
                            colors = OutlinedTextFieldDefaults.colors(
                                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                disabledBorderColor = if (viewModel.startTimeError != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline,
                                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable { startTimePickerDialog.show() }
                        )
                    }

                    // End Time Box
                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = viewModel.endTime,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("End Time") },
                            placeholder = { Text("HH:mm") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Schedule,
                                    contentDescription = "End Time"
                                )
                            },
                            isError = viewModel.endTimeError != null,
                            supportingText = viewModel.endTimeError?.let { msg -> { Text(msg) } },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = false,
                            colors = OutlinedTextFieldDefaults.colors(
                                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                disabledBorderColor = if (viewModel.endTimeError != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline,
                                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable { endTimePickerDialog.show() }
                        )
                    }
                }

                // Location Field
                CemsTextField(
                    value = viewModel.location,
                    onValueChange = viewModel::onLocationChange,
                    label = "Location / Venue",
                    leadingIcon = Icons.Outlined.LocationOn,
                    isError = viewModel.locationError != null,
                    supportingText = viewModel.locationError?.let { msg -> { Text(msg) } }
                )

                // Description Field
                CemsTextField(
                    value = viewModel.description,
                    onValueChange = viewModel::onDescriptionChange,
                    label = "Event Description",
                    leadingIcon = Icons.Outlined.Description,
                    isError = viewModel.descriptionError != null,
                    supportingText = viewModel.descriptionError?.let { msg -> { Text(msg) } }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Primary Action Button
                PrimaryButton(
                    text = if (viewModel.isSaving) "Saving Changes..." else "Save Changes",
                    onClick = { viewModel.updateEvent() },
                    enabled = !viewModel.isSaving,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
