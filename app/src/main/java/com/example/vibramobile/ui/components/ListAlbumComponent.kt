package com.example.vibramobile.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.vibramobile.R
import com.example.vibramobile.models.Playlist
import com.example.vibramobile.ui.extends.skeletonEffect
import io.ktor.http.encodeURLPath

@Composable
fun ListAlbumComponent(modifier: Modifier = Modifier, albums: List<Playlist>) {
    LazyRow() {
        itemsIndexed(albums) { index, album ->
            Column(modifier = Modifier.width(140.dp)) {
                AsyncImage(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(
                            shape = RoundedCornerShape(6.dp)
                        ),
                    model = album.thumbnail_path.encodeURLPath(),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.default_image),
                    error = painterResource(R.drawable.default_image)
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = album.name,
                    color = Color.White,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = album.author.name.toString(),
                    color = Color.LightGray.copy(alpha = 0.8f),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.width(16.dp))
        }
    }
}

@Composable
fun ListAlbumSkeleton(modifier: Modifier = Modifier) {
    Row(modifier = modifier.horizontalScroll(state = rememberScrollState())) {
        for (i in 0..4) {
            Column(modifier = Modifier.width(140.dp)) {
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(shape = RoundedCornerShape(6.dp))
                        .skeletonEffect()
                )
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(16.dp)
                        .skeletonEffect()
                )
                Spacer(Modifier.height(2.dp))
                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(12.dp)
                        .skeletonEffect()
                )
            }
            if (i < 4) {
                Spacer(Modifier.width(16.dp))
            }
        }
    }
}