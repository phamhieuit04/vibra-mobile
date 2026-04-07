package com.example.vibramobile.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibramobile.contracts.IAuthRepository
import com.example.vibramobile.events.LoginEvent
import com.example.vibramobile.states.UserState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: IAuthRepository
) : ViewModel() {
    private val _loginEvent = Channel<LoginEvent>(Channel.BUFFERED)
    val loginEvent = _loginEvent.receiveAsFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            runCatching {
                val user = authRepository.login(email = email, password = password)
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