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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gratitudegarden.data.model.GratitudeEntry
import com.example.gratitudegarden.data.model.Mood
import com.example.gratitudegarden.data.viewmodel.AddEntryViewModel
import com.example.gratitudegarden.ui.theme.*
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.Month
import java.util.*

@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: AddEntryViewModel
) {
    val entries by viewModel.entries.collectAsState()
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
            .padding(horizontal = 20.dp)
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "History",
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        MonthSelector(
            currentMonth = currentMonth,
            onMonthChange = { currentMonth = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        CalendarBox(
            month = currentMonth,
            entries = entries,
            onDayClick = { entry ->
                navController.navigate("detail/${entry.id}")
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Previous entries",
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (entries.isEmpty()) {
            Text("No entries yet", color = TextPrimary)
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(entries) { entry ->
                    HistoryItem(entry) {
                        navController.navigate("detail/${entry.id}")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
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
            .background(Color.White, RectangleShape)
            .border(1.dp, TextPrimary, RectangleShape)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            "<",
            modifier = Modifier.clickable {
                onMonthChange(currentMonth.minusMonths(1))
            },
            color = TextPrimary
        )

        Box {
            Text(
                text = "${currentMonth.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${currentMonth.year}",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                modifier = Modifier.clickable { expanded = true }
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
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
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            if (isSelected)
                                                TextPrimary.copy(alpha = 0.08f)
                                            else Color.Transparent
                                        )
                                        .padding(vertical = 6.dp, horizontal = 8.dp)
                                ) {
                                    Text(
                                        text = "${month.name.lowercase().replaceFirstChar { it.uppercase() }} $year",
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = TextPrimary
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }

        Text(
            ">",
            modifier = Modifier.clickable {
                onMonthChange(currentMonth.plusMonths(1))
            },
            color = TextPrimary
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
            .background(Color.White, RectangleShape)
            .border(1.dp, TextPrimary, RectangleShape)
            .padding(12.dp)
    ) {

        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").forEach {
                Text(
                    it,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
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
        if (entry != null) moodColor(Mood.valueOf(entry.mood))
        else Color.Transparent

    Box(
        modifier = Modifier
            .size(36.dp)
            .background(background, CircleShape)
            .then(if (entry != null) Modifier.clickable { onClick(entry) } else Modifier),
        contentAlignment = Alignment.Center
    ) {
        Text(date.dayOfMonth.toString(), color = TextPrimary)
    }
}

@Composable
fun HistoryItem(
    entry: GratitudeEntry,
    onClick: () -> Unit
) {
    val date = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        .format(Date(entry.timestamp))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RectangleShape)
            .border(1.dp, TextPrimary, RectangleShape)
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Text(date, color = TextPrimary)
        Spacer(modifier = Modifier.height(4.dp))
        Text(entry.text, maxLines = 2, color = TextPrimary)
        Spacer(modifier = Modifier.height(4.dp))
        Text(entry.mood, style = MaterialTheme.typography.labelSmall, color = TextPrimary)
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