package com.example.vibramobile.presentation.state

import com.example.vibramobile.presentation.config.ContextMenuConfig

data class ContextMenuState(
    val visible: Boolean = false,
    val config: ContextMenuConfig? = null
)