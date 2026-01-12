package com.example.gratitudegarden.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gratitudegarden.data.viewmodel.AddEntryViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: AddEntryViewModel
) {
    val entries by viewModel.entries.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Text(
            text = "History",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (entries.isEmpty()) {
            Text(
                text = "No entries yet 🌱",
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(entries) { entry ->
                    HistoryItem(
                        text = entry.text,
                        mood = entry.mood,
                        timestamp = entry.timestamp,
                        onClick = {
                            navController.navigate("detail/${entry.id}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun HistoryItem(
    text: String,
    mood: String,
    timestamp: Long,
    onClick: () -> Unit
) {
    val date = remember(timestamp) {
        SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            .format(Date(timestamp))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = date,
                style = MaterialTheme.typography.labelMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = mood,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}
