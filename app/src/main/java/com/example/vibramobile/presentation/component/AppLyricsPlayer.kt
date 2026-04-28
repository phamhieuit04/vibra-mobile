package com.example.vibramobile.presentation.component

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Loop
import androidx.compose.material.icons.filled.PauseCircleFilled
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.core.ModalBottomSheet
import com.composables.core.Scrim
import com.composables.core.Sheet
import com.composables.core.SheetDetent
import com.composables.core.rememberModalBottomSheetState
import com.example.vibramobile.core.util.ImageHelper
import com.example.vibramobile.core.util.LyricsHelper
import com.example.vibramobile.presentation.viewmodel.MediaPlayerViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppLyricsPlayer(
    mediaPlayerViewModel: MediaPlayerViewModel = koinViewModel()
) {
    val context = LocalContext.current

    val uiState by mediaPlayerViewModel.uiState.collectAsState()
    val currentTime = uiState.currentPosition
    val totalTime = uiState.duration
    val song by mediaPlayerViewModel.currentSong.collectAsState()
    val rawLyrics = song?.listLyric ?: emptyList()
    val lyricLines = remember(rawLyrics) { LyricsHelper.parseLyrics(rawLyrics) }
    val activeLyricIndex by remember(lyricLines, currentTime) {
        derivedStateOf { LyricsHelper.findActiveIndex(lyricLines, currentTime) }
    }

    val FullyExpanded = SheetDetent.FullyExpanded
    val sheetState = rememberModalBottomSheetState(
        initialDetent = SheetDetent.Hidden,
        detents = listOf(SheetDetent.Hidden, FullyExpanded)
    )

    val listState = rememberLazyListState()

    val dominantColor by produceState<Color>(
        initialValue = Color.Gray,
        key1 = song?.thumbnailPath
    ) {
        value = ImageHelper.getDominantColor(context, song?.thumbnailPath)
    }

    LaunchedEffect(uiState.isLyricsVisible) {
        sheetState.targetDetent =
            if (uiState.isLyricsVisible) FullyExpanded else SheetDetent.Hidden
    }

    LaunchedEffect(activeLyricIndex) {
        if (activeLyricIndex >= 0) {
            listState.animateScrollToItem((activeLyricIndex - 2).coerceAtLeast(0))
        }
    }

    LaunchedEffect(sheetState.currentDetent) {
        if (sheetState.currentDetent == SheetDetent.Hidden && uiState.isLyricsVisible) {
            mediaPlayerViewModel.toggleLyrics(false)
        }
    }

    ModalBottomSheet(state = sheetState) {
        Scrim()

        Sheet(modifier = Modifier.imePadding()) {
            Scaffold(
                containerColor = dominantColor,
                topBar = {
                    LyricsTopBar(
                        songName = song?.name ?: "",
                        authorName = song?.author?.name ?: "",
                        onClick = { mediaPlayerViewModel.toggleLyrics(false) }
                    )
                },
                bottomBar = {
                    LyricsBottomBar(
                        progress = uiState.progress,
                        currentTime = currentTime,
                        totalTime = totalTime,
                        isPlaying = uiState.isPlaying,
                        onPlay = { mediaPlayerViewModel.toggle() },
                    )
                }
            ) { padding ->
                Box(
                    modifier = Modifier.padding(paddingValues = padding)
                ) {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
                        state = listState
                    ) {
                        itemsIndexed(lyricLines) { index, line ->
                            val isActive = index == activeLyricIndex
                            if (line.text.isBlank()) {
                                Spacer(Modifier.height(20.dp))
                            } else {
                                Text(
                                    text = line.text,
                                    color = if (isActive) Color.White else Color.White.copy(alpha = 0.55f),
                                    fontSize = if (isActive) 22.sp else 18.sp,
                                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                                    lineHeight = if (isActive) 30.sp else 24.sp
                                )
                            }
                            if (index < lyricLines.lastIndex) {
                                Spacer(Modifier.height(20.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LyricsTopBar(
    songName: String,
    authorName: String,
    onClick: () -> Unit,
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        navigationIcon = {
            IconButton(onClick = onClick) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    contentDescription = "Close",
                    imageVector = Icons.Default.KeyboardArrowDown,
                    tint = Color.White
                )
            }
        },
        actions = { },
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = songName,
                    color = Color.White,
                    fontSize = 15.sp,
                    lineHeight = 1.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = authorName,
                    color = Color.White,
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    )
}

@Composable
private fun LyricsBottomBar(
    progress: Float,
    currentTime: Long,
    totalTime: Long,
    isPlaying: Boolean,
    onPlay: () -> Unit = { },
    onShuffle: () -> Unit = { },
    onPrevious: () -> Unit = { },
    onNext: () -> Unit = { },
    onLoop: () -> Unit = { }
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProgressBarComponent(
            progress = progress,
            currentTime = currentTime,
            totalTime = totalTime
        )

        MediaControlsComponent(
            isPlaying = isPlaying,
            onPlay = onPlay
        )
    }
}