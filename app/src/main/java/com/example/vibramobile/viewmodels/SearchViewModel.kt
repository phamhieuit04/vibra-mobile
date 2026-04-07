package com.example.vibramobile.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.vibramobile.contracts.ISearchResultRepository
import com.example.vibramobile.models.SearchResult
import com.example.vibramobile.states.UserState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class SearchViewModel(
    private val searchResultRepository: ISearchResultRepository
) : ViewModel() {
    private val accessToken: String = UserState.getCurrentUser()?.token.toString()

    private val _searchResult = MutableStateFlow<SearchResult?>(null)
    val searchResult = _searchResult.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    suspend fun search(keyword: String) {
        _isLoading.value = true
        delay(1000)
        withContext(Dispatchers.IO) {
            runCatching {
                _searchResult.value = searchResultRepository.search(
                    keyword = keyword,
                    accessToken = accessToken
                )
            }.onFailure { exception ->
                Log.e("MyApp", exception.toString())
            }
        }
        _isLoading.value = false
    }
}