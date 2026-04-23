package com.example.vibramobile.presentation.component

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.vibramobile.domain.model.User
import com.example.vibramobile.core.extension.skeletonEffect
import io.ktor.http.encodeURLPath

@Composable
fun ListArtistComponent(
    modifier: Modifier = Modifier,
    artists: List<User>,
    onClick: (User) -> Unit = {}
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
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "${artist.followers} người theo dõi",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (index < artists.lastIndex) {
                Spacer(Modifier.width(16.dp))
            }
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