package com.example.vibramobile.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composeunstyled.Text
import com.example.vibramobile.domain.model.Playlist
import com.example.vibramobile.domain.model.Song
import com.example.vibramobile.domain.model.User
import com.example.vibramobile.presentation.state.ArtistState
import com.example.vibramobile.presentation.state.SongState
import com.example.vibramobile.presentation.viewmodel.ArtistDetailViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ArtistDetailScreen(
    modifier: Modifier = Modifier,
    artist: User,
    artistDetailViewModel: ArtistDetailViewModel = koinViewModel()
) {
    val songs: List<Song> by SongState.songsByArtist.collectAsState()
    val albums: List<Playlist> by ArtistState.albumsByArtist.collectAsState()

    val isRefreshing by artistDetailViewModel.isRefreshing.collectAsState()
    val pullToRefreshState = rememberPullToRefreshState()

    LaunchedEffect(Unit) {
        artistDetailViewModel.refresh(artist.id!!)
    }

    PullToRefreshBox(
        modifier = modifier.fillMaxSize(),
        state = pullToRefreshState,
        isRefreshing = isRefreshing,
        onRefresh = { artistDetailViewModel.refresh(artist.id!!) },
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
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

        }
    }
}