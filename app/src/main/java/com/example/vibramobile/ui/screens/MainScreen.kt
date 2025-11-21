package com.example.vibramobile.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.vibramobile.Destination
import com.example.vibramobile.helpers.NavigationAction
import com.example.vibramobile.helpers.Navigator
import com.example.vibramobile.helpers.ObserverAsEvents
import com.example.vibramobile.states.UiState
import com.example.vibramobile.ui.AppMediaPlayer
import com.example.vibramobile.ui.AppNavigationBar
import com.example.vibramobile.ui.graphs.homeGraph
import com.example.vibramobile.ui.graphs.searchGraph

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    ObserverAsEvents(Navigator.channel) { action ->
        when (action) {
            is NavigationAction.Navigate -> navController.navigate(route = action.destination) {
                if (action.popUpToStart) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
                action.navOptions(this)
            }

            is NavigationAction.NavigateUp -> navController.navigateUp()

            is NavigationAction.PopBackStack -> {
                val popped = navController.popBackStack(
                    route = action.destination,
                    inclusive = action.inclusive
                )
                if (!popped) navController.navigate(route = action.destination) {
                    launchSingleTop = true
                }
            }
        }
    }

    Scaffold(
        containerColor = Color.Black,
        bottomBar = {
            if (UiState.displayNavigationBar.value) AppNavigationBar()
        })
    { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = padding.calculateBottomPadding())
        ) {
            NavHost(navController = navController, startDestination = Destination.HomeGraph) {
                homeGraph()
                searchGraph()
            }
            if (UiState.displayMediaPlayer.value)
                AppMediaPlayer(modifier = Modifier.align(alignment = Alignment.BottomCenter))
        }
    }
}