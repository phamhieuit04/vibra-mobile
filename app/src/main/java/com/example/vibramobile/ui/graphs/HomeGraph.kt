package com.example.vibramobile.ui.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.vibramobile.ui.Destination
import com.example.vibramobile.ui.screens.home.HomeScreen

fun NavGraphBuilder.homeGraph() {
    navigation<Destination.HomeGraph>(startDestination = Destination.HomeScreen()) {
        composable<Destination.HomeScreen> { entry ->
            val homeScreen = entry.toRoute<Destination.HomeScreen>()
            HomeScreen(userName = homeScreen.userName)
        }
    }
}