package com.example.vibramobile.presentation.navigation.graph

import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.vibramobile.core.util.Navigator
import com.example.vibramobile.presentation.state.UiState
import com.example.vibramobile.presentation.state.rememberNavigationState
import com.example.vibramobile.presentation.state.toEntries
import com.example.vibramobile.presentation.component.AppContextMenu
import com.example.vibramobile.presentation.component.AppMediaPlayer
import com.example.vibramobile.presentation.component.AppNavigationBar
import com.example.vibramobile.presentation.component.TOP_LEVEL_DESTINATIONS
import com.example.vibramobile.presentation.navigation.destination.MainDestination
import com.example.vibramobile.presentation.component.FullscreenPlayer
import com.example.vibramobile.presentation.screen.AlbumDetailScreen
import com.example.vibramobile.presentation.screen.ArtistDetailScreen
import com.example.vibramobile.presentation.screen.GenreDetailScreen
import com.example.vibramobile.presentation.screen.HomeScreen
import com.example.vibramobile.presentation.screen.LibraryScreen
import com.example.vibramobile.presentation.screen.ProfileScreen
import com.example.vibramobile.presentation.screen.SearchResultScreen
import com.example.vibramobile.presentation.screen.SearchScreen

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

    CompositionLocalProvider(LocalOverscrollFactory provides null) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                Column() {
                    AppMediaPlayer(
                        isVisible = UiState.getDisplayMediaPlayer()
                    )

                    AppNavigationBar(
                        isVisible = UiState.getDisplayNavigationBar(),
                        selectedKey = navigationState.topLevelRoute,
                        onSelectKey = {
                            navigator.navigate(it)
                        }
                    )
                }
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = padding.calculateTopPadding())
            ) {
                NavDisplay(
                    onBack = navigator::goBack,
                    entries = navigationState.toEntries(
                        entryProvider {
                            entry<MainDestination.Home> {
                                HomeScreen(
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
                                    navigateBack = navigator::goBack,
                                    navigateToArtistDetail = { artist ->
                                        navigator.navigate(MainDestination.ArtistDetail(artist))
                                    }
                                )
                            }
                            entry<MainDestination.Library> {
                                LibraryScreen(
                                    navigateToArtistDetail = { artist ->
                                        navigator.navigate(MainDestination.ArtistDetail(artist))
                                    }
                                )
                            }
                            entry<MainDestination.Profile> {
                                ProfileScreen(
                                    isDarkMode,
                                    onDarkModeChange,
                                    accentColorHex,
                                    onAccentColorChange
                                )
                            }
                            entry<MainDestination.GenreDetail> { route ->
                                GenreDetailScreen(
                                    category = route.category,
                                    navigateBack = navigator::goBack,
                                    navigateToSearch = {
                                        navigator.navigate(MainDestination.Search)
                                    }
                                )
                            }
                            entry<MainDestination.ArtistDetail> { route ->
                                ArtistDetailScreen(
                                    artist = route.artist,
                                    navigateBack = navigator::goBack,
                                    navigateToAlbumDetail = { album ->
                                        navigator.navigate(MainDestination.AlbumDetail(album))
                                    }
                                )
                            }
                            entry<MainDestination.AlbumDetail> { route ->
                                AlbumDetailScreen(
                                    album = route.album,
                                    navigateBack = navigator::goBack
                                )
                            }
                        }
                    )
                )

                FullscreenPlayer(
                    isVisible = UiState.getDisplaySongDetail(),
                    onVisibleChange = { value ->
                        UiState.setDisplaySongDetail(value)
                    },
                    onClickContextMenu = { UiState.setDisplayContextMenu(true) }
                )

                AppContextMenu()
            }
        }
    }
}