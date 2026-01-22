package com.example.vibramobile.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vibramobile.models.User
import com.example.vibramobile.states.ArtistState
import com.example.vibramobile.states.CategoryState
import com.example.vibramobile.states.SongState
import com.example.vibramobile.states.UiState
import com.example.vibramobile.ui.components.HomeSkeleton
import com.example.vibramobile.ui.components.ListAlbumComponent
import com.example.vibramobile.ui.components.ListAlbumSkeleton
import com.example.vibramobile.ui.components.ListArtistComponent
import com.example.vibramobile.ui.components.ListArtistSkeleton
import com.example.vibramobile.ui.components.ListSongComponent
import com.example.vibramobile.ui.components.ListSongRowComponent
import com.example.vibramobile.ui.components.ListSongRowSkeleton
import com.example.vibramobile.ui.components.ListSongSkeleton
import com.example.vibramobile.ui.components.SkeletonComponent
import com.example.vibramobile.ui.components.TopArtistsComponent
import com.example.vibramobile.viewmodels.HomeViewModel
import com.example.vibramobile.viewmodels.MediaPlayerViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel(),
    mediaPlayerViewModel: MediaPlayerViewModel = koinViewModel()
) {
    LaunchedEffect(Unit) {
        UiState.setDisplayNavigationBar(true)
    }

    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val pullToRefreshState = rememberPullToRefreshState()
    val scrollState = rememberLazyListState()

    Scaffold(
        containerColor = Color.Black,
        topBar = {
            LazyRow(
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    FilledTonalButton(
                        onClick = {}, colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xffbc4d15)
                        )
                    ) {
                        Text(text = "All", color = Color.White, fontSize = 14.sp)
                    }
                }
                itemsIndexed(CategoryState.categories) { index, category ->
                    FilledTonalButton(
                        onClick = { }, colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xff303030)
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
        })
    { paddingValues ->
        PullToRefreshBox(
            modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
            state = pullToRefreshState,
            isRefreshing = isRefreshing,
            onRefresh = { viewModel.fetchAll() },
            indicator = {
                PullToRefreshDefaults.Indicator(
                    state = pullToRefreshState,
                    isRefreshing = isRefreshing,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
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
                        state = scrollState,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item(key = "recent_rotation") {
                            AnimatedVisibility(
                                visible = SongState.recentRotationSongs.isNotEmpty()
                            ) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    SectionTitle(text = "Lắng nghe gần đây")
                                    Spacer(Modifier.height(16.dp))
                                    ListSongRowComponent(
                                        onPlay = { mediaPlayerViewModel.playSong(song = it) },
                                        songs = SongState.recentRotationSongs
                                    )
                                }
                            }
                        }

                        item(key = "recommended") {
                            AnimatedVisibility(
                                visible = SongState.recommendedSongs.isNotEmpty()
                            ) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    SectionTitle(text = "Phù hợp với bạn")
                                    Spacer(Modifier.height(16.dp))
                                    ListSongComponent(
                                        onPlay = { mediaPlayerViewModel.playSong(song = it) },
                                        songs = SongState.recommendedSongs
                                    )
                                }
                            }
                        }

                        item(key = "top_artists") {
                            AnimatedVisibility(
                                visible = ArtistState.popularArtists.isNotEmpty()
                            ) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    SectionTitle(text = "Nghệ sĩ nổi bật")
                                    Spacer(Modifier.height(16.dp))
                                    TopArtistsComponent(
                                        artists = ArtistState.popularArtists.take(5),
                                        onClick = { }
                                    )
                                    Spacer(Modifier.height(16.dp))
                                    ListArtistComponent(
                                        artists = ArtistState.popularArtists.drop(5),
                                        onClick = { }
                                    )
                                }
                            }
                        }

                        item(key = "popular_songs") {
                            AnimatedVisibility(
                                visible = SongState.popularSongs.isNotEmpty()
                            ) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    SectionTitle(text = "Bài hát có nhiều lượt nghe")
                                    Spacer(Modifier.height(16.dp))
                                    ListSongComponent(
                                        onPlay = { mediaPlayerViewModel.playSong(song = it) },
                                        songs = SongState.popularSongs.take(5)
                                    )
                                    Spacer(Modifier.height(16.dp))
                                    ListSongRowComponent(
                                        onPlay = { mediaPlayerViewModel.playSong(song = it) },
                                        songs = SongState.popularSongs.drop(5).take(10)
                                    )
                                }
                            }
                        }

                        item(key = "popular_albums") {
                            AnimatedVisibility(
                                visible = SongState.popularAlbums.isNotEmpty()
                            ) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    SectionTitle(text = "Album phổ biến")
                                    Spacer(Modifier.height(16.dp))
                                    ListAlbumComponent(albums = SongState.popularAlbums)
                                }
                            }
                        }

                        item(key = "bottom_spacer") {
                            if (UiState.getDisplayMediaPlayer()) {
                                Spacer(Modifier.height(96.dp))
                            }
                        }
                    }
                } else {
                    HomeSkeleton()
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