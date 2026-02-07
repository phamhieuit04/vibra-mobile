package com.example.vibramobile.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.vibramobile.models.RecommendedSongs
import com.example.vibramobile.models.Response
import com.example.vibramobile.models.SearchResult
import com.example.vibramobile.models.Song
import com.example.vibramobile.states.SongState
import com.example.vibramobile.states.UserState
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class SearchViewModel(
    private val client: HttpClient,
    private val json: Json
) : ViewModel() {
    private val accessToken: String = UserState.getCurrentUser()?.token.toString()

    private val _searchResult = MutableStateFlow<SearchResult?>(null)
    val searchResult = _searchResult.asStateFlow()

    suspend fun search(keyword: String) {
        withContext(Dispatchers.IO) {
            runCatching {
                val response = client.get("home/search?search-key=$keyword") {
                    bearerAuth(accessToken)
                }.bodyAsText()
                val result = json.decodeFromString<Response<SearchResult>>(response)

                _searchResult.value = result.data
            }.onFailure { exception ->
                Log.e("MyApp", exception.toString())
            }
        }
    }
}