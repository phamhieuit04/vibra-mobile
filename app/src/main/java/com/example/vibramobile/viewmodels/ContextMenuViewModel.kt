package com.example.vibramobile.viewmodels

import androidx.lifecycle.ViewModel
import com.example.vibramobile.ui.ContextMenuState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ContextMenuViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ContextMenuState())
    val uiState = _uiState.asStateFlow()

    fun show() {
        _uiState.update { it.copy(visible = true) }
    }

    fun hide() {
        _uiState.update { it.copy(visible = false) }
    }
}