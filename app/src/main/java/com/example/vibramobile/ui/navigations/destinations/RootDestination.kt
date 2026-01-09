package com.example.vibramobile.ui.navigations.destinations

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed class RootDestination : NavKey {
    @Serializable
    data object Auth : RootDestination()

    @Serializable
    data object Main : RootDestination()
}