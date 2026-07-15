package com.usc.cems.ui.screens.splash

import android.app.Activity
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.usc.cems.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    onNavigateToLogin: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val view = LocalView.current

    // Adjust system status/navigation bars to look seamless on dark background
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val controller = WindowCompat.getInsetsController(window, view)
            controller.isAppearanceLightStatusBars = false
            controller.isAppearanceLightNavigationBars = false
        }
    }

    // Animation States
    val logoScale = remember { Animatable(0.85f) }
    val logoAlpha = remember { Animatable(0f) }
    val textAlpha = remember { Animatable(0f) }
    val progressAlpha = remember { Animatable(0f) }

    // Run animations and navigation trigger
    LaunchedEffect(Unit) {
        // Logo Animation (Delay 100ms)
        launch {
            delay(100)
            launch {
                logoScale.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = 800,
                        easing = CubicBezierEasing(0.4f, 0.0f, 0.2f, 1f)
                    )
                )
            }
            launch {
                logoAlpha.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = 800,
                        easing = CubicBezierEasing(0.4f, 0.0f, 0.2f, 1f)
                    )
                )
            }
        }

        // Text Brand Animation (Delay 300ms)
        launch {
            delay(300)
            textAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 800,
                    easing = CubicBezierEasing(0.4f, 0.0f, 0.2f, 1f)
                )
            )
        }

        // Circular Progress Animation (Delay 500ms)
        launch {
            delay(500)
            progressAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 800,
                    easing = CubicBezierEasing(0.4f, 0.0f, 0.2f, 1f)
                )
            )
        }

        // Simulate 2.5 seconds splash display before navigating to login
        delay(2500)
        onNavigateToLogin()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF003FB1)), // Primary blue brand color
        contentAlignment = Alignment.Center
    ) {
        // Subtle gradient mesh background decorative elements
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Top-left primary container light blue mesh blur
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF1A56DB).copy(alpha = 0.25f), Color.Transparent),
                    center = androidx.compose.ui.geometry.Offset(-size.width * 0.1f, -size.height * 0.1f),
                    radius = size.width * 0.8f
                )
            )
            // Bottom-right secondary container amber/gold mesh blur
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFFFFC329).copy(alpha = 0.15f), Color.Transparent),
                    center = androidx.compose.ui.geometry.Offset(size.width * 1.1f, size.height * 1.1f),
                    radius = size.width * 0.8f
                )
            )
        }

        // Content layout
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Animated Logo Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 24.dp),
                modifier = Modifier
                    .size(140.dp)
                    .scale(logoScale.value)
                    .alpha(logoAlpha.value)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize().padding(16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_logo),
                        contentDescription = "CarolinianEvents Logo",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Staggered Fade-in for Brand Title & Subtitle
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.alpha(textAlpha.value)
            ) {
                Text(
                    text = "CarolinianEvents",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "DISCOVER CAMPUS LIFE",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White.copy(alpha = 0.7f),
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 2.4.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Loading Indicator
            Box(
                modifier = Modifier.alpha(progressAlpha.value),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(48.dp),
                    strokeWidth = 4.dp
                )
            }
        }
    }
}