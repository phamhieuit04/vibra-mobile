package com.example.vibramobile.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Loop
import androidx.compose.material.icons.filled.PauseCircleFilled
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MediaControlsComponent(
    isPlaying: Boolean,
    onPlay: () -> Unit = { },
    onShuffle: () -> Unit = { },
    onPrevious: () -> Unit = { },
    onNext: () -> Unit = { },
    onLoop: () -> Unit = { }
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { }) {
            Icon(
                modifier = Modifier.size(28.dp),
                contentDescription = "",
                imageVector = Icons.Default.Shuffle,
                tint = Color.White
            )
        }
        IconButton(onClick = { }) {
            Icon(
                modifier = Modifier.size(52.dp),
                contentDescription = "",
                imageVector = Icons.Default.SkipPrevious,
                tint = Color.White
            )
        }
        IconButton(
            modifier = Modifier.size(80.dp),
            onClick = onPlay
        ) {
            Icon(
                modifier = Modifier.fillMaxSize(),
                contentDescription = "",
                imageVector = if (isPlaying)
                    Icons.Default.PauseCircleFilled
                else
                    Icons.Default.PlayCircleFilled,
                tint = Color.White
            )
        }
        IconButton(onClick = { }) {
            Icon(
                modifier = Modifier.size(52.dp),
                contentDescription = "",
                imageVector = Icons.Default.SkipNext,
                tint = Color.White
            )
        }
        IconButton(onClick = { }) {
            Icon(
                modifier = Modifier.size(28.dp),
                contentDescription = "",
                imageVector = Icons.Default.Loop,
                tint = Color.White
            )
        }
    }
}