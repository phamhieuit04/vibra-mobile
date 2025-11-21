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
                }
            )
        }
        composable<Destination.SignUpScreen> {
            SignUpScreen()
        }
        composable<Destination.SignUpPasswordScreen> {
            SignUpPasswordScreen()
        }
    }
}