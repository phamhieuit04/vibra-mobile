package com.example.vibramobile.presentation.screen

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LibraryMusic
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.vibramobile.R
import com.example.vibramobile.domain.model.Playlist
import com.example.vibramobile.domain.model.Song
import com.example.vibramobile.domain.model.User
import com.example.vibramobile.presentation.component.LibraryShimmer
import com.example.vibramobile.presentation.component.ListAlbumComponent
import com.example.vibramobile.presentation.component.ListArtistComponent
import com.example.vibramobile.presentation.component.ListSongComponent
import com.example.vibramobile.presentation.component.SpotifySection
import com.example.vibramobile.presentation.config.LayoutStyleConfig
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
    val likedSongs: List<Song> by UserState.likedSongs.collectAsState()
    val myPlaylists: List<Playlist> by UserState.myPlaylists.collectAsState()
    val followedArtists: List<User> by UserState.followedArtists.collectAsState()

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

    val isEmptyLibrary =
        likedSongs.isEmpty() &&
                myPlaylists.isEmpty() &&
                followedArtists.isEmpty()

    LaunchedEffect(Unit) {
        if (isEmptyLibrary) {
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
            label = "LibraryContent"
        ) { loading ->

            Box(modifier = Modifier.fillMaxSize()) {

                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .background(colorScheme.background),
                    contentPadding = PaddingValues(
                        top = topBarHeight,
                        bottom = bottomContentPadding
                    ),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {

                    item {
                        Spacer(
                            modifier = Modifier.height(
                                topBarHeight + statusBarHeight
                            )
                        )
                    }

                    if (!loading) {

                        if (isEmptyLibrary) {
                            item {
                                EmptyLibraryState(
                                    modifier = Modifier
                                        .fillParentMaxSize()
                                        .padding(horizontal = 32.dp)
                                )
                            }
                        }

                        if (likedSongs.isNotEmpty()) {
                            item {
                                SpotifySection(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    title = stringResource(R.string.library_favorite_songs),
                                ) {
                                    ListSongComponent(
                                        songs = likedSongs,
                                        layoutStyle = LayoutStyleConfig.Vertical,
                                        onClick = {
                                            contextMenuViewModel.showSong(it)
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
                                    title = stringResource(R.string.library_your_playlists)
                                ) {
                                    ListAlbumComponent(
                                        albums = myPlaylists,
                                        layoutStyle = LayoutStyleConfig.Vertical,
                                        onClick = { }
                                    )
                                }
                            }
                        }

                        if (followedArtists.isNotEmpty()) {
                            item {
                                SpotifySection(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    title = stringResource(R.string.label_artist)
                                ) {
                                    ListArtistComponent(
                                        artists = followedArtists,
                                        onClick = {
                                            navigateToArtistDetail(it)
                                        }
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
                        .padding(
                            top = 28.dp + statusBarHeight,
                            bottom = 32.dp
                        )
                        .padding(horizontal = 16.dp)
                        .align(Alignment.TopCenter)
                        .onGloballyPositioned { coordinates ->
                            topBarHeight = with(density) {
                                coordinates.size.height.toDp()
                            }
                        }
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

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
                                text = stringResource(R.string.library_title),
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

@Composable
private fun EmptyLibraryState(
    modifier: Modifier = Modifier
) {

    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Icon(
            imageVector = Icons.Rounded.LibraryMusic,
            contentDescription = null,
            modifier = Modifier.size(72.dp),
            tint = colorScheme.onBackground.copy(alpha = 0.35f)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Thư viện trống",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Các bài hát yêu thích, playlist và nghệ sĩ theo dõi sẽ xuất hiện tại đây.",
            style = MaterialTheme.typography.bodyMedium,
            color = colorScheme.onBackground.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )
    }
}