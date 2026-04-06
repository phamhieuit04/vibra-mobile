package com.example.vibramobile.ui.navigations.graphs

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.vibramobile.ui.extends.replace
import com.example.vibramobile.ui.navigations.destinations.RootDestination

@Composable
fun RootGraph(
    isDarkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    accentColorHex: String,
    onAccentColorChange: (String) -> Unit
) {
    val backStack = rememberNavBackStack(RootDestination.Auth)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<RootDestination.Auth> {
                AuthGraph(
                    isDarkMode,
                    onDarkModeChange,
                    navigateToMain = {
                        backStack.replace(RootDestination.Main)
                    }
                )
            }
            entry<RootDestination.Main> {
                MainGraph(
                    isDarkMode,
                    onDarkModeChange,
                    accentColorHex,
                    onAccentColorChange
                )
            }
        }
    )
}