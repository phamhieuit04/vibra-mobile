package com.example.vibramobile.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vibramobile.R
import com.example.vibramobile.models.Playlist
import com.example.vibramobile.models.User
import com.example.vibramobile.states.CategoryState
import com.example.vibramobile.states.SongState
import com.example.vibramobile.states.UiState
import com.example.vibramobile.ui.components.ListSongComponent
import com.example.vibramobile.ui.components.ListSongSkeleton
import com.example.vibramobile.ui.components.RecentRotationSongsComponent
import com.example.vibramobile.ui.components.RecentRotationSongsSkeleton
import com.example.vibramobile.ui.components.SkeletonComponent
import com.example.vibramobile.viewmodels.HomeViewModel
import com.example.vibramobile.viewmodels.MediaPlayerViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

val albums = listOf(
    Playlist(
        id = 0,
        name = "Stray Sheep",
        description = "Được phát hành vào ngày 5 tháng 8 năm 2020. Tên của album được lấy cảm hứng từ New Testament.",
        thumbnail = "http://localhost:8000/uploads/kenshi/thumbnails/Stray%20Sheep%20thumbnail.jpg",
        type = 1,
        total_song = 2,
        price = 100000,
        author = User(
            id = 0,
            name = "Kenshi Yonezu",
            description = "Kenshi Yonezu (米津玄師) là một nghệ sĩ đa tài người Nhật Bản: ca sĩ, nhạc sĩ, nhà sản xuất âm nhạc và họa sĩ minh họa...",
            email = "kenshi@gmail.com",
            avatar = "http://localhost:8000/uploads/kenshi/avatars/kenshi.jpg",
            followers = 0
        )
    ),
    Playlist(
        id = 1,
        name = "Stray Sheep",
        description = "Được phát hành vào ngày 5 tháng 8 năm 2020. Tên của album được lấy cảm hứng từ New Testament.",
        thumbnail = "http://localhost:8000/uploads/kenshi/thumbnails/Stray%20Sheep%20thumbnail.jpg",
        type = 1,
        total_song = 2,
        price = 100000,
        author = User(
            id = 0,
            name = "Kenshi Yonezu",
            description = "Kenshi Yonezu (米津玄師) là một nghệ sĩ đa tài người Nhật Bản: ca sĩ, nhạc sĩ, nhà sản xuất âm nhạc và họa sĩ minh họa...",
            email = "kenshi@gmail.com",
            avatar = "http://localhost:8000/uploads/kenshi/avatars/kenshi.jpg",
            followers = 0
        )
    ),
    Playlist(
        id = 2,
        name = "Stray Sheep",
        description = "Được phát hành vào ngày 5 tháng 8 năm 2020. Tên của album được lấy cảm hứng từ New Testament.",
        thumbnail = "http://localhost:8000/uploads/kenshi/thumbnails/Stray%20Sheep%20thumbnail.jpg",
        type = 1,
        total_song = 2,
        price = 100000,
        author = User(
            id = 0,
            name = "Kenshi Yonezu",
            description = "Kenshi Yonezu (米津玄師) là một nghệ sĩ đa tài người Nhật Bản: ca sĩ, nhạc sĩ, nhà sản xuất âm nhạc và họa sĩ minh họa...",
            email = "kenshi@gmail.com",
            avatar = "http://localhost:8000/uploads/kenshi/avatars/kenshi.jpg",
            followers = 0
        )
    ),
    Playlist(
        id = 3,
        name = "Stray Sheep",
        description = "Được phát hành vào ngày 5 tháng 8 năm 2020. Tên của album được lấy cảm hứng từ New Testament.",
        thumbnail = "http://localhost:8000/uploads/kenshi/thumbnails/Stray%20Sheep%20thumbnail.jpg",
        type = 1,
        total_song = 2,
        price = 100000,
        author = User(
            id = 0,
            name = "Kenshi Yonezu",
            description = "Kenshi Yonezu (米津玄師) là một nghệ sĩ đa tài người Nhật Bản: ca sĩ, nhạc sĩ, nhà sản xuất âm nhạc và họa sĩ minh họa...",
            email = "kenshi@gmail.com",
            avatar = "http://localhost:8000/uploads/kenshi/avatars/kenshi.jpg",
            followers = 0
        )
    ),
    Playlist(
        id = 4,
        name = "Stray Sheep",
        description = "Được phát hành vào ngày 5 tháng 8 năm 2020. Tên của album được lấy cảm hứng từ New Testament.",
        thumbnail = "http://localhost:8000/uploads/kenshi/thumbnails/Stray%20Sheep%20thumbnail.jpg",
        type = 1,
        total_song = 2,
        price = 100000,
        author = User(
            id = 0,
            name = "Kenshi Yonezu",
            description = "Kenshi Yonezu (米津玄師) là một nghệ sĩ đa tài người Nhật Bản: ca sĩ, nhạc sĩ, nhà sản xuất âm nhạc và họa sĩ minh họa...",
            email = "kenshi@gmail.com",
            avatar = "http://localhost:8000/uploads/kenshi/avatars/kenshi.jpg",
            followers = 0
        )
    ),
)
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
    var refreshing by remember { mutableStateOf(false) }

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
            isRefreshing = refreshing,
            onRefresh = {
                scope.launch {
                    refreshing = true
                    viewModel.fetchAll()
                    refreshing = false
                }
            }
        ) {
            LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
                item {
                    SectionTitle(text = "Lắng nghe gần đây")
                    Spacer(Modifier.height(16.dp))

                    SkeletonComponent(
                        isLoading = SongState.recentRotationSongs.isEmpty(),
                        skeletonContent = { RecentRotationSongsSkeleton() }
                    ) {
                        RecentRotationSongsComponent(
                            onPlay = { mediaPlayerViewModel.playSong(song = it) },
                            songs = SongState.recentRotationSongs
                        )
                    }
                }

                item {
                    Spacer(Modifier.height(10.dp))
                    SectionTitle(text = "Phù hợp với bạn")
                    Spacer(Modifier.height(16.dp))

                    SkeletonComponent(
                        isLoading = SongState.recommendedSongs.isEmpty(),
                        skeletonContent = { ListSongSkeleton() }) {
                        ListSongComponent(
                            onPlay = { mediaPlayerViewModel.playSong(song = it) },
                            songs = SongState.recommendedSongs
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                }

                item {
                    Spacer(Modifier.height(20.dp))
                    SectionTitle(text = "Album phổ biến")
                    Spacer(Modifier.height(16.dp))
                }
                item {
                    AlbumSection(albums = albums)
                }

                item {
                    Spacer(Modifier.height(20.dp))
                    SectionTitle(text = "Nghệ sĩ nổi bật")
                    Spacer(Modifier.height(16.dp))
                }
                item {
                    ArtistSection(artists = artists)
                }

//            item {
//                Spacer(Modifier.height(20.dp))
//                SectionTitle(text = "Bài hát có nhiều lượt nghe")
//                Spacer(Modifier.height(16.dp))
//            }
//            item {
//                SongSection(songs = songs)
//            }

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

@Composable
fun AlbumSection(modifier: Modifier = Modifier, albums: List<Playlist>) {
    LazyRow() {
        itemsIndexed(albums) { index, album ->
            Column() {
                Image(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(
                            shape = RoundedCornerShape(6.dp)
                        ),
                    painter = painterResource(R.drawable.default_image),
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.height(8.dp))
                Text(text = album.name, color = Color.White, fontSize = 16.sp)
                Spacer(Modifier.height(2.dp))
                Text(
                    text = album.author.name.toString(),
                    color = Color.LightGray.copy(alpha = 0.8f),
                    fontSize = 12.sp
                )
            }
            Spacer(Modifier.width(16.dp))
        }
    }
}

@Composable
fun ArtistSection(modifier: Modifier = Modifier, artists: List<User>) {
    LazyRow() {
        itemsIndexed(artists) { index, artist ->
            Column() {
                Image(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(
                            shape = RoundedCornerShape(6.dp)
                        ),
                    painter = painterResource(R.drawable.default_image),
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.height(8.dp))
                Text(text = artist.name.toString(), color = Color.White, fontSize = 16.sp)
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "${artist.followers} người theo dõi",
                    color = Color.LightGray.copy(alpha = 0.8f),
                    fontSize = 12.sp
                )
            }
            Spacer(Modifier.width(16.dp))
        }
    }
}