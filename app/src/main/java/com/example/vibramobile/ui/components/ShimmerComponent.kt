package com.example.vibramobile.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ShimmerComponent(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    skeletonContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    if (isLoading) skeletonContent()
    else content()
}

@Composable
fun HomeShimmer(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        ListSongRowSkeleton()
        Spacer(Modifier.height(16.dp))
        ListSongSkeleton()
        Spacer(Modifier.height(16.dp))
        ListAlbumSkeleton()
    }
}

@Composable
fun GenreDetailShimmer(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        ListSongRowSkeleton()
        Spacer(Modifier.height(16.dp))
        ListSongRowSkeleton()
    }
}

@Composable
fun SearchResultShimmer(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        ListSongRowSkeleton()
        Spacer(Modifier.height(16.dp))
        ListSongRowSkeleton()
        Spacer(Modifier.height(16.dp))
        ListSongRowSkeleton()
    }
}