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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.vibramobile.presentation.state.RepeatMode

@Composable
fun MediaControlsComponent(
    isPlaying: Boolean,
    isShuffleEnabled: Boolean = false,
    repeatMode: RepeatMode = RepeatMode.OFF,
    onPlay: () -> Unit = { },
    onShuffle: () -> Unit = { },
    onPrevious: () -> Unit = { },
    onNext: () -> Unit = { },
    onLoop: () -> Unit = { }
) {
    val activeColor = MaterialTheme.colorScheme.primary
    val inactiveColor = Color.White
    val shuffleTint = if (isShuffleEnabled) activeColor else inactiveColor
    val repeatTint = if (repeatMode != RepeatMode.OFF) activeColor else inactiveColor

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onShuffle) {
            Icon(
                modifier = Modifier.size(28.dp),
                contentDescription = "Shuffle",
                imageVector = Icons.Default.Shuffle,
                tint = shuffleTint
            )
        }
        IconButton(onClick = onPrevious) {
            Icon(
                modifier = Modifier.size(52.dp),
                contentDescription = "Previous",
                imageVector = Icons.Default.SkipPrevious,
                tint = inactiveColor
            )
        }
        IconButton(
            modifier = Modifier.size(80.dp),
            onClick = onPlay
        ) {
            Icon(
                modifier = Modifier.fillMaxSize(),
                contentDescription = "Play/Pause",
                imageVector = if (isPlaying)
                    Icons.Default.PauseCircleFilled
                else
                    Icons.Default.PlayCircleFilled,
                tint = inactiveColor
            )
        }
        IconButton(onClick = onNext) {
            Icon(
                modifier = Modifier.size(52.dp),
                contentDescription = "Next",
                imageVector = Icons.Default.SkipNext,
                tint = inactiveColor
            )
        }
        IconButton(onClick = onLoop) {
            Icon(
                modifier = Modifier.size(28.dp),
                contentDescription = "Repeat",
                imageVector = Icons.Default.Loop,
                tint = repeatTint
            )
        }
    }
}