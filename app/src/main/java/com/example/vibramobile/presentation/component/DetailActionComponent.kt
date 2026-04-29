package com.example.vibramobile.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.DownloadForOffline
import androidx.compose.material.icons.filled.LibraryAdd
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.regular.Playlist
import com.example.vibramobile.presentation.config.DetailActionConfig

@Composable
fun DetailActionComponent(
    modifier: Modifier = Modifier,
    config: DetailActionConfig,
    onShuffle: () -> Unit = {},
    onPlay: () -> Unit = {},
) {
    var showDropdownMenu by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                when (config) {
                    is DetailActionConfig.Artist -> {
                        AsyncImage(
                            model = config.avatarPath,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(4.dp))
                        )
                        OutlinedButton(
                            onClick = config.onFollow,
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.padding(start = 4.dp)
                        ) {
                            Text(
                                text = config.followLabel,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        DetailMoreButton(
                            showDropdownMenu = showDropdownMenu,
                            onShowDropdown = { showDropdownMenu = it },
                            dropdownItems = config.dropdownItems
                        )
                    }

                    is DetailActionConfig.Album -> {
                        Row {
                            IconButton(onClick = config.onAddToQueue) {
                                Icon(
                                    imageVector = Icons.Default.LibraryAdd,
                                    contentDescription = "Thêm vào danh sách phát",
                                    tint = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.size(28.dp)
                                )
                            }

                            IconButton(onClick = config.onDownload) {
                                Icon(
                                    imageVector = Icons.Default.Download,
                                    contentDescription = "Tải xuống",
                                    tint = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Shuffle,
                    contentDescription = "Shuffle",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { onShuffle() }
                )
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable { onPlay() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailMoreButton(
    showDropdownMenu: Boolean,
    onShowDropdown: (Boolean) -> Unit,
    dropdownItems: List<Pair<String, () -> Unit>>,
) {
    Box {
        IconButton(onClick = { onShowDropdown(true) }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More options",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
        DropdownMenu(
            expanded = showDropdownMenu,
            onDismissRequest = { onShowDropdown(false) },
            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
        ) {
            dropdownItems.forEach { (label, action) ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = label,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    onClick = {
                        onShowDropdown(false)
                        action()
                    }
                )
            }
        }
    }
}