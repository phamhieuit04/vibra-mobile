package com.example.vibramobile.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Loop
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.PauseCircleFilled
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.Queue
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.vibramobile.R
import com.example.vibramobile.states.SongState
import com.example.vibramobile.states.UiState
import com.example.vibramobile.ui.MediaPlayer
import io.ktor.http.encodeURLPath
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongDetailScreen(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    onVisibleChange: (Boolean) -> Unit
) {
    val state = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var progress by remember { mutableStateOf(0f) }

    LaunchedEffect(MediaPlayer.isPlaying.value) {
        while (MediaPlayer.isPlaying.value) {
            progress = MediaPlayer.getProgress()
            delay(1000)
        }
    }

    if (isVisible) {
        ModalBottomSheet(
            modifier = Modifier.fillMaxHeight(),
            sheetState = state,
            onDismissRequest = { onVisibleChange(false) },
            dragHandle = {},
            containerColor = Color(0xff79300f)
        ) {
            LazyColumn(
                modifier = Modifier.padding(top = 16.dp),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp)
            ) {
                item {
                    CenterAlignedTopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent
                        ),
                        navigationIcon = {
                            IconButton(onClick = { }) {
                                Icon(
                                    modifier = Modifier.size(32.dp),
                                    contentDescription = "",
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    tint = Color.White
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = {}) {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    imageVector = Icons.Default.MoreHoriz,
                                    contentDescription = "",
                                    tint = Color.White
                                )
                            }
                        },
                        title = {})

                    Spacer(Modifier.height(16.dp))
                }

                item {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .size(320.dp)
                                .clip(shape = RoundedCornerShape(12.dp)),
                            contentDescription = "",
                            model = SongState.currentSong.value?.thumbnail_path?.encodeURLPath(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(Modifier.height(16.dp))
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column() {
                            Text(
                                text = SongState.currentSong.value?.name.toString(),
                                color = Color.White,
                                fontSize = 24.sp,
                                lineHeight = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = SongState.currentSong.value?.author?.name.toString(),
                                color = Color.LightGray,
                                fontSize = 15.sp
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            IconButton(onClick = {}) {
                                Icon(
                                    modifier = Modifier.size(28.dp),
                                    contentDescription = "",
                                    imageVector = Icons.Default.AddCircle,
                                    tint = Color.White
                                )
                            }
                            IconButton(onClick = {}) {
                                Icon(
                                    modifier = Modifier.size(28.dp),
                                    contentDescription = "",
                                    imageVector = Icons.Default.FavoriteBorder,
                                    tint = Color.White
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))
                }

                item {
                    Column() {
                        Box(
                            modifier = Modifier
                                .height(4.dp)
                                .padding(horizontal = 2.dp)
                                .fillMaxWidth()
                                .clip(shape = RoundedCornerShape(8.dp))
                                .background(color = Color.Gray)
                        ) {
                            Box(
                                modifier = Modifier
                                    .height(4.dp)
                                    .fillMaxWidth(progress)
                                    .clip(shape = RoundedCornerShape(8.dp))
                                    .background(color = Color.White)
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "0:08", color = Color.LightGray.copy(alpha = 0.8f))
                            Text(text = "3:15", color = Color.LightGray.copy(alpha = 0.8f))
                        }
                    }

                    Spacer(Modifier.height(16.dp))
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {}) {
                            Icon(
                                modifier = Modifier.size(28.dp),
                                contentDescription = "",
                                imageVector = Icons.Default.Shuffle,
                                tint = Color.White
                            )
                        }
                        IconButton(
                            modifier = Modifier.size(64.dp),
                            onClick = {}
                        ) {
                            Icon(
                                modifier = Modifier.fillMaxSize(),
                                contentDescription = "",
                                imageVector = Icons.Default.SkipPrevious,
                                tint = Color.White
                            )
                        }
                        IconButton(
                            modifier = Modifier.size(96.dp),
                            onClick = { MediaPlayer.playOrPause() }
                        ) {
                            Icon(
                                modifier = Modifier.fillMaxSize(),
                                contentDescription = "",
                                imageVector = if (MediaPlayer.isPlaying.value) Icons.Default.PauseCircleFilled
                                else Icons.Default.PlayCircleFilled,
                                tint = Color.White
                            )
                        }
                        IconButton(
                            modifier = Modifier.size(64.dp),
                            onClick = {}
                        ) {
                            Icon(
                                modifier = Modifier.fillMaxSize(),
                                contentDescription = "",
                                imageVector = Icons.Default.SkipNext,
                                tint = Color.White
                            )
                        }
                        IconButton(onClick = {}) {
                            Icon(
                                modifier = Modifier.size(28.dp),
                                contentDescription = "",
                                imageVector = Icons.Default.Loop,
                                tint = Color.White
                            )
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {}) {
                            Icon(
                                modifier = Modifier.size(28.dp),
                                contentDescription = "",
                                imageVector = Icons.Default.Queue,
                                tint = Color.White
                            )
                        }
                        IconButton(onClick = { UiState.setDisplayQueuePlaylist(true) }) {
                            Icon(
                                modifier = Modifier.size(28.dp),
                                contentDescription = "",
                                imageVector = Icons.Default.LibraryMusic,
                                tint = Color.White
                            )
                        }
                    }

                    Spacer(Modifier.height(32.dp))
                }

                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp)
                    ) { }
                }
            }
        }
    }

    QueuePlaylistScreen(isVisible = UiState.getDisplayQueuePlaylist(), onVisibleChange = { value ->
        UiState.setDisplayQueuePlaylist(value)
    })
}

//@Preview(showBackground = true, device = "id:pixel_3", backgroundColor = 0xff000000)
//@Composable
//fun Preview(modifier: Modifier = Modifier) {
//    SongDetailScreen(isVisible = true, onVisibleChange = {})
//}