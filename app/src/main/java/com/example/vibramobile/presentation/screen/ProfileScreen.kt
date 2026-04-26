package com.example.vibramobile.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness6
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlaylistPlay
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vibramobile.domain.model.Bill
import com.example.vibramobile.presentation.state.UserState
import com.example.vibramobile.presentation.theme.AccentColorHexList
import com.example.vibramobile.presentation.viewmodel.ProfileViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.core.graphics.toColorInt
import coil3.compose.AsyncImage
import com.example.vibramobile.presentation.component.ListAlbumComponent
import com.example.vibramobile.presentation.component.SpotifySection
import com.example.vibramobile.presentation.viewmodel.ContextMenuViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    bottomContentPadding: Dp = 0.dp,
    isDarkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    selectedAccentColorHex: String,
    onAccentColorChange: (String) -> Unit,
    profileViewModel: ProfileViewModel = koinViewModel(),
    contextMenuViewModel: ContextMenuViewModel = koinViewModel()
) {
    val currentUser by UserState.currentUser.collectAsState()
    val followedArtists by UserState.followedArtists.collectAsState()
    val myPlaylists by UserState.myPlaylists.collectAsState()
    val myAlbums by UserState.myAlbums.collectAsState()
    val paymentHistory by UserState.paymentHistory.collectAsState()
    val isRefreshing by profileViewModel.isRefreshing.collectAsState()
    val pullToRefreshState = rememberPullToRefreshState()

    val statusBarHeight = WindowInsets.statusBars
        .asPaddingValues()
        .calculateTopPadding()

    PullToRefreshBox(
        modifier = modifier.fillMaxSize(),
        state = pullToRefreshState,
        isRefreshing = isRefreshing,
        onRefresh = { profileViewModel.refresh() },
        indicator = {
            PullToRefreshDefaults.Indicator(
                state = pullToRefreshState,
                isRefreshing = isRefreshing,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = statusBarHeight,
                ),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(bottom = bottomContentPadding)
        ) {
            item(key = "header") {
                Spacer(modifier = Modifier.height(24.dp))
                ProfileHeader(
                    currentUser?.name.orEmpty(),
                    currentUser?.avatarPath.orEmpty()
                )
            }

            item(key = "stats") {
                Spacer(modifier = Modifier.height(24.dp))
                StatsRow(
                    playlistCount = myPlaylists.size,
                    followedArtistsCount = followedArtists.size
                )
            }

            item(key = "settings") {
                SpotifySection(
                    title = "Cài đặt"
                ) {
                    SettingsSection(
                        isDarkMode = isDarkMode,
                        onDarkModeChange = onDarkModeChange,
                        selectedAccentColorHex = selectedAccentColorHex,
                        onAccentColorChange = onAccentColorChange
                    )
                }
            }

            if (myAlbums.isNotEmpty()) {
                item(key = "albums") {
                    SpotifySection(
                        title = "Album của tôi"
                    ) {
                        ListAlbumComponent(
                            albums = myAlbums,
                            onClick = {
                                contextMenuViewModel.show(
                                    it.thumbnailPath,
                                    it.name,
                                    it.author?.name
                                )
                            }
                        )
                    }
                }
            }

            item(key = "payment_history") {
                SpotifySection(
                    title = "Lịch sử giao dịch"
                ) {
                    PaymentHistorySection(bills = paymentHistory)
                }
            }
        }
    }
}

@Composable
private fun PaymentHistorySection(bills: List<Bill>) {
    if (bills.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Chưa có giao dịch nào",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp
            )
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            bills.forEachIndexed { index, bill ->
                BillItem(bill = bill)
                if (index < bills.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = DividerDefaults.Thickness,
                        color = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun BillItem(bill: Bill) {
    val itemName = bill.song?.name ?: bill.playlist?.name ?: "Không rõ"
    val itemPrice = bill.song?.price ?: bill.playlist?.price ?: 0
    val thumbnailPath = bill.song?.thumbnailPath ?: bill.playlist?.thumbnailPath
    val isPlaylist = bill.playlist != null

    val formattedDate = remember(bill.createdAt) {
        runCatching {
            val inputFormat =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val date = inputFormat.parse(bill.createdAt ?: "")
            outputFormat.format(date!!)
        }.getOrElse { bill.createdAt.orEmpty() }
    }

    val formattedPrice = remember(itemPrice) {
        NumberFormat.getNumberInstance(Locale("vi", "VN")).format(itemPrice) + "đ"
    }

    val statusText = when (bill.status) {
        "1" -> "Thất bại"
        "2" -> "Thành công"
        else -> "Không rõ"
    }

    val statusColor = when (bill.status) {
        "1" -> Color(0xFFE53935)
        "2" -> Color(0xFF1DB954)
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                if (!thumbnailPath.isNullOrBlank()) {
                    AsyncImage(
                        model = thumbnailPath,
                        contentDescription = itemName,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = if (isPlaylist) Icons.Default.PlaylistPlay else Icons.Default.MusicNote,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = itemName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = if (isPlaylist) "Playlist" else "Bài hát",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = formattedPrice,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = statusText,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = statusColor,
                fontSize = 13.sp
            )
        }
    }
}

@Composable
private fun ProfileHeader(userName: String, avatar: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = avatar,
                contentDescription = "",
                modifier = Modifier.size(100.dp),
                contentScale = ContentScale.FillBounds
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Hồ sơ",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = userName,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun StatsRow(playlistCount: Int, followedArtistsCount: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        StatItem(value = playlistCount.toString(), label = "Playlist")
        Spacer(modifier = Modifier.width(48.dp))
        StatItem(value = followedArtistsCount.toString(), label = "Đang theo dõi")
    }
}

@Composable
private fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun SettingsSection(
    isDarkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    selectedAccentColorHex: String,
    onAccentColorChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Brightness6,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(22.dp)
                )

                Spacer(modifier = Modifier.width(14.dp))

                Text(
                    text = "Giao diện tối",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Switch(
                checked = isDarkMode,
                onCheckedChange = onDarkModeChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                    checkedTrackColor = MaterialTheme.colorScheme.primary,
                    checkedBorderColor = MaterialTheme.colorScheme.primary
                )
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            thickness = DividerDefaults.Thickness,
            color = MaterialTheme.colorScheme.surfaceVariant
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Text(
                text = "Màu chủ đạo",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AccentColorHexList.forEach { colorHex ->
                    AccentColorOption(
                        colorHex = colorHex,
                        isSelected = colorHex.equals(selectedAccentColorHex, ignoreCase = true),
                        onClick = { onAccentColorChange(colorHex.uppercase()) }
                    )
                }
            }
        }
    }
}

@Composable
private fun AccentColorOption(
    colorHex: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val optionColor = Color(colorHex.toColorInt())

    Box(
        modifier = Modifier
            .size(34.dp)
            .clip(CircleShape)
            .background(optionColor)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                shape = CircleShape
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = if (optionColor.luminance() > 0.55f) Color.Black else Color.White
            )
        }
    }
}