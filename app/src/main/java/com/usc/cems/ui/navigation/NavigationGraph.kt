package com.usc.cems.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.usc.cems.ui.screens.bookmarks.BookmarksScreen
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
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash,
        modifier = modifier
    ) {
        composable<Screen.Splash> {
            SplashScreen()
        }
        composable<Screen.Login> {
            LoginScreen()
        }
        composable<Screen.Register> {
            RegisterScreen()
        }
        composable<Screen.Home> {
            HomeScreen()
        }
        composable<Screen.EventDetails> { backStackEntry ->
            val route = backStackEntry.toRoute<Screen.EventDetails>()
            EventDetailsScreen(eventId = route.eventId)
        }
        composable<Screen.CreateEvent> {
            CreateEventScreen()
        }
        composable<Screen.Bookmarks> {
            BookmarksScreen()
        }
        composable<Screen.MyEvents> {
            MyEventsScreen()
        }
        composable<Screen.Profile> {
            ProfileScreen()
        }
    }
}
