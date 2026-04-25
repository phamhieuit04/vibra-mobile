package com.example.vibramobile.presentation.screen

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.vibramobile.domain.model.User
import com.example.vibramobile.presentation.component.LibraryShimmer
import com.example.vibramobile.presentation.component.ListAlbumComponent
import com.example.vibramobile.presentation.component.ListArtistComponent
import com.example.vibramobile.presentation.component.ListSongComponent
import com.example.vibramobile.presentation.component.SpotifySection
import com.example.vibramobile.presentation.config.LayoutStyleConfig
import com.example.vibramobile.presentation.state.UiState
import com.example.vibramobile.presentation.state.UserState
import com.example.vibramobile.presentation.viewmodel.ContextMenuViewModel
import com.example.vibramobile.presentation.viewmodel.LibraryViewModel
import com.example.vibramobile.presentation.viewmodel.MediaPlayerViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LibraryScreen(
    modifier: Modifier = Modifier,
    bottomContentPadding: Dp = 0.dp,
    libraryViewModel: LibraryViewModel = koinViewModel(),
    contextMenuViewModel: ContextMenuViewModel = koinViewModel(),
    mediaPlayerViewModel: MediaPlayerViewModel = koinViewModel(),
    navigateToArtistDetail: (User) -> Unit
) {
    val currentUser = UserState.currentUser.collectAsState().value
    val likedSongs by UserState.likedSongs.collectAsState()
    val myPlaylists by UserState.myPlaylists.collectAsState()
    val followedArtists by UserState.followedArtists.collectAsState()

    val colorScheme = MaterialTheme.colorScheme

    val headerGradient = Brush.verticalGradient(
        colors = listOf(
            colorScheme.primary,
            colorScheme.background
        )
    )

    val isRefreshing by libraryViewModel.isRefreshing.collectAsState()
    val pullToRefreshState = rememberPullToRefreshState()

    val statusBarHeight = WindowInsets.statusBars
        .asPaddingValues()
        .calculateTopPadding()
    var topBarHeight by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    LaunchedEffect(Unit) {
        if (likedSongs.isEmpty() && myPlaylists.isEmpty() && followedArtists.isEmpty()) {
            libraryViewModel.refresh()
        }
    }

    PullToRefreshBox(
        modifier = Modifier.fillMaxSize(),
        state = pullToRefreshState,
        isRefreshing = isRefreshing,
        onRefresh = { libraryViewModel.refresh() },
        indicator = {
            PullToRefreshDefaults.Indicator(
                state = pullToRefreshState,
                isRefreshing = isRefreshing,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    ) {
        Crossfade(
            targetState = isRefreshing,
            label = "HomeContent"
        ) { loading ->
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .background(colorScheme.background),
                    contentPadding = PaddingValues(
                        bottom = bottomContentPadding,
                        top = topBarHeight
                    ),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(topBarHeight + statusBarHeight))
                    }

                    if (!loading) {
                        if (likedSongs.isNotEmpty()) {
                            item {
                                SpotifySection(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    title = "Bài hát yêu thích",
                                ) {
                                    ListSongComponent(
                                        songs = likedSongs,
                                        layoutStyle = LayoutStyleConfig.Vertical,
                                        onClick = {
                                            contextMenuViewModel.show(
                                                it.thumbnailPath,
                                                it.name,
                                                it.author?.name
                                            )
                                        },
                                        onPlay = {
                                            mediaPlayerViewModel.playSong(it)
                                        }
                                    )
                                }
                            }
                        }

                        if (myPlaylists.isNotEmpty()) {
                            item {
                                SpotifySection(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    title = "Playlist của bạn"
                                ) {
                                    ListAlbumComponent(
                                        albums = myPlaylists,
                                        layoutStyle = LayoutStyleConfig.Vertical,
                                        onClick = {
                                            contextMenuViewModel.show(
                                                it.thumbnailPath,
                                                it.name,
                                                it.author?.name
                                            )
                                        }
                                    )
                                }
                            }
                        }

                        if (followedArtists.isNotEmpty()) {
                            item {
                                SpotifySection(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    title = "Nghệ sĩ"
                                ) {
                                    ListArtistComponent(
                                        artists = followedArtists,
                                        onClick = { navigateToArtistDetail(it) }
                                    )
                                }
                            }
                        }
                    } else {
                        item {
                            LibraryShimmer()
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(headerGradient)
                        .padding(top = 28.dp + statusBarHeight, bottom = 32.dp)
                        .padding(horizontal = 16.dp)
                        .align(alignment = Alignment.TopCenter)
                        .onGloballyPositioned { coordinates ->
                            topBarHeight = with(density) { coordinates.size.height.toDp() }
                        }
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = currentUser?.avatarPath,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Thư viện của bạn",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = colorScheme.onBackground
                            )
                            Text(
                                text = currentUser?.name ?: "",
                                fontSize = 14.sp,
                                color = colorScheme.onBackground.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
        }
    }
}