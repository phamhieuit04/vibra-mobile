package com.example.vibramobile.presentation.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibramobile.core.util.SystemUtils
import com.example.vibramobile.domain.contract.IAuthRepository
import com.example.vibramobile.domain.contract.IUserRepository
import com.example.vibramobile.presentation.navigation.destination.MainDestination
import com.example.vibramobile.presentation.navigation.destination.RootDestination
import com.example.vibramobile.presentation.state.AppState
import com.example.vibramobile.presentation.state.UserState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashViewModel(
    private val authRepository: IAuthRepository,
    private val userRepository: IUserRepository,
    private val context: Context
) : ViewModel() {
    private val _uiState = MutableStateFlow(StartupUiState())
    val uiState: StateFlow<StartupUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val connectivityManager = context.getSystemService(ConnectivityManager::class.java)
            val isOnline = connectivityManager?.let { SystemUtils.isOnline(it) } ?: true
            AppState.setOffline(!isOnline)

            if (!isOnline) {
                startOfflineFlow()
                return@launch
            }

            val token = userRepository.getAccessToken()

            if (token.isBlank()) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        startDestination = RootDestination.Auth
                    )
                }
                return@launch
            }

            val user = runCatching {
                withContext(Dispatchers.IO) { authRepository.checkToken(token) }
            }.getOrElse {
                AppState.setOffline(true)
                startOfflineFlow()
                return@launch
            }

            val userId = user?.id

            if (userId != null) {
                val updatedUser = user.copy(token = token)
                userRepository.upsertUser(updatedUser)
                UserState.setCurrentUser(updatedUser.copy(token = null))
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        startDestination = RootDestination.Main
                    )
                }
            } else {
                userRepository.clearUser()
                UserState.setCurrentUser(null)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        startDestination = RootDestination.Auth
                    )
                }
            }
        }
    }

    private suspend fun startOfflineFlow() {
        val cachedUser = userRepository.getUser()
        val token = cachedUser?.token.orEmpty()
        if (cachedUser != null && token.isNotBlank()) {
            UserState.setCurrentUser(cachedUser.copy(token = null))
            _uiState.update {
                it.copy(
                    isLoading = false,
                    startDestination = RootDestination.Main,
                    mainStartDestination = MainDestination.Library
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    startDestination = RootDestination.Auth
                )
            }
        }
    }
}

data class StartupUiState(
    val isLoading: Boolean = true,
    val startDestination: RootDestination = RootDestination.Auth,
    val mainStartDestination: MainDestination = MainDestination.Home
)
