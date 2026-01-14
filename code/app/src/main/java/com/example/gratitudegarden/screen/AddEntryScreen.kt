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
fun AddEntryScreen(
    navController: NavController,
    viewModel: AddEntryViewModel
) {
    var text by remember { mutableStateOf("") }
    var selectedMood by remember { mutableStateOf<Mood?>(null) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showDatePicker by remember { mutableStateOf(false) }

    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")

    Scaffold(
        containerColor = AppBackground,
        topBar = {
            TopAppBar(
                title = { Text("Add Entry", color = TextPrimary) },
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
                        .clickable {
                            selectedDate = selectedDate.minusDays(1)
                        },
                    tint = TextPrimary
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { showDatePicker = true },
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
                        .clickable {
                            selectedDate = selectedDate.plusDays(1)
                        },
                    tint = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text("I am thankful for...", color = TextPrimary) },
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
                        if (selectedMood == null || selectedMood == mood)
                            moodColor(mood)
                        else
                            moodColor(mood).copy(alpha = 0.2f)

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .background(bg, RectangleShape)
                            .border(1.dp, TextPrimary, RectangleShape)
                            .clickable {
                                selectedMood =
                                    if (selectedMood == mood) null else mood
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

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, TextPrimary, RectangleShape)
                    .background(MoodPeaceful, RectangleShape)
                    .clickable(
                        enabled = text.isNotBlank() && selectedMood != null
                    ) {
                        viewModel.saveEntry(
                            text = text,
                            mood = selectedMood!!.name,
                            timestamp = selectedDate
                                .atStartOfDay(ZoneId.systemDefault())
                                .toInstant()
                                .toEpochMilli()
                        )
                        navController.popBackStack()
                    }
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Save entry", color = TextPrimary)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        selectedDate = Instant.ofEpochMilli(it)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            }
        ) {
            DatePicker(state = datePickerState)
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