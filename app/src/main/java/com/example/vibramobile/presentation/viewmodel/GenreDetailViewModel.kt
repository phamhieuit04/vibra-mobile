package com.example.vibramobile.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.vibramobile.domain.contract.ISongRepository
import com.example.vibramobile.domain.contract.IUserRepository
import com.example.vibramobile.presentation.state.SongState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class GenreDetailViewModel(
    private val songRepository: ISongRepository,
    private val userRepository: IUserRepository
) : ViewModel() {
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    suspend fun getSongsByCategory(categoryId: Int) {
        withContext(Dispatchers.IO) {
            _isRefreshing.value = true

            delay(500)
            runCatching {
                val token = userRepository.getAccessToken()
                SongState.setSongsByCategory(
                    songRepository.getSongsByCategory(
                        categoryId = categoryId,
                        accessToken = token
                    )
                )
            }.onFailure { exception ->
                Log.e("MyApp", exception.toString())
            }

            _isRefreshing.value = false
        }
    }
}