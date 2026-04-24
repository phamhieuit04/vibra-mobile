package com.example.vibramobile.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.vibramobile.core.util.FormatHelper
import com.example.vibramobile.domain.model.Playlist
import com.example.vibramobile.domain.model.Song
import com.example.vibramobile.domain.model.User
import com.example.vibramobile.presentation.component.ListAlbumComponent
import com.example.vibramobile.presentation.component.ListSongComponent
import com.example.vibramobile.presentation.component.SpotifySection
import com.example.vibramobile.presentation.component.TopSongsComponent
import com.example.vibramobile.presentation.config.LayoutStyle
import com.example.vibramobile.presentation.state.ArtistState
import com.example.vibramobile.presentation.state.SongState
import com.example.vibramobile.presentation.state.UiState
import com.example.vibramobile.presentation.viewmodel.ArtistDetailViewModel
import com.example.vibramobile.presentation.viewmodel.ContextMenuViewModel
import com.example.vibramobile.presentation.viewmodel.MediaPlayerViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistDetailScreen(
    modifier: Modifier = Modifier,
    artist: User,
    navigateBack: () -> Unit,
    artistDetailViewModel: ArtistDetailViewModel = koinViewModel(),
    mediaPlayerViewModel: MediaPlayerViewModel = koinViewModel(),
    contextMenuViewModel: ContextMenuViewModel = koinViewModel(),
) {
    val songs: List<Song> by SongState.songsByArtist.collectAsState()
    val albums: List<Playlist> by ArtistState.albumsByArtist.collectAsState()

    val isRefreshing by artistDetailViewModel.isRefreshing.collectAsState()
    val pullToRefreshState = rememberPullToRefreshState()
    val listState = rememberLazyListState()

    var showDropdownMenu by remember { mutableStateOf(false) }

    val headerImageHeight = 320.dp
    val scrollOffset by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex * 1000f + listState.firstVisibleItemScrollOffset
        }
    }
    val headerAlpha by animateFloatAsState(
        targetValue = (1f - scrollOffset / 800f).coerceIn(0f, 1f),
        animationSpec = tween(0),
        label = "headerAlpha"
    )
    val topBarAlpha by animateFloatAsState(
        targetValue = (scrollOffset / 600f).coerceIn(0f, 1f),
        animationSpec = tween(0),
        label = "topBarAlpha"
    )

    LaunchedEffect(Unit) {
        artistDetailViewModel.refresh(artist.id!!)
    }

    Box(modifier = modifier.fillMaxSize()) {
        PullToRefreshBox(
            modifier = Modifier.fillMaxSize(),
            state = pullToRefreshState,
            isRefreshing = isRefreshing,
            onRefresh = { artistDetailViewModel.refresh(artist.id!!) },
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
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item("header") {
                    ArtistDetailHeader(
                        headerImageHeight = headerImageHeight,
                        headerAlpha = headerAlpha,
                        artist = artist
                    )

                    Spacer(Modifier.height(6.dp))

                    ArtistDetailActionRow(
                        artist = artist,
                        showDropdownMenu = showDropdownMenu,
                        onShowDropdown = { showDropdownMenu = it }
                    )
                }

                if (songs.isNotEmpty()) {
                    item("popular_songs") {
                        SpotifySection(
                            title = "Bài hát phổ biến",
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            TopSongsComponent(
                                songs = songs.take(5),
                                onClick = {
                                    contextMenuViewModel.show(
                                        thumbnailPath = it.thumbnailPath,
                                        songTitle = it.name,
                                        artistName = it.author?.name
                                    )
                                },
                                onPlay = {
                                    mediaPlayerViewModel.playSong(song = it)
                                }
                            )
                        }
                    }

                    if (songs.size > 5) {
                        item("songs") {
                            SpotifySection(
                                title = "Danh sách bài hát",
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .padding(top = 12.dp)
                            ) {
                                ListSongComponent(
                                    songs = songs.drop(5),
                                    layoutStyle = LayoutStyle.Vertical,
                                    onClick = {
                                        contextMenuViewModel.show(
                                            thumbnailPath = it.thumbnailPath,
                                            songTitle = it.name,
                                            artistName = it.author?.name
                                        )
                                    },
                                    onPlay = {
                                        mediaPlayerViewModel.playSong(song = it)
                                    }
                                )
                            }
                        }
                    }
                }

                if (albums.isNotEmpty()) {
                    item("popular_albums") {
                        SpotifySection(
                            title = "Albums phổ biến",
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            ListAlbumComponent(albums = albums, onClick = { })
                        }
                    }
                }

                item("introduction") {
                    SpotifySection(
                        title = "Giới thiệu",
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        ArtistDetailIntroduction(artist = artist)
                    }
                }

                item(key = "bottom_spacer") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(
                                if (UiState.getDisplayMediaPlayer()) 192.dp else 96.dp
                            )
                    )
                }
            }
        }

        ArtistDetailTopBar(
            artist = artist,
            topBarAlpha = topBarAlpha,
            onClick = navigateBack
        )
    }
}

@Composable
private fun ArtistDetailHeader(
    headerImageHeight: Dp,
    headerAlpha: Float,
    artist: User
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(headerImageHeight)
            .graphicsLayer { alpha = headerAlpha }
    ) {
        AsyncImage(
            model = artist.avatarPath,
            contentDescription = artist.name,
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
                            Color.Black.copy(alpha = 0.3f),
                            Color.Black.copy(alpha = 0.85f)
                        ),
                        startY = 100f
                    )
                )
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Text(
                text = artist.name ?: "",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "${artist.followers?.let { FormatHelper.formatFollowers(it) } ?: "0"} người nghe hằng tháng",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun ArtistDetailIntroduction(
    artist: User
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Box {
            AsyncImage(
                model = artist.avatarPath,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            )
        }
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = artist.name ?: "",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${
                            artist.followers?.let {
                                FormatHelper.formatFollowers(
                                    it
                                )
                            } ?: "0"
                        } người nghe hằng tháng",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        fontSize = 13.sp
                    )
                }
                OutlinedButton(
                    onClick = { },
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = "Theo dõi",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 13.sp
                    )
                }
            }
            if (!artist.description.isNullOrBlank()) {
                Spacer(Modifier.height(12.dp))
                Text(
                    text = artist.description ?: "",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
private fun ArtistDetailTopBar(
    artist: User,
    topBarAlpha: Float,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.background.copy(alpha = topBarAlpha)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            AnimatedVisibility(
                visible = topBarAlpha > 0.8f,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    text = artist.name ?: "",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun ArtistDetailActionRow(
    artist: User,
    showDropdownMenu: Boolean,
    onShowDropdown: (Boolean) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AsyncImage(
                    model = artist.avatarPath,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(4.dp))
                )

                OutlinedButton(
                    onClick = { },
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = "Theo dõi",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Box {
                    IconButton(onClick = { onShowDropdown(true) }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More options",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    DropdownMenu(
                        expanded = showDropdownMenu,
                        onDismissRequest = { onShowDropdown(false) },
                        modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Hạn chế nghệ sĩ",
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            onClick = { onShowDropdown(false) }
                        )
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Shuffle,
                    contentDescription = "Shuffle",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { }
                )
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}