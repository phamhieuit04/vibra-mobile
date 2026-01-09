package com.example.vibramobile.ui.navigations.destinations

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed class AuthDestination : NavKey {
    @Serializable
    object Welcome : AuthDestination()

    @Serializable
    object Login : AuthDestination()

    @Serializable
    object SignUp : AuthDestination()

    @Serializable
    object SignUpPassword : AuthDestination()
}