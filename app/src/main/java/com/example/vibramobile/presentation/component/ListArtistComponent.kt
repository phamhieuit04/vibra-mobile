package com.example.vibramobile.presentation.component

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.vibramobile.R
import com.example.vibramobile.core.extension.noRippleClickable
import com.example.vibramobile.domain.model.User
import com.example.vibramobile.core.extension.skeletonEffect
import com.example.vibramobile.presentation.config.LayoutStyle
import io.ktor.http.encodeURLPath

@Composable
fun ListArtistComponent(
    modifier: Modifier = Modifier,
    artists: List<User>,
    layoutStyle: LayoutStyle = LayoutStyle.Horizontal,
    onClick: (User) -> Unit = {}
) {
    when (layoutStyle) {
        LayoutStyle.Horizontal -> HorizontalListArtist(artists = artists, onClick = onClick)
        LayoutStyle.Vertical -> VerticalListArtist(artists = artists, onClick = onClick)
    }
}

@Composable
fun HorizontalListArtist(
    modifier: Modifier = Modifier,
    artists: List<User>,
    onClick: (User) -> Unit
) {
    LazyRow() {
        itemsIndexed(artists) { index, artist ->
            Column(
                modifier = Modifier
                    .width(140.dp)
                    .clickable(onClick = { onClick(artist) })
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(shape = RoundedCornerShape(6.dp)),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(artist.avatarPath?.encodeURLPath())
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
                    text = artist.name.toString(),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 15.sp,
                    maxLines = 1,
                    fontWeight = FontWeight.SemiBold,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "${artist.followers} người theo dõi",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 1.sp
                )
            }
            if (index < artists.lastIndex) {
                Spacer(Modifier.width(16.dp))
            }
        }
    }
}

@Composable
fun VerticalListArtist(
    modifier: Modifier = Modifier,
    artists: List<User>,
    onClick: (User) -> Unit
) {
    Column(modifier = modifier) {
        for (artist in artists) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .noRippleClickable(onClick = { onClick(artist) }),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(artist.avatarPath?.encodeURLPath())
                            .size(400)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(R.drawable.default_image),
                        error = painterResource(R.drawable.default_image)
                    )
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            text = artist.name.toString(),
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 18.sp
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = "${artist.followers ?: 0} người theo dõi",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp,
                            lineHeight = 12.sp
                        )
                    }
                }
                IconButton(onClick = { onClick(artist) }) {
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
fun ListArtistSkeleton(modifier: Modifier = Modifier) {
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
                        .width(100.dp)
                        .height(16.dp)
                        .skeletonEffect()
                )
                Spacer(Modifier.height(2.dp))
                Box(
                    modifier = Modifier
                        .width(90.dp)
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
fun ListArtistRowSkeleton(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
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