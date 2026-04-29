package com.example.vibramobile.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Theaters
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.regular.Playlist
import com.composables.core.DragIndication
import com.composables.core.ModalBottomSheet
import com.composables.core.Scrim
import com.composables.core.Sheet
import com.composables.core.SheetDetent
import com.composables.core.rememberModalBottomSheetState
import com.example.vibramobile.core.extension.noRippleClickable
import com.example.vibramobile.domain.model.Playlist
import com.example.vibramobile.domain.model.Song
import com.example.vibramobile.domain.model.User
import com.example.vibramobile.presentation.viewmodel.ContextMenuViewModel
import com.example.vibramobile.presentation.viewmodel.MediaPlayerViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppContextMenu(
    modifier: Modifier = Modifier,
    contextMenuViewModel: ContextMenuViewModel = koinViewModel(),
    mediaPlayerViewModel: MediaPlayerViewModel = koinViewModel(),
    navigateToArtist: (artist: User) -> Unit
) {
    val uiState by contextMenuViewModel.uiState.collectAsState()

    val Peek = SheetDetent(identifier = "peek") { containerHeight, sheetHeight ->
        containerHeight * 0.6f
    }

    val sheetState = rememberModalBottomSheetState(
        initialDetent = SheetDetent.Hidden,
        detents = listOf(
            SheetDetent.Hidden,
            Peek,
            SheetDetent.FullyExpanded
        )
    )

    LaunchedEffect(uiState.visible) {
        sheetState.targetDetent = if (uiState.visible) {
            Peek
        } else {
            SheetDetent.Hidden
        }
    }

    LaunchedEffect(sheetState.currentDetent) {
        if (sheetState.currentDetent == SheetDetent.Hidden && uiState.visible) {
            contextMenuViewModel.hide()
        }
    }

    ModalBottomSheet(state = sheetState) {
        Scrim(
            modifier = Modifier.noRippleClickable(
                onClick = { contextMenuViewModel.hide() })
        )

        Sheet(
            modifier = Modifier
                .padding(top = 48.dp)
                .shadow(4.dp, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxWidth()
                .imePadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 24.dp)
            ) {
                DragIndication(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .background(
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.45f),
                            RoundedCornerShape(100.dp)
                        )
                        .width(40.dp)
                        .height(4.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                ContextMenuContent(
                    thumbnailPath = uiState.thumbnailPath,
                    songTitle = uiState.songTitle,
                    artistName = uiState.artistName,
                    onMenuItemClick = { action ->
                        when (action) {
                            is MenuAction.AddToQueue -> {}
                            is MenuAction.AddToLiked -> {}
                            is MenuAction.AddToPlaylist -> {}
                            is MenuAction.GoToArtist -> {}
                        }
                        contextMenuViewModel.hide()
                    }
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun ContextMenuContent(
    thumbnailPath: String,
    songTitle: String,
    artistName: String,
    onMenuItemClick: (MenuAction) -> Unit,
) {
    val scrollState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        TopSongComponent(
            thumbnailPath = thumbnailPath,
            songTitle = songTitle,
            artistName = artistName
        )

        HorizontalDivider(
            modifier = Modifier.padding(top = 12.dp, bottom = 18.dp),
            thickness = 2.dp
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            state = scrollState
        ) {
            item {
                MenuItem(
                    icon = PhosphorIcons.Regular.Playlist,
                    text = "Thêm vào danh sách phát",
                    onClick = { onMenuItemClick(MenuAction.AddToQueue) }
                )
            }

            item {
                MenuItem(
                    icon = Icons.Default.FavoriteBorder,
                    text = "Yêu thích bài hát",
                    onClick = { onMenuItemClick(MenuAction.AddToLiked) }
                )
            }

            item {
                MenuItem(
                    icon = Icons.Default.Add,
                    text = "Thêm vào playlist",
                    onClick = { onMenuItemClick(MenuAction.AddToPlaylist) }
                )
            }

            item {
                MenuItem(
                    icon = Icons.Default.Person,
                    text = "Thông tin nghệ sỹ",
                    onClick = { onMenuItemClick(MenuAction.GoToArtist) }
                )
            }
        }
    }
}

@Composable
private fun MenuItem(
    icon: ImageVector,
    text: String,
    trailingText: String? = null,
    trailingColor: Color? = null,
    onClick: () -> Unit = {}
) {
    val resolvedTrailingColor = trailingColor ?: MaterialTheme.colorScheme.primary

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .noRippleClickable(onClick = onClick)
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )

        if (trailingText != null) {
            Text(
                text = trailingText,
                color = resolvedTrailingColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

sealed class MenuAction {
    object AddToLiked : MenuAction()
    object AddToPlaylist : MenuAction()
    object AddToQueue : MenuAction()
    object GoToArtist : MenuAction()
}