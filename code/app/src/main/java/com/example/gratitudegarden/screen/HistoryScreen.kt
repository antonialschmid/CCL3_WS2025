package com.example.gratitudegarden.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gratitudegarden.data.model.GratitudeEntry
import com.example.gratitudegarden.data.model.Mood
import com.example.gratitudegarden.data.viewmodel.AddEntryViewModel
import com.example.gratitudegarden.ui.theme.*
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.Month
import com.example.gratitudegarden.R

@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: AddEntryViewModel
) {
    val entries by viewModel.entries.collectAsState()
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        item {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "History",
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary
            )
        }

        item {
            MonthSelector(
                currentMonth = currentMonth,
                onMonthChange = { currentMonth = it }
            )
        }

        item {
            CalendarBox(
                month = currentMonth,
                entries = entries,
                onDayClick = { entry ->
                    navController.navigate("detail/${entry.id}")
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Previous entries",
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary
            )
        }

        val groupedEntries = groupEntriesByDate(entries)

        if (groupedEntries.isEmpty()) {
            item {
                Text(
                    text = "No entries yet",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary
                )
            }
        } else {
            groupedEntries.forEach { (date, dayEntries) ->

                item {
                    Text(
                        text = date.format(
                            java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy")
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 12.dp, bottom = 0.2.dp)
                    )
                }

                items(dayEntries) { entry ->
                    HistoryItem(entry) {
                        navController.navigate("detail/${entry.id}")
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun MonthSelector(
    currentMonth: YearMonth,
    onMonthChange: (YearMonth) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val months = Month.values()
    val years = (2025..2026).toList()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardBackground)
            .border(1.dp, TextPrimary)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = Icons.Default.ChevronLeft,
            contentDescription = "Previous month",
            modifier = Modifier
                .size(32.dp)
                .clickable {
                    onMonthChange(currentMonth.minusMonths(1))
                },
            tint = TextPrimary
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .clickable { expanded = true },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = currentMonth.month.name
                    .lowercase()
                    .replaceFirstChar { it.uppercase() } +
                        " " + currentMonth.year,
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .background(CardBackground)
                    .border(1.dp, TextPrimary)
            ) {
                years.forEach { year ->
                    months.forEach { month ->
                        val monthValue = YearMonth.of(year, month)
                        val isSelected = monthValue == currentMonth

                        DropdownMenuItem(
                            onClick = {
                                onMonthChange(monthValue)
                                expanded = false
                            },
                            text = {
                                Text(
                                    text = "${month.name.lowercase().replaceFirstChar { it.uppercase() }} $year",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextPrimary,
                                    fontWeight = if (isSelected)
                                        FontWeight.Medium
                                    else
                                        FontWeight.Normal
                                )
                            }
                        )
                    }
                }
            }
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Next month",
            modifier = Modifier
                .size(32.dp)
                .clickable {
                    onMonthChange(currentMonth.plusMonths(1))
                },
            tint = TextPrimary
        )
    }
}

@Composable
fun CalendarBox(
    month: YearMonth,
    entries: List<GratitudeEntry>,
    onDayClick: (GratitudeEntry) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardBackground, RectangleShape)
            .border(1.dp, TextPrimary, RectangleShape)
            .padding(12.dp)
    ) {

        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").forEach {
                Text(
                    text = it,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            modifier = Modifier.heightIn(max = 300.dp),
            columns = GridCells.Fixed(7),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(daysInMonth(month)) { date ->
                if (date == null) {
                    Spacer(modifier = Modifier.size(36.dp))
                } else {
                    CalendarDay(date, entries, onDayClick)
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
    val entry = entries.firstOrNull {
        Instant.ofEpochMilli(it.timestamp)
            .atZone(ZoneId.systemDefault())
            .toLocalDate() == date
    }

    val background =
        entry?.let { moodColor(Mood.valueOf(it.mood)) } ?: Color.Transparent

    Box(
        modifier = Modifier
            .size(36.dp)
            .background(background, CircleShape)
            .then(
                if (entry != null) Modifier.clickable { onClick(entry) }
                else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            style = MaterialTheme.typography.bodySmall,
            color = TextPrimary
        )
    }
}

@Composable
fun HistoryItem(
    entry: GratitudeEntry,
    onClick: () -> Unit
) {
    val mood = Mood.valueOf(entry.mood)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardBackground, RectangleShape)
            .border(1.dp, TextPrimary, RectangleShape)
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = entry.text,
            modifier = Modifier.weight(1f),
            maxLines = 2,
            style = MaterialTheme.typography.bodyMedium,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.width(12.dp))

        Box(
            modifier = Modifier
                .size(36.dp)
                .background(
                    color = moodColor(mood),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = moodIcon(mood)),
                contentDescription = mood.name,
                tint = Color.Black.copy(alpha = 0.75f),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

private fun daysInMonth(month: YearMonth): List<LocalDate?> {
    val firstDay = month.atDay(1)
    val offset = firstDay.dayOfWeek.value - 1
    val days = mutableListOf<LocalDate?>()

    repeat(offset) { days.add(null) }
    repeat(month.lengthOfMonth()) { day ->
        days.add(month.atDay(day + 1))
    }
    return days
}

private fun groupEntriesByDate(
    entries: List<GratitudeEntry>
): List<Pair<LocalDate, List<GratitudeEntry>>> {

    return entries
        .sortedByDescending { it.timestamp }
        .groupBy {
            Instant.ofEpochMilli(it.timestamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        }
        .toSortedMap(compareByDescending { it })
        .map { it.key to it.value }
}

private fun moodIcon(mood: Mood): Int =
    when (mood) {
        Mood.HAPPY -> R.drawable.ic_mood_happy
        Mood.PEACEFUL -> R.drawable.ic_mood_peaceful
        Mood.GRATEFUL -> R.drawable.ic_mood_grateful
        Mood.HOPEFUL -> R.drawable.ic_mood_hopeful
        Mood.CALM -> R.drawable.ic_mood_content
    }