package com.example.vibramobile.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vibramobile.R
import com.example.vibramobile.models.User
import com.example.vibramobile.states.ArtistState
import com.example.vibramobile.states.CategoryState
import com.example.vibramobile.states.SongState
import com.example.vibramobile.states.UiState
import com.example.vibramobile.ui.components.ListSongComponent
import com.example.vibramobile.ui.components.ListSongSkeleton
import com.example.vibramobile.ui.components.ListAlbumComponent
import com.example.vibramobile.ui.components.ListAlbumSkeleton
import com.example.vibramobile.ui.components.ListArtistComponent
import com.example.vibramobile.ui.components.ListArtistSkeleton
import com.example.vibramobile.ui.components.RecentRotationSongsComponent
import com.example.vibramobile.ui.components.RecentRotationSongsSkeleton
import com.example.vibramobile.ui.components.SkeletonComponent
import com.example.vibramobile.viewmodels.HomeViewModel
import com.example.vibramobile.viewmodels.MediaPlayerViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

val artists = listOf(
    User(
        id = 0,
        name = "Kenshi Yonezu",
        description = "Kenshi Yonezu (米津玄師) là một nghệ sĩ đa tài người Nhật Bản: ca sĩ, nhạc sĩ, nhà sản xuất âm nhạc và họa sĩ minh họa...",
        email = "kenshi@gmail.com",
        avatar = "http://localhost:8000/uploads/kenshi/avatars/kenshi.jpg",
        followers = 0
    ),
    User(
        id = 1,
        name = "Kenshi Yonezu",
        description = "Kenshi Yonezu (米津玄師) là một nghệ sĩ đa tài người Nhật Bản: ca sĩ, nhạc sĩ, nhà sản xuất âm nhạc và họa sĩ minh họa...",
        email = "kenshi@gmail.com",
        avatar = "http://localhost:8000/uploads/kenshi/avatars/kenshi.jpg",
        followers = 0
    ),
    User(
        id = 2,
        name = "Kenshi Yonezu",
        description = "Kenshi Yonezu (米津玄師) là một nghệ sĩ đa tài người Nhật Bản: ca sĩ, nhạc sĩ, nhà sản xuất âm nhạc và họa sĩ minh họa...",
        email = "kenshi@gmail.com",
        avatar = "http://localhost:8000/uploads/kenshi/avatars/kenshi.jpg",
        followers = 0
    ),
    User(
        id = 3,
        name = "Kenshi Yonezu",
        description = "Kenshi Yonezu (米津玄師) là một nghệ sĩ đa tài người Nhật Bản: ca sĩ, nhạc sĩ, nhà sản xuất âm nhạc và họa sĩ minh họa...",
        email = "kenshi@gmail.com",
        avatar = "http://localhost:8000/uploads/kenshi/avatars/kenshi.jpg",
        followers = 0
    ),
    User(
        id = 4,
        name = "Kenshi Yonezu",
        description = "Kenshi Yonezu (米津玄師) là một nghệ sĩ đa tài người Nhật Bản: ca sĩ, nhạc sĩ, nhà sản xuất âm nhạc và họa sĩ minh họa...",
        email = "kenshi@gmail.com",
        avatar = "http://localhost:8000/uploads/kenshi/avatars/kenshi.jpg",
        followers = 0
    ),
)

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel(),
    mediaPlayerViewModel: MediaPlayerViewModel = koinViewModel()
) {
    LaunchedEffect(Unit) {
        UiState.setDisplayNavigationBar(true)
    }

    val scope = rememberCoroutineScope()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

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
            isRefreshing = isRefreshing,
            onRefresh = {
                scope.launch {
                    viewModel.fetchAll()
                }
            }
        ) {
            LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
                item {
                    SkeletonComponent(
                        isLoading = SongState.isRecentRotationLoading,
                        skeletonContent = { RecentRotationSongsSkeleton() }
                    ) {
                        if (SongState.recentRotationSongs.isEmpty()) return@SkeletonComponent
                        Column(modifier = Modifier.fillMaxWidth()) {
                            SectionTitle(text = "Lắng nghe gần đây")

                            Spacer(Modifier.height(16.dp))

                            RecentRotationSongsComponent(
                                onPlay = { mediaPlayerViewModel.playSong(song = it) },
                                songs = SongState.recentRotationSongs
                            )
                        }
                    }
                }

                item {
                    Spacer(Modifier.height(10.dp))

                    SkeletonComponent(
                        isLoading = SongState.isRecommendedSongsLoading,
                        skeletonContent = { ListSongSkeleton() }
                    ) {
                        if (SongState.recommendedSongs.isEmpty()) return@SkeletonComponent
                        Column(modifier = Modifier.fillMaxWidth()) {
                            SectionTitle(text = "Phù hợp với bạn")

                            Spacer(Modifier.height(16.dp))

                            ListSongComponent(
                                onPlay = { mediaPlayerViewModel.playSong(song = it) },
                                songs = SongState.recommendedSongs
                            )
                        }
                        Spacer(Modifier.height(16.dp))
                    }
                }

                item {
                    Spacer(Modifier.height(10.dp))

                    SkeletonComponent(
                        isLoading = ArtistState.isPopularArtistsLoading,
                        skeletonContent = { ListArtistSkeleton() }
                    ) {
                        if (ArtistState.popularArtists.isEmpty()) return@SkeletonComponent
                        Column(modifier = Modifier.fillMaxWidth()) {
                            SectionTitle(text = "Nghệ sĩ nổi bật")

                            Spacer(Modifier.height(16.dp))

                            ListArtistComponent(artists = ArtistState.popularArtists)
                        }
                        Spacer(Modifier.height(16.dp))
                    }
                }

                item {
                    Spacer(Modifier.height(10.dp))

                    SkeletonComponent(
                        isLoading = SongState.isPopularSongsLoading,
                        skeletonContent = { ListSongSkeleton() }
                    ) {
                        if (SongState.popularSongs.isEmpty()) return@SkeletonComponent
                        Column(modifier = Modifier.fillMaxWidth()) {
                            SectionTitle(text = "Bài hát có nhiều lượt nghe")

                            Spacer(Modifier.height(16.dp))

                            ListSongComponent(
                                onPlay = { mediaPlayerViewModel.playSong(song = it) },
                                songs = SongState.popularSongs
                            )
                        }
                        Spacer(Modifier.height(16.dp))
                    }
                }

                item { Spacer(Modifier.height(96.dp)) }
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