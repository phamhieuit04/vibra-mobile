package com.example.vibramobile.presentation.screen

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun LibraryScreen(modifier: Modifier = Modifier) {
    Text(
        text = "Library",
        fontSize = 96.sp,
        color = MaterialTheme.colorScheme.onBackground
    )
}