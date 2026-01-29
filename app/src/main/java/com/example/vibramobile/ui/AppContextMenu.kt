package com.example.vibramobile.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import com.composables.core.DragIndication
import com.composables.core.ModalBottomSheet
import com.composables.core.Scrim
import com.composables.core.Sheet
import com.composables.core.SheetDetent
import com.composables.core.rememberModalBottomSheetState
import com.example.vibramobile.ui.extends.noRippleClickable
import com.example.vibramobile.viewmodels.ContextMenuViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppContextMenu(
    modifier: Modifier = Modifier,
    viewModel: ContextMenuViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

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
            viewModel.hide()
        }
    }

    val scrimAlpha by animateFloatAsState(
        targetValue = if (sheetState.currentDetent != SheetDetent.Hidden) 0.5f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "scrim_alpha"
    )

    ModalBottomSheet(state = sheetState) {
        Scrim(
            scrimColor = Color.Black.copy(alpha = scrimAlpha),
            modifier = Modifier.noRippleClickable(onClick = { viewModel.hide() })
        )

        Sheet(
            modifier = Modifier
                .padding(top = 48.dp)
                .shadow(4.dp, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(Color(0xFF282828))
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
                            Color(0xFF404040),
                            RoundedCornerShape(100.dp)
                        )
                        .width(40.dp)
                        .height(4.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                ContextMenuContent(
                    songTitle = uiState.songTitle,
                    artistName = uiState.artistName,
                    onMenuItemClick = { action ->
                        when (action) {
                            is MenuAction.Share -> {}
                            is MenuAction.Premium -> {}
                            is MenuAction.AddToLiked -> {}
                            is MenuAction.AddToPlaylist -> {}
                            is MenuAction.GoToRadio -> {}
                            is MenuAction.GoToAlbum -> {}
                            is MenuAction.GoToArtist -> {}
                            is MenuAction.GoToConcerts -> {}
                            is MenuAction.ViewCredits -> {}
                        }
                        viewModel.hide()
                    }
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun ContextMenuContent(
    songTitle: String,
    artistName: String,
    onMenuItemClick: (MenuAction) -> Unit
) {
    val scrollState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        SongHeader(
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
                    icon = Icons.Default.Share,
                    text = "Share",
                    onClick = { onMenuItemClick(MenuAction.Share) }
                )
            }

            item {
                MenuItem(
                    icon = Icons.Default.Diamond,
                    text = "Listen to music ad-free",
                    trailingText = "Premium",
                    trailingColor = Color(0xFF1DB954),
                    onClick = { onMenuItemClick(MenuAction.Premium) }
                )
            }

            item {
                MenuItem(
                    icon = Icons.Default.FavoriteBorder,
                    text = "Add to Liked Songs",
                    onClick = { onMenuItemClick(MenuAction.AddToLiked) }
                )
            }

            item {
                MenuItem(
                    icon = Icons.Default.Add,
                    text = "Add to playlist",
                    onClick = { onMenuItemClick(MenuAction.AddToPlaylist) }
                )
            }

            item {
                MenuItem(
                    icon = Icons.Default.Radio,
                    text = "Go to radio",
                    onClick = { onMenuItemClick(MenuAction.GoToRadio) }
                )
            }

            item {
                MenuItem(
                    icon = Icons.Default.Album,
                    text = "Go to album",
                    onClick = { onMenuItemClick(MenuAction.GoToAlbum) }
                )
            }

            item {
                MenuItem(
                    icon = Icons.Default.Person,
                    text = "Go to artist",
                    onClick = { onMenuItemClick(MenuAction.GoToArtist) }
                )
            }

            item {
                MenuItem(
                    icon = Icons.Default.Theaters,
                    text = "Go to artist concerts",
                    onClick = { onMenuItemClick(MenuAction.GoToConcerts) }
                )
            }

            item {
                MenuItem(
                    icon = Icons.Default.Info,
                    text = "View song credits",
                    onClick = { onMenuItemClick(MenuAction.ViewCredits) }
                )
            }
        }
    }
}

@Composable
private fun SongHeader(
    songTitle: String,
    artistName: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color(0xFF404040))
        ) { }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = songTitle,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = artistName,
                color = Color(0xFFB3B3B3),
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun MenuItem(
    icon: ImageVector,
    text: String,
    trailingText: String? = null,
    trailingColor: Color = Color(0xFF1DB954),
    onClick: () -> Unit = {}
) {
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
            tint = Color(0xFFB3B3B3),
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = text,
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )

        if (trailingText != null) {
            Text(
                text = trailingText,
                color = trailingColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

sealed class MenuAction {
    object Share : MenuAction()
    object Premium : MenuAction()
    object AddToLiked : MenuAction()
    object AddToPlaylist : MenuAction()
    object GoToRadio : MenuAction()
    object GoToAlbum : MenuAction()
    object GoToArtist : MenuAction()
    object GoToConcerts : MenuAction()
    object ViewCredits : MenuAction()
}

data class ContextMenuState(
    val visible: Boolean = false,
    val songTitle: String = "Six Feet Under",
    val artistName: String = "Billie Eilish"
)