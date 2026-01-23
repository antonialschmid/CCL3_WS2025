package com.example.gratitudegarden.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ChevronRight
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
import com.example.gratitudegarden.data.viewmodel.AddEntryViewModel
import com.example.gratitudegarden.notifications.NotificationPrefs
import com.example.gratitudegarden.notifications.NotificationScheduler
import com.example.gratitudegarden.ui.theme.*
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: AddEntryViewModel
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val entries by viewModel.entries.collectAsState()

    val totalEntries = entries.size
    val today = LocalDate.now()
    val startOfWeek = today.minusDays(6)

    val entriesThisWeek = entries.count {
        Instant.ofEpochMilli(it.timestamp)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
            .isAfter(startOfWeek.minusDays(1))
    }

    val entryDates = entries
        .map {
            Instant.ofEpochMilli(it.timestamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        }
        .distinct()
        .sortedDescending()

    val currentStreak =
        if (entryDates.isEmpty()) 0
        else entryDates
            .zipWithNext()
            .takeWhile { (a, b) -> a.minusDays(1) == b }
            .count() + 1

    val mostCommonMood =
        entries.groupingBy { it.mood }
            .eachCount()
            .maxByOrNull { it.value }
            ?.key
            ?.lowercase()
            ?.replaceFirstChar { it.uppercase() }
            ?: "—"

    var notificationsEnabled by remember {
        mutableStateOf(NotificationPrefs.isEnabled(context))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {

        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Your stats",
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        StatCard("Total Entries", totalEntries.toString(), Icons.Default.ShowChart)
        StatCard("This Week", entriesThisWeek.toString(), Icons.Default.CalendarToday)
        StatCard("Current Streak", currentStreak.toString(), Icons.Default.EmojiEvents)
        StatCard("Common Mood", mostCommonMood, Icons.Default.SentimentSatisfied)

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Preferences",
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        SettingSwitchRow(
            title = "Notifications",
            subtitle = "Daily gratitude reminders",
            checked = notificationsEnabled,
            onCheckedChange = {
                notificationsEnabled = it
                NotificationPrefs.setEnabled(context, it)

                if (it) {
                    NotificationScheduler.scheduleDaily(context)
                } else {
                    NotificationScheduler.cancel(context)
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "About",
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        var showAboutDialog by remember { mutableStateOf(false) }
        var showPrivacyDialog by remember { mutableStateOf(false) }

        SettingNavRow(
            title = "About Gratitude Garden",
            subtitle = "App information & concept",
            onClick = { showAboutDialog = true }
        )

        SettingNavRow(
            title = "Privacy",
            subtitle = "How your data is handled",
            onClick = { showPrivacyDialog = true }
        )

        if (showAboutDialog) {
            AboutDialog(onDismiss = { showAboutDialog = false })
        }

        if (showPrivacyDialog) {
            PrivacyDialog(onDismiss = { showPrivacyDialog = false })
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    icon: ImageVector
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, TextPrimary, RectangleShape)
            .background(CardBackground, RectangleShape)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(title, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, style = MaterialTheme.typography.headlineSmall, color = TextPrimary)
        }
        Icon(icon, contentDescription = null, tint = TextPrimary)
    }

    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun SettingSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, TextPrimary, RectangleShape)
            .background(CardBackground, RectangleShape)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(title, style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
            Text(subtitle, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = TextPrimary,
                checkedTrackColor = MoodPeaceful,
                uncheckedThumbColor = TextPrimary,
                uncheckedTrackColor = CardBackground
            )
        )
    }

    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun SettingNavRow(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, TextPrimary, RectangleShape)
            .background(CardBackground, RectangleShape)
            .clickable { onClick() }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = TextSecondary
        )
    }

    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun SettingInfoRow(
    title: String,
    subtitle: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, TextPrimary, RectangleShape)
            .background(CardBackground, RectangleShape)
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
        }
    }

    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun AboutDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK", color = TextPrimary)
            }
        },
        title = {
            Text(
                text = "About Gratitude Garden",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary
            )
        },
        text = {
            Text(
                text =
                    "Gratitude Garden is a wellbeing app designed to encourage daily reflection and gratitude.\n\n" +
                            "Each entry contributes to the growth of your virtual plant, reinforcing positive habits through calm visual feedback.\n\n" +
                            "Developed as part of Creative Code Lab 3.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        },
        containerColor = CardBackground
    )
}

@Composable
private fun PrivacyDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK", color = TextPrimary)
            }
        },
        title = {
            Text(
                text = "Privacy",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary
            )
        },
        text = {
            Text(
                text =
                    "All data entered in Gratitude Garden is stored locally on your device.\n\n" +
                            "No personal information is collected, transmitted, or shared with third parties.\n\n" +
                            "Notifications, if enabled, are handled entirely on-device.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        },
        containerColor = CardBackground
    )
}