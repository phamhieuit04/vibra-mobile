package com.example.vibramobile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Brightness6
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.vibramobile.states.UserState
import com.example.vibramobile.ui.theme.AccentColorHexList
import com.example.vibramobile.viewmodels.ProfileViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.core.graphics.toColorInt
import coil3.compose.AsyncImage

@Composable
fun ProfileScreen(
    isDarkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    selectedAccentColorHex: String,
    onAccentColorChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    profileViewModel: ProfileViewModel = koinViewModel()
) {
    val currentUser by UserState.currentUser.collectAsState()
    val followedArtists by UserState.followedArtists.collectAsState()
    val myPlaylists by UserState.myPlaylists.collectAsState()

    LaunchedEffect(Unit) {
        profileViewModel.fetchFollowedArtists()
        profileViewModel.fetchMyPlaylists()
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            ProfileHeader(
                currentUser?.name.orEmpty(),
                currentUser?.avatar_path.orEmpty()
            )
            Spacer(modifier = Modifier.height(28.dp))
            StatsRow(
                playlistCount = myPlaylists.size,
                followedArtistsCount = followedArtists.size
            )
            Spacer(modifier = Modifier.height(32.dp))
            SettingsSection(
                isDarkMode = isDarkMode,
                onDarkModeChange = onDarkModeChange,
                selectedAccentColorHex = selectedAccentColorHex,
                onAccentColorChange = onAccentColorChange
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
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Cài đặt",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 12.dp)
        )

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