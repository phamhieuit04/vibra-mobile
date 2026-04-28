package com.example.vibramobile.presentation.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
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
import kotlin.math.abs

@Composable
fun AppLyricsPlayer(
    mediaPlayerViewModel: MediaPlayerViewModel = koinViewModel()
) {
    val context = LocalContext.current

    val uiState by mediaPlayerViewModel.uiState.collectAsState()
    val currentTime = uiState.currentPosition
    val totalTime = uiState.duration
    val onSeek: (Float) -> Unit = { fraction ->
        if (totalTime > 0) {
            mediaPlayerViewModel.seekTo((totalTime.toFloat() * fraction).toLong())
        }
    }
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
        initialValue = Color(0xFF1A1A2E),
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
                        onSeek = onSeek
                    )
                }
            ) { padding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues = padding)
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            horizontal = 24.dp,
                            vertical = 80.dp
                        ),
                        state = listState
                    ) {
                        itemsIndexed(lyricLines) { index, line ->
                            if (line.text.isBlank()) {
                                Spacer(Modifier.height(28.dp))
                            } else {
                                LyricLineItem(
                                    text = line.text,
                                    distance = index - activeLyricIndex,
                                    isActive = index == activeLyricIndex
                                )
                            }
                            if (index < lyricLines.lastIndex) {
                                Spacer(Modifier.height(18.dp))
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .align(Alignment.TopCenter)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        dominantColor,
                                        Color.Transparent
                                    )
                                )
                            )
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .align(Alignment.BottomCenter)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        dominantColor
                                    )
                                )
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun LyricLineItem(
    text: String,
    distance: Int,
    isActive: Boolean
) {
    val targetAlpha = when {
        isActive -> 1f
        else -> 0.8f
    }

    val targetScale = when {
        isActive -> 1f
        else -> 0.86f
    }

    val targetFontSize = when {
        isActive -> 22f
        else -> 18f
    }

    val animatedAlpha by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = tween(durationMillis = 400),
        label = "lyric_alpha_$text"
    )

    val animatedScale by animateFloatAsState(
        targetValue = targetScale,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 200f),
        label = "lyric_scale_$text"
    )

    val animatedFontSize by animateFloatAsState(
        targetValue = targetFontSize,
        animationSpec = tween(durationMillis = 350),
        label = "lyric_size_$text"
    )

    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                alpha = animatedAlpha
                scaleX = animatedScale
                scaleY = animatedScale
                transformOrigin = TransformOrigin(0.5f, 0.5f)
            },
        color = Color.White,
        fontSize = animatedFontSize.sp,
        fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
        lineHeight = (animatedFontSize * 1.45f).sp,
        textAlign = TextAlign.Center,
        style = if (isActive) TextStyle(
            shadow = Shadow(
                color = Color.White.copy(alpha = 0.8f),
                offset = androidx.compose.ui.geometry.Offset(0f, 0f),
                blurRadius = 12f
            )
        ) else TextStyle.Default
    )
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
                    color = Color.White.copy(alpha = 0.7f),
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
    onSeek: (Float) -> Unit = { },
    onShuffle: () -> Unit = { },
    onPrevious: () -> Unit = { },
    onNext: () -> Unit = { },
    onLoop: () -> Unit = { }
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProgressBarComponent(
            progress = progress,
            currentTime = currentTime,
            totalTime = totalTime,
            onSeek = onSeek
        )

        MediaControlsComponent(
            isPlaying = isPlaying,
            onPlay = onPlay
        )
    }
}