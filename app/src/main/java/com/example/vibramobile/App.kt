package com.example.vibramobile

import android.app.Application
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.vibramobile.injects.networkModule
import com.example.vibramobile.injects.viewModelModule
import com.example.vibramobile.states.UiState
import com.example.vibramobile.ui.AppMediaPlayer
import com.example.vibramobile.ui.AppNavigationBar
import com.example.vibramobile.ui.navigations.graphs.RootGraph
import com.example.vibramobile.ui.screens.FullscreenPlayer
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                networkModule,
                viewModelModule
            )
        }
    }
}