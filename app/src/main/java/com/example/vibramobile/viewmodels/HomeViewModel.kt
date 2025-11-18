package com.example.vibramobile.viewmodels

import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibramobile.helpers.JsonHelper
import com.example.vibramobile.models.Response
import com.example.vibramobile.models.Song
import com.example.vibramobile.states.HomeState
import com.example.vibramobile.states.UserState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.ListSerializer
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val client: HttpClient
) : ViewModel() {
    private val accessToken: String = UserState.currentUser?.token.toString()

    init {
        viewModelScope.launch {
            getRecommendedSongs()
        }
    }

    suspend fun getRecommendedSongs() {
        runCatching {
            val response: Response<List<Song>> = client.get("home/get-recommended-songs") {
                bearerAuth(accessToken)
            }.body()
            HomeState.recommendedSongs.clear()
            HomeState.recommendedSongs.addAll(response.data)
        }.onFailure { exception ->
            Log.e("MyApp", exception.toString())
        }
    }
}