package com.example.vibramobile.presentation.config

import androidx.annotation.StringRes

sealed class DetailActionConfig {
    data class Artist(
        val avatarPath: String?,
        @StringRes val followLabelRes: Int? = null,
        val dropdownItems: List<Pair<Int, () -> Unit>> = emptyList(),
        val onFollow: () -> Unit = {},
    ) : DetailActionConfig()

    data class Album(
        val onAddToQueue: () -> Unit = {},
        val onDownload: () -> Unit = {},
    ) : DetailActionConfig()
}