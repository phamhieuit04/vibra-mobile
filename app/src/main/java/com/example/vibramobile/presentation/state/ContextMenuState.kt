package com.example.vibramobile.presentation.state

data class ContextMenuState(
    val visible: Boolean = false,
    val thumbnailPath: String = "",
    val songTitle: String = "Six Feet Under",
    val artistName: String = "Billie Eilish"
)