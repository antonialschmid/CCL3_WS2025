package com.example.gratitudegarden.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Garden : BottomNavItem(
        route = "garden",
        label = "Garden",
        icon = Icons.Filled.Home
    )

    object History : BottomNavItem(
        route = "history",
        label = "History",
        icon = Icons.Filled.List
    )

    object Settings : BottomNavItem(
        route = "settings",
        label = "Settings",
        icon = Icons.Filled.Settings
    )
}
