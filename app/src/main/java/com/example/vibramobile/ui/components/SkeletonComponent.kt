package com.example.vibramobile.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

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