package com.example.gratitudegarden.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
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
        icon = Icons.AutoMirrored.Filled.List
    )
}
