package com.example.vibramobile.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.vibramobile.R
import com.example.vibramobile.domain.model.Playlist
import com.example.vibramobile.presentation.component.DetailActionComponent
import com.example.vibramobile.presentation.component.DetailTopbarComponent
import com.example.vibramobile.presentation.component.ListSongComponent
import com.example.vibramobile.presentation.component.SpotifySection
import com.example.vibramobile.presentation.config.DetailActionConfig
import com.example.vibramobile.presentation.config.LayoutStyleConfig
import com.example.vibramobile.presentation.state.SongState
import com.example.vibramobile.presentation.viewmodel.AlbumDetailViewModel
import com.example.vibramobile.presentation.viewmodel.ContextMenuViewModel
import com.example.vibramobile.presentation.viewmodel.MediaPlayerViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumDetailScreen(
    modifier: Modifier = Modifier,
    bottomContentPadding: Dp = 0.dp,
    album: Playlist,
    navigateBack: () -> Unit,
    albumDetailViewModel: AlbumDetailViewModel = koinViewModel(),
    mediaPlayerViewModel: MediaPlayerViewModel = koinViewModel(),
    contextMenuViewModel: ContextMenuViewModel = koinViewModel(),
) {
    val isRefreshing = albumDetailViewModel.isRefreshing.collectAsState().value
    val songs by SongState.songsByAlbum.collectAsState()

    val pullToRefreshState = rememberPullToRefreshState()
    val listState = rememberLazyListState()

    val scrollOffset by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex * 1000f + listState.firstVisibleItemScrollOffset
        }
    }
    val topBarAlpha by animateFloatAsState(
        targetValue = (scrollOffset / 500f).coerceIn(0f, 1f),
        animationSpec = tween(0),
        label = "topBarAlpha"
    )

    LaunchedEffect(Unit) {
        if (songs.isEmpty()) {
            albumDetailViewModel.refresh(album.id!!)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        PullToRefreshBox(
            modifier = Modifier.fillMaxSize(),
            state = pullToRefreshState,
            isRefreshing = isRefreshing,
            onRefresh = { albumDetailViewModel.refresh(album.id!!) },
            indicator = {
                PullToRefreshDefaults.Indicator(
                    state = pullToRefreshState,
                    isRefreshing = isRefreshing,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = bottomContentPadding)
            ) {
                item("header") {
                    AlbumDetailHeader(album = album)

                    DetailActionComponent(
                        config = DetailActionConfig.Album(
                            onAddToQueue = { mediaPlayerViewModel.enqueueSongs(songs = songs) },
                            onDownload = {},
                        ),
                        onShuffle = {
                            mediaPlayerViewModel.playAll(
                                songs = songs,
                                startIndex = 0,
                                prioritize = true,
                                enableShuffle = true
                            )
                        },
                        onPlay = {
                            mediaPlayerViewModel.playAll(
                                songs = songs,
                                startIndex = 0,
                                prioritize = true,
                                enableShuffle = false
                            )
                        },
                    )
                }

                if (songs.isNotEmpty()) {
                    item("songs") {
                        SpotifySection(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            title = stringResource(R.string.album_songs)
                        ) {
                            ListSongComponent(
                                songs = songs,
                                layoutStyle = LayoutStyleConfig.Vertical,
                                onClick = {
                                    contextMenuViewModel.showSong(it)
                                },
                                onPlay = {
                                    mediaPlayerViewModel.playSong(song = it)
                                }
                            )
                        }
                    }
                }
            }
        }

        DetailTopbarComponent(
            title = album.name ?: "",
            topBarAlpha = topBarAlpha,
            onBackClick = navigateBack
        )
    }
}

@Composable
private fun AlbumDetailHeader(album: Playlist) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.08f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            .padding(top = 72.dp, bottom = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = album.thumbnailPath,
            contentDescription = album.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth(0.58f)
                .aspectRatio(1f)
                .shadow(
                    elevation = 24.dp,
                    shape = RoundedCornerShape(4.dp),
                    ambientColor = Color.Black.copy(alpha = 0.6f),
                    spotColor = Color.Black.copy(alpha = 0.6f)
                )
                .clip(RoundedCornerShape(4.dp))
        )

        Spacer(Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = album.name ?: "",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (!album.author?.avatarPath.isNullOrBlank()) {
                    AsyncImage(
                        model = album.author?.avatarPath,
                        contentDescription = album.author?.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                    )
                }
                Text(
                    text = album.author?.name ?: "",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(Modifier.height(4.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(R.string.album_label),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    fontSize = 13.sp
                )
                val totalSong = album.totalSong
                if (totalSong != null) {
                    Text(
                        text = stringResource(R.string.separator_dot),
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                        fontSize = 13.sp
                    )
                    Text(
                        text = stringResource(R.string.song_count_format, totalSong),
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}