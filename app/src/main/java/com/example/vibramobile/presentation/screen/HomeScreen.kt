package com.example.vibramobile.presentation.screen

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.util.UnstableApi
import com.example.vibramobile.R
import com.example.vibramobile.core.util.SystemUtils
import com.example.vibramobile.domain.model.Category
import com.example.vibramobile.domain.model.Playlist
import com.example.vibramobile.domain.model.User
import com.example.vibramobile.presentation.state.ArtistState
import com.example.vibramobile.presentation.state.CategoryState
import com.example.vibramobile.presentation.state.SongState
import com.example.vibramobile.presentation.component.HomeShimmer
import com.example.vibramobile.presentation.component.ListAlbumComponent
import com.example.vibramobile.presentation.component.ListArtistComponent
import com.example.vibramobile.presentation.component.ListSongComponent
import com.example.vibramobile.presentation.component.OfflineContentComponent
import com.example.vibramobile.presentation.component.SpotifySection
import com.example.vibramobile.presentation.component.TopArtistsComponent
import com.example.vibramobile.presentation.component.TopSongsComponent
import com.example.vibramobile.presentation.config.LayoutStyleConfig
import com.example.vibramobile.presentation.viewmodel.ContextMenuViewModel
import com.example.vibramobile.presentation.viewmodel.HomeViewModel
import com.example.vibramobile.presentation.viewmodel.MediaPlayerViewModel
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import org.koin.androidx.compose.koinViewModel

@UnstableApi
@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    bottomContentPadding: Dp = 0.dp,
    homeViewModel: HomeViewModel = koinViewModel(),
    mediaPlayerViewModel: MediaPlayerViewModel = koinViewModel(),
    contextMenuViewModel: ContextMenuViewModel = koinViewModel(),
    navigateToGenreDetail: (Category) -> Unit,
    navigateToSearch: () -> Unit,
    navigateToArtistDetail: (User) -> Unit,
    navigateToAlbumDetail: (Playlist) -> Unit
) {
    val isRefreshing by homeViewModel.isRefreshing.collectAsState()
    val pullToRefreshState = rememberPullToRefreshState()
    val scrollState = rememberLazyListState()
    val hazeState = rememberHazeState()
    val isOnline by rememberIsOnline()

    var topBarHeight by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current
    val blurProgress by remember {
        derivedStateOf {
            val firstVisibleItemIndex = scrollState.firstVisibleItemIndex
            val firstVisibleItemScrollOffset = scrollState.firstVisibleItemScrollOffset
            val totalScroll = (firstVisibleItemIndex * 500f) + firstVisibleItemScrollOffset
            (totalScroll / 300f).coerceIn(0f, 1f)
        }
    }

    val categories by CategoryState.categories.collectAsState()
    val recentRotationSongs by SongState.recentRotationSongs.collectAsState()
    val recommendedSongs by SongState.recommendedSongs.collectAsState()
    val popularSongs by SongState.popularSongs.collectAsState()
    val popularAlbums by SongState.popularAlbums.collectAsState()
    val popularArtists by ArtistState.popularArtists.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        PullToRefreshBox(
            modifier = Modifier.hazeSource(hazeState),
            state = pullToRefreshState,
            isRefreshing = isRefreshing,
            onRefresh = { homeViewModel.fetchAll(forceRemote = true) },
            indicator = {
                PullToRefreshDefaults.Indicator(
                    state = pullToRefreshState,
                    isRefreshing = isRefreshing,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = topBarHeight)
                )
            }
        ) {
            Crossfade(
                targetState = isOnline to isRefreshing,
                label = "HomeContent"
            ) { (online, loading) ->
                when {
                    !online -> {
                        OfflineContentComponent(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = topBarHeight + 16.dp)
                        )
                    }

                    !loading -> {
                        LazyColumn(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            contentPadding = PaddingValues(
                                top = topBarHeight + 16.dp,
                                bottom = bottomContentPadding
                            ),
                            state = scrollState,
                            verticalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            item(key = "recent_rotation") {
                                AnimatedVisibility(visible = recentRotationSongs.isNotEmpty()) {
                                    SpotifySection(title = stringResource(R.string.home_recently_listened)) {
                                        ListSongComponent(
                                            layoutStyle = LayoutStyleConfig.Vertical,
                                            onClick = {
                                                contextMenuViewModel.showSong(it)
                                            },
                                            onPlay = { mediaPlayerViewModel.playSong(song = it) },
                                            songs = recentRotationSongs
                                        )
                                    }
                                }
                            }

                            item(key = "recommended") {
                                AnimatedVisibility(visible = recommendedSongs.isNotEmpty()) {
                                    SpotifySection(title = stringResource(R.string.home_for_you)) {
                                        TopSongsComponent(
                                            songs = recommendedSongs.take(10),
                                            onClick = {
                                                contextMenuViewModel.showSong(it)
                                            },
                                            onPlay = { mediaPlayerViewModel.playSong(song = it) },
                                        )
                                    }
                                }
                            }

                            item(key = "top_artists") {
                                AnimatedVisibility(visible = popularArtists.isNotEmpty()) {
                                    SpotifySection(title = stringResource(R.string.home_featured_artists)) {
                                        TopArtistsComponent(
                                            artists = popularArtists.take(5),
                                            onClick = { navigateToArtistDetail(it) }
                                        )
                                        Spacer(Modifier.height(16.dp))
                                        ListArtistComponent(
                                            artists = popularArtists.drop(5).take(5),
                                            onClick = { navigateToArtistDetail(it) }
                                        )
                                        Spacer(Modifier.height(16.dp))
                                        ListArtistComponent(
                                            layoutStyle = LayoutStyleConfig.Vertical,
                                            artists = popularArtists.drop(10).take(10),
                                            onClick = { navigateToArtistDetail(it) }
                                        )
                                    }
                                }
                            }

                            item(key = "popular_songs") {
                                AnimatedVisibility(visible = popularSongs.isNotEmpty()) {
                                    SpotifySection(title = stringResource(R.string.home_popular_songs)) {
                                        ListSongComponent(
                                            onClick = {
                                                contextMenuViewModel.showSong(it)
                                            },
                                            onPlay = { mediaPlayerViewModel.playSong(song = it) },
                                            songs = popularSongs.take(5)
                                        )
                                        Spacer(Modifier.height(16.dp))
                                        ListSongComponent(
                                            layoutStyle = LayoutStyleConfig.Vertical,
                                            onClick = {
                                                contextMenuViewModel.showSong(it)
                                            },
                                            onPlay = { mediaPlayerViewModel.playSong(song = it) },
                                            songs = popularSongs.drop(5).take(10)
                                        )
                                    }
                                }
                            }

                            item(key = "popular_albums") {
                                AnimatedVisibility(visible = popularAlbums.isNotEmpty()) {
                                    SpotifySection(title = stringResource(R.string.home_popular_albums)) {
                                        ListAlbumComponent(
                                            albums = popularAlbums.take(5),
                                            onClick = { navigateToAlbumDetail(it) }
                                        )
                                        Spacer(Modifier.height(16.dp))
                                        ListAlbumComponent(
                                            layoutStyle = LayoutStyleConfig.Vertical,
                                            albums = popularAlbums.drop(5).take(20),
                                            onClick = { navigateToAlbumDetail(it) }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    else -> {
                        HomeShimmer(modifier = Modifier.padding(top = topBarHeight + 16.dp))
                    }
                }
            }
        }

        val selectedCategoryColor: Color = MaterialTheme.colorScheme.primary
        val defaultCategoryColor: Color = MaterialTheme.colorScheme.surfaceVariant
        var selectedCategoryId by remember { mutableStateOf<Int?>(null) }

        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    topBarHeight = with(density) { coordinates.size.height.toDp() }
                }
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .hazeEffect(
                        state = hazeState,
                        style = HazeMaterials.ultraThin()
                    ) {
                        progressive = HazeProgressive.verticalGradient(
                            startIntensity = 1f,
                            endIntensity = 0f,
                            preferPerformance = true
                        )
                    }
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        MaterialTheme.colorScheme.background.copy(alpha = blurProgress * 0.6f)
                    )
            )

            LazyRow(
                modifier = Modifier.statusBarsPadding(),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    FilledTonalButton(
                        onClick = { selectedCategoryId = null },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedCategoryId == null) selectedCategoryColor
                            else defaultCategoryColor
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.home_all),
                            color = if (selectedCategoryId == null) MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp
                        )
                    }
                }
                items(items = categories.take(5), key = { it.id!! }) { category ->
                    FilledTonalButton(
                        onClick = {
                            selectedCategoryId = category.id
                            navigateToGenreDetail(category)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedCategoryId == category.id) selectedCategoryColor
                            else defaultCategoryColor
                        )
                    ) {
                        Text(
                            text = category.name.toString(),
                            color = if (selectedCategoryId == category.id) MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp,
                            lineHeight = 14.sp
                        )
                    }
                }
                item {
                    OutlinedButton(onClick = { selectedCategoryId = null; navigateToSearch() }) {
                        Text(
                            text = stringResource(R.string.home_see_more),
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun rememberIsOnline(): State<Boolean> {
    val context = LocalContext.current
    val connectivityManager = remember(context) {
        context.getSystemService(ConnectivityManager::class.java)
    }
    val isOnlineState = remember {
        mutableStateOf(connectivityManager?.let { SystemUtils.isOnline(it) } ?: true)
    }

    DisposableEffect(connectivityManager) {
        if (connectivityManager == null) {
            onDispose { }
        } else {
            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: android.net.Network) {
                    isOnlineState.value = true
                }

                override fun onLost(network: android.net.Network) {
                    isOnlineState.value = SystemUtils.isOnline(connectivityManager)
                }

                override fun onCapabilitiesChanged(
                    network: android.net.Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    isOnlineState.value = networkCapabilities.hasCapability(
                        NetworkCapabilities.NET_CAPABILITY_INTERNET
                    ) && networkCapabilities.hasCapability(
                        NetworkCapabilities.NET_CAPABILITY_VALIDATED
                    )
                }
            }
            val request = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()
            connectivityManager.registerNetworkCallback(request, callback)
            onDispose { connectivityManager.unregisterNetworkCallback(callback) }
        }
    }

    return isOnlineState
}