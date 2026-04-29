package com.example.vibramobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.vibramobile.domain.model.Song
import com.example.vibramobile.domain.model.User
import com.example.vibramobile.presentation.config.ContextMenuConfig
import com.example.vibramobile.presentation.config.ContextMenuHeader
import com.example.vibramobile.presentation.state.ContextMenuState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ContextMenuViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ContextMenuState())
    val uiState = _uiState.asStateFlow()

    fun show(config: ContextMenuConfig) {
        _uiState.update { it.copy(visible = true, config = config) }
    }

    fun showSong(
        song: Song,
        header: ContextMenuHeader = ContextMenuHeader(
            thumbnailPath = song.thumbnailPath,
            title = song.name,
            subtitle = song.author?.name
        ),
        showAddToQueue: Boolean = true,
        showAddToLiked: Boolean = true,
        showAddToPlaylist: Boolean = true,
        showGoToArtist: Boolean = true,
        onAddToQueue: ((Song) -> Unit)? = null,
        onAddToLiked: ((Song) -> Unit)? = null,
        onAddToPlaylist: ((Song) -> Unit)? = null,
        onGoToArtist: ((User) -> Unit)? = null
    ) {
        show(
            ContextMenuConfig.SongItem(
                header = header,
                song = song,
                showAddToQueue = showAddToQueue,
                showAddToLiked = showAddToLiked,
                showAddToPlaylist = showAddToPlaylist,
                showGoToArtist = showGoToArtist,
                onAddToQueue = onAddToQueue,
                onAddToLiked = onAddToLiked,
                onAddToPlaylist = onAddToPlaylist,
                onGoToArtist = onGoToArtist
            )
        )
    }

    fun hide() {
        _uiState.update { it.copy(visible = false) }
    }
}