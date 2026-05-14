package com.example.vibramobile.presentation.state

import com.example.vibramobile.data.source.local.dao.UserDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SessionStore(
    private val userDao: UserDao
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _accessToken = MutableStateFlow("")
    val accessToken: StateFlow<String> = _accessToken.asStateFlow()

    private val _isLoaded = MutableStateFlow(false)
    val isLoaded: StateFlow<Boolean> = _isLoaded.asStateFlow()

    init {
        scope.launch {
            userDao.getUser().collect { user ->
                _accessToken.value = user?.token.orEmpty()
                _isLoaded.value = true
            }
        }
    }

    suspend fun awaitAccessToken(): String {
        isLoaded.first { it }
        return _accessToken.value
    }

    suspend fun saveAccessToken(token: String) {
        val user = userDao.getUserOnce() ?: return
        userDao.update(user.copy(token = token))
        _accessToken.value = token
    }

    suspend fun clear() {
        userDao.clear()
        _accessToken.value = ""
    }

    fun currentAccessToken(): String = _accessToken.value
}
