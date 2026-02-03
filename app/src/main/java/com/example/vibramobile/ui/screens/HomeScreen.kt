package com.example.vibramobile.ui.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vibramobile.models.Category
import com.example.vibramobile.states.ArtistState
import com.example.vibramobile.states.CategoryState
import com.example.vibramobile.states.SongState
import com.example.vibramobile.states.UiState
import com.example.vibramobile.ui.components.HomeShimmer
import com.example.vibramobile.ui.components.ListAlbumComponent
import com.example.vibramobile.ui.components.ListArtistComponent
import com.example.vibramobile.ui.components.ListSongComponent
import com.example.vibramobile.ui.components.ListSongRowComponent
import com.example.vibramobile.ui.components.TopArtistsComponent
import com.example.vibramobile.viewmodels.ContextMenuViewModel
import com.example.vibramobile.viewmodels.HomeViewModel
import com.example.vibramobile.viewmodels.MediaPlayerViewModel
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
    navigateToGenreDetail: (Category) -> Unit
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
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item(key = "recent_rotation") {
                            AnimatedVisibility(
                                visible = recentRotationSongs.isNotEmpty()
                            ) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    SectionTitle(text = "Lắng nghe gần đây")
                                    Spacer(Modifier.height(16.dp))
                                    ListSongRowComponent(
                                        onClick = {
                                            contextMenuViewModel.show(
                                                thumbnailPath = it.thumbnail_path,
                                                songTitle = it.name,
                                                artistName = it.author?.name
                                            )
                                        },
                                        onPlay = {
                                            mediaPlayerViewModel.playSong(song = it);
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
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    SectionTitle(text = "Phù hợp với bạn")
                                    Spacer(Modifier.height(16.dp))
                                    ListSongComponent(
                                        onClick = {
                                            contextMenuViewModel.show(
                                                thumbnailPath = it.thumbnail_path,
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
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    SectionTitle(text = "Nghệ sĩ nổi bật")
                                    Spacer(Modifier.height(16.dp))
                                    TopArtistsComponent(
                                        artists = popularArtists.take(5),
                                        onClick = { }
                                    )
                                    Spacer(Modifier.height(16.dp))
                                    ListArtistComponent(
                                        artists = popularArtists.drop(5),
                                        onClick = { }
                                    )
                                }
                            }
                        }

                        item(key = "popular_songs") {
                            AnimatedVisibility(
                                visible = popularSongs.isNotEmpty()
                            ) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    SectionTitle(text = "Bài hát có nhiều lượt nghe")
                                    Spacer(Modifier.height(16.dp))
                                    ListSongComponent(
                                        onClick = {
                                            contextMenuViewModel.show(
                                                thumbnailPath = it.thumbnail_path,
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
                                                thumbnailPath = it.thumbnail_path,
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
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    SectionTitle(text = "Album phổ biến")
                                    Spacer(Modifier.height(16.dp))
                                    ListAlbumComponent(albums = popularAlbums)
                                }
                            }
                        }

                        item(key = "bottom_spacer") {
                            Spacer(Modifier.height(96.dp))

                            if (UiState.getDisplayMediaPlayer()) {
                                Spacer(Modifier.height(96.dp))
                            }
                        }
                    }
                } else {
                    HomeShimmer(modifier = Modifier.padding(top = topBarHeight))
                }
            }
        }

        val selectedCategoryColor: Color = Color(0xffbc4d15)
        val defaultCategoryColor: Color = Color(0xff303030)
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
                    Text(text = "All", color = Color.White, fontSize = 14.sp)
                }
            }
            items(items = categories, key = { it.id!! }) { category ->
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
                    Text(
                        text = category.name.toString(),
                        color = Color.White,
                        fontSize = 14.sp,
                        lineHeight = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun SectionTitle(modifier: Modifier = Modifier, text: String) {
    Text(
        text = text,
        color = Color.White,
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp
    )
}