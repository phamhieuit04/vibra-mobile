package com.example.vibramobile.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibramobile.Destination
import com.example.vibramobile.helpers.JsonHelper
import com.example.vibramobile.helpers.Navigator
import com.example.vibramobile.models.User
import com.example.vibramobile.states.UserState
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.http.parameters
import kotlinx.coroutines.launch

class AuthViewModel(
    private val client: HttpClient
) : ViewModel() {
    fun login(email: String, password: String) {
        viewModelScope.launch {
            runCatching {
                val response = client.submitForm(
                    url = "login",
                    formParameters = parameters {
                        append(name = "email", value = email)
                        append(name = "password", value = password)
                    })

                val user = JsonHelper.parseJson(from = response.body(), to = User::class)
                UserState.setCurrentUser(user)

                Navigator.navigate(Destination.HomeGraph) {
                    popUpTo(Destination.AuthGraph) { inclusive = true }
                    launchSingleTop = true
                }
            }.onFailure { exception ->
                Log.e("MyApp", exception.toString())
            }
        }
    }

    fun signup() {
        viewModelScope.launch {
//
        }
    }
}