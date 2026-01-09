package com.example.vibramobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.vibramobile.ui.navigations.graphs.RootGraph
import com.example.vibramobile.ui.theme.VibraMobileTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VibraMobileTheme {
                RootGraph()
            }
        }
    }
}