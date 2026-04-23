package com.example.vibramobile.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.composeunstyled.Text
import com.example.vibramobile.domain.model.User

@Composable
fun ArtistDetailScreen(
    modifier: Modifier = Modifier,
    artist: User
) {
    Text(text = artist.name ?: "Unknown Artist")
}