package com.example.vibramobile.presentation.event

sealed class LoginEvent {
    object Success : LoginEvent()
    data class Error(val message: String?) : LoginEvent()
}
