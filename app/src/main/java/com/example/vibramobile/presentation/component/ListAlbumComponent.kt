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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.vibramobile.R
import com.example.vibramobile.core.extension.noRippleClickable
import com.example.vibramobile.domain.model.Playlist
import com.example.vibramobile.core.extension.skeletonEffect
import com.example.vibramobile.presentation.config.LayoutStyleConfig
import io.ktor.http.encodeURLPath

@Composable
fun ListAlbumComponent(
    modifier: Modifier = Modifier,
    albums: List<Playlist>,
    layoutStyle: LayoutStyleConfig = LayoutStyleConfig.Horizontal,
    onClick: (Playlist) -> Unit
) {
    when (layoutStyle) {
        LayoutStyleConfig.Vertical -> VerticalListAlbum(albums = albums, onClick = onClick)
        LayoutStyleConfig.Horizontal -> HorizontalListAlbum(albums = albums, onClick = onClick)
    }

}

@Composable
fun VerticalListAlbum(
    modifier: Modifier = Modifier,
    albums: List<Playlist>,
    onClick: (Playlist) -> Unit
) {
    Column(modifier = modifier) {
        for (album in albums) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .noRippleClickable(onClick = { onClick(album) }),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(album.thumbnailPath?.encodeURLPath())
                            .size(400)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(R.drawable.default_image),
                        error = painterResource(R.drawable.default_image)
                    )
                    Spacer(Modifier.width(12.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = album.name ?: "",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 1.sp,
                        )
                        val authorName = album.author?.name
                        if (!authorName.isNullOrBlank()) {
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = authorName,
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 12.sp,
                                lineHeight = 1.sp
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
fun HorizontalListAlbum(
    modifier: Modifier = Modifier,
    albums: List<Playlist>,
    onClick: (Playlist) -> Unit
) {
    LazyRow(modifier = modifier) {
        itemsIndexed(albums) { index, album ->
            Column(
                modifier = Modifier
                    .width(140.dp)
                    .noRippleClickable { onClick(album) }
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(
                            shape = RoundedCornerShape(6.dp)
                        ),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(album.thumbnailPath?.encodeURLPath())
                        .size(400)
                        .crossfade(true)
                        .build(),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.default_image),
                    error = painterResource(R.drawable.default_image)
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = album.name ?: "",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 1.sp
                )
                val authorName = album.author?.name
                if (!authorName.isNullOrBlank()) {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = authorName,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 1.sp
                    )
                }
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

@Composable
fun ListAlbumRowSkeleton(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        for (i in 0..3) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .skeletonEffect()
                    )
                    Spacer(Modifier.width(12.dp))
                    Column {
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
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}