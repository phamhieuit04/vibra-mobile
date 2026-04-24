package com.example.vibramobile.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.vibramobile.R
import com.example.vibramobile.domain.model.Song
import com.example.vibramobile.domain.model.User
import com.example.vibramobile.presentation.state.UiState
import com.example.vibramobile.presentation.component.ListAlbumComponent
import com.example.vibramobile.presentation.component.ListArtistComponent
import com.example.vibramobile.presentation.component.ListSongComponent
import com.example.vibramobile.presentation.component.SearchResultShimmer
import com.example.vibramobile.presentation.component.SpotifySection
import com.example.vibramobile.presentation.component.TopArtistsComponent
import com.example.vibramobile.presentation.config.LayoutStyle
import com.example.vibramobile.presentation.viewmodel.ContextMenuViewModel
import com.example.vibramobile.presentation.viewmodel.MediaPlayerViewModel
import com.example.vibramobile.presentation.viewmodel.SearchViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultScreen(
    modifier: Modifier = Modifier,
    searchViewModel: SearchViewModel = koinViewModel(),
    mediaPlayerViewModel: MediaPlayerViewModel = koinViewModel(),
    contextMenuViewModel: ContextMenuViewModel = koinViewModel(),
    navigateBack: () -> Unit,
    navigateToArtistDetail: (User) -> Unit
) {
    val scope = rememberCoroutineScope()
    var query by remember { mutableStateOf("") }
    val searchBarState = rememberSearchBarState()

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val searchResult by searchViewModel.searchResult.collectAsState()
    val isLoading by searchViewModel.isLoading.collectAsState()

    val currentSong by mediaPlayerViewModel.currentSong.collectAsState()
    val mediaPlayerIsPlaying by mediaPlayerViewModel.isPlaying.collectAsState()

    val topSong = searchResult?.songs?.firstOrNull()
    val isPlaying by remember(currentSong, mediaPlayerIsPlaying, topSong) {
        derivedStateOf {
            topSong != null && currentSong == topSong && mediaPlayerIsPlaying
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SearchBar(
                    modifier = Modifier.weight(1f),
                    state = searchBarState,
                    inputField = {
                        SearchBarDefaults.InputField(
                            modifier = Modifier.focusRequester(focusRequester),
                            query = query,
                            onQueryChange = { query = it },
                            onSearch = {
                                focusManager.clearFocus()
                                scope.launch {
                                    searchViewModel.search(query)
                                }
                            },
                            expanded = false,
                            onExpandedChange = {},
                            placeholder = {
                                Text(
                                    "Nội dung...",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 16.sp
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Search,
                                    null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        )
                    }
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = navigateBack,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Icon(
                        Icons.Default.Close,
                        null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    ) { padding ->
        if (isLoading) {
            SearchResultShimmer(
                modifier = Modifier.padding(padding)
            )
        } else {
            Crossfade(
                targetState = searchResult,
                modifier = Modifier.padding(padding)
            ) { state ->
                when (state) {
                    null -> {
                        EmptySearchState()
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            if (searchResult!!.songs.isNotEmpty()) {
                                item(key = "top") {
                                    TopResultCard(
                                        isPlaying = isPlaying,
                                        song = searchResult!!.songs.first(),
                                        onClick = { mediaPlayerViewModel.playSong(topSong) }
                                    )
                                }
                            }

                            if (searchResult!!.songs.drop(1).isNotEmpty()) {
                                item(key = "songs") {
                                    SpotifySection(
                                        title = "Bài hát"
                                    ) {
                                        ListSongComponent(
                                            onClick = {
                                                contextMenuViewModel.show(
                                                    thumbnailPath = it.thumbnailPath,
                                                    songTitle = it.name,
                                                    artistName = it.author?.name
                                                )
                                            },
                                            onPlay = { mediaPlayerViewModel.playSong(it) },
                                            layoutStyle = LayoutStyle.Vertical,
                                            songs = searchResult!!.songs.drop(1).take(20)
                                        )
                                    }
                                }
                            }

                            if (searchResult!!.artists.isNotEmpty()) {
                                item(key = "artists") {
                                    SpotifySection(
                                        title = "Nghệ sĩ"
                                    ) {
                                        TopArtistsComponent(
                                            artists = searchResult!!.artists.take(5),
                                            onClick = { navigateToArtistDetail(it) }
                                        )
                                        Spacer(Modifier.height(16.dp))
                                        ListArtistComponent(
                                            artists = searchResult!!.artists.drop(5),
                                            onClick = { navigateToArtistDetail(it) }
                                        )
                                    }
                                }
                            }

                            if (searchResult!!.albums.isNotEmpty()) {
                                item(key = "albums") {
                                    SpotifySection(
                                        title = "Album"
                                    ) {
                                        ListAlbumComponent(
                                            albums = searchResult!!.albums,
                                            onClick = { }
                                        )
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
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptySearchState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                modifier = Modifier.size(50.dp),
                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Tìm kiếm bài hát, nghệ sĩ, album",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Khám phá âm nhạc yêu thích của bạn",
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            fontSize = 14.sp
        )
    }
}

@Composable
private fun TopResultCard(
    modifier: Modifier = Modifier,
    isPlaying: Boolean = false,
    song: Song,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(song.thumbnailPath)
                .crossfade(true)
                .build(),
            contentDescription = song.name,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.default_image),
            error = painterResource(R.drawable.default_image)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.25f),
                            Color.Black.copy(alpha = 0.78f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Top tìm kiếm",
                color = Color.White.copy(alpha = 0.92f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Column {
                Text(
                    text = song.name ?: "",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 28.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Bài hát",
                        color = Color.White.copy(alpha = 0.82f),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = " • ",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 13.sp
                    )
                    Text(
                        text = song.author?.name ?: "",
                        color = Color.White.copy(alpha = 0.82f),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .size(56.dp)
                .background(MaterialTheme.colorScheme.primary, CircleShape)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                tint = Color.Black,
                imageVector = if (isPlaying)
                    Icons.Default.Pause
                else
                    Icons.Default.PlayArrow,
                contentDescription = ""
            )
        }
    }
}