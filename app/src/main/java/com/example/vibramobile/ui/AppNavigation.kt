package com.example.vibramobile.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vibramobile.helpers.NavigationAction
import com.example.vibramobile.helpers.Navigator
import com.example.vibramobile.helpers.ObserverAsEvents
import com.example.vibramobile.ui.screens.WelcomeScreen
import com.example.vibramobile.ui.screens.login.LoginScreen
import com.example.vibramobile.ui.screens.signup.SignUpPasswordScreen
import com.example.vibramobile.ui.screens.signup.SignUpScreen
import kotlinx.serialization.Serializable

sealed interface Destination {
    @Serializable
    object Welcome : Destination

    @Serializable
    object Login : Destination

    @Serializable
    object SignUp : Destination

    @Serializable
    object SignUpPassword : Destination
}

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val animationSpec = tween<IntOffset>(700)
    val navController = rememberNavController()

    ObserverAsEvents(Navigator.channel) { action ->
        when (action) {
            is NavigationAction.Navigate -> navController.navigate(route = action.destination)
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

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Destination.Welcome,
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
        composable<Destination.Welcome> {
            WelcomeScreen()
        }
        composable<Destination.Login> {
            LoginScreen()
        }
        composable<Destination.SignUp> {
            SignUpScreen()
        }
        composable<Destination.SignUpPassword> {
            SignUpPasswordScreen()
        }
    }
}