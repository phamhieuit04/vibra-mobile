package com.example.vibramobile.states

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.vibramobile.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object ArtistState {
    private val _popularArtists = MutableStateFlow<List<User>>(emptyList())
    val popularArtists: StateFlow<List<User>> = _popularArtists.asStateFlow()

    fun setPopularArtists(artists: List<User>) {
        _popularArtists.value = artists
    }
}