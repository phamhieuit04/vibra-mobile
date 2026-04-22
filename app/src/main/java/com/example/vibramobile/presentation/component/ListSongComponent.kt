package com.example.vibramobile.presentation.component

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
fun ListSongComponent(
    onClick: (Song) -> Unit,
    onPlay: (Song) -> Unit,
    modifier: Modifier = Modifier,
    songs: List<Song>
) {
    LazyRow() {
        itemsIndexed(songs, key = { index, song -> song.id!! }) { index, song ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .noRippleClickable(onClick = { onPlay(song) }),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(
                            shape = CircleShape
                        ),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(song.thumbnailPath?.encodeURLPath())
                        .size(400)
                        .crossfade(true)
                        .build(),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.default_image),
                    error = painterResource(R.drawable.default_image)
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column() {
                        Text(
                            text = song.name.toString(),
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 16.sp,
                            lineHeight = 16.sp
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = song.author?.name.toString(),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp,
                            lineHeight = 12.sp
                        )
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
            }
            Spacer(Modifier.width(16.dp))
        }
    }
}

@Composable
fun ListSongSkeleton(modifier: Modifier = Modifier) {
    Row(modifier = modifier.horizontalScroll(state = rememberScrollState())) {
        for (i in 0..4) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(
                            shape = CircleShape
                        )
                        .skeletonEffect()
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .size(width = 80.dp, height = 16.dp)
                                .skeletonEffect()
                        )
                        Spacer(Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .size(width = 52.dp, height = 12.dp)
                                .skeletonEffect()
                        )
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
            }
            Spacer(Modifier.width(16.dp))
        }
    }
}