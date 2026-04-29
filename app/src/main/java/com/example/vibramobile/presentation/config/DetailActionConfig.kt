package com.example.vibramobile.presentation.config

sealed class DetailActionConfig {
    data class Artist(
        val avatarPath: String?,
        val followLabel: String = "Theo dõi",
        val dropdownItems: List<Pair<String, () -> Unit>> = emptyList(),
        val onFollow: () -> Unit = {},
    ) : DetailActionConfig()

    data class Album(
        val onAddToQueue: () -> Unit = {},
        val onDownload: () -> Unit = {},
    ) : DetailActionConfig()
}