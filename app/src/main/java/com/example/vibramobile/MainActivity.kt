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
import com.example.vibramobile.core.di.storeModule
import com.example.vibramobile.core.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.vibramobile.presentation.viewmodel.SplashViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.appcompat.app.AppCompatDelegate
import com.example.vibramobile.core.di.databaseModule

private const val APP_SETTINGS = "app_settings"
private const val KEY_DARK_MODE = "dark_mode"
private const val KEY_ACCENT_HEX = "accent_hex"

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        val sharedPreferences = getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE)
        val isDarkPref = sharedPreferences.getBoolean(KEY_DARK_MODE, true)
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkPref) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )

        startKoin {
            androidContext(this@App)
            modules(
                jsonModule,
                networkModule,
                repositoryModule,
                storeModule,
                viewModelModule,
                databaseModule
            )
        }
    }
}

class MainActivity : ComponentActivity() {

    private val splashViewModel: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferences = getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE)
        val isDarkPref = sharedPreferences.getBoolean(KEY_DARK_MODE, true)
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkPref) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )

        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition {
            splashViewModel.uiState.value.isLoading
        }

        enableEdgeToEdge()

        setContent {
            val startupState by splashViewModel.uiState.collectAsState()

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
                if (!startupState.isLoading) {
                    RootGraph(
                        startDestination = startupState.startDestination,
                        isDarkMode = isDarkMode,
                        onDarkModeChange = { isDarkMode = it },
                        accentColorHex = accentColorHex,
                        onAccentColorChange = { accentColorHex = it }
                    )
                }
            }
        }
    }
}