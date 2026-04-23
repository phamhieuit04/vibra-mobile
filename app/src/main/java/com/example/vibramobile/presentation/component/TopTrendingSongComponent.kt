package com.example.vibramobile.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.vibramobile.domain.model.Song

@Composable
fun TopTrendingSongComponent(
    songs: List<Song>,
    onClick: (Song) -> Unit,
    onPlay: (Song) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        songs.take(5).forEachIndexed { index, song ->
            TrendingRow(
                index = index + 1,
                song = song,
                onClick = onClick,
                onPlay = onPlay
            )
        }
    }
}

@Composable
private fun TrendingRow(
    index: Int,
    song: Song,
    onClick: (Song) -> Unit,
    onPlay: (Song) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPlay(song) }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = index.toString(),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.onBackground.copy(alpha = 0.6f),
            modifier = Modifier.width(36.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        AsyncImage(
            model = song.thumbnailPath,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(6.dp))
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = song.name ?: "Unknow title",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = colorScheme.onBackground,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = song.author?.name ?: "",
                fontSize = 13.sp,
                color = colorScheme.onBackground.copy(alpha = 0.6f),
                maxLines = 1
            )
        }

        Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = "Play",
            tint = colorScheme.onBackground,
            modifier = Modifier
                .size(28.dp)
                .clickable { onPlay(song) }
        )

        Spacer(modifier = Modifier.width(12.dp))

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