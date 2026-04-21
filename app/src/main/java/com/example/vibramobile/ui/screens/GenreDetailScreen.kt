package com.example.vibramobile.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.vibramobile.R
import com.example.vibramobile.models.Category
import com.example.vibramobile.states.SongState
import com.example.vibramobile.states.UiState
import com.example.vibramobile.ui.components.GenreDetailShimmer
import com.example.vibramobile.ui.components.ListSongComponent
import com.example.vibramobile.ui.components.ListSongRowComponent
import com.example.vibramobile.ui.components.SectionTitle
import com.example.vibramobile.viewmodels.ContextMenuViewModel
import com.example.vibramobile.viewmodels.GenreDetailViewModel
import com.example.vibramobile.viewmodels.MediaPlayerViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun GenreDetailScreen(
    modifier: Modifier = Modifier,
    category: Category,
    genreDetailViewModel: GenreDetailViewModel = koinViewModel(),
    mediaPlayerViewModel: MediaPlayerViewModel = koinViewModel(),
    contextMenuViewModel: ContextMenuViewModel = koinViewModel(),
    navigateBack: () -> Unit,
    navigateToSearch: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val isRefreshing by genreDetailViewModel.isRefreshing.collectAsState()
    val pullToRefreshState = rememberPullToRefreshState()
    val scrollState = rememberLazyListState()
    val songsByCategory by SongState.songsByCategory.collectAsState()

    LaunchedEffect(Unit) {
        genreDetailViewModel.getSongsByCategory(category.id!!)
    }

    PullToRefreshBox(
        state = pullToRefreshState,
        isRefreshing = isRefreshing,
        onRefresh = {
            scope.launch { genreDetailViewModel.getSongsByCategory(category.id!!) }
        },
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
            label = "GenreDetailContent"
        ) { loading ->
            LazyColumn(
                state = scrollState,
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                item(key = "header") {
                    GenreHeader(category = category, onBackClick = navigateBack)
                }

                if (loading) {
                    item(key = "loading") {
                        Spacer(modifier.height(24.dp))

                        GenreDetailShimmer()
                    }
                } else {
                    if (songsByCategory.isEmpty()) {
                        item(key = "empty_state") {
                            EmptyStateView(onExploreClick = navigateToSearch)
                        }
                    } else {
                        item(key = "featured_songs") {
                            AnimatedVisibility(visible = songsByCategory.isNotEmpty()) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 16.dp, end = 16.dp, top = 24.dp)
                                ) {
                                    SectionTitle(text = "Danh sách nhạc nổi bật")
                                    ListSongRowComponent(
                                        onPlay = { mediaPlayerViewModel.playSong(it) },
                                        onClick = {
                                            contextMenuViewModel.show(
                                                thumbnailPath = it.thumbnail_path,
                                                songTitle = it.name,
                                                artistName = it.author?.name
                                            )
                                        },
                                        songs = songsByCategory.take(5)
                                    )
                                }
                            }
                        }
                        item(key = "list_song") {
                            AnimatedVisibility(visible = songsByCategory.size > 5) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 16.dp, end = 16.dp, top = 24.dp)
                                ) {
                                    SectionTitle(text = "Bài hát")
                                    ListSongRowComponent(
                                        onPlay = { mediaPlayerViewModel.playSong(it) },
                                        onClick = {
                                            contextMenuViewModel.show(
                                                thumbnailPath = it.thumbnail_path,
                                                songTitle = it.name,
                                                artistName = it.author?.name
                                            )
                                        },
                                        songs = songsByCategory.drop(5)
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

@Composable
private fun GenreHeader(
    modifier: Modifier = Modifier,
    category: Category,
    onBackClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(280.dp)
    ) {
        AsyncImage(
            model = category.thumbnail_path,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .blur(50.dp),
            contentScale = ContentScale.Crop,
            alpha = 0.3f,
            placeholder = painterResource(R.drawable.default_image),
            error = painterResource(R.drawable.default_image)
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.8f)
                        )
                    )
                )
        )


        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.TopStart),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
            )
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            AsyncImage(
                model = category.thumbnail_path,
                contentDescription = category.name,
                modifier = Modifier
                    .size(140.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shadow(8.dp, RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.default_image),
                error = painterResource(R.drawable.default_image)
            )

            Spacer(modifier = Modifier.width(20.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.Bottom)
            ) {
                Text(
                    text = "Thể loại",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = category.name ?: "",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 32.sp
                )

                if (!category.description.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = category.description ?: "",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 13.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyStateView(
    modifier: Modifier = Modifier,
    onExploreClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.MusicNote,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Chưa có bài hát",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Giai điệu cho từng khoảnh khắc",
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onExploreClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(24.dp),
            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Khám phá ngay",
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}