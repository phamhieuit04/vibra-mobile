package com.example.vibramobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.vibramobile.presentation.component.ContextMenuState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ContextMenuViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ContextMenuState())
    val uiState = _uiState.asStateFlow()

    fun show(thumbnailPath: String?, songTitle: String?, artistName: String?) {
        _uiState.update {
            it.copy(
                visible = true, thumbnailPath = thumbnailPath ?: "",
                songTitle = songTitle ?: "",
                artistName = artistName ?: ""
            )
        }
    }

    fun hide() {
        _uiState.update { it.copy(visible = false) }
    }
}