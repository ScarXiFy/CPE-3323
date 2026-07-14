package com.usc.cems.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Branded top app bar used across the app.
 *
 * @param title       Text displayed in the center (defaults to brand name).
 * @param onNavUp     If non-null, shows a back arrow that calls this lambda.
 * @param actions     Trailing icons slot (e.g. bookmark, share).
 * @param scrollBehavior Optional scroll behaviour for collapsing/pinned bars.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CemsTopAppBar(
    title: String = "CarolinianEvents",
    onNavUp: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        },
        navigationIcon = {
            if (onNavUp != null) {
                IconButton(onClick = onNavUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Navigate back",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
        scrollBehavior = scrollBehavior,
    )
}
