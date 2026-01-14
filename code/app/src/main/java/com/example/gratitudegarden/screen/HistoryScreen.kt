package com.example.gratitudegarden.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gratitudegarden.data.viewmodel.AddEntryViewModel
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import com.example.gratitudegarden.data.model.GratitudeEntry
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId

@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: AddEntryViewModel
) {
    val entries by viewModel.entries.collectAsState()

    var currentMonth by remember {
        mutableStateOf(
            java.time.YearMonth.now()
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Text(
            text = "History",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        MonthSelector(
            currentMonth = currentMonth,
            onPrevious = { currentMonth = currentMonth.minusMonths(1) },
            onNext = { currentMonth = currentMonth.plusMonths(1) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        CalendarGrid(
            month = currentMonth,
            entries = entries,
            onDayClick = { entry ->
                navController.navigate("detail/${entry.id}")
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Previous entries",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (entries.isEmpty()) {
            Text(
                text = "No entries yet",
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
fun MonthSelector(
    currentMonth: java.time.YearMonth,
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "<",
            modifier = Modifier.clickable { onPrevious() }
        )

        Text(
            text = currentMonth.month.name
                .lowercase()
                .replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = ">",
            modifier = Modifier.clickable { onNext() }
        )
    }
}

@Composable
fun CalendarGrid(
    month: YearMonth,
    entries: List<GratitudeEntry>,
    onDayClick: (GratitudeEntry) -> Unit
) {
    val days = remember(month) { daysInMonth(month) }

    val weekdays = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    Column {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            weekdays.forEach { day ->
                Text(
                    text = day,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(days) { date ->
                if (date == null) {
                    Spacer(modifier = Modifier.size(36.dp))
                } else {
                    CalendarDay(
                        date = date,
                        entries = entries,
                        onClick = onDayClick
                    )
                }
            }
        }
    }
}

@Composable
fun CalendarDay(
    date: LocalDate,
    entries: List<GratitudeEntry>,
    onClick: (GratitudeEntry) -> Unit
) {
    val entryForDay = remember(entries, date) {
        entries.firstOrNull {
            Instant.ofEpochMilli(it.timestamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDate() == date
        }
    }

    val isFuture = date.isAfter(LocalDate.now())

    val color = when {
        isFuture -> MaterialTheme.colorScheme.surfaceVariant
        entryForDay == null -> MaterialTheme.colorScheme.outlineVariant
        else -> when (entryForDay.mood) {
            "Happy" -> MaterialTheme.colorScheme.primary
            "Peaceful" -> MaterialTheme.colorScheme.secondary
            "Grateful" -> MaterialTheme.colorScheme.tertiary
            else -> MaterialTheme.colorScheme.primary
        }
    }

    Surface(
        modifier = Modifier
            .size(36.dp)
            .then(
                if (entryForDay != null && !isFuture) {
                    Modifier.clickable { onClick(entryForDay) }
                } else {
                    Modifier
                }
            ),
        shape = CircleShape,
        color = color
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = date.dayOfMonth.toString(),
                style = MaterialTheme.typography.labelSmall
            )
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
private fun daysInMonth(month: YearMonth): List<LocalDate?> {
    val firstDay = month.atDay(1)
    val daysInMonth = month.lengthOfMonth()
    val startOffset = firstDay.dayOfWeek.value - 1
    val days = mutableListOf<LocalDate?>()

    repeat(startOffset) { days.add(null) }
    for (day in 1..daysInMonth) {
        days.add(month.atDay(day))
    }

    return days
}

