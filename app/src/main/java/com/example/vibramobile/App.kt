package com.example.vibramobile

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vibramobile.ui.graphs.authGraph
import com.example.vibramobile.ui.screens.MainScreen
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
    object WelcomeScreen : Destination

    @Serializable
    object MainScreen : Destination

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

@HiltAndroidApp
class App : Application() {}

@Composable
fun AppScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    Scaffold() { padding ->
        NavHost(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding()),
            navController = navController,
            startDestination = Destination.AuthGraph
        ) {
            authGraph(navController = navController)
            composable<Destination.MainScreen> { MainScreen() }
        }
    }
}