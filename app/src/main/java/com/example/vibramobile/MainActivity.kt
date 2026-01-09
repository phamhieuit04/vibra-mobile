package com.example.vibramobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext
import com.example.vibramobile.ui.MediaPlayer
import com.example.vibramobile.ui.theme.VibraMobileTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            MediaPlayer.createPlayer(context = context)

            VibraMobileTheme {
                AppScreen()
            }
        }
    }
}