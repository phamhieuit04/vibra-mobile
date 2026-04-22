package com.example.vibramobile.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import com.example.vibramobile.presentation.navigation.destination.MainDestination

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
    ),
    MainDestination.Profile to BottomNavItem(
        "Profile", Icons.Outlined.AccountCircle, Icons.Default.AccountCircle
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

    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    val gradientBase = MaterialTheme.colorScheme.background

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        gradientBase.copy(alpha = 0.2f),
                        gradientBase.copy(alpha = 0.5f),
                        gradientBase.copy(alpha = 0.8f),
                        gradientBase.copy(alpha = 0.95f)
                    ),
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY
                )
            )
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TOP_LEVEL_DESTINATIONS.forEach { (destination, data) ->
                AppNavigationBarItem(
                    onClick = { onSelectKey(destination) },
                    isSelected = destination == selectedKey,
                    icon = data.icon,
                    iconColor = onSurfaceColor,
                    selectedIcon = data.selectedIcon,
                    label = data.label,
                    labelColor = onSurfaceColor,
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
        modifier = modifier
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
                tint = if (isSelected) iconColor else iconColor.copy(alpha = 0.8f)
            )
            Spacer(Modifier.height(3.dp))
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) labelColor else labelColor.copy(alpha = 0.8f)
            )
        }
    }
}