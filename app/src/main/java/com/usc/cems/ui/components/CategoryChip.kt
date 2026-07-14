package com.usc.cems.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Pill-shaped filter chip used in the Home screen category row.
 *
 * When [selected] is true, it renders with the primary colour fill and white text,
 * matching the wireframe's active chip state.
 *
 * @param label    Text to display on the chip.
 * @param selected Whether this chip is currently active.
 * @param onClick  Callback when the chip is tapped.
 */
@Composable
fun CategoryChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
            )
        },
        shape = CircleShape,
        modifier = modifier.height(36.dp),
        colors = FilterChipDefaults.filterChipColors(
            // Active state
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
            // Inactive state
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selected,
            borderColor = MaterialTheme.colorScheme.outlineVariant,
            selectedBorderColor = MaterialTheme.colorScheme.primary,
            borderWidth = 0.dp,
            selectedBorderWidth = 0.dp,
        ),
    )
}
