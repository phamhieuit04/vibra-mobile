package com.example.vibramobile.presentation.state

import com.example.vibramobile.domain.model.Playlist
import com.example.vibramobile.domain.model.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object SongState {
    private val _currentSong = MutableStateFlow<Song?>(null)
    val currentSong: StateFlow<Song?> = _currentSong.asStateFlow()

    fun setCurrentSong(song: Song?) {
        _currentSong.value = song
    }

    private val _recommendedSongs = MutableStateFlow<List<Song>>(emptyList())
    val recommendedSongs: StateFlow<List<Song>> = _recommendedSongs.asStateFlow()

    fun setRecommendedSongs(songs: List<Song>) {
        _recommendedSongs.value = songs
    }

    private val _popularSongs = MutableStateFlow<List<Song>>(emptyList())
    val popularSongs: StateFlow<List<Song>> = _popularSongs.asStateFlow()

    fun setPopularSongs(songs: List<Song>) {
        _popularSongs.value = songs
    }

    private val _popularAlbums = MutableStateFlow<List<Playlist>>(emptyList())
    val popularAlbums: StateFlow<List<Playlist>> = _popularAlbums.asStateFlow()

    fun setPopularAlbums(albums: List<Playlist>) {
        _popularAlbums.value = albums
    }

    private val _recentRotationSongs = MutableStateFlow<List<Song>>(emptyList())
    val recentRotationSongs: StateFlow<List<Song>> = _recentRotationSongs.asStateFlow()

    fun setRecentRotationSongs(songs: List<Song>) {
        _recentRotationSongs.value = songs
    }

    private val _songsByCategory = MutableStateFlow<List<Song>>(emptyList())
    val songsByCategory: StateFlow<List<Song>> = _songsByCategory.asStateFlow()

    fun setSongsByCategory(songs: List<Song>) {
        _songsByCategory.value = songs
    }

    private val _songsByArtist = MutableStateFlow<List<Song>>(emptyList())
    val songsByArtist: StateFlow<List<Song>> = _songsByArtist.asStateFlow()

    fun setSongsByArtist(songs: List<Song>) {
        _songsByArtist.value = songs
    }

    private val _songsByAlbum = MutableStateFlow<List<Song>>(emptyList())
    val songsByAlbum: StateFlow<List<Song>> = _songsByAlbum.asStateFlow()

    fun setSongsByAlbum(songs: List<Song>) {
        _songsByAlbum.value = songs
    }
}