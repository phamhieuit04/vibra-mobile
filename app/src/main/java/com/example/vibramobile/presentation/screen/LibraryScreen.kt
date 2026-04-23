package com.example.vibramobile.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.vibramobile.domain.model.User
import com.example.vibramobile.presentation.component.ListAlbumRowComponent
import com.example.vibramobile.presentation.component.ListArtistComponent
import com.example.vibramobile.presentation.component.ListSongRowComponent
import com.example.vibramobile.presentation.component.SpotifySection
import com.example.vibramobile.presentation.state.UiState
import com.example.vibramobile.presentation.state.UserState
import com.example.vibramobile.presentation.viewmodel.ContextMenuViewModel
import com.example.vibramobile.presentation.viewmodel.LibraryViewModel
import com.example.vibramobile.presentation.viewmodel.MediaPlayerViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LibraryScreen(
    modifier: Modifier = Modifier,
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

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(headerGradient)
                    .padding(top = 28.dp, bottom = 32.dp)
                    .padding(horizontal = 16.dp)
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

        if (likedSongs.isNotEmpty()) {
            item {
                SpotifySection(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    title = "Bài hát yêu thích",
                ) {
                    ListSongRowComponent(
                        songs = likedSongs,
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
                    ListAlbumRowComponent(
                        albums = myPlaylists,
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
                SpotifySection(modifier = Modifier.padding(horizontal = 16.dp), title = "Nghệ sĩ") {
                    ListArtistComponent(
                        artists = followedArtists,
                        onClick = { navigateToArtistDetail(it) }
                    )
                }
            }
        }

        item {
            Spacer(
                modifier = Modifier.height(
                    if (UiState.getDisplayMediaPlayer()) 180.dp else 100.dp
                )
            )
        }
    }
}
