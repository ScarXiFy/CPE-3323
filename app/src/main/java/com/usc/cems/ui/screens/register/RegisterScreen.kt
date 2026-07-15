package com.usc.cems.ui.screens.register

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.EventNote
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.usc.cems.ui.components.CemsTextField
import com.usc.cems.ui.components.PrimaryButton
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = hiltViewModel(),
) {
    // Listen for register success event
    LaunchedEffect(key1 = viewModel.registerSuccess) {
        viewModel.registerSuccess.collectLatest {
            onNavigateToHome()
        }
    }

    // Animation states for staggered entry
    val headerAlpha = remember { Animatable(0f) }
    val headerOffsetY = remember { Animatable(30f) }
    val cardAlpha = remember { Animatable(0f) }
    val cardOffsetY = remember { Animatable(40f) }
    val footerAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Staggered launch animations
        launch {
            headerAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(600, easing = CubicBezierEasing(0.34f, 1.56f, 0.64f, 1f))
            )
        }
        launch {
            headerOffsetY.animateTo(
                targetValue = 0f,
                animationSpec = tween(600, easing = CubicBezierEasing(0.34f, 1.56f, 0.64f, 1f))
            )
        }

        launch {
            cardAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(800, delayMillis = 100, easing = CubicBezierEasing(0.34f, 1.56f, 0.64f, 1f))
            )
        }
        launch {
            cardOffsetY.animateTo(
                targetValue = 0f,
                animationSpec = tween(800, delayMillis = 100, easing = CubicBezierEasing(0.34f, 1.56f, 0.64f, 1f))
            )
        }

        launch {
            footerAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(800, delayMillis = 300)
            )
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        // Atmospheric background mesh gradients
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF003FB1).copy(alpha = 0.05f), Color.Transparent),
                    center = androidx.compose.ui.geometry.Offset(-size.width * 0.1f, -size.height * 0.1f),
                    radius = size.width * 0.8f
                )
            )
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFFFFC329).copy(alpha = 0.10f), Color.Transparent),
                    center = androidx.compose.ui.geometry.Offset(size.width * 1.1f, size.height * 1.1f),
                    radius = size.width * 0.8f
                )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Brand Header Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .alpha(headerAlpha.value)
                    .offset { IntOffset(0, headerOffsetY.value.toInt()) }
            ) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier.size(64.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.EventNote,
                            contentDescription = "Event Note Icon",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "CarolinianEvents",
                    style = MaterialTheme.typography.displayLarge.copy(fontSize = 32.sp),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Join the community and never miss a campus moment.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Registration Form Card
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(cardAlpha.value)
                    .offset { IntOffset(0, cardOffsetY.value.toInt()) }
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(24.dp)
                    ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Authentication Error Display
                    viewModel.authError?.let { error ->
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(12.dp),
                            textAlign = TextAlign.Center
                        )
                    }

                    // Full Name Field
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Full Name",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.outline,
                            fontWeight = FontWeight.SemiBold
                        )
                        CemsTextField(
                            value = viewModel.fullname,
                            onValueChange = viewModel::onFullnameChange,
                            label = "John Doe",
                            leadingIcon = Icons.Outlined.Person,
                            isError = viewModel.fullnameError != null,
                            supportingText = viewModel.fullnameError?.let {
                                { Text(text = it) }
                            },
                            enabled = !viewModel.isLoading
                        )
                    }

                    // Email Field
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Email Address",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.outline,
                            fontWeight = FontWeight.SemiBold
                        )
                        CemsTextField(
                            value = viewModel.email,
                            onValueChange = viewModel::onEmailChange,
                            label = "name@university.edu",
                            leadingIcon = Icons.Outlined.Email,
                            isError = viewModel.emailError != null,
                            supportingText = viewModel.emailError?.let {
                                { Text(text = it) }
                            },
                            enabled = !viewModel.isLoading
                        )
                    }

                    // Password Field
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Password",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.outline,
                            fontWeight = FontWeight.SemiBold
                        )
                        CemsTextField(
                            value = viewModel.password,
                            onValueChange = viewModel::onPasswordChange,
                            label = "At least 6 characters",
                            leadingIcon = Icons.Outlined.Lock,
                            isPassword = true,
                            isError = viewModel.passwordError != null,
                            supportingText = viewModel.passwordError?.let {
                                { Text(text = it) }
                            },
                            enabled = !viewModel.isLoading
                        )
                    }

                    // Confirm Password Field
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Confirm Password",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.outline,
                            fontWeight = FontWeight.SemiBold
                        )
                        CemsTextField(
                            value = viewModel.confirmPassword,
                            onValueChange = viewModel::onConfirmPasswordChange,
                            label = "Repeat password",
                            leadingIcon = Icons.Outlined.Lock,
                            isPassword = true,
                            isError = viewModel.confirmPasswordError != null,
                            supportingText = viewModel.confirmPasswordError?.let {
                                { Text(text = it) }
                            },
                            enabled = !viewModel.isLoading
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Register Action Button
                    PrimaryButton(
                        text = "Register",
                        onClick = viewModel::register,
                        isLoading = viewModel.isLoading,
                        enabled = !viewModel.isLoading
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Footer Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.alpha(footerAlpha.value)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Already have an account? ",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    TextButton(
                        onClick = onNavigateToLogin,
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.height(24.dp)
                    ) {
                        Text(
                            text = "Log In",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
