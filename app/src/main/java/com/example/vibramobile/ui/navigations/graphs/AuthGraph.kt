package com.example.vibramobile.ui.navigations.graphs

import androidx.compose.runtime.Composable
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
fun AuthGraph(navigateToMain: () -> Unit) {
    val backStack = rememberNavBackStack(AuthDestination.Welcome)

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
}