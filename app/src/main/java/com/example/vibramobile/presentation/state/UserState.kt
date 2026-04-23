package com.example.vibramobile.presentation.state

import com.example.vibramobile.domain.model.Bill
import com.example.vibramobile.domain.model.Library
import com.example.vibramobile.domain.model.Playlist
import com.example.vibramobile.domain.model.Song
import com.example.vibramobile.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object UserState {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _followedArtists = MutableStateFlow<List<User>>(emptyList())
    val followedArtists: StateFlow<List<User>> = _followedArtists.asStateFlow()

    private val _myPlaylists = MutableStateFlow<List<Playlist>>(emptyList())
    val myPlaylists: StateFlow<List<Playlist>> = _myPlaylists.asStateFlow()

    private val _myAlbums = MutableStateFlow<List<Playlist>>(emptyList())
    val myAlbums: StateFlow<List<Playlist>> = _myAlbums.asStateFlow()

    private val _paymentHistory = MutableStateFlow<List<Bill>>(emptyList())
    val paymentHistory: StateFlow<List<Bill>> = _paymentHistory.asStateFlow()

    private val _likedSongs = MutableStateFlow<List<Song>>(emptyList())
    val likedSongs: StateFlow<List<Song>> = _likedSongs.asStateFlow()

    fun setCurrentUser(user: User?) {
        val previousUserId = _currentUser.value?.id
        _currentUser.value = user

        if (user == null || user.id != previousUserId) {
            _followedArtists.value = emptyList()
            _myPlaylists.value = emptyList()
        }
    }

    fun setFollowedArtists(artists: List<User>) {
        _followedArtists.value = artists
    }

    fun setMyPlaylists(playlists: List<Playlist>) {
        _myPlaylists.value = playlists
    }

    fun setMyAlbums(albums: List<Playlist>) {
        _myAlbums.value = albums
    }

    fun setPaymentHistory(bills: List<Bill>) {
        _paymentHistory.value = bills
    }

    fun setLikedSongs(songs: List<Song>) {
        _likedSongs.value = songs
    }
}