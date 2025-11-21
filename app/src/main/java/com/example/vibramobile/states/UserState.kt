package com.example.vibramobile.states

import androidx.compose.runtime.mutableStateOf
import com.example.vibramobile.models.User

object UserState {
    var isLoggedIn = mutableStateOf(false)
    var currentUser: User? = null
}