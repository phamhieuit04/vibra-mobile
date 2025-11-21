package com.example.vibramobile.ui.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.vibramobile.Destination
import com.example.vibramobile.ui.screens.search.SearchScreen

fun NavGraphBuilder.searchGraph() {
    navigation<Destination.SearchGraph>(startDestination = Destination.SearchScreen) {
        composable<Destination.SearchScreen> {
            SearchScreen()
        }
    }
}