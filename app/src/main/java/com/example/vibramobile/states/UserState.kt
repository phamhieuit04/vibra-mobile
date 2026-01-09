package com.example.vibramobile.states

import androidx.compose.runtime.mutableStateOf
import com.example.vibramobile.models.User

object UserState {
    private var currentUser: User? = null
    fun setCurrentUser(user: User?) {
        currentUser = user
    }

    fun getCurrentUser(): User? {
        return currentUser
    }
}