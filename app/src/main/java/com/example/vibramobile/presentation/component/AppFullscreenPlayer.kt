package com.example.vibramobile.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Loop
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PauseCircleFilled
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.Queue
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.composables.core.ModalBottomSheet
import com.composables.core.Scrim
import com.composables.core.Sheet
import com.composables.core.SheetDetent
import com.composables.core.rememberModalBottomSheetState
import com.example.vibramobile.core.extension.noRippleClickable
import com.example.vibramobile.core.util.FormatHelper
import com.example.vibramobile.core.util.ImageHelper
import com.example.vibramobile.domain.model.Song
import com.example.vibramobile.presentation.state.SongState
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
    val context = LocalContext.current

    val uiState by mediaPlayerViewModel.uiState.collectAsState()
    val song by mediaPlayerViewModel.currentSong.collectAsState()
    val lyrics = song?.listLyric ?: emptyList()
    val songsByArtist by SongState.songsByArtist.collectAsState()

    val FullyExpanded = SheetDetent.FullyExpanded

    val sheetState = rememberModalBottomSheetState(
        initialDetent = SheetDetent.Hidden,
        detents = listOf(SheetDetent.Hidden, FullyExpanded)
    )

    val listState = rememberLazyListState()

    val topBarAlpha by remember {
        derivedStateOf {
            val item = listState.layoutInfo.visibleItemsInfo
                .find { it.index == 2 }

            if (item != null) {
                val offset = item.offset
                (-offset / 300f).coerceIn(0f, 1f)
            } else {
                if (listState.firstVisibleItemIndex > 2) 1f else 0f
            }
        }
    }

    val dominantColor by produceState<Color>(
        initialValue = Color.Gray,
        key1 = song?.thumbnailPath
    ) {
        value = ImageHelper.getDominantColor(context, song?.thumbnailPath)
    }

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

        Sheet(modifier = modifier.imePadding()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                Box(modifier = Modifier.matchParentSize()) {
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
                    state = listState,
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
                    item("cover") {
                        Column(modifier = Modifier.fillMaxWidth()) {
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
                    }

                    item("info") {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
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
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    IconButton(
                                        onClick = { },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            contentDescription = "",
                                            imageVector = Icons.Default.AddCircle,
                                            tint = Color.White
                                        )
                                    }
                                    IconButton(
                                        onClick = { },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            contentDescription = "",
                                            imageVector = Icons.Default.FavoriteBorder,
                                            tint = Color.White
                                        )
                                    }
                                }
                            }

                            Column {
                                FullscreenProgressBar(
                                    modifier = Modifier.padding(horizontal = 2.dp),
                                    progress = uiState.progress
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "0:08", color = Color.White, fontSize = 15.sp)
                                    Text(text = "3:15", color = Color.White, fontSize = 15.sp)
                                }
                            }
                        }
                    }

                    item("controls") {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
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
                    }

                    if (songsByArtist.isNotEmpty()) {
                        item("explore_songs") {
                            Spacer(Modifier.height(24.dp))

                            ExploreSongsSection(
                                artistName = song?.author?.name ?: "",
                                songs = songsByArtist,
                                onClick = { mediaPlayerViewModel.playSong(it) }
                            )
                        }
                    }

                    if (lyrics.any { it.isNotBlank() }) {
                        item("lyrics_preview") {
                            LyricsPreviewSection(lyrics = lyrics)
                        }
                    }

                    item("about_artist") {
                        song?.author?.let { artist ->
                            AboutArtistSection(
                                artistName = artist.name ?: "",
                                artistAvatarPath = artist.avatarPath,
                                followers = artist.followers ?: 0
                            )
                        }
                    }
                }

                FullscreenTopbar(
                    song = song!!,
                    topBarAlpha = topBarAlpha,
                    dominantColor = dominantColor,
                    isPlaying = uiState.isPlaying,
                    progress = uiState.progress,
                    onToggle = { mediaPlayerViewModel.toggle() }
                )
            }
        }
    }
}

@Composable
private fun LyricsPreviewSection(lyrics: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.1f))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        Text(
            text = "Lyrics preview",
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(12.dp))

        lyrics.take(3).forEach { line ->
            Text(
                text = line,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
            )

            Spacer(Modifier.height(8.dp))
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            shape = RoundedCornerShape(50)
        ) {
            Text(
                text = "Show lyrics",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun AboutArtistSection(
    artistName: String,
    artistAvatarPath: String?,
    followers: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF1A1A1A))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3f / 4f)
        ) {
            AsyncImage(
                model = artistAvatarPath?.encodeURLPath(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Text(
                text = "About the artist",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(14.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = artistName,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = FormatHelper.formatMonthlyListeners(followers),
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 13.sp
                )
            }
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ),
                border = androidx.compose.foundation.BorderStroke(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.8f)
                ),
                shape = RoundedCornerShape(50),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Follow",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun FullscreenTopbar(
    song: Song,
    dominantColor: Color,
    topBarAlpha: Float,
    isPlaying: Boolean,
    progress: Float,
    onToggle: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(dominantColor.copy(alpha = topBarAlpha))
            .statusBarsPadding()
    ) {
        AnimatedVisibility(
            visible = topBarAlpha > 0.8f,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.padding(start = 20.dp)
                    ) {
                        Text(
                            text = song.name.toString(),
                            color = Color.White,
                            fontSize = 18.sp,
                            lineHeight = 1.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = song.author?.name.toString(),
                            color = Color.White,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    }
                    Row(modifier = Modifier.padding(end = 10.dp)) {
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        IconButton(onClick = onToggle) {
                            Icon(
                                imageVector = if (isPlaying)
                                    Icons.Default.Pause
                                else
                                    Icons.Default.PlayArrow,
                                contentDescription = "",
                                tint = Color.White,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                }

                FullscreenProgressBar(
                    progress = progress,
                    height = 3.dp,
                    roundedCornerShape = RoundedCornerShape(0.dp)
                )
            }
        }
    }
}

@Composable
private fun FullscreenProgressBar(
    modifier: Modifier = Modifier,
    progress: Float,
    height: Dp = 4.dp,
    roundedCornerShape: RoundedCornerShape = RoundedCornerShape(8.dp)
) {
    Box(
        modifier = modifier
            .height(height)
            .fillMaxWidth()
            .clip(shape = roundedCornerShape)
            .background(color = Color.White.copy(alpha = 0.28f))
    ) {
        Box(
            modifier = Modifier
                .height(height)
                .fillMaxWidth(progress)
                .clip(shape = roundedCornerShape)
                .background(color = Color.White)
        )
    }
}

@Composable
private fun ExploreSongsSection(
    artistName: String,
    songs: List<Song>,
    onClick: (Song) -> Unit
) {
    val cardBg = Color.White.copy(alpha = 0.1f)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(cardBg)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Explore $artistName",
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(songs) { song ->
                Box(
                    modifier = Modifier
                        .size(width = 110.dp, height = 110.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .noRippleClickable(onClick = { onClick(song) })
                ) {
                    AsyncImage(
                        model = song.thumbnailPath?.encodeURLPath(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.7f)
                                    )
                                )
                            )
                    )
                    Text(
                        text = song.name ?: "",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}