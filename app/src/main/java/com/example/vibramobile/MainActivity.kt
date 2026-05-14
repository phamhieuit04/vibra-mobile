package com.example.vibramobile

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.example.vibramobile.presentation.navigation.graph.RootGraph
import com.example.vibramobile.presentation.theme.DEFAULT_ACCENT_COLOR_HEX
import com.example.vibramobile.presentation.theme.VibraMobileTheme
import com.example.vibramobile.core.di.jsonModule
import com.example.vibramobile.core.di.networkModule
import com.example.vibramobile.core.di.repositoryModule
import com.example.vibramobile.core.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.vibramobile.presentation.viewmodel.SplashViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.appcompat.app.AppCompatDelegate
import com.example.vibramobile.core.di.databaseModule
import com.example.vibramobile.presentation.viewmodel.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidContext(this@App)
            modules(
                jsonModule,
                networkModule,
                repositoryModule,
                viewModelModule,
                databaseModule
            )
        }
    }
}

class MainActivity : ComponentActivity() {

    private val splashViewModel: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition {
            splashViewModel.uiState.value.isLoading
        }

        enableEdgeToEdge()

        setContent {
            val startupState by splashViewModel.uiState.collectAsState()
            val settingsViewModel: SettingsViewModel = koinViewModel()
            val setting by settingsViewModel.setting.collectAsState()

            val isDarkMode = setting.isDarkMode ?: true
            val accentColorHex = setting.accentHex ?: DEFAULT_ACCENT_COLOR_HEX

            LaunchedEffect(isDarkMode, accentColorHex) {
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

                AppCompatDelegate.setDefaultNightMode(
                    if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
                    else AppCompatDelegate.MODE_NIGHT_NO
                )
            }

            VibraMobileTheme(
                darkTheme = isDarkMode,
                accentColorHex = accentColorHex
            ) {
                if (!startupState.isLoading) {
                    RootGraph(
                        startDestination = startupState.startDestination,
                        isDarkMode = isDarkMode,
                        onDarkModeChange = { settingsViewModel.updateDarkMode(it) },
                        accentColorHex = accentColorHex,
                        onAccentColorChange = { settingsViewModel.updateAccentColor(it) }
                    )
                }
            }
        }
    }
}