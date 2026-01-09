package com.example.vibramobile.events

sealed class LoginEvent {
    object Success : LoginEvent()
    data class Error(val message: String?) : LoginEvent()
}
