package com.example.vibramobile

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.vibramobile.ui.AppNavigation
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {}

@Composable
fun AppScreen(modifier: Modifier = Modifier) {
    Scaffold() { padding ->
        AppNavigation(modifier = Modifier.padding(padding))
    }
}