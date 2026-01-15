package com.example.gratitudegarden.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import com.example.gratitudegarden.R
import com.example.gratitudegarden.data.model.Mood
import com.example.gratitudegarden.data.viewmodel.AddEntryViewModel
import com.example.gratitudegarden.ui.theme.*
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailEntryScreen(
    navController: NavController,
    viewModel: AddEntryViewModel,
    entryId: Long
) {
    val entry = viewModel.getEntryById(entryId) ?: return

    var isEditing by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf(entry.text) }
    var selectedMood by remember { mutableStateOf(Mood.valueOf(entry.mood)) }
    var selectedDate by remember {
        mutableStateOf(
            Instant.ofEpochMilli(entry.timestamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        )
    }
    var showDatePicker by remember { mutableStateOf(false) }

    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")

    Scaffold(
        containerColor = AppBackground,
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0),
                title = {
                    Text(
                        if (isEditing) "Edit Entry" else "Entry Details",
                        color = TextPrimary,
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppBackground
                )
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, TextPrimary, RectangleShape)
                    .background(CardBackground, RectangleShape)
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.ChevronLeft,
                    contentDescription = "Previous day",
                    modifier = Modifier
                        .size(32.dp)
                        .clickable(enabled = isEditing) {
                            selectedDate = selectedDate.minusDays(1)
                        },
                    tint = TextPrimary
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(enabled = isEditing) {
                            showDatePicker = true
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = selectedDate.format(formatter),
                        color = TextPrimary
                    )
                }

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Next day",
                    modifier = Modifier
                        .size(32.dp)
                        .clickable(enabled = isEditing) {
                            selectedDate = selectedDate.plusDays(1)
                        },
                    tint = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                enabled = isEditing,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(CardBackground, RectangleShape)
                    .border(1.dp, TextPrimary, RectangleShape),
                shape = RectangleShape,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TextPrimary,
                    unfocusedBorderColor = TextPrimary,
                    focusedContainerColor = CardBackground,
                    unfocusedContainerColor = CardBackground,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text("How do you feel today?", color = TextPrimary)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Mood.values().forEach { mood ->
                    val bg =
                        if (selectedMood == mood)
                            moodColor(mood)
                        else
                            moodColor(mood).copy(alpha = 0.2f)

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .background(bg, RectangleShape)
                            .border(1.dp, TextPrimary, RectangleShape)
                            .clickable(enabled = isEditing) {
                                selectedMood = mood
                            }
                            .padding(vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(id = moodIcon(mood)),
                            contentDescription = mood.name,
                            tint = TextPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = mood.name.lowercase()
                                .replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.labelSmall,
                            color = TextPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            if (!isEditing) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, TextPrimary, RectangleShape)
                        .background(MoodCalm, RectangleShape)
                        .clickable { isEditing = true }
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Edit entry", color = TextPrimary)
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, TextPrimary, RectangleShape)
                        .background(MoodPeaceful, RectangleShape)
                        .clickable {
                            viewModel.updateEntry(
                                entry.copy(
                                    text = text,
                                    mood = selectedMood.name,
                                    timestamp = selectedDate
                                        .atStartOfDay(ZoneId.systemDefault())
                                        .toInstant()
                                        .toEpochMilli()
                                )
                            )
                            navController.popBackStack()
                        }
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Save changes", color = TextPrimary)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, TextPrimary, RectangleShape)
                    .background(MoodHappy, RectangleShape)
                    .clickable {
                        viewModel.deleteEntry(entry)
                        navController.popBackStack()
                    }
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Delete", color = TextPrimary)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    /* ---------- EDIT MODE DATE PICKER (STYLED) ---------- */

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            colors = DatePickerDefaults.colors(
                containerColor = CardBackground
            ),
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        selectedDate = Instant.ofEpochMilli(it)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                    }
                    showDatePicker = false
                }) {
                    Text("OK", color = TextPrimary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel", color = TextPrimary)
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = CardBackground,
                    titleContentColor = TextPrimary,
                    headlineContentColor = TextPrimary,
                    weekdayContentColor = TextSecondary,
                    dayContentColor = TextPrimary,
                    selectedDayContainerColor = MoodPeaceful,
                    selectedDayContentColor = TextPrimary,
                    todayDateBorderColor = TextPrimary,
                    dividerColor = TextPrimary.copy(alpha = 0.15f)
                )
            )
        }
    }
}

private fun moodIcon(mood: Mood): Int =
    when (mood) {
        Mood.HAPPY -> R.drawable.ic_mood_happy
        Mood.PEACEFUL -> R.drawable.ic_mood_peaceful
        Mood.GRATEFUL -> R.drawable.ic_mood_grateful
        Mood.HOPEFUL -> R.drawable.ic_mood_hopeful
        Mood.CALM -> R.drawable.ic_mood_content
    }