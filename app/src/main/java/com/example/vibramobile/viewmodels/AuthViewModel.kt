package com.example.vibramobile.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibramobile.helpers.JsonHelper
import com.example.vibramobile.helpers.Navigator
import com.example.vibramobile.models.User
import com.example.vibramobile.repositories.auth.AuthRepository
import com.example.vibramobile.states.UiState
import com.example.vibramobile.states.UserState
import com.example.vibramobile.ui.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.call.body
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    fun login() {
        viewModelScope.launch {
            val response =
                authRepository.login(email = "tomnguyenhieu2004@gmail.com", password = "12345678")
            if (response != null) {
                val user = JsonHelper.parseJson(from = response.body(), to = User::class)
                UserState.currentUser = user
                Navigator.navigate(
                    destination = Destination.HomeGraph,
                    navOptions = {
                        popUpTo(Destination.AuthGraph) {
                            inclusive = true
                        }
                    })
                UiState.displayNavigationBar.value = true
            }
        }
    }

    fun signup() {
        viewModelScope.launch {
//
        }
    }
}