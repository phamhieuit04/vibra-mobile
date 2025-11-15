package com.example.vibramobile

import android.app.Application
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {}

@Composable
fun AppScreen(modifier: Modifier = Modifier) {
    Scaffold() { paddingValues ->
        Text(text = "Hello world", modifier = Modifier.padding(paddingValues = paddingValues))
    }
}