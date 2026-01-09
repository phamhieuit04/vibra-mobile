package com.example.vibramobile.ui.navigations.destinations

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed class MainDestination : NavKey {
    @Serializable
    object Home : MainDestination()

    @Serializable
    object Search : MainDestination()

    @Serializable
    object Library : MainDestination()
}