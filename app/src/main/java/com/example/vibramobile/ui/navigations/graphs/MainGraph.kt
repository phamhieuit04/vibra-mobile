package com.example.vibramobile.ui.navigations.graphs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.vibramobile.helpers.Navigator
import com.example.vibramobile.states.UiState
import com.example.vibramobile.states.rememberNavigationState
import com.example.vibramobile.states.toEntries
import com.example.vibramobile.ui.AppMediaPlayer
import com.example.vibramobile.ui.AppNavigationBar
import com.example.vibramobile.ui.TOP_LEVEL_DESTINATIONS
import com.example.vibramobile.ui.navigations.destinations.MainDestination
import com.example.vibramobile.ui.screens.FullscreenPlayer
import com.example.vibramobile.ui.screens.HomeScreen
import com.example.vibramobile.ui.screens.LibraryScreen
import com.example.vibramobile.ui.screens.SearchScreen

@Composable
fun MainGraph() {
    val navigationState = rememberNavigationState(
        startRoute = MainDestination.Home,
        topLevelRoutes = TOP_LEVEL_DESTINATIONS.keys
    )
    val navigator = remember { Navigator(navigationState) }

    Scaffold(
        containerColor = Color.Black,
        bottomBar = {
            AppNavigationBar(
                isVisible = UiState.getDisplayNavigationBar(),
                selectedKey = navigationState.topLevelRoute,
                onSelectKey = {
                    navigator.navigate(it)
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            NavDisplay(
                onBack = navigator::goBack,
                entries = navigationState.toEntries(
                    entryProvider {
                        entry<MainDestination.Home> {
                            HomeScreen()
                        }
                        entry<MainDestination.Search> {
                            SearchScreen()
                        }
                        entry<MainDestination.Library> {
                            LibraryScreen()
                        }
                    }
                )
            )

            FullscreenPlayer(
                isVisible = UiState.getDisplaySongDetail(),
                onVisibleChange = { value ->
                    UiState.setDisplaySongDetail(value)
                })

            AppMediaPlayer(
                modifier = Modifier.align(alignment = Alignment.BottomEnd),
                isVisible = UiState.getDisplayMediaPlayer()
            )
        }
    }
}