package com.example.vibramobile.ui.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.vibramobile.Destination
import com.example.vibramobile.ui.screens.HomeScreen

fun NavGraphBuilder.homeGraph() {
    navigation<Destination.HomeGraph>(startDestination = Destination.HomeScreen) {
        composable<Destination.HomeScreen> {
            HomeScreen()
        }
    }
}