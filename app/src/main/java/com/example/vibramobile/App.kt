package com.example.vibramobile

import android.app.Application
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.vibramobile.helpers.NavigationAction
import com.example.vibramobile.helpers.Navigator
import com.example.vibramobile.helpers.ObserverAsEvents
import com.example.vibramobile.states.UiState
import com.example.vibramobile.ui.AppMediaPlayer
import com.example.vibramobile.ui.AppNavigationBar
import com.example.vibramobile.ui.graphs.authGraph
import com.example.vibramobile.ui.graphs.homeGraph
import com.example.vibramobile.ui.graphs.libraryGraph
import com.example.vibramobile.ui.graphs.searchGraph
import com.example.vibramobile.ui.screens.FullscreenPlayer
import dagger.hilt.android.HiltAndroidApp
import kotlinx.serialization.Serializable

sealed interface Destination {
    @Serializable
    object AuthGraph : Destination

    @Serializable
    object HomeGraph : Destination

    @Serializable
    object SearchGraph : Destination

    @Serializable
    object LibraryGraph : Destination

    @Serializable
    object WelcomeScreen : Destination

    @Serializable
    object LoginScreen : Destination

    @Serializable
    object SignUpScreen : Destination

    @Serializable
    object SignUpPasswordScreen : Destination

    @Serializable
    object HomeScreen : Destination

    @Serializable
    object SearchScreen : Destination

    @Serializable
    object LibraryScreen : Destination
}

@HiltAndroidApp
class App : Application()

@Composable
fun AppScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    ObserverAsEvents(Navigator.channel) { action ->
        when (action) {
            is NavigationAction.Navigate -> {
                navController.navigate(route = action.destination) {
                    if (action.popUpToStart) {
                        val popUpToRoute = action.popUpToRoute ?: Destination.HomeScreen
                        popUpTo(popUpToRoute) {
                            saveState = true
                            inclusive = false
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                    action.navOptions(this)
                }
            }

            is NavigationAction.NavigateUp -> navController.navigateUp()
            is NavigationAction.PopBackStack -> {
                if (!navController.popBackStack(action.destination, action.inclusive)) {
                    navController.navigate(action.destination) {
                        launchSingleTop = true
                    }
                }
            }
        }
    }

    Scaffold(
        containerColor = Color.Black,
        bottomBar = {
            AppNavigationBar(isVisible = UiState.getDisplayNavigationBar())
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            NavHost(
                modifier = Modifier.fillMaxSize(),
                navController = navController,
                startDestination = Destination.AuthGraph
            ) {
                authGraph()
                homeGraph()
                searchGraph()
                libraryGraph()
            }

            FullscreenPlayer(
                isVisible = UiState.getDisplaySongDetail(),
                onVisibleChange = { value ->
                    UiState.setDisplaySongDetail(value)
                })

            AppMediaPlayer(
                modifier = Modifier.align(alignment = Alignment.BottomEnd),
                isVisible = UiState.getDisplayMediaPlayer()
            )
        }
    }
}