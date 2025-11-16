package com.example.vibramobile.helpers

import androidx.navigation.NavOptionsBuilder
import com.example.vibramobile.ui.Destination
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

sealed interface NavigationAction {
    data class Navigate(
        val destination: Destination, val navOptions: NavOptionsBuilder.() -> Unit = {}
    ) : NavigationAction

    object NavigateUp : NavigationAction

    data class PopBackStack(
        val destination: Destination,
        val inclusive: Boolean
    ) : NavigationAction
}

object Navigator {
    private val _channel = Channel<NavigationAction>()
    val channel = _channel.receiveAsFlow()

    suspend fun navigate(destination: Destination, navOptions: NavOptionsBuilder.() -> Unit = {}) {
        _channel.send(
            NavigationAction.Navigate(
                destination = destination,
                navOptions = navOptions
            )
        )
    }

    suspend fun navigateUp() {
        _channel.send(NavigationAction.NavigateUp)
    }

    suspend fun popBackStack(destination: Destination, inclusive: Boolean) {
        _channel.send(
            NavigationAction.PopBackStack(
                destination = destination,
                inclusive = inclusive
            )
        )
    }
}