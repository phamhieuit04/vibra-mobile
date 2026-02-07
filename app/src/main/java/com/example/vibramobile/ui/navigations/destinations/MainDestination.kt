package com.example.vibramobile.ui.navigations.destinations

import androidx.navigation3.runtime.NavKey
import com.example.vibramobile.models.Category
import kotlinx.serialization.Serializable

@Serializable
sealed class MainDestination : NavKey {
    @Serializable
    object Home : MainDestination()

    @Serializable
    object Search : MainDestination()

    @Serializable
    object Library : MainDestination()

    @Serializable
    object Profile : MainDestination()

    @Serializable
    data class GenreDetail(val category: Category) : MainDestination()
}