package com.example.vibramobile.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibramobile.models.Response
import com.example.vibramobile.models.Song
import com.example.vibramobile.states.SongState
import com.example.vibramobile.states.UserState
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class GenreDetailViewModel(
    private val client: HttpClient,
    private val json: Json
) : ViewModel() {
    private val accessToken: String = UserState.getCurrentUser()?.token.toString()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    suspend fun getSongsByCategory(categoryId: Int) {
        withContext(Dispatchers.IO) {
            _isRefreshing.value = true

            runCatching {
                val response = client.get("category/show/$categoryId") {
                    bearerAuth(accessToken)
                }.bodyAsText()
                val result = json.decodeFromString<Response<List<Song>>>(response)

                SongState.setSongsByCategory(result.data)
            }.onFailure { exception ->
                Log.e("MyApp", exception.toString())
            }

            _isRefreshing.value = false
        }
    }
}