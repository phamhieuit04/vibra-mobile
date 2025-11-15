package com.example.vibramobile.helpers

import com.example.vibramobile.ui.Destination
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

sealed interface NavigationEvent {
    object NavigateToLogin : NavigationEvent
    object NavigateToSignUp : NavigationEvent
}

object Navigator {
    private val _channel = Channel<NavigationEvent>()
    val channel = _channel.receiveAsFlow()

    suspend fun navigate(navigationEvent: NavigationEvent) {
        _channel.send(navigationEvent)
    }
}