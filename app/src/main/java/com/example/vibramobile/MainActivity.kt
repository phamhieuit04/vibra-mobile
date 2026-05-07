package com.example.vibramobile

import android.app.Application
import android.os.Bundle
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.example.vibramobile.presentation.navigation.graph.RootGraph
import com.example.vibramobile.presentation.theme.DEFAULT_ACCENT_COLOR_HEX
import com.example.vibramobile.presentation.theme.VibraMobileTheme
import androidx.core.content.edit
import com.example.vibramobile.core.di.jsonModule
import com.example.vibramobile.core.di.networkModule
import com.example.vibramobile.core.di.repositoryModule
import com.example.vibramobile.core.di.socketModule
import com.example.vibramobile.core.di.storeModule
import com.example.vibramobile.core.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                jsonModule,
                networkModule,
                repositoryModule,
                storeModule,
                viewModelModule,
                socketModule
            )
        }
    }
}

class MainActivity : ComponentActivity() {

    companion object {
        private const val APP_SETTINGS = "app_settings"
        private const val KEY_DARK_MODE = "dark_mode"
        private const val KEY_ACCENT_HEX = "accent_hex"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE)

        enableEdgeToEdge()

        setContent {
            var isDarkMode by remember {
                mutableStateOf(sharedPreferences.getBoolean(KEY_DARK_MODE, true))
            }

            var accentColorHex by remember {
                mutableStateOf(
                    sharedPreferences.getString(KEY_ACCENT_HEX, DEFAULT_ACCENT_COLOR_HEX)
                        ?: DEFAULT_ACCENT_COLOR_HEX
                )
            }

            LaunchedEffect(isDarkMode, accentColorHex) {
                sharedPreferences.edit {
                    putBoolean(KEY_DARK_MODE, isDarkMode)
                        .putString(KEY_ACCENT_HEX, accentColorHex)
                }

                val style = if (isDarkMode) {
                    SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
                } else {
                    SystemBarStyle.light(
                        android.graphics.Color.TRANSPARENT,
                        android.graphics.Color.TRANSPARENT
                    )
                }

                enableEdgeToEdge(
                    statusBarStyle = style,
                    navigationBarStyle = style
                )
            }

            VibraMobileTheme(
                darkTheme = isDarkMode,
                accentColorHex = accentColorHex
            ) {
                RootGraph(
                    isDarkMode,
                    { isDarkMode = it },
                    accentColorHex,
                    { accentColorHex = it }
                )
            }
        }
    }
}