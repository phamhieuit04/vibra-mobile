package com.example.vibramobile.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SkeletonComponent(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    skeletonContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    if (isLoading) skeletonContent()
    else content()
}

@Composable
fun HomeSkeleton() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        ListSongRowSkeleton()
        Spacer(Modifier.height(16.dp))
        ListSongSkeleton()
        Spacer(Modifier.height(16.dp))
        ListAlbumSkeleton()
    }
}