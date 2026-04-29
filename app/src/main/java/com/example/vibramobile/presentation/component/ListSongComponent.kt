package com.example.vibramobile.presentation.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.vibramobile.R
import com.example.vibramobile.domain.model.Song
import com.example.vibramobile.core.extension.noRippleClickable
import com.example.vibramobile.core.extension.skeletonEffect
import com.example.vibramobile.core.util.SongHelper
import com.example.vibramobile.presentation.config.LayoutStyleConfig
import io.ktor.http.encodeURLPath
import kotlin.math.sin

@Composable
fun ListSongComponent(
    modifier: Modifier = Modifier,
    songs: List<Song>,
    layoutStyle: LayoutStyleConfig = LayoutStyleConfig.Horizontal,
    onClick: (Song) -> Unit,
    onPlay: (Song) -> Unit,
    currentSongId: Int? = null,
    currentSongPath: String? = null
) {
    when (layoutStyle) {
        LayoutStyleConfig.Horizontal -> HorizontalListSong(
            songs = songs,
            onClick = onClick,
            onPlay = onPlay,
            currentSongId = currentSongId,
            currentSongPath = currentSongPath
        )

        LayoutStyleConfig.Vertical -> VerticalListSong(
            songs = songs,
            onClick = onClick,
            onPlay = onPlay,
            currentSongId = currentSongId,
            currentSongPath = currentSongPath
        )
    }
}

@Composable
fun HorizontalListSong(
    modifier: Modifier = Modifier,
    songs: List<Song>,
    onClick: (Song) -> Unit,
    onPlay: (Song) -> Unit,
    currentSongId: Int? = null,
    currentSongPath: String? = null
) {
    LazyRow {
        itemsIndexed(songs, key = { _, song -> song.id!! }) { _, song ->
            val isActive = SongHelper.songMatches(song, currentSongId, currentSongPath)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .noRippleClickable(onClick = { onPlay(song) }),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box {
                    AsyncImage(
                        modifier = Modifier
                            .size(140.dp)
                            .clip(CircleShape),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(song.thumbnailPath?.encodeURLPath())
                            .size(400)
                            .crossfade(true)
                            .build(),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(R.drawable.default_image),
                        error = painterResource(R.drawable.default_image)
                    )
                    if (isActive) {
                        Box(
                            modifier = Modifier
                                .size(140.dp)
                                .clip(CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            WaveformIcon(
                                color = Color.White,
                                size = 24.dp
                            )
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = song.name.toString(),
                            color = if (isActive) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onBackground,
                            fontSize = 15.sp,
                            lineHeight = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = song.author?.name.toString(),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp,
                            lineHeight = 12.sp
                        )
                    }
                    IconButton(onClick = { onClick(song) }) {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            imageVector = Icons.Default.MoreHoriz,
                            contentDescription = stringResource(R.string.cd_more),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            Spacer(Modifier.width(16.dp))
        }
    }
}

@Composable
fun VerticalListSong(
    modifier: Modifier = Modifier,
    songs: List<Song>,
    onClick: (Song) -> Unit,
    onPlay: (Song) -> Unit,
    currentSongId: Int? = null,
    currentSongPath: String? = null
) {
    Column {
        for (song in songs) {
            val isActive = SongHelper.songMatches(song, currentSongId, currentSongPath)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .noRippleClickable(onClick = { onPlay(song) }),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box {
                        AsyncImage(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(song.thumbnailPath?.encodeURLPath())
                                .size(400)
                                .crossfade(true)
                                .build(),
                            placeholder = painterResource(R.drawable.default_image),
                            error = painterResource(R.drawable.default_image)
                        )
                        if (isActive) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                WaveformIcon(
                                    color = Color.White,
                                    size = 14.dp
                                )
                            }
                        }
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            text = song.name.toString(),
                            color = if (isActive) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onBackground,
                            fontSize = 15.sp,
                            lineHeight = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = song.author?.name.toString(),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp,
                            lineHeight = 12.sp
                        )
                    }
                }
                IconButton(onClick = { onClick(song) }) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = Icons.Default.MoreHoriz,
                        contentDescription = stringResource(R.string.cd_more),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
fun WaveformIcon(
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    size: Dp = 18.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "waveform")

    val phase1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase1"
    )
    val phase2 by infiniteTransition.animateFloat(
        initialValue = (Math.PI / 2).toFloat(),
        targetValue = ((2 * Math.PI) + (Math.PI / 2)).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase2"
    )
    val phase3 by infiniteTransition.animateFloat(
        initialValue = (Math.PI).toFloat(),
        targetValue = ((2 * Math.PI) + Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1100, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase3"
    )

    Canvas(modifier = modifier.size(size)) {
        val barCount = 3
        val totalWidth = this.size.width
        val barWidth = totalWidth * 0.22f
        val gap = (totalWidth - barWidth * barCount) / (barCount + 1)
        val maxHeight = this.size.height
        val minHeight = maxHeight * 0.25f
        val phases = listOf(phase1, phase2, phase3)

        phases.forEachIndexed { i, phase ->
            val x = gap + i * (barWidth + gap)
            val heightFraction = ((sin(phase) + 1f) / 2f)
            val barHeight = minHeight + heightFraction * (maxHeight - minHeight)
            val top = (maxHeight - barHeight) / 2f

            drawRoundRect(
                color = color,
                topLeft = Offset(x, top),
                size = androidx.compose.ui.geometry.Size(barWidth, barHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(barWidth / 2)
            )
        }
    }
}

@Composable
fun ListSongSkeleton(modifier: Modifier = Modifier) {
    Row(modifier = modifier.horizontalScroll(state = rememberScrollState())) {
        for (i in 0..4) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .skeletonEffect()
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .size(width = 80.dp, height = 16.dp)
                                .skeletonEffect()
                        )
                        Spacer(Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .size(width = 52.dp, height = 12.dp)
                                .skeletonEffect()
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            imageVector = Icons.Default.MoreHoriz,
                            contentDescription = stringResource(R.string.cd_more),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.65f)
                        )
                    }
                }
            }
            Spacer(Modifier.width(16.dp))
        }
    }
}

@Composable
fun ListSongRowSkeleton(modifier: Modifier = Modifier) {
    Column {
        for (i in 0..3) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .skeletonEffect()
                    )
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Box(
                            modifier = Modifier
                                .size(width = 160.dp, height = 15.dp)
                                .skeletonEffect()
                        )
                        Spacer(Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .size(width = 96.dp, height = 15.dp)
                                .skeletonEffect()
                        )
                    }
                }
                IconButton(onClick = {}) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = Icons.Default.MoreHoriz,
                        contentDescription = stringResource(R.string.cd_more),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.65f)
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}