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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.composables.core.ModalBottomSheet
import com.composables.core.Scrim
import com.composables.core.Sheet
import com.composables.core.SheetDetent
import com.composables.core.rememberModalBottomSheetState
import com.example.vibramobile.presentation.viewmodel.MediaPlayerViewModel
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import io.ktor.http.encodeURLPath
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun AppFullscreenPlayer(
    modifier: Modifier = Modifier,
    bottomContentPadding: Dp = 0.dp,
    mediaPlayerViewModel: MediaPlayerViewModel = koinViewModel()
) {
    val uiState by mediaPlayerViewModel.uiState.collectAsState()
    val song by mediaPlayerViewModel.currentSong.collectAsState()

    val FullyExpanded = SheetDetent.FullyExpanded

    val sheetState = rememberModalBottomSheetState(
        initialDetent = SheetDetent.Hidden,
        detents = listOf(SheetDetent.Hidden, FullyExpanded)
    )

    LaunchedEffect(uiState.isFullscreenVisible) {
        sheetState.targetDetent =
            if (uiState.isFullscreenVisible) FullyExpanded else SheetDetent.Hidden
    }

    LaunchedEffect(sheetState.currentDetent) {
        if (sheetState.currentDetent == SheetDetent.Hidden && uiState.isFullscreenVisible) {
            mediaPlayerViewModel.toggleFullscreen(false)
        }
    }

    ModalBottomSheet(state = sheetState) {
        Scrim()

        Sheet(
            modifier = modifier.imePadding()
        ) {
            Box() {
                Box(modifier = Modifier.fillMaxSize()) {
                    AsyncImage(
                        model = song?.thumbnailPath?.encodeURLPath(),
                        contentDescription = null,
                        modifier = Modifier
                            .matchParentSize()
                            .blur(36.dp),
                        contentScale = ContentScale.Crop
                    )

                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                Brush.verticalGradient(
                                    colorStops = arrayOf(
                                        0.0f to Color.Black.copy(alpha = 0.25f),
                                        0.2f to Color.Black.copy(alpha = 0.3f),
                                        0.5f to Color.Transparent,
                                        0.75f to Color.Black.copy(alpha = 0.3f),
                                        1.0f to Color.Black.copy(alpha = 0.45f)
                                    )
                                )
                            )
                    )
                }

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
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    item {
                        CenterAlignedTopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color.Transparent
                            ),
                            navigationIcon = {
                                IconButton(onClick = {
                                    mediaPlayerViewModel.toggleFullscreen(false)
                                }) {
                                    Icon(
                                        modifier = Modifier.size(32.dp),
                                        contentDescription = "Close",
                                        imageVector = Icons.Default.KeyboardArrowDown,
                                        tint = Color.White
                                    )
                                }
                            },
                            actions = {
                                IconButton(onClick = { }) {
                                    Icon(
                                        modifier = Modifier.size(24.dp),
                                        imageVector = Icons.Default.MoreHoriz,
                                        contentDescription = "More",
                                        tint = Color.White
                                    )
                                }
                            },
                            title = { }
                        )
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
                                model = song?.thumbnailPath?.encodeURLPath(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = song?.name.toString(),
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    lineHeight = 1.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = song?.author?.name.toString(),
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    lineHeight = 18.sp
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                IconButton(onClick = { }) {
                                    Icon(
                                        modifier = Modifier.size(28.dp),
                                        contentDescription = "",
                                        imageVector = Icons.Default.AddCircle,
                                        tint = Color.White
                                    )
                                }
                                IconButton(onClick = { }) {
                                    Icon(
                                        modifier = Modifier.size(28.dp),
                                        contentDescription = "",
                                        imageVector = Icons.Default.FavoriteBorder,
                                        tint = Color.White
                                    )
                                }
                            }
                        }
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
                                        color = Color.White.copy(
                                            alpha = 0.28f
                                        )
                                    )
                            ) {
                                Box(
                                    modifier = Modifier
                                        .height(4.dp)
                                        .fillMaxWidth(uiState.progress)
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
                                Text(
                                    text = "0:08",
                                    color = Color.White,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = "3:15",
                                    color = Color.White,
                                    fontSize = 15.sp
                                )
                            }
                        }
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
                                    tint = Color.White
                                )
                            }
                            IconButton(
                                onClick = { }
                            ) {
                                Icon(
                                    modifier = Modifier.size(52.dp),
                                    contentDescription = "",
                                    imageVector = Icons.Default.SkipPrevious,
                                    tint = Color.White
                                )
                            }
                            IconButton(
                                modifier = Modifier.size(80.dp),
                                onClick = { mediaPlayerViewModel.toggle() }
                            ) {
                                Icon(
                                    modifier = Modifier.fillMaxSize(),
                                    contentDescription = "",
                                    imageVector = if (uiState.isPlaying)
                                        Icons.Default.PauseCircleFilled
                                    else
                                        Icons.Default.PlayCircleFilled,
                                    tint = Color.White
                                )
                            }
                            IconButton(
                                onClick = { }
                            ) {
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
                                    tint = Color.White
                                )
                            }
                            IconButton(onClick = { }) {
                                Icon(
                                    modifier = Modifier.size(28.dp),
                                    contentDescription = "",
                                    imageVector = Icons.Default.LibraryMusic,
                                    tint = Color.White
                                )
                            }
                        }
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
}