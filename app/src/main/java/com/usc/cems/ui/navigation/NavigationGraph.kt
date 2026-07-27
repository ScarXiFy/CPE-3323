package com.usc.cems.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.usc.cems.data.repository.AuthRepository
import com.usc.cems.ui.screens.admin.AdminDashboardScreen
import com.usc.cems.ui.screens.createevent.CreateEventScreen
import com.usc.cems.ui.screens.event.EventDetailsScreen
import com.usc.cems.ui.screens.home.HomeScreen
import com.usc.cems.ui.screens.login.LoginScreen
import com.usc.cems.ui.screens.myevents.MyEventsScreen
import com.usc.cems.ui.screens.profile.ProfileScreen
import com.usc.cems.ui.screens.register.RegisterScreen
import com.usc.cems.ui.screens.splash.SplashScreen
import com.usc.cems.ui.screens.updateevent.UpdateEventScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    authRepository: AuthRepository,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash,
        modifier = modifier,
    ) {
        composable<Screen.Splash> {
            SplashScreen(
                onNavigateToHome = {
                    val user = authRepository.getCurrentUser()
                    val dest = if (user?.role?.equals("admin", ignoreCase = true) == true) Screen.AdminDashboard else Screen.Home
                    navController.navigate(dest) {
                        popUpTo(Screen.Splash) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login) {
                        popUpTo(Screen.Splash) { inclusive = true }
                    }
                },
            )
        }

        composable<Screen.Login> {
            LoginScreen(
                onNavigateToHome = {
                    val user = authRepository.getCurrentUser()
                    val dest = if (user?.role?.equals("admin", ignoreCase = true) == true) Screen.AdminDashboard else Screen.Home
                    navController.navigate(dest) {
                        popUpTo(Screen.Login) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register)
                },
            )
        }

        composable<Screen.Register> {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onNavigateToHome = {
                    val user = authRepository.getCurrentUser()
                    val dest = if (user?.role?.equals("admin", ignoreCase = true) == true) Screen.AdminDashboard else Screen.Home
                    navController.navigate(dest) {
                        popUpTo(Screen.Register) { inclusive = true }
                    }
                },
            )
        }

        composable<Screen.Home> {
            HomeScreen(
                onEventClick = { eventId ->
                    navController.navigate(Screen.EventDetails(eventId))
                },
                onNavigateToRegistered = {
                    navController.navigate(Screen.MyEvents)
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile)
                },
                onNavigateToCreateEvent = {
                    navController.navigate(Screen.CreateEvent)
                },
            )
        }

        composable<Screen.EventDetails> { backStackEntry ->
            val route = backStackEntry.toRoute<Screen.EventDetails>()
            Box(modifier = Modifier.fillMaxSize().systemBarsPadding()) {
                EventDetailsScreen(
                    eventId = route.eventId,
                    onNavUp = { navController.popBackStack() },
                    onNavigateToEditEvent = { eventId ->
                        navController.navigate(Screen.UpdateEvent(eventId))
                    }
                )
            }
        }

        composable<Screen.CreateEvent> {
            CreateEventScreen(
                eventId = null,
                onNavUp = { navController.popBackStack() },
            )
        }

        composable<Screen.UpdateEvent> { backStackEntry ->
            val route = backStackEntry.toRoute<Screen.UpdateEvent>()
            com.usc.cems.ui.screens.editevent.EditEventScreen(
                eventId = route.eventId,
                onNavUp = { navController.popBackStack() },
            )
        }

        composable<Screen.MyEvents> {
            MyEventsScreen(
                onEventClick = { eventId ->
                    navController.navigate(Screen.EventDetails(eventId))
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home) {
                        popUpTo(Screen.Home) { inclusive = false }
                    }
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile)
                },
            )
        }

        composable<Screen.Profile> {
            ProfileScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home) {
                        popUpTo(Screen.Home) { inclusive = false }
                    }
                },
                onNavigateToRegistered = {
                    navController.navigate(Screen.MyEvents)
                },
                onNavigateToAdminDashboard = {
                    navController.navigate(Screen.AdminDashboard)
                },
                onLogout = {
                    navController.navigate(Screen.Login) {
                        popUpTo(0) { inclusive = true }
                    }
                },
            )
        }

        composable<Screen.AdminDashboard> {
            AdminDashboardScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home) {
                        popUpTo(Screen.Home) { inclusive = false }
                    }
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile)
                },
                onNavigateToRegistered = {
                    navController.navigate(Screen.MyEvents)
                },
                onNavigateToAddEvent = {
                    navController.navigate(Screen.CreateEvent)
                },
                onNavigateToEditEvent = { eventId ->
                    navController.navigate(Screen.UpdateEvent(eventId))
                },
                onEventClick = { eventId ->
                    navController.navigate(Screen.EventDetails(eventId))
                }
            )
        }
    }
}
