package com.example.vibramobile.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.vibramobile.R
import com.example.vibramobile.domain.model.Song
import com.example.vibramobile.core.extension.noRippleClickable
import com.example.vibramobile.core.extension.skeletonEffect
import io.ktor.http.encodeURLPath

@Composable
fun ListSongRowComponent(
    onPlay: (Song) -> Unit,
    onClick: (Song) -> Unit,
    modifier: Modifier = Modifier,
    songs: List<Song>
) {
    Column() {
        for (song in songs) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .noRippleClickable(onClick = { onPlay(song) }),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row() {
                    AsyncImage(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(
                                shape = RoundedCornerShape(
                                    4.dp
                                )
                            ),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(song.thumbnailPath?.encodeURLPath())
                            .size(400)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(R.drawable.default_image),
                        error = painterResource(R.drawable.default_image)
                    )
                    Spacer(Modifier.width(12.dp))
                    Column() {
                        Text(
                            text = song.name.toString(),
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 15.sp,
                            lineHeight = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = song.author?.name.toString(),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp,
                            lineHeight = 12.sp
                        )
                    }
                }
                IconButton(onClick = { onClick(song) }) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = Icons.Default.MoreHoriz,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
fun ListSongRowSkeleton(modifier: Modifier = Modifier) {
    Column() {
        for (i in 0..3) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(
                                shape = RoundedCornerShape(
                                    4.dp
                                )
                            )
                            .skeletonEffect()
                    )
                    Spacer(Modifier.width(12.dp))
                    Column() {
                        Box(
                            modifier = Modifier
                                .size(width = 160.dp, height = 15.dp)
                                .skeletonEffect()
                        )
                        Spacer(Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .size(width = 96.dp, height = 15.dp)
                                .skeletonEffect()
                        )
                    }
                }
                IconButton(onClick = {}) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = Icons.Default.MoreHoriz,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.65f)
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}