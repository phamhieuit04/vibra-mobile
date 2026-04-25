package com.example.vibramobile.presentation.navigation.destination

import androidx.navigation3.runtime.NavKey
import com.example.vibramobile.domain.model.Category
import com.example.vibramobile.domain.model.Playlist
import com.example.vibramobile.domain.model.Song
import com.example.vibramobile.domain.model.User
import kotlinx.serialization.Serializable

@Serializable
sealed class MainDestination : NavKey {
    @Serializable
    object Home : MainDestination()

    @Serializable
    object Search : MainDestination()

    @Serializable
    object SearchResult : MainDestination()

    @Serializable
    object Library : MainDestination()

    @Serializable
    object Profile : MainDestination()

    @Serializable
    data class GenreDetail(val category: Category) : MainDestination()

    @Serializable
    data class ArtistDetail(val artist: User) : MainDestination()

    @Serializable
    data class AlbumDetail(val album: Playlist) : MainDestination()

    @Serializable
    data class SongDetail(val song: Song) : MainDestination()
}