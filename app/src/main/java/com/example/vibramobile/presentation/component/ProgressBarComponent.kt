package com.example.vibramobile.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vibramobile.core.util.FormatHelper

@Composable
fun ProgressBarComponent(
    modifier: Modifier = Modifier,
    progress: Float,
    height: Dp = 4.dp,
    roundedCornerShape: RoundedCornerShape = RoundedCornerShape(8.dp),
    currentTime: Long? = null,
    totalTime: Long? = null
) {
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .height(height)
                .fillMaxWidth()
                .clip(shape = roundedCornerShape)
                .background(color = Color.White.copy(alpha = 0.28f))
        ) {
            Box(
                modifier = Modifier
                    .height(height)
                    .fillMaxWidth(progress)
                    .clip(shape = roundedCornerShape)
                    .background(color = Color.White)
            )
        }

        if (currentTime != null && totalTime != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = FormatHelper.formatTime(currentTime),
                    color = Color.White,
                    fontSize = 15.sp
                )
                Text(
                    text = FormatHelper.formatTime(totalTime),
                    color = Color.White,
                    fontSize = 15.sp
                )
            }
        }
    }
}