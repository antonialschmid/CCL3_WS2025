package com.example.gratitudegarden.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.example.gratitudegarden.ui.theme.CardBackground
import com.example.gratitudegarden.ui.theme.MoodPeaceful
import com.example.gratitudegarden.ui.theme.TextPrimary
import com.example.gratitudegarden.ui.theme.TextSecondary

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
                Box(
                    modifier = Modifier
                        .background(CardBackground, RectangleShape)
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                ) {
                    Switch(
                        checked = notificationsEnabled,
                        onCheckedChange = { notificationsEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = TextPrimary,
                            checkedTrackColor = MoodPeaceful,
                            uncheckedThumbColor = TextPrimary,
                            uncheckedTrackColor = CardBackground,
                            uncheckedBorderColor = TextPrimary,
                            checkedBorderColor = TextPrimary
                        )
                    )
                }
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, TextPrimary, RectangleShape)
            .background(CardBackground, RectangleShape)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                color = TextPrimary
            )
        }

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = TextPrimary
        )
    }

    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun SettingRow(
    title: String,
    subtitle: String,
    trailing: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, TextPrimary, RectangleShape)
            .background(CardBackground, RectangleShape)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
        }

        trailing()
    }

    Spacer(modifier = Modifier.height(8.dp))
}