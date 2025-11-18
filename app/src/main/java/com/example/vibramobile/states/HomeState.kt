package com.example.vibramobile.states

import androidx.compose.runtime.mutableStateListOf
import com.example.vibramobile.models.Category
import com.example.vibramobile.models.Playlist
import com.example.vibramobile.models.Song
import com.example.vibramobile.models.User

object HomeState {
    var recommendedSongs = mutableStateListOf<Song>()
    var categories: List<Category>? = null
    var popularSongs: List<Song>? = null
    var popularAlbums: List<Playlist>? = null
    var popularArtists: List<User>? = null
    var recentRotationSongs: List<Song>? = null
}