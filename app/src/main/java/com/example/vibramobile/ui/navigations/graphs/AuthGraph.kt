package com.example.vibramobile.ui.navigations.graphs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness6
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.vibramobile.ui.extends.addSingleTop
import com.example.vibramobile.ui.extends.replace
import com.example.vibramobile.ui.navigations.destinations.AuthDestination
import com.example.vibramobile.ui.screens.LoginScreen
import com.example.vibramobile.ui.screens.SignUpPasswordScreen
import com.example.vibramobile.ui.screens.SignUpScreen
import com.example.vibramobile.ui.screens.WelcomeScreen

@Composable
fun AuthGraph(
    isDarkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    navigateToMain: () -> Unit
) {
    val backStack = rememberNavBackStack(AuthDestination.Welcome)

    Box {
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryProvider = entryProvider {
                entry<AuthDestination.Welcome> {
                    WelcomeScreen(
                        navigateToLogin = {
                            backStack.addSingleTop(AuthDestination.Login)
                        },
                        navigateToSignUp = {
                            backStack.addSingleTop(AuthDestination.SignUp)
                        }
                    )
                }
                entry<AuthDestination.Login> {
                    LoginScreen(
                        navigateBack = {
                            backStack.removeLastOrNull()
                        },
                        navigateToSignUp = {
                            backStack.replace(AuthDestination.SignUp)
                        },
                        navigateToMain = navigateToMain
                    )
                }
                entry<AuthDestination.SignUp> {
                    SignUpScreen(
                        navigateBack = {
                            backStack.removeLastOrNull()
                        },
                        navigateToLogin = {
                            backStack.replace(AuthDestination.Login)
                        },
                        navigateToSignUpPassword = {
                            backStack.add(AuthDestination.SignUpPassword)
                        }
                    )
                }
                entry<AuthDestination.SignUpPassword> {
                    SignUpPasswordScreen(
                        navigateBack = {
                            backStack.removeLastOrNull()
                        },
                        navigateToLogin = {
                            backStack.remove(AuthDestination.SignUp)
                            backStack.replace(AuthDestination.Login)
                        },
                    )
                }
            }
        )

        IconButton(
            onClick = { onDarkModeChange(!isDarkMode) },
            modifier = Modifier
                .statusBarsPadding()
                .align(Alignment.TopEnd)
                .padding(top = 8.dp, end = 12.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Brightness6,
                contentDescription = if (isDarkMode) "Switch to light mode" else "Switch to dark mode",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}