package com.example.vibramobile.ui.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.vibramobile.ui.Destination
import com.example.vibramobile.ui.screens.WelcomeScreen
import com.example.vibramobile.ui.screens.login.LoginScreen
import com.example.vibramobile.ui.screens.signup.SignUpPasswordScreen
import com.example.vibramobile.ui.screens.signup.SignUpScreen

fun NavGraphBuilder.authGraph() {
    navigation<Destination.AuthGraph>(startDestination = Destination.WelcomeScreen) {
        composable<Destination.WelcomeScreen> {
            WelcomeScreen()
        }
        composable<Destination.LoginScreen> {
            LoginScreen()
        }
        composable<Destination.SignUpScreen> {
            SignUpScreen()
        }
        composable<Destination.SignUpPasswordScreen> {
            SignUpPasswordScreen()
        }
    }
}