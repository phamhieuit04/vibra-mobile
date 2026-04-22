package com.example.vibramobile.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
fun TopArtistsComponent(
    modifier: Modifier = Modifier,
    artists: List<User>,
    onClick: (User) -> Unit = {}
) {
    Row(
        modifier = modifier.horizontalScroll(state = rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        artists.forEachIndexed { index, artist ->
            TopArtistCard(
                artist = artist,
                rank = index + 1,
                onClick = { onClick(artist) }
            )
        }
    }
}

@Composable
fun TopArtistCard(
    artist: User,
    rank: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(200.dp)
            .height(280.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = ImageRequest.Builder(LocalContext.current)
                .data(artist.avatar_path?.encodeURLPath())
                .size(400)
                .crossfade(true)
                .build(),
            contentDescription = artist.name,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.default_image),
            error = painterResource(R.drawable.default_image)
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.22f),
                            Color.Black.copy(alpha = 0.74f)
                        )
                    )
                )
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
                .size(40.dp)
                .background(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = rank.toString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text(
                text = artist.name.toString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "${artist.followers} người theo dõi",
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.82f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun TopArtistsSkeleton(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.horizontalScroll(state = rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        repeat(5) { index ->
            TopArtistCardSkeleton(rank = index + 1)
        }
    }
}

@Composable
fun TopArtistCardSkeleton(rank: Int) {
    Box(
        modifier = Modifier
            .width(200.dp)
            .height(280.dp)
            .clip(RoundedCornerShape(16.dp))
            .skeletonEffect()
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
                .size(40.dp)
                .background(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = rank.toString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(140.dp)
                    .height(18.dp)
                    .background(
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.20f),
                        RoundedCornerShape(4.dp)
                    )
            )
            Spacer(Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(13.dp)
                    .background(
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.14f),
                        RoundedCornerShape(4.dp)
                    )
            )
        }
    }
}