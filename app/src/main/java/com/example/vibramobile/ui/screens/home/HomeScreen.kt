package com.example.vibramobile.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Queue
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vibramobile.R
import com.example.vibramobile.models.Category
import com.example.vibramobile.models.Playlist
import com.example.vibramobile.models.Song
import com.example.vibramobile.models.User
import com.example.vibramobile.states.UserState

val categories = listOf(
    Category(
        id = 0,
        name = "Classical",
        description = "Âm nhạc bác học với cấu trúc tinh tế, thường được trình diễn bởi dàn nhạc cổ điển.",
        thumbnail = "http://localhost:8000/assets/categories/thumbnails/classical.jpg"
    ),
    Category(
        id = 1,
        name = "Classical",
        description = "Âm nhạc bác học với cấu trúc tinh tế, thường được trình diễn bởi dàn nhạc cổ điển.",
        thumbnail = "http://localhost:8000/assets/categories/thumbnails/classical.jpg"
    ),
    Category(
        id = 2,
        name = "Classical",
        description = "Âm nhạc bác học với cấu trúc tinh tế, thường được trình diễn bởi dàn nhạc cổ điển.",
        thumbnail = "http://localhost:8000/assets/categories/thumbnails/classical.jpg"
    ),
    Category(
        id = 3,
        name = "Classical",
        description = "Âm nhạc bác học với cấu trúc tinh tế, thường được trình diễn bởi dàn nhạc cổ điển.",
        thumbnail = "http://localhost:8000/assets/categories/thumbnails/classical.jpg"
    ),
    Category(
        id = 4,
        name = "Classical",
        description = "Âm nhạc bác học với cấu trúc tinh tế, thường được trình diễn bởi dàn nhạc cổ điển.",
        thumbnail = "http://localhost:8000/assets/categories/thumbnails/classical.jpg"
    ),
    Category(
        id = 5,
        name = "Classical",
        description = "Âm nhạc bác học với cấu trúc tinh tế, thường được trình diễn bởi dàn nhạc cổ điển.",
        thumbnail = "http://localhost:8000/assets/categories/thumbnails/classical.jpg"
    )
)
val songs = listOf(
    Song(
        id = 0,
        name = "失われた大義",
        description = "Eilish sử dụng phong cách hát ngân nga. Trong lời bài hát, cô ấy ăn mừng sự chia tay với một người bạn đời cũ kiêu ngạo và thờ ơ, gọi họ là 'lost cause' trong phần điệp khúc .",
        lyric = "",
        thumbnail = "http://localhost:8000/uploads/kenshi/thumbnails/Lemon%20thumbnail.jpg",
        author = User(
            id = 0,
            name = "Kenshi Yonezu",
            description = "Kenshi Yonezu (米津玄師) là một nghệ sĩ đa tài người Nhật Bản: ca sĩ, nhạc sĩ, nhà sản xuất âm nhạc và họa sĩ minh họa...",
            email = "kenshi@gmail.com",
            avatar = "http://localhost:8000/uploads/kenshi/avatars/kenshi.jpg",
            followers = 0
        )
    ),
    Song(
        id = 1,
        name = "失われた大義",
        description = "Eilish sử dụng phong cách hát ngân nga. Trong lời bài hát, cô ấy ăn mừng sự chia tay với một người bạn đời cũ kiêu ngạo và thờ ơ, gọi họ là 'lost cause' trong phần điệp khúc .",
        lyric = "",
        thumbnail = "http://localhost:8000/uploads/kenshi/thumbnails/Lemon%20thumbnail.jpg",
        author = User(
            id = 0,
            name = "Kenshi Yonezu",
            description = "Kenshi Yonezu (米津玄師) là một nghệ sĩ đa tài người Nhật Bản: ca sĩ, nhạc sĩ, nhà sản xuất âm nhạc và họa sĩ minh họa...",
            email = "kenshi@gmail.com",
            avatar = "http://localhost:8000/uploads/kenshi/avatars/kenshi.jpg",
            followers = 0
        )
    ),
    Song(
        id = 2,
        name = "失われた大義",
        description = "Eilish sử dụng phong cách hát ngân nga. Trong lời bài hát, cô ấy ăn mừng sự chia tay với một người bạn đời cũ kiêu ngạo và thờ ơ, gọi họ là 'lost cause' trong phần điệp khúc .",
        lyric = "",
        thumbnail = "http://localhost:8000/uploads/kenshi/thumbnails/Lemon%20thumbnail.jpg",
        author = User(
            id = 0,
            name = "Kenshi Yonezu",
            description = "Kenshi Yonezu (米津玄師) là một nghệ sĩ đa tài người Nhật Bản: ca sĩ, nhạc sĩ, nhà sản xuất âm nhạc và họa sĩ minh họa...",
            email = "kenshi@gmail.com",
            avatar = "http://localhost:8000/uploads/kenshi/avatars/kenshi.jpg",
            followers = 0
        )
    ),
    Song(
        id = 3,
        name = "失われた大義",
        description = "Eilish sử dụng phong cách hát ngân nga. Trong lời bài hát, cô ấy ăn mừng sự chia tay với một người bạn đời cũ kiêu ngạo và thờ ơ, gọi họ là 'lost cause' trong phần điệp khúc .",
        lyric = "",
        thumbnail = "http://localhost:8000/uploads/kenshi/thumbnails/Lemon%20thumbnail.jpg",
        author = User(
            id = 0,
            name = "Kenshi Yonezu",
            description = "Kenshi Yonezu (米津玄師) là một nghệ sĩ đa tài người Nhật Bản: ca sĩ, nhạc sĩ, nhà sản xuất âm nhạc và họa sĩ minh họa...",
            email = "kenshi@gmail.com",
            avatar = "http://localhost:8000/uploads/kenshi/avatars/kenshi.jpg",
            followers = 0
        )
    ),
    Song(
        id = 4,
        name = "失われた大義",
        description = "Eilish sử dụng phong cách hát ngân nga. Trong lời bài hát, cô ấy ăn mừng sự chia tay với một người bạn đời cũ kiêu ngạo và thờ ơ, gọi họ là 'lost cause' trong phần điệp khúc .",
        lyric = "",
        thumbnail = "http://localhost:8000/uploads/kenshi/thumbnails/Lemon%20thumbnail.jpg",
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
fun HomeScreen(modifier: Modifier = Modifier) {
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
                itemsIndexed(categories) { index, category ->
                    FilledTonalButton(
                        onClick = {}, colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xff303030)
                        )
                    ) {
                        Text(text = category.name, color = Color.White, fontSize = 14.sp)
                    }
                }
            }
        })
    { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding())
                .padding(horizontal = 16.dp)
        ) {
            item {
                SectionTitle(text = "Lắng nghe gần đây")
                Spacer(Modifier.height(16.dp))
            }
            itemsIndexed(songs) { index, song ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row() {
                        Image(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(
                                    shape = RoundedCornerShape(
                                        4.dp
                                    )
                                ),
                            painter = painterResource(R.drawable.default_image),
                            contentDescription = "",
                            contentScale = ContentScale.Crop
                        )
                        Spacer(Modifier.width(12.dp))
                        Column() {
                            Text(text = song.name, color = Color.White, fontSize = 18.sp)
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = song.author.name.toString(),
                                color = Color.LightGray.copy(alpha = 0.8f),
                                fontSize = 12.sp
                            )
                        }
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            imageVector = Icons.Default.MoreHoriz,
                            contentDescription = "",
                            tint = Color.LightGray.copy(alpha = 0.8f)
                        )
                    }
                }
                Spacer(Modifier.height(12.dp))
            }

            item {
                Spacer(Modifier.height(10.dp))
                SectionTitle(text = "Phù hợp với bạn")
                Spacer(Modifier.height(16.dp))
            }
            item {
                SongSection(songs = songs)
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

            item {
                Spacer(Modifier.height(20.dp))
                SectionTitle(text = "Bài hát có nhiều lượt nghe")
                Spacer(Modifier.height(16.dp))
            }
            item {
                SongSection(songs = songs)
            }
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_3", backgroundColor = 0xff000000)
@Composable
fun Preview(modifier: Modifier = Modifier) {
    HomeScreen()
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
fun SongSection(modifier: Modifier = Modifier, songs: List<Song>) {
    LazyRow() {
        itemsIndexed(com.example.vibramobile.ui.screens.home.songs) { index, song ->
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(
                            shape = CircleShape
                        ),
                    painter = painterResource(R.drawable.default_image),
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column() {
                        Text(text = song.name, color = Color.White, fontSize = 16.sp)
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = song.author.name.toString(),
                            color = Color.LightGray.copy(alpha = 0.8f),
                            fontSize = 12.sp
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            imageVector = Icons.Default.Queue,
                            contentDescription = "",
                            tint = Color.LightGray.copy(alpha = 0.8f)
                        )
                    }
                }
            }
            Spacer(Modifier.width(16.dp))
        }
    }
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