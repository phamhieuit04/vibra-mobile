package com.example.vibramobile.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.vibramobile.helpers.NavigationAction
import com.example.vibramobile.helpers.Navigator
import com.example.vibramobile.helpers.ObserverAsEvents
import com.example.vibramobile.states.UiState
import com.example.vibramobile.ui.graphs.authGraph
import com.example.vibramobile.ui.graphs.homeGraph
import com.example.vibramobile.ui.graphs.searchGraph
import kotlinx.serialization.Serializable

sealed interface Destination {
    @Serializable
    object AuthGraph : Destination

    @Serializable
    object HomeGraph : Destination

    @Serializable
    object SearchGraph : Destination

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
}

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val animationSpec = tween<IntOffset>(700)
    val navController = rememberNavController()
    var display by mutableStateOf(false)

    ObserverAsEvents(Navigator.channel) { action ->
        when (action) {
            is NavigationAction.Navigate -> navController.navigate(route = action.destination) {
                popUpTo(UiState.currentGraph.value.destination) {
                    inclusive = true
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

    LaunchedEffect(UiState.displayNavigationBar.value) {
        if (UiState.displayNavigationBar.value) display = true
    }

    Scaffold(
        containerColor = Color.Black,
        bottomBar = { if (display) AppNavigationBar() }
    ) { paddingValues ->
        NavHost(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                ),
            navController = navController,
            startDestination = Destination.AuthGraph,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec
                )
            }
        ) {
            authGraph()
            homeGraph()
            searchGraph()
        }
    }
}