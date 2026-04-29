package com.example.vibramobile.presentation.navigation.graph

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.vibramobile.core.util.Navigator
import com.example.vibramobile.presentation.state.rememberNavigationState
import com.example.vibramobile.presentation.state.toEntries
import com.example.vibramobile.presentation.component.AppContextMenu
import com.example.vibramobile.presentation.component.MiniPlayerComponent
import com.example.vibramobile.presentation.component.AppNavigationBar
import com.example.vibramobile.presentation.component.TOP_LEVEL_DESTINATIONS
import com.example.vibramobile.presentation.navigation.destination.MainDestination
import com.example.vibramobile.presentation.component.AppFullscreenPlayer
import com.example.vibramobile.presentation.screen.AlbumDetailScreen
import com.example.vibramobile.presentation.screen.ArtistDetailScreen
import com.example.vibramobile.presentation.screen.GenreDetailScreen
import com.example.vibramobile.presentation.screen.HomeScreen
import com.example.vibramobile.presentation.screen.LibraryScreen
import com.example.vibramobile.presentation.screen.ProfileScreen
import com.example.vibramobile.presentation.screen.SearchResultScreen
import com.example.vibramobile.presentation.screen.SearchScreen
import androidx.compose.animation.AnimatedVisibility
import com.example.vibramobile.presentation.component.AppLyricsPlayer
import com.example.vibramobile.presentation.component.AppQueuePlayback
import com.example.vibramobile.presentation.viewmodel.MediaPlayerViewModel
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import org.koin.androidx.compose.koinViewModel

private const val PUSH_DURATION = 340
private const val POP_DURATION = 300

@Composable
fun MainGraph(
    isDarkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    accentColorHex: String,
    onAccentColorChange: (String) -> Unit
) {
    val navigationState = rememberNavigationState(
        startRoute = MainDestination.Home,
        topLevelRoutes = TOP_LEVEL_DESTINATIONS.keys
    )
    val navigator = remember { Navigator(navigationState) }
    val bottomContentPadding = 240.dp

    CompositionLocalProvider(LocalOverscrollFactory provides null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                NavDisplay(
                    onBack = navigator::goBack,
                    transitionSpec = {
                        (slideInHorizontally(
                            initialOffsetX = { it },
                            animationSpec = tween(PUSH_DURATION)
                        ) + fadeIn(animationSpec = tween(PUSH_DURATION / 2))) togetherWith
                                (slideOutHorizontally(
                                    targetOffsetX = { -(it / 4) },
                                    animationSpec = tween(PUSH_DURATION)
                                ) + fadeOut(animationSpec = tween(PUSH_DURATION / 2)))
                    },
                    popTransitionSpec = {
                        (slideInHorizontally(
                            initialOffsetX = { -(it / 4) },
                            animationSpec = tween(POP_DURATION)
                        ) + fadeIn(animationSpec = tween(POP_DURATION / 2))) togetherWith
                                (slideOutHorizontally(
                                    targetOffsetX = { it },
                                    animationSpec = tween(POP_DURATION)
                                ) + fadeOut(animationSpec = tween(POP_DURATION / 2)))
                    },
                    predictivePopTransitionSpec = {
                        (slideInHorizontally(
                            initialOffsetX = { -(it / 4) },
                            animationSpec = tween(POP_DURATION)
                        ) + fadeIn(animationSpec = tween(POP_DURATION / 2))) togetherWith
                                (slideOutHorizontally(
                                    targetOffsetX = { it },
                                    animationSpec = tween(POP_DURATION)
                                ) + fadeOut(animationSpec = tween(POP_DURATION / 2)))
                    },
                    entries = navigationState.toEntries(
                        entryProvider {
                            entry<MainDestination.Home> {
                                HomeScreen(
                                    bottomContentPadding = bottomContentPadding,
                                    navigateToGenreDetail = { category ->
                                        navigator.navigate(MainDestination.GenreDetail(category))
                                    },
                                    navigateToSearch = {
                                        navigator.navigate(MainDestination.Search)
                                    },
                                    navigateToArtistDetail = { artist ->
                                        navigator.navigate(MainDestination.ArtistDetail(artist))
                                    },
                                    navigateToAlbumDetail = { album ->
                                        navigator.navigate(MainDestination.AlbumDetail(album))
                                    }
                                )
                            }
                            entry<MainDestination.Search> {
                                SearchScreen(
                                    bottomContentPadding = bottomContentPadding,
                                    navigateToGenreDetail = { category ->
                                        navigator.navigate(MainDestination.GenreDetail(category))
                                    },
                                    navigateToSearchResult = {
                                        navigator.navigate(MainDestination.SearchResult)
                                    }
                                )
                            }
                            entry<MainDestination.SearchResult> {
                                SearchResultScreen(
                                    bottomContentPadding = bottomContentPadding,
                                    navigateBack = navigator::goBack,
                                    navigateToArtistDetail = { artist ->
                                        navigator.navigate(MainDestination.ArtistDetail(artist))
                                    }
                                )
                            }
                            entry<MainDestination.Library> {
                                LibraryScreen(
                                    bottomContentPadding = bottomContentPadding,
                                    navigateToArtistDetail = { artist ->
                                        navigator.navigate(MainDestination.ArtistDetail(artist))
                                    }
                                )
                            }
                            entry<MainDestination.Profile> {
                                ProfileScreen(
                                    bottomContentPadding = bottomContentPadding,
                                    isDarkMode = isDarkMode,
                                    onDarkModeChange = onDarkModeChange,
                                    selectedAccentColorHex = accentColorHex,
                                    onAccentColorChange = onAccentColorChange
                                )
                            }
                            entry<MainDestination.GenreDetail> { route ->
                                GenreDetailScreen(
                                    bottomContentPadding = bottomContentPadding,
                                    category = route.category,
                                    navigateBack = navigator::goBack,
                                    navigateToSearch = {
                                        navigator.navigate(MainDestination.Search)
                                    }
                                )
                            }
                            entry<MainDestination.ArtistDetail> { route ->
                                ArtistDetailScreen(
                                    bottomContentPadding = bottomContentPadding,
                                    artist = route.artist,
                                    navigateBack = navigator::goBack,
                                    navigateToAlbumDetail = { album ->
                                        navigator.navigate(MainDestination.AlbumDetail(album))
                                    }
                                )
                            }
                            entry<MainDestination.AlbumDetail> { route ->
                                AlbumDetailScreen(
                                    bottomContentPadding = bottomContentPadding,
                                    album = route.album,
                                    navigateBack = navigator::goBack
                                )
                            }
                        }
                    )
                )

                AppContextMenu(
                    navigateToArtist = { artist ->
                        navigator.navigate(MainDestination.ArtistDetail(artist))
                    }
                )

                AppFullscreenPlayer(
                    bottomContentPadding = bottomContentPadding,
                    navigateToArtistDetail = { artist ->
                        navigator.navigate(MainDestination.ArtistDetail(artist))
                    },
                )

                AppLyricsPlayer()

                AppQueuePlayback(
                    bottomContentPadding = bottomContentPadding,
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(alignment = Alignment.BottomCenter),
            ) {
                MiniPlayerComponent()

                AppNavigationBar(
                    selectedKey = navigationState.topLevelRoute,
                    onSelectKey = {
                        navigator.navigate(it)
                    }
                )
            }
        }
    }
}