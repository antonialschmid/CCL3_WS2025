package com.example.gratitudegarden.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SettingsScreen(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {

        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineMedium
            )

            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Stats cards
        StatCard(
            title = "Total Entries",
            value = "3",
            icon = Icons.Default.ShowChart
        )

        StatCard(
            title = "This Week",
            value = "3",
            icon = Icons.Default.CalendarToday
        )

        StatCard(
            title = "Current Streak",
            value = "0",
            icon = Icons.Default.EmojiEvents
        )

        StatCard(
            title = "Common Mood",
            value = "Peaceful",
            icon = Icons.Default.SentimentSatisfied
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Settings",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        var notificationsEnabled by remember { mutableStateOf(false) }

        SettingRow(
            title = "Notifications",
            subtitle = "Daily reminders",
            trailing = {
                Switch(
                    checked = notificationsEnabled,
                    onCheckedChange = { notificationsEnabled = it }
                )
            }
        )

        SettingRow(
            title = "Export Data",
            subtitle = "Download your entries",
            trailing = {}
        )

        SettingRow(
            title = "About",
            subtitle = "Version 1.0",
            trailing = {}
        )

        SettingRow(
            title = "Privacy Policy",
            subtitle = "How we protect your data",
            trailing = {}
        )
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RectangleShape
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            Icon(
                imageVector = icon,
                contentDescription = null
            )
        }
    }
}

@Composable
fun SettingRow(
    title: String,
    subtitle: String,
    trailing: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RectangleShape
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            trailing()
        }
    }
}