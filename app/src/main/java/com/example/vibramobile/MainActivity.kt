package com.example.vibramobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.toArgb
import com.example.vibramobile.ui.navigations.graphs.RootGraph
import com.example.vibramobile.ui.theme.VibraMobileTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isDarkMode by rememberSaveable { mutableStateOf(true) }

            VibraMobileTheme(darkTheme = isDarkMode) {
                val backgroundColor = MaterialTheme.colorScheme.background.toArgb()

                SideEffect {
                    enableEdgeToEdge(
                        statusBarStyle = if (isDarkMode) {
                            SystemBarStyle.dark(backgroundColor)
                        } else {
                            SystemBarStyle.light(backgroundColor, backgroundColor)
                        },
                        navigationBarStyle = if (isDarkMode) {
                            SystemBarStyle.dark(backgroundColor)
                        } else {
                            SystemBarStyle.light(backgroundColor, backgroundColor)
                        }
                    )
                }

                RootGraph(
                    isDarkMode = isDarkMode,
                    onDarkModeChange = { isDarkMode = it }
                )
            }
        }
    }
}