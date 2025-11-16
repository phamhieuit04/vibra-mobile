package com.example.vibramobile.ui.screens.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(modifier: Modifier = Modifier, userName: String?) {
    if (userName != null) {
        Text(text = userName, fontSize = 96.sp, color = Color.White)
    }
}