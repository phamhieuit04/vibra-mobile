package com.example.vibramobile.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibramobile.helpers.JsonHelper
import com.example.vibramobile.helpers.Navigator
import com.example.vibramobile.models.User
import com.example.vibramobile.states.UiState
import com.example.vibramobile.states.UserState
import com.example.vibramobile.ui.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.http.parameters
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val client: HttpClient
) : ViewModel() {
    fun login(email: String, password: String) {
        viewModelScope.launch {
            val response = client.submitForm(
                url = "login",
                formParameters = parameters {
                    append(name = "email", value = email)
                    append(name = "password", value = password)
                })
            UserState.currentUser = JsonHelper.parseJson(from = response.body(), to = User::class)

            Navigator.navigate(
                destination = Destination.HomeGraph,
                navOptions = {
                    popUpTo(Destination.AuthGraph) {
                        inclusive = true
                    }
                })
            UiState.displayNavigationBar.value = true
            UiState.displayMediaPlayer.value = true
        }
    }

    fun signup() {
        viewModelScope.launch {
//
        }
    }
}