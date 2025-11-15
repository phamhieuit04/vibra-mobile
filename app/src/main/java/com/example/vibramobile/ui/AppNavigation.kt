package com.example.vibramobile.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Destination.Welcome
    ) {
        composable<Destination.Welcome> {
            WelcomeScreen(
                onNavigateToLogin = { navController.navigate(Destination.Login) },
                onNavigateToSignUp = { navController.navigate(Destination.SignUp) }
            )
        }
        composable<Destination.Login> {
            LoginScreen()
        }
        composable<Destination.SignUp> {
            SignUpScreen(onNavigateToSignUpPassword = { navController.navigate(Destination.SignUpPassword) })
        }
        composable<Destination.SignUpPassword> {
            SignUpPasswordScreen()
        }
    }
}