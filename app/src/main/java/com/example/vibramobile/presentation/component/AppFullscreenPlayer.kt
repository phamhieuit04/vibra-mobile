package com.example.vibramobile.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.composables.core.ModalBottomSheet
import com.composables.core.Scrim
import com.composables.core.Sheet
import com.composables.core.SheetDetent
import com.composables.core.rememberModalBottomSheetState
import com.example.vibramobile.presentation.viewmodel.MediaPlayerViewModel
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.CupertinoMaterials
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import io.ktor.http.encodeURLPath
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun AppFullscreenPlayer(
    modifier: Modifier = Modifier,
    bottomContentPadding: Dp = 0.dp,
    hazeState: HazeState = rememberHazeState(),
    mediaPlayerViewModel: MediaPlayerViewModel = koinViewModel()
) {
    val isPlaying by mediaPlayerViewModel.isPlaying.collectAsState()
    val progress by mediaPlayerViewModel.progress.collectAsState()
    val currentSong by mediaPlayerViewModel.currentSong.collectAsState()
    val isFullscreenVisible by mediaPlayerViewModel.isFullscreenPlayerVisible.collectAsState()

    val FullyExpanded = SheetDetent.FullyExpanded

    val sheetState = rememberModalBottomSheetState(
        initialDetent = SheetDetent.Hidden,
        detents = listOf(SheetDetent.Hidden, FullyExpanded)
    )

    LaunchedEffect(isFullscreenVisible) {
        sheetState.targetDetent = if (isFullscreenVisible) FullyExpanded else SheetDetent.Hidden
    }

    LaunchedEffect(sheetState.currentDetent) {
        if (sheetState.currentDetent == SheetDetent.Hidden && isFullscreenVisible) {
            mediaPlayerViewModel.hideFullscreenPlayer()
        }
    }

    ModalBottomSheet(state = sheetState) {
        Scrim()

        Sheet(
            modifier = modifier
                .shadow(8.dp, RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp))
                .clip(RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp))
                .background(color = Color.Transparent)
                .fillMaxSize()
                .imePadding()
                .hazeEffect(
                    state = hazeState,
                    style = HazeMaterials.thin()
                )
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding(),
                contentPadding = PaddingValues(
                    start = 24.dp,
                    end = 24.dp,
                    top = 16.dp,
                    bottom = bottomContentPadding
                ),
            ) {
                item {
                    CenterAlignedTopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent
                        ),
                        navigationIcon = {
                            IconButton(onClick = { mediaPlayerViewModel.hideFullscreenPlayer() }) {
                                Icon(
                                    modifier = Modifier.size(32.dp),
                                    contentDescription = "Close",
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = { }) {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    imageVector = Icons.Default.MoreHoriz,
                                    contentDescription = "More",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        title = { }
                    )

                    Spacer(Modifier.height(16.dp))
                }

                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .size(320.dp)
                                .clip(shape = RoundedCornerShape(12.dp)),
                            contentDescription = "",
                            model = currentSong?.thumbnailPath?.encodeURLPath(),
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
                        Column {
                            Text(
                                text = currentSong?.name.toString(),
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 24.sp,
                                lineHeight = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = currentSong?.author?.name.toString(),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 15.sp
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
//                            IconButton(onClick = { }) {
//                                Icon(
//                                    modifier = Modifier.size(28.dp),
//                                    contentDescription = "",
//                                    imageVector = Icons.Default.AddCircle,
//                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
//                                )
//                            }
                            IconButton(onClick = { }) {
                                Icon(
                                    modifier = Modifier.size(28.dp),
                                    contentDescription = "",
                                    imageVector = Icons.Default.FavoriteBorder,
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))
                }

                item {
                    Column {
                        Box(
                            modifier = Modifier
                                .height(4.dp)
                                .padding(horizontal = 2.dp)
                                .fillMaxWidth()
                                .clip(shape = RoundedCornerShape(8.dp))
                                .background(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                        alpha = 0.28f
                                    )
                                )
                        ) {
                            Box(
                                modifier = Modifier
                                    .height(4.dp)
                                    .fillMaxWidth(progress)
                                    .clip(shape = RoundedCornerShape(8.dp))
                                    .background(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "0:08",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Text(
                                text = "3:15",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
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
                        IconButton(onClick = { }) {
                            Icon(
                                modifier = Modifier.size(28.dp),
                                contentDescription = "",
                                imageVector = Icons.Default.Shuffle,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        IconButton(
                            modifier = Modifier.size(64.dp),
                            onClick = { }
                        ) {
                            Icon(
                                modifier = Modifier.fillMaxSize(),
                                contentDescription = "",
                                imageVector = Icons.Default.SkipPrevious,
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        IconButton(
                            modifier = Modifier.size(96.dp),
                            onClick = { mediaPlayerViewModel.toggle() }
                        ) {
                            Icon(
                                modifier = Modifier.fillMaxSize(),
                                contentDescription = "",
                                imageVector = if (isPlaying)
                                    Icons.Default.PauseCircleFilled
                                else
                                    Icons.Default.PlayCircleFilled,
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        IconButton(
                            modifier = Modifier.size(64.dp),
                            onClick = { }
                        ) {
                            Icon(
                                modifier = Modifier.fillMaxSize(),
                                contentDescription = "",
                                imageVector = Icons.Default.SkipNext,
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        IconButton(onClick = { }) {
                            Icon(
                                modifier = Modifier.size(28.dp),
                                contentDescription = "",
                                imageVector = Icons.Default.Loop,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
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
                        IconButton(onClick = { }) {
                            Icon(
                                modifier = Modifier.size(28.dp),
                                contentDescription = "",
                                imageVector = Icons.Default.Queue,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        IconButton(onClick = { }) {
                            Icon(
                                modifier = Modifier.size(28.dp),
                                contentDescription = "",
                                imageVector = Icons.Default.LibraryMusic,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
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
}