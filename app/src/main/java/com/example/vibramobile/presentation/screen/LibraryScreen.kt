package com.example.vibramobile.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vibramobile.presentation.component.ListAlbumRowComponent
import com.example.vibramobile.presentation.component.ListSongRowComponent
import com.example.vibramobile.presentation.component.SectionTitle
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
    mediaPlayerViewModel: MediaPlayerViewModel = koinViewModel()
) {
    val likedSongs by UserState.likedSongs.collectAsState()
    val myPlaylists by UserState.myPlaylists.collectAsState()

    val isRefreshing by libraryViewModel.isRefreshing.collectAsState()
    val pullToRefreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        modifier = modifier.fillMaxSize(),
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            if (likedSongs.isNotEmpty()) {
                item(key = "liked_songs") {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        SectionTitle(text = "Bài hát yêu thích")
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
                item(key = "playlists") {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        SectionTitle(text = "Playlist của tôi")
                        ListAlbumRowComponent(albums = myPlaylists, onClick = {
                            contextMenuViewModel.show(
                                it.thumbnailPath,
                                it.name,
                                it.author?.name
                            )
                        })
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
    }
}