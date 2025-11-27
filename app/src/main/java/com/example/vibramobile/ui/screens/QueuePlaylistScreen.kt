package com.example.vibramobile.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PauseCircleFilled
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vibramobile.R
import com.example.vibramobile.ui.MediaPlayer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QueuePlaylistScreen(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    onVisibleChange: (Boolean) -> Unit
) {
    val state = rememberModalBottomSheetState()

    if (isVisible) {
        ModalBottomSheet(
            modifier = Modifier.fillMaxHeight(),
            sheetState = state,
            onDismissRequest = { onVisibleChange(false) },
            containerColor = Color(0xff1f1f1f)
        ) {
            LazyColumn(
                modifier = Modifier.padding(top = 16.dp),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp)
            ) {
                item {
                    Column() {
                        Text(
                            text = "Danh sách phát",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            lineHeight = 24.sp
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = "Đăng phát",
                            color = Color.LightGray.copy(alpha = 0.8f),
                            fontSize = 14.sp,
                            lineHeight = 14.sp
                        )

                        Spacer(Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row() {
                                Image(
                                    modifier = Modifier.size(64.dp),
                                    contentScale = ContentScale.Crop,
                                    contentDescription = "",
                                    painter = painterResource(R.drawable.default_image)
                                )
                                Column() {
                                    Text(
                                        text = "Tên bài hát",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        lineHeight = 16.sp
                                    )
                                    Spacer(Modifier.height(2.dp))
                                    Text(
                                        text = "Tên nghệ sĩ",
                                        color = Color.LightGray.copy(alpha = 0.8f),
                                        fontSize = 12.sp,
                                        lineHeight = 12.sp
                                    )
                                }
                            }
                            IconButton(
                                modifier = Modifier.size(52.dp),
                                onClick = { MediaPlayer.playOrPause() }
                            ) {
                                Icon(
                                    modifier = Modifier.fillMaxSize(),
                                    contentDescription = "",
                                    imageVector = if (MediaPlayer.isPlaying.value) Icons.Default.PauseCircleFilled
                                    else Icons.Default.PlayCircleFilled,
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_3", backgroundColor = 0xff000000)
@Composable
fun Preview(modifier: Modifier = Modifier) {
    QueuePlaylistScreen(isVisible = true, onVisibleChange = {})
}