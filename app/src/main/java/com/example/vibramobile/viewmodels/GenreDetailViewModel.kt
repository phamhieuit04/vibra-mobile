package com.example.vibramobile.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.vibramobile.contracts.ISongRepository
import com.example.vibramobile.states.SongState
import com.example.vibramobile.states.UserState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class GenreDetailViewModel(
    private val songRepository: ISongRepository
) : ViewModel() {
    private fun accessToken(): String = UserState.currentUser.value?.token.orEmpty()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    suspend fun getSongsByCategory(categoryId: Int) {
        withContext(Dispatchers.IO) {
            _isRefreshing.value = true

            delay(500)
            runCatching {
                SongState.setSongsByCategory(
                    songRepository.getSongsByCategory(
                        categoryId = categoryId,
                        accessToken = accessToken()
                    )
                )
            }.onFailure { exception ->
                Log.e("MyApp", exception.toString())
            }

            _isRefreshing.value = false
        }
    }
}