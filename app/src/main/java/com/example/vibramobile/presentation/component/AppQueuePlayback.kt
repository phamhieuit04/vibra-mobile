package com.example.vibramobile.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.composables.core.DragIndication
import com.composables.core.ModalBottomSheet
import com.composables.core.Scrim
import com.composables.core.Sheet
import com.composables.core.SheetDetent
import com.composables.core.rememberModalBottomSheetState
import com.example.vibramobile.core.extension.noRippleClickable
import com.example.vibramobile.domain.model.Song
import com.example.vibramobile.presentation.config.LayoutStyleConfig
import com.example.vibramobile.presentation.viewmodel.ContextMenuViewModel
import com.example.vibramobile.presentation.viewmodel.MediaPlayerViewModel
import org.koin.androidx.compose.koinViewModel
import kotlin.collections.emptyList

@Composable
fun AppQueuePlayback(
    bottomContentPadding: Dp = 0.dp,
    mediaPlayerViewModel: MediaPlayerViewModel = koinViewModel(),
    contextMenuViewModel: ContextMenuViewModel = koinViewModel()
) {
    val uiState by mediaPlayerViewModel.uiState.collectAsState()
    val currentSong = uiState.currentSong
    val queuePlaybacks = uiState.queue

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

    LaunchedEffect(uiState.isQueueVisible) {
        sheetState.targetDetent = if (uiState.isQueueVisible) {
            Peek
        } else {
            SheetDetent.Hidden
        }
    }

    LaunchedEffect(sheetState.currentDetent) {
        if (sheetState.currentDetent == SheetDetent.Hidden && uiState.isQueueVisible) {
            mediaPlayerViewModel.toggleQueue(false)
        }
    }

    ModalBottomSheet(state = sheetState) {
        Scrim(
            modifier = Modifier.noRippleClickable(
                onClick = { mediaPlayerViewModel.toggleQueue(false) })
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
                    .padding(horizontal = 16.dp)
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

                SpotifySection(
                    title = "Danh sách phát của bạn"
                ) {
                    TopSongComponent(
                        thumbnailPath = currentSong?.thumbnailPath ?: "",
                        songTitle = currentSong?.name ?: "",
                        artistName = currentSong?.author?.name ?: ""
                    )
                }

                HorizontalDivider(
                    modifier = Modifier.padding(top = 12.dp, bottom = 18.dp),
                    thickness = 2.dp
                )

                ListSongComponent(
                    songs = queuePlaybacks,
                    layoutStyle = LayoutStyleConfig.Vertical,
                    onPlay = { mediaPlayerViewModel.playSong(it) },
                    onClick = {
                        contextMenuViewModel.showSong(it)
                    },
                    currentSongId = currentSong?.id,
                    currentSongPath = currentSong?.songPath
                )

                Spacer(modifier = Modifier.height(bottomContentPadding))
            }
        }
    }
}