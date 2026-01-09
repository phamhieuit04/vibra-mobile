package com.example.vibramobile.ui.extends

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

fun <T : NavKey> NavBackStack<T>.addSingleTop(route: T) {
    if (lastOrNull() != route) {
        add(route)
    }
}

fun <T : NavKey> NavBackStack<T>.replace(route: T) {
    removeLastOrNull()
    add(route)
}

