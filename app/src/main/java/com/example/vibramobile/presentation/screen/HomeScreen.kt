package com.example.vibramobile.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vibramobile.domain.model.Category
import com.example.vibramobile.domain.model.Playlist
import com.example.vibramobile.domain.model.User
import com.example.vibramobile.presentation.state.ArtistState
import com.example.vibramobile.presentation.state.CategoryState
import com.example.vibramobile.presentation.state.SongState
import com.example.vibramobile.presentation.state.UiState
import com.example.vibramobile.presentation.component.HomeShimmer
import com.example.vibramobile.presentation.component.ListAlbumComponent
import com.example.vibramobile.presentation.component.ListArtistComponent
import com.example.vibramobile.presentation.component.ListSongComponent
import com.example.vibramobile.presentation.component.ListSongRowComponent
import com.example.vibramobile.presentation.component.SectionTitle
import com.example.vibramobile.presentation.component.SpotifySection
import com.example.vibramobile.presentation.component.TopArtistsComponent
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

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = koinViewModel(),
    mediaPlayerViewModel: MediaPlayerViewModel = koinViewModel(),
    contextMenuViewModel: ContextMenuViewModel = koinViewModel(),
    navigateToGenreDetail: (Category) -> Unit,
    navigateToSearch: () -> Unit,
    navigateToArtistDetail: (User) -> Unit,
    navigateToAlbumDetail: (Playlist) -> Unit
) {
    LaunchedEffect(Unit) {
        UiState.setDisplayNavigationBar(true)
    }

    val isRefreshing by homeViewModel.isRefreshing.collectAsState()
    val pullToRefreshState = rememberPullToRefreshState()
    val scrollState = rememberLazyListState()
    val hazeState = rememberHazeState()

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
            onRefresh = { homeViewModel.fetchAll() },
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
                targetState = isRefreshing,
                label = "HomeContent"
            ) { loading ->
                if (!loading) {
                    LazyColumn(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        contentPadding = PaddingValues(top = topBarHeight),
                        state = scrollState,
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        item(key = "recent_rotation") {
                            AnimatedVisibility(
                                visible = recentRotationSongs.isNotEmpty()
                            ) {
                                SpotifySection(
                                    title = "Lắng nghe gần đây"
                                ) {
                                    ListSongRowComponent(
                                        onClick = {
                                            contextMenuViewModel.show(
                                                thumbnailPath = it.thumbnailPath,
                                                songTitle = it.name,
                                                artistName = it.author?.name
                                            )
                                        },
                                        onPlay = {
                                            mediaPlayerViewModel.playSong(song = it)
                                        },
                                        songs = recentRotationSongs
                                    )
                                }
                            }
                        }

                        item(key = "recommended") {
                            AnimatedVisibility(
                                visible = recommendedSongs.isNotEmpty()
                            ) {
                                SpotifySection(
                                    title = "Dành cho bạn"
                                ) {
                                    ListSongComponent(
                                        onClick = {
                                            contextMenuViewModel.show(
                                                thumbnailPath = it.thumbnailPath,
                                                songTitle = it.name,
                                                artistName = it.author?.name
                                            )
                                        },
                                        onPlay = { mediaPlayerViewModel.playSong(song = it) },
                                        songs = recommendedSongs
                                    )
                                }
                            }
                        }

                        item(key = "top_artists") {
                            AnimatedVisibility(
                                visible = popularArtists.isNotEmpty()
                            ) {
                                SpotifySection(
                                    title = "Nghệ sĩ nổi bật"
                                ) {
                                    TopArtistsComponent(
                                        artists = popularArtists.take(5),
                                        onClick = { navigateToArtistDetail(it) }
                                    )
                                    Spacer(Modifier.height(16.dp))
                                    ListArtistComponent(
                                        artists = popularArtists.drop(5),
                                        onClick = { navigateToArtistDetail(it) }
                                    )
                                }
                            }
                        }

                        item(key = "popular_songs") {
                            AnimatedVisibility(
                                visible = popularSongs.isNotEmpty()
                            ) {
                                SpotifySection(
                                    title = "Bài hát phổ biến"
                                ) {
                                    ListSongComponent(
                                        onClick = {
                                            contextMenuViewModel.show(
                                                thumbnailPath = it.thumbnailPath,
                                                songTitle = it.name,
                                                artistName = it.author?.name
                                            )
                                        },
                                        onPlay = { mediaPlayerViewModel.playSong(song = it) },
                                        songs = popularSongs.take(5)
                                    )
                                    Spacer(Modifier.height(16.dp))
                                    ListSongRowComponent(
                                        onClick = {
                                            contextMenuViewModel.show(
                                                thumbnailPath = it.thumbnailPath,
                                                songTitle = it.name,
                                                artistName = it.author?.name
                                            )
                                        },
                                        onPlay = { mediaPlayerViewModel.playSong(song = it) },
                                        songs = popularSongs.drop(5).take(10)
                                    )
                                }
                            }
                        }

                        item(key = "popular_albums") {
                            AnimatedVisibility(
                                visible = popularAlbums.isNotEmpty()
                            ) {
                                SpotifySection(
                                    title = "Album phổ biến"
                                ) {
                                    ListAlbumComponent(
                                        albums = popularAlbums,
                                        onClick = { navigateToAlbumDetail(it) })
                                }
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
                } else {
                    HomeShimmer(modifier = Modifier.padding(top = topBarHeight))
                }
            }
        }

        val selectedCategoryColor: Color = MaterialTheme.colorScheme.primary
        val defaultCategoryColor: Color = MaterialTheme.colorScheme.surfaceVariant
        var selectedCategoryId by remember { mutableStateOf<Int?>(null) }
        LazyRow(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .background(color = Color.Transparent)
                .onGloballyPositioned { coordinates ->
                    topBarHeight = with(density) {
                        coordinates.size.height.toDp()
                    }
                }
                .hazeEffect(
                    state = hazeState,
                    style = HazeMaterials.thin()
                ) {
                    blurEnabled = true
                    progressive =
                        HazeProgressive.verticalGradient(
                            startIntensity = 1f,
                            endIntensity = 0f,
                            preferPerformance = true
                        )
                    alpha = blurProgress
                },
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                FilledTonalButton(
                    onClick = { selectedCategoryId = null },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedCategoryId == null) {
                            selectedCategoryColor
                        } else {
                            defaultCategoryColor
                        }
                    )
                ) {
                    val allTextColor = if (selectedCategoryId == null) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }

                    Text(
                        text = "All",
                        color = allTextColor,
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
                        containerColor = if (selectedCategoryId == category.id) {
                            selectedCategoryColor
                        } else {
                            defaultCategoryColor
                        }
                    )
                ) {
                    val categoryTextColor = if (selectedCategoryId == category.id) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }

                    Text(
                        text = category.name.toString(),
                        color = categoryTextColor,
                        fontSize = 14.sp,
                        lineHeight = 14.sp
                    )
                }
            }
            item {
                OutlinedButton(
                    onClick = { selectedCategoryId = null; navigateToSearch() }
                ) {
                    Text(
                        text = "See more",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}