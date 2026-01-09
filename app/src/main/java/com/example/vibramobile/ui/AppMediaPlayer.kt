package com.example.vibramobile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.vibramobile.states.SongState
import com.example.vibramobile.states.UiState
import com.example.vibramobile.ui.extends.noRippleClickable
import com.example.vibramobile.viewmodels.MediaPlayerViewModel
import io.ktor.http.encodeURLPath
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppMediaPlayer(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    viewModel: MediaPlayerViewModel = koinViewModel()
) {
    if (!isVisible) return

    val isPlaying by viewModel.isPlaying.collectAsState()
    val progress by viewModel.progress.collectAsState()
    val currentSong by SongState.currentSong

    Box(
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable(onClick = { UiState.setDisplaySongDetail(true) })
            .padding(8.dp)
            .clip(shape = RoundedCornerShape(8.dp))
            .background(color = Color(0xff79300f))
            .padding(start = 8.dp, end = 8.dp, top = 8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.padding(start = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(shape = RoundedCornerShape(4.dp)),
                    contentDescription = "",
                    model = currentSong?.thumbnail_path?.encodeURLPath()
                )
                Spacer(Modifier.width(8.dp))
                Column() {
                    Text(
                        text = currentSong?.name.toString(),
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 12.sp
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = currentSong?.author?.name.toString(),
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 10.sp,
                        lineHeight = 10.sp
                    )
                }
            }
            Row() {
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "",
                        tint = Color.White
                    )
                }
                IconButton(onClick = viewModel::toggle) {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        imageVector = if (isPlaying)
                            Icons.Default.Pause
                        else
                            Icons.Default.PlayArrow,
                        contentDescription = "",
                        tint = Color.White
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .height(2.dp)
                .padding(horizontal = 2.dp)
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(8.dp))
                .background(color = Color.Gray)
                .align(alignment = Alignment.BottomCenter)
        ) {
            Box(
                modifier = Modifier
                    .height(2.dp)
                    .fillMaxWidth(progress)
                    .clip(shape = RoundedCornerShape(8.dp))
                    .background(color = Color.White)
            )
        }
    }
}