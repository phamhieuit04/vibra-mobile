package com.example.vibramobile.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibramobile.data.source.remote.socket.ISocketClient
import com.example.vibramobile.domain.contract.IAuthRepository
import com.example.vibramobile.domain.contract.IUserRepository
import com.example.vibramobile.domain.model.User
import com.example.vibramobile.presentation.event.LoginEvent
import com.example.vibramobile.presentation.state.UserState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: IAuthRepository,
    private val userRepository: IUserRepository
) : ViewModel() {
    private val _loginEvent = Channel<LoginEvent>(Channel.BUFFERED)
    val loginEvent = _loginEvent.receiveAsFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            var user: User? = null;

            runCatching {
                user = authRepository.login(email = email, password = password)
            }.onSuccess {
                user?.let {
                    userRepository.upsertUser(it)
                    UserState.setCurrentUser(it.copy(token = null))
                }

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