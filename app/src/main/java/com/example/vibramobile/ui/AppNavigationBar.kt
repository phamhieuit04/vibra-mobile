package com.example.vibramobile.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import com.example.vibramobile.ui.navigations.destinations.MainDestination

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector
)

val TOP_LEVEL_DESTINATIONS = mapOf(
    MainDestination.Home to BottomNavItem(
        "Home", Icons.Outlined.Home, Icons.Default.Home
    ),
    MainDestination.Search to BottomNavItem(
        "Search", Icons.Outlined.Search, Icons.Default.Search
    ),
    MainDestination.Library to BottomNavItem(
        "Library", Icons.Outlined.LibraryMusic, Icons.Default.LibraryMusic
    )
)

@Composable
fun AppNavigationBar(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    selectedKey: NavKey,
    onSelectKey: (NavKey) -> Unit,
) {
    if (!isVisible) return

    NavigationBar(
        containerColor = Color.Black,
        windowInsets = NavigationBarDefaults.windowInsets
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TOP_LEVEL_DESTINATIONS.forEach { (destination, data) ->
                AppNavigationBarItem(
                    onClick = { onSelectKey(destination) },
                    isSelected = destination == selectedKey,
                    icon = data.icon,
                    iconColor = Color.White,
                    selectedIcon = data.selectedIcon,
                    label = data.label,
                    labelColor = Color.White,
                )
            }
        }
    }
}

@Composable
fun AppNavigationBarItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isSelected: Boolean = false,
    icon: ImageVector,
    iconColor: Color,
    selectedIcon: ImageVector,
    label: String,
    labelColor: Color,
) {
    Surface(
        modifier = Modifier
            .widthIn(min = 64.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            ),
        color = Color.Transparent,
        shape = RoundedCornerShape(5.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp),
        ) {
            Icon(
                modifier = Modifier.size(26.dp),
                contentDescription = "",
                imageVector = if (isSelected) selectedIcon else icon,
                tint = if (isSelected) iconColor.copy(alpha = 0.8f) else iconColor.copy(alpha = 0.6f)
            )
            Spacer(Modifier.height(3.dp))
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) labelColor.copy(alpha = 0.8f) else labelColor.copy(alpha = 0.6f)
            )
        }
    }
}