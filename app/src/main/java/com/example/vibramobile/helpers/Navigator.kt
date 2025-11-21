package com.example.vibramobile.helpers

import androidx.navigation.NavOptionsBuilder
import com.example.vibramobile.Destination
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

sealed interface NavigationAction {
    data class Navigate(
        val destination: Destination,
        val popUpToStart: Boolean,
        val navOptions: NavOptionsBuilder.() -> Unit = {}
    ) : NavigationAction

    object NavigateUp : NavigationAction

    data class PopBackStack(
        val destination: Destination,
        val inclusive: Boolean
    ) : NavigationAction
}

interface NavigatorInterface {
    suspend fun navigate(
        destination: Destination,
        popUpToStart: Boolean = false,
        navOptions: NavOptionsBuilder.() -> Unit = {}
    )

    suspend fun navigateUp()
    suspend fun popBackStack(destination: Destination, inclusive: Boolean)
}

object Navigator : NavigatorInterface {
    private val _channel = Channel<NavigationAction>()
    val channel = _channel.receiveAsFlow()

    override suspend fun navigate(
        destination: Destination,
        popUpToStart: Boolean,
        navOptions: NavOptionsBuilder.() -> Unit
    ) {
        _channel.send(
            NavigationAction.Navigate(
                destination = destination,
                popUpToStart = popUpToStart,
                navOptions = navOptions
            )
        )
    }

    override suspend fun navigateUp() {
        _channel.send(NavigationAction.NavigateUp)
    }

    override suspend fun popBackStack(destination: Destination, inclusive: Boolean) {
        _channel.send(
            NavigationAction.PopBackStack(
                destination = destination,
                inclusive = inclusive
            )
        )
    }
}