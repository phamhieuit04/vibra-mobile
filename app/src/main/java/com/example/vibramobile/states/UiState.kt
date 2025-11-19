package com.example.vibramobile.states

import androidx.compose.runtime.mutableStateOf
import com.example.vibramobile.ui.NavDestination

object UiState {
    var displayNavigationBar = mutableStateOf(false)
    var displayMediaPlayer = mutableStateOf(false)
    var currentGraph = mutableStateOf(NavDestination.HOME)
}