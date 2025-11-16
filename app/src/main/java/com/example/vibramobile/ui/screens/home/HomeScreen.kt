package com.example.vibramobile.ui.screens.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Text(text = "Home", color = Color.White, fontSize = 96.sp)
}

@Preview(showBackground = true, device = "id:pixel_3", backgroundColor = 0xff000000)
@Composable
fun Preview(modifier: Modifier = Modifier) {
    HomeScreen()
}