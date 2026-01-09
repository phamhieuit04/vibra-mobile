package com.example.vibramobile.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibramobile.events.LoginEvent
import com.example.vibramobile.helpers.JsonHelper
import com.example.vibramobile.models.User
import com.example.vibramobile.states.UserState
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.http.parameters
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val client: HttpClient
) : ViewModel() {
    private val _loginEvent = Channel<LoginEvent>(Channel.BUFFERED)
    val loginEvent = _loginEvent.receiveAsFlow()

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
            }.onSuccess {
                _loginEvent.send(LoginEvent.Success)
            }.onFailure { exception ->
                Log.e("myapp", exception.toString())
                _loginEvent.send(LoginEvent.Error(exception.message))
            }
        }
    }

    fun signup() {
        viewModelScope.launch {
//
        }
    }
}