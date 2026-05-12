package com.example.vibramobile.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import com.example.vibramobile.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen() {
    var showSpinner by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(1000)
        showSpinner = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null,
                modifier = Modifier.size(120.dp)
            )

            if (showSpinner) {
                Spacer(modifier = Modifier.height(24.dp))
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onBackground,
                    trackColor = Color.Transparent
                )
            }
        }
    }
}
