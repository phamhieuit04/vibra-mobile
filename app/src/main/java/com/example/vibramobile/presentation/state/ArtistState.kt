package com.example.vibramobile.presentation.state

import com.example.vibramobile.domain.model.User
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