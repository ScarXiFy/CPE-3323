package com.usc.cems.ui.components

import androidx.compose.ui.graphics.Color


fun getCategoryColor(category: String): Color {
    return when (category.trim().lowercase()) {
        "academic" -> Color(0xFFF59E0B)  // Yellow / Amber
        "sports", "sport" -> Color(0xFFF97316)  // Orange
        "workshop", "workshops" -> Color(0xFF10B981)  // Green
        "social" -> Color(0xFF8B5CF6)  // Purple
        else -> Color(0xFF3B82F6)  // Blue ("Other" or custom category)
    }
}
