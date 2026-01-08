package com.example.vibramobile.ui.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.vibramobile.Destination
import com.example.vibramobile.ui.screens.LibraryScreen

fun NavGraphBuilder.libraryGraph() {
    navigation<Destination.LibraryGraph>(startDestination = Destination.LibraryScreen) {
        composable<Destination.LibraryScreen> {
            LibraryScreen()
        }
    }
}