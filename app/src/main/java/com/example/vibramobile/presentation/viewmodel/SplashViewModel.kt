package com.example.vibramobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibramobile.domain.contract.IAuthRepository
import com.example.vibramobile.domain.contract.ILocalUserRepository
import com.example.vibramobile.presentation.navigation.destination.RootDestination
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
    private val localUserRepository: ILocalUserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(StartupUiState())
    val uiState: StateFlow<StartupUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val token = localUserRepository.getAccessToken()

            if (token.isBlank()) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        startDestination = RootDestination.Auth
                    )
                }
                return@launch
            }

            val user = withContext(Dispatchers.IO) { authRepository.checkToken(token) }
            val userId = user?.id

            if (userId != null) {
                val updatedUser = user.copy(token = token)
                localUserRepository.upsertUser(updatedUser)
                UserState.setCurrentUser(updatedUser.copy(token = null))
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        startDestination = RootDestination.Main
                    )
                }
            } else {
                localUserRepository.clearUser()
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
}

data class StartupUiState(
    val isLoading: Boolean = true,
    val startDestination: RootDestination = RootDestination.Auth
)
