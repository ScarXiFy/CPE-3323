package com.usc.cems.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.usc.cems.ui.screens.createevent.CreateEventScreen
import com.usc.cems.ui.screens.event.EventDetailsScreen
import com.usc.cems.ui.screens.home.HomeScreen
import com.usc.cems.ui.screens.login.LoginScreen
import com.usc.cems.ui.screens.myevents.MyEventsScreen
import com.usc.cems.ui.screens.profile.ProfileScreen
import com.usc.cems.ui.screens.register.RegisterScreen
import com.usc.cems.ui.screens.splash.SplashScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
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
                    navController.navigate(Screen.Home) {
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
                    navController.navigate(Screen.Home) {
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
                    navController.navigate(Screen.Home) {
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
            EventDetailsScreen(
                eventId = route.eventId,
                onNavUp = { navController.popBackStack() },
            )
        }

        composable<Screen.CreateEvent> {
            CreateEventScreen(
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
                onLogout = {
                    navController.navigate(Screen.Login) {
                        popUpTo(0) { inclusive = true }
                    }
                },
            )
        }
    }
}
