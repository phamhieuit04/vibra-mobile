package com.example.vibramobile.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.vibramobile.R
import com.example.vibramobile.domain.model.Category
import com.example.vibramobile.presentation.component.DetailTopbarComponent
import com.example.vibramobile.presentation.state.SongState
import com.example.vibramobile.presentation.component.GenreDetailShimmer
import com.example.vibramobile.presentation.component.ListSongComponent
import com.example.vibramobile.presentation.component.SpotifySection
import com.example.vibramobile.presentation.component.TopSongsComponent
import com.example.vibramobile.presentation.config.LayoutStyleConfig
import com.example.vibramobile.presentation.viewmodel.ContextMenuViewModel
import com.example.vibramobile.presentation.viewmodel.GenreDetailViewModel
import com.example.vibramobile.presentation.viewmodel.MediaPlayerViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun GenreDetailScreen(
    modifier: Modifier = Modifier,
    bottomContentPadding: Dp = 0.dp,
    category: Category,
    genreDetailViewModel: GenreDetailViewModel = koinViewModel(),
    mediaPlayerViewModel: MediaPlayerViewModel = koinViewModel(),
    contextMenuViewModel: ContextMenuViewModel = koinViewModel(),
    navigateBack: () -> Unit,
    navigateToSearch: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val isRefreshing by genreDetailViewModel.isRefreshing.collectAsState()
    val songsByCategory by SongState.songsByCategory.collectAsState()
    var isInitialLoading by remember(category.id) { mutableStateOf(true) }
    val showLoading = isInitialLoading || isRefreshing

    val pullToRefreshState = rememberPullToRefreshState()
    val scrollState = rememberLazyListState()
    val scrollOffset by remember {
        derivedStateOf {
            scrollState.firstVisibleItemIndex * 1000f + scrollState.firstVisibleItemScrollOffset
        }
    }
    val topBarAlpha by animateFloatAsState(
        targetValue = (scrollOffset / 500f).coerceIn(0f, 1f),
        animationSpec = tween(0),
        label = "topBarAlpha"
    )

    LaunchedEffect(category.id) {
        isInitialLoading = true
        SongState.setSongsByCategory(emptyList())
        genreDetailViewModel.getSongsByCategory(category.id!!)
        isInitialLoading = false
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
                targetState = showLoading,
                label = "GenreDetailContent"
            ) { loading ->
                LazyColumn(
                    state = scrollState,
                    contentPadding = PaddingValues(bottom = bottomContentPadding),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    item(key = "header") {
                        GenreDetailHeader(category = category)
                    }

                    if (loading) {
                        item(key = "loading") {
                            GenreDetailShimmer()
                        }
                    } else {
                        if (songsByCategory.isEmpty()) {
                            item(key = "empty_state") {
                                EmptyStateView(onExploreClick = navigateToSearch)
                            }
                        } else {

                            item(key = "featured_songs") {
                                SpotifySection(
                                    title = "Danh sách nhạc nổi bật",
                                    modifier = Modifier.padding(
                                        start = 16.dp,
                                        end = 16.dp
                                    )
                                ) {
                                    TopSongsComponent(
                                        onPlay = { mediaPlayerViewModel.playSong(it) },
                                        onClick = {
                                            contextMenuViewModel.show(
                                                thumbnailPath = it.thumbnailPath,
                                                songTitle = it.name,
                                                artistName = it.author?.name
                                            )
                                        },
                                        songs = songsByCategory.take(5)
                                    )
                                }
                            }

                            if (songsByCategory.size > 5) {
                                item(key = "list_song") {
                                    SpotifySection(
                                        title = "Bài hát",
                                        modifier = Modifier.padding(
                                            start = 16.dp,
                                            end = 16.dp,
                                            top = 24.dp
                                        )
                                    ) {
                                        ListSongComponent(
                                            onPlay = { mediaPlayerViewModel.playSong(it) },
                                            layoutStyle = LayoutStyleConfig.Vertical,
                                            onClick = {
                                                contextMenuViewModel.show(
                                                    thumbnailPath = it.thumbnailPath,
                                                    songTitle = it.name,
                                                    artistName = it.author?.name
                                                )
                                            },
                                            songs = songsByCategory.drop(5)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        DetailTopbarComponent(
            title = category.name ?: "",
            topBarAlpha = topBarAlpha,
            onBackClick = navigateBack
        )
    }
}

@Composable
private fun GenreDetailHeader(
    modifier: Modifier = Modifier,
    category: Category
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        AsyncImage(
            model = category.thumbnailPath,
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

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            AsyncImage(
                model = category.thumbnailPath,
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