package com.example.gratitudegarden.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.gratitudegarden.R
import com.example.gratitudegarden.ui.theme.MoodPeaceful
import com.example.gratitudegarden.ui.theme.NavBarBackground
import com.example.gratitudegarden.ui.theme.TextPrimary

@Composable
fun BottomNavBar(
    navController: NavController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = NavBarBackground,
        tonalElevation = 0.dp
    ) {

        NavIconItem(
            selected = currentRoute == "garden",
            iconRes = R.drawable.ic_garden,
            onClick = {
                navController.navigate("garden") {
                    popUpTo("garden")
                    launchSingleTop = true
                }
            }
        )

        NavIconItem(
            selected = currentRoute == "addEntry",
            iconRes = R.drawable.ic_add,
            onClick = {
                navController.navigate("addEntry")
            }
        )

        NavIconItem(
            selected = currentRoute == "history",
            iconRes = R.drawable.ic_history,
            onClick = {
                navController.navigate("history") {
                    popUpTo("garden")
                    launchSingleTop = true
                }
            }
        )
    }
}

@Composable
private fun RowScope.NavIconItem(
    selected: Boolean,
    iconRes: Int,
    onClick: () -> Unit
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        if (selected)
                            MoodPeaceful.copy(alpha = 0.5f)
                        else
                            Color.Transparent
                    ),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    tint = TextPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }
        },
        alwaysShowLabel = false,
        colors = NavigationBarItemDefaults.colors(
            indicatorColor = Color.Transparent
        )
    )
}