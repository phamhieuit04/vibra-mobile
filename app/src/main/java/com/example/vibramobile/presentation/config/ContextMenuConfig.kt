package com.example.vibramobile.presentation.config

import com.example.vibramobile.domain.model.Song
import com.example.vibramobile.domain.model.User

sealed class ContextMenuConfig {
    data class SongItem(
        val header: ContextMenuHeader,
        val song: Song,
        val showAddToQueue: Boolean = true,
        val showAddToLiked: Boolean = true,
        val showAddToPlaylist: Boolean = true,
        val showGoToArtist: Boolean = true,
        val onAddToQueue: ((Song) -> Unit)? = null,
        val onAddToLiked: ((Song) -> Unit)? = null,
        val onAddToPlaylist: ((Song) -> Unit)? = null,
        val onGoToArtist: ((User) -> Unit)? = null
    ) : ContextMenuConfig()
}

data class ContextMenuHeader(
    val thumbnailPath: String? = null,
    val title: String? = null,
    val subtitle: String? = null
)
