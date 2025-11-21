package com.example.vibramobile.ui.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.vibramobile.Destination
import com.example.vibramobile.ui.screens.WelcomeScreen
import com.example.vibramobile.ui.screens.login.LoginScreen
import com.example.vibramobile.ui.screens.signup.SignUpPasswordScreen
import com.example.vibramobile.ui.screens.signup.SignUpScreen

fun NavGraphBuilder.authGraph(navController: NavController) {
    navigation<Destination.AuthGraph>(startDestination = Destination.WelcomeScreen) {
        composable<Destination.WelcomeScreen> {
            WelcomeScreen(
                onNavigateToLogInScreen = {
                    navController.navigate(Destination.LoginScreen)
                },
                onNavigateToSignUpScreen = {
                    navController.navigate(Destination.SignUpScreen)
                }
            )
        }
        composable<Destination.LoginScreen> {
            LoginScreen(
                onNavigateToMainScreen = {
                    navController.navigate(Destination.MainScreen) {
                        popUpTo(Destination.AuthGraph) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToSignUpScreen = {
                    val popped = navController.popBackStack(
                        route = Destination.SignUpScreen,
                        inclusive = false
                    )
                    if (!popped) navController.navigate(route = Destination.SignUpScreen) {
                        launchSingleTop = true
                    }
                },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable<Destination.SignUpScreen> {
            SignUpScreen(
                onNavigateToLogInScreen = {
                    val popped = navController.popBackStack(
                        route = Destination.LoginScreen,
                        inclusive = false
                    )
                    if (!popped) navController.navigate(route = Destination.LoginScreen) {
                        launchSingleTop = true
                    }
                },
                onNavigateToSignUpPasswordScreen = {
                    navController.navigate(Destination.SignUpPasswordScreen)
                },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable<Destination.SignUpPasswordScreen> {
            SignUpPasswordScreen(
                onNavigateToLogInScreen = {
                    navController.navigate(Destination.LoginScreen) {
                        popUpTo(Destination.SignUpPasswordScreen) {
                            inclusive = true
                        }
                    }
                },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}