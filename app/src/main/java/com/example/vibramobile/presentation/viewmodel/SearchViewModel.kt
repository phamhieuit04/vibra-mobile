package com.example.vibramobile.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.vibramobile.domain.contract.IUserRepository
import com.example.vibramobile.domain.contract.ISearchResultRepository
import com.example.vibramobile.domain.model.SearchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class SearchViewModel(
    private val searchResultRepository: ISearchResultRepository,
    private val userRepository: IUserRepository
) : ViewModel() {
    private val _searchResult = MutableStateFlow<SearchResult?>(null)
    val searchResult = _searchResult.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    suspend fun search(keyword: String) {
        _isLoading.value = true
        delay(1000)
        withContext(Dispatchers.IO) {
            runCatching {
                val token = userRepository.getAccessToken()
                val result = searchResultRepository.search(
                    keyword = keyword,
                    accessToken = token
                )

                if (result.albums.isEmpty() &&
                    result.artists.isEmpty() &&
                    result.songs.isEmpty()
                ) {
                    _searchResult.value = null
                } else {
                    _searchResult.value = result
                }
            }.onFailure { exception ->
                Log.e("MyApp", exception.toString())
            }
        }
        _isLoading.value = false
    }
}